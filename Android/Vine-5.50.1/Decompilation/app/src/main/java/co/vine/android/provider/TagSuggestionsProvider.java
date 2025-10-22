package co.vine.android.provider;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import co.vine.android.model.VineTag;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineDatabaseSQL;
import co.vine.android.provider.VineSuggestionsProvider;
import co.vine.android.util.BuildUtil;
import java.util.HashSet;

/* loaded from: classes.dex */
public final class TagSuggestionsProvider implements VineSuggestionsProvider.SuggestionsProvider {
    private static final String AUTHORITY;
    private static final String CONTENT_AUTHORITY;
    private static final boolean LOGGABLE;
    private static final String TAG = TagSuggestionsProvider.class.getSimpleName();
    public static final Uri TAGS_URI;
    private static final UriMatcher sUriMatcher;

    public interface TagsQuery {
        public static final String[] PROJECTION = {"_id", "tag_id", "tag_name"};
    }

    static {
        LOGGABLE = BuildUtil.isLogsOn() || Log.isLoggable(TAG, 3);
        AUTHORITY = BuildUtil.getAuthority(".provider.VineSuggestionsProvider");
        CONTENT_AUTHORITY = "content://" + AUTHORITY + "/";
        sUriMatcher = new UriMatcher(-1);
        TAGS_URI = Uri.parse(CONTENT_AUTHORITY + "tags");
        sUriMatcher.addURI(AUTHORITY, "tags", 1);
    }

    @Override // co.vine.android.provider.VineSuggestionsProvider.SuggestionsProvider
    public boolean doesProvide(Uri uri) {
        return sUriMatcher.match(uri) == 1;
    }

    @Override // co.vine.android.provider.VineSuggestionsProvider.SuggestionsProvider
    public Cursor provideCursor(ContentResolver contentResolver, Uri uri, String selection) {
        if (LOGGABLE) {
            Log.d(TAG, "QUERY: uri " + uri + " -> " + sUriMatcher.match(uri));
        }
        if (doesProvide(uri)) {
            return getComposeTagSuggestions(contentResolver, selection);
        }
        return null;
    }

    private Cursor getComposeTagSuggestions(ContentResolver resolver, String query) {
        String selection;
        String[] selectionArgs;
        Iterable<VineTag> tags;
        MatrixCursor cursor = new MatrixCursor(TagsQuery.PROJECTION);
        int id = 0;
        boolean haveQuery = !TextUtils.isEmpty(query);
        Uri uri = Vine.TagsRecentlyUsed.CONTENT_URI;
        if (haveQuery) {
            selection = "tag_name LIKE ?";
            selectionArgs = new String[]{query + "%"};
        } else {
            selection = null;
            selectionArgs = null;
        }
        Cursor c = resolver.query(uri, VineDatabaseSQL.TagsRecentlyUsedQuery.PROJECTION, selection, selectionArgs, "last_used_timestamp DESC");
        HashSet<Long> prefetchIds = new HashSet<>();
        if (c != null) {
            while (c.moveToNext()) {
                if (!prefetchIds.contains(Long.valueOf(c.getLong(1)))) {
                    if (id > 50) {
                        break;
                    }
                    MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
                    rowBuilder.add(Integer.valueOf(id));
                    rowBuilder.add(Long.valueOf(c.getLong(1)));
                    rowBuilder.add(c.getString(2));
                    prefetchIds.add(Long.valueOf(c.getLong(1)));
                    id++;
                }
            }
            c.close();
        }
        if (haveQuery && id <= 50 && (tags = VineModelFactory.getModelInstance().getTagModel().getTagsForQuery(query)) != null) {
            for (VineTag tag : tags) {
                if (!prefetchIds.contains(Long.valueOf(tag.getTagId()))) {
                    if (id > 50) {
                        break;
                    }
                    MatrixCursor.RowBuilder rowBuilder2 = cursor.newRow();
                    rowBuilder2.add(Integer.valueOf(id));
                    rowBuilder2.add(Long.valueOf(tag.getTagId()));
                    rowBuilder2.add(tag.getTagName());
                    id++;
                }
            }
        }
        return cursor;
    }
}
