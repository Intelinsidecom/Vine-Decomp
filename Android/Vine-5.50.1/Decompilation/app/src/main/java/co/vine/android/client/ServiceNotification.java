package co.vine.android.client;

import android.content.Context;
import android.os.Bundle;
import java.util.List;

/* loaded from: classes.dex */
public class ServiceNotification {
    public final int actionCode;
    public final Bundle b;
    public final Context context;
    public final List<AppSessionListener> listeners;
    public final String reasonPhrase;
    public final String reqId;
    public final int statusCode;

    public ServiceNotification(Context context, String reqId, int actionCode, int statusCode, String reasonPhrase, Bundle b, List<AppSessionListener> listeners) {
        this.context = context;
        this.reqId = reqId;
        this.actionCode = actionCode;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.b = b;
        this.listeners = listeners;
    }
}
