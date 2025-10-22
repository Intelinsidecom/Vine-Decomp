package co.vine.android.api;

import java.util.HashMap;

/* loaded from: classes.dex */
public enum TimelineItemType {
    FEED("feed"),
    POST("post"),
    POST_MOSAIC("postMosaic"),
    URL_ACTION("urlAction"),
    SOLICITOR("solicitor"),
    USER_MOSAIC("userMosaic"),
    INVALID_TYPE("INVALID");

    private static HashMap<String, TimelineItemType> sTypeMap = new HashMap<>();
    private String mType;

    static {
        sTypeMap.put("feed", FEED);
        sTypeMap.put("post", POST);
        sTypeMap.put("postMosaic", POST_MOSAIC);
        sTypeMap.put("urlAction", URL_ACTION);
        sTypeMap.put("solicitor", SOLICITOR);
        sTypeMap.put("userMosaic", USER_MOSAIC);
    }

    TimelineItemType(String type) {
        this.mType = type;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.mType;
    }
}
