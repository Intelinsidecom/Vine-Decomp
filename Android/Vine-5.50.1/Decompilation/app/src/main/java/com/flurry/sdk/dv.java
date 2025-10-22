package com.flurry.sdk;

import android.os.Looper;
import android.telephony.TelephonyManager;
import java.util.Arrays;

/* loaded from: classes.dex */
public class dv {
    private static final String a = dv.class.getSimpleName();
    private static byte[] b;

    public static synchronized byte[] a() {
        byte[] bArr;
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            throw new IllegalStateException("Must be called from a background thread!");
        }
        if (b != null) {
            bArr = b;
        } else if (dl.a().b().checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
            bArr = null;
        } else {
            b();
            bArr = b;
        }
        return bArr;
    }

    private static void b() {
        String deviceId;
        TelephonyManager telephonyManager = (TelephonyManager) dl.a().b().getSystemService("phone");
        if (telephonyManager != null && (deviceId = telephonyManager.getDeviceId()) != null && deviceId.trim().length() > 0) {
            try {
                byte[] bArrD = fb.d(deviceId);
                if (bArrD != null && bArrD.length == 20) {
                    b = bArrD;
                } else {
                    el.a(6, a, "sha1 is not 20 bytes long: " + Arrays.toString(bArrD));
                }
            } catch (Exception e) {
                el.a(6, a, "Exception in generateHashedImei()");
            }
        }
    }
}
