package com.example.j_kost.Tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.j_kost.R;

public class TabProfilKost extends Fragment {

    TextView namaKost, alamatKost, ukuranKamar, peraturan, fasilitas;
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tab_profil_kost, container, false);

        namaKost = view.findViewById(R.id.text_judulKostProfile);
        alamatKost = view.findViewById(R.id.alamatKost);
        ukuranKamar = view.findViewById(R.id.tvLuasKamar);
        fasilitas = view.findViewById(R.id.tvFasilitas);
        peraturan = view.findViewById(R.id.tvPeraturan);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        getDataUser();

        return view;
    }

    private void getDataUser(){
        String kostName = sharedPreferences.getString("namaKost", "-");
        String kostAddress = sharedPreferences.getString("alamatKost", "-");
        String kostRules = sharedPreferences.getString("peraturanKost", "-");
        String kostFasilitas = sharedPreferences.getString("fasilitasKost", "-");
        String roomSize = sharedPreferences.getString("ukuranKamar", "-");

        namaKost.setText(kostName);
        alamatKost.setText(kostAddress);
        fasilitas.setText(kostFasilitas);
        peraturan.setText(kostRules);
        ukuranKamar.setText("Ukuran kamar : "+roomSize+" m");
    }

}