package com.twitter.sdk.android.core;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.persistence.SerializationStrategy;

/* loaded from: classes.dex */
public class TwitterSession extends Session<TwitterAuthToken> {

    @SerializedName("user_name")
    private final String userName;

    public String getUserName() {
        return this.userName;
    }

    @Override // com.twitter.sdk.android.core.Session
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TwitterSession that = (TwitterSession) o;
        if (this.userName != null) {
            if (this.userName.equals(that.userName)) {
                return true;
            }
        } else if (that.userName == null) {
            return true;
        }
        return false;
    }

    @Override // com.twitter.sdk.android.core.Session
    public int hashCode() {
        int result = super.hashCode();
        return (result * 31) + (this.userName != null ? this.userName.hashCode() : 0);
    }

    static class Serializer implements SerializationStrategy<TwitterSession> {
        private final Gson gson = new Gson();

        @Override // io.fabric.sdk.android.services.persistence.SerializationStrategy
        public String serialize(TwitterSession session) {
            if (session != null && session.getAuthToken() != null) {
                try {
                    return this.gson.toJson(session);
                } catch (Exception e) {
                    Fabric.getLogger().d("Twitter", e.getMessage());
                }
            }
            return "";
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.fabric.sdk.android.services.persistence.SerializationStrategy
        public TwitterSession deserialize(String serializedSession) {
            if (!TextUtils.isEmpty(serializedSession)) {
                try {
                    return (TwitterSession) this.gson.fromJson(serializedSession, TwitterSession.class);
                } catch (Exception e) {
                    Fabric.getLogger().d("Twitter", e.getMessage());
                }
            }
            return null;
        }
    }
}
