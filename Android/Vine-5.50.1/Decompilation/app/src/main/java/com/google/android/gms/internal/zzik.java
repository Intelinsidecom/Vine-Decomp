package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import java.math.BigInteger;
import java.util.Locale;

@zzha
/* loaded from: classes.dex */
public final class zzik {
    private static String zzKs;
    private static final Object zzqf = new Object();

    public static String zza(Context context, String str, String str2) {
        String str3;
        synchronized (zzqf) {
            if (zzKs == null && !TextUtils.isEmpty(str)) {
                zzb(context, str, str2);
            }
            str3 = zzKs;
        }
        return str3;
    }

    private static void zzb(Context context, String str, String str2) {
        try {
            ClassLoader classLoader = context.createPackageContext(str2, 3).getClassLoader();
            Class<?> cls = Class.forName("com.google.ads.mediation.MediationAdapter", false, classLoader);
            BigInteger bigInteger = new BigInteger(new byte[1]);
            String[] strArrSplit = str.split(",");
            BigInteger bit = bigInteger;
            for (int i = 0; i < strArrSplit.length; i++) {
                if (com.google.android.gms.ads.internal.zzp.zzbx().zza(classLoader, cls, strArrSplit[i])) {
                    bit = bit.setBit(i);
                }
            }
            zzKs = String.format(Locale.US, "%X", bit);
        } catch (Throwable th) {
            zzKs = "err";
        }
    }

    public static String zzgW() {
        String str;
        synchronized (zzqf) {
            str = zzKs;
        }
        return str;
    }
}
