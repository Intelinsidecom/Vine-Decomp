package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VinePostResponse$$Parcelable implements Parcelable, ParcelWrapper<VinePostResponse> {
    public static final VinePostResponse$$Parcelable$Creator$$28 CREATOR = new VinePostResponse$$Parcelable$Creator$$28();
    private VinePostResponse vinePostResponse$$0;

    public VinePostResponse$$Parcelable(Parcel parcel$$324) {
        VinePostResponse vinePostResponse$$2;
        if (parcel$$324.readInt() == -1) {
            vinePostResponse$$2 = null;
        } else {
            vinePostResponse$$2 = readco_vine_android_api_response_VinePostResponse(parcel$$324);
        }
        this.vinePostResponse$$0 = vinePostResponse$$2;
    }

    public VinePostResponse$$Parcelable(VinePostResponse vinePostResponse$$4) {
        this.vinePostResponse$$0 = vinePostResponse$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$325, int flags) {
        if (this.vinePostResponse$$0 == null) {
            parcel$$325.writeInt(-1);
        } else {
            parcel$$325.writeInt(1);
            writeco_vine_android_api_response_VinePostResponse(this.vinePostResponse$$0, parcel$$325, flags);
        }
    }

    private VinePostResponse readco_vine_android_api_response_VinePostResponse(Parcel parcel$$326) {
        VinePostResponse vinePostResponse$$1 = new VinePostResponse();
        vinePostResponse$$1.postId = parcel$$326.readLong();
        return vinePostResponse$$1;
    }

    private void writeco_vine_android_api_response_VinePostResponse(VinePostResponse vinePostResponse$$3, Parcel parcel$$327, int flags$$120) {
        parcel$$327.writeLong(vinePostResponse$$3.postId);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VinePostResponse getParcel() {
        return this.vinePostResponse$$0;
    }
}
