package com.example.j_kost.Tab;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.DetailActivity.AboutApps;
import com.example.j_kost.DetailActivity.ConfirmOldPass;
import com.example.j_kost.DetailActivity.DetailEditProfile;
import com.example.j_kost.Activity.LoginActivity;
import com.example.j_kost.DetailActivity.DetailGantiPassword;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.NetworkUtils;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class TabProfilUser extends Fragment {
    TextView nama, email, notelp;
    Button btnDetailProfile, btnLogout, btnChangePass, btnAbout;
    ImageView profilePhoto;
    SharedPreferences sharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab_profil_user, container, false);

        nama = view.findViewById(R.id.tvName);
        email = view.findViewById(R.id.tvEmail);
        notelp = view.findViewById(R.id.tvNumber);
        profilePhoto = view.findViewById(R.id.profile);

        btnDetailProfile = view.findViewById(R.id.btnEdit);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnChangePass = view.findViewById(R.id.btnGantiPass);
        btnAbout = view.findViewById(R.id.btnAbout);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", "");
        getDataUser(idUser);

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AboutApps.class);
                startActivity(i);
            }
        });

        btnDetailProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DetailEditProfile.class);
                startActivity(i);
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ConfirmOldPass.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPopUp.showConfirmDialog(getContext(), "Konfirmasi", "Apakah anda yakin ingin log out?", new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                        logoutUser();
                    }

                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                        dialog.dismiss();
                    }
                });
            }
        });


        return view;
    }


    private void logoutUser() {
        // Lakukan proses logout di sini, misalnya dengan menghapus data dari SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Menghapus semua data pengguna dari SharedPreferences
        editor.apply();

        // Redirect ke halaman login setelah logout
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish(); // Tutup aktivitas saat ini setelah logout
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
                                String userEmail = dataObject.getString("email");
                                String userTelp = dataObject.getString("Notelp User");
                                String photoPath = dataObject.getString("foto_user");

                                nama.setText(userName);
                                email.setText(userEmail);
                                notelp.setText(userTelp);

                                if (!photoPath.isEmpty()) {
                                    loadImageToImageView(photoPath, profilePhoto);
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
                        // Handle error jika ada ketika mengambil data dari server
                    }
                });

        // Tambahkan request ke queue Volley
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }


    private void loadImageToImageView(String imageUrl, ImageView imageView) {
        String fullImageUrl = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/foto/" + imageUrl;
        Picasso.get().load(fullImageUrl).into(imageView);
    }
}