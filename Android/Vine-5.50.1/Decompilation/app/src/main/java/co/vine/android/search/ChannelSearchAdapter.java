package co.vine.android.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.ChannelActivity;
import co.vine.android.ChannelFollowButtonClickListener;
import co.vine.android.ExploreVideoListActivity;
import co.vine.android.Friendships;
import co.vine.android.R;
import co.vine.android.api.SearchResult;
import co.vine.android.api.VineChannel;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class ChannelSearchAdapter extends BaseAdapter {
    public int displayCount;
    private ChannelFollowButtonClickListener followButtonClickListener;
    protected final AppController mAppController;
    private final int mChannelImageSize;
    protected ArrayList<VineChannel> mChannels;
    protected Context mContext;
    private Friendships mFriendships;
    protected ArrayList<ChannelSearchViewHolder> mViewHolders = new ArrayList<>();

    public ChannelSearchAdapter(Context context, AppController appController, ChannelFollowButtonClickListener followButtonClickListener, Friendships friendships) {
        this.mContext = context;
        this.mAppController = appController;
        this.mChannelImageSize = context.getResources().getDimensionPixelOffset(R.dimen.user_image_size);
        this.followButtonClickListener = followButtonClickListener;
        this.mFriendships = friendships;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        int dataCount = this.mChannels != null ? 0 + this.mChannels.size() : 0;
        return Math.min(dataCount, this.displayCount);
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        if (this.mChannels == null || this.mChannels.size() <= 0) {
            return null;
        }
        return this.mChannels.get(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = newView(position, parent);
        } else {
            view = convertView;
        }
        bindView(position, view);
        return view;
    }

    public View newView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.channel_search_row_view, parent, false);
        ChannelSearchViewHolder holder = new ChannelSearchViewHolder(view);
        this.mViewHolders.add(holder);
        view.setTag(holder);
        return view;
    }

    private static class ChannelSearchViewHolder implements QueryableRowHolder {
        ImageKey avatarUrl;
        VineChannel channel;
        ImageView channelIcon;
        String channelName;
        TextView channelTitle;
        View followButton;
        TextView header;
        ViewGroup root;

        public ChannelSearchViewHolder(View view) {
            this.channelTitle = (TextView) view.findViewById(R.id.channel_name);
            this.channelIcon = (ImageView) view.findViewById(R.id.channel_image);
            this.followButton = view.findViewById(R.id.follow_channel);
            this.root = (ViewGroup) view.findViewById(R.id.root_layout);
            this.header = (TextView) view.findViewById(R.id.header_label);
            this.root.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.search.ChannelSearchAdapter.ChannelSearchViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (!ChannelSearchViewHolder.this.channel.channel.equals("Popular Now")) {
                        ChannelActivity.startExploreChannel(v.getContext(), ChannelSearchViewHolder.this.channel, false);
                    } else {
                        ExploreVideoListActivity.start(v.getContext(), Uri.parse("vine://popular-now"));
                    }
                }
            });
        }

        @Override // co.vine.android.search.QueryableRowHolder
        public String getSearchQueryString() {
            return this.channelName;
        }
    }

    public void bindView(int position, View view) {
        boolean following;
        ChannelSearchViewHolder holder = (ChannelSearchViewHolder) view.getTag();
        VineChannel channel = this.mChannels.get(position);
        holder.followButton.setOnClickListener(this.followButtonClickListener);
        holder.followButton.setTag(channel);
        Friendships friendships = this.mFriendships;
        if (friendships != null && friendships.containsChannel(channel.channelId)) {
            following = friendships.isChannelFollowing(channel.channelId);
        } else {
            following = channel.following;
        }
        if (following) {
            holder.followButton.setBackgroundResource(R.drawable.channel_following_ic);
        } else {
            holder.followButton.setBackgroundResource(R.drawable.channel_follow_ic);
        }
        holder.followButton.getBackground().mutate();
        if (channel.backgroundColor == null || channel.backgroundColor.isEmpty()) {
            holder.followButton.setVisibility(8);
        } else {
            holder.followButton.setVisibility(0);
            holder.followButton.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor(channel.backgroundColor), PorterDuff.Mode.SRC_ATOP));
        }
        if (!TextUtils.isEmpty(channel.channel)) {
            holder.channelTitle.setText(channel.channel);
            holder.channelName = channel.channel;
        }
        holder.channelName = channel.channel;
        if (position == 0) {
            holder.header.setVisibility(0);
            holder.header.setText(this.mContext.getString(R.string.channels));
        } else {
            holder.header.setVisibility(8);
        }
        String url = channel.retinaIconFullUrl;
        if (!TextUtils.isEmpty(url)) {
            ImageKey key = new ImageKey(url, this.mChannelImageSize, this.mChannelImageSize, true);
            holder.avatarUrl = key;
            setChannelImage(holder, this.mAppController.getPhotoBitmap(key));
        } else {
            setChannelImage(holder, null);
        }
        holder.channel = channel;
    }

    private void setChannelImage(ChannelSearchViewHolder holder, Bitmap bmp) {
        if (bmp != null) {
            holder.channelIcon.setImageDrawable(new RecyclableBitmapDrawable(this.mContext.getResources(), bmp));
        } else {
            holder.channelIcon.setImageResource(R.drawable.circle_shape_light);
        }
    }

    public synchronized void setChannelImages(HashMap<ImageKey, UrlImage> images) {
        ArrayList<ChannelSearchViewHolder> toRemove = new ArrayList<>();
        Iterator<ChannelSearchViewHolder> it = this.mViewHolders.iterator();
        while (it.hasNext()) {
            ChannelSearchViewHolder ref = it.next();
            if (ref == null) {
                toRemove.add(ref);
            } else {
                UrlImage image = images.get(ref.avatarUrl);
                if (image != null && image.isValid()) {
                    setChannelImage(ref, image.bitmap);
                }
            }
        }
        Iterator<ChannelSearchViewHolder> it2 = toRemove.iterator();
        while (it2.hasNext()) {
            ChannelSearchViewHolder r = it2.next();
            this.mViewHolders.remove(r);
        }
    }

    public void replaceData(SearchResult data) {
        this.mChannels = new ArrayList<>();
        if (data.getChannels() != null) {
            this.mChannels.addAll(data.getChannels().getItems());
            this.displayCount = data.getChannels().getDisplayCount();
        }
        notifyDataSetChanged();
    }
}
