package com.malikazizali.e_library;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class AdapterPencarian extends RecyclerView.Adapter<AdapterPencarian.MyViewHolder>  {
    private Context mContext;
    private ArrayList<String> array_gambar, array_judul, array_penulis, array_tahun, array_rating;

    public AdapterPencarian(Context mContext, ArrayList<String> array_gambar, ArrayList<String> array_judul, ArrayList<String> array_penulis, ArrayList<String> array_tahun, ArrayList<String> array_rating) {
        super();
        this.mContext = mContext;
        this.array_gambar = array_gambar;
        this.array_judul = array_judul;
        this.array_penulis = array_penulis;
        this.array_tahun = array_tahun;
        this.array_rating = array_rating;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.template_rv_pencarian, parent, false);
        return new AdapterPencarian.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, penulis, tahun, rating;
        public ImageView gambar_buku;
        public CardView cv_pencarian;

        public MyViewHolder(View itemView) {
            super(itemView);
            cv_pencarian = itemView.findViewById(R.id.cv_buku_pencarian);
            judul = itemView.findViewById(R.id.judul);
            gambar_buku = itemView.findViewById(R.id.gambar);
            penulis = itemView.findViewById(R.id.penulis);
            tahun = itemView.findViewById(R.id.tahun);
            rating = itemView.findViewById(R.id.rating);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        TextView jdl = holder.judul;
        holder.judul.setText(array_judul.get(position));
        holder.penulis.setText(array_penulis.get(position));
        holder.tahun.setText(array_tahun.get(position));
        holder.rating.setText(array_rating.get(position));
        ImageView gambar_buku = holder.gambar_buku;
        Glide.with(holder.itemView.getContext()).load(array_gambar.get(position)).into(holder.gambar_buku);
        jdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gambar_buku.getContext(), DetailBuku.class);
                intent.putExtra("link_gambar", array_gambar.get(position));
                gambar_buku.getContext().startActivity(intent);
            }
        });
        gambar_buku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gambar_buku.getContext(), DetailBuku.class);
                intent.putExtra("link_gambar", array_gambar.get(position));
                gambar_buku.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return array_judul.size();
    }


}
