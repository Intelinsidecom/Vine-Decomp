package co.vine.android.model.impl;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class TimelineDetails implements Parcelable {
    public static final Parcelable.Creator<TimelineDetails> CREATOR = new Parcelable.Creator<TimelineDetails>() { // from class: co.vine.android.model.impl.TimelineDetails.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TimelineDetails createFromParcel(Parcel in) {
            return new TimelineDetails(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TimelineDetails[] newArray(int size) {
            return new TimelineDetails[size];
        }
    };
    public long channelId;
    public String sort;
    public int type;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeLong(this.channelId);
        dest.writeString(this.sort);
    }

    private TimelineDetails(Parcel in) {
        this.type = in.readInt();
        this.channelId = in.readLong();
        this.sort = in.readString();
    }

    public TimelineDetails(int type, Long channelId, String sort) {
        this.type = type;
        this.channelId = channelId.longValue();
        this.sort = sort;
    }

    public String getUniqueMarker() {
        return String.valueOf(hashCode());
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TimelineDetails)) {
            return false;
        }
        TimelineDetails data = (TimelineDetails) o;
        return this.type == data.type && this.channelId == data.channelId && this.sort.equals(data.sort);
    }

    public int hashCode() {
        int result = this.type + 1081;
        return (((result * 47) + ((int) (this.channelId ^ (this.channelId >> 32)))) * 47) + this.sort.hashCode();
    }
}
