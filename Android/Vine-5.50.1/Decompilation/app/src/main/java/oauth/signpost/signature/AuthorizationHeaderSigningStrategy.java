package oauth.signpost.signature;

import java.util.Iterator;
import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

/* loaded from: classes.dex */
public class AuthorizationHeaderSigningStrategy implements SigningStrategy {
    private static final long serialVersionUID = 1;

    @Override // oauth.signpost.signature.SigningStrategy
    public String writeSignature(String signature, HttpRequest request, HttpParameters requestParameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("OAuth ");
        if (requestParameters.containsKey("realm")) {
            sb.append(requestParameters.getAsHeaderElement("realm"));
            sb.append(", ");
        }
        HttpParameters oauthParams = requestParameters.getOAuthParameters();
        oauthParams.put("oauth_signature", signature, true);
        Iterator<String> iter = oauthParams.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            sb.append(oauthParams.getAsHeaderElement(key));
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        String header = sb.toString();
        OAuth.debugOut("Auth Header", header);
        request.setHeader("Authorization", header);
        return header;
    }
}
