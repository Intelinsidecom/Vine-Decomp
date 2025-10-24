package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class LaunchDetails$$Parcelable implements Parcelable, ParcelWrapper<LaunchDetails> {
    public static final LaunchDetails$$Parcelable$Creator$$2 CREATOR = new LaunchDetails$$Parcelable$Creator$$2();
    private LaunchDetails launchDetails$$3;

    public LaunchDetails$$Parcelable(Parcel parcel$$64) {
        LaunchDetails launchDetails$$5;
        if (parcel$$64.readInt() == -1) {
            launchDetails$$5 = null;
        } else {
            launchDetails$$5 = readco_vine_android_scribe_model_LaunchDetails(parcel$$64);
        }
        this.launchDetails$$3 = launchDetails$$5;
    }

    public LaunchDetails$$Parcelable(LaunchDetails launchDetails$$7) {
        this.launchDetails$$3 = launchDetails$$7;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$65, int flags) {
        if (this.launchDetails$$3 == null) {
            parcel$$65.writeInt(-1);
        } else {
            parcel$$65.writeInt(1);
            writeco_vine_android_scribe_model_LaunchDetails(this.launchDetails$$3, parcel$$65, flags);
        }
    }

    private LaunchDetails readco_vine_android_scribe_model_LaunchDetails(Parcel parcel$$66) {
        LaunchDetails launchDetails$$4 = new LaunchDetails();
        launchDetails$$4.webSrc = parcel$$66.readString();
        return launchDetails$$4;
    }

    private void writeco_vine_android_scribe_model_LaunchDetails(LaunchDetails launchDetails$$6, Parcel parcel$$67, int flags$$29) {
        parcel$$67.writeString(launchDetails$$6.webSrc);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public LaunchDetails getParcel() {
        return this.launchDetails$$3;
    }
}
