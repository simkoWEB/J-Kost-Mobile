package com.example.j_kost.Tab;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.Adapter.HistoryAdapter;
import com.example.j_kost.Models.Transaksi;
import com.example.j_kost.R;
import com.example.j_kost.Utils.NetworkUtils;

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

public class TabHistory extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private HistoryAdapter historyAdapter;
    private List<Transaksi> listData;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_history, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.kumpulan_card_history);
        listData = new ArrayList<>();

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", "");

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

    private void getDataHistory(String idUser, RequestQueue requestQueue) {
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
                                    String namaTransaksi = "Pembayaran Bulan " + namaBulan;

                                    listData.add(new Transaksi(idTransaksi, namaTransaksi, tanggal, harga));
                                }

                                historyAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        // Tambahkan request ke dalam queue untuk dieksekusi
        requestQueue.add(jsonObjectRequest);
    }


}
