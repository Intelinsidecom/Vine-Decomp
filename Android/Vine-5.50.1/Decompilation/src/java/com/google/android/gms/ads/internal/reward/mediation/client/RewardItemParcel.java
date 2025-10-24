package com.google.android.gms.ads.internal.reward.mediation.client;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public final class RewardItemParcel implements SafeParcelable {
    public static final zzc CREATOR = new zzc();
    public final String type;
    public final int versionCode;
    public final int zzJD;

    public RewardItemParcel(int versionCode, String type, int amount) {
        this.versionCode = versionCode;
        this.type = type;
        this.zzJD = amount;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }
}
