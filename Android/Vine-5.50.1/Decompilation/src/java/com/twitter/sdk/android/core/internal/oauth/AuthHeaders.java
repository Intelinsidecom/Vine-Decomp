package com.twitter.sdk.android.core.internal.oauth;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import java.util.Map;

/* loaded from: classes.dex */
public interface AuthHeaders {
    Map<String, String> getAuthHeaders(TwitterAuthConfig twitterAuthConfig, String str, String str2, Map<String, String> map);
}
