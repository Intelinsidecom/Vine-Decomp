package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class ClientEvent {

    @JsonField(name = {"app_state"})
    public ApplicationState appState;

    @JsonField(name = {"client_id"})
    public String clientId;

    @JsonField(name = {"device_data"})
    public DeviceData deviceData;

    @JsonField(name = {"event_details"})
    public EventDetails eventDetails;

    @JsonField(name = {"event_type"})
    public String eventType;

    @JsonField(name = {"navigation"})
    public AppNavigation navigation;
}
