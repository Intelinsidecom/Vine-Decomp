package co.vine.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import co.vine.android.client.SessionManager;

/* loaded from: classes.dex */
public class SessionChangedReceiver extends BroadcastReceiver {
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();

    public static final class RecordSessionChangedReceiver extends SessionChangedReceiver {
    }

    public static final class UploadSessionChangedReceiver extends SessionChangedReceiver {
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("co.vine.android.session.login".equals(action) || "co.vine.android.session.logout".equals(action)) {
            this.mSessionManager.resetSessions(context);
        }
    }

    public static Intent createSessionLoginIntent() {
        return new Intent("co.vine.android.session.login");
    }

    public static Intent createSessionLogoutIntent() {
        return new Intent("co.vine.android.session.logout");
    }
}
