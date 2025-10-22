package co.vine.android.share.screens;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import co.vine.android.R;
import co.vine.android.TumblrLoginActivity;
import co.vine.android.animation.SimpleAnimatorListener;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VineRecipient;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.Session;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.share.utils.SharingPreferecesUtils;
import co.vine.android.share.utils.TwitterAuthUtil;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.share.widgets.VineRecipientSelectionIndicatorRow;
import co.vine.android.share.widgets.VineToggleRow;
import co.vine.android.social.FacebookHelper;
import co.vine.android.social.TumblrHelper;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.StartButton;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class ShareScreenHelper {
    protected final Context mContext;
    protected ImageView mFakeActionBarActionView;
    protected View mFakeActionBarBackArrow;
    protected boolean mIsVmAndChannelPickerContainerVisible = true;
    protected OnActionListener mOnActionListener;
    protected final Resources mRes;
    protected ScreenManager mScreenManager;
    protected final ShareScreenViewHolder mViewHolder;

    public interface OnActionListener {
        void onSubmit(boolean z, boolean z2, boolean z3, boolean z4, long j, ArrayList<VineRecipient> arrayList, String str, boolean z5);

        void onUndoRevine();
    }

    public enum ShareTarget {
        VINE,
        TWITTER,
        FACEBOOK,
        TUMBLR
    }

    protected abstract void ensureSubmitButton();

    public abstract void onBindFakeActionBar(FakeActionBar fakeActionBar);

    protected abstract void onShowAnimationStart();

    public static class ShareScreenViewHolder {
        public final VineToggleRow mFacebookToggleRow;
        public final StartButton mSubmitButton;
        public final VineToggleRow mTumblrToggleRow;
        public final VineToggleRow mTwitterToggleRow;
        public final VineToggleRow mVineToggleRow;
        public final VineRecipientSelectionIndicatorRow mVmRecipientIndicator;
        public final View mVmShareRow;

        public ShareScreenViewHolder(View view) {
            this.mVineToggleRow = (VineToggleRow) view.findViewById(R.id.vine_toggle_row);
            this.mTwitterToggleRow = (VineToggleRow) view.findViewById(R.id.twitter_toggle_row);
            this.mFacebookToggleRow = (VineToggleRow) view.findViewById(R.id.facebook_toggle_row);
            this.mTumblrToggleRow = (VineToggleRow) view.findViewById(R.id.tumblr_toggle_row);
            this.mSubmitButton = (StartButton) view.findViewById(R.id.finish);
            this.mVmShareRow = view.findViewById(R.id.share_with_a_friend_container);
            ((ImageView) this.mVmShareRow.findViewById(R.id.share_with_a_friend_icon)).setColorFilter(view.getContext().getResources().getColor(R.color.black_thirty_five_percent), PorterDuff.Mode.SRC_IN);
            this.mVmRecipientIndicator = (VineRecipientSelectionIndicatorRow) view.findViewById(R.id.vm_recipients_indicator);
        }
    }

    public ShareScreenHelper(final Context context, final ShareScreenViewHolder viewHolder, final boolean savePreferencesOnSubmit) {
        this.mContext = context;
        this.mRes = context.getResources();
        this.mViewHolder = viewHolder;
        View.OnClickListener showVMScreenClickListener = new View.OnClickListener() { // from class: co.vine.android.share.screens.ShareScreenHelper.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ShareScreenHelper.this.onVmScreenShown();
            }
        };
        viewHolder.mVmShareRow.setOnClickListener(showVMScreenClickListener);
        viewHolder.mVmRecipientIndicator.setOnClickListener(showVMScreenClickListener);
        viewHolder.mSubmitButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.screens.ShareScreenHelper.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ShareScreenHelper.this.mOnActionListener != null) {
                    if (savePreferencesOnSubmit) {
                        SharingPreferecesUtils.saveSharingPreferences(context, ShareScreenHelper.this.isTwitterSelected(), ShareScreenHelper.this.isFacebookSelected(), ShareScreenHelper.this.isTumblrSelected());
                    }
                    ShareScreenHelper.this.mOnActionListener.onSubmit(ShareScreenHelper.this.isVineSelected(), ShareScreenHelper.this.isTwitterSelected(), ShareScreenHelper.this.isFacebookSelected(), ShareScreenHelper.this.isTumblrSelected(), ShareScreenHelper.this.getSelectedChannelId(), viewHolder.mVmRecipientIndicator.getRecipients(), ShareScreenHelper.this.getComment(), false);
                }
            }
        });
        viewHolder.mSubmitButton.setTextTypeface(0, 3);
    }

    protected String getComment() {
        return null;
    }

    protected long getSelectedChannelId() {
        return -1L;
    }

    public VineChannel getSelectedChannel() {
        return null;
    }

    protected void onVmScreenShown() {
        this.mScreenManager.showScreen("vm");
    }

    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        AppController appController = AppController.getInstance(activity);
        if (requestCode == 1) {
            if (resultCode == -1) {
                String username = data.getStringExtra("screen_name");
                String token = data.getStringExtra("tk");
                String secret = data.getStringExtra("ts");
                long userId = data.getLongExtra("user_id", 0L);
                Session activeSession = appController.getActiveSession();
                appController.connectTwitter(activeSession, username, token, secret, userId);
                return true;
            }
            Util.showDefaultToast(activity, activity.getString(R.string.error_twitter_sdk));
            setShareTargetCheckedNoEvent(ShareTarget.TWITTER, false);
            return true;
        }
        if (requestCode == 2) {
            if (resultCode == -1) {
                String username2 = data.getStringExtra("screen_name");
                String token2 = data.getStringExtra("token");
                String secret2 = data.getStringExtra("secret");
                long userId2 = data.getLongExtra("user_id", 0L);
                Session activeSession2 = appController.getActiveSession();
                appController.connectTwitter(activeSession2, username2, token2, secret2, userId2);
                return true;
            }
            Util.showDefaultToast(activity, activity.getString(R.string.error_xauth));
            setShareTargetCheckedNoEvent(ShareTarget.TWITTER, false);
            return true;
        }
        if (requestCode == 3) {
            if (resultCode != -1) {
                setShareTargetCheckedNoEvent(ShareTarget.TUMBLR, false);
                return true;
            }
            return true;
        }
        SLog.d("Facebook auth came back: {}", Integer.valueOf(requestCode));
        boolean selected = FacebookHelper.onActivityResult(activity, requestCode, resultCode, data);
        if (selected) {
            this.mViewHolder.mVineToggleRow.setChecked(true);
        }
        setShareTargetCheckedNoEvent(ShareTarget.FACEBOOK, selected);
        return true;
    }

    public VineToggleRow findShareTargetToggleRow(ShareTarget shareTarget) {
        switch (shareTarget) {
            case VINE:
                return this.mViewHolder.mVineToggleRow;
            case TWITTER:
                return this.mViewHolder.mTwitterToggleRow;
            case FACEBOOK:
                return this.mViewHolder.mFacebookToggleRow;
            case TUMBLR:
                return this.mViewHolder.mTumblrToggleRow;
            default:
                SLog.w("Unable to find share target.");
                return null;
        }
    }

    public void setShareTargetChecked(ShareTarget shareTarget, boolean checked) {
        VineToggleRow toggleRow = findShareTargetToggleRow(shareTarget);
        if (toggleRow != null) {
            toggleRow.setChecked(checked);
        }
    }

    public void setShareTargetCheckedNoEvent(ShareTarget shareTarget, boolean checked) {
        VineToggleRow toggleRow = findShareTargetToggleRow(shareTarget);
        if (toggleRow != null) {
            toggleRow.setCheckedWithoutEvent(checked);
        }
    }

    public void setShareTargetEnabled(ShareTarget shareTarget) {
        VineToggleRow toggleRow = findShareTargetToggleRow(shareTarget);
        if (toggleRow != null) {
            ViewUtil.enableAndShow(toggleRow);
        }
    }

    public boolean isVineSelected() {
        return this.mViewHolder.mVineToggleRow.isEnabled() && this.mViewHolder.mVineToggleRow.isChecked();
    }

    public boolean isTwitterSelected() {
        return this.mViewHolder.mTwitterToggleRow.isEnabled() && this.mViewHolder.mTwitterToggleRow.isChecked();
    }

    public boolean isFacebookSelected() {
        return this.mViewHolder.mFacebookToggleRow.isEnabled() && this.mViewHolder.mFacebookToggleRow.isChecked();
    }

    public boolean isTumblrSelected() {
        return this.mViewHolder.mTumblrToggleRow.isEnabled() && this.mViewHolder.mTumblrToggleRow.isChecked();
    }

    public ArrayList<VineRecipient> getSelectedRecipients() {
        return this.mViewHolder.mVmRecipientIndicator.getRecipients();
    }

    public void onSaveInstanceState(Bundle bundle) {
        SharingPreferecesUtils.saveSharingPreferences(bundle, this.mViewHolder.mVineToggleRow.isEnabled(), isVineSelected(), this.mViewHolder.mTwitterToggleRow.isEnabled(), isTwitterSelected(), this.mViewHolder.mFacebookToggleRow.isEnabled(), isFacebookSelected(), this.mViewHolder.mTumblrToggleRow.isEnabled(), isTumblrSelected());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        ensureVmRecipientSelector();
        SharingPreferecesUtils.restoreShareOptions(savedInstanceState, this.mViewHolder.mVineToggleRow, this.mViewHolder.mTwitterToggleRow, this.mViewHolder.mFacebookToggleRow, this.mViewHolder.mTumblrToggleRow);
        onResume();
    }

    public void onResume() {
        VineToggleRow.OnCheckedStateChangedListener vineListener = new VineToggleRow.OnCheckedStateChangedListener() { // from class: co.vine.android.share.screens.ShareScreenHelper.3
            @Override // co.vine.android.share.widgets.VineToggleRow.OnCheckedStateChangedListener
            public void onCheckedStateChanged(boolean selected, boolean fromUserInput) {
                ShareScreenHelper.this.ensureSubmitButton();
                ShareScreenHelper.this.onVineCheckStateChanged(selected);
            }
        };
        this.mViewHolder.mVineToggleRow.setOnCheckedStateChangedListener(vineListener);
        VineToggleRow.OnCheckedStateChangedListener twitterListener = new VineToggleRow.OnCheckedStateChangedListener() { // from class: co.vine.android.share.screens.ShareScreenHelper.4
            @Override // co.vine.android.share.widgets.VineToggleRow.OnCheckedStateChangedListener
            public void onCheckedStateChanged(boolean selected, boolean fromUserInput) {
                ShareScreenHelper.this.onTwitterCheckStateChanged();
                ShareScreenHelper.this.ensureSubmitButton();
                if (selected && (ShareScreenHelper.this.mContext instanceof Activity)) {
                    Activity activity = (Activity) ShareScreenHelper.this.mContext;
                    AppController appController = AppController.getInstance(ShareScreenHelper.this.mContext);
                    if (!TwitterAuthUtil.isTwitterConnected(ShareScreenHelper.this.mContext)) {
                        AppController.startTwitterAuthWithFinish(appController.getTwitter(), activity);
                    }
                }
            }
        };
        this.mViewHolder.mTwitterToggleRow.setOnCheckedStateChangedListener(twitterListener);
        VineToggleRow.OnCheckedStateChangedListener facebookListener = new VineToggleRow.OnCheckedStateChangedListener() { // from class: co.vine.android.share.screens.ShareScreenHelper.5
            @Override // co.vine.android.share.widgets.VineToggleRow.OnCheckedStateChangedListener
            public void onCheckedStateChanged(boolean selected, boolean fromUserInput) {
                ShareScreenHelper.this.onFacebookCheckStateChanged();
                ShareScreenHelper.this.ensureSubmitButton();
                if (selected && (ShareScreenHelper.this.mContext instanceof Activity)) {
                    if (!FacebookHelper.isFacebookConnected(ShareScreenHelper.this.mContext)) {
                        FacebookHelper.connectToFacebookProfile((Activity) ShareScreenHelper.this.mContext);
                    } else if (!FacebookHelper.hasPermission("publish_actions")) {
                        FacebookHelper.connectToFacebookPublish((Activity) ShareScreenHelper.this.mContext);
                    }
                }
            }
        };
        this.mViewHolder.mFacebookToggleRow.setOnCheckedStateChangedListener(facebookListener);
        VineToggleRow.OnCheckedStateChangedListener tumblrListener = new VineToggleRow.OnCheckedStateChangedListener() { // from class: co.vine.android.share.screens.ShareScreenHelper.6
            @Override // co.vine.android.share.widgets.VineToggleRow.OnCheckedStateChangedListener
            public void onCheckedStateChanged(boolean selected, boolean fromUserInput) {
                ShareScreenHelper.this.onTumblrCheckStateChanged();
                ShareScreenHelper.this.ensureSubmitButton();
                if (selected && (ShareScreenHelper.this.mContext instanceof Activity) && !TumblrHelper.isTumblrConnected(ShareScreenHelper.this.mContext)) {
                    Activity activity = (Activity) ShareScreenHelper.this.mContext;
                    Intent intent = new Intent(activity, (Class<?>) TumblrLoginActivity.class);
                    activity.startActivityForResult(intent, 3, ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.slide_in_from_bottom, R.anim.slide_out_to_top).toBundle());
                }
            }
        };
        this.mViewHolder.mTumblrToggleRow.setOnCheckedStateChangedListener(tumblrListener);
    }

    public void setExternalNetworkShareTargetRowsEnabled(boolean enabled) {
        this.mViewHolder.mTwitterToggleRow.setEnabled(enabled);
        this.mViewHolder.mFacebookToggleRow.setEnabled(enabled);
        this.mViewHolder.mTumblrToggleRow.setEnabled(enabled);
    }

    public void ensureVmRecipientSelector() {
        if (this.mViewHolder.mVmRecipientIndicator.hasRecipients()) {
            ViewUtil.disableAndHide(this.mViewHolder.mVmShareRow);
            ViewUtil.enableAndShow(this.mViewHolder.mVmRecipientIndicator);
        } else {
            ViewUtil.disableAndHide(this.mViewHolder.mVmRecipientIndicator);
            ViewUtil.enableAndShow(this.mViewHolder.mVmShareRow);
        }
    }

    public void onShow(Bundle previousResult) {
        ensureVmRecipientSelector();
        ensureSubmitButton();
    }

    public String getActionButtonTextPost(boolean vineSelected, int numVmRecipientsSelected) {
        boolean vmRecipientsSelected = numVmRecipientsSelected > 0;
        if (vineSelected) {
            return this.mRes.getString(R.string.share_post);
        }
        if (vmRecipientsSelected && numVmRecipientsSelected == 1) {
            return this.mRes.getString(R.string.vm_share_with_friend);
        }
        if (vmRecipientsSelected && numVmRecipientsSelected > 1) {
            return this.mRes.getString(R.string.vm_share_with_friends, Integer.valueOf(numVmRecipientsSelected));
        }
        return this.mRes.getString(R.string.save_to_camera);
    }

    public AnimatorSet createAnimatorSet(final View view, ValueAnimator alpha) {
        alpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.share.screens.ShareScreenHelper.7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float valueY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                view.setAlpha(valueY);
            }
        });
        alpha.setTarget(this);
        alpha.setDuration(250L);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alpha);
        return animatorSet;
    }

    public AnimatorSet getHideAnimatorSet(final View view) {
        ValueAnimator alpha = ValueAnimator.ofFloat(1.0f, 0.8f);
        alpha.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.ShareScreenHelper.8
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
            }

            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                ViewUtil.disableAndHide(view);
            }
        });
        return createAnimatorSet(view, alpha);
    }

    public AnimatorSet getShowAnimatorSet(final View view) {
        ValueAnimator alpha = ValueAnimator.ofFloat(0.8f, 1.0f);
        alpha.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.ShareScreenHelper.9
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                ViewUtil.enableAndShow(view);
                ShareScreenHelper.this.ensureSubmitButton();
                ShareScreenHelper.this.ensureVmRecipientSelector();
                ShareScreenHelper.this.onShowAnimationStart();
            }
        });
        return createAnimatorSet(view, alpha);
    }

    public void onInitialize(ScreenManager screenManager, Bundle initialData) {
        this.mScreenManager = screenManager;
        this.mScreenManager.registerAppSessionListener(createAppSessionListener((Activity) this.mContext));
    }

    public AppSessionListener createAppSessionListener(final Activity activity) {
        return new AppSessionListener() { // from class: co.vine.android.share.screens.ShareScreenHelper.10
            @Override // co.vine.android.client.AppSessionListener
            public void onConnectTwitterComplete(String reqId, int statusCode, String reasonPhrase, String username, String token, String secret, long userId) {
                if (statusCode != 200) {
                    CommonUtil.showCenteredToast(activity, R.string.twitter_connection_failed);
                } else if (userId > 0 && !TextUtils.isEmpty(token) && !TextUtils.isEmpty(secret)) {
                    String loginEmail = AppController.getInstance(ShareScreenHelper.this.mContext).getActiveSession().getUsername();
                    VineAccountHelper.saveTwitterInfo(activity, userId, loginEmail, username, token, secret, userId);
                    Util.getDefaultSharedPrefs(activity).edit().putBoolean("settings_twitter_connected", true).apply();
                    ShareScreenHelper.this.setShareTargetChecked(ShareTarget.TWITTER, true);
                    return;
                }
                ShareScreenHelper.this.setShareTargetChecked(ShareTarget.TWITTER, false);
            }
        };
    }

    protected void onTumblrCheckStateChanged() {
    }

    public void setState(boolean isPostMine, boolean isRevinedByMe, boolean disableExternalShare) {
    }

    public void setOnActionListener(OnActionListener listener) {
        this.mOnActionListener = listener;
    }

    public boolean onBack() {
        return false;
    }

    protected void onFacebookCheckStateChanged() {
    }

    protected void onVineCheckStateChanged(boolean selected) {
    }

    protected void onTwitterCheckStateChanged() {
    }
}
