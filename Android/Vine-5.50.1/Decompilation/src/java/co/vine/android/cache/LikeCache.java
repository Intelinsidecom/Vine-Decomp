package co.vine.android.cache;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class LikeCache extends ActionCache implements Parcelable {
    public static final Parcelable.Creator<LikeCache> CREATOR = new Parcelable.Creator<LikeCache>() { // from class: co.vine.android.cache.LikeCache.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LikeCache createFromParcel(Parcel in) {
            return new LikeCache(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LikeCache[] newArray(int size) {
            return new LikeCache[size];
        }
    };

    public LikeCache() {
    }

    public LikeCache(Parcel in) {
        super(in);
    }

    @Override // co.vine.android.cache.ActionCache, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // co.vine.android.cache.ActionCache, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
}
