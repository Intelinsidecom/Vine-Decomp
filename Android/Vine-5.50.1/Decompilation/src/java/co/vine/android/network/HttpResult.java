package co.vine.android.network;

/* loaded from: classes.dex */
public class HttpResult {
    public long durationMs;
    public Exception exception;
    public final String reasonPhrase;
    public final int statusCode;
    public String uploadKey;

    public HttpResult(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }
}
