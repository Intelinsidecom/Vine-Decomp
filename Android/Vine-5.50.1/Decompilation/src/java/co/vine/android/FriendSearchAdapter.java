package co.vine.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class FriendSearchAdapter extends ArrayAdapter<VineUser> {
    private final AppController mAppController;
    private final int mProfileImageSize;
    protected final ArrayList<WeakReference<UserViewHolder>> mViewHolders;
    private final int mVineGreen;

    public FriendSearchAdapter(Context context, AppController appController, List<VineUser> users) {
        super(context, 0, users);
        this.mAppController = appController;
        this.mProfileImageSize = context.getResources().getDimensionPixelOffset(R.dimen.user_image_size);
        this.mViewHolders = new ArrayList<>();
        this.mVineGreen = context.getResources().getColor(R.color.vine_green);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        UserViewHolder holder;
        if (convertView != null) {
            holder = (UserViewHolder) convertView.getTag();
            this.mViewHolders.add(new WeakReference<>(holder));
        } else {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.user_row_simple_view, parent, false);
            holder = new UserViewHolder(convertView);
        }
        VineUser user = getItem(position);
        holder.username.setText(user.username);
        String url = user.avatarUrl;
        if (!TextUtils.isEmpty(url)) {
            ImageKey key = new ImageKey(url, this.mProfileImageSize, this.mProfileImageSize, true);
            if (Util.isDefaultAvatarUrl(url)) {
                Util.safeSetDefaultAvatar(holder.image, Util.ProfileImageSize.MEDIUM, (-16777216) | this.mVineGreen);
            } else {
                holder.avatarUrl = key;
                setUserImage(holder, this.mAppController.getPhotoBitmap(key));
            }
        } else {
            setUserImage(holder, null);
        }
        convertView.setTag(holder);
        return convertView;
    }

    public synchronized void setUserImages(HashMap<ImageKey, UrlImage> images) {
        ArrayList<WeakReference<UserViewHolder>> toRemove = new ArrayList<>();
        Iterator<WeakReference<UserViewHolder>> it = this.mViewHolders.iterator();
        while (it.hasNext()) {
            WeakReference<UserViewHolder> ref = it.next();
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
        Iterator<WeakReference<UserViewHolder>> it2 = toRemove.iterator();
        while (it2.hasNext()) {
            WeakReference<UserViewHolder> r = it2.next();
            this.mViewHolders.remove(r);
        }
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public long getItemId(int position) {
        VineUser user = getItem(position);
        return user.userId;
    }

    public void setData(List<VineUser> users) {
        if (Build.VERSION.SDK_INT >= 11) {
            addAll(users);
            return;
        }
        for (VineUser user : users) {
            add(user);
        }
    }

    private void setUserImage(UserViewHolder holder, Bitmap bmp) {
        holder.image.setColorFilter((ColorFilter) null);
        if (bmp != null) {
            holder.image.setImageDrawable(new RecyclableBitmapDrawable(getContext().getResources(), bmp));
        } else {
            holder.image.setImageResource(R.drawable.circle_shape_light);
        }
    }
}
