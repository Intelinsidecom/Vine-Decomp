package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class ExperimentData {

    @JsonField(name = {"experiment_values"})
    public ArrayList<ExperimentValue> experimentValues;
}
