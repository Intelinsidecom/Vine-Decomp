package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class VineLoopSubmissionResponse {

    @JsonField(name = {"nextAfter"})
    public int mSubmissionInterval;

    public long getSubmissionIntervalMillis() {
        return this.mSubmissionInterval * 1000;
    }
}
