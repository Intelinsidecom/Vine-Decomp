package co.vine.android;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemUtil;
import co.vine.android.api.VinePost;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppSessionListener;
import co.vine.android.model.ModelEvents;
import co.vine.android.model.PagingInfoModel;
import co.vine.android.model.TagModel;
import co.vine.android.model.TimelineItemModel;
import co.vine.android.model.TimelineModel;
import co.vine.android.model.impl.Timeline;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.service.components.Components;
import co.vine.android.util.ResourceLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class DownloadPostsFragment extends BaseGridViewFragment {
    public Set<String> mDownloadedVines;
    private boolean mIsLoading;
    private ResourceLoader mLoader;
    private ModelEvents.ModelListener mModelListener;
    private List<VinePost> mPosts;
    public Set<VideoKey> mSelectedVines;
    private String mSortOrder;
    private TimelineDetails mTimelineDetails;
    private final AppSessionListener mVideoPathListener = new AppSessionListener() { // from class: co.vine.android.DownloadPostsFragment.1
        @Override // co.vine.android.client.AppSessionListener
        public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
            if (DownloadPostsFragment.this.mSelectedVines != null) {
                for (VideoKey videoKey : DownloadPostsFragment.this.mSelectedVines) {
                    UrlVideo video = videos.get(videoKey);
                    if (video != null && video.isValid()) {
                        DownloadPostsFragment.this.setLocalPath(videoKey.url, video.getAbsolutePath());
                    }
                }
            }
        }
    };

    @Override // co.vine.android.BaseGridViewFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPosts = new ArrayList();
        this.mSelectedVines = new HashSet();
        this.mDownloadedVines = new HashSet();
        this.mSortOrder = ProfileSortManager.getInstance().getUserProfilePostsSortOrder(getActivity(), this.mAppController.getActiveId());
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mAdapter = new DownloadPostsAdapter();
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addOnScrollListener(new DownloadPostsOnScrollListener());
        this.mLoader = new ResourceLoader(getActivity(), this.mAppController);
        this.mModelListener = new ModelEvents.ModelListener() { // from class: co.vine.android.DownloadPostsFragment.2
            @Override // co.vine.android.model.ModelEvents.ModelListener
            public void onTagsAdded(TagModel tagModel, String query) {
            }

            @Override // co.vine.android.model.ModelEvents.ModelListener
            public void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails) {
                if (timelineDetails.equals(DownloadPostsFragment.this.mTimelineDetails)) {
                    DownloadPostsFragment.this.updatePosts();
                    DownloadPostsFragment.this.mIsLoading = false;
                }
            }
        };
    }

    @Override // co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mModelListener != null) {
            VineModelFactory.getModelInstance().getModelEvents().addListener(this.mModelListener);
        }
        if (this.mTimelineDetails == null) {
            this.mTimelineDetails = new TimelineDetails(40, Long.valueOf(this.mAppController.getActiveId()), this.mSortOrder);
        }
        if (this.mPosts == null) {
            this.mPosts = new ArrayList();
        }
        updatePosts();
        if (this.mPosts.isEmpty()) {
            fetchContent(3, UrlCachePolicy.NETWORK_THEN_CACHE);
        }
        this.mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: co.vine.android.DownloadPostsFragment.3
            @Override // android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                DownloadPostsFragment.this.fetchContent(2, UrlCachePolicy.FORCE_REFRESH);
            }
        });
        this.mAppController.addListener(this.mVideoPathListener);
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mAppController.removeListener(this.mVideoPathListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePosts() {
        Timeline timeline = VineModelFactory.getModelInstance().getTimelineModel().getUserTimeline(this.mTimelineDetails);
        if (timeline != null && timeline.itemIds != null) {
            ArrayList<TimelineItem> items = new ArrayList<>();
            TimelineItemModel model = VineModelFactory.getModelInstance().getTimelineItemModel();
            Iterator<Long> it = timeline.itemIds.iterator();
            while (it.hasNext()) {
                long id = it.next().longValue();
                items.add(model.getTimelineItem(id));
            }
            ArrayList<VinePost> posts = TimelineItemUtil.getVinePostFromEndOfLastItem(items, this.mPosts.size());
            Iterator<VinePost> it2 = posts.iterator();
            while (it2.hasNext()) {
                VinePost post = it2.next();
                this.mLoader.prefetchImage(post.thumbnailUrl, 0);
            }
            this.mPosts.addAll(posts);
            this.mAdapter.notifyDataSetChanged();
            if (PagingInfoModel.getInstance().hasMore(this.mTimelineDetails.getUniqueMarker())) {
                fetchContent(1, UrlCachePolicy.FORCE_REFRESH);
            }
        }
        this.mRefreshLayout.setRefreshing(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchContent(final int fetchType, final UrlCachePolicy cachePolicy) {
        this.mIsLoading = true;
        if (fetchType == 1) {
            this.mPosts.add(null);
            this.mAdapter.notifyDataSetChanged();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() { // from class: co.vine.android.DownloadPostsFragment.4
                @Override // java.lang.Runnable
                public void run() {
                    DownloadPostsFragment.this.mPosts.remove(DownloadPostsFragment.this.mPosts.size() - 1);
                    DownloadPostsFragment.this.fetch(fetchType, cachePolicy);
                }
            }, 3000L);
            return;
        }
        fetch(fetchType, cachePolicy);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetch(int fetchType, UrlCachePolicy cachePolicy) {
        long userId = this.mAppController.getActiveId();
        Components.timelineFetchComponent().fetchPosts(this.mAppController, this.mAppController.getActiveSession(), HttpResponseCode.BAD_REQUEST, userId, 40, true, "" + userId, this.mSortOrder, null, cachePolicy, false, -1L, fetchType);
    }

    public void selectAll() {
        ((DownloadPostsAdapter) this.mAdapter).selectAll();
    }

    public void unSelectAll() {
        ((DownloadPostsAdapter) this.mAdapter).unSelectAll();
    }

    private class DownloadPostsOnScrollListener extends RecyclerView.OnScrollListener {
        private DownloadPostsOnScrollListener() {
        }

        @Override // android.support.v7.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int position = DownloadPostsFragment.this.mGridLayoutManager.findLastVisibleItemPosition();
            int totalItemCount = DownloadPostsFragment.this.mAdapter.getItemCount();
            boolean reachedEnd = position > totalItemCount - (DownloadPostsFragment.this.mGridLayoutManager.getSpanCount() * 2);
            if (!DownloadPostsFragment.this.mIsLoading && dy > 0 && reachedEnd && PagingInfoModel.getInstance().hasMore(DownloadPostsFragment.this.mTimelineDetails.getUniqueMarker())) {
                DownloadPostsFragment.this.fetchContent(1, UrlCachePolicy.FORCE_REFRESH);
            }
        }
    }

    private class DownloadPostsAdapter extends ThumbnailGridViewAdapter {
        private DownloadPostsAdapter() {
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == 1) {
                final VinePost vinePost = (VinePost) DownloadPostsFragment.this.mPosts.get(position);
                final ThumbnailViewHolder viewHolder = (ThumbnailViewHolder) holder;
                viewHolder.setSelected(DownloadPostsFragment.this.mSelectedVines.contains(new VideoKey(vinePost.videoUrl)));
                viewHolder.setDownloaded(DownloadPostsFragment.this.mDownloadedVines.contains(vinePost.videoUrl));
                viewHolder.getImageView().setImageBitmap(null);
                ResourceLoader.ImageViewImageSetter imageSetter = new ResourceLoader.ImageViewImageSetter(viewHolder.getImageView());
                DownloadPostsFragment.this.mLoader.setImageWhenLoaded(imageSetter, vinePost.thumbnailUrl);
                holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.DownloadPostsFragment.DownloadPostsAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        String sourceUrl = vinePost.videoUrl;
                        VideoKey videoKey = new VideoKey(sourceUrl);
                        if (DownloadPostsFragment.this.mSelectedVines.contains(videoKey)) {
                            DownloadPostsFragment.this.mSelectedVines.remove(videoKey);
                            viewHolder.setSelected(false);
                            DownloadPostsFragment.this.updateSelection();
                        } else {
                            DownloadPostsFragment.this.mSelectedVines.add(videoKey);
                            viewHolder.setSelected(true);
                            DownloadPostsFragment.this.updateSelection();
                        }
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: co.vine.android.DownloadPostsFragment.DownloadPostsAdapter.2
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View v) {
                        DownloadPostsFragment.this.toPreviewFragment(vinePost.videoUrl, vinePost);
                        return true;
                    }
                });
                return;
            }
            if (holder.getItemViewType() == 0) {
                ((ProgressBarViewHolder) holder).getProgress().setIndeterminate(true);
            }
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return DownloadPostsFragment.this.mPosts.size();
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemViewType(int position) {
            return DownloadPostsFragment.this.mPosts.get(position) != null ? 1 : 0;
        }

        public void selectAll() {
            for (VinePost post : DownloadPostsFragment.this.mPosts) {
                String sourceUrl = post.videoUrl;
                VideoKey videoKey = new VideoKey(sourceUrl);
                if (!DownloadPostsFragment.this.mSelectedVines.contains(videoKey)) {
                    DownloadPostsFragment.this.mSelectedVines.add(videoKey);
                }
            }
            DownloadPostsFragment.this.updateSelection();
            DownloadPostsFragment.this.mAdapter.notifyDataSetChanged();
        }

        public void unSelectAll() {
            for (VinePost post : DownloadPostsFragment.this.mPosts) {
                String sourceUrl = post.videoUrl;
                VideoKey videoKey = new VideoKey(sourceUrl);
                if (DownloadPostsFragment.this.mSelectedVines.contains(videoKey)) {
                    DownloadPostsFragment.this.mSelectedVines.remove(videoKey);
                }
            }
            DownloadPostsFragment.this.updateSelection();
            DownloadPostsFragment.this.mAdapter.notifyDataSetChanged();
        }
    }

    public static DownloadPostsFragment newInstance(Bundle bundle) {
        DownloadPostsFragment fragment = new DownloadPostsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
