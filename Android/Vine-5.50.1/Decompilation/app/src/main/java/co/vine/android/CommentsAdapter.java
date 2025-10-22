package co.vine.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.api.VineComment;
import co.vine.android.api.VineEntity;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.EntityLinkClickableSpan;
import co.vine.android.widget.VineClickableSpan;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/* loaded from: classes.dex */
public class CommentsAdapter extends BaseAdapter {
    private AppController mAppController;
    private Context mContext;
    private long mPostId;
    private int mVineGreen;
    private HashMap<Long, Integer> mIdToPositionMap = new HashMap<>();
    private ArrayList<VineComment> mComments = new ArrayList<>();
    private final HashSet<String> mSpamCommentIds = new HashSet<>();
    final ArrayList<WeakReference<CommentViewHolder>> mViewHolders = new ArrayList<>();
    private HashMap<String, Editable> mCommentSbs = new HashMap<>();

    public CommentsAdapter(Context context, long postId, AppController appController) {
        this.mContext = context;
        this.mPostId = postId;
        loadSpamCommentIds(postId);
        this.mAppController = appController;
        this.mVineGreen = context.getResources().getColor(R.color.vine_green);
    }

    private void loadSpamCommentIds(long postId) {
        SharedPreferences sp = StandalonePreference.SPAM_COMMENT_IDS.getPref(this.mContext);
        if (sp != null) {
            Set<String> spamCommentIds = sp.getStringSet(String.valueOf(postId), new HashSet());
            if (spamCommentIds.size() != 0) {
                this.mSpamCommentIds.addAll(spamCommentIds);
            }
        }
    }

    public View newView() {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this.mContext).inflate(R.layout.comment_row_view, (ViewGroup) null, false);
        CommentViewHolder h = new CommentViewHolder();
        h.profileImage = (ImageView) layout.findViewById(R.id.profile_image);
        h.content = (TextView) layout.findViewById(R.id.content);
        h.content.setMovementMethod(LinkMovementMethod.getInstance());
        h.usernameTextview = (TextView) layout.findViewById(R.id.username);
        h.verified = (ImageView) layout.findViewById(R.id.verified);
        h.twitterScreenname = (TextView) layout.findViewById(R.id.twitterScreenname);
        h.topSpacer = layout.findViewById(R.id.spacer_top);
        layout.setTag(h);
        this.mViewHolders.add(new WeakReference<>(h));
        return layout;
    }

    public void bindView(View view, VineComment comment) throws Resources.NotFoundException {
        CommentViewHolder h = (CommentViewHolder) view.getTag();
        int position = getPositionForId(comment.commentId);
        if (position == 0) {
            h.topSpacer.setVisibility(0);
        } else {
            h.topSpacer.setVisibility(8);
        }
        String username = comment.username;
        String commentString = comment.comment;
        String twitterScreenname = comment.twitterScreenname;
        ArrayList<VineEntity> entities = comment.entities;
        if (username == null) {
            username = "";
            CrashUtil.logException(new VineLoggingException(), "Username is null for " + comment.commentId, new Object[0]);
        }
        h.content.setMovementMethod(LinkMovementMethod.getInstance());
        h.commentId = String.valueOf(comment.commentId);
        h.userId = comment.userId;
        h.username = username;
        h.usernameTextview.setText(username);
        if (comment.user != null && comment.user.verified == 1) {
            h.verified.setVisibility(0);
        } else {
            h.verified.setVisibility(8);
        }
        if (comment.user != null && !comment.user.hiddenTwitter && twitterScreenname != null) {
            h.twitterScreenname.setText("@" + twitterScreenname);
            h.twitterScreenname.setVisibility(0);
        } else {
            h.twitterScreenname.setVisibility(8);
        }
        Editable commentSb = this.mCommentSbs.get(h.commentId);
        if (commentSb == null) {
            commentSb = new SpannableStringBuilder(commentString);
            this.mCommentSbs.put(h.commentId, commentSb);
            if (entities != null) {
                try {
                    Util.adjustEntities(entities, commentSb, 0, false);
                    Iterator<VineEntity> it = entities.iterator();
                    while (it.hasNext()) {
                        VineEntity entity = it.next();
                        if (entity.link != null) {
                            VineClickableSpan clickableSpan = new EntityLinkClickableSpan(this.mContext, this.mVineGreen, 2, entity.link);
                            Util.safeSetSpan(commentSb, clickableSpan, entity.start, entity.end, 33);
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    CrashUtil.logException(e);
                }
            }
            String date = Util.getRelativeTimeString(this.mContext, comment.timestamp, false);
            int start = commentSb.length();
            commentSb.append((CharSequence) "  ").append((CharSequence) date);
            int end = commentSb.length();
            ColorStateList color = this.mContext.getResources().getColorStateList(R.color.timestamp_text);
            TextAppearanceSpan timestampSpan = new TextAppearanceSpan(null, 0, this.mContext.getResources().getDimensionPixelSize(R.dimen.font_size_mini), color, null);
            Util.safeSetSpan(commentSb, timestampSpan, start, end, 33);
        }
        h.content.setText(commentSb);
        String url = comment.avatarUrl;
        if (Util.isDefaultAvatarUrl(url)) {
            Util.safeSetDefaultAvatar(h.profileImage, Util.ProfileImageSize.MEDIUM, (-16777216) | this.mVineGreen);
            ViewUtil.setBackground(h.profileImage, null);
            return;
        }
        h.profileImage.setColorFilter((ColorFilter) null);
        ImageKey key = new ImageKey(url, true);
        h.imageKey = key;
        Bitmap profileImage = this.mAppController.getPhotoBitmap(key);
        if (profileImage != null) {
            h.profileImage.setImageDrawable(new RecyclableBitmapDrawable(this.mContext.getResources(), profileImage));
        } else {
            ViewUtil.setBackground(h.profileImage, this.mContext.getResources().getDrawable(R.drawable.circle_shape_light));
            h.profileImage.setImageDrawable(null);
        }
    }

    public void updateProfileImages(HashMap<ImageKey, UrlImage> images) {
        UrlImage image;
        ArrayList<WeakReference<CommentViewHolder>> toRemove = new ArrayList<>();
        for (int i = this.mViewHolders.size() - 1; i >= 0; i--) {
            WeakReference<CommentViewHolder> wf = this.mViewHolders.get(i);
            CommentViewHolder vh = wf.get();
            if (vh == null) {
                toRemove.add(wf);
            } else if (vh.imageKey != null && (image = images.get(vh.imageKey)) != null) {
                vh.profileImage.setImageDrawable(new RecyclableBitmapDrawable(this.mContext.getResources(), image.bitmap));
            }
        }
        Iterator<WeakReference<CommentViewHolder>> it = toRemove.iterator();
        while (it.hasNext()) {
            this.mViewHolders.remove(it.next());
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mComments.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mComments.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int pos) {
        return this.mComments.get(pos).commentId;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) throws Resources.NotFoundException {
        if (convertView == null) {
            convertView = newView();
        }
        bindView(convertView, (VineComment) getItem(position));
        return convertView;
    }

    public int getPositionForId(long id) {
        Integer position;
        if (this.mIdToPositionMap == null || (position = this.mIdToPositionMap.get(Long.valueOf(id))) == null) {
            return -1;
        }
        return position.intValue();
    }

    public void mergeComments(ArrayList<VineComment> comments) {
        LinkedList<VineComment> filtered = new LinkedList<>();
        Iterator<VineComment> it = comments.iterator();
        while (it.hasNext()) {
            VineComment comment = it.next();
            if (!this.mSpamCommentIds.contains(String.valueOf(comment.commentId))) {
                filtered.add(comment);
            }
        }
        this.mComments.addAll(0, filtered);
        rebuildIdToPositionMap();
        notifyDataSetChanged();
    }

    private void rebuildIdToPositionMap() {
        this.mIdToPositionMap.clear();
        for (int i = 0; i < this.mComments.size(); i++) {
            VineComment comment = this.mComments.get(i);
            this.mIdToPositionMap.put(Long.valueOf(comment.commentId), Integer.valueOf(i));
        }
    }

    public void addMyComment(VineComment commentToMerge) {
        this.mComments.add(commentToMerge);
        rebuildIdToPositionMap();
        notifyDataSetChanged();
    }

    public void deleteComment(long commentId) {
        int position = getPositionForId(commentId);
        if (position >= 0) {
            this.mComments.remove(position);
            rebuildIdToPositionMap();
            notifyDataSetChanged();
        }
    }

    public void spamComment(long commentId) {
        int position;
        SharedPreferences sp = StandalonePreference.SPAM_COMMENT_IDS.getPref(this.mContext);
        if (sp != null && (position = getPositionForId(commentId)) >= 0) {
            this.mComments.remove(position);
            this.mSpamCommentIds.add(String.valueOf(commentId));
            sp.edit().putStringSet(String.valueOf(this.mPostId), this.mSpamCommentIds).apply();
            rebuildIdToPositionMap();
            notifyDataSetChanged();
        }
    }
}
