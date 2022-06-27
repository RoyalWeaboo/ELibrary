package com.malikazizali.e_library;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRiwayat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRiwayat extends Fragment {
    RecyclerView rv_r;
    private ArrayList<String> array_id;
    private ArrayList<String> array_gambar;
    private ArrayList<String> array_judul;
    private ArrayList<String> array_tanggal_pinjam;
    private ArrayList<String> array_tanggal_kembali;
    private ArrayList<String> array_status;
    AdapterRiwayat adapterRiwayat;
    private Context mContext;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentRiwayat() {
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
     * @return A new instance of fragment FragmentRiwayat.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRiwayat newInstance(String param1, String param2) {
        FragmentRiwayat fragment = new FragmentRiwayat();
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
        View view = inflater.inflate(R.layout.fragment_riwayat, container, false);
        // Inflate the layout for this fragment
        rv_r = view.findViewById(R.id.rv_riwayat);
        rv_r.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_r.setLayoutManager(layoutManager);
        rv_r.setAdapter(adapterRiwayat);

        getData();

        return view;
    }

    void initializeArray() {
        array_id = new ArrayList<String>();
        array_gambar = new ArrayList<String>();
        array_judul = new ArrayList<String>();
        array_tanggal_pinjam = new ArrayList<String>();
        array_tanggal_kembali = new ArrayList<String>();
        array_status = new ArrayList<String>();

        array_id.clear();
        array_gambar.clear();
        array_judul.clear();
        array_tanggal_pinjam.clear();
        array_tanggal_kembali.clear();
        array_status.clear();
    }

    public void getData() {
        initializeArray();
        AndroidNetworking.get("http://10.0.2.2/elibrary/getData.php")
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

                                    array_id.add(jo.getString("id_peminjaman"));
                                    array_gambar.add(jo.getString("gambar_buku"));
                                    array_judul.add(jo.getString("judul_buku"));
                                    array_tanggal_pinjam.add(jo.getString("tgl_pengambilan"));
                                    array_tanggal_kembali.add(jo.getString("tgl_pengembalian"));
                                    array_status.add(jo.getString("status"));
                                    }
                                adapterRiwayat = new AdapterRiwayat(mContext, array_id, array_gambar, array_judul, array_tanggal_pinjam, array_tanggal_kembali, array_status);
                                rv_r.setAdapter(adapterRiwayat);
                            } else {
                                Toast.makeText(mContext, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                                adapterRiwayat = new AdapterRiwayat(mContext, array_id, array_gambar, array_judul, array_tanggal_pinjam ,array_tanggal_kembali, array_status);
                                rv_r.setAdapter(adapterRiwayat);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "exception error : "+e, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(mContext, String.valueOf(anError), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}