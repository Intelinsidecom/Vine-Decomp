package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class ApplicationState {

    @JsonField(name = {"ab_connected"})
    public boolean abConnected;

    @JsonField(name = {"active_experiments"})
    public ExperimentData activeExperiments;

    @JsonField(name = {"application_status"})
    public String applicationStatus;

    @JsonField(name = {"edition"})
    public String edition;

    @JsonField(name = {"fb_connected"})
    public Boolean facebookConnected;

    @JsonField(name = {"last_launch_timestamp"})
    public Double lastLaunchTimestamp;

    @JsonField(name = {"logged_in_user_id"})
    public Long loggedInUserId;

    @JsonField(name = {"num_drafts"})
    public Long numDrafts;

    @JsonField(name = {"tw_connected"})
    public Boolean twitterConnected;

    @JsonField(name = {"video_cache_size"})
    public Long videoCacheSize;
}
