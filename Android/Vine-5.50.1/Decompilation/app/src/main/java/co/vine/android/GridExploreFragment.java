package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.search.SearchActivity;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.MediaUtil;
import co.vine.android.util.ResourceLoader;
import co.vine.android.widget.ScrollAwayFrameLayout;
import co.vine.android.widget.TypefacesTextView;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class GridExploreFragment extends BaseAdapterFragment {
    ChannelsAdapter mAdapter;
    private AppController mAppController;
    private boolean mEnableLetterboxDetecton;
    private ScheduledExecutorService mExecutorService;
    private PendingRequestHelper mPendingRequestHelper;
    private RecyclerView mRecyclerView;
    private ResourceLoader mResourceLoader;
    private ScrollAwayFrameLayout mSearchBarContainer;
    private TimelineFetchActionsListener mTimelineFetchActionsListener;
    private String onTheRiseRequestId;
    private String popNowRequestId;
    private ArrayList<VineChannel> mRegularChannels = new ArrayList<>();
    private VineChannel popNow = new VineChannel();
    private VineChannel onTheRise = new VineChannel();
    private Random mRandomGenerator = new Random();

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.popNow.channel = getResources().getString(R.string.popular_now);
        this.popNow.timeline = new VineChannel.TimeLine();
        this.onTheRise.channel = getResources().getString(R.string.on_the_rise);
        this.onTheRise.timeline = new VineChannel.TimeLine();
        this.mTimelineFetchActionsListener = new TimelineFetchActionsListener() { // from class: co.vine.android.GridExploreFragment.1
            @Override // co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener
            public void onChannelsFetched(String reqId, int statusCode, String reasonPhrase, Bundle bundle) {
                PendingRequest req = GridExploreFragment.this.mPendingRequestHelper.removeRequest(reqId);
                if (req != null && statusCode == 200 && bundle != null) {
                    GridExploreFragment.this.mRegularChannels = bundle.getParcelableArrayList("channels");
                    if (GridExploreFragment.this.mAdapter == null) {
                        GridExploreFragment.this.mAdapter = GridExploreFragment.this.new ChannelsAdapter(GridExploreFragment.this.getActivity());
                        GridExploreFragment.this.mRecyclerView.setAdapter(GridExploreFragment.this.mAdapter);
                    } else {
                        GridExploreFragment.this.mAdapter.updateChannels();
                        GridExploreFragment.this.mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override // co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener
            public void onTimelineFetched(String reqId, int statusCode, String reasonPhrase, int type, int count, boolean memory, boolean userInitiated, int size, String title, UrlCachePolicy cachePolicy, boolean network, Bundle bundle) {
            }
        };
        setAppSessionListener(new TimelineFetchListener());
    }

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mExecutorService = Executors.newScheduledThreadPool(1);
        Components.timelineFetchComponent().addListener(this.mTimelineFetchActionsListener);
        if (this.mAdapter == null) {
            fetchContent(UrlCachePolicy.CACHE_THEN_NETWORK);
        } else {
            fetchContent(UrlCachePolicy.NETWORK_THEN_CACHE);
        }
    }

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mExecutorService.shutdownNow();
        Components.timelineFetchComponent().removeListener(this.mTimelineFetchActionsListener);
    }

    @Override // co.vine.android.BaseFragment
    protected AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.EXPLORE_GRID;
    }

    private void fetchContent(UrlCachePolicy urlCachePolicy) {
        this.popNowRequestId = this.mAppController.fetchPosts(this.mAppController.getActiveSession(), 5, this.mAppController.getActiveId(), 5, 1, null, null, false, null, null, null, urlCachePolicy, false);
        this.mPendingRequestHelper.addRequest(this.popNowRequestId);
        this.onTheRiseRequestId = this.mAppController.fetchPosts(this.mAppController.getActiveSession(), 5, this.mAppController.getActiveId(), 4, 1, null, null, false, null, null, null, urlCachePolicy, false);
        this.mPendingRequestHelper.addRequest(this.onTheRiseRequestId);
        this.mPendingRequestHelper.addRequest(Components.timelineFetchComponent().fetchChannels(this.mAppController, this.mAppController.getActiveSession(), urlCachePolicy), 4);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.grid_view_explore, container, false);
        this.mSearchBarContainer = (ScrollAwayFrameLayout) v.findViewById(R.id.search_bar_container);
        this.mSearchBarContainer.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.GridExploreFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v2) {
                GridExploreFragment.this.startSectionedSearchActivity();
            }
        });
        this.mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: co.vine.android.GridExploreFragment.3
            @Override // android.support.v7.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int position) {
                return (GridExploreFragment.this.mAdapter.getItemCount() % 2 == 1 && position == GridExploreFragment.this.mAdapter.getItemCount() + (-1)) ? 2 : 1;
            }
        });
        this.mRecyclerView.setLayoutManager(gridLayoutManager);
        int spacingInPixels = MediaUtil.convertDpToPixel(1, getActivity());
        this.mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        return v;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mAppController = AppController.getInstance(getActivity());
        this.mResourceLoader = new ResourceLoader(getActivity(), this.mAppController);
        this.mPendingRequestHelper = new PendingRequestHelper();
        this.mPendingRequestHelper.onCreate(this.mAppController, savedInstanceState);
        this.mEnableLetterboxDetecton = ClientFlagsHelper.isLetterboxDetectionForExploreEnabled(getActivity());
    }

    private class ChannelsAdapter extends RecyclerView.Adapter<ViewHolder> {
        private Context mContext;
        private ArrayList<VineChannel> mItems = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TypefacesTextView channelName;
            public ImageView channelThumbnail;
            public Future future;
            public int index;
            public ArrayList<TimelineItem> items;

            public ViewHolder(View v) {
                super(v);
                this.channelThumbnail = (ImageView) v.findViewById(R.id.channel_thumbnail);
                this.channelName = (TypefacesTextView) v.findViewById(R.id.channel_name);
                this.channelName.setWeight(4);
            }
        }

        public ChannelsAdapter(Context context) {
            this.mContext = context;
            this.mItems.add(GridExploreFragment.this.popNow);
            this.mItems.add(GridExploreFragment.this.onTheRise);
            this.mItems.addAll(GridExploreFragment.this.mRegularChannels);
            prefetchFirstThumbnails();
        }

        public void updateChannels() {
            this.mItems.clear();
            this.mItems.add(GridExploreFragment.this.popNow);
            this.mItems.add(GridExploreFragment.this.onTheRise);
            this.mItems.addAll(GridExploreFragment.this.mRegularChannels);
            prefetchFirstThumbnails();
        }

        private void prefetchFirstThumbnails() {
            Iterator<VineChannel> it = this.mItems.iterator();
            while (it.hasNext()) {
                VineChannel channel = it.next();
                VineChannel.TimeLine timeline = channel.timeline;
                if (timeline != null && timeline.items != null && timeline.items.size() > 0 && (timeline.items.get(0) instanceof VinePost)) {
                    VinePost firstPost = (VinePost) timeline.items.get(0);
                    GridExploreFragment.this.mResourceLoader.prefetchImage(firstPost.thumbnailUrl, 0, GridExploreFragment.this.mEnableLetterboxDetecton);
                }
            }
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View channelView = inflater.inflate(R.layout.channel_grid_cell, parent, false);
            channelView.getLayoutParams().width = parent.getWidth();
            channelView.getLayoutParams().height = parent.getWidth() / 2;
            return new ViewHolder(channelView);
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public void onBindViewHolder(ViewHolder holder, int position) {
            final VineChannel channel = this.mItems.get(position);
            VineChannel.TimeLine timeline = channel.timeline;
            holder.index = 0;
            if (timeline != null && timeline.items != null) {
                ArrayList<TimelineItem> items = timeline.items;
                holder.items = items;
                while (!(items.get(holder.index) instanceof VinePost)) {
                    holder.index++;
                }
                VinePost firstPost = (VinePost) items.get(holder.index);
                Animation fadeInAnimation = new AlphaAnimation(0.2f, 1.0f);
                GridExploreFragment.this.setupAnimation(fadeInAnimation, new AccelerateInterpolator(), HttpResponseCode.OK, true);
                GridExploreFragment.this.mResourceLoader.setImageWhenLoadedWithAnimation(new ResourceLoader.ImageViewImageSetter(holder.channelThumbnail), firstPost.thumbnailUrl, false, fadeInAnimation, GridExploreFragment.this.mEnableLetterboxDetecton);
                holder.index++;
                if (holder.index < holder.items.size() && (holder.items.get(holder.index) instanceof VinePost)) {
                    VinePost nextPost = (VinePost) holder.items.get(holder.index + 1);
                    GridExploreFragment.this.mResourceLoader.prefetchImage(nextPost.thumbnailUrl, 0, GridExploreFragment.this.mEnableLetterboxDetecton);
                }
                if (ClientFlagsHelper.isThumbnailTransitionEnabled(GridExploreFragment.this.getActivity())) {
                    holder.future = GridExploreFragment.this.mExecutorService.schedule(GridExploreFragment.this.new ScheduleThumbnailUpdateRunnable(holder), Math.round(1000.0f * ((GridExploreFragment.this.mRandomGenerator.nextFloat() * 5.0f) + 1.0f)), TimeUnit.MILLISECONDS);
                }
            }
            holder.channelName.setText(channel.channel);
            holder.channelThumbnail.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.GridExploreFragment.ChannelsAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (!channel.channel.equals(GridExploreFragment.this.popNow.channel)) {
                        if (channel.channel.equals(GridExploreFragment.this.onTheRise.channel)) {
                            ChannelsAdapter.this.mContext.startActivity(new Intent(GridExploreFragment.this.getActivity(), (Class<?>) ExploreVideoListActivity.class).setData(Uri.parse("vine://trending-people")));
                            return;
                        } else {
                            ChannelActivity.startExploreChannel(GridExploreFragment.this.getActivity(), channel, false);
                            return;
                        }
                    }
                    Intent i = new Intent(GridExploreFragment.this.getActivity(), (Class<?>) ExploreVideoListActivity.class).setData(Uri.parse("vine://popular-now"));
                    i.putExtra("channel_type", "popular-now");
                    ChannelsAdapter.this.mContext.startActivity(i);
                }
            });
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled((ChannelsAdapter) holder);
            if (holder.future != null) {
                holder.future.cancel(true);
                holder.channelThumbnail.setImageResource(0);
            }
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mItems.size();
        }
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected void hideProgress(int progressType) {
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected void showProgress(int progressType) {
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override // android.support.v7.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = 0;
            int viewPosition = parent.getChildLayoutPosition(view);
            if (viewPosition == 0 || viewPosition == 1) {
                outRect.top = 0;
            } else {
                outRect.top = this.space;
            }
            boolean isSingleLastItem = false;
            if (GridExploreFragment.this.mAdapter.getItemCount() % 2 == 1 && viewPosition == GridExploreFragment.this.mAdapter.getItemCount() - 1) {
                isSingleLastItem = true;
            }
            if (viewPosition % 2 == 0 && !isSingleLastItem) {
                outRect.right = this.space;
            } else {
                outRect.right = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSectionedSearchActivity() {
        Intent intent = new Intent(getActivity(), (Class<?>) SearchActivity.class);
        intent.putExtra("enter_anim", R.anim.activity_open_enter_slide);
        startActivity(intent);
    }

    private class TimelineFetchListener extends AppSessionListener {
        private TimelineFetchListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetTimeLineComplete(String reqId, int statusCode, String reasonPhrase, int type, int count, boolean memory, ArrayList<TimelineItem> items, String tag, int pageType, int next, int previous, String anchor, String backAnchor, boolean userInitiated, int size, String title, UrlCachePolicy cachePolicy, boolean network, Bundle bundle) {
            PendingRequest req = GridExploreFragment.this.mPendingRequestHelper.removeRequest(reqId);
            if (req != null) {
                if (reqId.equals(GridExploreFragment.this.popNowRequestId)) {
                    GridExploreFragment.this.popNow.timeline.items = items;
                    if (GridExploreFragment.this.mAdapter != null) {
                        GridExploreFragment.this.mAdapter.updateChannels();
                        return;
                    }
                    return;
                }
                if (reqId.equals(GridExploreFragment.this.onTheRiseRequestId)) {
                    GridExploreFragment.this.onTheRise.timeline.items = items;
                    if (GridExploreFragment.this.mAdapter != null) {
                        GridExploreFragment.this.mAdapter.updateChannels();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupAnimation(Animation animation, Interpolator interpolator, int duration, boolean fillAfter) {
        animation.setInterpolator(interpolator);
        animation.setDuration(duration);
        animation.setFillAfter(fillAfter);
        animation.setAnimationListener(null);
    }

    private class ScheduleThumbnailUpdateRunnable implements Runnable {
        ChannelsAdapter.ViewHolder holder;

        public ScheduleThumbnailUpdateRunnable(ChannelsAdapter.ViewHolder viewHolder) {
            this.holder = viewHolder;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (this.holder.index >= this.holder.items.size()) {
                    this.holder.index = 0;
                }
                TimelineItem item = this.holder.items.get(this.holder.index);
                while (!(item instanceof VinePost)) {
                    this.holder.index++;
                }
                if (this.holder.index + 1 < this.holder.items.size() && (this.holder.items.get(this.holder.index + 1) instanceof VinePost)) {
                    VinePost nextPost = (VinePost) this.holder.items.get(this.holder.index + 1);
                    GridExploreFragment.this.mResourceLoader.prefetchImage(nextPost.thumbnailUrl, 0, GridExploreFragment.this.mEnableLetterboxDetecton);
                }
                Handler handler = new Handler(GridExploreFragment.this.getActivity().getMainLooper());
                handler.post(GridExploreFragment.this.new UpdateThumbnailRunnable(this.holder));
            } catch (Exception e) {
                SLog.i("Grid Explore thumbnail transition while loop", (Throwable) e);
            }
        }
    }

    private class UpdateThumbnailRunnable implements Runnable {
        ChannelsAdapter.ViewHolder mHolder;
        ImageView mImageView;
        String mThumbnailUrl;
        Animation mFadeOutAnimation = new AlphaAnimation(1.0f, 0.2f);
        Animation mFadeInAnimation = new AlphaAnimation(0.2f, 1.0f);

        public UpdateThumbnailRunnable(ChannelsAdapter.ViewHolder viewHolder) {
            this.mHolder = viewHolder;
            this.mImageView = this.mHolder.channelThumbnail;
            if (this.mHolder.items.get(this.mHolder.index) instanceof VinePost) {
                VinePost post = (VinePost) this.mHolder.items.get(this.mHolder.index);
                this.mThumbnailUrl = post.thumbnailUrl;
            }
            GridExploreFragment.this.setupAnimation(this.mFadeOutAnimation, new AccelerateInterpolator(), 300, true);
            GridExploreFragment.this.setupAnimation(this.mFadeInAnimation, new AccelerateInterpolator(), 300, true);
            this.mFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: co.vine.android.GridExploreFragment.UpdateThumbnailRunnable.1
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    UpdateThumbnailRunnable.this.mFadeOutAnimation.setAnimationListener(null);
                    GridExploreFragment.this.mResourceLoader.setImageWhenLoadedWithAnimation(new ResourceLoader.ImageViewImageSetter(UpdateThumbnailRunnable.this.mImageView), UpdateThumbnailRunnable.this.mThumbnailUrl, false, UpdateThumbnailRunnable.this.mFadeInAnimation, true);
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }
            });
            this.mFadeInAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: co.vine.android.GridExploreFragment.UpdateThumbnailRunnable.2
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    UpdateThumbnailRunnable.this.mFadeInAnimation.setAnimationListener(null);
                    UpdateThumbnailRunnable.this.mHolder.index++;
                    if (UpdateThumbnailRunnable.this.mHolder.index >= UpdateThumbnailRunnable.this.mHolder.items.size()) {
                        UpdateThumbnailRunnable.this.mHolder.index = 0;
                    }
                    if (UpdateThumbnailRunnable.this.mHolder.future != null) {
                        UpdateThumbnailRunnable.this.mHolder.future.cancel(true);
                    }
                    try {
                        UpdateThumbnailRunnable.this.mHolder.future = GridExploreFragment.this.mExecutorService.schedule(GridExploreFragment.this.new ScheduleThumbnailUpdateRunnable(UpdateThumbnailRunnable.this.mHolder), Math.round(1000.0d * ((GridExploreFragment.this.mRandomGenerator.nextFloat() * 6.5d) + 1.5d)), TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        SLog.e("Grid explore ", (Throwable) e);
                    }
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mImageView.startAnimation(this.mFadeOutAnimation);
        }
    }
}
