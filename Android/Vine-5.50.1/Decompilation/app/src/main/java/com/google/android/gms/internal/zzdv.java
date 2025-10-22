package com.google.android.gms.internal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzdv implements Iterable<zzdu> {
    private final List<zzdu> zzzm = new LinkedList();

    private zzdu zzc(zzjn zzjnVar) {
        Iterator<zzdu> it = com.google.android.gms.ads.internal.zzp.zzbL().iterator();
        while (it.hasNext()) {
            zzdu next = it.next();
            if (next.zzps == zzjnVar) {
                return next;
            }
        }
        return null;
    }

    @Override // java.lang.Iterable
    public Iterator<zzdu> iterator() {
        return this.zzzm.iterator();
    }

    public void zza(zzdu zzduVar) {
        this.zzzm.add(zzduVar);
    }

    public boolean zza(zzjn zzjnVar) {
        zzdu zzduVarZzc = zzc(zzjnVar);
        if (zzduVarZzc == null) {
            return false;
        }
        zzduVarZzc.zzzj.abort();
        return true;
    }

    public void zzb(zzdu zzduVar) {
        this.zzzm.remove(zzduVar);
    }

    public boolean zzb(zzjn zzjnVar) {
        return zzc(zzjnVar) != null;
    }
}
