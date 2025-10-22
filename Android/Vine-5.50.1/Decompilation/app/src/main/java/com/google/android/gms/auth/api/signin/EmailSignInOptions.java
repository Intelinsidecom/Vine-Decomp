package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Patterns;
import com.google.android.gms.auth.api.signin.internal.zze;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

/* loaded from: classes2.dex */
public class EmailSignInOptions implements SafeParcelable {
    public static final Parcelable.Creator<EmailSignInOptions> CREATOR = new zza();
    final int versionCode;
    private final Uri zzVo;
    private String zzVp;
    private Uri zzVq;

    EmailSignInOptions(int versionCode, Uri serverWidgetUrl, String modeQueryName, Uri tosUrl) {
        zzx.zzb(serverWidgetUrl, "Server widget url cannot be null in order to use email/password sign in.");
        zzx.zzh(serverWidgetUrl.toString(), "Server widget url cannot be null in order to use email/password sign in.");
        zzx.zzb(Patterns.WEB_URL.matcher(serverWidgetUrl.toString()).matches(), "Invalid server widget url");
        this.versionCode = versionCode;
        this.zzVo = serverWidgetUrl;
        this.zzVp = modeQueryName;
        this.zzVq = tosUrl;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj != null) {
            try {
                EmailSignInOptions emailSignInOptions = (EmailSignInOptions) obj;
                if (this.zzVo.equals(emailSignInOptions.zzmo())) {
                    if (this.zzVq == null) {
                        if (emailSignInOptions.zzmp() == null) {
                            if (TextUtils.isEmpty(this.zzVp) ? this.zzVp.equals(emailSignInOptions.zzmq()) : TextUtils.isEmpty(emailSignInOptions.zzmq())) {
                            }
                        }
                    } else if (this.zzVq.equals(emailSignInOptions.zzmp())) {
                        z = TextUtils.isEmpty(this.zzVp) ? true : true;
                    }
                }
            } catch (ClassCastException e) {
            }
        }
        return z;
    }

    public int hashCode() {
        return new zze().zzo(this.zzVo).zzo(this.zzVq).zzo(this.zzVp).zzmM();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zza.zza(this, out, flags);
    }

    public Uri zzmo() {
        return this.zzVo;
    }

    public Uri zzmp() {
        return this.zzVq;
    }

    public String zzmq() {
        return this.zzVp;
    }
}
