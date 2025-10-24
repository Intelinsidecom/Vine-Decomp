package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import co.vine.android.ChannelActivity;
import co.vine.android.CommentsActivity;
import co.vine.android.R;
import co.vine.android.api.VineComment;
import co.vine.android.api.VineEntity;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.InlineCommentsViewHolder;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import co.vine.android.widget.EntityLinkClickableSpan;
import co.vine.android.widget.VineClickableSpan;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class InlineCommentsViewManager implements ViewManager {
    private final Context mContext;

    public InlineCommentsViewManager(Context context) {
        this.mContext = context;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.INLINE_COMMENTS;
    }

    public void bind(InlineCommentsViewHolder h, Comments comments, final long postId, final long repostId, final long userId, int color) throws Resources.NotFoundException {
        TextView textView;
        if (comments == null || comments.getCount() <= 0) {
            h.container.setVisibility(8);
            return;
        }
        h.container.setVisibility(0);
        h.comment1.setVisibility(8);
        h.comment2.setVisibility(8);
        h.comment3.setVisibility(8);
        h.divider.setVisibility(4);
        int size = comments.getCount();
        if (size > 3) {
            Resources res = this.mContext.getResources();
            String cta = res.getString(R.string.timeline_comments_many, Integer.valueOf(size));
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append((CharSequence) cta);
            VineClickableSpan clickableSpan = new VineClickableSpan(res.getColor(R.color.black_thirty_five_percent)) { // from class: co.vine.android.feedadapter.viewmanager.InlineCommentsViewManager.1
                @Override // android.text.style.ClickableSpan
                public void onClick(View view) {
                    CommentsActivity.start(view.getContext(), postId, repostId, userId, true);
                }
            };
            Util.safeSetSpan(ssb, clickableSpan, 0, ssb.length(), 33);
            h.viewAll.setVisibility(0);
            h.viewAll.setMovementMethod(LinkMovementMethod.getInstance());
            h.viewAll.setText(ssb);
        } else {
            h.viewAll.setVisibility(8);
        }
        ArrayList<CharSequence> commentStrings = comments.getBuiltComments(color);
        int count = 0;
        int index = 0;
        while (index < Math.min(3, size) && index < commentStrings.size()) {
            CharSequence comment = commentStrings.get(index);
            if (count < 3) {
                switch (count) {
                    case 0:
                        textView = h.comment1;
                        break;
                    case 1:
                        textView = h.comment2;
                        break;
                    default:
                        textView = h.comment3;
                        break;
                }
                TextView thisComment = textView;
                thisComment.setVisibility(0);
                thisComment.setMovementMethod(LinkMovementMethod.getInstance());
                thisComment.setText(comment);
                index++;
                count++;
            } else {
                return;
            }
        }
    }

    public static final class Comments {
        private final ArrayList<VineComment> mComments;
        private final Context mContext;
        private final int mCount;
        private final ArrayList<CharSequence> mReady;

        Comments(ArrayList<CharSequence> comments, int count) {
            this.mReady = comments;
            this.mCount = count;
            this.mContext = null;
            this.mComments = null;
        }

        Comments(Context context, ArrayList<VineComment> comments, int count) {
            this.mContext = context;
            this.mComments = comments;
            this.mCount = count;
            this.mReady = new ArrayList<>();
        }

        public ArrayList<CharSequence> getBuiltComments(int color) {
            if (!this.mReady.isEmpty()) {
                return this.mReady;
            }
            int size = this.mComments.size();
            int count = 0;
            int index = Math.max(size - 3, 0);
            while (index < size) {
                final VineComment comment = this.mComments.get(index);
                if (count >= 3) {
                    break;
                }
                SpannableStringBuilder commentSb = new SpannableStringBuilder();
                try {
                    commentSb.append((CharSequence) comment.username).append(' ').append((CharSequence) comment.comment);
                    VineClickableSpan clickableSpan = new VineClickableSpan(this.mContext, this.mContext.getResources().getColor(R.color.primary_text), 3) { // from class: co.vine.android.feedadapter.viewmanager.InlineCommentsViewManager.Comments.1
                        @Override // android.text.style.ClickableSpan
                        public void onClick(View view) {
                            ChannelActivity.startProfile(view.getContext(), comment.userId, null);
                        }
                    };
                    int usernameEnd = comment.username.length();
                    Util.safeSetSpan(commentSb, clickableSpan, 0, usernameEnd, 33);
                    if (comment.transientEntities == null && comment.entities != null) {
                        comment.transientEntities = new ArrayList<>();
                        Iterator<VineEntity> it = comment.entities.iterator();
                        while (it.hasNext()) {
                            VineEntity entity = it.next();
                            comment.transientEntities.add(entity.duplicate());
                        }
                    }
                    ArrayList<VineEntity> entities = comment.transientEntities;
                    if (entities != null) {
                        try {
                            Util.adjustEntities(entities, commentSb, usernameEnd + 1, false);
                            Iterator<VineEntity> it2 = entities.iterator();
                            while (it2.hasNext()) {
                                VineEntity e = it2.next();
                                EntityLinkClickableSpan entityClickableSpan = new EntityLinkClickableSpan(this.mContext, color, 2, e.link);
                                Util.safeSetSpan(commentSb, entityClickableSpan, e.start, e.end, 33);
                            }
                        } catch (Exception e2) {
                            if (BuildUtil.isLogsOn()) {
                                throw new RuntimeException(e2);
                            }
                            CrashUtil.logException(e2, "Failed to adjust entities on " + comment.commentId, new Object[0]);
                        }
                    }
                    this.mReady.add(commentSb);
                } catch (NullPointerException e3) {
                    CrashUtil.logException(e3, "Comment attribute caused NPE: {} {} {}", Long.valueOf(comment.commentId), comment.username, comment.comment);
                }
                index++;
                count++;
            }
            return this.mReady;
        }

        public int getCount() {
            return this.mCount;
        }
    }
}
