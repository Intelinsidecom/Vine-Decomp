package co.vine.android.feedadapter.viewmanager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.R;
import co.vine.android.TimelineItemScribeActionsListener;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.VineMosaic;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.SuggestedFeedVideoGridCardViewHolder;
import co.vine.android.service.components.Components;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.widget.OnTopViewBoundListener;
import com.edisonwang.android.slog.SLog;
import com.google.common.android.base.android.Optional;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes.dex */
public class SuggestedFeedVideoGridCardViewManager extends CardViewManager {
    private static int VIDEO_COUNT;
    private final AppController mAppController;
    private final TimelineClickListenerFactory.Callback mCallback;
    private final int mCardHeight;
    private final int mCardWidth;
    private final Activity mContext;
    private final TextViewManager mDayManager;
    private final FeedAdapterItems mItems;
    private final TimelineItemScribeActionsListener mMosaicLogger;
    private final PostMosaicViewManager mMosaicManager;
    private final OnTopViewBoundListener mOnTopViewBoundListener;
    private final PostMosaicVideoGridViewManager mVideoGridManager;
    private final int mVideoHeightLarge;
    private final int mVideoHeightSmall;
    private final int mVideoWidthSmall;
    private final FeedViewHolderCollection mViewHolders;

    private SuggestedFeedVideoGridCardViewManager(Builder builder) {
        this.mItems = builder.items;
        this.mViewHolders = builder.viewHolders;
        this.mContext = builder.context;
        this.mCallback = builder.callback;
        this.mOnTopViewBoundListener = builder.onTopViewBoundListener;
        if (ClientFlagsHelper.autoPlayPreviewVids(this.mContext)) {
            VIDEO_COUNT = 5;
        } else {
            VIDEO_COUNT = 1;
        }
        this.mCardWidth = this.mContext.getResources().getDisplayMetrics().widthPixels;
        this.mCardHeight = (int) (0.4d * this.mCardWidth);
        this.mVideoWidthSmall = (int) (0.3d * this.mCardWidth);
        this.mVideoHeightSmall = this.mCardHeight / 3;
        this.mVideoHeightLarge = this.mVideoHeightSmall * 2;
        this.mAppController = AppController.getInstance(this.mContext);
        this.mMosaicLogger = builder.mosaicLogger;
        this.mMosaicManager = new PostMosaicViewManager(this.mContext, this.mAppController);
        this.mVideoGridManager = new PostMosaicVideoGridViewManager(this.mContext, this.mAppController, VIDEO_COUNT, this.mVideoWidthSmall, this.mVideoHeightSmall, this.mVideoHeightLarge);
        this.mDayManager = new TextViewManager(ViewType.DAY);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.SUGGESTED_FEED;
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public View newView(int position, View view, ViewGroup viewGroup) throws IllegalStateException {
        if (view == null || !(view.getTag() instanceof SuggestedFeedVideoGridCardViewManager)) {
            view = this.mContext.getLayoutInflater().inflate(R.layout.suggested_banner_video_grid_card, viewGroup, false);
            View card = view.findViewById(R.id.suggested_banner_video_grid_card);
            ViewGroup.LayoutParams params = card.getLayoutParams();
            params.height = this.mCardHeight;
            card.setLayoutParams(params);
            SuggestedFeedVideoGridCardViewHolder tag = new SuggestedFeedVideoGridCardViewHolder(view, VIDEO_COUNT);
            view.setTag(tag);
            this.mViewHolders.add(new WeakReference<>(tag));
        }
        SuggestedFeedVideoGridCardViewHolder h = (SuggestedFeedVideoGridCardViewHolder) view.getTag();
        TimelineItem item = this.mItems.getItem(position);
        if (item != null && item.getType() == TimelineItemType.POST_MOSAIC) {
            h.mosaic = Optional.of((VineMosaic) item);
            h.position = position;
            if (position == 0 && this.mOnTopViewBoundListener != null) {
                this.mOnTopViewBoundListener.onTopViewBound();
            }
            bind(h, h.mosaic.get());
            if (this.mMosaicLogger != null) {
                this.mMosaicLogger.onMosaicViewed((VineMosaic) item, position);
            }
        } else {
            SLog.e("Item at position: " + position + " is not a mosaic!");
        }
        return view;
    }

    public void bind(SuggestedFeedVideoGridCardViewHolder h, VineMosaic data) throws IllegalStateException {
        if (data != null && data.mosaicItems != null) {
            ArrayList<String> thumbnailUrls = new ArrayList<>(data.mosaicItems.size());
            ArrayList<String> videoUrls = new ArrayList<>(data.mosaicItems.size());
            if (ClientFlagsHelper.autoPlayPreviewVids(this.mContext)) {
                Iterator<TimelineItem> it = data.mosaicItems.iterator();
                while (it.hasNext()) {
                    TimelineItem item = it.next();
                    if (item.getType() == TimelineItemType.POST) {
                        thumbnailUrls.add(((VinePost) item).thumbnailLowUrl);
                        videoUrls.add(((VinePost) item).videoPreview);
                    }
                }
            } else {
                Iterator<TimelineItem> it2 = data.mosaicItems.iterator();
                while (it2.hasNext()) {
                    TimelineItem item2 = it2.next();
                    if (item2.getType() == TimelineItemType.POST) {
                        thumbnailUrls.add(((VinePost) item2).thumbnailUrl);
                        videoUrls.add(((VinePost) item2).videoLowUrl);
                    }
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("EE.", Locale.getDefault());
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            this.mMosaicManager.bind(h.getMosaicHolder(), thumbnailUrls);
            this.mVideoGridManager.bind(h.getVideoGridHolder(), videoUrls);
            this.mDayManager.bind(h.getDayHolder(), dayOfTheWeek);
            h.setOnClickListener(TimelineClickListenerFactory.newPromptClickListener(this.mCallback, data, h));
        }
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onRemoveItem(TimelineItem item) {
        if (item.getType() == TimelineItemType.POST_MOSAIC) {
            VineMosaic mosaic = (VineMosaic) item;
            Components.userInteractionsComponent().closePrompt(this.mAppController, mosaic.reference);
        }
    }

    public static class Builder {
        private TimelineClickListenerFactory.Callback callback;
        private Activity context;
        private FeedAdapterItems items;
        private TimelineItemScribeActionsListener mosaicLogger;
        private OnTopViewBoundListener onTopViewBoundListener;
        private FeedViewHolderCollection viewHolders;

        public SuggestedFeedVideoGridCardViewManager build() {
            if (this.items == null || this.viewHolders == null || this.context == null || this.callback == null) {
                throw new IllegalArgumentException("One or more required variables are null");
            }
            return new SuggestedFeedVideoGridCardViewManager(this);
        }

        public Builder items(FeedAdapterItems items) {
            this.items = items;
            return this;
        }

        public Builder viewHolders(FeedViewHolderCollection viewHolders) {
            this.viewHolders = viewHolders;
            return this;
        }

        public Builder context(Activity context) {
            this.context = context;
            return this;
        }

        public Builder callback(TimelineClickListenerFactory.Callback callback) {
            this.callback = callback;
            return this;
        }

        public Builder mosaicLogger(TimelineItemScribeActionsListener logger) {
            this.mosaicLogger = logger;
            return this;
        }

        public Builder onTopViewBoundListener(OnTopViewBoundListener listener) {
            this.onTopViewBoundListener = listener;
            return this;
        }
    }
}
