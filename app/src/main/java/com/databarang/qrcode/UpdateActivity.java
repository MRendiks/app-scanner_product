package com.databarang.qrcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.databarang.qrcode.api.ApiClient;
import com.databarang.qrcode.api.ApiInterface;
import com.databarang.qrcode.model.LihatModel;
import com.databarang.qrcode.model.submit.Update;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etNama, etKategori, etJumlah, etHarga;
    String id, nama, kategori, jumlah, harga;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    private Button btnScan, btnUpdate;
    private TextView resultText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        btnScan = findViewById(R.id.btn_scan);

        etNama = findViewById(R.id.produkname);
        etKategori = findViewById(R.id.kategori);
        etJumlah = findViewById(R.id.quantity);
        etHarga = findViewById(R.id.price);

        btnUpdate = findViewById(R.id.btn_Update);
        btnUpdate.setOnClickListener(this);

        resultText = findViewById(R.id.result);



        btnScan.setOnClickListener(s -> {
            if (ContextCompat.checkSelfPermission(UpdateActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateActivity.this, Manifest.permission.CAMERA)){
                    startScan();
                } else {
                    ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                }
            } else {
                startScan();
            }
        });
    }

    private void startScan(){
        Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
        startActivityForResult(intent, 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20){
            if (resultCode == RESULT_OK && data!=null){
                String code = data.getStringExtra("result");
                resultText.setText(code);
                ambilData(code);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScan();
            } else {
                Toast.makeText(this, "Gagal membuka kamera!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Update:
                id = resultText.getText().toString();
                nama = etNama.getText().toString();
                kategori = etKategori.getText().toString();
                jumlah = etJumlah.getText().toString();
                harga = etHarga.getText().toString();
                update(id, nama, kategori, jumlah, harga);
                break;
        }
    }

    private void update(String id, String nama, String kategori, String jumlah, String harga) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Update> call = apiInterface.update(id, nama, kategori, jumlah, harga);
        call.enqueue(new Callback<Update>() {
            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){
                    Intent intent = new Intent(UpdateActivity.this, LihatBarangActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UpdateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ambilData(String id)
    {
        ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
        Call<LihatModel> ambilData = ardData.ambil(id);
        ambilData.enqueue(new Callback<LihatModel>() {
            @Override
            public void onResponse(Call<LihatModel> call, Response<LihatModel> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isStatus()){
                    etNama.setText(response.body().getNama());
                    etKategori.setText(response.body().getKategori());
                    etJumlah.setText(response.body().getQuantity());
                    etHarga.setText(response.body().getPrice());
                }else{
                    Toast.makeText(UpdateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LihatModel> call, Throwable t) {
                Log.e("Error", t.getLocalizedMessage());
            }
        });
    }
}