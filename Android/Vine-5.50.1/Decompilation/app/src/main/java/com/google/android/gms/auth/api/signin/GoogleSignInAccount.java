package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zznl;
import com.google.android.gms.internal.zzno;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GoogleSignInAccount implements SafeParcelable {
    public static final Parcelable.Creator<GoogleSignInAccount> CREATOR = new zzc();
    public static zznl zzVs = zzno.zzrM();
    private static Comparator<Scope> zzVz = new Comparator<Scope>() { // from class: com.google.android.gms.auth.api.signin.GoogleSignInAccount.1
        @Override // java.util.Comparator
        /* renamed from: zza, reason: merged with bridge method [inline-methods] */
        public int compare(Scope scope, Scope scope2) {
            return scope.zzoM().compareTo(scope2.zzoM());
        }
    };
    final int versionCode;
    List<Scope> zzTV;
    private String zzUN;
    private String zzVt;
    private String zzVu;
    private Uri zzVv;
    private String zzVw;
    private long zzVx;
    private String zzVy;
    private String zzxX;

    GoogleSignInAccount(int versionCode, String id, String idToken, String email, String displayName, Uri photoUrl, String serverAuthCode, long expirationTimeSecs, String obfuscatedIdentifier, List<Scope> grantedScopes) {
        this.versionCode = versionCode;
        this.zzxX = id;
        this.zzUN = idToken;
        this.zzVt = email;
        this.zzVu = displayName;
        this.zzVv = photoUrl;
        this.zzVw = serverAuthCode;
        this.zzVx = expirationTimeSecs;
        this.zzVy = obfuscatedIdentifier;
        this.zzTV = grantedScopes;
    }

    public static GoogleSignInAccount zza(String str, String str2, String str3, String str4, Uri uri, Long l, String str5, Set<Scope> set) {
        if (l == null) {
            l = Long.valueOf(zzVs.currentTimeMillis() / 1000);
        }
        return new GoogleSignInAccount(2, str, str2, str3, str4, uri, null, l.longValue(), zzx.zzcG(str5), new ArrayList((Collection) zzx.zzy(set)));
    }

    public static GoogleSignInAccount zzbE(String str) throws JSONException, NumberFormatException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(str);
        String strOptString = jSONObject.optString("photoUrl", null);
        Uri uri = !TextUtils.isEmpty(strOptString) ? Uri.parse(strOptString) : null;
        long j = Long.parseLong(jSONObject.getString("expirationTime"));
        HashSet hashSet = new HashSet();
        JSONArray jSONArray = jSONObject.getJSONArray("grantedScopes");
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            hashSet.add(new Scope(jSONArray.getString(i)));
        }
        return zza(jSONObject.optString("id"), jSONObject.optString("tokenId", null), jSONObject.optString("email", null), jSONObject.optString("displayName", null), uri, Long.valueOf(j), jSONObject.getString("obfuscatedIdentifier"), hashSet).zzbF(jSONObject.optString("serverAuthCode", null));
    }

    private JSONObject zzms() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            if (getId() != null) {
                jSONObject.put("id", getId());
            }
            if (getIdToken() != null) {
                jSONObject.put("tokenId", getIdToken());
            }
            if (getEmail() != null) {
                jSONObject.put("email", getEmail());
            }
            if (getDisplayName() != null) {
                jSONObject.put("displayName", getDisplayName());
            }
            if (getPhotoUrl() != null) {
                jSONObject.put("photoUrl", getPhotoUrl().toString());
            }
            if (getServerAuthCode() != null) {
                jSONObject.put("serverAuthCode", getServerAuthCode());
            }
            jSONObject.put("expirationTime", this.zzVx);
            jSONObject.put("obfuscatedIdentifier", zzmw());
            JSONArray jSONArray = new JSONArray();
            Collections.sort(this.zzTV, zzVz);
            Iterator<Scope> it = this.zzTV.iterator();
            while (it.hasNext()) {
                jSONArray.put(it.next().zzoM());
            }
            jSONObject.put("grantedScopes", jSONArray);
            return jSONObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof GoogleSignInAccount) {
            return ((GoogleSignInAccount) obj).zzmr().equals(zzmr());
        }
        return false;
    }

    public String getDisplayName() {
        return this.zzVu;
    }

    public String getEmail() {
        return this.zzVt;
    }

    public String getId() {
        return this.zzxX;
    }

    public String getIdToken() {
        return this.zzUN;
    }

    public Uri getPhotoUrl() {
        return this.zzVv;
    }

    public String getServerAuthCode() {
        return this.zzVw;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }

    public GoogleSignInAccount zzbF(String str) {
        this.zzVw = str;
        return this;
    }

    public String zzmr() {
        return zzms().toString();
    }

    public long zzmv() {
        return this.zzVx;
    }

    public String zzmw() {
        return this.zzVy;
    }
}
