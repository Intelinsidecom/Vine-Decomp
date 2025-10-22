package co.vine.android.storage.operation;

import co.vine.android.storage.RealmManager;
import co.vine.android.storage.model.SessionData;
import io.realm.Realm;

/* loaded from: classes.dex */
public final class SessionStoreSyncOperation implements RealmOperation<Void> {
    private final String mAvatarUrl;
    private final String mEdition;
    private final String mScreenName;
    private final long mUserId;
    private final String mUsername;

    public SessionStoreSyncOperation(long userId, String username, String screenName, String avatarUrl, String edition) {
        this.mUserId = userId;
        this.mUsername = username;
        this.mScreenName = screenName;
        this.mAvatarUrl = avatarUrl;
        this.mEdition = edition;
    }

    @Override // co.vine.android.storage.operation.RealmOperation
    public final Void initiate() {
        Realm realm = getRealm();
        realm.beginTransaction();
        execute(realm);
        realm.commitTransaction();
        realm.close();
        return null;
    }

    @Override // co.vine.android.storage.operation.RealmOperation
    public final Void execute(Realm realm) {
        SessionData data = (SessionData) realm.where(SessionData.class).equalTo("userId", Long.valueOf(this.mUserId)).findFirst();
        if (data == null) {
            data = (SessionData) realm.createObject(SessionData.class);
        }
        data.setUserId(this.mUserId);
        data.setUsername(this.mUsername);
        data.setScreenName(this.mScreenName);
        data.setAvatarUrl(this.mAvatarUrl);
        data.setEdition(this.mEdition);
        return null;
    }

    public final Realm getRealm() {
        return RealmManager.getDefaultInstance();
    }
}
