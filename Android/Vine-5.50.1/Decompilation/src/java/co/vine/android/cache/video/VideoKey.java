package co.vine.android.cache.video;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.cache.CacheKey;

/* loaded from: classes.dex */
public class VideoKey extends CacheKey implements Parcelable {
    public static final Parcelable.Creator<VideoKey> CREATOR = new Parcelable.Creator<VideoKey>() { // from class: co.vine.android.cache.video.VideoKey.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VideoKey createFromParcel(Parcel in) {
            return new VideoKey(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VideoKey[] newArray(int size) {
            return new VideoKey[size];
        }
    };
    public final String url;

    public VideoKey(String url) {
        this.url = url;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.url != null) {
            return this.url.equals(((VideoKey) o).url);
        }
        return ((VideoKey) o).url == null;
    }

    @Override // co.vine.android.cache.CacheKey
    public String toString() {
        return super.toString() + "\n" + this.url;
    }

    public int hashCode() {
        if (this.url != null) {
            return this.url.hashCode() * 31;
        }
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.url);
    }

    public VideoKey(Parcel in) {
        this.url = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
