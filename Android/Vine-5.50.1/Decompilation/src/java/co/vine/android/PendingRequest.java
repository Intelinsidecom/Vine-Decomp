package co.vine.android;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class PendingRequest implements Parcelable {
    public static final Parcelable.Creator<PendingRequest> CREATOR = new Parcelable.Creator<PendingRequest>() { // from class: co.vine.android.PendingRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PendingRequest createFromParcel(Parcel in) {
            return new PendingRequest(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PendingRequest[] newArray(int i) {
            return new PendingRequest[i];
        }
    };
    public final int fetchType;
    public final String id;

    public PendingRequest(String id) {
        this.id = id;
        this.fetchType = 0;
    }

    public PendingRequest(String id, int fetchType) {
        this.id = id;
        this.fetchType = fetchType;
    }

    public PendingRequest(Parcel in) {
        this.id = in.readString();
        this.fetchType = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeInt(this.fetchType);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PendingRequest request = (PendingRequest) o;
        if (this.id != null) {
            if (this.id.equals(request.id)) {
                return true;
            }
        } else if (request.id == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.id != null) {
            return this.id.hashCode();
        }
        return 0;
    }
}
