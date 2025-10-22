package co.vine.android.client;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import co.vine.android.AppImpl;
import co.vine.android.StandalonePreference;
import co.vine.android.StartActivity;
import co.vine.android.VineLoggingException;
import co.vine.android.api.VineLogin;
import co.vine.android.api.VineUser;
import co.vine.android.client.Session;
import co.vine.android.provider.SettingsManager;
import co.vine.android.service.GCMRegistrationService;
import co.vine.android.service.SessionChangedReceiver;
import co.vine.android.social.FacebookHelper;
import co.vine.android.storage.RealmManager;
import co.vine.android.storage.model.SessionData;
import co.vine.android.storage.operation.QueryOperation;
import co.vine.android.storage.operation.WriteOperation;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.Util;
import com.flurry.android.FlurryAgent;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class SessionManager {
    private static final SessionManager sInstance = new SessionManager();
    private Session mCurrentSession = new Session();
    private final List<Session> mActiveSessions = new ArrayList();

    protected SessionManager() {
    }

    public static SessionManager getSharedInstance() {
        return sInstance;
    }

    public void initSessions(Context context) {
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType(VineAccountHelper.ACCOUNT_TYPE);
        if (accounts.length != 0) {
            SharedPreferences sharedPreferences = Util.getDefaultSharedPrefs(context);
            long currentId = sharedPreferences.getLong("current_user_id", 0L);
            List<Session> persistentSessions = getStoredSessions();
            if (persistentSessions != null && !persistentSessions.isEmpty()) {
                for (Session session : persistentSessions) {
                    Session restoredSession = restoreRealmSession(context, session, am);
                    if (restoredSession != null) {
                        this.mActiveSessions.add(restoredSession);
                        if (currentId == restoredSession.getUserId()) {
                            setCurrentSession(context, restoredSession);
                        }
                    }
                }
            } else {
                Session restoredSession2 = restoreSQLiteSession(context, SettingsManager.getCurrentAccount(context), am);
                if (restoredSession2 != null) {
                    this.mActiveSessions.add(restoredSession2);
                    setCurrentSession(context, restoredSession2);
                }
            }
            if (!this.mCurrentSession.isLoggedIn() && !this.mActiveSessions.isEmpty()) {
                setCurrentSession(context, this.mActiveSessions.get(0));
            }
            if (this.mCurrentSession.isLoggedIn()) {
                sharedPreferences.edit().putLong("current_user_id", this.mCurrentSession.getUserId()).apply();
            }
        }
    }

    private Session restoreRealmSession(Context context, Session session, AccountManager am) {
        Account account = VineAccountHelper.getAccount(context, session.getUserId(), session.getUsername());
        if (account == null) {
            return null;
        }
        String sessionKey = VineAccountHelper.getSessionKey(am, account);
        if (TextUtils.isEmpty(sessionKey)) {
            return null;
        }
        session.setSessionKey(sessionKey);
        session.setLoginStatus(Session.LoginStatus.LOGGED_IN);
        return session;
    }

    private Session restoreSQLiteSession(Context context, String username, AccountManager am) {
        Account account = VineAccountHelper.getAccount(context, 0L, username);
        if (account != null) {
            String sessionKey = VineAccountHelper.getSessionKey(am, account);
            if (!TextUtils.isEmpty(sessionKey)) {
                Session session = new Session(sessionKey, VineAccountHelper.getUserId(am, account), account.name, VineAccountHelper.getName(am, account), VineAccountHelper.getAvatarUrl(am, account), SettingsManager.getEdition(context));
                session.setLoginStatus(Session.LoginStatus.LOGGED_IN);
                session.storeAsync();
                return session;
            }
        }
        return null;
    }

    public void resetSessions(Context context) {
        this.mActiveSessions.clear();
        this.mCurrentSession.logout();
        initSessions(context);
    }

    public Session getCurrentSession() {
        return this.mCurrentSession;
    }

    public List<Session> getSessions() {
        return new ArrayList(this.mActiveSessions);
    }

    public void setCurrentSession(Session session) {
        this.mCurrentSession = session;
        if (session.isLoggedIn() && !this.mActiveSessions.contains(session)) {
            this.mActiveSessions.add(session);
        }
    }

    public void setCurrentSession(Context context, Session session) {
        setCurrentSession(session);
        if (session.getUserId() != 0) {
            Util.getDefaultSharedPrefs(context).edit().putLong("current_user_id", session.getUserId()).apply();
        }
    }

    protected List<Session> getStoredSessions() {
        if (CrossConstants.DISABLE_REALM) {
            return null;
        }
        return (List) RealmManager.executeOperation(new QueryOperation<List<Session>>() { // from class: co.vine.android.client.SessionManager.1
            @Override // co.vine.android.storage.operation.RealmOperation
            public List<Session> execute(Realm realm) {
                List<Session> results = new ArrayList<>();
                Iterator it = realm.where(SessionData.class).findAll().iterator();
                while (it.hasNext()) {
                    SessionData sessionData = (SessionData) it.next();
                    results.add(new Session(sessionData));
                }
                return results;
            }
        });
    }

    public void setCurrentSessionKey(String key) {
        this.mCurrentSession.setSessionKey(key);
    }

    public void onAddSession(Context context) {
        Intent intent = new Intent(context, (Class<?>) StartActivity.class);
        intent.putExtra("is_new_user", false);
        intent.putExtra("logged_in_add_account", true);
        context.startActivity(intent);
    }

    public void loginComplete(Context context, int responseCode, VineLogin login, VineUser user) throws VineLoggingException {
        loginComplete(context, responseCode, user.username, null, login, user.avatarUrl);
    }

    public void loginComplete(Context context, int responseCode, String accountName, String password, VineLogin login, String avatarUrl) throws VineLoggingException {
        String uniqueLogin;
        if (responseCode == 200) {
            this.mCurrentSession.setLoginStatus(Session.LoginStatus.LOGGED_IN);
            switch (login.loginType) {
                case 1:
                    uniqueLogin = login.username;
                    break;
                case 2:
                    uniqueLogin = accountName;
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported login type.");
            }
            this.mCurrentSession.setSessionKey(login.key);
            this.mCurrentSession.setUserId(login.userId);
            this.mCurrentSession.setUsername(login.username);
            this.mCurrentSession.setScreenName(accountName);
            this.mCurrentSession.setAvatarUrl(avatarUrl);
            this.mCurrentSession.storeSync();
            Iterator<Session> it = this.mActiveSessions.iterator();
            while (true) {
                if (it.hasNext()) {
                    Session session = it.next();
                    if (session.getUserId() == login.userId) {
                        this.mActiveSessions.remove(session);
                    }
                }
            }
            this.mActiveSessions.add(this.mCurrentSession);
            VineAccountHelper.createAccount(context, uniqueLogin, login, password, accountName);
            SharedPreferences sharedPreferences = Util.getDefaultSharedPrefs(context);
            if (sharedPreferences.getLong("last_user_id", 0L) != login.userId) {
                FacebookHelper.clearFacebookToken(context);
                Intent i = AppImpl.getInstance().getDiscardAllIntent(context);
                if (i != null) {
                    context.startService(i);
                }
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("current_user_id", login.userId);
            if (login.edition != null) {
                editor.putString("settings_edition", login.edition);
            }
            editor.apply();
            FlurryAgent.setUserId(String.valueOf(login.userId));
        }
    }

    public boolean isLoggedIn() {
        return this.mCurrentSession.isLoggedIn();
    }

    public void updateLocalSessionData(VineUser user) {
        this.mCurrentSession.setUserId(user.userId);
        this.mCurrentSession.setUsername(user.email);
        this.mCurrentSession.setScreenName(user.username);
        this.mCurrentSession.setAvatarUrl(user.avatarUrl);
        this.mCurrentSession.setEdition(user.edition);
        this.mCurrentSession.storeAsync();
    }

    public void clearLocalDataForSessionLogout(Context context) {
        AppController appController = AppController.getInstance(context);
        if (!Session.LoginStatus.LOGGED_OUT.equals(this.mCurrentSession.getLoginStatus())) {
            String cachedKey = this.mCurrentSession.getSessionKey();
            SharedPreferences sharedPreferences = Util.getDefaultSharedPrefs(context);
            sharedPreferences.edit().clear().apply();
            StandalonePreference.clearAll(context);
            appController.clearDbAll();
            appController.clearAccount();
            context.startService(GCMRegistrationService.getUnregisterIntent(context, this.mCurrentSession.getUserId(), cachedKey));
            VineAccountHelper.removeAccount(context, this.mCurrentSession.getUserId(), this.mCurrentSession.getUsername());
            this.mActiveSessions.remove(this.mCurrentSession);
            if (!CrossConstants.DISABLE_REALM) {
                if (this.mActiveSessions.isEmpty()) {
                    RealmManager.clearAllTables(RealmManager.getDefaultInstance());
                } else {
                    final long userId = this.mCurrentSession.getUserId();
                    RealmManager.executeOperation(new WriteOperation() { // from class: co.vine.android.client.SessionManager.2
                        @Override // co.vine.android.storage.operation.RealmOperation
                        public Void execute(Realm realm) {
                            realm.where(SessionData.class).equalTo("userId", Long.valueOf(userId)).findAll().clear();
                            return null;
                        }
                    });
                }
            }
            if (this.mActiveSessions.isEmpty()) {
                appController.clearFileCache();
                sharedPreferences.edit().putLong("last_user_id", this.mCurrentSession.getUserId()).remove("current_user_id").apply();
                this.mCurrentSession.logout();
            } else {
                this.mCurrentSession = this.mActiveSessions.get(0);
            }
            Intent logoutIntent = SessionChangedReceiver.createSessionLogoutIntent();
            context.sendBroadcast(logoutIntent, CrossConstants.BROADCAST_PERMISSION);
            Intent i = AppImpl.getInstance().getClearNotificationsIntent(context);
            if (i != null) {
                context.startService(i);
            }
            StartActivity.toStart(context);
        }
    }
}
