package co.vine.android;

import android.os.Bundle;

/* loaded from: classes.dex */
public class TumblrLoginActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true);
        setupActionBar((Boolean) true, (Boolean) true, R.string.tumblr, (Boolean) true, (Boolean) false);
        setActionBarColor(getResources().getColor(R.color.tumblr_navy));
        if (savedInstanceState == null) {
            TumblrLoginFragment fragment = new TumblrLoginFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
