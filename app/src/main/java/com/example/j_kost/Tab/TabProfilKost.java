package com.example.j_kost.Tab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.NetworkUtils;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class TabProfilKost extends Fragment {

    TextView namaKost, alamatKost, ukuranKamar, peraturan, fasilitas, noHpPemilik, namaPemilik;
    ImageView img1, img2,img3;
    SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tab_profil_kost, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());

        namaKost = view.findViewById(R.id.text_judulKostProfile);
        alamatKost = view.findViewById(R.id.alamatKost);
        ukuranKamar = view.findViewById(R.id.tvLuasKamar);
        fasilitas = view.findViewById(R.id.tvFasilitas);
        peraturan = view.findViewById(R.id.tvPeraturan);
        noHpPemilik = view.findViewById(R.id.tvNotelp);
        namaPemilik = view.findViewById(R.id.tvNamaPemilik);

        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String kamarId = sharedPreferences.getString("nomorKamar", "");
        String userId = sharedPreferences.getString("idUser", "");

        Log.d("Get kamar id", kamarId);

        getDataKamar(userId);
        getDataKost(kamarId);

        return view;
    }

    private void getDataKost(String roomId) {
        String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/GetDataMobile/getUserKost/" + roomId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String kostName = data.getString("nama_kost");
                            String fasilitasKost = data.getString("fasilitas_kost");
                            String peraturanKost = data.getString("peraturan_kost");
                            String fotoKost = data.getString("link_fotoKost");
                            String alamat = data.getString("alamat");
                            String noTelpOwner = data.getString("no_hp");
                            String namaOwner = data.getString("nama_pemilik");

                            namaKost.setText(kostName);
                            fasilitas.setText(fasilitasKost);
                            peraturan.setText(peraturanKost);
                            alamatKost.setText(alamat);
                            noHpPemilik.setText(noTelpOwner);
                            namaPemilik.setText("("+namaOwner+")");

                            // Memisahkan string menjadi 3 bagian berdasarkan koma
                            String[] fotoArray = fotoKost.split(",");

                            if (fotoArray.length >= 1) {
                                loadImageToImageView(fotoArray[0], img1);
                            }
                            if (fotoArray.length >= 2) {
                                loadImageToImageView(fotoArray[1], img2);
                            }
                            if (fotoArray.length >= 3) {
                                loadImageToImageView(fotoArray[2], img3);
                            }

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

    private void loadImageToImageView(String imageUrl, ImageView imageView) {
        String fullImageUrl = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/foto/" + imageUrl;
        Picasso.get().load(fullImageUrl).into(imageView);
    }


    private void getDataKamar(String userId){
        String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/GetDataMobile/getUserData/" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String roomSize = data.getString("Ukuran Kamar");

                            ukuranKamar.setText(roomSize+" m");

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