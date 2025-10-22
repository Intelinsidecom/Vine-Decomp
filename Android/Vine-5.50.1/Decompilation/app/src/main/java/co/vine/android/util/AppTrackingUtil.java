package co.vine.android.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import co.vine.android.client.Session;
import co.vine.android.client.VineAccountHelper;
import com.mobileapptracker.MATEvent;
import com.mobileapptracker.MobileAppTracker;

/* loaded from: classes.dex */
public final class AppTrackingUtil {
    public static void initialize(Application mAppContext) {
        SharedPreferences sharedPref = mAppContext.getSharedPreferences("normalVideoTestPlayable", 0);
        boolean firstRun = sharedPref.getAll().size() == 0;
        MobileAppTracker mobileAppTracker = MobileAppTracker.init(mAppContext, "20954", "7aff069a84a0aafe549367906810d6bc");
        mobileAppTracker.setExistingUser(firstRun ? false : true);
    }

    public static void sendAppOpenedMessage(Activity activity) {
        MobileAppTracker.getInstance().setReferralSources(activity);
        MobileAppTracker.getInstance().measureSession();
    }

    public static void logUserRegistration(Context context, Session session) {
        AccountManager am = AccountManager.get(context);
        Account acc = VineAccountHelper.getAccount(context, session.getUserId(), session.getUsername());
        if (am != null && acc != null) {
            MobileAppTracker tracker = MobileAppTracker.getInstance();
            if (session.getScreenName() != null) {
                tracker.setUserName(session.getScreenName());
            }
            if (session.getUserId() != 0) {
                tracker.setUserId(Long.toString(session.getUserId()));
            }
            if (VineAccountHelper.getLoginType(am, acc).intValue() == 2) {
                tracker.setTwitterUserId(VineAccountHelper.getTwitterUserId(am, acc));
            } else if (session.getUsername() != null) {
                tracker.setUserEmail(session.getUsername());
            }
            tracker.measureEvent(MATEvent.REGISTRATION);
        }
    }
}
