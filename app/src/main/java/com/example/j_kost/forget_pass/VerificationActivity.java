package com.example.j_kost.forget_pass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.chaos.view.PinView;
import com.example.j_kost.LoginActivity;
import com.example.j_kost.R;

public class VerificationActivity extends AppCompatActivity {
    Button continueBtn;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VerificationActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        continueBtn = findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VerificationActivity.this, ResetPassActivity.class);
                startActivity(i);
                finish();
            }
        });

        final PinView pinView = findViewById(R.id.firstPinView);
        pinView.setAnimationEnable(true);

    }
}
