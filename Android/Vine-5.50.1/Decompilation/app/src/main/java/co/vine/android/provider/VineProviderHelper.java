package co.vine.android.provider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import co.vine.android.api.VineRecipient;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineDatabaseSQL;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class VineProviderHelper {
    public static ArrayList<VineRecipient> getConversationRecipients(ContentResolver resolver, long conversationRowId) {
        ArrayList<VineRecipient> recipients = new ArrayList<>();
        Cursor cursor = queryConversationRecipientsUsersView(resolver, conversationRowId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long userRowId = cursor.getLong(2);
                long userId = cursor.getLong(6);
                String email = cursor.getString(5);
                String phone = cursor.getString(4);
                String display = cursor.getString(3);
                if (userId > 0) {
                    recipients.add(VineRecipient.fromUser(display, userId, 0, userRowId));
                } else if (!TextUtils.isEmpty(email)) {
                    recipients.add(VineRecipient.fromEmail(display, -1L, email, userRowId));
                } else if (!TextUtils.isEmpty(phone)) {
                    recipients.add(VineRecipient.fromPhone(display, -1L, phone, userRowId));
                }
            }
            cursor.close();
        }
        return recipients;
    }

    public static Cursor queryConversationRecipientsUsersView(ContentResolver resolver, long conversationRowId) {
        String conversationRowIdString = String.valueOf(conversationRowId);
        Uri contentUri = Vine.ConversationRecipientsUsersView.CONTENT_URI_CONVERSATION.buildUpon().appendQueryParameter("conversation_row_id", conversationRowIdString).build();
        return resolver.query(contentUri, VineDatabaseSQL.ConversationRecipientsUsersViewQuery.PROJECTION, null, null, null);
    }
}
