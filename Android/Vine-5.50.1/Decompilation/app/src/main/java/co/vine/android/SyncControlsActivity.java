package co.vine.android;

import android.os.Bundle;
import android.view.MenuItem;

/* loaded from: classes.dex */
public class SyncControlsActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true);
        setupActionBar((Boolean) true, (Boolean) true, R.string.sync, (Boolean) true, (Boolean) false);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getInt("color", 0) != 0) {
            setActionBarColor(extras.getInt("color"));
        }
        if (savedInstanceState == null) {
            SyncControlsFragment frag = new SyncControlsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, frag, "sync_controls").commit();
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
