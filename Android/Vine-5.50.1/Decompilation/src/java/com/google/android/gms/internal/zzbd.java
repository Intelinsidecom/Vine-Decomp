package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzbb;
import com.google.android.gms.internal.zzjo;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzbd implements zzbb {
    private final zzjn zzps;

    public zzbd(Context context, VersionInfoParcel versionInfoParcel, zzan zzanVar) {
        this.zzps = com.google.android.gms.ads.internal.zzp.zzby().zza(context, new AdSizeParcel(), false, false, zzanVar, versionInfoParcel);
        this.zzps.getWebView().setWillNotDraw(true);
    }

    private void runOnUiThread(Runnable runnable) {
        if (com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            runnable.run();
        } else {
            zzip.zzKO.post(runnable);
        }
    }

    @Override // com.google.android.gms.internal.zzbb
    public void destroy() {
        this.zzps.destroy();
    }

    @Override // com.google.android.gms.internal.zzbb
    public void zza(com.google.android.gms.ads.internal.client.zza zzaVar, com.google.android.gms.ads.internal.overlay.zzg zzgVar, zzdh zzdhVar, com.google.android.gms.ads.internal.overlay.zzn zznVar, boolean z, zzdn zzdnVar, zzdp zzdpVar, com.google.android.gms.ads.internal.zze zzeVar, zzfs zzfsVar) {
        this.zzps.zzhC().zzb(zzaVar, zzgVar, zzdhVar, zznVar, z, zzdnVar, zzdpVar, new com.google.android.gms.ads.internal.zze(false), zzfsVar);
    }

    @Override // com.google.android.gms.internal.zzbb
    public void zza(final zzbb.zza zzaVar) {
        this.zzps.zzhC().zza(new zzjo.zza() { // from class: com.google.android.gms.internal.zzbd.6
            @Override // com.google.android.gms.internal.zzjo.zza
            public void zza(zzjn zzjnVar, boolean z) {
                zzaVar.zzcr();
            }
        });
    }

    @Override // com.google.android.gms.internal.zzbe
    public void zza(String str, zzdl zzdlVar) {
        this.zzps.zzhC().zza(str, zzdlVar);
    }

    @Override // com.google.android.gms.internal.zzbe
    public void zza(final String str, final String str2) {
        runOnUiThread(new Runnable() { // from class: com.google.android.gms.internal.zzbd.2
            @Override // java.lang.Runnable
            public void run() {
                zzbd.this.zzps.zza(str, str2);
            }
        });
    }

    @Override // com.google.android.gms.internal.zzbe
    public void zza(final String str, final JSONObject jSONObject) {
        runOnUiThread(new Runnable() { // from class: com.google.android.gms.internal.zzbd.1
            @Override // java.lang.Runnable
            public void run() {
                zzbd.this.zzps.zza(str, jSONObject);
            }
        });
    }

    @Override // com.google.android.gms.internal.zzbe
    public void zzb(String str, zzdl zzdlVar) {
        this.zzps.zzhC().zzb(str, zzdlVar);
    }

    @Override // com.google.android.gms.internal.zzbe
    public void zzb(String str, JSONObject jSONObject) {
        this.zzps.zzb(str, jSONObject);
    }

    @Override // com.google.android.gms.internal.zzbb
    public zzbf zzcq() {
        return new zzbg(this);
    }

    @Override // com.google.android.gms.internal.zzbb
    public void zzs(String str) {
        final String str2 = String.format("<!DOCTYPE html><html><head><script src=\"%s\"></script></head><body></body></html>", str);
        runOnUiThread(new Runnable() { // from class: com.google.android.gms.internal.zzbd.3
            @Override // java.lang.Runnable
            public void run() {
                zzbd.this.zzps.loadData(str2, "text/html", "UTF-8");
            }
        });
    }

    @Override // com.google.android.gms.internal.zzbb
    public void zzt(final String str) {
        runOnUiThread(new Runnable() { // from class: com.google.android.gms.internal.zzbd.5
            @Override // java.lang.Runnable
            public void run() {
                zzbd.this.zzps.loadUrl(str);
            }
        });
    }

    @Override // com.google.android.gms.internal.zzbb
    public void zzu(final String str) {
        runOnUiThread(new Runnable() { // from class: com.google.android.gms.internal.zzbd.4
            @Override // java.lang.Runnable
            public void run() {
                zzbd.this.zzps.loadData(str, "text/html", "UTF-8");
            }
        });
    }
}
