package com.malikazizali.e_library;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentMenuUtama extends Fragment {
    RecyclerView rv_mu;
    private ArrayList<String> array_gambar;
    private ArrayList<String> array_rating;
    AdapterMenuUtama adapterMenuUtama;
    private Context mContext;
    TextView rating;
    ViewFlipper viewFlipper;

    //user data
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMenuUtama() {
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
     * @return A new instance of fragment FragmentMenuUtama.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMenuUtama newInstance(String param1, String param2) {
        FragmentMenuUtama fragment = new FragmentMenuUtama();
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
        View view = inflater.inflate(R.layout.fragment_menu_utama, container, false);
        //Flipper
        int images[] ={R.drawable.slider1,R.drawable.slider2,R.drawable.slider3};

        viewFlipper = view.findViewById(R.id.flipper_dashboard);

        for (int i =0; i<images.length; i++){
            fliverImages(images[i]);
        }
        for (int image: images)
            fliverImages(image);

        //user data
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        userID = user.getUid();
        final TextView greetUser = (TextView) view.findViewById(R.id.tv_nama);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String namaUser = userProfile.nama;

                    greetUser.setText("Halo, " + namaUser + "!");
                }else {
                    greetUser.setText("Halo, !");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "Gagal Mengambil Data User", Toast.LENGTH_LONG).show();
            }
        });

        //List Buku
        rv_mu = view.findViewById(R.id.rv_menu_utama);
        rv_mu.hasFixedSize();
        LinearLayoutManager layoutManager = new GridLayoutManager(mContext,2);
        rv_mu.setLayoutManager(layoutManager);
        rv_mu.setAdapter(adapterMenuUtama);
        rating = view.findViewById(R.id.rating);

        getData();

        return view;
    }

    void initializeArray() {
        array_gambar = new ArrayList<String>();
        array_rating = new ArrayList<String>();

        array_gambar.clear();
        array_rating.clear();
    }

    public void getData() {
        initializeArray();
        AndroidNetworking.get("http://10.0.2.2/elibrary/getBuku.php")
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
                                    array_rating.add(jo.getString("rating"));
                                }
                                adapterMenuUtama = new AdapterMenuUtama(mContext, array_gambar, array_rating);
                                rv_mu.setAdapter(adapterMenuUtama);
                            } else {
                                Toast.makeText(mContext, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                                adapterMenuUtama = new AdapterMenuUtama(mContext, array_gambar, array_rating);
                                rv_mu.setAdapter(adapterMenuUtama);
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

    public void fliverImages(int images){
        ImageView imageView = new ImageView(mContext);
        imageView.setBackgroundResource(images);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(mContext,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(mContext,android.R.anim.slide_out_right);
    }
}