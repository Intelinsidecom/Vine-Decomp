package co.vine.android.cache;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;

/* loaded from: classes.dex */
public class RevineCache extends ActionCache implements Parcelable {
    public static final Parcelable.Creator<RevineCache> CREATOR = new Parcelable.Creator<RevineCache>() { // from class: co.vine.android.cache.RevineCache.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RevineCache createFromParcel(Parcel in) {
            return new RevineCache(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RevineCache[] newArray(int size) {
            return new RevineCache[size];
        }
    };
    HashMap<Long, Long> mRepostIdMap;

    public RevineCache() {
        this.mRepostIdMap = new HashMap<>();
    }

    public RevineCache(Parcel in) {
        super(in);
        this.mRepostIdMap = (HashMap) in.readSerializable();
    }

    @Override // co.vine.android.cache.ActionCache, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // co.vine.android.cache.ActionCache, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeSerializable(this.mRepostIdMap);
    }
}
