package com.malikazizali.e_library;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CustomerService extends AppCompatActivity implements View.OnClickListener {
    CardView telp, sms, ig, wa;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        telp = findViewById(R.id.cv_call);
        telp.setOnClickListener(this);
        sms = findViewById(R.id.cv_sms);
        sms.setOnClickListener(this);
        ig = findViewById(R.id.cv_ig);
        ig.setOnClickListener(this);
        wa = findViewById(R.id.cv_wa);
        wa.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cv_call:
                teleponCS();
                break;
            case R.id.cv_sms:
                smsCS();
                break;
            case R.id.cv_ig:
                igCS();
                break;
            case R.id.cv_wa:
                waCS();
                break;
        }

    }

    private void igCS() {
        Uri uri = Uri.parse("https://www.instagram.com/dybim__/");
        Intent IgMe = new Intent(Intent.ACTION_VIEW, uri);

        IgMe.setPackage("com.instagram.android");

        try {
            startActivity(IgMe);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dybim__/")));
        }
    }

    private void waCS() {
        Uri uri = Uri.parse("https://wa.me/6285879610915");
        Intent waMe = new Intent(Intent.ACTION_VIEW, uri);

        waMe.setPackage("com.whatsapp");

        try {
            startActivity(waMe);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/6285879610915")));
        }
    }

    private void smsCS() {
        String num = "smsto:085879610915";
        Uri sendto = Uri.parse(num);
        Intent sms = new Intent(Intent.ACTION_SENDTO, sendto);
        sms.setData(Uri.parse(num));
        sms.putExtra("sms_body", "Apa masalah anda ?");
        startActivity(sms);

    }

    private void teleponCS() {
        if (ContextCompat.checkSelfPermission(CustomerService.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerService.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:081234567890")));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                teleponCS();
            }
        } else {
            Toast.makeText(this, "Permission Denied",
                    Toast.LENGTH_SHORT).show();
        }
    }
}