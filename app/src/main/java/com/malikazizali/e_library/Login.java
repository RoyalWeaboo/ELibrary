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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView register, lupa_pass;
    private EditText et_email, et_pass;
    private Button login;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        lupa_pass = findViewById(R.id.lupa_pass);
        lupa_pass.setOnClickListener(this);

        login = findViewById(R.id.button_login);
        login.setOnClickListener(this);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_password);

        progressBar = findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.GONE);

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent i = new Intent(Login.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(Login.this, Register.class));
                break;
            case R.id.lupa_pass:
                startActivity(new Intent(Login.this, LupaPassword.class));
                break;
            case R.id.button_login:
                loginUser();
                break;
        }
    }

    private void loginUser() {
        String email = et_email.getText().toString().trim();
        String pass = et_pass.getText().toString().trim();

        if (email.isEmpty()) {
            et_email.setError("Email Tidak Boleh Kosong!");
            et_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("Email tidak Valid!");
            et_email.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            et_pass.setError("Password Tidak Boleh Kosong!");
            et_pass.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            et_pass.setError("Panjang Password Minimal 6 Karakter!");
            et_pass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(Login.this, MainActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Silahkan cek email anda untuk diverifikasi!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(Login.this, "Gagal Login, Data Login Tidak Ditemukan", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
    }
}