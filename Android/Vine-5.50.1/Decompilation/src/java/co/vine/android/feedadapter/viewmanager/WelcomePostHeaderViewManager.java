package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.WelcomePostHeaderViewHolder;
import co.vine.android.scribe.FollowScribeActionsLogger;

/* loaded from: classes.dex */
public class WelcomePostHeaderViewManager implements ViewManager {
    private final AvatarViewManager mAvatarManager;
    protected final Context mContext;
    private final FollowScribeActionsLogger mFollowActionsLogger;
    private final FollowToggleViewManager mFollowManager;
    private final PostUsernameViewManager mUsernameManager;

    public WelcomePostHeaderViewManager(Context context, AppController appController, FollowScribeActionsLogger logger) {
        this.mContext = context;
        this.mFollowActionsLogger = logger;
        this.mAvatarManager = new AvatarViewManager(this.mContext, appController);
        this.mUsernameManager = new PostUsernameViewManager(this.mContext);
        this.mFollowManager = new FollowToggleViewManager(this.mContext, appController);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.POST_HEADER;
    }

    public void bind(WelcomePostHeaderViewHolder h, VinePost data) {
        if (data != null) {
            this.mAvatarManager.bind(h.getAvatarHolder(), data.avatarUrl, data.isRevined(), 0);
            this.mUsernameManager.bind(h.getUsernameHolder(), data.username, data.userId, data.styledUserName, null, "Welcome");
            this.mFollowManager.bind(h.getFollowHolder(), data.userId, data.following);
            this.mFollowManager.bindClickListener(h.getFollowHolder(), data, this.mFollowActionsLogger);
        }
    }
}
