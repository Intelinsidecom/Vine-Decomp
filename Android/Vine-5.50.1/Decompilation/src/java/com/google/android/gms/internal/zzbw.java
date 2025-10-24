package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzbw {
    private final Collection<zzbv> zzuZ = new ArrayList();
    private final Collection<zzbv<String>> zzva = new ArrayList();
    private final Collection<zzbv<String>> zzvb = new ArrayList();

    public void zza(zzbv zzbvVar) {
        this.zzuZ.add(zzbvVar);
    }

    public void zzb(zzbv<String> zzbvVar) {
        this.zzva.add(zzbvVar);
    }

    public void zzc(zzbv<String> zzbvVar) {
        this.zzvb.add(zzbvVar);
    }

    public List<String> zzdl() {
        ArrayList arrayList = new ArrayList();
        Iterator<zzbv<String>> it = this.zzva.iterator();
        while (it.hasNext()) {
            String str = it.next().get();
            if (str != null) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }
}
