package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzp;

/* loaded from: classes2.dex */
public class ResolveAccountResponse implements SafeParcelable {
    public static final Parcelable.Creator<ResolveAccountResponse> CREATOR = new zzz();
    final int mVersionCode;
    private boolean zzafR;
    IBinder zzaiS;
    private ConnectionResult zzakB;
    private boolean zzakC;

    public ResolveAccountResponse(int connectionResultStatusCode) {
        this(new ConnectionResult(connectionResultStatusCode, null));
    }

    ResolveAccountResponse(int versionCode, IBinder accountAccessorBinder, ConnectionResult connectionResult, boolean saveDefaultAccount, boolean isFromCrossClientAuth) {
        this.mVersionCode = versionCode;
        this.zzaiS = accountAccessorBinder;
        this.zzakB = connectionResult;
        this.zzafR = saveDefaultAccount;
        this.zzakC = isFromCrossClientAuth;
    }

    public ResolveAccountResponse(ConnectionResult result) {
        this(1, null, result, false, false);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResolveAccountResponse)) {
            return false;
        }
        ResolveAccountResponse resolveAccountResponse = (ResolveAccountResponse) o;
        return this.zzakB.equals(resolveAccountResponse.zzakB) && zzqH().equals(resolveAccountResponse.zzqH());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        zzz.zza(this, dest, flags);
    }

    public zzp zzqH() {
        return zzp.zza.zzaP(this.zzaiS);
    }

    public ConnectionResult zzqI() {
        return this.zzakB;
    }

    public boolean zzqJ() {
        return this.zzafR;
    }

    public boolean zzqK() {
        return this.zzakC;
    }
}
