package io.fabric.sdk.android.services.events;

/* loaded from: classes.dex */
public interface EventsStrategy<T> extends EventsManager<T>, FileRollOverManager {
    FilesSender getFilesSender();
}
