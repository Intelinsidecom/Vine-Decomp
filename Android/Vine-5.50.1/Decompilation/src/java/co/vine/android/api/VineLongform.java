package co.vine.android.api;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class VineLongform {

    @JsonField(name = {"aspectRatio"})
    public float aspectRatio;

    @JsonField(name = {"duration"})
    public float duration;

    @JsonField(name = {"longformId"})
    public String longformId;

    @JsonField(name = {"thumbnailUrl"})
    public String thumbnailUrl;

    @JsonField(name = {"videoUrl"})
    public String videoUrl;
}
