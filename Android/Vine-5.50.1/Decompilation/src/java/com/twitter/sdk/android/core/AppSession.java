package com.twitter.sdk.android.core;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.sdk.android.core.internal.oauth.OAuth2Token;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.persistence.SerializationStrategy;

/* loaded from: classes.dex */
public class AppSession extends Session<OAuth2Token> {

    static class Serializer implements SerializationStrategy<AppSession> {
        private final Gson gson = new GsonBuilder().registerTypeAdapter(OAuth2Token.class, new AuthTokenAdapter()).create();

        @Override // io.fabric.sdk.android.services.persistence.SerializationStrategy
        public String serialize(AppSession session) {
            if (session != null && session.getAuthToken() != null) {
                try {
                    return this.gson.toJson(session);
                } catch (Exception e) {
                    Fabric.getLogger().d("Twitter", "Failed to serialize session " + e.getMessage());
                }
            }
            return "";
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.fabric.sdk.android.services.persistence.SerializationStrategy
        public AppSession deserialize(String serializedSession) {
            if (!TextUtils.isEmpty(serializedSession)) {
                try {
                    return (AppSession) this.gson.fromJson(serializedSession, AppSession.class);
                } catch (Exception e) {
                    Fabric.getLogger().d("Twitter", "Failed to deserialize session " + e.getMessage());
                }
            }
            return null;
        }
    }
}
