package co.vine.android.service.components.loopreporting;

import co.vine.android.api.response.VineLoopSubmissionResponse;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.service.components.Components;
import co.vine.android.util.LoopManager;
import com.edisonwang.android.slog.SLog;
import java.util.List;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class SendLoopCountsActionNotifier implements ListenerNotifier {
    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        LoopManager loopManager = LoopManager.get(notification.context);
        VineLoopSubmissionResponse response = (VineLoopSubmissionResponse) Parcels.unwrap(notification.b.getParcelable("loop_submission"));
        if (notification.statusCode == 200 && response != null) {
            long intervalMs = response.getSubmissionIntervalMillis();
            Components.loopReportingComponent().onIntervalUpdated(notification.context, intervalMs);
            loopManager.onServerSuccess();
        } else {
            List<LoopManager.Record> failedLoops = notification.b.getParcelableArrayList("loops");
            if (failedLoops != null) {
                SLog.d("Loop submission failed; re-saving {} pending loops", Integer.valueOf(failedLoops.size()));
                loopManager.onServerFailure(failedLoops);
            }
        }
    }
}
