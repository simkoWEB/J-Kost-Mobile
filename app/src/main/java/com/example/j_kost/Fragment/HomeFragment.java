package com.example.j_kost.Fragment;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.Adapter.HistoryAdapter;
import com.example.j_kost.Models.Transaksi;
import com.example.j_kost.R;
import com.example.j_kost.Tab.TabHistory;
import com.example.j_kost.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private HistoryAdapter historyAdapter;
    private List<Transaksi> listData;
    private SharedPreferences sharedPreferences;
    ImageView profilePhoto;
    public TextView namaUser, namaKost, selengkapnya, bulanPembayaran, tvHarga;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());


        profilePhoto = view.findViewById(R.id.profile);
        namaUser = view.findViewById(R.id.tvUsername);
        namaKost = view.findViewById(R.id.tvKost);
        selengkapnya = view.findViewById(R.id.selengkapnya);
        bulanPembayaran = view.findViewById(R.id.bulanPembayaran);
        tvHarga = view.findViewById(R.id.hargaBulanan);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", "");
        String kamarId = sharedPreferences.getString("nomorKamar", "");

        getDataUser(idUser);
        getDataKost(kamarId);

        // Mendapatkan bulan saat ini
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);

        // Konversi nomor bulan menjadi nama bulan
        String monthName = getMonthName(currentMonth);
        bulanPembayaran.setText(monthName);

        selengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            navigateToTabHistoryFragment();
            }
        });

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.kumpulan_card_history);
        listData = new ArrayList<>();

        listData.add(new Transaksi("Transaksi November", "30-11-2023", 350000));
        listData.add(new Transaksi("Transaksi Oktober", "25-10-2023",350000));
        listData.add(new Transaksi("Transaksi September", "29-09-2023",350000));
        listData.add(new Transaksi("Transaksi Agustus", "23-08-2023",350000));

        // Set up LinearLayoutManager and HistoryAdapter
        linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        historyAdapter = new HistoryAdapter(requireContext(), listData);
        recyclerView.setAdapter(historyAdapter);

        // Notify adapter of data changes
        historyAdapter.notifyDataSetChanged();

        return view;

    }

    private void getDataKost(String roomId){
        String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/GetDataMobile/getUserKost/" + roomId; // URL yang diinginkan

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String kostName = data.getString("nama_kost");

                            namaKost.setText(kostName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani jika terjadi kesalahan dalam permintaan
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void getDataUser(String idUser){
        String apiUrl = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/GetDataMobile/getUserData/" + idUser;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            String status = jsonObject.getString("status");

                            if (code == 200 && status.equals("success")) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                String userName = dataObject.getString("Nama Penghuni");
                                int hargaBulanan = dataObject.getInt("harga_bulanan");
                                String photoPath = dataObject.getString("foto_user");

                                namaUser.setText(userName);

                                String formattedHarga = formatDecimal(hargaBulanan);
                                tvHarga.setText("Rp. " + formattedHarga);

                                if (!photoPath.isEmpty()) {
                                    loadImageToImageView(photoPath, profilePhoto);
                                } else {
                                    profilePhoto.setImageResource(R.drawable.default_profilepng);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error jika ada ketika mengambil data dari server
                    }
                });

        // Tambahkan request ke queue Volley
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


    private void loadImageToImageView(String imageUrl, ImageView imageView) {
        String fullImageUrl = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/foto/" + imageUrl;
        Picasso.get().load(fullImageUrl).into(imageView);
    }

    private void navigateToTabHistoryFragment() {
        // Buat objek FragmentManager
        FragmentManager fragmentManager = getParentFragmentManager();

        // Mulai transaksi fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Ganti fragment saat ini dengan TabHistoryFragment
        TabHistory tabHistoryFragment = new TabHistory();
        fragmentTransaction.replace(R.id.mainFrameLayout, tabHistoryFragment);

        // Tambahkan transaksi ke back stack agar dapat di-pop kembali
        fragmentTransaction.addToBackStack(null);

        // Lakukan commit transaksi
        fragmentTransaction.commit();
    }

    private String getMonthName(int month) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        Date date = calendar.getTime();
        return monthFormat.format(date);
    }

    private String formatDecimal(int value) {
        return String.format(Locale.getDefault(), "%,d", value).replace('.', ',');
    }

}