package com.example.j_kost.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j_kost.R;

public class DetailGantiPassword extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    ImageView btnBack;
    Button btnSimpan;
    TextView currentPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ganti_password);

        currentPass = findViewById(R.id.currentPass);
        btnBack = findViewById(R.id.btnBack);

        sharedPreferences = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        getDataUser();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getDataUser(){
        String userPasswrod = sharedPreferences.getString("passwordUser", "-");

        currentPass.setText(userPasswrod);
    }
}