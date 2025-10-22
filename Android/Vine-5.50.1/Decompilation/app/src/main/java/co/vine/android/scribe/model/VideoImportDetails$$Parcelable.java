package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VideoImportDetails$$Parcelable implements Parcelable, ParcelWrapper<VideoImportDetails> {
    public static final VideoImportDetails$$Parcelable$Creator$$7 CREATOR = new VideoImportDetails$$Parcelable$Creator$$7();
    private VideoImportDetails videoImportDetails$$6;

    public VideoImportDetails$$Parcelable(Parcel parcel$$163) {
        VideoImportDetails videoImportDetails$$8;
        if (parcel$$163.readInt() == -1) {
            videoImportDetails$$8 = null;
        } else {
            videoImportDetails$$8 = readco_vine_android_scribe_model_VideoImportDetails(parcel$$163);
        }
        this.videoImportDetails$$6 = videoImportDetails$$8;
    }

    public VideoImportDetails$$Parcelable(VideoImportDetails videoImportDetails$$10) {
        this.videoImportDetails$$6 = videoImportDetails$$10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$164, int flags) {
        if (this.videoImportDetails$$6 == null) {
            parcel$$164.writeInt(-1);
        } else {
            parcel$$164.writeInt(1);
            writeco_vine_android_scribe_model_VideoImportDetails(this.videoImportDetails$$6, parcel$$164, flags);
        }
    }

    private VideoImportDetails readco_vine_android_scribe_model_VideoImportDetails(Parcel parcel$$165) {
        VideoImportDetails videoImportDetails$$7 = new VideoImportDetails();
        videoImportDetails$$7.result = parcel$$165.readString();
        return videoImportDetails$$7;
    }

    private void writeco_vine_android_scribe_model_VideoImportDetails(VideoImportDetails videoImportDetails$$9, Parcel parcel$$166, int flags$$71) {
        parcel$$166.writeString(videoImportDetails$$9.result);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VideoImportDetails getParcel() {
        return this.videoImportDetails$$6;
    }
}
