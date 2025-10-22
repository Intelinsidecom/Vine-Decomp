package co.vine.android.feedadapter;

import co.vine.android.PendingRequest;
import co.vine.android.PendingRequestHelper;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineRepost;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.FeedAdapter;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.CardViewHolder;
import co.vine.android.service.components.feedactions.FeedActionsListener;
import co.vine.android.service.components.postactions.PostActionsListener;

/* loaded from: classes.dex */
public class TimelineActionResponseHandler implements FeedActionsListener, PostActionsListener {
    private AppController mAppController;
    private FeedAdapter mFeedAdapter;
    private PendingRequestHelper mPendingRequestHelper;
    private FeedAdapterItems mPosts;
    private FeedViewHolderCollection mViewHolderCollection;

    public TimelineActionResponseHandler(FeedAdapterItems posts, AppController appController, PendingRequestHelper pendingRequestHelper, FeedViewHolderCollection viewHolderCollection, FeedAdapter adapter) {
        this.mPosts = posts;
        this.mAppController = appController;
        this.mPendingRequestHelper = pendingRequestHelper;
        this.mViewHolderCollection = viewHolderCollection;
        this.mFeedAdapter = adapter;
    }

    @Override // co.vine.android.service.components.postactions.PostActionsListener
    public void onLikePost(String reqId, int statusCode, String reasonPhrase, long postId) {
        CardViewHolder holder;
        PendingRequest req = this.mPendingRequestHelper.removeRequest(reqId);
        if (req != null && (holder = this.mViewHolderCollection.getViewHolderForPostId(postId)) != null && holder.getType() == ViewType.POST && holder.post.isPresent()) {
            VinePost post = holder.post.get();
            if (statusCode != 200) {
                post.removeMeLike(this.mAppController.getActiveId());
            }
        }
    }

    @Override // co.vine.android.service.components.postactions.PostActionsListener
    public void onUnlikePost(String reqId, int statusCode, String reasonPhrase, long postId) {
        CardViewHolder holder;
        PendingRequest req = this.mPendingRequestHelper.removeRequest(reqId);
        if (req != null && (holder = this.mViewHolderCollection.getViewHolderForPostId(postId)) != null && holder.getType() == ViewType.POST && holder.post.isPresent()) {
            VinePost post = holder.post.get();
            if (statusCode == 404) {
                post.removeMeLike(this.mAppController.getActiveId());
            } else if (statusCode != 200) {
                post.addMeLike(this.mAppController.getActiveSession());
            }
        }
    }

    @Override // co.vine.android.service.components.postactions.PostActionsListener
    public void onRevine(String reqId, int statusCode, String reasonPhrase, long postId, VineRepost repost) {
        CardViewHolder holder;
        PendingRequest req = this.mPendingRequestHelper.removeRequest(reqId);
        if (req != null && (holder = this.mViewHolderCollection.getViewHolderForPostId(postId)) != null && holder.getType() == ViewType.POST && holder.post.isPresent()) {
            VinePost post = holder.post.get();
            if (statusCode == 200) {
                post.updateRevined(repost, true);
            } else {
                post.setFlagRevined(false);
            }
        }
    }

    @Override // co.vine.android.service.components.postactions.PostActionsListener
    public void onUnrevine(String reqId, int statusCode, String reasonPhrase, long postId) {
        CardViewHolder holder;
        PendingRequest req = this.mPendingRequestHelper.removeRequest(reqId);
        if (req != null && (holder = this.mViewHolderCollection.getViewHolderForPostId(postId)) != null && holder.getType() == ViewType.POST && holder.post.isPresent()) {
            VinePost post = holder.post.get();
            if (statusCode == 200) {
                if (post.byline == null && post.repost != null && post.repost.userId == this.mAppController.getActiveId()) {
                    this.mPosts.removeWithId(post.postId);
                }
                post.updateRevined(null, false);
                return;
            }
            post.setFlagRevined(true);
        }
    }

    @Override // co.vine.android.service.components.postactions.PostActionsListener
    public void onPostEditCaption(String reqId, int statusCode, String reasonPhrase) {
    }

    @Override // co.vine.android.service.components.feedactions.FeedActionsListener
    public void onShareFeed(String reqId, int statusCode, String reasonPhrase) {
    }

    @Override // co.vine.android.service.components.feedactions.FeedActionsListener
    public void onDeleteFeed(String reqId, int statusCode, String reasonPhrase, long feedId) {
        this.mPendingRequestHelper.removeRequest(reqId);
        if (statusCode == 200 && this.mFeedAdapter != null) {
            this.mFeedAdapter.removeItem(feedId);
        }
    }
}
