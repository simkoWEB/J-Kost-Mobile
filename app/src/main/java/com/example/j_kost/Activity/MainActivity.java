package com.example.j_kost.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.j_kost.Fragment.HomeFragment;
import com.example.j_kost.Fragment.KomplainFragment;
import com.example.j_kost.Fragment.ProfileFragment;
import com.example.j_kost.Fragment.TransaksiFragment;
import com.example.j_kost.R;
import com.example.j_kost.Utils.NetworkUtils;
import com.example.j_kost.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!NetworkUtils.isNetworkConnected(this)) {
            showNoInternetDialog();
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
                finish();  // Close the current activity
            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000); // Reset after 2 seconds
                return true;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tidak terhubung ke internet. Silakan cek koneksi Anda dan coba lagi.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        // Di sini, kamu dapat menambahkan tindakan lanjutan jika diperlukan
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
