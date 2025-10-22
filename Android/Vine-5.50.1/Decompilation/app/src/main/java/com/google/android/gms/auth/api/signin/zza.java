package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes2.dex */
public class zza implements Parcelable.Creator<EmailSignInOptions> {
    static void zza(EmailSignInOptions emailSignInOptions, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, emailSignInOptions.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) emailSignInOptions.zzmo(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, emailSignInOptions.zzmq(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable) emailSignInOptions.zzmp(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzP, reason: merged with bridge method [inline-methods] */
    public EmailSignInOptions createFromParcel(Parcel parcel) {
        Uri uri;
        String strZzp;
        Uri uri2;
        int iZzg;
        Uri uri3 = null;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int i = 0;
        String str = null;
        Uri uri4 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    Uri uri5 = uri3;
                    strZzp = str;
                    uri2 = uri4;
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    uri = uri5;
                    break;
                case 2:
                    iZzg = i;
                    String str2 = str;
                    uri2 = (Uri) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, Uri.CREATOR);
                    uri = uri3;
                    strZzp = str2;
                    break;
                case 3:
                    uri2 = uri4;
                    iZzg = i;
                    Uri uri6 = uri3;
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    uri = uri6;
                    break;
                case 4:
                    uri = (Uri) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, Uri.CREATOR);
                    strZzp = str;
                    uri2 = uri4;
                    iZzg = i;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    uri = uri3;
                    strZzp = str;
                    uri2 = uri4;
                    iZzg = i;
                    break;
            }
            i = iZzg;
            uri4 = uri2;
            str = strZzp;
            uri3 = uri;
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new EmailSignInOptions(i, uri4, str, uri3);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaK, reason: merged with bridge method [inline-methods] */
    public EmailSignInOptions[] newArray(int i) {
        return new EmailSignInOptions[i];
    }
}
