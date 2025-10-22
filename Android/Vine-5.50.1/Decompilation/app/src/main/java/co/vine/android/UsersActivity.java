package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class UsersActivity extends BaseControllerActionBarActivity {
    protected UsersFragment mFragment;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.user_list, true);
        Intent intent = getIntent();
        setupActionBar((Boolean) true, (Boolean) true, (String) null, (Boolean) true, (Boolean) false);
        ActionBar ab = getSupportActionBar();
        int type = intent.getIntExtra("u_type", 0);
        switch (type) {
            case 1:
                ab.setTitle(R.string.user_following);
                break;
            case 2:
                ab.setTitle(R.string.user_followers);
                break;
            case 5:
                ab.setTitle(R.string.user_likers);
                break;
            case 8:
                String query = intent.getStringExtra("q");
                String title = !TextUtils.isEmpty(query) ? query : getString(R.string.search);
                ab.setTitle(title);
                ab.setSubtitle(getString(R.string.people));
                ab.setIcon((Drawable) null);
                break;
            case 9:
                ab.setTitle(R.string.revines_title);
                break;
            case 12:
                ab.setTitle("");
                break;
        }
        if (savedInstanceState == null && type != 14) {
            this.mFragment = new UsersFragment();
            Bundle b = UsersFragment.prepareArguments(intent, false);
            b.putBoolean("refresh", true);
            this.mFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, this.mFragment).commit();
        }
    }

    public static void startUserListForNotification(Context context, long notificationId, String anchor) {
        Intent i = new Intent(context, (Class<?>) UsersActivity.class);
        i.putExtra("u_type", 12);
        i.putExtra("notification_id", notificationId);
        i.putExtra("anchor", anchor);
        context.startActivity(i);
    }

    public static void startUserListForReviners(Context context, long postId) {
        Intent i = new Intent(context, (Class<?>) UsersActivity.class);
        i.putExtra("post_id", postId);
        i.putExtra("u_type", 9);
        context.startActivity(i);
    }

    public static void startUserListForLikers(Context context, long postId) {
        Intent i = new Intent(context, (Class<?>) UsersActivity.class);
        i.putExtra("post_id", postId);
        i.putExtra("u_type", 5);
        context.startActivity(i);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mFragment.onActivityResult(requestCode, resultCode, data);
    }

    public static void startUserListForSearchQuery(Context context, String searchQuery) {
        Intent i = new Intent(context, (Class<?>) UsersActivity.class);
        i.putExtra("q", searchQuery);
        i.putExtra("u_type", 8);
        context.startActivity(i);
    }
}
