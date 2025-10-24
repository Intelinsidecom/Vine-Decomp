package co.vine.android.network;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class UrlCachePolicy implements Parcelable {
    public final boolean mCacheTakesPriority;
    public final boolean mCachedResponseAllowed;
    public final long mIfReachableFetchMaxStaleTimeMs;
    public final boolean mNetworkDataAllowed;
    public final boolean mShouldCacheResponse;
    public final boolean mUseExpiredDataAllowedIfNetworkIsDown;
    public static final UrlCachePolicy CACHE_THEN_NETWORK = new UrlCachePolicy(true, true, true, true, -1, true);
    public static final UrlCachePolicy NETWORK_THEN_CACHE = new UrlCachePolicy(true, true, false, true, -1, true);
    public static final UrlCachePolicy NOT_CACHABLE = new UrlCachePolicy(false, false, false, false, -1, true);
    public static final UrlCachePolicy NETWORK_ONLY = new UrlCachePolicy(true, false, false, false, -1, true);
    public static final UrlCachePolicy FORCE_REFRESH = new UrlCachePolicy(true, true, false, false, -1, true);
    public static final UrlCachePolicy CACHE_ONLY = new UrlCachePolicy(true, true, true, true, -1, false);
    public static final Parcelable.Creator<UrlCachePolicy> CREATOR = new Parcelable.Creator<UrlCachePolicy>() { // from class: co.vine.android.network.UrlCachePolicy.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UrlCachePolicy createFromParcel(Parcel in) {
            return new UrlCachePolicy(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UrlCachePolicy[] newArray(int size) {
            return new UrlCachePolicy[size];
        }
    };

    private UrlCachePolicy(Parcel in) {
        this.mShouldCacheResponse = in.readInt() != 0;
        this.mCacheTakesPriority = in.readInt() != 0;
        this.mCachedResponseAllowed = in.readInt() != 0;
        this.mUseExpiredDataAllowedIfNetworkIsDown = in.readInt() != 0;
        this.mNetworkDataAllowed = in.readInt() != 0;
        this.mIfReachableFetchMaxStaleTimeMs = in.readLong();
    }

    private UrlCachePolicy(boolean shouldCacheResponse, boolean cachedResponseAllowed, boolean cacheTakesPriority, boolean useExpiredDataAllowedIfNetworkIsDown, long ifReachableFetchMaxStaleTimeMs, boolean networkDataAllowed) {
        if (cacheTakesPriority && !cachedResponseAllowed) {
            throw new IllegalArgumentException("Cache can't take priority if cache reponses are not allowed.");
        }
        this.mNetworkDataAllowed = networkDataAllowed;
        this.mShouldCacheResponse = shouldCacheResponse;
        this.mCachedResponseAllowed = cachedResponseAllowed;
        this.mUseExpiredDataAllowedIfNetworkIsDown = useExpiredDataAllowedIfNetworkIsDown;
        this.mIfReachableFetchMaxStaleTimeMs = ifReachableFetchMaxStaleTimeMs;
        this.mCacheTakesPriority = cacheTakesPriority;
    }

    public static UrlCachePolicy cacheAllowedPolicy(boolean expiredDataAllowedIfNetworkIsDown, long ifReachableFetchMaxStaleTimeMs) {
        return new UrlCachePolicy(true, true, true, expiredDataAllowedIfNetworkIsDown, ifReachableFetchMaxStaleTimeMs, true);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mShouldCacheResponse ? 1 : 0);
        dest.writeInt(this.mCacheTakesPriority ? 1 : 0);
        dest.writeInt(this.mCachedResponseAllowed ? 1 : 0);
        dest.writeInt(this.mUseExpiredDataAllowedIfNetworkIsDown ? 1 : 0);
        dest.writeInt(this.mNetworkDataAllowed ? 1 : 0);
        dest.writeLong(this.mIfReachableFetchMaxStaleTimeMs);
    }

    public boolean equals(Object o) {
        if (!(o instanceof UrlCachePolicy)) {
            return false;
        }
        UrlCachePolicy other = (UrlCachePolicy) o;
        return this.mShouldCacheResponse == other.mShouldCacheResponse && this.mCacheTakesPriority == other.mCacheTakesPriority && this.mIfReachableFetchMaxStaleTimeMs == other.mIfReachableFetchMaxStaleTimeMs && this.mNetworkDataAllowed == other.mNetworkDataAllowed && this.mCachedResponseAllowed == other.mCachedResponseAllowed && this.mUseExpiredDataAllowedIfNetworkIsDown == other.mUseExpiredDataAllowedIfNetworkIsDown;
    }

    public int hashCode() {
        int r = (this.mShouldCacheResponse ? 1 : 0) * 31;
        int r2 = r + ((this.mCacheTakesPriority ? 1 : 0) * 31) + r;
        int r3 = r2 + ((this.mNetworkDataAllowed ? 1 : 0) * 31) + r2;
        int r4 = r3 + ((this.mCachedResponseAllowed ? 1 : 0) * 31) + r3;
        int r5 = r4 + ((this.mUseExpiredDataAllowedIfNetworkIsDown ? 1 : 0) * 31) + r4;
        return r5 + (((int) ((this.mIfReachableFetchMaxStaleTimeMs >> 32) ^ this.mIfReachableFetchMaxStaleTimeMs)) * 31) + r5;
    }
}
