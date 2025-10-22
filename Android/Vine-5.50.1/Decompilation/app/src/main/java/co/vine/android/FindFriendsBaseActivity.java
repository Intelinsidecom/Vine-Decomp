package co.vine.android;

import android.content.ComponentCallbacks;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;
import co.vine.android.util.BuildUtil;
import co.vine.android.widget.OnTabChangedListener;
import co.vine.android.widget.tabs.IconTabHost;
import co.vine.android.widget.tabs.TabsAdapter;
import co.vine.android.widget.tabs.ViewPagerScrollBar;

/* loaded from: classes.dex */
public class FindFriendsBaseActivity extends BaseControllerActionBarActivity implements TabHost.OnTabChangeListener, IconTabHost.OnTabClickedListener {
    protected ViewPagerScrollBar mScrollBar;
    protected IconTabHost mTabHost;
    protected TabsAdapter mTabsAdapter;
    protected ViewPager mViewPager;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.main_find_friends, true);
        if (BuildUtil.isExplore()) {
            setupTabs();
        }
        this.mScrollBar = (ViewPagerScrollBar) findViewById(R.id.scrollbar);
        Intent intent = getIntent();
        if (intent != null) {
            if (!BuildUtil.isAmazon()) {
                this.mScrollBar.setColorIds(R.array.find_friends_colors);
            } else if (intent.getBooleanExtra("is_twitter_reg", false)) {
                this.mScrollBar.setColorIds(R.array.find_friends_colors_amazon);
            }
        }
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        this.mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.home_pager_margin));
        this.mViewPager.setPageMarginDrawable(R.color.bg_grouped_list);
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        String cur = state.getString("currentTab");
        if (cur != null) {
            setCurrentTabByTag(cur);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String cur = this.mTabHost.getCurrentTabTag();
        if (cur != null) {
            outState.putString("currentTab", cur);
        }
    }

    private void setupTabs() {
        this.mTabHost = (IconTabHost) findViewById(android.R.id.tabhost);
        if (this.mTabHost == null) {
            throw new RuntimeException("Your content must have a TabHost whose id attribute is 'android.R.id.tabhost'");
        }
        this.mTabHost.setup();
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
    }

    @Override // android.support.v7.app.AppCompatActivity
    public void onSupportContentChanged() {
        super.onSupportContentChanged();
        if (!BuildUtil.isExplore()) {
            setupTabs();
        }
    }

    public void onCurrentTabClicked() {
    }

    void setCurrentTabByTag(String tag) {
        TabHost tabHost = this.mTabHost;
        if (!tag.equals(tabHost.getCurrentTabTag())) {
            tabHost.setCurrentTabByTag(tag);
        }
        this.mScrollBar.setPosition(this.mViewPager.getCurrentItem());
    }

    @Override // android.widget.TabHost.OnTabChangeListener
    public void onTabChanged(String tag) throws Resources.NotFoundException {
        this.mViewPager.setCurrentItem(this.mTabHost.getCurrentTab());
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        int currentIndex = this.mTabHost.getCurrentTab();
        ComponentCallbacks componentCallbacksFragment = this.mTabsAdapter.getTabs().get(currentIndex).fragment();
        if (componentCallbacksFragment instanceof OnTabChangedListener) {
            ((OnTabChangedListener) componentCallbacksFragment).onMoveAway(currentIndex);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
            case 2:
                Fragment frag = this.mTabsAdapter.getTab(1).fragment();
                if (frag != null) {
                    frag.onActivityResult(requestCode, resultCode, data);
                    break;
                }
                break;
            default:
                Fragment frag2 = this.mTabsAdapter.getTab(2).fragment();
                if (frag2 != null) {
                    frag2.onActivityResult(requestCode, resultCode, data);
                    break;
                }
                break;
        }
    }
}
