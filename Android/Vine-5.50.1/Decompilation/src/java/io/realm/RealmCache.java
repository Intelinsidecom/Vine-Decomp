package io.realm;

import io.realm.internal.ColumnIndices;
import io.realm.internal.log.RealmLog;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
class RealmCache {
    private static Map<String, RealmCache> cachesMap = new HashMap();
    private final RealmConfiguration configuration;
    private final EnumMap<RealmCacheType, RefAndCount> refAndCountMap = new EnumMap<>(RealmCacheType.class);
    private ColumnIndices typedColumnIndices;

    interface Callback {
        void onResult(int i);
    }

    private static class RefAndCount {
        private int globalCount;
        private final ThreadLocal<Integer> localCount;
        private final ThreadLocal<BaseRealm> localRealm;

        private RefAndCount() {
            this.localRealm = new ThreadLocal<>();
            this.localCount = new ThreadLocal<>();
            this.globalCount = 0;
        }

        static /* synthetic */ int access$308(RefAndCount x0) {
            int i = x0.globalCount;
            x0.globalCount = i + 1;
            return i;
        }

        static /* synthetic */ int access$310(RefAndCount x0) {
            int i = x0.globalCount;
            x0.globalCount = i - 1;
            return i;
        }
    }

    private enum RealmCacheType {
        TYPED_REALM,
        DYNAMIC_REALM;

        static RealmCacheType valueOf(Class<? extends BaseRealm> clazz) {
            if (clazz == Realm.class) {
                return TYPED_REALM;
            }
            if (clazz == DynamicRealm.class) {
                return DYNAMIC_REALM;
            }
            throw new IllegalArgumentException("The type of Realm class must be Realm or DynamicRealm.");
        }
    }

    private RealmCache(RealmConfiguration config) {
        this.configuration = config;
        for (RealmCacheType type : RealmCacheType.values()) {
            this.refAndCountMap.put((EnumMap<RealmCacheType, RefAndCount>) type, (RealmCacheType) new RefAndCount());
        }
    }

    static synchronized <E extends BaseRealm> E createRealmOrGetFromCache(RealmConfiguration configuration, Class<E> realmClass) {
        RefAndCount refAndCount;
        BaseRealm realm;
        boolean isCacheInMap = true;
        RealmCache cache = cachesMap.get(configuration.getPath());
        if (cache == null) {
            cache = new RealmCache(configuration);
            isCacheInMap = false;
        } else {
            cache.validateConfiguration(configuration);
        }
        refAndCount = cache.refAndCountMap.get(RealmCacheType.valueOf((Class<? extends BaseRealm>) realmClass));
        if (refAndCount.localRealm.get() == null) {
            if (realmClass == Realm.class) {
                realm = Realm.createInstance(configuration, cache.typedColumnIndices);
            } else if (realmClass == DynamicRealm.class) {
                realm = DynamicRealm.createInstance(configuration);
            } else {
                throw new IllegalArgumentException("The type of Realm class must be Realm or DynamicRealm.");
            }
            if (!isCacheInMap) {
                cachesMap.put(configuration.getPath(), cache);
            }
            refAndCount.localRealm.set(realm);
            refAndCount.localCount.set(0);
        }
        Integer refCount = (Integer) refAndCount.localCount.get();
        if (refCount.intValue() == 0) {
            if (realmClass == Realm.class && refAndCount.globalCount == 0) {
                cache.typedColumnIndices = ((BaseRealm) refAndCount.localRealm.get()).schema.columnIndices;
            }
            RefAndCount.access$308(refAndCount);
        }
        refAndCount.localCount.set(Integer.valueOf(refCount.intValue() + 1));
        return (E) refAndCount.localRealm.get();
    }

    static synchronized void release(BaseRealm realm) {
        String canonicalPath = realm.getPath();
        RealmCache cache = cachesMap.get(canonicalPath);
        Integer refCount = null;
        RefAndCount refAndCount = null;
        if (cache != null) {
            RefAndCount refAndCount2 = cache.refAndCountMap.get(RealmCacheType.valueOf((Class<? extends BaseRealm>) realm.getClass()));
            refAndCount = refAndCount2;
            refCount = (Integer) refAndCount.localCount.get();
        }
        if (refCount == null) {
            refCount = 0;
        }
        if (refCount.intValue() <= 0) {
            RealmLog.w("Realm " + canonicalPath + " has been closed already.");
        } else {
            Integer refCount2 = Integer.valueOf(refCount.intValue() - 1);
            if (refCount2.intValue() == 0) {
                refAndCount.localCount.set(null);
                refAndCount.localRealm.set(null);
                RefAndCount.access$310(refAndCount);
                if (refAndCount.globalCount < 0) {
                    throw new IllegalStateException("Global reference counter of Realm" + canonicalPath + " got corrupted.");
                }
                if ((realm instanceof Realm) && refAndCount.globalCount == 0) {
                    cache.typedColumnIndices = null;
                }
                int totalRefCount = 0;
                for (RealmCacheType type : RealmCacheType.values()) {
                    totalRefCount += cache.refAndCountMap.get(type).globalCount;
                }
                if (totalRefCount == 0) {
                    cachesMap.remove(canonicalPath);
                }
                realm.doClose();
            } else {
                refAndCount.localCount.set(refCount2);
            }
        }
    }

    private void validateConfiguration(RealmConfiguration newConfiguration) {
        if (this.configuration.equals(newConfiguration)) {
            return;
        }
        if (!Arrays.equals(this.configuration.getEncryptionKey(), newConfiguration.getEncryptionKey())) {
            throw new IllegalArgumentException("Wrong key used to decrypt Realm.");
        }
        throw new IllegalArgumentException("Configurations cannot be different if used to open the same file. \nCached configuration: \n" + this.configuration + "\n\nNew configuration: \n" + newConfiguration);
    }

    static synchronized void invokeWithGlobalRefCount(RealmConfiguration configuration, Callback callback) {
        RealmCache cache = cachesMap.get(configuration.getPath());
        if (cache == null) {
            callback.onResult(0);
        } else {
            int totalRefCount = 0;
            for (RealmCacheType type : RealmCacheType.values()) {
                totalRefCount += cache.refAndCountMap.get(type).globalCount;
            }
            callback.onResult(totalRefCount);
        }
    }
}
