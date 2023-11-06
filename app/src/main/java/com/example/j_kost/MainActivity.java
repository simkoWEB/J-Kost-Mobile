package com.example.j_kost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.j_kost.Adapter.ViewPagerAdapter;
import com.example.j_kost.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String namaLengkap = getIntent().getStringExtra("namaLengkap");
        Log.d("NamaLengkap Main Activity", namaLengkap);

        if (namaLengkap != null) {
            // Buat objek Bundle dan masukkan data ke dalamnya
            Bundle bundle = new Bundle();
            bundle.putString("namaLengkap", namaLengkap);

            // Inisialisasi HomeFragment
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle); // Mengatur argumen fragment

            // Gantikan atau tambahkan HomeFragment ke container Anda
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, homeFragment)
                    .commit();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.navBar.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.homeId) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.transaksiId) {
                replaceFragment(new TransaksiFragment());

            } else if (item.getItemId() == R.id.komplainId) {
                replaceFragment(new KomplainFragment());

            } else if (item.getItemId() == R.id.profileId) {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (doubleBackToExitPressedOnce) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000); // Reset setelah 2 detik
                return true;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
