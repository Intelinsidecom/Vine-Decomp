package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class UrlActionCardViewHolder extends CardViewHolder {
    private final ActionButtonViewHolder mActionButtonHolder;
    private final ImageViewHolder mBackgroundImageHolder;
    private final VideoViewHolder mBackgroundVideoHolder;
    private final ButtonViewHolder mCloseButtonHolder;
    private final TextViewHolder mDescriptionHolder;
    private final TextViewHolder mTitleHolder;

    public UrlActionCardViewHolder(View view) {
        super(view);
        this.mBackgroundImageHolder = new ImageViewHolder(view, ViewType.BACKGROUND_IMAGE, R.id.background_image);
        this.mBackgroundVideoHolder = new VideoViewHolder(view, ViewType.BACKGROUND_VIDEO, R.id.background_video);
        this.mCloseButtonHolder = new ButtonViewHolder(view, ViewType.CLOSE_BUTTON, R.id.close_button);
        this.mTitleHolder = new TextViewHolder(view, ViewType.TITLE, R.id.title);
        this.mDescriptionHolder = new TextViewHolder(view, ViewType.DESCRIPTION, R.id.description);
        this.mActionButtonHolder = new ActionButtonViewHolder(view);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.URL_ACTION;
    }

    public ImageViewHolder getBackgroundImageHolder() {
        return this.mBackgroundImageHolder;
    }

    public VideoViewHolder getBackgroundVideoHolder() {
        return this.mBackgroundVideoHolder;
    }

    public TextViewHolder getTitleHolder() {
        return this.mTitleHolder;
    }

    public TextViewHolder getDescriptionHolder() {
        return this.mDescriptionHolder;
    }

    public ActionButtonViewHolder getActionButtonHolder() {
        return this.mActionButtonHolder;
    }

    public void setOnClickListener(View.OnClickListener listener, boolean closeable) {
        this.view.setOnClickListener(listener);
        if (closeable) {
            this.mCloseButtonHolder.button.setVisibility(0);
            this.mCloseButtonHolder.button.setOnClickListener(listener);
        } else {
            this.mCloseButtonHolder.button.setVisibility(8);
        }
    }

    @Override // co.vine.android.feedadapter.ViewGroupHelper.ViewChildViewHolder
    public View getMeasuringView() {
        return this.view;
    }
}
