package co.vine.android.feedadapter;

import android.view.View;
import co.vine.android.CommentsActivity;
import co.vine.android.R;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VinePost;
import co.vine.android.feedadapter.viewholder.CardViewHolder;

/* loaded from: classes.dex */
public class TimelineClickListenerFactory {

    public interface Callback {
        void onClosePromptClicked(TimelineItem timelineItem, CardViewHolder cardViewHolder);

        void onFeedOverlayClicked(VineFeed vineFeed);

        void onLikePost(VinePost vinePost, int i);

        void onLongformOverlayClicked(VinePost vinePost, int i);

        void onMoreButtonClicked(TimelineItem timelineItem);

        void onPlaylistButtonClicked(VinePost vinePost);

        void onPromptClicked(TimelineItem timelineItem, CardViewHolder cardViewHolder);

        void onShareClicked(VinePost vinePost);

        void onShareFeedButtonClicked(VineFeed vineFeed);

        void onUnlikePost(VinePost vinePost);
    }

    public static View.OnClickListener newLikeClickListener(final Callback callback, final VinePost post, final int position, final boolean unlikeIfLiked) {
        return new View.OnClickListener() { // from class: co.vine.android.feedadapter.TimelineClickListenerFactory.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (post.isLiked()) {
                    if (unlikeIfLiked) {
                        callback.onUnlikePost(post);
                        v.setSelected(false);
                        return;
                    }
                    return;
                }
                callback.onLikePost(post, position);
                v.setSelected(true);
            }
        };
    }

    public static View.OnClickListener newShareClickListener(final Callback callback, final VinePost post) {
        return new View.OnClickListener() { // from class: co.vine.android.feedadapter.TimelineClickListenerFactory.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                callback.onShareClicked(post);
            }
        };
    }

    public static View.OnClickListener newCommentClickListener(final VinePost post) {
        return new View.OnClickListener() { // from class: co.vine.android.feedadapter.TimelineClickListenerFactory.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                CommentsActivity.start(v.getContext(), post.postId, post.getVineRepostRepostId(), post.userId, false);
            }
        };
    }

    public static View.OnClickListener newMoreButtonClickListener(final Callback callback, final TimelineItem timelineItem) {
        return new View.OnClickListener() { // from class: co.vine.android.feedadapter.TimelineClickListenerFactory.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                callback.onMoreButtonClicked(timelineItem);
            }
        };
    }

    public static View.OnClickListener newLongformOverlayClickListener(final Callback callback, final VinePost post, final int position) {
        return new View.OnClickListener() { // from class: co.vine.android.feedadapter.TimelineClickListenerFactory.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                callback.onLongformOverlayClicked(post, position);
            }
        };
    }

    public static View.OnClickListener newPromptClickListener(final Callback callback, final TimelineItem item, final CardViewHolder holder) {
        return new View.OnClickListener() { // from class: co.vine.android.feedadapter.TimelineClickListenerFactory.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (v.getId() == R.id.close_button) {
                    callback.onClosePromptClicked(item, holder);
                } else {
                    callback.onPromptClicked(item, holder);
                }
            }
        };
    }

    public static View.OnClickListener newFeedOverlayClickedListener(final Callback callback, final VineFeed item) {
        return new View.OnClickListener() { // from class: co.vine.android.feedadapter.TimelineClickListenerFactory.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                callback.onFeedOverlayClicked(item);
            }
        };
    }

    public static View.OnClickListener newShareFeedButtonClickedListener(final Callback callback, final VineFeed item) {
        return new View.OnClickListener() { // from class: co.vine.android.feedadapter.TimelineClickListenerFactory.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                callback.onShareFeedButtonClicked(item);
            }
        };
    }

    public static View.OnClickListener newPlaylistClickListener(final Callback callback, final VinePost post) {
        return new View.OnClickListener() { // from class: co.vine.android.feedadapter.TimelineClickListenerFactory.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                callback.onPlaylistButtonClicked(post);
            }
        };
    }
}
