package com.example.j_kost.forget_pass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.j_kost.LoginActivity;
import com.example.j_kost.R;

public class VerificationActivity extends AppCompatActivity {

    private EditText et1, et2, et3, et4;
    Button continueBtn;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        setupEditTextListeners();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VerificationActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        continueBtn = findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VerificationActivity.this, ResetPassActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void setupEditTextListeners() {
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 1) {
                    // A character was deleted, move focus to the previous EditText (et1).
                    et1.requestFocus();
                } else if (s.length() > 1) {
                    et1.setText(s.subSequence(0, 1));
                    et1.setSelection(1);
                    et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 1) {
                    et1.requestFocus();
                } else if (s.length() > 1) {
                    et2.setText(s.subSequence(0, 1));
                    et2.setSelection(1);
                    et3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 1) {
                    et2.requestFocus();
                } else if (s.length() > 1) {
                    et3.setText(s.subSequence(0, 1));
                    et3.setSelection(1);
                    et4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 1) {
                    et3.requestFocus();
                } else if (s.length() > 1) {
                    et4.setText(s.subSequence(0, 1));
                    et4.setSelection(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}
