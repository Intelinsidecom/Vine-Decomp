package co.vine.android.api;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineNotificationSetting {

    @JsonField(name = {"choices"})
    public ArrayList<Choice> choices;

    @JsonField(name = {"boolean"})
    public boolean isBooleanSetting;

    @JsonField(name = {"name"})
    public String name;

    @JsonField(name = {"section"})
    public String section;

    @JsonField(name = {"title"})
    public String title;

    @JsonField(name = {"value"})
    public String value;

    @JsonObject
    public static class Choice {

        @JsonField(name = {"title"})
        public String title;

        @JsonField(name = {"value"})
        public String value;
    }
}
