package co.vine.android.api;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class VineEndlessLikesRecord {

    @JsonField(name = {"count"})
    public int count;

    @JsonField(name = {"endTime"})
    public float endTime;

    @JsonField(name = {"level"})
    public float level;

    @JsonField(name = {"startTime"})
    public float startTime;
}
