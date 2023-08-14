package com.databarang.qrcode.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.databarang.qrcode.R;
import com.databarang.qrcode.UbahActivity;
import com.databarang.qrcode.model.BarangModel;

import java.util.List;

public class AdapterBarang extends RecyclerView.Adapter<AdapterBarang.HolderData> {
    private Context ctx;
    private List<BarangModel> listBarang;

    public AdapterBarang (Context ctx, List<BarangModel> listBarang){
        this.ctx = ctx;
        this.listBarang = listBarang;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        BarangModel barang = listBarang.get(position);

        holder.barcode.setText(barang.getId());
        holder.nama.setText(barang.getNama());
        holder.price.setText(barang.getPrice());
        holder.kategori.setText(barang.getKategori());
        holder.quantity.setText(barang.getQuantity());


        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnUpdate:
                        Intent update = new Intent(ctx, UbahActivity.class);
                        update.putExtra("id", String.valueOf(barang.getId()));
                        update.putExtra("nama", String.valueOf(barang.getNama()));
                        update.putExtra("kategori", String.valueOf(barang.getKategori()));
                        update.putExtra("jumlah", String.valueOf(barang.getQuantity()));
                        update.putExtra("harga", String.valueOf(barang.getPrice()));
                        ctx.startActivity(update);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView barcode, nama, tanggal, kategori, quantity, price;
        Button btnUpdate;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            barcode = itemView.findViewById(R.id.viewitembarcode);
            nama = itemView.findViewById(R.id.viewitemname);
            price = itemView.findViewById(R.id.viewitemprice);
            kategori = itemView.findViewById(R.id.viewitemcategory);
            quantity = itemView.findViewById(R.id.viewitemquantity);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
