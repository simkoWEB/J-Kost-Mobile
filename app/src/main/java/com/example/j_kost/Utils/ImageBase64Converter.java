package com.example.j_kost.Utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageBase64Converter {
    public static String convertImageToBase64(Uri imageUri, ContentResolver contentResolver) {
        try (InputStream inputStream = contentResolver.openInputStream(imageUri)) {
            if (inputStream != null) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                byte[] bytes = output.toByteArray();
                return Base64.encodeToString(bytes, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

