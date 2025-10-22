package co.vine.android.client;

import co.vine.android.storage.RealmManager;
import co.vine.android.storage.model.SessionData;
import co.vine.android.storage.operation.SessionStoreSyncOperation;
import co.vine.android.storage.operation.WriteOperation;
import co.vine.android.util.CrossConstants;
import io.realm.Realm;

/* loaded from: classes.dex */
public class Session {
    private String mAvatarUrl;
    private String mEdition;
    private LoginStatus mLoginStatus;
    private String mScreenName;
    private String mSessionKey;
    private long mUserId;
    private String mUsername;

    public enum LoginStatus {
        LOGGED_OUT,
        LOGGING_IN,
        LOGGED_IN,
        LOGGING_OUT
    }

    public Session() {
        this(null, 0L, null, null, null, null);
    }

    public Session(SessionData sessionData) {
        this(null, sessionData.getUserId(), sessionData.getUsername(), sessionData.getScreenName(), sessionData.getAvatarUrl(), sessionData.getEdition());
    }

    public Session(String sessionKey, long userId, String username, String screenName, String avatar, String edition) {
        this.mLoginStatus = LoginStatus.LOGGED_OUT;
        this.mSessionKey = sessionKey;
        this.mUserId = userId;
        this.mUsername = username;
        this.mScreenName = screenName;
        this.mAvatarUrl = avatar;
        this.mEdition = edition;
    }

    public LoginStatus getLoginStatus() {
        return this.mLoginStatus;
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.mLoginStatus = loginStatus;
    }

    public boolean isLoggedIn() {
        return this.mLoginStatus.equals(LoginStatus.LOGGED_IN);
    }

    public String getSessionKey() {
        return this.mSessionKey;
    }

    public void setSessionKey(String key) {
        this.mSessionKey = key;
    }

    public long getUserId() {
        return this.mUserId;
    }

    public void setUserId(long userId) {
        this.mUserId = userId;
    }

    public String getUsername() {
        return this.mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getScreenName() {
        return this.mScreenName;
    }

    public void setScreenName(String screenName) {
        this.mScreenName = screenName;
    }

    public String getAvatarUrl() {
        return this.mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public void setEdition(String edition) {
        this.mEdition = edition;
    }

    public void storeAsync() {
        if (!CrossConstants.DISABLE_REALM) {
            RealmManager.executeOperation(new WriteOperation() { // from class: co.vine.android.client.Session.1
                @Override // co.vine.android.storage.operation.RealmOperation
                public Void execute(Realm realm) {
                    SessionData data = (SessionData) realm.where(SessionData.class).equalTo("userId", Long.valueOf(Session.this.mUserId)).findFirst();
                    if (data == null) {
                        data = (SessionData) realm.createObject(SessionData.class);
                    }
                    data.setUserId(Session.this.mUserId);
                    data.setUsername(Session.this.mUsername);
                    data.setScreenName(Session.this.mScreenName);
                    data.setAvatarUrl(Session.this.mAvatarUrl);
                    data.setEdition(Session.this.mEdition);
                    return null;
                }
            });
        }
    }

    public void storeSync() {
        if (!CrossConstants.DISABLE_REALM) {
            RealmManager.executeOperation(new SessionStoreSyncOperation(this.mUserId, this.mUsername, this.mScreenName, this.mAvatarUrl, this.mEdition));
        }
    }

    public void logout() {
        this.mLoginStatus = LoginStatus.LOGGED_OUT;
        this.mSessionKey = null;
        this.mUserId = 0L;
        this.mUsername = null;
        this.mScreenName = null;
        this.mAvatarUrl = null;
        this.mEdition = null;
    }
}
