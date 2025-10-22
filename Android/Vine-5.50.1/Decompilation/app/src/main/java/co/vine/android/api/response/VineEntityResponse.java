package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineEntityResponse {

    @JsonField(name = {"id"})
    public long id;

    @JsonField(name = {"link"})
    public String link;

    @JsonField(name = {"range"})
    public ArrayList<Integer> range;

    @JsonField(name = {"title"})
    public String title;

    @JsonField(name = {"type"})
    public String type;
}
