package co.vine.android.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import co.vine.android.Friendships;
import co.vine.android.VineLoggingException;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VineConversation;
import co.vine.android.api.VineLike;
import co.vine.android.api.VinePagedData;
import co.vine.android.api.VinePost;
import co.vine.android.api.VinePrivateMessage;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineSingleNotification;
import co.vine.android.api.VineUser;
import co.vine.android.api.response.VineEditions;
import co.vine.android.client.AppController;
import co.vine.android.model.VineTag;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineDatabaseSQL;
import co.vine.android.service.VineDatabaseHelperInterface;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.LongSparseArray;
import co.vine.android.util.Util;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/* loaded from: classes.dex */
public class VineDatabaseHelper extends SQLiteOpenHelper implements VineDatabaseHelperInterface {
    private static final boolean LOGGABLE;
    private static final HashMap<String, VineDatabaseHelper> sHelperMap;
    private final Context mAppContext;
    private ContentResolver mContentResolver;

    static {
        LOGGABLE = BuildUtil.isLogsOn() && Log.isLoggable("VineDH", 3);
        sHelperMap = new HashMap<>();
    }

    public static synchronized VineDatabaseHelper getDatabaseHelper(Context context) {
        VineDatabaseHelper helper;
        String databaseName = getDatabaseName(1);
        helper = sHelperMap.get(databaseName);
        if (helper == null) {
            helper = new VineDatabaseHelper(context.getApplicationContext(), databaseName);
            sHelperMap.put(databaseName, helper);
        }
        return helper;
    }

    public static File getDatabasePath(Context context) {
        return context.getDatabasePath(getDatabaseName(1));
    }

    private VineDatabaseHelper(Context context, String name) {
        super(context, name, (SQLiteDatabase.CursorFactory) null, 27);
        this.mContentResolver = context.getContentResolver();
        this.mAppContext = context.getApplicationContext();
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) throws SQLException {
        createTables(db);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        if (LOGGABLE) {
            Log.d("VineDH", "Upgrading from " + oldVersion + " to " + newVersion);
        }
        dropAndCreateTables(db);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        if (LOGGABLE) {
            Log.d("VineDH", "Downgrading from " + oldVersion + " to " + newVersion);
        }
        dropAndCreateTables(db);
    }

    private void dropAndCreateTables(SQLiteDatabase db) throws SQLException {
        dropTables(db);
        createTables(db);
    }

    protected void createTables(SQLiteDatabase db) throws SQLException {
        db.execSQL("CREATE TABLE  users (_id INTEGER PRIMARY KEY,user_id INT,avatar_url TEXT,blocked INT,blocking INT,hide_profile_reposts INT,description TEXT,location TEXT,explicit INT,external INT,follower_count INT,following_count INT,following_flag INT,like_count INT,post_count INT,username TEXT,email_address TEXT,phone_number TEXT,verified INT,follow_status INT,last_refresh INT,accepts_oon_conversations INT,profile_background INT,loop_count INT,twitter_screenname TEXT,twitter_hidden INT);");
        db.execSQL("CREATE TABLE  settings (_id INTEGER PRIMARY KEY,name TEXT UNIQUE ON CONFLICT REPLACE,value TEXT);");
        db.execSQL("CREATE TABLE  user_sections (_id INTEGER PRIMARY KEY,user_id INT, section_type INT, section_index INT, section_title TEXT, last_section_refresh INT);");
        db.execSQL("CREATE TABLE  user_groups (_id INTEGER PRIMARY KEY,type INT,tag TEXT,user_id INT,is_last INT,g_flags INT,order_id INT);");
        db.execSQL("CREATE TABLE  comments (_id INTEGER PRIMARY KEY,comment_id INT UNIQUE NOT NULL,post_id INT NOT NULL,avatar_url TEXT,comment TEXT,user_id INT,username TEXT,timestamp INT,location TEXT,verified INT,entities BLOB,is_last INT,last_refresh INT);");
        db.execSQL("CREATE VIEW  user_groups_view AS SELECT user_groups._id AS _id,user_groups.type AS type,user_groups.tag AS tag,user_groups.user_id AS user_id,user_groups.is_last AS is_last,user_groups.g_flags AS g_flags,user_groups.order_id AS order_id,user.username AS username,user.user_id AS user_id,user.avatar_url AS avatar_url,user.blocked AS blocked,user.blocking AS blocking,user.description AS description,user.location AS location,user.explicit AS explicit,user.follower_count AS follower_count,user.following_count AS following_count,user.following_flag AS following_flag,user.like_count AS like_count,user.post_count AS post_count,user.follow_status AS follow_status,user.verified AS verified,user.accepts_oon_conversations AS accepts_oon_conversations,user.profile_background AS profile_background, user.loop_count AS loop_count, user.twitter_screenname AS twitter_screenname, user.twitter_hidden AS twitter_hidden, section.last_section_refresh AS last_section_refresh,section.section_index AS section_index,section.section_title AS section_title,section.section_type AS section_type FROM user_groups LEFT JOIN users AS user ON user_groups.user_id=user.user_id LEFT JOIN user_sections as section ON section.user_id=user.user_id ;");
        db.execSQL("CREATE TABLE  likes (_id INTEGER PRIMARY KEY,like_id INT UNIQUE NOT NULL,post_id INT NOT NULL,avatar_url TEXT,user_id INT,username TEXT,timestamp INT,location TEXT,verified INT,last_refresh INT);");
        db.execSQL("CREATE TABLE  posts (_id INTEGER PRIMARY KEY,post_id INT NOT NULL,my_repost_id INT,tags BLOB,thumbnail_url TEXT,share_url TEXT,video_low_uRL TEXT,video_url TEXT,description TEXT,foursquare_venue_id TEXT,metadata_flags INT,post_flags INT,avatar_url TEXT,user_id INT,username TEXT,timestamp INT,location TEXT,venue_data BLOB,entities BLOB,likes_count INT,reviners_count INT,comments_count INT,user_background_color INT,last_refresh INT,loops INT,velocity INT,on_fire INT);");
        db.execSQL("CREATE TABLE  post_groups (_id INTEGER PRIMARY KEY,post_type INT,tag TEXT,post_id INT,is_last INT,repost_data BLOB,reposter_id INT,sort_id INT,g_flags INT);");
        db.execSQL("CREATE VIEW  post_groups_view AS SELECT post_groups._id AS _id,post_groups.post_type AS post_type,post_groups.tag AS tag,post_groups.post_id AS post_id,post_groups.is_last AS is_last,post_groups.g_flags AS g_flags,post_groups.sort_id AS sort_id,post_groups.repost_data AS repost_data,post_groups.reposter_id AS reposter_id,posts.username AS username,posts.avatar_url AS avatar_url,posts.thumbnail_url AS thumbnail_url,posts.metadata_flags AS metadata_flags,posts.user_id AS user_id,posts.my_repost_id AS my_repost_id,posts.timestamp AS timestamp,posts.location AS location,posts.tags AS tags,posts.share_url AS share_url,posts.video_low_uRL AS video_low_uRL,posts.video_url AS video_url,posts.video_low_uRL AS video_low_uRL,posts.description AS description,posts.foursquare_venue_id AS foursquare_venue_id,posts.post_flags AS post_flags,posts.venue_data AS venue_data,posts.entities AS entities,posts.likes_count AS likes_count,posts.reviners_count AS reviners_count,posts.comments_count AS comments_count,posts.user_background_color AS user_background_color,posts.loops AS loops,posts.velocity AS velocity,posts.on_fire AS on_fire,posts.last_refresh AS last_refresh FROM post_groups LEFT JOIN posts AS posts ON post_groups.post_id=posts.post_id;");
        db.execSQL("CREATE VIEW  post_comments_likes_view AS  SELECT post_groups_view._id AS _id,post_groups_view.post_id AS post_id,post_groups_view.my_repost_id AS my_repost_id,post_groups_view.tags AS tags,post_groups_view.thumbnail_url AS thumbnail_url,post_groups_view.share_url AS share_url,post_groups_view.video_low_uRL AS video_low_uRL,post_groups_view.video_url AS video_url,post_groups_view.description AS description,post_groups_view.foursquare_venue_id AS foursquare_venue_id,post_groups_view.metadata_flags AS metadata_flags,post_groups_view.post_flags AS post_flags,post_groups_view.post_type AS post_type,post_groups_view.tag AS tag,post_groups_view.sort_id AS sort_id,post_groups_view.is_last AS is_last,post_groups_view.avatar_url AS avatar_url,post_groups_view.user_id AS user_id,post_groups_view.timestamp AS timestamp,post_groups_view.location AS location,post_groups_view.username AS username,post_groups_view.venue_data AS venue_data,post_groups_view.entities AS entities,post_groups_view.repost_data AS repost_data,post_groups_view.reposter_id AS reposter_id,post_groups_view.likes_count AS likes_count,post_groups_view.reviners_count AS reviners_count,post_groups_view.comments_count AS comments_count,post_groups_view.user_background_color AS user_background_color,post_groups_view.loops AS loops,post_groups_view.velocity AS velocity,post_groups_view.on_fire AS on_fire,post_groups_view.last_refresh AS last_refresh,likes.like_id AS like_id,likes.avatar_url AS like_user_avatar_url,likes.user_id AS like_user_user_id,likes.timestamp AS like_user_timestamp,likes.location AS like_user_location,likes.username AS like_user_username,likes.verified AS like_user_verified,null  AS comment_id,null  AS comment,null  AS comment_user_avatar_url,null  AS comment_user_user_id,null  AS comment_user_timestamp,null  AS comment_user_location,null  AS comment_user_username,null  AS comment_user_verified,null  AS comment_is_last,null  AS comment_entities FROM post_groups_view LEFT JOIN likes ON post_groups_view.post_id = likes.post_id UNION SELECT post_groups_view._id AS _id,post_groups_view.post_id AS post_id,post_groups_view.my_repost_id AS my_repost_id,post_groups_view.tags AS tags,post_groups_view.thumbnail_url AS thumbnail_url,post_groups_view.share_url AS share_url,post_groups_view.video_low_uRL AS video_low_uRL,post_groups_view.video_url AS video_url,post_groups_view.description AS description,post_groups_view.foursquare_venue_id AS foursquare_venue_id,post_groups_view.metadata_flags AS metadata_flags,post_groups_view.post_flags AS post_flags,post_groups_view.post_type AS post_type,post_groups_view.tag AS tag,post_groups_view.sort_id AS sort_id,post_groups_view.is_last AS is_last,post_groups_view.avatar_url AS avatar_url,post_groups_view.user_id AS user_id,post_groups_view.timestamp AS timestamp,post_groups_view.location AS location,post_groups_view.username AS username,post_groups_view.venue_data AS venue_data,post_groups_view.entities AS entities,post_groups_view.repost_data AS repost_data,post_groups_view.reposter_id AS reposter_id,post_groups_view.likes_count AS likes_count,post_groups_view.reviners_count AS reviners_count,post_groups_view.comments_count AS comments_count,post_groups_view.user_background_color AS user_background_color,post_groups_view.loops AS loops,post_groups_view.velocity AS velocity,post_groups_view.on_fire AS on_fire,post_groups_view.last_refresh AS last_refresh,null  AS like_id,null  AS like_user_avatar_url,null  AS like_user_user_id,null  AS like_user_timestamp,null  AS like_user_location,null  AS like_user_username,null  AS like_user_verified,comments.comment_id AS comment_id,comments.comment AS comment,comments.avatar_url AS comment_user_avatar_url,comments.user_id AS comment_user_user_id,comments.timestamp AS comment_user_timestamp,comments.location AS comment_user_location,comments.username AS comment_user_username,comments.verified AS comment_user_verified,comments.is_last AS comment_is_last,comments.entities AS comment_entities FROM post_groups_view LEFT JOIN comments ON post_groups_view.post_id = comments.post_id ");
        db.execSQL("CREATE TABLE  page_cursors (_id INTEGER PRIMARY KEY,type INT,tag TEXT,kind INT,next_page INT,previous_page INT,anchor TEXT,reversed INT);");
        db.execSQL("CREATE TABLE  tag_search_results (_id INTEGER PRIMARY KEY,tag_id INT UNIQUE NOT NULL,tag_name TEXT NOT NULL,last_used_timestamp TEXT,is_last INT);");
        db.execSQL("CREATE TABLE  channels (_id INTEGER PRIMARY KEY,channel_id INT UNIQUE NOT NULL,channel TEXT NOT NULL,timestamp INT,background_color TEXT,font_color TEXT,last_used_timestamp INT,is_last INT,icon_full_url TEXT,retina_icon_full_url TEXT,following INT,retina_explore_icon_full_url TEXT);");
        db.execSQL("CREATE TABLE  notifications (_id INTEGER PRIMARY KEY,notification_id INT UNIQUE NOT NULL,notification_type INT,message TEXT,cleared INT, conversation_row_id INT, avatar_url TEXT);");
        db.execSQL("CREATE TABLE  tag_recently_used (_id INTEGER PRIMARY KEY,tag_id INT UNIQUE NOT NULL,tag_name TEXT NOT NULL,last_used_timestamp TEXT);");
        db.execSQL("CREATE TABLE  editions (_id INTEGER PRIMARY KEY,edition_code TEXT,edition_name TEXT);");
        db.execSQL("CREATE TABLE  messages (_id INTEGER PRIMARY KEY,conversation_row_id INT NOT NULL,message_id INT NOT NULL,user_row_id INT NOT NULL,timestamp INT,message TEXT,video_url TEXT,thumbnail_url TEXT, is_last INT,vanished INT,max_loops INT,vanished_date INT,ephemeral INT,local_start_time INT,deleted INT,post_id INT,upload_path TEXT,error_code INT,error_reason TEXT);");
        db.execSQL("CREATE TABLE  conversations (_id INTEGER PRIMARY KEY,conversation_id INT NOT NULL,network_type INT NOT NULL, unread_message_count INT, is_hidden INT DEFAULT 0, last_message INT, last_message_timestamp INT, is_last INT);");
        db.execSQL("CREATE VIEW message_users_view AS SELECT m._id AS _id,m.conversation_row_id AS conversation_row_id,m.message_id AS message_id,m.user_row_id AS user_row_id,m.message AS message,m.timestamp AS timestamp,m.video_url AS video_url,m.thumbnail_url AS thumbnail_url,m.is_last AS is_last,m.vanished AS vanished,m.max_loops AS max_loops,m.vanished_date AS vanished_date,m.ephemeral AS ephemeral,m.local_start_time AS local_start_time,m.deleted AS deleted,m.post_id AS post_id,m.upload_path AS upload_path,m.error_code AS error_code,m.error_reason AS error_reason,c.conversation_id AS conversation_id,c.network_type AS network_type,c.is_hidden AS is_hidden,c.unread_message_count AS unread_message_count,user.username AS sender_user_name,user.avatar_url AS sender_avatar,user.profile_background AS sender_profile_background,post.user_id AS author_user_id,post.username AS author_user_name,post.avatar_url AS author_avatar,post.entities AS post_entities,post.description AS post_description,post.share_url AS post_share_url FROM messages AS  m LEFT JOIN users AS user ON m.user_row_id=user._id LEFT JOIN conversations AS c ON m.conversation_row_id=c._id LEFT JOIN (SELECT p.post_id, p.user_id, p.username, p.avatar_url, p.entities, p.description, p.share_url FROM messages AS m LEFT JOIN post_comments_likes_view AS p ON m.post_id=p.post_id GROUP BY p.post_id) AS post ON m.post_id=post.post_id;");
        db.execSQL("CREATE TABLE conversation_recipients (_id INTEGER PRIMARY KEY,conversation_row_id INT NOT NULL,user_row_id INT NOT NULL, UNIQUE (conversation_row_id,user_row_id) ON CONFLICT IGNORE);");
        db.execSQL("CREATE VIEW inbox_view AS SELECT cr._id AS _id,cr.conversation_row_id AS conversation_row_id,( SELECT COUNT(*) FROM conversation_recipients where conversation_row_id = cr.conversation_row_id group by conversation_row_id) AS recipients_count,m.timestamp AS timestamp,m.error_reason AS error_reason,c.network_type AS network_type,c.unread_message_count AS unread_message_count,c.is_hidden AS is_hidden,c.last_message AS last_message,c.is_last AS is_last,u.username AS username,u._id AS user_row_id,u.user_id AS user_id,u.external AS user_is_external,u.following_flag AS following_flag,u.avatar_url AS avatar_url,u.profile_background AS profile_background,u.twitter_screenname AS twitter_screenname,u.twitter_hidden AS twitter_hidden,u.verified AS user_verified FROM conversation_recipients AS cr JOIN users AS u ON cr.user_row_id= u._id JOIN messages AS m ON c.last_message=m._id JOIN conversations AS c ON c._id =cr.conversation_row_id GROUP BY cr.conversation_row_id ORDER BY timestamp DESC;");
        db.execSQL("CREATE VIEW  conversation_recipients_users_view AS SELECT conversation_recipients._id AS _id,conversation_recipients.conversation_row_id AS conversation_row_id,conversation_recipients.user_row_id AS user_row_id,user.username AS username,user.user_id AS user_id,user.phone_number AS phone_number,user.email_address AS email_address,user.profile_background AS profile_background FROM conversation_recipients LEFT JOIN users AS user ON conversation_recipients.user_row_id=user._id;");
    }

    private void dropTables(SQLiteDatabase db) throws SQLException {
        db.execSQL("DROP VIEW IF EXISTS  user_groups_view;");
        db.execSQL("DROP VIEW IF EXISTS  post_groups_view;");
        db.execSQL("DROP VIEW IF EXISTS  post_comments_likes_view;");
        db.execSQL("DROP TABLE IF EXISTS users;");
        db.execSQL("DROP TABLE IF EXISTS settings;");
        db.execSQL("DROP TABLE IF EXISTS user_groups;");
        db.execSQL("DROP TABLE IF EXISTS comments;");
        db.execSQL("DROP TABLE IF EXISTS likes;");
        db.execSQL("DROP TABLE IF EXISTS posts;");
        db.execSQL("DROP TABLE IF EXISTS post_groups;");
        db.execSQL("DROP TABLE IF EXISTS page_cursors;");
        db.execSQL("DROP TABLE IF EXISTS tag_search_results;");
        db.execSQL("DROP TABLE IF EXISTS tag_search_results;");
        db.execSQL("DROP TABLE IF EXISTS channels;");
        db.execSQL("DROP TABLE IF EXISTS notifications;");
        db.execSQL("DROP TABLE IF EXISTS tag_recently_used;");
        db.execSQL("DROP TABLE IF EXISTS editions;");
        db.execSQL("DROP TABLE IF EXISTS messages;");
        db.execSQL("DROP TABLE IF EXISTS conversation_recipients;");
        db.execSQL("DROP TABLE IF EXISTS conversations;");
        db.execSQL("DROP TABLE IF EXISTS user_sections;");
        db.execSQL("DROP VIEW IF EXISTS message_users_view;");
        db.execSQL("DROP VIEW IF EXISTS inbox_view;");
        db.execSQL("DROP VIEW IF EXISTS conversation_recipients_users_view;");
    }

    @Override // co.vine.android.service.VineDatabaseHelperInterface
    public synchronized Cursor getOldestSortId(int groupType) {
        SQLiteDatabase db;
        db = getReadableDatabase();
        return db.query("post_groups", new String[]{"sort_id"}, "post_type=?", new String[]{String.valueOf(groupType)}, null, null, "CAST (sort_id AS INTEGER) DESC", "1");
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x02b9 A[Catch: all -> 0x0040, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:4:0x0028, B:6:0x002e, B:11:0x0043, B:13:0x0049, B:22:0x008c, B:24:0x0096, B:26:0x009c, B:18:0x0067, B:19:0x006b, B:21:0x0071, B:32:0x00dc, B:34:0x00e4, B:36:0x00ea, B:37:0x00f9, B:47:0x0122, B:48:0x0125, B:57:0x0166, B:58:0x0169, B:60:0x016d, B:62:0x0173, B:64:0x0179, B:79:0x01af, B:82:0x01cf, B:84:0x01df, B:85:0x01f9, B:86:0x01fc, B:88:0x0214, B:113:0x02ac, B:138:0x0354, B:139:0x0359, B:114:0x02b1, B:116:0x02b9, B:144:0x0363, B:186:0x04a8, B:187:0x04ad, B:145:0x0368, B:147:0x036c, B:157:0x03c1, B:174:0x0474, B:204:0x0559, B:205:0x055c, B:184:0x049a, B:181:0x0484, B:183:0x0493, B:188:0x04ae, B:190:0x04b4, B:191:0x04cc, B:193:0x04ec, B:195:0x053a, B:151:0x03a4, B:77:0x01ab, B:78:0x01ae, B:74:0x01a6, B:75:0x01a9, B:67:0x018a, B:17:0x005e, B:28:0x00a9, B:29:0x00b8, B:31:0x00be, B:89:0x0217, B:92:0x0224, B:94:0x0237, B:95:0x023d, B:97:0x0247, B:98:0x024d, B:101:0x026c, B:103:0x0274, B:105:0x027c, B:107:0x0288, B:108:0x029f, B:112:0x02a9, B:39:0x0107, B:43:0x0111, B:71:0x019c, B:117:0x02bc, B:120:0x02c9, B:122:0x02dc, B:123:0x02e2, B:125:0x02ec, B:126:0x02f2, B:129:0x031c, B:131:0x0324, B:133:0x032c, B:135:0x0338, B:143:0x0360, B:159:0x03d7, B:161:0x03dd, B:173:0x046f, B:201:0x0552, B:202:0x0557, B:50:0x013e, B:52:0x0144, B:54:0x0157, B:55:0x0160), top: B:206:0x0001, inners: #2, #3, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x036c A[Catch: all -> 0x0040, TryCatch #0 {, blocks: (B:3:0x0001, B:4:0x0028, B:6:0x002e, B:11:0x0043, B:13:0x0049, B:22:0x008c, B:24:0x0096, B:26:0x009c, B:18:0x0067, B:19:0x006b, B:21:0x0071, B:32:0x00dc, B:34:0x00e4, B:36:0x00ea, B:37:0x00f9, B:47:0x0122, B:48:0x0125, B:57:0x0166, B:58:0x0169, B:60:0x016d, B:62:0x0173, B:64:0x0179, B:79:0x01af, B:82:0x01cf, B:84:0x01df, B:85:0x01f9, B:86:0x01fc, B:88:0x0214, B:113:0x02ac, B:138:0x0354, B:139:0x0359, B:114:0x02b1, B:116:0x02b9, B:144:0x0363, B:186:0x04a8, B:187:0x04ad, B:145:0x0368, B:147:0x036c, B:157:0x03c1, B:174:0x0474, B:204:0x0559, B:205:0x055c, B:184:0x049a, B:181:0x0484, B:183:0x0493, B:188:0x04ae, B:190:0x04b4, B:191:0x04cc, B:193:0x04ec, B:195:0x053a, B:151:0x03a4, B:77:0x01ab, B:78:0x01ae, B:74:0x01a6, B:75:0x01a9, B:67:0x018a, B:17:0x005e, B:28:0x00a9, B:29:0x00b8, B:31:0x00be, B:89:0x0217, B:92:0x0224, B:94:0x0237, B:95:0x023d, B:97:0x0247, B:98:0x024d, B:101:0x026c, B:103:0x0274, B:105:0x027c, B:107:0x0288, B:108:0x029f, B:112:0x02a9, B:39:0x0107, B:43:0x0111, B:71:0x019c, B:117:0x02bc, B:120:0x02c9, B:122:0x02dc, B:123:0x02e2, B:125:0x02ec, B:126:0x02f2, B:129:0x031c, B:131:0x0324, B:133:0x032c, B:135:0x0338, B:143:0x0360, B:159:0x03d7, B:161:0x03dd, B:173:0x046f, B:201:0x0552, B:202:0x0557, B:50:0x013e, B:52:0x0144, B:54:0x0157, B:55:0x0160), top: B:206:0x0001, inners: #2, #3, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:153:0x03b9  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x03dd A[Catch: all -> 0x0558, TRY_LEAVE, TryCatch #5 {all -> 0x0558, blocks: (B:159:0x03d7, B:161:0x03dd, B:173:0x046f, B:201:0x0552, B:202:0x0557, B:164:0x03e7, B:171:0x0469, B:172:0x046c, B:198:0x054d, B:199:0x0550, B:166:0x03f3, B:168:0x03f9, B:170:0x0425), top: B:213:0x03d7, outer: #0, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0484 A[Catch: all -> 0x0040, TryCatch #0 {, blocks: (B:3:0x0001, B:4:0x0028, B:6:0x002e, B:11:0x0043, B:13:0x0049, B:22:0x008c, B:24:0x0096, B:26:0x009c, B:18:0x0067, B:19:0x006b, B:21:0x0071, B:32:0x00dc, B:34:0x00e4, B:36:0x00ea, B:37:0x00f9, B:47:0x0122, B:48:0x0125, B:57:0x0166, B:58:0x0169, B:60:0x016d, B:62:0x0173, B:64:0x0179, B:79:0x01af, B:82:0x01cf, B:84:0x01df, B:85:0x01f9, B:86:0x01fc, B:88:0x0214, B:113:0x02ac, B:138:0x0354, B:139:0x0359, B:114:0x02b1, B:116:0x02b9, B:144:0x0363, B:186:0x04a8, B:187:0x04ad, B:145:0x0368, B:147:0x036c, B:157:0x03c1, B:174:0x0474, B:204:0x0559, B:205:0x055c, B:184:0x049a, B:181:0x0484, B:183:0x0493, B:188:0x04ae, B:190:0x04b4, B:191:0x04cc, B:193:0x04ec, B:195:0x053a, B:151:0x03a4, B:77:0x01ab, B:78:0x01ae, B:74:0x01a6, B:75:0x01a9, B:67:0x018a, B:17:0x005e, B:28:0x00a9, B:29:0x00b8, B:31:0x00be, B:89:0x0217, B:92:0x0224, B:94:0x0237, B:95:0x023d, B:97:0x0247, B:98:0x024d, B:101:0x026c, B:103:0x0274, B:105:0x027c, B:107:0x0288, B:108:0x029f, B:112:0x02a9, B:39:0x0107, B:43:0x0111, B:71:0x019c, B:117:0x02bc, B:120:0x02c9, B:122:0x02dc, B:123:0x02e2, B:125:0x02ec, B:126:0x02f2, B:129:0x031c, B:131:0x0324, B:133:0x032c, B:135:0x0338, B:143:0x0360, B:159:0x03d7, B:161:0x03dd, B:173:0x046f, B:201:0x0552, B:202:0x0557, B:50:0x013e, B:52:0x0144, B:54:0x0157, B:55:0x0160), top: B:206:0x0001, inners: #2, #3, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0493 A[Catch: all -> 0x0040, TryCatch #0 {, blocks: (B:3:0x0001, B:4:0x0028, B:6:0x002e, B:11:0x0043, B:13:0x0049, B:22:0x008c, B:24:0x0096, B:26:0x009c, B:18:0x0067, B:19:0x006b, B:21:0x0071, B:32:0x00dc, B:34:0x00e4, B:36:0x00ea, B:37:0x00f9, B:47:0x0122, B:48:0x0125, B:57:0x0166, B:58:0x0169, B:60:0x016d, B:62:0x0173, B:64:0x0179, B:79:0x01af, B:82:0x01cf, B:84:0x01df, B:85:0x01f9, B:86:0x01fc, B:88:0x0214, B:113:0x02ac, B:138:0x0354, B:139:0x0359, B:114:0x02b1, B:116:0x02b9, B:144:0x0363, B:186:0x04a8, B:187:0x04ad, B:145:0x0368, B:147:0x036c, B:157:0x03c1, B:174:0x0474, B:204:0x0559, B:205:0x055c, B:184:0x049a, B:181:0x0484, B:183:0x0493, B:188:0x04ae, B:190:0x04b4, B:191:0x04cc, B:193:0x04ec, B:195:0x053a, B:151:0x03a4, B:77:0x01ab, B:78:0x01ae, B:74:0x01a6, B:75:0x01a9, B:67:0x018a, B:17:0x005e, B:28:0x00a9, B:29:0x00b8, B:31:0x00be, B:89:0x0217, B:92:0x0224, B:94:0x0237, B:95:0x023d, B:97:0x0247, B:98:0x024d, B:101:0x026c, B:103:0x0274, B:105:0x027c, B:107:0x0288, B:108:0x029f, B:112:0x02a9, B:39:0x0107, B:43:0x0111, B:71:0x019c, B:117:0x02bc, B:120:0x02c9, B:122:0x02dc, B:123:0x02e2, B:125:0x02ec, B:126:0x02f2, B:129:0x031c, B:131:0x0324, B:133:0x032c, B:135:0x0338, B:143:0x0360, B:159:0x03d7, B:161:0x03dd, B:173:0x046f, B:201:0x0552, B:202:0x0557, B:50:0x013e, B:52:0x0144, B:54:0x0157, B:55:0x0160), top: B:206:0x0001, inners: #2, #3, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:188:0x04ae A[Catch: all -> 0x0040, TryCatch #0 {, blocks: (B:3:0x0001, B:4:0x0028, B:6:0x002e, B:11:0x0043, B:13:0x0049, B:22:0x008c, B:24:0x0096, B:26:0x009c, B:18:0x0067, B:19:0x006b, B:21:0x0071, B:32:0x00dc, B:34:0x00e4, B:36:0x00ea, B:37:0x00f9, B:47:0x0122, B:48:0x0125, B:57:0x0166, B:58:0x0169, B:60:0x016d, B:62:0x0173, B:64:0x0179, B:79:0x01af, B:82:0x01cf, B:84:0x01df, B:85:0x01f9, B:86:0x01fc, B:88:0x0214, B:113:0x02ac, B:138:0x0354, B:139:0x0359, B:114:0x02b1, B:116:0x02b9, B:144:0x0363, B:186:0x04a8, B:187:0x04ad, B:145:0x0368, B:147:0x036c, B:157:0x03c1, B:174:0x0474, B:204:0x0559, B:205:0x055c, B:184:0x049a, B:181:0x0484, B:183:0x0493, B:188:0x04ae, B:190:0x04b4, B:191:0x04cc, B:193:0x04ec, B:195:0x053a, B:151:0x03a4, B:77:0x01ab, B:78:0x01ae, B:74:0x01a6, B:75:0x01a9, B:67:0x018a, B:17:0x005e, B:28:0x00a9, B:29:0x00b8, B:31:0x00be, B:89:0x0217, B:92:0x0224, B:94:0x0237, B:95:0x023d, B:97:0x0247, B:98:0x024d, B:101:0x026c, B:103:0x0274, B:105:0x027c, B:107:0x0288, B:108:0x029f, B:112:0x02a9, B:39:0x0107, B:43:0x0111, B:71:0x019c, B:117:0x02bc, B:120:0x02c9, B:122:0x02dc, B:123:0x02e2, B:125:0x02ec, B:126:0x02f2, B:129:0x031c, B:131:0x0324, B:133:0x032c, B:135:0x0338, B:143:0x0360, B:159:0x03d7, B:161:0x03dd, B:173:0x046f, B:201:0x0552, B:202:0x0557, B:50:0x013e, B:52:0x0144, B:54:0x0157, B:55:0x0160), top: B:206:0x0001, inners: #2, #3, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:214:0x013e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0169 A[Catch: all -> 0x0040, TryCatch #0 {, blocks: (B:3:0x0001, B:4:0x0028, B:6:0x002e, B:11:0x0043, B:13:0x0049, B:22:0x008c, B:24:0x0096, B:26:0x009c, B:18:0x0067, B:19:0x006b, B:21:0x0071, B:32:0x00dc, B:34:0x00e4, B:36:0x00ea, B:37:0x00f9, B:47:0x0122, B:48:0x0125, B:57:0x0166, B:58:0x0169, B:60:0x016d, B:62:0x0173, B:64:0x0179, B:79:0x01af, B:82:0x01cf, B:84:0x01df, B:85:0x01f9, B:86:0x01fc, B:88:0x0214, B:113:0x02ac, B:138:0x0354, B:139:0x0359, B:114:0x02b1, B:116:0x02b9, B:144:0x0363, B:186:0x04a8, B:187:0x04ad, B:145:0x0368, B:147:0x036c, B:157:0x03c1, B:174:0x0474, B:204:0x0559, B:205:0x055c, B:184:0x049a, B:181:0x0484, B:183:0x0493, B:188:0x04ae, B:190:0x04b4, B:191:0x04cc, B:193:0x04ec, B:195:0x053a, B:151:0x03a4, B:77:0x01ab, B:78:0x01ae, B:74:0x01a6, B:75:0x01a9, B:67:0x018a, B:17:0x005e, B:28:0x00a9, B:29:0x00b8, B:31:0x00be, B:89:0x0217, B:92:0x0224, B:94:0x0237, B:95:0x023d, B:97:0x0247, B:98:0x024d, B:101:0x026c, B:103:0x0274, B:105:0x027c, B:107:0x0288, B:108:0x029f, B:112:0x02a9, B:39:0x0107, B:43:0x0111, B:71:0x019c, B:117:0x02bc, B:120:0x02c9, B:122:0x02dc, B:123:0x02e2, B:125:0x02ec, B:126:0x02f2, B:129:0x031c, B:131:0x0324, B:133:0x032c, B:135:0x0338, B:143:0x0360, B:159:0x03d7, B:161:0x03dd, B:173:0x046f, B:201:0x0552, B:202:0x0557, B:50:0x013e, B:52:0x0144, B:54:0x0157, B:55:0x0160), top: B:206:0x0001, inners: #2, #3, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01cf A[Catch: all -> 0x0040, TryCatch #0 {, blocks: (B:3:0x0001, B:4:0x0028, B:6:0x002e, B:11:0x0043, B:13:0x0049, B:22:0x008c, B:24:0x0096, B:26:0x009c, B:18:0x0067, B:19:0x006b, B:21:0x0071, B:32:0x00dc, B:34:0x00e4, B:36:0x00ea, B:37:0x00f9, B:47:0x0122, B:48:0x0125, B:57:0x0166, B:58:0x0169, B:60:0x016d, B:62:0x0173, B:64:0x0179, B:79:0x01af, B:82:0x01cf, B:84:0x01df, B:85:0x01f9, B:86:0x01fc, B:88:0x0214, B:113:0x02ac, B:138:0x0354, B:139:0x0359, B:114:0x02b1, B:116:0x02b9, B:144:0x0363, B:186:0x04a8, B:187:0x04ad, B:145:0x0368, B:147:0x036c, B:157:0x03c1, B:174:0x0474, B:204:0x0559, B:205:0x055c, B:184:0x049a, B:181:0x0484, B:183:0x0493, B:188:0x04ae, B:190:0x04b4, B:191:0x04cc, B:193:0x04ec, B:195:0x053a, B:151:0x03a4, B:77:0x01ab, B:78:0x01ae, B:74:0x01a6, B:75:0x01a9, B:67:0x018a, B:17:0x005e, B:28:0x00a9, B:29:0x00b8, B:31:0x00be, B:89:0x0217, B:92:0x0224, B:94:0x0237, B:95:0x023d, B:97:0x0247, B:98:0x024d, B:101:0x026c, B:103:0x0274, B:105:0x027c, B:107:0x0288, B:108:0x029f, B:112:0x02a9, B:39:0x0107, B:43:0x0111, B:71:0x019c, B:117:0x02bc, B:120:0x02c9, B:122:0x02dc, B:123:0x02e2, B:125:0x02ec, B:126:0x02f2, B:129:0x031c, B:131:0x0324, B:133:0x032c, B:135:0x0338, B:143:0x0360, B:159:0x03d7, B:161:0x03dd, B:173:0x046f, B:201:0x0552, B:202:0x0557, B:50:0x013e, B:52:0x0144, B:54:0x0157, B:55:0x0160), top: B:206:0x0001, inners: #2, #3, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0214 A[Catch: all -> 0x0040, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:4:0x0028, B:6:0x002e, B:11:0x0043, B:13:0x0049, B:22:0x008c, B:24:0x0096, B:26:0x009c, B:18:0x0067, B:19:0x006b, B:21:0x0071, B:32:0x00dc, B:34:0x00e4, B:36:0x00ea, B:37:0x00f9, B:47:0x0122, B:48:0x0125, B:57:0x0166, B:58:0x0169, B:60:0x016d, B:62:0x0173, B:64:0x0179, B:79:0x01af, B:82:0x01cf, B:84:0x01df, B:85:0x01f9, B:86:0x01fc, B:88:0x0214, B:113:0x02ac, B:138:0x0354, B:139:0x0359, B:114:0x02b1, B:116:0x02b9, B:144:0x0363, B:186:0x04a8, B:187:0x04ad, B:145:0x0368, B:147:0x036c, B:157:0x03c1, B:174:0x0474, B:204:0x0559, B:205:0x055c, B:184:0x049a, B:181:0x0484, B:183:0x0493, B:188:0x04ae, B:190:0x04b4, B:191:0x04cc, B:193:0x04ec, B:195:0x053a, B:151:0x03a4, B:77:0x01ab, B:78:0x01ae, B:74:0x01a6, B:75:0x01a9, B:67:0x018a, B:17:0x005e, B:28:0x00a9, B:29:0x00b8, B:31:0x00be, B:89:0x0217, B:92:0x0224, B:94:0x0237, B:95:0x023d, B:97:0x0247, B:98:0x024d, B:101:0x026c, B:103:0x0274, B:105:0x027c, B:107:0x0288, B:108:0x029f, B:112:0x02a9, B:39:0x0107, B:43:0x0111, B:71:0x019c, B:117:0x02bc, B:120:0x02c9, B:122:0x02dc, B:123:0x02e2, B:125:0x02ec, B:126:0x02f2, B:129:0x031c, B:131:0x0324, B:133:0x032c, B:135:0x0338, B:143:0x0360, B:159:0x03d7, B:161:0x03dd, B:173:0x046f, B:201:0x0552, B:202:0x0557, B:50:0x013e, B:52:0x0144, B:54:0x0157, B:55:0x0160), top: B:206:0x0001, inners: #2, #3, #4, #5, #6 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized co.vine.android.provider.DbResponse mergePosts(java.util.Collection<co.vine.android.api.VinePost> r58, int r59, java.lang.String r60, int r61, int r62, int r63, java.lang.String r64, boolean r65) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1373
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.provider.VineDatabaseHelper.mergePosts(java.util.Collection, int, java.lang.String, int, int, int, java.lang.String, boolean):co.vine.android.provider.DbResponse");
    }

    public synchronized int cleanUp(String anchor) {
        Long timeAnchor;
        SQLiteDatabase db;
        int deleted = 0;
        synchronized (this) {
            try {
                timeAnchor = Long.valueOf(Long.parseLong(anchor));
                db = getWritableDatabase();
            } catch (NumberFormatException ex) {
                SLog.e("Anchor format is not a number", (Throwable) ex);
            }
            try {
                db.beginTransaction();
                db.execSQL("DELETE FROM post_groups WHERE post_id IN (SELECT post_id FROM posts WHERE last_refresh < ?);", new String[]{String.valueOf(timeAnchor)});
                deleted = db.delete("posts", "last_refresh<?", new String[]{String.valueOf(timeAnchor)});
                db.setTransactionSuccessful();
                if (LOGGABLE) {
                    Log.d("VineDH", "Cleanup performed, deleted " + deleted + " posts with last_refresh less than anchor: " + timeAnchor);
                }
                if (deleted > 0) {
                    notifyPostCommentsViewUris();
                }
            } finally {
                safeFinishTransaction(db);
            }
        }
        return deleted;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x009e A[Catch: all -> 0x003d, TryCatch #5 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0040, B:22:0x0077, B:20:0x0073, B:21:0x0076, B:23:0x007a, B:26:0x009e, B:28:0x00ae, B:29:0x00c6, B:30:0x00c9, B:33:0x00d5, B:35:0x00e5, B:36:0x00fd, B:37:0x0100, B:39:0x0106, B:40:0x0114, B:42:0x011c, B:52:0x015e, B:64:0x01b0, B:65:0x01b5, B:55:0x0167, B:68:0x01bb, B:99:0x02a7, B:100:0x02ac, B:71:0x01c4, B:88:0x0261, B:108:0x02ba, B:109:0x02bd, B:93:0x0274, B:95:0x0278, B:92:0x026a, B:13:0x004f, B:15:0x0055, B:17:0x0068, B:56:0x016a, B:59:0x0177, B:67:0x01b8, B:73:0x01d9, B:75:0x01df, B:87:0x025c, B:105:0x02b3, B:106:0x02b8, B:78:0x01e9, B:85:0x0256, B:86:0x0259, B:102:0x02ae, B:103:0x02b1, B:80:0x01f5, B:82:0x01fb, B:84:0x0218, B:43:0x011f, B:46:0x012c, B:49:0x0154, B:51:0x015b), top: B:118:0x0001, inners: #1, #2, #3, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00d5 A[Catch: all -> 0x003d, TryCatch #5 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0040, B:22:0x0077, B:20:0x0073, B:21:0x0076, B:23:0x007a, B:26:0x009e, B:28:0x00ae, B:29:0x00c6, B:30:0x00c9, B:33:0x00d5, B:35:0x00e5, B:36:0x00fd, B:37:0x0100, B:39:0x0106, B:40:0x0114, B:42:0x011c, B:52:0x015e, B:64:0x01b0, B:65:0x01b5, B:55:0x0167, B:68:0x01bb, B:99:0x02a7, B:100:0x02ac, B:71:0x01c4, B:88:0x0261, B:108:0x02ba, B:109:0x02bd, B:93:0x0274, B:95:0x0278, B:92:0x026a, B:13:0x004f, B:15:0x0055, B:17:0x0068, B:56:0x016a, B:59:0x0177, B:67:0x01b8, B:73:0x01d9, B:75:0x01df, B:87:0x025c, B:105:0x02b3, B:106:0x02b8, B:78:0x01e9, B:85:0x0256, B:86:0x0259, B:102:0x02ae, B:103:0x02b1, B:80:0x01f5, B:82:0x01fb, B:84:0x0218, B:43:0x011f, B:46:0x012c, B:49:0x0154, B:51:0x015b), top: B:118:0x0001, inners: #1, #2, #3, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0106 A[Catch: all -> 0x003d, TryCatch #5 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0040, B:22:0x0077, B:20:0x0073, B:21:0x0076, B:23:0x007a, B:26:0x009e, B:28:0x00ae, B:29:0x00c6, B:30:0x00c9, B:33:0x00d5, B:35:0x00e5, B:36:0x00fd, B:37:0x0100, B:39:0x0106, B:40:0x0114, B:42:0x011c, B:52:0x015e, B:64:0x01b0, B:65:0x01b5, B:55:0x0167, B:68:0x01bb, B:99:0x02a7, B:100:0x02ac, B:71:0x01c4, B:88:0x0261, B:108:0x02ba, B:109:0x02bd, B:93:0x0274, B:95:0x0278, B:92:0x026a, B:13:0x004f, B:15:0x0055, B:17:0x0068, B:56:0x016a, B:59:0x0177, B:67:0x01b8, B:73:0x01d9, B:75:0x01df, B:87:0x025c, B:105:0x02b3, B:106:0x02b8, B:78:0x01e9, B:85:0x0256, B:86:0x0259, B:102:0x02ae, B:103:0x02b1, B:80:0x01f5, B:82:0x01fb, B:84:0x0218, B:43:0x011f, B:46:0x012c, B:49:0x0154, B:51:0x015b), top: B:118:0x0001, inners: #1, #2, #3, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x011c A[Catch: all -> 0x003d, TRY_LEAVE, TryCatch #5 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0040, B:22:0x0077, B:20:0x0073, B:21:0x0076, B:23:0x007a, B:26:0x009e, B:28:0x00ae, B:29:0x00c6, B:30:0x00c9, B:33:0x00d5, B:35:0x00e5, B:36:0x00fd, B:37:0x0100, B:39:0x0106, B:40:0x0114, B:42:0x011c, B:52:0x015e, B:64:0x01b0, B:65:0x01b5, B:55:0x0167, B:68:0x01bb, B:99:0x02a7, B:100:0x02ac, B:71:0x01c4, B:88:0x0261, B:108:0x02ba, B:109:0x02bd, B:93:0x0274, B:95:0x0278, B:92:0x026a, B:13:0x004f, B:15:0x0055, B:17:0x0068, B:56:0x016a, B:59:0x0177, B:67:0x01b8, B:73:0x01d9, B:75:0x01df, B:87:0x025c, B:105:0x02b3, B:106:0x02b8, B:78:0x01e9, B:85:0x0256, B:86:0x0259, B:102:0x02ae, B:103:0x02b1, B:80:0x01f5, B:82:0x01fb, B:84:0x0218, B:43:0x011f, B:46:0x012c, B:49:0x0154, B:51:0x015b), top: B:118:0x0001, inners: #1, #2, #3, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0163 A[PHI: r32
  0x0163: PHI (r32v1 'numInserted' int) = (r32v0 'numInserted' int), (r32v2 'numInserted' int) binds: [B:41:0x011a, B:52:0x015e] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0167 A[Catch: all -> 0x003d, TRY_LEAVE, TryCatch #5 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0040, B:22:0x0077, B:20:0x0073, B:21:0x0076, B:23:0x007a, B:26:0x009e, B:28:0x00ae, B:29:0x00c6, B:30:0x00c9, B:33:0x00d5, B:35:0x00e5, B:36:0x00fd, B:37:0x0100, B:39:0x0106, B:40:0x0114, B:42:0x011c, B:52:0x015e, B:64:0x01b0, B:65:0x01b5, B:55:0x0167, B:68:0x01bb, B:99:0x02a7, B:100:0x02ac, B:71:0x01c4, B:88:0x0261, B:108:0x02ba, B:109:0x02bd, B:93:0x0274, B:95:0x0278, B:92:0x026a, B:13:0x004f, B:15:0x0055, B:17:0x0068, B:56:0x016a, B:59:0x0177, B:67:0x01b8, B:73:0x01d9, B:75:0x01df, B:87:0x025c, B:105:0x02b3, B:106:0x02b8, B:78:0x01e9, B:85:0x0256, B:86:0x0259, B:102:0x02ae, B:103:0x02b1, B:80:0x01f5, B:82:0x01fb, B:84:0x0218, B:43:0x011f, B:46:0x012c, B:49:0x0154, B:51:0x015b), top: B:118:0x0001, inners: #1, #2, #3, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01c0 A[PHI: r33
  0x01c0: PHI (r33v1 'numUpdated' int) = (r33v0 'numUpdated' int), (r33v2 'numUpdated' int) binds: [B:54:0x0165, B:68:0x01bb] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01df A[Catch: all -> 0x02b9, TRY_LEAVE, TryCatch #3 {all -> 0x02b9, blocks: (B:73:0x01d9, B:75:0x01df, B:87:0x025c, B:105:0x02b3, B:106:0x02b8, B:78:0x01e9, B:85:0x0256, B:86:0x0259, B:102:0x02ae, B:103:0x02b1, B:80:0x01f5, B:82:0x01fb, B:84:0x0218), top: B:115:0x01d9, outer: #5, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x026a A[Catch: all -> 0x003d, TryCatch #5 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0040, B:22:0x0077, B:20:0x0073, B:21:0x0076, B:23:0x007a, B:26:0x009e, B:28:0x00ae, B:29:0x00c6, B:30:0x00c9, B:33:0x00d5, B:35:0x00e5, B:36:0x00fd, B:37:0x0100, B:39:0x0106, B:40:0x0114, B:42:0x011c, B:52:0x015e, B:64:0x01b0, B:65:0x01b5, B:55:0x0167, B:68:0x01bb, B:99:0x02a7, B:100:0x02ac, B:71:0x01c4, B:88:0x0261, B:108:0x02ba, B:109:0x02bd, B:93:0x0274, B:95:0x0278, B:92:0x026a, B:13:0x004f, B:15:0x0055, B:17:0x0068, B:56:0x016a, B:59:0x0177, B:67:0x01b8, B:73:0x01d9, B:75:0x01df, B:87:0x025c, B:105:0x02b3, B:106:0x02b8, B:78:0x01e9, B:85:0x0256, B:86:0x0259, B:102:0x02ae, B:103:0x02b1, B:80:0x01f5, B:82:0x01fb, B:84:0x0218, B:43:0x011f, B:46:0x012c, B:49:0x0154, B:51:0x015b), top: B:118:0x0001, inners: #1, #2, #3, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0278 A[Catch: all -> 0x003d, TRY_LEAVE, TryCatch #5 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0040, B:22:0x0077, B:20:0x0073, B:21:0x0076, B:23:0x007a, B:26:0x009e, B:28:0x00ae, B:29:0x00c6, B:30:0x00c9, B:33:0x00d5, B:35:0x00e5, B:36:0x00fd, B:37:0x0100, B:39:0x0106, B:40:0x0114, B:42:0x011c, B:52:0x015e, B:64:0x01b0, B:65:0x01b5, B:55:0x0167, B:68:0x01bb, B:99:0x02a7, B:100:0x02ac, B:71:0x01c4, B:88:0x0261, B:108:0x02ba, B:109:0x02bd, B:93:0x0274, B:95:0x0278, B:92:0x026a, B:13:0x004f, B:15:0x0055, B:17:0x0068, B:56:0x016a, B:59:0x0177, B:67:0x01b8, B:73:0x01d9, B:75:0x01df, B:87:0x025c, B:105:0x02b3, B:106:0x02b8, B:78:0x01e9, B:85:0x0256, B:86:0x0259, B:102:0x02ae, B:103:0x02b1, B:80:0x01f5, B:82:0x01fb, B:84:0x0218, B:43:0x011f, B:46:0x012c, B:49:0x0154, B:51:0x015b), top: B:118:0x0001, inners: #1, #2, #3, #6 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized int mergeLikes(java.util.Collection<co.vine.android.api.VineLike> r45, long r46, int r48, int r49) {
        /*
            Method dump skipped, instructions count: 702
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.provider.VineDatabaseHelper.mergeLikes(java.util.Collection, long, int, int):int");
    }

    public synchronized int addFollow(long userId, long sessionOwnerId, long followId, boolean notify) {
        int result;
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {String.valueOf(userId)};
        Cursor c = db.query("users", new String[]{"following_flag"}, "user_id=?", whereArgs, null, null, null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    int currentFriendship = c.getInt(0);
                    int newFriendship = Friendships.setFriendship(currentFriendship, 1);
                    ContentValues cv = new ContentValues();
                    cv.put("following_flag", Integer.valueOf(newFriendship));
                    result = db.update("users", cv, "user_id=?", whereArgs);
                    VineUser skeletonUser = new VineUser();
                    skeletonUser.userId = userId;
                    ArrayList<VineUser> users = new ArrayList<>(1);
                    users.add(skeletonUser);
                    LongSparseArray<Long> orderMap = null;
                    if (followId > 0) {
                        orderMap = new LongSparseArray<>(1);
                        orderMap.put(userId, Long.valueOf(followId));
                    }
                    mergeUserGroups(users, 1, String.valueOf(sessionOwnerId), null, orderMap);
                    if (notify) {
                        this.mContentResolver.notifyChange(Vine.UserGroupsView.CONTENT_URI_FOLLOWING, null);
                    }
                    if (LOGGABLE) {
                        Log.d("VineDH", "Followed user " + userId);
                    }
                } else {
                    if (c != null) {
                        c.close();
                    }
                    result = -1;
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        } else {
            result = -1;
        }
        return result;
    }

    public synchronized int removeFollow(long userId, boolean deleteRow, boolean notify) {
        int result;
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {String.valueOf(userId)};
        Cursor c = db.query("users", new String[]{"following_flag"}, "user_id=?", whereArgs, null, null, null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    int currentFriendship = c.getInt(0);
                    int newFriendship = Friendships.unsetFriendship(currentFriendship, 1);
                    ContentValues cv = new ContentValues();
                    cv.put("following_flag", Integer.valueOf(newFriendship));
                    result = db.update("users", cv, "user_id=?", whereArgs);
                    if (deleteRow) {
                        removeUserWithType(userId, 1, notify, Vine.UserGroupsView.CONTENT_URI_FOLLOWING);
                    }
                    if (LOGGABLE) {
                        Log.d("VineDH", "Unfollowed user " + userId);
                    }
                } else {
                    c.close();
                    result = -1;
                }
            } finally {
                c.close();
            }
        } else {
            result = -1;
        }
        return result;
    }

    @Override // co.vine.android.service.VineDatabaseHelperInterface
    public synchronized int addLike(VineLike like, boolean notify) {
        int result;
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {String.valueOf(like.postId)};
        result = 0;
        try {
            db.beginTransaction();
            Cursor c = db.query("posts", new String[]{"metadata_flags"}, "post_id=?", whereArgs, null, null, null);
            if (c != null) {
                try {
                    if (c.moveToFirst()) {
                        ContentValues cv = new ContentValues();
                        int flag = c.getInt(0);
                        cv.put("metadata_flags", Integer.valueOf(flag | 8));
                        result = db.update("posts", cv, "post_id=?", whereArgs) > 0 ? 1 : 0;
                        if (LOGGABLE) {
                            Log.d("VineDH", "Updating like value in post: " + like.postId);
                        }
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
            ArrayList<VineLike> likes = new ArrayList<>(1);
            likes.add(like);
            int mergeResult = mergeLikes(likes, like.postId, 0, 0);
            db.setTransactionSuccessful();
            if (notify && (result > 0 || mergeResult > 0)) {
                notifyPostCommentsViewUris();
            }
        } finally {
            safeFinishTransaction(db);
        }
        return result;
    }

    @Override // co.vine.android.service.VineDatabaseHelperInterface
    public synchronized int removeLike(long postId, long myUserId, boolean notify) {
        int result;
        SQLiteDatabase db = getWritableDatabase();
        result = 0;
        try {
            db.beginTransaction();
            String[] whereArgs = {String.valueOf(postId)};
            Cursor c = db.query("posts", new String[]{"metadata_flags"}, "post_id=?", whereArgs, null, null, null);
            if (c != null) {
                try {
                    if (c.moveToFirst()) {
                        ContentValues cv = new ContentValues();
                        int flag = c.getInt(0);
                        cv.put("metadata_flags", Integer.valueOf(flag & (-9)));
                        if (LOGGABLE) {
                            Log.d("VineDH", "Removing like in post: " + postId);
                        }
                        result = db.update("posts", cv, "post_id=?", whereArgs) > 0 ? 1 : 0;
                    }
                } finally {
                    c.close();
                }
            }
            int deletes = db.delete("likes", "post_id=? AND user_id=?", new String[]{String.valueOf(postId), String.valueOf(myUserId)});
            int deletes2 = deletes + removePostsFromGroupByPostId(3, postId);
            db.setTransactionSuccessful();
            if (LOGGABLE) {
                Log.d("VineDH", "Deleted self like for post: " + postId);
            }
            if (notify && (result > 0 || deletes2 > 0)) {
                notifyPostCommentsViewUris();
            }
        } finally {
            safeFinishTransaction(db);
        }
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x00c6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // co.vine.android.service.VineDatabaseHelperInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized int revine(co.vine.android.api.VineRepost r23, long r24, boolean r26) {
        /*
            Method dump skipped, instructions count: 553
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.provider.VineDatabaseHelper.revine(co.vine.android.api.VineRepost, long, boolean):int");
    }

    public synchronized ArrayList<VineSingleNotification> mergePushNotification(VineSingleNotification notification) {
        ArrayList<VineSingleNotification> ret;
        SQLiteDatabase db = getWritableDatabase();
        ret = new ArrayList<>();
        int type = notification.notificationTypeId;
        String selection = "notification_id=?" + (notification.isMessaging() ? " AND notification_type=?" : "");
        String[] whereArgs = notification.isMessaging() ? new String[]{String.valueOf(notification.getNotificationId()), String.valueOf(type)} : new String[]{String.valueOf(notification.getNotificationId())};
        Cursor c = db.query("notifications", VineDatabaseSQL.NotificationsQuery.PROJECTION, selection, whereArgs, null, null, "notification_id DESC");
        try {
            db.beginTransaction();
            if (c != null) {
                try {
                    if (!c.moveToFirst()) {
                        ContentValues values = new ContentValues();
                        fillNotificationValues(values, notification, Util.toPrettyComment(notification));
                        int inserted = 0 + (db.insert("notifications", null, values) > 0 ? 1 : 0);
                        if (inserted > 0) {
                            ret.addAll(getNotifications(type));
                        }
                    }
                } finally {
                    c.close();
                }
            }
            db.setTransactionSuccessful();
        } finally {
            safeFinishTransaction(db);
        }
        return ret;
    }

    public synchronized ArrayList<VineSingleNotification> getNotifications(int notificationType) {
        ArrayList<VineSingleNotification> ret;
        synchronized (this) {
            boolean isPrivateMessage = notificationType == 18;
            String selection = "notification_type" + (isPrivateMessage ? "=?" : "<>?");
            String[] whereArgs = {String.valueOf(18)};
            SQLiteDatabase db = getReadableDatabase();
            ret = new ArrayList<>();
            Cursor c = db.query("notifications", VineDatabaseSQL.NotificationsQuery.PROJECTION, selection, whereArgs, null, null, "notification_id DESC", "10");
            if (c != null) {
                while (c.moveToNext()) {
                    VineSingleNotification notification = new VineSingleNotification();
                    notification.avatarUrl = c.getString(6);
                    notification.comment = c.getString(3);
                    notification.notificationId = c.getLong(1);
                    notification.conversationRowId = c.getLong(5);
                    notification.notificationTypeId = c.getInt(2);
                    ret.add(notification);
                }
                c.close();
            }
        }
        return ret;
    }

    public synchronized int deletePushNotifications(int notificationDisplayId) {
        int deleted;
        synchronized (this) {
            boolean isPrivateMessage = notificationDisplayId == 2;
            String selection = "notification_type" + (isPrivateMessage ? "=?" : "<>?");
            String[] whereArgs = {String.valueOf(18)};
            SQLiteDatabase db = getWritableDatabase();
            deleted = 0 + db.delete("notifications", selection, whereArgs);
        }
        return deleted;
    }

    public void deletePushNotificationsForConversation(long conversationRowId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("notifications", "conversation_row_id=?", new String[]{String.valueOf(conversationRowId)});
    }

    public synchronized Uri mergeSuggestedTag(VineTag tag) {
        Uri result;
        result = null;
        String[] selectionArgs = {String.valueOf(tag.getTagId())};
        Cursor c = this.mContentResolver.query(Vine.TagsRecentlyUsed.CONTENT_URI, VineDatabaseSQL.TagsRecentlyUsedQuery.PROJECTION, "tag_id=?", selectionArgs, "_id DESC");
        if (c != null) {
            ContentValues values = new ContentValues();
            if (c.getCount() < 1) {
                fillTagValues(values, tag);
                result = this.mContentResolver.insert(Vine.TagsRecentlyUsed.CONTENT_URI_PUT_TAG, values);
            } else if (c.moveToFirst()) {
                String[] selectionArgs2 = {String.valueOf(tag.getTagId())};
                values.put("last_used_timestamp", Long.valueOf(System.currentTimeMillis()));
                this.mContentResolver.update(Vine.TagsRecentlyUsed.CONTENT_URI_UPDATE_RECENT_TAG, values, "tag_id=?", selectionArgs2);
            }
            c.close();
        }
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00b0 A[Catch: all -> 0x01a8, TryCatch #2 {all -> 0x01a8, blocks: (B:5:0x0009, B:17:0x0088, B:18:0x008b, B:20:0x00b0, B:22:0x00f5, B:27:0x0134, B:49:0x01b3, B:50:0x01b6, B:29:0x0139, B:31:0x0159, B:32:0x0190, B:51:0x01b7, B:40:0x01a4, B:41:0x01a7, B:24:0x0117, B:26:0x011d, B:7:0x002b, B:9:0x0031, B:11:0x0037, B:13:0x0060, B:14:0x007a), top: B:57:0x0009, outer: #3, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x011d A[Catch: all -> 0x01b2, TRY_LEAVE, TryCatch #0 {all -> 0x01b2, blocks: (B:24:0x0117, B:26:0x011d), top: B:53:0x0117, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0139 A[Catch: all -> 0x01a8, TryCatch #2 {all -> 0x01a8, blocks: (B:5:0x0009, B:17:0x0088, B:18:0x008b, B:20:0x00b0, B:22:0x00f5, B:27:0x0134, B:49:0x01b3, B:50:0x01b6, B:29:0x0139, B:31:0x0159, B:32:0x0190, B:51:0x01b7, B:40:0x01a4, B:41:0x01a7, B:24:0x0117, B:26:0x011d, B:7:0x002b, B:9:0x0031, B:11:0x0037, B:13:0x0060, B:14:0x007a), top: B:57:0x0009, outer: #3, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x019a A[Catch: all -> 0x01af, TRY_LEAVE, TryCatch #3 {, blocks: (B:3:0x0001, B:33:0x0193, B:35:0x019a, B:43:0x01a9, B:44:0x01ae, B:5:0x0009, B:17:0x0088, B:18:0x008b, B:20:0x00b0, B:22:0x00f5, B:27:0x0134, B:49:0x01b3, B:50:0x01b6, B:29:0x0139, B:31:0x0159, B:32:0x0190, B:51:0x01b7, B:40:0x01a4, B:41:0x01a7, B:24:0x0117, B:26:0x011d, B:7:0x002b, B:9:0x0031, B:11:0x0037, B:13:0x0060, B:14:0x007a), top: B:58:0x0001, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01b7 A[Catch: all -> 0x01a8, TRY_LEAVE, TryCatch #2 {all -> 0x01a8, blocks: (B:5:0x0009, B:17:0x0088, B:18:0x008b, B:20:0x00b0, B:22:0x00f5, B:27:0x0134, B:49:0x01b3, B:50:0x01b6, B:29:0x0139, B:31:0x0159, B:32:0x0190, B:51:0x01b7, B:40:0x01a4, B:41:0x01a7, B:24:0x0117, B:26:0x011d, B:7:0x002b, B:9:0x0031, B:11:0x0037, B:13:0x0060, B:14:0x007a), top: B:57:0x0009, outer: #3, inners: #0, #1 }] */
    @Override // co.vine.android.service.VineDatabaseHelperInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized int unRevine(long r28, long r30, boolean r32, boolean r33) {
        /*
            Method dump skipped, instructions count: 536
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.provider.VineDatabaseHelper.unRevine(long, long, boolean, boolean):int");
    }

    public synchronized int deletePost(long postId) {
        int deleted;
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            deleted = db.delete("posts", "post_id=?", new String[]{String.valueOf(postId)});
            int deletedGroups = 0;
            if (deleted > 0) {
                deletedGroups = db.delete("post_groups", "post_id=?", new String[]{String.valueOf(postId)});
            }
            if (deleted > 0 || deletedGroups > 0) {
                notifyPostCommentsViewUris();
            }
            if (LOGGABLE) {
                if (deletedGroups > 0) {
                    Log.d("VineDH", "Removing the post: " + postId);
                } else {
                    Log.e("VineDH", "Couldn't delete post: " + postId);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            safeFinishTransaction(db);
        }
        return deleted;
    }

    public synchronized void expireTimeline(int type) {
        removePostsFromGroup(type);
        notifyPostCommentsViewUris();
    }

    private synchronized int removePostsFromGroupByPostId(int type, long postId) {
        SQLiteDatabase db;
        String[] selArgs;
        db = getWritableDatabase();
        selArgs = new String[]{String.valueOf(type), String.valueOf(postId)};
        return db.delete("post_groups", "post_type=? AND post_id=?", selArgs);
    }

    private synchronized int removePostsFromGroup(int type) {
        int numGroups;
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            numGroups = db.delete("post_groups", "post_type=?", new String[]{String.valueOf(type)});
            db.setTransactionSuccessful();
            if (LOGGABLE) {
                Log.d("VineDH", "Clearing group " + type + " deleting " + numGroups + " groups ");
            }
        } finally {
            safeFinishTransaction(db);
        }
        return numGroups;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0057 A[Catch: all -> 0x0072, TryCatch #0 {, blocks: (B:3:0x0001, B:9:0x0046, B:10:0x0049, B:13:0x0057, B:17:0x0065, B:18:0x006a, B:28:0x0112, B:30:0x0119, B:34:0x011f, B:35:0x0124, B:20:0x006e, B:21:0x0071, B:5:0x002e, B:7:0x0034, B:25:0x0075, B:27:0x00d6), top: B:36:0x0001, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00d6 A[Catch: all -> 0x011e, TRY_LEAVE, TryCatch #2 {all -> 0x011e, blocks: (B:25:0x0075, B:27:0x00d6), top: B:39:0x0075, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0119 A[Catch: all -> 0x0072, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:9:0x0046, B:10:0x0049, B:13:0x0057, B:17:0x0065, B:18:0x006a, B:28:0x0112, B:30:0x0119, B:34:0x011f, B:35:0x0124, B:20:0x006e, B:21:0x0071, B:5:0x002e, B:7:0x0034, B:25:0x0075, B:27:0x00d6), top: B:36:0x0001, inners: #1, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized int removePostsFromGroupByUser(long r20, int r22, java.lang.String r23) {
        /*
            Method dump skipped, instructions count: 293
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.provider.VineDatabaseHelper.removePostsFromGroupByUser(long, int, java.lang.String):int");
    }

    public synchronized int mergeUsers(Collection<VineUser> users, int type, String groupTag, int next, int previous, LongSparseArray<Long> orderMap, long sectionRefreshTime, int sectionType) {
        return mergeUsers(users, type, groupTag, next, previous, null, orderMap, sectionRefreshTime, sectionType);
    }

    @Override // co.vine.android.service.VineDatabaseHelperInterface
    public synchronized int mergeUsers(Collection<VineUser> users, int type, String groupTag, int next, int previous, LongSparseArray<Long> orderMap) {
        return mergeUsers(users, type, groupTag, next, previous, null, orderMap);
    }

    public synchronized int mergeUsers(Collection<VineUser> users, int type, String groupTag, int next, int previous, LongSparseArray<Long> tagMap, LongSparseArray<Long> orderMap) {
        return mergeUsers(users, type, groupTag, next, previous, tagMap, orderMap, -1L, -1);
    }

    private void mergeUserSection(SQLiteDatabase db, ContentValues values, VineUser user, long refresh, int sectionType) {
        if (sectionType >= 0) {
            values.clear();
            values.put("section_type", Integer.valueOf(sectionType));
            if (refresh > 0) {
                values.put("last_section_refresh", Long.valueOf(refresh));
            }
            if (user.friendIndex > 0) {
                values.put("section_index", Long.valueOf(user.friendIndex));
            }
            values.put("user_id", Long.valueOf(user.userId));
            values.put("section_title", user.sectionTitle);
            String[] whereArgs = {String.valueOf(user.userId), String.valueOf(sectionType)};
            Cursor query = db.query("user_sections", new String[]{"_id"}, "user_id=? AND section_type=?", whereArgs, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        db.update("user_sections", values, "user_id=? AND section_type=?", whereArgs);
                    } else {
                        db.insert("user_sections", null, values);
                    }
                } finally {
                    query.close();
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0084 A[Catch: all -> 0x0033, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0036, B:22:0x0073, B:20:0x006f, B:21:0x0072, B:23:0x0076, B:25:0x0084, B:35:0x00cd, B:48:0x0125, B:49:0x012a, B:36:0x00d2, B:38:0x00d8, B:52:0x0130, B:89:0x02a6, B:90:0x02ab, B:55:0x0139, B:72:0x01e4, B:98:0x02b9, B:99:0x02bc, B:83:0x0267, B:85:0x026b, B:78:0x01f2, B:81:0x0205, B:82:0x0217, B:26:0x0087, B:29:0x0094, B:32:0x00b9, B:34:0x00ca, B:39:0x00db, B:42:0x00e8, B:45:0x0115, B:51:0x012d, B:57:0x014c, B:59:0x0152, B:71:0x01df, B:95:0x02b2, B:96:0x02b7, B:13:0x004d, B:15:0x0053, B:17:0x0066), top: B:101:0x0001, inners: #2, #3, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00d2 A[Catch: all -> 0x0033, PHI: r24
  0x00d2: PHI (r24v1 'numInserted' int) = (r24v0 'numInserted' int), (r24v2 'numInserted' int) binds: [B:24:0x0082, B:35:0x00cd] A[DONT_GENERATE, DONT_INLINE], TryCatch #1 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0036, B:22:0x0073, B:20:0x006f, B:21:0x0072, B:23:0x0076, B:25:0x0084, B:35:0x00cd, B:48:0x0125, B:49:0x012a, B:36:0x00d2, B:38:0x00d8, B:52:0x0130, B:89:0x02a6, B:90:0x02ab, B:55:0x0139, B:72:0x01e4, B:98:0x02b9, B:99:0x02bc, B:83:0x0267, B:85:0x026b, B:78:0x01f2, B:81:0x0205, B:82:0x0217, B:26:0x0087, B:29:0x0094, B:32:0x00b9, B:34:0x00ca, B:39:0x00db, B:42:0x00e8, B:45:0x0115, B:51:0x012d, B:57:0x014c, B:59:0x0152, B:71:0x01df, B:95:0x02b2, B:96:0x02b7, B:13:0x004d, B:15:0x0053, B:17:0x0066), top: B:101:0x0001, inners: #2, #3, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00d8 A[Catch: all -> 0x0033, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0036, B:22:0x0073, B:20:0x006f, B:21:0x0072, B:23:0x0076, B:25:0x0084, B:35:0x00cd, B:48:0x0125, B:49:0x012a, B:36:0x00d2, B:38:0x00d8, B:52:0x0130, B:89:0x02a6, B:90:0x02ab, B:55:0x0139, B:72:0x01e4, B:98:0x02b9, B:99:0x02bc, B:83:0x0267, B:85:0x026b, B:78:0x01f2, B:81:0x0205, B:82:0x0217, B:26:0x0087, B:29:0x0094, B:32:0x00b9, B:34:0x00ca, B:39:0x00db, B:42:0x00e8, B:45:0x0115, B:51:0x012d, B:57:0x014c, B:59:0x0152, B:71:0x01df, B:95:0x02b2, B:96:0x02b7, B:13:0x004d, B:15:0x0053, B:17:0x0066), top: B:101:0x0001, inners: #2, #3, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0135 A[PHI: r25
  0x0135: PHI (r25v1 'numUpdated' int) = (r25v0 'numUpdated' int), (r25v2 'numUpdated' int) binds: [B:37:0x00d6, B:52:0x0130] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0152 A[Catch: all -> 0x02b8, TRY_LEAVE, TryCatch #4 {all -> 0x02b8, blocks: (B:57:0x014c, B:59:0x0152, B:71:0x01df, B:95:0x02b2, B:96:0x02b7, B:62:0x015c, B:69:0x01d9, B:70:0x01dc, B:92:0x02ad, B:93:0x02b0, B:64:0x0168, B:66:0x016e, B:68:0x019b), top: B:106:0x014c, outer: #1, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01ed  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01f2 A[Catch: all -> 0x0033, TryCatch #1 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0036, B:22:0x0073, B:20:0x006f, B:21:0x0072, B:23:0x0076, B:25:0x0084, B:35:0x00cd, B:48:0x0125, B:49:0x012a, B:36:0x00d2, B:38:0x00d8, B:52:0x0130, B:89:0x02a6, B:90:0x02ab, B:55:0x0139, B:72:0x01e4, B:98:0x02b9, B:99:0x02bc, B:83:0x0267, B:85:0x026b, B:78:0x01f2, B:81:0x0205, B:82:0x0217, B:26:0x0087, B:29:0x0094, B:32:0x00b9, B:34:0x00ca, B:39:0x00db, B:42:0x00e8, B:45:0x0115, B:51:0x012d, B:57:0x014c, B:59:0x0152, B:71:0x01df, B:95:0x02b2, B:96:0x02b7, B:13:0x004d, B:15:0x0053, B:17:0x0066), top: B:101:0x0001, inners: #2, #3, #4, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x026b A[Catch: all -> 0x0033, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:4:0x001f, B:6:0x0025, B:11:0x0036, B:22:0x0073, B:20:0x006f, B:21:0x0072, B:23:0x0076, B:25:0x0084, B:35:0x00cd, B:48:0x0125, B:49:0x012a, B:36:0x00d2, B:38:0x00d8, B:52:0x0130, B:89:0x02a6, B:90:0x02ab, B:55:0x0139, B:72:0x01e4, B:98:0x02b9, B:99:0x02bc, B:83:0x0267, B:85:0x026b, B:78:0x01f2, B:81:0x0205, B:82:0x0217, B:26:0x0087, B:29:0x0094, B:32:0x00b9, B:34:0x00ca, B:39:0x00db, B:42:0x00e8, B:45:0x0115, B:51:0x012d, B:57:0x014c, B:59:0x0152, B:71:0x01df, B:95:0x02b2, B:96:0x02b7, B:13:0x004d, B:15:0x0053, B:17:0x0066), top: B:101:0x0001, inners: #2, #3, #4, #6 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized int mergeUsers(java.util.Collection<co.vine.android.api.VineUser> r35, int r36, java.lang.String r37, int r38, int r39, co.vine.android.util.LongSparseArray<java.lang.Long> r40, co.vine.android.util.LongSparseArray<java.lang.Long> r41, long r42, int r44) {
        /*
            Method dump skipped, instructions count: 701
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.provider.VineDatabaseHelper.mergeUsers(java.util.Collection, int, java.lang.String, int, int, co.vine.android.util.LongSparseArray, co.vine.android.util.LongSparseArray, long, int):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0083 A[Catch: all -> 0x0035, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:4:0x001d, B:6:0x0023, B:11:0x0038, B:22:0x0078, B:20:0x0074, B:21:0x0077, B:23:0x007b, B:25:0x0083, B:35:0x00c1, B:47:0x0110, B:48:0x0115, B:36:0x00c6, B:38:0x00cc, B:51:0x011b, B:64:0x016f, B:65:0x0174, B:58:0x0140, B:60:0x0144, B:57:0x0136, B:56:0x0128, B:13:0x004d, B:15:0x0053, B:17:0x0069, B:39:0x00cf, B:40:0x00dc, B:42:0x00e2, B:50:0x0118, B:26:0x0086, B:27:0x0093, B:29:0x0099, B:32:0x00ba, B:34:0x00be), top: B:68:0x0001, inners: #0, #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c6 A[Catch: all -> 0x0035, PHI: r13
  0x00c6: PHI (r13v1 'numInserted' int) = (r13v0 'numInserted' int), (r13v2 'numInserted' int) binds: [B:24:0x0081, B:35:0x00c1] A[DONT_GENERATE, DONT_INLINE], TryCatch #1 {, blocks: (B:3:0x0001, B:4:0x001d, B:6:0x0023, B:11:0x0038, B:22:0x0078, B:20:0x0074, B:21:0x0077, B:23:0x007b, B:25:0x0083, B:35:0x00c1, B:47:0x0110, B:48:0x0115, B:36:0x00c6, B:38:0x00cc, B:51:0x011b, B:64:0x016f, B:65:0x0174, B:58:0x0140, B:60:0x0144, B:57:0x0136, B:56:0x0128, B:13:0x004d, B:15:0x0053, B:17:0x0069, B:39:0x00cf, B:40:0x00dc, B:42:0x00e2, B:50:0x0118, B:26:0x0086, B:27:0x0093, B:29:0x0099, B:32:0x00ba, B:34:0x00be), top: B:68:0x0001, inners: #0, #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00cc A[Catch: all -> 0x0035, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:4:0x001d, B:6:0x0023, B:11:0x0038, B:22:0x0078, B:20:0x0074, B:21:0x0077, B:23:0x007b, B:25:0x0083, B:35:0x00c1, B:47:0x0110, B:48:0x0115, B:36:0x00c6, B:38:0x00cc, B:51:0x011b, B:64:0x016f, B:65:0x0174, B:58:0x0140, B:60:0x0144, B:57:0x0136, B:56:0x0128, B:13:0x004d, B:15:0x0053, B:17:0x0069, B:39:0x00cf, B:40:0x00dc, B:42:0x00e2, B:50:0x0118, B:26:0x0086, B:27:0x0093, B:29:0x0099, B:32:0x00ba, B:34:0x00be), top: B:68:0x0001, inners: #0, #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0128 A[Catch: all -> 0x0035, TryCatch #1 {, blocks: (B:3:0x0001, B:4:0x001d, B:6:0x0023, B:11:0x0038, B:22:0x0078, B:20:0x0074, B:21:0x0077, B:23:0x007b, B:25:0x0083, B:35:0x00c1, B:47:0x0110, B:48:0x0115, B:36:0x00c6, B:38:0x00cc, B:51:0x011b, B:64:0x016f, B:65:0x0174, B:58:0x0140, B:60:0x0144, B:57:0x0136, B:56:0x0128, B:13:0x004d, B:15:0x0053, B:17:0x0069, B:39:0x00cf, B:40:0x00dc, B:42:0x00e2, B:50:0x0118, B:26:0x0086, B:27:0x0093, B:29:0x0099, B:32:0x00ba, B:34:0x00be), top: B:68:0x0001, inners: #0, #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0144 A[Catch: all -> 0x0035, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:4:0x001d, B:6:0x0023, B:11:0x0038, B:22:0x0078, B:20:0x0074, B:21:0x0077, B:23:0x007b, B:25:0x0083, B:35:0x00c1, B:47:0x0110, B:48:0x0115, B:36:0x00c6, B:38:0x00cc, B:51:0x011b, B:64:0x016f, B:65:0x0174, B:58:0x0140, B:60:0x0144, B:57:0x0136, B:56:0x0128, B:13:0x004d, B:15:0x0053, B:17:0x0069, B:39:0x00cf, B:40:0x00dc, B:42:0x00e2, B:50:0x0118, B:26:0x0086, B:27:0x0093, B:29:0x0099, B:32:0x00ba, B:34:0x00be), top: B:68:0x0001, inners: #0, #2, #3 }] */
    @Override // co.vine.android.service.VineDatabaseHelperInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized int mergeSearchedTags(java.util.Collection<co.vine.android.model.VineTag> r21, int r22, int r23) {
        /*
            Method dump skipped, instructions count: 373
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.provider.VineDatabaseHelper.mergeSearchedTags(java.util.Collection, int, int):int");
    }

    public synchronized int mergeChannels(Collection<VineChannel> channels, int next, int previous) {
        int inserted;
        SQLiteDatabase db = getWritableDatabase();
        inserted = 0;
        if (!channels.isEmpty()) {
            db.beginTransaction();
            try {
                db.delete("channels", null, null);
                ContentValues values = new ContentValues();
                for (VineChannel channel : channels) {
                    values.clear();
                    fillChannelValues(values, channel);
                    inserted += db.insert("channels", null, values) > 0 ? 1 : 0;
                }
                db.setTransactionSuccessful();
            } finally {
                safeFinishTransaction(db);
            }
        }
        if (next > 0 || previous > 0) {
            savePageCursor(2, 0, null, next, previous, null, true);
        }
        if (inserted > 0) {
            if (LOGGABLE) {
                Log.d("VineDH", "Inserted " + inserted + " new channels.");
            }
            this.mContentResolver.notifyChange(Vine.Channels.CONTENT_URI, null);
        }
        return inserted;
    }

    private synchronized void mergeUserGroups(Collection<VineUser> users, int type, String groupTag, LongSparseArray<Long> tagMap, LongSparseArray<Long> orderMap) {
        Cursor cursor;
        if (users != null && type != -1) {
            if (LOGGABLE) {
                Log.d("VineDH", "mergeUserGroups: " + users.size() + ", tagged by: " + groupTag + ", of type: " + type);
            }
            LinkedHashMap<Long, VineUser> newUserGroupsMap = new LinkedHashMap<>();
            for (VineUser user : users) {
                newUserGroupsMap.put(Long.valueOf(user.userId), user);
            }
            SQLiteDatabase db = getWritableDatabase();
            if (groupTag != null) {
                cursor = db.query("user_groups", VineDatabaseSQL.UserGroupsQuery.PROJECTION, "type=? AND tag=?", new String[]{String.valueOf(type), String.valueOf(groupTag)}, null, null, null);
            } else {
                cursor = db.query("user_groups", VineDatabaseSQL.UserGroupsQuery.PROJECTION, "type=?", new String[]{String.valueOf(type)}, null, null, null);
            }
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    try {
                        newUserGroupsMap.remove(Long.valueOf(cursor.getLong(0)));
                    } catch (Throwable th) {
                        cursor.close();
                        throw th;
                    }
                }
                cursor.close();
                if (LOGGABLE) {
                    Log.d("VineDH", "Inserting new user groups: " + newUserGroupsMap.size());
                }
                int newUserGroupsCount = newUserGroupsMap.size();
                if (newUserGroupsCount > 0) {
                    db.beginTransaction();
                    try {
                        ContentValues values = new ContentValues();
                        values.put("type", Integer.valueOf(type));
                        if (groupTag != null) {
                            values.put("tag", groupTag);
                        }
                        boolean useOrdering = orderMap != null;
                        for (VineUser user2 : newUserGroupsMap.values()) {
                            values.put("g_flags", (Integer) 0);
                            values.put("user_id", Long.valueOf(user2.userId));
                            if (useOrdering && orderMap.get(user2.userId) != null) {
                                values.put("order_id", orderMap.get(user2.userId));
                            } else if (tagMap != null && tagMap.get(user2.userId) != null) {
                                values.put("tag", tagMap.get(user2.userId));
                            }
                            db.insert("user_groups", "user_id", values);
                        }
                        db.setTransactionSuccessful();
                        safeFinishTransaction(db);
                        this.mContentResolver.notifyChange(Vine.UserGroupsView.CONTENT_URI, null);
                    } catch (Throwable th2) {
                        safeFinishTransaction(db);
                        throw th2;
                    }
                }
            }
        }
    }

    private synchronized void mergePostGroups(Collection<VinePost> posts, int type, String groupTag, LongSparseArray<Long> orderMap) {
        if (posts != null && type != -1) {
            if (LOGGABLE) {
                Log.d("VineDH", "mergePostGroups: " + posts.size() + ", tagged by: " + groupTag + ", of type: " + type);
            }
            LinkedHashMap<Long, VinePost> newPostGroups = new LinkedHashMap<>();
            for (VinePost post : posts) {
                newPostGroups.put(Long.valueOf(post.postId), post);
            }
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.query("post_groups", VineDatabaseSQL.PostGroupsQuery.PROJECTION, "post_type=?", new String[]{String.valueOf(type)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        newPostGroups.remove(Long.valueOf(cursor.getLong(0)));
                    } finally {
                        cursor.close();
                    }
                } while (cursor.moveToNext());
            }
            int numInserted = 0;
            int newPostGroupsCount = newPostGroups.size();
            if (newPostGroupsCount > 0) {
                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put("post_type", Integer.valueOf(type));
                    values.put("tag", groupTag);
                    for (VinePost post2 : newPostGroups.values()) {
                        values.put("g_flags", (Integer) 0);
                        values.put("post_id", Long.valueOf(post2.postId));
                        values.put("repost_data", Util.toByteArray(post2.repost));
                        if (post2.repost != null) {
                            values.put("reposter_id", Long.valueOf(post2.repost.userId));
                        } else {
                            values.remove("reposter_id");
                        }
                        if (orderMap != null) {
                            values.put("sort_id", orderMap.get(post2.postId));
                        }
                        numInserted += db.insert("post_groups", "post_id", values) > 0 ? 1 : 0;
                    }
                    db.setTransactionSuccessful();
                    safeFinishTransaction(db);
                    this.mContentResolver.notifyChange(Vine.PostGroupsView.CONTENT_URI, null);
                } catch (Throwable th) {
                    safeFinishTransaction(db);
                    throw th;
                }
            }
            if (LOGGABLE) {
                Log.d("VineDH", "Inserted new post groups: " + numInserted + " of type " + type);
            }
        }
    }

    public synchronized boolean removeUserWithType(long userId, int type, boolean notify, Uri uri) {
        boolean z = true;
        synchronized (this) {
            SQLiteDatabase db = getWritableDatabase();
            String[] selArgs = {String.valueOf(type), String.valueOf(userId)};
            int deleted = db.delete("user_groups", "type=? AND user_id=?", selArgs);
            if (deleted > 0) {
                if (notify) {
                    this.mContentResolver.notifyChange(uri, null);
                }
                if (LOGGABLE) {
                    Log.d("VineDH", "Deleted user with userId=" + userId + " of type=" + type);
                }
            } else {
                z = false;
            }
        }
        return z;
    }

    public synchronized void mergeConversationRecipients(long conversationRowId, Collection<Long> userRowIds) {
        int inserted = 0;
        if (LOGGABLE) {
            Log.d("VineDH", "mergeConversations, count=" + userRowIds.size());
        }
        if (!userRowIds.isEmpty()) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                db.beginTransaction();
                ContentValues values = new ContentValues();
                values.put("conversation_row_id", Long.valueOf(conversationRowId));
                for (Long entry : userRowIds) {
                    values.put("user_row_id", entry);
                    inserted += db.insert("conversation_recipients", null, values) > 0 ? 1 : 0;
                }
                db.setTransactionSuccessful();
            } finally {
                safeFinishTransaction(db);
            }
        }
        if (inserted > 0) {
            this.mContentResolver.notifyChange(Vine.InboxView.CONTENT_URI, null);
        }
    }

    public synchronized void mergeInbox(long sessionOwnerId, VinePagedData inbox, int next, int previous, String anchor, int networkType, int userGroup) throws IOException {
        if (inbox.participants != null && !inbox.participants.isEmpty()) {
            mergeUsers(inbox.participants, userGroup, null, 0, 0, null);
        }
        Iterator it = inbox.items.iterator();
        while (it.hasNext()) {
            VineConversation conversation = (VineConversation) it.next();
            mergeConversation(sessionOwnerId, conversation, networkType, 0, 0, null);
        }
        if (next > 0 || previous > 0 || anchor != null) {
            savePageCursor(6, networkType, null, next, previous, anchor, false);
        }
    }

    public synchronized long mergeConversation(long sessionOwnerId, VineConversation conversation, int networkType, int nextPage, int prevPage, String anchor) throws IOException {
        long conversationRowId;
        conversationRowId = mergeMessages(sessionOwnerId, conversation, networkType, nextPage, prevPage, anchor);
        if (conversation.users != null) {
            ArrayList<Long> userRowIds = new ArrayList<>();
            Iterator<Long> it = conversation.users.iterator();
            while (it.hasNext()) {
                long user = it.next().longValue();
                long userRowId = getUserRowIdForUserRemoteId(user);
                if (userRowId > -1) {
                    userRowIds.add(Long.valueOf(userRowId));
                }
            }
            mergeConversationRecipients(conversationRowId, userRowIds);
        }
        return conversationRowId;
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0309 A[Catch: all -> 0x0093, TRY_ENTER, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0030, B:6:0x0038, B:8:0x003e, B:10:0x004c, B:11:0x0052, B:13:0x0062, B:14:0x007d, B:19:0x0096, B:20:0x00ab, B:22:0x00af, B:23:0x00cb, B:25:0x00f0, B:27:0x00f6, B:30:0x0101, B:31:0x0104, B:33:0x0118, B:35:0x011e, B:37:0x0133, B:39:0x0146, B:40:0x0149, B:42:0x0158, B:44:0x015c, B:45:0x0178, B:65:0x020e, B:60:0x01ec, B:61:0x01f1, B:66:0x021b, B:68:0x0221, B:70:0x0225, B:71:0x0241, B:88:0x02bd, B:83:0x029b, B:84:0x02a0, B:99:0x02e9, B:95:0x02ce, B:102:0x0309, B:72:0x0244, B:73:0x0251, B:75:0x0257, B:78:0x0288, B:80:0x0290, B:87:0x02a5, B:46:0x017b, B:47:0x0188, B:49:0x018e, B:52:0x01be, B:54:0x01ce, B:55:0x01db, B:57:0x01e1, B:64:0x01f6), top: B:104:0x0001, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0221 A[Catch: all -> 0x0093, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0030, B:6:0x0038, B:8:0x003e, B:10:0x004c, B:11:0x0052, B:13:0x0062, B:14:0x007d, B:19:0x0096, B:20:0x00ab, B:22:0x00af, B:23:0x00cb, B:25:0x00f0, B:27:0x00f6, B:30:0x0101, B:31:0x0104, B:33:0x0118, B:35:0x011e, B:37:0x0133, B:39:0x0146, B:40:0x0149, B:42:0x0158, B:44:0x015c, B:45:0x0178, B:65:0x020e, B:60:0x01ec, B:61:0x01f1, B:66:0x021b, B:68:0x0221, B:70:0x0225, B:71:0x0241, B:88:0x02bd, B:83:0x029b, B:84:0x02a0, B:99:0x02e9, B:95:0x02ce, B:102:0x0309, B:72:0x0244, B:73:0x0251, B:75:0x0257, B:78:0x0288, B:80:0x0290, B:87:0x02a5, B:46:0x017b, B:47:0x0188, B:49:0x018e, B:52:0x01be, B:54:0x01ce, B:55:0x01db, B:57:0x01e1, B:64:0x01f6), top: B:104:0x0001, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x02c6  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x02ce A[Catch: all -> 0x0093, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0030, B:6:0x0038, B:8:0x003e, B:10:0x004c, B:11:0x0052, B:13:0x0062, B:14:0x007d, B:19:0x0096, B:20:0x00ab, B:22:0x00af, B:23:0x00cb, B:25:0x00f0, B:27:0x00f6, B:30:0x0101, B:31:0x0104, B:33:0x0118, B:35:0x011e, B:37:0x0133, B:39:0x0146, B:40:0x0149, B:42:0x0158, B:44:0x015c, B:45:0x0178, B:65:0x020e, B:60:0x01ec, B:61:0x01f1, B:66:0x021b, B:68:0x0221, B:70:0x0225, B:71:0x0241, B:88:0x02bd, B:83:0x029b, B:84:0x02a0, B:99:0x02e9, B:95:0x02ce, B:102:0x0309, B:72:0x0244, B:73:0x0251, B:75:0x0257, B:78:0x0288, B:80:0x0290, B:87:0x02a5, B:46:0x017b, B:47:0x0188, B:49:0x018e, B:52:0x01be, B:54:0x01ce, B:55:0x01db, B:57:0x01e1, B:64:0x01f6), top: B:104:0x0001, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x02e9 A[Catch: all -> 0x0093, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0030, B:6:0x0038, B:8:0x003e, B:10:0x004c, B:11:0x0052, B:13:0x0062, B:14:0x007d, B:19:0x0096, B:20:0x00ab, B:22:0x00af, B:23:0x00cb, B:25:0x00f0, B:27:0x00f6, B:30:0x0101, B:31:0x0104, B:33:0x0118, B:35:0x011e, B:37:0x0133, B:39:0x0146, B:40:0x0149, B:42:0x0158, B:44:0x015c, B:45:0x0178, B:65:0x020e, B:60:0x01ec, B:61:0x01f1, B:66:0x021b, B:68:0x0221, B:70:0x0225, B:71:0x0241, B:88:0x02bd, B:83:0x029b, B:84:0x02a0, B:99:0x02e9, B:95:0x02ce, B:102:0x0309, B:72:0x0244, B:73:0x0251, B:75:0x0257, B:78:0x0288, B:80:0x0290, B:87:0x02a5, B:46:0x017b, B:47:0x0188, B:49:0x018e, B:52:0x01be, B:54:0x01ce, B:55:0x01db, B:57:0x01e1, B:64:0x01f6), top: B:104:0x0001, inners: #1, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized long mergeMessages(long r50, co.vine.android.api.VineConversation r52, int r53, int r54, int r55, java.lang.String r56) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 819
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.provider.VineDatabaseHelper.mergeMessages(long, co.vine.android.api.VineConversation, int, int, int, java.lang.String):long");
    }

    public synchronized void updateConversationWithLastMessage(long conversationRowId, long messageRowId, long created) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("last_message", Long.valueOf(messageRowId));
            values.put("last_message_timestamp", Long.valueOf(created));
            int updated = db.update("conversations", values, "_id=? ", new String[]{String.valueOf(conversationRowId)});
            db.setTransactionSuccessful();
            if (updated > 0) {
                this.mContentResolver.notifyChange(Vine.InboxView.CONTENT_URI, null);
                this.mContentResolver.notifyChange(Vine.Conversations.CONTENT_URI, null);
            }
        } finally {
            safeFinishTransaction(db);
        }
    }

    public synchronized long mergeConversationWithLocalId(long conversationRowId, long conversationId, int networkType) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        fillConversationValues(values, Long.valueOf(conversationId), Integer.valueOf(networkType), null, 0L, 0L);
        try {
            db.beginTransaction();
            int updated = db.update("conversations", values, "_id=?", new String[]{String.valueOf(conversationRowId)});
            if (updated <= 0) {
                conversationRowId = db.insert("conversations", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            safeFinishTransaction(db);
        }
        return conversationRowId;
    }

    public synchronized long mergeConversationWithRemoteId(long conversationId, Integer networkType, Boolean hidden, long unreadMessageCount, long lastMessage) {
        long id;
        SQLiteDatabase db = getWritableDatabase();
        String[] selArgs = {String.valueOf(conversationId)};
        try {
            db.beginTransaction();
            Cursor cursor = db.query("conversations", new String[]{"_id"}, "conversation_id=?", selArgs, null, null, "1");
            id = -1;
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        ContentValues values = new ContentValues();
                        fillConversationValues(values, Long.valueOf(conversationId), networkType, hidden, unreadMessageCount, lastMessage);
                        db.update("conversations", values, "conversation_id=?", new String[]{String.valueOf(conversationId)});
                        id = cursor.getLong(0);
                    }
                } finally {
                    cursor.close();
                }
            }
            if (id == -1) {
                ContentValues values2 = new ContentValues();
                fillConversationValues(values2, Long.valueOf(conversationId), networkType, hidden, unreadMessageCount, lastMessage);
                id = db.insert("conversations", null, values2);
                if (id == -1) {
                    throw new RuntimeException("Failed to insert conversation.");
                }
            }
            db.setTransactionSuccessful();
        } finally {
            safeFinishTransaction(db);
        }
        return id;
    }

    public synchronized long mergeMessageWithMessageRow(long messageRowId, long conversationRowId, VinePrivateMessage message) {
        long inserted;
        SQLiteDatabase db = getWritableDatabase();
        inserted = -1;
        ContentValues values = new ContentValues();
        fillMessageValues(values, conversationRowId, message);
        try {
            db.beginTransaction();
            long updated = db.update("messages", values, "_id=?", new String[]{String.valueOf(messageRowId)});
            db.setTransactionSuccessful();
            safeFinishTransaction(db);
            if (updated <= 0) {
                values.put("deleted", "0");
                inserted = db.insert("messages", null, values);
            }
            if (inserted > 0 || updated > 0) {
                this.mContentResolver.notifyChange(Vine.ConversationMessageUsersView.CONTENT_URI, null);
                this.mContentResolver.notifyChange(Vine.InboxView.CONTENT_URI, null);
            }
            if (inserted <= 0) {
                inserted = messageRowId;
            }
        } catch (Throwable th) {
            safeFinishTransaction(db);
            throw th;
        }
        return inserted;
    }

    public synchronized long mergeMessage(long conversationRowId, VinePrivateMessage message) {
        long inserted;
        SQLiteDatabase db = getWritableDatabase();
        inserted = -1;
        long updated = -1;
        ContentValues values = new ContentValues();
        fillMessageValues(values, conversationRowId, message);
        if (message.messageId > 0) {
            String[] selArgs = {String.valueOf(message.messageId)};
            Cursor cursor = this.mContentResolver.query(Vine.Messages.CONTENT_URI, VineDatabaseSQL.MessagesQuery.PROJECTION, "message_id=?", selArgs, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    try {
                        db.beginTransaction();
                        updated = db.update("messages", values, "message_id=?", new String[]{String.valueOf(message.messageId)});
                        db.setTransactionSuccessful();
                    } finally {
                        safeFinishTransaction(db);
                    }
                }
                cursor.close();
            }
        }
        if (updated <= 0) {
            values.put("deleted", "0");
            inserted = db.insert("messages", null, values);
        }
        if (inserted > 0 || updated > 0) {
            this.mContentResolver.notifyChange(Vine.ConversationMessageUsersView.CONTENT_URI, null);
            this.mContentResolver.notifyChange(Vine.InboxView.CONTENT_URI, null);
        }
        return inserted;
    }

    public synchronized void removeUsersByGroup(int type) {
        SQLiteDatabase db = getWritableDatabase();
        int deleted = db.delete("user_groups", "type=?", new String[]{String.valueOf(type)});
        if (LOGGABLE) {
            Log.d("VineDH", "Deleted " + deleted + " users of type " + type);
        }
    }

    public synchronized void removePostsByReposterId(long reposterId) {
        SQLiteDatabase db = getWritableDatabase();
        String[] selArgs = {String.valueOf(reposterId), String.valueOf(1)};
        int deleted = db.delete("post_groups", "reposter_id=? AND post_type =?", selArgs);
        if (LOGGABLE) {
            Log.d("VineDH", "Deleted " + deleted + " posts with reposterId=" + reposterId);
        }
        if (deleted > 0) {
            this.mContentResolver.notifyChange(Vine.PostGroupsView.CONTENT_URI, null);
            this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI, null);
            this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI_ALL_TIMELINE, null);
        }
    }

    public synchronized void deleteConversation(long conversationId, long conversationRowId) {
        SQLiteDatabase db = getWritableDatabase();
        int deleted = 0;
        try {
            db.beginTransaction();
            if (conversationId > 0) {
                deleted = db.delete("conversations", "conversation_id=?", new String[]{String.valueOf(conversationId)});
            }
            if (conversationRowId > 0) {
                deleted = deleted + db.delete("conversations", "_id=?", new String[]{String.valueOf(conversationRowId)}) + db.delete("messages", "conversation_row_id=?", new String[]{String.valueOf(conversationRowId)}) + db.delete("conversation_recipients", "conversation_row_id=?", new String[]{String.valueOf(conversationRowId)});
            }
            db.setTransactionSuccessful();
            safeFinishTransaction(db);
            if (LOGGABLE) {
                Log.d("VineDH", "Deleted conversation " + conversationId);
            }
            if (deleted > 0) {
                notifyVMUris();
            }
        } catch (Throwable th) {
            safeFinishTransaction(db);
            throw th;
        }
    }

    private void notifyVMUris() {
        this.mContentResolver.notifyChange(Vine.InboxView.CONTENT_URI, null);
        this.mContentResolver.notifyChange(Vine.ConversationMessageUsersView.CONTENT_URI, null);
        this.mContentResolver.notifyChange(Vine.Messages.CONTENT_URI, null);
        this.mContentResolver.notifyChange(Vine.Conversations.CONTENT_URI, null);
        this.mContentResolver.notifyChange(Vine.ConversationRecipients.CONTENT_URI, null);
    }

    public synchronized void markLastUser(int type, String tag) {
        markLastUser(type, tag, "order_id ASC");
    }

    public synchronized void markLastUser(int type, String tag, String isLastOrdering) {
        long rowId = getLastUserRowId(type, tag, isLastOrdering);
        if (rowId != 0) {
            if (LOGGABLE) {
                Log.d("VineDH", "Tagging the oldest user of type: " + type + " row id: " + rowId);
            }
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("is_last", (Integer) 0);
            try {
                db.beginTransaction();
                db.update("user_groups", values, "is_last=1", null);
                values.put("is_last", (Integer) 1);
                db.update("user_groups", values, "_id=?", new String[]{String.valueOf(rowId)});
                db.setTransactionSuccessful();
                safeFinishTransaction(db);
                this.mContentResolver.notifyChange(Vine.UserGroupsView.CONTENT_URI, null);
            } catch (Throwable th) {
                safeFinishTransaction(db);
                throw th;
            }
        }
    }

    public synchronized void markLastMessage(long conversationRowId) {
        long rowId = getLastMessageRowId(conversationRowId);
        if (rowId != 0) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("is_last", (Integer) 0);
            try {
                db.beginTransaction();
                db.update("messages", values, "is_last=1 AND conversation_row_id=?", new String[]{String.valueOf(conversationRowId)});
                values.put("is_last", (Integer) 1);
                db.update("messages", values, "_id=?", new String[]{String.valueOf(rowId)});
                db.setTransactionSuccessful();
            } finally {
                safeFinishTransaction(db);
            }
        }
    }

    @Override // co.vine.android.service.VineDatabaseHelperInterface
    public synchronized void markLastTag() {
        long rowId = getLastTagRowId();
        if (rowId != 0) {
            if (LOGGABLE) {
                Log.d("VineDH", "Tagging the oldest tag with row id: " + rowId);
            }
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("is_last", (Integer) 0);
            try {
                db.beginTransaction();
                db.update("tag_search_results", values, "is_last=1", null);
                values.put("is_last", (Integer) 1);
                db.update("tag_search_results", values, "_id=?", new String[]{String.valueOf(rowId)});
                db.setTransactionSuccessful();
                this.mContentResolver.notifyChange(Vine.TagSearchResults.CONTENT_URI, null);
            } finally {
                safeFinishTransaction(db);
            }
        }
    }

    public synchronized void markLastChannel() {
        long rowId = getLastChannelRowId();
        if (rowId != 0) {
            if (LOGGABLE) {
                Log.d("VineDH", "Tagging the oldest channel with row id: " + rowId);
            }
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("is_last", (Integer) 0);
            try {
                db.beginTransaction();
                db.update("channels", values, "is_last=1", null);
                values.put("is_last", (Integer) 1);
                db.update("channels", values, "_id=?", new String[]{String.valueOf(rowId)});
                safeFinishTransaction(db);
                this.mContentResolver.notifyChange(Vine.Channels.CONTENT_URI, null);
            } catch (Throwable th) {
                safeFinishTransaction(db);
                throw th;
            }
        }
    }

    public synchronized void markChannelLastUsedTimestamp(long channelId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("last_used_timestamp", Long.valueOf(System.currentTimeMillis()));
        db.update("channels", values, "channel_id=?", new String[]{String.valueOf(channelId)});
    }

    public synchronized void markLastConversation(int networkType) {
        long rowId = getLastConversationId(networkType);
        if (rowId != 0) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("is_last", (Integer) 0);
            try {
                db.beginTransaction();
                db.update("conversations", values, "is_last=1 AND network_type=?", new String[]{String.valueOf(networkType)});
                values.put("is_last", (Integer) 1);
                db.update("conversations", values, "_id=?", new String[]{String.valueOf(rowId)});
                db.setTransactionSuccessful();
            } finally {
                safeFinishTransaction(db);
            }
        }
    }

    public synchronized int saveUserValue(String propName, ContentValues values) {
        int i = 0;
        synchronized (this) {
            SQLiteDatabase db = getWritableDatabase();
            if (db.update("settings", values, "name=?", new String[]{propName}) == 0) {
                values.put("name", propName);
                if (db.insert("settings", "name", values) != 0) {
                }
            }
            if (this.mContentResolver == null) {
                this.mContentResolver = this.mAppContext.getContentResolver();
            }
            this.mContentResolver.notifyChange(Vine.Settings.CONTENT_URI, null);
            i = 1;
        }
        return i;
    }

    public synchronized int savePageCursor(int kind, int type, String tag, int nextPage, int prevPage, String anchor, boolean reversed) {
        int num;
        SQLiteDatabase db = getWritableDatabase();
        if (tag == null) {
            tag = "";
        }
        String[] selectionArgs = {String.valueOf(kind), String.valueOf(type), tag};
        Cursor c = db.query("page_cursors", VineDatabaseSQL.PageCursorsQuery.PROJECTION, "kind=? AND type=? AND tag=?", selectionArgs, null, null, null);
        ContentValues values = new ContentValues();
        values.put("kind", Integer.valueOf(kind));
        values.put("type", Integer.valueOf(type));
        values.put("tag", tag);
        values.put("next_page", Integer.valueOf(nextPage));
        values.put("previous_page", Integer.valueOf(prevPage));
        values.put("anchor", anchor);
        values.put("reversed", Boolean.valueOf(reversed));
        if (c == null) {
            num = 0 + (db.insert("page_cursors", "_id", values) > 0 ? 1 : 0);
        } else {
            try {
                if (c.getCount() == 1) {
                    num = 0 + (db.update("page_cursors", values, "kind=? AND type=? AND tag=?", selectionArgs) > 0 ? 1 : 0);
                } else {
                    num = 0 + (db.insert("page_cursors", "_id", values) > 0 ? 1 : 0);
                }
            } finally {
                c.close();
            }
        }
        if (LOGGABLE && num > 0) {
            Log.d("VineDH", "Saved pagecursor of kind: " + kind + " type: " + type + " tag: " + tag + " next:" + nextPage + " prev: " + prevPage + " anchor: " + anchor + " reverse: " + reversed);
        }
        return num;
    }

    public synchronized int getNextPageCursor(int kind, int type, String tag, boolean reversed) {
        String column;
        int result;
        SQLiteDatabase db = getReadableDatabase();
        if (reversed) {
            column = "previous_page";
        } else {
            column = "next_page";
        }
        if (tag == null) {
            tag = "";
        }
        Cursor c = db.query("page_cursors", new String[]{column}, "kind=? AND type=? AND tag=?", new String[]{String.valueOf(kind), String.valueOf(type), String.valueOf(tag)}, null, null, null);
        result = -1;
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    result = c.getInt(0);
                }
            } finally {
                c.close();
            }
        }
        return result;
    }

    public synchronized int getPreviousPageCursor(int kind, int type, String tag, boolean reversed) {
        String column;
        int result;
        SQLiteDatabase db = getReadableDatabase();
        if (reversed) {
            column = "next_page";
        } else {
            column = "previous_page";
        }
        if (tag == null) {
            tag = "";
        }
        Cursor c = db.query("page_cursors", new String[]{column}, "kind=? AND type=? AND tag=?", new String[]{String.valueOf(kind), String.valueOf(type), String.valueOf(tag)}, null, null, null);
        result = -1;
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    int index = c.getColumnIndex(column);
                    result = c.getInt(index);
                }
            } finally {
                c.close();
            }
        }
        return result;
    }

    @Override // co.vine.android.service.VineDatabaseHelperInterface
    public synchronized void clearAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("settings", null, null);
        db.delete("user_groups", null, null);
        db.delete("posts", null, null);
        db.delete("likes", null, null);
        db.delete("post_groups", null, null);
        db.delete("comments", null, null);
        db.delete("page_cursors", null, null);
        db.delete("tag_search_results", null, null);
        db.delete("channels", null, null);
        db.delete("notifications", null, null);
        db.delete("tag_recently_used", null, null);
        db.delete("messages", null, null);
        db.delete("conversation_recipients", null, null);
        db.delete("conversations", null, null);
    }

    public synchronized void clearCachedData() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete("users", null, null);
            db.delete("user_groups", null, null);
            db.delete("posts", null, null);
            db.delete("likes", null, null);
            db.delete("post_groups", null, null);
            db.delete("comments", null, null);
            db.delete("page_cursors", null, null);
            db.delete("tag_search_results", null, null);
            db.delete("channels", null, null);
            db.delete("notifications", null, null);
            db.delete("tag_recently_used", null, null);
            db.delete("messages", null, null);
            db.delete("conversation_recipients", null, null);
            db.delete("conversations", null, null);
            db.setTransactionSuccessful();
        } finally {
            safeFinishTransaction(db);
        }
    }

    public synchronized void clearCachedData(boolean notify) {
        clearCachedData();
        if (notify) {
            notifyPostCommentsViewUris();
        }
    }

    private long getLastUserRowId(int type, String tag, String isLastOrdering) {
        String selection;
        String[] selectionArgs;
        SQLiteDatabase db = getReadableDatabase();
        if (tag != null) {
            selection = "type=? AND tag=?";
            selectionArgs = new String[]{String.valueOf(type), tag};
        } else {
            selection = "type=?";
            selectionArgs = new String[]{String.valueOf(type)};
        }
        Cursor cursor = db.query("user_groups_view", VineDatabaseSQL.UsersQuery.PROJECTION, selection, selectionArgs, null, null, isLastOrdering, "1");
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getLong(0);
                }
            } finally {
                cursor.close();
            }
        }
        return 0L;
    }

    private long getLastMessageRowId(long conversationRowId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("messages", VineDatabaseSQL.MessagesQuery.PROJECTION, "conversation_row_id =? AND deleted != 1 ", new String[]{String.valueOf(conversationRowId)}, null, null, "message_id ASC", "1");
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getLong(0);
                }
            } finally {
                cursor.close();
            }
        }
        return 0L;
    }

    private long getLastTagRowId() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("tag_search_results", VineDatabaseSQL.TagsQuery.PROJECTION, null, null, null, null, "_id DESC", "1");
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getLong(0);
                }
            } finally {
                cursor.close();
            }
        }
        return 0L;
    }

    private long getLastChannelRowId() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("channels", VineDatabaseSQL.ChannelsQuery.PROJECTION, null, null, null, null, "channel_id DESC", "1");
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getLong(0);
                }
            } finally {
                cursor.close();
            }
        }
        return 0L;
    }

    private long getLastConversationId(int networkType) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("conversations", new String[]{"_id"}, "last_message>0 AND network_type =? ", new String[]{String.valueOf(networkType)}, null, null, "last_message_timestamp ASC, _id DESC", "1");
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getLong(0);
                }
            } finally {
                cursor.close();
            }
        }
        return 0L;
    }

    public boolean setShouldHideProfileRevines(long userId, boolean hide) {
        SQLiteDatabase db = getWritableDatabase();
        String[] selArgs = {String.valueOf(userId)};
        ContentValues values = new ContentValues();
        values.put("hide_profile_reposts", Integer.valueOf(hide ? 1 : 0));
        db.beginTransaction();
        try {
            int updated = db.update("users", values, "user_id=?", selArgs);
            db.setTransactionSuccessful();
            return updated > 0;
        } finally {
            safeFinishTransaction(db);
        }
    }

    private void fillUserContentValues(ContentValues values, VineUser user, long lastRefresh) {
        values.put("user_id", Long.valueOf(user.userId));
        values.put("avatar_url", user.avatarUrl);
        values.put("username", user.username);
        values.put("blocked", Integer.valueOf(user.blocked));
        values.put("blocking", Integer.valueOf(user.blocking));
        values.put("description", user.description);
        values.put("location", user.location);
        values.put("explicit", Integer.valueOf(user.explicit));
        values.put("external", Integer.valueOf(user.external ? 1 : 0));
        values.put("follower_count", Integer.valueOf(user.followerCount));
        values.put("following_count", Integer.valueOf(user.followingCount));
        if (!TextUtils.isEmpty(user.email)) {
            values.put("email_address", user.email);
        }
        if (!TextUtils.isEmpty(user.phoneNumber)) {
            values.put("phone_number", user.phoneNumber);
        }
        if (user.following != -1) {
            values.put("following_flag", Integer.valueOf(user.following));
        }
        values.put("like_count", Integer.valueOf(user.likeCount));
        values.put("post_count", Integer.valueOf(user.postCount));
        values.put("loop_count", Long.valueOf(user.loopCount));
        values.put("verified", Integer.valueOf(user.verified));
        values.put("follow_status", Integer.valueOf(user.getFollowStatus()));
        values.put("last_refresh", Long.valueOf(lastRefresh));
        if (user.profileBackground > -1) {
            values.put("profile_background", Integer.valueOf(user.profileBackground));
        }
        values.put("accepts_oon_conversations", Integer.valueOf(user.acceptsOutOfNetworkConversations ? 1 : 0));
        values.put("twitter_screenname", user.twitterScreenname);
        values.put("twitter_hidden", Boolean.valueOf(user.hiddenTwitter));
    }

    private void fillPostValues(ContentValues values, VinePost post, long lastRefresh) throws IOException {
        values.put("avatar_url", post.avatarUrl);
        values.put("description", post.description);
        values.put("foursquare_venue_id", post.foursquareVenueId);
        values.put("metadata_flags", Integer.valueOf(post.metadataFlags));
        values.put("location", post.location);
        values.put("post_flags", Integer.valueOf(post.postFlags));
        values.put("post_id", Long.valueOf(post.postId));
        values.put("my_repost_id", Long.valueOf(post.myRepostId));
        values.put("share_url", post.shareUrl);
        values.put("thumbnail_url", post.thumbnailUrl);
        values.put("video_url", post.videoUrl);
        values.put("video_low_uRL", post.videoLowUrl);
        values.put("timestamp", Long.valueOf(post.created));
        values.put("username", post.username);
        values.put("user_id", Long.valueOf(post.userId));
        values.put("likes_count", Integer.valueOf(post.likesCount));
        values.put("reviners_count", Integer.valueOf(post.revinersCount));
        values.put("comments_count", Integer.valueOf(post.commentsCount));
        values.put("user_background_color", Integer.valueOf(post.userBackgroundColor));
        values.put("tags", VinePost.getBytesFromTags(post));
        values.put("loops", Long.valueOf(post.loops));
        values.put("velocity", Double.valueOf(post.velocity));
        values.put("on_fire", Boolean.valueOf(post.onFire));
        if (post.venueData != null) {
            values.put("venue_data", Util.toByteArray(post.venueData));
        }
        if (post.entities != null) {
            values.put("entities", Util.toByteArray(post.entities));
        }
        values.put("last_refresh", Long.valueOf(lastRefresh));
    }

    private void fillLikeValues(ContentValues values, VineLike like, long lastRefresh) {
        values.put("like_id", Long.valueOf(like.likeId));
        values.put("user_id", Long.valueOf(like.userId));
        values.put("post_id", Long.valueOf(like.postId));
        values.put("avatar_url", like.avatarUrl);
        values.put("timestamp", Long.valueOf(like.created));
        values.put("location", like.location);
        values.put("username", like.username);
        values.put("verified", Integer.valueOf(like.verified));
        values.put("last_refresh", Long.valueOf(lastRefresh));
    }

    private void fillNotificationValues(ContentValues values, VineSingleNotification notification, String message) {
        values.put("avatar_url", notification.avatarUrl);
        values.put("notification_id", Long.valueOf(notification.getNotificationId()));
        values.put("notification_type", Integer.valueOf(notification.notificationTypeId));
        values.put("conversation_row_id", Long.valueOf(notification.conversationRowId));
        values.put("message", message);
    }

    private void fillConversationValues(ContentValues values, Long conversationId, Integer networkType, Boolean hidden, long unreadMessageCount, long lastMessage) {
        if (conversationId != null) {
            values.put("conversation_id", conversationId);
        }
        if (networkType != null) {
            values.put("network_type", networkType);
        }
        if (hidden != null) {
            values.put("is_hidden", Integer.valueOf(hidden.booleanValue() ? 1 : 0));
        }
        if (unreadMessageCount > -1) {
            values.put("unread_message_count", Long.valueOf(unreadMessageCount));
        }
        if (lastMessage > 0) {
            values.put("last_message", Long.valueOf(lastMessage));
        }
    }

    private void fillMessageValues(ContentValues values, long conversationRowId, VinePrivateMessage message) {
        values.put("conversation_row_id", Long.valueOf(conversationRowId));
        values.put("message_id", Long.valueOf(message.messageId));
        values.put("user_row_id", Long.valueOf(message.userId));
        values.put("message", message.message);
        values.put("timestamp", Long.valueOf(message.created));
        if (!TextUtils.isEmpty(message.videoUrl)) {
            values.put("video_url", message.videoUrl);
        }
        if (!TextUtils.isEmpty(message.thumbnailUrl)) {
            values.put("thumbnail_url", message.thumbnailUrl);
        }
        values.put("is_last", Boolean.valueOf(message.isLast));
        values.put("post_id", Long.valueOf(message.postId));
        values.put("error_code", Integer.valueOf(message.errorCode));
        values.put("error_reason", message.errorReason);
        if (!TextUtils.isEmpty(message.uploadPath)) {
            values.put("upload_path", message.uploadPath);
        }
    }

    private void fillTagValues(ContentValues values, VineTag tag) {
        values.put("tag_id", Long.valueOf(tag.getTagId()));
        values.put("tag_name", tag.getTagName());
        values.put("last_used_timestamp", Long.valueOf(System.currentTimeMillis()));
    }

    private void fillChannelValues(ContentValues values, VineChannel channel) {
        values.put("channel_id", Long.valueOf(channel.channelId));
        values.put("channel", channel.channel);
        values.put("background_color", channel.backgroundColor);
        values.put("font_color", channel.fontColor);
        values.put("timestamp", Long.valueOf(channel.created));
        values.put("icon_full_url", channel.iconFullUrl);
        values.put("retina_icon_full_url", channel.retinaIconFullUrl);
        values.put("is_last", (Boolean) false);
        values.put("following", Boolean.valueOf(channel.following));
    }

    public static String getDatabaseName(int schemaVersion) {
        return "vine-" + schemaVersion;
    }

    private void notifyPostCommentsViewUris() {
        this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI_ALL_TIMELINE, null);
        this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI_ALL_TIMELINE_USER, null);
        this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI_ALL_TIMELINE_USER_LIKES, null);
        this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI_ALL_TIMELINE_SINGLE, null);
    }

    private void notifyByType(int groupType) {
        switch (groupType) {
            case 4:
                this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI_ALL_TIMELINE_ON_THE_RISE, null);
                break;
            case 5:
                this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI_ALL_TIMELINE_POPULAR_NOW, null);
                break;
            case 6:
                this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI_ALL_TIMELINE_TAG, null);
                break;
            case 7:
            default:
                notifyPostCommentsViewUris();
                break;
            case 8:
                this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI_ALL_TIMELINE_CHANNEL_POPULAR, null);
                break;
            case 9:
                this.mContentResolver.notifyChange(Vine.PostCommentsLikesView.CONTENT_URI_ALL_TIMELINE_CHANNEL_RECENT, null);
                break;
        }
    }

    public void safeFinishTransaction(SQLiteDatabase db) {
        if (db.inTransaction()) {
            try {
                db.endTransaction();
            } catch (SQLiteDiskIOException e) {
                throw e;
            } catch (SQLiteFullException e2) {
                long currentSize = -1;
                try {
                    currentSize = Util.getCacheSize(this.mAppContext);
                } catch (VineLoggingException e3) {
                    CrashUtil.logException(e2);
                }
                AppController.clearFileCache(this.mAppContext);
                try {
                    CrashUtil.logException(new VineLoggingException(), "Db full, cleaned cache from {} to {}.", Long.valueOf(currentSize), Long.valueOf(Util.getCacheSize(this.mAppContext)));
                } catch (VineLoggingException e4) {
                    CrashUtil.logException(new VineLoggingException(), "Db full, cleaned cache from {} to unknown.", Long.valueOf(currentSize));
                }
                if (db.inTransaction()) {
                    db.endTransaction();
                }
            } catch (SQLiteException e5) {
                if (!e5.getMessage().contains("(code 1)")) {
                    throw e5;
                }
            } catch (IllegalStateException e6) {
                if (!e6.getMessage().contains("already-closed")) {
                    throw e6;
                }
            }
        }
    }

    public synchronized void mergeEditions(VineEditions items) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("editions", null, null);
        if (items != null && items.editions != null && items.editions.size() != 0) {
            ArrayList<VineEditions.Edition> editions = items.editions;
            int numInserted = 0;
            if (!editions.isEmpty()) {
                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    for (int i = 0; i < editions.size(); i++) {
                        values.put("edition_code", editions.get(i).editionId);
                        values.put("edition_name", editions.get(i).name);
                        numInserted = (int) (numInserted + db.insert("editions", null, values));
                    }
                    db.setTransactionSuccessful();
                } finally {
                    safeFinishTransaction(db);
                }
            }
            if (numInserted > 0) {
                this.mContentResolver.notifyChange(Vine.Editions.CONTENT_URI, null);
            }
        }
    }

    public synchronized long getConversationRemoteId(long conversationRowId) {
        long id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("conversations", new String[]{"conversation_id"}, "_id =?", new String[]{String.valueOf(conversationRowId)}, null, null, null, "1");
        if (c != null) {
            id = -1;
            if (c.moveToFirst()) {
                id = c.getLong(0);
            }
            c.close();
        } else {
            id = -1;
        }
        return id;
    }

    public synchronized long getConversationRowId(long conversationRemoteId) {
        long id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("conversations", new String[]{"_id"}, "conversation_id =?", new String[]{String.valueOf(conversationRemoteId)}, null, null, null, "1");
        if (c != null) {
            id = -1;
            if (c.moveToFirst()) {
                id = c.getLong(0);
            }
            c.close();
        } else {
            id = -1;
        }
        return id;
    }

    public synchronized long getLastMessageIdInConversation(long conversationRowId) {
        long ret;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("messages", new String[]{"message_id"}, "conversation_row_id =? ", new String[]{String.valueOf(conversationRowId)}, null, null, "message_id DESC", "1");
        ret = -1;
        if (c != null) {
            if (c.moveToFirst()) {
                ret = c.getLong(0);
            }
            c.close();
        }
        return ret;
    }

    public synchronized void clearUnreadCount(long conversationRowId) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues(1);
            values.put("unread_message_count", (Integer) 0);
            String[] whereArgs = {String.valueOf(conversationRowId)};
            int updated = db.update("conversations", values, "_id=?", whereArgs);
            db.setTransactionSuccessful();
            if (updated > 0) {
                this.mContentResolver.notifyChange(Vine.ConversationMessageUsersView.CONTENT_URI, null);
                this.mContentResolver.notifyChange(Vine.InboxView.CONTENT_URI, null);
            }
        } finally {
            safeFinishTransaction(db);
        }
    }

    public synchronized void deleteMessage(long messageId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("deleted", (Integer) 1);
        String messageIdString = String.valueOf(messageId);
        int updated = db.update("messages", values, "message_id=?", new String[]{messageIdString});
        Cursor c = db.query("messages", new String[]{"conversation_row_id", "is_last"}, "message_id=?", new String[]{messageIdString}, null, null, null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    long conversationRowId = c.getLong(0);
                    boolean isLast = c.getInt(1) == 1;
                    if (getNumberVisibleMessagesInConversation(conversationRowId, db) == 0) {
                        setConversationHidden(conversationRowId, true);
                    } else if (isLast) {
                        markLastMessage(conversationRowId);
                    }
                }
            } finally {
                c.close();
            }
        }
        if (updated > 0) {
            notifyVMUris();
        }
    }

    public int getNumberVisibleMessagesInConversation(long conversationRowId, SQLiteDatabase db) {
        int res = 0;
        Cursor c = db.query("messages", new String[]{"_id"}, "conversation_row_id =? AND deleted != 1", new String[]{String.valueOf(conversationRowId)}, null, null, null);
        if (c != null) {
            try {
                res = c.getCount();
            } finally {
                c.close();
            }
        }
        return res;
    }

    public void setConversationHidden(long conversationRowId, boolean hidden) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("is_hidden", Integer.valueOf(hidden ? 1 : 0));
        int updated = db.update("conversations", cv, "_id=?", new String[]{String.valueOf(conversationRowId)});
        Log.d("VineDH", "Marked conversation " + conversationRowId + " hidden value " + hidden + " updated rows: " + updated);
    }

    public long createPreMergeConversationIdAndRecipientsIfNecessary(List<VineRecipient> recipients, int networkType) {
        HashSet<Long> userRowIds = new HashSet<>();
        for (VineRecipient recipient : recipients) {
            if (!recipient.isFromUser()) {
                long recipientId = getUserRowIdForRecipient(recipient);
                if (recipientId <= 0) {
                    String email = null;
                    String phone = null;
                    if (recipient.isFromEmail()) {
                        email = recipient.value;
                    } else {
                        phone = recipient.value;
                    }
                    recipientId = createNewUserFromNonUserIdRecipientInfo(recipient.getDisplay(), phone, email);
                }
                recipient.recipientId = recipientId;
                userRowIds.add(Long.valueOf(recipientId));
            } else {
                userRowIds.add(Long.valueOf(recipient.recipientId));
            }
        }
        return createConversationRowId(userRowIds, networkType);
    }

    public long createNewUserFromNonUserIdRecipientInfo(String display, String phone, String email) {
        VineUser user = new VineUser(display, null, null, null, null, null, -1L, -1L, 0, 0, 0, true, 0, 0, 0, 0, 0, 0, email, phone, 0, 0, 0, 0, null, false, 0, 0, 0, "", false, false, false, false, 0L, false, false, false);
        ContentValues values = new ContentValues();
        fillUserContentValues(values, user, System.currentTimeMillis());
        SQLiteDatabase db = getWritableDatabase();
        return db.insertOrThrow("users", "last_refresh", values);
    }

    public long mergeUserAndGetResultingRowId(VineUser user) {
        ContentValues values = new ContentValues();
        fillUserContentValues(values, user, System.currentTimeMillis());
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("users", new String[]{"_id"}, "user_id=?", new String[]{String.valueOf(user.userId)}, null, null, null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    long updateId = c.getLong(0);
                    db.update("users", values, "_id=?", new String[]{String.valueOf(updateId)});
                    return updateId;
                }
            } finally {
                c.close();
            }
        }
        return db.insertOrThrow("users", null, values);
    }

    public synchronized long createConversationRowId(HashSet<Long> userRowIds, int networkType) {
        long conversationRowId;
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            fillConversationValues(values, -1L, Integer.valueOf(networkType), false, 0L, 0L);
            conversationRowId = db.insert("conversations", null, values);
            db.setTransactionSuccessful();
            safeFinishTransaction(db);
            if (conversationRowId > 0) {
                mergeConversationRecipients(conversationRowId, userRowIds);
                this.mContentResolver.notifyChange(Vine.InboxView.CONTENT_URI, null);
            }
        } catch (Throwable th) {
            safeFinishTransaction(db);
            throw th;
        }
        return conversationRowId;
    }

    public VinePrivateMessage getMessageFromMessageRow(long mergedMessageId) {
        VinePrivateMessage vpm = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = null;
        try {
            c = db.query("message_users_view", VineDatabaseSQL.ConversationMessageUsersQuery.PROJECTION, "_id=?", new String[]{String.valueOf(mergedMessageId)}, null, null, null);
            if (c != null) {
                try {
                    if (c.moveToFirst()) {
                        vpm = new VinePrivateMessage(c);
                    }
                } finally {
                    c.close();
                }
            }
            return vpm;
        } finally {
            if (c != null) {
            }
        }
    }

    public void mergeRecipientsWithUsersAndRemoveUnusedRecipients(long conversationRowId, ArrayList<VineRecipient> recipients, ArrayList<VineUser> users) {
        HashSet<Long> conversationRecipients = new HashSet<>();
        HashMap<Long, VineUser> userMap = new HashMap<>();
        Iterator<VineUser> it = users.iterator();
        while (it.hasNext()) {
            VineUser user = it.next();
            userMap.put(Long.valueOf(user.userId), user);
        }
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("conversation_recipients", new String[]{"user_row_id"}, "conversation_row_id =? ", new String[]{String.valueOf(conversationRowId)}, null, null, null);
        if (c != null) {
            try {
                c.moveToPosition(-1);
                while (c.moveToNext()) {
                    conversationRecipients.add(Long.valueOf(c.getLong(0)));
                }
            } finally {
                c.close();
            }
        }
        try {
            db.beginTransaction();
            Iterator<VineRecipient> it2 = recipients.iterator();
            while (it2.hasNext()) {
                VineRecipient recipient = it2.next();
                if (recipient != null && recipient.userId > 0 && recipient.recipientId > 0) {
                    ContentValues cv = new ContentValues();
                    VineUser user2 = userMap.get(Long.valueOf(recipient.userId));
                    if (user2 != null) {
                        fillUserContentValues(cv, user2, System.currentTimeMillis());
                        db.update("users", cv, "_id=? ", new String[]{String.valueOf(recipient.recipientId)});
                        conversationRecipients.remove(Long.valueOf(recipient.recipientId));
                    }
                }
            }
            db.setTransactionSuccessful();
            safeFinishTransaction(db);
            if (conversationRecipients.size() > 0) {
                try {
                    db.beginTransaction();
                    int i = 0;
                    int size = conversationRecipients.size();
                    String[] recipientsStringArray = new String[size];
                    StringBuilder builder = new StringBuilder();
                    builder.append(" IN (");
                    Iterator<Long> it3 = conversationRecipients.iterator();
                    while (it3.hasNext()) {
                        Long l = it3.next();
                        builder.append("?");
                        recipientsStringArray[i] = l.toString();
                        if (i != size - 1) {
                            builder.append(", ");
                        }
                        i++;
                    }
                    builder.append(")");
                    String inClause = builder.toString();
                    int deleted = db.delete("users", "_id" + inClause, recipientsStringArray);
                    int iDelete = deleted + db.delete("conversation_recipients", "user_row_id" + inClause, recipientsStringArray);
                    db.setTransactionSuccessful();
                } finally {
                }
            }
        } finally {
        }
    }

    public long getUserRowIdForUserRemoteId(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("users", new String[]{"_id"}, "user_id =? ", new String[]{String.valueOf(userId)}, null, null, null);
        long rowId = -1;
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    rowId = c.getLong(0);
                }
            } finally {
                c.close();
            }
        }
        return rowId;
    }

    public long getUserRemoteIdForUserRowId(long userRowId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("users", new String[]{"user_id"}, "_id =? ", new String[]{String.valueOf(userRowId)}, null, null, null);
        long rowId = -1;
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    rowId = c.getLong(0);
                }
            } finally {
                c.close();
            }
        }
        return rowId;
    }

    public long getUserRowIdForRecipient(VineRecipient recipient) {
        Cursor c;
        if (recipient.recipientId > 0) {
            return recipient.recipientId;
        }
        if (recipient.isFromUser() && recipient.userId > 0) {
            return getUserRowIdForUserRemoteId(recipient.userId);
        }
        SQLiteDatabase db = getReadableDatabase();
        String column = null;
        if (recipient.isFromEmail()) {
            column = "email_address";
        } else if (recipient.isFromPhone()) {
            column = "phone_number";
        }
        if (column != null && (c = db.query("users", new String[]{"_id"}, column + "=? AND user_id= -1", new String[]{recipient.value}, null, null, null)) != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getLong(0);
                }
            } finally {
                c.close();
            }
        }
        return -1L;
    }

    public long determineOrCreateBestConversationRowIdForRecipients(List<VineRecipient> recipients, int networkType) {
        long conversationRowId = -1;
        if (recipients.size() == 1) {
            VineRecipient recipient = recipients.get(0);
            conversationRowId = determineBestConversationRowIdForRecipient(recipient);
        }
        if (conversationRowId <= 0) {
            long conversationRowId2 = createPreMergeConversationIdAndRecipientsIfNecessary(recipients, networkType);
            return conversationRowId2;
        }
        return conversationRowId;
    }

    public long determineBestConversationRowIdForRecipient(VineRecipient recipient) {
        long userRowId = getUserRowIdForRecipient(recipient);
        return determineBestConversationRowIdForUserRowId(userRowId);
    }

    public long determineBestConversationRowIdForUserRemoteId(long userRemoteId) {
        long userRowId = getUserRowIdForUserRemoteId(userRemoteId);
        return determineBestConversationRowIdForUserRowId(userRowId);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0057, code lost:
    
        r12 = r10;
        r9.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x005b, code lost:
    
        if (r9 == null) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x005d, code lost:
    
        r9.close();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long determineBestConversationRowIdForUserRowId(long r16) {
        /*
            r15 = this;
            android.database.sqlite.SQLiteDatabase r0 = r15.getReadableDatabase()
            java.lang.String r1 = "conversation_recipients"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]
            r3 = 0
            java.lang.String r4 = "conversation_row_id"
            r2[r3] = r4
            java.lang.String r3 = "user_row_id =?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]
            r5 = 0
            java.lang.String r6 = java.lang.String.valueOf(r16)
            r4[r5] = r6
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r8 = r0.query(r1, r2, r3, r4, r5, r6, r7)
            r12 = -1
            if (r8 == 0) goto L65
        L26:
            boolean r1 = r8.moveToNext()     // Catch: java.lang.Throwable -> L6c
            if (r1 == 0) goto L60
            r1 = 0
            long r10 = r8.getLong(r1)     // Catch: java.lang.Throwable -> L6c
            java.lang.String r1 = "conversation_recipients"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> L6c
            r3 = 0
            java.lang.String r4 = "_id"
            r2[r3] = r4     // Catch: java.lang.Throwable -> L6c
            java.lang.String r3 = "conversation_row_id =? "
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: java.lang.Throwable -> L6c
            r5 = 0
            java.lang.String r6 = java.lang.String.valueOf(r10)     // Catch: java.lang.Throwable -> L6c
            r4[r5] = r6     // Catch: java.lang.Throwable -> L6c
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r9 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L6c
            if (r9 == 0) goto L26
            int r1 = r9.getCount()     // Catch: java.lang.Throwable -> L73
            r2 = 1
            if (r1 != r2) goto L66
            r12 = r10
            r9.close()     // Catch: java.lang.Throwable -> L73
            if (r9 == 0) goto L60
            r9.close()     // Catch: java.lang.Throwable -> L6c
        L60:
            if (r8 == 0) goto L65
            r8.close()
        L65:
            return r12
        L66:
            if (r9 == 0) goto L26
            r9.close()     // Catch: java.lang.Throwable -> L6c
            goto L26
        L6c:
            r1 = move-exception
            if (r8 == 0) goto L72
            r8.close()
        L72:
            throw r1
        L73:
            r1 = move-exception
            if (r9 == 0) goto L79
            r9.close()     // Catch: java.lang.Throwable -> L6c
        L79:
            throw r1     // Catch: java.lang.Throwable -> L6c
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.provider.VineDatabaseHelper.determineBestConversationRowIdForUserRowId(long):long");
    }

    public ArrayList<VineRecipient> getConversationRecipientsFromConversationRowId(long conversationRowId) throws Throwable {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("conversation_recipients", new String[]{"user_row_id"}, "conversation_row_id =? ", new String[]{String.valueOf(conversationRowId)}, null, null, null);
        if (c == null) {
            return null;
        }
        try {
            ArrayList<VineRecipient> result = new ArrayList<>();
            while (c.moveToNext()) {
                try {
                    long userRowId = c.getLong(0);
                    Cursor c2 = db.query("users", new String[]{"user_id"}, "_id=?", new String[]{String.valueOf(userRowId)}, null, null, null);
                    if (c2 != null) {
                        try {
                            if (c2.moveToFirst()) {
                                result.add(VineRecipient.fromUser(null, c2.getLong(0), 0, userRowId));
                            }
                            c2.close();
                        } catch (Throwable th) {
                            c2.close();
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    c.close();
                    throw th;
                }
            }
            c.close();
            return result;
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public void setMessageError(String uploadPath, int errorCode, String errorReason) {
        if (!TextUtils.isEmpty(uploadPath)) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("error_code", Integer.valueOf(errorCode));
            cv.put("error_reason", errorReason);
            int updated = db.update("messages", cv, "upload_path=?", new String[]{uploadPath});
            if (updated > 0) {
                notifyVMUris();
            }
        }
    }

    public void setMessageError(long messageRowId, int errorCode, String errorReason) {
        if (messageRowId > 0) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("error_code", Integer.valueOf(errorCode));
            cv.put("error_reason", errorReason);
            int updated = db.update("messages", cv, "_id=?", new String[]{String.valueOf(messageRowId)});
            if (updated > 0) {
                notifyVMUris();
            }
        }
    }

    public long getNewMessageId() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("messages", new String[]{"message_id"}, null, null, null, null, "message_id DESC", "1");
        long ret = 1;
        if (c != null) {
            if (c.moveToFirst()) {
                ret = c.getLong(0) + 1;
            }
            c.close();
        }
        return ret;
    }

    public boolean amFollowingUser(long userRowId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("users", new String[]{"following_flag"}, "_id =? ", new String[]{String.valueOf(userRowId)}, null, null, null);
        boolean following = false;
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    following = c.getInt(0) == 1;
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
        return following;
    }

    public void removeUserSectionsWithType(int sectionType) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("user_sections", "section_type=?", new String[]{String.valueOf(sectionType)});
    }

    public String getDisplayForUser(long recipientId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("users", new String[]{"username"}, "_id =? ", new String[]{String.valueOf(recipientId)}, null, null, null);
        String name = null;
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    name = c.getString(0);
                }
            } finally {
                c.close();
            }
        }
        return name;
    }

    public void updateUserFollowingFlag(long userIdToFollow, boolean following) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("following_flag", Integer.valueOf(following ? 1 : 0));
        db.update("users", values, "user_id=?", new String[]{String.valueOf(userIdToFollow)});
    }

    public Cursor getMessagesForConversationRowId(long conversationRowId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("message_users_view", VineDatabaseSQL.ConversationMessageUsersQuery.PROJECTION, "conversation_row_id=? AND error_code <> 0", new String[]{String.valueOf(conversationRowId)}, null, null, "message_id ASC");
    }

    public void removeAccount() {
        setValue("current_account", (String) null);
        setValue("current_user_id", 0L);
        setValue("current_name", (String) null);
    }

    private void setValue(String propName, String value) {
        ContentValues values = new ContentValues();
        values.put("value", value);
        saveUserValue(propName, values);
    }

    private void setValue(String propName, long value) {
        ContentValues values = new ContentValues();
        values.put("value", Long.valueOf(value));
        saveUserValue(propName, values);
    }
}
