package com.flurry.sdk;

import android.content.Intent;
import android.text.TextUtils;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class fb {
    private static final String a = fb.class.getSimpleName();

    public static String a(String str) {
        return a(str, 255);
    }

    public static String a(String str, int i) {
        if (str == null) {
            return "";
        }
        return str.length() > i ? str.substring(0, i) : str;
    }

    public static String b(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            el.a(5, a, "Cannot encode '" + str + "'");
            return "";
        }
    }

    public static String c(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            el.a(5, a, "Cannot decode '" + str + "'");
            return "";
        }
    }

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    public static byte[] d(String str) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(str.getBytes(), 0, str.length());
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            el.a(6, a, "Unsupported SHA1: " + e.getMessage());
            return null;
        }
    }

    public static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (byte b : bArr) {
            sb.append(cArr[(byte) ((b & 240) >> 4)]);
            sb.append(cArr[(byte) (b & 15)]);
        }
        return sb.toString();
    }

    public static boolean a(long j) {
        if (j != 0 && System.currentTimeMillis() > j) {
            return false;
        }
        return true;
    }

    public static boolean a(Intent intent) {
        return dl.a().c().queryIntentActivities(intent, 65536).size() > 0;
    }

    public static boolean b(Intent intent) {
        if (intent == null) {
            return false;
        }
        return dl.a().b().getPackageName().equals(intent.resolveActivity(dl.a().c()).getPackageName());
    }

    public static String e(String str) {
        return str.replace("'", "\\'").replace("\\n", "").replace("\\r", "").replace("\\t", "");
    }

    public static Map<String, String> f(String str) {
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(str)) {
            for (String str2 : str.split("&")) {
                String[] strArrSplit = str2.split("=");
                if (!strArrSplit[0].equals("event")) {
                    map.put(c(strArrSplit[0]), c(strArrSplit[1]));
                }
            }
        }
        return map;
    }

    public static long g(String str) {
        if (str == null) {
            return 0L;
        }
        long j = 1125899906842597L;
        int i = 0;
        while (i < str.length()) {
            long jCharAt = str.charAt(i) + (j * 31);
            i++;
            j = jCharAt;
        }
        return j;
    }

    public static long a(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        long j = 0;
        while (true) {
            int i = inputStream.read(bArr);
            if (i >= 0) {
                outputStream.write(bArr, 0, i);
                j += i;
            } else {
                return j;
            }
        }
    }
}
