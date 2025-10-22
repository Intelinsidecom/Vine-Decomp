package co.vine.android.widget;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.cache.image.ImageKey;

/* loaded from: classes.dex */
public final class ActivityViewHolder {
    public ImageView avatar;
    public ImageKey avatarImageKey;
    public TextView contentLine;
    public TextView dateLine;
    public View extraSpacer;
    public View followButton;
    public TextView headerText;
    public View headerView;
    public ImageView milestoneBackground;
    public ImageKey milestoneBackgroundImageKey;
    public TextView milestoneDescription;
    public ImageView milestoneIcon;
    public ImageKey milestoneIconImageKey;
    public ImageView milestoneImage;
    public ImageView milestoneImageFrame;
    public ImageKey milestoneImageImageKey;
    public View milestoneOverlay;
    public TextView milestoneTitle;
    public ActivityNotification notification;
    public ImageView thumbnail;
    public ImageKey thumbnailImageKey;
    public View topSpacer;
    public TextView twitterScreenname;
    public ImageView typeIcon;
    public TextView username;
    public ImageView verified;

    public static final class ActivityNotification {
        private final String link;
        private final long postId;

        public ActivityNotification(long postId, String link) {
            this.postId = postId;
            this.link = link;
        }

        public long getPostId() {
            return this.postId;
        }

        public String getLink() {
            return this.link;
        }
    }

    public ActivityViewHolder(View v) {
        this.typeIcon = (ImageView) v.findViewById(R.id.activity_type_icon);
        this.avatar = (ImageView) v.findViewById(R.id.user_image);
        this.thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        this.followButton = v.findViewById(R.id.follow);
        this.contentLine = (TextView) v.findViewById(R.id.content_line);
        this.dateLine = (TextView) v.findViewById(R.id.date_line);
        this.username = (TextView) v.findViewById(R.id.username);
        this.verified = (ImageView) v.findViewById(R.id.verified);
        this.twitterScreenname = (TextView) v.findViewById(R.id.twitterScreenname);
        this.milestoneBackground = (ImageView) v.findViewById(R.id.milestone_background);
        this.milestoneOverlay = v.findViewById(R.id.milestone_color_overlay);
        this.milestoneImage = (ImageView) v.findViewById(R.id.milestone_image);
        this.milestoneImageFrame = (ImageView) v.findViewById(R.id.milestone_image_frame);
        this.milestoneIcon = (ImageView) v.findViewById(R.id.milestone_icon);
        this.milestoneTitle = (TextView) v.findViewById(R.id.milestone_title);
        this.milestoneDescription = (TextView) v.findViewById(R.id.milestone_secondary);
        this.headerView = v.findViewById(R.id.header);
        this.headerText = (TextView) v.findViewById(R.id.header_text);
        this.topSpacer = v.findViewById(R.id.spacer_top);
        this.extraSpacer = v.findViewById(R.id.spacer_extra);
    }
}
