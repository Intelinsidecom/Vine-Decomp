package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import twitter4j.conf.PropertyConfiguration;

@JsonObject
/* loaded from: classes.dex */
public class VMRecipient {

    @JsonField(name = {"is_email"})
    public Boolean isEmail;

    @JsonField(name = {"is_phone"})
    public Boolean isPhone;

    @JsonField(name = {PropertyConfiguration.USER})
    public UserDetails user;
}
