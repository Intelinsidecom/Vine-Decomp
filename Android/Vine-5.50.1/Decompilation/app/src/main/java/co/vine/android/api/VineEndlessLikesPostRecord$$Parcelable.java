package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineEndlessLikesPostRecord$$Parcelable implements Parcelable, ParcelWrapper<VineEndlessLikesPostRecord> {
    public static final VineEndlessLikesPostRecord$$Parcelable$Creator$$49 CREATOR = new VineEndlessLikesPostRecord$$Parcelable$Creator$$49();
    private VineEndlessLikesPostRecord vineEndlessLikesPostRecord$$0;

    public VineEndlessLikesPostRecord$$Parcelable(Parcel parcel$$445) {
        VineEndlessLikesPostRecord vineEndlessLikesPostRecord$$2;
        if (parcel$$445.readInt() == -1) {
            vineEndlessLikesPostRecord$$2 = null;
        } else {
            vineEndlessLikesPostRecord$$2 = readco_vine_android_api_VineEndlessLikesPostRecord(parcel$$445);
        }
        this.vineEndlessLikesPostRecord$$0 = vineEndlessLikesPostRecord$$2;
    }

    public VineEndlessLikesPostRecord$$Parcelable(VineEndlessLikesPostRecord vineEndlessLikesPostRecord$$4) {
        this.vineEndlessLikesPostRecord$$0 = vineEndlessLikesPostRecord$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$446, int flags) {
        if (this.vineEndlessLikesPostRecord$$0 == null) {
            parcel$$446.writeInt(-1);
        } else {
            parcel$$446.writeInt(1);
            writeco_vine_android_api_VineEndlessLikesPostRecord(this.vineEndlessLikesPostRecord$$0, parcel$$446, flags);
        }
    }

    private VineEndlessLikesPostRecord readco_vine_android_api_VineEndlessLikesPostRecord(Parcel parcel$$447) {
        VineEndlessLikesPostRecord vineEndlessLikesPostRecord$$1 = new VineEndlessLikesPostRecord();
        vineEndlessLikesPostRecord$$1.time = parcel$$447.readFloat();
        return vineEndlessLikesPostRecord$$1;
    }

    private void writeco_vine_android_api_VineEndlessLikesPostRecord(VineEndlessLikesPostRecord vineEndlessLikesPostRecord$$3, Parcel parcel$$448, int flags$$149) {
        parcel$$448.writeFloat(vineEndlessLikesPostRecord$$3.time);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineEndlessLikesPostRecord getParcel() {
        return this.vineEndlessLikesPostRecord$$0;
    }
}
