package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class ExperimentValue {

    @JsonField(name = {"key"})
    public String key;

    @JsonField(name = {"value"})
    public String value;
}
