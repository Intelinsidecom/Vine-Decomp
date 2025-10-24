package co.vine.android;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.cache.image.ImageKey;

/* loaded from: classes.dex */
public class CommentViewHolder {
    String commentId;
    TextView content;
    ImageKey imageKey;
    ImageView profileImage;
    View topSpacer;
    TextView twitterScreenname;
    long userId;
    String username;
    TextView usernameTextview;
    ImageView verified;
}
