package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Set;

/* loaded from: classes2.dex */
public class AuthAccountRequest implements SafeParcelable {
    public static final Parcelable.Creator<AuthAccountRequest> CREATOR = new zzc();
    final int mVersionCode;
    final Scope[] zzaem;
    final IBinder zzaiS;

    AuthAccountRequest(int versionCode, IBinder accountAccessorBinder, Scope[] scopes) {
        this.mVersionCode = versionCode;
        this.zzaiS = accountAccessorBinder;
        this.zzaem = scopes;
    }

    public AuthAccountRequest(zzp accountAccessor, Set<Scope> scopes) {
        this(1, accountAccessor.asBinder(), (Scope[]) scopes.toArray(new Scope[scopes.size()]));
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
