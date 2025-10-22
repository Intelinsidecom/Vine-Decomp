package co.vine.android.plugin;

import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.MenuItem;
import co.vine.android.util.DebugUtil;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ReportBugPlugin extends BasePlugin {
    public ReportBugPlugin() {
        super("Report a bug");
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("Report a bug");
        return true;
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public boolean onOptionsItemSelected(MenuItem item) throws PackageManager.NameNotFoundException, IOException {
        if (!"Report a bug".equals(item.getTitle())) {
            return false;
        }
        DebugUtil.sendBugReport(getActivity());
        return true;
    }
}
