package co.vine.android.feedadapter.viewmanager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.R;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.VineUrlAction;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.UrlActionCardViewHolder;
import co.vine.android.service.components.Components;
import co.vine.android.widget.OnTopViewBoundListener;
import com.edisonwang.android.slog.SLog;
import com.google.common.android.base.android.Optional;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class UrlActionCardViewManager extends CardViewManager {
    private final ActionButtonViewManager mActionButtonManager;
    private final AppController mAppController;
    private final ImageViewManager mBackgroundImageManager;
    private final VideoViewManager mBackgroundVideoManager;
    private final TimelineClickListenerFactory.Callback mCallback;
    private final Activity mContext;
    private final TextViewManager mDescriptionManager;
    private final FeedAdapterItems mItems;
    private final OnTopViewBoundListener mOnTopViewBoundListener;
    private final TextViewManager mTitleManager;
    private final FeedViewHolderCollection mViewHolders;

    private UrlActionCardViewManager(Builder builder) {
        this.mItems = builder.items;
        this.mViewHolders = builder.viewHolders;
        this.mContext = builder.context;
        this.mCallback = builder.callback;
        this.mOnTopViewBoundListener = builder.onTopViewBoundListener;
        this.mAppController = AppController.getInstance(this.mContext);
        this.mBackgroundImageManager = new ImageViewManager(this.mContext, this.mAppController, ViewType.BACKGROUND_IMAGE);
        this.mBackgroundVideoManager = new VideoViewManager(this.mContext, this.mAppController, ViewType.BACKGROUND_VIDEO);
        this.mTitleManager = new TextViewManager(ViewType.TITLE);
        this.mDescriptionManager = new TextViewManager(ViewType.DESCRIPTION);
        this.mActionButtonManager = new ActionButtonViewManager(this.mContext, this.mAppController);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.URL_ACTION;
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public View newView(int position, View view, ViewGroup viewGroup) throws IllegalStateException {
        if (view == null || !(view.getTag() instanceof UrlActionCardViewHolder)) {
            view = this.mContext.getLayoutInflater().inflate(R.layout.url_action_prompt, viewGroup, false);
            UrlActionCardViewHolder tag = new UrlActionCardViewHolder(view);
            view.setTag(tag);
            this.mViewHolders.add(new WeakReference<>(tag));
        }
        UrlActionCardViewHolder h = (UrlActionCardViewHolder) view.getTag();
        TimelineItem item = this.mItems.getItem(position);
        if (item != null && item.getType() == TimelineItemType.URL_ACTION) {
            h.urlAction = Optional.fromNullable((VineUrlAction) item);
        } else {
            SLog.e("Item at position: " + position + " is not a url action!");
        }
        h.position = position;
        if (position == 0 && this.mOnTopViewBoundListener != null) {
            this.mOnTopViewBoundListener.onTopViewBound();
        }
        if (h.urlAction.isPresent()) {
            bind(h, h.urlAction.get());
        }
        return view;
    }

    public void bind(UrlActionCardViewHolder h, VineUrlAction data) throws IllegalStateException {
        if (data != null) {
            this.mBackgroundImageManager.bind(h.getBackgroundImageHolder(), data.backgroundImageUrl);
            this.mBackgroundVideoManager.bind(h.getBackgroundVideoHolder(), data.backgroundVideoUrl);
            this.mTitleManager.bind(h.getTitleHolder(), data.title);
            this.mDescriptionManager.bind(h.getDescriptionHolder(), data.description);
            this.mActionButtonManager.bind(h.getActionButtonHolder(), data.actionTitle, data.actionIconUrl);
            h.setOnClickListener(TimelineClickListenerFactory.newPromptClickListener(this.mCallback, data, h), data.closeable);
        }
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onRemoveItem(TimelineItem item) {
        if (item.getType() == TimelineItemType.URL_ACTION) {
            VineUrlAction urlAction = (VineUrlAction) item;
            Components.userInteractionsComponent().closePrompt(this.mAppController, urlAction.reference);
        }
    }

    public static class Builder {
        private TimelineClickListenerFactory.Callback callback;
        private Activity context;
        private FeedAdapterItems items;
        private OnTopViewBoundListener onTopViewBoundListener;
        private FeedViewHolderCollection viewHolders;

        public UrlActionCardViewManager build() {
            if (this.items == null || this.viewHolders == null || this.context == null || this.callback == null) {
                throw new IllegalArgumentException("One or more required variables are null");
            }
            return new UrlActionCardViewManager(this);
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

        public Builder onTopViewBoundListener(OnTopViewBoundListener listener) {
            this.onTopViewBoundListener = listener;
            return this;
        }
    }
}
