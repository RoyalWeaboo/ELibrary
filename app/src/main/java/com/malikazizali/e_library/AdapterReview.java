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

public class AdapterReview extends RecyclerView.Adapter<AdapterReview.MyViewHolder>  {
    private Context mContext;
    private ArrayList<String> array_nama_reviewer, array_komentar, array_rating;

    public AdapterReview(Context mContext, ArrayList<String> array_nama_reviewer, ArrayList<String> array_komentar, ArrayList<String> array_rating) {
        super();
        this.mContext = mContext;
        this.array_nama_reviewer = array_nama_reviewer;
        this.array_komentar = array_komentar;
        this.array_rating = array_rating;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.template_rv_review, parent, false);
        return new AdapterReview.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.nama_r.setText(array_nama_reviewer.get(position));
        holder.komen.setText(array_komentar.get(position));
        holder.rating.setText(array_rating.get(position));
    }

    @Override
    public int getItemCount() {
        return array_nama_reviewer.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_r,komen,rating;
        public CardView cv_review;

        public MyViewHolder(View itemView) {
            super(itemView);
            cv_review = itemView.findViewById(R.id.cv_review);
            nama_r = itemView.findViewById(R.id.nama_reviewer);
            komen = itemView.findViewById(R.id.komentar);
            rating = itemView.findViewById(R.id.rating_review);
        }
    }
}
