package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineNotificationSetting$$Parcelable implements Parcelable, ParcelWrapper<VineNotificationSetting> {
    public static final VineNotificationSetting$$Parcelable$Creator$$31 CREATOR = new VineNotificationSetting$$Parcelable$Creator$$31();
    private VineNotificationSetting vineNotificationSetting$$0;

    public VineNotificationSetting$$Parcelable(Parcel parcel$$339) {
        VineNotificationSetting vineNotificationSetting$$2;
        if (parcel$$339.readInt() == -1) {
            vineNotificationSetting$$2 = null;
        } else {
            vineNotificationSetting$$2 = readco_vine_android_api_VineNotificationSetting(parcel$$339);
        }
        this.vineNotificationSetting$$0 = vineNotificationSetting$$2;
    }

    public VineNotificationSetting$$Parcelable(VineNotificationSetting vineNotificationSetting$$4) {
        this.vineNotificationSetting$$0 = vineNotificationSetting$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$340, int flags) {
        if (this.vineNotificationSetting$$0 == null) {
            parcel$$340.writeInt(-1);
        } else {
            parcel$$340.writeInt(1);
            writeco_vine_android_api_VineNotificationSetting(this.vineNotificationSetting$$0, parcel$$340, flags);
        }
    }

    private VineNotificationSetting readco_vine_android_api_VineNotificationSetting(Parcel parcel$$341) {
        VineNotificationSetting vineNotificationSetting$$1 = new VineNotificationSetting();
        vineNotificationSetting$$1.isBooleanSetting = parcel$$341.readInt() == 1;
        vineNotificationSetting$$1.name = parcel$$341.readString();
        vineNotificationSetting$$1.section = parcel$$341.readString();
        vineNotificationSetting$$1.title = parcel$$341.readString();
        vineNotificationSetting$$1.choices = (ArrayList) parcel$$341.readSerializable();
        vineNotificationSetting$$1.value = parcel$$341.readString();
        return vineNotificationSetting$$1;
    }

    private void writeco_vine_android_api_VineNotificationSetting(VineNotificationSetting vineNotificationSetting$$3, Parcel parcel$$342, int flags$$123) {
        parcel$$342.writeInt(vineNotificationSetting$$3.isBooleanSetting ? 1 : 0);
        parcel$$342.writeString(vineNotificationSetting$$3.name);
        parcel$$342.writeString(vineNotificationSetting$$3.section);
        parcel$$342.writeString(vineNotificationSetting$$3.title);
        parcel$$342.writeSerializable(vineNotificationSetting$$3.choices);
        parcel$$342.writeString(vineNotificationSetting$$3.value);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineNotificationSetting getParcel() {
        return this.vineNotificationSetting$$0;
    }
}
