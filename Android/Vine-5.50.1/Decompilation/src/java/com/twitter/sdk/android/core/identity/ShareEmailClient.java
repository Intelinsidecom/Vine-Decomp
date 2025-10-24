package com.twitter.sdk.android.core.identity;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import retrofit.http.GET;
import retrofit.http.Query;

/* loaded from: classes.dex */
class ShareEmailClient extends TwitterApiClient {

    interface EmailService {
        @GET("/1.1/account/verify_credentials.json?include_email=true")
        void verifyCredentials(@Query("include_entities") Boolean bool, @Query("skip_status") Boolean bool2, Callback<User> callback);
    }

    ShareEmailClient(TwitterSession session) {
        super(session);
    }

    protected void getEmail(Callback<User> callback) {
        ((EmailService) getService(EmailService.class)).verifyCredentials(true, true, callback);
    }
}
