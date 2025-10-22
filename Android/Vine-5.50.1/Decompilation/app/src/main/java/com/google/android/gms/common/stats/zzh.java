package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class zzh implements Parcelable.Creator<WakeLockEvent> {
    static void zza(WakeLockEvent wakeLockEvent, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, wakeLockEvent.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, wakeLockEvent.getTimeMillis());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, wakeLockEvent.zzrB(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 5, wakeLockEvent.zzrD());
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 6, wakeLockEvent.zzrE(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, wakeLockEvent.zzrx());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, wakeLockEvent.zzrC(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 11, wakeLockEvent.getEventType());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 12, wakeLockEvent.zzru(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 13, wakeLockEvent.zzrG(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 14, wakeLockEvent.zzrF());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 15, wakeLockEvent.zzrH());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 16, wakeLockEvent.zzrI());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaG, reason: merged with bridge method [inline-methods] */
    public WakeLockEvent createFromParcel(Parcel parcel) {
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        long jZzi = 0;
        int iZzg2 = 0;
        String strZzp = null;
        int iZzg3 = 0;
        ArrayList<String> arrayListZzD = null;
        String strZzp2 = null;
        long jZzi2 = 0;
        int iZzg4 = 0;
        String strZzp3 = null;
        String strZzp4 = null;
        float fZzl = 0.0f;
        long jZzi3 = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzat);
                    break;
                case 3:
                case 7:
                case 9:
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
                case 4:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 5:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 6:
                    arrayListZzD = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzat);
                    break;
                case 8:
                    jZzi2 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzat);
                    break;
                case 10:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 11:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 12:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 13:
                    strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 14:
                    iZzg4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 15:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 16:
                    jZzi3 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new WakeLockEvent(iZzg, jZzi, iZzg2, strZzp, iZzg3, arrayListZzD, strZzp2, jZzi2, iZzg4, strZzp3, strZzp4, fZzl, jZzi3);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzco, reason: merged with bridge method [inline-methods] */
    public WakeLockEvent[] newArray(int i) {
        return new WakeLockEvent[i];
    }
}
