package co.vine.android.recorder.audio;

/* loaded from: classes.dex */
public interface AudioSource {
    boolean isInitialized();

    void start(AudioReceiver audioReceiver);

    void stop();
}
