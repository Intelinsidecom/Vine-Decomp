package co.vine.android.api;

import co.vine.android.util.CrashUtil;
import java.util.ArrayList;
import java.util.Collection;

/* loaded from: classes.dex */
public final class TimelineItemUtil {
    public static ArrayList<TimelineItem> wrapPostsInTimelineItemList(Collection<VinePost> posts) {
        ArrayList<TimelineItem> items = new ArrayList<>();
        if (posts != null) {
            for (VinePost post : posts) {
                items.add(post);
            }
        }
        return items;
    }

    public static ArrayList<VinePost> getVinePostsFromItems(Collection<TimelineItem> items) {
        ArrayList<VinePost> posts = new ArrayList<>();
        if (items != null) {
            for (TimelineItem item : items) {
                if (item.getType() == TimelineItemType.POST) {
                    if (((VinePost) item).postId == 0) {
                        CrashUtil.logException(new IllegalArgumentException(), "getting post id 0 origin URL {}, user ID {}", ((VinePost) item).originUrl, Long.valueOf(((VinePost) item).userId), ((VinePost) item).description);
                    } else {
                        posts.add((VinePost) item);
                    }
                }
            }
        }
        return posts;
    }

    public static ArrayList<VinePost> getVinePostFromEndOfLastItem(Collection<TimelineItem> items, int cutoff) {
        ArrayList<VinePost> posts = new ArrayList<>();
        if (items != null) {
            for (TimelineItem item : items) {
                if (item.getType() == TimelineItemType.POST && cutoff <= 0) {
                    if (((VinePost) item).postId == 0) {
                        CrashUtil.logException(new IllegalArgumentException(), "getting post id 0 origin URL {}, user ID {}", ((VinePost) item).originUrl, Long.valueOf(((VinePost) item).userId), ((VinePost) item).description);
                    } else {
                        posts.add((VinePost) item);
                    }
                }
                cutoff--;
            }
        }
        return posts;
    }
}
