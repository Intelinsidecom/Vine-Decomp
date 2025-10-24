package com.google.android.gms.clearcut;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.playlog.internal.PlayLoggerContext;

/* loaded from: classes2.dex */
public class zzc implements Parcelable.Creator<LogEventParcelable> {
    static void zza(LogEventParcelable logEventParcelable, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, logEventParcelable.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) logEventParcelable.zzadx, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, logEventParcelable.zzady, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, logEventParcelable.zzadz, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaf, reason: merged with bridge method [inline-methods] */
    public LogEventParcelable createFromParcel(Parcel parcel) {
        int[] iArrZzv;
        byte[] bArrZzs;
        PlayLoggerContext playLoggerContext;
        int iZzg;
        int[] iArr = null;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int i = 0;
        byte[] bArr = null;
        PlayLoggerContext playLoggerContext2 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    int[] iArr2 = iArr;
                    bArrZzs = bArr;
                    playLoggerContext = playLoggerContext2;
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    iArrZzv = iArr2;
                    break;
                case 2:
                    iZzg = i;
                    byte[] bArr2 = bArr;
                    playLoggerContext = (PlayLoggerContext) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, PlayLoggerContext.CREATOR);
                    iArrZzv = iArr;
                    bArrZzs = bArr2;
                    break;
                case 3:
                    playLoggerContext = playLoggerContext2;
                    iZzg = i;
                    int[] iArr3 = iArr;
                    bArrZzs = com.google.android.gms.common.internal.safeparcel.zza.zzs(parcel, iZzat);
                    iArrZzv = iArr3;
                    break;
                case 4:
                    iArrZzv = com.google.android.gms.common.internal.safeparcel.zza.zzv(parcel, iZzat);
                    bArrZzs = bArr;
                    playLoggerContext = playLoggerContext2;
                    iZzg = i;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    iArrZzv = iArr;
                    bArrZzs = bArr;
                    playLoggerContext = playLoggerContext2;
                    iZzg = i;
                    break;
            }
            i = iZzg;
            playLoggerContext2 = playLoggerContext;
            bArr = bArrZzs;
            iArr = iArrZzv;
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new LogEventParcelable(i, playLoggerContext2, bArr, iArr);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbs, reason: merged with bridge method [inline-methods] */
    public LogEventParcelable[] newArray(int i) {
        return new LogEventParcelable[i];
    }
}
