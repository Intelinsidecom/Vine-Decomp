package co.vine.android.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import co.vine.android.api.VineUser;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineSuggestionsProvider;
import co.vine.android.util.BuildUtil;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public final class UserSuggestionsProvider implements VineSuggestionsProvider.SuggestionsProvider {
    private static final String AUTHORITY;
    private static final String CONTENT_AUTHORITY;
    private static final boolean LOGGABLE;
    private static final String TAG = UserSuggestionsProvider.class.getSimpleName();
    public static final Uri USERS_URI;
    private static final UriMatcher sUriMatcher;
    private static final ConcurrentHashMap<String, Iterable<VineUser>> sUsersTypeAhead;

    public interface UsersQuery {
        public static final String[] PROJECTION = {"_id", "user_id", "username", "avatar_url"};
    }

    static {
        LOGGABLE = BuildUtil.isLogsOn() || Log.isLoggable(TAG, 3);
        AUTHORITY = BuildUtil.getAuthority(".provider.VineSuggestionsProvider");
        CONTENT_AUTHORITY = "content://" + AUTHORITY + "/";
        sUriMatcher = new UriMatcher(-1);
        USERS_URI = Uri.parse(CONTENT_AUTHORITY + "users");
        sUsersTypeAhead = new ConcurrentHashMap<>();
        sUriMatcher.addURI(AUTHORITY, "users/#", 1);
    }

    @Override // co.vine.android.provider.VineSuggestionsProvider.SuggestionsProvider
    public boolean doesProvide(Uri uri) {
        return sUriMatcher.match(uri) == 1;
    }

    @Override // co.vine.android.provider.VineSuggestionsProvider.SuggestionsProvider
    public Cursor provideCursor(ContentResolver contentResolver, Uri uri, String selection) throws NumberFormatException {
        if (LOGGABLE) {
            Log.d(TAG, "QUERY: uri " + uri + " -> " + sUriMatcher.match(uri));
        }
        if (!doesProvide(uri)) {
            return null;
        }
        long userId = Long.parseLong(uri.getLastPathSegment());
        return getComposeUserSuggestions(contentResolver, selection, userId);
    }

    private Cursor getComposeUserSuggestions(ContentResolver resolver, String query, long userId) {
        String selection;
        String[] selectionArgs;
        Iterable<VineUser> users;
        MatrixCursor cursor = new MatrixCursor(UsersQuery.PROJECTION);
        int id = 0;
        boolean haveQuery = !TextUtils.isEmpty(query);
        Uri uri = ContentUris.withAppendedId(Vine.UserGroupsView.CONTENT_URI_ALL_FOLLOW, userId);
        if (haveQuery) {
            selection = "username LIKE ?";
            selectionArgs = new String[]{query + "%"};
        } else {
            selection = null;
            selectionArgs = null;
        }
        Cursor c = resolver.query(uri, UsersQuery.PROJECTION, selection, selectionArgs, "order_id DESC");
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
                    rowBuilder.add(c.getString(3));
                    prefetchIds.add(Long.valueOf(c.getLong(1)));
                    id++;
                }
            }
            c.close();
        }
        if (haveQuery && id <= 50 && (users = sUsersTypeAhead.get(query)) != null) {
            for (VineUser user : users) {
                if (!prefetchIds.contains(Long.valueOf(user.userId))) {
                    if (id > 50) {
                        break;
                    }
                    MatrixCursor.RowBuilder rowBuilder2 = cursor.newRow();
                    rowBuilder2.add(Integer.valueOf(id));
                    rowBuilder2.add(Long.valueOf(user.userId));
                    rowBuilder2.add(user.username);
                    rowBuilder2.add(user.avatarUrl);
                    id++;
                }
            }
        }
        return cursor;
    }

    public static void addRealtimeUserSuggestions(String query, Iterable<VineUser> users) {
        sUsersTypeAhead.put(query, users);
    }
}
