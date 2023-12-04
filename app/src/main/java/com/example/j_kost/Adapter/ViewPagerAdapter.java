package com.example.j_kost.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.j_kost.Fragment.ProfileFragment;
import com.example.j_kost.Tab.TabHistory;
import com.example.j_kost.Tab.TabPembayaran;
import com.example.j_kost.Tab.TabProfilKost;
import com.example.j_kost.Tab.TabProfilUser;
import com.example.j_kost.Fragment.TransaksiFragment;
import com.example.j_kost.Tab.TabTransaksi;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private Fragment parentFragment;

    public ViewPagerAdapter(Fragment parentFragment) {
        super(parentFragment);
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (parentFragment instanceof TransaksiFragment) {
            // Ketika digunakan di TransaksiFragment
            switch (position) {
                case 0:
                    return new TabTransaksi();
                case 1:
                    return new TabHistory();
                default:
                    return new TabTransaksi();
            }
        } else if (parentFragment instanceof ProfileFragment) {
            // Ketika digunakan di ProfileFragment
            switch (position) {
                case 0:
                    return new TabProfilUser();
                case 1:
                    return new TabProfilKost();
                default:
                    return new TabProfilUser();
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

