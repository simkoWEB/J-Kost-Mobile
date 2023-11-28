package com.example.j_kost.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyToast;
import com.example.j_kost.Utils.NetworkUtils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmOldPass extends AppCompatActivity {
    Button btnCofirm;
    TextInputEditText oldPass;
    ImageView btnBack;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_old_pass);

        btnCofirm = findViewById(R.id.confirmBtn);
        btnBack = findViewById(R.id.btnBack);
        oldPass = findViewById(R.id.currentPass);

        btnCofirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordToCheck = oldPass.getText().toString();

                // Memeriksa apakah input password kosong atau tidak
                if (passwordToCheck.isEmpty()) {
                    // Jika kosong, tampilkan pesan kesalahan
                    MyToast.showToastError(ConfirmOldPass.this, "Password tidak boleh kosong");
                } else {
                    // Jika tidak kosong, lanjutkan dengan proses permintaan HTTP POST menggunakan Volley
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
                    String userId = sharedPreferences.getString("idUser", "");

                    // Buat permintaan HTTP POST menggunakan Volley
                    String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/EditDataApi/checkPassword/" + userId;

                    // Persiapan untuk memulai permintaan
                    RequestQueue queue = Volley.newRequestQueue(ConfirmOldPass.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("Hasil Input", passwordToCheck);
                                        Log.d("ConfirmOldPass", "Respons dari server: " + response); // Letakkan log di sini
                                        JSONObject jsonObject = new JSONObject(response);
                                        int code = jsonObject.getInt("code");
                                        String status = jsonObject.getString("status");

                                        if (code == 200) {
                                            MyToast.showToastSuccess(ConfirmOldPass.this, "Password Benar");
                                            Intent i = new Intent(ConfirmOldPass.this, DetailGantiPassword.class);
                                            startActivity(i);
                                        } else {
                                            MyToast.showToastError(ConfirmOldPass.this, "Password Salah");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Tangani kesalahan saat melakukan permintaan
                                    error.printStackTrace();
                                    MyToast.showToastError(ConfirmOldPass.this, "Terjadi kesalahan. Periksa koneksi internet Anda.");
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("password", passwordToCheck); // Sertakan kata sandi yang akan diverifikasi
                            return params;
                        }
                    };

                    // Tambahkan permintaan ke queue
                    queue.add(stringRequest);
                }
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finish();
    }


}