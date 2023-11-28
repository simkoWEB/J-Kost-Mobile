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
                String newPassword = newPass.getText().toString().trim();
                String confirmNewPassword = confirmNewPass.getText().toString().trim();

                if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                    MyToast.showToastWarning(DetailGantiPassword.this, "Data tidak boleh kosong");
                } else {
                    if (newPassword.equals(confirmNewPassword)) {
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
                        String userId = sharedPreferences.getString("idUser", "");


                        String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/EditDataApi/editPass/" + userId;

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Handle respons dari server setelah berhasil mengubah password
                                        MyToast.showToastSuccess(DetailGantiPassword.this, "Password berhasil diubah");
                                        // Kembali ke MainActivity (Activity yang meng-host ProfileFragment)
                                        Intent intent = new Intent(DetailGantiPassword.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Hapus semua activity di atasnya
                                        startActivity(intent);
                                        finish();
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

                    } else {
                        // Jika tidak sama, tampilkan pesan kesalahan bahwa password tidak cocok
                        MyToast.showToastError(DetailGantiPassword.this, "Password tidak sama");
                    }
                }

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

}