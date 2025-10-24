package com.google.android.gms.common.server;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: classes2.dex */
public class FavaDiagnosticsEntity implements SafeParcelable {
    public static final zza CREATOR = new zza();
    final int mVersionCode;
    public final String zzakM;
    public final int zzakN;

    public FavaDiagnosticsEntity(int versionCode, String namespace, int typeNum) {
        this.mVersionCode = versionCode;
        this.zzakM = namespace;
        this.zzakN = typeNum;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zza.zza(this, out, flags);
    }
}
