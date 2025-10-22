package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class MobileRadioDetails$$JsonObjectMapper extends JsonMapper<MobileRadioDetails> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public MobileRadioDetails parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static MobileRadioDetails _parse(JsonParser jsonParser) throws IOException {
        MobileRadioDetails instance = new MobileRadioDetails();
        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            parseField(instance, fieldName, jsonParser);
            jsonParser.skipChildren();
        }
        return instance;
    }

    public static void parseField(MobileRadioDetails instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("mobile_network_operator_code".equals(fieldName)) {
            instance.mobileNetworkOperatorCode = jsonParser.getValueAsString(null);
            return;
        }
        if ("mobile_network_operator_country_code".equals(fieldName)) {
            instance.mobileNetworkOperatorCountryCode = jsonParser.getValueAsString(null);
            return;
        }
        if ("mobile_network_operator_iso_country_code".equals(fieldName)) {
            instance.mobileNetworkOperatorIsoCountryCode = jsonParser.getValueAsString(null);
            return;
        }
        if ("mobile_network_operator_name".equals(fieldName)) {
            instance.mobileNetworkOperatorName = jsonParser.getValueAsString(null);
            return;
        }
        if ("mobile_sim_provider_code".equals(fieldName)) {
            instance.mobileSimProviderCode = jsonParser.getValueAsString(null);
            return;
        }
        if ("mobile_sim_provider_iso_country_code".equals(fieldName)) {
            instance.mobileSimProviderIsoCountryCode = jsonParser.getValueAsString(null);
            return;
        }
        if ("mobile_sim_provider_name".equals(fieldName)) {
            instance.mobileSimProviderName = jsonParser.getValueAsString(null);
        } else if ("radio_status".equals(fieldName)) {
            instance.radioStatus = jsonParser.getValueAsString(null);
        } else if ("signal_strength".equals(fieldName)) {
            instance.signalStrength = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(MobileRadioDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(MobileRadioDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.mobileNetworkOperatorCode != null) {
            jsonGenerator.writeStringField("mobile_network_operator_code", object.mobileNetworkOperatorCode);
        }
        if (object.mobileNetworkOperatorCountryCode != null) {
            jsonGenerator.writeStringField("mobile_network_operator_country_code", object.mobileNetworkOperatorCountryCode);
        }
        if (object.mobileNetworkOperatorIsoCountryCode != null) {
            jsonGenerator.writeStringField("mobile_network_operator_iso_country_code", object.mobileNetworkOperatorIsoCountryCode);
        }
        if (object.mobileNetworkOperatorName != null) {
            jsonGenerator.writeStringField("mobile_network_operator_name", object.mobileNetworkOperatorName);
        }
        if (object.mobileSimProviderCode != null) {
            jsonGenerator.writeStringField("mobile_sim_provider_code", object.mobileSimProviderCode);
        }
        if (object.mobileSimProviderIsoCountryCode != null) {
            jsonGenerator.writeStringField("mobile_sim_provider_iso_country_code", object.mobileSimProviderIsoCountryCode);
        }
        if (object.mobileSimProviderName != null) {
            jsonGenerator.writeStringField("mobile_sim_provider_name", object.mobileSimProviderName);
        }
        if (object.radioStatus != null) {
            jsonGenerator.writeStringField("radio_status", object.radioStatus);
        }
        if (object.signalStrength != null) {
            jsonGenerator.writeNumberField("signal_strength", object.signalStrength.intValue());
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
