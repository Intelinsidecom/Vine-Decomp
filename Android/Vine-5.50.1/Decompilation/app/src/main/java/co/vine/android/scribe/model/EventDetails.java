package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.mobileapptracker.MATEvent;
import java.util.List;

@JsonObject
/* loaded from: classes.dex */
public class EventDetails {

    @JsonField(name = {"alert"})
    public AlertDetails alert;

    @JsonField(name = {"http_performance_data"})
    public HTTPPerformanceData httpPerformanceData;

    @JsonField(name = {"http_request_details"})
    public HTTPRequestDetails httpRequestDetails;

    @JsonField(name = {"items"})
    public List<Item> items;

    @JsonField(name = {"launch"})
    public LaunchDetails launch;

    @JsonField(name = {"playback_summary"})
    public PlaybackSummaryDetails playbackSummary;

    @JsonField(name = {MATEvent.SHARE})
    public ShareDetails share;

    @JsonField(name = {"timestamp"})
    public Double timestamp;

    @JsonField(name = {"timing"})
    public TimingDetails timing;

    @JsonField(name = {"video_import_details"})
    public VideoImportDetails videoImportDetails;
}
