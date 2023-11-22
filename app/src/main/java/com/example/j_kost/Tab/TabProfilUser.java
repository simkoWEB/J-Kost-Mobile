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
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j_kost.DetailActivity.AboutApps;
import com.example.j_kost.DetailActivity.ConfirmOldPass;
import com.example.j_kost.DetailActivity.DetailEditProfile;
import com.example.j_kost.Activity.LoginActivity;
import com.example.j_kost.DetailActivity.DetailGantiPassword;
import com.example.j_kost.R;
import com.example.j_kost.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class TabProfilUser extends Fragment {
    TextView nama, email, notelp;
    Button btnDetailProfile, btnLogout, btnChangePass, btnAbout;
    ImageView profilePhoto;
    SharedPreferences sharedPreferences;
    public String ipLocal = "192.168.1.3";
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab_profil_user, container, false);

        nama = view.findViewById(R.id.tvName);
        email = view.findViewById(R.id.tvEmail);
        notelp = view.findViewById(R.id.tvNumber);
        profilePhoto = view.findViewById(R.id.profile);

        btnDetailProfile = view.findViewById(R.id.btnEdit);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnChangePass = view.findViewById(R.id.btnGantiPass);
        btnAbout = view.findViewById(R.id.btnAbout);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        getDataUser();

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AboutApps.class);
                startActivity(i);
            }
        });

        btnDetailProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DetailEditProfile.class);
                startActivity(i);
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ConfirmOldPass.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogStyle);
        builder.setTitle("Konfirmasi!");
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
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // Mengubah teks tombol menjadi huruf kecil
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setAllCaps(false);
                    positiveButton.setTextColor(getResources().getColor(android.R.color.holo_red_light)); // Mengubah warna teks
                }

                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                if (negativeButton != null) {
                    negativeButton.setAllCaps(false);
                    negativeButton.setTextColor(getResources().getColor(R.color.black)); // Mengubah warna teks
                }
            }
        });

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
        String photoPath = sharedPreferences.getString("fotoUser", "");

        nama.setText(userName);
        email.setText(userEmail);
        notelp.setText(userTelp);

        if (!photoPath.equals("")) {
//            ini local
            Picasso.get().load("http://"+ NetworkUtils.BASE_URL +"/PHP-MVC/public/foto/"+photoPath).into(profilePhoto);
        } else {
            // Jika tidak ada foto yang tersimpan, kamu bisa menampilkan foto placeholder atau pesan lainnya
            profilePhoto.setImageResource(R.drawable.pp);
        }
    }
}