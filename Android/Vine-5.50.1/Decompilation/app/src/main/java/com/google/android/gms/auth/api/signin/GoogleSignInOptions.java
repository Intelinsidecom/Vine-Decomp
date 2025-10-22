package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.internal.zze;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes2.dex */
public class GoogleSignInOptions implements Api.ApiOptions.Optional, SafeParcelable {
    final int versionCode;
    private Account zzSo;
    private boolean zzVD;
    private final boolean zzVE;
    private final boolean zzVF;
    private String zzVG;
    private final ArrayList<Scope> zzVr;
    public static final Scope zzVA = new Scope("profile");
    public static final Scope zzVB = new Scope("email");
    public static final Scope zzVC = new Scope("openid");
    public static final GoogleSignInOptions DEFAULT_SIGN_IN = new Builder().requestId().requestProfile().build();
    public static final Parcelable.Creator<GoogleSignInOptions> CREATOR = new zzd();

    public static final class Builder {
        private Account zzSo;
        private boolean zzVD;
        private boolean zzVE;
        private boolean zzVF;
        private String zzVG;
        private Set<Scope> zzVH = new HashSet();

        public GoogleSignInOptions build() {
            return new GoogleSignInOptions(this.zzVH, this.zzSo, this.zzVD, this.zzVE, this.zzVF, this.zzVG);
        }

        public Builder requestId() {
            this.zzVH.add(GoogleSignInOptions.zzVC);
            return this;
        }

        public Builder requestProfile() {
            this.zzVH.add(GoogleSignInOptions.zzVA);
            return this;
        }
    }

    GoogleSignInOptions(int versionCode, ArrayList<Scope> scopes, Account account, boolean idTokenRequested, boolean serverAuthCodeRequested, boolean forceCodeForRefreshToken, String serverClientId) {
        this.versionCode = versionCode;
        this.zzVr = scopes;
        this.zzSo = account;
        this.zzVD = idTokenRequested;
        this.zzVE = serverAuthCodeRequested;
        this.zzVF = forceCodeForRefreshToken;
        this.zzVG = serverClientId;
    }

    private GoogleSignInOptions(Set<Scope> scopes, Account account, boolean idTokenRequested, boolean serverAuthCodeRequested, boolean forceCodeForRefreshToken, String serverClientId) {
        this(1, (ArrayList<Scope>) new ArrayList(scopes), account, idTokenRequested, serverAuthCodeRequested, forceCodeForRefreshToken, serverClientId);
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
            GoogleSignInOptions googleSignInOptions = (GoogleSignInOptions) obj;
            if (this.zzVr.size() != googleSignInOptions.zzmu().size() || !this.zzVr.containsAll(googleSignInOptions.zzmu())) {
                return false;
            }
            if (this.zzSo == null) {
                if (googleSignInOptions.getAccount() != null) {
                    return false;
                }
            } else if (!this.zzSo.equals(googleSignInOptions.getAccount())) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzVG)) {
                if (!TextUtils.isEmpty(googleSignInOptions.zzmB())) {
                    return false;
                }
            } else if (!this.zzVG.equals(googleSignInOptions.zzmB())) {
                return false;
            }
            if (this.zzVF == googleSignInOptions.zzmA() && this.zzVD == googleSignInOptions.zzmy()) {
                return this.zzVE == googleSignInOptions.zzmz();
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public Account getAccount() {
        return this.zzSo;
    }

    public int hashCode() {
        ArrayList arrayList = new ArrayList();
        Iterator<Scope> it = this.zzVr.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().zzoM());
        }
        Collections.sort(arrayList);
        return new zze().zzo(arrayList).zzo(this.zzSo).zzo(this.zzVG).zzP(this.zzVF).zzP(this.zzVD).zzP(this.zzVE).zzmM();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzd.zza(this, out, flags);
    }

    public boolean zzmA() {
        return this.zzVF;
    }

    public String zzmB() {
        return this.zzVG;
    }

    public ArrayList<Scope> zzmu() {
        return new ArrayList<>(this.zzVr);
    }

    public boolean zzmy() {
        return this.zzVD;
    }

    public boolean zzmz() {
        return this.zzVE;
    }
}
