package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.response.VineShortPost;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineBylineAction$$Parcelable implements Parcelable, ParcelWrapper<VineBylineAction> {
    public static final VineBylineAction$$Parcelable$Creator$$44 CREATOR = new VineBylineAction$$Parcelable$Creator$$44();
    private VineBylineAction vineBylineAction$$0;

    public VineBylineAction$$Parcelable(Parcel parcel$$416) {
        VineBylineAction vineBylineAction$$2;
        if (parcel$$416.readInt() == -1) {
            vineBylineAction$$2 = null;
        } else {
            vineBylineAction$$2 = readco_vine_android_api_VineBylineAction(parcel$$416);
        }
        this.vineBylineAction$$0 = vineBylineAction$$2;
    }

    public VineBylineAction$$Parcelable(VineBylineAction vineBylineAction$$4) {
        this.vineBylineAction$$0 = vineBylineAction$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$417, int flags) {
        if (this.vineBylineAction$$0 == null) {
            parcel$$417.writeInt(-1);
        } else {
            parcel$$417.writeInt(1);
            writeco_vine_android_api_VineBylineAction(this.vineBylineAction$$0, parcel$$417, flags);
        }
    }

    private VineBylineAction readco_vine_android_api_VineBylineAction(Parcel parcel$$418) {
        ArrayList<VineShortPost> list$$40;
        VineShortPost vineShortPost$$1;
        VineBylineAction vineBylineAction$$1 = new VineBylineAction();
        vineBylineAction$$1.detailedDescription = parcel$$418.readString();
        vineBylineAction$$1.actionTitle = parcel$$418.readString();
        int int$$346 = parcel$$418.readInt();
        if (int$$346 < 0) {
            list$$40 = null;
        } else {
            list$$40 = new ArrayList<>();
            for (int int$$347 = 0; int$$347 < int$$346; int$$347++) {
                if (parcel$$418.readInt() == -1) {
                    vineShortPost$$1 = null;
                } else {
                    vineShortPost$$1 = readco_vine_android_api_response_VineShortPost(parcel$$418);
                }
                list$$40.add(vineShortPost$$1);
            }
        }
        vineBylineAction$$1.records = list$$40;
        vineBylineAction$$1.description = parcel$$418.readString();
        vineBylineAction$$1.actionIconUrl = parcel$$418.readString();
        return vineBylineAction$$1;
    }

    private VineShortPost readco_vine_android_api_response_VineShortPost(Parcel parcel$$419) {
        Long long$$86;
        VineShortPost vineShortPost$$0 = new VineShortPost();
        int int$$348 = parcel$$419.readInt();
        if (int$$348 < 0) {
            long$$86 = null;
        } else {
            long$$86 = Long.valueOf(parcel$$419.readLong());
        }
        vineShortPost$$0.postId = long$$86;
        vineShortPost$$0.type = parcel$$419.readString();
        vineShortPost$$0.thumbnailUrl = parcel$$419.readString();
        return vineShortPost$$0;
    }

    private void writeco_vine_android_api_VineBylineAction(VineBylineAction vineBylineAction$$3, Parcel parcel$$420, int flags$$142) {
        parcel$$420.writeString(vineBylineAction$$3.detailedDescription);
        parcel$$420.writeString(vineBylineAction$$3.actionTitle);
        if (vineBylineAction$$3.records == null) {
            parcel$$420.writeInt(-1);
        } else {
            parcel$$420.writeInt(vineBylineAction$$3.records.size());
            Iterator<VineShortPost> it = vineBylineAction$$3.records.iterator();
            while (it.hasNext()) {
                VineShortPost vineShortPost$$2 = it.next();
                if (vineShortPost$$2 == null) {
                    parcel$$420.writeInt(-1);
                } else {
                    parcel$$420.writeInt(1);
                    writeco_vine_android_api_response_VineShortPost(vineShortPost$$2, parcel$$420, flags$$142);
                }
            }
        }
        parcel$$420.writeString(vineBylineAction$$3.description);
        parcel$$420.writeString(vineBylineAction$$3.actionIconUrl);
    }

    private void writeco_vine_android_api_response_VineShortPost(VineShortPost vineShortPost$$3, Parcel parcel$$421, int flags$$143) {
        if (vineShortPost$$3.postId == null) {
            parcel$$421.writeInt(-1);
        } else {
            parcel$$421.writeInt(1);
            parcel$$421.writeLong(vineShortPost$$3.postId.longValue());
        }
        parcel$$421.writeString(vineShortPost$$3.type);
        parcel$$421.writeString(vineShortPost$$3.thumbnailUrl);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineBylineAction getParcel() {
        return this.vineBylineAction$$0;
    }
}
