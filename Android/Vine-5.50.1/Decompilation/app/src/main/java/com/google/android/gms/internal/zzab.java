package com.google.android.gms.internal;

import com.google.android.gms.internal.zzm;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public class zzab extends zzk<String> {
    private final zzm.zzb<String> zzaG;

    public zzab(int i, String str, zzm.zzb<String> zzbVar, zzm.zza zzaVar) {
        super(i, str, zzaVar);
        this.zzaG = zzbVar;
    }

    public zzab(String str, zzm.zzb<String> zzbVar, zzm.zza zzaVar) {
        this(0, str, zzbVar, zzaVar);
    }

    @Override // com.google.android.gms.internal.zzk
    protected zzm<String> zza(zzi zziVar) {
        String str;
        try {
            str = new String(zziVar.data, zzx.zza(zziVar.zzA));
        } catch (UnsupportedEncodingException e) {
            str = new String(zziVar.data);
        }
        return zzm.zza(str, zzx.zzb(zziVar));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.zzk
    /* renamed from: zzi, reason: merged with bridge method [inline-methods] */
    public void zza(String str) {
        this.zzaG.zzb(str);
    }
}
