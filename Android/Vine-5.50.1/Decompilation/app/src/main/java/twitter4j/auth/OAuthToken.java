package twitter4j.auth;

import java.io.Serializable;
import javax.crypto.spec.SecretKeySpec;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.util.z_T4JInternalStringUtil;

/* loaded from: classes.dex */
abstract class OAuthToken implements Serializable {
    private static final long serialVersionUID = 3891133932519746686L;
    String[] responseStr;
    private transient SecretKeySpec secretKeySpec;
    private String token;
    private String tokenSecret;

    public OAuthToken(String token, String tokenSecret) {
        this.responseStr = null;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    OAuthToken(HttpResponse response) throws TwitterException {
        this(response.asString());
    }

    OAuthToken(String string) {
        this.responseStr = null;
        this.responseStr = z_T4JInternalStringUtil.split(string, "&");
        this.tokenSecret = getParameter("oauth_token_secret");
        this.token = getParameter("oauth_token");
    }

    public String getToken() {
        return this.token;
    }

    public String getTokenSecret() {
        return this.tokenSecret;
    }

    void setSecretKeySpec(SecretKeySpec secretKeySpec) {
        this.secretKeySpec = secretKeySpec;
    }

    SecretKeySpec getSecretKeySpec() {
        return this.secretKeySpec;
    }

    public String getParameter(String parameter) {
        for (String str : this.responseStr) {
            if (str.startsWith(parameter + '=')) {
                String[] splitResult = z_T4JInternalStringUtil.split(str, "=");
                if (splitResult.length <= 1 || splitResult[1] == null) {
                    return null;
                }
                String value = splitResult[1].trim();
                return value;
            }
        }
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OAuthToken)) {
            return false;
        }
        OAuthToken that = (OAuthToken) o;
        if (this.token == null ? that.token != null : !this.token.equals(that.token)) {
            return false;
        }
        if (this.tokenSecret != null) {
            if (this.tokenSecret.equals(that.tokenSecret)) {
                return true;
            }
        } else if (that.tokenSecret == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.token.hashCode();
        return (result * 31) + this.tokenSecret.hashCode();
    }

    public String toString() {
        return "OAuthToken{token='" + this.token + "', tokenSecret='" + this.tokenSecret + "', secretKeySpec=" + this.secretKeySpec + '}';
    }
}
