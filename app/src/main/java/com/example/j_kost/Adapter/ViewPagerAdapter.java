package com.example.j_kost.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.j_kost.TabHistory;
import com.example.j_kost.TabPembayaran;
import com.example.j_kost.TransaksiFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull TransaksiFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
      switch (position){
          case 0 : return new TabPembayaran();
          case 1 : return new TabHistory();
          default: return new TabPembayaran();
      }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
