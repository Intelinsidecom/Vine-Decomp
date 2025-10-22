package co.vine.android.provider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import co.vine.android.client.AppController;
import co.vine.android.util.CrashUtil;
import java.util.HashMap;

/* loaded from: classes.dex */
public class VineUploadDatabaseHelper extends SQLiteOpenHelper {
    private static final HashMap<String, VineUploadDatabaseHelper> sHelperMap = new HashMap<>();
    private final Context mAppContext;

    public static synchronized VineUploadDatabaseHelper getDatabaseHelper(Context context) {
        VineUploadDatabaseHelper helper;
        String databaseName = getDatabaseName(1);
        helper = sHelperMap.get(databaseName);
        if (helper == null) {
            helper = new VineUploadDatabaseHelper(context.getApplicationContext(), databaseName);
            sHelperMap.put(databaseName, helper);
        }
        return helper;
    }

    private VineUploadDatabaseHelper(Context context, String name) {
        super(context, name, (SQLiteDatabase.CursorFactory) null, 4);
        this.mAppContext = context.getApplicationContext();
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) throws SQLException {
        createTables(db);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        int version = oldVersion;
        if (version == 1) {
            try {
                db.execSQL("ALTER TABLE uploads ADD COLUMN reference TEXT;");
                db.execSQL("ALTER TABLE uploads ADD COLUMN owner_id INT;");
                db.execSQL("UPDATE uploads SET status=2 WHERE (status=0 OR status=1);");
                long userId = AppController.getInstance(this.mAppContext).getActiveSessionReadOnly().getUserId();
                db.execSQL("UPDATE uploads SET owner_id=" + userId + ";");
                version = 2;
            } catch (SQLiteException e) {
                CrashUtil.logException(e);
                db.execSQL("DROP TABLE IF EXISTS uploads;");
                db.execSQL("CREATE TABLE  IF NOT EXISTS uploads (_id INTEGER PRIMARY KEY,path TEXT,hash TEXT,status INT,post_info TEXT,video_url TEXT,thumbnail_path TEXT,thumbnail_url TEXT,upload_time TEXT,is_private INT,conversation_row_id INT,reference TEXT,owner_id INT,captcha_url TEXT, max_loops INT, message_row INT);");
                return;
            }
        }
        if (version == 2) {
            db.execSQL("ALTER TABLE uploads ADD COLUMN captcha_url TEXT;");
            version = 3;
        }
        if (version == 3) {
            db.execSQL("ALTER TABLE uploads ADD COLUMN is_private INT;");
            db.execSQL("ALTER TABLE uploads ADD COLUMN conversation_row_id INT;");
            db.execSQL("ALTER TABLE uploads ADD COLUMN max_loops INT;");
            db.execSQL("ALTER TABLE uploads ADD COLUMN message_row INT;");
        }
    }

    protected void createTables(SQLiteDatabase db) throws SQLException {
        db.execSQL("CREATE TABLE  IF NOT EXISTS uploads (_id INTEGER PRIMARY KEY,path TEXT,hash TEXT,status INT,post_info TEXT,video_url TEXT,thumbnail_path TEXT,thumbnail_url TEXT,upload_time TEXT,is_private INT,conversation_row_id INT,reference TEXT,owner_id INT,captcha_url TEXT, max_loops INT, message_row INT);");
    }

    public static String getDatabaseName(int schemaVersion) {
        return "upload-" + schemaVersion;
    }
}
