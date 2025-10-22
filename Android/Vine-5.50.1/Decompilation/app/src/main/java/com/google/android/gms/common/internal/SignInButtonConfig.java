package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: classes2.dex */
public class SignInButtonConfig implements SafeParcelable {
    public static final Parcelable.Creator<SignInButtonConfig> CREATOR = new zzaa();
    final int mVersionCode;
    private final Scope[] zzaem;
    private final int zzakD;
    private final int zzakE;

    SignInButtonConfig(int versionCode, int buttonSize, int colorScheme, Scope[] scopes) {
        this.mVersionCode = versionCode;
        this.zzakD = buttonSize;
        this.zzakE = colorScheme;
        this.zzaem = scopes;
    }

    public SignInButtonConfig(int buttonSize, int colorScheme, Scope[] scopes) {
        this(1, buttonSize, colorScheme, scopes);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        zzaa.zza(this, dest, flags);
    }

    public int zzqL() {
        return this.zzakD;
    }

    public int zzqM() {
        return this.zzakE;
    }

    public Scope[] zzqN() {
        return this.zzaem;
    }
}
