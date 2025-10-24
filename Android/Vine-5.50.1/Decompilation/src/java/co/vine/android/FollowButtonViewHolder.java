package co.vine.android;

import android.widget.Button;
import co.vine.android.api.VineUser;

/* loaded from: classes.dex */
public class FollowButtonViewHolder {
    public Button followButton;
    public boolean following;
    public VineUser user;
    public long userId;

    public FollowButtonViewHolder(VineUser user, Button followButton) {
        this.userId = user.userId;
        this.following = user.following == 1;
        this.user = user;
        this.followButton = followButton;
    }

    public FollowButtonViewHolder(long userId, boolean following) {
        this.userId = userId;
        this.following = following;
    }
}
