package co.vine.android.analytics;

import com.flurry.android.FlurryAgent;
import java.util.HashMap;

/* loaded from: classes.dex */
public class FlurryEvent {
    private final String mEventName;
    public HashMap<String, String> mInfo;

    public static void log(String eventName, String key, Object value, String key2, Object value2) {
        new FlurryEvent(eventName).add(key, value).add(key2, value2).log();
    }

    public static void log(String eventName, String key, Object value) {
        new FlurryEvent(eventName).add(key, value).log();
    }

    public static void log(String event) {
        FlurryAgent.logEvent(event);
    }

    public FlurryEvent add(String name, Object value) {
        if (this.mInfo == null) {
            this.mInfo = new HashMap<>();
        }
        this.mInfo.put(name, String.valueOf(value));
        return this;
    }

    public FlurryEvent(String eventName) {
        this.mEventName = eventName;
    }

    public void log() {
        if (this.mInfo != null) {
            FlurryAgent.logEvent(this.mEventName, this.mInfo);
        } else {
            FlurryAgent.logEvent(this.mEventName);
        }
    }
}
