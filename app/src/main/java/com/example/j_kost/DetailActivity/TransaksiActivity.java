package com.example.j_kost.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.R;
import com.example.j_kost.Session.SessionManager;
import com.example.j_kost.Tab.TabPembayaran;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.MyToast;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

public class TransaksiActivity extends AppCompatActivity {

    TextView tvBayar, tvSisa, tvHargaTotal, noRek, jenisBayar, namaPemilik;
    EditText nominalBayar, btnMetodePembayaran;
    ImageView buktiPembayaran;
    Button btnPilihFoto, btnKonfirmasi;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    public static String formatDec(int val) {
        return String.format("%,d", val).replace('.', ',');
    }

    public static String reFormat(String val) {
        return val.replaceAll("[,.]", ""); // Menghapus semua koma dan titik
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        requestQueue = Volley.newRequestQueue(TransaksiActivity.this);

        btnMetodePembayaran = findViewById(R.id.MetodePembayaran);

        tvBayar = findViewById(R.id.tvbayar);
        tvSisa = findViewById(R.id.tvSisa);

        namaPemilik = findViewById(R.id.NamaPemilik);
        noRek = findViewById(R.id.NoRekPemilik);
        jenisBayar = findViewById(R.id.JenisBank);
        btnKonfirmasi = findViewById(R.id.btnConfirm);

        int hargaBulanan = SessionManager.getHargaBulanan(TransaksiActivity.this); // Mengambil hargaBulanan dari SharedPreferences
        tvHargaTotal = findViewById(R.id.tvhragaTotal);

        if (hargaBulanan != 0) {
            String formattedTotal = formatDec(hargaBulanan); // Format nilai jika diperlukan
            tvHargaTotal.setText(formattedTotal); // Mengatur nilai ke tvHargaTotal
        } else {
            tvHargaTotal.setText("Default Value");
        }


        btnPilihFoto = findViewById(R.id.btnChoseFoto);
        buktiPembayaran = findViewById(R.id.ivBuktiPembayaran);

        nominalBayar = findViewById(R.id.Nominal);
        InputFilter filter = new InputFilter.LengthFilter(10);
        nominalBayar.setFilters(new InputFilter[]{filter});

        btnPilihFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(TransaksiActivity.this)
                        .crop()
                        .compress(5120)
                        .start();
            }
        });

        btnMetodePembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransaksiActivity.this, MetodePembayaran.class);
                startActivityForResult(i, 1);
            }
        });

        nominalBayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String cleanString = editable.toString().replaceAll("[,.]", "");
                int parsed = cleanString.isEmpty() ? 0 : Integer.parseInt(cleanString);
                String formatted = formatDec(parsed);

                nominalBayar.removeTextChangedListener(this);
                nominalBayar.setText(formatted);
                nominalBayar.setSelection(formatted.length());
                nominalBayar.addTextChangedListener(this);

                int total = Integer.parseInt(tvHargaTotal.getText().toString().replaceAll("[,.]", ""));
                int bayar = parsed > total ? total : parsed;
                tvBayar.setText(formatDec(bayar));

                // Kondisi jika nilai pada EditText adalah 0
                int sisa = parsed == 0 ? 0 : total - bayar;
                tvSisa.setText(formatDec(sisa));
            }
        });

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengambil nilai dari field-field yang perlu divalidasi
                String nominal = nominalBayar.getText().toString().trim();
                String metodePembayaran = btnMetodePembayaran.getText().toString().trim();
                Drawable buktiPembayaranDrawable = buktiPembayaran.getDrawable();

                // Mengambil total yang harus dibayarkan
                int total = Integer.parseInt(tvHargaTotal.getText().toString().replaceAll("[,.]", ""));

                // Melakukan pengecekan apakah semua field telah diisi
                if (nominal.isEmpty() || metodePembayaran.isEmpty() || buktiPembayaranDrawable == null) {
                    // Jika ada field yang kosong, tampilkan pesan kesalahan kepada pengguna
                    // Misalnya menggunakan Toast atau dialog error
                    // Contoh menggunakan Toast:
                    MyToast.showToastError(TransaksiActivity.this,"Harap isi semua data terlebih dahulu");
                } else {
                    // Jika semua field telah diisi, cek apakah nominal yang dibayarkan kurang dari total yang harus dibayarkan
                    int bayar = Integer.parseInt(reFormat(nominal));

                    if (bayar < total) {
                        // Jika nominal yang dibayarkan kurang dari total, tampilkan pesan kesalahan
                        MyToast.showToastError(TransaksiActivity.this, "Nominal pembayaran kurang dari total yang harus dibayarkan");
                    } else {
                        // Jika semua validasi terpenuhi, tampilkan dialog sukses dan lakukan reset field
                        MyPopUp.showSuccessDialog(TransaksiActivity.this, "Sukses", "Pembayaran berhasil dilakukan", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();

                                nominalBayar.setText("");
                                btnMetodePembayaran.setText("");
                                buktiPembayaran.setImageResource(R.drawable.place_holder_img);
                            }
                        });
                    }
                }
            }
        });



        sharedPreferences = TransaksiActivity.this.getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String kamarId = sharedPreferences.getString("nomorKamar", "");

//        getDataKost(kamarId);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Mengatur teks pada EditText dengan nilai yang dipilih dari MetodePembayaran Activity
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String selectedValue = data.getStringExtra("selectedValue");
                btnMetodePembayaran.setText(selectedValue);
            }
        }

//        ini untuk set foto ketika milih bukti pembayaran
        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            // Menyetel foto ke ImageView menggunakan Picasso
            Picasso.get().load(uri).into(buktiPembayaran); // profile adalah ImageView yang ingin ditampilkan foto di dalamnya
        }
    }
}