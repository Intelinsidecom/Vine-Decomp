package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import twitter4j.conf.PropertyConfiguration;

@JsonObject
/* loaded from: classes.dex */
public class Item {

    @JsonField(name = {"activity"})
    public ActivityDetails activity;

    @JsonField(name = {"comment"})
    public CommentDetails comment;

    @JsonField(name = {"item_type"})
    public String itemType;

    @JsonField(name = {"position"})
    public ItemPosition position;

    @JsonField(name = {"post_mosaic"})
    public MosaicDetails postMosaic;

    @JsonField(name = {"post_or_repost"})
    public PostOrRepostDetails postOrRepost;

    @JsonField(name = {"reference"})
    public String reference;

    @JsonField(name = {"suggestion"})
    public SuggestionDetails suggestion;

    @JsonField(name = {"tag"})
    public TagDetails tag;

    @JsonField(name = {PropertyConfiguration.USER})
    public UserDetails user;

    @JsonField(name = {"user_mosaic"})
    public MosaicDetails userMosaic;

    public enum ItemType {
        USER,
        POST,
        REPOST,
        COMMENT,
        ACTIVITY,
        VENUE,
        TAG,
        SUGGESTION,
        POST_MOSAIC,
        USER_MOSAIC,
        URL_ACTION,
        SOLICITOR
    }
}
