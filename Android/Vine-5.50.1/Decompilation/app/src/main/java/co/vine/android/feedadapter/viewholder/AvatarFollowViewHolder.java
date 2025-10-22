package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.widget.VineToggleButton;

/* loaded from: classes.dex */
public final class AvatarFollowViewHolder extends ButtonViewHolder {
    public final VineToggleButton toggle;

    public AvatarFollowViewHolder(View view) {
        super(view, ViewType.AVATAR_FOLLOW, R.id.avatar_follow_container);
        this.toggle = (VineToggleButton) view.findViewById(R.id.follow_button);
    }
}
