package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class zzc implements Parcelable.Creator<ScopeDetail> {
    static void zza(ScopeDetail scopeDetail, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, scopeDetail.version);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, scopeDetail.description, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, scopeDetail.zzWR, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, scopeDetail.zzWS, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, scopeDetail.zzWT, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, scopeDetail.zzWU, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 7, scopeDetail.zzWV, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, (Parcelable) scopeDetail.zzWW, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzY, reason: merged with bridge method [inline-methods] */
    public ScopeDetail createFromParcel(Parcel parcel) {
        FACLData fACLData = null;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        ArrayList<String> arrayList = new ArrayList<>();
        String strZzp = null;
        String strZzp2 = null;
        String strZzp3 = null;
        String strZzp4 = null;
        String strZzp5 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    strZzp5 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 3:
                    strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 4:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 5:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 6:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 7:
                    arrayList = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzat);
                    break;
                case 8:
                    fACLData = (FACLData) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, FACLData.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new ScopeDetail(iZzg, strZzp5, strZzp4, strZzp3, strZzp2, strZzp, arrayList, fACLData);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaV, reason: merged with bridge method [inline-methods] */
    public ScopeDetail[] newArray(int i) {
        return new ScopeDetail[i];
    }
}
