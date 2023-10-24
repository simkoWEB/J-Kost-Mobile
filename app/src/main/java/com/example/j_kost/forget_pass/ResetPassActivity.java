package com.example.j_kost.forget_pass;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.j_kost.LoginActivity;
import com.example.j_kost.R;

public class ResetPassActivity extends AppCompatActivity {

    Button resetPassBtn;
    ImageView backBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        resetPassBtn = findViewById(R.id.resetBtn);
        backBtn = findViewById(R.id.btnBack);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResetPassActivity.this, VerificationActivity.class);
                startActivity(i);
                finish();
            }
        });

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResetPassActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}