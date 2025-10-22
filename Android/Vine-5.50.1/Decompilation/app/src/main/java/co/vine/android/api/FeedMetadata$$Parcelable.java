package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.FeedMetadata;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class FeedMetadata$$Parcelable implements Parcelable, ParcelWrapper<FeedMetadata> {
    public static final FeedMetadata$$Parcelable$Creator$$51 CREATOR = new FeedMetadata$$Parcelable$Creator$$51();
    private FeedMetadata feedMetadata$$0;

    public FeedMetadata$$Parcelable(Parcel parcel$$455) {
        FeedMetadata feedMetadata$$2;
        if (parcel$$455.readInt() == -1) {
            feedMetadata$$2 = null;
        } else {
            feedMetadata$$2 = readco_vine_android_api_FeedMetadata(parcel$$455);
        }
        this.feedMetadata$$0 = feedMetadata$$2;
    }

    public FeedMetadata$$Parcelable(FeedMetadata feedMetadata$$4) {
        this.feedMetadata$$0 = feedMetadata$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$456, int flags) {
        if (this.feedMetadata$$0 == null) {
            parcel$$456.writeInt(-1);
        } else {
            parcel$$456.writeInt(1);
            writeco_vine_android_api_FeedMetadata(this.feedMetadata$$0, parcel$$456, flags);
        }
    }

    private FeedMetadata readco_vine_android_api_FeedMetadata(Parcel parcel$$457) {
        FeedMetadata feedMetadata$$1 = new FeedMetadata();
        feedMetadata$$1.feedType = (FeedMetadata.FeedType) parcel$$457.readSerializable();
        feedMetadata$$1.channel = (VineChannel) parcel$$457.readParcelable(FeedMetadata$$Parcelable.class.getClassLoader());
        feedMetadata$$1.userProfile = (VineUser) parcel$$457.readParcelable(FeedMetadata$$Parcelable.class.getClassLoader());
        feedMetadata$$1.profileUserId = parcel$$457.readLong();
        return feedMetadata$$1;
    }

    private void writeco_vine_android_api_FeedMetadata(FeedMetadata feedMetadata$$3, Parcel parcel$$458, int flags$$151) {
        parcel$$458.writeSerializable(feedMetadata$$3.feedType);
        parcel$$458.writeParcelable(feedMetadata$$3.channel, flags$$151);
        parcel$$458.writeParcelable(feedMetadata$$3.userProfile, flags$$151);
        parcel$$458.writeLong(feedMetadata$$3.profileUserId);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public FeedMetadata getParcel() {
        return this.feedMetadata$$0;
    }
}
