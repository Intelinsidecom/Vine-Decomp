package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class TagDetails$$Parcelable implements Parcelable, ParcelWrapper<TagDetails> {
    public static final TagDetails$$Parcelable$Creator$$16 CREATOR = new TagDetails$$Parcelable$Creator$$16();
    private TagDetails tagDetails$$9;

    public TagDetails$$Parcelable(Parcel parcel$$216) {
        TagDetails tagDetails$$11;
        if (parcel$$216.readInt() == -1) {
            tagDetails$$11 = null;
        } else {
            tagDetails$$11 = readco_vine_android_scribe_model_TagDetails(parcel$$216);
        }
        this.tagDetails$$9 = tagDetails$$11;
    }

    public TagDetails$$Parcelable(TagDetails tagDetails$$13) {
        this.tagDetails$$9 = tagDetails$$13;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$217, int flags) {
        if (this.tagDetails$$9 == null) {
            parcel$$217.writeInt(-1);
        } else {
            parcel$$217.writeInt(1);
            writeco_vine_android_scribe_model_TagDetails(this.tagDetails$$9, parcel$$217, flags);
        }
    }

    private TagDetails readco_vine_android_scribe_model_TagDetails(Parcel parcel$$218) {
        TagDetails tagDetails$$10 = new TagDetails();
        tagDetails$$10.tagId = parcel$$218.readString();
        return tagDetails$$10;
    }

    private void writeco_vine_android_scribe_model_TagDetails(TagDetails tagDetails$$12, Parcel parcel$$219, int flags$$84) {
        parcel$$219.writeString(tagDetails$$12.tagId);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public TagDetails getParcel() {
        return this.tagDetails$$9;
    }
}
