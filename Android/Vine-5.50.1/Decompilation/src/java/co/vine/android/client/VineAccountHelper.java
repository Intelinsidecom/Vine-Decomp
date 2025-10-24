package co.vine.android.client;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import co.vine.android.VineLoggingException;
import co.vine.android.api.VineLogin;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.SystemUtil;
import com.edisonwang.android.slog.SLog;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public class VineAccountHelper {
    public static final String ACCOUNT_TYPE = BuildUtil.getAuthority(".auth");

    public static Account getAccount(Context context, long userId, String accountName) {
        AccountManager am = AccountManager.get(context);
        if (am != null) {
            Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
            int length = accounts.length;
            for (int i = 0; i < length; i++) {
                Account account = accounts[i];
                if (account.name.equals(accountName) || account.name.equals(String.valueOf(userId))) {
                    return account;
                }
            }
        }
        return null;
    }

    public static Account createAccount(Context context, String uniqueLogin, VineLogin vl, String password, String accountName) throws VineLoggingException {
        AccountManager am = AccountManager.get(context);
        Account account = getAccount(context, vl.userId, uniqueLogin);
        if (account == null) {
            account = new Account(String.valueOf(vl.userId), ACCOUNT_TYPE);
            if (am == null) {
                return null;
            }
            try {
                if (!am.addAccountExplicitly(account, null, null)) {
                    return null;
                }
            } catch (SecurityException e) {
                throw new VineLoggingException(e);
            }
        }
        switch (vl.loginType) {
            case 1:
                try {
                    am.setPassword(account, encrypt(getDeviceId(context), password));
                    am.setUserData(account, "account_password_encrypted", String.valueOf(true));
                } catch (Exception e2) {
                    SLog.e("Error encrypting password.", (Throwable) e2);
                    am.setPassword(account, password);
                }
                am.setUserData(account, "account_login_type", String.valueOf(1));
                break;
            case 2:
                am.setUserData(account, "account_t_token", vl.twitterToken);
                am.setUserData(account, "account_t_secret", vl.twitterSecret);
                am.setUserData(account, "account_t_id", String.valueOf(vl.twitterUserId));
                am.setUserData(account, "account_t_username", vl.twitterUsername);
                am.setUserData(account, "account_login_type", String.valueOf(2));
                break;
        }
        am.setUserData(account, "account_user_name", accountName);
        am.setUserData(account, "account_user_info", String.valueOf(vl.userId));
        am.setAuthToken(account, "co.vine.android.basic_auth.token.secret", vl.key);
        return account;
    }

    public static AccountManagerFuture<Boolean> removeAccount(Context context, long userId, String accountName) {
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
        for (Account account : accounts) {
            if (account.name.equals(accountName) || account.name.equals(String.valueOf(userId))) {
                return am.removeAccount(account, null, null);
            }
        }
        return null;
    }

    public static void saveTwitterInfo(Context context, long userId, String accountName, String username, String token, String secret, long twitterUserId) {
        Account account = getAccount(context, userId, accountName);
        if (account != null) {
            AccountManager am = AccountManager.get(context);
            am.setUserData(account, "account_t_token", token);
            am.setUserData(account, "account_t_secret", secret);
            am.setUserData(account, "account_t_id", String.valueOf(twitterUserId));
            am.setUserData(account, "account_t_username", username);
            am.setUserData(account, "account_login_type", String.valueOf(2));
        }
    }

    public static void removeTwitterInfo(Context context, long userId, String accountName) {
        Account account = getAccount(context, userId, accountName);
        if (account != null) {
            AccountManager am = AccountManager.get(context);
            am.setUserData(account, "account_t_token", null);
            am.setUserData(account, "account_t_secret", null);
            am.setUserData(account, "account_t_id", null);
            am.setUserData(account, "account_t_username", null);
            am.setUserData(account, "account_login_type", String.valueOf(1));
        }
    }

    public static void setSessionKey(AccountManager am, Account account, String key) {
        am.setAuthToken(account, "co.vine.android.basic_auth.token.secret", key);
    }

    public static String getSessionKey(AccountManager am, Account account) {
        return am.peekAuthToken(account, "co.vine.android.basic_auth.token.secret");
    }

    @Deprecated
    public static long getUserId(AccountManager am, Account account) {
        String userIdS = am.getUserData(account, "account_user_info");
        if (userIdS != null) {
            return Long.parseLong(userIdS);
        }
        return 0L;
    }

    @Deprecated
    public static String getAvatarUrl(AccountManager am, Account account) {
        return am.getUserData(account, "account_user_avatar");
    }

    @Deprecated
    public static String getName(AccountManager am, Account account) {
        return am.getUserData(account, "account_user_name");
    }

    public static Integer getLoginType(AccountManager am, Account account) {
        String data = am.getUserData(account, "account_login_type");
        if (data != null) {
            return Integer.valueOf(Integer.parseInt(data));
        }
        return null;
    }

    public static String getPassword(AccountManager am, Account account, Context context) {
        String password = am.getPassword(account);
        String passwordFlag = am.getUserData(account, "account_password_encrypted");
        if (passwordFlag != null && Boolean.parseBoolean(passwordFlag)) {
            try {
                password = decrypt(getDeviceId(context), password);
            } catch (Exception e) {
                SLog.e("Error decrypting password.", (Throwable) e);
                CrashUtil.logException(e);
                return null;
            }
        } else if (password != null) {
            migratePassword(am, account, context);
        }
        return password;
    }

    public static String getTwitterToken(AccountManager am, Account account) {
        return am.getUserData(account, "account_t_token");
    }

    public static String getTwitterSecret(AccountManager am, Account account) {
        return am.getUserData(account, "account_t_secret");
    }

    public static String getTwitterUsername(AccountManager am, Account account) {
        return am.getUserData(account, "account_t_username");
    }

    public static String getTwitterUserId(AccountManager am, Account account) {
        return am.getUserData(account, "account_t_id");
    }

    public static boolean needsAddwidget(AccountManager am, Account account) {
        String data = am.getUserData(account, "account_add_widget");
        return data == null;
    }

    public static void setDidShowAddWidget(AccountManager am, Account account) {
        am.setUserData(account, "account_add_widget", "YES");
    }

    public static boolean shouldShowStoreContactsPrompt(AccountManager am, Account account) {
        if (am == null || account == null) {
            return true;
        }
        String data = am.getUserData(account, "account_did_show_store_contacts");
        return data == null;
    }

    public static void setDidShowStoreContactsPrompt(AccountManager am, Account account) {
        if (am != null && account != null) {
            am.setUserData(account, "account_did_show_store_contacts", "YES");
        }
    }

    public static void syncNormalVideoPlayable(Context context) {
        SystemUtil.PrefBooleanState accountStatus = isNormalVideoPlayable(context);
        SystemUtil.PrefBooleanState preferenceStatus = SystemUtil.isNormalVideoPlayable(context);
        if (accountStatus != preferenceStatus) {
            if (preferenceStatus != SystemUtil.PrefBooleanState.UNKNOWN) {
                AccountManager am = AccountManager.get(context);
                Account account = getActiveAccount(context);
                if (am != null && account != null) {
                    am.setUserData(account, "account_normal_video_playable", preferenceStatus.name());
                    return;
                }
                return;
            }
            SystemUtil.setNormalVideoPlayable(context, accountStatus == SystemUtil.PrefBooleanState.TRUE);
        }
    }

    public static SystemUtil.PrefBooleanState isNormalVideoPlayable(Context activity) {
        AccountManager am = AccountManager.get(activity);
        if (am == null) {
            return SystemUtil.PrefBooleanState.UNKNOWN;
        }
        Account account = getActiveAccount(activity);
        String data = null;
        if (account != null) {
            data = am.getUserData(account, "account_normal_video_playable");
        }
        if (data != null) {
            return SystemUtil.PrefBooleanState.valueOf(data);
        }
        return SystemUtil.PrefBooleanState.UNKNOWN;
    }

    public static Account getActiveAccount(Context context) {
        Session session = AppController.getInstance(context).getActiveSession();
        return getAccount(context, session.getUserId(), session.getUsername());
    }

    private static String encrypt(String key, String cleartext) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(getKeyDigest(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, secretKeySpec);
        byte[] encrypted = cipher.doFinal(cleartext.getBytes("UTF-8"));
        return Base64.encodeToString(encrypted, 0);
    }

    private static String decrypt(String key, String encrypted) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(getKeyDigest(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, secretKeySpec);
        byte[] decrypted = cipher.doFinal(Base64.decode(encrypted, 0));
        return new String(decrypted, "UTF-8");
    }

    public static byte[] getKeyDigest(String key) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(key.getBytes("UTF-8"));
        return messageDigest.digest();
    }

    private static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        return (telephonyManager == null || telephonyManager.getDeviceId() == null) ? Build.SERIAL : telephonyManager.getDeviceId();
    }

    public static void migratePassword(AccountManager am, Account account, Context context) {
        String password;
        if (am == null || account == null) {
            SLog.i("Skipping migrate password check due to null AccountManager or Account");
            return;
        }
        String passwordFlag = am.getUserData(account, "account_password_encrypted");
        if ((passwordFlag == null || !Boolean.parseBoolean(passwordFlag)) && (password = am.getPassword(account)) != null) {
            try {
                am.setPassword(account, encrypt(getDeviceId(context), password));
                am.setUserData(account, "account_password_encrypted", String.valueOf(true));
            } catch (Exception e) {
                SLog.e("Error migrating password.", (Throwable) e);
                CrashUtil.logException(e);
            }
        }
    }

    public static void saveTumblrInfo(Context context, String oauthToken, String oauthSecret) {
        Account account = getActiveAccount(context);
        AccountManager am = AccountManager.get(context);
        if (account != null && am != null) {
            am.setUserData(account, "tumblr_token", oauthToken);
            am.setUserData(account, "tumblr_secret", oauthSecret);
        }
    }

    public static String getTumblrToken(Account account, AccountManager am) {
        if (account == null || am == null) {
            return null;
        }
        return am.getUserData(account, "tumblr_token");
    }

    public static String getTumblrSecret(Account account, AccountManager am) {
        if (account == null || am == null) {
            return null;
        }
        return am.getUserData(account, "tumblr_secret");
    }

    public static void removeTumblrTokens(Context context) {
        Account account = getActiveAccount(context);
        AccountManager am = AccountManager.get(context);
        if (account != null && am != null) {
            am.setUserData(account, "tumblr_token", "");
            am.setUserData(account, "tumblr_secret", "");
        }
    }
}
