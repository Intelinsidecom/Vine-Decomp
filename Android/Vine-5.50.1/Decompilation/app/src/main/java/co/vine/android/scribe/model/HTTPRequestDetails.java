package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class HTTPRequestDetails {

    @JsonField(name = {"api_error"})
    public Integer apiError;

    @JsonField(name = {"http_status"})
    public Integer httpStatus;

    @JsonField(name = {"method"})
    public String method;

    @JsonField(name = {"network_error"})
    public String networkError;

    @JsonField(name = {"os_error_details"})
    public String osErrorDetails;

    @JsonField(name = {"url"})
    public String url;
}
