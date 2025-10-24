package com.twitter.sdk.android.core;

import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.MigrationHelper;
import com.twitter.sdk.android.core.internal.SessionMonitor;
import com.twitter.sdk.android.core.internal.TwitterSessionVerifier;
import com.twitter.sdk.android.core.internal.scribe.TwitterCoreScribeClientHolder;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.network.NetworkUtils;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.SSLSocketFactory;

/* loaded from: classes.dex */
public class TwitterCore extends Kit<Boolean> {
    private final ConcurrentHashMap<Session, TwitterApiClient> apiClients = new ConcurrentHashMap<>();
    SessionManager<AppSession> appSessionManager;
    private final TwitterAuthConfig authConfig;
    SessionMonitor<TwitterSession> sessionMonitor;
    private volatile SSLSocketFactory sslSocketFactory;
    SessionManager<TwitterSession> twitterSessionManager;

    public TwitterCore(TwitterAuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    public static TwitterCore getInstance() {
        checkInitialized();
        return (TwitterCore) Fabric.getKit(TwitterCore.class);
    }

    @Override // io.fabric.sdk.android.Kit
    public String getVersion() {
        return "1.6.4.99";
    }

    public TwitterAuthConfig getAuthConfig() {
        return this.authConfig;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        checkInitialized();
        if (this.sslSocketFactory == null) {
            createSSLSocketFactory();
        }
        return this.sslSocketFactory;
    }

    private synchronized void createSSLSocketFactory() {
        if (this.sslSocketFactory == null) {
            try {
                this.sslSocketFactory = NetworkUtils.getSSLSocketFactory(new TwitterPinningInfoProvider(getContext()));
                Fabric.getLogger().d("Twitter", "Custom SSL pinning enabled");
            } catch (Exception e) {
                Fabric.getLogger().e("Twitter", "Exception setting up custom SSL pinning", e);
            }
        }
    }

    @Override // io.fabric.sdk.android.Kit
    protected boolean onPreExecute() {
        MigrationHelper migrationHelper = new MigrationHelper();
        migrationHelper.migrateSessionStore(getContext(), getIdentifier(), getIdentifier() + ":session_store.xml");
        this.twitterSessionManager = new PersistedSessionManager(new PreferenceStoreImpl(getContext(), "session_store"), new TwitterSession.Serializer(), "active_twittersession", "twittersession");
        this.sessionMonitor = new SessionMonitor<>(this.twitterSessionManager, getFabric().getExecutorService(), new TwitterSessionVerifier());
        this.appSessionManager = new PersistedSessionManager(new PreferenceStoreImpl(getContext(), "session_store"), new AppSession.Serializer(), "active_appsession", "appsession");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.fabric.sdk.android.Kit
    public Boolean doInBackground() {
        this.twitterSessionManager.getActiveSession();
        this.appSessionManager.getActiveSession();
        getSSLSocketFactory();
        initializeScribeClient();
        this.sessionMonitor.monitorActivityLifecycle(getFabric().getActivityLifecycleManager());
        return true;
    }

    @Override // io.fabric.sdk.android.Kit
    public String getIdentifier() {
        return "com.twitter.sdk.android:twitter-core";
    }

    private static void checkInitialized() {
        if (Fabric.getKit(TwitterCore.class) == null) {
            throw new IllegalStateException("Must start Twitter Kit with Fabric.with() first");
        }
    }

    private void initializeScribeClient() {
        List<SessionManager<? extends Session>> sessionManagers = new ArrayList<>();
        sessionManagers.add(this.twitterSessionManager);
        sessionManagers.add(this.appSessionManager);
        TwitterCoreScribeClientHolder.initialize(this, sessionManagers, getIdManager());
    }

    public SessionManager<TwitterSession> getSessionManager() {
        checkInitialized();
        return this.twitterSessionManager;
    }
}
