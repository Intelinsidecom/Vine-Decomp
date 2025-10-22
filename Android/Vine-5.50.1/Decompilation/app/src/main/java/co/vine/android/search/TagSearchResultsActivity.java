package co.vine.android.search;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import co.vine.android.BaseControllerActionBarActivity;
import co.vine.android.PendingRequest;
import co.vine.android.PendingRequestHelper;
import co.vine.android.R;
import co.vine.android.TabbedFeedActivityFactory;
import co.vine.android.client.AppSessionListener;
import co.vine.android.feedadapter.ArrayListScrollListener;
import co.vine.android.model.VineTag;
import co.vine.android.util.Util;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class TagSearchResultsActivity extends BaseControllerActionBarActivity {
    private TagSearchAdapter mAdapter;
    private String mAnchor;
    private boolean mFetched;
    private RefreshableListView mListView;
    private PendingRequestHelper mPendingRequestHelper;
    private String mQueryString;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.tag_search_results, true);
        if (getIntent() != null) {
            this.mQueryString = getIntent().getStringExtra("query");
        }
        setupActionBar((Boolean) true, (Boolean) true, this.mQueryString, (Boolean) true, (Boolean) false);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(getString(R.string.tags));
        }
        this.mAppSessionListener = new SessionListener();
        this.mAdapter = new TagSearchAdapter();
        RefreshableListView listView = (RefreshableListView) findViewById(R.id.list);
        listView.disablePTR(true);
        listView.setDivider(null);
        listView.setAdapter((ListAdapter) this.mAdapter);
        listView.setOnScrollListener(new ArrayListScrollListener() { // from class: co.vine.android.search.TagSearchResultsActivity.1
            @Override // co.vine.android.feedadapter.ArrayListScrollListener
            public void onScrollLastItem(int totalItemCount) {
                super.onScrollLastItem(totalItemCount);
                if (!TextUtils.isEmpty(TagSearchResultsActivity.this.mAnchor) && TagSearchResultsActivity.this.mAdapter.getCount() <= 400) {
                    TagSearchResultsActivity.this.fetchContent(1);
                }
            }
        });
        this.mListView = listView;
        this.mPendingRequestHelper = new PendingRequestHelper() { // from class: co.vine.android.search.TagSearchResultsActivity.2
            @Override // co.vine.android.PendingRequestHelper
            public void showProgress(int progressType) {
                TagSearchResultsActivity.this.mListView.refreshMore(true);
            }

            @Override // co.vine.android.PendingRequestHelper
            public void hideProgress(int progressType) {
                TagSearchResultsActivity.this.mListView.refreshMore(false);
            }
        };
        this.mPendingRequestHelper.onCreate(this.mAppController, savedInstanceState);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.mAdapter.isEmpty() && !this.mFetched) {
            fetchContent(3);
            this.mFetched = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchContent(int fetchType) {
        if (!this.mPendingRequestHelper.hasPendingRequest(fetchType)) {
            String reqId = this.mAppController.searchTags(this.mQueryString, this.mAnchor);
            if (!TextUtils.isEmpty(reqId)) {
                this.mPendingRequestHelper.addRequest(reqId);
                this.mPendingRequestHelper.showProgress(fetchType);
            }
        }
    }

    public static void start(Context contex, String mQueryString) {
        Intent intent = new Intent(contex, (Class<?>) TagSearchResultsActivity.class);
        intent.putExtra("query", mQueryString);
        contex.startActivity(intent);
    }

    private class SessionListener extends AppSessionListener {
        private SessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onTagSearchComplete(String reqId, int statusCode, String reasonPhrase, int nextPage, String anchor, ArrayList<VineTag> tags) {
            if (reqId != null) {
                PendingRequest pendingRequest = TagSearchResultsActivity.this.mPendingRequestHelper.removeRequest(reqId);
                TagSearchResultsActivity.this.mPendingRequestHelper.hideProgress(pendingRequest.fetchType);
            }
            if (statusCode == 200) {
                TagSearchResultsActivity.this.mAnchor = anchor;
                if (tags != null && !tags.isEmpty()) {
                    TagSearchResultsActivity.this.mAdapter.tags.addAll(tags);
                    TagSearchResultsActivity.this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private class TagSearchAdapter extends BaseAdapter {
        public ArrayList<VineTag> tags = new ArrayList<>();

        public TagSearchAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (this.tags != null) {
                return this.tags.size();
            }
            return 0;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) throws Resources.NotFoundException {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = TagSearchResultsActivity.this.getLayoutInflater().inflate(R.layout.tag_row_view, parent, false);
            }
            VineTag tag = this.tags.get(position);
            final String tagName = tag.getTagName();
            ((TextView) view.findViewById(R.id.tag_name)).setText("#" + tagName);
            if (!TextUtils.isEmpty(tagName)) {
                view.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.search.TagSearchResultsActivity.TagSearchAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        TabbedFeedActivityFactory.startTabbedPostsActivity(v.getContext(), tagName);
                    }
                });
            }
            String postCountFormatted = Util.numberFormat(view.getResources(), tag.getPostCount());
            String postCount = view.getResources().getQuantityString(R.plurals.tag_post_count, (int) tag.getPostCount(), postCountFormatted);
            ((TextView) view.findViewById(R.id.post_count)).setText(postCount);
            return view;
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return 0L;
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return null;
        }
    }
}
