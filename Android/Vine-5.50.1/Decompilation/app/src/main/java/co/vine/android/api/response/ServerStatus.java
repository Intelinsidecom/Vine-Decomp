package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class ServerStatus {

    @JsonField(name = {"message"})
    public String message;

    @JsonField(name = {"staticTimelineUrl"})
    public String staticTimelineUrl;

    @JsonField(name = {"status"})
    public String status;

    @JsonField(name = {"uploadType"})
    public String uploadType;
}
