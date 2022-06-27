package com.malikazizali.e_library;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class CSDialog extends DialogFragment implements View.OnClickListener {
    CardView telp, sms, ig, wa;
    private static final int REQUEST_CALL = 1;
    Context mContext;

    public CSDialog() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.cs_dialog, new LinearLayout(getActivity()), false);

        telp = view.findViewById(R.id.cv_call);
        telp.setOnClickListener(this);
        sms = view.findViewById(R.id.cv_sms);
        sms.setOnClickListener(this);
        ig = view.findViewById(R.id.cv_ig);
        ig.setOnClickListener(this);
        wa = view.findViewById(R.id.cv_wa);
        wa.setOnClickListener(this);

        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(view);
        return builder;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
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
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

}