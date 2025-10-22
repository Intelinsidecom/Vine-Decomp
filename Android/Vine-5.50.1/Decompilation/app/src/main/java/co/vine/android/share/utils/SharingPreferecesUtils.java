package co.vine.android.share.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import co.vine.android.StandalonePreference;
import co.vine.android.client.AppController;
import co.vine.android.share.widgets.VineToggleRow;
import co.vine.android.social.FacebookHelper;
import co.vine.android.social.TumblrHelper;

/* loaded from: classes.dex */
public class SharingPreferecesUtils {
    public static void saveSharingPreferences(Context context, boolean twitterSelected, boolean facebookSelected, boolean tumblrSelected) {
        SharedPreferences sharingPrefs;
        if (context != null && (sharingPrefs = StandalonePreference.SHARING.getPref(context)) != null) {
            SharedPreferences.Editor editor = sharingPrefs.edit();
            editor.putBoolean("twitter_selected", twitterSelected).putBoolean("facebook_selected", facebookSelected).putBoolean("tumblr_selected", tumblrSelected).apply();
        }
    }

    public static void saveSharingPreferences(Bundle bundle, boolean vineEnabled, boolean vineSelected, boolean twitterEnabled, boolean twitterSelected, boolean facebookEnabled, boolean facebookSelected, boolean tumblrEnabled, boolean tumblrSelected) {
        bundle.putBoolean("vine_enabled", vineEnabled);
        bundle.putBoolean("vine_selected", vineSelected);
        bundle.putBoolean("twitter_enabled", twitterEnabled);
        bundle.putBoolean("twitter_selected", twitterSelected);
        bundle.putBoolean("facebook_enabled", facebookEnabled);
        bundle.putBoolean("facebook_selected", facebookSelected);
        bundle.putBoolean("tumblr_enabled", tumblrEnabled);
        bundle.putBoolean("tumblr_selected", tumblrSelected);
    }

    private static boolean getBundledVineEnablement(Bundle bundle) {
        return bundle.getBoolean("vine_enabled", false);
    }

    private static boolean getBundledVineSharePreference(Bundle bundle) {
        return bundle.getBoolean("vine_selected", false);
    }

    public static boolean getDefaultTwitterSharePreference(AppController appController, Context context) {
        if (context == null) {
            return false;
        }
        SharedPreferences sharingPrefs = StandalonePreference.SHARING.getPref(context);
        if (appController == null || sharingPrefs == null || !TwitterAuthUtil.isTwitterConnected(context)) {
            return false;
        }
        return sharingPrefs.getBoolean("twitter_selected", true);
    }

    private static boolean getBundledTwitterEnablement(Bundle savedInstanceState) {
        return savedInstanceState.getBoolean("twitter_enabled", true);
    }

    private static boolean getBundledTwitterSharePreference(Bundle savedInstanceState) {
        return savedInstanceState.getBoolean("twitter_selected", true);
    }

    public static boolean getDefaultFacebookSharePreference(Context context) {
        SharedPreferences sharingPrefs;
        return context != null && (sharingPrefs = StandalonePreference.SHARING.getPref(context)) != null && (context instanceof Activity) && FacebookHelper.isFacebookConnected(context) && sharingPrefs.getBoolean("facebook_selected", false);
    }

    private static boolean getBundledFacebookEnablement(Bundle savedInstanceState) {
        return savedInstanceState.getBoolean("facebook_enabled", false);
    }

    private static boolean getBundledFacebookSharePreference(Bundle savedInstanceState) {
        return savedInstanceState.getBoolean("facebook_selected", false);
    }

    public static boolean getTumblrSharePreference(Context context) {
        SharedPreferences sharingPrefs;
        if (context == null || (sharingPrefs = StandalonePreference.SHARING.getPref(context)) == null || !TumblrHelper.isTumblrConnected(context)) {
            return false;
        }
        return sharingPrefs.getBoolean("tumblr_selected", false);
    }

    private static boolean getBundledTumblrEnablement(Bundle savedInstanceState) {
        return savedInstanceState.getBoolean("tumblr_enabled", false);
    }

    private static boolean getBundledTumblrSharePreference(Bundle savedInstanceState) {
        return savedInstanceState.getBoolean("tumblr_selected", false);
    }

    public static void restoreShareOptions(Bundle savedInstanceState, VineToggleRow vineToggleRow, VineToggleRow twitterToggleRow, VineToggleRow facebookToggleRow, VineToggleRow tumblrToggleRow) {
        twitterToggleRow.setEnabled(getBundledTwitterEnablement(savedInstanceState));
        twitterToggleRow.setCheckedWithoutEvent(getBundledTwitterSharePreference(savedInstanceState));
        facebookToggleRow.setEnabled(getBundledFacebookEnablement(savedInstanceState));
        facebookToggleRow.setCheckedWithoutEvent(getBundledFacebookSharePreference(savedInstanceState));
        tumblrToggleRow.setEnabled(getBundledTumblrEnablement(savedInstanceState));
        tumblrToggleRow.setCheckedWithoutEvent(getBundledTumblrSharePreference(savedInstanceState));
        vineToggleRow.setEnabled(getBundledVineEnablement(savedInstanceState));
        vineToggleRow.setCheckedWithoutEvent(getBundledVineSharePreference(savedInstanceState));
    }
}
