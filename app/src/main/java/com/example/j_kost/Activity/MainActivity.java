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
import android.widget.Button;
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

        // Cek koneksi internet sebelum tampilan utama dimuat
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

    // Metode untuk mengganti fragment
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
                finish();
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

    // Method untuk menampilkan dialog ketika tidak ada koneksi internet
    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        builder.setTitle("Peringatan!");
        builder.setMessage("Anda tidak terhubung ke internet. Silakan periksa koneksi Anda dan coba lagi.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        alert.show();
    }

}
