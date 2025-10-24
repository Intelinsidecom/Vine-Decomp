package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.List;

@JsonObject
/* loaded from: classes.dex */
public class ShareDetails {

    @JsonField(name = {"has_comment"})
    public Boolean hasComment;

    @JsonField(name = {"message_recipients"})
    public List<VMRecipient> messageRecipients;

    @JsonField(name = {"post_id"})
    public String postId;

    @JsonField(name = {"share_targets"})
    public List<String> shareTargets;
}
