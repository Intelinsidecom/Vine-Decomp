package co.vine.android.storage;

import android.content.Context;
import co.vine.android.storage.operation.RealmOperation;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/* loaded from: classes.dex */
public class RealmManager {
    private static RealmConfiguration sDefaultConfig;

    public static void initialize(Context context) {
        sDefaultConfig = new RealmConfiguration.Builder(context).schemaVersion(1L).migration(new RealmMigrator()).build();
    }

    public static Realm getDefaultInstance() {
        return Realm.getInstance(sDefaultConfig);
    }

    public static <T> T executeOperation(RealmOperation<T> operation) {
        return operation.initiate();
    }

    public static void clearAllTables(Realm realm) {
        realm.close();
        Realm.deleteRealm(realm.getConfiguration());
    }
}
