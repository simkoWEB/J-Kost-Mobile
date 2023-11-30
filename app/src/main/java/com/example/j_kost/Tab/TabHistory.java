package com.example.j_kost.Tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.j_kost.Adapter.HistoryAdapter;
import com.example.j_kost.Models.Transaksi;
import com.example.j_kost.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabHistory#newInstance} factory method to
 * create an instance of this fragment.
 */

public class TabHistory extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private HistoryAdapter historyAdapter;
    private List<Transaksi> listData;

    // Fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TabHistory() {
        // Required empty public constructor
    }

    public static TabHistory newInstance(String param1, String param2) {
        TabHistory fragment = new TabHistory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_history, container, false);

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

}
