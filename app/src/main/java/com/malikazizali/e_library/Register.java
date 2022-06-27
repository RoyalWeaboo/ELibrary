package com.malikazizali.e_library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_login;
    private EditText nama, email, password, alamat, no_telp, konfirm_pass;
    private Button register;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        tv_login = findViewById(R.id.tv_login);
        nama =findViewById(R.id.nama);
        email =findViewById(R.id.email);
        password =findViewById(R.id.password);
        konfirm_pass = findViewById(R.id.konfirmasi_password);
        alamat =findViewById(R.id.alamat);
        no_telp =findViewById(R.id.telp);
        register = findViewById(R.id.register_btn);

        tv_login.setOnClickListener(this);
        register.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_login:
                Intent login = new Intent(Register.this, Login.class);
                startActivity(login);
                 break;
            case R.id.register_btn:
                daftarUser();
                break;
        }
    }

    private void daftarUser() {
        String em = email.getText().toString().trim();
        String nm = nama.getText().toString().trim();
        String alm = alamat.getText().toString().trim();
        String tlp = no_telp.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String k_pass = konfirm_pass.getText().toString().trim();

        if(em.isEmpty()){
            email.setError("Email Tidak Boleh Kosong!");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
            email.setError("Email tidak Valid!");
            email.requestFocus();
            return;
        }

        if(nm.isEmpty()){
            nama.setError("Nama Tidak Boleh Kosong!");
            nama.requestFocus();
            return;
        }

        if(alm.isEmpty()){
            alamat.setError("Alamat Tidak Boleh Kosong!");
            alamat.requestFocus();
            return;
        }

        if(tlp.isEmpty()){
            no_telp.setError("Nomor Telepon Tidak Boleh Kosong!");
            no_telp.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            password.setError("Password Tidak Boleh Kosong!");
            password.requestFocus();
            return;
        }

        if(pass.length()<6){
            password.setError("Panjang Password Minimal 6 Karakter!");
            password.requestFocus();
            return;
        }

        if(!pass.equals(k_pass)){
            konfirm_pass.setError("Konfirmasi Password tidak sesuai!");
            konfirm_pass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(em,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(em,nm,tlp,alm);
                            FirebaseDatabase.getInstance().getReference()
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressBar.setVisibility(View.GONE);
                                            }
                                            else{
                                                Toast.makeText(Register.this, "Gagal Mendaftarkan User Baru", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }
                        else{
                            String e = task.getException().getMessage();
                            Toast.makeText(Register.this, e, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Login.class));
    }
}