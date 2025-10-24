package twitter4j.auth;

import java.io.Serializable;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

/* loaded from: classes.dex */
public class AccessToken extends OAuthToken implements Serializable {
    private static final long serialVersionUID = -8344528374458826291L;
    private String screenName;
    private long userId;

    @Override // twitter4j.auth.OAuthToken
    public /* bridge */ /* synthetic */ String getParameter(String str) {
        return super.getParameter(str);
    }

    @Override // twitter4j.auth.OAuthToken
    public /* bridge */ /* synthetic */ String getToken() {
        return super.getToken();
    }

    @Override // twitter4j.auth.OAuthToken
    public /* bridge */ /* synthetic */ String getTokenSecret() {
        return super.getTokenSecret();
    }

    AccessToken(HttpResponse res) throws TwitterException {
        this(res.asString());
    }

    AccessToken(String str) {
        super(str);
        this.userId = -1L;
        this.screenName = getParameter("screen_name");
        String sUserId = getParameter("user_id");
        if (sUserId != null) {
            this.userId = Long.parseLong(sUserId);
        }
    }

    public AccessToken(String token, String tokenSecret) {
        super(token, tokenSecret);
        this.userId = -1L;
        int dashIndex = token.indexOf("-");
        if (dashIndex != -1) {
            String sUserId = token.substring(0, dashIndex);
            try {
                this.userId = Long.parseLong(sUserId);
            } catch (NumberFormatException e) {
            }
        }
    }

    public AccessToken(String token, String tokenSecret, long userId) {
        super(token, tokenSecret);
        this.userId = -1L;
        this.userId = userId;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public long getUserId() {
        return this.userId;
    }

    @Override // twitter4j.auth.OAuthToken
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AccessToken that = (AccessToken) o;
        if (this.userId != that.userId) {
            return false;
        }
        if (this.screenName != null) {
            if (this.screenName.equals(that.screenName)) {
                return true;
            }
        } else if (that.screenName == null) {
            return true;
        }
        return false;
    }

    @Override // twitter4j.auth.OAuthToken
    public int hashCode() {
        int result = super.hashCode();
        return (((result * 31) + (this.screenName != null ? this.screenName.hashCode() : 0)) * 31) + ((int) (this.userId ^ (this.userId >>> 32)));
    }

    @Override // twitter4j.auth.OAuthToken
    public String toString() {
        return "AccessToken{screenName='" + this.screenName + "', userId=" + this.userId + '}';
    }
}
