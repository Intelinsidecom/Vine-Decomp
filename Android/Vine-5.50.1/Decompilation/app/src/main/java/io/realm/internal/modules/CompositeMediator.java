package io.realm.internal.modules;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Table;
import io.realm.internal.Util;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class CompositeMediator extends RealmProxyMediator {
    private final Map<Class<? extends RealmObject>, RealmProxyMediator> mediators;

    public CompositeMediator(RealmProxyMediator... mediators) {
        HashMap<Class<? extends RealmObject>, RealmProxyMediator> tempMediators = new HashMap<>();
        if (mediators != null) {
            for (RealmProxyMediator mediator : mediators) {
                for (Class<? extends RealmObject> realmClass : mediator.getModelClasses()) {
                    tempMediators.put(realmClass, mediator);
                }
            }
        }
        this.mediators = Collections.unmodifiableMap(tempMediators);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public Table createTable(Class<? extends RealmObject> clazz, ImplicitTransaction transaction) {
        RealmProxyMediator mediator = getMediator(clazz);
        return mediator.createTable(clazz, transaction);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public ColumnInfo validateTable(Class<? extends RealmObject> clazz, ImplicitTransaction transaction) {
        RealmProxyMediator mediator = getMediator(clazz);
        return mediator.validateTable(clazz, transaction);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public String getTableName(Class<? extends RealmObject> clazz) {
        RealmProxyMediator mediator = getMediator(clazz);
        return mediator.getTableName(clazz);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public <E extends RealmObject> E newInstance(Class<E> cls, ColumnInfo columnInfo) {
        return (E) getMediator(cls).newInstance(cls, columnInfo);
    }

    @Override // io.realm.internal.RealmProxyMediator
    public Set<Class<? extends RealmObject>> getModelClasses() {
        return this.mediators.keySet();
    }

    @Override // io.realm.internal.RealmProxyMediator
    public <E extends RealmObject> E copyOrUpdate(Realm realm, E e, boolean z, Map<RealmObject, RealmObjectProxy> map) {
        return (E) getMediator(Util.getOriginalModelClass(e.getClass())).copyOrUpdate(realm, e, z, map);
    }

    private RealmProxyMediator getMediator(Class<? extends RealmObject> clazz) {
        RealmProxyMediator mediator = this.mediators.get(clazz);
        if (mediator == null) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " is not part of the schema for this Realm");
        }
        return mediator;
    }
}
