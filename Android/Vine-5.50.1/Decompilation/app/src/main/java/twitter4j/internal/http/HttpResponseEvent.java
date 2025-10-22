package twitter4j.internal.http;

import twitter4j.TwitterException;

/* loaded from: classes.dex */
public final class HttpResponseEvent {
    private HttpRequest request;
    private HttpResponse response;
    private TwitterException twitterException;

    HttpResponseEvent(HttpRequest request, HttpResponse response, TwitterException te) {
        this.request = request;
        this.response = response;
        this.twitterException = te;
    }

    public HttpRequest getRequest() {
        return this.request;
    }

    public HttpResponse getResponse() {
        return this.response;
    }

    public TwitterException getTwitterException() {
        return this.twitterException;
    }

    public boolean isAuthenticated() {
        return this.request.getAuthorization().isEnabled();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpResponseEvent that = (HttpResponseEvent) o;
        if (this.request == null ? that.request != null : !this.request.equals(that.request)) {
            return false;
        }
        if (this.response != null) {
            if (this.response.equals(that.response)) {
                return true;
            }
        } else if (that.response == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.request != null ? this.request.hashCode() : 0;
        return (result * 31) + (this.response != null ? this.response.hashCode() : 0);
    }

    public String toString() {
        return "HttpResponseEvent{request=" + this.request + ", response=" + this.response + '}';
    }
}
