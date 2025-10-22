package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.VineHomeFeedSetting;
import co.vine.android.api.response.VineHomeFeedSettingsResponse;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineHomeFeedSettingsResponse$Data$$Parcelable implements Parcelable, ParcelWrapper<VineHomeFeedSettingsResponse.Data> {
    public static final VineHomeFeedSettingsResponse$Data$$Parcelable$Creator$$47 CREATOR = new VineHomeFeedSettingsResponse$Data$$Parcelable$Creator$$47();
    private VineHomeFeedSettingsResponse.Data data$$10;

    public VineHomeFeedSettingsResponse$Data$$Parcelable(Parcel parcel$$433) {
        VineHomeFeedSettingsResponse.Data data$$12;
        if (parcel$$433.readInt() == -1) {
            data$$12 = null;
        } else {
            data$$12 = readco_vine_android_api_response_VineHomeFeedSettingsResponse$Data(parcel$$433);
        }
        this.data$$10 = data$$12;
    }

    public VineHomeFeedSettingsResponse$Data$$Parcelable(VineHomeFeedSettingsResponse.Data data$$14) {
        this.data$$10 = data$$14;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$434, int flags) {
        if (this.data$$10 == null) {
            parcel$$434.writeInt(-1);
        } else {
            parcel$$434.writeInt(1);
            writeco_vine_android_api_response_VineHomeFeedSettingsResponse$Data(this.data$$10, parcel$$434, flags);
        }
    }

    private VineHomeFeedSettingsResponse.Data readco_vine_android_api_response_VineHomeFeedSettingsResponse$Data(Parcel parcel$$435) {
        ArrayList<VineHomeFeedSetting> list$$41;
        VineHomeFeedSetting vineHomeFeedSetting$$6;
        VineHomeFeedSettingsResponse.Data data$$11 = new VineHomeFeedSettingsResponse.Data();
        int int$$350 = parcel$$435.readInt();
        if (int$$350 < 0) {
            list$$41 = null;
        } else {
            list$$41 = new ArrayList<>();
            for (int int$$351 = 0; int$$351 < int$$350; int$$351++) {
                if (parcel$$435.readInt() == -1) {
                    vineHomeFeedSetting$$6 = null;
                } else {
                    vineHomeFeedSetting$$6 = readco_vine_android_api_VineHomeFeedSetting(parcel$$435);
                }
                list$$41.add(vineHomeFeedSetting$$6);
            }
        }
        data$$11.items = list$$41;
        data$$11.previousPage = parcel$$435.readInt();
        data$$11.size = parcel$$435.readInt();
        data$$11.nextPage = parcel$$435.readInt();
        data$$11.anchor = parcel$$435.readString();
        data$$11.count = parcel$$435.readInt();
        return data$$11;
    }

    private VineHomeFeedSetting readco_vine_android_api_VineHomeFeedSetting(Parcel parcel$$436) {
        VineHomeFeedSetting vineHomeFeedSetting$$5 = new VineHomeFeedSetting();
        vineHomeFeedSetting$$5.isBooleanSetting = parcel$$436.readInt() == 1;
        vineHomeFeedSetting$$5.name = parcel$$436.readString();
        vineHomeFeedSetting$$5.description = parcel$$436.readString();
        vineHomeFeedSetting$$5.section = parcel$$436.readString();
        vineHomeFeedSetting$$5.title = parcel$$436.readString();
        vineHomeFeedSetting$$5.choices = (ArrayList) parcel$$436.readSerializable();
        vineHomeFeedSetting$$5.value = parcel$$436.readString();
        return vineHomeFeedSetting$$5;
    }

    private void writeco_vine_android_api_response_VineHomeFeedSettingsResponse$Data(VineHomeFeedSettingsResponse.Data data$$13, Parcel parcel$$437, int flags$$146) {
        if (data$$13.items == null) {
            parcel$$437.writeInt(-1);
        } else {
            parcel$$437.writeInt(data$$13.items.size());
            Iterator<VineHomeFeedSetting> it = data$$13.items.iterator();
            while (it.hasNext()) {
                VineHomeFeedSetting vineHomeFeedSetting$$7 = it.next();
                if (vineHomeFeedSetting$$7 == null) {
                    parcel$$437.writeInt(-1);
                } else {
                    parcel$$437.writeInt(1);
                    writeco_vine_android_api_VineHomeFeedSetting(vineHomeFeedSetting$$7, parcel$$437, flags$$146);
                }
            }
        }
        parcel$$437.writeInt(data$$13.previousPage);
        parcel$$437.writeInt(data$$13.size);
        parcel$$437.writeInt(data$$13.nextPage);
        parcel$$437.writeString(data$$13.anchor);
        parcel$$437.writeInt(data$$13.count);
    }

    private void writeco_vine_android_api_VineHomeFeedSetting(VineHomeFeedSetting vineHomeFeedSetting$$8, Parcel parcel$$438, int flags$$147) {
        parcel$$438.writeInt(vineHomeFeedSetting$$8.isBooleanSetting ? 1 : 0);
        parcel$$438.writeString(vineHomeFeedSetting$$8.name);
        parcel$$438.writeString(vineHomeFeedSetting$$8.description);
        parcel$$438.writeString(vineHomeFeedSetting$$8.section);
        parcel$$438.writeString(vineHomeFeedSetting$$8.title);
        parcel$$438.writeSerializable(vineHomeFeedSetting$$8.choices);
        parcel$$438.writeString(vineHomeFeedSetting$$8.value);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineHomeFeedSettingsResponse.Data getParcel() {
        return this.data$$10;
    }
}
