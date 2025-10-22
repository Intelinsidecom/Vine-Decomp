package twitter4j.conf;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import twitter4j.Version;

/* loaded from: classes.dex */
class ConfigurationBase implements Serializable, Configuration {
    public static final String DALVIK = "twitter4j.dalvik";
    private static final String DEFAULT_OAUTH2_INVALIDATE_TOKEN_URL = "https://api.twitter.com/oauth2/invalidate_token";
    private static final String DEFAULT_OAUTH2_TOKEN_URL = "https://api.twitter.com/oauth2/token";
    private static final String DEFAULT_OAUTH_ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    private static final String DEFAULT_OAUTH_AUTHENTICATION_URL = "https://api.twitter.com/oauth/authenticate";
    private static final String DEFAULT_OAUTH_AUTHORIZATION_URL = "https://api.twitter.com/oauth/authorize";
    private static final String DEFAULT_OAUTH_REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    private static final String DEFAULT_REST_BASE_URL = "https://api.twitter.com/1.1/";
    private static final String DEFAULT_SITE_STREAM_BASE_URL = "https://sitestream.twitter.com/1.1/";
    private static final String DEFAULT_STREAM_BASE_URL = "https://stream.twitter.com/1.1/";
    private static final String DEFAULT_USER_STREAM_BASE_URL = "https://userstream.twitter.com/1.1/";
    public static final String GAE = "twitter4j.gae";
    static String dalvikDetected = null;
    static String gaeDetected = null;
    private static final List<ConfigurationBase> instances;
    private static final long serialVersionUID = -6610497517837844232L;
    private boolean IS_DALVIK;
    private boolean IS_GAE;
    private int asyncNumThreads;
    private String clientURL;
    private String clientVersion;
    private long contributingTo;
    private boolean debug;
    private int defaultMaxPerRoute;
    private String dispatcherImpl;
    private boolean gzipEnabled;
    private int httpConnectionTimeout;
    private String httpProxyHost;
    private String httpProxyPassword;
    private int httpProxyPort;
    private String httpProxyUser;
    private int httpReadTimeout;
    private int httpRetryCount;
    private int httpRetryIntervalSeconds;
    private int httpStreamingReadTimeout;
    private boolean jsonStoreEnabled;
    private String loggerFactory;
    private int maxTotalConnections;
    private boolean mbeanEnabled;
    private String mediaProvider;
    private String mediaProviderAPIKey;
    private Properties mediaProviderParameters;
    private String oAuth2AccessToken;
    private String oAuth2InvalidateTokenURL;
    private String oAuth2TokenType;
    private String oAuth2TokenURL;
    private String oAuthAccessToken;
    private String oAuthAccessTokenSecret;
    private String oAuthAccessTokenURL;
    private String oAuthAuthenticationURL;
    private String oAuthAuthorizationURL;
    private String oAuthConsumerKey;
    private String oAuthConsumerSecret;
    private String oAuthRequestTokenURL;
    private String password;
    private boolean prettyDebug;
    Map<String, String> requestHeaders;
    private String restBaseURL;
    private String siteStreamBaseURL;
    private boolean stallWarningsEnabled;
    private String streamBaseURL;
    private boolean useSSL;
    private String user;
    private String userAgent;
    private String userStreamBaseURL;
    private boolean userStreamRepliesAllEnabled;
    private boolean includeRTsEnabled = true;
    private boolean includeEntitiesEnabled = true;
    private boolean includeMyRetweetEnabled = true;
    private boolean trimUserEnabled = false;
    private boolean applicationOnlyAuthEnabled = false;

    static {
        try {
            Class.forName("dalvik.system.VMRuntime");
            dalvikDetected = "true";
        } catch (ClassNotFoundException e) {
            dalvikDetected = "false";
        }
        try {
            Class.forName("com.google.appengine.api.urlfetch.URLFetchService");
            gaeDetected = "true";
        } catch (ClassNotFoundException e2) {
            gaeDetected = "false";
        }
        instances = new ArrayList();
    }

    protected ConfigurationBase() {
        String isDalvik;
        String isGAE;
        setDebug(false);
        setUser(null);
        setPassword(null);
        setUseSSL(true);
        setPrettyDebugEnabled(false);
        setGZIPEnabled(true);
        setHttpProxyHost(null);
        setHttpProxyUser(null);
        setHttpProxyPassword(null);
        setHttpProxyPort(-1);
        setHttpConnectionTimeout(20000);
        setHttpReadTimeout(120000);
        setHttpStreamingReadTimeout(40000);
        setHttpRetryCount(0);
        setHttpRetryIntervalSeconds(5);
        setHttpMaxTotalConnections(20);
        setHttpDefaultMaxPerRoute(2);
        setOAuthConsumerKey(null);
        setOAuthConsumerSecret(null);
        setOAuthAccessToken(null);
        setOAuthAccessTokenSecret(null);
        setAsyncNumThreads(1);
        setContributingTo(-1L);
        setClientVersion(Version.getVersion());
        setClientURL("http://twitter4j.org/en/twitter4j-" + Version.getVersion() + ".xml");
        setUserAgent("twitter4j http://twitter4j.org/ /" + Version.getVersion());
        setJSONStoreEnabled(false);
        setMBeanEnabled(false);
        setOAuthRequestTokenURL(DEFAULT_OAUTH_REQUEST_TOKEN_URL);
        setOAuthAuthorizationURL(DEFAULT_OAUTH_AUTHORIZATION_URL);
        setOAuthAccessTokenURL(DEFAULT_OAUTH_ACCESS_TOKEN_URL);
        setOAuthAuthenticationURL(DEFAULT_OAUTH_AUTHENTICATION_URL);
        setOAuth2TokenURL(DEFAULT_OAUTH2_TOKEN_URL);
        setOAuth2InvalidateTokenURL(DEFAULT_OAUTH2_INVALIDATE_TOKEN_URL);
        setRestBaseURL(DEFAULT_REST_BASE_URL);
        setStreamBaseURL(DEFAULT_STREAM_BASE_URL);
        setUserStreamBaseURL(DEFAULT_USER_STREAM_BASE_URL);
        setSiteStreamBaseURL(DEFAULT_SITE_STREAM_BASE_URL);
        setDispatcherImpl("twitter4j.internal.async.DispatcherImpl");
        setLoggerFactory(null);
        setUserStreamRepliesAllEnabled(false);
        setStallWarningsEnabled(true);
        try {
            isDalvik = System.getProperty(DALVIK, dalvikDetected);
        } catch (SecurityException e) {
            isDalvik = dalvikDetected;
        }
        this.IS_DALVIK = Boolean.valueOf(isDalvik).booleanValue();
        try {
            isGAE = System.getProperty(GAE, gaeDetected);
        } catch (SecurityException e2) {
            isGAE = gaeDetected;
        }
        this.IS_GAE = Boolean.valueOf(isGAE).booleanValue();
        setMediaProvider("TWITTER");
        setMediaProviderAPIKey(null);
        setMediaProviderParameters(null);
    }

    @Override // twitter4j.conf.Configuration
    public final boolean isDalvik() {
        return this.IS_DALVIK;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isGAE() {
        return this.IS_GAE;
    }

    @Override // twitter4j.conf.Configuration
    public final boolean isDebugEnabled() {
        return this.debug;
    }

    protected final void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override // twitter4j.conf.Configuration
    public final String getUserAgent() {
        return this.userAgent;
    }

    protected final void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        initRequestHeaders();
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public final String getUser() {
        return this.user;
    }

    protected final void setUser(String user) {
        this.user = user;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public final String getPassword() {
        return this.password;
    }

    protected final void setPassword(String password) {
        this.password = password;
    }

    @Override // twitter4j.internal.http.HttpClientConfiguration
    public boolean isPrettyDebugEnabled() {
        return this.prettyDebug;
    }

    protected final void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
        fixRestBaseURL();
    }

    protected final void setPrettyDebugEnabled(boolean prettyDebug) {
        this.prettyDebug = prettyDebug;
    }

    protected final void setGZIPEnabled(boolean gzipEnabled) {
        this.gzipEnabled = gzipEnabled;
        initRequestHeaders();
    }

    @Override // twitter4j.internal.http.HttpClientConfiguration
    public boolean isGZIPEnabled() {
        return this.gzipEnabled;
    }

    private void initRequestHeaders() {
        this.requestHeaders = new HashMap();
        this.requestHeaders.put("X-Twitter-Client-Version", getClientVersion());
        this.requestHeaders.put("X-Twitter-Client-URL", getClientURL());
        this.requestHeaders.put("X-Twitter-Client", "Twitter4J");
        this.requestHeaders.put("User-Agent", getUserAgent());
        if (this.gzipEnabled) {
            this.requestHeaders.put("Accept-Encoding", "gzip");
        }
        if (this.IS_DALVIK) {
            this.requestHeaders.put("Connection", "close");
        }
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientWrapperConfiguration
    public Map<String, String> getRequestHeaders() {
        return this.requestHeaders;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final String getHttpProxyHost() {
        return this.httpProxyHost;
    }

    protected final void setHttpProxyHost(String proxyHost) {
        this.httpProxyHost = proxyHost;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final String getHttpProxyUser() {
        return this.httpProxyUser;
    }

    protected final void setHttpProxyUser(String proxyUser) {
        this.httpProxyUser = proxyUser;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final String getHttpProxyPassword() {
        return this.httpProxyPassword;
    }

    protected final void setHttpProxyPassword(String proxyPassword) {
        this.httpProxyPassword = proxyPassword;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpProxyPort() {
        return this.httpProxyPort;
    }

    protected final void setHttpProxyPort(int proxyPort) {
        this.httpProxyPort = proxyPort;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpConnectionTimeout() {
        return this.httpConnectionTimeout;
    }

    protected final void setHttpConnectionTimeout(int connectionTimeout) {
        this.httpConnectionTimeout = connectionTimeout;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpReadTimeout() {
        return this.httpReadTimeout;
    }

    protected final void setHttpReadTimeout(int readTimeout) {
        this.httpReadTimeout = readTimeout;
    }

    @Override // twitter4j.conf.Configuration
    public int getHttpStreamingReadTimeout() {
        return this.httpStreamingReadTimeout;
    }

    protected final void setHttpStreamingReadTimeout(int httpStreamingReadTimeout) {
        this.httpStreamingReadTimeout = httpStreamingReadTimeout;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpRetryCount() {
        return this.httpRetryCount;
    }

    protected final void setHttpRetryCount(int retryCount) {
        this.httpRetryCount = retryCount;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpRetryIntervalSeconds() {
        return this.httpRetryIntervalSeconds;
    }

    protected final void setHttpRetryIntervalSeconds(int retryIntervalSeconds) {
        this.httpRetryIntervalSeconds = retryIntervalSeconds;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpMaxTotalConnections() {
        return this.maxTotalConnections;
    }

    protected final void setHttpMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpDefaultMaxPerRoute() {
        return this.defaultMaxPerRoute;
    }

    protected final void setHttpDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public final String getOAuthConsumerKey() {
        return this.oAuthConsumerKey;
    }

    protected final void setOAuthConsumerKey(String oAuthConsumerKey) {
        this.oAuthConsumerKey = oAuthConsumerKey;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public final String getOAuthConsumerSecret() {
        return this.oAuthConsumerSecret;
    }

    protected final void setOAuthConsumerSecret(String oAuthConsumerSecret) {
        this.oAuthConsumerSecret = oAuthConsumerSecret;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public String getOAuthAccessToken() {
        return this.oAuthAccessToken;
    }

    protected final void setOAuthAccessToken(String oAuthAccessToken) {
        this.oAuthAccessToken = oAuthAccessToken;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public String getOAuthAccessTokenSecret() {
        return this.oAuthAccessTokenSecret;
    }

    protected final void setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
        this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public String getOAuth2TokenType() {
        return this.oAuth2TokenType;
    }

    protected final void setOAuth2TokenType(String oAuth2TokenType) {
        this.oAuth2TokenType = oAuth2TokenType;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public String getOAuth2AccessToken() {
        return this.oAuth2AccessToken;
    }

    protected final void setOAuth2AccessToken(String oAuth2AccessToken) {
        this.oAuth2AccessToken = oAuth2AccessToken;
    }

    @Override // twitter4j.conf.Configuration
    public final int getAsyncNumThreads() {
        return this.asyncNumThreads;
    }

    protected final void setAsyncNumThreads(int asyncNumThreads) {
        this.asyncNumThreads = asyncNumThreads;
    }

    @Override // twitter4j.conf.Configuration
    public final long getContributingTo() {
        return this.contributingTo;
    }

    protected final void setContributingTo(long contributingTo) {
        this.contributingTo = contributingTo;
    }

    @Override // twitter4j.conf.Configuration
    public final String getClientVersion() {
        return this.clientVersion;
    }

    protected final void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
        initRequestHeaders();
    }

    @Override // twitter4j.conf.Configuration
    public final String getClientURL() {
        return this.clientURL;
    }

    protected final void setClientURL(String clientURL) {
        this.clientURL = clientURL;
        initRequestHeaders();
    }

    @Override // twitter4j.conf.Configuration
    public String getRestBaseURL() {
        return this.restBaseURL;
    }

    protected final void setRestBaseURL(String restBaseURL) {
        this.restBaseURL = restBaseURL;
        fixRestBaseURL();
    }

    private void fixRestBaseURL() {
        if (DEFAULT_REST_BASE_URL.equals(fixURL(false, this.restBaseURL))) {
            this.restBaseURL = fixURL(this.useSSL, this.restBaseURL);
        }
        if (DEFAULT_OAUTH_ACCESS_TOKEN_URL.equals(fixURL(false, this.oAuthAccessTokenURL))) {
            this.oAuthAccessTokenURL = fixURL(this.useSSL, this.oAuthAccessTokenURL);
        }
        if (DEFAULT_OAUTH_AUTHENTICATION_URL.equals(fixURL(false, this.oAuthAuthenticationURL))) {
            this.oAuthAuthenticationURL = fixURL(this.useSSL, this.oAuthAuthenticationURL);
        }
        if (DEFAULT_OAUTH_AUTHORIZATION_URL.equals(fixURL(false, this.oAuthAuthorizationURL))) {
            this.oAuthAuthorizationURL = fixURL(this.useSSL, this.oAuthAuthorizationURL);
        }
        if (DEFAULT_OAUTH_REQUEST_TOKEN_URL.equals(fixURL(false, this.oAuthRequestTokenURL))) {
            this.oAuthRequestTokenURL = fixURL(this.useSSL, this.oAuthRequestTokenURL);
        }
    }

    @Override // twitter4j.conf.Configuration
    public String getStreamBaseURL() {
        return this.streamBaseURL;
    }

    protected final void setStreamBaseURL(String streamBaseURL) {
        this.streamBaseURL = streamBaseURL;
    }

    @Override // twitter4j.conf.Configuration
    public String getUserStreamBaseURL() {
        return this.userStreamBaseURL;
    }

    protected final void setUserStreamBaseURL(String siteStreamBaseURL) {
        this.userStreamBaseURL = siteStreamBaseURL;
    }

    @Override // twitter4j.conf.Configuration
    public String getSiteStreamBaseURL() {
        return this.siteStreamBaseURL;
    }

    protected final void setSiteStreamBaseURL(String siteStreamBaseURL) {
        this.siteStreamBaseURL = siteStreamBaseURL;
    }

    @Override // twitter4j.conf.Configuration
    public String getOAuthRequestTokenURL() {
        return this.oAuthRequestTokenURL;
    }

    protected final void setOAuthRequestTokenURL(String oAuthRequestTokenURL) {
        this.oAuthRequestTokenURL = oAuthRequestTokenURL;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration
    public String getOAuthAuthorizationURL() {
        return this.oAuthAuthorizationURL;
    }

    protected final void setOAuthAuthorizationURL(String oAuthAuthorizationURL) {
        this.oAuthAuthorizationURL = oAuthAuthorizationURL;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration
    public String getOAuthAccessTokenURL() {
        return this.oAuthAccessTokenURL;
    }

    protected final void setOAuthAccessTokenURL(String oAuthAccessTokenURL) {
        this.oAuthAccessTokenURL = oAuthAccessTokenURL;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration
    public String getOAuthAuthenticationURL() {
        return this.oAuthAuthenticationURL;
    }

    protected final void setOAuthAuthenticationURL(String oAuthAuthenticationURL) {
        this.oAuthAuthenticationURL = oAuthAuthenticationURL;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration
    public String getOAuth2TokenURL() {
        return this.oAuth2TokenURL;
    }

    protected final void setOAuth2TokenURL(String oAuth2TokenURL) {
        this.oAuth2TokenURL = oAuth2TokenURL;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration
    public String getOAuth2InvalidateTokenURL() {
        return this.oAuth2InvalidateTokenURL;
    }

    protected final void setOAuth2InvalidateTokenURL(String oAuth2InvalidateTokenURL) {
        this.oAuth2InvalidateTokenURL = oAuth2InvalidateTokenURL;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration
    public String getDispatcherImpl() {
        return this.dispatcherImpl;
    }

    protected final void setDispatcherImpl(String dispatcherImpl) {
        this.dispatcherImpl = dispatcherImpl;
    }

    @Override // twitter4j.conf.Configuration
    public String getLoggerFactory() {
        return this.loggerFactory;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isIncludeRTsEnabled() {
        return this.includeRTsEnabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isIncludeEntitiesEnabled() {
        return this.includeEntitiesEnabled;
    }

    protected final void setLoggerFactory(String loggerImpl) {
        this.loggerFactory = loggerImpl;
    }

    protected final void setIncludeRTsEnbled(boolean enabled) {
        this.includeRTsEnabled = enabled;
    }

    protected final void setIncludeEntitiesEnbled(boolean enabled) {
        this.includeEntitiesEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isIncludeMyRetweetEnabled() {
        return this.includeMyRetweetEnabled;
    }

    public void setIncludeMyRetweetEnabled(boolean enabled) {
        this.includeMyRetweetEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isTrimUserEnabled() {
        return this.trimUserEnabled;
    }

    public void setTrimUserEnabled(boolean enabled) {
        this.trimUserEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isJSONStoreEnabled() {
        return this.jsonStoreEnabled;
    }

    protected final void setJSONStoreEnabled(boolean enabled) {
        this.jsonStoreEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isMBeanEnabled() {
        return this.mbeanEnabled;
    }

    protected final void setMBeanEnabled(boolean enabled) {
        this.mbeanEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isUserStreamRepliesAllEnabled() {
        return this.userStreamRepliesAllEnabled;
    }

    protected final void setUserStreamRepliesAllEnabled(boolean enabled) {
        this.userStreamRepliesAllEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isStallWarningsEnabled() {
        return this.stallWarningsEnabled;
    }

    protected final void setStallWarningsEnabled(boolean stallWarningsEnabled) {
        this.stallWarningsEnabled = stallWarningsEnabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isApplicationOnlyAuthEnabled() {
        return this.applicationOnlyAuthEnabled;
    }

    protected final void setApplicationOnlyAuthEnabled(boolean applicationOnlyAuthEnabled) {
        this.applicationOnlyAuthEnabled = applicationOnlyAuthEnabled;
    }

    @Override // twitter4j.conf.Configuration
    public String getMediaProvider() {
        return this.mediaProvider;
    }

    protected final void setMediaProvider(String mediaProvider) {
        this.mediaProvider = mediaProvider;
    }

    @Override // twitter4j.conf.Configuration
    public String getMediaProviderAPIKey() {
        return this.mediaProviderAPIKey;
    }

    protected final void setMediaProviderAPIKey(String mediaProviderAPIKey) {
        this.mediaProviderAPIKey = mediaProviderAPIKey;
    }

    @Override // twitter4j.conf.Configuration
    public Properties getMediaProviderParameters() {
        return this.mediaProviderParameters;
    }

    protected final void setMediaProviderParameters(Properties props) {
        this.mediaProviderParameters = props;
    }

    static String fixURL(boolean useSSL, String url) {
        if (url == null) {
            return null;
        }
        int index = url.indexOf("://");
        if (-1 == index) {
            throw new IllegalArgumentException("url should contain '://'");
        }
        String hostAndLater = url.substring(index + 3);
        if (useSSL) {
            return "https://" + hostAndLater;
        }
        return "http://" + hostAndLater;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigurationBase that = (ConfigurationBase) o;
        if (this.IS_DALVIK == that.IS_DALVIK && this.IS_GAE == that.IS_GAE && this.asyncNumThreads == that.asyncNumThreads && this.contributingTo == that.contributingTo && this.debug == that.debug && this.defaultMaxPerRoute == that.defaultMaxPerRoute && this.gzipEnabled == that.gzipEnabled && this.httpConnectionTimeout == that.httpConnectionTimeout && this.httpProxyPort == that.httpProxyPort && this.httpReadTimeout == that.httpReadTimeout && this.httpRetryCount == that.httpRetryCount && this.httpRetryIntervalSeconds == that.httpRetryIntervalSeconds && this.httpStreamingReadTimeout == that.httpStreamingReadTimeout && this.includeEntitiesEnabled == that.includeEntitiesEnabled && this.includeMyRetweetEnabled == that.includeMyRetweetEnabled && this.trimUserEnabled == that.trimUserEnabled && this.includeRTsEnabled == that.includeRTsEnabled && this.jsonStoreEnabled == that.jsonStoreEnabled && this.maxTotalConnections == that.maxTotalConnections && this.mbeanEnabled == that.mbeanEnabled && this.prettyDebug == that.prettyDebug && this.stallWarningsEnabled == that.stallWarningsEnabled && this.applicationOnlyAuthEnabled == that.applicationOnlyAuthEnabled && this.useSSL == that.useSSL && this.userStreamRepliesAllEnabled == that.userStreamRepliesAllEnabled) {
            if (this.clientURL == null ? that.clientURL != null : !this.clientURL.equals(that.clientURL)) {
                return false;
            }
            if (this.clientVersion == null ? that.clientVersion != null : !this.clientVersion.equals(that.clientVersion)) {
                return false;
            }
            if (this.dispatcherImpl == null ? that.dispatcherImpl != null : !this.dispatcherImpl.equals(that.dispatcherImpl)) {
                return false;
            }
            if (this.httpProxyHost == null ? that.httpProxyHost != null : !this.httpProxyHost.equals(that.httpProxyHost)) {
                return false;
            }
            if (this.httpProxyPassword == null ? that.httpProxyPassword != null : !this.httpProxyPassword.equals(that.httpProxyPassword)) {
                return false;
            }
            if (this.httpProxyUser == null ? that.httpProxyUser != null : !this.httpProxyUser.equals(that.httpProxyUser)) {
                return false;
            }
            if (this.loggerFactory == null ? that.loggerFactory != null : !this.loggerFactory.equals(that.loggerFactory)) {
                return false;
            }
            if (this.mediaProvider == null ? that.mediaProvider != null : !this.mediaProvider.equals(that.mediaProvider)) {
                return false;
            }
            if (this.mediaProviderAPIKey == null ? that.mediaProviderAPIKey != null : !this.mediaProviderAPIKey.equals(that.mediaProviderAPIKey)) {
                return false;
            }
            if (this.mediaProviderParameters == null ? that.mediaProviderParameters != null : !this.mediaProviderParameters.equals(that.mediaProviderParameters)) {
                return false;
            }
            if (this.oAuthAccessToken == null ? that.oAuthAccessToken != null : !this.oAuthAccessToken.equals(that.oAuthAccessToken)) {
                return false;
            }
            if (this.oAuthAccessTokenSecret == null ? that.oAuthAccessTokenSecret != null : !this.oAuthAccessTokenSecret.equals(that.oAuthAccessTokenSecret)) {
                return false;
            }
            if (this.oAuth2TokenType == null ? that.oAuth2TokenType != null : !this.oAuth2TokenType.equals(that.oAuth2TokenType)) {
                return false;
            }
            if (this.oAuth2AccessToken == null ? that.oAuth2AccessToken != null : !this.oAuth2AccessToken.equals(that.oAuth2AccessToken)) {
                return false;
            }
            if (this.oAuthAccessTokenURL == null ? that.oAuthAccessTokenURL != null : !this.oAuthAccessTokenURL.equals(that.oAuthAccessTokenURL)) {
                return false;
            }
            if (this.oAuthAuthenticationURL == null ? that.oAuthAuthenticationURL != null : !this.oAuthAuthenticationURL.equals(that.oAuthAuthenticationURL)) {
                return false;
            }
            if (this.oAuthAuthorizationURL == null ? that.oAuthAuthorizationURL != null : !this.oAuthAuthorizationURL.equals(that.oAuthAuthorizationURL)) {
                return false;
            }
            if (this.oAuth2TokenURL == null ? that.oAuth2TokenURL != null : !this.oAuth2TokenURL.equals(that.oAuth2TokenURL)) {
                return false;
            }
            if (this.oAuth2InvalidateTokenURL == null ? that.oAuth2InvalidateTokenURL != null : !this.oAuth2InvalidateTokenURL.equals(that.oAuth2InvalidateTokenURL)) {
                return false;
            }
            if (this.oAuthConsumerKey == null ? that.oAuthConsumerKey != null : !this.oAuthConsumerKey.equals(that.oAuthConsumerKey)) {
                return false;
            }
            if (this.oAuthConsumerSecret == null ? that.oAuthConsumerSecret != null : !this.oAuthConsumerSecret.equals(that.oAuthConsumerSecret)) {
                return false;
            }
            if (this.oAuthRequestTokenURL == null ? that.oAuthRequestTokenURL != null : !this.oAuthRequestTokenURL.equals(that.oAuthRequestTokenURL)) {
                return false;
            }
            if (this.password == null ? that.password != null : !this.password.equals(that.password)) {
                return false;
            }
            if (this.requestHeaders == null ? that.requestHeaders != null : !this.requestHeaders.equals(that.requestHeaders)) {
                return false;
            }
            if (this.restBaseURL == null ? that.restBaseURL != null : !this.restBaseURL.equals(that.restBaseURL)) {
                return false;
            }
            if (this.siteStreamBaseURL == null ? that.siteStreamBaseURL != null : !this.siteStreamBaseURL.equals(that.siteStreamBaseURL)) {
                return false;
            }
            if (this.streamBaseURL == null ? that.streamBaseURL != null : !this.streamBaseURL.equals(that.streamBaseURL)) {
                return false;
            }
            if (this.user == null ? that.user != null : !this.user.equals(that.user)) {
                return false;
            }
            if (this.userAgent == null ? that.userAgent != null : !this.userAgent.equals(that.userAgent)) {
                return false;
            }
            if (this.userStreamBaseURL != null) {
                if (this.userStreamBaseURL.equals(that.userStreamBaseURL)) {
                    return true;
                }
            } else if (that.userStreamBaseURL == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.debug ? 1 : 0;
        return (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((result * 31) + (this.userAgent != null ? this.userAgent.hashCode() : 0)) * 31) + (this.user != null ? this.user.hashCode() : 0)) * 31) + (this.password != null ? this.password.hashCode() : 0)) * 31) + (this.useSSL ? 1 : 0)) * 31) + (this.prettyDebug ? 1 : 0)) * 31) + (this.gzipEnabled ? 1 : 0)) * 31) + (this.httpProxyHost != null ? this.httpProxyHost.hashCode() : 0)) * 31) + (this.httpProxyUser != null ? this.httpProxyUser.hashCode() : 0)) * 31) + (this.httpProxyPassword != null ? this.httpProxyPassword.hashCode() : 0)) * 31) + this.httpProxyPort) * 31) + this.httpConnectionTimeout) * 31) + this.httpReadTimeout) * 31) + this.httpStreamingReadTimeout) * 31) + this.httpRetryCount) * 31) + this.httpRetryIntervalSeconds) * 31) + this.maxTotalConnections) * 31) + this.defaultMaxPerRoute) * 31) + (this.oAuthConsumerKey != null ? this.oAuthConsumerKey.hashCode() : 0)) * 31) + (this.oAuthConsumerSecret != null ? this.oAuthConsumerSecret.hashCode() : 0)) * 31) + (this.oAuthAccessToken != null ? this.oAuthAccessToken.hashCode() : 0)) * 31) + (this.oAuthAccessTokenSecret != null ? this.oAuthAccessTokenSecret.hashCode() : 0)) * 31) + (this.oAuth2TokenType != null ? this.oAuth2TokenType.hashCode() : 0)) * 31) + (this.oAuth2AccessToken != null ? this.oAuth2AccessToken.hashCode() : 0)) * 31) + (this.oAuthRequestTokenURL != null ? this.oAuthRequestTokenURL.hashCode() : 0)) * 31) + (this.oAuthAuthorizationURL != null ? this.oAuthAuthorizationURL.hashCode() : 0)) * 31) + (this.oAuthAccessTokenURL != null ? this.oAuthAccessTokenURL.hashCode() : 0)) * 31) + (this.oAuthAuthenticationURL != null ? this.oAuthAuthenticationURL.hashCode() : 0)) * 31) + (this.oAuth2TokenURL != null ? this.oAuth2TokenURL.hashCode() : 0)) * 31) + (this.oAuth2InvalidateTokenURL != null ? this.oAuth2InvalidateTokenURL.hashCode() : 0)) * 31) + (this.restBaseURL != null ? this.restBaseURL.hashCode() : 0)) * 31) + (this.streamBaseURL != null ? this.streamBaseURL.hashCode() : 0)) * 31) + (this.userStreamBaseURL != null ? this.userStreamBaseURL.hashCode() : 0)) * 31) + (this.siteStreamBaseURL != null ? this.siteStreamBaseURL.hashCode() : 0)) * 31) + (this.dispatcherImpl != null ? this.dispatcherImpl.hashCode() : 0)) * 31) + (this.loggerFactory != null ? this.loggerFactory.hashCode() : 0)) * 31) + this.asyncNumThreads) * 31) + ((int) (this.contributingTo ^ (this.contributingTo >>> 32)))) * 31) + (this.includeRTsEnabled ? 1 : 0)) * 31) + (this.includeEntitiesEnabled ? 1 : 0)) * 31) + (this.includeMyRetweetEnabled ? 1 : 0)) * 31) + (this.trimUserEnabled ? 1 : 0)) * 31) + (this.jsonStoreEnabled ? 1 : 0)) * 31) + (this.mbeanEnabled ? 1 : 0)) * 31) + (this.userStreamRepliesAllEnabled ? 1 : 0)) * 31) + (this.stallWarningsEnabled ? 1 : 0)) * 31) + (this.applicationOnlyAuthEnabled ? 1 : 0)) * 31) + (this.mediaProvider != null ? this.mediaProvider.hashCode() : 0)) * 31) + (this.mediaProviderAPIKey != null ? this.mediaProviderAPIKey.hashCode() : 0)) * 31) + (this.mediaProviderParameters != null ? this.mediaProviderParameters.hashCode() : 0)) * 31) + (this.clientVersion != null ? this.clientVersion.hashCode() : 0)) * 31) + (this.clientURL != null ? this.clientURL.hashCode() : 0)) * 31) + (this.IS_DALVIK ? 1 : 0)) * 31) + (this.IS_GAE ? 1 : 0)) * 31) + (this.requestHeaders != null ? this.requestHeaders.hashCode() : 0);
    }

    public String toString() {
        return "ConfigurationBase{debug=" + this.debug + ", userAgent='" + this.userAgent + "', user='" + this.user + "', password='" + this.password + "', useSSL=" + this.useSSL + ", prettyDebug=" + this.prettyDebug + ", gzipEnabled=" + this.gzipEnabled + ", httpProxyHost='" + this.httpProxyHost + "', httpProxyUser='" + this.httpProxyUser + "', httpProxyPassword='" + this.httpProxyPassword + "', httpProxyPort=" + this.httpProxyPort + ", httpConnectionTimeout=" + this.httpConnectionTimeout + ", httpReadTimeout=" + this.httpReadTimeout + ", httpStreamingReadTimeout=" + this.httpStreamingReadTimeout + ", httpRetryCount=" + this.httpRetryCount + ", httpRetryIntervalSeconds=" + this.httpRetryIntervalSeconds + ", maxTotalConnections=" + this.maxTotalConnections + ", defaultMaxPerRoute=" + this.defaultMaxPerRoute + ", oAuthConsumerKey='" + this.oAuthConsumerKey + "', oAuthConsumerSecret='" + this.oAuthConsumerSecret + "', oAuthAccessToken='" + this.oAuthAccessToken + "', oAuthAccessTokenSecret='" + this.oAuthAccessTokenSecret + "', oAuth2TokenType='" + this.oAuth2TokenType + "', oAuth2AccessToken='" + this.oAuth2AccessToken + "', oAuthRequestTokenURL='" + this.oAuthRequestTokenURL + "', oAuthAuthorizationURL='" + this.oAuthAuthorizationURL + "', oAuthAccessTokenURL='" + this.oAuthAccessTokenURL + "', oAuthAuthenticationURL='" + this.oAuthAuthenticationURL + "', oAuth2TokenURL='" + this.oAuth2TokenURL + "', oAuth2InvalidateTokenURL='" + this.oAuth2InvalidateTokenURL + "', restBaseURL='" + this.restBaseURL + "', streamBaseURL='" + this.streamBaseURL + "', userStreamBaseURL='" + this.userStreamBaseURL + "', siteStreamBaseURL='" + this.siteStreamBaseURL + "', dispatcherImpl='" + this.dispatcherImpl + "', loggerFactory='" + this.loggerFactory + "', asyncNumThreads=" + this.asyncNumThreads + ", contributingTo=" + this.contributingTo + ", includeRTsEnabled=" + this.includeRTsEnabled + ", includeEntitiesEnabled=" + this.includeEntitiesEnabled + ", includeMyRetweetEnabled=" + this.includeMyRetweetEnabled + ", trimUserEnabled=" + this.trimUserEnabled + ", jsonStoreEnabled=" + this.jsonStoreEnabled + ", mbeanEnabled=" + this.mbeanEnabled + ", userStreamRepliesAllEnabled=" + this.userStreamRepliesAllEnabled + ", stallWarningsEnabled=" + this.stallWarningsEnabled + ", applicationOnlyAuthEnabled=" + this.applicationOnlyAuthEnabled + ", mediaProvider='" + this.mediaProvider + "', mediaProviderAPIKey='" + this.mediaProviderAPIKey + "', mediaProviderParameters=" + this.mediaProviderParameters + ", clientVersion='" + this.clientVersion + "', clientURL='" + this.clientURL + "', IS_DALVIK=" + this.IS_DALVIK + ", IS_GAE=" + this.IS_GAE + ", requestHeaders=" + this.requestHeaders + '}';
    }

    private static void cacheInstance(ConfigurationBase conf) {
        if (!instances.contains(conf)) {
            instances.add(conf);
        }
    }

    protected void cacheInstance() {
        cacheInstance(this);
    }

    private static ConfigurationBase getInstance(ConfigurationBase configurationBase) {
        int index = instances.indexOf(configurationBase);
        if (index != -1) {
            return instances.get(index);
        }
        instances.add(configurationBase);
        return configurationBase;
    }

    protected Object readResolve() throws ObjectStreamException {
        return getInstance(this);
    }
}
