package com.databarang.qrcode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.databarang.qrcode.model.submit.Hapus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HapusActivity extends AppCompatActivity implements View.OnClickListener{

    String id;
    ApiInterface apiInterface;
    private Button btnScan, btnHapus;
    private TextView resultText, namabarang, kategori, jumlah, harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hapus);

        btnScan = findViewById(R.id.btn_scan);
        btnHapus = findViewById(R.id.btnHapus);
        btnHapus.setOnClickListener(this);
        resultText = findViewById(R.id.Idresult);
        namabarang = findViewById(R.id.hapusprodukname);
        kategori = findViewById(R.id.hapuskategori);
        jumlah = findViewById(R.id.hapusquantity);
        harga = findViewById(R.id.hapusprice);

        btnScan.setOnClickListener(s -> {
            if (ContextCompat.checkSelfPermission(HapusActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(HapusActivity.this, Manifest.permission.CAMERA)){
                    startScan();
                } else {
                    ActivityCompat.requestPermissions(HapusActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
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
            case R.id.btnHapus:
                id = resultText.getText().toString();
                deleteData(id, jumlah.getText().toString());
                break;
        }
    }

    public void ambilData(String id)
    {
        ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
        Call<LihatModel> ambilData = ardData.ambil(id);
        ambilData.enqueue(new Callback<LihatModel>() {
            @Override
            public void onResponse(Call<LihatModel> call, Response<LihatModel> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isStatus()){
                    namabarang.setText(response.body().getNama());
                    kategori.setText(response.body().getKategori());
                    jumlah.setText(response.body().getQuantity());
                    harga.setText(response.body().getPrice());
                }
            }

            @Override
            public void onFailure(Call<LihatModel> call, Throwable t) {
                Log.e("Error", t.getLocalizedMessage());
            }
        });
    }

    public void deleteData(String id, String quantity) {
        ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
        Call<Hapus> hapusData = ardData.deleteData(id, quantity);
        hapusData.enqueue(new Callback<Hapus>() {
            @Override
            public void onResponse(Call<Hapus> call, Response<Hapus> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HapusActivity.this);

                    alertDialogBuilder.setTitle("Menghapus Data Berhasil");

                    alertDialogBuilder
                            .setMessage("Klik Ya untuk Melanjutkan Pengolahan lainnya!")
                            .setIcon(R.mipmap.ic_launcher)
                            .setCancelable(false)
                            .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Intent intent = new Intent(HapusActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HapusActivity.this);

                    alertDialogBuilder.setTitle("Menghapus Data Tidak Berhasil");

                    alertDialogBuilder
                            .setMessage("Klik Ya untuk Mengulang Penghapusan!")
                            .setIcon(R.mipmap.ic_launcher)
                            .setCancelable(false)
                            .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<Hapus> call, Throwable t) {
                Log.e("Error", t.getLocalizedMessage());
            }


        });
    }
}

