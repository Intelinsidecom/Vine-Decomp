package co.vine.android;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.widget.Filter;
import co.vine.android.provider.TagSuggestionsProvider;
import co.vine.android.provider.UserSuggestionsProvider;

/* loaded from: classes.dex */
public final class ComposeFilter extends Filter {
    private final Activity mActivity;
    private final CursorAdapterProvider mCallback;
    private final long mUserId;

    public interface CursorAdapterProvider {
        CursorAdapter provideAdapter();
    }

    public ComposeFilter(Activity activity, CursorAdapterProvider adapterProvider, long userId) {
        this.mActivity = activity;
        this.mCallback = adapterProvider;
        this.mUserId = userId;
    }

    @Override // android.widget.Filter
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        String selection;
        Uri uri;
        Cursor cursor;
        Filter.FilterResults filterResults = new Filter.FilterResults();
        if (!TextUtils.isEmpty(constraint)) {
            CharSequence plainConstraint = constraint.subSequence(1, constraint.length());
            if (plainConstraint.length() > 0) {
                selection = plainConstraint.toString();
            } else {
                selection = null;
            }
            if (constraint.toString().startsWith("@")) {
                uri = ContentUris.withAppendedId(UserSuggestionsProvider.USERS_URI, this.mUserId);
            } else if (constraint.toString().startsWith("#")) {
                uri = TagSuggestionsProvider.TAGS_URI;
            } else {
                uri = null;
            }
            if (uri != null && (cursor = this.mActivity.getContentResolver().query(uri, null, selection, null, null)) != null) {
                filterResults.count = cursor.getCount();
                filterResults.values = cursor;
            }
        }
        return filterResults;
    }

    @Override // android.widget.Filter
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
        CursorAdapter cursorAdapter = this.mCallback.provideAdapter();
        Cursor prevCursor = cursorAdapter.getCursor();
        if (prevCursor != null) {
            this.mActivity.stopManagingCursor(prevCursor);
        }
        Cursor cursor = (Cursor) results.values;
        if (cursor != null) {
            this.mActivity.startManagingCursor(cursor);
        }
        cursorAdapter.changeCursor(cursor);
    }
}
