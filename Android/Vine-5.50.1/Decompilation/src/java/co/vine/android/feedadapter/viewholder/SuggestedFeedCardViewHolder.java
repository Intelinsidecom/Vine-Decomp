package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class SuggestedFeedCardViewHolder extends CardViewHolder {
    private final ButtonViewHolder mCloseButtonHolder;
    private final TextViewHolder mDescriptionHolder;
    private final PostMosaicViewHolder mMosaicHolder;
    private final VideoViewHolder mPreviewHolder;
    private final TextViewHolder mTitleHolder;

    public SuggestedFeedCardViewHolder(View view) {
        super(view);
        this.mMosaicHolder = new PostMosaicViewHolder(view);
        this.mPreviewHolder = new VideoViewHolder(view, ViewType.PREVIEW, R.id.preview);
        this.mCloseButtonHolder = new ButtonViewHolder(view, ViewType.CLOSE_BUTTON, R.id.close_button);
        this.mTitleHolder = new TextViewHolder(view, ViewType.TITLE, R.id.title);
        this.mDescriptionHolder = new TextViewHolder(view, ViewType.DESCRIPTION, R.id.description);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.SUGGESTED_FEED;
    }

    public PostMosaicViewHolder getMosaicHolder() {
        return this.mMosaicHolder;
    }

    public VideoViewHolder getPreviewHolder() {
        return this.mPreviewHolder;
    }

    public TextViewHolder getTitleHolder() {
        return this.mTitleHolder;
    }

    public TextViewHolder getDescriptionHolder() {
        return this.mDescriptionHolder;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.view.setOnClickListener(listener);
        this.mCloseButtonHolder.button.setOnClickListener(listener);
    }

    @Override // co.vine.android.feedadapter.ViewGroupHelper.ViewChildViewHolder
    public View getMeasuringView() {
        return this.view;
    }
}
