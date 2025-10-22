package com.mobileapptracker;

import android.content.Context;
import android.os.Bundle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

/* loaded from: classes.dex */
class MATFBBridge {
    private static boolean justActivated = false;
    private static Object logger;

    public static void startLogger(Context context, boolean limitEventAndDataUsage) {
        String sdkVersion = getFbSdkVersion();
        startLoggerForVersion(sdkVersion, context, limitEventAndDataUsage);
    }

    private static String getFbSdkVersion() throws NoSuchMethodException, SecurityException {
        try {
            Method sdkVersionMethod = Class.forName("com.facebook.FacebookSdk").getMethod("getSdkVersion", new Class[0]);
            return (String) sdkVersionMethod.invoke(null, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Method sdkVersionMethod2 = Class.forName("com.facebook.Settings").getMethod("getSdkVersion", new Class[0]);
                return (String) sdkVersionMethod2.invoke(null, new Object[0]);
            } catch (Exception e1) {
                e1.printStackTrace();
                return "";
            }
        }
    }

    private static void startLoggerForVersion(String sdkVersion, Context context, boolean limitEventAndDataUsage) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (!sdkVersion.isEmpty()) {
            String appEventsLoggerClassName = "";
            String setLimitEventAndDataUsageClassName = "";
            if (sdkVersion.startsWith("4.")) {
                appEventsLoggerClassName = "com.facebook.appevents.AppEventsLogger";
                setLimitEventAndDataUsageClassName = "com.facebook.FacebookSdk";
            } else if (sdkVersion.startsWith("3.")) {
                appEventsLoggerClassName = "com.facebook.AppEventsLogger";
                setLimitEventAndDataUsageClassName = "com.facebook.Settings";
            }
            try {
                Class[] activateMethodParams = {Context.class};
                Method activateMethod = Class.forName(appEventsLoggerClassName).getMethod("activateApp", activateMethodParams);
                Object[] activateArgs = {context};
                activateMethod.invoke(null, activateArgs);
                justActivated = true;
                Class[] limitMethodParams = {Context.class, Boolean.TYPE};
                Method limitMethod = Class.forName(setLimitEventAndDataUsageClassName).getMethod("setLimitEventAndDataUsage", limitMethodParams);
                Object[] limitArgs = {context, Boolean.valueOf(limitEventAndDataUsage)};
                limitMethod.invoke(null, limitArgs);
                Method loggerMethod = Class.forName(appEventsLoggerClassName).getMethod("newLogger", activateMethodParams);
                logger = loggerMethod.invoke(null, activateArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void logEvent(MATEvent event) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (logger != null) {
            try {
                Class[] methodParams = {String.class, Double.TYPE, Bundle.class};
                Method method = logger.getClass().getMethod("logEvent", methodParams);
                String fbEventName = event.getEventName();
                double valueToSum = event.getRevenue();
                MATParameters tuneParams = MATParameters.getInstance();
                String eventNameLower = event.getEventName().toLowerCase(Locale.US);
                if (eventNameLower.contains("session")) {
                    if (!justActivated) {
                        fbEventName = "fb_mobile_activate_app";
                    } else {
                        return;
                    }
                } else if (eventNameLower.contains(MATEvent.REGISTRATION)) {
                    fbEventName = "fb_mobile_complete_registration";
                } else if (eventNameLower.contains(MATEvent.CONTENT_VIEW)) {
                    fbEventName = "fb_mobile_content_view";
                } else if (eventNameLower.contains(MATEvent.SEARCH)) {
                    fbEventName = "fb_mobile_search";
                } else if (eventNameLower.contains(MATEvent.RATED)) {
                    fbEventName = "fb_mobile_rate";
                    try {
                        valueToSum = event.getRating();
                    } catch (Exception e) {
                    }
                } else if (eventNameLower.contains(MATEvent.TUTORIAL_COMPLETE)) {
                    fbEventName = "fb_mobile_tutorial_completion";
                } else if (eventNameLower.contains(MATEvent.ADD_TO_CART)) {
                    fbEventName = "fb_mobile_add_to_cart";
                } else if (eventNameLower.contains(MATEvent.ADD_TO_WISHLIST)) {
                    fbEventName = "fb_mobile_add_to_wishlist";
                } else if (eventNameLower.contains(MATEvent.CHECKOUT_INITIATED)) {
                    fbEventName = "fb_mobile_initiated_checkout";
                } else if (eventNameLower.contains(MATEvent.ADDED_PAYMENT_INFO)) {
                    fbEventName = "fb_mobile_add_payment_info";
                } else if (eventNameLower.contains(MATEvent.PURCHASE)) {
                    fbEventName = "fb_mobile_purchase";
                } else if (eventNameLower.contains(MATEvent.LEVEL_ACHIEVED)) {
                    fbEventName = "fb_mobile_level_achieved";
                } else if (eventNameLower.contains(MATEvent.ACHIEVEMENT_UNLOCKED)) {
                    fbEventName = "fb_mobile_achievement_unlocked";
                } else if (eventNameLower.contains(MATEvent.SPENT_CREDITS)) {
                    fbEventName = "fb_mobile_spent_credits";
                    try {
                        valueToSum = event.getQuantity();
                    } catch (Exception e2) {
                    }
                }
                Bundle bundle = new Bundle();
                addBundleValue(bundle, "fb_currency", event.getCurrencyCode());
                addBundleValue(bundle, "fb_content_id", event.getContentId());
                addBundleValue(bundle, "fb_content_type", event.getContentType());
                addBundleValue(bundle, "fb_search_string", event.getSearchString());
                addBundleValue(bundle, "fb_num_items", Integer.toString(event.getQuantity()));
                addBundleValue(bundle, "fb_level", Integer.toString(event.getLevel()));
                addBundleValue(bundle, "tune_referral_source", tuneParams.getReferralSource());
                addBundleValue(bundle, "tune_source_sdk", "TUNE-MAT");
                Object[] args = {fbEventName, Double.valueOf(valueToSum), bundle};
                method.invoke(logger, args);
                justActivated = false;
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    private static void addBundleValue(Bundle bundle, String key, String value) {
        if (value != null) {
            bundle.putString(key, value);
        }
    }
}
