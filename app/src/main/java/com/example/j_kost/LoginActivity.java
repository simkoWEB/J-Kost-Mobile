package com.example.j_kost;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.j_kost.forget_pass.VerificationActivity;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;

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
        String apiUrl = "https://j-kost.000webhostapp.com/PHP-MVC/public/LoginApi/login";

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

                    SharedPreferences userPref = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();

                    editor.putString("idUser", dataUser.getString("id_user"));
                    editor.putString("namaLengkap", dataUser.getString("Nama Penghuni"));
                    editor.putString("emailUser", dataUser.getString("email"));
                    editor.putString("passwordUser", dataUser.getString("password"));
                    editor.putString("alamatUser", dataUser.getString("Alamat User"));
                    editor.putString("noHp", dataUser.getString("Notelp User"));
                    editor.putString("fotoUser", dataUser.getString("foto_user"));

                    editor.putString("nomorKamar", dataUser.getString("Nomor Kamar"));
                    editor.putString("fasilitasKamar", dataUser.getString("fasilitas"));
                    editor.putString("kategoriKost", dataUser.getString("kategori"));
                    editor.putString("ukuranKamar", dataUser.getString("Ukuran Kamar"));
                    editor.putString("namaKost", dataUser.getString("nama_kost"));
                    editor.putString("alamatKost", dataUser.getString("Alamat Kost"));

                    editor.apply();

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
}
