package com.digits.sdk.android;

import android.net.Uri;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders;
import io.fabric.sdk.android.services.network.HttpMethod;
import java.util.Map;

/* loaded from: classes.dex */
public class DigitsOAuthSigning {
    protected final TwitterAuthConfig authConfig;
    protected final TwitterAuthToken authToken;
    protected final OAuth1aHeaders oAuth1aHeaders;

    public DigitsOAuthSigning(TwitterAuthConfig authConfig, TwitterAuthToken authToken) {
        this(authConfig, authToken, new OAuth1aHeaders());
    }

    DigitsOAuthSigning(TwitterAuthConfig authConfig, TwitterAuthToken authToken, OAuth1aHeaders oAuth1aHeaders) {
        if (authConfig == null) {
            throw new IllegalArgumentException("authConfig must not be null");
        }
        if (authToken == null) {
            throw new IllegalArgumentException("authToken must not be null");
        }
        this.authConfig = authConfig;
        this.authToken = authToken;
        this.oAuth1aHeaders = oAuth1aHeaders;
    }

    public Map<String, String> getOAuthEchoHeadersForVerifyCredentials(Map<String, String> optParams) {
        return this.oAuth1aHeaders.getOAuthEchoHeaders(this.authConfig, this.authToken, null, HttpMethod.GET.name(), createProviderUrlWithQueryParams(optParams), null);
    }

    private String createProviderUrlWithQueryParams(Map<String, String> optParams) {
        if (optParams == null) {
            return "https://api.digits.com/1.1/sdk/account.json";
        }
        Uri.Builder uriHeader = Uri.parse("https://api.digits.com/1.1/sdk/account.json").buildUpon();
        for (String key : optParams.keySet()) {
            uriHeader.appendQueryParameter(key, optParams.get(key));
        }
        return uriHeader.toString();
    }
}
