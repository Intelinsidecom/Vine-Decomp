package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class GeneralError {

    @JsonField(name = {"code"})
    public Integer code;

    @JsonField(name = {"data"})
    public String data;

    @JsonField(name = {"error"})
    public String error;
}
