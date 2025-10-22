package co.vine.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import co.vine.android.api.VineUser;
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
import java.util.List;

/* loaded from: classes.dex */
public abstract class UsersMemoryAdapter<T extends UserViewHolder> extends BaseAdapter {
    protected final AppController mAppController;
    protected final Context mContext;
    long mLoggedInUserId;
    private final int mProfileImageSize;
    protected ArrayList<VineUser> mUsers;
    protected final ArrayList<WeakReference<T>> mViewHolders = new ArrayList<>();

    protected abstract void bindExtraViews(View view, int i, T t, VineUser vineUser);

    protected abstract View newView(int i, ViewGroup viewGroup);

    public UsersMemoryAdapter(Context context, AppController appController) {
        this.mContext = context;
        this.mAppController = appController;
        this.mLoggedInUserId = appController.getActiveId();
        this.mProfileImageSize = context.getResources().getDimensionPixelOffset(R.dimen.user_image_size);
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = newView(position, parent);
        } else {
            view = convertView;
        }
        bindView(view, position);
        return view;
    }

    public final void bindView(View view, int position) {
        VineUser user = this.mUsers.get(position);
        UserViewHolder userViewHolder = (UserViewHolder) view.getTag();
        userViewHolder.username.setText(user.username);
        userViewHolder.userId = user.userId;
        if (user.verified == 1) {
            userViewHolder.verified.setVisibility(0);
        } else {
            userViewHolder.verified.setVisibility(8);
        }
        if (!user.hiddenTwitter && user.twitterScreenname != null) {
            userViewHolder.twitterScreenname.setText("@" + user.twitterScreenname);
            userViewHolder.twitterScreenname.setVisibility(0);
        } else {
            userViewHolder.twitterScreenname.setVisibility(8);
        }
        userViewHolder.twitterVerified.setVisibility(8);
        String url = user.avatarUrl;
        if (!TextUtils.isEmpty(url)) {
            ImageKey key = new ImageKey(url, this.mProfileImageSize, this.mProfileImageSize, true);
            if (Util.isDefaultAvatarUrl(url)) {
                Util.safeSetDefaultAvatar(userViewHolder.image, Util.ProfileImageSize.MEDIUM, (-16777216) | this.mContext.getResources().getColor(R.color.vine_green));
            } else {
                userViewHolder.avatarUrl = key;
                setUserImage(userViewHolder, this.mAppController.getPhotoBitmap(key));
            }
        } else {
            setUserImage(userViewHolder, null);
        }
        bindExtraViews(view, position, userViewHolder, user);
    }

    public synchronized void setUserImages(HashMap<ImageKey, UrlImage> images) {
        ArrayList<WeakReference<T>> toRemove = new ArrayList<>();
        Iterator<WeakReference<T>> it = this.mViewHolders.iterator();
        while (it.hasNext()) {
            WeakReference<T> ref = it.next();
            T holder = ref.get();
            if (holder == null) {
                toRemove.add(ref);
            } else {
                UrlImage image = images.get(holder.avatarUrl);
                if (image != null && image.isValid()) {
                    setUserImage(holder, image.bitmap);
                }
            }
        }
        Iterator<WeakReference<T>> it2 = toRemove.iterator();
        while (it2.hasNext()) {
            WeakReference<T> r = it2.next();
            this.mViewHolders.remove(r);
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this.mUsers != null) {
            return this.mUsers.size();
        }
        return 0;
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        if (this.mUsers != null) {
            return this.mUsers.get(position);
        }
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        VineUser user;
        if (this.mUsers == null || position >= this.mUsers.size() || (user = this.mUsers.get(position)) == null) {
            return 0L;
        }
        return user.userId;
    }

    private void setUserImage(UserViewHolder holder, Bitmap bmp) {
        holder.image.setColorFilter((ColorFilter) null);
        if (bmp != null) {
            holder.image.setImageDrawable(new RecyclableBitmapDrawable(this.mContext.getResources(), bmp));
        } else {
            holder.image.setImageResource(R.drawable.circle_shape_light);
        }
    }

    public void mergeData(List<VineUser> users, boolean mergeAfter) {
        if (!mergeAfter) {
            this.mUsers = new ArrayList<>();
        }
        if (this.mUsers == null) {
            this.mUsers = new ArrayList<>();
        }
        if (users != null) {
            for (VineUser user : users) {
                this.mUsers.add(user);
            }
        }
        if (users != null) {
            notifyDataSetChanged();
        }
    }
}
