package org.scribe.oauth;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;

/* loaded from: classes.dex */
public interface OAuthService {
    void signRequest(Token token, OAuthRequest oAuthRequest);
}
