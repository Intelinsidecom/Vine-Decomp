package org.scribe.extractors;

import java.io.UnsupportedEncodingException;
import org.scribe.exceptions.OAuthParametersMissingException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.ParameterList;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/* loaded from: classes.dex */
public class BaseStringExtractorImpl implements BaseStringExtractor {
    @Override // org.scribe.extractors.BaseStringExtractor
    public String extract(OAuthRequest request) throws UnsupportedEncodingException {
        checkPreconditions(request);
        String verb = OAuthEncoder.encode(request.getVerb().name());
        String url = OAuthEncoder.encode(request.getSanitizedUrl());
        String params = getSortedAndEncodedParams(request);
        return String.format("%s&%s&%s", verb, url, params);
    }

    private String getSortedAndEncodedParams(OAuthRequest request) {
        ParameterList params = new ParameterList();
        params.addAll(request.getQueryStringParams());
        params.addAll(request.getBodyParams());
        params.addAll(new ParameterList(request.getOauthParameters()));
        return params.sort().asOauthBaseString();
    }

    private void checkPreconditions(OAuthRequest request) {
        Preconditions.checkNotNull(request, "Cannot extract base string from null object");
        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
            throw new OAuthParametersMissingException(request);
        }
    }
}
