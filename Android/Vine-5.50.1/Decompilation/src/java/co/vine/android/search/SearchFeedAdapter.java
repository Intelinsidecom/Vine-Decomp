package co.vine.android.search;

import android.app.Activity;
import android.widget.ListView;
import co.vine.android.api.SearchResult;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemUtil;
import co.vine.android.feedadapter.v2.FeedAdapter;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.viewmanager.CardViewManager;
import com.edisonwang.android.slog.SLogger;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class SearchFeedAdapter extends FeedAdapter {
    public SearchFeedAdapter(Activity context, ListView listView, ArrayList<CardViewManager> viewManagers, FeedAdapterItems posts, SLogger logger, FeedViewHolderCollection viewHolderCollection) {
        super(context, listView, viewManagers, logger, posts, viewHolderCollection);
    }

    public void replacePosts(SearchResult data) {
        if (data.getPosts() != null) {
            ArrayList<TimelineItem> items = TimelineItemUtil.wrapPostsInTimelineItemList(data.getPosts().getItems());
            super.replaceItems(items);
        } else {
            super.replaceItems(new ArrayList<>());
        }
    }

    public FeedViewHolderCollection getCollections() {
        return this.mViewHolders;
    }

    public FeedAdapterItems getPostAdapter() {
        return this.mItems;
    }
}
