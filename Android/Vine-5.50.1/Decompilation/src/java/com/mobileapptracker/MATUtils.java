package com.mobileapptracker;

import android.content.Context;
import com.flurry.android.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class MATUtils {
    public static synchronized void saveToSharedPreferences(Context context, String prefsKey, String prefsValue) {
        context.getSharedPreferences("com.mobileapptracking", 0).edit().putString(prefsKey, prefsValue).commit();
    }

    public static synchronized void saveToSharedPreferences(Context context, String prefsKey, boolean prefsValue) {
        context.getSharedPreferences("com.mobileapptracking", 0).edit().putBoolean(prefsKey, prefsValue).commit();
    }

    public static synchronized String getStringFromSharedPreferences(Context context, String prefsKey) {
        String string;
        try {
            string = context.getSharedPreferences("com.mobileapptracking", 0).getString(prefsKey, "");
        } catch (ClassCastException e) {
            string = "";
        }
        return string;
    }

    public static synchronized boolean getBooleanFromSharedPreferences(Context context, String prefsKey) {
        boolean z = false;
        synchronized (MATUtils.class) {
            try {
                z = context.getSharedPreferences("com.mobileapptracking", 0).getBoolean(prefsKey, false);
            } catch (ClassCastException e) {
            }
        }
        return z;
    }

    public static String readStream(InputStream stream) throws IOException {
        if (stream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    builder.append(line).append("\n");
                } else {
                    reader.close();
                    return builder.toString();
                }
            }
        } else {
            return "";
        }
    }

    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }
        int len = data.length;
        String str = "";
        for (int i = 0; i < len; i++) {
            if ((data[i] & Constants.UNKNOWN) < 16) {
                str = String.valueOf(str) + "0" + Integer.toHexString(data[i] & Constants.UNKNOWN);
            } else {
                str = String.valueOf(str) + Integer.toHexString(data[i] & Constants.UNKNOWN);
            }
        }
        return str;
    }

    public static byte[] hexToBytes(String str) {
        byte[] buffer = null;
        if (str != null && str.length() >= 2) {
            int len = str.length() / 2;
            buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, (i * 2) + 2), 16);
            }
        }
        return buffer;
    }

    public static String md5(String s) throws NoSuchAlgorithmException {
        if (s == null) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            return bytesToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sha1(String s) throws NoSuchAlgorithmException {
        if (s == null) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            return bytesToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sha256(String s) throws NoSuchAlgorithmException {
        if (s == null) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            return bytesToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
