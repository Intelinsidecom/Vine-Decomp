package co.vine.android;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/* loaded from: classes.dex */
public class SupportListActivity extends FragmentActivity {
    protected ListAdapter mAdapter;
    protected ListView mList;
    private Handler mHandler = new Handler();
    private boolean mFinishedStart = false;
    private Runnable mRequestFocus = new Runnable() { // from class: co.vine.android.SupportListActivity.1
        @Override // java.lang.Runnable
        public void run() {
            SupportListActivity.this.mList.focusableViewAvailable(SupportListActivity.this.mList);
        }
    };
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() { // from class: co.vine.android.SupportListActivity.2
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            SupportListActivity.this.onListItemClick((ListView) parent, v, position, id);
        }
    };

    protected void onListItemClick(ListView l, View v, int position, long id) {
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle state) {
        ensureList();
        super.onRestoreInstanceState(state);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        super.onDestroy();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onContentChanged() {
        super.onContentChanged();
        View emptyView = findViewById(android.R.id.empty);
        this.mList = (ListView) findViewById(android.R.id.list);
        if (this.mList == null) {
            throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
        }
        if (emptyView != null) {
            this.mList.setEmptyView(emptyView);
        }
        this.mList.setOnItemClickListener(this.mOnClickListener);
        if (this.mFinishedStart) {
            setListAdapter(this.mAdapter);
        }
        this.mHandler.post(this.mRequestFocus);
        this.mFinishedStart = true;
    }

    public void setListAdapter(ListAdapter adapter) {
        synchronized (this) {
            ensureList();
            this.mAdapter = adapter;
            this.mList.setAdapter(adapter);
        }
    }

    public ListView getListView() {
        ensureList();
        return this.mList;
    }

    public ListAdapter getListAdapter() {
        return this.mAdapter;
    }

    private void ensureList() {
        if (this.mList == null) {
            setContentView(android.R.layout.list_content);
        }
    }
}
