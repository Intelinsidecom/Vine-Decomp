package co.vine.android.storage.operation;

import android.os.AsyncTask;
import co.vine.android.storage.RealmManager;
import io.realm.Realm;

/* loaded from: classes.dex */
public abstract class WriteOperation implements RealmOperation<Void> {
    /* JADX WARN: Type inference failed for: r0v0, types: [co.vine.android.storage.operation.WriteOperation$1] */
    @Override // co.vine.android.storage.operation.RealmOperation
    public final Void initiate() {
        new AsyncTask<Void, Void, Void>() { // from class: co.vine.android.storage.operation.WriteOperation.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public final Void doInBackground(Void... params) {
                Realm realm = WriteOperation.this.getRealm();
                realm.beginTransaction();
                WriteOperation.this.execute(realm);
                realm.commitTransaction();
                realm.close();
                return null;
            }
        }.execute(new Void[0]);
        return null;
    }

    public Realm getRealm() {
        return RealmManager.getDefaultInstance();
    }
}
