package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class ItemPosition$$Parcelable implements Parcelable, ParcelWrapper<ItemPosition> {
    public static final ItemPosition$$Parcelable$Creator$$21 CREATOR = new ItemPosition$$Parcelable$Creator$$21();
    private ItemPosition itemPosition$$9;

    public ItemPosition$$Parcelable(Parcel parcel$$247) {
        ItemPosition itemPosition$$11;
        if (parcel$$247.readInt() == -1) {
            itemPosition$$11 = null;
        } else {
            itemPosition$$11 = readco_vine_android_scribe_model_ItemPosition(parcel$$247);
        }
        this.itemPosition$$9 = itemPosition$$11;
    }

    public ItemPosition$$Parcelable(ItemPosition itemPosition$$13) {
        this.itemPosition$$9 = itemPosition$$13;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$248, int flags) {
        if (this.itemPosition$$9 == null) {
            parcel$$248.writeInt(-1);
        } else {
            parcel$$248.writeInt(1);
            writeco_vine_android_scribe_model_ItemPosition(this.itemPosition$$9, parcel$$248, flags);
        }
    }

    private ItemPosition readco_vine_android_scribe_model_ItemPosition(Parcel parcel$$249) {
        Integer integer$$19;
        ItemPosition itemPosition$$10 = new ItemPosition();
        int int$$205 = parcel$$249.readInt();
        if (int$$205 < 0) {
            integer$$19 = null;
        } else {
            integer$$19 = Integer.valueOf(parcel$$249.readInt());
        }
        itemPosition$$10.offset = integer$$19;
        return itemPosition$$10;
    }

    private void writeco_vine_android_scribe_model_ItemPosition(ItemPosition itemPosition$$12, Parcel parcel$$250, int flags$$92) {
        if (itemPosition$$12.offset == null) {
            parcel$$250.writeInt(-1);
        } else {
            parcel$$250.writeInt(1);
            parcel$$250.writeInt(itemPosition$$12.offset.intValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public ItemPosition getParcel() {
        return this.itemPosition$$9;
    }
}
