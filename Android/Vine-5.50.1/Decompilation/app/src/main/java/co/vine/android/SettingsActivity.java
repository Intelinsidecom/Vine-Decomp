package co.vine.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

/* loaded from: classes.dex */
public class SettingsActivity extends BaseControllerActionBarActivity {
    private SettingsFragment mFrag;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true);
        setupActionBar((Boolean) true, (Boolean) true, R.string.settings, (Boolean) true, (Boolean) false);
        if (savedInstanceState == null) {
            SettingsFragment fragment = new SettingsFragment();
            fragment.setArguments(BaseTimelineFragment.prepareArguments(getIntent()));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, "settings").commit();
            this.mFrag = fragment;
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (this.mFrag == null) {
                    this.mFrag = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("settings");
                }
                if (this.mFrag != null) {
                    this.mFrag.onBackPressed();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.mFrag == null) {
            this.mFrag = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("settings");
        }
        if (this.mFrag != null) {
            this.mFrag.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) throws PackageManager.NameNotFoundException {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mFrag == null) {
            this.mFrag = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("settings");
        }
        if (this.mFrag != null) {
            this.mFrag.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
