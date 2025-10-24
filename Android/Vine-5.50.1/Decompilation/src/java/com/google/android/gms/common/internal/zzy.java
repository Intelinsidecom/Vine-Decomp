package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes2.dex */
public class zzy implements Parcelable.Creator<ResolveAccountRequest> {
    static void zza(ResolveAccountRequest resolveAccountRequest, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, resolveAccountRequest.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) resolveAccountRequest.getAccount(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, resolveAccountRequest.getSessionId());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable) resolveAccountRequest.zzqG(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzap, reason: merged with bridge method [inline-methods] */
    public ResolveAccountRequest createFromParcel(Parcel parcel) {
        GoogleSignInAccount googleSignInAccount;
        int iZzg;
        Account account;
        int iZzg2;
        GoogleSignInAccount googleSignInAccount2 = null;
        int i = 0;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        Account account2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    GoogleSignInAccount googleSignInAccount3 = googleSignInAccount2;
                    iZzg = i;
                    account = account2;
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    googleSignInAccount = googleSignInAccount3;
                    break;
                case 2:
                    iZzg2 = i2;
                    int i3 = i;
                    account = (Account) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, Account.CREATOR);
                    googleSignInAccount = googleSignInAccount2;
                    iZzg = i3;
                    break;
                case 3:
                    account = account2;
                    iZzg2 = i2;
                    GoogleSignInAccount googleSignInAccount4 = googleSignInAccount2;
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    googleSignInAccount = googleSignInAccount4;
                    break;
                case 4:
                    googleSignInAccount = (GoogleSignInAccount) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, GoogleSignInAccount.CREATOR);
                    iZzg = i;
                    account = account2;
                    iZzg2 = i2;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    googleSignInAccount = googleSignInAccount2;
                    iZzg = i;
                    account = account2;
                    iZzg2 = i2;
                    break;
            }
            i2 = iZzg2;
            account2 = account;
            i = iZzg;
            googleSignInAccount2 = googleSignInAccount;
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new ResolveAccountRequest(i2, account2, i, googleSignInAccount2);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbY, reason: merged with bridge method [inline-methods] */
    public ResolveAccountRequest[] newArray(int i) {
        return new ResolveAccountRequest[i];
    }
}
