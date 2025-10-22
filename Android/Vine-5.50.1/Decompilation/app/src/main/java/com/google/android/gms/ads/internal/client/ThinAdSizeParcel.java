package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class ThinAdSizeParcel extends AdSizeParcel {
    public ThinAdSizeParcel(AdSizeParcel originalAdSize) {
        super(originalAdSize.versionCode, originalAdSize.zztV, originalAdSize.height, originalAdSize.heightPixels, originalAdSize.zztW, originalAdSize.width, originalAdSize.widthPixels, originalAdSize.zztX, originalAdSize.zztY, originalAdSize.zztZ, originalAdSize.zzua);
    }

    @Override // com.google.android.gms.ads.internal.client.AdSizeParcel, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, this.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, this.zztV, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, this.height);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 6, this.width);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }
}
