package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class VineShortPost {

    @JsonField(name = {"postId"})
    public Long postId;

    @JsonField(name = {"thumbnailUrl"})
    public String thumbnailUrl;

    @JsonField(name = {"type"})
    public String type;
}
