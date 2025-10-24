package co.vine.android.widget.tabs;

import android.content.ComponentCallbacks;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import co.vine.android.BaseArrayListFragment;
import co.vine.android.BaseControllerActionBarActivity;
import co.vine.android.BaseFragment;
import co.vine.android.widget.OnTabChangedListener;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class TabsAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private final BaseControllerActionBarActivity mActivity;
    private final ViewPagerScrollBar mScrollBar;
    private boolean mSetActionBarColorOnPageSelectedEnabled;
    private final DummyTabFactory mTabFactory;
    private final TabHost mTabHost;
    private final ArrayList<TabInfo> mTabs;
    private final ViewPager mViewPager;
    public int previousTab;

    public ArrayList<TabInfo> getTabs() {
        return this.mTabs;
    }

    public TabInfo getTab(int pos) {
        return this.mTabs.get(pos);
    }

    public TabsAdapter(BaseControllerActionBarActivity activity, TabHost tabHost, ViewPager pager, ViewPagerScrollBar scrollBar) throws Resources.NotFoundException {
        super(activity.getSupportFragmentManager());
        this.mTabs = new ArrayList<>();
        this.mSetActionBarColorOnPageSelectedEnabled = true;
        this.mActivity = activity;
        this.mTabHost = tabHost;
        this.mViewPager = pager;
        this.mViewPager.setAdapter(this);
        this.mViewPager.setOnPageChangeListener(this);
        this.mScrollBar = scrollBar;
        this.mTabFactory = new DummyTabFactory(this.mActivity);
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(clss, args, tabSpec.getTag());
        tabSpec.setContent(this.mTabFactory);
        this.mTabs.add(info);
        this.mTabHost.addTab(tabSpec);
        notifyDataSetChanged();
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return this.mTabs.size();
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getItemPosition(Object object) {
        boolean found = false;
        Iterator<TabInfo> it = this.mTabs.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            TabInfo info = it.next();
            if (info.fragment() == object) {
                found = true;
                break;
            }
        }
        return found ? -1 : -2;
    }

    public Fragment getCurrentFragment() {
        TabInfo tab = getTab(this.mTabHost.getCurrentTab());
        if (tab != null) {
            return tab.fragment();
        }
        return null;
    }

    @Override // android.support.v4.app.FragmentPagerAdapter, android.support.v4.view.PagerAdapter
    public Parcelable saveState() {
        return new TabSavedState(this.mTabs);
    }

    @Override // android.support.v4.app.FragmentPagerAdapter, android.support.v4.view.PagerAdapter
    public void restoreState(Parcelable state, ClassLoader loader) {
        TabSavedState savedState = (TabSavedState) state;
        String[] tags = savedState.tags;
        for (int i = 0; i < tags.length; i++) {
            String tag = tags[i];
            Fragment fragment = this.mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment != null) {
                this.mTabs.get(i).setFragment(fragment);
                if (fragment instanceof BaseArrayListFragment) {
                    ((BaseArrayListFragment) fragment).setScrollListener(new TabScrollListener(this.mActivity, i));
                }
            }
        }
    }

    @Override // android.support.v4.app.FragmentPagerAdapter
    public Fragment getItem(int position) {
        return Fragment.instantiate(this.mActivity, this.mTabs.get(position).clss.getName(), this.mTabs.get(position).bundle);
    }

    @Override // android.support.v4.app.FragmentPagerAdapter, android.support.v4.view.PagerAdapter
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment blf = (Fragment) super.instantiateItem(container, position);
        this.mTabs.get(position).setFragment(blf);
        if (blf instanceof BaseArrayListFragment) {
            ((BaseArrayListFragment) blf).setScrollListener(new TabScrollListener(this.mActivity, position));
        }
        return blf;
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (this.mViewPager.getCurrentItem() < position && this.mScrollBar != null) {
            this.mScrollBar.scroll(position, -positionOffsetPixels);
        } else if (this.mScrollBar != null) {
            this.mScrollBar.scroll(position, positionOffsetPixels);
        }
    }

    public void onPageSelected(int position) {
        TabWidget widget = this.mTabHost.getTabWidget();
        int descendantFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(393216);
        if (position != this.previousTab) {
            ((IconTabHost) this.mTabHost).setTabExplicit(position);
            ComponentCallbacks componentCallbacksFragment = this.mTabs.get(this.previousTab).fragment();
            if (componentCallbacksFragment != null && (componentCallbacksFragment instanceof OnTabChangedListener)) {
                ((OnTabChangedListener) componentCallbacksFragment).onMoveAway(position);
            }
            ComponentCallbacks componentCallbacksFragment2 = this.mTabs.get(position).fragment();
            if (componentCallbacksFragment2 != null && (componentCallbacksFragment2 instanceof OnTabChangedListener)) {
                ((OnTabChangedListener) componentCallbacksFragment2).onMoveTo(this.previousTab);
            }
            if (componentCallbacksFragment2 != null && this.mSetActionBarColorOnPageSelectedEnabled) {
                ((BaseFragment) componentCallbacksFragment2).setActionBarColor();
            }
        }
        this.previousTab = this.mTabHost.getCurrentTab();
        if (this.mScrollBar != null) {
            this.mScrollBar.setPageColor(position);
        }
        widget.setDescendantFocusability(descendantFocusability);
    }

    public void enableSetActionBarColorOnPageSelected(boolean enable) {
        this.mSetActionBarColorOnPageSelectedEnabled = enable;
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int state) {
    }

    public TabIndicator getIndicator(int tabIndex) {
        TabWidget widget = this.mTabHost.getTabWidget();
        if (widget != null) {
            return (TabIndicator) widget.getChildTabViewAt(tabIndex);
        }
        return null;
    }

    public void setNew(int tabIndex, boolean isNew) {
        TabIndicator indicator = getIndicator(tabIndex);
        if (indicator != null) {
            indicator.setNew(isNew);
        }
    }

    public ImageView getNewIndicatorForTab(int tabIndex) {
        TabIndicator indicator = getIndicator(tabIndex);
        if (indicator != null) {
            return indicator.getNewIndicator();
        }
        return null;
    }

    public void setSetActionBarColorOnPageSelectedEnabled(boolean setActionBarColorOnPageSelectedEnabled) {
        this.mSetActionBarColorOnPageSelectedEnabled = setActionBarColorOnPageSelectedEnabled;
    }
}
