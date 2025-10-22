package co.vine.android.util;

import android.content.Context;
import co.vine.android.R;
import co.vine.android.client.AppController;

/* loaded from: classes.dex */
public class ShareUtils {
    public static String getSmsMessage(Context context) {
        BuildUtil.getMarket();
        return getShareMyProfileMessage(context, R.string.find_friends_invite_sms);
    }

    public static String getEmailMessage(Context context) {
        BuildUtil.getMarket();
        return getShareMyProfileMessage(context, R.string.find_friends_invite_email);
    }

    private static String getShareMyProfileMessage(Context context, int stringId) {
        return context.getString(stringId, String.valueOf(AppController.getInstance(context).getActiveId())).replace("vine://user", BuildUtil.getWebsiteUrl(context) + "/u");
    }
}
