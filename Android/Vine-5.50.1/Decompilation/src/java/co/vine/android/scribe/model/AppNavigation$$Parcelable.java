package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class AppNavigation$$Parcelable implements Parcelable, ParcelWrapper<AppNavigation> {
    public static final AppNavigation$$Parcelable$Creator$$10 CREATOR = new AppNavigation$$Parcelable$Creator$$10();
    private AppNavigation appNavigation$$6;

    public AppNavigation$$Parcelable(Parcel parcel$$180) {
        AppNavigation appNavigation$$8;
        if (parcel$$180.readInt() == -1) {
            appNavigation$$8 = null;
        } else {
            appNavigation$$8 = readco_vine_android_scribe_model_AppNavigation(parcel$$180);
        }
        this.appNavigation$$6 = appNavigation$$8;
    }

    public AppNavigation$$Parcelable(AppNavigation appNavigation$$10) {
        this.appNavigation$$6 = appNavigation$$10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$181, int flags) {
        if (this.appNavigation$$6 == null) {
            parcel$$181.writeInt(-1);
        } else {
            parcel$$181.writeInt(1);
            writeco_vine_android_scribe_model_AppNavigation(this.appNavigation$$6, parcel$$181, flags);
        }
    }

    private AppNavigation readco_vine_android_scribe_model_AppNavigation(Parcel parcel$$182) {
        AppNavigation appNavigation$$7 = new AppNavigation();
        appNavigation$$7.view = parcel$$182.readString();
        appNavigation$$7.timelineApiUrl = parcel$$182.readString();
        appNavigation$$7.ui_element = parcel$$182.readString();
        appNavigation$$7.captureSourceSection = parcel$$182.readString();
        appNavigation$$7.searchQuery = parcel$$182.readString();
        appNavigation$$7.isNewSearchView = parcel$$182.readInt() == 1;
        appNavigation$$7.section = parcel$$182.readString();
        appNavigation$$7.subview = parcel$$182.readString();
        appNavigation$$7.filtering = parcel$$182.readString();
        return appNavigation$$7;
    }

    private void writeco_vine_android_scribe_model_AppNavigation(AppNavigation appNavigation$$9, Parcel parcel$$183, int flags$$75) {
        parcel$$183.writeString(appNavigation$$9.view);
        parcel$$183.writeString(appNavigation$$9.timelineApiUrl);
        parcel$$183.writeString(appNavigation$$9.ui_element);
        parcel$$183.writeString(appNavigation$$9.captureSourceSection);
        parcel$$183.writeString(appNavigation$$9.searchQuery);
        parcel$$183.writeInt(appNavigation$$9.isNewSearchView ? 1 : 0);
        parcel$$183.writeString(appNavigation$$9.section);
        parcel$$183.writeString(appNavigation$$9.subview);
        parcel$$183.writeString(appNavigation$$9.filtering);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public AppNavigation getParcel() {
        return this.appNavigation$$6;
    }
}
