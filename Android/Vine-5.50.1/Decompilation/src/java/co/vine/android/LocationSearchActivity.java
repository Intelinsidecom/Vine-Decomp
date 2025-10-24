package co.vine.android;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;

/* loaded from: classes.dex */
public class LocationSearchActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState, R.layout.activity_location_search, true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().hide();
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.location_search_box, new LocationSearchFragment()).commit();
        }
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }
}
