package co.vine.android.solicitor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.BaseAdapterFragment;
import co.vine.android.R;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemUtil;
import co.vine.android.api.VinePost;
import co.vine.android.model.ModelEvents;
import co.vine.android.model.TagModel;
import co.vine.android.model.TimelineItemModel;
import co.vine.android.model.TimelineModel;
import co.vine.android.model.impl.Timeline;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.service.components.Components;
import co.vine.android.views.swipeable.SwipeFlingAdapterView;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class SolicitorFragment extends BaseAdapterFragment {
    private View mBackground;
    private VinePost mLastPost;
    private ArrayList<VinePost> mPosts;
    private SolicitorAdapter mStackAdapter;
    private SwipeFlingAdapterView mStackView;
    private Timeline mTimeline;
    private TimelineDetails mTimelineDetails;
    private ModelEvents.ModelListener mTimelineFetchActionsListener;

    public static SolicitorFragment newInstance() {
        SolicitorFragment fragment = new SolicitorFragment();
        return fragment;
    }

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPosts = new ArrayList<>();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.solicitor_stack, (ViewGroup) null);
        this.mStackView = (SwipeFlingAdapterView) view.findViewById(R.id.solicitor_pager);
        this.mBackground = view.findViewById(R.id.background_img);
        return view;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        final FragmentActivity activity = getActivity();
        this.mStackAdapter = new SolicitorAdapter(activity, this.mAppController, this.mPosts, this.mStackView, this.mBackground);
        this.mStackAdapter.setNotifyOnChange(true);
        this.mStackView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() { // from class: co.vine.android.solicitor.SolicitorFragment.1
            @Override // co.vine.android.views.swipeable.SwipeFlingAdapterView.onFlingListener
            public void removeFirstObjectInAdapter() {
                if (SolicitorFragment.this.mPosts.isEmpty()) {
                    activity.finish();
                }
                SolicitorFragment.this.mLastPost = (VinePost) SolicitorFragment.this.mPosts.get(0);
                SolicitorFragment.this.mPosts.remove(0);
                SolicitorFragment.this.mStackAdapter.notifyDataSetChanged();
            }

            @Override // co.vine.android.views.swipeable.SwipeFlingAdapterView.onFlingListener
            public void onLeftCardExit(Object dataObject) {
                SolicitorFragment.this.mStackAdapter.incrementDismissCount();
            }

            @Override // co.vine.android.views.swipeable.SwipeFlingAdapterView.onFlingListener
            public void onRightCardExit(Object dataObject) {
                SolicitorFragment.this.mStackAdapter.incrementDismissCount();
                SolicitorFragment.this.mStackAdapter.addLike(SolicitorFragment.this.mLastPost);
            }

            @Override // co.vine.android.views.swipeable.SwipeFlingAdapterView.onFlingListener
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                SolicitorFragment.this.addRequest(Components.timelineFetchComponent().fetchPosts(SolicitorFragment.this.mAppController, SolicitorFragment.this.mAppController.getActiveSession(), 20, -1L, 31, true, String.valueOf(SolicitorFragment.this.mAppController.getActiveId()), "recent", null, UrlCachePolicy.CACHE_THEN_NETWORK, false, -1L, 1), 1);
            }

            @Override // co.vine.android.views.swipeable.SwipeFlingAdapterView.onFlingListener
            public void onScroll(float scrollProgressPercent) {
                if (scrollProgressPercent > 0.0f) {
                    if (Math.abs(scrollProgressPercent) < 0.1f) {
                        SolicitorFragment.this.mStackAdapter.deanimateLike(scrollProgressPercent);
                        return;
                    } else {
                        SolicitorFragment.this.mStackAdapter.animateLike(scrollProgressPercent);
                        return;
                    }
                }
                if (Math.abs(scrollProgressPercent) < 0.1f) {
                    SolicitorFragment.this.mStackAdapter.deanimateDislike(scrollProgressPercent);
                } else {
                    SolicitorFragment.this.mStackAdapter.animateDislike(scrollProgressPercent);
                }
            }
        });
        this.mStackView.setAdapter(this.mStackAdapter);
        this.mTimelineFetchActionsListener = new ModelEvents.ModelListener() { // from class: co.vine.android.solicitor.SolicitorFragment.2
            @Override // co.vine.android.model.ModelEvents.ModelListener
            public void onTagsAdded(TagModel tagModel, String query) {
            }

            @Override // co.vine.android.model.ModelEvents.ModelListener
            public void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails) {
                if (timelineDetails.equals(SolicitorFragment.this.mTimelineDetails)) {
                    SolicitorFragment.this.updateFeedAdapter();
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFeedAdapter() {
        Timeline timeline = VineModelFactory.getModelInstance().getTimelineModel().getUserTimeline(this.mTimelineDetails);
        if (timeline != null && timeline.itemIds != null) {
            ArrayList<TimelineItem> timelineItems = new ArrayList<>();
            TimelineItemModel timelineItemModel = VineModelFactory.getModelInstance().getTimelineItemModel();
            for (int i = this.mStackAdapter.getDismissCount(); i < timeline.itemIds.size(); i++) {
                Long id = timeline.itemIds.get(i);
                timelineItems.add(timelineItemModel.getTimelineItem(id.longValue()));
            }
            ArrayList<VinePost> posts = TimelineItemUtil.getVinePostsFromItems(timelineItems);
            this.mPosts.addAll(posts);
            this.mStackAdapter.notifyDataSetChanged();
        }
    }

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mTimelineFetchActionsListener != null) {
            VineModelFactory.getModelInstance().getModelEvents().addListener(this.mTimelineFetchActionsListener);
        }
        if (this.mTimelineDetails == null) {
            this.mTimelineDetails = new TimelineDetails(31, -1L, "recent");
        }
        if (this.mPosts == null) {
            this.mPosts = new ArrayList<>();
        }
        TimelineModel timelineModel = VineModelFactory.getModelInstance().getTimelineModel();
        this.mTimeline = timelineModel.getUserTimeline(this.mTimelineDetails);
        if (this.mTimeline != null && this.mPosts != null) {
            ArrayList<Long> itemIds = this.mTimeline.itemIds;
            ArrayList<TimelineItem> items = new ArrayList<>();
            TimelineItemModel timelineItemModel = VineModelFactory.getModelInstance().getTimelineItemModel();
            Iterator<Long> it = itemIds.iterator();
            while (it.hasNext()) {
                Long itemId = it.next();
                items.add(timelineItemModel.getTimelineItem(itemId.longValue()));
            }
            ArrayList<VinePost> posts = TimelineItemUtil.getVinePostsFromItems(items);
            this.mPosts.addAll(posts);
        }
        if (this.mPosts.isEmpty()) {
            addRequest(Components.timelineFetchComponent().fetchPosts(this.mAppController, this.mAppController.getActiveSession(), 20, -1L, 31, true, String.valueOf(this.mAppController.getActiveId()), "recent", null, UrlCachePolicy.CACHE_THEN_NETWORK, false, -1L, 2), 2);
        }
        if (this.mStackAdapter != null) {
            this.mStackAdapter.playCurrentVideo();
        }
    }

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.mTimelineFetchActionsListener != null) {
            VineModelFactory.getModelInstance().getModelEvents().addListener(this.mTimelineFetchActionsListener);
        }
        if (this.mStackAdapter != null) {
            this.mStackAdapter.pauseCurrentVideo();
        }
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected void hideProgress(int fetchType) {
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected void showProgress(int fetchType) {
    }
}
