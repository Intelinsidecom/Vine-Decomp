package twitter4j;

import java.io.Serializable;

/* loaded from: classes.dex */
public interface RateLimitStatus extends Serializable {
    int getLimit();

    int getRemaining();

    int getRemainingHits();

    int getResetTimeInSeconds();

    int getSecondsUntilReset();
}
