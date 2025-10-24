package co.vine.android;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
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

/* loaded from: classes.dex */
public class ImportLikedVineFragment extends BaseGridViewFragment {
    private boolean mIsLoading;
    private ResourceLoader mLoader;
    private ModelEvents.ModelListener mModelListener;
    private List<VinePost> mPosts;
    private Set<VideoKey> mSelectedVines;
    private String mSortOrder;
    private TimelineDetails mTimelineDetails;
    private final AppSessionListener mVideoPathListener = new AppSessionListener() { // from class: co.vine.android.ImportLikedVineFragment.1
        @Override // co.vine.android.client.AppSessionListener
        public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
            if (ImportLikedVineFragment.this.mSelectedVines != null) {
                for (VideoKey videoKey : ImportLikedVineFragment.this.mSelectedVines) {
                    UrlVideo video = videos.get(videoKey);
                    if (video != null && video.isValid()) {
                        ImportLikedVineFragment.this.setLocalPath(videoKey.url, video.getAbsolutePath());
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
        this.mSortOrder = ProfileSortManager.getInstance().getUserProfileLikesSortOrder(getActivity(), this.mAppController.getActiveId());
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mAdapter = new ImportLikedVineAdapter();
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addOnScrollListener(new ImportLikedVineOnScrollListerner());
        this.mLoader = new ResourceLoader(getActivity(), this.mAppController);
        this.mModelListener = new ModelEvents.ModelListener() { // from class: co.vine.android.ImportLikedVineFragment.2
            @Override // co.vine.android.model.ModelEvents.ModelListener
            public void onTagsAdded(TagModel tagModel, String query) {
            }

            @Override // co.vine.android.model.ModelEvents.ModelListener
            public void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails) {
                if (timelineDetails.equals(ImportLikedVineFragment.this.mTimelineDetails)) {
                    ImportLikedVineFragment.this.updateLikedVines();
                    ImportLikedVineFragment.this.mIsLoading = false;
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
            this.mTimelineDetails = new TimelineDetails(3, Long.valueOf(this.mAppController.getActiveId()), this.mSortOrder);
        }
        if (this.mPosts == null) {
            this.mPosts = new ArrayList();
        }
        updateLikedVines();
        if (this.mPosts.isEmpty()) {
            fetchContent(3, UrlCachePolicy.CACHE_THEN_NETWORK);
        }
        this.mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: co.vine.android.ImportLikedVineFragment.3
            @Override // android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                ImportLikedVineFragment.this.fetchContent(2, UrlCachePolicy.FORCE_REFRESH);
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
    public void updateLikedVines() {
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
            handler.postDelayed(new Runnable() { // from class: co.vine.android.ImportLikedVineFragment.4
                @Override // java.lang.Runnable
                public void run() {
                    ImportLikedVineFragment.this.mPosts.remove(ImportLikedVineFragment.this.mPosts.size() - 1);
                    ImportLikedVineFragment.this.fetch(fetchType, cachePolicy);
                }
            }, 3000L);
            return;
        }
        fetch(fetchType, cachePolicy);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetch(int fetchType, UrlCachePolicy cachePolicy) {
        long userId = this.mAppController.getActiveId();
        Components.timelineFetchComponent().fetchPosts(this.mAppController, this.mAppController.getActiveSession(), 20, userId, 3, true, "" + userId, this.mSortOrder, null, cachePolicy, false, -1L, fetchType);
    }

    private class ImportLikedVineOnScrollListerner extends RecyclerView.OnScrollListener {
        private ImportLikedVineOnScrollListerner() {
        }

        @Override // android.support.v7.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int position = ImportLikedVineFragment.this.mGridLayoutManager.findLastVisibleItemPosition();
            int totalItemCount = ImportLikedVineFragment.this.mAdapter.getItemCount();
            boolean reachedEnd = position > totalItemCount - (ImportLikedVineFragment.this.mGridLayoutManager.getSpanCount() * 2);
            if (!ImportLikedVineFragment.this.mIsLoading && dy > 0 && reachedEnd && totalItemCount < 400 && PagingInfoModel.getInstance().hasMore(ImportLikedVineFragment.this.mTimelineDetails.getUniqueMarker())) {
                ImportLikedVineFragment.this.fetchContent(1, UrlCachePolicy.FORCE_REFRESH);
            }
        }
    }

    private class ImportLikedVineAdapter extends ThumbnailGridViewAdapter {
        private ImportLikedVineAdapter() {
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == 1) {
                final VinePost vinePost = (VinePost) ImportLikedVineFragment.this.mPosts.get(position);
                ThumbnailViewHolder viewHolder = (ThumbnailViewHolder) holder;
                viewHolder.setSelectedOrder(ImportLikedVineFragment.this.getVideoOrder(vinePost.videoUrl));
                viewHolder.getImageView().setImageBitmap(null);
                ResourceLoader.ImageViewImageSetter imageSetter = new ResourceLoader.ImageViewImageSetter(viewHolder.getImageView());
                ImportLikedVineFragment.this.mLoader.setImageWhenLoaded(imageSetter, vinePost.thumbnailUrl);
                holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ImportLikedVineFragment.ImportLikedVineAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        String sourceUrl = vinePost.videoUrl;
                        ImportLikedVineFragment.this.updateSelection(sourceUrl, null, "" + vinePost.postId);
                        VideoKey videoKey = new VideoKey(sourceUrl);
                        int position2 = ImportLikedVineFragment.this.getVideoOrder(sourceUrl);
                        if (position2 == -1 && ImportLikedVineFragment.this.mSelectedVines.contains(videoKey)) {
                            ImportLikedVineFragment.this.mSelectedVines.remove(videoKey);
                        } else if (position2 != -1 && !ImportLikedVineFragment.this.mSelectedVines.contains(videoKey)) {
                            ImportLikedVineFragment.this.mSelectedVines.add(videoKey);
                            String localPath = ImportLikedVineFragment.this.mAppController.getVideoFilePath(videoKey);
                            if (localPath != null) {
                                ImportLikedVineFragment.this.setLocalPath(sourceUrl, localPath);
                            }
                        }
                        ImportLikedVineFragment.this.mAdapter.notifyDataSetChanged();
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: co.vine.android.ImportLikedVineFragment.ImportLikedVineAdapter.2
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View v) {
                        ImportLikedVineFragment.this.toPreviewFragment(vinePost.videoUrl, vinePost);
                        return true;
                    }
                });
                if (vinePost.remixDisabled) {
                    disableRemix(viewHolder);
                    return;
                }
                return;
            }
            if (holder.getItemViewType() == 0) {
                ((ProgressBarViewHolder) holder).getProgress().setIndeterminate(true);
            }
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ImportLikedVineFragment.this.mPosts.size();
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemViewType(int position) {
            return ImportLikedVineFragment.this.mPosts.get(position) != null ? 1 : 0;
        }

        private void disableRemix(ThumbnailViewHolder holder) {
            Drawable drawable = holder.getImageView().getDrawable().mutate();
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0.0f);
            drawable.setColorFilter(new ColorMatrixColorFilter(matrix));
            holder.getImageView().setImageDrawable(drawable);
            holder.getRemixDisabledOverlay().setVisibility(0);
            holder.itemView.setOnLongClickListener(null);
            holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ImportLikedVineFragment.ImportLikedVineAdapter.3
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Toast.makeText(ImportLikedVineFragment.this.getActivity(), R.string.remix_disabled, 0).show();
                }
            });
        }
    }
}
