package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class TwitterUser$$Parcelable implements Parcelable, ParcelWrapper<TwitterUser> {
    public static final TwitterUser$$Parcelable$Creator$$34 CREATOR = new TwitterUser$$Parcelable$Creator$$34();
    private TwitterUser twitterUser$$0;

    public TwitterUser$$Parcelable(Parcel parcel$$358) {
        TwitterUser twitterUser$$2;
        if (parcel$$358.readInt() == -1) {
            twitterUser$$2 = null;
        } else {
            twitterUser$$2 = readco_vine_android_api_TwitterUser(parcel$$358);
        }
        this.twitterUser$$0 = twitterUser$$2;
    }

    public TwitterUser$$Parcelable(TwitterUser twitterUser$$4) {
        this.twitterUser$$0 = twitterUser$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$359, int flags) {
        if (this.twitterUser$$0 == null) {
            parcel$$359.writeInt(-1);
        } else {
            parcel$$359.writeInt(1);
            writeco_vine_android_api_TwitterUser(this.twitterUser$$0, parcel$$359, flags);
        }
    }

    private TwitterUser readco_vine_android_api_TwitterUser(Parcel parcel$$360) {
        TwitterUser twitterUser$$1 = new TwitterUser();
        twitterUser$$1.profileUrl = parcel$$360.readString();
        twitterUser$$1.name = parcel$$360.readString();
        twitterUser$$1.description = parcel$$360.readString();
        twitterUser$$1.location = parcel$$360.readString();
        twitterUser$$1.screenName = parcel$$360.readString();
        twitterUser$$1.userId = parcel$$360.readLong();
        twitterUser$$1.url = parcel$$360.readString();
        twitterUser$$1.defaultProfileImage = parcel$$360.readInt() == 1;
        return twitterUser$$1;
    }

    private void writeco_vine_android_api_TwitterUser(TwitterUser twitterUser$$3, Parcel parcel$$361, int flags$$128) {
        parcel$$361.writeString(twitterUser$$3.profileUrl);
        parcel$$361.writeString(twitterUser$$3.name);
        parcel$$361.writeString(twitterUser$$3.description);
        parcel$$361.writeString(twitterUser$$3.location);
        parcel$$361.writeString(twitterUser$$3.screenName);
        parcel$$361.writeLong(twitterUser$$3.userId);
        parcel$$361.writeString(twitterUser$$3.url);
        parcel$$361.writeInt(twitterUser$$3.defaultProfileImage ? 1 : 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public TwitterUser getParcel() {
        return this.twitterUser$$0;
    }
}
