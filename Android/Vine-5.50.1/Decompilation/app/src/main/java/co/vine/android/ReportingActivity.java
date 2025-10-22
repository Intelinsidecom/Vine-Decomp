package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import co.vine.android.client.AppController;
import co.vine.android.util.CrashUtil;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class ReportingActivity extends BaseControllerActionBarActivity {
    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true);
        if (this.mAppController == null) {
            this.mAppController = AppController.getInstance(this);
            CrashUtil.logException(new VineLoggingException(), "App controller is null here, and now? " + this.mAppController, new Object[0]);
        }
        if (savedInstanceState == null) {
            ReportingFragment fragment = ReportingFragment.newFragment(getIntent());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, "reportingmenu").commit();
        }
    }

    public static Intent getReportCommentIntent(Context context, String commentId, long userId, String username, long postId) {
        Intent i = new Intent(context, (Class<?>) ReportingActivity.class);
        i.putExtra("menu_type", "comment");
        i.putExtra("commentId", commentId);
        i.putExtra("userId", userId);
        i.putExtra("postId", postId);
        i.putExtra("username", username);
        return i;
    }

    public static Intent getReportPostIntent(Context context, long postId, long userId, String username) {
        Intent i = new Intent(context, (Class<?>) ReportingActivity.class);
        i.putExtra("menu_type", "post");
        i.putExtra("postId", postId);
        i.putExtra("userId", userId);
        i.putExtra("username", username);
        return i;
    }

    public static Intent getReportUserIntent(Context context, long userId, String username, boolean isBlocking) {
        Intent i = new Intent(context, (Class<?>) ReportingActivity.class);
        i.putExtra("menu_type", PropertyConfiguration.USER);
        i.putExtra("userId", userId);
        i.putExtra("blockingUser", isBlocking);
        i.putExtra("username", username);
        return i;
    }
}
