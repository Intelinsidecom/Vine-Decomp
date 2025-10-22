package co.vine.android.service;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class VineServiceCallback implements Parcelable {
    public static final Parcelable.Creator<VineServiceCallback> CREATOR = new Parcelable.Creator<VineServiceCallback>() { // from class: co.vine.android.service.VineServiceCallback.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineServiceCallback createFromParcel(Parcel source) {
            return new VineServiceCallback(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineServiceCallback[] newArray(int size) {
            return new VineServiceCallback[size];
        }
    };
    private final VineServiceResponder mResponder;

    public VineServiceCallback(VineServiceResponder responder) {
        this.mResponder = responder;
    }

    public VineServiceCallback(Parcel parcel) {
        this.mResponder = (VineServiceResponder) parcel.readStrongBinder();
    }

    public VineServiceResponder getResponder() {
        return this.mResponder;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(this.mResponder);
    }
}
