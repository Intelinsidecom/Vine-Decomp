package co.vine.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedNotifier;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.viewmanager.CardViewManager;
import co.vine.android.feedadapter.viewmanager.WelcomePostCardViewManager;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.nux.NuxResolver;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.UIEventScribeLogger;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.util.LinkBuilderUtil;
import co.vine.android.util.LinkSuppressor;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ExploreTimelineFragment extends BaseTimelineFragment {
    private Activity mActivity;
    protected String mCategory;
    private Uri mData;
    private String mTag;

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        this.mCategory = b.getString("category");
        this.mData = (Uri) b.getParcelable("data");
        this.mTag = b.getString("tag");
        setFlurryEventSource("Explore Timeline: " + this.mCategory);
        setFocused(true);
        Bundle data = new Bundle();
        data.putString("tag_name", this.mTag);
        data.putParcelable("data", this.mData);
        this.mApiUrl = LinkBuilderUtil.buildUrl(getTypeFromCategory(), data);
    }

    @Override // co.vine.android.BaseTimelineFragment, android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mActivity = getActivity();
        this.mListView.setAdapter((ListAdapter) this.mFeedAdapter);
        if ("welcome-feed".equals(this.mCategory)) {
            View footer = LayoutInflater.from(getActivity()).inflate(R.layout.welcome_feed_footer, (ViewGroup) null);
            Button button = (Button) footer.findViewById(R.id.button);
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ExploreTimelineFragment.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        Context appContext = ExploreTimelineFragment.this.getActivity().getApplicationContext();
                        UIEventScribeLogger.onFinishWelcomeFeed(ScribeLoggerSingleton.getInstance(appContext), AppStateProviderSingleton.getInstance(appContext), AppNavigationProviderSingleton.getInstance());
                        NuxResolver.toNuxFromWelcomeFeed(ExploreTimelineFragment.this.mActivity);
                    }
                });
            }
            this.mListView.addFooterView(footer);
        }
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseFragment
    public AppNavigation.Views getAppNavigationView() {
        return "welcome-feed".equals(this.mCategory) ? AppNavigation.Views.WELCOME_FEED : super.getAppNavigationView();
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected LinkSuppressor getLinkSuppressor() {
        return new LinkSuppressor() { // from class: co.vine.android.ExploreTimelineFragment.2
            @Override // co.vine.android.util.LinkSuppressor
            public boolean shouldSuppressTagLink(String tag) {
                return (ExploreTimelineFragment.this.mCategory.equals("tag") || ExploreTimelineFragment.this.mCategory.equals("tag-recent")) && tag.equals(ExploreTimelineFragment.this.mTag);
            }

            @Override // co.vine.android.util.LinkSuppressor
            public boolean shouldSuppressVenueLink(String foursquareVenueId) {
                return ExploreTimelineFragment.this.mCategory.equals("venue") && foursquareVenueId.equals(ExploreTimelineFragment.this.mData.getLastPathSegment());
            }
        };
    }

    @Override // co.vine.android.BaseTimelineFragment
    public ArrayList<CardViewManager> getViewManagers(Activity activity, FeedNotifier feedNotifier, FeedAdapterItems adapterItems, FeedViewHolderCollection viewHolderCollection) {
        return !"welcome-feed".equals(this.mCategory) ? super.getViewManagers(activity, feedNotifier, adapterItems, viewHolderCollection) : getWelcomeViewManagers(activity, adapterItems, viewHolderCollection);
    }

    private ArrayList<CardViewManager> getWelcomeViewManagers(Activity activity, FeedAdapterItems adapterItems, FeedViewHolderCollection viewHolderCollection) {
        ArrayList<CardViewManager> viewManagers = new ArrayList<>();
        WelcomePostCardViewManager.Builder postViewManagerBuilder = new WelcomePostCardViewManager.Builder();
        postViewManagerBuilder.items(adapterItems).viewHolders(viewHolderCollection).listView(this.mListView).context(activity).callback(this.mTimelineClickEventCallback).followActionsLogger(this.mFollowScribeActionsLogger).postPlayedListener(this.mTimelineItemScribeActionsLogger).logger(this.mLogger);
        viewManagers.add(postViewManagerBuilder.build());
        return viewManagers;
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mFeedAdapter.onResume(isFocused());
        startLoadingTimeProfiling();
        if (this.mFeedAdapter.getCount() == 0 && !this.mFetched) {
            fetchInitialRequest(UrlCachePolicy.CACHE_ONLY);
        }
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mFeedAdapter.onPause(isFocused());
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected String fetchPosts(int page, String anchor, String backAnchor, boolean userInitiated, UrlCachePolicy cachePolicy) {
        int type = getTypeFromCategory();
        if (type == -1) {
            return null;
        }
        return this.mAppController.fetchPosts(this.mAppController.getActiveSession(), 20, this.mAppController.getActiveId(), type, page, anchor, backAnchor, userInitiated, this.mTag, null, this.mData, cachePolicy, false);
    }

    private int getTypeFromCategory() {
        if (this.mCategory == null) {
            return -1;
        }
        if ("trending-people".equals(this.mCategory)) {
            return 4;
        }
        if ("popular-now".equals(this.mCategory)) {
            return 5;
        }
        if ("welcome-feed".equals(this.mCategory)) {
            return 30;
        }
        if ("tag".equals(this.mCategory)) {
            if (this.mTag == null) {
                throw new IllegalArgumentException("You have to specify a tag.");
            }
            return 6;
        }
        if ("tag-recent".equals(this.mCategory)) {
            if (this.mTag == null) {
                throw new IllegalArgumentException("You have to specify a tag.");
            }
            return 16;
        }
        if ("timelines".equals(this.mCategory)) {
            return 11;
        }
        if ("post-search-top".equals(this.mCategory)) {
            return 14;
        }
        if ("post-search-recent".equals(this.mCategory)) {
            return 15;
        }
        if ("venue".equals(this.mCategory)) {
            return 13;
        }
        if ("remix-recent".equals(this.mCategory)) {
            return 17;
        }
        if ("remix-top".equals(this.mCategory)) {
            return 18;
        }
        throw new IllegalArgumentException("Unknown category " + this.mCategory);
    }

    public static Bundle prepareArguments(Intent intent, boolean refreshable, String category, String tag, Uri data) {
        Bundle bundle = BaseArrayListFragment.prepareArguments(intent, refreshable);
        bundle.putString("category", category);
        bundle.putString("tag", tag);
        bundle.putParcelable("data", data);
        return bundle;
    }
}
