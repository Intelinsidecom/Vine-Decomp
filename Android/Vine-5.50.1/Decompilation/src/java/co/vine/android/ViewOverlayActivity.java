package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public class ViewOverlayActivity extends BaseControllerActionBarActivity implements View.OnClickListener {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        ActionBar ab;
        Intent intent = getIntent();
        if (intent.getExtras() == null) {
            throw new IllegalStateException("Extras bundle must not be null; use this class's getIntent() helper.");
        }
        int layoutId = intent.getIntExtra("layout", 0);
        if (layoutId < 1) {
            throw new IllegalStateException("You must provide a layout id when starting this Activity");
        }
        super.onCreate(savedInstanceState, layoutId, true);
        if (Build.VERSION.SDK_INT <= 14 && (ab = getSupportActionBar()) != null) {
            ab.hide();
        }
        View cta = findViewById(intent.getIntExtra("call_to_action_id", 0));
        if (cta != null) {
            cta.setOnClickListener(this);
        }
        View skip = findViewById(intent.getIntExtra("skip_id", 0));
        if (skip != null) {
            skip.setOnClickListener(this);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.gingerbread_notif_button) {
            Util.getDefaultSharedPrefs(this).edit().putBoolean("pref_gb_notif_dismissed", true).apply();
            setResult(-1);
            finish();
        }
        if (id == R.id.skip) {
            setResult(0);
            finish();
        }
    }

    public static Intent getIntent(Context context, int layout, int callToActionId, int skipButtonId) {
        Intent intent = new Intent(context, (Class<?>) ViewOverlayActivity.class);
        intent.putExtra("layout", layout);
        intent.putExtra("call_to_action_id", callToActionId);
        intent.putExtra("skip_id", skipButtonId);
        return intent;
    }
}
