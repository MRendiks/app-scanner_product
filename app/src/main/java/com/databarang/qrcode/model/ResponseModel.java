package com.databarang.qrcode.model;

import java.util.List;

public class ResponseModel {
    private int kode;
    private String pesan;
    private List<BarangModel> data;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<BarangModel> getData() {
        return data;
    }

    public void setData(List<BarangModel> data) {
        this.data = data;
    }
}
