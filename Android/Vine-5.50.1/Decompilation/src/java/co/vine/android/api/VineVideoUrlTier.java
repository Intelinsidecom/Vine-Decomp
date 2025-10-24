package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class VineVideoUrlTier implements Parcelable {
    public static final Parcelable.Creator<VineVideoUrlTier> CREATOR = new Parcelable.Creator<VineVideoUrlTier>() { // from class: co.vine.android.api.VineVideoUrlTier.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineVideoUrlTier createFromParcel(Parcel in) {
            return new VineVideoUrlTier(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineVideoUrlTier[] newArray(int size) {
            return new VineVideoUrlTier[size];
        }
    };
    public final String format;
    public final float rate;
    public final String url;

    public VineVideoUrlTier(String url, float rate, String format) {
        this.url = url;
        this.rate = rate;
        this.format = format;
    }

    public VineVideoUrlTier(Parcel in) {
        this.url = in.readString();
        this.rate = in.readFloat();
        this.format = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.url);
        out.writeFloat(this.rate);
        out.writeString(this.format);
    }
}
