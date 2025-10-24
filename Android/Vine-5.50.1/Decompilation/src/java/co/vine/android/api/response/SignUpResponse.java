package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class SignUpResponse {

    @JsonField(name = {"data"})
    public Response data;

    @JsonObject
    public static class Response {

        @JsonField(name = {"edition"})
        public String edition;

        @JsonField(name = {"key"})
        public String key;

        @JsonField(name = {"userId"})
        public long userId;
    }
}
