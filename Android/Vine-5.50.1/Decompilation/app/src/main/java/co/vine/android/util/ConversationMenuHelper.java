package co.vine.android.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import co.vine.android.BaseControllerActionBarActivity;
import co.vine.android.ConversationActivity;
import co.vine.android.FriendSearchAdapter;
import co.vine.android.InboxFragment;
import co.vine.android.R;
import co.vine.android.Settings;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.suggestions.SuggestionsActionListener;
import co.vine.android.views.TouchableRelativeLayout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ConversationMenuHelper extends SlidingActivityHelper implements Handler.Callback, TextWatcher, View.OnClickListener, AdapterView.OnItemClickListener {
    private ViewGroup mActionContainer;
    private final BaseControllerActionBarActivity mActivity;
    private AppController mAppController;
    private final ConversationMenuHelperAppSessionListener mAppSessionListener;
    private FriendSearchAdapter mFriendSearchAdapter;
    private Handler mHandler;
    private InboxFragment mInboxFragment;
    private boolean mIsSearching;
    private View mNothingFoundText;
    private int mProfileColor;
    private View mSadFace;
    private ViewGroup mSearchContainer;
    private EditText mSearchField;
    private ProgressBar mSearchInProgress;
    private View mSearchOnBackgroundView;
    private RefreshableListView mSearchResultView;
    private final SuggestedFriendsListener mSuggestedFriendsListener;
    private long mWaitingToStartSearchUserId;
    private long mWaitingToStartUserId;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConversationMenuHelper(BaseControllerActionBarActivity activity) {
        super(activity);
        this.mWaitingToStartUserId = -1L;
        this.mWaitingToStartSearchUserId = -1L;
        this.mActivity = activity;
        this.mAppSessionListener = new ConversationMenuHelperAppSessionListener();
        this.mSuggestedFriendsListener = new SuggestedFriendsListener();
    }

    public void setupConversationSlidingMenu() {
        this.mAppController = AppController.getInstance(this.mActivity);
        View view = this.mActivity.setBehindContentView(R.layout.conversation_menu);
        this.mSearchField = (EditText) view.findViewById(R.id.search_field);
        this.mSearchField.addTextChangedListener(this);
        this.mActionContainer = (ViewGroup) view.findViewById(R.id.menu_action_container);
        setPersonalizedColor(Util.getProfileColor(this.mActivity));
        this.mSearchContainer = (ViewGroup) view.findViewById(R.id.menu_search_container);
        this.mSearchResultView = (RefreshableListView) view.findViewById(R.id.search_result);
        this.mSearchResultView.setOnItemClickListener(this);
        this.mSearchOnBackgroundView = view.findViewById(R.id.saerch_toggle_background_view);
        this.mSearchOnBackgroundView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.util.ConversationMenuHelper.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ConversationMenuHelper.this.toggleContactSearch(false);
            }
        });
        this.mSadFace = view.findViewById(R.id.search_real_sadface);
        this.mNothingFoundText = view.findViewById(R.id.search_empty_text);
        view.findViewById(R.id.menu_search).setOnClickListener(this);
        view.findViewById(R.id.clear_search).setOnClickListener(this);
        ViewUtil.setActionBarHeight(this.mActivity, view.findViewById(R.id.menu_list_header));
        this.mSearchInProgress = (ProgressBar) view.findViewById(R.id.search_in_progress);
        setSlidingActionBarEnabled(true);
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidth(35);
        sm.setBehindOffset(this.mActivity.getResources().getDimensionPixelSize(R.dimen.sliding_menu_offset));
        sm.setTouchModeAbove(1);
        ((TouchableRelativeLayout) this.mActivity.findViewById(R.id.menu_content)).setTouchListener(new ViewBehindTouchListener(this.mActivity, sm));
        FragmentManager fm = this.mActivity.getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag("Inbox");
        if (frag == null) {
            this.mInboxFragment = new InboxFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.menu_content, this.mInboxFragment, "Inbox");
            ft.commit();
        } else {
            this.mInboxFragment = (InboxFragment) frag;
        }
        sm.setOnClosedListener(this.mInboxFragment);
        sm.setOnOpenedListener(this.mInboxFragment);
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.menu_search) {
            toggleContactSearch(true);
        } else if (id == R.id.clear_search) {
            toggleContactSearch(false);
        }
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 1:
                Components.suggestionsComponent().fetchFriendsTypeAhead(this.mAppController, String.valueOf(message.obj));
                break;
        }
        return false;
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.mHandler.removeMessages(1);
        String query = s.toString().toLowerCase();
        if (!TextUtils.isEmpty(query) && s.length() >= 2) {
            Message msg = Message.obtain(null, 1, query);
            if (msg != null) {
                showSearchProgressBar(true);
                this.mHandler.sendMessageDelayed(msg, 200L);
                this.mIsSearching = true;
                return;
            }
            return;
        }
        if (this.mIsSearching) {
            showSearchProgressBar(false);
            this.mSearchResultView.setVisibility(8);
            this.mSadFace.setVisibility(8);
            this.mIsSearching = false;
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long userRemoteId = this.mFriendSearchAdapter.getItemId(position);
        this.mWaitingToStartSearchUserId = userRemoteId;
        if (userRemoteId < 1) {
            throw new IllegalStateException("The userRemoteId of the clicked search result item is invalid");
        }
        this.mAppController.fetchConversationRowIdFromUserRemoteId(userRemoteId, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displaySearchResults(ArrayList<VineUser> users) {
        if (users.isEmpty()) {
            this.mSearchResultView.setVisibility(8);
            this.mSadFace.setVisibility(0);
            this.mNothingFoundText.setVisibility(0);
            return;
        }
        this.mSadFace.setVisibility(8);
        if (this.mFriendSearchAdapter == null) {
            this.mFriendSearchAdapter = new FriendSearchAdapter(this.mActivity, this.mAppController, users);
            this.mSearchResultView.setAdapter((ListAdapter) this.mFriendSearchAdapter);
        } else {
            FriendSearchAdapter adapter = this.mFriendSearchAdapter;
            adapter.clear();
            adapter.setData(users);
            adapter.notifyDataSetChanged();
        }
        this.mSearchResultView.setVisibility(0);
    }

    public void showSearchProgressBar(boolean show) {
        this.mSearchInProgress.setVisibility(show ? 0 : 8);
    }

    public void toggleContactSearch(boolean show) {
        this.mSearchOnBackgroundView.setVisibility(show ? 0 : 4);
        this.mSearchContainer.setVisibility(show ? 0 : 4);
        this.mActionContainer.setVisibility(show ? 4 : 0);
        if (show) {
            this.mSearchField.requestFocus();
        } else {
            this.mSearchField.clearFocus();
            this.mSearchField.setText("");
        }
        Util.setSoftKeyboardVisibility(this.mActivity, this.mSearchField, show);
    }

    public void onPause() {
        this.mAppController.removeListener(this.mAppSessionListener);
        Components.suggestionsComponent().removeListener(this.mSuggestedFriendsListener);
    }

    public void onResume() {
        SlidingMenu sm = getSlidingMenu();
        if (sm.isMenuShowing()) {
            sm.showContent(false);
        }
        this.mAppController.addListener(this.mAppSessionListener);
        Components.suggestionsComponent().addListener(this.mSuggestedFriendsListener);
    }

    public void setPersonalizedColor(int profileBackground) {
        if (profileBackground == Settings.DEFAULT_PROFILE_COLOR || profileBackground <= 0) {
            profileBackground = 16777215 & this.mActivity.getResources().getColor(R.color.vine_green);
        }
        this.mProfileColor = (-16777216) | profileBackground;
        this.mActionContainer.setBackgroundColor(this.mProfileColor);
        if (this.mInboxFragment != null) {
            this.mInboxFragment.setPersonalizedColor(profileBackground);
        }
    }

    private final class SuggestedFriendsListener extends SuggestionsActionListener {
        private SuggestedFriendsListener() {
        }

        @Override // co.vine.android.service.components.suggestions.SuggestionsActionListener
        public void onGetFriendsTypeAheadComplete(String reqId, int statusCode, String reasonPhrase, String query, ArrayList<VineUser> users) {
            ConversationMenuHelper.this.showSearchProgressBar(false);
            if (users != null && ConversationMenuHelper.this.mIsSearching) {
                ConversationMenuHelper.this.displaySearchResults(users);
            }
        }
    }

    private class ConversationMenuHelperAppSessionListener extends AppSessionListener {
        private ConversationMenuHelperAppSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetConversationRowIdComplete(long recipientId, boolean isRecipientExternal, long conversationObjectId, String username, boolean amFollowing) {
            if (recipientId == ConversationMenuHelper.this.mWaitingToStartUserId || recipientId == ConversationMenuHelper.this.mWaitingToStartSearchUserId) {
                if (recipientId == ConversationMenuHelper.this.mWaitingToStartSearchUserId) {
                    ConversationMenuHelper.this.toggleContactSearch(false);
                }
                ConversationMenuHelper.this.mWaitingToStartUserId = -1L;
                ConversationMenuHelper.this.mAppController.clearUnreadMessageCount(conversationObjectId);
                ConversationMenuHelper.this.mActivity.startActivity(ConversationActivity.getIntent(ConversationMenuHelper.this.mActivity, conversationObjectId, username, recipientId, isRecipientExternal, amFollowing, false));
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            if (ConversationMenuHelper.this.mIsSearching && ConversationMenuHelper.this.mFriendSearchAdapter != null) {
                ConversationMenuHelper.this.mFriendSearchAdapter.setUserImages(images);
            }
        }
    }
}
