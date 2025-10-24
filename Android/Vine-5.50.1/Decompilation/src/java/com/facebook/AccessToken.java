package com.facebook;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public final class AccessToken implements Parcelable {
    private final String applicationId;
    private final Set<String> declinedPermissions;
    private final Date expires;
    private final Date lastRefresh;
    private final Set<String> permissions;
    private final AccessTokenSource source;
    private final String token;
    private final String userId;
    private static final Date MAX_DATE = new Date(Long.MAX_VALUE);
    private static final Date DEFAULT_EXPIRATION_TIME = MAX_DATE;
    private static final Date DEFAULT_LAST_REFRESH_TIME = new Date();
    private static final AccessTokenSource DEFAULT_ACCESS_TOKEN_SOURCE = AccessTokenSource.FACEBOOK_APPLICATION_WEB;
    public static final Parcelable.Creator<AccessToken> CREATOR = new Parcelable.Creator() { // from class: com.facebook.AccessToken.2
        @Override // android.os.Parcelable.Creator
        public AccessToken createFromParcel(Parcel source) {
            return new AccessToken(source);
        }

        @Override // android.os.Parcelable.Creator
        public AccessToken[] newArray(int size) {
            return new AccessToken[size];
        }
    };

    public AccessToken(String accessToken, String applicationId, String userId, Collection<String> permissions, Collection<String> declinedPermissions, AccessTokenSource accessTokenSource, Date expirationTime, Date lastRefreshTime) {
        Validate.notNullOrEmpty(accessToken, "accessToken");
        Validate.notNullOrEmpty(applicationId, "applicationId");
        Validate.notNullOrEmpty(userId, "userId");
        this.expires = expirationTime == null ? DEFAULT_EXPIRATION_TIME : expirationTime;
        this.permissions = Collections.unmodifiableSet(permissions != null ? new HashSet(permissions) : new HashSet());
        this.declinedPermissions = Collections.unmodifiableSet(declinedPermissions != null ? new HashSet(declinedPermissions) : new HashSet());
        this.token = accessToken;
        this.source = accessTokenSource == null ? DEFAULT_ACCESS_TOKEN_SOURCE : accessTokenSource;
        this.lastRefresh = lastRefreshTime == null ? DEFAULT_LAST_REFRESH_TIME : lastRefreshTime;
        this.applicationId = applicationId;
        this.userId = userId;
    }

    public static AccessToken getCurrentAccessToken() {
        return AccessTokenManager.getInstance().getCurrentAccessToken();
    }

    public static void setCurrentAccessToken(AccessToken accessToken) {
        AccessTokenManager.getInstance().setCurrentAccessToken(accessToken);
    }

    public String getToken() {
        return this.token;
    }

    public Date getExpires() {
        return this.expires;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    public Set<String> getDeclinedPermissions() {
        return this.declinedPermissions;
    }

    public AccessTokenSource getSource() {
        return this.source;
    }

    public Date getLastRefresh() {
        return this.lastRefresh;
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{AccessToken");
        builder.append(" token:").append(tokenToString());
        appendPermissions(builder);
        builder.append("}");
        return builder.toString();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AccessToken)) {
            return false;
        }
        AccessToken o = (AccessToken) other;
        return this.expires.equals(o.expires) && this.permissions.equals(o.permissions) && this.declinedPermissions.equals(o.declinedPermissions) && this.token.equals(o.token) && this.source == o.source && this.lastRefresh.equals(o.lastRefresh) && (this.applicationId != null ? this.applicationId.equals(o.applicationId) : o.applicationId == null) && this.userId.equals(o.userId);
    }

    public int hashCode() {
        int result = this.expires.hashCode() + 527;
        return (((((((((((((result * 31) + this.permissions.hashCode()) * 31) + this.declinedPermissions.hashCode()) * 31) + this.token.hashCode()) * 31) + this.source.hashCode()) * 31) + this.lastRefresh.hashCode()) * 31) + (this.applicationId == null ? 0 : this.applicationId.hashCode())) * 31) + this.userId.hashCode();
    }

    static AccessToken createFromLegacyCache(Bundle bundle) throws JSONException {
        List<String> permissions = getPermissionsFromBundle(bundle, "com.facebook.TokenCachingStrategy.Permissions");
        List<String> declinedPermissions = getPermissionsFromBundle(bundle, "com.facebook.TokenCachingStrategy.DeclinedPermissions");
        String applicationId = LegacyTokenHelper.getApplicationId(bundle);
        if (Utility.isNullOrEmpty(applicationId)) {
            applicationId = FacebookSdk.getApplicationId();
        }
        String tokenString = LegacyTokenHelper.getToken(bundle);
        JSONObject userInfo = Utility.awaitGetGraphMeRequestWithCache(tokenString);
        try {
            String userId = userInfo.getString("id");
            return new AccessToken(tokenString, applicationId, userId, permissions, declinedPermissions, LegacyTokenHelper.getSource(bundle), LegacyTokenHelper.getDate(bundle, "com.facebook.TokenCachingStrategy.ExpirationDate"), LegacyTokenHelper.getDate(bundle, "com.facebook.TokenCachingStrategy.LastRefreshDate"));
        } catch (JSONException e) {
            return null;
        }
    }

    static List<String> getPermissionsFromBundle(Bundle bundle, String key) {
        List<String> originalPermissions = bundle.getStringArrayList(key);
        if (originalPermissions == null) {
            List<String> permissions = Collections.emptyList();
            return permissions;
        }
        List<String> permissions2 = Collections.unmodifiableList(new ArrayList(originalPermissions));
        return permissions2;
    }

    public boolean isExpired() {
        return new Date().after(this.expires);
    }

    JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", 1);
        jsonObject.put("token", this.token);
        jsonObject.put("expires_at", this.expires.getTime());
        JSONArray permissionsArray = new JSONArray((Collection) this.permissions);
        jsonObject.put("permissions", permissionsArray);
        JSONArray declinedPermissionsArray = new JSONArray((Collection) this.declinedPermissions);
        jsonObject.put("declined_permissions", declinedPermissionsArray);
        jsonObject.put("last_refresh", this.lastRefresh.getTime());
        jsonObject.put("source", this.source.name());
        jsonObject.put("application_id", this.applicationId);
        jsonObject.put("user_id", this.userId);
        return jsonObject;
    }

    static AccessToken createFromJSONObject(JSONObject jsonObject) throws JSONException {
        int version = jsonObject.getInt("version");
        if (version > 1) {
            throw new FacebookException("Unknown AccessToken serialization format.");
        }
        String token = jsonObject.getString("token");
        Date expiresAt = new Date(jsonObject.getLong("expires_at"));
        JSONArray permissionsArray = jsonObject.getJSONArray("permissions");
        JSONArray declinedPermissionsArray = jsonObject.getJSONArray("declined_permissions");
        Date lastRefresh = new Date(jsonObject.getLong("last_refresh"));
        AccessTokenSource source = AccessTokenSource.valueOf(jsonObject.getString("source"));
        String applicationId = jsonObject.getString("application_id");
        String userId = jsonObject.getString("user_id");
        return new AccessToken(token, applicationId, userId, Utility.jsonArrayToStringList(permissionsArray), Utility.jsonArrayToStringList(declinedPermissionsArray), source, expiresAt, lastRefresh);
    }

    private String tokenToString() {
        if (this.token == null) {
            return "null";
        }
        if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.INCLUDE_ACCESS_TOKENS)) {
            return this.token;
        }
        return "ACCESS_TOKEN_REMOVED";
    }

    private void appendPermissions(StringBuilder builder) {
        builder.append(" permissions:");
        if (this.permissions == null) {
            builder.append("null");
            return;
        }
        builder.append("[");
        builder.append(TextUtils.join(", ", this.permissions));
        builder.append("]");
    }

    AccessToken(Parcel parcel) {
        this.expires = new Date(parcel.readLong());
        ArrayList<String> permissionsList = new ArrayList<>();
        parcel.readStringList(permissionsList);
        this.permissions = Collections.unmodifiableSet(new HashSet(permissionsList));
        permissionsList.clear();
        parcel.readStringList(permissionsList);
        this.declinedPermissions = Collections.unmodifiableSet(new HashSet(permissionsList));
        this.token = parcel.readString();
        this.source = AccessTokenSource.valueOf(parcel.readString());
        this.lastRefresh = new Date(parcel.readLong());
        this.applicationId = parcel.readString();
        this.userId = parcel.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.expires.getTime());
        dest.writeStringList(new ArrayList(this.permissions));
        dest.writeStringList(new ArrayList(this.declinedPermissions));
        dest.writeString(this.token);
        dest.writeString(this.source.name());
        dest.writeLong(this.lastRefresh.getTime());
        dest.writeString(this.applicationId);
        dest.writeString(this.userId);
    }
}
