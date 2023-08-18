package com.databarang.qrcode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.databarang.qrcode.model.submit.CekStokBarang;
import com.databarang.qrcode.model.submit.Submit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etNama, etKategori, etJumlah, etHarga;
    String id, nama, kategori, jumlah, harga;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    private Button btnScan, btnSubmit;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan = findViewById(R.id.btn_scan);
        cek_stok_barang();

        etNama = findViewById(R.id.produkname);
        etKategori = findViewById(R.id.kategori);
        etJumlah = findViewById(R.id.quantity);
        etHarga = findViewById(R.id.price);

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        resultText = findViewById(R.id.result);

        btnScan.setOnClickListener(s -> {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)){
                    startScan();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
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
            case R.id.btn_submit:
                id = resultText.getText().toString();
                nama = etNama.getText().toString();
                kategori = etKategori.getText().toString();
                jumlah = etJumlah.getText().toString();
                harga = etHarga.getText().toString();
                masukkan(id, nama, kategori, jumlah, harga);
                break;
        }
    }

    public void cek_stok_barang(){
        ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
        Call<CekStokBarang> cekStok = ardData.lihatStok();
        cekStok.enqueue(new Callback<CekStokBarang>(){

            @Override
            public void onResponse(Call<CekStokBarang> call, Response<CekStokBarang> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isStatus()){
                    if (response.body().isStatus() == true)
                    {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                        alertDialogBuilder.setTitle("STOK BARANG MENIPIS");

                        alertDialogBuilder
                                .setMessage(response.body().getMessage())
                                .setIcon(R.mipmap.ic_launcher)
                                .setCancelable(false)
                                .setPositiveButton("Lanjut",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("Mengerti", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();

                        alertDialog.show();
                    }

                }

            }

            @Override
            public void onFailure(Call<CekStokBarang> call, Throwable t) {

            }
        });
    }

    private void masukkan(String id, String nama, String kategori, String jumlah, String harga) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Submit> call = apiInterface.submitResponse(id, nama, kategori, jumlah, harga);
        call.enqueue(new Callback<Submit>() {
            @Override
            public void onResponse(Call<Submit> call, Response<Submit> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){
                    Toast.makeText(MainActivity.this, response.body().getSubmitData().getNama(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Submit> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}