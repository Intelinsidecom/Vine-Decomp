package co.vine.android.storage.operation;

import io.realm.Realm;

/* loaded from: classes.dex */
public interface RealmOperation<T> {
    T execute(Realm realm);

    T initiate();
}
