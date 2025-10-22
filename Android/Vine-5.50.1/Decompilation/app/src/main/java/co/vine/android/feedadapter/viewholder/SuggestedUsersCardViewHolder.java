package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class SuggestedUsersCardViewHolder extends CardViewHolder {
    private final ButtonViewHolder mCloseButtonHolder;
    private final TextViewHolder mDescriptionHolder;
    private final UserMosaicViewHolder mMosaicHolder;
    private final TextViewHolder mTitleHolder;

    public SuggestedUsersCardViewHolder(View view) {
        super(view);
        this.mMosaicHolder = new UserMosaicViewHolder(view);
        this.mCloseButtonHolder = new ButtonViewHolder(view, ViewType.CLOSE_BUTTON, R.id.close_button);
        this.mTitleHolder = new TextViewHolder(view, ViewType.TITLE, R.id.title);
        this.mDescriptionHolder = new TextViewHolder(view, ViewType.DESCRIPTION, R.id.description);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.SUGGESTED_FEED;
    }

    public UserMosaicViewHolder getMosaicHolder() {
        return this.mMosaicHolder;
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
