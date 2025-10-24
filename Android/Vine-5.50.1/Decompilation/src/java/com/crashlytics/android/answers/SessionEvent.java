package com.crashlytics.android.answers;

import android.app.Activity;
import java.util.Collections;
import java.util.Map;

/* loaded from: classes.dex */
final class SessionEvent {
    public final Map<String, Object> customAttributes;
    public final String customType;
    public final Map<String, String> details;
    public final SessionEventMetadata sessionEventMetadata;
    private String stringRepresentation;
    public final long timestamp;
    public final Type type;

    enum Type {
        CREATE,
        START,
        RESUME,
        SAVE_INSTANCE_STATE,
        PAUSE,
        STOP,
        DESTROY,
        ERROR,
        CRASH,
        INSTALL,
        CUSTOM
    }

    public static SessionEvent buildActivityLifecycleEvent(SessionEventMetadata metadata, Type type, Activity activity) {
        Map<String, String> details = Collections.singletonMap("activity", activity.getClass().getName());
        return buildEvent(metadata, type, details);
    }

    public static SessionEvent buildInstallEvent(SessionEventMetadata metadata) {
        return buildEvent(metadata, Type.INSTALL, Collections.emptyMap());
    }

    public static SessionEvent buildErrorEvent(SessionEventMetadata metadata, String sessionId) {
        Map<String, String> details = Collections.singletonMap("sessionId", sessionId);
        return buildEvent(metadata, Type.ERROR, details);
    }

    public static SessionEvent buildCrashEvent(SessionEventMetadata metadata, String sessionId) {
        Map<String, String> details = Collections.singletonMap("sessionId", sessionId);
        return buildEvent(metadata, Type.CRASH, details);
    }

    private static SessionEvent buildEvent(SessionEventMetadata metadata, Type type, Map<String, String> details) {
        return buildEvent(metadata, type, details, null, Collections.emptyMap());
    }

    private static SessionEvent buildEvent(SessionEventMetadata metadata, Type type, Map<String, String> details, String customType, Map<String, Object> customAttributes) {
        return new SessionEvent(metadata, System.currentTimeMillis(), type, details, customType, customAttributes);
    }

    private SessionEvent(SessionEventMetadata sessionEventMetadata, long timestamp, Type type, Map<String, String> details, String customType, Map<String, Object> customAttributes) {
        this.sessionEventMetadata = sessionEventMetadata;
        this.timestamp = timestamp;
        this.type = type;
        this.details = details;
        this.customType = customType;
        this.customAttributes = customAttributes;
    }

    public String toString() {
        if (this.stringRepresentation == null) {
            StringBuilder sb = new StringBuilder().append("[").append(getClass().getSimpleName()).append(": ").append("timestamp=").append(this.timestamp).append(", type=").append(this.type).append(", details=").append(this.details.toString()).append(", customType=").append(this.customType).append(", customAttributes=").append(this.customAttributes.toString()).append(", metadata=[").append(this.sessionEventMetadata).append("]]");
            this.stringRepresentation = sb.toString();
        }
        return this.stringRepresentation;
    }
}
