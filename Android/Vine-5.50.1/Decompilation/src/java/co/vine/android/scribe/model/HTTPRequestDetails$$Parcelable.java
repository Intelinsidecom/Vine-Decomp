package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class HTTPRequestDetails$$Parcelable implements Parcelable, ParcelWrapper<HTTPRequestDetails> {
    public static final HTTPRequestDetails$$Parcelable$Creator$$12 CREATOR = new HTTPRequestDetails$$Parcelable$Creator$$12();
    private HTTPRequestDetails hTTPRequestDetails$$6;

    public HTTPRequestDetails$$Parcelable(Parcel parcel$$192) {
        HTTPRequestDetails hTTPRequestDetails$$8;
        if (parcel$$192.readInt() == -1) {
            hTTPRequestDetails$$8 = null;
        } else {
            hTTPRequestDetails$$8 = readco_vine_android_scribe_model_HTTPRequestDetails(parcel$$192);
        }
        this.hTTPRequestDetails$$6 = hTTPRequestDetails$$8;
    }

    public HTTPRequestDetails$$Parcelable(HTTPRequestDetails hTTPRequestDetails$$10) {
        this.hTTPRequestDetails$$6 = hTTPRequestDetails$$10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$193, int flags) {
        if (this.hTTPRequestDetails$$6 == null) {
            parcel$$193.writeInt(-1);
        } else {
            parcel$$193.writeInt(1);
            writeco_vine_android_scribe_model_HTTPRequestDetails(this.hTTPRequestDetails$$6, parcel$$193, flags);
        }
    }

    private HTTPRequestDetails readco_vine_android_scribe_model_HTTPRequestDetails(Parcel parcel$$194) {
        Integer integer$$14;
        Integer integer$$15;
        HTTPRequestDetails hTTPRequestDetails$$7 = new HTTPRequestDetails();
        hTTPRequestDetails$$7.method = parcel$$194.readString();
        int int$$164 = parcel$$194.readInt();
        if (int$$164 < 0) {
            integer$$14 = null;
        } else {
            integer$$14 = Integer.valueOf(parcel$$194.readInt());
        }
        hTTPRequestDetails$$7.apiError = integer$$14;
        int int$$165 = parcel$$194.readInt();
        if (int$$165 < 0) {
            integer$$15 = null;
        } else {
            integer$$15 = Integer.valueOf(parcel$$194.readInt());
        }
        hTTPRequestDetails$$7.httpStatus = integer$$15;
        hTTPRequestDetails$$7.osErrorDetails = parcel$$194.readString();
        hTTPRequestDetails$$7.networkError = parcel$$194.readString();
        hTTPRequestDetails$$7.url = parcel$$194.readString();
        return hTTPRequestDetails$$7;
    }

    private void writeco_vine_android_scribe_model_HTTPRequestDetails(HTTPRequestDetails hTTPRequestDetails$$9, Parcel parcel$$195, int flags$$78) {
        parcel$$195.writeString(hTTPRequestDetails$$9.method);
        if (hTTPRequestDetails$$9.apiError == null) {
            parcel$$195.writeInt(-1);
        } else {
            parcel$$195.writeInt(1);
            parcel$$195.writeInt(hTTPRequestDetails$$9.apiError.intValue());
        }
        if (hTTPRequestDetails$$9.httpStatus == null) {
            parcel$$195.writeInt(-1);
        } else {
            parcel$$195.writeInt(1);
            parcel$$195.writeInt(hTTPRequestDetails$$9.httpStatus.intValue());
        }
        parcel$$195.writeString(hTTPRequestDetails$$9.osErrorDetails);
        parcel$$195.writeString(hTTPRequestDetails$$9.networkError);
        parcel$$195.writeString(hTTPRequestDetails$$9.url);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public HTTPRequestDetails getParcel() {
        return this.hTTPRequestDetails$$6;
    }
}
