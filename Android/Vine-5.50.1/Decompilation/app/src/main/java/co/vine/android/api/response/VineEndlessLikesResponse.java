package co.vine.android.api.response;

import co.vine.android.api.VineEndlessLikesRecord;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineEndlessLikesResponse {

    @JsonField(name = {"data"})
    public ArrayList<VineEndlessLikesRecord> data;
}
