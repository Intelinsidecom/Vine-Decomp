package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineEditions$$Parcelable implements Parcelable, ParcelWrapper<VineEditions> {
    public static final VineEditions$$Parcelable$Creator$$30 CREATOR = new VineEditions$$Parcelable$Creator$$30();
    private VineEditions vineEditions$$0;

    public VineEditions$$Parcelable(Parcel parcel$$334) {
        VineEditions vineEditions$$2;
        if (parcel$$334.readInt() == -1) {
            vineEditions$$2 = null;
        } else {
            vineEditions$$2 = readco_vine_android_api_response_VineEditions(parcel$$334);
        }
        this.vineEditions$$0 = vineEditions$$2;
    }

    public VineEditions$$Parcelable(VineEditions vineEditions$$4) {
        this.vineEditions$$0 = vineEditions$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$335, int flags) {
        if (this.vineEditions$$0 == null) {
            parcel$$335.writeInt(-1);
        } else {
            parcel$$335.writeInt(1);
            writeco_vine_android_api_response_VineEditions(this.vineEditions$$0, parcel$$335, flags);
        }
    }

    private VineEditions readco_vine_android_api_response_VineEditions(Parcel parcel$$336) {
        VineEditions vineEditions$$1 = new VineEditions();
        vineEditions$$1.editions = (ArrayList) parcel$$336.readSerializable();
        return vineEditions$$1;
    }

    private void writeco_vine_android_api_response_VineEditions(VineEditions vineEditions$$3, Parcel parcel$$337, int flags$$122) {
        parcel$$337.writeSerializable(vineEditions$$3.editions);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineEditions getParcel() {
        return this.vineEditions$$0;
    }
}
