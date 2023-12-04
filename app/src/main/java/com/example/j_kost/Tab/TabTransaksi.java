package com.example.j_kost.Tab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.Adapter.HistoryAdapter;
import com.example.j_kost.Adapter.PembayaranAdapter;
import com.example.j_kost.Models.Pembayaran;
import com.example.j_kost.Models.Transaksi;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.NetworkUtils;
import com.example.j_kost.Utils.ProgressLoading;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

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


public class TabTransaksi extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PembayaranAdapter historyAdapter;
    private List<Pembayaran> listData;
    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;
    ImageView imgNoData;
    private ProgressLoading progressLoading;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab_transaksi, container, false);

        progressLoading = new ProgressLoading();
        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.kumpulan_card_pembayaran);
        listData = new ArrayList<>();

        imgNoData = view.findViewById(R.id.image_no_data);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", "");

        requestQueue = Volley.newRequestQueue(requireContext());

        getDataPembayaran(idUser);

        // Set up LinearLayoutManager and HistoryAdapter
        linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        historyAdapter = new PembayaranAdapter(requireContext(), listData);
        recyclerView.setAdapter(historyAdapter);

        // Notify adapter of data changes
        historyAdapter.notifyDataSetChanged();

        return view;
    }

    private void getDataPembayaran(String idUser){
        progressLoading.show(requireContext(), "Memuat data...");
        String url = "http://"+NetworkUtils.BASE_URL+"/PHP-MVC/public/GetDataMobile/getPembayaran/"+idUser;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int code = response.getInt("code");
                            if (code == 200) {
                                JSONArray dataArray = response.getJSONArray("data");
                                Log.d("DataArrayLength", "Data array length: " + dataArray.length());

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject data = dataArray.getJSONObject(i);
                                    String idTransaksi = data.getString("id_transaksi");
                                    String tanggal = data.getString("tggl_transaksi");
                                    int harga = data.getInt("bayar");
                                    String statusBayar = data.getString("status");

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

                                    listData.add(new Pembayaran(idTransaksi, namaTransaksi, statusBayar, harga));
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
                            progressLoading.hide();
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressLoading.hide();
                        MyPopUp.showErrorDialog(requireContext(), "Gagal menampilkan data", "Silahkan cek kembali koneksi anda", new OnDialogButtonClickListener() {
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