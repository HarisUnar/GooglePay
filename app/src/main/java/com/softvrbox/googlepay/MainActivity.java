package com.softvrbox.googlepay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int REQUEST_CODE_PAYMENT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pay(View view) {

        String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "9182dawinder@oksbi") //Enter Upi id here
                .appendQueryParameter("pn", "clash of clans fans") //Enter name here
                .appendQueryParameter("tn", "Payment just for testing") // Enter note here
                .appendQueryParameter("am", "5") // Enter amount here
                .appendQueryParameter("cu", "PKR")  // Currency type
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        try {

            startActivityForResult(intent, REQUEST_CODE_PAYMENT);

        } catch (Exception e) {
            Toast.makeText(this, "Google pay app not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    String value = data.getStringExtra("response");
                    getStatus(value);

                } else {
                    Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getStatus(String data) {
        boolean paymentCancel = false;
        boolean paymentSuccess = false;
        boolean paymentFailed = false;
        String[] value = data.split("&");
        for (String s : value) {
            String[] checkString = s.split("=");
            if (checkString.length >= 2) {

                if (checkString[0].toLowerCase().equals("status")) {

                    if (checkString[1].equals("success")) {
                        paymentSuccess = true;
                    }

                } else {
                    paymentFailed = true;
                }

            } else {
                paymentCancel = true;
            }
        }
        if (paymentSuccess) {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        } else if (paymentCancel) {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        } else if (paymentFailed) {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}