package com.google.android.gms.auth.api.signin;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Collections;

/* loaded from: classes2.dex */
public class FacebookSignInOptions implements SafeParcelable {
    public static final Parcelable.Creator<FacebookSignInOptions> CREATOR = new zzb();
    private Intent mIntent;
    final int versionCode;
    private final ArrayList<String> zzVr;

    public FacebookSignInOptions() {
        this(1, null, new ArrayList());
    }

    FacebookSignInOptions(int versionCode, Intent intent, ArrayList<String> scopes) {
        this.versionCode = versionCode;
        this.mIntent = intent;
        this.zzVr = scopes;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            FacebookSignInOptions facebookSignInOptions = (FacebookSignInOptions) obj;
            if (this.zzVr.size() == facebookSignInOptions.zzmu().size()) {
                return this.zzVr.containsAll(facebookSignInOptions.zzmu());
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        Collections.sort(this.zzVr);
        return this.zzVr.hashCode();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }

    public Intent zzmt() {
        return this.mIntent;
    }

    public ArrayList<String> zzmu() {
        return new ArrayList<>(this.zzVr);
    }
}
