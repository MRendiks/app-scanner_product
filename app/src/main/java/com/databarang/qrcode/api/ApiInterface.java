package com.databarang.qrcode.api;

import com.databarang.qrcode.model.LihatModel;
import com.databarang.qrcode.model.ResponseModel;
import com.databarang.qrcode.model.login.Login;
import com.databarang.qrcode.model.submit.CekStokBarang;
import com.databarang.qrcode.model.submit.Hapus;
import com.databarang.qrcode.model.submit.Submit;
import com.databarang.qrcode.model.submit.Update;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<Login> loginResponse(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("kirim.php")
    Call<Submit> submitResponse(
            @Field("id") String id,
            @Field("nama") String nama,
            @Field("kategori") String kategori,
            @Field("quantity") String quantity,
            @Field("price") String price
    );

    @FormUrlEncoded
    @POST("ambil_data.php")
    Call<LihatModel> ambil(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<Update> update(
            @Field("id") String id,
            @Field("nama") String nama,
            @Field("kategori") String kategori,
            @Field("quantity") String quantity,
            @Field("price") String price
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<Hapus> deleteData(
            @Field("id") String id,
            @Field("quantity") String quantity
            );

    @GET("lihat.php")
    Call<ResponseModel> lihatResponse();

    @GET("cek_stok_barang.php")
    Call<CekStokBarang> lihatStok();
}
