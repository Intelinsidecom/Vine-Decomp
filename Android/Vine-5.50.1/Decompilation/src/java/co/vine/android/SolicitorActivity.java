package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import co.vine.android.solicitor.SolicitorFragment;

/* loaded from: classes.dex */
public final class SolicitorActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().hide();
        }
        getIntent();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, SolicitorFragment.newInstance()).commit();
        }
    }

    public static Intent getIntent(Context context) {
        Intent i = new Intent(context, (Class<?>) SolicitorActivity.class);
        return i;
    }

    public static void start(Context context) {
        Intent i = getIntent(context);
        context.startActivity(i);
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }
}
