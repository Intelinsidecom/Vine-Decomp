package co.vine.android.share.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.text.TextUtils;
import co.vine.android.client.Session;
import co.vine.android.client.SessionManager;
import co.vine.android.client.VineAccountHelper;

/* loaded from: classes.dex */
public class TwitterAuthUtil {
    public static boolean isTwitterConnected(Context context) {
        if (context == null) {
            return false;
        }
        Session session = SessionManager.getSharedInstance().getCurrentSession();
        Account account = VineAccountHelper.getAccount(context, session.getUserId(), session.getUsername());
        AccountManager accountManager = AccountManager.get(context);
        return (account == null || accountManager == null || TextUtils.isEmpty(VineAccountHelper.getTwitterToken(accountManager, account))) ? false : true;
    }
}
