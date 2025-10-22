package co.vine.android.api.response;

import co.vine.android.api.VineHomeFeedSetting;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineHomeFeedSettingsResponse {

    @JsonField(name = {"data"})
    public Data data;

    @JsonObject
    public static class Data extends PagedDataResponse {

        @JsonField(name = {"records"})
        public ArrayList<VineHomeFeedSetting> items;
    }
}
