package co.vine.android.api;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineEndlessLikesPostBody {

    @JsonField(name = {"likes"})
    public ArrayList<VineEndlessLikesPostRecord> likes;
}
