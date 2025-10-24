package co.vine.android.feedadapter.v2;

import android.app.Activity;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import co.vine.android.ViewOffsetResolver;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.VineMosaic;
import co.vine.android.api.VinePost;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.feedadapter.viewmanager.CardViewManager;
import co.vine.android.feedadapter.viewmanager.PostViewManager;
import co.vine.android.util.FeedImageViewUtils;
import com.edisonwang.android.slog.SLog;
import com.edisonwang.android.slog.SLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class FeedAdapter extends BaseAdapter {
    private final Activity mContext;
    protected final FeedAdapterItems mItems;
    private final ListView mListView;
    private final SLogger mLogger;
    protected final FeedViewHolderCollection mViewHolders;
    private final ArrayList<CardViewManager> mViewManagersList;
    public final ArrayMap<ViewType, CardViewManager> mViewManagersMap = new ArrayMap<>();
    private ViewOffsetResolver mViewOffsetResolver;

    public FeedAdapter(Activity context, ListView listView, ArrayList<CardViewManager> viewManagers, SLogger logger, FeedAdapterItems items, FeedViewHolderCollection viewHolderCollection) {
        this.mContext = context;
        this.mLogger = logger;
        this.mViewHolders = viewHolderCollection;
        this.mListView = listView;
        this.mListView.setDivider(null);
        this.mItems = items;
        this.mLogger.d("Adapter constructed, accelerated? {}", Boolean.valueOf(listView.isHardwareAccelerated()));
        Iterator<CardViewManager> it = viewManagers.iterator();
        while (it.hasNext()) {
            CardViewManager viewManager = it.next();
            this.mViewManagersMap.put(viewManager.getType(), viewManager);
        }
        this.mViewManagersList = viewManagers;
    }

    public void setProfileColor(int color) {
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.setProfileColor(color);
        }
    }

    protected void resetStates(boolean hasDataSetChanged) {
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.resetStates(hasDataSetChanged);
        }
    }

    public void pausePlayer() {
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.pausePlayer();
        }
    }

    public void onScrollIdle() {
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.onScrollIdle();
        }
    }

    public boolean isPlaying() {
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            if (manager.isPlaying()) {
                return true;
            }
        }
        return false;
    }

    public void removeItem(long itemId) {
        this.mItems.removeWithId(itemId);
        notifyDataSetChanged();
        playCurrentPosition();
    }

    public long findNextPost(int index) {
        for (int i = index < 0 ? 0 : index; i < getCount(); i++) {
            TimelineItem item = (TimelineItem) getItem(i);
            if (item != null && item.getType().equals(TimelineItemType.POST)) {
                return item.getId();
            }
        }
        return -1L;
    }

    public void removeAllFromUser(long userId) {
        this.mItems.removeWithUserId(userId);
        notifyDataSetChanged();
        playCurrentPosition();
    }

    public void togglePostHide(long postId) {
        this.mItems.toggleHideWithId(postId);
        notifyDataSetChanged();
    }

    public synchronized void onPause(boolean focused) {
        this.mLogger.d("OnPause {}. {}", Boolean.valueOf(focused), this);
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.onPause(focused);
        }
        onFocusChanged(focused);
    }

    public void onFocusChanged(boolean focused) {
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.onFocusChanged(focused);
        }
        resetStates(false);
    }

    public synchronized void onResume(boolean focused) {
        this.mLogger.d("OnResume {}. {}", Boolean.valueOf(focused), this);
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.onResume(focused);
        }
    }

    public void onDestroy() {
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.onDestroy();
        }
    }

    public void toggleMute(boolean mute) {
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.toggleMute(mute);
        }
    }

    public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
        this.mLogger.d("CALLBACK onVideoPathObtained");
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.onVideoPathObtained(videos);
        }
    }

    public void onDestroyView() {
        Iterator<CardViewManager> it = this.mViewManagersList.iterator();
        while (it.hasNext()) {
            CardViewManager manager = it.next();
            manager.onDestroyView();
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mItems.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.mItems.getItem(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewType type = getItemViewTypeEnum(i);
        CardViewManager viewManager = this.mViewManagersMap.get(type);
        if (viewManager != null) {
            return viewManager.newView(i, view, viewGroup);
        }
        throw new IllegalArgumentException("Unsupported view type: " + type);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        return ViewType.getValue(getItemViewTypeEnum(position));
    }

    private ViewType getItemViewTypeEnum(int position) {
        TimelineItem item = this.mItems.getItem(position);
        if (item != null) {
            switch (item.getType()) {
                case POST:
                    return ViewType.POST;
                case POST_MOSAIC:
                    VineMosaic mosaic = (VineMosaic) item;
                    if ("default".equals(mosaic.mosaicType)) {
                        return ViewType.SUGGESTED_FEED;
                    }
                    if ("avatarIncluded".equals(mosaic.mosaicType)) {
                        return ViewType.SIMILAR_USER;
                    }
                    break;
                case USER_MOSAIC:
                    return ViewType.SUGGESTED_USERS;
                case URL_ACTION:
                    return ViewType.URL_ACTION;
                case SOLICITOR:
                    return ViewType.SOLICITOR;
                case FEED:
                    return ViewType.FEED;
            }
        }
        return ViewType.ERROR;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 7;
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return this.mItems.getItemId(position);
    }

    public void addOlderItems(ArrayList<TimelineItem> newItems) {
        this.mItems.addOlderPosts(newItems);
        notifyDataSetChanged();
    }

    public void replaceItems(ArrayList<TimelineItem> newItems) {
        this.mItems.replace(newItems);
        notifyDataSetChanged();
    }

    public int getCurrentPosition() {
        return ((PostViewManager) this.mViewManagersMap.get(ViewType.POST)).getCurrentlyPlayingPosition();
    }

    public void removeItemAtPosition(int position) {
        TimelineItem item = this.mItems.getItem(position);
        ViewType type = getItemViewTypeEnum(position);
        CardViewManager viewManager = this.mViewManagersMap.get(type);
        if (viewManager != null) {
            this.mViewManagersMap.get(getItemViewTypeEnum(position)).onRemoveItem(item);
            this.mItems.remove(position);
            notifyDataSetChanged();
            return;
        }
        throw new IllegalArgumentException("Unsupported view type: " + type + " for position: " + position);
    }

    public void removePostMosaic() {
        TimelineItem item = this.mItems.getItem(0);
        if (item.getType().equals(TimelineItemType.POST_MOSAIC)) {
            this.mItems.remove(0);
            notifyDataSetChanged();
        }
    }

    public void injectTimelineItemAtPosition(TimelineItem item, int position) {
        ArrayList<TimelineItem> list = new ArrayList<>();
        list.add(item);
        this.mItems.addAll(position, list, 1);
        this.mItems.clearPaths();
        this.mItems.clearUrlReverse();
        notifyDataSetChanged();
    }

    public void clearData() {
        replaceItems(new ArrayList<>());
        notifyDataSetChanged();
    }

    public int prependItems(ArrayList<TimelineItem> newItems) {
        boolean wasEmpty = this.mItems.size() == 0;
        int added = this.mItems.prependItemsIfOverlaps(newItems);
        if (added == -1) {
            return -1;
        }
        if (!wasEmpty) {
            int index = this.mListView.getFirstVisiblePosition();
            int foundFirstChild = -1;
            int i = 0;
            while (true) {
                if (i >= this.mListView.getChildCount()) {
                    break;
                }
                if (this.mViewManagersMap.get(ViewType.POST).getValidViewHolder(i) == null) {
                    i++;
                } else {
                    foundFirstChild = i;
                    break;
                }
            }
            int extraNeeded = 1;
            if (foundFirstChild == 2) {
                extraNeeded = 2;
            }
            SLog.i("First child is at index {}", Integer.valueOf(foundFirstChild));
            int hvc = this.mListView.getHeaderViewsCount();
            View v = this.mListView.getChildAt(hvc);
            int top = v == null ? 0 : v.getTop();
            notifyDataSetChanged();
            this.mListView.setSelectionFromTop(index + added + extraNeeded, top);
            SLog.i("Scroll: {}, {}, {}, {}", new Object[]{Integer.valueOf(index), Integer.valueOf(added), Integer.valueOf(hvc), Integer.valueOf(top)});
            return added;
        }
        notifyDataSetChanged();
        return 0;
    }

    public void mergeItems(ArrayList<TimelineItem> newItems) {
        this.mItems.mergeItems(newItems);
        notifyDataSetChanged();
    }

    public void mergePost(VinePost post) {
        ArrayList<TimelineItem> items = new ArrayList<>();
        items.add(post);
        mergeItems(items);
    }

    private void playCurrentPosition() {
        ((PostViewManager) this.mViewManagersMap.get(ViewType.POST)).playCurrentPosition();
    }

    public int getItemIndexForPostWithTimelineItemId(long id) {
        for (int i = 0; i < this.mItems.size(); i++) {
            TimelineItem item = this.mItems.getItem(i);
            if (item != null && item.getType() == TimelineItemType.POST && ((VinePost) item).timelineItemId == id) {
                return i;
            }
        }
        return -1;
    }

    public String getVideoPathForPostId(long postId) {
        return this.mItems.getPath(postId);
    }

    public void updateCurrentItem(TimelineItem item) {
        this.mItems.updateItemForId(item.getId(), item);
        notifyDataSetChanged();
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        resetStates(true);
    }

    public boolean onBackPressed() {
        return false;
    }

    public void setOffsetResolver(ViewOffsetResolver resolver) {
        this.mViewOffsetResolver = resolver;
    }

    public void setImages(HashMap<ImageKey, UrlImage> images) {
        FeedImageViewUtils.setFeedImages(images, this.mViewHolders.getViewHolders(), this.mViewManagersList, this.mContext, this.mLogger);
    }

    public void onTrimMemory(int level) {
        this.mLogger.e("onTrimMemory called.");
    }

    public void onLowMemory() {
        this.mLogger.e("onLowMemory called.");
    }
}
