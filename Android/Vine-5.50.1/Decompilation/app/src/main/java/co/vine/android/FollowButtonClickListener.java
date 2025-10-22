package co.vine.android;

import android.content.Context;
import android.view.View;
import co.vine.android.client.AppController;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.service.components.Components;

/* loaded from: classes.dex */
public final class FollowButtonClickListener implements View.OnClickListener {
    private final AppController mAppController;
    private final FollowScribeActionsLogger mFollowScribeActionsLogger;
    private final Friendships mFollowingCache;
    private final PendingRequestHelper mPendingRequestHelper;

    public FollowButtonClickListener(Context context, AppController appController, PendingRequestHelper pendingRequestHelper, Friendships followingCache, FollowScribeActionsLogger followLogger) {
        this.mAppController = appController;
        this.mPendingRequestHelper = pendingRequestHelper;
        this.mFollowingCache = followingCache;
        this.mFollowScribeActionsLogger = followLogger;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        FollowButtonViewHolder holder = (FollowButtonViewHolder) view.getTag();
        long userId = holder.userId;
        boolean following = holder.following;
        if (following) {
            String reqId = Components.userInteractionsComponent().unfollowUser(this.mAppController, userId, false, this.mFollowScribeActionsLogger);
            this.mPendingRequestHelper.addRequest(reqId);
            this.mFollowingCache.removeFollowing(userId);
            view.setSelected(false);
        } else {
            String reqId2 = Components.userInteractionsComponent().followUser(this.mAppController, userId, false, this.mFollowScribeActionsLogger);
            this.mPendingRequestHelper.addRequest(reqId2);
            this.mFollowingCache.addFollowing(userId);
            view.setSelected(true);
        }
        holder.following = following ? false : true;
    }
}
