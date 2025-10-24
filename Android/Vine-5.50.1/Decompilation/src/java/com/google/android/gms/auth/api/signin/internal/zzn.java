package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.zzx;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class zzn {
    private static final Lock zzWv = new ReentrantLock();
    private static zzn zzWw;
    private final Lock zzWx = new ReentrantLock();
    private final SharedPreferences zzWy;

    zzn(Context context) {
        this.zzWy = context.getSharedPreferences("com.google.android.gms.signin", 0);
    }

    public static zzn zzae(Context context) {
        zzx.zzy(context);
        zzWv.lock();
        try {
            if (zzWw == null) {
                zzWw = new zzn(context.getApplicationContext());
            }
            return zzWw;
        } finally {
            zzWv.unlock();
        }
    }

    private String zzt(String str, String str2) {
        return str + ":" + str2;
    }

    GoogleSignInAccount zzbL(String str) {
        String strZzbN;
        if (TextUtils.isEmpty(str) || (strZzbN = zzbN(zzt("googleSignInAccount", str))) == null) {
            return null;
        }
        try {
            return GoogleSignInAccount.zzbE(strZzbN);
        } catch (JSONException e) {
            return null;
        }
    }

    protected String zzbN(String str) {
        this.zzWx.lock();
        try {
            return this.zzWy.getString(str, null);
        } finally {
            this.zzWx.unlock();
        }
    }

    public GoogleSignInAccount zzmW() {
        return zzbL(zzbN("defaultGoogleSignInAccount"));
    }
}
