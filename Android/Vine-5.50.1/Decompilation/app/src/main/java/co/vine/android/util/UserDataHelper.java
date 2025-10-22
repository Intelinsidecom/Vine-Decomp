package co.vine.android.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/* loaded from: classes.dex */
public class UserDataHelper {
    private final Context mContext;

    public UserDataHelper(Context context) {
        this.mContext = context;
    }

    public boolean getBoolean(Account account, String key, boolean defaultValue) {
        String data;
        return (account == null || (data = am().getUserData(account, key)) == null) ? defaultValue : Boolean.parseBoolean(data);
    }

    public void setBoolean(Account account, String key, boolean value) {
        am().setUserData(account, key, Boolean.toString(value));
    }

    public long getLong(Account account, String key, long defaultValue) {
        String data;
        if (account != null && (data = am().getUserData(account, key)) != null) {
            return Long.parseLong(data);
        }
        return defaultValue;
    }

    public void setLong(Account account, String key, long value) {
        am().setUserData(account, key, Long.toString(value));
    }

    private AccountManager am() {
        return (AccountManager) this.mContext.getSystemService("account");
    }
}
