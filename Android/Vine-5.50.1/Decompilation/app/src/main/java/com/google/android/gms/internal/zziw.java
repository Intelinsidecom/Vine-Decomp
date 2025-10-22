package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@zzha
/* loaded from: classes.dex */
public final class zziw extends zzil {
    private final Context mContext;
    private final String zzF;
    private String zzKi;
    private final String zzrD;

    public zziw(Context context, String str, String str2) {
        this.zzKi = null;
        this.mContext = context;
        this.zzrD = str;
        this.zzF = str2;
    }

    public zziw(Context context, String str, String str2, String str3) {
        this.zzKi = null;
        this.mContext = context;
        this.zzrD = str;
        this.zzF = str2;
        this.zzKi = str3;
    }

    @Override // com.google.android.gms.internal.zzil
    public void onStop() {
    }

    @Override // com.google.android.gms.internal.zzil
    public void zzbp() {
        try {
            com.google.android.gms.ads.internal.util.client.zzb.v("Pinging URL: " + this.zzF);
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.zzF).openConnection();
            try {
                if (TextUtils.isEmpty(this.zzKi)) {
                    com.google.android.gms.ads.internal.zzp.zzbx().zza(this.mContext, this.zzrD, true, httpURLConnection);
                } else {
                    com.google.android.gms.ads.internal.zzp.zzbx().zza(this.mContext, this.zzrD, true, httpURLConnection, this.zzKi);
                }
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode < 200 || responseCode >= 300) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH("Received non-success response code " + responseCode + " from pinging URL: " + this.zzF);
                }
            } finally {
                httpURLConnection.disconnect();
            }
        } catch (IOException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Error while pinging URL: " + this.zzF + ". " + e.getMessage());
        } catch (IndexOutOfBoundsException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Error while parsing ping URL: " + this.zzF + ". " + e2.getMessage());
        } catch (RuntimeException e3) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Error while pinging URL: " + this.zzF + ". " + e3.getMessage());
        }
    }
}
