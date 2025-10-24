package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class LikeResponse {

    @JsonField(name = {"data"})
    public Response data;

    @JsonObject
    public static class Response {

        @JsonField(name = {"likeId"})
        public long likeId;
    }
}
