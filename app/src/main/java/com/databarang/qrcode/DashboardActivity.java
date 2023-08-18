package com.databarang.qrcode;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.databarang.qrcode.api.ApiClient;
import com.databarang.qrcode.api.ApiInterface;
import com.databarang.qrcode.model.submit.CekStokBarang;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {
    TextView Username;
    String nama;
    ImageButton btnTambah, btnLihat, btnHapus, btnUbah, btnLogout, btnAddQuantity;
    SessionManager sessionManager;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sessionManager = new SessionManager(DashboardActivity.this);

        if(!sessionManager.isLoggedIn()){
            moveToLogin();
        }

        cek_stok_barang();

        //etUsername = findViewById(R.id.etMainUsername);
        Username = findViewById(R.id.tvUsername);

        //username = sessionManager.getUserDetail().get(SessionManager.USERNAME);
        nama = sessionManager.getUserDetail().get(SessionManager.NAMA);

        //etUsername.setText(username);
        Username.setText(nama);

        btnTambah = findViewById(R.id.btnTambah);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnUbah = findViewById(R.id.btnUbah);
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, UpdateActivity.class);
                startActivity(intent);
            }
        });

        btnLihat = findViewById(R.id.btnLihat);
        btnLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, LihatBarangActivity.class);
                startActivity(intent);
            }
        });

        btnHapus = findViewById(R.id.btnDelete);
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, HapusActivity.class);
                startActivity(intent);
            }
        });

        btnAddQuantity = findViewById(R.id.btnaddQauntityDash);
        btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, AddQuantityActivity.class);
                startActivity(intent);
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutSession();
                moveToLogin();
            }
        });
    }

    private void moveToLogin() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionLogout:
                sessionManager.logoutSession();
                moveToLogin();
        }
        return super.onOptionsItemSelected(item);
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
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashboardActivity.this);

                        alertDialogBuilder.setTitle("STOK BARANG MENIPIS");

                        alertDialogBuilder
                                .setMessage(response.body().getMessage())
                                .setIcon(R.mipmap.ic_launcher)
                                .setCancelable(false)
                                .setPositiveButton("Lanjut",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Intent intent = new Intent(DashboardActivity.this, UpdateActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("Mengerti", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

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
}