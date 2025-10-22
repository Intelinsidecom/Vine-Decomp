package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.List;

/* loaded from: classes2.dex */
public class ScopeDetail implements SafeParcelable {
    public static final zzc CREATOR = new zzc();
    String description;
    final int version;
    String zzWR;
    String zzWS;
    String zzWT;
    String zzWU;
    List<String> zzWV;
    public FACLData zzWW;

    ScopeDetail(int version, String description, String detail, String iconBase64, String paclPickerDataBase64, String service, List<String> warnings, FACLData friendPickerData) {
        this.version = version;
        this.description = description;
        this.zzWR = detail;
        this.zzWS = iconBase64;
        this.zzWT = paclPickerDataBase64;
        this.zzWU = service;
        this.zzWV = warnings;
        this.zzWW = friendPickerData;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        zzc.zza(this, dest, flags);
    }
}
