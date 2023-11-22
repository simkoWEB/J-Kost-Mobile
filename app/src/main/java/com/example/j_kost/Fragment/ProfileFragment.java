package com.example.j_kost.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.j_kost.Adapter.ViewPagerAdapter;
import com.example.j_kost.R;
import com.google.android.material.tabs.TabLayout;


public class ProfileFragment extends Fragment {

    TabLayout tablayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tablayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager2Profile);
        viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView)(((LinearLayout)((LinearLayout)tablayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary)); // Ganti dengan warna yang diinginkan saat dipilih
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView)(((LinearLayout)((LinearLayout)tablayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.disableColor)); // Ganti dengan warna yang diinginkan saat tidak dipilih
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TextView textView = (TextView)(((LinearLayout)((LinearLayout)tablayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary)); // Ganti dengan warna yang diinginkan saat dipilih
            }
        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tablayout.getTabAt(position).select();
            }
        });

        return view;
    }
}
