package co.vine.android.feedadapter.viewmanager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.R;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.VineSolicitor;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.SolicitorCardViewHolder;
import co.vine.android.service.components.Components;
import co.vine.android.widget.OnTopViewBoundListener;
import com.edisonwang.android.slog.SLog;
import com.google.common.android.base.android.Optional;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class SolicitorCardViewManager extends CardViewManager {
    private final AppController mAppController;
    private final TimelineClickListenerFactory.Callback mCallback;
    private final Activity mContext;
    private final TextViewManager mDescriptionManager;
    private final FeedAdapterItems mItems;
    private final OnTopViewBoundListener mOnTopViewBoundListener;
    private final TextViewManager mPromptButtonManager;
    private final TextViewManager mTitleManager;
    private final FeedViewHolderCollection mViewHolders;

    private SolicitorCardViewManager(Builder builder) {
        this.mItems = builder.items;
        this.mViewHolders = builder.viewHolders;
        this.mContext = builder.context;
        this.mCallback = builder.callback;
        this.mOnTopViewBoundListener = builder.onTopViewBoundListener;
        this.mAppController = AppController.getInstance(this.mContext);
        this.mTitleManager = new TextViewManager(ViewType.TITLE);
        this.mDescriptionManager = new TextViewManager(ViewType.DESCRIPTION);
        this.mPromptButtonManager = new TextViewManager(ViewType.SOLICITOR_PROMPT_BUTTON);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.SOLICITOR;
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public View newView(int position, View view, ViewGroup viewGroup) {
        if (view == null || !(view.getTag() instanceof SolicitorCardViewHolder)) {
            view = this.mContext.getLayoutInflater().inflate(R.layout.solicitor_prompt, viewGroup, false);
            SolicitorCardViewHolder tag = new SolicitorCardViewHolder(view);
            view.setTag(tag);
            this.mViewHolders.add(new WeakReference<>(tag));
        }
        SolicitorCardViewHolder h = (SolicitorCardViewHolder) view.getTag();
        TimelineItem item = this.mItems.getItem(position);
        if (item != null && item.getType() == TimelineItemType.SOLICITOR) {
            h.solicitor = Optional.fromNullable((VineSolicitor) item);
        } else {
            SLog.e("Item at position: " + position + " is not a solicitor!");
        }
        h.position = position;
        if (position == 0 && this.mOnTopViewBoundListener != null) {
            this.mOnTopViewBoundListener.onTopViewBound();
        }
        if (h.solicitor.isPresent()) {
            bind(h, h.solicitor.get());
        }
        return view;
    }

    public void bind(SolicitorCardViewHolder h, VineSolicitor data) {
        if (data != null) {
            this.mTitleManager.bind(h.getTitleHolder(), data.title);
            this.mDescriptionManager.bind(h.getDescriptionHolder(), data.description);
            this.mPromptButtonManager.bind(h.getPromptButtonHolder(), data.buttonText);
            h.setOnClickListener(TimelineClickListenerFactory.newPromptClickListener(this.mCallback, data, h), data.closeable);
        }
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onRemoveItem(TimelineItem item) {
        if (item.getType() == TimelineItemType.SOLICITOR) {
            VineSolicitor solicitor = (VineSolicitor) item;
            Components.userInteractionsComponent().closePrompt(this.mAppController, solicitor.reference);
        }
    }

    public static class Builder {
        private TimelineClickListenerFactory.Callback callback;
        private Activity context;
        private FeedAdapterItems items;
        private OnTopViewBoundListener onTopViewBoundListener;
        private FeedViewHolderCollection viewHolders;

        public SolicitorCardViewManager build() {
            if (this.items == null || this.viewHolders == null || this.context == null || this.callback == null) {
                throw new IllegalArgumentException("One or more required variables are null");
            }
            return new SolicitorCardViewManager(this);
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
