package twitter4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpResponse;

/* loaded from: classes.dex */
class TwitterImpl extends TwitterBaseImpl implements Twitter {
    private static final Map<Configuration, HttpParameter[]> implicitParamsMap = new HashMap();
    private static final Map<Configuration, String> implicitParamsStrMap = new HashMap();
    private static final long serialVersionUID = -1486360080128882436L;
    private final HttpParameter[] IMPLICIT_PARAMS;
    private final String IMPLICIT_PARAMS_STR;
    private final HttpParameter INCLUDE_MY_RETWEET;

    TwitterImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
        this.INCLUDE_MY_RETWEET = new HttpParameter("include_my_retweet", conf.isIncludeMyRetweetEnabled());
        HttpParameter[] implicitParams = implicitParamsMap.get(conf);
        String implicitParamsStr = implicitParamsStrMap.get(conf);
        if (implicitParams == null) {
            String includeEntities = conf.isIncludeEntitiesEnabled() ? "1" : "0";
            String includeRTs = conf.isIncludeRTsEnabled() ? "1" : "0";
            boolean contributorsEnabled = conf.getContributingTo() != -1;
            implicitParamsStr = "include_entities=" + includeEntities + "&include_rts=" + includeRTs + (contributorsEnabled ? "&contributingto=" + conf.getContributingTo() : "");
            implicitParamsStrMap.put(conf, implicitParamsStr);
            List<HttpParameter> params = new ArrayList<>(4);
            params.add(new HttpParameter("include_entities", includeEntities));
            params.add(new HttpParameter("include_rts", includeRTs));
            if (contributorsEnabled) {
                params.add(new HttpParameter("contributingto", conf.getContributingTo()));
            }
            if (conf.isTrimUserEnabled()) {
                params.add(new HttpParameter("trim_user", "1"));
            }
            implicitParams = (HttpParameter[]) params.toArray(new HttpParameter[params.size()]);
            implicitParamsMap.put(conf, implicitParams);
        }
        this.IMPLICIT_PARAMS = implicitParams;
        this.IMPLICIT_PARAMS_STR = implicitParamsStr;
    }

    private HttpResponse get(String url) throws TwitterException {
        String url2;
        ensureAuthorizationEnabled();
        if (url.contains("?")) {
            url2 = url + "&" + this.IMPLICIT_PARAMS_STR;
        } else {
            url2 = url + "?" + this.IMPLICIT_PARAMS_STR;
        }
        if (!this.conf.isMBeanEnabled()) {
            return this.http.get(url2, this.auth);
        }
        long start = System.currentTimeMillis();
        try {
            HttpResponse response = this.http.get(url2, this.auth);
            return response;
        } finally {
            long jCurrentTimeMillis = System.currentTimeMillis() - start;
        }
    }

    private HttpResponse get(String url, HttpParameter[] params) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!this.conf.isMBeanEnabled()) {
            return this.http.get(url, mergeImplicitParams(params), this.auth);
        }
        long start = System.currentTimeMillis();
        try {
            HttpResponse response = this.http.get(url, mergeImplicitParams(params), this.auth);
            return response;
        } finally {
            long jCurrentTimeMillis = System.currentTimeMillis() - start;
        }
    }

    private HttpResponse post(String url) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!this.conf.isMBeanEnabled()) {
            return this.http.post(url, this.IMPLICIT_PARAMS, this.auth);
        }
        long start = System.currentTimeMillis();
        try {
            HttpResponse response = this.http.post(url, this.IMPLICIT_PARAMS, this.auth);
            return response;
        } finally {
            long jCurrentTimeMillis = System.currentTimeMillis() - start;
        }
    }

    private HttpResponse post(String url, HttpParameter[] params) throws TwitterException {
        ensureAuthorizationEnabled();
        if (!this.conf.isMBeanEnabled()) {
            return this.http.post(url, mergeImplicitParams(params), this.auth);
        }
        long start = System.currentTimeMillis();
        try {
            HttpResponse response = this.http.post(url, mergeImplicitParams(params), this.auth);
            return response;
        } finally {
            long jCurrentTimeMillis = System.currentTimeMillis() - start;
        }
    }

    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter[] params2) {
        if (params1 != null && params2 != null) {
            HttpParameter[] params = new HttpParameter[params1.length + params2.length];
            System.arraycopy(params1, 0, params, 0, params1.length);
            System.arraycopy(params2, 0, params, params1.length, params2.length);
            return params;
        }
        if (params1 == null && params2 == null) {
            return new HttpParameter[0];
        }
        return params1 != null ? params1 : params2;
    }

    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter params2) {
        if (params1 != null && params2 != null) {
            HttpParameter[] params = new HttpParameter[params1.length + 1];
            System.arraycopy(params1, 0, params, 0, params1.length);
            params[params.length - 1] = params2;
            return params;
        }
        if (params1 == null && params2 == null) {
            return new HttpParameter[0];
        }
        return params1 != null ? params1 : new HttpParameter[]{params2};
    }

    private HttpParameter[] mergeImplicitParams(HttpParameter[] params) {
        return mergeParameters(params, this.IMPLICIT_PARAMS);
    }

    private boolean isOk(HttpResponse response) {
        return response != null && response.getStatusCode() < 300;
    }

    @Override // twitter4j.TwitterBase
    public void createFriendship(String screenName) throws TwitterException {
        post(this.conf.getRestBaseURL() + "friendships/create.json?screen_name=" + screenName);
    }

    @Override // twitter4j.TwitterBase
    public void createFriendship(long userId) throws TwitterException {
        post(this.conf.getRestBaseURL() + "friendships/create.json?user_id=" + userId);
    }

    @Override // twitter4j.TwitterBaseImpl
    public String toString() {
        return "TwitterImpl{INCLUDE_MY_RETWEET=" + this.INCLUDE_MY_RETWEET + '}';
    }
}
