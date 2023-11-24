package com.example.j_kost.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.j_kost.Session.SessionManager;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.MyToast;
import com.example.j_kost.Utils.NetworkUtils;
import com.example.j_kost.databinding.ActivityMainBinding;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cek koneksi internet sebelum tampilan utama dimuat
        if (!NetworkUtils.isNetworkConnected(this)) {
            showNoInternetDialog();
        } else {
            // Cek status login
            if (!SessionManager.isLoggedIn(this)) {
                popUpSessionEnd();
                return; // untuk menghentikan eksekusi setelah menampilkan dialog
            }
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
                MyToast.showToastInfo(MainActivity.this, "Tekan sekali lagi untuk keluar");
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000); // Reset setelah 2 detik
                return true;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // Method untuk menampilkan dialog ketika tidak ada koneksi internet
    private void showNoInternetDialog() {
        MyPopUp.showAlertDialog(MainActivity.this, "Peringatan!", "Perangkat anda tidak terhubung dengan internet, silahkan coba lagi", new OnDialogButtonClickListener() {
            @Override
            public void onDismissClicked(Dialog dialog) {
                super.onDismissClicked(dialog);
                finish();
            }
        });
    }

    private void popUpSessionEnd(){
        MyPopUp.showAlertDialog(MainActivity.this, "Oops", "Sesi anda telah berakhir, silahkan login kembali", new OnDialogButtonClickListener() {
            @Override
            public void onDismissClicked(Dialog dialog) {
                super.onDismissClicked(dialog);
                dialog.dismiss();
                redirectToLogin();
            }
        });
    }

    private void redirectToLogin() {
        // Tambahkan intent ke LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
