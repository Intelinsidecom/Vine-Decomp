package org.scribe.oauth;

import java.io.IOException;
import java.util.Map;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.services.Base64Encoder;
import org.scribe.utils.MapUtils;

/* loaded from: classes.dex */
public class OAuth10aServiceImpl implements OAuthService {
    private DefaultApi10a api;
    private OAuthConfig config;

    public OAuth10aServiceImpl(DefaultApi10a api, OAuthConfig config) {
        this.api = api;
        this.config = config;
    }

    private void addOAuthParams(OAuthRequest request, Token token) throws IOException {
        request.addOAuthParameter("oauth_timestamp", this.api.getTimestampService().getTimestampInSeconds());
        request.addOAuthParameter("oauth_nonce", this.api.getTimestampService().getNonce());
        request.addOAuthParameter("oauth_consumer_key", this.config.getApiKey());
        request.addOAuthParameter("oauth_signature_method", this.api.getSignatureService().getSignatureMethod());
        request.addOAuthParameter("oauth_version", getVersion());
        if (this.config.hasScope()) {
            request.addOAuthParameter("scope", this.config.getScope());
        }
        request.addOAuthParameter("oauth_signature", getSignature(request, token));
        this.config.log("appended additional OAuth parameters: " + MapUtils.toString(request.getOauthParameters()));
    }

    @Override // org.scribe.oauth.OAuthService
    public void signRequest(Token token, OAuthRequest request) throws IOException {
        this.config.log("signing request: " + request.getCompleteUrl());
        if (!token.isEmpty()) {
            request.addOAuthParameter("oauth_token", token.getToken());
        }
        this.config.log("setting token to: " + token);
        addOAuthParams(request, token);
        appendSignature(request);
    }

    public String getVersion() {
        return "1.0";
    }

    private String getSignature(OAuthRequest request, Token token) throws IOException {
        this.config.log("generating signature...");
        this.config.log("using base64 encoder: " + Base64Encoder.type());
        String baseString = this.api.getBaseStringExtractor().extract(request);
        String signature = this.api.getSignatureService().getSignature(baseString, this.config.getApiSecret(), token.getSecret());
        this.config.log("base string is: " + baseString);
        this.config.log("signature is: " + signature);
        return signature;
    }

    private void appendSignature(OAuthRequest request) throws IOException {
        switch (this.config.getSignatureType()) {
            case Header:
                this.config.log("using Http Header signature");
                String oauthHeader = this.api.getHeaderExtractor().extract(request);
                request.addHeader("Authorization", oauthHeader);
                break;
            case QueryString:
                this.config.log("using Querystring signature");
                for (Map.Entry<String, String> entry : request.getOauthParameters().entrySet()) {
                    request.addQuerystringParameter(entry.getKey(), entry.getValue());
                }
                break;
        }
    }
}
