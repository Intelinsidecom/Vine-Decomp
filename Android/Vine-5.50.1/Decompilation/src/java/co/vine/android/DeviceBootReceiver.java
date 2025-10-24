package co.vine.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import co.vine.android.service.ResourceService;
import co.vine.android.service.components.Components;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class DeviceBootReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        SLog.d("Received device boot message {}.", action);
        if (!"android.intent.action.PACKAGE_REPLACED".equals(action)) {
            try {
                context.startService(new Intent(context, (Class<?>) ResourceService.class));
                context.startService(Components.deviceBootComponent().getDeviceBootIntent(context));
            } catch (SecurityException e) {
                CrashUtil.logException(e);
            }
        }
    }
}
