package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.internal.zzqq;
import com.google.android.gms.internal.zztd;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
class zzd extends zzw {
    private static final Map<String, String> zzaSu = new ArrayMap(5);
    private final zza zzaSv;
    private final zzaa zzaSw;

    private class zza extends SQLiteOpenHelper {
        zza(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
        }

        private void zza(SQLiteDatabase sQLiteDatabase, String str, String str2, String str3, Map<String, String> map) throws SQLException {
            if (!zza(sQLiteDatabase, str)) {
                sQLiteDatabase.execSQL(str2);
            }
            try {
                zza(sQLiteDatabase, str, str3, map);
            } catch (SQLiteException e) {
                zzd.this.zzzz().zzBl().zzj("Failed to verify columns on table that was just created", str);
                throw e;
            }
        }

        private void zza(SQLiteDatabase sQLiteDatabase, String str, String str2, Map<String, String> map) throws SQLException {
            Set<String> setZzb = zzb(sQLiteDatabase, str);
            for (String str3 : str2.split(",")) {
                if (!setZzb.remove(str3)) {
                    throw new SQLiteException("Database " + str + " is missing required column: " + str3);
                }
            }
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (!setZzb.remove(entry.getKey())) {
                        sQLiteDatabase.execSQL(entry.getValue());
                    }
                }
            }
            if (!setZzb.isEmpty()) {
                throw new SQLiteException("Database " + str + " table has extra columns");
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x0041  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private boolean zza(android.database.sqlite.SQLiteDatabase r11, java.lang.String r12) throws java.lang.Throwable {
            /*
                r10 = this;
                r8 = 0
                r9 = 0
                java.lang.String r1 = "SQLITE_MASTER"
                r0 = 1
                java.lang.String[] r2 = new java.lang.String[r0]     // Catch: android.database.sqlite.SQLiteException -> L26 java.lang.Throwable -> L3e
                r0 = 0
                java.lang.String r3 = "name"
                r2[r0] = r3     // Catch: android.database.sqlite.SQLiteException -> L26 java.lang.Throwable -> L3e
                java.lang.String r3 = "name=?"
                r0 = 1
                java.lang.String[] r4 = new java.lang.String[r0]     // Catch: android.database.sqlite.SQLiteException -> L26 java.lang.Throwable -> L3e
                r0 = 0
                r4[r0] = r12     // Catch: android.database.sqlite.SQLiteException -> L26 java.lang.Throwable -> L3e
                r5 = 0
                r6 = 0
                r7 = 0
                r0 = r11
                android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: android.database.sqlite.SQLiteException -> L26 java.lang.Throwable -> L3e
                boolean r0 = r1.moveToFirst()     // Catch: java.lang.Throwable -> L45 android.database.sqlite.SQLiteException -> L48
                if (r1 == 0) goto L25
                r1.close()
            L25:
                return r0
            L26:
                r0 = move-exception
                r1 = r9
            L28:
                com.google.android.gms.measurement.internal.zzd r2 = com.google.android.gms.measurement.internal.zzd.this     // Catch: java.lang.Throwable -> L45
                com.google.android.gms.measurement.internal.zzo r2 = r2.zzzz()     // Catch: java.lang.Throwable -> L45
                com.google.android.gms.measurement.internal.zzo$zza r2 = r2.zzBm()     // Catch: java.lang.Throwable -> L45
                java.lang.String r3 = "Error querying for table"
                r2.zze(r3, r12, r0)     // Catch: java.lang.Throwable -> L45
                if (r1 == 0) goto L3c
                r1.close()
            L3c:
                r0 = r8
                goto L25
            L3e:
                r0 = move-exception
            L3f:
                if (r9 == 0) goto L44
                r9.close()
            L44:
                throw r0
            L45:
                r0 = move-exception
                r9 = r1
                goto L3f
            L48:
                r0 = move-exception
                goto L28
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzd.zza.zza(android.database.sqlite.SQLiteDatabase, java.lang.String):boolean");
        }

        private Set<String> zzb(SQLiteDatabase sQLiteDatabase, String str) {
            HashSet hashSet = new HashSet();
            Cursor cursorRawQuery = sQLiteDatabase.rawQuery("SELECT * FROM " + str + " LIMIT 0", null);
            try {
                Collections.addAll(hashSet, cursorRawQuery.getColumnNames());
                return hashSet;
            } finally {
                cursorRawQuery.close();
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public SQLiteDatabase getWritableDatabase() {
            if (!zzd.this.zzaSw.zzv(zzd.this.zzAX().zzAA())) {
                throw new SQLiteException("Database open failed");
            }
            try {
                return super.getWritableDatabase();
            } catch (SQLiteException e) {
                zzd.this.zzaSw.start();
                zzd.this.zzzz().zzBl().zzez("Opening the database failed, dropping and recreating it");
                zzd.this.getContext().getDatabasePath(zzd.this.zzjz()).delete();
                try {
                    SQLiteDatabase writableDatabase = super.getWritableDatabase();
                    zzd.this.zzaSw.clear();
                    return writableDatabase;
                } catch (SQLiteException e2) {
                    zzd.this.zzzz().zzBl().zzj("Failed to open freshly created database", e2);
                    throw e2;
                }
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase database) {
            if (Build.VERSION.SDK_INT >= 9) {
                File file = new File(database.getPath());
                file.setReadable(false, false);
                file.setWritable(false, false);
                file.setReadable(true, true);
                file.setWritable(true, true);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(SQLiteDatabase database) throws SQLException {
            if (Build.VERSION.SDK_INT < 15) {
                Cursor cursorRawQuery = database.rawQuery("PRAGMA journal_mode=memory", null);
                try {
                    cursorRawQuery.moveToFirst();
                } finally {
                    cursorRawQuery.close();
                }
            }
            zza(database, "events", "CREATE TABLE IF NOT EXISTS events ( app_id TEXT NOT NULL, name TEXT NOT NULL, lifetime_count INTEGER NOT NULL, current_bundle_count INTEGER NOT NULL, last_fire_timestamp INTEGER NOT NULL, PRIMARY KEY (app_id, name)) ;", "app_id,name,lifetime_count,current_bundle_count,last_fire_timestamp", null);
            zza(database, "user_attributes", "CREATE TABLE IF NOT EXISTS user_attributes ( app_id TEXT NOT NULL, name TEXT NOT NULL, set_timestamp INTEGER NOT NULL, value BLOB NOT NULL, PRIMARY KEY (app_id, name)) ;", "app_id,name,set_timestamp,value", null);
            zza(database, "apps", "CREATE TABLE IF NOT EXISTS apps ( app_id TEXT NOT NULL, app_instance_id TEXT, gmp_app_id TEXT, resettable_device_id_hash TEXT, last_bundle_index INTEGER NOT NULL, last_bundle_end_timestamp INTEGER NOT NULL, PRIMARY KEY (app_id)) ;", "app_id,app_instance_id,gmp_app_id,resettable_device_id_hash,last_bundle_index,last_bundle_end_timestamp", zzd.zzaSu);
            zza(database, "queue", "CREATE TABLE IF NOT EXISTS queue ( app_id TEXT NOT NULL, bundle_end_timestamp INTEGER NOT NULL, data BLOB NOT NULL);", "app_id,bundle_end_timestamp,data", null);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    static {
        zzaSu.put("app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;");
        zzaSu.put("app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;");
        zzaSu.put("gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;");
        zzaSu.put("dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;");
        zzaSu.put("measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;");
    }

    zzd(zzt zztVar) {
        super(zztVar);
        this.zzaSw = new zzaa(zziT());
        this.zzaSv = new zza(getContext(), zzjz());
    }

    private boolean zzBc() {
        return getContext().getDatabasePath(zzjz()).exists();
    }

    static int zza(Cursor cursor, int i) {
        if (Build.VERSION.SDK_INT >= 11) {
            return cursor.getType(i);
        }
        CursorWindow window = ((SQLiteCursor) cursor).getWindow();
        int position = cursor.getPosition();
        if (window.isNull(position, i)) {
            return 0;
        }
        if (window.isLong(position, i)) {
            return 1;
        }
        if (window.isFloat(position, i)) {
            return 2;
        }
        if (window.isString(position, i)) {
            return 3;
        }
        return window.isBlob(position, i) ? 4 : -1;
    }

    private long zza(String str, String[] strArr, long j) {
        Cursor cursorRawQuery = null;
        try {
            try {
                cursorRawQuery = getWritableDatabase().rawQuery(str, strArr);
                if (cursorRawQuery.moveToFirst()) {
                    j = cursorRawQuery.getLong(0);
                } else if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
                return j;
            } catch (SQLiteException e) {
                zzzz().zzBl().zze("Database error", str, e);
                throw e;
            }
        } finally {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String zzjz() {
        if (zzAX().zzka() && !zzAX().zzkb()) {
            zzzz().zzBn().zzez("Using secondary database");
            return zzAX().zzkB();
        }
        return zzAX().zzkA();
    }

    public void beginTransaction() {
        zzje();
        getWritableDatabase().beginTransaction();
    }

    public void endTransaction() {
        zzje();
        getWritableDatabase().endTransaction();
    }

    SQLiteDatabase getWritableDatabase() {
        zziS();
        try {
            return this.zzaSv.getWritableDatabase();
        } catch (SQLiteException e) {
            zzzz().zzBm().zzj("Error opening database", e);
            throw e;
        }
    }

    public void setTransactionSuccessful() {
        zzje();
        getWritableDatabase().setTransactionSuccessful();
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String zzAY() {
        /*
            r5 = this;
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r5.getWritableDatabase()
            java.lang.String r2 = "SELECT q.app_id FROM queue q JOIN apps a ON a.app_id=q.app_id WHERE a.measurement_enabled!=0 ORDER BY q.rowid LIMIT 1;"
            r3 = 0
            android.database.Cursor r2 = r1.rawQuery(r2, r3)     // Catch: android.database.sqlite.SQLiteException -> L23 java.lang.Throwable -> L38
            boolean r1 = r2.moveToFirst()     // Catch: java.lang.Throwable -> L41 android.database.sqlite.SQLiteException -> L43
            if (r1 == 0) goto L1d
            r1 = 0
            java.lang.String r0 = r2.getString(r1)     // Catch: java.lang.Throwable -> L41 android.database.sqlite.SQLiteException -> L43
            if (r2 == 0) goto L1c
            r2.close()
        L1c:
            return r0
        L1d:
            if (r2 == 0) goto L1c
            r2.close()
            goto L1c
        L23:
            r1 = move-exception
            r2 = r0
        L25:
            com.google.android.gms.measurement.internal.zzo r3 = r5.zzzz()     // Catch: java.lang.Throwable -> L41
            com.google.android.gms.measurement.internal.zzo$zza r3 = r3.zzBl()     // Catch: java.lang.Throwable -> L41
            java.lang.String r4 = "Database error getting next bundle app id"
            r3.zzj(r4, r1)     // Catch: java.lang.Throwable -> L41
            if (r2 == 0) goto L1c
            r2.close()
            goto L1c
        L38:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L3b:
            if (r2 == 0) goto L40
            r2.close()
        L40:
            throw r0
        L41:
            r0 = move-exception
            goto L3b
        L43:
            r1 = move-exception
            goto L25
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzd.zzAY():java.lang.String");
    }

    void zzAZ() {
        zziS();
        zzje();
        if (zzBc()) {
            long j = zzAW().zzaTI.get();
            long jElapsedRealtime = zziT().elapsedRealtime();
            if (Math.abs(jElapsedRealtime - j) > zzAX().zzAG()) {
                zzAW().zzaTI.set(jElapsedRealtime);
                zzBa();
            }
        }
    }

    void zzBa() {
        int iDelete;
        zziS();
        zzje();
        if (zzBc() && (iDelete = getWritableDatabase().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(zziT().currentTimeMillis()), String.valueOf(zzAX().zzAF())})) > 0) {
            zzzz().zzBr().zzj("Deleted stale rows. rowsDeleted", Integer.valueOf(iDelete));
        }
    }

    public long zzBb() {
        return zza("select max(bundle_end_timestamp) from queue", (String[]) null, 0L);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x008c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.google.android.gms.measurement.internal.zzh zzL(java.lang.String r13, java.lang.String r14) throws java.lang.Throwable {
        /*
            r12 = this;
            r10 = 0
            com.google.android.gms.common.internal.zzx.zzcG(r13)
            com.google.android.gms.common.internal.zzx.zzcG(r14)
            r12.zziS()
            r12.zzje()
            android.database.sqlite.SQLiteDatabase r0 = r12.getWritableDatabase()     // Catch: android.database.sqlite.SQLiteException -> L73 java.lang.Throwable -> L89
            java.lang.String r1 = "events"
            r2 = 3
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: android.database.sqlite.SQLiteException -> L73 java.lang.Throwable -> L89
            r3 = 0
            java.lang.String r4 = "lifetime_count"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L73 java.lang.Throwable -> L89
            r3 = 1
            java.lang.String r4 = "current_bundle_count"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L73 java.lang.Throwable -> L89
            r3 = 2
            java.lang.String r4 = "last_fire_timestamp"
            r2[r3] = r4     // Catch: android.database.sqlite.SQLiteException -> L73 java.lang.Throwable -> L89
            java.lang.String r3 = "app_id=? and name=?"
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: android.database.sqlite.SQLiteException -> L73 java.lang.Throwable -> L89
            r5 = 0
            r4[r5] = r13     // Catch: android.database.sqlite.SQLiteException -> L73 java.lang.Throwable -> L89
            r5 = 1
            r4[r5] = r14     // Catch: android.database.sqlite.SQLiteException -> L73 java.lang.Throwable -> L89
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r11 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: android.database.sqlite.SQLiteException -> L73 java.lang.Throwable -> L89
            boolean r0 = r11.moveToFirst()     // Catch: java.lang.Throwable -> L90 android.database.sqlite.SQLiteException -> L96
            if (r0 != 0) goto L44
            if (r11 == 0) goto L42
            r11.close()
        L42:
            r1 = r10
        L43:
            return r1
        L44:
            r0 = 0
            long r4 = r11.getLong(r0)     // Catch: java.lang.Throwable -> L90 android.database.sqlite.SQLiteException -> L96
            r0 = 1
            long r6 = r11.getLong(r0)     // Catch: java.lang.Throwable -> L90 android.database.sqlite.SQLiteException -> L96
            r0 = 2
            long r8 = r11.getLong(r0)     // Catch: java.lang.Throwable -> L90 android.database.sqlite.SQLiteException -> L96
            com.google.android.gms.measurement.internal.zzh r1 = new com.google.android.gms.measurement.internal.zzh     // Catch: java.lang.Throwable -> L90 android.database.sqlite.SQLiteException -> L96
            r2 = r13
            r3 = r14
            r1.<init>(r2, r3, r4, r6, r8)     // Catch: java.lang.Throwable -> L90 android.database.sqlite.SQLiteException -> L96
            boolean r0 = r11.moveToNext()     // Catch: java.lang.Throwable -> L90 android.database.sqlite.SQLiteException -> L96
            if (r0 == 0) goto L6d
            com.google.android.gms.measurement.internal.zzo r0 = r12.zzzz()     // Catch: java.lang.Throwable -> L90 android.database.sqlite.SQLiteException -> L96
            com.google.android.gms.measurement.internal.zzo$zza r0 = r0.zzBl()     // Catch: java.lang.Throwable -> L90 android.database.sqlite.SQLiteException -> L96
            java.lang.String r2 = "Got multiple records for event aggregates, expected one"
            r0.zzez(r2)     // Catch: java.lang.Throwable -> L90 android.database.sqlite.SQLiteException -> L96
        L6d:
            if (r11 == 0) goto L43
            r11.close()
            goto L43
        L73:
            r0 = move-exception
            r1 = r10
        L75:
            com.google.android.gms.measurement.internal.zzo r2 = r12.zzzz()     // Catch: java.lang.Throwable -> L93
            com.google.android.gms.measurement.internal.zzo$zza r2 = r2.zzBl()     // Catch: java.lang.Throwable -> L93
            java.lang.String r3 = "Error querying events"
            r2.zzd(r3, r13, r14, r0)     // Catch: java.lang.Throwable -> L93
            if (r1 == 0) goto L87
            r1.close()
        L87:
            r1 = r10
            goto L43
        L89:
            r0 = move-exception
        L8a:
            if (r10 == 0) goto L8f
            r10.close()
        L8f:
            throw r0
        L90:
            r0 = move-exception
            r10 = r11
            goto L8a
        L93:
            r0 = move-exception
            r10 = r1
            goto L8a
        L96:
            r0 = move-exception
            r1 = r11
            goto L75
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzd.zzL(java.lang.String, java.lang.String):com.google.android.gms.measurement.internal.zzh");
    }

    public void zzM(String str, String str2) throws IllegalStateException {
        com.google.android.gms.common.internal.zzx.zzcG(str);
        com.google.android.gms.common.internal.zzx.zzcG(str2);
        zziS();
        zzje();
        try {
            zzzz().zzBr().zzj("Deleted user attribute rows:", Integer.valueOf(getWritableDatabase().delete("user_attributes", "app_id=? and name=?", new String[]{str, str2})));
        } catch (SQLiteException e) {
            zzzz().zzBl().zzd("Error deleting user attribute", str, str2, e);
        }
    }

    public void zzP(long j) {
        zziS();
        zzje();
        if (getWritableDatabase().delete("queue", "rowid=?", new String[]{String.valueOf(j)}) != 1) {
            zzzz().zzBl().zzez("Deleted fewer rows from queue than expected");
        }
    }

    void zza(ContentValues contentValues, String str, Object obj) {
        com.google.android.gms.common.internal.zzx.zzcG(str);
        com.google.android.gms.common.internal.zzx.zzy(obj);
        if (obj instanceof String) {
            contentValues.put(str, (String) obj);
        } else if (obj instanceof Long) {
            contentValues.put(str, (Long) obj);
        } else {
            if (!(obj instanceof Float)) {
                throw new IllegalArgumentException("Invalid value type");
            }
            contentValues.put(str, (Float) obj);
        }
    }

    public void zza(zzqq.zzd zzdVar) {
        zziS();
        zzje();
        com.google.android.gms.common.internal.zzx.zzy(zzdVar);
        com.google.android.gms.common.internal.zzx.zzcG(zzdVar.appId);
        com.google.android.gms.common.internal.zzx.zzy(zzdVar.zzaVw);
        zzAZ();
        long jCurrentTimeMillis = zziT().currentTimeMillis();
        if (zzdVar.zzaVw.longValue() < jCurrentTimeMillis - zzAX().zzAF() || zzdVar.zzaVw.longValue() > zzAX().zzAF() + jCurrentTimeMillis) {
            zzzz().zzBm().zze("Storing bundle outside of the max uploading time span. now, timestamp", Long.valueOf(jCurrentTimeMillis), zzdVar.zzaVw);
        }
        try {
            byte[] bArr = new byte[zzdVar.getSerializedSize()];
            zztd zztdVarZzD = zztd.zzD(bArr);
            zzdVar.writeTo(zztdVarZzD);
            zztdVarZzD.zzHy();
            byte[] bArrZzg = zzAU().zzg(bArr);
            zzzz().zzBr().zzj("Saving bundle, size", Integer.valueOf(bArrZzg.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzdVar.appId);
            contentValues.put("bundle_end_timestamp", zzdVar.zzaVw);
            contentValues.put("data", bArrZzg);
            try {
                if (getWritableDatabase().insert("queue", null, contentValues) == -1) {
                    zzzz().zzBl().zzez("Failed to insert bundle (got -1)");
                }
            } catch (SQLiteException e) {
                zzzz().zzBl().zzj("Error storing bundle", e);
            }
        } catch (IOException e2) {
            zzzz().zzBl().zzj("Data loss. Failed to serialize bundle", e2);
        }
    }

    public void zza(com.google.android.gms.measurement.internal.zza zzaVar) {
        com.google.android.gms.common.internal.zzx.zzy(zzaVar);
        zziS();
        zzje();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzaVar.zzaRd);
        contentValues.put("app_instance_id", zzaVar.zzaSe);
        contentValues.put("gmp_app_id", zzaVar.zzaSf);
        contentValues.put("resettable_device_id_hash", zzaVar.zzaSg);
        contentValues.put("last_bundle_index", Long.valueOf(zzaVar.zzaSh));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(zzaVar.zzaSi));
        contentValues.put("app_version", zzaVar.zzRl);
        contentValues.put("app_store", zzaVar.zzaSj);
        contentValues.put("gmp_version", Long.valueOf(zzaVar.zzaSk));
        contentValues.put("dev_cert_hash", Long.valueOf(zzaVar.zzaSl));
        contentValues.put("measurement_enabled", Boolean.valueOf(zzaVar.zzaSm));
        try {
            if (getWritableDatabase().insertWithOnConflict("apps", null, contentValues, 5) == -1) {
                zzzz().zzBl().zzez("Failed to insert/update app (got -1)");
            }
        } catch (SQLiteException e) {
            zzzz().zzBl().zzj("Error storing app", e);
        }
    }

    public void zza(zzac zzacVar) {
        com.google.android.gms.common.internal.zzx.zzy(zzacVar);
        zziS();
        zzje();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzacVar.zzaRd);
        contentValues.put("name", zzacVar.mName);
        contentValues.put("set_timestamp", Long.valueOf(zzacVar.zzaVf));
        zza(contentValues, "value", zzacVar.zzLI);
        try {
            if (getWritableDatabase().insertWithOnConflict("user_attributes", null, contentValues, 5) == -1) {
                zzzz().zzBl().zzez("Failed to insert/update user attribute (got -1)");
            }
        } catch (SQLiteException e) {
            zzzz().zzBl().zzj("Error storing user attribute", e);
        }
    }

    public void zza(zzh zzhVar) {
        com.google.android.gms.common.internal.zzx.zzy(zzhVar);
        zziS();
        zzje();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzhVar.zzaRd);
        contentValues.put("name", zzhVar.mName);
        contentValues.put("lifetime_count", Long.valueOf(zzhVar.zzaSF));
        contentValues.put("current_bundle_count", Long.valueOf(zzhVar.zzaSG));
        contentValues.put("last_fire_timestamp", Long.valueOf(zzhVar.zzaSH));
        try {
            if (getWritableDatabase().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                zzzz().zzBl().zzez("Failed to insert/update event aggregates (got -1)");
            }
        } catch (SQLiteException e) {
            zzzz().zzBl().zzj("Error storing event aggregates", e);
        }
    }

    Object zzb(Cursor cursor, int i) {
        int iZza = zza(cursor, i);
        switch (iZza) {
            case 0:
                zzzz().zzBl().zzez("Loaded invalid null value from database");
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                zzzz().zzBl().zzez("Loaded invalid blob type value, ignoring it");
                break;
            default:
                zzzz().zzBl().zzj("Loaded invalid unknown value type, ignoring it", Integer.valueOf(iZza));
                break;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List<com.google.android.gms.measurement.internal.zzac> zzev(java.lang.String r12) {
        /*
            r11 = this;
            r10 = 0
            com.google.android.gms.common.internal.zzx.zzcG(r12)
            r11.zziS()
            r11.zzje()
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            android.database.sqlite.SQLiteDatabase r0 = r11.getWritableDatabase()     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            java.lang.String r1 = "user_attributes"
            r2 = 3
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            r3 = 0
            java.lang.String r4 = "name"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            r3 = 1
            java.lang.String r4 = "set_timestamp"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            r3 = 2
            java.lang.String r4 = "value"
            r2[r3] = r4     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            java.lang.String r3 = "app_id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            r5 = 0
            r4[r5] = r12     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            r5 = 0
            r6 = 0
            java.lang.String r7 = "rowid"
            com.google.android.gms.measurement.internal.zzc r8 = r11.zzAX()     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            int r8 = r8.zzAz()     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            int r8 = r8 + 1
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> Lc5 android.database.sqlite.SQLiteException -> Ld2
            boolean r0 = r7.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            if (r0 != 0) goto L54
            if (r7 == 0) goto L52
            r7.close()
        L52:
            r0 = r9
        L53:
            return r0
        L54:
            r0 = 0
            java.lang.String r3 = r7.getString(r0)     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            r0 = 1
            long r4 = r7.getLong(r0)     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            r0 = 2
            java.lang.Object r6 = r11.zzb(r7, r0)     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            if (r6 != 0) goto La5
            com.google.android.gms.measurement.internal.zzo r0 = r11.zzzz()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            com.google.android.gms.measurement.internal.zzo$zza r0 = r0.zzBl()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            java.lang.String r1 = "Read invalid user attribute value, ignoring it"
            r0.zzez(r1)     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
        L72:
            boolean r0 = r7.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            if (r0 != 0) goto L54
            int r0 = r9.size()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            com.google.android.gms.measurement.internal.zzc r1 = r11.zzAX()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            int r1 = r1.zzAz()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            if (r0 <= r1) goto L9e
            com.google.android.gms.measurement.internal.zzo r0 = r11.zzzz()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            com.google.android.gms.measurement.internal.zzo$zza r0 = r0.zzBl()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            java.lang.String r1 = "Loaded too many user attributes"
            r0.zzez(r1)     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            com.google.android.gms.measurement.internal.zzc r0 = r11.zzAX()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            int r0 = r0.zzAz()     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            r9.remove(r0)     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
        L9e:
            if (r7 == 0) goto La3
            r7.close()
        La3:
            r0 = r9
            goto L53
        La5:
            com.google.android.gms.measurement.internal.zzac r1 = new com.google.android.gms.measurement.internal.zzac     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            r2 = r12
            r1.<init>(r2, r3, r4, r6)     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            r9.add(r1)     // Catch: android.database.sqlite.SQLiteException -> Laf java.lang.Throwable -> Lcd
            goto L72
        Laf:
            r0 = move-exception
            r1 = r7
        Lb1:
            com.google.android.gms.measurement.internal.zzo r2 = r11.zzzz()     // Catch: java.lang.Throwable -> Lcf
            com.google.android.gms.measurement.internal.zzo$zza r2 = r2.zzBl()     // Catch: java.lang.Throwable -> Lcf
            java.lang.String r3 = "Error querying user attributes"
            r2.zze(r3, r12, r0)     // Catch: java.lang.Throwable -> Lcf
            if (r1 == 0) goto Lc3
            r1.close()
        Lc3:
            r0 = r10
            goto L53
        Lc5:
            r0 = move-exception
            r7 = r10
        Lc7:
            if (r7 == 0) goto Lcc
            r7.close()
        Lcc:
            throw r0
        Lcd:
            r0 = move-exception
            goto Lc7
        Lcf:
            r0 = move-exception
            r7 = r1
            goto Lc7
        Ld2:
            r0 = move-exception
            r1 = r10
            goto Lb1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzd.zzev(java.lang.String):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00fb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.google.android.gms.measurement.internal.zza zzew(java.lang.String r21) {
        /*
            Method dump skipped, instructions count: 266
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzd.zzew(java.lang.String):com.google.android.gms.measurement.internal.zza");
    }

    @Override // com.google.android.gms.measurement.internal.zzw
    protected void zzir() {
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x00da  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List<android.util.Pair<com.google.android.gms.internal.zzqq.zzd, java.lang.Long>> zzn(java.lang.String r12, int r13, int r14) {
        /*
            Method dump skipped, instructions count: 230
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzd.zzn(java.lang.String, int, int):java.util.List");
    }
}
