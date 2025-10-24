package com.twitter.sdk.android.core.internal.scribe;

import java.util.List;

/* loaded from: classes.dex */
public class ScribeEventFactory {
    public static ScribeEvent newScribeEvent(EventNamespace ns, long timestamp, String language, String advertisingId, List<Object> list) {
        switch (ns.client) {
            case "tfw":
                return new SyndicationClientEvent(ns, timestamp, language, advertisingId, list);
            default:
                return new SyndicatedSdkImpressionEvent(ns, timestamp, language, advertisingId, list);
        }
    }
}
