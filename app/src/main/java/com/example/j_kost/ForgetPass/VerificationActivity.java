package com.example.j_kost.ForgetPass;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.example.j_kost.Activity.LoginActivity;
import com.example.j_kost.ForgetPass.OTP.EmailSender;
import com.example.j_kost.ForgetPass.OTP.OTPGenerator;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.MyToast;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.Locale;

public class VerificationActivity extends AppCompatActivity {
    Button btnContinue;
    ImageView backBtn;
    PinView pinView;
    TextView otp;
    private String kodeOtp;

    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        kodeOtp = getIntent().getStringExtra("otp");

        btnContinue = findViewById(R.id.btnSelanjutnya);
        backBtn = findViewById(R.id.btnBack);
        pinView = findViewById(R.id.firstPinView);
        timerTextView = findViewById(R.id.timerTextview);
//        otp = findViewById(R.id.tvOtp);

        pinView.setAnimationEnable(true);
        pinView.isCursorVisible();

//        otp.setText(kodeOtp);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = getIntent().getStringExtra("userId");
                String userInputOTP = pinView.getText().toString();

                if (userInputOTP.isEmpty()) {
                    MyToast.showToastError(VerificationActivity.this, "Masukkan kode OTP");
                } else {

                    if (userInputOTP.equals(kodeOtp)) {
                        MyToast.showToastSuccess(VerificationActivity.this, "Kode OTP valid");
                        Intent i = new Intent(VerificationActivity.this, ResetPassActivity.class);
                        i.putExtra("userId", userId);
                        startActivity(i);
                    } else {
                        MyToast.showToastError(VerificationActivity.this, "Kode OTP tidak valid");
                    }

                }
            }
        });

        long timerDuration = 30000;
        countDownTimer = new CountDownTimer(timerDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update tampilan timer setiap detik
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timerTextView.setText(timeLeftFormatted);
            }

            public void onFinish() {
                // Aksi yang ingin dilakukan saat timer selesai
                timerTextView.setText("Waktu Habis!");
                MyPopUp.showAlertDialog(VerificationActivity.this, "Waktu Habis", "Siliahkan ulangi kembali proses lupa password, karena kode telah kadaluarsa", new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                        dialog.dismiss();
                        navigateToLoginActivity();
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hentikan timer jika activity dihancurkan
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Membersihkan tumpukan Activity
        startActivity(intent);
        finish(); // Menutup Activity saat ini agar pengguna tidak bisa kembali ke VerificationActivity dari tombol back
    }
}
