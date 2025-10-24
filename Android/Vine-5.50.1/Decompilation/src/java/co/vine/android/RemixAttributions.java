package co.vine.android;

import co.vine.android.api.VineAudioMetadata;
import co.vine.android.api.VineSource;

/* loaded from: classes.dex */
public class RemixAttributions {
    private final VineAudioMetadata mAudioMetadata;
    private final int mType;
    private final VineSource mVineSource;

    public RemixAttributions(VineSource vineSource) {
        this.mVineSource = vineSource;
        this.mType = 0;
        this.mAudioMetadata = null;
    }

    public RemixAttributions(VineAudioMetadata audioMetadata) {
        this.mAudioMetadata = audioMetadata;
        this.mType = 1;
        this.mVineSource = null;
    }

    public int getType() {
        return this.mType;
    }

    public VineSource getVineSource() {
        return this.mVineSource;
    }

    public VineAudioMetadata getAudioMetadata() {
        return this.mAudioMetadata;
    }
}
