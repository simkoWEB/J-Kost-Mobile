package com.example.j_kost.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.j_kost.R;
import com.google.android.material.textfield.TextInputEditText;

public class DetailEditProfile extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    Button Btnedit;
    ImageView btnBack;
    EditText nama, email, noHp, alamat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit_profile);

        nama = findViewById(R.id.editNama);
        email = findViewById(R.id.editEmail);
        noHp = findViewById(R.id.editNotelp);
        alamat = findViewById(R.id.editAlamat);

        Btnedit = findViewById(R.id.btnEdit);
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
        String userName = sharedPreferences.getString("namaLengkap", "-");
        String userEmail = sharedPreferences.getString("emailUser", "-");
        String userNumber = sharedPreferences.getString("noHp", "-");
        String userAddress = sharedPreferences.getString("alamatUser", "-");

        nama.setText(userName);
        email.setText(userEmail);
        noHp.setText(userNumber);
        alamat.setText(userAddress);
    }
}