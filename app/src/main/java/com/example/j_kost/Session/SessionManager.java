package com.example.j_kost.Session;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.Activity.LoginActivity;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.NetworkUtils;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    private static final String USER_DATA = "userData";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String SESSION_EXPIRY = "sessionExpiry"; // konstanta untuk waktu kedaluwarsa

    public static int getHargaBulanan(Context context) {
        SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        return userPref.getInt("hargaBulanan",0);
    }

    public static void loginUser(
            Context context,
            String idUser,
            String email,
            String password,
            String nomorKamar,
            int hargaBulanan
    ) {
        SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();

        editor.putString("idUser", idUser);
        editor.putString("emailUser", email);
        editor.putString("passwordUser", password);
        editor.putString("nomorKamar", nomorKamar);
        editor.putInt("hargaBulanan", hargaBulanan);

        // Tambahkan waktu kedaluwarsa 1 hari dari sekarang
        long expiryTimeMillis = System.currentTimeMillis() + (1 * 24 * 60 * 60 * 1000); // 1 hari dalam milidetik
        editor.putLong(SESSION_EXPIRY, expiryTimeMillis);

        editor.putBoolean(IS_LOGGED_IN, true); // Set logged-in state to true
        editor.apply();
    }


    public static void logoutUser(Context context) {
        SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();

        editor.clear(); // Clear all stored data
        editor.putBoolean(IS_LOGGED_IN, false); // Set logged-in state to false
        editor.apply();
        showPopUpLogout(context);
    }

    private static void showPopUpLogout(Context context){
        MyPopUp.showConfirmDialog(context, "Konfirmasi", "Apakah anda yakin ingin log out?", new OnDialogButtonClickListener() {
            @Override
            public void onPositiveClicked(Dialog dialog) {
                super.onPositiveClicked(dialog);
                redirectToLogin(context);
            }

            @Override
            public void onNegativeClicked(Dialog dialog) {
                super.onNegativeClicked(dialog);
                dialog.dismiss();
            }
        });
    }

    private static void redirectToLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        boolean isLoggedIn = userPref.getBoolean(IS_LOGGED_IN, false);
        long expiryTime = userPref.getLong(SESSION_EXPIRY, 0);

        // Periksa apakah sesi masih berlaku
        return isLoggedIn && expiryTime >= System.currentTimeMillis();
    }

    public static void fetchDataAndUpdateSession(Context context, String userId) {
        String apiUrl = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/GetDataMobile/getUserData/" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing response dari server
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            String status = jsonObject.getString("status");

                            if (code == 200 && status.equals("success")) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                // Mendapatkan nilai dari setiap kunci yang diperlukan dari objek data
                                String idUser = dataObject.getString("id_user");
                                String email = dataObject.getString("email");
                                String password = dataObject.getString("password");
                                String nomorKamar = dataObject.getString("Nomor Kamar");
                                int hargaBulanan = dataObject.getInt("harga_bulanan");

                                // Simpan nilai-nilai yang diperoleh dalam sesi
                                SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = userPref.edit();

                                editor.putString("idUser", idUser);
                                editor.putString("emailUser", email);
                                editor.putString("passwordUser", password);
                                editor.putString("nomorKamar", nomorKamar);
                                editor.putInt("hargaBulanan", hargaBulanan);

                                // Tambahkan waktu kedaluwarsa 1 hari dari sekarang
                                long expiryTimeMillis = System.currentTimeMillis() + (1 * 24 * 60 * 60 * 1000); // 1 hari dalam milidetik
                                editor.putLong(SESSION_EXPIRY, expiryTimeMillis);

                                editor.putBoolean(IS_LOGGED_IN, true); // Set logged-in state to true
                                editor.apply();

                                // Pastikan untuk menyimpan data terbaru dalam sesi
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle kesalahan jika ada ketika mengambil data dari server
                    }
                });

        // Tambahkan request ke queue Volley
        Volley.newRequestQueue(context).add(stringRequest);
    }



}
