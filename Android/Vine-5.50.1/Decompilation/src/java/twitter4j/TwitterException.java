package twitter4j;

import java.io.IOException;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseCode;
import twitter4j.internal.json.z_T4JInternalParseUtil;

/* loaded from: classes.dex */
public class TwitterException extends Exception implements TwitterResponse, HttpResponseCode {
    private static final String[] FILTER = {"twitter4j"};
    private static final long serialVersionUID = -2623309261327598087L;
    private int errorCode;
    private String errorMessage;
    private ExceptionDiagnosis exceptionDiagnosis;
    boolean nested;
    private HttpResponse response;
    private int statusCode;

    public TwitterException(String message, Throwable cause) throws JSONException {
        super(message, cause);
        this.statusCode = -1;
        this.errorCode = -1;
        this.exceptionDiagnosis = null;
        this.errorMessage = null;
        this.nested = false;
        decode(message);
    }

    public TwitterException(String message) {
        this(message, (Throwable) null);
    }

    public TwitterException(Exception cause) {
        this(cause.getMessage(), cause);
        if (cause instanceof TwitterException) {
            ((TwitterException) cause).setNested();
        }
    }

    public TwitterException(String message, HttpResponse res) {
        this(message);
        this.response = res;
        this.statusCode = res.getStatusCode();
    }

    public TwitterException(String message, Exception cause, int statusCode) {
        this(message, cause);
        this.statusCode = statusCode;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuilder value = new StringBuilder();
        if (this.errorMessage != null && this.errorCode != -1) {
            value.append("message - ").append(this.errorMessage).append("\n");
            value.append("code - ").append(this.errorCode).append("\n");
        } else {
            value.append(super.getMessage());
        }
        if (this.statusCode != -1) {
            return getCause(this.statusCode) + "\n" + value.toString();
        }
        return value.toString();
    }

    private void decode(String str) throws JSONException {
        if (str != null && str.startsWith("{")) {
            try {
                JSONObject json = new JSONObject(str);
                if (!json.isNull("errors")) {
                    JSONObject error = json.getJSONArray("errors").getJSONObject(0);
                    this.errorMessage = error.getString("message");
                    this.errorCode = z_T4JInternalParseUtil.getInt("code", error);
                }
            } catch (JSONException e) {
            }
        }
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getResponseHeader(String name) {
        if (this.response == null) {
            return null;
        }
        List<String> header = this.response.getResponseHeaderFields().get(name);
        if (header.size() <= 0) {
            return null;
        }
        String value = header.get(0);
        return value;
    }

    @Override // twitter4j.TwitterResponse
    public int getAccessLevel() {
        return z_T4JInternalParseUtil.toAccessLevel(this.response);
    }

    public int getRetryAfter() {
        if (this.statusCode == 400 || this.statusCode != 420) {
            return -1;
        }
        try {
            String retryAfterStr = this.response.getResponseHeader("Retry-After");
            if (retryAfterStr == null) {
                return -1;
            }
            int retryAfter = Integer.valueOf(retryAfterStr).intValue();
            return retryAfter;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean isCausedByNetworkIssue() {
        return getCause() instanceof IOException;
    }

    public boolean resourceNotFound() {
        return this.statusCode == 404;
    }

    public String getExceptionCode() {
        return getExceptionDiagnosis().asHexString();
    }

    private ExceptionDiagnosis getExceptionDiagnosis() {
        if (this.exceptionDiagnosis == null) {
            this.exceptionDiagnosis = new ExceptionDiagnosis(this, FILTER);
        }
        return this.exceptionDiagnosis;
    }

    void setNested() {
        this.nested = true;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isErrorMessageAvailable() {
        return this.errorMessage != null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TwitterException that = (TwitterException) o;
        if (this.errorCode == that.errorCode && this.nested == that.nested && this.statusCode == that.statusCode) {
            if (this.errorMessage == null ? that.errorMessage != null : !this.errorMessage.equals(that.errorMessage)) {
                return false;
            }
            if (this.exceptionDiagnosis == null ? that.exceptionDiagnosis != null : !this.exceptionDiagnosis.equals(that.exceptionDiagnosis)) {
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
        return false;
    }

    public int hashCode() {
        int result = this.statusCode;
        return (((((((((result * 31) + this.errorCode) * 31) + (this.exceptionDiagnosis != null ? this.exceptionDiagnosis.hashCode() : 0)) * 31) + (this.response != null ? this.response.hashCode() : 0)) * 31) + (this.errorMessage != null ? this.errorMessage.hashCode() : 0)) * 31) + (this.nested ? 1 : 0);
    }

    @Override // java.lang.Throwable
    public String toString() {
        return getMessage() + (this.nested ? "" : "\nRelevant discussions can be found on the Internet at:\n\thttp://www.google.co.jp/search?q=" + getExceptionDiagnosis().getStackLineHashAsHex() + " or\n\thttp://www.google.co.jp/search?q=" + getExceptionDiagnosis().getLineNumberHashAsHex()) + "\nTwitterException{" + (this.nested ? "" : "exceptionCode=[" + getExceptionCode() + "], ") + "statusCode=" + this.statusCode + ", message=" + this.errorMessage + ", code=" + this.errorCode + ", retryAfter=" + getRetryAfter() + ", version=" + Version.getVersion() + '}';
    }

    private static String getCause(int statusCode) {
        String cause;
        switch (statusCode) {
            case 304:
                cause = "There was no new data to return.";
                break;
            case HttpResponseCode.BAD_REQUEST /* 400 */:
                cause = "The request was invalid. An accompanying error message will explain why. This is the status code will be returned during version 1.0 rate limiting(https://dev.twitter.com/pages/rate-limiting). In API v1.1, a request without authentication is considered invalid and you will get this response.";
                break;
            case HttpResponseCode.UNAUTHORIZED /* 401 */:
                cause = "Authentication credentials (https://dev.twitter.com/pages/auth) were missing or incorrect. Ensure that you have set valid consumer key/secret, access token/secret, and the system clock is in sync.";
                break;
            case HttpResponseCode.FORBIDDEN /* 403 */:
                cause = "The request is understood, but it has been refused. An accompanying error message will explain why. This code is used when requests are being denied due to update limits (https://support.twitter.com/articles/15364-about-twitter-limits-update-api-dm-and-following).";
                break;
            case HttpResponseCode.NOT_FOUND /* 404 */:
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists. Also returned when the requested format is not supported by the requested method.";
                break;
            case HttpResponseCode.NOT_ACCEPTABLE /* 406 */:
                cause = "Returned by the Search API when an invalid format is specified in the request.\nReturned by the Streaming API when one or more of the parameters are not suitable for the resource. The track parameter, for example, would throw this error if:\n The track keyword is too long or too short.\n The bounding box specified is invalid.\n No predicates defined for filtered resource, for example, neither track nor follow parameter defined.\n Follow userid cannot be read.";
                break;
            case HttpResponseCode.ENHANCE_YOUR_CLAIM /* 420 */:
                cause = "Returned by the Search and Trends API when you are being rate limited (https://dev.twitter.com/docs/rate-limiting).\nReturned by the Streaming API:\n Too many login attempts in a short period of time.\n Running too many copies of the same application authenticating with the same account name.";
                break;
            case HttpResponseCode.UNPROCESSABLE_ENTITY /* 422 */:
                cause = "Returned when an image uploaded to POST account/update_profile_banner(https://dev.twitter.com/docs/api/1/post/account/update_profile_banner) is unable to be processed.";
                break;
            case HttpResponseCode.TOO_MANY_REQUESTS /* 429 */:
                cause = "Returned in API v1.1 when a request cannot be served due to the application's rate limit having been exhausted for the resource. See Rate Limiting in API v1.1.(https://dev.twitter.com/docs/rate-limiting/1.1)";
                break;
            case HttpResponseCode.INTERNAL_SERVER_ERROR /* 500 */:
                cause = "Something is broken. Please post to the group (https://dev.twitter.com/docs/support) so the Twitter team can investigate.";
                break;
            case HttpResponseCode.BAD_GATEWAY /* 502 */:
                cause = "Twitter is down or being upgraded.";
                break;
            case HttpResponseCode.SERVICE_UNAVAILABLE /* 503 */:
                cause = "The Twitter servers are up, but overloaded with requests. Try again later.";
                break;
            case HttpResponseCode.GATEWAY_TIMEOUT /* 504 */:
                cause = "The Twitter servers are up, but the request couldn't be serviced due to some failure within our stack. Try again later.";
                break;
            default:
                cause = "";
                break;
        }
        return statusCode + ":" + cause;
    }
}
