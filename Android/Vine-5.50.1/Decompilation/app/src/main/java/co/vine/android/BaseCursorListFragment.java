package co.vine.android;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.vine.android.PostOptionsDialogActivity;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.views.ListState;
import co.vine.android.widget.OnTabChangedListener;
import co.vine.android.widget.tabs.TabScrollListener;
import com.twitter.android.widget.ItemPosition;
import com.twitter.android.widget.RefreshListener;
import com.twitter.android.widget.RefreshableListView;

/* loaded from: classes.dex */
public class BaseCursorListFragment extends BaseCursorAdapterFragment implements AbsListView.OnScrollListener, OnTabChangedListener, RefreshListener {
    protected int mBgColor;
    protected int mChoiceMode;
    private ProgressBar mEmptyProgress;
    protected TextView mEmptyText;
    protected int mEmptyTextStringRes;
    private View mEmptyView;
    protected boolean mFocused;
    private boolean mIsSadFaceHeaderAdded;
    private boolean mIsSadFacePartOfHeader;
    protected ListState mListState;
    protected ListView mListView;
    protected long mOwnerId;
    protected boolean mRefreshable;
    private View mRootView;
    protected View mSadface;
    private TabScrollListener mScrollListener;
    private int mScrollOffset;
    private int mScrollPos;
    private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: co.vine.android.BaseCursorListFragment.1
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
            BaseCursorListFragment.this.onListItemClick((ListView) parent, view, i, l);
        }
    };
    private final AdapterView.OnItemLongClickListener mOnItemLongClickListenerClickListener = new AdapterView.OnItemLongClickListener() { // from class: co.vine.android.BaseCursorListFragment.2
        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return BaseCursorListFragment.this.onListItemLongClick((ListView) parent, view, position, id);
        }
    };
    private final Runnable mRequestFocus = new Runnable() { // from class: co.vine.android.BaseCursorListFragment.3
        @Override // java.lang.Runnable
        public void run() {
            BaseCursorListFragment.this.mListView.focusableViewAvailable(BaseCursorListFragment.this.mListView);
        }
    };

    @Override // co.vine.android.widget.OnTabChangedListener
    public void onMoveAway(int newPosition) {
        this.mFocused = false;
    }

    @Override // co.vine.android.widget.OnTabChangedListener
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
        this.mListState = new ListState();
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
            this.mOwnerId = args.getLong("owner_id", this.mAppController.getActiveId());
            if (args.containsKey("chmode")) {
                this.mChoiceMode = args.getInt("chmode", 0);
            }
            this.mBgColor = args.getInt("bg_color", 0);
            this.mEmptyTextStringRes = args.getInt("empty_desc", 0);
            return;
        }
        this.mRefreshable = true;
        this.mOwnerId = this.mAppController.getActiveId();
        this.mChoiceMode = 0;
        this.mBgColor = 0;
    }

    public View createView(LayoutInflater inflater, int layout, ViewGroup container) {
        View v = inflater.inflate(layout, container, false);
        this.mRootView = v;
        ListView listView = (ListView) v.findViewById(android.R.id.list);
        listView.setOnItemClickListener(this.mOnItemClickListener);
        listView.setOnItemLongClickListener(this.mOnItemLongClickListenerClickListener);
        listView.setScrollbarFadingEnabled(true);
        listView.setOnScrollListener(this);
        listView.setChoiceMode(this.mChoiceMode);
        int bgColor = this.mBgColor;
        if (bgColor != 0) {
            listView.setBackgroundColor(bgColor);
            listView.setCacheColorHint(bgColor);
        }
        View emptyView = v.findViewById(android.R.id.empty);
        if (emptyView != null) {
            listView.setEmptyView(emptyView);
            this.mEmptyProgress = (ProgressBar) emptyView.findViewById(R.id.list_empty_progress);
        }
        this.mEmptyView = emptyView;
        this.mEmptyText = (TextView) v.findViewById(R.id.empty_text);
        if (this.mEmptyTextStringRes > 0 && this.mEmptyText != null) {
            this.mEmptyText.setText(this.mEmptyTextStringRes);
        }
        this.mSadface = v.findViewById(R.id.real_sadface);
        if (this.mRefreshable) {
            ((RefreshableListView) listView).setRefreshListener(this);
        }
        this.mListView = listView;
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
        savePosition();
    }

    @Override // co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savePosition();
        outState.putInt("scroll_pos", this.mScrollPos);
        outState.putInt("scroll_off", this.mScrollOffset);
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Cursor cursor;
        this.mListState.firstVisibleItem = firstVisibleItem;
        this.mListState.visibleItemCount = visibleItemCount;
        this.mListState.totalItemCount = totalItemCount;
        if (visibleItemCount != 0) {
            if (firstVisibleItem == 0 && this.mScrollListener != null) {
                this.mScrollListener.onScrollFirstItem();
            }
            if (firstVisibleItem > 0 && this.mCursorAdapter != null) {
                int position = firstVisibleItem + visibleItemCount;
                if (position >= totalItemCount - 1 && this.mCursorAdapter.getCount() > 0 && (cursor = this.mCursorAdapter.getCursor()) != null && cursor.moveToLast() && this.mListState.hasNewScrollState) {
                    this.mListState.hasNewScrollState = false;
                    onScrollLastItem(cursor);
                }
            }
        }
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.mListState.hasNewScrollState = true;
        this.mListState.scrollState = scrollState;
        if (scrollState == 0) {
            onScroll(view, this.mListState.firstVisibleItem, this.mListState.visibleItemCount, this.mListState.totalItemCount);
        }
    }

    protected void refresh() {
    }

    public synchronized void makeSadFaceHeaderView() {
        if (!this.mIsSadFacePartOfHeader) {
            this.mIsSadFacePartOfHeader = true;
            this.mIsSadFaceHeaderAdded = true;
            if (this.mSadface != null) {
                boolean alreadyAttached = false;
                ViewParent parent = this.mSadface.getParent();
                if (parent != null) {
                    if (parent instanceof ViewGroup) {
                        if (parent != this.mListView) {
                            ((ViewGroup) parent).removeView(this.mSadface);
                        } else {
                            alreadyAttached = true;
                        }
                    } else {
                        throw new IllegalStateException("The sad face does not belong to a valid parent: " + parent);
                    }
                }
                if (!alreadyAttached) {
                    ViewGroup.LayoutParams params = this.mSadface.getLayoutParams();
                    if (params != null) {
                        this.mSadface.setLayoutParams(new AbsListView.LayoutParams(params.width, params.height));
                    }
                    this.mListView.addHeaderView(this.mSadface, null, false);
                }
            }
        }
    }

    protected void onScrollLastItem(Cursor cursor) {
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected void showProgress(int progressType) {
        switch (progressType) {
            case 1:
            case 7:
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
            case 5:
                showLoading(true);
                break;
        }
    }

    @Override // co.vine.android.BaseAdapterFragment
    protected void hideProgress(int progressType) {
        switch (progressType) {
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
            case 5:
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

    protected void showSadface(boolean show) {
        showSadface(show, show);
    }

    protected void showSadface(boolean show, boolean showFrown) {
        if (show) {
            if (this.mListView != null && this.mSadface != null) {
                if (!this.mIsSadFacePartOfHeader) {
                    this.mListView.setVisibility(8);
                } else {
                    this.mListView.setVisibility(0);
                    if (!this.mIsSadFaceHeaderAdded) {
                        this.mListView.addHeaderView(this.mSadface);
                        this.mIsSadFaceHeaderAdded = true;
                    }
                }
                this.mSadface.setVisibility(0);
                View sadFace = this.mSadface.findViewById(R.id.sadface);
                if (sadFace != null) {
                    sadFace.setVisibility(showFrown ? 0 : 8);
                    return;
                }
                return;
            }
            return;
        }
        if (this.mListView != null && this.mSadface != null) {
            this.mListView.setVisibility(0);
            if (this.mIsSadFacePartOfHeader) {
                if (this.mIsSadFaceHeaderAdded) {
                    this.mListView.removeHeaderView(this.mSadface);
                    this.mSadface.setVisibility(8);
                    this.mIsSadFacePartOfHeader = false;
                    return;
                }
                return;
            }
            this.mSadface.setVisibility(8);
        }
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
        CursorAdapter adapter = this.mCursorAdapter;
        if (adapter != null) {
            Cursor cursor = adapter.getCursor();
            if (cursor != null) {
            }
        }
    }

    @Override // co.vine.android.BaseCursorAdapterFragment
    protected void handleContentChanged() {
        onContentChanged();
    }

    protected void onContentChanged() {
        onRefreshFinished();
    }

    @Override // com.twitter.android.widget.RefreshListener
    public int getPositionForItemId(long id) {
        Cursor cursor;
        int idColumnIndex = getIdColumnIndex();
        if (idColumnIndex >= 0 && (cursor = this.mCursorAdapter.getCursor()) != null && cursor.moveToFirst()) {
            while (cursor.getLong(idColumnIndex) != id) {
                if (!cursor.moveToNext()) {
                }
            }
            int pos = cursor.getPosition() + this.mListView.getHeaderViewsCount();
            if (this.mRefreshing) {
                return pos + 1;
            }
            return pos;
        }
        return 0;
    }

    protected int getIdColumnIndex() {
        return -1;
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

    public static Bundle prepareArguments(Intent intent, boolean refreshable) {
        Bundle args = intent.getExtras();
        if (args == null) {
            args = new Bundle();
        }
        args.putParcelable("data", intent.getData());
        args.putBoolean("refresh", refreshable);
        return args;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mPendingRequestHelper.onActivityResult(requestCode, resultCode, data);
    }
}
