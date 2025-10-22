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
import co.vine.android.feedadapter.viewholder.SuggestedFeedCardViewHolder;
import co.vine.android.service.components.Components;
import co.vine.android.widget.OnTopViewBoundListener;
import com.edisonwang.android.slog.SLog;
import com.google.common.android.base.android.Optional;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SuggestedFeedCardViewManager extends CardViewManager {
    private final AppController mAppController;
    private final TimelineClickListenerFactory.Callback mCallback;
    private final Activity mContext;
    private final TextViewManager mDescriptionManager;
    private final FeedAdapterItems mItems;
    private final TimelineItemScribeActionsListener mMosaicLogger;
    private final PostMosaicViewManager mMosaicManager;
    private final OnTopViewBoundListener mOnTopViewBoundListener;
    private final VideoViewManager mPreviewManager;
    private final TextViewManager mTitleManager;
    private final FeedViewHolderCollection mViewHolders;

    private SuggestedFeedCardViewManager(Builder builder) {
        this.mItems = builder.items;
        this.mViewHolders = builder.viewHolders;
        this.mContext = builder.context;
        this.mCallback = builder.callback;
        this.mOnTopViewBoundListener = builder.onTopViewBoundListener;
        this.mAppController = AppController.getInstance(this.mContext);
        this.mMosaicLogger = builder.mosaicLogger;
        this.mMosaicManager = new PostMosaicViewManager(this.mContext, this.mAppController);
        this.mPreviewManager = new VideoViewManager(this.mContext, this.mAppController, ViewType.PREVIEW);
        this.mTitleManager = new TextViewManager(ViewType.TITLE);
        this.mDescriptionManager = new TextViewManager(ViewType.DESCRIPTION);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.SUGGESTED_FEED;
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public View newView(int position, View view, ViewGroup viewGroup) throws IllegalStateException {
        if (view == null || !(view.getTag() instanceof SuggestedFeedCardViewHolder)) {
            view = this.mContext.getLayoutInflater().inflate(R.layout.suggested_banner, viewGroup, false);
            SuggestedFeedCardViewHolder tag = new SuggestedFeedCardViewHolder(view);
            view.setTag(tag);
            this.mViewHolders.add(new WeakReference<>(tag));
        }
        SuggestedFeedCardViewHolder h = (SuggestedFeedCardViewHolder) view.getTag();
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

    public void bind(SuggestedFeedCardViewHolder h, VineMosaic data) throws IllegalStateException {
        if (data != null && data.mosaicItems != null) {
            ArrayList<String> thumbnailUrls = new ArrayList<>(data.mosaicItems.size());
            Iterator<TimelineItem> it = data.mosaicItems.iterator();
            while (it.hasNext()) {
                TimelineItem item = it.next();
                if (item.getType() == TimelineItemType.POST) {
                    thumbnailUrls.add(((VinePost) item).thumbnailUrl);
                }
            }
            String previewUrl = "";
            if (data.mosaicItems.size() > 0) {
                TimelineItem post = data.mosaicItems.get(0);
                if (post.getType() == TimelineItemType.POST) {
                    previewUrl = ((VinePost) post).videoUrl;
                }
            }
            this.mMosaicManager.bind(h.getMosaicHolder(), thumbnailUrls);
            this.mPreviewManager.bind(h.getPreviewHolder(), previewUrl);
            this.mTitleManager.bind(h.getTitleHolder(), data.title);
            this.mDescriptionManager.bind(h.getDescriptionHolder(), data.description);
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

        public SuggestedFeedCardViewManager build() {
            if (this.items == null || this.viewHolders == null || this.context == null || this.callback == null) {
                throw new IllegalArgumentException("One or more required variables are null");
            }
            return new SuggestedFeedCardViewManager(this);
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
