package com.example.j_kost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.j_kost.Data.UserData;
import com.example.j_kost.forget_pass.VerificationActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    Button btnLogin;
    TextView tvForgetPass;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.loginBtn);
        tvForgetPass = findViewById(R.id.tvLupaPass);
        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPass);

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
                    // Lakukan permintaan login ke API
                    new LoginTask().execute(email, password);
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
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000); // Reset setelah 2 detik
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            OkHttpClient client = new OkHttpClient();
//            jangan lupa ini seringki diganti kalo masih pake localhost
            String apiUrl = "http://192.168.1.13/PHP-MVC/public/LoginApi/login";

            RequestBody formBody = new FormBody.Builder()
                    .add("email", email)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(formBody)
                    .build();

            try {
                Log.d("MyApp", "Sending request to " + apiUrl); // Log permintaan
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = Objects.requireNonNull(response.body()).string();
                    Log.d("MyApp", "Response: " + responseBody);
                    return responseBody;
                } else {
                    Log.d("MyApp", "Unsuccessful response"); // Log jika respons tidak berhasil
                    return "Gagal mendapatkan respons dari server.";
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("MyApp", "Network error"); // Log jika terjadi kesalahan jaringan
                return "Terjadi kesalahan jaringan.";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response != null) {
                if (response.equals("Gagal mendapatkan respons dari server.") || response.equals("Terjadi kesalahan jaringan.")) {
                    showToast(response);
                } else {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int code = jsonResponse.getInt("code");
                        String status = jsonResponse.getString("status");

                        if (code == 200) {
                            JSONObject dataUser = jsonResponse.getJSONObject("data_user");

                            SharedPreferences userPref = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = userPref.edit();

                            editor.putString("idUser", dataUser.getString("id_user"));
                            editor.putString("namaLengkap", dataUser.getString("nama_lengkap"));
                            editor.putString("emailUser", dataUser.getString("email"));
                            editor.putString("passwordUser", dataUser.getString("password"));
                            editor.putString("tgglLahir", dataUser.getString("tggl_lahir"));
                            editor.putString("jenisKelamin", dataUser.getString("jenis_kelamin"));
                            editor.putString("alamatUser", dataUser.getString("alamat"));
                            editor.putString("noHp", dataUser.getString("no_hp"));
                            editor.putString("fotoUser", dataUser.getString("foto_user"));
                            editor.apply();

//                            String idUser = dataUser.getString("id_user");
//                            String namaLengkap = dataUser.getString("nama_lengkap");
//                            String email = dataUser.getString("email");
//                            String tgglLahir = dataUser.getString("tggl_lahir");
//                            String jenisKelamin = dataUser.getString("jenis_kelamin");
//                            String alamat = dataUser.getString("alamat");
//                            String noHp = dataUser.getString("no_hp");
//                            String fotoUser = dataUser.getString("foto_user");

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
                }
            } else {
                showToast("Gagal terhubung ke server.");
            }
        }
    }

}
