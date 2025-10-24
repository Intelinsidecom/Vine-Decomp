package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class TimingDetails {

    @JsonField(name = {"duration"})
    public Double duration;

    @JsonField(name = {"start_timestamp"})
    public Double startTimestamp;
}
