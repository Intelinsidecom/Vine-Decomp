package com.google.android.gms.playlog.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

/* loaded from: classes2.dex */
public class zzc implements Parcelable.Creator<LogEvent> {
    static void zza(LogEvent logEvent, Parcel parcel, int i) {
        int iZzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, logEvent.versionCode);
        zzb.zza(parcel, 2, logEvent.zzaYn);
        zzb.zza(parcel, 3, logEvent.tag, false);
        zzb.zza(parcel, 4, logEvent.zzaYp, false);
        zzb.zza(parcel, 5, logEvent.zzaYq, false);
        zzb.zza(parcel, 6, logEvent.zzaYo);
        zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzgn, reason: merged with bridge method [inline-methods] */
    public LogEvent createFromParcel(Parcel parcel) {
        long jZzi = 0;
        Bundle bundleZzr = null;
        int iZzau = zza.zzau(parcel);
        int iZzg = 0;
        byte[] bArrZzs = null;
        String strZzp = null;
        long jZzi2 = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = zza.zzat(parcel);
            switch (zza.zzcc(iZzat)) {
                case 1:
                    iZzg = zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    jZzi2 = zza.zzi(parcel, iZzat);
                    break;
                case 3:
                    strZzp = zza.zzp(parcel, iZzat);
                    break;
                case 4:
                    bArrZzs = zza.zzs(parcel, iZzat);
                    break;
                case 5:
                    bundleZzr = zza.zzr(parcel, iZzat);
                    break;
                case 6:
                    jZzi = zza.zzi(parcel, iZzat);
                    break;
                default:
                    zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new LogEvent(iZzg, jZzi2, jZzi, strZzp, bArrZzs, bundleZzr);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzjp, reason: merged with bridge method [inline-methods] */
    public LogEvent[] newArray(int i) {
        return new LogEvent[i];
    }
}
