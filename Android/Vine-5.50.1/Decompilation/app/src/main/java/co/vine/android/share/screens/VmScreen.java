package co.vine.android.share.screens;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import co.vine.android.ContactEntry;
import co.vine.android.R;
import co.vine.android.Settings;
import co.vine.android.animation.SimpleAnimatorListener;
import co.vine.android.api.VineRecipient;
import co.vine.android.client.AppController;
import co.vine.android.service.components.Components;
import co.vine.android.share.adapters.ContactsAdapter;
import co.vine.android.share.adapters.RecipientsAdapter;
import co.vine.android.share.providers.RecipientProvider;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.share.widgets.VineCommentRow;
import co.vine.android.share.widgets.VineRecipientSelectionIndicatorRow;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.views.SimpleTextWatcher;
import co.vine.android.widget.ObservableSet;
import co.vine.android.widget.SectionAdapter;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.TypefacesEditText;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class VmScreen extends Screen {
    private Button mActionBarActionView;
    private View mActionBarBackAddArrow;
    private View mActionBarBackArrow;
    private VineCommentRow mAddCommentRow;
    private AppController mAppController;
    private LinearLayout mCommentContainer;
    private SectionAdapter mDefaultListAdapter;
    private boolean mDefaultListAdapterDisplayContacts;
    private VineRecipientSelectionIndicatorRow mIndicatorRow;
    private boolean mIsCommentsEnabled;
    private RefreshableListView mListView;
    private long mPostId;
    private TypefacesEditText mQuery;
    private RecipientProvider mRecipientProvider;
    private ScreenManager mScreenManager;
    private ObservableSet<VineRecipient> mSelectedRecipientsRepository;
    private SectionAdapter mUserSearchListAdapter;

    public VmScreen(Context context) {
        super(context, null);
        this.mPostId = -1L;
    }

    public VmScreen(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.mPostId = -1L;
    }

    public VmScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPostId = -1L;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mCommentContainer = (LinearLayout) findViewById(R.id.comment_container);
        this.mAddCommentRow = (VineCommentRow) findViewById(R.id.add_a_comment);
        this.mListView = (RefreshableListView) findViewById(R.id.vm_recipient_selector_list_view);
        this.mIndicatorRow = (VineRecipientSelectionIndicatorRow) findViewById(R.id.vm_recipients_indicator);
        this.mQuery = (TypefacesEditText) findViewById(R.id.query);
        this.mDefaultListAdapterDisplayContacts = false;
        View emptyView = findViewById(R.id.list_view_empty);
        emptyView.setMinimumWidth(SystemUtil.getDisplaySize(getContext()).x);
        this.mListView.setEmptyView(emptyView);
        this.mAddCommentRow.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.screens.VmScreen.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                VmScreen.this.mScreenManager.showScreen("comment");
            }
        });
    }

    @Override // co.vine.android.share.screens.Screen
    public void onInitialize(ScreenManager screenManager, AppController appController, final Bundle initialData) throws Resources.NotFoundException {
        this.mScreenManager = screenManager;
        this.mAppController = appController;
        Point windowSize = SystemUtil.getDisplaySize(getContext());
        getLayoutParams().height = (windowSize.y - ViewUtil.getStatusBarHeightPx(getResources())) - this.mScreenManager.getFakeActionBar().calculateBounds(windowSize).y;
        this.mQuery.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.share.screens.VmScreen.2
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void afterTextChanged(Editable text) {
                String filter = text.toString();
                if (filter.length() < 2) {
                    if (VmScreen.this.mListView.getAdapter() != VmScreen.this.mDefaultListAdapter) {
                        VmScreen.this.mListView.setAdapter((ListAdapter) VmScreen.this.mDefaultListAdapter);
                        return;
                    }
                    return;
                }
                VmScreen.this.mListView.setAdapter((ListAdapter) VmScreen.this.mUserSearchListAdapter);
            }
        });
        this.mPostId = initialData.getLong("postId", -1L);
        FakeActionBar fakeActionBar = screenManager.getFakeActionBar();
        this.mActionBarBackArrow = fakeActionBar.inflateBackView(R.layout.fake_action_bar_down_arrow);
        this.mActionBarBackAddArrow = fakeActionBar.inflateActionView(R.layout.fake_action_bar_down_plus_arrow);
        this.mActionBarActionView = (Button) fakeActionBar.inflateActionView(R.layout.vm_screen_action_bar_action);
        this.mActionBarActionView.setTypeface(Typefaces.get(getContext()).getContentTypeface(Typefaces.get(getContext()).mediumContentBold.getStyle(), 3));
        this.mActionBarActionView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.screens.VmScreen.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String remoteVideoUrl = initialData.getString("remoteVideoUrl");
                String videoThumbnailUrl = initialData.getString("videoThumbnailUrl");
                ArrayList<VineRecipient> recipients = new ArrayList<>();
                Iterator it = VmScreen.this.mSelectedRecipientsRepository.iterator();
                while (it.hasNext()) {
                    VineRecipient recipient = (VineRecipient) it.next();
                    recipients.add(recipient);
                }
                VmScreen.this.sendPostAsVm(VmScreen.this.mPostId, recipients, remoteVideoUrl, videoThumbnailUrl);
                VmScreen.this.sendCommentAsVm(recipients, VmScreen.this.mAddCommentRow.getComment());
                ((Activity) VmScreen.this.getContext()).finish();
            }
        });
        setActionBarActionViewButtonBackgroundColor();
        ensureFakeActionBarState();
        updateCommentRowState();
    }

    @Override // co.vine.android.share.screens.Screen
    public void onBindFakeActionBar(FakeActionBar fakeActionBar) throws Resources.NotFoundException {
        Resources resources = getResources();
        String friends = resources.getString(R.string.friends);
        fakeActionBar.setLabelText(friends);
        fakeActionBar.setActionView(this.mActionBarActionView);
        ensureFakeActionBarState();
    }

    @Override // co.vine.android.share.screens.Screen
    public boolean onBack() {
        String comment = this.mAddCommentRow.getComment();
        if (!CommonUtil.isNullOrWhitespace(comment)) {
            Bundle result = new Bundle();
            result.putString("comment", comment);
            this.mScreenManager.setScreenResult(result);
            return false;
        }
        return false;
    }

    @Override // co.vine.android.share.screens.Screen
    public void onShow(Bundle previousResult) {
        if (previousResult.containsKey("comment")) {
            String comment = previousResult.getString("comment");
            this.mAddCommentRow.setComment(comment);
            updateCommentRowState();
        }
    }

    @Override // co.vine.android.share.screens.Screen
    public void onHide() {
        this.mIndicatorRow.clearSelectedRecipientIndicator();
    }

    @Override // co.vine.android.share.screens.Screen
    public AnimatorSet getShowAnimatorSet() {
        Point windowSize = SystemUtil.getDisplaySize(getContext());
        ValueAnimator translation = ValueAnimator.ofFloat(windowSize.y, this.mScreenManager.getFakeActionBar().calculateBounds(windowSize).y);
        translation.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.VmScreen.4
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                Activity activity = (Activity) VmScreen.this.getContext();
                activity.getWindow().setSoftInputMode(48);
                ViewUtil.enableAndShow(VmScreen.this);
                VmScreen.this.ensureFakeActionBarState();
                VmScreen.this.updateCommentRowState();
                VmScreen.this.mQuery.requestFocus();
                CommonUtil.setSoftKeyboardVisibility(VmScreen.this.getContext(), VmScreen.this.mQuery, true);
            }
        });
        translation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.share.screens.VmScreen.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float valueY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                VmScreen.this.setY(valueY);
            }
        });
        translation.setInterpolator(new DecelerateInterpolator());
        translation.setDuration(250L);
        translation.setTarget(this);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translation);
        return animatorSet;
    }

    @Override // co.vine.android.share.screens.Screen
    public AnimatorSet getHideAnimatorSet() {
        Point windowSize = SystemUtil.getDisplaySize(getContext());
        ValueAnimator translation = ValueAnimator.ofFloat(this.mScreenManager.getFakeActionBar().calculateBounds(windowSize).y, windowSize.y);
        translation.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.VmScreen.6
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                ViewUtil.disableAndHide(VmScreen.this);
                VmScreen.this.setY(0.0f);
            }
        });
        translation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.share.screens.VmScreen.7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float valueY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                VmScreen.this.setY(valueY);
            }
        });
        translation.setInterpolator(new DecelerateInterpolator());
        translation.setDuration(250L);
        translation.setTarget(this);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translation);
        return animatorSet;
    }

    public void setListAdapters(SectionAdapter defaultListAdapter, SectionAdapter userSearchListAdapter) {
        this.mDefaultListAdapter = defaultListAdapter;
        this.mUserSearchListAdapter = userSearchListAdapter;
        this.mListView.setAdapter((ListAdapter) defaultListAdapter);
        this.mRecipientProvider.setOnDataChangedListener(new RecipientProvider.OnDataChangedListener() { // from class: co.vine.android.share.screens.VmScreen.8
            @Override // co.vine.android.share.providers.RecipientProvider.OnDataChangedListener
            public void onDefaultsChanged(List<VineRecipient> recent, List<VineRecipient> friends, List<VineRecipient> following) {
                SectionAdapter defaultListAdapter2 = VmScreen.this.mDefaultListAdapter;
                if (defaultListAdapter2 != null) {
                    BaseAdapter adapter = defaultListAdapter2.getAdapter(0);
                    if (adapter instanceof RecipientsAdapter) {
                        RecipientsAdapter recentsAdapter = (RecipientsAdapter) adapter;
                        recentsAdapter.replaceData(recent);
                    }
                    BaseAdapter adapter2 = defaultListAdapter2.getAdapter(1);
                    if (adapter2 instanceof RecipientsAdapter) {
                        RecipientsAdapter friendsAdapter = (RecipientsAdapter) adapter2;
                        friendsAdapter.replaceData(friends);
                    }
                    BaseAdapter adapter3 = defaultListAdapter2.getAdapter(2);
                    if (adapter3 instanceof RecipientsAdapter) {
                        RecipientsAdapter followingAdapter = (RecipientsAdapter) adapter3;
                        followingAdapter.replaceData(following);
                    }
                    defaultListAdapter2.notifyDataSetChanged();
                    if (!VmScreen.this.mRecipientProvider.hasMoreRecipientsGroupedBySection()) {
                        VmScreen.this.mDefaultListAdapterDisplayContacts = true;
                        VmScreen.this.mRecipientProvider.requestContacts("");
                    }
                }
            }

            @Override // co.vine.android.share.providers.RecipientProvider.OnDataChangedListener
            public void onContactsChanged(List<ContactEntry> contacts) {
                SectionAdapter defaultListAdapter2 = VmScreen.this.mDefaultListAdapter;
                SectionAdapter searchListAdapter = VmScreen.this.mUserSearchListAdapter;
                if (defaultListAdapter2 != null && VmScreen.this.mDefaultListAdapterDisplayContacts) {
                    BaseAdapter adapter = defaultListAdapter2.getAdapter(3);
                    if (adapter instanceof ContactsAdapter) {
                        ContactsAdapter contactsAdapter = (ContactsAdapter) adapter;
                        contactsAdapter.replaceData(contacts);
                    }
                    defaultListAdapter2.notifyDataSetChanged();
                }
                if (searchListAdapter != null) {
                    BaseAdapter adapter2 = searchListAdapter.getAdapter(1);
                    if (adapter2 instanceof ContactsAdapter) {
                        ContactsAdapter contactsAdapter2 = (ContactsAdapter) adapter2;
                        contactsAdapter2.replaceData(contacts);
                    }
                    searchListAdapter.notifyDataSetChanged();
                }
            }

            @Override // co.vine.android.share.providers.RecipientProvider.OnDataChangedListener
            public void onUserSearchChanged(List<VineRecipient> search) {
                SectionAdapter searchListAdapter = VmScreen.this.mUserSearchListAdapter;
                if (searchListAdapter != null) {
                    BaseAdapter adapter = searchListAdapter.getAdapter(0);
                    if (adapter instanceof RecipientsAdapter) {
                        RecipientsAdapter searchAdapter = (RecipientsAdapter) adapter;
                        searchAdapter.replaceData(search);
                    }
                    searchListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override // co.vine.android.share.screens.Screen
    public void onSaveInstanceState(Bundle bundle) {
        if (this.mSelectedRecipientsRepository != null) {
            ArrayList<VineRecipient> recipients = new ArrayList<>();
            Iterator<VineRecipient> it = this.mSelectedRecipientsRepository.iterator();
            while (it.hasNext()) {
                VineRecipient r = it.next();
                recipients.add(r);
            }
            bundle.putParcelableArrayList("extra_vm_recipients", recipients);
        }
    }

    @Override // co.vine.android.share.screens.Screen
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        ArrayList<VineRecipient> recipients = savedInstanceState.getParcelableArrayList("extra_vm_recipients");
        if (recipients != null) {
            Iterator<VineRecipient> it = recipients.iterator();
            while (it.hasNext()) {
                VineRecipient r = it.next();
                this.mSelectedRecipientsRepository.add(r);
            }
        }
    }

    public void setSelectedRecipientsRepository(ObservableSet<VineRecipient> selectedRecipientsRepository) {
        this.mSelectedRecipientsRepository = selectedRecipientsRepository;
        this.mSelectedRecipientsRepository.addObserver(new ObservableSet.ChangeObserver<VineRecipient>() { // from class: co.vine.android.share.screens.VmScreen.9
            @Override // co.vine.android.widget.ObservableSet.ChangeObserver
            public void onAdd(VineRecipient recipient) {
                VmScreen.this.mListView.invalidateViews();
                VmScreen.this.ensureFakeActionBarState();
                VmScreen.this.updateCommentRowState();
            }

            @Override // co.vine.android.widget.ObservableSet.ChangeObserver
            public void onRemove(VineRecipient recipient) {
                VmScreen.this.mListView.invalidateViews();
                VmScreen.this.ensureFakeActionBarState();
                VmScreen.this.updateCommentRowState();
            }
        });
    }

    public void setRecipientProvider(RecipientProvider recipientProvider) {
        this.mRecipientProvider = recipientProvider;
        this.mListView.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: co.vine.android.share.screens.VmScreen.10
            private boolean mProcessedScrolledToLast = false;

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                CommonUtil.setSoftKeyboardVisibility(VmScreen.this.getContext(), VmScreen.this, false);
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ListAdapter adapter = VmScreen.this.mListView.getAdapter();
                if (adapter == VmScreen.this.mDefaultListAdapter) {
                    if (view.getLastVisiblePosition() == totalItemCount - 1 && !this.mProcessedScrolledToLast) {
                        VmScreen.this.mRecipientProvider.requestNextRecipientsGroupedBySection();
                        this.mProcessedScrolledToLast = true;
                    } else {
                        this.mProcessedScrolledToLast = false;
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sendPostAsVm(long postId, ArrayList<VineRecipient> recipients, String remoteVideoUrl, String videoThumbnailUrl) {
        if (postId <= 0 || recipients == null || recipients.isEmpty()) {
            return false;
        }
        Components.shareComponent().shareVM(this.mAppController, recipients, postId, remoteVideoUrl, videoThumbnailUrl, "");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sendCommentAsVm(ArrayList<VineRecipient> recipients, String comment) {
        if (recipients == null || recipients.isEmpty() || CommonUtil.isNullOrWhitespace(comment)) {
            return false;
        }
        Components.shareComponent().shareVM(this.mAppController, recipients, -1L, null, null, comment);
        return true;
    }

    private void setActionBarActionViewButtonBackgroundColor() throws Resources.NotFoundException {
        SharedPreferences sharedPreferences = Util.getDefaultSharedPrefs(getContext());
        int profileColor = sharedPreferences.getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR) | ViewCompat.MEASURED_STATE_MASK;
        GradientDrawable actionBarActionViewDrawable = (GradientDrawable) this.mActionBarActionView.getBackground();
        actionBarActionViewDrawable.setColor(profileColor);
        float strokeWidth = getResources().getDimension(R.dimen.vm_recipient_view_stoke_width);
        actionBarActionViewDrawable.setStroke((int) strokeWidth, profileColor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureFakeActionBarState() {
        if (!this.mSelectedRecipientsRepository.isEmpty()) {
            if (this.mPostId > 0) {
                ViewUtil.enableAndShow(this.mActionBarActionView);
            } else {
                ViewUtil.disableAndHide(this.mActionBarActionView);
            }
            this.mScreenManager.getFakeActionBar().setBackView(this.mActionBarBackAddArrow);
            return;
        }
        ViewUtil.disableAndHide(this.mActionBarActionView);
        this.mScreenManager.getFakeActionBar().setBackView(this.mActionBarBackArrow);
    }

    public void updateCommentRowState() {
        if (this.mIsCommentsEnabled) {
            this.mCommentContainer.setY(0.0f);
            if (this.mSelectedRecipientsRepository != null) {
                this.mCommentContainer.setVisibility(this.mSelectedRecipientsRepository.isEmpty() ? 8 : 0);
            }
        }
    }

    private void setCommentsEnabled(boolean isCommentEnabled) {
        this.mIsCommentsEnabled = isCommentEnabled;
        this.mCommentContainer.setVisibility(this.mIsCommentsEnabled ? 0 : 8);
    }

    public static VmScreen inflateInitialized(LayoutInflater layoutInflater, SectionAdapter defaultRecipientsMultiDataSetAdapter, SectionAdapter searchMultiDataSetAdapter, RecipientProvider recipientProvider, ObservableSet<VineRecipient> selectedRecipientsRepository, boolean isCommentEnabled) {
        final VmScreen screen = (VmScreen) layoutInflater.inflate(R.layout.vm_screen, (ViewGroup) null, false);
        screen.setCommentsEnabled(isCommentEnabled);
        screen.setSelectedRecipientsRepository(selectedRecipientsRepository);
        screen.setRecipientProvider(recipientProvider);
        screen.setListAdapters(defaultRecipientsMultiDataSetAdapter, searchMultiDataSetAdapter);
        final VineRecipientSelectionIndicatorRow recipientIndicator = (VineRecipientSelectionIndicatorRow) screen.findViewById(R.id.vm_recipients_indicator);
        recipientIndicator.setSelectedRecipientsRepository(selectedRecipientsRepository);
        Iterator<VineRecipient> it = selectedRecipientsRepository.iterator();
        while (it.hasNext()) {
            VineRecipient recipient = it.next();
            recipientIndicator.addRecipient(recipient);
        }
        selectedRecipientsRepository.addObserver(new ObservableSet.ChangeObserver<VineRecipient>() { // from class: co.vine.android.share.screens.VmScreen.11
            @Override // co.vine.android.widget.ObservableSet.ChangeObserver
            public void onAdd(VineRecipient recipient2) {
                recipientIndicator.addRecipient(recipient2);
                recipientIndicator.clearSearchTermAndDismissKeyboard();
                screen.updateCommentRowState();
            }

            @Override // co.vine.android.widget.ObservableSet.ChangeObserver
            public void onRemove(VineRecipient recipient2) {
                recipientIndicator.removeRecipient(recipient2);
                screen.updateCommentRowState();
            }
        });
        recipientIndicator.setRecipientProvider(recipientProvider);
        return screen;
    }
}
