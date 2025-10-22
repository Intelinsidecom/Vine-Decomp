package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class SolicitorCardViewHolder extends CardViewHolder {
    private final ButtonViewHolder mCloseButtonHolder;
    private final TextViewHolder mDescriptionHolder;
    private final TextViewHolder mPromptButtonHolder;
    private final TextViewHolder mTitleHolder;

    public SolicitorCardViewHolder(View view) {
        super(view);
        this.mCloseButtonHolder = new ButtonViewHolder(view, ViewType.CLOSE_BUTTON, R.id.close_button);
        this.mTitleHolder = new TextViewHolder(view, ViewType.TITLE, R.id.title);
        this.mDescriptionHolder = new TextViewHolder(view, ViewType.DESCRIPTION, R.id.description);
        this.mPromptButtonHolder = new TextViewHolder(view, ViewType.SOLICITOR_PROMPT_BUTTON, R.id.solicitor_prompt_button_title);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.SOLICITOR;
    }

    public TextViewHolder getTitleHolder() {
        return this.mTitleHolder;
    }

    public TextViewHolder getDescriptionHolder() {
        return this.mDescriptionHolder;
    }

    public TextViewHolder getPromptButtonHolder() {
        return this.mPromptButtonHolder;
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
