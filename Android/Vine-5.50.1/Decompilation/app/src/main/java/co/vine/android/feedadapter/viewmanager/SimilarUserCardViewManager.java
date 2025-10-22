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
import co.vine.android.feedadapter.viewholder.SimilarUserCardViewHolder;
import com.edisonwang.android.slog.SLog;
import com.google.common.android.base.android.Optional;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SimilarUserCardViewManager extends CardViewManager {
    protected final AppController mAppController;
    private final AvatarViewManager mAvatarManager;
    private final ImageViewManager mBackgroundManager;
    private final TimelineClickListenerFactory.Callback mCallback;
    private final Activity mContext;
    private final TextViewManager mDescriptionManager;
    private final FeedAdapterItems mItems;
    private final TimelineItemScribeActionsListener mMosaicLogger;
    private final PostMosaicViewManager mMosaicManager;
    private final VideoViewManager mPreviewManager;
    private final TextViewManager mTitleManager;
    private final FeedViewHolderCollection mViewHolders;

    private SimilarUserCardViewManager(Builder builder) {
        this.mItems = builder.items;
        this.mViewHolders = builder.viewHolders;
        this.mContext = builder.context;
        this.mCallback = builder.callback;
        this.mAppController = AppController.getInstance(this.mContext);
        this.mMosaicLogger = builder.mosaicLogger;
        this.mBackgroundManager = new ImageViewManager(this.mContext, this.mAppController, ViewType.BACKGROUND_IMAGE);
        this.mAvatarManager = new AvatarViewManager(this.mContext, this.mAppController);
        this.mMosaicManager = new PostMosaicViewManager(this.mContext, this.mAppController);
        this.mPreviewManager = new VideoViewManager(this.mContext, this.mAppController, ViewType.PREVIEW);
        this.mTitleManager = new TextViewManager(ViewType.TITLE);
        this.mDescriptionManager = new TextViewManager(ViewType.DESCRIPTION);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.SIMILAR_USER;
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public View newView(int position, View view, ViewGroup viewGroup) throws IllegalStateException {
        if (view == null || !(view.getTag() instanceof SimilarUserCardViewHolder)) {
            view = this.mContext.getLayoutInflater().inflate(R.layout.similar_viner_banner, viewGroup, false);
            SimilarUserCardViewHolder tag = new SimilarUserCardViewHolder(view);
            view.setTag(tag);
            this.mViewHolders.add(new WeakReference<>(tag));
        }
        SimilarUserCardViewHolder h = (SimilarUserCardViewHolder) view.getTag();
        TimelineItem item = this.mItems.getItem(position);
        if (item != null && item.getType() == TimelineItemType.POST_MOSAIC) {
            h.mosaic = Optional.of((VineMosaic) item);
            h.position = position;
            bind(h, h.mosaic.get());
            if (this.mMosaicLogger != null) {
                this.mMosaicLogger.onMosaicViewed((VineMosaic) item, position);
            }
        } else {
            SLog.e("Item at position: " + position + " is not a mosaic!");
        }
        return view;
    }

    public void bind(SimilarUserCardViewHolder h, VineMosaic data) throws IllegalStateException {
        if (data != null && data.mosaicItems != null) {
            ArrayList<String> thumbnailUrls = new ArrayList<>(data.mosaicItems.size());
            Iterator<TimelineItem> it = data.mosaicItems.iterator();
            while (it.hasNext()) {
                TimelineItem item = it.next();
                if (item.getType() == TimelineItemType.POST) {
                    thumbnailUrls.add(((VinePost) item).thumbnailUrl);
                }
            }
            String backgroundUrl = "";
            String previewUrl = "";
            if (data.mosaicItems.size() > 0) {
                TimelineItem post = data.mosaicItems.get(0);
                if (post.getType() == TimelineItemType.POST) {
                    backgroundUrl = ((VinePost) post).thumbnailUrl;
                    previewUrl = ((VinePost) post).videoUrl;
                }
            }
            this.mBackgroundManager.bind(h.getBackgroundHolder(), backgroundUrl, 25);
            this.mAvatarManager.bind(h.getAvatarHolder(), data.avatarUrl, true, 0);
            this.mMosaicManager.bind(h.getMosaicHolder(), thumbnailUrls);
            this.mPreviewManager.bind(h.getPreviewHolder(), previewUrl);
            this.mTitleManager.bind(h.getTitleHolder(), data.title);
            this.mDescriptionManager.bind(h.getDescriptionHolder(), data.description);
            h.setOnClickListener(TimelineClickListenerFactory.newPromptClickListener(this.mCallback, data, h));
        }
    }

    public static class Builder {
        private TimelineClickListenerFactory.Callback callback;
        private Activity context;
        private FeedAdapterItems items;
        private TimelineItemScribeActionsListener mosaicLogger;
        private FeedViewHolderCollection viewHolders;

        public SimilarUserCardViewManager build() {
            if (this.items == null || this.viewHolders == null || this.context == null || this.callback == null) {
                throw new IllegalArgumentException("One or more required variables are null");
            }
            return new SimilarUserCardViewManager(this);
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
    }
}
