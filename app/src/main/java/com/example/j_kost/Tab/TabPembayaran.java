package com.example.j_kost.Tab;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.j_kost.R;
public class TabPembayaran extends Fragment {

    EditText nominalBayar;

    public static String formatDec(int val) {
        return String.format("%,d", val).replace('.', ',');
    }

    //untuk menghilangkan titik
    public static String reFormat(String val) {
        return val.replaceAll("[,.]", ""); // Menghapus semua koma dan titik
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab_pembayaran, container, false);

        nominalBayar = view.findViewById(R.id.etNominal);
        InputFilter filter = new InputFilter.LengthFilter(10);
        nominalBayar.setFilters(new InputFilter[]{filter});

        nominalBayar.addTextChangedListener(new TextWatcher() {
            boolean isFormatting;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Tambahkan validasi saat teks berubah
                if (charSequence.length() > 10) {
                    // Jika panjang input melebihi 8 karakter, maka batasi panjangnya
                    String limitedInput = charSequence.toString().substring(0, 10);
                    nominalBayar.setText(limitedInput); // Set ulang teks pada EditText
                    nominalBayar.setSelection(limitedInput.length()); // Posisi kursor di akhir teks
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Cek apakah sedang dalam proses pemformatan untuk menghindari rekursi tak terbatas
                if (isFormatting) return;

                // Matikan pemformatan sementara agar tidak masuk ke dalam loop
                isFormatting = true;

                // Ambil teks yang dimasukkan
                String originalText = editable.toString();

                // Hapus tanda pemisah ribuan
                String cleanString = originalText.replaceAll("[,.]", "");

                // Cek apakah string kosong atau tidak
                if (!cleanString.isEmpty()) {
                    try {
                        // Parsing ke tipe data long (bisa juga menggunakan double atau int, sesuai kebutuhan)
                        long parsed = Long.parseLong(cleanString);

                        // Format angka ke dalam format desimal yang diinginkan
                        String formatted = formatDec((int) parsed);

                        // Set hasil format ke dalam EditText
                        nominalBayar.setText(formatted);
                        nominalBayar.setSelection(formatted.length()); // Pindahkan kursor ke akhir teks
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                isFormatting = false;
            }
        });

        return view;
    }
}