package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import co.vine.android.client.AppSessionListener;
import co.vine.android.util.BuildUtil;
import co.vine.android.widget.tabs.IconTabHost;
import co.vine.android.widget.tabs.TabIndicator;
import co.vine.android.widget.tabs.TabsAdapter;

/* loaded from: classes.dex */
public class FindFriendsActivity extends FindFriendsBaseActivity implements IconTabHost.OnTabClickedListener {
    @Override // co.vine.android.FindFriendsBaseActivity, co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onCreate(savedInstanceState);
        setupActionBar((Boolean) true, (Boolean) true, R.string.find_friends, (Boolean) true, (Boolean) false);
        this.mAppSessionListener = new FindFriendsListener();
        IconTabHost tabHost = this.mTabHost;
        if (!BuildUtil.isAmazon()) {
            this.mScrollBar.setRange(4);
            this.mViewPager.setOffscreenPageLimit(3);
        } else {
            this.mScrollBar.setRange(2);
            this.mViewPager.setOffscreenPageLimit(1);
        }
        this.mTabsAdapter = new TabsAdapter(this, tabHost, this.mViewPager, this.mScrollBar);
        LayoutInflater inflater = LayoutInflater.from(this);
        tabHost.setOnTabChangedListener(this);
        tabHost.setOnTabClickedListener(this);
        Bundle suggestedBundle = new Bundle();
        suggestedBundle.putInt("layout", R.layout.find_friends_suggested_list_fragment);
        this.mTabsAdapter.addTab(tabHost.newTabSpec("suggested").setIndicator(TabIndicator.newIconIndicator(inflater, R.layout.find_friends_tab_indicator, tabHost, R.drawable.find_friends_suggested, 0)), FindFriendsSuggestedFragment.class, suggestedBundle);
        if (!BuildUtil.isAmazon()) {
            Bundle twitterBundle = new Bundle();
            twitterBundle.putInt("empty_desc", R.string.find_friends_twitter_no_friends);
            this.mTabsAdapter.addTab(tabHost.newTabSpec("twitter").setIndicator(TabIndicator.newIconIndicator(inflater, R.layout.find_friends_tab_indicator, tabHost, R.drawable.find_friends_twitter, 0)), FindFriendsTwitterFragment.class, twitterBundle);
            Bundle facebookBundle = new Bundle();
            facebookBundle.putInt("empty_desc", R.string.find_friends_facebook_no_friends);
            this.mTabsAdapter.addTab(tabHost.newTabSpec("facebook").setIndicator(TabIndicator.newIconIndicator(inflater, R.layout.find_friends_tab_indicator, tabHost, R.drawable.find_friends_facebook, 0)), FindFriendsFacebookFragment.class, facebookBundle);
        }
        Bundle addressBundle = new Bundle();
        addressBundle.putInt("empty_desc", R.string.find_friends_address_no_friends);
        this.mTabsAdapter.addTab(tabHost.newTabSpec("contacts").setIndicator(TabIndicator.newIconIndicator(inflater, R.layout.find_friends_tab_indicator, tabHost, R.drawable.find_friends_address, 0)), FindFriendsAddressFragment.class, addressBundle);
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("find_friends_tag") != null) {
            setCurrentTabByTag(intent.getStringExtra("find_friends_tag"));
        }
    }

    @Override // co.vine.android.FindFriendsBaseActivity, co.vine.android.widget.tabs.IconTabHost.OnTabClickedListener
    public void onCurrentTabClicked() {
    }

    @Override // co.vine.android.FindFriendsBaseActivity
    void setCurrentTabByTag(String tag) {
        TabHost tabHost = this.mTabHost;
        if (!tag.equals(tabHost.getCurrentTabTag())) {
            tabHost.setCurrentTabByTag(tag);
        }
        this.mScrollBar.setPosition(this.mViewPager.getCurrentItem());
    }

    private class FindFriendsListener extends AppSessionListener {
        private FindFriendsListener() {
        }
    }

    public static void start(Context context, String tag) {
        Intent intent = new Intent(context, (Class<?>) FindFriendsActivity.class);
        intent.putExtra("find_friends_tag", tag);
        context.startActivity(intent);
    }
}
