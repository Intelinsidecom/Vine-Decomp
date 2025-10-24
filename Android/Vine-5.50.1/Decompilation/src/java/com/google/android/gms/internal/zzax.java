package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public final class zzax {
    private final String zzrA;
    private final JSONObject zzrB;
    private final String zzrC;
    private final String zzrD;
    private final boolean zzrE;
    private final boolean zzrF;

    public zzax(String str, VersionInfoParcel versionInfoParcel, String str2, JSONObject jSONObject, boolean z, boolean z2) {
        this.zzrD = versionInfoParcel.afmaVersion;
        this.zzrB = jSONObject;
        this.zzrC = str;
        this.zzrA = str2;
        this.zzrE = z;
        this.zzrF = z2;
    }

    public String zzbX() {
        return this.zzrA;
    }

    public String zzbY() {
        return this.zzrD;
    }

    public JSONObject zzbZ() {
        return this.zzrB;
    }

    public String zzca() {
        return this.zzrC;
    }

    public boolean zzcb() {
        return this.zzrE;
    }

    public boolean zzcc() {
        return this.zzrF;
    }
}
