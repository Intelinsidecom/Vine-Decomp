package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class PlaybackSummaryDetails$$Parcelable implements Parcelable, ParcelWrapper<PlaybackSummaryDetails> {
    public static final PlaybackSummaryDetails$$Parcelable$Creator$$53 CREATOR = new PlaybackSummaryDetails$$Parcelable$Creator$$53();
    private PlaybackSummaryDetails playbackSummaryDetails$$9;

    public PlaybackSummaryDetails$$Parcelable(Parcel parcel$$465) {
        PlaybackSummaryDetails playbackSummaryDetails$$11;
        if (parcel$$465.readInt() == -1) {
            playbackSummaryDetails$$11 = null;
        } else {
            playbackSummaryDetails$$11 = readco_vine_android_scribe_model_PlaybackSummaryDetails(parcel$$465);
        }
        this.playbackSummaryDetails$$9 = playbackSummaryDetails$$11;
    }

    public PlaybackSummaryDetails$$Parcelable(PlaybackSummaryDetails playbackSummaryDetails$$13) {
        this.playbackSummaryDetails$$9 = playbackSummaryDetails$$13;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$466, int flags) {
        if (this.playbackSummaryDetails$$9 == null) {
            parcel$$466.writeInt(-1);
        } else {
            parcel$$466.writeInt(1);
            writeco_vine_android_scribe_model_PlaybackSummaryDetails(this.playbackSummaryDetails$$9, parcel$$466, flags);
        }
    }

    private PlaybackSummaryDetails readco_vine_android_scribe_model_PlaybackSummaryDetails(Parcel parcel$$467) {
        Float float$$15;
        Float float$$16;
        Integer integer$$26;
        Float float$$17;
        Float float$$18;
        Float float$$19;
        PlaybackSummaryDetails playbackSummaryDetails$$10 = new PlaybackSummaryDetails();
        int int$$358 = parcel$$467.readInt();
        if (int$$358 < 0) {
            float$$15 = null;
        } else {
            float$$15 = Float.valueOf(parcel$$467.readFloat());
        }
        playbackSummaryDetails$$10.videoEndTime = float$$15;
        int int$$359 = parcel$$467.readInt();
        if (int$$359 < 0) {
            float$$16 = null;
        } else {
            float$$16 = Float.valueOf(parcel$$467.readFloat());
        }
        playbackSummaryDetails$$10.videoStarttime = float$$16;
        int int$$360 = parcel$$467.readInt();
        if (int$$360 < 0) {
            integer$$26 = null;
        } else {
            integer$$26 = Integer.valueOf(parcel$$467.readInt());
        }
        playbackSummaryDetails$$10.playbackInterruptions = integer$$26;
        int int$$361 = parcel$$467.readInt();
        if (int$$361 < 0) {
            float$$17 = null;
        } else {
            float$$17 = Float.valueOf(parcel$$467.readFloat());
        }
        playbackSummaryDetails$$10.timeSpentPlaying = float$$17;
        int int$$362 = parcel$$467.readInt();
        if (int$$362 < 0) {
            float$$18 = null;
        } else {
            float$$18 = Float.valueOf(parcel$$467.readFloat());
        }
        playbackSummaryDetails$$10.timeSpentBuffering = float$$18;
        int int$$363 = parcel$$467.readInt();
        if (int$$363 < 0) {
            float$$19 = null;
        } else {
            float$$19 = Float.valueOf(parcel$$467.readFloat());
        }
        playbackSummaryDetails$$10.timeSpentPaused = float$$19;
        return playbackSummaryDetails$$10;
    }

    private void writeco_vine_android_scribe_model_PlaybackSummaryDetails(PlaybackSummaryDetails playbackSummaryDetails$$12, Parcel parcel$$468, int flags$$153) {
        if (playbackSummaryDetails$$12.videoEndTime == null) {
            parcel$$468.writeInt(-1);
        } else {
            parcel$$468.writeInt(1);
            parcel$$468.writeFloat(playbackSummaryDetails$$12.videoEndTime.floatValue());
        }
        if (playbackSummaryDetails$$12.videoStarttime == null) {
            parcel$$468.writeInt(-1);
        } else {
            parcel$$468.writeInt(1);
            parcel$$468.writeFloat(playbackSummaryDetails$$12.videoStarttime.floatValue());
        }
        if (playbackSummaryDetails$$12.playbackInterruptions == null) {
            parcel$$468.writeInt(-1);
        } else {
            parcel$$468.writeInt(1);
            parcel$$468.writeInt(playbackSummaryDetails$$12.playbackInterruptions.intValue());
        }
        if (playbackSummaryDetails$$12.timeSpentPlaying == null) {
            parcel$$468.writeInt(-1);
        } else {
            parcel$$468.writeInt(1);
            parcel$$468.writeFloat(playbackSummaryDetails$$12.timeSpentPlaying.floatValue());
        }
        if (playbackSummaryDetails$$12.timeSpentBuffering == null) {
            parcel$$468.writeInt(-1);
        } else {
            parcel$$468.writeInt(1);
            parcel$$468.writeFloat(playbackSummaryDetails$$12.timeSpentBuffering.floatValue());
        }
        if (playbackSummaryDetails$$12.timeSpentPaused == null) {
            parcel$$468.writeInt(-1);
        } else {
            parcel$$468.writeInt(1);
            parcel$$468.writeFloat(playbackSummaryDetails$$12.timeSpentPaused.floatValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public PlaybackSummaryDetails getParcel() {
        return this.playbackSummaryDetails$$9;
    }
}
