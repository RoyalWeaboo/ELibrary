package com.malikazizali.e_library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LupaPassword extends AppCompatActivity {
    private EditText et_email_reset;
    private Button btn_reset;
    private ProgressBar progressBar;
    private ImageView iv_back;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        firebaseAuth = FirebaseAuth.getInstance();

        et_email_reset = findViewById(R.id.et_email_reset);
        btn_reset = findViewById(R.id.btn_reset);
        progressBar = findViewById(R.id.progressBarReset);


        iv_back = findViewById(R.id.back_btn);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LupaPassword.this, Login.class));
            }
        });

        progressBar.setVisibility(View.GONE);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String emailForReset = et_email_reset.getText().toString().trim();

        if(emailForReset.isEmpty()){
            et_email_reset.setError("Email Tidak Boleh Kosong!");
            et_email_reset.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailForReset).matches()){
            et_email_reset.setError("Format Email tidak Valid!");
            et_email_reset.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(emailForReset).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LupaPassword.this, "Cek Email anda untuk mereset password akun anda!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(LupaPassword.this,"Ada Kesalahan Dalam Sistem",Toast.LENGTH_LONG).show();
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