package org.scribe.model;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class OAuthRequest extends Request {
    private Map<String, String> oauthParameters;

    public OAuthRequest(Verb verb, String url) {
        super(verb, url);
        this.oauthParameters = new HashMap();
    }

    public void addOAuthParameter(String key, String value) {
        this.oauthParameters.put(checkKey(key), value);
    }

    private String checkKey(String key) {
        if (key.startsWith("oauth_") || key.equals("scope")) {
            return key;
        }
        throw new IllegalArgumentException(String.format("OAuth parameters must either be '%s' or start with '%s'", "scope", "oauth_"));
    }

    public Map<String, String> getOauthParameters() {
        return this.oauthParameters;
    }

    @Override // org.scribe.model.Request
    public String toString() {
        return String.format("@OAuthRequest(%s, %s)", getVerb(), getUrl());
    }
}
