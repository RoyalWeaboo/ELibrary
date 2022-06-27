package com.malikazizali.e_library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class AdapterRiwayat extends RecyclerView.Adapter<AdapterRiwayat.MyViewHolder>  {
    private Context mContext;
    private ArrayList<String> array_id, array_gambar, array_judul, array_tanggal_pinjam, array_tanggal_kembali, array_status;

    public AdapterRiwayat(Context mContext, ArrayList<String> array_id, ArrayList<String> array_gambar, ArrayList<String> array_judul, ArrayList<String> array_tanggal_pinjam, ArrayList<String> array_tanggal_kembali, ArrayList<String> array_status) {
        super();
        this.mContext = mContext;
        this.array_id = array_id;
        this.array_gambar = array_gambar;
        this.array_judul = array_judul;
        this.array_tanggal_pinjam = array_tanggal_pinjam;
        this.array_tanggal_kembali = array_tanggal_kembali;
        this.array_status = array_status;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.template_rv_riwayat, parent, false);
        return new AdapterRiwayat.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.id.setText(array_id.get(position));
        holder.judul.setText(array_judul.get(position));
        holder.tgl_pinjam.setText(array_tanggal_pinjam.get(position));
        holder.tgl_kembali.setText(array_tanggal_kembali.get(position));
        holder.status.setText(array_status.get(position));
        Glide.with(holder.itemView.getContext()).load(array_gambar.get(position)).into(holder.gambar_buku);
    }

    @Override
    public int getItemCount() {
        return array_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id,judul,tgl_pinjam,tgl_kembali,status;
        public ImageView gambar_buku;
        public CardView cv_main;

        public MyViewHolder(View itemView) {
            super(itemView);
            cv_main = itemView.findViewById(R.id.cv_buku_dipinjam);
            id = itemView.findViewById(R.id.id);
            judul = itemView.findViewById(R.id.judul);
            gambar_buku = itemView.findViewById(R.id.gambar);
            tgl_pinjam = itemView.findViewById(R.id.tgl_pinjam);
            tgl_kembali = itemView.findViewById(R.id.tgl_kembali);
            status = itemView.findViewById(R.id.status);
        }
    }
}
