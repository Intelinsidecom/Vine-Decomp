package co.vine.android.scribe;

import co.vine.android.scribe.model.ClientEvent;

/* loaded from: classes.dex */
public interface ScribeLogger {
    ClientEvent getDefaultClientEvent();

    void logClientEvent(ClientEvent clientEvent);
}
