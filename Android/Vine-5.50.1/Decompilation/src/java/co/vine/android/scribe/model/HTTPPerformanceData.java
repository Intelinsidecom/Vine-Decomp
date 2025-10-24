package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class HTTPPerformanceData {

    @JsonField(name = {"bytes_rcvd"})
    public Long bytesReceived;

    @JsonField(name = {"bytes_sent"})
    public Long bytesSent;

    @JsonField(name = {"duration"})
    public Double duration;

    @JsonField(name = {"duration_to_first_byte"})
    public Double durationToFirstByte;

    @JsonField(name = {"duration_to_request_sent"})
    public Double durationToRequestSent;

    @JsonField(name = {"start_timestamp"})
    public Double startTimestamp;
}
