package twitter4j;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

/* loaded from: classes.dex */
public final class VineTwitterFactory implements Serializable {
    static final Authorization DEFAULT_AUTHORIZATION = AuthorizationFactory.getInstance(ConfigurationContext.getInstance());
    private static final Twitter SINGLETON;
    private static final Constructor<Twitter> TWITTER_CONSTRUCTOR;
    private static final long serialVersionUID = 5193900138477709155L;
    private final Configuration conf;

    static {
        String className = null;
        if (ConfigurationContext.getInstance().isGAE()) {
            try {
                Class.forName("twitter4j.AppEngineTwitterImpl");
                className = "twitter4j.AppEngineTwitterImpl";
            } catch (ClassNotFoundException e) {
            }
        }
        if (className == null) {
            className = "twitter4j.VineTwitter4j";
        }
        try {
            Class clazz = Class.forName(className);
            Constructor<Twitter> constructor = clazz.getDeclaredConstructor(Configuration.class, Authorization.class);
            TWITTER_CONSTRUCTOR = constructor;
            try {
                SINGLETON = TWITTER_CONSTRUCTOR.newInstance(ConfigurationContext.getInstance(), DEFAULT_AUTHORIZATION);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InstantiationException e3) {
                throw new AssertionError(e3);
            } catch (InvocationTargetException e4) {
                throw new AssertionError(e4);
            }
        } catch (ClassNotFoundException e5) {
            throw new AssertionError(e5);
        } catch (NoSuchMethodException e6) {
            throw new AssertionError(e6);
        }
    }

    public VineTwitterFactory() {
        this(ConfigurationContext.getInstance());
    }

    public VineTwitterFactory(Configuration conf) {
        if (conf == null) {
            throw new NullPointerException("configuration cannot be null");
        }
        this.conf = conf;
    }

    public VineTwitterFactory(String configTreePath) {
        this(ConfigurationContext.getInstance(configTreePath));
    }

    public Twitter getInstance() {
        return getInstance(AuthorizationFactory.getInstance(this.conf));
    }

    public Twitter getInstance(AccessToken accessToken) {
        String consumerKey = this.conf.getOAuthConsumerKey();
        String consumerSecret = this.conf.getOAuthConsumerSecret();
        if (consumerKey == null && consumerSecret == null) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth2 = new OAuthAuthorization(this.conf);
        oauth2.setOAuthAccessToken(accessToken);
        return getInstance(oauth2);
    }

    public Twitter getInstance(Authorization auth) {
        try {
            return TWITTER_CONSTRUCTOR.newInstance(this.conf, auth);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InstantiationException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new AssertionError(e3);
        }
    }

    public static Twitter getSingleton() {
        return SINGLETON;
    }
}
