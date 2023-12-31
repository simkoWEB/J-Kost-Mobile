package com.example.j_kost.Fragment;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import com.example.j_kost.Tab.TabTransaksi;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.NetworkUtils;
import com.example.j_kost.Utils.ProgressLoading;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
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
    CardView cardStatus;
    public TextView namaUser, namaKost, selengkapnya, bulanPembayaran, tvHarga, txtStatus;
    private RequestQueue requestQueue;
    ImageView imgNoData;
    private ProgressLoading progressLoading;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());

        progressLoading = new ProgressLoading();

        profilePhoto = view.findViewById(R.id.profile);
        namaUser = view.findViewById(R.id.tvUsername);
        namaKost = view.findViewById(R.id.tvKost);
        selengkapnya = view.findViewById(R.id.selengkapnya);
        bulanPembayaran = view.findViewById(R.id.bulanPembayaran);
        tvHarga = view.findViewById(R.id.hargaBulanan);
        imgNoData = view.findViewById(R.id.imgNoData);
        txtStatus = view.findViewById(R.id.statusText);
        cardStatus = view.findViewById(R.id.statusCard);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", "");
        String kamarId = sharedPreferences.getString("nomorKamar", "");

        getDataUser(idUser);
        getDataKost(kamarId);
        getStatus(idUser);

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

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        getDataHistory(idUser, requestQueue);

        // Set up LinearLayoutManager and HistoryAdapter
        linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        historyAdapter = new HistoryAdapter(requireContext(), listData);
        recyclerView.setAdapter(historyAdapter);

        // Notify adapter of data changes
        historyAdapter.notifyDataSetChanged();

        return view;

    }

    private void getStatus(String roomId){
        String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/GetDataMobile/getStatus/" + roomId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String statusBayar = data.getString("status");

                            txtStatus.setText(statusBayar);

                            if (statusBayar.equals("Proses")){
                                cardStatus.setCardBackgroundColor(getContext().getResources().getColor(R.color.orangeProses));
                            } else if(statusBayar.equals("Belum Bayar")){
                                cardStatus.setCardBackgroundColor(getContext().getResources().getColor(R.color.redDanger));
                            }else{
                                txtStatus.setText("Lunas");
                                cardStatus.setCardBackgroundColor(getContext().getResources().getColor(R.color.greenSuccess));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
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

    private void getDataHistory(String idUser, RequestQueue requestQueue) {
//        progressLoading.show(getContext(), "Memuat data...");
        String url = "http://"+NetworkUtils.BASE_URL+"/PHP-MVC/public/GetDataMobile/getHistory/" + idUser;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int code = response.getInt("code");
                            if (code == 200) {
                                JSONArray dataArray = response.getJSONArray("data");
                                Log.d("DataArrayLength", "Data array length: " + dataArray.length()); // Tambahkan log ini

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject data = dataArray.getJSONObject(i);
                                    String idTransaksi = data.getString("id_transaksi");
                                    String tanggal = data.getString("tggl_transaksi");
                                    int harga = data.getInt("bayar");

                                    // Mengonversi format tanggal ke nama bulan
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = dateFormat.parse(tanggal);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);

                                    // Mendapatkan nama bulan sesuai dengan tanggal
                                    int month = calendar.get(Calendar.MONTH);
                                    String namaBulan = new DateFormatSymbols().getMonths()[month];

                                    // Membuat nama transaksi sesuai dengan bulan dari tanggal
                                    String namaTransaksi = namaBulan;

                                    listData.add(new Transaksi(idTransaksi, namaTransaksi, tanggal, harga));
                                }

                                historyAdapter.notifyDataSetChanged();

                                if (listData.isEmpty()) {
                                    imgNoData.setVisibility(View.VISIBLE);
                                } else {
                                    imgNoData.setVisibility(View.GONE);
                                }

                            }
                            progressLoading.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressLoading.hide();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressLoading.hide();
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

        // Tambahkan request ke dalam queue untuk dieksekusi
        requestQueue.add(jsonObjectRequest);
    }

}