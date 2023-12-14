package com.example.j_kost.DetailActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.Activity.MainActivity;
import com.example.j_kost.R;
import com.example.j_kost.Session.SessionManager;
import com.example.j_kost.Utils.ImageBase64Converter;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.MyToast;
import com.example.j_kost.Utils.NetworkUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DetailEditProfile extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    Button Btnedit, BtnUbahProfile;
    ImageView btnBack, profilePhoto;
    EditText nama, noHp, jenisKelamin, tglLahir, alamat;
    RadioButton radioBtnMale, radioBtnFemale;
//    private String selectedImageUri = "";
    private String previousUserPhoto = "";
    private Uri selectedImageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit_profile);

        nama = findViewById(R.id.editNama);
        noHp = findViewById(R.id.editNotelp);
        InputFilter filter = new InputFilter.LengthFilter(12);
        noHp.setFilters(new InputFilter[]{filter});

        alamat = findViewById(R.id.editAlamat);
        radioBtnMale = findViewById(R.id.radioButtonLakiLaki);
        radioBtnFemale = findViewById(R.id.radioButtonPerempuan);
        tglLahir = findViewById(R.id.editTglLahir);

        Btnedit = findViewById(R.id.btnEdit);
        BtnUbahProfile = findViewById(R.id.btnUbah);
        btnBack = findViewById(R.id.btnBack);
        profilePhoto = findViewById(R.id.profile);

        tglLahir.setFocusable(false); // Mencegah keyboard muncul saat editText di-klik
        tglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ambil teks dari EditText tglLahir
                String tanggalLahir = tglLahir.getText().toString();

                // Parse tahun, bulan, dan hari dari teks
                String[] parts = tanggalLahir.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]) - 1; // Bulan dimulai dari 0 (0 = Januari, 1 = Februari, ...)
                int dayOfMonth = Integer.parseInt(parts[2]);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailEditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                        tglLahir.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

                // Menampilkan date picker dialog
                datePickerDialog.show();
            }
        });


        sharedPreferences = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", "");
        getDataUser(idUser);

        BtnUbahProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(DetailEditProfile.this)
                        .cropSquare()
                        .start();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelConfirmation();
            }
        });

        Btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check internet connectivity
                if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
                    // Ambil nilai dari EditTexts
                    String enama = nama.getText().toString().trim();
                    String enotelp = noHp.getText().toString().trim();
                    String etglLahir = tglLahir.getText().toString().trim();
                    String ealamat = alamat.getText().toString().trim();

                    if (enama.isEmpty() || enotelp.isEmpty() || etglLahir.isEmpty() || ealamat.isEmpty()) {
                        // Jika ada yang kosong, tampilkan pesan kesalahan
                        MyPopUp.showErrorDialog(DetailEditProfile.this, "Gagal Update Data!", "Pastikan semua field sudah harus terisi", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();
                            }
                        });
                    } else {
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
                        String userId = sharedPreferences.getString("idUser", "");

                        updateUserData(userId);
                    }
                } else {
                    // Show a pop-up alert indicating no internet connection
                    MyPopUp.showAlertDialog(DetailEditProfile.this, "Tidak terkoneksi internet",
                            "Silahkan cek internet dan coba lagi.", new OnDialogButtonClickListener() {
                                @Override
                                public void onDismissClicked(Dialog dialog) {
                                    super.onDismissClicked(dialog);
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            if (uri != null) {
                // Assign the selected image URI to the class-level variable
                selectedImageUri = uri;

                // Display image using Picasso
                Picasso.get().load(uri).into(profilePhoto);

                Log.d("Test Img Picker", selectedImageUri.toString());
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showCancelConfirmation();
    }

    private void showCancelConfirmation() {
        MyPopUp.showConfirmDialog(DetailEditProfile.this, "Konfirmasi!", "Apakah anda yakin ingin membatalkan perubahan", new OnDialogButtonClickListener() {
            @Override
            public void onPositiveClicked(Dialog dialog) {
                super.onPositiveClicked(dialog);
                dialog.dismiss();
                finish();
            }

            @Override
            public void onNegativeClicked(Dialog dialog) {
                super.onNegativeClicked(dialog);
                dialog.dismiss();
            }
        });
    }


    private void getDataUser(String userId){
        String apiUrl = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/GetDataMobile/getUserData/" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            String status = jsonObject.getString("status");

                            if (code == 200 && status.equals("success")) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                String userName = dataObject.getString("Nama Penghuni");
                                String userNumber = dataObject.getString("Notelp User");
                                String userAddress = dataObject.getString("Alamat User");
                                String userGender = dataObject.getString("Jenis Kelamin");
                                String userBirth = dataObject.getString("Tanggal Lahir");
                                String photoPath = dataObject.getString("foto_user");

                                nama.setText(userName);
                                noHp.setText(userNumber);
                                alamat.setText(userAddress);
                                tglLahir.setText(userBirth);

                                if (userGender.equals("Laki-laki")) {
                                    radioBtnMale.setChecked(true);
                                } else if (userGender.equals("Perempuan")) {
                                    radioBtnFemale.setChecked(true);
                                }

                                if (!photoPath.isEmpty()) {
                                    loadImageToImageView(photoPath, profilePhoto);
                                    previousUserPhoto = photoPath;

                                    String getPhotoPath = photoPath.toString();
                                    Log.d("Get img path", getPhotoPath);
                                } else {
                                    profilePhoto.setImageResource(R.drawable.default_profilepng);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyPopUp.showErrorDialog(DetailEditProfile.this, "Gagal menampilkan data", "Silahkan cek kembali koneksi anda", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();
                                finish();
                            }
                        });
                    }
                });

        // Tambahkan request ke queue Volley
        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void loadImageToImageView(String imageUrl, ImageView imageView) {
        String fullImageUrl = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/foto/" + imageUrl;
        Picasso.get().load(fullImageUrl).into(imageView);
    }

    private void updateUserData(String userId) {
        if (!userId.isEmpty()) {
            String apiUrl = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/EditDataApi/editUser/" + userId;

            String userName = nama.getText().toString().trim();
            String userNumber = noHp.getText().toString().trim();
            String userAddress = alamat.getText().toString().trim();
            String userGender = radioBtnMale.isChecked() ? "Laki-laki" : "Perempuan";
            String userBirth = tglLahir.getText().toString().trim();

            // Simpan URL foto sebelumnya
            String previousPhoto = previousUserPhoto;

            // Jika foto baru dipilih, perbarui URL foto baru
//            if (selectedImageUri != null && !selectedImageUri.isEmpty()) {
//                previousPhoto = selectedImageUri;
//            }

            if (selectedImageUri != null) {
                try {
                    // Ubah gambar yang dipilih menjadi base64
                    InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    String base64Image = ImageBase64Converter.convert(imageStream);

                    // Simpan gambar dalam format base64
                    previousPhoto = base64Image;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String finalUserPhoto = previousPhoto;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String responseMessage = "Data berhasil diubah";

                            if (!response.isEmpty()) {
                                responseMessage = response;
                            }

                            // Tampilkan dialog sukses
                            MyPopUp.showSuccessDialog(DetailEditProfile.this, "Sukses", "Data berhasil di edit", new OnDialogButtonClickListener() {
                                @Override
                                public void onDismissClicked(Dialog dialog) {
                                    super.onDismissClicked(dialog);
                                    dialog.dismiss();

                                    // Simpan perubahan ke SharedPreferences setelah berhasil mengubah data
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("namaLengkap", userName);
                                    editor.putString("noHp", userNumber);
                                    editor.putString("alamatUser", userAddress);
                                    editor.putString("jenisKelamin", userGender);
                                    editor.putString("tglLahir", userBirth);
                                    editor.putString("fotoUser", finalUserPhoto);
                                    editor.apply();

                                    SessionManager.fetchDataAndUpdateSession(DetailEditProfile.this, userId);
                                    finish();
                                }
                            });
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Tampilkan pesan kesalahan
                            MyPopUp.showErrorDialog(DetailEditProfile.this, "Error", "Data gagal di edit", new OnDialogButtonClickListener() {
                                @Override
                                public void onDismissClicked(Dialog dialog) {
                                    super.onDismissClicked(dialog);
                                    dialog.dismiss();
                                }
                            });
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("nama_lengkap", userName);
                    params.put("no_hp", userNumber);
                    params.put("alamat", userAddress);
                    params.put("jenis_kelamin", userGender);
                    params.put("tggl_lahir", userBirth);
                    params.put("id_user", userId);
                    params.put("foto_user", finalUserPhoto);

                    return params;
                }
            };

            Volley.newRequestQueue(this).add(stringRequest);
        } else {
            // Jika ID kosong, tampilkan pesan kesalahan
            MyToast.showToastError(DetailEditProfile.this, "Id tidak ditemukan");
        }
    }
}