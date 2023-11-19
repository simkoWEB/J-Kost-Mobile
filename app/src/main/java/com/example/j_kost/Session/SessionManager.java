package com.example.j_kost.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String USER_DATA = "userData";
    private static final String IS_LOGGED_IN = "isLoggedIn";

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
            String alamatKost
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

        editor.putBoolean(IS_LOGGED_IN, true); // Set logged-in state to true
        editor.apply();
    }

    public static void logoutUser(Context context) {
        SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();

        editor.clear(); // Clear all stored data
        editor.putBoolean(IS_LOGGED_IN, false); // Set logged-in state to false
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences userPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        return userPref.getBoolean(IS_LOGGED_IN, false); // Check logged-in state
    }
}
