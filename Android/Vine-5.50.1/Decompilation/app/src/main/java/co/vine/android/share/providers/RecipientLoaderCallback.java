package co.vine.android.share.providers;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.PointerIconCompat;
import co.vine.android.ContactEntry;
import co.vine.android.api.VineRecipient;
import co.vine.android.client.AppController;
import co.vine.android.provider.Vine;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class RecipientLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
    private AppController mAppController;
    private Context mContext;
    private RecipientProvider mProvider;

    public RecipientLoaderCallback(Context context, AppController appController, RecipientProvider provider) {
        this.mContext = context;
        this.mProvider = provider;
        this.mAppController = appController;
    }

    @Override // android.app.LoaderManager.LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case 1000:
                return null;
            case PointerIconCompat.TYPE_CONTEXT_MENU /* 1001 */:
                String[] projection = {"contact_id", "display_name", "display_name_alt", "mimetype", "data1"};
                String[] selectionArgs = {"vnd.android.cursor.item/email_v2", "vnd.android.cursor.item/phone_v2"};
                return new CursorLoader(this.mContext, ContactsContract.Data.CONTENT_URI, projection, "mimetype=? OR mimetype=?", selectionArgs, "contact_id ASC ");
            case PointerIconCompat.TYPE_HAND /* 1002 */:
                Uri contentUri = ContentUris.withAppendedId(Vine.UserGroupsView.CONTENT_URI_FRIENDS, this.mAppController.getActiveId());
                String[] projection2 = {"user_id", "username", "avatar_url", "profile_background", "section_index", "section_title"};
                String refreshTime = args.getString("refresh_time");
                String[] selectionArgs2 = {refreshTime};
                return new CursorLoader(this.mContext, contentUri, projection2, "section_type=0 AND last_section_refresh=?", selectionArgs2, "last_section_refresh DESC, section_index ASC");
            default:
                SLog.i("Unknown cursor loader ID. Ignoring request to create cursor loader.");
                return null;
        }
    }

    @Override // android.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case 1000:
                break;
            case PointerIconCompat.TYPE_CONTEXT_MENU /* 1001 */:
                List<ContactEntry> contactEntries = cursor != null ? parseContactEntriesCursor(cursor) : new ArrayList<>();
                Collections.sort(contactEntries);
                this.mProvider.setContactsCache(contactEntries);
                this.mProvider.fireOnContactsChanged(contactEntries);
                break;
            case PointerIconCompat.TYPE_HAND /* 1002 */:
                List<VineRecipient> recentRecipients = new ArrayList<>();
                List<VineRecipient> friendsRecipients = new ArrayList<>();
                List<VineRecipient> followingRecipients = new ArrayList<>();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        VineRecipient recipient = parseCurrentRecipientGroupedBySectionCursorRow(cursor);
                        if ("Recent".equals(recipient.sectionTitle)) {
                            recentRecipients.add(recipient);
                        } else if ("Friends".equals(recipient.sectionTitle)) {
                            friendsRecipients.add(recipient);
                        } else if ("Following".equals(recipient.sectionTitle)) {
                            followingRecipients.add(recipient);
                        }
                    }
                }
                this.mProvider.fireOnDefaultsChanged(recentRecipients, friendsRecipients, followingRecipients);
                break;
            default:
                SLog.d("Unknown loader. Ignoring loader load finished event.");
                break;
        }
    }

    private VineRecipient parseCurrentRecipientGroupedBySectionCursorRow(Cursor cursor) {
        long userId = cursor.getLong(0);
        String username = cursor.getString(1);
        String avatarUrl = cursor.getString(2);
        int profileBackgroundColor = cursor.getInt(3);
        int sectionIndex = cursor.getInt(4);
        String sectionTitle = cursor.getString(5);
        VineRecipient recipient = VineRecipient.fromUser(username, userId, profileBackgroundColor, -1L);
        recipient.avatarUrl = avatarUrl;
        recipient.sectionIndex = sectionIndex;
        recipient.sectionTitle = sectionTitle;
        return recipient;
    }

    private List<ContactEntry> parseContactEntriesCursor(Cursor cursor) {
        ContactEntry entry;
        ArrayList<ContactEntry> entries = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            ContactEntry previousEntry = null;
            if (!entries.isEmpty()) {
                ContactEntry previousEntry2 = entries.get(entries.size() - 1);
                previousEntry = previousEntry2;
            }
            String mimeType = cursor.getString(2);
            Long contactId = Long.valueOf(cursor.getLong(0));
            if (previousEntry != null && previousEntry.contactId == contactId.longValue()) {
                entry = previousEntry;
                entry.typeFlag = ("vnd.android.cursor.item/email_v2".equals(mimeType) ? 1 : 2) | entry.typeFlag;
            } else {
                entry = new ContactEntry(cursor);
                entries.add(entry);
            }
            int isMimeTypeEmail = "vnd.android.cursor.item/email_v2".equals(mimeType) ? 1 : 0;
            int isMimeTypePhone = "vnd.android.cursor.item/phone_v2".equals(mimeType) ? 2 : 0;
            entry.valueMimeMap.put(cursor.getString(3), Integer.valueOf(isMimeTypeEmail | isMimeTypePhone));
        }
        return entries;
    }

    @Override // android.app.LoaderManager.LoaderCallbacks
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
