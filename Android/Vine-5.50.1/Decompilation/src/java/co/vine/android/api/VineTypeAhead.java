package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class VineTypeAhead implements Parcelable {
    public static final Parcelable.Creator<VineTypeAhead> CREATOR = new Parcelable.Creator<VineTypeAhead>() { // from class: co.vine.android.api.VineTypeAhead.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineTypeAhead createFromParcel(Parcel in) {
            return new VineTypeAhead(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineTypeAhead[] newArray(int size) {
            return new VineTypeAhead[size];
        }
    };
    public long id;
    public final String token;
    public final String type;

    public VineTypeAhead(String type, String token, long id) {
        this.id = id;
        this.token = token;
        this.type = type;
    }

    public static String getPlainTag(String tagWithHash) {
        return tagWithHash.substring(1, tagWithHash.length());
    }

    public VineTypeAhead(Parcel in) {
        this.id = in.readLong();
        this.token = in.readString();
        this.type = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.id);
        out.writeString(this.token);
        out.writeString(this.type);
    }
}
