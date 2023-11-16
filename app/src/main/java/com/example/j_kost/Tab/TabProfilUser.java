package com.example.j_kost.Tab;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.j_kost.Activity.DetailEditProfile;
import com.example.j_kost.Activity.LoginActivity;
import com.example.j_kost.R;

public class TabProfilUser extends Fragment {
    TextView nama, email, notelp, alamat;
    Button btnEdit, btnLogout;
    SharedPreferences sharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab_profil_user, container, false);

        nama = view.findViewById(R.id.tvName);
        email = view.findViewById(R.id.tvEmail);
        notelp = view.findViewById(R.id.tvNumber);
        alamat = view.findViewById(R.id.tvAddress);

        btnEdit = view.findViewById(R.id.btnEdit);
        btnLogout = view.findViewById(R.id.btnLogout);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        getDataUser();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DetailEditProfile.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog();
            }
        });


        return view;
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Apakah anda yakin ingin logout?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Lakukan proses logout
                        logoutUser();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Batal logout, tutup dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logoutUser() {
        // Lakukan proses logout di sini, misalnya dengan menghapus data dari SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Menghapus semua data pengguna dari SharedPreferences
        editor.apply();

        // Redirect ke halaman login setelah logout
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish(); // Tutup aktivitas saat ini setelah logout
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