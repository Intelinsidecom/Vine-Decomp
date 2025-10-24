package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineFeed$$Parcelable implements Parcelable, ParcelWrapper<VineFeed> {
    public static final VineFeed$$Parcelable$Creator$$52 CREATOR = new VineFeed$$Parcelable$Creator$$52();
    private VineFeed vineFeed$$0;

    public VineFeed$$Parcelable(Parcel parcel$$460) {
        VineFeed vineFeed$$2;
        if (parcel$$460.readInt() == -1) {
            vineFeed$$2 = null;
        } else {
            vineFeed$$2 = readco_vine_android_api_VineFeed(parcel$$460);
        }
        this.vineFeed$$0 = vineFeed$$2;
    }

    public VineFeed$$Parcelable(VineFeed vineFeed$$4) {
        this.vineFeed$$0 = vineFeed$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$461, int flags) {
        if (this.vineFeed$$0 == null) {
            parcel$$461.writeInt(-1);
        } else {
            parcel$$461.writeInt(1);
            writeco_vine_android_api_VineFeed(this.vineFeed$$0, parcel$$461, flags);
        }
    }

    private VineFeed readco_vine_android_api_VineFeed(Parcel parcel$$462) {
        VineFeed vineFeed$$1 = new VineFeed();
        vineFeed$$1.backgroundColor = parcel$$462.readString();
        vineFeed$$1.feedMetadata = (FeedMetadata) parcel$$462.readParcelable(VineFeed$$Parcelable.class.getClassLoader());
        vineFeed$$1.feedId = parcel$$462.readLong();
        vineFeed$$1.link = parcel$$462.readString();
        vineFeed$$1.description = parcel$$462.readString();
        vineFeed$$1.coverPost = (VinePost) parcel$$462.readParcelable(VineFeed$$Parcelable.class.getClassLoader());
        vineFeed$$1.title = parcel$$462.readString();
        vineFeed$$1.type = parcel$$462.readString();
        vineFeed$$1.user = (VineUser) parcel$$462.readParcelable(VineFeed$$Parcelable.class.getClassLoader());
        vineFeed$$1.userId = parcel$$462.readLong();
        vineFeed$$1.secondaryColor = parcel$$462.readString();
        return vineFeed$$1;
    }

    private void writeco_vine_android_api_VineFeed(VineFeed vineFeed$$3, Parcel parcel$$463, int flags$$152) {
        parcel$$463.writeString(vineFeed$$3.backgroundColor);
        parcel$$463.writeParcelable(vineFeed$$3.feedMetadata, flags$$152);
        parcel$$463.writeLong(vineFeed$$3.feedId);
        parcel$$463.writeString(vineFeed$$3.link);
        parcel$$463.writeString(vineFeed$$3.description);
        parcel$$463.writeParcelable(vineFeed$$3.coverPost, flags$$152);
        parcel$$463.writeString(vineFeed$$3.title);
        parcel$$463.writeString(vineFeed$$3.type);
        parcel$$463.writeParcelable(vineFeed$$3.user, flags$$152);
        parcel$$463.writeLong(vineFeed$$3.userId);
        parcel$$463.writeString(vineFeed$$3.secondaryColor);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineFeed getParcel() {
        return this.vineFeed$$0;
    }
}
