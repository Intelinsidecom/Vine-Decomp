package com.twitter.sdk.android.core.internal.oauth;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.UrlUtils;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes2.dex */
class OAuth1aParameters {
    private static final SecureRandom RAND = new SecureRandom();
    private final TwitterAuthConfig authConfig;
    private final TwitterAuthToken authToken;
    private final String callback;
    private final String method;
    private final Map<String, String> postParams;
    private final String url;

    public OAuth1aParameters(TwitterAuthConfig authConfig, TwitterAuthToken authToken, String callback, String method, String url, Map<String, String> postParams) {
        this.authConfig = authConfig;
        this.authToken = authToken;
        this.callback = callback;
        this.method = method;
        this.url = url;
        this.postParams = postParams;
    }

    public String getAuthorizationHeader() throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String nonce = getNonce();
        String timestamp = getTimestamp();
        String signatureBase = constructSignatureBase(nonce, timestamp);
        String signature = calculateSignature(signatureBase);
        return constructAuthorizationHeader(nonce, timestamp, signature);
    }

    private String getNonce() {
        return String.valueOf(System.nanoTime()) + String.valueOf(Math.abs(RAND.nextLong()));
    }

    private String getTimestamp() {
        long secondsFromEpoch = System.currentTimeMillis() / 1000;
        return Long.toString(secondsFromEpoch);
    }

    String constructSignatureBase(String nonce, String timestamp) {
        URI uri = URI.create(this.url);
        TreeMap<String, String> params = UrlUtils.getQueryParams(uri, true);
        if (this.postParams != null) {
            params.putAll(this.postParams);
        }
        if (this.callback != null) {
            params.put("oauth_callback", this.callback);
        }
        params.put("oauth_consumer_key", this.authConfig.getConsumerKey());
        params.put("oauth_nonce", nonce);
        params.put("oauth_signature_method", "HMAC-SHA1");
        params.put("oauth_timestamp", timestamp);
        if (this.authToken != null && this.authToken.token != null) {
            params.put("oauth_token", this.authToken.token);
        }
        params.put("oauth_version", "1.0");
        String baseUrl = uri.getScheme() + "://" + uri.getHost() + uri.getPath();
        StringBuilder sb = new StringBuilder().append(this.method.toUpperCase(Locale.ENGLISH)).append('&').append(UrlUtils.percentEncode(baseUrl)).append('&').append(getEncodedQueryParams(params));
        return sb.toString();
    }

    private String getEncodedQueryParams(TreeMap<String, String> params) {
        StringBuilder paramsBuf = new StringBuilder();
        int numParams = params.size();
        int current = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsBuf.append(UrlUtils.percentEncode(UrlUtils.percentEncode(entry.getKey()))).append("%3D").append(UrlUtils.percentEncode(UrlUtils.percentEncode(entry.getValue())));
            current++;
            if (current < numParams) {
                paramsBuf.append("%26");
            }
        }
        return paramsBuf.toString();
    }

    String calculateSignature(String signatureBase) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        try {
            String key = getSigningKey();
            byte[] signatureBaseBytes = signatureBase.getBytes("UTF8");
            byte[] keyBytes = key.getBytes("UTF8");
            SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            byte[] signatureBytes = mac.doFinal(signatureBaseBytes);
            return new String(HttpRequest.Base64.encodeBytesToBytes(signatureBytes, 0, signatureBytes.length), "UTF8");
        } catch (UnsupportedEncodingException e) {
            Fabric.getLogger().e("Twitter", "Failed to calculate signature", e);
            return "";
        } catch (InvalidKeyException e2) {
            Fabric.getLogger().e("Twitter", "Failed to calculate signature", e2);
            return "";
        } catch (NoSuchAlgorithmException e3) {
            Fabric.getLogger().e("Twitter", "Failed to calculate signature", e3);
            return "";
        }
    }

    private String getSigningKey() {
        String tokenSecret = this.authToken != null ? this.authToken.secret : null;
        return UrlUtils.urlEncode(this.authConfig.getConsumerSecret()) + '&' + UrlUtils.urlEncode(tokenSecret);
    }

    String constructAuthorizationHeader(String nonce, String timestamp, String signature) {
        StringBuilder sb = new StringBuilder("OAuth");
        appendParameter(sb, "oauth_callback", this.callback);
        appendParameter(sb, "oauth_consumer_key", this.authConfig.getConsumerKey());
        appendParameter(sb, "oauth_nonce", nonce);
        appendParameter(sb, "oauth_signature", signature);
        appendParameter(sb, "oauth_signature_method", "HMAC-SHA1");
        appendParameter(sb, "oauth_timestamp", timestamp);
        String token = this.authToken != null ? this.authToken.token : null;
        appendParameter(sb, "oauth_token", token);
        appendParameter(sb, "oauth_version", "1.0");
        return sb.substring(0, sb.length() - 1);
    }

    private void appendParameter(StringBuilder sb, String name, String value) {
        if (value != null) {
            sb.append(' ').append(UrlUtils.percentEncode(name)).append("=\"").append(UrlUtils.percentEncode(value)).append("\",");
        }
    }
}
