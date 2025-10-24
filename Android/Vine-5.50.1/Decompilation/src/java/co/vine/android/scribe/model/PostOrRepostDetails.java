package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class PostOrRepostDetails {

    @JsonField(name = {"byline"})
    public Byline byline;

    @JsonField(name = {"has_similar_posts"})
    public Boolean hasSimilarPosts;

    @JsonField(name = {"liked"})
    public Boolean liked;

    @JsonField(name = {"longform_id"})
    public String longformId;

    @JsonField(name = {"post_author_id"})
    public Long postAuthorId;

    @JsonField(name = {"post_id"})
    public Long postId;

    @JsonField(name = {"repost_author_id"})
    public Long repostAuthorId;

    @JsonField(name = {"repost_id"})
    public Long repostId;

    @JsonField(name = {"reposted"})
    public Boolean reposted;
}
