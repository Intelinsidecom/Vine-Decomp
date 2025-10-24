package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.UIEventScribeLogger;
import co.vine.android.util.SmartOnGestureListener;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class TheaterActivity extends FragmentActivity {
    private TheaterFragment mFragment;

    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.fragment_layout);
        long id = intent.getLongExtra("id", -1L);
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");
        TimelineDetails timelineDetails = (TimelineDetails) intent.getParcelableExtra("timeline_details");
        this.mFragment = TheaterFragment.newInstance(timelineDetails, id, title, url);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, this.mFragment, "theater").commit();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                this.mFragment.onActivityResult(requestCode, resultCode, data);
                break;
            case HttpResponseCode.OK /* 200 */:
                this.mFragment.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        Context appContext = getApplicationContext();
        UIEventScribeLogger.onTheaterExit(ScribeLoggerSingleton.getInstance(appContext), AppStateProviderSingleton.getInstance(appContext), AppNavigationProviderSingleton.getInstance(), false);
        finish(SmartOnGestureListener.Direction.NONE, this.mFragment.getCurrentId());
    }

    public void finish(SmartOnGestureListener.Direction direction, long id) {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        setResult(-1, intent);
        super.finish();
        if (direction == SmartOnGestureListener.Direction.DOWN) {
            overridePendingTransition(R.anim.activity_fade_in_enter, R.anim.activity_dismiss_bottom);
        } else if (direction == SmartOnGestureListener.Direction.UP) {
            overridePendingTransition(R.anim.activity_fade_in_enter, R.anim.activity_dismiss_top);
        } else {
            overridePendingTransition(R.anim.activity_fade_in_enter, R.anim.activity_fade_in_exit);
        }
    }

    public static Intent getIntent(Context context, TimelineDetails timelineDetails, long id, String title, String url) {
        Intent i = new Intent(context, (Class<?>) TheaterActivity.class);
        i.putExtra("timeline_details", timelineDetails);
        i.putExtra("id", id);
        i.putExtra("title", title);
        i.putExtra("url", url);
        return i;
    }
}
