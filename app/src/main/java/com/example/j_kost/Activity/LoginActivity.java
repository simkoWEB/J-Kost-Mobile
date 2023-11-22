package com.example.j_kost.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.ForgetPass.VerificationActivity;
import com.example.j_kost.R;
import com.example.j_kost.Session.SessionManager;
import com.example.j_kost.Utils.NetworkUtils;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    Button btnLogin;
    TextView tvForgetPass;
    ProgressDialog progressDialog;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (!NetworkUtils.isNetworkConnected(this)) {
            showNoInternetDialog();
        }

        btnLogin = findViewById(R.id.loginBtn);
        tvForgetPass = findViewById(R.id.tvLupaPass);
        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPass);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Silahkan Tunggu...");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(emailEditText.getText()).toString();
                String password = Objects.requireNonNull(passwordEditText.getText()).toString();

                if (email.isEmpty() || password.isEmpty()) {
                    showToast("Harap isi semua field!");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showToast("Harap masukkan email yang valid!");
                } else {
                    progressDialog.show();
                    // Lakukan permintaan login ke API menggunakan Volley
                    performLoginRequest(email, password);
                }
            }
        });

        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, VerificationActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast("Tekan sekali lagi untuk keluar");

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000); // Reset setelah 2 detik
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void performLoginRequest(String email, String password) {
//        String apiUrl = "https://j-kost.000webhostapp.com/PHP-MVC/public/LoginApi/login";
        String apiUrl = "http://"+NetworkUtils.BASE_URL+"/PHP-MVC/public/LoginApi/login";

        // Buat permintaan login menggunakan Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleLoginResponse(response);
//                        menghilangkan loading
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        menghilangkan loading
                        progressDialog.dismiss();
                        showToast("Terjadi kesalahan jaringan.");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        // Tambahkan permintaan ke antrian Volley
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void handleLoginResponse(String response) {
        if (response != null) {
            Log.d("MyApp", "Response: " + response);
            try {
                JSONObject jsonResponse = new JSONObject(response);
                int code = jsonResponse.getInt("code");
                String status = jsonResponse.getString("status");

                if (code == 200) {
                    JSONObject dataUser = jsonResponse.getJSONObject("data_user");

                    // Save user data using SessionManager
                    SessionManager.loginUser(
                            this,
                            dataUser.getString("id_user"),
                            dataUser.getString("Nama Penghuni"),
                            dataUser.getString("email"),
                            dataUser.getString("password"),
                            dataUser.getString("Alamat User"),
                            dataUser.getString("Notelp User"),
                            dataUser.getString("Jenis Kelamin"),
                            dataUser.getString("Tanggal Lahir"),
                            dataUser.getString("foto_user"),
                            dataUser.getString("Nomor Kamar"),
                            dataUser.getString("fasilitas"),
                            dataUser.getString("kategori"),
                            dataUser.getString("Ukuran Kamar"),
                            dataUser.getString("nama_kost"),
                            dataUser.getString("Alamat Kost")
                    );

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                    showToast("Berhasil Login");
                } else {
                    showToast(status);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showToast("Terjadi kesalahan dalam pemrosesan data.");
            }
        } else {
            showToast("Gagal terhubung ke server.");
        }
    }

    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        builder.setTitle("Peringatan!");
        builder.setMessage("Anda Tidak terhubung ke internet. Silakan periksa koneksi Anda dan coba lagi.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
