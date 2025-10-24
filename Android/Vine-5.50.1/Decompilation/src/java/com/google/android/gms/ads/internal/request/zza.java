package com.google.android.gms.ads.internal.request;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzil;

@zzha
/* loaded from: classes.dex */
public class zza {

    /* renamed from: com.google.android.gms.ads.internal.request.zza$zza, reason: collision with other inner class name */
    public interface InterfaceC0024zza {
        void zza(zzie.zza zzaVar);
    }

    public zzil zza(Context context, AdRequestInfoParcel.zza zzaVar, zzan zzanVar, InterfaceC0024zza interfaceC0024zza) {
        zzil zzmVar = zzaVar.zzGq.extras.getBundle("sdk_less_server_data") != null ? new zzm(context, zzaVar, interfaceC0024zza) : new zzb(context, zzaVar, zzanVar, interfaceC0024zza);
        zzmVar.zzfR();
        return zzmVar;
    }
}
