package co.vine.android;

import android.app.ProgressDialog;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import co.vine.android.api.VineUser;
import co.vine.android.nux.NuxResolver;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.tabs.IconTabHost;
import co.vine.android.widget.tabs.TabIndicator;
import co.vine.android.widget.tabs.TabsAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class FindFriendsNUXActivity extends FindFriendsBaseActivity implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener, IconTabHost.OnTabClickedListener {
    private ImageButton mClear;
    private boolean mIsTwitterReg;
    private EditText mQuery;
    private MenuItem mSearch;
    private HashSet<String> mUserIdsToFollow;

    public interface FilterableFriendsList {
        void onTextChanged(CharSequence charSequence, int i, int i2, int i3);

        boolean shouldShowSearchIcon();
    }

    @Override // co.vine.android.FindFriendsBaseActivity, co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onCreate(savedInstanceState);
        setupActionBar((Boolean) false, (Boolean) true, 0, (Boolean) false, (Boolean) false);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowCustomEnabled(true);
            ab.setCustomView(R.layout.search_action_box);
            ViewGroup customView = (ViewGroup) ab.getCustomView();
            TextView title = (TextView) customView.findViewById(R.id.action_bar_title);
            title.setText(R.string.find_friends);
            EditText query = (EditText) customView.findViewById(R.id.query);
            ViewUtil.reduceTextSizeViaFontScaleIfNeeded(this, 1.1f, 12.0f, query);
            query.setOnEditorActionListener(this);
            query.addTextChangedListener(this);
            query.setInputType(524288);
            query.setPadding(0, 0, 0, 0);
            this.mQuery = query;
            ImageButton clear = (ImageButton) customView.findViewById(R.id.clear);
            clear.setOnClickListener(this);
            this.mClear = clear;
            showSearchBar(false);
        }
        Components.userInteractionsComponent().addListener(new FindFriendsNUXListener());
        IconTabHost tabHost = this.mTabHost;
        this.mScrollBar.setRange(3);
        this.mViewPager.setOffscreenPageLimit(2);
        this.mTabsAdapter = new TabsAdapter(this, tabHost, this.mViewPager, this.mScrollBar);
        this.mTabsAdapter.setSetActionBarColorOnPageSelectedEnabled(false);
        Intent intent = getIntent();
        if (intent != null) {
            this.mIsTwitterReg = intent.getBooleanExtra("is_twitter_reg", false);
        }
        tabHost.setOnTabChangedListener(this);
        tabHost.setOnTabClickedListener(this);
        if (this.mIsTwitterReg) {
            addTwitterTab();
            addContactsTab();
            addFacebookTab();
        } else {
            this.mScrollBar.setColorIds(R.array.find_friends_nux_colors);
            addContactsTab();
            addTwitterTab();
            addFacebookTab();
        }
        this.mUserIdsToFollow = new HashSet<>();
        FlurryUtils.trackNuxScreenDisplayed("discover_friends");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v.getId() == R.id.clear) {
            clearSearch();
        }
    }

    public void clearSearch() {
        this.mQuery.setText("");
        this.mClear.setVisibility(8);
        this.mQuery.clearFocus();
        showSearchBar(false);
        Util.setSoftKeyboardVisibility(this, this.mQuery, false);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (this.mViewPager.getCurrentItem()) {
            case 2:
                getMenuInflater().inflate(R.menu.nux_done, menu);
                break;
            default:
                getMenuInflater().inflate(R.menu.nux_next, menu);
                break;
        }
        MenuItem search = menu.findItem(R.id.search);
        ComponentCallbacks currentFragment = this.mTabsAdapter.getCurrentFragment();
        if ((currentFragment instanceof FilterableFriendsList) && search != null) {
            search.setVisible(((FilterableFriendsList) currentFragment).shouldShowSearchIcon());
        }
        this.mSearch = search;
        return true;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) throws Resources.NotFoundException {
        int id = item.getItemId();
        if (id == R.id.search) {
            item.setVisible(false);
            showSearchBar(true);
            this.mClear.setVisibility(0);
            this.mQuery.requestFocus();
            Util.setSoftKeyboardVisibility(this, this.mQuery, true);
        } else if (id == R.id.next) {
            this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() + 1);
        } else if (!this.mUserIdsToFollow.isEmpty()) {
            ProgressDialog dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
            this.mProgressDialog = dialog;
            dialog.setProgressStyle(0);
            dialog.setMessage(getString(R.string.following_friends));
            try {
                dialog.show();
            } catch (Exception e) {
            }
            ArrayList<String> userIdsToFollow = new ArrayList<>(this.mUserIdsToFollow);
            Components.userInteractionsComponent().bulkFollowUsers(this.mAppController, userIdsToFollow);
        } else {
            finishNux();
        }
        return true;
    }

    private void addContactsTab() {
        TabHost.TabSpec tabSpec = this.mTabHost.newTabSpec("contacts");
        tabSpec.setIndicator(getTabIndicator(R.string.contacts, R.color.find_friend_address));
        Bundle bundle = new Bundle();
        if (this.mIsTwitterReg) {
            bundle.putBoolean("take_focus", false);
        }
        bundle.putInt("empty_desc", R.string.find_friends_address_no_friends);
        this.mTabsAdapter.addTab(tabSpec, FindFriendsNUXAddressFragment.class, bundle);
    }

    private void addTwitterTab() {
        TabHost.TabSpec tabSpec = this.mTabHost.newTabSpec("twitter");
        tabSpec.setIndicator(getTabIndicator(R.string.twitter, R.color.find_friend_twitter));
        Bundle bundle = new Bundle();
        bundle.putBoolean("from_sign_up", true);
        bundle.putInt("empty_desc", R.string.find_friends_twitter_no_friends);
        this.mTabsAdapter.addTab(tabSpec, FindFriendsTwitterFragment.class, bundle);
    }

    private void addFacebookTab() {
        TabHost.TabSpec tabSpec = this.mTabHost.newTabSpec("facebook");
        tabSpec.setIndicator(getTabIndicator(R.string.facebook, R.color.find_friend_facebook));
        Bundle bundle = new Bundle();
        bundle.putInt("empty_desc", R.string.find_friends_facebook_no_friends);
        this.mTabsAdapter.addTab(tabSpec, FindFriendsFacebookFragment.class, bundle);
    }

    private TabIndicator getTabIndicator(int tabTextId, int colorResId) throws Resources.NotFoundException {
        TabIndicator indicator = TabIndicator.newTextIndicator(LayoutInflater.from(this), R.layout.normal_tab_indicator, this.mTabHost, tabTextId, false);
        ColorStateList indicatorStateList = TabIndicator.createTextColorList(getResources().getColor(colorResId), getResources().getColor(R.color.light_light_gray));
        TextView indicatorText = indicator.getIndicatorText();
        indicatorText.setTypeface(Typefaces.get(this).boldContent);
        indicatorText.setTextColor(indicatorStateList);
        return indicator;
    }

    public void addUserToFollow(long userId) {
        this.mUserIdsToFollow.add(String.valueOf(userId));
    }

    public void addUsersToFollow(ArrayList<VineUser> users, Friendships friendships) {
        Iterator<VineUser> it = users.iterator();
        while (it.hasNext()) {
            VineUser user = it.next();
            this.mUserIdsToFollow.add(String.valueOf(user.userId));
            friendships.addFollowing(user.userId);
        }
    }

    public void removeUserToFollow(long userId) {
        this.mUserIdsToFollow.remove(String.valueOf(userId));
    }

    public void toggleFollowAll(boolean selectAll, ArrayList<VineUser> users, Friendships friendships) {
        if (selectAll) {
            addUsersToFollow(users, friendships);
            return;
        }
        Iterator<VineUser> it = users.iterator();
        while (it.hasNext()) {
            VineUser user = it.next();
            this.mUserIdsToFollow.remove(String.valueOf(user.userId));
            friendships.removeFollowing(user.userId);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() throws Resources.NotFoundException {
        if (!TextUtils.isEmpty(this.mQuery.getText().toString()) || this.mQuery.hasFocus()) {
            clearSearch();
        } else if (this.mViewPager.getCurrentItem() > 0) {
            this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() - 1);
        }
    }

    public void showSearchIcon(boolean show) {
        if (this.mSearch != null) {
            this.mSearch.setVisible(show);
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

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ComponentCallbacks currentFragment = this.mTabsAdapter.getCurrentFragment();
        if (currentFragment instanceof FilterableFriendsList) {
            ((FilterableFriendsList) currentFragment).onTextChanged(s, start, before, count);
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() != R.id.query) {
            return false;
        }
        if (actionId != 3 && (event == null || event.getKeyCode() != 66)) {
            return false;
        }
        this.mQuery.clearFocus();
        Util.setSoftKeyboardVisibility(this, this.mQuery, false);
        return true;
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) FindFriendsNUXActivity.class));
    }

    public void finishNux() {
        FlurryUtils.trackNuxCompleted();
        NuxResolver.toNuxFromFindFriends(this);
        finish();
    }

    private class FindFriendsNUXListener extends UserInteractionsListener {
        private FindFriendsNUXListener() {
        }

        @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
        public void onBulkFollowComplete(String reqId, int statusCode, String reasonPhrase) {
            FindFriendsNUXActivity.this.dismissDialog();
            FindFriendsNUXActivity.this.finishNux();
        }
    }

    private void showSearchBar(boolean show) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ViewGroup customView = (ViewGroup) ab.getCustomView();
            EditText query = (EditText) customView.findViewById(R.id.query);
            ImageButton clear = (ImageButton) customView.findViewById(R.id.clear);
            TextView title = (TextView) customView.findViewById(R.id.action_bar_title);
            query.setVisibility(show ? 0 : 8);
            clear.setVisibility(show ? 0 : 8);
            title.setVisibility(show ? 8 : 0);
        }
    }
}
