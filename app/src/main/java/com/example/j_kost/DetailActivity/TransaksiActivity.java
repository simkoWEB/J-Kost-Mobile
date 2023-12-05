package com.example.j_kost.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.R;
import com.example.j_kost.Session.SessionManager;
import com.example.j_kost.Tab.TabPembayaran;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.MyToast;
import com.example.j_kost.Utils.NetworkUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class TransaksiActivity extends AppCompatActivity {

    TextView tvBayar, tvSisa, tvHargaTotal, noRek, jenisBayar, namaPemilik, judul;
    EditText nominalBayar, btnMetodePembayaran;
    ImageView buktiPembayaran, btnBack;
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
        String idTransaksi = getIntent().getStringExtra("idTransaksi");
        btnMetodePembayaran = findViewById(R.id.MetodePembayaran);

        tvBayar = findViewById(R.id.tvbayar);
        tvSisa = findViewById(R.id.tvSisa);

        namaPemilik = findViewById(R.id.NamaPemilik);
        noRek = findViewById(R.id.NoRekPemilik);
        jenisBayar = findViewById(R.id.JenisBank);
        btnKonfirmasi = findViewById(R.id.btnConfirm);
        judul = findViewById(R.id.tittleHead);
        btnBack = findViewById(R.id.btnKembali);

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                String nominal = nominalBayar.getText().toString().trim();
                String metodePembayaran = btnMetodePembayaran.getText().toString().trim();
                Drawable buktiPembayaranDrawable = buktiPembayaran.getDrawable();
                int total = Integer.parseInt(tvHargaTotal.getText().toString().replaceAll("[,.]", ""));

                if (nominal.isEmpty() || metodePembayaran.isEmpty() || buktiPembayaranDrawable == null) {
                    MyToast.showToastError(TransaksiActivity.this,"Harap isi semua data terlebih dahulu");
                } else {
                    // Jika semua field telah diisi, cek apakah nominal yang dibayarkan kurang dari total yang harus dibayarkan
                    int bayar = Integer.parseInt(reFormat(nominal));

                    if (bayar < total) {
                        MyToast.showToastError(TransaksiActivity.this, "Nominal pembayaran kurang dari total yang harus dibayarkan");
                    } else {
                        editTransaction(idTransaksi, String.valueOf(bayar), metodePembayaran, buktiPembayaranDrawable);
                    }
                }
            }
        });



        sharedPreferences = TransaksiActivity.this.getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String kamarId = sharedPreferences.getString("nomorKamar", "");
        getDataKost(kamarId);


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

    private void editTransaction(String idTransaksi, String bayar, String metode, Drawable photoBitmap) {
        String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/EditDataApi/editTransaction/" + idTransaksi;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response if the request is successful
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int code = jsonResponse.getInt("code");
                            if (code == 200) {
                                showPaymentSuccessDialog();
                            } else {
                                MyToast.showToastError(TransaksiActivity.this, "Transaksi gagal dilakukan");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String e = error.toString();
                        // Handle error response
                        MyPopUp.showErrorDialog(TransaksiActivity.this, "Error", e, new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();
                            }
                        });
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("bayar", bayar);
                params.put("metode_pembayaran", metode);

                // Encode gambar ke dalam format base64
                String encodedImage = encodeImage(photoBitmap);
                params.put("foto_bukti_bayar", encodedImage);


                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private String encodeImage(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    private void getDataKost(String roomId) {
        String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/GetDataMobile/getUserKost/" + roomId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String namaOwner = data.getString("nama_rekening");
                            String jenisBank = data.getString("jenis_bank");
                            String norek = data.getString("no_rekening");

                            namaPemilik.setText(namaOwner);
                            jenisBayar.setText(jenisBank);
                            noRek.setText(norek);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyPopUp.showErrorDialog(TransaksiActivity.this, "Gagal menampilkan data", "Silahkan cek kembali koneksi anda", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();
                                finish();
                            }
                        });
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void showPaymentSuccessDialog() {
        MyPopUp.showSuccessDialog(TransaksiActivity.this, "Sukses", "Pembayaran berhasil dilakukan", new OnDialogButtonClickListener() {
            @Override
            public void onDismissClicked(Dialog dialog) {
                super.onDismissClicked(dialog);
                dialog.dismiss();
                resetFields();
                finish();
            }
        });
    }

    private void resetFields() {
        nominalBayar.setText("");
        btnMetodePembayaran.setText("");
        buktiPembayaran.setImageResource(R.drawable.place_holder_img);
    }
}