package co.vine.android.api.response;

import co.vine.android.api.VineComment;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class VineCommentResponse {

    @JsonField(name = {"data"})
    public VineComment data;
}
