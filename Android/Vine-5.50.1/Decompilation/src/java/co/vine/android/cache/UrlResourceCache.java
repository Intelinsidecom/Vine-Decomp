package co.vine.android.cache;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import co.vine.android.cache.CacheKey;
import co.vine.android.cache.UrlResource;
import co.vine.android.network.HttpResult;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationFactory;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.util.CommonUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public abstract class UrlResourceCache<K extends CacheKey, V extends UrlResource<T>, T, J> {
    protected static final boolean LOGGABLE = Log.isLoggable("ResourceCache", 3);
    protected static final ExecutorService sExecutor = Executors.newSingleThreadExecutor();
    private static final Handler sMainHandler = new Handler(Looper.getMainLooper());
    protected final Context mContext;
    private final NetworkOperationFactory<J> mNetworkFactory;
    private final J mNetworkOperationContext;
    private final LruCache<K, V> mResourceCache;
    final int[] mLock = new int[0];
    protected final LinkedHashMap<K, PendingRes> mPendingDownload = new LinkedHashMap<>(20, 10.0f, true);
    protected final HashMap<K, PendingRes> mQueuedDownload = new HashMap<>(20, 10.0f);

    protected abstract V instantiateResource(K k, String str, T t);

    protected abstract V loadResource(long j, K k, String str);

    protected abstract V obtainResource(K k, String str, InputStream inputStream);

    protected abstract void onResourceError(K k, HttpResult httpResult);

    protected abstract void onResourceLoaded(HashMap<K, V> map);

    protected abstract V saveResource(long j, K k, String str, InputStream inputStream, boolean z);

    public UrlResourceCache(Context context, int maxCacheSize, J networkOperationContext, NetworkOperationFactory<J> networkFactory) {
        this.mContext = context.getApplicationContext();
        if (maxCacheSize > 0) {
            this.mResourceCache = new MemoryLruCache(maxCacheSize);
        } else {
            this.mResourceCache = null;
        }
        this.mNetworkOperationContext = networkOperationContext;
        this.mNetworkFactory = networkFactory;
    }

    public Map<K, V> snapShot() {
        return this.mResourceCache.snapshot();
    }

    public void clear() {
        if (this.mResourceCache != null) {
            synchronized (this.mResourceCache) {
                this.mResourceCache.evictAll();
            }
        }
        synchronized (this.mLock) {
            this.mPendingDownload.clear();
        }
    }

    protected final V get(long j, K k, String str, boolean z) {
        V v = (V) get(j, k, str, z, false);
        if (v != null) {
            k.setLoadedFromMemory();
        }
        return v;
    }

    protected final V get(long j, K k, String str, boolean z, boolean z2) {
        V v;
        if (str == null) {
            return null;
        }
        if (LOGGABLE) {
            Log.d("ResourceCache", "Url: " + str);
        }
        if (this.mResourceCache != null) {
            synchronized (this.mResourceCache) {
                v = this.mResourceCache.get(k);
            }
        } else {
            v = null;
        }
        if (v != null) {
            if (LOGGABLE) {
                Log.d("ResourceCache", "Have resource: " + v.url);
            }
            if (!v.url.equals(str) || (v.nextRequestTime > 0 && v.nextRequestTime < System.currentTimeMillis())) {
                return (V) queueResourceLoad(j, k, str, z, z2);
            }
            k.wasLastLoadFromMemory();
            return v;
        }
        if (LOGGABLE) {
            Log.d("ResourceCache", "Looking in persistent storage: " + str);
        }
        return (V) queueResourceLoad(j, k, str, z, z2);
    }

    private V queueResourceLoad(long j, K k, String str, boolean z, boolean z2) {
        if (!TextUtils.isEmpty(str)) {
            if (z2) {
                return (V) loadResource(j, k, str);
            }
            synchronized (this.mLock) {
                if (!this.mPendingDownload.containsKey(k)) {
                    PendingRes pendingRes = new PendingRes(str, z);
                    this.mPendingDownload.put(k, pendingRes);
                    this.mQueuedDownload.put(k, pendingRes);
                    sMainHandler.postDelayed(new QueueRunnable(j), 100L);
                }
            }
        }
        return null;
    }

    void urlResourceLoaded(HashMap<K, V> result) {
        if (!result.isEmpty()) {
            for (Map.Entry<K, V> entry : result.entrySet()) {
                K key = entry.getKey();
                V resource = entry.getValue();
                if (this.mResourceCache != null && resource != null) {
                    synchronized (this.mResourceCache) {
                        this.mResourceCache.put(key, resource);
                    }
                }
                synchronized (this.mLock) {
                    this.mPendingDownload.remove(key);
                }
            }
            onResourceLoaded(result);
        }
    }

    public boolean hasPendingItems() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mQueuedDownload.size() > 0;
        }
        return z;
    }

    private static class PendingRes {
        final boolean persist;
        final String url;

        public PendingRes(String url, boolean persist) {
            this.url = url;
            this.persist = persist;
        }
    }

    private class QueueRunnable implements Runnable {
        private final long mOwnerId;

        public QueueRunnable(long ownerId) {
            this.mOwnerId = ownerId;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (UrlResourceCache.this.mLock) {
                if (!UrlResourceCache.this.mQueuedDownload.isEmpty()) {
                    HashMap<K, PendingRes> workQueue = new HashMap<>(UrlResourceCache.this.mQueuedDownload.size());
                    workQueue.putAll(UrlResourceCache.this.mQueuedDownload);
                    UrlResourceCache.this.mQueuedDownload.clear();
                    UrlResourceCache.sExecutor.execute(new FetchRunnable(this.mOwnerId, workQueue));
                }
            }
        }
    }

    private class FetchRunnable implements Runnable {
        private final long mOwnerId;
        private final HashMap<K, PendingRes> mWorkQueue;

        public FetchRunnable(long ownerId, HashMap<K, PendingRes> queue) {
            this.mOwnerId = ownerId;
            this.mWorkQueue = queue;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public void run() {
            HashMap<K, PendingRes> copy = this.mWorkQueue;
            if (UrlResourceCache.LOGGABLE) {
                Log.d("ResourceCache", "Queueing " + copy.size());
            }
            if (!copy.isEmpty()) {
                HashMap map = new HashMap(copy.size());
                for (Map.Entry<K, PendingRes> entry : copy.entrySet()) {
                    K key = entry.getKey();
                    PendingRes pendingRes = entry.getValue();
                    String url = pendingRes.url;
                    key.setLoadStartTime(System.currentTimeMillis());
                    UrlResource urlResourceLoadResource = UrlResourceCache.this.loadResource(this.mOwnerId, key, url);
                    if (urlResourceLoadResource != null) {
                        key.setCacheState(CacheKey.CacheState.DISK);
                        map.put(key, urlResourceLoadResource);
                    } else {
                        key.setCacheState(CacheKey.CacheState.NOT_CACHED);
                        if (UrlResourceCache.LOGGABLE) {
                            Log.d("ResourceCache", "Fetch " + url);
                        }
                        URI uri = CommonUtil.parseURI(url);
                        if (uri != null) {
                            UrlResourceCache<K, V, T, J>.ResourceHttpOperationReader reader = new ResourceHttpOperationReader(this.mOwnerId, key, url, pendingRes.persist);
                            NetworkOperation op = UrlResourceCache.this.mNetworkFactory.createResourceGetRequest(UrlResourceCache.this.mContext, new StringBuilder(url), UrlResourceCache.this.mNetworkOperationContext, reader).execute();
                            if (op.isOK()) {
                                urlResourceLoadResource = reader.getResultResource();
                            }
                            if (urlResourceLoadResource == null) {
                                urlResourceLoadResource = UrlResourceCache.this.instantiateResource(key, url, null);
                                urlResourceLoadResource.nextRequestTime = System.currentTimeMillis() + 60000;
                            }
                            map.put(key, urlResourceLoadResource);
                        }
                    }
                    key.setLoadTime((int) (System.currentTimeMillis() - key.getLoadStartTime()));
                }
                UrlResourceCache.sMainHandler.post(new ResultRunnable(map));
            }
        }
    }

    private static class MemoryLruCache<K, V extends UrlResource> extends LruCache<K, V> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.util.LruCache
        protected /* bridge */ /* synthetic */ int sizeOf(Object obj, Object obj2) {
            return sizeOf((MemoryLruCache<K, V>) obj, obj2);
        }

        public MemoryLruCache(int maxMemory) {
            super(maxMemory);
        }

        protected int sizeOf(K key, V value) {
            return value.size();
        }
    }

    private class ResourceHttpOperationReader implements NetworkOperationReader {
        private final K mKey;
        private final long mOwnerId;
        private final boolean mPersist;
        private V mResource = null;
        private final String mUrl;

        public ResourceHttpOperationReader(long ownerId, K key, String url, boolean persist) {
            this.mOwnerId = ownerId;
            this.mKey = key;
            this.mUrl = url;
            this.mPersist = persist;
        }

        public final V getResultResource() {
            return this.mResource;
        }

        @Override // co.vine.android.network.NetworkOperationReader
        public final void readInput(int i, long j, InputStream inputStream) throws IOException {
            if (this.mPersist) {
                this.mResource = (V) UrlResourceCache.this.saveResource(this.mOwnerId, this.mKey, this.mUrl, inputStream, true);
            } else {
                this.mResource = (V) UrlResourceCache.this.obtainResource(this.mKey, this.mUrl, inputStream);
            }
        }

        @Override // co.vine.android.network.NetworkOperationReader
        public void onHandleError(HttpResult result) {
            UrlResourceCache.this.onResourceError(this.mKey, result);
        }
    }

    private class ResultRunnable implements Runnable {
        private final HashMap<K, V> mResult;

        public ResultRunnable(HashMap<K, V> result) {
            this.mResult = result;
        }

        @Override // java.lang.Runnable
        public void run() {
            UrlResourceCache.this.urlResourceLoaded(this.mResult);
        }
    }
}
