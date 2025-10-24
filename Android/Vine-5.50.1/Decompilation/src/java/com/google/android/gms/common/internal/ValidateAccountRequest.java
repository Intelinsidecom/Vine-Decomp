package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: classes2.dex */
public class ValidateAccountRequest implements SafeParcelable {
    public static final Parcelable.Creator<ValidateAccountRequest> CREATOR = new zzae();
    final int mVersionCode;
    private final String zzUr;
    private final Scope[] zzaem;
    final IBinder zzaiS;
    private final int zzakH;
    private final Bundle zzakI;

    ValidateAccountRequest(int versionCode, int clientVersion, IBinder accountAccessorBinder, Scope[] scopes, Bundle extraArgs, String callingPackage) {
        this.mVersionCode = versionCode;
        this.zzakH = clientVersion;
        this.zzaiS = accountAccessorBinder;
        this.zzaem = scopes;
        this.zzakI = extraArgs;
        this.zzUr = callingPackage;
    }

    public ValidateAccountRequest(zzp accountAccessor, Scope[] scopes, String callingPackage, Bundle extraArgs) {
        this(1, GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE, accountAccessor == null ? null : accountAccessor.asBinder(), scopes, extraArgs, callingPackage);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getCallingPackage() {
        return this.zzUr;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        zzae.zza(this, dest, flags);
    }

    public Scope[] zzqN() {
        return this.zzaem;
    }

    public int zzqO() {
        return this.zzakH;
    }

    public Bundle zzqP() {
        return this.zzakI;
    }
}
