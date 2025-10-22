package org.scribe.model;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class OAuthConfig {
    private final String apiKey;
    private final String apiSecret;
    private final String callback;
    private final OutputStream debugStream;
    private final String scope;
    private final SignatureType signatureType;

    public OAuthConfig(String key, String secret, String callback, SignatureType type, String scope, OutputStream stream) {
        this.apiKey = key;
        this.apiSecret = secret;
        this.callback = callback;
        this.signatureType = type;
        this.scope = scope;
        this.debugStream = stream;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getApiSecret() {
        return this.apiSecret;
    }

    public SignatureType getSignatureType() {
        return this.signatureType;
    }

    public String getScope() {
        return this.scope;
    }

    public boolean hasScope() {
        return this.scope != null;
    }

    public void log(String message) throws IOException {
        if (this.debugStream != null) {
            try {
                this.debugStream.write((message + "\n").getBytes("UTF8"));
            } catch (Exception e) {
                throw new RuntimeException("there were problems while writting to the debug stream", e);
            }
        }
    }
}
