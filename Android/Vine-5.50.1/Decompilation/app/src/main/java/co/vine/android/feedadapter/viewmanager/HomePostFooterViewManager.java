package co.vine.android.feedadapter.viewmanager;

import android.app.Activity;
import android.text.TextUtils;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.HomePostFooterViewHolder;
import co.vine.android.feedadapter.viewmanager.InlineCommentsViewManager;
import co.vine.android.feedadapter.viewmanager.PostDescriptionViewManager;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.LinkSuppressor;
import co.vine.android.util.Util;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class HomePostFooterViewManager implements ViewManager {
    private final AppController mAppController;
    private final TimelineClickListenerFactory.Callback mCallback;
    private final PostActionLabelViewManager mCommentsLabelManager;
    private final InlineCommentsViewManager mCommentsManager;
    private final ConsumptionButtonViewManager mConsumptionManager;
    private final Activity mContext;
    private final PostActionLabelViewManager mLikesLabelManager;
    private final PostActionLabelViewManager mSharesLabelManager;
    private final PostDescriptionViewManager mDescriptionManager = new PostDescriptionViewManager();
    private final PostActionButtonViewManager mLikeButtonManager = new PostActionButtonViewManager(ViewType.LIKE_BUTTON);
    private final PostActionButtonViewManager mCommentButtonManager = new PostActionButtonViewManager(ViewType.COMMENT_BUTTON);
    private final PostActionButtonViewManager mShareButtonManager = new PostActionButtonViewManager(ViewType.SHARE_BUTTON);
    private final PostActionButtonViewManager mMoreButtonManager = new PostActionButtonViewManager(ViewType.MORE_OPTIONS);
    private final PostActionButtonViewManager mPlaylistButtonManager = new PostActionButtonViewManager(ViewType.PLAYLIST_BUTTON);

    public HomePostFooterViewManager(Activity context, AppController appController, TimelineClickListenerFactory.Callback callback) {
        this.mContext = context;
        this.mAppController = appController;
        this.mCallback = callback;
        this.mConsumptionManager = new ConsumptionButtonViewManager(this.mContext, this.mAppController);
        this.mLikesLabelManager = new PostActionLabelViewManager(ViewType.LIKES_LABEL, context);
        this.mCommentsLabelManager = new PostActionLabelViewManager(ViewType.COMMENTS_LABEL, context);
        this.mSharesLabelManager = new PostActionLabelViewManager(ViewType.SHARES_LABEL, context);
        this.mCommentsManager = new InlineCommentsViewManager(this.mContext);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.POST_FOOTER;
    }

    public void bind(HomePostFooterViewHolder h, VinePost data, int position, int entityColor, int profileColor, LinkSuppressor suppressor) {
        InlineCommentsViewManager.Comments comments;
        PostDescriptionViewManager.Description description;
        if (data != null) {
            if (!TextUtils.isEmpty(data.description)) {
                if (data.isRTL == null) {
                    data.isRTL = Boolean.valueOf(Util.isRtlLanguage(Util.addDirectionalMarkers(data.description)));
                }
                if (data.transientEntities == null && data.entities != null) {
                    data.transientEntities = new ArrayList<>();
                    Iterator<VineEntity> it = data.entities.iterator();
                    while (it.hasNext()) {
                        VineEntity entity = it.next();
                        data.transientEntities.add(entity.duplicate());
                    }
                }
                ArrayList<VineEntity> entities = data.transientEntities;
                if (data.cachedDescription == null) {
                    description = new PostDescriptionViewManager.Description(this.mContext, data.description, entities);
                    data.cachedDescription = description.getBuiltDescription(entityColor, 2, suppressor);
                } else {
                    description = new PostDescriptionViewManager.Description(this.mContext, data.cachedDescription);
                }
                this.mDescriptionManager.bind(h.getDescriptionHolder(), description, entities != null && entities.size() > 0, data.isRTL, entityColor, 2, suppressor);
            } else {
                h.getDescriptionHolder().text.setVisibility(8);
            }
            boolean enableNewAttribution = ClientFlagsHelper.multiAttributionsView(this.mContext);
            if (data.sources != null && !data.sources.isEmpty() && !enableNewAttribution) {
                this.mConsumptionManager.bind(h.getConsumptionButtonHolder(), data.sources);
            } else if (data.audioMetadata != null && !data.audioMetadata.isEmpty() && !enableNewAttribution) {
                this.mConsumptionManager.bind(h.getConsumptionButtonHolder(), data.audioMetadata.get(0));
            } else {
                h.getConsumptionButtonHolder().button.setVisibility(8);
            }
            boolean showShare = this.mAppController.getActiveId() == data.userId || data.isShareable();
            this.mLikeButtonManager.bind(h.getLikeButtonHolder(), true, data.isLiked(), profileColor);
            this.mLikeButtonManager.bindClickListener(h.getLikeButtonHolder(), TimelineClickListenerFactory.newLikeClickListener(this.mCallback, data, position, true));
            this.mLikesLabelManager.bind(h.getLikesLabelHolder(), data.likesCount, data.postId, data.getVineRepostRepostId(), data.userId);
            this.mCommentButtonManager.bind(h.getCommentButtonHolder(), true, false, profileColor);
            this.mCommentButtonManager.bindClickListener(h.getCommentButtonHolder(), TimelineClickListenerFactory.newCommentClickListener(data));
            this.mCommentsLabelManager.bind(h.getCommentsLabelHolder(), data.commentsCount, data.postId, data.getVineRepostRepostId(), data.userId);
            this.mShareButtonManager.bind(h.getShareButtonHolder(), showShare, data.isRevined() || data.myRepostId > 0, profileColor);
            this.mShareButtonManager.bindClickListener(h.getShareButtonHolder(), TimelineClickListenerFactory.newShareClickListener(this.mCallback, data));
            this.mSharesLabelManager.bind(h.getSharesLabelHolder(), data.revinersCount, data.postId, data.getVineRepostRepostId(), data.userId);
            this.mMoreButtonManager.bind(h.getMoreButtonHolder(), true, false, profileColor);
            this.mMoreButtonManager.bindClickListener(h.getMoreButtonHolder(), TimelineClickListenerFactory.newMoreButtonClickListener(this.mCallback, data));
            if (ClientFlagsHelper.isPlaylistEnabled(this.mContext)) {
                h.getPlaylistButtonHolder().button.setVisibility(0);
                this.mPlaylistButtonManager.bind(h.getPlaylistButtonHolder(), true, false, profileColor);
                this.mPlaylistButtonManager.bindClickListener(h.getPlaylistButtonHolder(), TimelineClickListenerFactory.newPlaylistClickListener(this.mCallback, data));
            }
            if (data.comments != null && data.comments.items != null && data.comments.items.size() > 0) {
                if (data.cachedComments == null) {
                    comments = new InlineCommentsViewManager.Comments(this.mContext, data.comments.items, data.commentsCount);
                    data.cachedComments = comments.getBuiltComments(entityColor);
                } else {
                    comments = new InlineCommentsViewManager.Comments(data.cachedComments, data.commentsCount);
                }
                this.mCommentsManager.bind(h.getCommentsHolder(), comments, data.postId, data.getVineRepostRepostId(), data.userId, entityColor);
                return;
            }
            h.getCommentsHolder().container.setVisibility(8);
        }
    }

    protected void updateLikedIcon(HomePostFooterViewHolder h, VinePost data, boolean liked) {
        h.getLikeButtonHolder().button.setSelected(liked);
        this.mLikesLabelManager.bind(h.getLikesLabelHolder(), data.likesCount, data.postId, data.getVineRepostRepostId(), data.userId);
    }

    protected void updateRevinedIcon(HomePostFooterViewHolder h, VinePost data, boolean revined) {
        h.getShareButtonHolder().button.setSelected(revined);
        this.mSharesLabelManager.bind(h.getSharesLabelHolder(), data.revinersCount, data.postId, data.getVineRepostRepostId(), data.userId);
    }
}
