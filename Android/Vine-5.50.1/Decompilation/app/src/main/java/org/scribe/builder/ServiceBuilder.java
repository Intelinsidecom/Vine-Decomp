package org.scribe.builder;

import java.io.OutputStream;
import org.scribe.builder.api.Api;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.Preconditions;

/* loaded from: classes.dex */
public class ServiceBuilder {
    private Api api;
    private String apiKey;
    private String apiSecret;
    private String scope;
    private String callback = "oob";
    private SignatureType signatureType = SignatureType.Header;
    private OutputStream debugStream = null;

    public ServiceBuilder provider(Class<? extends Api> apiClass) {
        this.api = createApi(apiClass);
        return this;
    }

    private Api createApi(Class<? extends Api> apiClass) throws IllegalAccessException, InstantiationException {
        Preconditions.checkNotNull(apiClass, "Api class cannot be null");
        try {
            Api api = apiClass.newInstance();
            return api;
        } catch (Exception e) {
            throw new OAuthException("Error while creating the Api object", e);
        }
    }

    public ServiceBuilder apiKey(String apiKey) {
        Preconditions.checkEmptyString(apiKey, "Invalid Api key");
        this.apiKey = apiKey;
        return this;
    }

    public ServiceBuilder apiSecret(String apiSecret) {
        Preconditions.checkEmptyString(apiSecret, "Invalid Api secret");
        this.apiSecret = apiSecret;
        return this;
    }

    public OAuthService build() {
        Preconditions.checkNotNull(this.api, "You must specify a valid api through the provider() method");
        Preconditions.checkEmptyString(this.apiKey, "You must provide an api key");
        Preconditions.checkEmptyString(this.apiSecret, "You must provide an api secret");
        return this.api.createService(new OAuthConfig(this.apiKey, this.apiSecret, this.callback, this.signatureType, this.scope, this.debugStream));
    }
}
