package co.vine.android.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.cache.image.ImageKey;

/* loaded from: classes.dex */
public class UserViewHolder {
    public ImageKey avatarUrl;
    public View divider;
    public ImageView image;
    public ViewGroup root;
    public TextView twitterScreenname;
    public ImageView twitterVerified;
    public long userId;
    public TextView username;
    public ImageView verified;

    public UserViewHolder(View view) {
        this.root = (ViewGroup) view.findViewById(R.id.root_layout);
        this.username = (TextView) view.findViewById(R.id.username);
        this.image = (ImageView) view.findViewById(R.id.user_image);
        this.verified = (ImageView) view.findViewById(R.id.user_verified);
        this.divider = view.findViewById(R.id.divider);
        this.twitterScreenname = (TextView) view.findViewById(R.id.twitterScreenname);
        this.twitterVerified = (ImageView) view.findViewById(R.id.twitter_verified);
    }
}
