package com.google.android.gms.internal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzbj {
    private int zzsM;
    private final Object zzpK = new Object();
    private List<zzbi> zzsN = new LinkedList();

    public boolean zza(zzbi zzbiVar) {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzsN.contains(zzbiVar);
        }
        return z;
    }

    public boolean zzb(zzbi zzbiVar) {
        boolean z;
        synchronized (this.zzpK) {
            Iterator<zzbi> it = this.zzsN.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                zzbi next = it.next();
                if (zzbiVar != next && next.zzcu().equals(zzbiVar.zzcu())) {
                    it.remove();
                    z = true;
                    break;
                }
            }
        }
        return z;
    }

    public void zzc(zzbi zzbiVar) {
        synchronized (this.zzpK) {
            if (this.zzsN.size() >= 10) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Queue is full, current size = " + this.zzsN.size());
                this.zzsN.remove(0);
            }
            int i = this.zzsM;
            this.zzsM = i + 1;
            zzbiVar.zzh(i);
            this.zzsN.add(zzbiVar);
        }
    }

    public zzbi zzcA() {
        int i;
        zzbi zzbiVar;
        zzbi zzbiVar2 = null;
        synchronized (this.zzpK) {
            if (this.zzsN.size() == 0) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Queue empty");
                return null;
            }
            if (this.zzsN.size() < 2) {
                zzbi zzbiVar3 = this.zzsN.get(0);
                zzbiVar3.zzcv();
                return zzbiVar3;
            }
            int i2 = Integer.MIN_VALUE;
            for (zzbi zzbiVar4 : this.zzsN) {
                int score = zzbiVar4.getScore();
                if (score > i2) {
                    zzbiVar = zzbiVar4;
                    i = score;
                } else {
                    i = i2;
                    zzbiVar = zzbiVar2;
                }
                i2 = i;
                zzbiVar2 = zzbiVar;
            }
            this.zzsN.remove(zzbiVar2);
            return zzbiVar2;
        }
    }
}
