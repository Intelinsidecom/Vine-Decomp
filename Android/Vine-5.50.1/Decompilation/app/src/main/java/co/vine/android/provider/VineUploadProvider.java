package co.vine.android.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import co.vine.android.provider.VineUploads;
import co.vine.android.provider.VineUploadsDatabaseSQL;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;
import java.util.Arrays;

/* loaded from: classes.dex */
public class VineUploadProvider extends ContentProvider {
    public static final String AUTHORITY;
    public static final String CONTENT_AUTHORITY;
    private static final boolean LOGGABLE;
    private static final UriMatcher sUriMatcher;

    static {
        LOGGABLE = BuildUtil.isLogsOn() || Log.isLoggable("VineUploadProvider", 3);
        AUTHORITY = BuildUtil.getAuthority(".provider.VineUploadProvider");
        CONTENT_AUTHORITY = "content://" + AUTHORITY + "/";
        sUriMatcher = new UriMatcher(-1);
        sUriMatcher.addURI(AUTHORITY, "uploads/#", 1);
        sUriMatcher.addURI(AUTHORITY, "uploads/upload/#", 2);
        sUriMatcher.addURI(AUTHORITY, "uploads/reference/#", 13);
        sUriMatcher.addURI(AUTHORITY, "uploads/delete_upload", 4);
        sUriMatcher.addURI(AUTHORITY, "uploads/put_new_upload", 6);
        sUriMatcher.addURI(AUTHORITY, "uploads/put_status", 8);
        sUriMatcher.addURI(AUTHORITY, "uploads/put_uris", 9);
        sUriMatcher.addURI(AUTHORITY, "uploads/put_post_info", 10);
        sUriMatcher.addURI(AUTHORITY, "uploads/put_upload_time", 11);
        sUriMatcher.addURI(AUTHORITY, "uploads/put_values", 12);
        sUriMatcher.addURI(AUTHORITY, "uploads/put_message_row_id", 14);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selArgs, String sortOrder) {
        SQLiteDatabase db;
        try {
            db = VineUploadDatabaseHelper.getDatabaseHelper(getContext()).getReadableDatabase();
        } catch (SQLiteException e) {
            CrashUtil.logException(e, "Failed to get a readable database on query.", new Object[0]);
            db = VineUploadDatabaseHelper.getDatabaseHelper(getContext()).getWritableDatabase();
        }
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        if (LOGGABLE) {
            Log.d("VineUploadProvider", "QUERY: uri " + uri + " -> " + sUriMatcher.match(uri));
        }
        int match = sUriMatcher.match(uri);
        switch (match) {
            case 1:
                String ownerId = uri.getLastPathSegment();
                qb.setTables("uploads");
                qb.appendWhere("owner_id=" + ownerId);
                break;
            case 2:
                qb.setTables("uploads");
                String ownerId2 = uri.getLastPathSegment();
                String path = uri.getQueryParameter("path");
                if (!TextUtils.isEmpty(path)) {
                    Cursor c = qb.query(db, VineUploadsDatabaseSQL.UploadsQuery.PROJECTION, "path=? AND owner_id=?", new String[]{path, ownerId2}, null, null, "_id ASC", "1");
                    return c;
                }
                return null;
            case 13:
                qb.setTables("uploads");
                String ownerId3 = uri.getLastPathSegment();
                String reference = uri.getQueryParameter("reference");
                if (!TextUtils.isEmpty(reference)) {
                    Cursor c2 = qb.query(db, VineUploadsDatabaseSQL.UploadsQuery.PROJECTION, "reference=? AND owner_id=?", new String[]{reference, ownerId3}, null, null, "_id ASC");
                    return c2;
                }
                return null;
            default:
                qb.setTables("uploads");
                break;
        }
        try {
            Cursor c3 = qb.query(db, projection, selection, selArgs, null, null, sortOrder);
            c3.setNotificationUri(getContext().getContentResolver(), uri);
            return c3;
        } catch (Exception e2) {
            if (LOGGABLE) {
                SLog.d("Cannot execute {} {}  {} {} {} {}", (Object[]) new String[]{db.toString(), qb.getTables(), selection, null, sortOrder});
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
        long rowId = -1;
        SQLiteDatabase db = VineUploadDatabaseHelper.getDatabaseHelper(getContext()).getWritableDatabase();
        if (LOGGABLE) {
            Log.d("VineUploadProvider", "QUERY: uri " + uri + " -> " + sUriMatcher.match(uri));
        }
        int match = sUriMatcher.match(uri);
        switch (match) {
            case 6:
                rowId = db.insert("uploads", null, values);
                if (LOGGABLE) {
                    Log.d("VineUploadProvider", "Upload inserted with rowId=" + rowId);
                    break;
                }
                break;
        }
        if (rowId >= 0) {
            getContext().getContentResolver().notifyChange(VineUploads.Uploads.CONTENT_URI, null);
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        SQLiteDatabase db = VineUploadDatabaseHelper.getDatabaseHelper(getContext()).getWritableDatabase();
        if (LOGGABLE) {
            Log.d("VineUploadProvider", "QUERY: uri " + uri + " -> " + sUriMatcher.match(uri));
        }
        int match = sUriMatcher.match(uri);
        switch (match) {
            case 4:
                rowsDeleted = db.delete("uploads", selection, selectionArgs);
                break;
            case 5:
                rowsDeleted = db.delete("uploads", "1", null);
                break;
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(VineUploads.Uploads.CONTENT_URI, null);
        }
        return rowsDeleted;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName;
        int rowsUpdated = 0;
        if (values == null) {
            values = new ContentValues();
        }
        SQLiteDatabase db = VineUploadDatabaseHelper.getDatabaseHelper(getContext()).getWritableDatabase();
        if (LOGGABLE) {
            Log.d("VineUploadProvider", "QUERY: uri " + uri + " -> " + sUriMatcher.match(uri));
        }
        int match = sUriMatcher.match(uri);
        switch (match) {
            case 8:
                tableName = "uploads";
                String path = uri.getQueryParameter("path");
                String status = uri.getQueryParameter("status");
                String captcha = uri.getQueryParameter("captcha_url");
                SLog.d("PUT_STATUS path={}, status={}", path, status);
                selection = "path=?";
                selectionArgs = new String[]{path};
                values.put("status", status);
                values.put("captcha_url", captcha);
                break;
            case 9:
                tableName = "uploads";
                String path2 = uri.getQueryParameter("path");
                String videoUrl = uri.getQueryParameter("video_url");
                SLog.d("PUT_URIS path={}, videoUrl={}", path2, videoUrl);
                selection = "path=?";
                selectionArgs = new String[]{path2};
                values.put("video_url", videoUrl);
                break;
            case 10:
                tableName = "uploads";
                String path3 = uri.getQueryParameter("path");
                String postInfo = uri.getQueryParameter("post_info");
                SLog.d("PUT_POST_INFO path={}, postInfo={}", path3, postInfo);
                selection = "path=?";
                selectionArgs = new String[]{path3};
                values.put("post_info", postInfo);
                break;
            case 11:
                tableName = "uploads";
                String path4 = uri.getQueryParameter("path");
                String uploadTime = uri.getQueryParameter("upload_time");
                SLog.d("PUT_UPLOAD_TIME path={}, uploadTime={}", path4, uploadTime);
                selection = "path=?";
                selectionArgs = new String[]{path4};
                values.put("upload_time", uploadTime);
                break;
            case 12:
                SLog.d("PUT_VALUES selectionArgs={}, values={}", selectionArgs, values.toString());
                tableName = "uploads";
                break;
            case 13:
            default:
                tableName = "uploads";
                break;
            case 14:
                tableName = "uploads";
                String path5 = uri.getQueryParameter("path");
                String messageId = uri.getQueryParameter("message_row_id");
                SLog.d("PUT_MESSAGE_ROW_ID path={}, id={}", path5, messageId);
                selection = "path=?";
                selectionArgs = new String[]{path5};
                values.put("message_row", messageId);
                break;
        }
        try {
            if (!values.keySet().isEmpty()) {
                rowsUpdated = db.update(tableName, values, selection, selectionArgs);
            }
            if (rowsUpdated > 0 && String.valueOf(2).equals(uri.getQueryParameter("status"))) {
                getContext().getContentResolver().notifyChange(VineUploads.Uploads.CONTENT_URI, null);
            }
            return rowsUpdated;
        } catch (SQLiteException e) {
            if (LOGGABLE) {
                SLog.d("Cannot execute update with db={}, tablename={}, selection={}, selectionArgs={}", (Object[]) new String[]{db.toString(), tableName, selection, Arrays.toString(selectionArgs)});
            }
            throw new RuntimeException(e);
        }
    }
}
