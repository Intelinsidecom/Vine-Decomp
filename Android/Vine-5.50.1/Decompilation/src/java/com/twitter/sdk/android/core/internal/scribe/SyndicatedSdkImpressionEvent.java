package com.twitter.sdk.android.core.internal.scribe;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/* loaded from: classes.dex */
public class SyndicatedSdkImpressionEvent extends ScribeEvent {

    @SerializedName("device_id_created_at")
    public final long deviceIdCreatedAt;

    @SerializedName("external_ids")
    public final ExternalIds externalIds;

    @SerializedName("language")
    public final String language;

    public SyndicatedSdkImpressionEvent(EventNamespace eventNamespace, long timestamp, String language, String adId, List<Object> list) {
        super("syndicated_sdk_impression", eventNamespace, timestamp, list);
        this.language = language;
        this.externalIds = new ExternalIds(adId);
        this.deviceIdCreatedAt = 0L;
    }

    public class ExternalIds {

        @SerializedName("AD_ID")
        public final String adId;

        public ExternalIds(String adId) {
            this.adId = adId;
        }
    }
}
