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
    private static final String SESSION_EXPIRY = "sessionExpiry"; // Tambahkan konstanta untuk waktu kedaluwarsa

    public static int getHargaBulanan(Context context) {
        SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        return userPref.getInt("hargaBulanan",0);
    }

    public static void loginUser(
            Context context,
            String idUser,
            String namaLengkap,
            String email,
            String password,
            String alamatUser,
            String noHp,
            String jenisKelamin,
            String tglLahir,
            String fotoUser,
            String nomorKamar,
            String fasilitasKost,
            String peraturanKost,
            String ukuranKamar,
            String namaKost,
            String alamatKost,
            int hargaBulanan
    ) {
        SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();

        editor.putString("idUser", idUser);
        editor.putString("namaLengkap", namaLengkap);
        editor.putString("emailUser", email);
        editor.putString("passwordUser", password);
        editor.putString("alamatUser", alamatUser);
        editor.putString("noHp", noHp);
        editor.putString("jenisKelamin", jenisKelamin);
        editor.putString("tglLahir", tglLahir);
        editor.putString("fotoUser", fotoUser);
        editor.putString("nomorKamar", nomorKamar);
        editor.putString("fasilitasKost", fasilitasKost);
        editor.putString("peraturanKost", peraturanKost);
        editor.putString("ukuranKamar", ukuranKamar);
        editor.putString("namaKost", namaKost);
        editor.putString("alamatKost", alamatKost);
        editor.putInt("hargaBulanan", hargaBulanan);

        // Tambahkan waktu kedaluwarsa 1 hari dari sekarang
        long expiryTimeMillis = System.currentTimeMillis() + (1 * 24 * 60 * 60 * 1000); // 1 hari dalam milidetik
//        long expiryTimeMillis = System.currentTimeMillis() + (1 * 60 * 1000); // 1 menit dalam milidetik
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
        String apiUrl = "http://"+ NetworkUtils.BASE_URL +"/PHP-MVC/public/GetDataMobile/getUserData/" + userId;

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

                                // Mendapatkan nilai dari setiap kunci di dalam objek data
                                String idUser = dataObject.getString("id_user");
                                String namaPenghuni = dataObject.getString("Nama Penghuni");
                                String email = dataObject.getString("email");
                                String password = dataObject.getString("password");
                                String alamatUser = dataObject.getString("Alamat User");
                                String notelpUser = dataObject.getString("Notelp User");
                                String jk = dataObject.getString("Jenis Kelamin");
                                String tglLahir = dataObject.getString("Tanggal Lahir");
                                String fotoUser = dataObject.getString("foto_user");
                                String nomorKamar = dataObject.getString("Nomor Kamar");
                                String fasilitas = dataObject.getString("fasilitas_kost");
                                String peraturan = dataObject.getString("peraturan_kost");
                                String ukuranKamar = dataObject.getString("Ukuran Kamar");
                                String namaKost = dataObject.getString("nama_kost");
                                String alamatKost = dataObject.getString("Alamat Kost");
                                int hargaBulanan = dataObject.getInt("harga_bulanan");


                                loginUser(context, idUser, namaPenghuni, email, password, alamatUser, notelpUser,
                                        jk, tglLahir, fotoUser, nomorKamar, fasilitas, peraturan, ukuranKamar, namaKost,
                                        alamatKost, hargaBulanan);

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
