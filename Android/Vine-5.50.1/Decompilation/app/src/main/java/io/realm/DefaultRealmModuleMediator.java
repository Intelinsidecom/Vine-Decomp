package io.realm;

import co.vine.android.storage.model.LongformData;
import co.vine.android.storage.model.SessionData;
import io.realm.annotations.RealmModule;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RealmModule
/* loaded from: classes.dex */
class DefaultRealmModuleMediator extends RealmProxyMediator {
    private static final Set<Class<? extends RealmObject>> MODEL_CLASSES;

    DefaultRealmModuleMediator() {
    }

    static {
        Set<Class<? extends RealmObject>> modelClasses = new HashSet<>();
        modelClasses.add(SessionData.class);
        modelClasses.add(LongformData.class);
        MODEL_CLASSES = Collections.unmodifiableSet(modelClasses);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public Table createTable(Class<? extends RealmObject> clazz, ImplicitTransaction transaction) {
        checkClass(clazz);
        if (clazz.equals(SessionData.class)) {
            return SessionDataRealmProxy.initTable(transaction);
        }
        if (clazz.equals(LongformData.class)) {
            return LongformDataRealmProxy.initTable(transaction);
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public ColumnInfo validateTable(Class<? extends RealmObject> clazz, ImplicitTransaction transaction) {
        checkClass(clazz);
        if (clazz.equals(SessionData.class)) {
            return SessionDataRealmProxy.validateTable(transaction);
        }
        if (clazz.equals(LongformData.class)) {
            return LongformDataRealmProxy.validateTable(transaction);
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public String getTableName(Class<? extends RealmObject> clazz) {
        checkClass(clazz);
        if (clazz.equals(SessionData.class)) {
            return SessionDataRealmProxy.getTableName();
        }
        if (clazz.equals(LongformData.class)) {
            return LongformDataRealmProxy.getTableName();
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public <E extends RealmObject> E newInstance(Class<E> clazz, ColumnInfo columnInfo) {
        checkClass(clazz);
        if (clazz.equals(SessionData.class)) {
            return clazz.cast(new SessionDataRealmProxy(columnInfo));
        }
        if (clazz.equals(LongformData.class)) {
            return clazz.cast(new LongformDataRealmProxy(columnInfo));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public Set<Class<? extends RealmObject>> getModelClasses() {
        return MODEL_CLASSES;
    }

    @Override // io.realm.internal.RealmProxyMediator
    public <E extends RealmObject> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmObject, RealmObjectProxy> cache) {
        Class<?> superclass = obj instanceof RealmObjectProxy ? obj.getClass().getSuperclass() : obj.getClass();
        if (superclass.equals(SessionData.class)) {
            return (E) superclass.cast(SessionDataRealmProxy.copyOrUpdate(realm, (SessionData) obj, update, cache));
        }
        if (superclass.equals(LongformData.class)) {
            return (E) superclass.cast(LongformDataRealmProxy.copyOrUpdate(realm, (LongformData) obj, update, cache));
        }
        throw getMissingProxyClassException(superclass);
    }
}
