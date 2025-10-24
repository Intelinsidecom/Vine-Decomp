package co.vine.android;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import co.vine.android.util.FileLogger;
import java.io.File;

/* loaded from: classes.dex */
public class ShowLogActivity extends BaseActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.debug_home, false, true);
        File file = new File(getIntent().getAction());
        final TextView tv = (TextView) findViewById(R.id.debuginfo);
        Button button = (Button) findViewById(R.id.writeSessionKeyToLog);
        button.setText("Clear Logs");
        findViewById(R.id.debug_query_parent).setVisibility(8);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Log " + file.getName());
        }
        final FileLogger logger = FileLogger.getLogger(file);
        while (true) {
            String line = logger.read();
            if (line != null) {
                tv.append(line);
                tv.append("\n");
            } else {
                button.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ShowLogActivity.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        logger.clear();
                        tv.setText("");
                    }
                });
                return;
            }
        }
    }
}
