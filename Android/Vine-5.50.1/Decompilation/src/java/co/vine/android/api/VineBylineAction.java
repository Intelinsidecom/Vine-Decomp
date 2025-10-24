package co.vine.android.api;

import co.vine.android.api.response.VineShortPost;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineBylineAction {

    @JsonField(name = {"actionIconUrl"})
    public String actionIconUrl;

    @JsonField(name = {"actionTitle"})
    public String actionTitle;

    @JsonField(name = {"description"})
    public String description;

    @JsonField(name = {"detailedDescription"})
    public String detailedDescription;

    @JsonField(name = {"records"})
    public ArrayList<VineShortPost> records;
}
