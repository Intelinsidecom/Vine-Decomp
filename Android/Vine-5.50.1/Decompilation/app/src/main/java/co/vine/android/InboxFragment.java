package co.vine.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.Session;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineDatabaseSQL;
import co.vine.android.service.GCMNotificationService;
import co.vine.android.util.ConversationMenuHelper;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.MuteUtil;
import co.vine.android.util.StringAnchorManager;
import co.vine.android.util.Util;
import co.vine.android.widget.ContactsMenuAdapter;
import co.vine.android.widget.SectionAdapter;
import co.vine.android.widget.Typefaces;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.edisonwang.android.slog.SLog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.HashMap;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class InboxFragment extends BaseCursorListFragment implements View.OnClickListener, SlidingMenu.OnClosedListener, SlidingMenu.OnOpenedListener {
    private BaseAdapter mActiveAdapter;
    private ContactsMenuAdapter mAddressAdapter;
    private StringAnchorManager mAnchorManager;
    private ArrayList<ContactEntry> mContactEntries;
    private MessageBoxAdapter mDummyAdapter;
    private ImageView mEmptyImage;
    private TextView mEmptyTitle;
    private View mEmptyView;
    private TextView mEmptyWords;
    protected int mFetchFlags;
    private MenuUsersAdapter mFriendsAdapter;
    private TextView mFriendsText;
    protected MessageBoxAdapter mInboxAdapter;
    private int mInboxCursorCount;
    private boolean mIsAddressBookShowing;
    private boolean mIsEmptyViewAdded;
    private boolean mIsSwitching;
    protected long mMessageCount;
    private MessageBoxAdapter mOtherAdapter;
    private int mOthersCursorCount;
    private TextView mOthersText;
    private SectionAdapter mSectionAdapter;
    private Typefaces mTypefaces;
    private int mUnReadCount;
    protected long mWaitingToStartId = -1;
    private boolean mLoadingMore = false;
    private final BroadcastReceiver mColorChangedReceiver = new BroadcastReceiver() { // from class: co.vine.android.InboxFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int color = Util.getDefaultSharedPrefs(InboxFragment.this.getActivity()).getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR);
            ((ConversationMenuHelper) ((BaseActionBarActivity) InboxFragment.this.getActivity()).getSlidingMenuHelper()).setPersonalizedColor(color);
        }
    };
    private final Runnable mInvalidateEmptyViewRunnable = new Runnable() { // from class: co.vine.android.InboxFragment.2
        @Override // java.lang.Runnable
        public void run() {
            if (InboxFragment.this.mActiveAdapter == InboxFragment.this.mSectionAdapter) {
                if (InboxFragment.this.mInboxCursorCount == 0) {
                    if (!InboxFragment.this.mIsEmptyViewAdded) {
                        SLog.d("Header change: Add.");
                        InboxFragment.this.mIsEmptyViewAdded = true;
                        InboxFragment.this.mListView.addHeaderView(InboxFragment.this.mEmptyView);
                        return;
                    }
                    return;
                }
                if (InboxFragment.this.mIsEmptyViewAdded) {
                    SLog.d("Header change: Remove.");
                    InboxFragment.this.mIsEmptyViewAdded = false;
                    InboxFragment.this.mListView.removeHeaderView(InboxFragment.this.mEmptyView);
                    return;
                }
                return;
            }
            if (InboxFragment.this.mOthersCursorCount == 0) {
                if (!InboxFragment.this.mIsEmptyViewAdded) {
                    SLog.d("Header change: Add.");
                    InboxFragment.this.mIsEmptyViewAdded = true;
                    InboxFragment.this.mListView.addHeaderView(InboxFragment.this.mEmptyView);
                    return;
                }
                return;
            }
            if (InboxFragment.this.mIsEmptyViewAdded) {
                SLog.d("Header change: Remove.");
                InboxFragment.this.mIsEmptyViewAdded = false;
                InboxFragment.this.mListView.removeHeaderView(InboxFragment.this.mEmptyView);
            }
        }
    };
    private final PromptDialogSupportFragment.OnDialogDoneListener mStoreContactDialogDoneListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.InboxFragment.3
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            FragmentActivity activity = InboxFragment.this.getActivity();
            if (activity != null) {
                boolean enable = which == -1;
                if (enable) {
                    InboxFragment.this.mAppController.sendAddressBook();
                }
                InboxFragment.this.mAppController.updateEnableAddressBook(enable);
                SharedPreferences.Editor editor = Util.getDefaultSharedPrefs(activity).edit();
                editor.putBoolean("enable_address_book", enable).apply();
            }
        }
    };
    private final Runnable mInvalidateContactEntryRunnable = new Runnable() { // from class: co.vine.android.InboxFragment.4
        /* JADX WARN: Removed duplicated region for block: B:22:0x0066  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                r7 = this;
                r4 = 1
                co.vine.android.InboxFragment r5 = co.vine.android.InboxFragment.this
                android.widget.BaseAdapter r5 = co.vine.android.InboxFragment.access$000(r5)
                co.vine.android.InboxFragment r6 = co.vine.android.InboxFragment.this
                co.vine.android.widget.SectionAdapter r6 = co.vine.android.InboxFragment.access$100(r6)
                if (r5 != r6) goto L65
                co.vine.android.InboxFragment r5 = co.vine.android.InboxFragment.this
                android.widget.ListView r5 = r5.mListView
                int r2 = r5.getLastVisiblePosition()
                r3 = 1
                co.vine.android.InboxFragment r5 = co.vine.android.InboxFragment.this     // Catch: java.lang.Exception -> L68
                android.widget.ListView r5 = r5.mListView     // Catch: java.lang.Exception -> L68
                int r5 = r5.getCount()     // Catch: java.lang.Exception -> L68
                int r5 = r5 + (-1)
                if (r2 != r5) goto L66
                co.vine.android.InboxFragment r5 = co.vine.android.InboxFragment.this     // Catch: java.lang.Exception -> L68
                android.widget.ListView r5 = r5.mListView     // Catch: java.lang.Exception -> L68
                android.view.View r5 = r5.getChildAt(r2)     // Catch: java.lang.Exception -> L68
                int r5 = r5.getBottom()     // Catch: java.lang.Exception -> L68
                co.vine.android.InboxFragment r6 = co.vine.android.InboxFragment.this     // Catch: java.lang.Exception -> L68
                android.widget.ListView r6 = r6.mListView     // Catch: java.lang.Exception -> L68
                int r6 = r6.getHeight()     // Catch: java.lang.Exception -> L68
                if (r5 > r6) goto L66
                r3 = r4
            L3b:
                r1 = 0
                co.vine.android.InboxFragment r5 = co.vine.android.InboxFragment.this     // Catch: java.lang.Exception -> L6f
                co.vine.android.MessageBoxAdapter r5 = r5.mInboxAdapter     // Catch: java.lang.Exception -> L6f
                boolean r1 = r5.isEmpty()     // Catch: java.lang.Exception -> L6f
            L44:
                co.vine.android.InboxFragment r5 = co.vine.android.InboxFragment.this
                boolean r5 = co.vine.android.InboxFragment.access$1300(r5)
                if (r5 != 0) goto L60
                co.vine.android.InboxFragment r5 = co.vine.android.InboxFragment.this
                boolean r5 = r5.isFetched(r4)
                if (r5 == 0) goto L56
                if (r1 != 0) goto L60
            L56:
                co.vine.android.InboxFragment r5 = co.vine.android.InboxFragment.this
                boolean r4 = r5.isFetched(r4)
                if (r4 == 0) goto L65
                if (r3 == 0) goto L65
            L60:
                co.vine.android.InboxFragment r4 = co.vine.android.InboxFragment.this
                r4.addEntriesToAddressAdapter()
            L65:
                return
            L66:
                r3 = 0
                goto L3b
            L68:
                r0 = move-exception
                java.lang.String r5 = "Cannot get listview state."
                com.edisonwang.android.slog.SLog.e(r5)
                goto L3b
            L6f:
                r0 = move-exception
                co.vine.android.util.CrashUtil.logException(r0)
                goto L44
            */
            throw new UnsupportedOperationException("Method not decompiled: co.vine.android.InboxFragment.AnonymousClass4.run():void");
        }
    };
    private final OnContactEntryClickedListener mListener = new OnContactEntryClickedListener() { // from class: co.vine.android.InboxFragment.5
        @Override // co.vine.android.OnContactEntryClickedListener
        protected void onContactEntryClicked(ContactEntry entry, VineRecipient recipient, int typeFlag, String value) {
            if (InboxFragment.this.mWaitingToStartId == -1) {
                InboxFragment.this.mWaitingToStartId = recipient.contactId;
                InboxFragment.this.mAppController.fetchConversationRowIdFromSingleRecipient(recipient, 1);
            }
        }
    };

    @Override // co.vine.android.BaseCursorListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createView(inflater, R.layout.friends_menu_list_fragment, container);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case 1:
                if (cursor != null) {
                    onLoadMessageBoxComplete(loader.getId(), cursor);
                    break;
                }
                break;
            case 4:
                this.mUnReadCount = 0;
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        this.mUnReadCount += cursor.getInt(4);
                    } while (cursor.moveToNext());
                    cursor.moveToFirst();
                }
                invalidateUnreadCount();
                if (cursor != null) {
                    onLoadMessageBoxComplete(loader.getId(), cursor);
                    break;
                }
                break;
        }
        SLog.d("Load finished: {}.", Integer.valueOf(loader.getId()));
        invalidateEmptyView(false);
        invalidateContactEntries();
    }

    private synchronized void invalidateEmptyView(boolean immediate) {
        this.mHandler.removeCallbacks(this.mInvalidateEmptyViewRunnable);
        if (immediate) {
            this.mInvalidateEmptyViewRunnable.run();
        } else {
            this.mHandler.postDelayed(this.mInvalidateEmptyViewRunnable, 100L);
        }
    }

    private void invalidateUnreadCount() {
        String text = null;
        if (this.mUnReadCount > 0) {
            if (this.mUnReadCount > 20) {
                text = "20+";
            } else {
                text = String.valueOf(this.mUnReadCount);
            }
        }
        if (this.mOthersText != null) {
            String currentText = this.mOthersText.getText().toString();
            int index = currentText.indexOf("(");
            if (text != null) {
                TextView textView = this.mOthersText;
                StringBuilder sb = new StringBuilder();
                if (index != -1) {
                    currentText = currentText.substring(0, index - 1);
                }
                textView.setText(sb.append(currentText).append(" (").append(text).append(") ").toString());
                return;
            }
            if (index != -1) {
                this.mOthersText.setText(currentText.substring(0, index - 1));
            }
        }
    }

    public void onLoadMessageBoxComplete(int loaderId, Cursor cursor) {
        MessageBoxAdapter adapter = loaderId == 4 ? this.mOtherAdapter : this.mInboxAdapter;
        int flag = loaderId == 4 ? 4 : 1;
        Cursor oldCursor = adapter.getCursor();
        if (cursor != null) {
            if (loaderId == 4) {
                this.mOthersCursorCount = cursor.getCount();
            } else {
                this.mInboxCursorCount = cursor.getCount();
            }
            if (oldCursor != null) {
                oldCursor.unregisterContentObserver(this.mChangeObserver);
            }
            cursor.registerContentObserver(this.mChangeObserver);
        }
        adapter.swapCursor(cursor);
        if (adapter.isEmpty()) {
            if (!isFetched(flag)) {
                fetchContent(3, flag, true);
                return;
            } else {
                showSadface(true);
                return;
            }
        }
        hideProgress(3);
    }

    protected void fetchContent(int fetchType, int fetchMode, boolean silent) {
        int pageType;
        if (!hasPendingRequest(fetchType)) {
            switch (fetchType) {
                case 1:
                    pageType = 3;
                    break;
                case 2:
                case 4:
                    pageType = 1;
                    break;
                case 3:
                    pageType = 1;
                    break;
                default:
                    pageType = 1;
                    break;
            }
            if (pageType == 3 && fetchMode == 1) {
                this.mLoadingMore = true;
            }
            if ((fetchMode & 4) != 0) {
                setFetched(4);
                addRequest(this.mAppController.fetchConversations(pageType, false, 2), fetchType);
            }
            if ((fetchMode & 1) != 0) {
                setFetched(1);
                addRequest(this.mAppController.fetchConversations(pageType, false, 1), fetchType);
            }
            if (!silent) {
                showProgress(fetchType);
            }
        }
    }

    protected boolean isFetched(int mode) {
        return (this.mFetchFlags & mode) != 0;
    }

    protected void setFetched(int mode) {
        this.mFetchFlags |= mode;
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppSessionListener(new ConversationMenuSessionListener());
        this.mMessageCount = 20L;
    }

    @Override // co.vine.android.BaseCursorListFragment
    public View createView(LayoutInflater inflater, int id, ViewGroup container) {
        View root = super.createView(inflater, id, container);
        View header = getActivity().getLayoutInflater().inflate(R.layout.menu_header, (ViewGroup) this.mListView, false);
        this.mFriendsText = (TextView) header.findViewById(R.id.friends);
        this.mOthersText = (TextView) header.findViewById(R.id.others);
        this.mListView.addHeaderView(header, null, false);
        this.mListView.setBackgroundColor(getResources().getColor(R.color.solid_white));
        this.mEmptyView = inflater.inflate(R.layout.vm_friends_empty, (ViewGroup) null);
        this.mEmptyImage = (ImageView) this.mEmptyView.findViewById(R.id.sadface);
        this.mEmptyWords = (TextView) this.mEmptyView.findViewById(R.id.sadwords);
        this.mEmptyTitle = (TextView) this.mEmptyView.findViewById(R.id.sadTitle);
        return root;
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener
    public void onClosed() {
        this.mMessageCount = 20L;
        this.mAppController.clearInboxPageCursors();
        this.mListView.setSelectionFromTop(0, 0);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            restartLoader();
            ConversationMenuHelper helper = (ConversationMenuHelper) ((BaseActionBarActivity) activity).getSlidingMenuHelper();
            helper.toggleContactSearch(false);
        }
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener
    public void onOpened() {
        Activity a = getActivity();
        if (a != null) {
            a.startService(GCMNotificationService.getClearNotificationIntent(a, 2));
        }
        if (a instanceof HomeTabActivity) {
            if (!MuteUtil.isMuted(a)) {
                a.sendBroadcast(new Intent(MuteUtil.ACTION_CHANGED_TO_MUTE), CrossConstants.BROADCAST_PERMISSION);
            }
            AccountManager am = AccountManager.get(a);
            Account account = VineAccountHelper.getActiveAccount(a);
            if (account != null && VineAccountHelper.shouldShowStoreContactsPrompt(am, account)) {
                PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(1);
                p.setListener(this.mStoreContactDialogDoneListener);
                p.setMessage(R.string.store_contacts_desc);
                p.setTitle(R.string.store_contacts);
                p.setNegativeButton(R.string.no);
                p.setPositiveButton(R.string.ok);
                try {
                    p.show(((HomeTabActivity) a).getSupportFragmentManager());
                    VineAccountHelper.setDidShowStoreContactsPrompt(am, account);
                } catch (Exception e) {
                    CrashUtil.logException(e, "Failed to show contact agreement box. ", new Object[0]);
                }
            }
        }
    }

    public void setPersonalizedColor(int profileBackground) throws Resources.NotFoundException {
        if (profileBackground == Settings.DEFAULT_PROFILE_COLOR || profileBackground <= 0) {
            profileBackground = 16777215 & getResources().getColor(R.color.vine_green);
        }
        if (this.mListView != null) {
            ((RefreshableListView) this.mListView).colorizePTR((-16777216) | profileBackground);
        }
    }

    public boolean isLoadingMore() {
        return this.mLoadingMore;
    }

    private class TabSwitcher implements View.OnClickListener, Runnable {
        public final BaseAdapter targetAdapter;

        public TabSwitcher(BaseAdapter target) {
            this.targetAdapter = target;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (InboxFragment.this.mActiveAdapter != this.targetAdapter && !InboxFragment.this.mIsSwitching) {
                InboxFragment.this.mIsSwitching = true;
                InboxFragment.this.mHandler.removeCallbacks(this);
                InboxFragment.this.mHandler.postDelayed(this, 200L);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            InboxFragment.this.mActiveAdapter = this.targetAdapter;
            InboxFragment.this.mListView.setAdapter((ListAdapter) InboxFragment.this.mActiveAdapter);
            InboxFragment.this.mIsSwitching = false;
            InboxFragment.this.invalidateAdapters();
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onActivityCreated(savedInstanceState);
        this.mTypefaces = Typefaces.get(getActivity());
        int profileColor = Util.getDefaultSharedPrefs(getActivity()).getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR);
        if (profileColor == Settings.DEFAULT_PROFILE_COLOR || profileColor <= 0) {
            profileColor = 16777215 & getResources().getColor(R.color.vine_green);
        }
        if (this.mInboxAdapter == null) {
            this.mInboxAdapter = new MessageBoxAdapter(getActivity(), this.mAppController, this, 0);
        }
        if (this.mDummyAdapter == null) {
            this.mDummyAdapter = new MessageBoxAdapter(getActivity(), this.mAppController, this, 0);
        }
        if (this.mFriendsAdapter == null) {
            this.mFriendsAdapter = new MenuUsersAdapter(getActivity(), this.mAppController, 0);
            this.mFriendsAdapter.setInboxAdapter(this.mInboxAdapter);
        }
        if (this.mAddressAdapter == null) {
            this.mAddressAdapter = new ContactsMenuAdapter(getActivity());
        }
        if (this.mOtherAdapter == null) {
            this.mOtherAdapter = new MessageBoxAdapter(getActivity(), this.mAppController, this, 0);
        }
        this.mSectionAdapter = new SectionAdapter(this.mInboxAdapter, this.mFriendsAdapter, this.mAddressAdapter);
        this.mFriendsText.setOnClickListener(new TabSwitcher(this.mSectionAdapter));
        this.mOthersText.setOnClickListener(new TabSwitcher(this.mOtherAdapter));
        this.mActiveAdapter = this.mSectionAdapter;
        this.mListView.setAdapter((ListAdapter) this.mActiveAdapter);
        this.mListView.setOnScrollListener(this);
        ((RefreshableListView) this.mListView).colorizePTR((-16777216) | profileColor);
        this.mAnchorManager = Vine.getUserSectionsAnchorManager(getActivity(), 1);
        invalidateAdapters();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateAdapters() {
        Resources res = getActivity().getResources();
        if (this.mActiveAdapter == this.mOtherAdapter) {
            this.mFriendsText.setTextColor(1509949440);
            this.mOthersText.setTextColor(res.getColor(R.color.black_eighty_percent));
            this.mFriendsText.setTypeface(this.mTypefaces.regularContent);
            this.mOthersText.setTypeface(this.mTypefaces.mediumContent);
            this.mCursorAdapter = null;
            this.mEmptyWords.setText(R.string.vm_other_empty);
            this.mEmptyImage.setImageResource(R.drawable.vm_watermark);
            this.mEmptyTitle.setVisibility(8);
        } else {
            this.mOthersText.setTextColor(1509949440);
            this.mFriendsText.setTextColor(res.getColor(R.color.black_eighty_percent));
            this.mOthersText.setTypeface(this.mTypefaces.regularContent);
            this.mFriendsText.setTypeface(this.mTypefaces.mediumContent);
            this.mCursorAdapter = this.mFriendsAdapter;
            this.mEmptyTitle.setVisibility(0);
            this.mEmptyWords.setText(R.string.empty_inbox);
            this.mEmptyImage.setImageResource(R.drawable.vm_roundel);
        }
        invalidateEmptyView(true);
    }

    public void addEntriesToAddressAdapter() {
        if (this.mContactEntries != null) {
            SLog.d("Showing address book: {}.", Integer.valueOf(this.mContactEntries.size()));
            this.mIsAddressBookShowing = true;
            if (this.mAddressAdapter.getCount() != this.mContactEntries.size()) {
                this.mAddressAdapter.clear();
                this.mAddressAdapter.addAll(this.mContactEntries);
            }
            makeSadFaceHeaderView();
        }
    }

    private void invalidateContactEntries() {
        this.mHandler.removeCallbacks(this.mInvalidateContactEntryRunnable);
        this.mHandler.postDelayed(this.mInvalidateContactEntryRunnable, 100L);
    }

    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri = Vine.InboxView.CONTENT_URI.buildUpon().appendQueryParameter("hidden", "0").appendQueryParameter("network_type", String.valueOf(id == 4 ? 2 : 1)).appendQueryParameter("limit", String.valueOf(this.mMessageCount)).build();
        return new CursorLoader(getActivity(), contentUri, VineDatabaseSQL.InboxQuery.PROJECTION, null, null, null);
    }

    public void onInboxCursorClicked(Cursor inboxCursor, View item) {
        if (TextUtils.isEmpty(inboxCursor.getString(15)) || inboxCursor.getInt(14) <= 1) {
            long conversationObjectId = inboxCursor.getLong(1);
            String username = inboxCursor.getString(8);
            boolean amFollowing = inboxCursor.getInt(11) == 1;
            long userId = inboxCursor.getLong(10);
            boolean isUserExternal = inboxCursor.getInt(16) == 1;
            BaseActionBarActivity activity = (BaseActionBarActivity) getActivity();
            startActivity(ConversationActivity.getIntent(activity, conversationObjectId, username, userId, isUserExternal, amFollowing, false));
            return;
        }
        this.mAppController.retryMessagesInConversationRowId(inboxCursor.getLong(1));
        TextView retryText = (TextView) item.findViewById(R.id.tap_to_retry);
        retryText.setText(R.string.message_sending);
        item.findViewById(R.id.failed_upload).setVisibility(8);
        item.findViewById(R.id.retry_progress).setVisibility(0);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.load_more && !hasPendingRequest(1)) {
            view.findViewById(R.id.load_more_content).setVisibility(8);
            view.findViewById(R.id.progress).setVisibility(0);
            this.mMessageCount += 20;
            restartLoader(1);
            fetchContent(1, 1, true);
        }
    }

    private class ConversationMenuSessionListener extends AppSessionListener {
        private ConversationMenuSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetMessageInboxComplete(String reqId, int statusCode, String reasonPhrase, int count) {
            PendingRequest req = InboxFragment.this.removeRequest(reqId);
            if (req != null) {
                InboxFragment.this.mLoadingMore = false;
                InboxFragment.this.hideProgress(req.fetchType);
                InboxFragment.this.onGetMessageInboxComplete(count);
                switch (statusCode) {
                    case HttpResponseCode.OK /* 200 */:
                        break;
                    default:
                        InboxFragment.this.hideProgress(3);
                        if (req.fetchType != 3) {
                            Util.showCenteredToast(InboxFragment.this.getActivity(), R.string.error_server);
                        }
                        SLog.e("Error " + statusCode + " - " + reasonPhrase);
                        break;
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetConversationRowIdComplete(long recipientId, boolean isRecipientExternal, long conversationObjectId, String username, boolean amFollowing) {
            if (recipientId == InboxFragment.this.mWaitingToStartId) {
                InboxFragment.this.mWaitingToStartId = -1L;
                InboxFragment.this.mAppController.clearUnreadMessageCount(conversationObjectId);
                InboxFragment.this.startActivity(ConversationActivity.getIntent(InboxFragment.this.getActivity(), conversationObjectId, username, recipientId, isRecipientExternal, amFollowing, false));
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            InboxFragment.this.onPhotoImageLoaded(images);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetUsersComplete(Session session, String reqId, int statusCode, String reasonPhrase, int count, ArrayList<VineUser> users, int nextPage, int previousPage, String anchor) {
            PendingRequest req = InboxFragment.this.removeRequest(reqId);
            if (req != null) {
                InboxFragment.this.hideProgress(req.fetchType);
                switch (statusCode) {
                    case HttpResponseCode.OK /* 200 */:
                        break;
                    default:
                        InboxFragment.this.hideProgress(3);
                        SLog.e("Error:  " + reasonPhrase);
                        break;
                }
            }
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mInboxAdapter.getCursor() == null) {
            initLoader(1);
        } else if (this.mInboxAdapter.isEmpty() && !isFetched(1)) {
            fetchContent(3, 1, false);
        }
        if (this.mOtherAdapter.getCursor() == null) {
            initLoader(4);
        } else if (this.mOtherAdapter.isEmpty() && !isFetched(4)) {
            fetchContent(3, 4, false);
        }
        getActivity().registerReceiver(this.mColorChangedReceiver, Util.COLOR_CHANGED_INTENT_FILTER, CrossConstants.BROADCAST_PERMISSION, null);
    }

    @Override // co.vine.android.BaseCursorListFragment
    protected void onListItemClick(ListView l, View v, int position, long id) {
        int position2 = position - this.mListView.getHeaderViewsCount();
        if (position2 >= 0) {
            if (this.mActiveAdapter == this.mSectionAdapter) {
                switch (this.mSectionAdapter.getItemViewType(position2)) {
                    case 1:
                        onInboxCursorClicked((Cursor) this.mSectionAdapter.getItem(position2), v);
                        break;
                }
            }
            onInboxCursorClicked((Cursor) this.mOtherAdapter.getItem(position2), v);
        }
    }

    protected void onGetMessageInboxComplete(int count) {
        if (this.mActiveAdapter == this.mSectionAdapter) {
            if (count == 0) {
                invalidateContactEntries();
            } else {
                this.mAddressAdapter.clear();
                this.mIsAddressBookShowing = false;
                this.mAddressAdapter.notifyDataSetChanged();
            }
        }
        invalidateEmptyView(false);
    }

    @Override // co.vine.android.BaseCursorListFragment
    protected void refresh() {
        if (this.mActiveAdapter == this.mSectionAdapter) {
            fetchContent(2, 1, false);
        } else {
            fetchContent(2, 4, false);
        }
    }

    @Override // co.vine.android.BaseCursorListFragment
    protected void onScrollLastItem(Cursor cursor) {
        if (this.mActiveAdapter == this.mSectionAdapter && !this.mAnchorManager.haveMore()) {
            if (!this.mIsAddressBookShowing) {
                addEntriesToAddressAdapter();
            }
            hideProgress(3);
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(this.mColorChangedReceiver);
    }

    public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
        if (this.mInboxAdapter != null) {
            this.mInboxAdapter.setUserImages(images);
        }
        if (this.mFriendsAdapter != null) {
            this.mFriendsAdapter.setUserImages(images);
        }
        if (this.mOtherAdapter != null) {
            this.mOtherAdapter.setUserImages(images);
        }
    }

    @Override // co.vine.android.BaseFragment
    protected void updateAppNavigationProvider() {
    }
}
