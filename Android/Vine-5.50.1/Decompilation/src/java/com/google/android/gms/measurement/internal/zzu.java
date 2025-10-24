package com.google.android.gms.measurement.internal;

import android.os.Binder;
import android.os.Process;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.measurement.internal.zzl;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class zzu extends zzl.zza {
    private final zzt zzaQX;
    private final boolean zzaUB;

    public zzu(zzt zztVar) {
        com.google.android.gms.common.internal.zzx.zzy(zztVar);
        this.zzaQX = zztVar;
        this.zzaUB = false;
    }

    public zzu(zzt zztVar, boolean z) {
        com.google.android.gms.common.internal.zzx.zzy(zztVar);
        this.zzaQX = zztVar;
        this.zzaUB = z;
    }

    private void zzeB(String str) throws SecurityException {
        if (TextUtils.isEmpty(str)) {
            this.zzaQX.zzzz().zzBl().zzez("Measurement Service called without app package");
            throw new SecurityException("Measurement Service called without app package");
        }
        try {
            zzeC(str);
        } catch (SecurityException e) {
            this.zzaQX.zzzz().zzBl().zzj("Measurement Service called with invalid calling package", str);
            throw e;
        }
    }

    private void zzeC(String str) throws SecurityException {
        int iMyUid = this.zzaUB ? Process.myUid() : Binder.getCallingUid();
        if (GooglePlayServicesUtil.zzb(this.zzaQX.getContext(), iMyUid, str)) {
            return;
        }
        if (!GooglePlayServicesUtil.zze(this.zzaQX.getContext(), iMyUid) || this.zzaQX.zzBI()) {
            throw new SecurityException(String.format("Unknown calling package name '%s'.", str));
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzl
    public void zza(final AppMetadata appMetadata) throws IllegalStateException, SecurityException {
        com.google.android.gms.common.internal.zzx.zzy(appMetadata);
        zzeB(appMetadata.packageName);
        this.zzaQX.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzu.6
            @Override // java.lang.Runnable
            public void run() throws NoSuchAlgorithmException {
                zzu.this.zzeA(appMetadata.zzaSr);
                zzu.this.zzaQX.zzd(appMetadata);
            }
        });
    }

    @Override // com.google.android.gms.measurement.internal.zzl
    public void zza(final EventParcel eventParcel, final AppMetadata appMetadata) throws IllegalStateException, SecurityException {
        com.google.android.gms.common.internal.zzx.zzy(eventParcel);
        com.google.android.gms.common.internal.zzx.zzy(appMetadata);
        zzeB(appMetadata.packageName);
        this.zzaQX.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzu.2
            @Override // java.lang.Runnable
            public void run() {
                zzu.this.zzeA(appMetadata.zzaSr);
                zzu.this.zzaQX.zzb(eventParcel, appMetadata);
            }
        });
    }

    @Override // com.google.android.gms.measurement.internal.zzl
    public void zza(final EventParcel eventParcel, final String str, final String str2) throws IllegalStateException, SecurityException {
        com.google.android.gms.common.internal.zzx.zzy(eventParcel);
        com.google.android.gms.common.internal.zzx.zzcG(str);
        zzeB(str);
        this.zzaQX.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzu.3
            @Override // java.lang.Runnable
            public void run() {
                zzu.this.zzeA(str2);
                zzu.this.zzaQX.zza(eventParcel, str);
            }
        });
    }

    @Override // com.google.android.gms.measurement.internal.zzl
    public void zza(final UserAttributeParcel userAttributeParcel, final AppMetadata appMetadata) throws IllegalStateException, SecurityException {
        com.google.android.gms.common.internal.zzx.zzy(userAttributeParcel);
        com.google.android.gms.common.internal.zzx.zzy(appMetadata);
        zzeB(appMetadata.packageName);
        if (userAttributeParcel.getValue() == null) {
            this.zzaQX.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzu.4
                @Override // java.lang.Runnable
                public void run() {
                    zzu.this.zzeA(appMetadata.zzaSr);
                    zzu.this.zzaQX.zzc(userAttributeParcel, appMetadata);
                }
            });
        } else {
            this.zzaQX.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzu.5
                @Override // java.lang.Runnable
                public void run() {
                    zzu.this.zzeA(appMetadata.zzaSr);
                    zzu.this.zzaQX.zzb(userAttributeParcel, appMetadata);
                }
            });
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzl
    public void zzb(final AppMetadata appMetadata) throws IllegalStateException, SecurityException {
        com.google.android.gms.common.internal.zzx.zzy(appMetadata);
        zzeB(appMetadata.packageName);
        this.zzaQX.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzu.1
            @Override // java.lang.Runnable
            public void run() throws NoSuchAlgorithmException {
                zzu.this.zzeA(appMetadata.zzaSr);
                zzu.this.zzaQX.zzc(appMetadata);
            }
        });
    }

    void zzeA(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String[] strArrSplit = str.split(":", 2);
        if (strArrSplit.length == 2) {
            try {
                long jLongValue = Long.valueOf(strArrSplit[0]).longValue();
                if (jLongValue > 0) {
                    this.zzaQX.zzAW().zzaTE.zzg(strArrSplit[1], jLongValue);
                } else {
                    this.zzaQX.zzzz().zzBm().zzj("Combining sample with a non-positive weight", Long.valueOf(jLongValue));
                }
            } catch (NumberFormatException e) {
                this.zzaQX.zzzz().zzBm().zzj("Combining sample with a non-number weight", strArrSplit[0]);
            }
        }
    }
}
