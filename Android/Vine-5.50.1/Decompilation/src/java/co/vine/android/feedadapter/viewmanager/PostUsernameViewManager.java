package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import co.vine.android.ChannelActivity;
import co.vine.android.api.VineChannel;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.TextViewHolder;
import co.vine.android.util.LinkSuppressor;
import co.vine.android.util.Util;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.TypefacesSpan;

/* loaded from: classes.dex */
public class PostUsernameViewManager implements ViewManager {
    private final Context mContext;

    public PostUsernameViewManager(Context context) {
        this.mContext = context;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.USERNAME;
    }

    public void bind(TextViewHolder h, String username, final long userId, SpannableStringBuilder styledUserName, final LinkSuppressor suppressor, final String followEventSource) {
        if (h.text != null && username != null) {
            if (styledUserName == null) {
                SpannableStringBuilder sb = new SpannableStringBuilder(username);
                TypefacesSpan usernameSpan = new TypefacesSpan(null, Typefaces.get(this.mContext).getContentTypeface(0, 3));
                Util.safeSetSpan(sb, usernameSpan, 0, username.length(), 33);
                styledUserName = sb;
            }
            h.text.setText(styledUserName);
            h.text.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.PostUsernameViewManager.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (suppressor == null || !suppressor.shouldSuppressUserLink(userId)) {
                        ChannelActivity.startProfile(PostUsernameViewManager.this.mContext, userId, followEventSource);
                    }
                }
            });
        }
    }

    public void bind(TextViewHolder h, String username, final VineChannel channel, String followEventSource) {
        if (h.text != null && username != null) {
            h.text.setText(username);
            h.text.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.PostUsernameViewManager.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    ChannelActivity.startExploreChannel(PostUsernameViewManager.this.mContext, channel, false);
                }
            });
        }
    }
}
