package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public class CheckServerAuthResult implements SafeParcelable {
    public static final Parcelable.Creator<CheckServerAuthResult> CREATOR = new zzc();
    final int mVersionCode;
    final boolean zzbbU;
    final List<Scope> zzbbV;

    CheckServerAuthResult(int versionCode, boolean newAuthCodeRequired, List<Scope> additionalScopes) {
        this.mVersionCode = versionCode;
        this.zzbbU = newAuthCodeRequired;
        this.zzbbV = additionalScopes;
    }

    public CheckServerAuthResult(boolean newAuthCodeRequired, Set<Scope> additionalScopes) {
        this(1, newAuthCodeRequired, zze(additionalScopes));
    }

    private static List<Scope> zze(Set<Scope> set) {
        return set == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(set));
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
