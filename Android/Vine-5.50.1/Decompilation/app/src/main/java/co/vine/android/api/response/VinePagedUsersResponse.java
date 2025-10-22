package co.vine.android.api.response;

import co.vine.android.api.VineUser;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VinePagedUsersResponse {

    @JsonField(name = {"data"})
    public Data data;

    @JsonObject
    public static class Data extends PagedDataResponse {

        @JsonField(name = {"records"})
        public ArrayList<VineUser> items;
    }
}
