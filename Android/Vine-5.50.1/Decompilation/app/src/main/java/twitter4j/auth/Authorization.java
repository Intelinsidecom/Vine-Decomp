package twitter4j.auth;

import java.io.Serializable;
import twitter4j.internal.http.HttpRequest;

/* loaded from: classes.dex */
public interface Authorization extends Serializable {
    String getAuthorizationHeader(HttpRequest httpRequest);

    boolean isEnabled();
}
