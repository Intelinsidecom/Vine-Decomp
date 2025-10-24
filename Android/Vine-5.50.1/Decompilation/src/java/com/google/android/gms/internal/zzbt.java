package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import java.util.Collections;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzbt implements zzbu {
    @Override // com.google.android.gms.internal.zzbu
    public List<String> zza(AdRequestInfoParcel adRequestInfoParcel) {
        return adRequestInfoParcel.zzGG == null ? Collections.emptyList() : adRequestInfoParcel.zzGG;
    }
}
