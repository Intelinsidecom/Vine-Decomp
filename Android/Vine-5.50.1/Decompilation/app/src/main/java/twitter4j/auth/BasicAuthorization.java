package twitter4j.auth;

import java.io.Serializable;
import twitter4j.internal.http.BASE64Encoder;
import twitter4j.internal.http.HttpRequest;

/* loaded from: classes.dex */
public class BasicAuthorization implements Serializable, Authorization {
    private static final long serialVersionUID = -5861104407848415060L;
    private String basic = encodeBasicAuthenticationString();
    private String password;
    private String userId;

    public BasicAuthorization(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getPassword() {
        return this.password;
    }

    private String encodeBasicAuthenticationString() {
        if (this.userId == null || this.password == null) {
            return null;
        }
        return "Basic " + BASE64Encoder.encode((this.userId + ":" + this.password).getBytes());
    }

    @Override // twitter4j.auth.Authorization
    public String getAuthorizationHeader(HttpRequest req) {
        return this.basic;
    }

    @Override // twitter4j.auth.Authorization
    public boolean isEnabled() {
        return true;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BasicAuthorization)) {
            return false;
        }
        BasicAuthorization that = (BasicAuthorization) o;
        return this.basic.equals(that.basic);
    }

    public int hashCode() {
        return this.basic.hashCode();
    }

    public String toString() {
        return "BasicAuthorization{userId='" + this.userId + "', password='**********''}";
    }
}
