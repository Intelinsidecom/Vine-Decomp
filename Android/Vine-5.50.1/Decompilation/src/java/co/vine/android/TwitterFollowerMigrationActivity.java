package co.vine.android;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

/* loaded from: classes.dex */
public class TwitterFollowerMigrationActivity extends UsersActivity {
    @Override // co.vine.android.UsersActivity, co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.follow_on_twitter_title);
            ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));
        }
        this.mFragment = new TwitterFollowerMigrationFragment();
        Bundle b = TwitterFollowerMigrationFragment.prepareArguments(getIntent(), false);
        b.putBoolean("refresh", true);
        this.mFragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, this.mFragment).commit();
    }
}
