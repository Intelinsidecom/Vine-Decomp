package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

/* loaded from: classes2.dex */
public class SignInAccount implements SafeParcelable {
    public static final Parcelable.Creator<SignInAccount> CREATOR = new zzf();
    final int versionCode;
    private String zzJg;
    private String zzUN;
    private String zzVL;
    private GoogleSignInAccount zzVO;
    private String zzVP;
    private String zzVt;
    private String zzVu;
    private Uri zzVv;

    SignInAccount(int versionCode, String providerId, String idToken, String email, String displayName, Uri photoUrl, GoogleSignInAccount googleSignInAccount, String userId, String refreshToken) {
        this.versionCode = versionCode;
        this.zzVt = zzx.zzh(email, "Email cannot be empty.");
        this.zzVu = displayName;
        this.zzVv = photoUrl;
        this.zzVL = providerId;
        this.zzUN = idToken;
        this.zzVO = googleSignInAccount;
        this.zzJg = zzx.zzcG(userId);
        this.zzVP = refreshToken;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getDisplayName() {
        return this.zzVu;
    }

    public String getEmail() {
        return this.zzVt;
    }

    public String getIdToken() {
        return this.zzUN;
    }

    public Uri getPhotoUrl() {
        return this.zzVv;
    }

    public String getUserId() {
        return this.zzJg;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }

    String zzmC() {
        return this.zzVL;
    }

    public GoogleSignInAccount zzmD() {
        return this.zzVO;
    }

    public String zzmE() {
        return this.zzVP;
    }
}
