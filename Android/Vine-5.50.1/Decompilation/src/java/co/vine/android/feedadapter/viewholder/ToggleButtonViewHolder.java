package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public final class ToggleButtonViewHolder extends ButtonViewHolder {
    public final View selectedView;
    public final View unselectedView;

    public ToggleButtonViewHolder(View view) {
        super(view, ViewType.FOLLOW_TOGGLE, R.id.welcome_follow_button);
        this.selectedView = view.findViewById(R.id.welcome_unfollow);
        this.unselectedView = view.findViewById(R.id.welcome_follow);
    }
}
