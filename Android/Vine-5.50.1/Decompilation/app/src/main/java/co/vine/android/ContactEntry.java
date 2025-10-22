package co.vine.android;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ContactEntry implements Comparable<ContactEntry> {
    public long contactId;
    public String displayName;
    public int typeFlag;
    public HashMap<String, Integer> valueMimeMap = new HashMap<>();
    public static final Uri URI = ContactsContract.Data.CONTENT_URI;
    public static final String[] PROJECTION = {"contact_id", "display_name", "mimetype", "data1"};
    public static final String[] SELECTION_ARGS = {"vnd.android.cursor.item/email_v2", "vnd.android.cursor.item/phone_v2"};

    public ContactEntry(Cursor cursor) {
        this.contactId = cursor.getLong(0);
        this.displayName = cursor.getString(1);
        this.typeFlag = "vnd.android.cursor.item/email_v2".equals(cursor.getString(2)) ? 1 : 2;
    }

    @Override // java.lang.Comparable
    public int compareTo(ContactEntry contactEntry) {
        return getCompareResult(this.displayName, contactEntry.displayName);
    }

    private int getCompareResult(String lhs, String rhs) {
        if (lhs == null && rhs == null) {
            return 0;
        }
        if (lhs == null) {
            return 1;
        }
        if (rhs == null) {
            return -1;
        }
        String lhs2 = lhs.toLowerCase();
        String rhs2 = rhs.toLowerCase();
        boolean thisStartsWithLetter = lhs2.length() > 0 && Character.isLetter(lhs2.charAt(0));
        boolean thatStartsWithLetter = rhs2.length() > 0 && Character.isLetter(rhs2.charAt(0));
        if (!(thisStartsWithLetter && thatStartsWithLetter) && (thisStartsWithLetter || thatStartsWithLetter)) {
            return thisStartsWithLetter ? -1 : 1;
        }
        return lhs2.compareTo(rhs2);
    }

    public boolean isSameSection(ContactEntry that) {
        if (this.displayName == null && that.displayName == null) {
            return true;
        }
        if (this.displayName == null || that.displayName == null) {
            return false;
        }
        return this.displayName.toLowerCase().charAt(0) == that.displayName.toLowerCase().charAt(0);
    }
}
