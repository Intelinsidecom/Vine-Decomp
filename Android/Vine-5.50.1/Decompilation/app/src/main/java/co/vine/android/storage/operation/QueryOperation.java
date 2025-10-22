package co.vine.android.storage.operation;

import co.vine.android.storage.RealmManager;
import io.realm.Realm;

/* loaded from: classes.dex */
public abstract class QueryOperation<T> implements RealmOperation<T> {
    @Override // co.vine.android.storage.operation.RealmOperation
    public final T initiate() {
        Realm realm = getRealm();
        T result = execute(realm);
        realm.close();
        return result;
    }

    public Realm getRealm() {
        return RealmManager.getDefaultInstance();
    }
}
