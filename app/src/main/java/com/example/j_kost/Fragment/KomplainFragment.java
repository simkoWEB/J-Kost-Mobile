package com.example.j_kost.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.MyToast;
import com.example.j_kost.Utils.NetworkUtils;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.HashMap;
import java.util.Map;

public class KomplainFragment extends Fragment {

    Button btnKomplain;
    EditText textKomplain;
    Spinner jenisKomplainSpinner, masalahKomplainSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_komplain, container, false);

        jenisKomplainSpinner = view.findViewById(R.id.jenis_komplain);
        masalahKomplainSpinner = view.findViewById(R.id.masalah_komplain);

        textKomplain = view.findViewById(R.id.editText_permasalahan);
        btnKomplain = view.findViewById(R.id.btnAjukan);

        btnKomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
                String userId = sharedPreferences.getString("idUser", "");

                // Mengambil nilai yang dipilih dari Spinner
                String selectedJenisKomplain = jenisKomplainSpinner.getSelectedItem().toString();
                String selectedMasalahKomplain = masalahKomplainSpinner.getSelectedItem().toString();

                // Mengambil deskripsi komplain dari EditText
                String deskripsiKomplain = textKomplain.getText().toString();

                // Melakukan validasi jika userId tidak kosong
                if (!userId.isEmpty() && !selectedJenisKomplain.equals("Pilih tipe komplain") && !selectedMasalahKomplain.equals("Pilih tipe terlebih dahulu") && !deskripsiKomplain.isEmpty()) {
                    // Lakukan proses pengiriman data ke API
                    sendDataToAPI(userId, selectedJenisKomplain, selectedMasalahKomplain, deskripsiKomplain);
                } else {
                    // Jika terdapat input yang masih kosong, tampilkan pesan kepada pengguna
                    MyToast.showToastError(getContext(), "Silahkan lengkapi data sebelum komplain");
                }

            }
        });

        ArrayAdapter<CharSequence> jenisKomplainAdapter = ArrayAdapter.createFromResource(getContext(), R.array.TipeKomplain, android.R.layout.simple_spinner_item);
        jenisKomplainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenisKomplainSpinner.setAdapter(jenisKomplainAdapter);

        jenisKomplainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Mendapatkan pilihan yang dipilih pada spinner jenis komplain
                String selectedTipeKomplain = parent.getItemAtPosition(position).toString();

                // Set adapter untuk spinner masalah komplain berdasarkan pilihan pada jenis komplain
                ArrayAdapter<CharSequence> masalahKomplainAdapter = null;
                switch (selectedTipeKomplain) {
                    case "Keamanan":
                        masalahKomplainAdapter = ArrayAdapter.createFromResource(getContext(), R.array.komplainKeamanan, android.R.layout.simple_spinner_item);
                        break;
                    case "Fasilitas":
                        masalahKomplainAdapter = ArrayAdapter.createFromResource(getContext(), R.array.komplainFasilitas, android.R.layout.simple_spinner_item);
                        break;
                    case "Kegaduhan":
                        masalahKomplainAdapter = ArrayAdapter.createFromResource(getContext(), R.array.komplainKegaduhan, android.R.layout.simple_spinner_item);
                        break;
                    // Handle jika memilih "Pilih tipe komplain" atau "Lainnya"
                    default:
                        masalahKomplainAdapter = ArrayAdapter.createFromResource(getContext(), R.array.defaultArray, android.R.layout.simple_spinner_item);
                        break;
                }

                // Set dropdown style
                if (masalahKomplainAdapter != null) {
                    masalahKomplainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    masalahKomplainSpinner.setAdapter(masalahKomplainAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        return view;
    }

    private void sendDataToAPI(String userId, String jenisKomplain, String masalahKomplain, String deskripsiKomplain) {
        // Buat request POST ke endpoint API
        String url = "http://"+NetworkUtils.BASE_URL+"/PHP-MVC/public/InsertDataApi/insertKomplain/" + userId;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyPopUp.showSuccessDialog(getContext(), "Komplain Berhasil Dikirim", "Semoga kritik dan saran dari anda akan jadi evaluasi untuk pemilik kost", new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                        dialog.dismiss();

                        masalahKomplainSpinner.setSelection(0);
                        jenisKomplainSpinner.setSelection(0);
                        textKomplain.setText("");
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Tangani kesalahan yang terjadi pada saat permintaan gagal dikirim
                // Tampilkan pesan kesalahan atau lakukan penanganan sesuai kebutuhan
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Kumpulkan data untuk dikirim ke server
                Map<String, String> params = new HashMap<>();
                params.put("jenis_komplain", jenisKomplain);
                params.put("tipe_komplain", masalahKomplain);
                params.put("deskripsi_komplain", deskripsiKomplain);
                // Tambahkan parameter lainnya jika diperlukan

                return params;
            }
        };

        // Tambahkan request ke antrian permintaan
        Volley.newRequestQueue(getContext()).add(request);
    }

}