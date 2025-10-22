package com.google.android.gms.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/* loaded from: classes2.dex */
public final class zzsd implements Api.ApiOptions.Optional {
    public static final zzsd zzbbH = new zza().zzDQ();
    private final boolean zzVD;
    private final boolean zzVF;
    private final String zzVG;
    private final boolean zzbbI;
    private final GoogleApiClient.ServerAuthCodeCallbacks zzbbJ;
    private final boolean zzbbK;
    private final boolean zzbbL;

    public static final class zza {
        private String zzaYL;
        private boolean zzbbM;
        private boolean zzbbN;
        private GoogleApiClient.ServerAuthCodeCallbacks zzbbO;
        private boolean zzbbP;
        private boolean zzbbQ;
        private boolean zzbbR;

        public zzsd zzDQ() {
            return new zzsd(this.zzbbM, this.zzbbN, this.zzaYL, this.zzbbO, this.zzbbP, this.zzbbQ, this.zzbbR);
        }
    }

    private zzsd(boolean z, boolean z2, String str, GoogleApiClient.ServerAuthCodeCallbacks serverAuthCodeCallbacks, boolean z3, boolean z4, boolean z5) {
        this.zzbbI = z;
        this.zzVD = z2;
        this.zzVG = str;
        this.zzbbJ = serverAuthCodeCallbacks;
        this.zzbbK = z3;
        this.zzVF = z4;
        this.zzbbL = z5;
    }

    public boolean zzDM() {
        return this.zzbbI;
    }

    public GoogleApiClient.ServerAuthCodeCallbacks zzDN() {
        return this.zzbbJ;
    }

    public boolean zzDO() {
        return this.zzbbK;
    }

    public boolean zzDP() {
        return this.zzbbL;
    }

    public boolean zzmA() {
        return this.zzVF;
    }

    public String zzmB() {
        return this.zzVG;
    }

    public boolean zzmy() {
        return this.zzVD;
    }
}
