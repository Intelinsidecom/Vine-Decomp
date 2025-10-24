package com.google.android.gms.internal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public abstract class zzbm {
    private static MessageDigest zztd = null;
    protected Object zzpK = new Object();

    protected MessageDigest zzcG() {
        MessageDigest messageDigest;
        synchronized (this.zzpK) {
            if (zztd != null) {
                messageDigest = zztd;
            } else {
                for (int i = 0; i < 2; i++) {
                    try {
                        zztd = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                    }
                }
                messageDigest = zztd;
            }
        }
        return messageDigest;
    }

    abstract byte[] zzz(String str);
}
