package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes2.dex */
public class zzg implements Parcelable.Creator<RecordConsentRequest> {
    static void zza(RecordConsentRequest recordConsentRequest, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, recordConsentRequest.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) recordConsentRequest.getAccount(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable[]) recordConsentRequest.zzDT(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, recordConsentRequest.zzmB(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzgI, reason: merged with bridge method [inline-methods] */
    public RecordConsentRequest createFromParcel(Parcel parcel) {
        String strZzp;
        Scope[] scopeArr;
        Account account;
        int iZzg;
        String str = null;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int i = 0;
        Scope[] scopeArr2 = null;
        Account account2 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    String str2 = str;
                    scopeArr = scopeArr2;
                    account = account2;
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    strZzp = str2;
                    break;
                case 2:
                    iZzg = i;
                    Scope[] scopeArr3 = scopeArr2;
                    account = (Account) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, Account.CREATOR);
                    strZzp = str;
                    scopeArr = scopeArr3;
                    break;
                case 3:
                    account = account2;
                    iZzg = i;
                    String str3 = str;
                    scopeArr = (Scope[]) com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat, Scope.CREATOR);
                    strZzp = str3;
                    break;
                case 4:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    scopeArr = scopeArr2;
                    account = account2;
                    iZzg = i;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    strZzp = str;
                    scopeArr = scopeArr2;
                    account = account2;
                    iZzg = i;
                    break;
            }
            i = iZzg;
            account2 = account;
            scopeArr2 = scopeArr;
            str = strZzp;
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new RecordConsentRequest(i, account2, scopeArr2, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzjM, reason: merged with bridge method [inline-methods] */
    public RecordConsentRequest[] newArray(int i) {
        return new RecordConsentRequest[i];
    }
}
