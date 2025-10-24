package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import co.vine.android.ChannelActivity;
import co.vine.android.R;
import co.vine.android.api.VineByline;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VineRepost;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.FeedNotifier;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.BylineViewHolder;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.ResourceLoader;

/* loaded from: classes.dex */
public class BylineViewManager implements ViewManager {
    private final AppController mAppController;
    private final Context mContext;
    private final FeedNotifier mFeedNotifier;

    public BylineViewManager(Context context, AppController appController, FeedNotifier feedNotifier) {
        this.mContext = context;
        this.mAppController = appController;
        this.mFeedNotifier = feedNotifier;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.BYLINE;
    }

    public void bind(BylineViewHolder h, VineByline byline) {
        if (byline == null || byline.body == null || byline.bylineAction != null) {
            h.container.setVisibility(8);
            return;
        }
        h.container.setOnClickListener(null);
        h.container.setVisibility(0);
        h.icon.setVisibility(0);
        if (h.icon != null) {
            if (!TextUtils.isEmpty(byline.iconUrl)) {
                ResourceLoader bitmapLoader = new ResourceLoader(this.mContext, this.mAppController);
                bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(h.icon), byline.iconUrl);
            } else {
                h.icon.setImageResource(R.drawable.vine_logo);
            }
        }
        h.text.setTextColor(Color.parseColor("#59000000"));
        h.gearIcon.setImageDrawable(null);
        if (h.text == null) {
            h.container.setVisibility(8);
        } else {
            h.text.setText(byline.body);
        }
    }

    public void bind(BylineViewHolder h, VineFeed feed) {
        if (feed == null || feed.user == null) {
            h.container.setVisibility(8);
            return;
        }
        h.container.setOnClickListener(null);
        h.container.setVisibility(0);
        h.icon.setVisibility(8);
        h.gearIcon.setVisibility(8);
        h.text.setText(this.mContext.getString(R.string.feed_item_byline, feed.user.username));
        h.text.setTextColor(Color.parseColor("#59000000"));
    }

    public void bind(BylineViewHolder h, VineRepost repost, boolean isYou) {
        if (repost == null) {
            h.container.setVisibility(8);
            return;
        }
        h.container.setOnClickListener(null);
        h.container.setVisibility(0);
        h.icon.setVisibility(0);
        h.icon.setImageResource(R.drawable.ic_revine);
        h.text.setTextColor(Color.parseColor("#59000000"));
        h.gearIcon.setImageDrawable(null);
        if (h.text == null) {
            h.container.setVisibility(8);
            return;
        }
        if (isYou) {
            h.text.setText(this.mContext.getString(R.string.revine_line_me));
            return;
        }
        if (repost.username != null) {
            String revinedBy = this.mContext.getString(R.string.revine_line, repost.username);
            SpannableStringBuilder revinedBySb = new SpannableStringBuilder(revinedBy);
            h.text.setMovementMethod(LinkMovementMethod.getInstance());
            h.text.setText(revinedBySb);
            return;
        }
        h.container.setVisibility(8);
    }

    public void bindClickListener(BylineViewHolder h, final String url) {
        if (url != null) {
            View.OnClickListener listener = new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.BylineViewManager.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    LinkDispatcher.dispatch(url, BylineViewManager.this.mContext);
                }
            };
            h.container.setOnClickListener(listener);
            h.icon.setOnClickListener(listener);
            h.text.setOnClickListener(listener);
        }
    }

    public void bindClickListener(BylineViewHolder h, final long userId) {
        View.OnClickListener listener = new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.BylineViewManager.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ChannelActivity.startProfile(v.getContext(), userId, "timeline");
            }
        };
        h.container.setOnClickListener(listener);
        h.icon.setOnClickListener(listener);
        h.text.setOnClickListener(listener);
    }
}
