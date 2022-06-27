package com.malikazizali.e_library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailBuku extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView judul, penulis, tahun, bahasa, rating, sinopsis, halaman;
    private ImageView gambar;
    Button pinjam;
    String link_gambar, namaPeminjam;
    private ImageView back;
    private String tgl_pengambilan, tgl_kembali;

    //data buku
    private ArrayList<String> array_judul;
    private ArrayList<String> array_gambar;
    private ArrayList<String> array_penulis;
    private ArrayList<String> array_sinopsis;
    private ArrayList<String> array_bahasa;
    private ArrayList<String> array_halaman;
    private ArrayList<String> array_tahun;
    private ArrayList<String> array_rating;
    private AlertDialog alertDialogPinjam;

    //user data
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    //review
    RecyclerView rv_review;
    private ArrayList<String> array_nama_reviewer;
    private ArrayList<String> array_komentar;
    private ArrayList<String> array_rating_review;
    AdapterReview adapterReview;


    public DetailBuku() {
        //empty constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_buku);

        judul = findViewById(R.id.judul);
        penulis = findViewById(R.id.penulis);
        tahun = findViewById(R.id.tahun);
        bahasa = findViewById(R.id.bahasa);
        sinopsis = findViewById(R.id.sinopsis);
        halaman = findViewById(R.id.halaman);
        rating = findViewById(R.id.rating);
        gambar = findViewById(R.id.gambar);
        pinjam = findViewById(R.id.btn_pinjam);
        back = findViewById(R.id.back_btn);

        mAuth = FirebaseAuth.getInstance();

        //ambil gambar
        link_gambar = getIntent().getStringExtra("link_gambar");

        //ambil user
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    namaPeminjam = userProfile.nama;
                }else {
                    namaPeminjam = "";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailBuku.this, "Gagal Mengambil Data User", Toast.LENGTH_LONG).show();
            }
        });

        //panggil data
        getData(link_gambar);

        rv_review = findViewById(R.id.rv_review_buku);
        rv_review.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_review.setLayoutManager(layoutManager);
        rv_review.setAdapter(adapterReview);

        getReview();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailBuku.this, MainActivity.class));
            }
        });

        //pinjam buku
        pinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinjamBuku();
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailBuku.this);
                builder.setMessage("Peminjaman Berhasil, Cek Riwayat untuk melihat Riwayat Peminjaman Anda\n\n" +
                                "- Silakan ambil buku di perpustakaan dalam rentang waktu 2 hari\n\n" +
                                "- Dimohon untuk membawa KTP untuk verifikasi saat pengambilan buku")
                        .setTitle("Peminjaman Buku Berhasil !");
                builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertDialogPinjam = builder.create();
                alertDialogPinjam.show();
            }
        });



    }

    void initializeArray() {
        array_penulis = new ArrayList<String>();
        array_gambar = new ArrayList<String>();
        array_judul = new ArrayList<String>();
        array_halaman = new ArrayList<String>();
        array_sinopsis = new ArrayList<String>();
        array_tahun = new ArrayList<String>();
        array_bahasa = new ArrayList<String>();
        array_rating = new ArrayList<String>();

        array_bahasa.clear();
        array_sinopsis.clear();
        array_halaman.clear();
        array_penulis.clear();
        array_gambar.clear();
        array_judul.clear();
        array_tahun.clear();
        array_rating.clear();
    }

    void initializeArrayReview() {
        array_nama_reviewer = new ArrayList<String>();
        array_komentar = new ArrayList<String>();
        array_rating_review = new ArrayList<String>();

        array_nama_reviewer.clear();
        array_komentar.clear();
        array_rating_review.clear();

    }


    public void getData(String link) {
        initializeArray();
        AndroidNetworking.get("http://10.0.2.2/elibrary/getBookData.php")
                .addQueryParameter("gambar_buku", link)
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean status = response.getBoolean("status");
                            if (status) {
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon", "" + ja);
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jo = ja.getJSONObject(i);

                                    array_gambar.add(jo.getString("gambar_buku"));
                                    array_judul.add(jo.getString("judul_buku"));
                                    array_penulis.add(jo.getString("penulis"));
                                    array_sinopsis.add(jo.getString("sinopsis"));
                                    array_halaman.add(jo.getString("halaman"));
                                    array_bahasa.add(jo.getString("bahasa"));
                                    array_tahun.add(jo.getString("tahun"));
                                    array_rating.add(jo.getString("rating"));
                                }
                                setData();
                            } else {
                                setData();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(DetailBuku.this, "exception error : " + e, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DetailBuku.this, String.valueOf(anError), Toast.LENGTH_SHORT).show();
                        Log.d("Error di Get : ", String.valueOf(anError));
                    }
                });
    }

    public void getReview() {
        initializeArrayReview();
        AndroidNetworking.get("http://10.0.2.2/elibrary/getReview.php")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean status = response.getBoolean("status");
                            if (status) {
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon", "" + ja);
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jo = ja.getJSONObject(i);

                                    array_nama_reviewer.add(jo.getString("nama"));
                                    array_komentar.add(jo.getString("komentar"));
                                    array_rating_review.add(jo.getString("rating"));
                                    adapterReview = new AdapterReview(DetailBuku.this, array_nama_reviewer, array_komentar, array_rating_review);
                                    rv_review.setAdapter(adapterReview);
                                }
                            }else{
                                adapterReview = new AdapterReview(DetailBuku.this, array_nama_reviewer, array_komentar, array_rating_review);
                                rv_review.setAdapter(adapterReview);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(DetailBuku.this, "exception error : " + e, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DetailBuku.this, String.valueOf(anError), Toast.LENGTH_SHORT).show();
                        Log.d("Error di Get : ", String.valueOf(anError));
                    }
                });
    }

    public void setData() {
        judul.setText(array_judul.get(0));
        penulis.setText(array_penulis.get(0));
        sinopsis.setText(array_sinopsis.get(0));
        bahasa.setText("Bahasa\n"+array_bahasa.get(0));
        halaman.setText(array_halaman.get(0)+"\nHalaman");
        tahun.setText("Tahun\n"+array_tahun.get(0));
        rating.setText(array_rating.get(0));
        Glide.with(this).load(array_gambar.get(0)).into(gambar);
    }

    public void pinjamBuku() {
        hitungPengambilan();
        hitungPengembalian();
        String jdl = judul.getText().toString();
        AndroidNetworking.post("http://10.0.2.2/elibrary/borrowBook.php")
                .addBodyParameter("judul_buku", jdl)
                .addBodyParameter("gambar_buku", link_gambar)
                .addBodyParameter("peminjam", namaPeminjam)
                .addBodyParameter("tgl_pengambilan", tgl_pengambilan)
                .addBodyParameter("tgl_pengembalian", tgl_kembali)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int status = response.getInt(0);
                            if (status==0) {
                                Toast.makeText(DetailBuku.this, "Gagal melakukan Pinjaman", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(DetailBuku.this, "Berhasil Melakukan Pinjaman", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                    }
                });
    }

    public void hitungPengambilan() {
        Date currentTime = Calendar.getInstance().getTime();
        getTanggalAmbil(currentTime, 2);
    }

    public void hitungPengembalian() {
        Date currentTime = Calendar.getInstance().getTime();
        getTanggalKembali(currentTime, 16);
    }

    public void getTanggalKembali(Date currentDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, days);

        Date futureDate = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(futureDate);
        tgl_kembali = formattedDate;
    }

    public void getTanggalAmbil(Date currentDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, days);

        Date futureDate = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(futureDate);
        tgl_pengambilan = formattedDate;
    }

}