package co.vine.android.storage;

import co.vine.android.storage.migration.Migration;
import co.vine.android.storage.migration.SessionDataMigration;
import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class RealmMigrator implements RealmMigration {
    private static final ArrayList<Migration> sMigrations = new ArrayList<>();

    static {
        sMigrations.add(new SessionDataMigration());
    }

    @Override // io.realm.RealmMigration
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        for (int i = 0; i < 1; i++) {
            Migration migration = sMigrations.get(i);
            if (migration.getFromVersion() == i) {
                if (i >= oldVersion) {
                    migration.apply(schema);
                }
            } else {
                throw new IllegalStateException("Realm tried applying migrations in the wrong order.");
            }
        }
    }
}
