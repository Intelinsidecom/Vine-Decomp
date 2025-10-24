package io.realm.internal;

import io.realm.RealmObject;
import java.util.Map;

/* loaded from: classes.dex */
public class ColumnIndices {
    private final Map<Class<? extends RealmObject>, ColumnInfo> classes;

    public ColumnIndices(Map<Class<? extends RealmObject>, ColumnInfo> classes) {
        this.classes = classes;
    }

    public ColumnInfo getColumnInfo(Class<? extends RealmObject> clazz) {
        return this.classes.get(clazz);
    }
}
