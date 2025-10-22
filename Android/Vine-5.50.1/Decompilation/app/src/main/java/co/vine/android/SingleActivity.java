package co.vine.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

/* loaded from: classes.dex */
public class SingleActivity extends BaseTimelineActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, false);
        setRequestedOrientation(1);
        setupActionBar((Boolean) true, (Boolean) true, R.string.timeline_post_title, (Boolean) true, (Boolean) true);
        Intent intent = getIntent();
        if (savedInstanceState == null) {
            Fragment fragment = new SingleFragment();
            intent.putExtra("take_focus", true);
            fragment.setArguments(BaseArrayListFragment.prepareArguments(intent, false));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, "single_post").commit();
        }
    }

    @Override // co.vine.android.BaseTimelineActivity
    protected BaseTimelineFragment getCurrentTimeLineFragment() {
        return (BaseTimelineFragment) getSupportFragmentManager().findFragmentByTag("single_post");
    }

    @Override // co.vine.android.BaseTimelineActivity, co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mute, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static Intent getIntent(Context context, long postId) {
        Intent i = new Intent(context, (Class<?>) SingleActivity.class);
        i.putExtra("post_id", postId);
        return i;
    }

    public static void start(Context context, long postId) {
        context.startActivity(getIntent(context, postId));
    }

    public static void start(Context context, String shareId) {
        Intent i = new Intent(context, (Class<?>) SingleActivity.class);
        i.putExtra("post_share_id", shareId);
        context.startActivity(i);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("single_post");
        if (fragment instanceof BaseTimelineFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
