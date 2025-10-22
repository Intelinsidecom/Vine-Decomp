package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineLoopSubmissionResponse$$Parcelable implements Parcelable, ParcelWrapper<VineLoopSubmissionResponse> {
    public static final VineLoopSubmissionResponse$$Parcelable$Creator$$29 CREATOR = new VineLoopSubmissionResponse$$Parcelable$Creator$$29();
    private VineLoopSubmissionResponse vineLoopSubmissionResponse$$0;

    public VineLoopSubmissionResponse$$Parcelable(Parcel parcel$$329) {
        VineLoopSubmissionResponse vineLoopSubmissionResponse$$2;
        if (parcel$$329.readInt() == -1) {
            vineLoopSubmissionResponse$$2 = null;
        } else {
            vineLoopSubmissionResponse$$2 = readco_vine_android_api_response_VineLoopSubmissionResponse(parcel$$329);
        }
        this.vineLoopSubmissionResponse$$0 = vineLoopSubmissionResponse$$2;
    }

    public VineLoopSubmissionResponse$$Parcelable(VineLoopSubmissionResponse vineLoopSubmissionResponse$$4) {
        this.vineLoopSubmissionResponse$$0 = vineLoopSubmissionResponse$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$330, int flags) {
        if (this.vineLoopSubmissionResponse$$0 == null) {
            parcel$$330.writeInt(-1);
        } else {
            parcel$$330.writeInt(1);
            writeco_vine_android_api_response_VineLoopSubmissionResponse(this.vineLoopSubmissionResponse$$0, parcel$$330, flags);
        }
    }

    private VineLoopSubmissionResponse readco_vine_android_api_response_VineLoopSubmissionResponse(Parcel parcel$$331) {
        VineLoopSubmissionResponse vineLoopSubmissionResponse$$1 = new VineLoopSubmissionResponse();
        vineLoopSubmissionResponse$$1.mSubmissionInterval = parcel$$331.readInt();
        return vineLoopSubmissionResponse$$1;
    }

    private void writeco_vine_android_api_response_VineLoopSubmissionResponse(VineLoopSubmissionResponse vineLoopSubmissionResponse$$3, Parcel parcel$$332, int flags$$121) {
        parcel$$332.writeInt(vineLoopSubmissionResponse$$3.mSubmissionInterval);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineLoopSubmissionResponse getParcel() {
        return this.vineLoopSubmissionResponse$$0;
    }
}
