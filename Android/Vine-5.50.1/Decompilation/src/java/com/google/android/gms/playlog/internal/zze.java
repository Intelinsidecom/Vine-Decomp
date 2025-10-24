package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

/* loaded from: classes2.dex */
public class zze implements Parcelable.Creator<PlayLoggerContext> {
    static void zza(PlayLoggerContext playLoggerContext, Parcel parcel, int i) {
        int iZzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, playLoggerContext.versionCode);
        zzb.zza(parcel, 2, playLoggerContext.packageName, false);
        zzb.zzc(parcel, 3, playLoggerContext.zzaYy);
        zzb.zzc(parcel, 4, playLoggerContext.zzaYz);
        zzb.zza(parcel, 5, playLoggerContext.zzaYA, false);
        zzb.zza(parcel, 6, playLoggerContext.zzaYB, false);
        zzb.zza(parcel, 7, playLoggerContext.zzaYC);
        zzb.zza(parcel, 8, playLoggerContext.zzaYD, false);
        zzb.zza(parcel, 9, playLoggerContext.zzaYE);
        zzb.zzc(parcel, 10, playLoggerContext.zzaYF);
        zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzgo, reason: merged with bridge method [inline-methods] */
    public PlayLoggerContext createFromParcel(Parcel parcel) {
        String strZzp = null;
        int iZzg = 0;
        int iZzau = zza.zzau(parcel);
        boolean zZzc = true;
        boolean zZzc2 = false;
        String strZzp2 = null;
        String strZzp3 = null;
        int iZzg2 = 0;
        int iZzg3 = 0;
        String strZzp4 = null;
        int iZzg4 = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = zza.zzat(parcel);
            switch (zza.zzcc(iZzat)) {
                case 1:
                    iZzg4 = zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    strZzp4 = zza.zzp(parcel, iZzat);
                    break;
                case 3:
                    iZzg3 = zza.zzg(parcel, iZzat);
                    break;
                case 4:
                    iZzg2 = zza.zzg(parcel, iZzat);
                    break;
                case 5:
                    strZzp3 = zza.zzp(parcel, iZzat);
                    break;
                case 6:
                    strZzp2 = zza.zzp(parcel, iZzat);
                    break;
                case 7:
                    zZzc = zza.zzc(parcel, iZzat);
                    break;
                case 8:
                    strZzp = zza.zzp(parcel, iZzat);
                    break;
                case 9:
                    zZzc2 = zza.zzc(parcel, iZzat);
                    break;
                case 10:
                    iZzg = zza.zzg(parcel, iZzat);
                    break;
                default:
                    zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new PlayLoggerContext(iZzg4, strZzp4, iZzg3, iZzg2, strZzp3, strZzp2, zZzc, strZzp, zZzc2, iZzg);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzjq, reason: merged with bridge method [inline-methods] */
    public PlayLoggerContext[] newArray(int i) {
        return new PlayLoggerContext[i];
    }
}
