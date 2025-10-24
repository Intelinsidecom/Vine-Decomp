package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineShortPost$$Parcelable implements Parcelable, ParcelWrapper<VineShortPost> {
    public static final VineShortPost$$Parcelable$Creator$$45 CREATOR = new VineShortPost$$Parcelable$Creator$$45();
    private VineShortPost vineShortPost$$4;

    public VineShortPost$$Parcelable(Parcel parcel$$423) {
        VineShortPost vineShortPost$$6;
        if (parcel$$423.readInt() == -1) {
            vineShortPost$$6 = null;
        } else {
            vineShortPost$$6 = readco_vine_android_api_response_VineShortPost(parcel$$423);
        }
        this.vineShortPost$$4 = vineShortPost$$6;
    }

    public VineShortPost$$Parcelable(VineShortPost vineShortPost$$8) {
        this.vineShortPost$$4 = vineShortPost$$8;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$424, int flags) {
        if (this.vineShortPost$$4 == null) {
            parcel$$424.writeInt(-1);
        } else {
            parcel$$424.writeInt(1);
            writeco_vine_android_api_response_VineShortPost(this.vineShortPost$$4, parcel$$424, flags);
        }
    }

    private VineShortPost readco_vine_android_api_response_VineShortPost(Parcel parcel$$425) {
        Long long$$87;
        VineShortPost vineShortPost$$5 = new VineShortPost();
        int int$$349 = parcel$$425.readInt();
        if (int$$349 < 0) {
            long$$87 = null;
        } else {
            long$$87 = Long.valueOf(parcel$$425.readLong());
        }
        vineShortPost$$5.postId = long$$87;
        vineShortPost$$5.type = parcel$$425.readString();
        vineShortPost$$5.thumbnailUrl = parcel$$425.readString();
        return vineShortPost$$5;
    }

    private void writeco_vine_android_api_response_VineShortPost(VineShortPost vineShortPost$$7, Parcel parcel$$426, int flags$$144) {
        if (vineShortPost$$7.postId == null) {
            parcel$$426.writeInt(-1);
        } else {
            parcel$$426.writeInt(1);
            parcel$$426.writeLong(vineShortPost$$7.postId.longValue());
        }
        parcel$$426.writeString(vineShortPost$$7.type);
        parcel$$426.writeString(vineShortPost$$7.thumbnailUrl);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineShortPost getParcel() {
        return this.vineShortPost$$4;
    }
}
