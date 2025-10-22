package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class UserDetails$$Parcelable implements Parcelable, ParcelWrapper<UserDetails> {
    public static final UserDetails$$Parcelable$Creator$$23 CREATOR = new UserDetails$$Parcelable$Creator$$23();
    private UserDetails userDetails$$14;

    public UserDetails$$Parcelable(Parcel parcel$$257) {
        UserDetails userDetails$$16;
        if (parcel$$257.readInt() == -1) {
            userDetails$$16 = null;
        } else {
            userDetails$$16 = readco_vine_android_scribe_model_UserDetails(parcel$$257);
        }
        this.userDetails$$14 = userDetails$$16;
    }

    public UserDetails$$Parcelable(UserDetails userDetails$$18) {
        this.userDetails$$14 = userDetails$$18;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$258, int flags) {
        if (this.userDetails$$14 == null) {
            parcel$$258.writeInt(-1);
        } else {
            parcel$$258.writeInt(1);
            writeco_vine_android_scribe_model_UserDetails(this.userDetails$$14, parcel$$258, flags);
        }
    }

    private UserDetails readco_vine_android_scribe_model_UserDetails(Parcel parcel$$259) {
        Boolean boolean$$33;
        Long long$$67;
        UserDetails userDetails$$15 = new UserDetails();
        int int$$211 = parcel$$259.readInt();
        if (int$$211 < 0) {
            boolean$$33 = null;
        } else {
            boolean$$33 = Boolean.valueOf(parcel$$259.readInt() == 1);
        }
        userDetails$$15.following = boolean$$33;
        int int$$212 = parcel$$259.readInt();
        if (int$$212 < 0) {
            long$$67 = null;
        } else {
            long$$67 = Long.valueOf(parcel$$259.readLong());
        }
        userDetails$$15.userId = long$$67;
        return userDetails$$15;
    }

    private void writeco_vine_android_scribe_model_UserDetails(UserDetails userDetails$$17, Parcel parcel$$260, int flags$$94) {
        if (userDetails$$17.following == null) {
            parcel$$260.writeInt(-1);
        } else {
            parcel$$260.writeInt(1);
            parcel$$260.writeInt(userDetails$$17.following.booleanValue() ? 1 : 0);
        }
        if (userDetails$$17.userId == null) {
            parcel$$260.writeInt(-1);
        } else {
            parcel$$260.writeInt(1);
            parcel$$260.writeLong(userDetails$$17.userId.longValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public UserDetails getParcel() {
        return this.userDetails$$14;
    }
}
