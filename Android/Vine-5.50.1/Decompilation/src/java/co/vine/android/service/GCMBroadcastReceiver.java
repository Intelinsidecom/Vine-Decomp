package co.vine.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import co.vine.android.Settings;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class GCMBroadcastReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        SLog.dWithTag("GCMBroadcastReceiver", "Message received!");
        if (Settings.isNotificationEnabled(context)) {
            Intent serviceIntent = new Intent(context, (Class<?>) GCMNotificationService.class);
            serviceIntent.putExtras(intent.getExtras());
            serviceIntent.setAction("co.vine.android.notifications.ACTION_NEW_NOTIFICATION");
            context.startService(serviceIntent);
        }
        setResultCode(-1);
    }
}
