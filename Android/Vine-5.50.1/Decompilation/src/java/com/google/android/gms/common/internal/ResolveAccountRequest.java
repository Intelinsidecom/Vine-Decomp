package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: classes2.dex */
public class ResolveAccountRequest implements SafeParcelable {
    public static final Parcelable.Creator<ResolveAccountRequest> CREATOR = new zzy();
    final int mVersionCode;
    private final Account zzSo;
    private final GoogleSignInAccount zzakA;
    private final int zzakz;

    ResolveAccountRequest(int versionCode, Account account, int sessionId, GoogleSignInAccount signInAccountHint) {
        this.mVersionCode = versionCode;
        this.zzSo = account;
        this.zzakz = sessionId;
        this.zzakA = signInAccountHint;
    }

    public ResolveAccountRequest(Account account, int sessionId, GoogleSignInAccount signInAccountHint) {
        this(2, account, sessionId, signInAccountHint);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Account getAccount() {
        return this.zzSo;
    }

    public int getSessionId() {
        return this.zzakz;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        zzy.zza(this, dest, flags);
    }

    public GoogleSignInAccount zzqG() {
        return this.zzakA;
    }
}
