package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

@zzha
/* loaded from: classes.dex */
public class zzif {
    private boolean zzIi;
    private final LinkedList<zza> zzJM;
    private final String zzJN;
    private final String zzJO;
    private long zzJP;
    private long zzJQ;
    private long zzJR;
    private long zzJS;
    private long zzJT;
    private long zzJU;
    private final Object zzpK;
    private final zzig zzqC;

    @zzha
    private static final class zza {
        private long zzJV = -1;
        private long zzJW = -1;

        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            bundle.putLong("topen", this.zzJV);
            bundle.putLong("tclose", this.zzJW);
            return bundle;
        }

        public long zzgG() {
            return this.zzJW;
        }

        public void zzgH() {
            this.zzJW = SystemClock.elapsedRealtime();
        }

        public void zzgI() {
            this.zzJV = SystemClock.elapsedRealtime();
        }
    }

    public zzif(zzig zzigVar, String str, String str2) {
        this.zzpK = new Object();
        this.zzJP = -1L;
        this.zzJQ = -1L;
        this.zzIi = false;
        this.zzJR = -1L;
        this.zzJS = 0L;
        this.zzJT = -1L;
        this.zzJU = -1L;
        this.zzqC = zzigVar;
        this.zzJN = str;
        this.zzJO = str2;
        this.zzJM = new LinkedList<>();
    }

    public zzif(String str, String str2) {
        this(com.google.android.gms.ads.internal.zzp.zzbA(), str, str2);
    }

    public Bundle toBundle() {
        Bundle bundle;
        synchronized (this.zzpK) {
            bundle = new Bundle();
            bundle.putString("seq_num", this.zzJN);
            bundle.putString("slotid", this.zzJO);
            bundle.putBoolean("ismediation", this.zzIi);
            bundle.putLong("treq", this.zzJT);
            bundle.putLong("tresponse", this.zzJU);
            bundle.putLong("timp", this.zzJQ);
            bundle.putLong("tload", this.zzJR);
            bundle.putLong("pcc", this.zzJS);
            bundle.putLong("tfetch", this.zzJP);
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
            Iterator<zza> it = this.zzJM.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().toBundle());
            }
            bundle.putParcelableArrayList("tclick", arrayList);
        }
        return bundle;
    }

    public void zzA(boolean z) {
        synchronized (this.zzpK) {
            if (this.zzJU != -1) {
                this.zzIi = z;
                this.zzqC.zza(this);
            }
        }
    }

    public void zzgD() {
        synchronized (this.zzpK) {
            if (this.zzJU != -1 && this.zzJQ == -1) {
                this.zzJQ = SystemClock.elapsedRealtime();
                this.zzqC.zza(this);
            }
            this.zzqC.zzgL().zzgD();
        }
    }

    public void zzgE() {
        synchronized (this.zzpK) {
            if (this.zzJU != -1) {
                zza zzaVar = new zza();
                zzaVar.zzgI();
                this.zzJM.add(zzaVar);
                this.zzJS++;
                this.zzqC.zzgL().zzgE();
                this.zzqC.zza(this);
            }
        }
    }

    public void zzgF() {
        synchronized (this.zzpK) {
            if (this.zzJU != -1 && !this.zzJM.isEmpty()) {
                zza last = this.zzJM.getLast();
                if (last.zzgG() == -1) {
                    last.zzgH();
                    this.zzqC.zza(this);
                }
            }
        }
    }

    public void zzj(AdRequestParcel adRequestParcel) {
        synchronized (this.zzpK) {
            this.zzJT = SystemClock.elapsedRealtime();
            this.zzqC.zzgL().zzb(adRequestParcel, this.zzJT);
        }
    }

    public void zzl(long j) {
        synchronized (this.zzpK) {
            this.zzJU = j;
            if (this.zzJU != -1) {
                this.zzqC.zza(this);
            }
        }
    }

    public void zzm(long j) {
        synchronized (this.zzpK) {
            if (this.zzJU != -1) {
                this.zzJP = j;
                this.zzqC.zza(this);
            }
        }
    }

    public void zzz(boolean z) {
        synchronized (this.zzpK) {
            if (this.zzJU != -1) {
                this.zzJR = SystemClock.elapsedRealtime();
                if (!z) {
                    this.zzJQ = this.zzJR;
                    this.zzqC.zza(this);
                }
            }
        }
    }
}
