package com.example.j_kost.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.j_kost.Adapter.ViewPagerAdapter;
import com.example.j_kost.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TransaksiFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    public TransaksiFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaksi, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager2Transaksi);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        return view;  // Kode untuk mengembalikan tampilan harus ditempatkan di sini
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary)); // Ganti dengan warna yang diinginkan saat dipilih
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.disableColor)); // Ganti dengan warna yang diinginkan saat dipilih
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TextView textView = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary)); // Ganti dengan warna yang diinginkan saat dipilih
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }
}
