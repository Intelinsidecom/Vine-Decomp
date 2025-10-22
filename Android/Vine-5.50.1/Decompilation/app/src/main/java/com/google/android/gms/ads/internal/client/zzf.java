package com.google.android.gms.ads.internal.client;

import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.internal.zzha;
import java.util.ArrayList;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public final class zzf {
    private Bundle mExtras;
    private Location zzba;
    private boolean zzpt;
    private long zztG;
    private int zztH;
    private List<String> zztI;
    private boolean zztJ;
    private int zztK;
    private String zztL;
    private SearchAdRequestParcel zztM;
    private String zztN;
    private Bundle zztO;
    private Bundle zztP;
    private List<String> zztQ;
    private String zztR;
    private String zztS;
    private boolean zztT;

    public zzf() {
        this.zztG = -1L;
        this.mExtras = new Bundle();
        this.zztH = -1;
        this.zztI = new ArrayList();
        this.zztJ = false;
        this.zztK = -1;
        this.zzpt = false;
        this.zztL = null;
        this.zztM = null;
        this.zzba = null;
        this.zztN = null;
        this.zztO = new Bundle();
        this.zztP = new Bundle();
        this.zztQ = new ArrayList();
        this.zztR = null;
        this.zztS = null;
        this.zztT = false;
    }

    public zzf(AdRequestParcel adRequestParcel) {
        this.zztG = adRequestParcel.zztq;
        this.mExtras = adRequestParcel.extras;
        this.zztH = adRequestParcel.zztr;
        this.zztI = adRequestParcel.zzts;
        this.zztJ = adRequestParcel.zztt;
        this.zztK = adRequestParcel.zztu;
        this.zzpt = adRequestParcel.zztv;
        this.zztL = adRequestParcel.zztw;
        this.zztM = adRequestParcel.zztx;
        this.zzba = adRequestParcel.zzty;
        this.zztN = adRequestParcel.zztz;
        this.zztO = adRequestParcel.zztA;
        this.zztP = adRequestParcel.zztB;
        this.zztQ = adRequestParcel.zztC;
        this.zztR = adRequestParcel.zztD;
        this.zztS = adRequestParcel.zztE;
    }

    public zzf zza(Location location) {
        this.zzba = location;
        return this;
    }

    public AdRequestParcel zzcI() {
        return new AdRequestParcel(7, this.zztG, this.mExtras, this.zztH, this.zztI, this.zztJ, this.zztK, this.zzpt, this.zztL, this.zztM, this.zzba, this.zztN, this.zztO, this.zztP, this.zztQ, this.zztR, this.zztS, this.zztT);
    }
}
