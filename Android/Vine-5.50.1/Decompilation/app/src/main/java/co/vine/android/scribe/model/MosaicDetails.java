package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class MosaicDetails {

    @JsonField(name = {"link"})
    public String link;

    @JsonField(name = {"mosaic_type"})
    public String mosaicType;
}
