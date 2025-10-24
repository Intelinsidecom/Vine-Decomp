package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class SimilarUserCardViewHolder extends CardViewHolder {
    private final ImageViewHolder mAvatarHolder;
    private final ImageViewHolder mBackgroundHolder;
    private final TextViewHolder mDescriptionHolder;
    private final PostMosaicViewHolder mMosaicHolder;
    private final VideoViewHolder mPreviewHolder;
    private final TextViewHolder mTitleHolder;

    public SimilarUserCardViewHolder(View view) {
        super(view);
        this.mBackgroundHolder = new ImageViewHolder(view, ViewType.BACKGROUND_IMAGE, R.id.background_image);
        this.mAvatarHolder = new ImageViewHolder(view, ViewType.AVATAR, R.id.user_image);
        this.mMosaicHolder = new PostMosaicViewHolder(view);
        this.mPreviewHolder = new VideoViewHolder(view, ViewType.PREVIEW, R.id.preview);
        this.mTitleHolder = new TextViewHolder(view, ViewType.TITLE, R.id.title);
        this.mDescriptionHolder = new TextViewHolder(view, ViewType.DESCRIPTION, R.id.description);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.SIMILAR_USER;
    }

    public ImageViewHolder getBackgroundHolder() {
        return this.mBackgroundHolder;
    }

    public ImageViewHolder getAvatarHolder() {
        return this.mAvatarHolder;
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
    }

    @Override // co.vine.android.feedadapter.ViewGroupHelper.ViewChildViewHolder
    public View getMeasuringView() {
        return this.view;
    }
}
