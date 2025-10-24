package com.twitter.sdk.android.core.internal.scribe;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/* loaded from: classes.dex */
public class SyndicationClientEvent extends ScribeEvent {

    @SerializedName("external_ids")
    public final ExternalIds externalIds;

    @SerializedName("language")
    public final String language;

    public SyndicationClientEvent(EventNamespace eventNamespace, long timestamp, String language, String adId, List<Object> list) {
        super("tfw_client_event", eventNamespace, timestamp, list);
        this.language = language;
        this.externalIds = new ExternalIds(adId);
    }

    public class ExternalIds {

        @SerializedName("6")
        public final String adId;

        public ExternalIds(String adId) {
            this.adId = adId;
        }
    }
}
