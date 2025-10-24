package co.vine.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.vine.android.PostOptionsDialogActivity;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.OnTabChangedListener;
import co.vine.android.widget.PinnedHeaderListView;
import co.vine.android.widget.tabs.TabScrollListener;
import com.twitter.android.widget.ItemPosition;
import com.twitter.android.widget.RefreshListener;
import com.twitter.android.widget.RefreshableListView;

/* loaded from: classes.dex */
public class BaseArrayListFragment extends BaseAdapterFragment implements OnTabChangedListener, PinnedHeaderListView.ScrollDeltaListener, RefreshListener {
    protected BaseAdapter mAdapter;
    protected String mAnchor;
    protected String mBackAnchor;
    private int mBgColor;
    private int mChoiceMode;
    protected View mEmptyProgress;
    private TextView mEmptyText;
    private int mEmptyTextStringRes;
    protected boolean mFocused;
    protected ListView mListView;
    protected boolean mRefreshable;
    private View mSadface;
    private TabScrollListener mScrollListener;
    private int mScrollOffset;
    private int mScrollPos;
    protected int mNextPage = 1;
    private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: co.vine.android.BaseArrayListFragment.1
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
            BaseArrayListFragment.this.onListItemClick((ListView) parent, view, i, l);
        }
    };
    private final AdapterView.OnItemLongClickListener mOnItemLongClickListenerClickListener = new AdapterView.OnItemLongClickListener() { // from class: co.vine.android.BaseArrayListFragment.2
        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return BaseArrayListFragment.this.onListItemLongClick((ListView) parent, view, position, id);
        }
    };
    private final Runnable mRequestFocus = new Runnable() { // from class: co.vine.android.BaseArrayListFragment.3
        @Override // java.lang.Runnable
        public void run() {
            BaseArrayListFragment.this.mListView.focusableViewAvailable(BaseArrayListFragment.this.mListView);
        }
    };

    public void onMoveAway(int newPosition) {
        this.mFocused = false;
    }

    public void onMoveTo(int oldPosition) {
        this.mFocused = true;
    }

    protected boolean isFocused() {
        return this.mFocused;
    }

    public void setFocused(boolean focused) {
        this.mFocused = focused;
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
    }

    protected boolean onListItemLongClick(ListView parent, View view, int position, long id) {
        return false;
    }

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mScrollPos = savedInstanceState.getInt("scroll_pos");
            this.mScrollOffset = savedInstanceState.getInt("scroll_off");
            this.mPendingRequestHelper.restorePendingCaptchaRequest(savedInstanceState);
        } else {
            this.mScrollPos = 0;
            this.mScrollOffset = 0;
        }
        Bundle args = getArguments();
        if (args != null) {
            this.mRefreshable = args.getBoolean("refresh", true);
            if (args.containsKey("chmode")) {
                this.mChoiceMode = args.getInt("chmode", 0);
            }
            this.mBgColor = args.getInt("bg_color", 0);
            this.mEmptyTextStringRes = args.getInt("empty_desc", 0);
            return;
        }
        this.mRefreshable = true;
        this.mChoiceMode = 0;
        this.mBgColor = 0;
    }

    public View createView(LayoutInflater inflater, int layout, ViewGroup container) {
        View v = inflater.inflate(layout, container, false);
        ListView listView = (ListView) v.findViewById(android.R.id.list);
        listView.setOnItemClickListener(this.mOnItemClickListener);
        listView.setOnItemLongClickListener(this.mOnItemLongClickListenerClickListener);
        listView.setScrollbarFadingEnabled(true);
        listView.setChoiceMode(this.mChoiceMode);
        int bgColor = this.mBgColor;
        if (bgColor != 0) {
            listView.setBackgroundColor(bgColor);
            listView.setCacheColorHint(bgColor);
        }
        View emptyView = v.findViewById(android.R.id.empty);
        if (emptyView != null) {
            listView.setEmptyView(emptyView);
            this.mEmptyProgress = emptyView.findViewById(R.id.list_empty_progress);
            if (getActivity() instanceof HomeTabActivity) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) this.mEmptyProgress.getLayoutParams();
                params.topMargin = getActivity().getResources().getDimensionPixelSize(R.dimen.tabbar_height);
            }
        }
        this.mEmptyText = (TextView) v.findViewById(R.id.empty_text);
        if (this.mEmptyTextStringRes > 0 && this.mEmptyText != null) {
            this.mEmptyText.setText(this.mEmptyTextStringRes);
        }
        this.mSadface = v.findViewById(R.id.real_sadface);
        if (this.mRefreshable) {
            ((RefreshableListView) listView).setRefreshListener(this);
        }
        this.mListView = listView;
        if (this.mListView instanceof PinnedHeaderListView) {
            ((PinnedHeaderListView) this.mListView).setScrollDeltaListener(this);
        }
        return v;
    }

    protected View createDefaultView(LayoutInflater inflater, ViewGroup container) {
        return createView(inflater, R.layout.msg_list_fragment, container);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createDefaultView(inflater, container);
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mHandler.post(this.mRequestFocus);
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        super.onDestroyView();
    }

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        restorePosition();
        this.mPendingRequestHelper.handlePendingCaptchaRequest();
    }

    @Override // co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.mListView.getCount() > 0) {
            savePosition();
        }
    }

    @Override // co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savePosition();
        outState.putInt("scroll_pos", this.mScrollPos);
        outState.putInt("scroll_off", this.mScrollOffset);
    }

    public void setRefreshableHeaderOffset(int offset) {
        if (this.mRefreshable) {
            ((RefreshableListView) this.mListView).setViewYOffset(offset);
        }
    }

    protected void refresh() {
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected void showProgress(int progressType) {
        switch (progressType) {
            case 1:
                if (this.mRefreshable) {
                    ((RefreshableListView) this.mListView).refreshMore(true);
                    break;
                }
                break;
            case 2:
            case 4:
                if (this.mRefreshable) {
                    ((RefreshableListView) this.mListView).startRefresh();
                    this.mRefreshing = true;
                    break;
                }
                break;
            case 3:
                showLoading(true);
                break;
        }
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected void hideProgress(int progressType) {
        switch (progressType) {
            case 0:
                showLoading(false);
                if (this.mRefreshable) {
                    ((RefreshableListView) this.mListView).stopRefresh();
                    break;
                }
                break;
            case 1:
                if (this.mRefreshable) {
                    ((RefreshableListView) this.mListView).refreshMore(false);
                    break;
                }
                break;
            case 2:
            case 4:
                showLoading(false);
                if (this.mRefreshable) {
                    ((RefreshableListView) this.mListView).stopRefresh();
                    this.mRefreshing = false;
                    break;
                }
                break;
            case 3:
                showLoading(false);
                break;
        }
    }

    protected void processPostOptionsResult(PostOptionsDialogActivity.Result result) {
        if (result.request != null) {
            addRequest(result.request);
        }
        if (result.intent != null) {
            startActivity(result.intent);
        }
    }

    protected boolean isEmpty() {
        ListView l = this.mListView;
        return (l.getCount() - l.getHeaderViewsCount()) - l.getFooterViewsCount() == 0;
    }

    private void showLoading(boolean show) {
        if (show) {
            ListView l = this.mListView;
            l.setVisibility(8);
            if (this.mSadface != null) {
                this.mSadface.setVisibility(8);
            }
            if (this.mEmptyProgress != null) {
                this.mEmptyProgress.setVisibility(0);
                return;
            }
            return;
        }
        ListView l2 = this.mListView;
        if (!isEmpty()) {
            l2.setVisibility(0);
        } else if (this.mEmptyProgress != null) {
            this.mEmptyProgress.setVisibility(8);
        }
    }

    protected void hideSadface() {
        if (this.mListView != null && this.mSadface != null) {
            this.mListView.setVisibility(0);
            this.mSadface.setVisibility(8);
        }
    }

    protected void showSadface(boolean showFrown) {
        if (this.mListView != null && this.mSadface != null) {
            this.mEmptyProgress.setVisibility(8);
            this.mListView.setVisibility(8);
            this.mSadface.setVisibility(0);
            View sadFace = this.mSadface.findViewById(R.id.sadface);
            if (sadFace != null) {
                sadFace.setVisibility(showFrown ? 0 : 8);
            }
        }
    }

    protected void setEmptyStringMessage(int res) {
        this.mEmptyText.setText(res);
    }

    @Override // com.twitter.android.widget.RefreshListener
    public void onRefreshPulled() {
    }

    @Override // com.twitter.android.widget.RefreshListener
    public void onRefreshCancelled() {
    }

    @Override // com.twitter.android.widget.RefreshListener
    public void onRefreshReleased(boolean shouldStartRefresh) {
        if (shouldStartRefresh) {
            FlurryUtils.trackValidPullToRefreshRelease(getClass().getName());
            refresh();
        }
    }

    @Override // com.twitter.android.widget.RefreshListener
    public void onRefreshFinishedNewData() {
    }

    @Override // com.twitter.android.widget.RefreshListener
    public void onRefreshFinishedNoChange() {
    }

    @Override // com.twitter.android.widget.RefreshListener
    public void onRefreshFinished() {
    }

    @Override // com.twitter.android.widget.RefreshListener
    public int getPositionForItemId(long id) {
        if (this.mAdapter != null) {
            for (int i = 0; i < this.mAdapter.getCount(); i++) {
                if (this.mAdapter.getItemId(i) == id) {
                    return i;
                }
            }
        }
        return 0;
    }

    protected void restorePosition() {
        this.mListView.setSelectionFromTop(this.mScrollPos, this.mScrollOffset);
    }

    protected void savePosition() {
        int offset;
        ListView l = this.mListView;
        if (l != null) {
            View v = l.getChildAt(0);
            if (v != null) {
                offset = v.getTop();
            } else {
                offset = 0;
            }
            this.mScrollPos = l.getFirstVisiblePosition();
            this.mScrollOffset = offset;
        }
    }

    @Override // com.twitter.android.widget.RefreshListener
    public ItemPosition getFirstItemPosition() {
        int position;
        View child;
        ListView l = this.mListView;
        if (l.getCount() < 1) {
            return null;
        }
        int firstPos = l.getFirstVisiblePosition();
        int headerCount = l.getHeaderViewsCount();
        if (this.mRefreshing) {
            headerCount++;
        }
        if (firstPos < headerCount) {
            position = headerCount;
            child = l.getChildAt(position - firstPos);
        } else {
            position = firstPos;
            child = l.getChildAt(0);
        }
        return new ItemPosition(position, l.getItemIdAtPosition(position), child != null ? child.getTop() : 0);
    }

    void invokeScrollFirstItem() {
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollFirstItem();
        }
    }

    public static Bundle prepareArguments(Intent intent, boolean refreshable) {
        Bundle args = intent.getExtras();
        if (args == null) {
            args = new Bundle();
        }
        args.putParcelable("data", intent.getData());
        args.putBoolean("refresh", refreshable);
        return args;
    }

    public void setScrollListener(TabScrollListener listener) {
        this.mScrollListener = listener;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mPendingRequestHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override // co.vine.android.widget.PinnedHeaderListView.ScrollDeltaListener
    public void onScroll(int delta) {
        KeyEvent.Callback activity = getActivity();
        if (activity instanceof ScrollListener) {
            ((ScrollListener) activity).onScroll(delta);
        }
    }

    public void setNavBottom(int offset) {
        if (this.mListView instanceof PinnedHeaderListView) {
            ((PinnedHeaderListView) this.mListView).setNavBottom(offset);
        }
    }
}
