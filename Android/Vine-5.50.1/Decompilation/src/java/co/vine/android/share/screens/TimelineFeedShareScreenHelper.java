package co.vine.android.share.screens;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import co.vine.android.R;
import co.vine.android.share.screens.ShareScreenHelper;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.share.widgets.VineCommentRow;
import co.vine.android.widget.Typefaces;

/* loaded from: classes.dex */
public class TimelineFeedShareScreenHelper extends ShareScreenHelper {
    private final TimelineShareScreenHolder timelineHolder;

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected void onTumblrCheckStateChanged() {
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected void onFacebookCheckStateChanged() {
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected void onVineCheckStateChanged(boolean selected) {
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected void onTwitterCheckStateChanged() {
    }

    public TimelineFeedShareScreenHelper(Context context, ShareScreenHelper.ShareScreenViewHolder viewHolder, TimelineShareScreenHolder timelineHolder) throws Resources.NotFoundException {
        super(context, viewHolder, false);
        this.timelineHolder = timelineHolder;
        int undoRevineIconColor = this.mRes.getColor(R.color.vine_green);
        timelineHolder.mUndoRevineIcon.setColorFilter(undoRevineIconColor, PorterDuff.Mode.SRC_IN);
        Typefaces typefaces = Typefaces.get(context);
        timelineHolder.mUndoRevineButton.setTypeface(typefaces.getContentTypeface(typefaces.mediumContent.getStyle(), 3));
        timelineHolder.mUndoRevineButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.screens.TimelineFeedShareScreenHelper.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (TimelineFeedShareScreenHelper.this.mOnActionListener != null) {
                    TimelineFeedShareScreenHelper.this.mOnActionListener.onUndoRevine();
                }
            }
        });
        timelineHolder.mAddCommentRow.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.screens.TimelineFeedShareScreenHelper.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TimelineFeedShareScreenHelper.this.mScreenManager.showScreen("comment");
            }
        });
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected void onVmScreenShown() {
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void onInitialize(ScreenManager screenManager, final Bundle initialData) {
        super.onInitialize(screenManager, initialData);
        FakeActionBar fakeActionBar = this.mScreenManager.getFakeActionBar();
        this.mFakeActionBarBackArrow = fakeActionBar.inflateBackView(R.layout.fake_action_bar_back_arrow);
        this.mFakeActionBarActionView = (ImageView) fakeActionBar.inflateActionView(R.layout.main_screen_action_bar_action);
        this.mFakeActionBarActionView.setColorFilter(-1, PorterDuff.Mode.SRC_IN);
        this.mFakeActionBarActionView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.screens.TimelineFeedShareScreenHelper.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String username = initialData.getString("username");
                String shareUrl = initialData.getString("shareUrl");
                String subject = TimelineFeedShareScreenHelper.this.mContext.getString(R.string.share_post_subject, username);
                Intent moreShare = new Intent("android.intent.action.SEND");
                moreShare.setType("text/plain");
                moreShare.putExtra("android.intent.extra.SUBJECT", subject);
                moreShare.putExtra("android.intent.extra.TEXT", shareUrl);
                TimelineFeedShareScreenHelper.this.mContext.startActivity(Intent.createChooser(moreShare, "Share"));
            }
        });
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void onShow(Bundle previousResult) {
        super.onShow(previousResult);
        if (previousResult.containsKey("comment")) {
            String comment = previousResult.getString("comment");
            this.timelineHolder.mAddCommentRow.setComment(comment);
        }
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected void onShowAnimationStart() {
        ((Activity) this.mContext).getWindow().setSoftInputMode(48);
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void setState(boolean isPostMine, boolean isRevinedByMe, boolean disableExternalShare) {
        if (disableExternalShare) {
            setExternalNetworkShareTargetRowsEnabled(false);
        }
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String comment = savedInstanceState.getString("extra_vm_text");
        if (!TextUtils.isEmpty(comment)) {
            this.timelineHolder.mAddCommentRow.setComment(comment);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected void ensureSubmitButton() {
        boolean shareSelected = isVineSelected() || isTwitterSelected() || isFacebookSelected() || isTumblrSelected();
        if (shareSelected) {
            this.mViewHolder.mSubmitButton.setText(this.mRes.getString(R.string.share_title));
            this.mViewHolder.mSubmitButton.setSelected(false);
            this.mViewHolder.mSubmitButton.setTextColor(-1);
        } else {
            this.mViewHolder.mSubmitButton.setText(this.mRes.getString(R.string.cancel));
            this.mViewHolder.mSubmitButton.setSelected(true);
            this.mViewHolder.mSubmitButton.setTextColor(this.mRes.getColor(R.color.black_thirty_five_percent));
        }
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("extra_vm_text", this.timelineHolder.mAddCommentRow.getComment());
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void onBindFakeActionBar(FakeActionBar fakeActionBar) throws Resources.NotFoundException {
        String share = this.mRes.getString(R.string.share_title);
        fakeActionBar.setLabelText(share);
        fakeActionBar.setBackView(this.mFakeActionBarBackArrow);
        fakeActionBar.setActionView(this.mFakeActionBarActionView);
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected String getComment() {
        return this.timelineHolder.mAddCommentRow.getComment();
    }

    static final class TimelineShareScreenHolder {
        public final VineCommentRow mAddCommentRow;
        public final LinearLayout mCommentContainer;
        public final LinearLayout mCommentContainerContainer;
        public final Button mUndoRevineButton;
        public final ImageView mUndoRevineIcon;
        public final ViewGroup mUndoRevineRow;

        public TimelineShareScreenHolder(View view) {
            this.mCommentContainer = (LinearLayout) view.findViewById(R.id.comment_container);
            this.mCommentContainerContainer = (LinearLayout) view.findViewById(R.id.comment_container_container);
            this.mUndoRevineRow = (ViewGroup) view.findViewById(R.id.undo_revine_row);
            this.mUndoRevineIcon = (ImageView) view.findViewById(R.id.undo_revine_icon);
            this.mUndoRevineButton = (Button) view.findViewById(R.id.undo_revine);
            this.mAddCommentRow = (VineCommentRow) view.findViewById(R.id.add_a_comment);
            LayoutTransition layoutTransition = new LayoutTransition();
            layoutTransition.disableTransitionType(0);
            layoutTransition.setStartDelay(2, 0L);
            this.mCommentContainerContainer.setLayoutTransition(layoutTransition);
        }
    }
}
