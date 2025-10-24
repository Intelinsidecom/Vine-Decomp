package co.vine.android.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import co.vine.android.util.BuildUtil;
import com.googlecode.javacv.cpp.avcodec;
import com.googlecode.javacv.cpp.avutil;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class VineProvider extends ContentProvider {
    private static final boolean LOGGABLE;
    private static final UriMatcher sUriMatcher;

    static {
        LOGGABLE = BuildUtil.isLogsOn() || Log.isLoggable("VineProvider", 3);
        sUriMatcher = new UriMatcher(-1);
        sUriMatcher.addURI(Vine.AUTHORITY, "users", 1);
        sUriMatcher.addURI(Vine.AUTHORITY, "users/id/#", 2);
        sUriMatcher.addURI(Vine.AUTHORITY, "settings", 3);
        sUriMatcher.addURI(Vine.AUTHORITY, "comments", 4);
        sUriMatcher.addURI(Vine.AUTHORITY, "likes", 5);
        sUriMatcher.addURI(Vine.AUTHORITY, "posts", 6);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/following/#", 100);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/followers/#", 101);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/all_follow/#", 102);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/friends/#", 103);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/find_friends_twitter/#", 105);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/find_friends_facebook/#", 99);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/find_friends_address/#", 104);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/likers/#", 106);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/reviners/#", 108);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/user_search_results", 107);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_groups_view/timeline/#", 20);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_groups_view/user_profile/#", 21);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_groups_view/user_likes/#", 22);
        sUriMatcher.addURI(Vine.AUTHORITY, "tag_search_results", 7);
        sUriMatcher.addURI(Vine.AUTHORITY, "tag_recently_used", 110);
        sUriMatcher.addURI(Vine.AUTHORITY, "users/hide_profile_reposts/#", 116);
        sUriMatcher.addURI(Vine.AUTHORITY, "channels", 8);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_comments_likes_view/all_timeline/#", 23);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_comments_likes_view/all_timeline_user/#", 24);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_comments_likes_view/all_timeline_user_likes/#", 25);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_comments_likes_view/all_timeline_on_the_rise/#", 26);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_comments_likes_view/all_timeline_popular/#", 27);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_comments_likes_view/all_timeline_tag/#", 28);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_comments_likes_view/post/#", 29);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_comments_likes_view/timeline_channel_popular/#", 30);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_comments_likes_view/timeline_channel_recent/#", 31);
        sUriMatcher.addURI(Vine.AUTHORITY, "post_comments_likes_view/timelines/#", 32);
        sUriMatcher.addURI(Vine.AUTHORITY, "conversations", 118);
        sUriMatcher.addURI(Vine.AUTHORITY, "conversations/conversation", 119);
        sUriMatcher.addURI(Vine.AUTHORITY, "messages", 111);
        sUriMatcher.addURI(Vine.AUTHORITY, "messages/message", 112);
        sUriMatcher.addURI(Vine.AUTHORITY, "message_users_view/conversation", avcodec.AV_CODEC_ID_INDEO5);
        sUriMatcher.addURI(Vine.AUTHORITY, "conversation_recipients/conversation", avcodec.AV_CODEC_ID_MIMIC);
        sUriMatcher.addURI(Vine.AUTHORITY, "conversation_recipients_users_view/conversation", 120);
        sUriMatcher.addURI(Vine.AUTHORITY, "inbox_view", 115);
        sUriMatcher.addURI(Vine.AUTHORITY, "user_groups_view/friends/filter/#", 117);
        sUriMatcher.addURI(Vine.AUTHORITY, "tag_recently_used/put_tag", avutil.AV_PIX_FMT_YUV420P12BE);
        sUriMatcher.addURI(Vine.AUTHORITY, "tag_recently_used/update_tag", 501);
        sUriMatcher.addURI(Vine.AUTHORITY, "editions", HttpResponseCode.UNAUTHORIZED);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:12:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0057  */
    @Override // android.content.ContentProvider
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.database.Cursor query(android.net.Uri r22, java.lang.String[] r23, java.lang.String r24, java.lang.String[] r25, java.lang.String r26) {
        /*
            Method dump skipped, instructions count: 1724
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.provider.VineProvider.query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String):android.database.Cursor");
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case 1:
                return "vnd.android.cursor.dir/vnd.vine.android.users";
            case 2:
                return "vnd.android.cursor.item/vnd.vine.android.users";
            case 3:
                return "vnd.android.cursor.item/vnd.vine.android.settings";
            case 100:
            case 101:
                return "vnd.android.cursor.dir/vnd.vine.android.users.groups";
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = VineDatabaseHelper.getDatabaseHelper(getContext()).getWritableDatabase();
        if (LOGGABLE) {
            Log.d("VineProvider", "INSERT: uri " + uri + " -> " + sUriMatcher.match(uri));
        }
        long rowId = -1;
        String id = "";
        int match = sUriMatcher.match(uri);
        switch (match) {
            case avutil.AV_PIX_FMT_YUV420P12BE /* 301 */:
                id = values.getAsString("tag_id");
                rowId = db.insert("tag_recently_used", null, values);
                if (LOGGABLE) {
                    Log.d("VineProvider", "Tag inserted to recently-used with rowId=" + rowId);
                    break;
                }
                break;
        }
        if (rowId >= 0) {
            return uri.buildUpon().appendPath(id).build();
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = VineDatabaseHelper.getDatabaseHelper(getContext()).getWritableDatabase();
        if (LOGGABLE) {
            Log.d("VineProvider", "UPDATE: uri " + uri + " -> " + sUriMatcher.match(uri));
        }
        int match = sUriMatcher.match(uri);
        switch (match) {
            case 501:
                db.update("tag_recently_used", values, selection, selectionArgs);
            default:
                return 0;
        }
    }
}
