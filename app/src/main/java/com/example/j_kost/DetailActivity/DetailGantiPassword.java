package com.example.j_kost.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.Activity.MainActivity;
import com.example.j_kost.Fragment.ProfileFragment;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.MyToast;
import com.example.j_kost.Utils.NetworkUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.HashMap;
import java.util.Map;

public class DetailGantiPassword extends AppCompatActivity {
    ImageView btnBack;
    Button btnSimpan;
    TextInputEditText newPass, confirmNewPass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ganti_password);

        btnBack = findViewById(R.id.btnBack);
        newPass = findViewById(R.id.newPass);
        confirmNewPass = findViewById(R.id.confirmNewPass);
        btnSimpan = findViewById(R.id.btnSave);

        newPass.requestFocus();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelConfirm();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            handleEditPass();
            }
        });
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showCancelConfirm();
    }

    private void showCancelConfirm(){
        MyPopUp.showConfirmDialog(DetailGantiPassword.this, "Konfirmasi!", "Apakah anda yakin ingin membatalkan ganti passwor?", new OnDialogButtonClickListener() {
            @Override
            public void onPositiveClicked(Dialog dialog) {
                super.onPositiveClicked(dialog);
                dialog.dismiss();
                // Kembali ke MainActivity (Activity yang meng-host ProfileFragment)
                Intent intent = new Intent(DetailGantiPassword.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Hapus semua activity di atasnya
                startActivity(intent);
                finish();
            }

            @Override
            public void onNegativeClicked(Dialog dialog) {
                super.onNegativeClicked(dialog);
                dialog.dismiss();
            }
        });
    }

    private void handleEditPass() {
        String newPassword = newPass.getText().toString().trim();
        String confirmNewPassword = confirmNewPass.getText().toString().trim();

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            MyToast.showToastWarning(DetailGantiPassword.this, "Data tidak boleh kosong");
        } else if (!isValidPassword(newPassword)) {
            MyToast.showToastError(DetailGantiPassword.this, "Password harus terdiri dari minimal 8 karakter dan mengandung kombinasi angka dan huruf");
        } else if (!newPassword.equals(confirmNewPassword)) {
            MyToast.showToastError(DetailGantiPassword.this, "Password tidak sama");
        } else {
            // Semua validasi terpenuhi, kirim permintaan ke server
            sendPasswordToServer(newPassword);
        }
    }

    private void sendPasswordToServer(String newPassword) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("idUser", "");

        String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/EditDataApi/editPass/" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyPopUp.showSuccessDialog(DetailGantiPassword.this, "Sukses", "Password berhasil diubah", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();

                                Intent intent = new Intent(DetailGantiPassword.this, MainActivity.class);
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
                        MyToast.showToastError(DetailGantiPassword.this, "Gagal mengubah password");
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
        Volley.newRequestQueue(DetailGantiPassword.this).add(stringRequest);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[a-zA-Z].*");
    }



}