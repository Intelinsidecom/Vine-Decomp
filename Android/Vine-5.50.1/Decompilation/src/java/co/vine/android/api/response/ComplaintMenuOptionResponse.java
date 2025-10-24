package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;
import java.util.Map;

@JsonObject
/* loaded from: classes.dex */
public class ComplaintMenuOptionResponse {

    @JsonField(name = {"data"})
    public Map<String, ComplaintMenuData> data;

    @JsonObject
    public static class ComplaintMenuData {

        @JsonField(name = {"choices"})
        public ArrayList<Choice> choices;

        @JsonField(name = {"prompt"})
        public String prompt;

        @JsonObject
        public static class Choice {

            @JsonField(name = {"confirmation"})
            public String confirmation;

            @JsonField(name = {"title"})
            public String title;

            @JsonField(name = {"value"})
            public String value;
        }
    }
}
