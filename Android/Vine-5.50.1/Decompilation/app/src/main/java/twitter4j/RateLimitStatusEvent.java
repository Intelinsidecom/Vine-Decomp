package twitter4j;

import java.util.EventObject;

/* loaded from: classes.dex */
public final class RateLimitStatusEvent extends EventObject {
    private static final long serialVersionUID = -2332507741769177298L;
    private boolean isAccountRateLimitStatus;
    private RateLimitStatus rateLimitStatus;

    RateLimitStatusEvent(Object source, RateLimitStatus rateLimitStatus, boolean isAccountRateLimitStatus) {
        super(source);
        this.rateLimitStatus = rateLimitStatus;
        this.isAccountRateLimitStatus = isAccountRateLimitStatus;
    }
}
