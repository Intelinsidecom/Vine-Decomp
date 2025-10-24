package twitter4j;

import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;

/* loaded from: classes.dex */
public interface TwitterBase {
    void createFriendship(long j) throws TwitterException;

    void createFriendship(String str) throws TwitterException;

    Authorization getAuthorization();

    Configuration getConfiguration();

    void shutdown();
}
