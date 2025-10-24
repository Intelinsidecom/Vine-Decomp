package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class FeedFooterViewHolder implements ViewHolder {
    private final TextViewHolder mDescriptionHolder;
    private final ButtonViewHolder mMoreButtonHolder;
    private final ButtonViewHolder mShareChannelHolder;

    public FeedFooterViewHolder(View view) {
        this.mShareChannelHolder = new ButtonViewHolder(view, ViewType.SHARE_CHANNEL_BUTTON, R.id.share_channel_button);
        this.mDescriptionHolder = new TextViewHolder(view, ViewType.DESCRIPTION, R.id.description);
        this.mMoreButtonHolder = new ButtonViewHolder(view, ViewType.MORE_OPTIONS, R.id.more_options);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.FEED_FOOTER;
    }

    public TextViewHolder getDescriptionHolder() {
        return this.mDescriptionHolder;
    }

    public ButtonViewHolder getMoreButtonHolder() {
        return this.mMoreButtonHolder;
    }

    public ButtonViewHolder getShareChannelHolder() {
        return this.mShareChannelHolder;
    }
}
