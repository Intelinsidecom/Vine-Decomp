package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.vine.android.api.VineChannel;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineDatabaseSQL;
import co.vine.android.widget.VineToggleButton;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
public class ChannelsListFragment extends BaseCursorListFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private boolean mFetched;
    private boolean mIsNux;
    private boolean mResponseReceived;
    private VineToggleButton mSelectAllButton;
    private long mSelectedChannel;
    private boolean mShowChannelFollow;
    private HashSet<String> mAllChannelIds = new HashSet<>();
    private HashSet<String> mSelectedChannelIds = new HashSet<>();
    private HashSet<String> mChannelsToUnfollow = new HashSet<>();
    private boolean mFollowingAll = true;

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppSessionListener(new ChannelsListSessionListener());
        Bundle args = getArguments();
        this.mSelectedChannel = args.getLong("selected_channel");
        this.mShowChannelFollow = args.getBoolean("show_channel_follow");
        this.mIsNux = args.getBoolean("is_nux");
        if (this.mIsNux || this.mShowChannelFollow) {
            setHasOptionsMenu(true);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.mCursorAdapter == null) {
            this.mCursorAdapter = new ChannelsAdapter(getActivity(), this.mAppController, 0);
        }
        ListView listview = this.mListView;
        listview.setBackgroundColor(getResources().getColor(R.color.solid_white));
        listview.setDivider(getResources().getDrawable(R.drawable.rule_horizontal_5));
        if (savedInstanceState == null && this.mShowChannelFollow) {
            View header = LayoutInflater.from(getActivity()).inflate(R.layout.channel_row_view, (ViewGroup) listview, false);
            header.setLayoutParams(new AbsListView.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.channel_row_height)));
            ChannelViewHolder tmp = new ChannelViewHolder(header);
            tmp.channelImage.setVisibility(8);
            tmp.followButton.setVisibility(0);
            tmp.channelTitle.setText(R.string.all_featured_posts);
            this.mSelectAllButton = tmp.followButton;
            this.mSelectAllButton.setCheckedColorStyle(Integer.valueOf(getResources().getColor(R.color.vine_green)));
            listview.addHeaderView(header);
        }
        listview.setAdapter((ListAdapter) this.mCursorAdapter);
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.mIsNux) {
            inflater.inflate(R.menu.next, menu);
        } else if (this.mShowChannelFollow) {
            inflater.inflate(R.menu.save, menu);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem next = menu.findItem(R.id.next);
        MenuItem done = menu.findItem(R.id.done);
        if (next != null) {
            next.setEnabled(true);
        }
        if (done != null) {
            done.setEnabled(true);
        }
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        return getActivity().onOptionsItemSelected(item);
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mCursorAdapter.getCursor() == null) {
            initLoader();
        }
        if (!this.mFetched) {
            fetchContent(3);
        }
    }

    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Vine.Channels.CONTENT_URI, VineDatabaseSQL.ChannelsQuery.PROJECTION, null, null, null);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        super.onLoadFinished(loader, cursor);
        if (this.mResponseReceived) {
            hideProgress(3);
            if (this.mCursorAdapter.isEmpty()) {
                showSadface(true);
            } else {
                showSadface(false);
            }
        }
    }

    @Override // co.vine.android.BaseCursorListFragment
    protected void onScrollLastItem(Cursor cursor) {
        if (this.mRefreshable && cursor.getInt(7) == 0) {
            fetchContent(1);
        }
    }

    public Set<String> getSelectedChannelIds() {
        return this.mSelectedChannelIds;
    }

    public Set<String> getChannelIdsToUnfollow() {
        return this.mChannelsToUnfollow;
    }

    private void fetchContent(int fetchType) {
        int pageType;
        if (!hasPendingRequest(fetchType)) {
            switch (fetchType) {
                case 1:
                    pageType = 3;
                    break;
                case 2:
                    pageType = 2;
                    break;
                default:
                    pageType = 1;
                    break;
            }
            addRequest(this.mAppController.fetchChannels(pageType), fetchType);
            this.mFetched = true;
        }
    }

    @Override // co.vine.android.BaseCursorListFragment
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor;
        String iconUrl;
        if (!this.mShowChannelFollow && (cursor = this.mCursorAdapter.getCursor()) != null && cursor.moveToPosition(position)) {
            Intent intent = new Intent();
            long channelId = cursor.getLong(1);
            String channel = cursor.getString(2);
            String color = cursor.getString(4);
            if (getResources().getDisplayMetrics().densityDpi < 240) {
                iconUrl = cursor.getString(8);
            } else {
                iconUrl = cursor.getString(9);
            }
            this.mAppController.markChannelLastUsed(channelId);
            intent.putExtra("channel", channel);
            intent.putExtra("channel_id", channelId);
            intent.putExtra("channel_color", color);
            intent.putExtra("channel_icon_url", iconUrl);
            getActivity().setResult(-1, intent);
            getActivity().finish();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    private class ChannelsListSessionListener extends AppSessionListener {
        private ChannelsListSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            ((ChannelsAdapter) ChannelsListFragment.this.mCursorAdapter).setChannelImages(images);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetChannelsComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineChannel> channels) {
            if (statusCode == 200) {
                Iterator<VineChannel> it = channels.iterator();
                while (it.hasNext()) {
                    VineChannel channel = it.next();
                    ChannelsListFragment.this.mAllChannelIds.add(String.valueOf(channel.channelId));
                    if (channel.following || ChannelsListFragment.this.mIsNux) {
                        ChannelsListFragment.this.mSelectedChannelIds.add(String.valueOf(channel.channelId));
                    } else {
                        ChannelsListFragment.this.mFollowingAll = false;
                    }
                }
                if (ChannelsListFragment.this.mFollowingAll && ChannelsListFragment.this.mSelectAllButton != null) {
                    ChannelsListFragment.this.mSelectAllButton.setChecked(true);
                }
            }
            ChannelsListFragment.this.mResponseReceived = true;
        }
    }

    private class ChannelsAdapter extends CursorAdapter {
        private final ArrayList<WeakReference<ChannelViewHolder>> mViewHolders;

        public ChannelsAdapter(Context context, AppController appController, int flags) {
            super(context, (Cursor) null, flags);
            ChannelsListFragment.this.mAppController = appController;
            this.mViewHolders = new ArrayList<>();
        }

        @Override // android.support.v4.widget.CursorAdapter
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.channel_row_view, parent, false);
            ChannelViewHolder holder = ChannelsListFragment.this.new ChannelViewHolder(v);
            this.mViewHolders.add(new WeakReference<>(holder));
            v.setTag(holder);
            return v;
        }

        @Override // android.support.v4.widget.CursorAdapter
        public void bindView(View view, Context context, Cursor cursor) {
            String iconFullUrl;
            ChannelViewHolder holder = (ChannelViewHolder) view.getTag();
            holder.channelTitle.setText(cursor.getString(2));
            long channelId = cursor.getLong(1);
            holder.channelId = String.valueOf(channelId);
            try {
                String colorStr = cursor.getString(4);
                holder.channelColor = Color.parseColor(colorStr);
                holder.followButton.setCheckedColorStyle(Integer.valueOf(holder.channelColor));
            } catch (Exception e) {
            }
            if (channelId == ChannelsListFragment.this.mSelectedChannel) {
                view.setBackground(ChannelsListFragment.this.getResources().getDrawable(R.color.generic_selected));
            } else {
                view.setBackground(ChannelsListFragment.this.getResources().getDrawable(R.drawable.bg_generic));
            }
            if (ChannelsListFragment.this.getResources().getDisplayMetrics().densityDpi < 240) {
                iconFullUrl = cursor.getString(8);
            } else {
                iconFullUrl = cursor.getString(9);
            }
            if (!TextUtils.isEmpty(iconFullUrl)) {
                ImageKey key = new ImageKey(iconFullUrl);
                holder.channelImageKey = key;
                setChannelImage(holder, ChannelsListFragment.this.mAppController.getPhotoBitmap(key));
            } else {
                setChannelImage(holder, null);
            }
            if (ChannelsListFragment.this.mShowChannelFollow) {
                holder.followButton.setVisibility(0);
                holder.followButton.setChecked(ChannelsListFragment.this.mSelectedChannelIds.contains(holder.channelId));
            } else {
                holder.followButton.setVisibility(8);
            }
        }

        public synchronized void setChannelImages(HashMap<ImageKey, UrlImage> images) {
            ArrayList<WeakReference<ChannelViewHolder>> toRemove = new ArrayList<>();
            Iterator<WeakReference<ChannelViewHolder>> it = this.mViewHolders.iterator();
            while (it.hasNext()) {
                WeakReference<ChannelViewHolder> ref = it.next();
                ChannelViewHolder holder = ref.get();
                if (holder == null) {
                    toRemove.add(ref);
                } else {
                    UrlImage image = images.get(holder.channelImageKey);
                    if (image != null && image.isValid()) {
                        setChannelImage(holder, image.bitmap);
                    }
                }
            }
            Iterator<WeakReference<ChannelViewHolder>> it2 = toRemove.iterator();
            while (it2.hasNext()) {
                WeakReference<ChannelViewHolder> r = it2.next();
                this.mViewHolders.remove(r);
            }
        }

        private void setChannelImage(ChannelViewHolder holder, Bitmap bmp) {
            if (bmp != null) {
                holder.channelImage.setImageDrawable(new RecyclableBitmapDrawable(this.mContext.getResources(), bmp));
                if (holder.channelColor != 0) {
                    holder.channelImage.setBackground(new ColorDrawable(holder.channelColor));
                    return;
                }
                return;
            }
            holder.channelImage.setImageBitmap(null);
            holder.channelImage.setBackgroundColor(this.mContext.getResources().getColor(R.color.solid_light_gray));
        }
    }

    private class ChannelViewHolder {
        public int channelColor;
        public String channelId;
        public ImageView channelImage;
        public ImageKey channelImageKey;
        public TextView channelTitle;
        public VineToggleButton followButton;
        public ViewGroup rootLayout;

        public ChannelViewHolder(View view) {
            this.channelImage = (ImageView) view.findViewById(R.id.channel_image);
            this.channelTitle = (TextView) view.findViewById(R.id.channel);
            this.followButton = (VineToggleButton) view.findViewById(R.id.follow_button);
            this.followButton.setOnCheckedChangeListener(ChannelsListFragment.this);
            this.rootLayout = (ViewGroup) view.findViewById(R.id.root_layout);
            if (ChannelsListFragment.this.mShowChannelFollow) {
                this.rootLayout.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ChannelsListFragment.ChannelViewHolder.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ChannelViewHolder.this.followButton.performClick();
                    }
                });
            }
        }
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object holder = ((View) buttonView.getParent()).getTag();
        if (this.mSelectAllButton == buttonView) {
            if (isChecked) {
                this.mSelectedChannelIds.addAll(this.mAllChannelIds);
                this.mChannelsToUnfollow.removeAll(this.mAllChannelIds);
            } else {
                this.mSelectedChannelIds.removeAll(this.mAllChannelIds);
                this.mChannelsToUnfollow.addAll(this.mAllChannelIds);
            }
            this.mCursorAdapter.notifyDataSetChanged();
            return;
        }
        if (holder instanceof ChannelViewHolder) {
            String channelId = ((ChannelViewHolder) holder).channelId;
            if (isChecked) {
                this.mSelectedChannelIds.add(channelId);
                this.mChannelsToUnfollow.remove(channelId);
                return;
            }
            this.mSelectedChannelIds.remove(channelId);
            this.mChannelsToUnfollow.add(channelId);
            if (this.mSelectAllButton != null) {
                this.mSelectAllButton.setCheckedWithoutEvent(false);
            }
        }
    }
}
