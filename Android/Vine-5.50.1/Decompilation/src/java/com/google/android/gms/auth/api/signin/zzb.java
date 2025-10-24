package com.google.android.gms.auth.api.signin;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class zzb implements Parcelable.Creator<FacebookSignInOptions> {
    static void zza(FacebookSignInOptions facebookSignInOptions, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, facebookSignInOptions.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) facebookSignInOptions.zzmt(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 3, facebookSignInOptions.zzmu(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzQ, reason: merged with bridge method [inline-methods] */
    public FacebookSignInOptions createFromParcel(Parcel parcel) {
        ArrayList<String> arrayListZzD;
        Intent intent;
        int iZzg;
        ArrayList<String> arrayList = null;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int i = 0;
        Intent intent2 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    ArrayList<String> arrayList2 = arrayList;
                    intent = intent2;
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    arrayListZzD = arrayList2;
                    break;
                case 2:
                    Intent intent3 = (Intent) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, Intent.CREATOR);
                    iZzg = i;
                    arrayListZzD = arrayList;
                    intent = intent3;
                    break;
                case 3:
                    arrayListZzD = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzat);
                    intent = intent2;
                    iZzg = i;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    arrayListZzD = arrayList;
                    intent = intent2;
                    iZzg = i;
                    break;
            }
            i = iZzg;
            intent2 = intent;
            arrayList = arrayListZzD;
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new FacebookSignInOptions(i, intent2, arrayList);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaL, reason: merged with bridge method [inline-methods] */
    public FacebookSignInOptions[] newArray(int i) {
        return new FacebookSignInOptions[i];
    }
}
