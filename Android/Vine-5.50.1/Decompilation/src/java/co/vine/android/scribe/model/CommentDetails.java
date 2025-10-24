package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class CommentDetails {

    @JsonField(name = {"author_id"})
    public Long authorId;

    @JsonField(name = {"comment_id"})
    public Long commentId;
}
