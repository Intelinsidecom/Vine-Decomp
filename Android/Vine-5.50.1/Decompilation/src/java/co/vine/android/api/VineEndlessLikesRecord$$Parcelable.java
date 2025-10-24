package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineEndlessLikesRecord$$Parcelable implements Parcelable, ParcelWrapper<VineEndlessLikesRecord> {
    public static final VineEndlessLikesRecord$$Parcelable$Creator$$48 CREATOR = new VineEndlessLikesRecord$$Parcelable$Creator$$48();
    private VineEndlessLikesRecord vineEndlessLikesRecord$$0;

    public VineEndlessLikesRecord$$Parcelable(Parcel parcel$$440) {
        VineEndlessLikesRecord vineEndlessLikesRecord$$2;
        if (parcel$$440.readInt() == -1) {
            vineEndlessLikesRecord$$2 = null;
        } else {
            vineEndlessLikesRecord$$2 = readco_vine_android_api_VineEndlessLikesRecord(parcel$$440);
        }
        this.vineEndlessLikesRecord$$0 = vineEndlessLikesRecord$$2;
    }

    public VineEndlessLikesRecord$$Parcelable(VineEndlessLikesRecord vineEndlessLikesRecord$$4) {
        this.vineEndlessLikesRecord$$0 = vineEndlessLikesRecord$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$441, int flags) {
        if (this.vineEndlessLikesRecord$$0 == null) {
            parcel$$441.writeInt(-1);
        } else {
            parcel$$441.writeInt(1);
            writeco_vine_android_api_VineEndlessLikesRecord(this.vineEndlessLikesRecord$$0, parcel$$441, flags);
        }
    }

    private VineEndlessLikesRecord readco_vine_android_api_VineEndlessLikesRecord(Parcel parcel$$442) {
        VineEndlessLikesRecord vineEndlessLikesRecord$$1 = new VineEndlessLikesRecord();
        vineEndlessLikesRecord$$1.level = parcel$$442.readFloat();
        vineEndlessLikesRecord$$1.count = parcel$$442.readInt();
        vineEndlessLikesRecord$$1.startTime = parcel$$442.readFloat();
        vineEndlessLikesRecord$$1.endTime = parcel$$442.readFloat();
        return vineEndlessLikesRecord$$1;
    }

    private void writeco_vine_android_api_VineEndlessLikesRecord(VineEndlessLikesRecord vineEndlessLikesRecord$$3, Parcel parcel$$443, int flags$$148) {
        parcel$$443.writeFloat(vineEndlessLikesRecord$$3.level);
        parcel$$443.writeInt(vineEndlessLikesRecord$$3.count);
        parcel$$443.writeFloat(vineEndlessLikesRecord$$3.startTime);
        parcel$$443.writeFloat(vineEndlessLikesRecord$$3.endTime);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineEndlessLikesRecord getParcel() {
        return this.vineEndlessLikesRecord$$0;
    }
}
