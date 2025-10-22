package co.vine.android.cache;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ActionCache implements Parcelable {
    public static final Parcelable.Creator<ActionCache> CREATOR = new Parcelable.Creator<ActionCache>() { // from class: co.vine.android.cache.ActionCache.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActionCache createFromParcel(Parcel in) {
            return new ActionCache(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActionCache[] newArray(int size) {
            return new ActionCache[size];
        }
    };
    protected final HashMap<Long, Boolean> mCache;

    public ActionCache() {
        this.mCache = new HashMap<>();
    }

    public ActionCache(Parcel in) {
        this.mCache = (HashMap) in.readSerializable();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(this.mCache);
    }
}
