package co.vine.android.share.screens;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import co.vine.android.R;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VineRecipient;
import co.vine.android.client.AppController;
import co.vine.android.share.activities.PostShareParameters;
import co.vine.android.share.screens.PostShareScreenHelper;
import co.vine.android.share.screens.ShareScreenHelper;
import co.vine.android.share.screens.TimelineFeedShareScreenHelper;
import co.vine.android.share.screens.TimelineShareScreenHelper;
import co.vine.android.share.utils.SharingPreferecesUtils;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.share.widgets.VineCommentRow;
import co.vine.android.share.widgets.VineRecipientSelectionIndicatorRow;
import co.vine.android.util.Util;
import co.vine.android.widget.ObservableSet;
import co.vine.android.widget.TypefacesTextView;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ShareScreen extends Screen {
    private ShareScreenHelper mHelper;

    public static ShareScreen inflateInitialized(LayoutInflater layoutInflater, boolean isTimeline, boolean isTimelineFeed) {
        return inflateInitialized(layoutInflater, isTimeline, isTimelineFeed, null, null, null);
    }

    public static ShareScreen inflateInitialized(LayoutInflater layoutInflater, boolean isTimeline, boolean isTimelineFeed, ObservableSet<VineRecipient> repo) {
        return inflateInitialized(layoutInflater, isTimeline, isTimelineFeed, repo, null, null);
    }

    public static ShareScreen inflateInitialized(LayoutInflater layoutInflater, boolean isTimeline, boolean isTimelineFeed, ObservableSet<VineRecipient> repo, VineChannel channel, PostShareParameters state) {
        ShareScreen screen = (ShareScreen) layoutInflater.inflate(R.layout.share, (ViewGroup) null, false);
        screen.initialize(isTimeline, isTimelineFeed, repo, channel, state);
        return screen;
    }

    public ShareScreen(Context context) {
        super(context);
    }

    public ShareScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setState(boolean isPostMine, boolean isRevinedByMe, boolean disableExternalShare) {
        this.mHelper.setState(isPostMine, isRevinedByMe, disableExternalShare);
    }

    public void initialize(boolean isTimeline, boolean isTimelineFeed, ObservableSet<VineRecipient> selectedRecipientsRepository, VineChannel channel, PostShareParameters state) {
        Context context = getContext();
        ShareScreenHelper.ShareScreenViewHolder viewHolder = new ShareScreenHelper.ShareScreenViewHolder(this);
        if (isTimeline) {
            findViewById(R.id.channel_picker_container).setVisibility(8);
            this.mHelper = new TimelineShareScreenHelper(context, viewHolder, new TimelineShareScreenHelper.TimelineShareScreenHolder(this));
            ((TypefacesTextView) this.mHelper.mViewHolder.mVineToggleRow.findViewById(R.id.toggle_label)).setText(R.string.revine_list_option);
        } else if (isTimelineFeed) {
            findViewById(R.id.vm_and_channel_container).setVisibility(8);
            findViewById(R.id.channel_picker_container).setVisibility(8);
            findViewById(R.id.undo_revine_row).setVisibility(8);
            VineCommentRow commentTextView = (VineCommentRow) findViewById(R.id.add_a_comment);
            commentTextView.setEmptyCommentLabelText(context.getString(R.string.add_comment));
            this.mHelper = new TimelineFeedShareScreenHelper(context, viewHolder, new TimelineFeedShareScreenHelper.TimelineShareScreenHolder(this));
            ((TypefacesTextView) this.mHelper.mViewHolder.mVineToggleRow.findViewById(R.id.toggle_label)).setText(R.string.share_feed_list_option);
        } else {
            findViewById(R.id.comment_container).setVisibility(8);
            findViewById(R.id.undo_revine_row).setVisibility(8);
            this.mHelper = new PostShareScreenHelper(context, viewHolder, new PostShareScreenHelper.PostShareScreenHolder(this), Util.getDefaultSharedPrefs(context).getBoolean("settings_private", false), channel);
            ((TypefacesTextView) this.mHelper.mViewHolder.mVineToggleRow.findViewById(R.id.toggle_label)).setText(R.string.vine);
        }
        setShareTargetEnabled(ShareScreenHelper.ShareTarget.VINE);
        setShareTargetEnabled(ShareScreenHelper.ShareTarget.TWITTER);
        setShareTargetEnabled(ShareScreenHelper.ShareTarget.FACEBOOK);
        setShareTargetEnabled(ShareScreenHelper.ShareTarget.TUMBLR);
        initializeSharingTargets(state);
        final VineRecipientSelectionIndicatorRow recipientIndicator = (VineRecipientSelectionIndicatorRow) findViewById(R.id.vm_recipients_indicator);
        if (selectedRecipientsRepository != null) {
            Iterator<VineRecipient> it = selectedRecipientsRepository.iterator();
            while (it.hasNext()) {
                VineRecipient recipient = it.next();
                recipientIndicator.addRecipient(recipient);
            }
            selectedRecipientsRepository.addObserver(new ObservableSet.ChangeObserver<VineRecipient>() { // from class: co.vine.android.share.screens.ShareScreen.1
                @Override // co.vine.android.widget.ObservableSet.ChangeObserver
                public void onAdd(VineRecipient recipient2) {
                    recipientIndicator.addRecipient(recipient2);
                }

                @Override // co.vine.android.widget.ObservableSet.ChangeObserver
                public void onRemove(VineRecipient recipient2) {
                    recipientIndicator.removeRecipient(recipient2);
                }
            });
        }
    }

    public void initializeSharingTargets(PostShareParameters state) {
        Context context = getContext();
        boolean stickyShareToTwitter = SharingPreferecesUtils.getDefaultTwitterSharePreference(AppController.getInstance(context), context);
        boolean stickyShareToFacebook = SharingPreferecesUtils.getDefaultFacebookSharePreference(context);
        boolean stickyShareToTumblr = SharingPreferecesUtils.getTumblrSharePreference(context);
        if (state == null) {
            initShareTarget(ShareScreenHelper.ShareTarget.VINE, true);
            initShareTarget(ShareScreenHelper.ShareTarget.TWITTER, false);
            initShareTarget(ShareScreenHelper.ShareTarget.FACEBOOK, false);
            initShareTarget(ShareScreenHelper.ShareTarget.FACEBOOK, false);
            return;
        }
        Boolean cachedShareToVine = state.shareToVine;
        Boolean cachedShareToTwitter = state.shareToTwitter;
        Boolean cachedShareToFacebook = state.shareToFacebook;
        Boolean cachedShareToTumblr = state.shareToTumblr;
        initShareTarget(ShareScreenHelper.ShareTarget.VINE, cachedShareToVine != null ? cachedShareToVine.booleanValue() : true);
        ShareScreenHelper.ShareTarget shareTarget = ShareScreenHelper.ShareTarget.TWITTER;
        if (cachedShareToTwitter != null) {
            stickyShareToTwitter = cachedShareToTwitter.booleanValue();
        }
        initShareTarget(shareTarget, stickyShareToTwitter);
        ShareScreenHelper.ShareTarget shareTarget2 = ShareScreenHelper.ShareTarget.FACEBOOK;
        if (cachedShareToFacebook != null) {
            stickyShareToFacebook = cachedShareToFacebook.booleanValue();
        }
        initShareTarget(shareTarget2, stickyShareToFacebook);
        ShareScreenHelper.ShareTarget shareTarget3 = ShareScreenHelper.ShareTarget.TUMBLR;
        if (cachedShareToTumblr != null) {
            stickyShareToTumblr = cachedShareToTumblr.booleanValue();
        }
        initShareTarget(shareTarget3, stickyShareToTumblr);
    }

    public ShareScreenHelper getHelper() {
        return this.mHelper;
    }

    @Override // co.vine.android.share.screens.Screen
    public void onBindFakeActionBar(FakeActionBar fakeActionBar) {
        this.mHelper.onBindFakeActionBar(fakeActionBar);
    }

    @Override // co.vine.android.share.screens.Screen
    public boolean onBack() {
        return this.mHelper.onBack();
    }

    @Override // co.vine.android.share.screens.Screen
    public void onShow(Bundle previousResult) {
        this.mHelper.onShow(previousResult);
    }

    @Override // co.vine.android.share.screens.Screen
    public AnimatorSet getShowAnimatorSet() {
        return this.mHelper.getShowAnimatorSet(this);
    }

    @Override // co.vine.android.share.screens.Screen
    public AnimatorSet getHideAnimatorSet() {
        return this.mHelper.getHideAnimatorSet(this);
    }

    @Override // co.vine.android.share.screens.Screen
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        this.mHelper.onActivityResult(activity, requestCode, resultCode, data);
        return true;
    }

    @Override // co.vine.android.share.screens.Screen
    public void onInitialize(ScreenManager screenManager, AppController appController, Bundle initialData) {
        this.mHelper.onInitialize(screenManager, initialData);
    }

    @Override // co.vine.android.share.screens.Screen
    public void onSaveInstanceState(Bundle bundle) {
        this.mHelper.onSaveInstanceState(bundle);
    }

    @Override // co.vine.android.share.screens.Screen
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.mHelper.onRestoreInstanceState(savedInstanceState);
    }

    public void setOnActionListener(ShareScreenHelper.OnActionListener listener) {
        this.mHelper.setOnActionListener(listener);
    }

    public void setShareTargetEnabled(ShareScreenHelper.ShareTarget shareTarget) {
        this.mHelper.setShareTargetEnabled(shareTarget);
    }

    public void initShareTarget(ShareScreenHelper.ShareTarget shareTarget, boolean checked) {
        this.mHelper.setShareTargetCheckedNoEvent(shareTarget, checked);
    }

    @Override // co.vine.android.share.screens.Screen
    public void onResume() {
        this.mHelper.onResume();
    }
}
