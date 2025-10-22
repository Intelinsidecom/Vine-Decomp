package com.google.android.gms.ads.internal.purchase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import com.google.android.gms.internal.zzha;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@zzha
/* loaded from: classes.dex */
public class zzh {
    private static zzh zzEW;
    private final zza zzEV;
    private static final String zzEU = String.format(Locale.US, "CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER)", "InAppPurchase", "purchase_id", "product_id", "developer_payload", "record_time");
    private static final Object zzpK = new Object();

    public class zza extends SQLiteOpenHelper {
        public zza(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 4);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase db) throws SQLException {
            db.execSQL(zzh.zzEU);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
            com.google.android.gms.ads.internal.util.client.zzb.zzaG("Database updated from version " + oldVersion + " to version " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS InAppPurchase");
            onCreate(db);
        }
    }

    zzh(Context context) {
        this.zzEV = new zza(context, "google_inapp_purchase.db");
    }

    public static zzh zzy(Context context) {
        zzh zzhVar;
        synchronized (zzpK) {
            if (zzEW == null) {
                zzEW = new zzh(context);
            }
            zzhVar = zzEW;
        }
        return zzhVar;
    }

    public int getRecordCount() {
        Cursor cursorRawQuery = null;
        int i = 0;
        synchronized (zzpK) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            try {
                if (writableDatabase != null) {
                    try {
                        cursorRawQuery = writableDatabase.rawQuery("select count(*) from InAppPurchase", null);
                    } catch (SQLiteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzaH("Error getting record count" + e.getMessage());
                        if (cursorRawQuery != null) {
                            cursorRawQuery.close();
                        }
                    }
                    if (cursorRawQuery.moveToFirst()) {
                        i = cursorRawQuery.getInt(0);
                    } else if (cursorRawQuery != null) {
                        cursorRawQuery.close();
                    }
                }
            } finally {
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
            }
        }
        return i;
    }

    public SQLiteDatabase getWritableDatabase() {
        try {
            return this.zzEV.getWritableDatabase();
        } catch (SQLiteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Error opening writable conversion tracking database");
            return null;
        }
    }

    public zzf zza(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        return new zzf(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
    }

    public void zza(zzf zzfVar) {
        if (zzfVar == null) {
            return;
        }
        synchronized (zzpK) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (writableDatabase != null) {
                writableDatabase.delete("InAppPurchase", String.format(Locale.US, "%s = %d", "purchase_id", Long.valueOf(zzfVar.zzEO)), null);
            }
        }
    }

    public void zzb(zzf zzfVar) {
        if (zzfVar == null) {
            return;
        }
        synchronized (zzpK) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (writableDatabase == null) {
                return;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("product_id", zzfVar.zzEQ);
            contentValues.put("developer_payload", zzfVar.zzEP);
            contentValues.put("record_time", Long.valueOf(SystemClock.elapsedRealtime()));
            zzfVar.zzEO = writableDatabase.insert("InAppPurchase", null, contentValues);
            if (getRecordCount() > 20000) {
                zzfL();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x005a A[Catch: all -> 0x0031, TryCatch #0 {, blocks: (B:4:0x0004, B:6:0x000a, B:14:0x002c, B:15:0x002f, B:28:0x005a, B:29:0x005d, B:24:0x0052), top: B:34:0x0004 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void zzfL() {
        /*
            r11 = this;
            r9 = 0
            java.lang.Object r10 = com.google.android.gms.ads.internal.purchase.zzh.zzpK
            monitor-enter(r10)
            android.database.sqlite.SQLiteDatabase r0 = r11.getWritableDatabase()     // Catch: java.lang.Throwable -> L31
            if (r0 != 0) goto Lc
            monitor-exit(r10)     // Catch: java.lang.Throwable -> L31
        Lb:
            return
        Lc:
            java.lang.String r7 = "record_time ASC"
            java.lang.String r1 = "InAppPurchase"
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r8 = "1"
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch: android.database.sqlite.SQLiteException -> L34 java.lang.Throwable -> L56
            if (r1 == 0) goto L2a
            boolean r0 = r1.moveToFirst()     // Catch: java.lang.Throwable -> L5e android.database.sqlite.SQLiteException -> L60
            if (r0 == 0) goto L2a
            com.google.android.gms.ads.internal.purchase.zzf r0 = r11.zza(r1)     // Catch: java.lang.Throwable -> L5e android.database.sqlite.SQLiteException -> L60
            r11.zza(r0)     // Catch: java.lang.Throwable -> L5e android.database.sqlite.SQLiteException -> L60
        L2a:
            if (r1 == 0) goto L2f
            r1.close()     // Catch: java.lang.Throwable -> L31
        L2f:
            monitor-exit(r10)     // Catch: java.lang.Throwable -> L31
            goto Lb
        L31:
            r0 = move-exception
            monitor-exit(r10)     // Catch: java.lang.Throwable -> L31
            throw r0
        L34:
            r0 = move-exception
            r1 = r9
        L36:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L5e
            r2.<init>()     // Catch: java.lang.Throwable -> L5e
            java.lang.String r3 = "Error remove oldest record"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L5e
            java.lang.String r0 = r0.getMessage()     // Catch: java.lang.Throwable -> L5e
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch: java.lang.Throwable -> L5e
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L5e
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(r0)     // Catch: java.lang.Throwable -> L5e
            if (r1 == 0) goto L2f
            r1.close()     // Catch: java.lang.Throwable -> L31
            goto L2f
        L56:
            r0 = move-exception
            r1 = r9
        L58:
            if (r1 == 0) goto L5d
            r1.close()     // Catch: java.lang.Throwable -> L31
        L5d:
            throw r0     // Catch: java.lang.Throwable -> L31
        L5e:
            r0 = move-exception
            goto L58
        L60:
            r0 = move-exception
            goto L36
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.purchase.zzh.zzfL():void");
    }

    public List<zzf> zzg(long j) {
        Cursor cursorQuery;
        Cursor cursor = null;
        synchronized (zzpK) {
            LinkedList linkedList = new LinkedList();
            if (j <= 0) {
                return linkedList;
            }
            SQLiteDatabase writableDatabase = getWritableDatabase();
            try {
                if (writableDatabase == null) {
                    return linkedList;
                }
                try {
                    cursorQuery = writableDatabase.query("InAppPurchase", null, null, null, null, null, "record_time ASC", String.valueOf(j));
                } catch (SQLiteException e) {
                    e = e;
                    cursorQuery = null;
                } catch (Throwable th) {
                    th = th;
                    if (0 != 0) {
                        cursor.close();
                    }
                    throw th;
                }
                try {
                    if (cursorQuery.moveToFirst()) {
                        do {
                            linkedList.add(zza(cursorQuery));
                        } while (cursorQuery.moveToNext());
                    }
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                } catch (SQLiteException e2) {
                    e = e2;
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH("Error extracing purchase info: " + e.getMessage());
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return linkedList;
                }
                return linkedList;
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }
}
