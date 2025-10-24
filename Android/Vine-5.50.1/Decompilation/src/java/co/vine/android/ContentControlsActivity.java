package co.vine.android;

import android.os.Bundle;
import android.view.MenuItem;

/* loaded from: classes.dex */
public class ContentControlsActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true);
        setupActionBar((Boolean) true, (Boolean) true, R.string.content_controls_title, (Boolean) true, (Boolean) false);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getInt("color", 0) != 0) {
            setActionBarColor(extras.getInt("color"));
        }
        if (savedInstanceState == null) {
            ContentControlsFragment frag = new ContentControlsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, frag, "content_controls").commit();
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
