package co.vine.android;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class PendingPhoneVerificationRequest implements Parcelable {
    public static final Parcelable.Creator<PendingPhoneVerificationRequest> CREATOR = new Parcelable.Creator<PendingPhoneVerificationRequest>() { // from class: co.vine.android.PendingPhoneVerificationRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PendingPhoneVerificationRequest createFromParcel(Parcel in) {
            return new PendingPhoneVerificationRequest(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PendingPhoneVerificationRequest[] newArray(int i) {
            return new PendingPhoneVerificationRequest[i];
        }
    };
    public final int actionCode;
    public final Bundle bundle;
    public final String reqId;
    public int state;

    public PendingPhoneVerificationRequest(String reqId, int actionCode, Bundle b) {
        this.reqId = reqId;
        this.actionCode = actionCode;
        this.bundle = b;
        this.state = 1;
    }

    public PendingPhoneVerificationRequest(Parcel in) {
        this.reqId = in.readString();
        this.actionCode = in.readInt();
        this.bundle = in.readBundle();
        this.state = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.reqId);
        out.writeInt(this.actionCode);
        out.writeBundle(this.bundle);
        out.writeInt(this.state);
    }
}
