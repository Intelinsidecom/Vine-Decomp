package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.api.VinePost;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.TextViewHolder;
import co.vine.android.feedadapter.viewmanager.PostDescriptionViewManager;
import co.vine.android.util.LinkSuppressor;

/* loaded from: classes.dex */
public class ImmersiveViewsPostInfoViewManager implements ViewManager {
    private View mInfoRootView;

    public interface UsernameClickListener {
        void onUsernameClicked(long j);
    }

    public ImmersiveViewsPostInfoViewManager(View postInfoRootView) {
        this.mInfoRootView = postInfoRootView;
    }

    public void bindPostData(Context context, final VinePost post, LinkSuppressor suppressor, final UsernameClickListener usernameClickListener) throws Resources.NotFoundException {
        int color = context.getResources().getColor(R.color.solid_white);
        TextView caption = (TextView) this.mInfoRootView.findViewById(R.id.caption);
        TextView username = (TextView) this.mInfoRootView.findViewById(R.id.username);
        username.setText(post.username);
        username.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.ImmersiveViewsPostInfoViewManager.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (usernameClickListener != null) {
                    usernameClickListener.onUsernameClicked(post.userId);
                }
            }
        });
        new PostDescriptionViewManager().bind(new TextViewHolder(this.mInfoRootView, ViewType.IMMERSIVE_DESCRIPTION, R.id.caption), new PostDescriptionViewManager.Description(context, post.description, post.entities), post.entities != null && post.entities.size() > 0, post.isRTL, color, 1, suppressor);
        if (TextUtils.isEmpty(post.description)) {
            caption.setVisibility(8);
        } else {
            caption.scrollTo(0, 0);
            caption.setVisibility(0);
        }
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.IMMERSIVE_DESCRIPTION;
    }
}
