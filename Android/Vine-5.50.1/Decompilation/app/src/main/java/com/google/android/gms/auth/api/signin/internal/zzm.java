package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.EmailSignInOptions;
import com.google.android.gms.auth.api.signin.FacebookSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

/* loaded from: classes2.dex */
public class zzm implements Parcelable.Creator<SignInConfiguration> {
    static void zza(SignInConfiguration signInConfiguration, Parcel parcel, int i) {
        int iZzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, signInConfiguration.versionCode);
        zzb.zza(parcel, 2, signInConfiguration.zzmP(), false);
        zzb.zza(parcel, 3, signInConfiguration.zzmB(), false);
        zzb.zza(parcel, 4, (Parcelable) signInConfiguration.zzmQ(), i, false);
        zzb.zza(parcel, 5, (Parcelable) signInConfiguration.zzmR(), i, false);
        zzb.zza(parcel, 6, (Parcelable) signInConfiguration.zzmS(), i, false);
        zzb.zza(parcel, 7, signInConfiguration.zzmT(), false);
        zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzV, reason: merged with bridge method [inline-methods] */
    public SignInConfiguration createFromParcel(Parcel parcel) {
        String strZzp = null;
        int iZzau = zza.zzau(parcel);
        int iZzg = 0;
        FacebookSignInOptions facebookSignInOptions = null;
        GoogleSignInOptions googleSignInOptions = null;
        EmailSignInOptions emailSignInOptions = null;
        String strZzp2 = null;
        String strZzp3 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = zza.zzat(parcel);
            switch (zza.zzcc(iZzat)) {
                case 1:
                    iZzg = zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    strZzp3 = zza.zzp(parcel, iZzat);
                    break;
                case 3:
                    strZzp2 = zza.zzp(parcel, iZzat);
                    break;
                case 4:
                    emailSignInOptions = (EmailSignInOptions) zza.zza(parcel, iZzat, EmailSignInOptions.CREATOR);
                    break;
                case 5:
                    googleSignInOptions = (GoogleSignInOptions) zza.zza(parcel, iZzat, GoogleSignInOptions.CREATOR);
                    break;
                case 6:
                    facebookSignInOptions = (FacebookSignInOptions) zza.zza(parcel, iZzat, FacebookSignInOptions.CREATOR);
                    break;
                case 7:
                    strZzp = zza.zzp(parcel, iZzat);
                    break;
                default:
                    zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new SignInConfiguration(iZzg, strZzp3, strZzp2, emailSignInOptions, googleSignInOptions, facebookSignInOptions, strZzp);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaQ, reason: merged with bridge method [inline-methods] */
    public SignInConfiguration[] newArray(int i) {
        return new SignInConfiguration[i];
    }
}
