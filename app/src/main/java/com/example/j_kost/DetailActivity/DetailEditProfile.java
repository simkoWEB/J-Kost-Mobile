package com.example.j_kost.DetailActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.j_kost.R;
import com.example.j_kost.Utils.NetworkUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class DetailEditProfile extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    Button Btnedit, BtnUbah;
    ImageView btnBack, profilePhoto;
    EditText nama, email, noHp, jenisKelamin, tglLahir, alamat;
    RadioButton radioBtnMale, radioBtnFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit_profile);

        nama = findViewById(R.id.editNama);
        email = findViewById(R.id.editEmail);

        noHp = findViewById(R.id.editNotelp);
        InputFilter filter = new InputFilter.LengthFilter(12);
        noHp.setFilters(new InputFilter[]{filter});

        alamat = findViewById(R.id.editAlamat);
        radioBtnMale = findViewById(R.id.radioButtonLakiLaki);
        radioBtnFemale = findViewById(R.id.radioButtonPerempuan);
        tglLahir = findViewById(R.id.editTglLahir);

        Btnedit = findViewById(R.id.btnEdit);
        BtnUbah = findViewById(R.id.btnUbah);
        btnBack = findViewById(R.id.btnBack);
        profilePhoto = findViewById(R.id.profile);

        tglLahir.setFocusable(false); // Mencegah keyboard muncul saat editText di-klik
        tglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ambil teks dari EditText tglLahir
                String tanggalLahir = tglLahir.getText().toString();

                // Parse tahun, bulan, dan hari dari teks
                String[] parts = tanggalLahir.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]) - 1; // Bulan dimulai dari 0 (0 = Januari, 1 = Februari, ...)
                int dayOfMonth = Integer.parseInt(parts[2]);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailEditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                        tglLahir.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

                // Menampilkan date picker dialog
                datePickerDialog.show();
            }
        });


        sharedPreferences = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        getDataUser();

        BtnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(DetailEditProfile.this)
                        .cropSquare()
                        .start();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelConfirmation();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            // Menyetel foto ke ImageView menggunakan Picasso
            Picasso.get().load(uri).into(profilePhoto); // profile adalah ImageView yang ingin ditampilkan foto di dalamnya
        }
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
                finish(); // Kembali ke layar sebelumnya
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


    private void getDataUser(){
        String userName = sharedPreferences.getString("namaLengkap", "-");
        String userEmail = sharedPreferences.getString("emailUser", "-");
        String userNumber = sharedPreferences.getString("noHp", "-");
        String userAddress = sharedPreferences.getString("alamatUser", "-");
        String userGender = sharedPreferences.getString("jenisKelamin", "-");
        String userBirth = sharedPreferences.getString("tglLahir", "-");
        String photoPath = sharedPreferences.getString("fotoUser", "");

        nama.setText(userName);
        email.setText(userEmail);
        noHp.setText(userNumber);
        alamat.setText(userAddress);
        tglLahir.setText(userBirth);

        if (userGender.equals("Laki-laki")) {
            radioBtnMale.setChecked(true);
        } else if (userGender.equals("Perempuan")) {
            radioBtnFemale.setChecked(true);
        }

        if (!photoPath.equals("")) {
//            ini local
            Picasso.get().load("http://"+ NetworkUtils.BASE_URL +"/PHP-MVC/public/foto/"+photoPath).into(profilePhoto);
        } else {
            // Jika tidak ada foto yang tersimpan, kamu bisa menampilkan foto placeholder atau pesan lainnya
            profilePhoto.setImageResource(R.drawable.pp);
        }
    }

}