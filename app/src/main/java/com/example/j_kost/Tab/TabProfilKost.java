package com.example.j_kost.Tab;

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

    TextView namaKost;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tab_profil_kost, container, false);

        namaKost = view.findViewById(R.id.text_judulKostProfile);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        getDataUser();

        return view;
    }

    private void getDataUser(){
        String kostName = sharedPreferences.getString("namaKost", "-");

        namaKost.setText(kostName);
    }

}