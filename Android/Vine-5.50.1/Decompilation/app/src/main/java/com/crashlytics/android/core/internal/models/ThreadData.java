package com.crashlytics.android.core.internal.models;

/* loaded from: classes2.dex */
public class ThreadData {
    public final FrameData[] frames;
    public final int importance;
    public final String name;

    public static final class FrameData {
        public final long address;
        public final String file;
        public final int importance;
        public final long offset;
        public final String symbol;
    }
}
