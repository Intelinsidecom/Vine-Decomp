package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineEditions {

    @JsonField(name = {"data"})
    public ArrayList<Edition> editions;

    @JsonObject
    public static class Edition {

        @JsonField(name = {"editionId"})
        public String editionId;

        @JsonField(name = {"name"})
        public String name;
    }
}
