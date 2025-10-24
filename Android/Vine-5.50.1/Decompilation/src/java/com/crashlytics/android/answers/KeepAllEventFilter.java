package com.crashlytics.android.answers;

/* loaded from: classes.dex */
public class KeepAllEventFilter implements EventFilter {
    @Override // com.crashlytics.android.answers.EventFilter
    public boolean skipEvent(SessionEvent sessionEvent) {
        return false;
    }
}
