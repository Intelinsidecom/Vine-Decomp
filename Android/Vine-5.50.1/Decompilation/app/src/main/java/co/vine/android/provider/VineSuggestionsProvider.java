package co.vine.android.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public final class VineSuggestionsProvider extends ContentProvider {
    private static final boolean LOGGABLE;
    private static final SuggestionsProvider[] suggestionsProviders;

    interface SuggestionsProvider {
        boolean doesProvide(Uri uri);

        Cursor provideCursor(ContentResolver contentResolver, Uri uri, String str);
    }

    static {
        LOGGABLE = BuildUtil.isLogsOn() || Log.isLoggable("VineSuggestProvider", 3);
        suggestionsProviders = new SuggestionsProvider[]{new TagSuggestionsProvider(), new UserSuggestionsProvider()};
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selArgs, String sortOrder) {
        SQLiteDatabase db;
        for (SuggestionsProvider provider : suggestionsProviders) {
            if (provider.doesProvide(uri)) {
                return provider.provideCursor(getContext().getContentResolver(), uri, selection);
            }
        }
        if (LOGGABLE) {
            Log.d("VineSuggestProvider", "QUERY: uri " + uri + " did not match any providers.");
        }
        try {
            db = VineDatabaseHelper.getDatabaseHelper(getContext()).getReadableDatabase();
        } catch (SQLiteException e) {
            CrashUtil.logException(e, "Failed to get a readable database on query.", new Object[0]);
            db = VineDatabaseHelper.getDatabaseHelper(getContext()).getWritableDatabase();
        }
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("user_groups_view");
        try {
            Cursor c = qb.query(db, projection, selection, selArgs, null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } catch (Exception e2) {
            if (LOGGABLE) {
                SLog.d("Cannot execute {} {}  {} {} {}", (Object[]) new String[]{db.toString(), qb.getTables(), selection, sortOrder});
            }
            throw new RuntimeException(e2);
        }
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
