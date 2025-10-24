package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class MobileRadioDetails {

    @JsonField(name = {"mobile_network_operator_code"})
    public String mobileNetworkOperatorCode;

    @JsonField(name = {"mobile_network_operator_country_code"})
    public String mobileNetworkOperatorCountryCode;

    @JsonField(name = {"mobile_network_operator_iso_country_code"})
    public String mobileNetworkOperatorIsoCountryCode;

    @JsonField(name = {"mobile_network_operator_name"})
    public String mobileNetworkOperatorName;

    @JsonField(name = {"mobile_sim_provider_code"})
    public String mobileSimProviderCode;

    @JsonField(name = {"mobile_sim_provider_iso_country_code"})
    public String mobileSimProviderIsoCountryCode;

    @JsonField(name = {"mobile_sim_provider_name"})
    public String mobileSimProviderName;

    @JsonField(name = {"radio_status"})
    public String radioStatus;

    @JsonField(name = {"signal_strength"})
    public Integer signalStrength;
}
