package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineSourceResponse {

    @JsonField(name = {"records"})
    public ArrayList<Record> records;

    @JsonObject
    public static class Record {

        @JsonField(name = {"contentType"})
        public int contentType;

        @JsonField(name = {"description"})
        public String description;

        @JsonField(name = {"postId"})
        public long postId;

        @JsonField(name = {"thumbnailUrl"})
        public String thumbnailUrl;

        @JsonField(name = {"username"})
        public String username;
    }
}
