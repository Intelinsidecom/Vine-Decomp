package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class AlertDetails {

    @JsonField(name = {"action"})
    public String action;

    @JsonField(name = {"name"})
    public String name;
}
