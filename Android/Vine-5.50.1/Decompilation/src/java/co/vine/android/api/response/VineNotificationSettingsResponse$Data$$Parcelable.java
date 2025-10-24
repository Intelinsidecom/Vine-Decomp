package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.VineNotificationSetting;
import co.vine.android.api.response.VineNotificationSettingsResponse;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineNotificationSettingsResponse$Data$$Parcelable implements Parcelable, ParcelWrapper<VineNotificationSettingsResponse.Data> {
    public static final VineNotificationSettingsResponse$Data$$Parcelable$Creator$$32 CREATOR = new VineNotificationSettingsResponse$Data$$Parcelable$Creator$$32();
    private VineNotificationSettingsResponse.Data data$$0;

    public VineNotificationSettingsResponse$Data$$Parcelable(Parcel parcel$$344) {
        VineNotificationSettingsResponse.Data data$$2;
        if (parcel$$344.readInt() == -1) {
            data$$2 = null;
        } else {
            data$$2 = readco_vine_android_api_response_VineNotificationSettingsResponse$Data(parcel$$344);
        }
        this.data$$0 = data$$2;
    }

    public VineNotificationSettingsResponse$Data$$Parcelable(VineNotificationSettingsResponse.Data data$$4) {
        this.data$$0 = data$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$345, int flags) {
        if (this.data$$0 == null) {
            parcel$$345.writeInt(-1);
        } else {
            parcel$$345.writeInt(1);
            writeco_vine_android_api_response_VineNotificationSettingsResponse$Data(this.data$$0, parcel$$345, flags);
        }
    }

    private VineNotificationSettingsResponse.Data readco_vine_android_api_response_VineNotificationSettingsResponse$Data(Parcel parcel$$346) {
        ArrayList<VineNotificationSetting> list$$30;
        VineNotificationSetting vineNotificationSetting$$6;
        VineNotificationSettingsResponse.Data data$$1 = new VineNotificationSettingsResponse.Data();
        int int$$319 = parcel$$346.readInt();
        if (int$$319 < 0) {
            list$$30 = null;
        } else {
            list$$30 = new ArrayList<>();
            for (int int$$320 = 0; int$$320 < int$$319; int$$320++) {
                if (parcel$$346.readInt() == -1) {
                    vineNotificationSetting$$6 = null;
                } else {
                    vineNotificationSetting$$6 = readco_vine_android_api_VineNotificationSetting(parcel$$346);
                }
                list$$30.add(vineNotificationSetting$$6);
            }
        }
        data$$1.items = list$$30;
        data$$1.previousPage = parcel$$346.readInt();
        data$$1.size = parcel$$346.readInt();
        data$$1.nextPage = parcel$$346.readInt();
        data$$1.anchor = parcel$$346.readString();
        data$$1.count = parcel$$346.readInt();
        return data$$1;
    }

    private VineNotificationSetting readco_vine_android_api_VineNotificationSetting(Parcel parcel$$347) {
        VineNotificationSetting vineNotificationSetting$$5 = new VineNotificationSetting();
        vineNotificationSetting$$5.isBooleanSetting = parcel$$347.readInt() == 1;
        vineNotificationSetting$$5.name = parcel$$347.readString();
        vineNotificationSetting$$5.section = parcel$$347.readString();
        vineNotificationSetting$$5.title = parcel$$347.readString();
        vineNotificationSetting$$5.choices = (ArrayList) parcel$$347.readSerializable();
        vineNotificationSetting$$5.value = parcel$$347.readString();
        return vineNotificationSetting$$5;
    }

    private void writeco_vine_android_api_response_VineNotificationSettingsResponse$Data(VineNotificationSettingsResponse.Data data$$3, Parcel parcel$$348, int flags$$124) {
        if (data$$3.items == null) {
            parcel$$348.writeInt(-1);
        } else {
            parcel$$348.writeInt(data$$3.items.size());
            Iterator<VineNotificationSetting> it = data$$3.items.iterator();
            while (it.hasNext()) {
                VineNotificationSetting vineNotificationSetting$$7 = it.next();
                if (vineNotificationSetting$$7 == null) {
                    parcel$$348.writeInt(-1);
                } else {
                    parcel$$348.writeInt(1);
                    writeco_vine_android_api_VineNotificationSetting(vineNotificationSetting$$7, parcel$$348, flags$$124);
                }
            }
        }
        parcel$$348.writeInt(data$$3.previousPage);
        parcel$$348.writeInt(data$$3.size);
        parcel$$348.writeInt(data$$3.nextPage);
        parcel$$348.writeString(data$$3.anchor);
        parcel$$348.writeInt(data$$3.count);
    }

    private void writeco_vine_android_api_VineNotificationSetting(VineNotificationSetting vineNotificationSetting$$8, Parcel parcel$$349, int flags$$125) {
        parcel$$349.writeInt(vineNotificationSetting$$8.isBooleanSetting ? 1 : 0);
        parcel$$349.writeString(vineNotificationSetting$$8.name);
        parcel$$349.writeString(vineNotificationSetting$$8.section);
        parcel$$349.writeString(vineNotificationSetting$$8.title);
        parcel$$349.writeSerializable(vineNotificationSetting$$8.choices);
        parcel$$349.writeString(vineNotificationSetting$$8.value);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineNotificationSettingsResponse.Data getParcel() {
        return this.data$$0;
    }
}
