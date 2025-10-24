package co.vine.android.cache;

import com.edisonwang.android.slog.MessageFormatter;

/* loaded from: classes.dex */
public class CacheKey {
    private int mCacheLoadTimeMs;
    private CacheState mCacheState = CacheState.UNKNOWN;
    private boolean mDownloadOnly;
    private boolean mLastLoadFromMemory;
    private long mLoadStartTime;

    public enum CacheState {
        UNKNOWN,
        NOT_CACHED,
        DISK,
        NETWORK,
        EXPIRED
    }

    public CacheState getCacheState() {
        return this.mCacheState;
    }

    public void setCacheState(CacheState cacheState) {
        this.mCacheState = cacheState;
    }

    public void setLoadTime(int timeMs) {
        this.mCacheLoadTimeMs = timeMs;
    }

    public long getLoadStartTime() {
        return this.mLoadStartTime;
    }

    public void setLoadStartTime(long startTime) {
        this.mLoadStartTime = startTime;
    }

    public boolean wasLastLoadFromMemory() {
        return this.mLastLoadFromMemory;
    }

    public void setLoadedFromMemory() {
        this.mLastLoadFromMemory = true;
    }

    public boolean isDownloadOnly() {
        return this.mDownloadOnly;
    }

    public void setDownloadOnly(boolean downloadOnly) {
        this.mDownloadOnly = downloadOnly;
    }

    public String toString() {
        return MessageFormatter.toStringMessage("Cache State: {}, Load took {}ms, loaded {}s ago, memory: {}", this.mCacheState.name(), Integer.valueOf(this.mCacheLoadTimeMs), Long.valueOf((System.currentTimeMillis() - this.mLoadStartTime) / 1000), Boolean.valueOf(this.mLastLoadFromMemory));
    }
}
