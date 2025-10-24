package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class Byline {

    @JsonField(name = {"action_icon_url"})
    public String actionIconUrl;

    @JsonField(name = {"action_title"})
    public String actionTitle;

    @JsonField(name = {"body"})
    public String body;

    @JsonField(name = {"description"})
    public String description;

    @JsonField(name = {"detailed_description"})
    public String detailedDescription;

    @JsonField(name = {"icon_url"})
    public String iconUrl;

    @JsonField(name = {"post_ids"})
    public ArrayList<Long> postIds;

    @JsonField(name = {"user_ids"})
    public ArrayList<Long> userIds;
}
