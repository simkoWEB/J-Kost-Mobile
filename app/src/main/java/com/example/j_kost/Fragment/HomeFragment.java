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

import com.example.j_kost.Adapter.HistoryAdapter;
import com.example.j_kost.R;
import com.example.j_kost.Tab.TabHistory;
import com.example.j_kost.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

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
    private List<String> listData;
    private SharedPreferences sharedPreferences;
    ImageView profilePhoto;
    public TextView namaUser, namaKost, selengkapnya, bulanPembayaran;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        profilePhoto = view.findViewById(R.id.profile);
        namaUser = view.findViewById(R.id.tvUsername);
        namaKost = view.findViewById(R.id.tvKost);
        selengkapnya = view.findViewById(R.id.selengkapnya);
        bulanPembayaran = view.findViewById(R.id.bulanPembayaran);
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        getDataUser();

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

        for (int i = 0; i < 10; i++) {
            listData.add("Transaksi bulan ke-" + i);
        }

        // Set up LinearLayoutManager and HistoryAdapter
        linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        historyAdapter = new HistoryAdapter(requireContext(), listData);
        recyclerView.setAdapter(historyAdapter);

        // Notify adapter of data changes
        historyAdapter.notifyDataSetChanged();

        return view;

    }

    private void getDataUser(){
        String userName = sharedPreferences.getString("namaLengkap", "-");
        String userKost = sharedPreferences.getString("namaKost", "-");
        String photoPath = sharedPreferences.getString("fotoUser", "");

        namaUser.setText(userName);
        namaKost.setText(userKost);

        if (!photoPath.equals("")) {
//            ini local
            Picasso.get().load("http://"+ NetworkUtils.BASE_URL +"/PHP-MVC/public/foto/"+photoPath).into(profilePhoto);
        } else {
            profilePhoto.setImageResource(R.drawable.pp);
        }
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

}