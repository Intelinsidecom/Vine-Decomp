package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class VineLoginResponse {

    @JsonField(name = {"data"})
    public LoginResponse data;

    @JsonField(name = {"success"})
    public Boolean success;

    @JsonObject
    public static class LoginResponse {

        @JsonField(name = {"avatarUrl"})
        public String avatarUrl;

        @JsonField(name = {"edition"})
        public String edition;

        @JsonField(name = {"key"})
        public String key;

        @JsonField(name = {"userId"})
        public Long userId;

        @JsonField(name = {"username"})
        public String username;
    }
}
