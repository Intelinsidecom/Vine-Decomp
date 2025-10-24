package com.google.android.gms.internal;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes2.dex */
public final class zznt {
    public static long zza(InputStream inputStream, OutputStream outputStream, boolean z) throws IOException {
        return zza(inputStream, outputStream, z, 1024);
    }

    public static long zza(InputStream inputStream, OutputStream outputStream, boolean z, int i) throws IOException {
        byte[] bArr = new byte[i];
        long j = 0;
        while (true) {
            try {
                int i2 = inputStream.read(bArr, 0, bArr.length);
                if (i2 == -1) {
                    break;
                }
                j += i2;
                outputStream.write(bArr, 0, i2);
            } finally {
                if (z) {
                    zzb(inputStream);
                    zzb(outputStream);
                }
            }
        }
        return j;
    }

    public static byte[] zza(InputStream inputStream, boolean z) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zza(inputStream, byteArrayOutputStream, z);
        return byteArrayOutputStream.toByteArray();
    }

    public static void zzb(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static byte[] zzk(InputStream inputStream) throws IOException {
        return zza(inputStream, true);
    }
}
