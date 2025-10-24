package co.vine.android.storage.migration;

import io.realm.FieldAttribute;
import io.realm.RealmSchema;

/* loaded from: classes.dex */
public class SessionDataMigration extends BaseMigration {
    @Override // co.vine.android.storage.migration.Migration
    public void apply(RealmSchema schema) throws Exception {
        schema.create("SessionData").addField("userId", Long.TYPE, new FieldAttribute[0]).addField("username", String.class, new FieldAttribute[0]).addField("screenName", String.class, new FieldAttribute[0]).addField("avatarUrl", String.class, new FieldAttribute[0]).addField("edition", String.class, new FieldAttribute[0]);
    }

    @Override // co.vine.android.storage.migration.Migration
    public int getToVersion() {
        return 1;
    }
}
