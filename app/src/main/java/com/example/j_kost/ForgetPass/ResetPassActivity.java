package com.example.j_kost.ForgetPass;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.Activity.LoginActivity;
import com.example.j_kost.Activity.MainActivity;
import com.example.j_kost.DetailActivity.DetailGantiPassword;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.MyToast;
import com.example.j_kost.Utils.NetworkUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.HashMap;
import java.util.Map;

public class ResetPassActivity extends AppCompatActivity {

    Button resetPassBtn;
    ImageView backBtn;
    TextInputEditText newPass, confirmNewPass;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        userId = getIntent().getStringExtra("userId");
        Log.d("id Intent", userId);

        newPass = findViewById(R.id.etNewPass);
        confirmNewPass = findViewById(R.id.etConfirmPass);
        resetPassBtn = findViewById(R.id.resetBtn);
        backBtn = findViewById(R.id.btnBack);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPopUp.showConfirmDialog(ResetPassActivity.this, "Konfirmasi", "Apakah yakin ingin membatalkan ubah passowrd", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                        Intent i = new Intent(ResetPassActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                        dialog.dismiss();
                    }
                });
            }
        });

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            handleEditPass();
            }
        });

    }

    private void handleEditPass() {
        String newPassword = newPass.getText().toString().trim();
        String confirmNewPassword = confirmNewPass.getText().toString().trim();

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            MyToast.showToastWarning(ResetPassActivity.this, "Data tidak boleh kosong");
        } else if (!isValidPassword(newPassword)) {
            MyToast.showToastError(ResetPassActivity.this, "Password harus terdiri dari minimal 8 karakter dan mengandung kombinasi angka dan huruf");
        } else if (!newPassword.equals(confirmNewPassword)) {
            MyToast.showToastError(ResetPassActivity.this, "Password tidak sama");
        } else {
            // Semua validasi terpenuhi, kirim permintaan ke server
            sendPasswordToServer(newPassword);
        }
    }

    private void sendPasswordToServer(String newPassword) {
        String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/EditDataApi/editPass/" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyPopUp.showSuccessDialog(ResetPassActivity.this, "Sukses", "Password berhasil direset", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();

                                Intent intent = new Intent(ResetPassActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle jika terjadi error pada permintaan ke server
                        MyToast.showToastError(ResetPassActivity.this, "Gagal mengubah password");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Masukkan parameter ke dalam body permintaan POST
                Map<String, String> params = new HashMap<>();
                params.put("password", newPassword);
                return params;
            }
        };

        // Tambahkan request ke dalam queue Volley
        Volley.newRequestQueue(ResetPassActivity.this).add(stringRequest);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[a-zA-Z].*");
    }
}