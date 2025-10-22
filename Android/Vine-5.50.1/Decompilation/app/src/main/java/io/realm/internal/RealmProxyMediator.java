package io.realm.internal;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.exceptions.RealmException;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public abstract class RealmProxyMediator {
    public abstract <E extends RealmObject> E copyOrUpdate(Realm realm, E e, boolean z, Map<RealmObject, RealmObjectProxy> map);

    public abstract Table createTable(Class<? extends RealmObject> cls, ImplicitTransaction implicitTransaction);

    public abstract Set<Class<? extends RealmObject>> getModelClasses();

    public abstract String getTableName(Class<? extends RealmObject> cls);

    public abstract <E extends RealmObject> E newInstance(Class<E> cls, ColumnInfo columnInfo);

    public abstract ColumnInfo validateTable(Class<? extends RealmObject> cls, ImplicitTransaction implicitTransaction);

    public boolean equals(Object o) {
        if (!(o instanceof RealmProxyMediator)) {
            return false;
        }
        RealmProxyMediator other = (RealmProxyMediator) o;
        return getModelClasses().equals(other.getModelClasses());
    }

    public int hashCode() {
        return getModelClasses().hashCode();
    }

    protected static void checkClass(Class<? extends RealmObject> clazz) {
        if (clazz == null) {
            throw new NullPointerException("A class extending RealmObject must be provided");
        }
    }

    protected static RealmException getMissingProxyClassException(Class<? extends RealmObject> clazz) {
        return new RealmException(clazz + " is not part of the schema for this Realm.");
    }
}
