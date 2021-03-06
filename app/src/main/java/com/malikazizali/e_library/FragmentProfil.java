package com.malikazizali.e_library;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentProfil extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private Context mContext;

    private ImageButton ib_user;
    private CardView changePass, customerService, logout;

    //user data
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    AlertDialog alertDialog;

    CSDialog csDialog;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentProfil() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentProfil newInstance(String param1, String param2) {
        FragmentProfil fragment = new FragmentProfil();
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
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        ib_user = view.findViewById(R.id.btn_edit_user);
        ib_user.setOnClickListener(this);

        changePass = view.findViewById(R.id.cv_ganti_password);
        changePass.setOnClickListener(this);

        customerService = view.findViewById(R.id.cv_customer_service);
        customerService.setOnClickListener(this);

        logout = view.findViewById(R.id.cv_logout);
        logout.setOnClickListener(this);

        csDialog = new CSDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Ganti Password Anda ?\nAnda akan menerima Email untuk mereset password anda")
                .setTitle("Peringatan Ganti Password");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updatePassword();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alertDialog = builder.create();

        mAuth = FirebaseAuth.getInstance();

        //user data
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        userID = user.getUid();
        final TextView email = (TextView) view.findViewById(R.id.tv_profil_email);
        final TextView nama = (TextView) view.findViewById(R.id.tv_profil_nama);
        final TextView telp = (TextView) view.findViewById(R.id.tv_profil_telp);
        final TextView alamat = (TextView) view.findViewById(R.id.tv_profil_alamat);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String emailUser = userProfile.email;
                    String namaUser = userProfile.nama;
                    String telpUser = userProfile.no_telp;
                    String alamatUser = userProfile.alamat;

                    email.setText(emailUser);
                    nama.setText(namaUser);
                    telp.setText(telpUser);
                    alamat.setText(alamatUser);
                } else {
                    email.setText("");
                    nama.setText("");
                    telp.setText("");
                    alamat.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "Gagal Mengambil Data User", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edit_user:
                startActivity(new Intent(mContext, EditUser.class));
                break;
            case R.id.cv_ganti_password:
                alertDialog.show();
                break;
            case R.id.cv_customer_service:
                csDialog.show(getFragmentManager(), "dialog");
                //startActivity(new Intent(mContext, CustomerService.class));
                break;
            case R.id.cv_logout:
                logout();
                break;
        }
    }

    private void updatePassword() {
        String emailUser = user.getEmail();
            mAuth.sendPasswordResetEmail(emailUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(mContext, "Cek Email anda untuk mereset password akun anda!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext,"Ada Kesalahan Dalam Sistem",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(mContext, Login.class));
    }
}