package co.vine.android;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import co.vine.android.util.CrashUtil;

/* loaded from: classes.dex */
public abstract class BaseCursorAdapterFragment extends BaseAdapterFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    protected ChangeObserver mChangeObserver;
    protected CursorAdapter mCursorAdapter;
    private int mLoaderId = 0;
    protected boolean mRefreshing;

    protected abstract void handleContentChanged();

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        Cursor cursor;
        if (this.mCursorAdapter != null && (cursor = this.mCursorAdapter.swapCursor(null)) != null && this.mChangeObserver != null) {
            try {
                cursor.unregisterContentObserver(this.mChangeObserver);
            } catch (IllegalStateException e) {
                CrashUtil.logException(e, "This is ok, because this cursor probably didn't need a change observer.", new Object[0]);
            }
        }
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ChangeObserver extends ContentObserver {
        public ChangeObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            if (!BaseCursorAdapterFragment.this.mRefreshing) {
                BaseCursorAdapterFragment.this.handleContentChanged();
            }
        }
    }

    protected void initLoader() {
        initLoader(0);
    }

    protected final void initLoader(int loaderId) {
        if (this.mChangeObserver == null) {
            this.mChangeObserver = new ChangeObserver(this.mHandler);
            this.mLoaderId = loaderId;
        }
        getLoaderManager().initLoader(loaderId, null, this);
    }

    protected boolean restartLoader() {
        return restartLoader(this.mLoaderId);
    }

    protected boolean restartLoader(int loaderId) {
        if (this.mChangeObserver == null) {
            return false;
        }
        getLoaderManager().restartLoader(loaderId, null, this);
        return true;
    }

    @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Cursor oldCursor = this.mCursorAdapter.getCursor();
        if (oldCursor != null) {
            try {
                oldCursor.unregisterContentObserver(this.mChangeObserver);
            } catch (IllegalStateException e) {
                CrashUtil.logException(e);
            }
        }
        if (cursor != null) {
            cursor.registerContentObserver(this.mChangeObserver);
        }
        this.mCursorAdapter.swapCursor(cursor);
    }

    @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoaderReset(Loader<Cursor> loader) {
        if (this.mCursorAdapter != null) {
            Cursor cursor = this.mCursorAdapter.getCursor();
            if (cursor != null) {
                cursor.unregisterContentObserver(this.mChangeObserver);
            }
            this.mCursorAdapter.swapCursor(null);
        }
    }
}
