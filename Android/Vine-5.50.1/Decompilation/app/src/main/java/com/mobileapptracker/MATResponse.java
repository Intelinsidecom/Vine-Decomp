package com.mobileapptracker;

import org.json.JSONObject;

/* loaded from: classes.dex */
public interface MATResponse {
    void didFailWithError(JSONObject jSONObject);

    void didSucceedWithData(JSONObject jSONObject);

    void enqueuedActionWithRefId(String str);
}
