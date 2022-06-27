package com.malikazizali.e_library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPencarian#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPencarian extends Fragment {
    RecyclerView rv_p;
    private ArrayList<String> array_judul;
    private ArrayList<String> array_gambar;
    private ArrayList<String> array_penulis;
    private ArrayList<String> array_tahun;
    private ArrayList<String> array_rating;
    AdapterPencarian adapterPencarian;
    private Context mContext;
    EditText judul;
    ImageButton btn_cari;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentPencarian() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPencarian.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPencarian newInstance(String param1, String param2) {
        FragmentPencarian fragment = new FragmentPencarian();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pencarian, container, false);
        rv_p = view.findViewById(R.id.rv_pencarian);
        rv_p.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_p.setLayoutManager(layoutManager);
        rv_p.setAdapter(adapterPencarian);
        judul = view.findViewById(R.id.et_judul);

        btn_cari = view.findViewById(R.id.btn_cari);
        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jdl = judul.getText().toString();
                getData(jdl);
            }
        });


        return view;
    }

    void initializeArray() {
        array_penulis = new ArrayList<String>();
        array_gambar = new ArrayList<String>();
        array_judul = new ArrayList<String>();
        array_tahun = new ArrayList<String>();
        array_rating = new ArrayList<String>();

        array_penulis.clear();
        array_gambar.clear();
        array_judul.clear();
        array_tahun.clear();
        array_rating.clear();
    }

    public void getData(String judul) {
        initializeArray();
        AndroidNetworking.get("http://10.0.2.2/elibrary/searchBook.php")
                .addQueryParameter("judul_buku", judul)
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
                                    array_tahun.add(jo.getString("tahun"));
                                    array_rating.add(jo.getString("rating"));
                                }
                                adapterPencarian = new AdapterPencarian(mContext, array_gambar, array_judul, array_penulis, array_tahun, array_rating);
                                rv_p.setAdapter(adapterPencarian);
                            } else {
                                Toast.makeText(mContext, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                                adapterPencarian = new AdapterPencarian(mContext, array_gambar, array_judul, array_penulis, array_tahun, array_rating);
                                rv_p.setAdapter(adapterPencarian);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "exception error : "+e, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(mContext, String.valueOf(anError), Toast.LENGTH_SHORT).show();
                        Log.d("Error di Get : ", String.valueOf(anError));
                    }
                });
    }
}