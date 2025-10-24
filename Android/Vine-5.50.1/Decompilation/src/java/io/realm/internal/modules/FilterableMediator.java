package io.realm.internal.modules;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Table;
import io.realm.internal.Util;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class FilterableMediator extends RealmProxyMediator {
    private final Set<Class<? extends RealmObject>> allowedClasses;
    private final RealmProxyMediator originalMediator;

    public FilterableMediator(RealmProxyMediator originalMediator, Collection<Class<? extends RealmObject>> allowedClasses) {
        this.originalMediator = originalMediator;
        Set<Class<? extends RealmObject>> tempAllowedClasses = new HashSet<>();
        if (originalMediator != null) {
            Set<Class<? extends RealmObject>> originalClasses = originalMediator.getModelClasses();
            for (Class<? extends RealmObject> clazz : allowedClasses) {
                if (originalClasses.contains(clazz)) {
                    tempAllowedClasses.add(clazz);
                }
            }
        }
        this.allowedClasses = Collections.unmodifiableSet(tempAllowedClasses);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public Table createTable(Class<? extends RealmObject> clazz, ImplicitTransaction transaction) {
        checkSchemaHasClass(clazz);
        return this.originalMediator.createTable(clazz, transaction);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public ColumnInfo validateTable(Class<? extends RealmObject> clazz, ImplicitTransaction transaction) {
        checkSchemaHasClass(clazz);
        return this.originalMediator.validateTable(clazz, transaction);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public String getTableName(Class<? extends RealmObject> clazz) {
        checkSchemaHasClass(clazz);
        return this.originalMediator.getTableName(clazz);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public <E extends RealmObject> E newInstance(Class<E> cls, ColumnInfo columnInfo) {
        checkSchemaHasClass(cls);
        return (E) this.originalMediator.newInstance(cls, columnInfo);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public Set<Class<? extends RealmObject>> getModelClasses() {
        return this.allowedClasses;
    }

    @Override // io.realm.internal.RealmProxyMediator
    public <E extends RealmObject> E copyOrUpdate(Realm realm, E e, boolean z, Map<RealmObject, RealmObjectProxy> map) {
        checkSchemaHasClass(Util.getOriginalModelClass(e.getClass()));
        return (E) this.originalMediator.copyOrUpdate(realm, e, z, map);
    }

    private void checkSchemaHasClass(Class<? extends RealmObject> clazz) {
        if (!this.allowedClasses.contains(clazz)) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " is not part of the schema for this Realm");
        }
    }
}
