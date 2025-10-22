package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class VineActivityCounts {

    @JsonField(name = {"messages"})
    public int messages;

    @JsonField(name = {"notifications"})
    public int notifications;
}
