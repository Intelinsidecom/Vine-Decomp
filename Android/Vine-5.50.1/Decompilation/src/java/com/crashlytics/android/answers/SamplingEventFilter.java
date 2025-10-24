package com.crashlytics.android.answers;

import com.crashlytics.android.answers.SessionEvent;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class SamplingEventFilter implements EventFilter {
    static final Set<SessionEvent.Type> EVENTS_TYPE_TO_SAMPLE = new HashSet<SessionEvent.Type>() { // from class: com.crashlytics.android.answers.SamplingEventFilter.1
        {
            add(SessionEvent.Type.CREATE);
            add(SessionEvent.Type.START);
            add(SessionEvent.Type.RESUME);
            add(SessionEvent.Type.SAVE_INSTANCE_STATE);
            add(SessionEvent.Type.PAUSE);
            add(SessionEvent.Type.STOP);
            add(SessionEvent.Type.DESTROY);
            add(SessionEvent.Type.ERROR);
            add(SessionEvent.Type.CRASH);
        }
    };
    final int samplingRate;

    public SamplingEventFilter(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    @Override // com.crashlytics.android.answers.EventFilter
    public boolean skipEvent(SessionEvent sessionEvent) {
        if (isNeverSampledEvent(sessionEvent)) {
            return false;
        }
        return shouldSkipEventBasedOnInstallID(sessionEvent);
    }

    boolean isNeverSampledEvent(SessionEvent sessionEvent) {
        return (EVENTS_TYPE_TO_SAMPLE.contains(sessionEvent.type) && sessionEvent.sessionEventMetadata.betaDeviceToken == null) ? false : true;
    }

    boolean shouldSkipEventBasedOnInstallID(SessionEvent sessionEvent) {
        String installID = sessionEvent.sessionEventMetadata.installationId;
        return Math.abs(installID.hashCode() % this.samplingRate) != 0;
    }
}
