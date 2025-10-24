package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class VineTagResponse {

    @JsonField(name = {"postCount"})
    public long postCount;

    @JsonField(name = {"tagId"})
    public long tagId;

    @JsonField(name = {"tag"})
    public String tagName;
}
