package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class HomePostFooterViewHolder implements ViewHolder {
    private final ButtonViewHolder mCommentButtonHolder;
    private final InlineCommentsViewHolder mCommentsHolder;
    private final TextViewHolder mCommentsLabelHolder;
    private final ConsumptionButtonViewHolder mConsumptionHolder;
    private final TextViewHolder mDescriptionHolder;
    private final ButtonViewHolder mLikeButtonHolder;
    private final TextViewHolder mLikesLabelHolder;
    private final ButtonViewHolder mMoreButtonHolder;
    private final ButtonViewHolder mPlaylistButtonHolder;
    private final ButtonViewHolder mShareButtonHolder;
    private final TextViewHolder mSharesLabelHolder;

    public HomePostFooterViewHolder(View view) {
        this.mDescriptionHolder = new TextViewHolder(view, ViewType.DESCRIPTION, R.id.description);
        this.mConsumptionHolder = new ConsumptionButtonViewHolder(view);
        this.mLikeButtonHolder = new ButtonViewHolder(view, ViewType.LIKE_BUTTON, R.id.like);
        this.mLikesLabelHolder = new TextViewHolder(view, ViewType.LIKES_LABEL, R.id.like_count);
        this.mCommentButtonHolder = new ButtonViewHolder(view, ViewType.COMMENT_BUTTON, R.id.comment);
        this.mCommentsLabelHolder = new TextViewHolder(view, ViewType.COMMENTS_LABEL, R.id.comment_count);
        this.mShareButtonHolder = new ButtonViewHolder(view, ViewType.SHARE_BUTTON, R.id.share_post);
        this.mSharesLabelHolder = new TextViewHolder(view, ViewType.SHARES_LABEL, R.id.share_count);
        this.mMoreButtonHolder = new ButtonViewHolder(view, ViewType.MORE_OPTIONS, R.id.more_options);
        this.mCommentsHolder = new InlineCommentsViewHolder(view);
        this.mPlaylistButtonHolder = new ButtonViewHolder(view, ViewType.PLAYLIST_BUTTON, R.id.playlist_button);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.POST_FOOTER;
    }

    public TextViewHolder getDescriptionHolder() {
        return this.mDescriptionHolder;
    }

    public ConsumptionButtonViewHolder getConsumptionButtonHolder() {
        return this.mConsumptionHolder;
    }

    public ButtonViewHolder getLikeButtonHolder() {
        return this.mLikeButtonHolder;
    }

    public TextViewHolder getLikesLabelHolder() {
        return this.mLikesLabelHolder;
    }

    public ButtonViewHolder getCommentButtonHolder() {
        return this.mCommentButtonHolder;
    }

    public TextViewHolder getCommentsLabelHolder() {
        return this.mCommentsLabelHolder;
    }

    public ButtonViewHolder getShareButtonHolder() {
        return this.mShareButtonHolder;
    }

    public TextViewHolder getSharesLabelHolder() {
        return this.mSharesLabelHolder;
    }

    public ButtonViewHolder getMoreButtonHolder() {
        return this.mMoreButtonHolder;
    }

    public InlineCommentsViewHolder getCommentsHolder() {
        return this.mCommentsHolder;
    }

    public ButtonViewHolder getPlaylistButtonHolder() {
        return this.mPlaylistButtonHolder;
    }
}
