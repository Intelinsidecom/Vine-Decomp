package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.view.View;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.ToggleButtonViewHolder;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.service.components.Components;

/* loaded from: classes.dex */
public class FollowToggleViewManager implements ViewManager {
    private final AppController mAppController;
    private final Context mContext;

    public FollowToggleViewManager(Context context, AppController appController) {
        this.mContext = context;
        this.mAppController = appController;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.FOLLOW_TOGGLE;
    }

    public void bind(ToggleButtonViewHolder h, long userId, boolean following) {
        if (userId != AppController.getInstance(this.mContext).getActiveId()) {
            if (!following) {
                h.unselectedView.setVisibility(0);
                h.selectedView.setVisibility(4);
                return;
            } else {
                h.unselectedView.setVisibility(4);
                h.selectedView.setVisibility(0);
                return;
            }
        }
        h.button.setVisibility(8);
    }

    public void bindClickListener(final ToggleButtonViewHolder h, final VinePost data, final FollowScribeActionsLogger logger) {
        h.button.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.FollowToggleViewManager.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!data.following) {
                    Components.userInteractionsComponent().followUser(FollowToggleViewManager.this.mAppController, data.userId, true, logger, data);
                    data.following = true;
                    h.unselectedView.setVisibility(4);
                    h.selectedView.setVisibility(0);
                    return;
                }
                Components.userInteractionsComponent().unfollowUser(FollowToggleViewManager.this.mAppController, data.userId, true, logger, data);
                data.following = false;
                h.unselectedView.setVisibility(0);
                h.selectedView.setVisibility(4);
            }
        });
    }
}
