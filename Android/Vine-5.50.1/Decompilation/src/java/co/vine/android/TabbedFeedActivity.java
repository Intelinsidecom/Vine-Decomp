package co.vine.android;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.widget.TabHost;
import co.vine.android.widget.tabs.IconTabHost;
import co.vine.android.widget.tabs.ScrollAwayTabWidget;
import co.vine.android.widget.tabs.TabIndicator;
import co.vine.android.widget.tabs.TabsAdapter;
import co.vine.android.widget.tabs.ViewPagerScrollBar;

/* loaded from: classes.dex */
public class TabbedFeedActivity extends BaseControllerActionBarActivity implements TabbedActivity {
    private TabHost.OnTabChangeListener mOnTabChangeListener = new TabHost.OnTabChangeListener() { // from class: co.vine.android.TabbedFeedActivity.1
        @Override // android.widget.TabHost.OnTabChangeListener
        public void onTabChanged(String s) throws Resources.NotFoundException {
            TabbedFeedActivity.this.mViewPager.setCurrentItem(TabbedFeedActivity.this.mTabHost.getCurrentTab());
        }
    };
    private ViewPagerScrollBar mScrollBar;
    private IconTabHost mTabHost;
    private TabsAdapter mTabsAdapter;
    private ViewPager mViewPager;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onCreate(savedInstanceState, R.layout.tabbed_search_results, false);
        Intent data = getIntent();
        setupActionBar(true, true, getIntent().getStringExtra("title"), true, false, Boolean.valueOf(data.getBooleanExtra("show_subtitle", true)));
        this.mTabHost = (IconTabHost) findViewById(android.R.id.tabhost);
        if (this.mTabHost == null) {
            throw new RuntimeException("Your content must have a TabHost whose id attribute is 'android.R.id.tabhost'");
        }
        this.mTabHost.setup();
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerScrollBar scrollBar = (ViewPagerScrollBar) findViewById(R.id.scrollbar);
        scrollBar.setLineColor(getResources().getColor(R.color.vine_green));
        scrollBar.setRange(2);
        scrollBar.setVisibility(0);
        this.mScrollBar = scrollBar;
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        this.mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.home_pager_margin));
        this.mViewPager.setPageMarginDrawable(R.color.bg_grouped_list);
        this.mViewPager.setOffscreenPageLimit(1);
        IconTabHost tabHost = this.mTabHost;
        TabsAdapter tabsAdapter = new TabsAdapter(this, tabHost, this.mViewPager, scrollBar);
        tabsAdapter.setSetActionBarColorOnPageSelectedEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        tabHost.setOnTabChangedListener(this.mOnTabChangeListener);
        Bundle tabBundle1 = data.getBundleExtra("tab_bundle_1");
        String tabTag = tabBundle1.getString("category");
        TabHost.TabSpec popularTabSpec = tabHost.newTabSpec(tabTag);
        popularTabSpec.setIndicator(TabIndicator.newTextIndicator(inflater, R.layout.tag_search_tab_indicator, tabHost, R.string.top, true));
        tabsAdapter.addTab(popularTabSpec, ExploreTimelineTabbedFragment.class, tabBundle1);
        Bundle tabBundle2 = data.getBundleExtra("tab_bundle_2");
        String tabTag2 = tabBundle2.getString("category");
        TabHost.TabSpec recentTabSpec = tabHost.newTabSpec(tabTag2);
        recentTabSpec.setIndicator(TabIndicator.newTextIndicator(inflater, R.layout.tag_search_tab_indicator, tabHost, R.string.tab_title_recent, true));
        tabsAdapter.addTab(recentTabSpec, ExploreTimelineTabbedFragment.class, tabBundle2);
        this.mTabsAdapter = tabsAdapter;
    }

    protected void setupActionBar(Boolean setHomeButtonEnabled, Boolean setDisplayShowTitleEnabled, String title, Boolean setDisplayHomeAsUpEnabled, Boolean setDisplayLogoEnabled, Boolean showSubtitle) {
        super.setupActionBar(setHomeButtonEnabled, setDisplayShowTitleEnabled, title, setDisplayHomeAsUpEnabled, setDisplayLogoEnabled);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && showSubtitle.booleanValue()) {
            actionBar.setSubtitle(getString(R.string.posts));
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = this.mTabsAdapter.getCurrentFragment();
        if (fragment instanceof BaseTimelineFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.ScrollListener
    public void onScroll(int delta) {
        ScrollAwayTabWidget tabWidget = (ScrollAwayTabWidget) this.mTabHost.getTabWidget();
        tabWidget.onScroll(delta);
        Fragment fragment = this.mTabsAdapter.getCurrentFragment();
        if (fragment instanceof BaseArrayListFragment) {
            int navBottom = tabWidget.getNavBottom();
            ((BaseArrayListFragment) fragment).setNavBottom(navBottom);
            this.mScrollBar.setNavOffset(navBottom);
        }
    }

    @Override // co.vine.android.TabbedActivity
    public boolean hasScrollAwayTabs() {
        return true;
    }

    @Override // co.vine.android.TabbedActivity
    public boolean isTabVisible(String tag) {
        return tag != null && tag.equals(this.mTabHost.getCurrentTabTag());
    }

    public void resetNav() {
        ScrollAwayTabWidget widget;
        Fragment fragment;
        if (this.mTabHost != null && (widget = (ScrollAwayTabWidget) this.mTabHost.getTabWidget()) != null) {
            widget.resetScroll();
            widget.invalidate();
            int navBottom = widget.getNavBottom();
            if (this.mTabsAdapter != null && (fragment = this.mTabsAdapter.getCurrentFragment()) != null) {
                ((BaseArrayListFragment) fragment).setNavBottom(navBottom);
                this.mScrollBar.setNavOffset(navBottom);
            }
        }
    }
}
