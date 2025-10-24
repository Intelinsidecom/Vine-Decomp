package co.vine.android.api.response;

import co.vine.android.api.VineComment;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineCommentsResponse {

    @JsonField(name = {"data"})
    public Data data;

    @JsonObject
    public static class Data extends PagedDataResponse {

        @JsonField(name = {"records"})
        public ArrayList<VineComment> items;
    }
}
