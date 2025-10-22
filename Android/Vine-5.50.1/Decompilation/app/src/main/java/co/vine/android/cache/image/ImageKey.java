package co.vine.android.cache.image;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.cache.CacheKey;

/* loaded from: classes.dex */
public class ImageKey extends CacheKey implements Parcelable {
    public static final Parcelable.Creator<ImageKey> CREATOR = new Parcelable.Creator<ImageKey>() { // from class: co.vine.android.cache.image.ImageKey.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ImageKey createFromParcel(Parcel in) {
            return new ImageKey(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ImageKey[] newArray(int size) {
            return new ImageKey[size];
        }
    };
    public final int blurRadius;
    public final boolean blurred;
    public final boolean circularCropped;
    public boolean desaturated;
    public final int height;
    public final boolean requestResize;
    public final String url;
    public final int width;
    public boolean zoomInOnLetterbox;

    public ImageKey(String url, boolean blurred, int blurRadius, boolean desaturated) {
        this(url, 0, 0, false, blurred, blurRadius, desaturated, false);
    }

    public ImageKey(String url, boolean circularCropped) {
        this(url, 0, 0, circularCropped, false, 0, false, false);
    }

    public ImageKey(String url, int w, int h, boolean circularCropped) {
        this(url, w, h, circularCropped, false, 0, false, false);
    }

    public ImageKey(String url) {
        this(url, 0, 0, false, false, 0, false, false);
    }

    public ImageKey(String url, int width, int height, boolean circularCropped, boolean blurred, int blurRadius, boolean desaturated, boolean zoomInOnLetterbox) {
        this.url = url;
        this.width = width;
        this.height = height;
        this.circularCropped = circularCropped;
        this.requestResize = width > 0 && height > 0;
        this.blurred = blurred;
        this.blurRadius = blurRadius;
        this.desaturated = desaturated;
        this.zoomInOnLetterbox = zoomInOnLetterbox;
        if (url == null) {
            throw new IllegalStateException("Tried to create Image key with null url.");
        }
    }

    public static ImageKey newDownloadOnlyKey(String url) {
        ImageKey key = new ImageKey(url, 1, 1, true);
        key.setDownloadOnly(true);
        return key;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageKey imageKey = (ImageKey) o;
        if (this.height != imageKey.height || this.width != imageKey.width || this.blurred != imageKey.blurred || this.blurRadius != imageKey.blurRadius || this.circularCropped != imageKey.circularCropped || this.desaturated != imageKey.desaturated || this.zoomInOnLetterbox != imageKey.zoomInOnLetterbox) {
            return false;
        }
        if (this.url != null) {
            return this.url.equals(imageKey.url);
        }
        return imageKey.url == null;
    }

    public int hashCode() {
        int result = this.url != null ? this.url.hashCode() : 0;
        return (((((((((((((result * 31) + this.width) * 31) + this.height) * 31) + (this.circularCropped ? 1 : 0)) * 31) + (this.blurred ? 1 : 0)) * 31) + this.blurRadius) * 31) + (this.desaturated ? 1 : 0)) * 31) + (this.zoomInOnLetterbox ? 1 : 0);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.width);
        out.writeInt(this.height);
        out.writeInt(this.blurRadius);
        out.writeInt(this.blurred ? 1 : 0);
        out.writeInt(this.circularCropped ? 1 : 0);
        out.writeInt(this.requestResize ? 1 : 0);
        out.writeInt(this.desaturated ? 1 : 0);
        out.writeInt(this.zoomInOnLetterbox ? 1 : 0);
        out.writeString(this.url);
    }

    public ImageKey(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.blurRadius = in.readInt();
        this.blurred = in.readInt() > 0;
        this.circularCropped = in.readInt() > 0;
        this.requestResize = in.readInt() > 0;
        this.desaturated = in.readInt() > 0;
        this.zoomInOnLetterbox = in.readInt() > 0;
        this.url = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
