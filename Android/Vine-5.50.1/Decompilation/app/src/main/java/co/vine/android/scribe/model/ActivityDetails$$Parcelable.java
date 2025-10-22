package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class ActivityDetails$$Parcelable implements Parcelable, ParcelWrapper<ActivityDetails> {
    public static final ActivityDetails$$Parcelable$Creator$$17 CREATOR = new ActivityDetails$$Parcelable$Creator$$17();
    private ActivityDetails activityDetails$$9;

    public ActivityDetails$$Parcelable(Parcel parcel$$221) {
        ActivityDetails activityDetails$$11;
        if (parcel$$221.readInt() == -1) {
            activityDetails$$11 = null;
        } else {
            activityDetails$$11 = readco_vine_android_scribe_model_ActivityDetails(parcel$$221);
        }
        this.activityDetails$$9 = activityDetails$$11;
    }

    public ActivityDetails$$Parcelable(ActivityDetails activityDetails$$13) {
        this.activityDetails$$9 = activityDetails$$13;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$222, int flags) {
        if (this.activityDetails$$9 == null) {
            parcel$$222.writeInt(-1);
        } else {
            parcel$$222.writeInt(1);
            writeco_vine_android_scribe_model_ActivityDetails(this.activityDetails$$9, parcel$$222, flags);
        }
    }

    private ActivityDetails readco_vine_android_scribe_model_ActivityDetails(Parcel parcel$$223) {
        Long long$$54;
        Integer integer$$17;
        ActivityDetails activityDetails$$10 = new ActivityDetails();
        int int$$175 = parcel$$223.readInt();
        if (int$$175 < 0) {
            long$$54 = null;
        } else {
            long$$54 = Long.valueOf(parcel$$223.readLong());
        }
        activityDetails$$10.activityId = long$$54;
        int int$$176 = parcel$$223.readInt();
        if (int$$176 < 0) {
            integer$$17 = null;
        } else {
            integer$$17 = Integer.valueOf(parcel$$223.readInt());
        }
        activityDetails$$10.nMore = integer$$17;
        activityDetails$$10.activityType = parcel$$223.readString();
        return activityDetails$$10;
    }

    private void writeco_vine_android_scribe_model_ActivityDetails(ActivityDetails activityDetails$$12, Parcel parcel$$224, int flags$$85) {
        if (activityDetails$$12.activityId == null) {
            parcel$$224.writeInt(-1);
        } else {
            parcel$$224.writeInt(1);
            parcel$$224.writeLong(activityDetails$$12.activityId.longValue());
        }
        if (activityDetails$$12.nMore == null) {
            parcel$$224.writeInt(-1);
        } else {
            parcel$$224.writeInt(1);
            parcel$$224.writeInt(activityDetails$$12.nMore.intValue());
        }
        parcel$$224.writeString(activityDetails$$12.activityType);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public ActivityDetails getParcel() {
        return this.activityDetails$$9;
    }
}
