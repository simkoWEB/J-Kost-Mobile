package com.example.j_kost.Session;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.j_kost.Activity.LoginActivity;

public class SessionManager {
    private static final String USER_DATA = "userData";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String SESSION_EXPIRY = "sessionExpiry"; // Tambahkan konstanta untuk waktu kedaluwarsa

    public static String getHargaBulanan(Context context) {
        SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        return userPref.getString("hargaBulanan", "-");
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
            String fasilitasKamar,
            String kategoriKost,
            String ukuranKamar,
            String namaKost,
            String alamatKost,
            String hargaBulanan
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
        editor.putString("fasilitasKamar", fasilitasKamar);
        editor.putString("kategoriKost", kategoriKost);
        editor.putString("ukuranKamar", ukuranKamar);
        editor.putString("namaKost", namaKost);
        editor.putString("alamatKost", alamatKost);
        editor.putString("hargaBulanan", hargaBulanan);

        // Tambahkan waktu kedaluwarsa 1 hari dari sekarang
//        long expiryTimeMillis = System.currentTimeMillis() + (1 * 24 * 60 * 60 * 1000); // 1 hari dalam milidetik
        long expiryTimeMillis = System.currentTimeMillis() + (1 * 60 * 1000); // 1 menit dalam milidetik
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

        showSessionExpiredDialog(context);
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        boolean isLoggedIn = userPref.getBoolean(IS_LOGGED_IN, false);
        long expiryTime = userPref.getLong(SESSION_EXPIRY, 0);

        // Periksa apakah sesi masih berlaku
        return isLoggedIn && expiryTime >= System.currentTimeMillis();
    }

    private static void showSessionExpiredDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sesi Berakhir!");
        builder.setMessage("Sesi Anda telah berakhir. Silakan login kembali.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Redirect ke layar login setelah dialog OK ditekan
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
