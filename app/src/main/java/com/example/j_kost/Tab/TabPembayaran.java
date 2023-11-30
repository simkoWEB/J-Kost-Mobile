package com.example.j_kost.Tab;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.DetailActivity.DetailEditProfile;
import com.example.j_kost.DetailActivity.MetodePembayaran;
import com.example.j_kost.R;
import com.example.j_kost.Session.SessionManager;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.MyToast;
import com.example.j_kost.Utils.NetworkUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class TabPembayaran extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab_pembayaran, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());

        btnMetodePembayaran = view.findViewById(R.id.BtnMetodePembayaran);

        tvBayar = view.findViewById(R.id.tvBayar);
        tvSisa = view.findViewById(R.id.tvKembali);

        namaPemilik = view.findViewById(R.id.tvNamaPemilik);
        noRek = view.findViewById(R.id.tvNoRekPemilik);
        jenisBayar = view.findViewById(R.id.tvJenisBank);
        btnKonfirmasi = view.findViewById(R.id.konfirmasiPembayaran);

        int hargaBulanan = SessionManager.getHargaBulanan(getContext()); // Mengambil hargaBulanan dari SharedPreferences
        tvHargaTotal = view.findViewById(R.id.tvhragaTotal);

        if (hargaBulanan != 0) {
            String formattedTotal = formatDec(hargaBulanan); // Format nilai jika diperlukan
            tvHargaTotal.setText(formattedTotal); // Mengatur nilai ke tvHargaTotal
        } else {
            tvHargaTotal.setText("Default Value");
        }


        btnPilihFoto = view.findViewById(R.id.btnPilihFoto);
        buktiPembayaran = view.findViewById(R.id.imageViewBuktiPembayaran);

        nominalBayar = view.findViewById(R.id.etNominal);
        InputFilter filter = new InputFilter.LengthFilter(10);
        nominalBayar.setFilters(new InputFilter[]{filter});

        btnPilihFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(TabPembayaran.this)
                        .crop()
                        .compress(5120)
                        .start();
            }
        });

        btnMetodePembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MetodePembayaran.class);
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
                    MyToast.showToastError(getContext(),"Harap isi semua data terlebih dahulu");
                } else {
                    // Jika semua field telah diisi, cek apakah nominal yang dibayarkan kurang dari total yang harus dibayarkan
                    int bayar = Integer.parseInt(reFormat(nominal));

                    if (bayar < total) {
                        // Jika nominal yang dibayarkan kurang dari total, tampilkan pesan kesalahan
                        MyToast.showToastError(getContext(), "Nominal pembayaran kurang dari total yang harus dibayarkan");
                    } else {
                        // Jika semua validasi terpenuhi, tampilkan dialog sukses dan lakukan reset field
                        MyPopUp.showSuccessDialog(getContext(), "Sukses", "Pembayaran berhasil dilakukan", new OnDialogButtonClickListener() {
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



        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String kamarId = sharedPreferences.getString("nomorKamar", "");

        getDataKost(kamarId);

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Mengatur teks pada EditText dengan nilai yang dipilih dari MetodePembayaran Activity
        if (requestCode == 1) {
            if(resultCode == getActivity().RESULT_OK){
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
                        MyPopUp.showErrorDialog(getContext(), "Gagal menampilkan data", "Silahkan cek kembali koneksi anda", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();
                                requireActivity().finishAffinity();
                            }
                        });
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

}