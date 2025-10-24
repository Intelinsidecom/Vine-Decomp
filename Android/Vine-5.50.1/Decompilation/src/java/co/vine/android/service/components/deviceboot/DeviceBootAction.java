package co.vine.android.service.components.deviceboot;

import android.content.Context;
import co.vine.android.prefetch.PrefetchManager;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.service.components.Components;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public final class DeviceBootAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        SLog.i("Running device boot actions.");
        try {
            PrefetchManager.getInstance((Context) request.service).onDeviceBoot();
            Components.loopReportingComponent().refreshLoopSendingAlarm((Context) request.service, System.currentTimeMillis());
            return null;
        } catch (RuntimeException e) {
            CrashUtil.logOrThrowInDebug(e);
            return null;
        }
    }
}
