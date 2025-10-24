package co.vine.android;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.util.Util;
import co.vine.android.widget.UserViewHolder;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class UsersAdapter extends CursorAdapter {
    protected final AppController mAppController;
    private boolean mFollow;
    private Friendships mFriendships;
    private View.OnClickListener mListener;
    long mLoggedInUserId;
    private final int mProfileImageSize;
    protected final ArrayList<WeakReference<FollowableUserViewHolder>> mViewHolders;

    public UsersAdapter(Context context, AppController appController, boolean follow, View.OnClickListener listener, Friendships friendships, int flags) {
        super(context, (Cursor) null, flags);
        this.mAppController = appController;
        this.mLoggedInUserId = appController.getActiveId();
        this.mFollow = follow;
        this.mListener = listener;
        this.mFriendships = friendships;
        this.mViewHolders = new ArrayList<>();
        this.mProfileImageSize = context.getResources().getDimensionPixelOffset(R.dimen.user_image_size);
    }

    @Override // android.support.v4.widget.CursorAdapter
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.user_row_view, parent, false);
        FollowableUserViewHolder holder = new FollowableUserViewHolder(v);
        if (this.mFollow) {
            holder.followButton.setOnClickListener(this.mListener);
        } else {
            holder.followButton.setVisibility(4);
        }
        this.mViewHolders.add(new WeakReference<>(holder));
        v.setTag(holder);
        return v;
    }

    @Override // android.support.v4.widget.CursorAdapter
    public void bindView(View view, Context context, Cursor cursor) {
        boolean following;
        FollowableUserViewHolder holder = (FollowableUserViewHolder) view.getTag();
        holder.username.setText(cursor.getString(13));
        holder.userId = cursor.getLong(1);
        if (cursor.getInt(14) == 1) {
            holder.verified.setVisibility(0);
        } else {
            holder.verified.setVisibility(8);
        }
        ImageButton followButton = holder.followButton;
        if (this.mFollow && holder.userId != this.mAppController.getActiveId()) {
            followButton.setVisibility(0);
            Friendships friendships = this.mFriendships;
            long userId = cursor.getLong(1);
            if (friendships != null) {
                if (friendships.contains(userId)) {
                    following = friendships.isFollowing(userId);
                } else {
                    following = (cursor.getInt(10) & 1) > 0;
                }
            } else {
                following = (cursor.getInt(10) & 1) > 0;
            }
            followButton.setTag(new FollowButtonViewHolder(userId, following));
            if (following) {
                followButton.setBackgroundResource(R.drawable.btn_following_default);
            } else {
                followButton.setBackgroundResource(R.drawable.btn_follow_default);
            }
        } else {
            followButton.setVisibility(8);
        }
        String url = cursor.getString(2);
        if (!TextUtils.isEmpty(url)) {
            ImageKey key = new ImageKey(url, this.mProfileImageSize, this.mProfileImageSize, true);
            if (Util.isDefaultAvatarUrl(url)) {
                Util.safeSetDefaultAvatar(holder.image, Util.ProfileImageSize.MEDIUM, (-16777216) | this.mContext.getResources().getColor(R.color.vine_green));
                return;
            } else {
                holder.avatarUrl = key;
                setUserImage(holder, this.mAppController.getPhotoBitmap(key));
                return;
            }
        }
        setUserImage(holder, null);
    }

    public synchronized void setUserImages(HashMap<ImageKey, UrlImage> images) {
        ArrayList<WeakReference<FollowableUserViewHolder>> toRemove = new ArrayList<>();
        Iterator<WeakReference<FollowableUserViewHolder>> it = this.mViewHolders.iterator();
        while (it.hasNext()) {
            WeakReference<FollowableUserViewHolder> ref = it.next();
            UserViewHolder holder = ref.get();
            if (holder == null) {
                toRemove.add(ref);
            } else {
                UrlImage image = images.get(holder.avatarUrl);
                if (image != null && image.isValid()) {
                    setUserImage(holder, image.bitmap);
                }
            }
        }
        Iterator<WeakReference<FollowableUserViewHolder>> it2 = toRemove.iterator();
        while (it2.hasNext()) {
            WeakReference<FollowableUserViewHolder> r = it2.next();
            this.mViewHolders.remove(r);
        }
    }

    @Override // android.support.v4.widget.CursorAdapter, android.widget.Adapter
    public long getItemId(int position) {
        Cursor c = (Cursor) super.getItem(position);
        if (c == null || !c.moveToPosition(position)) {
            return 0L;
        }
        return c.getLong(1);
    }

    private void setUserImage(UserViewHolder holder, Bitmap bmp) {
        holder.image.setColorFilter((ColorFilter) null);
        if (bmp != null) {
            holder.image.setImageDrawable(new RecyclableBitmapDrawable(this.mContext.getResources(), bmp));
        } else {
            holder.image.setImageResource(R.drawable.circle_shape_light);
        }
    }
}
