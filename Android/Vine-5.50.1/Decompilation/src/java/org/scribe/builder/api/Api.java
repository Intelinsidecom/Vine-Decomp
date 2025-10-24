package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuthService;

/* loaded from: classes.dex */
public interface Api {
    OAuthService createService(OAuthConfig oAuthConfig);
}
