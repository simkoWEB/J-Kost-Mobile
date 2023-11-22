package com.example.j_kost.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j_kost.Activity.MainActivity;
import com.example.j_kost.Fragment.ProfileFragment;
import com.example.j_kost.R;

public class DetailGantiPassword extends AppCompatActivity {
    ImageView btnBack;
    Button btnSimpan;
    TextView currentPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ganti_password);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelConfirmation();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showCancelConfirmation();
    }

    private void showCancelConfirmation() {
        // Tampilkan alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        builder.setTitle("Konfirmasi!");
        builder.setMessage("Apakah Anda yakin ingin membatalkan perubahan?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Kembali ke MainActivity (Activity yang meng-host ProfileFragment)
                Intent intent = new Intent(DetailGantiPassword.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Hapus semua activity di atasnya
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
}