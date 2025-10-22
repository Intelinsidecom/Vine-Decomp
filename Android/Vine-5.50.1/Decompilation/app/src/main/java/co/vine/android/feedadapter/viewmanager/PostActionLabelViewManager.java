package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import co.vine.android.CommentsActivity;
import co.vine.android.R;
import co.vine.android.UsersActivity;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.TextViewHolder;
import co.vine.android.util.Util;
import co.vine.android.widget.VineClickableSpan;

/* loaded from: classes.dex */
public class PostActionLabelViewManager implements ViewManager {
    private final Context mContext;
    private final ViewType mType;

    public PostActionLabelViewManager(ViewType type, Context context) {
        this.mType = type;
        this.mContext = context;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return this.mType;
    }

    public void bind(TextViewHolder h, int count, long postId, long repostId, long userId) throws Resources.NotFoundException {
        if (h.text != null) {
            if (count <= 0) {
                h.text.setVisibility(8);
                return;
            }
            h.text.setVisibility(0);
            SpannableStringBuilder sb = new SpannableStringBuilder();
            String numberLikesString = Util.numberFormat(this.mContext.getResources(), count);
            sb.append((CharSequence) numberLikesString);
            int end = sb.length();
            VineClickableSpan clickableSpan = getClickableSpan(postId, repostId, userId);
            Util.safeSetSpan(sb, clickableSpan, 0, end, 33);
            h.text.setMovementMethod(LinkMovementMethod.getInstance());
            h.text.setText(sb);
        }
    }

    private VineClickableSpan getClickableSpan(final long postId, final long repostId, final long userId) throws Resources.NotFoundException {
        int gray = this.mContext.getResources().getColor(R.color.black_thirty_five_percent);
        switch (this.mType) {
            case LIKES_LABEL:
                return new VineClickableSpan(this.mContext, gray, 2) { // from class: co.vine.android.feedadapter.viewmanager.PostActionLabelViewManager.1
                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view) {
                        Intent i = new Intent(PostActionLabelViewManager.this.mContext, (Class<?>) UsersActivity.class);
                        i.putExtra("post_id", postId);
                        i.putExtra("u_type", 5);
                        PostActionLabelViewManager.this.mContext.startActivity(i);
                    }
                };
            case COMMENTS_LABEL:
                return new VineClickableSpan(this.mContext, gray, 2) { // from class: co.vine.android.feedadapter.viewmanager.PostActionLabelViewManager.2
                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view) {
                        CommentsActivity.start(view.getContext(), postId, repostId, userId, true);
                    }
                };
            case SHARES_LABEL:
                return new VineClickableSpan(this.mContext, gray, 2) { // from class: co.vine.android.feedadapter.viewmanager.PostActionLabelViewManager.3
                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view) {
                        Intent i = new Intent(PostActionLabelViewManager.this.mContext, (Class<?>) UsersActivity.class);
                        i.putExtra("post_id", postId);
                        i.putExtra("u_type", 9);
                        PostActionLabelViewManager.this.mContext.startActivity(i);
                    }
                };
            default:
                return null;
        }
    }
}
