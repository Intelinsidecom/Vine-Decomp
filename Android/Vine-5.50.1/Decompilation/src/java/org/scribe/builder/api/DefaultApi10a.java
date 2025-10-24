package org.scribe.builder.api;

import org.scribe.extractors.BaseStringExtractor;
import org.scribe.extractors.BaseStringExtractorImpl;
import org.scribe.extractors.HeaderExtractor;
import org.scribe.extractors.HeaderExtractorImpl;
import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuth10aServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.services.HMACSha1SignatureService;
import org.scribe.services.SignatureService;
import org.scribe.services.TimestampService;
import org.scribe.services.TimestampServiceImpl;

/* loaded from: classes.dex */
public abstract class DefaultApi10a implements Api {
    public BaseStringExtractor getBaseStringExtractor() {
        return new BaseStringExtractorImpl();
    }

    public HeaderExtractor getHeaderExtractor() {
        return new HeaderExtractorImpl();
    }

    public SignatureService getSignatureService() {
        return new HMACSha1SignatureService();
    }

    public TimestampService getTimestampService() {
        return new TimestampServiceImpl();
    }

    @Override // org.scribe.builder.api.Api
    public OAuthService createService(OAuthConfig config) {
        return new OAuth10aServiceImpl(this, config);
    }
}
