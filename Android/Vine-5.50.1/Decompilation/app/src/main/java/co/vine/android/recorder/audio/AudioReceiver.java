package co.vine.android.recorder.audio;

import co.vine.android.recorder.audio.AudioArrays;

/* loaded from: classes.dex */
public interface AudioReceiver {
    void beforeDataCollection();

    AudioArrays.AudioArrayType getType();

    boolean needsMoreData();

    boolean onAudioDataReceived(int i, AudioArray audioArray);

    void onAudioSourceStopped();
}
