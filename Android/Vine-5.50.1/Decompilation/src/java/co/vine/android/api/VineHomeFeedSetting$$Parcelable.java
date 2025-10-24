package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineHomeFeedSetting$$Parcelable implements Parcelable, ParcelWrapper<VineHomeFeedSetting> {
    public static final VineHomeFeedSetting$$Parcelable$Creator$$46 CREATOR = new VineHomeFeedSetting$$Parcelable$Creator$$46();
    private VineHomeFeedSetting vineHomeFeedSetting$$0;

    public VineHomeFeedSetting$$Parcelable(Parcel parcel$$428) {
        VineHomeFeedSetting vineHomeFeedSetting$$2;
        if (parcel$$428.readInt() == -1) {
            vineHomeFeedSetting$$2 = null;
        } else {
            vineHomeFeedSetting$$2 = readco_vine_android_api_VineHomeFeedSetting(parcel$$428);
        }
        this.vineHomeFeedSetting$$0 = vineHomeFeedSetting$$2;
    }

    public VineHomeFeedSetting$$Parcelable(VineHomeFeedSetting vineHomeFeedSetting$$4) {
        this.vineHomeFeedSetting$$0 = vineHomeFeedSetting$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$429, int flags) {
        if (this.vineHomeFeedSetting$$0 == null) {
            parcel$$429.writeInt(-1);
        } else {
            parcel$$429.writeInt(1);
            writeco_vine_android_api_VineHomeFeedSetting(this.vineHomeFeedSetting$$0, parcel$$429, flags);
        }
    }

    private VineHomeFeedSetting readco_vine_android_api_VineHomeFeedSetting(Parcel parcel$$430) {
        VineHomeFeedSetting vineHomeFeedSetting$$1 = new VineHomeFeedSetting();
        vineHomeFeedSetting$$1.isBooleanSetting = parcel$$430.readInt() == 1;
        vineHomeFeedSetting$$1.name = parcel$$430.readString();
        vineHomeFeedSetting$$1.description = parcel$$430.readString();
        vineHomeFeedSetting$$1.section = parcel$$430.readString();
        vineHomeFeedSetting$$1.title = parcel$$430.readString();
        vineHomeFeedSetting$$1.choices = (ArrayList) parcel$$430.readSerializable();
        vineHomeFeedSetting$$1.value = parcel$$430.readString();
        return vineHomeFeedSetting$$1;
    }

    private void writeco_vine_android_api_VineHomeFeedSetting(VineHomeFeedSetting vineHomeFeedSetting$$3, Parcel parcel$$431, int flags$$145) {
        parcel$$431.writeInt(vineHomeFeedSetting$$3.isBooleanSetting ? 1 : 0);
        parcel$$431.writeString(vineHomeFeedSetting$$3.name);
        parcel$$431.writeString(vineHomeFeedSetting$$3.description);
        parcel$$431.writeString(vineHomeFeedSetting$$3.section);
        parcel$$431.writeString(vineHomeFeedSetting$$3.title);
        parcel$$431.writeSerializable(vineHomeFeedSetting$$3.choices);
        parcel$$431.writeString(vineHomeFeedSetting$$3.value);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineHomeFeedSetting getParcel() {
        return this.vineHomeFeedSetting$$0;
    }
}
