package co.vine.android.service.components.loopreporting;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import co.vine.android.client.AppController;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.ActionCodeComponent;
import co.vine.android.service.components.Components;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.LoopManager;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class LoopReportingComponent extends ActionCodeComponent<Actions> {
    private long mLoopCountSubmissionInterval = 60000;

    protected enum Actions {
        SEND_LOOP_COUNTS
    }

    public static void registerReceiver(Context context) {
        context.registerReceiver(new BroadcastReceiver() { // from class: co.vine.android.service.components.loopreporting.LoopReportingComponent.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Components.loopReportingComponent().sendLoopCounts(AppController.getInstance(context2), context2);
            }
        }, new IntentFilter("co.vine.android.bc_send_loops"), CrossConstants.BROADCAST_PERMISSION, null);
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.SEND_LOOP_COUNTS, new SendLoopCountsAction(), new SendLoopCountsActionNotifier());
    }

    public void refreshLoopSendingAlarm(Context context, long firstRunTime) {
        AlarmManager am = (AlarmManager) context.getSystemService("alarm");
        am.setRepeating(0, firstRunTime, this.mLoopCountSubmissionInterval, PendingIntent.getBroadcast(context, 0, new Intent("co.vine.android.bc_send_loops").setPackage(context.getPackageName()), 134217728));
    }

    public void onIntervalUpdated(Context context, long intervalMs) {
        SLog.d("Changing loop interval from {} to {}", Long.valueOf(this.mLoopCountSubmissionInterval), Long.valueOf(intervalMs));
        this.mLoopCountSubmissionInterval = Math.max(intervalMs, 60000L);
        refreshLoopSendingAlarm(context, System.currentTimeMillis() + this.mLoopCountSubmissionInterval);
    }

    public String sendLoopCounts(AppController appController, Context context) {
        LoopManager loopManager = LoopManager.get(context);
        ArrayList<LoopManager.Record> pendingLoops = new ArrayList<>(loopManager.popPendingLoops());
        if (pendingLoops.isEmpty()) {
            return "";
        }
        Bundle b = new Bundle();
        b.putParcelableArrayList("loops", pendingLoops);
        return executeServiceAction(appController, Actions.SEND_LOOP_COUNTS, b);
    }
}
