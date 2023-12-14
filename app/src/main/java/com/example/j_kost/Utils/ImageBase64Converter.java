package com.example.j_kost.Utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageBase64Converter {
    public static String convert(InputStream inputStream) throws IOException {
        // Ubah InputStream menjadi Bitmap
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();

        // Konversi Bitmap menjadi array byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // Konversi array byte menjadi format Base64
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}

