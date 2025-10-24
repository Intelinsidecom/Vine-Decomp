package com.twitter.sdk.android.core.internal.oauth;

import android.net.Uri;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.TwitterApi;
import io.fabric.sdk.android.services.network.UrlUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;
import javax.net.ssl.SSLSocketFactory;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/* loaded from: classes.dex */
public class OAuth1aService extends OAuthService {
    OAuthApi api;

    interface OAuthApi {
        @POST("/oauth/access_token")
        void getAccessToken(@Header("Authorization") String str, @Query("oauth_verifier") String str2, @Body String str3, Callback<Response> callback);

        @POST("/oauth/request_token")
        void getTempToken(@Header("Authorization") String str, @Body String str2, Callback<Response> callback);
    }

    public OAuth1aService(TwitterCore twitterCore, SSLSocketFactory sslSocketFactory, TwitterApi api) {
        super(twitterCore, sslSocketFactory, api);
        this.api = (OAuthApi) getApiAdapter().create(OAuthApi.class);
    }

    public void requestTempToken(Callback<OAuthResponse> callback) {
        TwitterAuthConfig config = getTwitterCore().getAuthConfig();
        String url = getTempTokenUrl();
        this.api.getTempToken(new OAuth1aHeaders().getAuthorizationHeader(config, null, buildCallbackUrl(config), "POST", url, null), "", getCallbackWrapper(callback));
    }

    String getTempTokenUrl() {
        return getApi().getBaseHostUrl() + "/oauth/request_token";
    }

    public String buildCallbackUrl(TwitterAuthConfig authConfig) {
        return Uri.parse("twittersdk://callback").buildUpon().appendQueryParameter("version", getTwitterCore().getVersion()).appendQueryParameter("app", authConfig.getConsumerKey()).build().toString();
    }

    public void requestAccessToken(Callback<OAuthResponse> callback, TwitterAuthToken requestToken, String verifier) {
        String url = getAccessTokenUrl();
        String authHeader = new OAuth1aHeaders().getAuthorizationHeader(getTwitterCore().getAuthConfig(), requestToken, null, "POST", url, null);
        this.api.getAccessToken(authHeader, verifier, "", getCallbackWrapper(callback));
    }

    String getAccessTokenUrl() {
        return getApi().getBaseHostUrl() + "/oauth/access_token";
    }

    public String getAuthorizeUrl(TwitterAuthToken requestToken) {
        return getApi().buildUponBaseHostUrl("oauth", "authorize").appendQueryParameter("oauth_token", requestToken.token).build().toString();
    }

    public static OAuthResponse parseAuthResponse(String response) throws NumberFormatException {
        long userId;
        TreeMap<String, String> params = UrlUtils.getQueryParams(response, false);
        String token = params.get("oauth_token");
        String secret = params.get("oauth_token_secret");
        String userName = params.get("screen_name");
        if (params.containsKey("user_id")) {
            userId = Long.parseLong(params.get("user_id"));
        } else {
            userId = 0;
        }
        if (token == null || secret == null) {
            return null;
        }
        return new OAuthResponse(new TwitterAuthToken(token, secret), userName, userId);
    }

    Callback<Response> getCallbackWrapper(final Callback<OAuthResponse> callback) {
        return new Callback<Response>() { // from class: com.twitter.sdk.android.core.internal.oauth.OAuth1aService.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<Response> result) throws Throwable {
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(result.data.getBody().in()));
                    while (true) {
                        try {
                            String line = reader2.readLine();
                            if (line == null) {
                                break;
                            } else {
                                sb.append(line);
                            }
                        } catch (Throwable th) {
                            th = th;
                            reader = reader2;
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e = e;
                                    callback.failure(new TwitterAuthException(e.getMessage(), e));
                                    return;
                                }
                            }
                            throw th;
                        }
                    }
                    if (reader2 != null) {
                        try {
                            reader2.close();
                        } catch (IOException e2) {
                            e = e2;
                            callback.failure(new TwitterAuthException(e.getMessage(), e));
                            return;
                        }
                    }
                    String responseAsStr = sb.toString();
                    OAuthResponse authResponse = OAuth1aService.parseAuthResponse(responseAsStr);
                    if (authResponse == null) {
                        callback.failure(new TwitterAuthException("Failed to parse auth response: " + responseAsStr));
                    } else {
                        callback.success(new Result(authResponse, null));
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException exception) {
                callback.failure(exception);
            }
        };
    }
}
