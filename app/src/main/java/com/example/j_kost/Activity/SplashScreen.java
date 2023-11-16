package com.example.j_kost.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.j_kost.R;
import com.example.j_kost.Session.SessionManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Cek status login menggunakan SessionManager
                boolean isLoggedIn = SessionManager.isLoggedIn(SplashScreen.this);

                // Intent untuk pindah ke LoginActivity atau MainActivity sesuai dengan status login
                Intent intent;
                if (isLoggedIn) {
                    intent = new Intent(SplashScreen.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 2000); // Delay 2 detik sebelum berpindah layar

    }
}