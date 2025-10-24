package co.vine.android.feedadapter.v2;

import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.VinePost;
import co.vine.android.cache.CacheKey;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.LongSparseArray;
import com.edisonwang.android.slog.SLog;
import com.edisonwang.android.slog.SLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class FeedAdapterItems {
    private final SLogger mLogger;
    private final ArrayList<TimelineItem> mItems = new ArrayList<>();
    private final LongSparseArray<String> mPaths = new LongSparseArray<>();
    private final LongSparseArray<VideoKey> mVideoKeys = new LongSparseArray<>();
    private final HashMap<VideoKey, Long> mUrlReverse = new HashMap<>();

    public FeedAdapterItems(SLogger logger) {
        this.mLogger = logger;
    }

    public int size() {
        return this.mItems.size();
    }

    public TimelineItem getItem(int position) {
        if (position < 0 || position >= this.mItems.size()) {
            return null;
        }
        return this.mItems.get(position);
    }

    public long getItemId(int position) {
        TimelineItem item = getItem(position);
        if (item != null) {
            return item.getId();
        }
        return 0L;
    }

    public void updateItemForId(long id, TimelineItem item) {
        for (int i = 0; i < this.mItems.size(); i++) {
            TimelineItem itemToReplace = this.mItems.get(i);
            if (itemToReplace.getId() == id && item.getType() == TimelineItemType.POST) {
                this.mItems.set(i, item);
                return;
            }
        }
    }

    public void remove(int position) {
        this.mItems.remove(position);
    }

    public ArrayList<TimelineItem> addAll(int position, ArrayList<TimelineItem> items, int limit) {
        if (position < 0 || position >= this.mItems.size()) {
            SLog.d("Invalid insert point for similar posts: " + position);
            return null;
        }
        if (limit >= items.size()) {
            limit = items.size();
        }
        List<TimelineItem> itemsToAdd = items.subList(0, limit);
        if (position + 1 < this.mItems.size()) {
            this.mItems.addAll(position + 1, itemsToAdd);
        } else {
            this.mItems.addAll(itemsToAdd);
        }
        return new ArrayList<>(itemsToAdd);
    }

    public void mergeItems(ArrayList<TimelineItem> newItems) {
        printIds("newItems", newItems);
        printIds("oldItems", this.mItems);
        HashMap<Long, Integer> usedViewIds = new HashMap<>();
        ArrayList<TimelineItem> items = new ArrayList<>(this.mItems);
        int size = items.size();
        for (int i = 0; i < size; i++) {
            usedViewIds.put(Long.valueOf(items.get(i).getId()), Integer.valueOf(i));
        }
        Iterator<TimelineItem> it = newItems.iterator();
        while (it.hasNext()) {
            TimelineItem newItem = it.next();
            Integer position = usedViewIds.get(Long.valueOf(newItem.getId()));
            if (position == null) {
                items.add(newItem);
            } else {
                items.set(position.intValue(), newItem);
            }
        }
        this.mLogger.i("{} : {}", "activePosts", usedViewIds.keySet());
        this.mItems.clear();
        this.mItems.addAll(items);
        printIds("mergedLists", this.mItems);
    }

    private void printIds(String tag, Collection<TimelineItem> items) {
        if (CommonUtil.DEBUG_VERBOSE) {
            long[] ids = new long[items.size()];
            int m = 0;
            for (TimelineItem item : items) {
                ids[m] = item.getId();
                m++;
            }
            this.mLogger.i("{} : {}", tag, ids);
        }
    }

    public int prependItemsIfOverlaps(List<TimelineItem> newItems) {
        printIds("newItems", newItems);
        printIds("oldItems", this.mItems);
        HashMap<Long, TimelineItem> ids = new HashMap<>();
        Iterator<TimelineItem> it = this.mItems.iterator();
        while (it.hasNext()) {
            TimelineItem item = it.next();
            if (item.getType() != TimelineItemType.POST_MOSAIC) {
                ids.put(Long.valueOf(item.getId()), item);
            }
        }
        ArrayList<TimelineItem> resolvedItems = new ArrayList<>();
        int added = 0;
        int postsAdded = 0;
        for (TimelineItem item2 : newItems) {
            if (item2.getType() != TimelineItemType.USER_MOSAIC && (item2.getType() == TimelineItemType.POST_MOSAIC || !ids.containsKey(Long.valueOf(item2.getId())))) {
                resolvedItems.add(item2);
                added++;
                if (item2.getType() == TimelineItemType.POST) {
                    postsAdded++;
                }
            }
        }
        if ((added != 1 || resolvedItems.get(0).getType() != TimelineItemType.POST_MOSAIC) && added != newItems.size()) {
            Iterator<TimelineItem> it2 = this.mItems.iterator();
            while (it2.hasNext()) {
                TimelineItem item3 = it2.next();
                if (item3.getType() != TimelineItemType.POST_MOSAIC) {
                    TimelineItem updated = ids.get(Long.valueOf(item3.getId()));
                    if (updated == null) {
                        resolvedItems.add(item3);
                    } else {
                        resolvedItems.add(updated);
                    }
                }
            }
            this.mItems.clear();
            this.mItems.addAll(resolvedItems);
            printIds("newItems", this.mItems);
            return postsAdded;
        }
        return -1;
    }

    public void addOlderPosts(List<TimelineItem> newItems) {
        this.mItems.addAll(newItems);
    }

    public void replace(List<TimelineItem> newItems) {
        this.mItems.clear();
        this.mItems.addAll(newItems);
        clearPaths();
        clearUrlReverse();
    }

    public void removeWithId(long id) {
        int i = 0;
        while (true) {
            if (i >= this.mItems.size()) {
                break;
            }
            if (this.mItems.get(i).getId() != id) {
                i++;
            } else {
                this.mItems.remove(i);
                break;
            }
        }
        this.mPaths.remove(id);
        removeFromUrlReverse(id);
    }

    public void removeWithUserId(long userId) {
        for (int i = 0; i < this.mItems.size(); i++) {
            if (this.mItems.get(i).getType() == TimelineItemType.POST) {
                VinePost post = (VinePost) this.mItems.get(i);
                if (post.userId == userId) {
                    this.mItems.remove(i);
                    this.mPaths.remove(post.postId);
                    removeFromUrlReverse(post.postId);
                }
            }
        }
    }

    public void toggleHideWithId(long id) {
        for (int i = 0; i < this.mItems.size(); i++) {
            if (this.mItems.get(i).getId() == id) {
                ((VinePost) this.mItems.get(i)).hidden = !((VinePost) this.mItems.get(i)).hidden;
                return;
            }
        }
    }

    private void removeFromUrlReverse(long postId) {
        ArrayList<VideoKey> toRemove = new ArrayList<>();
        for (Map.Entry<VideoKey, Long> entry : this.mUrlReverse.entrySet()) {
            Long id = entry.getValue();
            if (id != null && id.longValue() == postId) {
                toRemove.add(entry.getKey());
            }
        }
        Iterator<VideoKey> it = toRemove.iterator();
        while (it.hasNext()) {
            VideoKey key = it.next();
            this.mUrlReverse.remove(key);
        }
    }

    public String getPath(long postId) {
        return this.mPaths.get(postId);
    }

    public void putPath(long postId, String path, VideoKey url) {
        this.mPaths.put(postId, path);
        if (url != null) {
            this.mVideoKeys.put(postId, url);
        }
    }

    public void putUrlReverse(VideoKey url, long postId) {
        this.mUrlReverse.put(url, Long.valueOf(postId));
    }

    public void clearPaths() {
        this.mPaths.clear();
        this.mVideoKeys.clear();
    }

    public void clearUrlReverse() {
        this.mUrlReverse.clear();
    }

    public void removePath(long postId) {
        this.mPaths.remove(postId);
        this.mVideoKeys.remove(postId);
    }

    public CacheKey getKey(long postId) {
        return this.mVideoKeys.get(postId);
    }

    public Long getUrlReverse(VideoKey key) {
        return this.mUrlReverse.get(key);
    }
}
