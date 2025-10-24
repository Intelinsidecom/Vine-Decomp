package co.vine.android;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import co.vine.android.client.Session;
import co.vine.android.embed.player.VideoViewHelper;
import co.vine.android.util.AudioUtils;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.DebugUtil;
import co.vine.android.util.Util;
import java.io.IOException;

/* loaded from: classes.dex */
public class DebugHomeActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws PackageManager.NameNotFoundException {
        super.onCreate(savedInstanceState, R.layout.debug_home, true);
        final Session session = this.mAppController.getActiveSession();
        TextView tv = (TextView) findViewById(R.id.debuginfo);
        Button b = (Button) findViewById(R.id.writeSessionKeyToLog);
        if (BuildUtil.isLogsOn()) {
            b.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.DebugHomeActivity.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Log.d("VINEDEBUG", "vine-session-id: " + session.getSessionKey());
                }
            });
        } else {
            b.setVisibility(8);
        }
        setupActionBar((Boolean) true, (Boolean) true, (String) null, (Boolean) true, (Boolean) false);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Debug Screen");
        }
        final String debugInfo = DebugUtil.generateDebugInfo(this);
        tv.setText(debugInfo);
        Button button = new Button(this);
        if (BuildUtil.isLogsOn()) {
            button.setText("Send Logs");
            button.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.DebugHomeActivity.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) throws PackageManager.NameNotFoundException, IOException {
                    DebugUtil.sendBugReport(DebugHomeActivity.this, debugInfo);
                }
            });
        } else {
            button.setText("Cause Crash");
            button.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.DebugHomeActivity.3
                int i = 0;

                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    this.i++;
                    if (this.i >= 3) {
                        throw new RuntimeException(new VineLoggingException());
                    }
                }
            });
        }
        ((ViewGroup) tv.getParent()).addView(button);
        if (BuildUtil.isLogsOn()) {
            final EditText query = (EditText) findViewById(R.id.debug_query);
            findViewById(R.id.debug_query_submit).setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.DebugHomeActivity.4
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    DebugHomeActivity.this.handleDebugQuery(query.getText().toString());
                }
            });
        } else {
            LinearLayout debugQueryParent = (LinearLayout) findViewById(R.id.debug_query_parent);
            debugQueryParent.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDebugQuery(String query) {
        if (query != null) {
            String query2 = query.toLowerCase();
            if (query2.startsWith("p:")) {
                SingleActivity.start(this, Long.parseLong(query2.substring(2)));
            }
            if (query2.startsWith("s:")) {
                SingleActivity.start(this, query2.substring(2));
            }
            if (query2.startsWith("u:")) {
                ChannelActivity.startProfile(this, Long.parseLong(query2.substring(2)), "Debug Screen");
            }
            if (query2.startsWith("a:")) {
                if (query2.length() > 2) {
                    AudioUtils.setArtificialLatency(Long.parseLong(query2.substring(2)) * 1000);
                }
                Util.showCenteredToast(this, "Audio Latency: " + AudioUtils.getArtificialLatency());
            }
            if (query2.startsWith("turn on vine player")) {
                VideoViewHelper.setGlobalUseVineVideoView(true);
                Util.showCenteredToast(this, "Vine Player Turned on.");
            }
            if (query2.startsWith("turn off vine player")) {
                VideoViewHelper.setGlobalUseVineVideoView(false);
                Util.showCenteredToast(this, "Vine Player Turned off.");
            }
        }
    }
}
