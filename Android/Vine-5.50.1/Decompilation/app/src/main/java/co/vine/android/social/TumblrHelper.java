package co.vine.android.social;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.text.TextUtils;
import co.vine.android.client.VineAccountHelper;

/* loaded from: classes.dex */
public class TumblrHelper {
    public static boolean isTumblrConnected(Context context) {
        return (TextUtils.isEmpty(getTumblrToken(context)) || TextUtils.isEmpty(getTumblrSecret(context))) ? false : true;
    }

    public static String getTumblrToken(Context context) {
        Account account = VineAccountHelper.getActiveAccount(context);
        AccountManager am = AccountManager.get(context);
        return VineAccountHelper.getTumblrToken(account, am);
    }

    public static String getTumblrSecret(Context context) {
        Account account = VineAccountHelper.getActiveAccount(context);
        AccountManager am = AccountManager.get(context);
        return VineAccountHelper.getTumblrSecret(account, am);
    }
}
