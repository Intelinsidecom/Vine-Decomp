package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;

/* loaded from: classes2.dex */
public class FACLConfig implements SafeParcelable {
    public static final zza CREATOR = new zza();
    final int version;
    boolean zzWH;
    String zzWI;
    boolean zzWJ;
    boolean zzWK;
    boolean zzWL;
    boolean zzWM;

    FACLConfig(int version, boolean isAllCirclesVisible, String visibleEdges, boolean isAllContactsVisible, boolean showCircles, boolean showContacts, boolean hasShowCircles) {
        this.version = version;
        this.zzWH = isAllCirclesVisible;
        this.zzWI = visibleEdges;
        this.zzWJ = isAllContactsVisible;
        this.zzWK = showCircles;
        this.zzWL = showContacts;
        this.zzWM = hasShowCircles;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof FACLConfig)) {
            return false;
        }
        FACLConfig fACLConfig = (FACLConfig) o;
        return this.zzWH == fACLConfig.zzWH && TextUtils.equals(this.zzWI, fACLConfig.zzWI) && this.zzWJ == fACLConfig.zzWJ && this.zzWK == fACLConfig.zzWK && this.zzWL == fACLConfig.zzWL && this.zzWM == fACLConfig.zzWM;
    }

    public int hashCode() {
        return zzw.hashCode(Boolean.valueOf(this.zzWH), this.zzWI, Boolean.valueOf(this.zzWJ), Boolean.valueOf(this.zzWK), Boolean.valueOf(this.zzWL), Boolean.valueOf(this.zzWM));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        zza.zza(this, dest, flags);
    }
}
