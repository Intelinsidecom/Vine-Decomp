package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import co.vine.android.nux.NuxResolver;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.UIEventScribeLogger;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.widget.FakeActionBar;
import java.util.List;

/* loaded from: classes.dex */
public class ExploreVideoListActivity extends BaseTimelineActivity {
    private Integer mActionBarBackIconResId;
    private Integer mActionBarColor;
    private Integer mActionBarTitleColor;
    private boolean mShowNextButton;

    @Override // co.vine.android.BaseTimelineActivity
    protected BaseTimelineFragment getCurrentTimeLineFragment() {
        return (BaseTimelineFragment) getSupportFragmentManager().findFragmentByTag("explore_video_list");
    }

    @Override // co.vine.android.BaseActionBarActivity
    public boolean isFakeActionBarEnabled() {
        return true;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, false);
        Intent intent = getIntent();
        Uri data = getIntent().getData();
        if (data == null) {
            throw new IllegalAccessError("You can't access video list without specifying a url.");
        }
        setupActionBar((Boolean) true, (Boolean) true, (String) null, (Boolean) true, (Boolean) false);
        this.mShowCaptureIcon = false;
        String prefix = data.getHost();
        String scheme = data.getScheme();
        if ("popular-now".equals(intent.getStringExtra("channel_type")) && ClientFlagsHelper.isPopularTabsEnabled(this)) {
            LinearLayout popularTabs = (LinearLayout) findViewById(R.id.popular_tabs);
            View horizontalDivider = findViewById(R.id.horizontal_divider);
            popularTabs.setVisibility(0);
            horizontalDivider.setVisibility(0);
        }
        if (prefix.contains("vine.co") && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))) {
            List<String> pathSegments = data.getPathSegments();
            if (!pathSegments.isEmpty()) {
                prefix = pathSegments.get(0);
            }
        }
        initializeActionBar(data);
        if (savedInstanceState == null) {
            ExploreTimelineFragment fragment = new ExploreTimelineFragment();
            intent.putExtra("refresh", true);
            fragment.setArguments(ExploreTimelineFragment.prepareArguments(intent, true, prefix, data.getLastPathSegment(), data));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, "explore_video_list").commit();
        }
    }

    @Override // co.vine.android.BaseActionBarActivity
    protected void applyDefaultTitleViewStylingToFakeActionBar() {
        super.applyDefaultTitleViewStylingToFakeActionBar();
        FakeActionBar fakeActionBar = getFakeActionBar();
        if (this.mActionBarTitleColor != null) {
            fakeActionBar.getTitleView().setTextColor(this.mActionBarTitleColor.intValue());
        }
        if (this.mActionBarBackIconResId != null) {
            fakeActionBar.getBackIcon().setImageResource(this.mActionBarBackIconResId.intValue());
        }
        if (this.mActionBarColor != null) {
            fakeActionBar.setActionBarColor(this.mActionBarColor.intValue());
        }
        fakeActionBar.getActionBarRight().setVisibility(4);
    }

    private void initializeActionBar(Uri data) {
        String prefix = data.getHost();
        FakeActionBar ab = getFakeActionBar();
        Resources res = getResources();
        ab.setActionBarColor(res.getColor(R.color.vine_green));
        this.mActionBarTitleColor = Integer.valueOf(res.getColor(R.color.solid_white));
        this.mActionBarBackIconResId = Integer.valueOf(R.drawable.ic_back_arrow);
        this.mActionBarColor = Integer.valueOf(res.getColor(R.color.vine_green));
        this.mShowNextButton = false;
        if ("trending-people".equals(prefix)) {
            ab.setTitle(Integer.valueOf(R.string.on_the_rise));
            return;
        }
        if ("popular-now".equals(prefix)) {
            ab.setTitle(Integer.valueOf(R.string.popular_now));
            return;
        }
        if ("welcome-feed".equals(prefix)) {
            setupActionBar((Boolean) false, (Boolean) true, (String) null, (Boolean) false, (Boolean) false);
            this.mActionBarColor = Integer.valueOf(res.getColor(R.color.solid_black));
            this.mShowNextButton = true;
            ab.setTitle(Integer.valueOf(R.string.nux_welcome_feed));
            return;
        }
        if ("timelines".equals(prefix) || "venue".equals(prefix)) {
            String title = data.getQueryParameter("title");
            setActionBarTitle(ab, title);
            List<String> pathSegments = data.getPathSegments();
            if (pathSegments.contains("similar")) {
                ab.setTitle(Integer.valueOf(R.string.similar_vines));
                this.mActionBarColor = Integer.valueOf(res.getColor(R.color.vine_black));
                return;
            } else if (pathSegments.contains("remixes")) {
                ab.setTitle(Integer.valueOf(R.string.remixes));
                this.mActionBarColor = Integer.valueOf(res.getColor(R.color.vine_black));
                return;
            } else {
                if (pathSegments.contains("suggested")) {
                    ab.setTitle(Integer.valueOf(R.string.suggested_feed_title));
                    return;
                }
                return;
            }
        }
        if ("post-search-top".equals(prefix) || "post-search-recent".equals(prefix)) {
            setActionBarTitle(ab, getString(R.string.posts));
        } else {
            finish();
        }
    }

    private void setActionBarTitle(FakeActionBar ab, String title) {
        if (!TextUtils.isEmpty(title)) {
            ab.setTitle(title.replace('+', ' '));
        } else {
            ab.setTitle(getString(R.string.tab_title_timeline));
        }
    }

    public static Intent getIntent(Context context, String category, String item) {
        return new Intent(context, (Class<?>) ExploreVideoListActivity.class).setData(Uri.parse("vine://" + category + (item != null ? "/" + item : "")));
    }

    public static void start(Context context, Uri arbitraryTimelineUri) {
        Intent intent = new Intent(context, (Class<?>) ExploreVideoListActivity.class);
        intent.setData(arbitraryTimelineUri);
        context.startActivity(intent);
    }

    @Override // co.vine.android.BaseTimelineActivity, co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mShowNextButton) {
            getMenuInflater().inflate(R.menu.nux_done, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id != R.id.done) {
            return super.onOptionsItemSelected(item);
        }
        Context appContext = getApplicationContext();
        UIEventScribeLogger.onSkipWelcomeFeed(ScribeLoggerSingleton.getInstance(appContext), AppStateProviderSingleton.getInstance(appContext), AppNavigationProviderSingleton.getInstance());
        NuxResolver.toNuxFromWelcomeFeed(this);
        return true;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("explore_video_list");
        if (fragment instanceof BaseTimelineFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
