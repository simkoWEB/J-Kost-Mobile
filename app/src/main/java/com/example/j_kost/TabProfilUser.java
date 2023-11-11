package com.example.j_kost;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabProfilUser extends Fragment {
    TextView nama, email, notelp, alamat;
    SharedPreferences sharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab_profil_user, container, false);

        nama = view.findViewById(R.id.tvName);
        email = view.findViewById(R.id.tvEmail);
        notelp = view.findViewById(R.id.tvNumber);
        alamat = view.findViewById(R.id.tvAddress);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        getDataUser();

        return view;
    }
    private void getDataUser(){
        String userName = sharedPreferences.getString("namaLengkap", "-");
        String userEmail = sharedPreferences.getString("emailUser", "-");
        String userTelp = sharedPreferences.getString("noHp", "-");
        String userAddress = sharedPreferences.getString("alamatUser", "-");

        nama.setText(userName);
        email.setText(userEmail);
        notelp.setText(userTelp);
        alamat.setText(userAddress);
    }
}