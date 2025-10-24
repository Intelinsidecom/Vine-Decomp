package com.facebook.appevents;

import android.os.Bundle;
import com.facebook.LoggingBehavior;
import com.facebook.internal.Logger;
import java.io.Serializable;
import java.util.Locale;

/* loaded from: classes2.dex */
class FacebookTimeSpentData implements Serializable {
    private static final long serialVersionUID = 1;
    private String firstOpenSourceApplication;
    private int interruptionCount;
    private boolean isAppActive;
    private boolean isWarmLaunch;
    private long lastActivateEventLoggedTime;
    private long lastResumeTime;
    private long lastSuspendTime;
    private long millisecondsSpentInSession;
    private static final String TAG = AppEventsLogger.class.getCanonicalName();
    private static final long[] INACTIVE_SECONDS_QUANTA = {300000, 900000, 1800000, 3600000, 21600000, 43200000, 86400000, 172800000, 259200000, 604800000, 1209600000, 1814400000, 2419200000L, 5184000000L, 7776000000L, 10368000000L, 12960000000L, 15552000000L, 31536000000L};

    private static class SerializationProxyV2 implements Serializable {
        private static final long serialVersionUID = 6;
        private final String firstOpenSourceApplication;
        private final int interruptionCount;
        private final long lastResumeTime;
        private final long lastSuspendTime;
        private final long millisecondsSpentInSession;

        SerializationProxyV2(long lastResumeTime, long lastSuspendTime, long millisecondsSpentInSession, int interruptionCount, String firstOpenSourceApplication) {
            this.lastResumeTime = lastResumeTime;
            this.lastSuspendTime = lastSuspendTime;
            this.millisecondsSpentInSession = millisecondsSpentInSession;
            this.interruptionCount = interruptionCount;
            this.firstOpenSourceApplication = firstOpenSourceApplication;
        }

        private Object readResolve() {
            return new FacebookTimeSpentData(this.lastResumeTime, this.lastSuspendTime, this.millisecondsSpentInSession, this.interruptionCount, this.firstOpenSourceApplication);
        }
    }

    FacebookTimeSpentData() {
        resetSession();
    }

    private FacebookTimeSpentData(long lastResumeTime, long lastSuspendTime, long millisecondsSpentInSession, int interruptionCount, String firstOpenSourceApplication) {
        resetSession();
        this.lastResumeTime = lastResumeTime;
        this.lastSuspendTime = lastSuspendTime;
        this.millisecondsSpentInSession = millisecondsSpentInSession;
        this.interruptionCount = interruptionCount;
        this.firstOpenSourceApplication = firstOpenSourceApplication;
    }

    private Object writeReplace() {
        return new SerializationProxyV2(this.lastResumeTime, this.lastSuspendTime, this.millisecondsSpentInSession, this.interruptionCount, this.firstOpenSourceApplication);
    }

    void onResume(AppEventsLogger logger, long eventTime, String sourceApplicationInfo) {
        long interruptionDurationMillis;
        if (isColdLaunch() || eventTime - this.lastActivateEventLoggedTime > 300000) {
            Bundle eventParams = new Bundle();
            eventParams.putString("fb_mobile_launch_source", sourceApplicationInfo);
            logger.logEvent("fb_mobile_activate_app", eventParams);
            this.lastActivateEventLoggedTime = eventTime;
        }
        if (this.isAppActive) {
            Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Resume for active app");
            return;
        }
        if (wasSuspendedEver()) {
            interruptionDurationMillis = eventTime - this.lastSuspendTime;
        } else {
            interruptionDurationMillis = 0;
        }
        if (interruptionDurationMillis < 0) {
            Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Clock skew detected");
            interruptionDurationMillis = 0;
        }
        if (interruptionDurationMillis > 60000) {
            logAppDeactivatedEvent(logger, interruptionDurationMillis);
        } else if (interruptionDurationMillis > 1000) {
            this.interruptionCount++;
        }
        if (this.interruptionCount == 0) {
            this.firstOpenSourceApplication = sourceApplicationInfo;
        }
        this.lastResumeTime = eventTime;
        this.isAppActive = true;
    }

    private void logAppDeactivatedEvent(AppEventsLogger logger, long interruptionDurationMillis) {
        Bundle eventParams = new Bundle();
        eventParams.putInt("fb_mobile_app_interruptions", this.interruptionCount);
        eventParams.putString("fb_mobile_time_between_sessions", String.format(Locale.ROOT, "session_quanta_%d", Integer.valueOf(getQuantaIndex(interruptionDurationMillis))));
        eventParams.putString("fb_mobile_launch_source", this.firstOpenSourceApplication);
        logger.logEvent("fb_mobile_deactivate_app", this.millisecondsSpentInSession / 1000, eventParams);
        resetSession();
    }

    private static int getQuantaIndex(long timeBetweenSessions) {
        int quantaIndex = 0;
        while (quantaIndex < INACTIVE_SECONDS_QUANTA.length && INACTIVE_SECONDS_QUANTA[quantaIndex] < timeBetweenSessions) {
            quantaIndex++;
        }
        return quantaIndex;
    }

    private void resetSession() {
        this.isAppActive = false;
        this.lastResumeTime = -1L;
        this.lastSuspendTime = -1L;
        this.interruptionCount = 0;
        this.millisecondsSpentInSession = 0L;
    }

    private boolean wasSuspendedEver() {
        return this.lastSuspendTime != -1;
    }

    private boolean isColdLaunch() {
        boolean result = !this.isWarmLaunch;
        this.isWarmLaunch = true;
        return result;
    }
}
