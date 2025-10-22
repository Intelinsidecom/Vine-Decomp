package twitter4j.internal.http;

import twitter4j.TwitterException;

/* loaded from: classes.dex */
public interface HttpClient {
    HttpResponse request(HttpRequest httpRequest) throws TwitterException;

    void shutdown();
}
