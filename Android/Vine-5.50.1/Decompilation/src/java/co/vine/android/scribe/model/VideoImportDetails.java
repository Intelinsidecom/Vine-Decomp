package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class VideoImportDetails {

    @JsonField(name = {"result"})
    public String result;

    public enum VideoImportResult {
        SUCCESS,
        CANCELLED,
        ERROR_TRIM,
        ERROR_PROCESSING
    }
}
