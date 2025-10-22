package co.vine.android;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import co.vine.android.widget.UserViewHolder;

/* loaded from: classes.dex */
public class FollowableUserViewHolder extends UserViewHolder {
    public ImageButton followButton;
    public TextView suggestionByline;

    public FollowableUserViewHolder(View view) {
        super(view);
        this.followButton = (ImageButton) view.findViewById(R.id.user_row_btn_follow);
        this.suggestionByline = (TextView) view.findViewById(R.id.suggestion_byline);
    }
}
