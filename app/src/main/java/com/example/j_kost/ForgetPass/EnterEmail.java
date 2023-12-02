package com.example.j_kost.ForgetPass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.se.omapi.Session;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.Activity.LoginActivity;
import com.example.j_kost.DetailActivity.ConfirmOldPass;
import com.example.j_kost.DetailActivity.DetailGantiPassword;
import com.example.j_kost.ForgetPass.OTP.EmailSender;
import com.example.j_kost.ForgetPass.OTP.OTPGenerator;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.MyToast;
import com.example.j_kost.Utils.NetworkUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnterEmail extends AppCompatActivity {

    TextInputEditText etEmail;
    Button btnConfirm;
    ProgressDialog progressDialog;
    String EmailRecipient = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_emal);

        etEmail = findViewById(R.id.tvEmail);
        btnConfirm = findViewById(R.id.confirmBtn);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Objects.requireNonNull(etEmail.getText()).toString();


                if (email.isEmpty()) {
//                    showToast("Harap isi semua field!");
                    MyToast.showToastWarning(EnterEmail.this,"Harap isi field!");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                    showToast("Harap masukkan email yang valid!");
                    MyToast.showToastWarning(EnterEmail.this,"Harap masukkan email yang valid!");
                } else {
//                     Lakukan permintaan login ke API menggunakan Volley
                    checkEmail(email);
                }
            }
        });


    }

    private void checkEmail(String email){
        // Buat permintaan HTTP POST menggunakan Volley
        String url = "http://"+NetworkUtils.BASE_URL+"/PHP-MVC/public/EditDataApi/checkEmail";

        // Persiapan untuk memulai permintaan
        RequestQueue queue = Volley.newRequestQueue(EnterEmail.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            String status = jsonObject.getString("status");

                            if (code == 200) {
                                JSONObject userData = jsonObject.getJSONObject("user_data");
                                String userId = userData.getString("id_user");
                                String nama = userData.getString("nama_lengkap");

                                OTPGenerator otpGenerator = new OTPGenerator();
                                String generatedOTP = otpGenerator.generateOTP(4);

                                EmailSender.sendEmail(email, nama, generatedOTP);

                                Intent i = new Intent(EnterEmail.this, VerificationActivity.class);
                                i.putExtra("userId", userId);
                                i.putExtra("otp", generatedOTP);
                                startActivity(i);
                                finish();

                                MyToast.showToastSuccess(EnterEmail.this, "Email berhasil dikirim");

                            } else {
                                MyToast.showToastError(EnterEmail.this, "Email tidak valid");
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
                        MyToast.showToastError(EnterEmail.this, "Terjadi kesalahan. Periksa koneksi internet Anda.");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email); // Sertakan kata sandi yang akan diverifikasi
                return params;
            }
        };

        // Tambahkan permintaan ke queue
        queue.add(stringRequest);
    }
}