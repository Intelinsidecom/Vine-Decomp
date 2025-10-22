package co.vine.android.plugin;

import co.vine.android.recorder.BasicVineRecorder;

/* loaded from: classes.dex */
public interface RecorderPluginManager<T extends BasicVineRecorder> extends PluginManager<RecorderPlugin<?, T>> {
    T getRecorder();

    void onStartDrafts();
}
