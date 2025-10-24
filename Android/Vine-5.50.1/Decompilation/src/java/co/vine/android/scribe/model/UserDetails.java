package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class UserDetails {

    @JsonField(name = {"following"})
    public Boolean following;

    @JsonField(name = {"user_id"})
    public Long userId;
}
