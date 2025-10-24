package org.scribe.extractors;

import java.util.Map;
import org.scribe.exceptions.OAuthParametersMissingException;
import org.scribe.model.OAuthRequest;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/* loaded from: classes.dex */
public class HeaderExtractorImpl implements HeaderExtractor {
    @Override // org.scribe.extractors.HeaderExtractor
    public String extract(OAuthRequest request) {
        checkPreconditions(request);
        Map<String, String> parameters = request.getOauthParameters();
        StringBuilder header = new StringBuilder(parameters.size() * 20);
        header.append("OAuth ");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (header.length() > "OAuth ".length()) {
                header.append(", ");
            }
            header.append(String.format("%s=\"%s\"", entry.getKey(), OAuthEncoder.encode(entry.getValue())));
        }
        return header.toString();
    }

    private void checkPreconditions(OAuthRequest request) {
        Preconditions.checkNotNull(request, "Cannot extract a header from a null object");
        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
            throw new OAuthParametersMissingException(request);
        }
    }
}
