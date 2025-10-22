package com.digits.sdk.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.digits.sdk.vcard.VCardBuilder;
import com.googlecode.javacv.cpp.avformat;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
class ContactsHelper {
    private static final String[] allProjectionColumns = {"mimetype", "lookup", "data2", "data3", "is_primary", "data1", "data1", "data2", "data3", "is_primary", "data1", "data2", "data3"};
    private static final String[] selectionArgs = {"vnd.android.cursor.item/phone_v2", "vnd.android.cursor.item/email_v2", "vnd.android.cursor.item/name"};
    private final Context context;

    ContactsHelper(Context context) {
        this.context = context;
    }

    public Cursor getContactsCursor() {
        HashSet<String> tempSet = new HashSet<>(Arrays.asList(allProjectionColumns));
        String[] projectionColumns = (String[]) tempSet.toArray(new String[tempSet.size()]);
        Uri uri = ContactsContract.Data.CONTENT_URI.buildUpon().appendQueryParameter("limit", Integer.toString(avformat.AVStream.MAX_PROBE_PACKETS)).build();
        return this.context.getContentResolver().query(uri, projectionColumns, "mimetype=? OR mimetype=? OR mimetype=?", selectionArgs, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x005e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List<java.lang.String> createContactList(android.database.Cursor r10) {
        /*
            r9 = this;
            if (r10 == 0) goto L8
            int r7 = r10.getCount()
            if (r7 != 0) goto Ld
        L8:
            java.util.List r7 = java.util.Collections.emptyList()
        Lc:
            return r7
        Ld:
            java.lang.String r7 = "mimetype"
            int r6 = r10.getColumnIndex(r7)
            java.lang.String r7 = "lookup"
            int r3 = r10.getColumnIndex(r7)
            java.util.HashMap r4 = new java.util.HashMap
            r4.<init>()
        L1e:
            boolean r7 = r10.moveToNext()
            if (r7 == 0) goto Lb0
            java.lang.String r5 = r10.getString(r6)
            android.content.ContentValues r1 = new android.content.ContentValues
            r1.<init>()
            java.lang.String r7 = "mimetype"
            r1.put(r7, r5)
            r7 = -1
            int r8 = r5.hashCode()
            switch(r8) {
                case -1569536764: goto L75;
                case -1079224304: goto L80;
                case 684173810: goto L6a;
                default: goto L3a;
            }
        L3a:
            switch(r7) {
                case 0: goto L3e;
                case 1: goto L8b;
                case 2: goto La0;
                default: goto L3d;
            }
        L3d:
            goto L1e
        L3e:
            java.lang.String r7 = "data2"
            android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r10, r1, r7)
            java.lang.String r7 = "data3"
            android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r10, r1, r7)
            java.lang.String r7 = "is_primary"
            android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r10, r1, r7)
            java.lang.String r7 = "data1"
            android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r10, r1, r7)
        L52:
            java.lang.String r2 = r10.getString(r3)
            java.lang.Object r0 = r4.get(r2)
            java.util.List r0 = (java.util.List) r0
            if (r0 != 0) goto L66
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r4.put(r2, r0)
        L66:
            r0.add(r1)
            goto L1e
        L6a:
            java.lang.String r8 = "vnd.android.cursor.item/phone_v2"
            boolean r8 = r5.equals(r8)
            if (r8 == 0) goto L3a
            r7 = 0
            goto L3a
        L75:
            java.lang.String r8 = "vnd.android.cursor.item/email_v2"
            boolean r8 = r5.equals(r8)
            if (r8 == 0) goto L3a
            r7 = 1
            goto L3a
        L80:
            java.lang.String r8 = "vnd.android.cursor.item/name"
            boolean r8 = r5.equals(r8)
            if (r8 == 0) goto L3a
            r7 = 2
            goto L3a
        L8b:
            java.lang.String r7 = "data1"
            android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r10, r1, r7)
            java.lang.String r7 = "data2"
            android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r10, r1, r7)
            java.lang.String r7 = "data3"
            android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r10, r1, r7)
            java.lang.String r7 = "is_primary"
            android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r10, r1, r7)
            goto L52
        La0:
            java.lang.String r7 = "data1"
            android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r10, r1, r7)
            java.lang.String r7 = "data2"
            android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r10, r1, r7)
            java.lang.String r7 = "data3"
            android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r10, r1, r7)
            goto L52
        Lb0:
            java.util.List r7 = r9.processContactsMap(r4)
            goto Lc
        */
        throw new UnsupportedOperationException("Method not decompiled: com.digits.sdk.android.ContactsHelper.createContactList(android.database.Cursor):java.util.List");
    }

    private List<String> processContactsMap(Map<String, List<ContentValues>> mapContactsData) throws UnsupportedEncodingException {
        List<String> vCards = new ArrayList<>();
        Map<String, List<ContentValues>> contactMimeTypeMap = new HashMap<>();
        VCardBuilder builder = new VCardBuilder(-1073741823, "UTF-8");
        for (String key : mapContactsData.keySet()) {
            List<ContentValues> contentValuesList = mapContactsData.get(key);
            boolean hasPhoneOrEmail = false;
            contactMimeTypeMap.clear();
            builder.clear();
            for (ContentValues cv : contentValuesList) {
                String mimeType = cv.getAsString("mimetype");
                if ("vnd.android.cursor.item/phone_v2".equals(mimeType) || "vnd.android.cursor.item/email_v2".equals(mimeType)) {
                    hasPhoneOrEmail = true;
                }
                List<ContentValues> group = contactMimeTypeMap.get(mimeType);
                if (group == null) {
                    group = new ArrayList<>();
                    contactMimeTypeMap.put(mimeType, group);
                }
                group.add(cv);
            }
            if (hasPhoneOrEmail) {
                builder.appendNameProperties(contactMimeTypeMap.get("vnd.android.cursor.item/name")).appendPhones(contactMimeTypeMap.get("vnd.android.cursor.item/phone_v2"), null).appendEmails(contactMimeTypeMap.get("vnd.android.cursor.item/email_v2"));
                String vcard = builder.toString();
                vCards.add(vcard);
            }
        }
        return vCards;
    }
}
