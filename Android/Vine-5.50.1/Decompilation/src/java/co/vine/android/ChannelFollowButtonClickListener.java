package co.vine.android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import co.vine.android.api.VineChannel;
import co.vine.android.client.AppController;

/* loaded from: classes.dex */
public final class ChannelFollowButtonClickListener implements View.OnClickListener {
    private final AppController mAppController;
    private final Friendships mFollowingCache;

    public ChannelFollowButtonClickListener(Context context, AppController appController, Friendships followingCache) {
        this.mAppController = appController;
        this.mFollowingCache = followingCache;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        VineChannel channel = (VineChannel) view.getTag();
        channel.following = !channel.following;
        boolean following = channel.following;
        this.mAppController.followChannel(channel.channelId, channel.following);
        if (following) {
            view.setBackgroundResource(R.drawable.channel_following_ic);
            this.mFollowingCache.addChannelFollowing(channel.channelId);
        } else {
            view.setBackgroundResource(R.drawable.channel_follow_ic);
            this.mFollowingCache.removeChannelFollowing(channel.channelId);
        }
        if (channel.backgroundColor != null && !channel.backgroundColor.isEmpty()) {
            view.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor(channel.backgroundColor), PorterDuff.Mode.SRC_ATOP));
        }
    }
}
