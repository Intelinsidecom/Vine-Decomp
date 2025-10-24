package co.vine.android.plugin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import co.vine.android.PersistentPreference;
import co.vine.android.player.SdkVideoView;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.analytics.BehaviorManager;

/* loaded from: classes.dex */
public class DebugTasksPlugin extends BasePlugin implements DialogInterface.OnClickListener {

    public interface DebugTask {
        void performTask(Activity activity);
    }

    private enum DebugTasks {
        VIEW_LOGS("View Logs", new LogReaderTask()),
        DEBUG_SCREEN("Debug Screen", new DebugHomeTask()),
        ENABLE_MAIN_THREAD_MEDIA_OPS("Thread Flag for SdkVideoView", new DebugTask() { // from class: co.vine.android.plugin.DebugTasksPlugin.DebugTasks.1
            private boolean mWasEnabled = true;

            @Override // co.vine.android.plugin.DebugTasksPlugin.DebugTask
            public void performTask(Activity activity) {
                this.mWasEnabled = !this.mWasEnabled;
                SdkVideoView.setRunMediaOpsOnSeparateThread(this.mWasEnabled);
                CommonUtil.showCenteredToast(activity, "Run on separate thread? {}", Boolean.valueOf(this.mWasEnabled));
            }
        }),
        SHOW_USAGES("Show Viewing Statistics", new DebugTask() { // from class: co.vine.android.plugin.DebugTasksPlugin.DebugTasks.2
            @Override // co.vine.android.plugin.DebugTasksPlugin.DebugTask
            public void performTask(Activity activity) {
                BehaviorManager.getInstance(activity).showUsageView(activity);
            }
        }),
        ENABLE_VERBOSE_DEBUG_FLAG("Verbose Debug Flag", new DebugTask() { // from class: co.vine.android.plugin.DebugTasksPlugin.DebugTasks.3
            @Override // co.vine.android.plugin.DebugTasksPlugin.DebugTask
            public void performTask(Activity activity) {
                CommonUtil.DEBUG_VERBOSE = !CommonUtil.DEBUG_VERBOSE;
                CommonUtil.showCenteredToast(activity, "Debug Verbose: {}", Boolean.valueOf(CommonUtil.DEBUG_VERBOSE));
            }
        }),
        CLIENT_FLAGS_OVERRIDE("Client Flags Override", new ClientFlagsOverrideTask()),
        CLEAR_FLAGS_OVERRIDE("Clear Flags Override", new DebugTask() { // from class: co.vine.android.plugin.DebugTasksPlugin.DebugTasks.4
            @Override // co.vine.android.plugin.DebugTasksPlugin.DebugTask
            public void performTask(Activity activity) {
                PersistentPreference.CLIENT_FLAGS_OVERRIDE.clearValues(activity, ClientFlagsHelper.overrideFlags);
            }
        });

        private final String mLabel;
        private final DebugTask mTask;

        DebugTasks(String label, DebugTask task) {
            this.mLabel = label;
            this.mTask = task;
        }
    }

    public DebugTasksPlugin() {
        super("debug tasks");
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("Debug Tasks");
        return true;
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!"Debug Tasks".equals(item.getTitle())) {
            return false;
        }
        Activity activity = getActivity();
        if (activity != null) {
            DebugTasks[] tasks = DebugTasks.values();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            CharSequence[] taskNames = new CharSequence[tasks.length];
            int i = 0;
            for (DebugTasks t : tasks) {
                taskNames[i] = t.mLabel;
                i++;
            }
            builder.setItems(taskNames, this);
            builder.create().show();
        }
        return true;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        Activity activity = getActivity();
        if (activity != null) {
            DebugTasks.values()[which].mTask.performTask(activity);
        }
        dialog.dismiss();
    }
}
