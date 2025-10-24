package co.vine.android.nux;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import co.vine.android.ChannelsListNUXActivity;
import co.vine.android.ExploreVideoListActivity;
import co.vine.android.FindFriendsNUXActivity;
import co.vine.android.StartActivity;
import co.vine.android.util.ClientFlagsHelper;

/* loaded from: classes.dex */
public final class NuxResolver {
    public static void toNuxFromSignup(Activity activity) {
        if (ClientFlagsHelper.isNuxHideChannelPickerEnabled(activity)) {
            toNuxFromChannelList(activity);
        } else {
            activity.startActivity(new Intent(activity, (Class<?>) ChannelsListNUXActivity.class));
        }
    }

    public static void toNuxFromChannelList(Activity activity) {
        if (ClientFlagsHelper.isNuxHideFriendsFinderEnabled(activity)) {
            toNuxFromFindFriends(activity);
        } else {
            FindFriendsNUXActivity.start(activity);
        }
    }

    public static void toNuxFromFindFriends(Activity activity) {
        if (ClientFlagsHelper.isNuxShowWelcomeFeedEnabled(activity)) {
            Uri parsedUri = Uri.parse("vine://welcome-feed");
            ExploreVideoListActivity.start(activity, parsedUri);
        } else {
            toNuxFromWelcomeFeed(activity);
        }
    }

    public static void toNuxFromWelcomeFeed(Activity activity) {
        StartActivity.toStart(activity, true);
    }
}
