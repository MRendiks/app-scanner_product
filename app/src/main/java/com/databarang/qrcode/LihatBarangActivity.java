package com.databarang.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.databarang.qrcode.adapter.AdapterBarang;
import com.databarang.qrcode.api.ApiClient;
import com.databarang.qrcode.api.ApiInterface;
import com.databarang.qrcode.model.BarangModel;
import com.databarang.qrcode.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LihatBarangActivity extends AppCompatActivity {
    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;

    private List<BarangModel> listBarang = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_barang);

        rvData = findViewById(R.id.rv_data);
        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);

        retrieveData();
    }

    public void retrieveData(){
        ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> tampilData = ardData.lihatResponse();
        tampilData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                listBarang = response.body().getData();

                adData = new AdapterBarang(LihatBarangActivity.this, listBarang);
                rvData.setAdapter(adData);
                adData.notifyDataSetChanged();

                Toast.makeText(LihatBarangActivity.this, "body"+adData, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LihatBarangActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}