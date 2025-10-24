package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class ThumbnailData implements Parcelable {
    public static final Parcelable.Creator<ThumbnailData> CREATOR = new Parcelable.Creator<ThumbnailData>() { // from class: co.vine.android.api.ThumbnailData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ThumbnailData createFromParcel(Parcel in) {
            return new ThumbnailData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ThumbnailData[] newArray(int size) {
            return new ThumbnailData[size];
        }
    };
    public final int height;
    public final String url;
    public final int width;

    public ThumbnailData(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public ThumbnailData(Parcel in) {
        this.url = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.url);
        out.writeInt(this.width);
        out.writeInt(this.height);
    }
}
