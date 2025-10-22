package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class ShareDetails$$JsonObjectMapper extends JsonMapper<ShareDetails> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ShareDetails parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ShareDetails _parse(JsonParser jsonParser) throws IOException {
        ShareDetails instance = new ShareDetails();
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

    public static void parseField(ShareDetails instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("has_comment".equals(fieldName)) {
            instance.hasComment = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("message_recipients".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VMRecipient> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VMRecipient value1 = VMRecipient$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.messageRecipients = collection1;
                return;
            }
            instance.messageRecipients = null;
            return;
        }
        if ("post_id".equals(fieldName)) {
            instance.postId = jsonParser.getValueAsString(null);
            return;
        }
        if ("share_targets".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<String> collection12 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    String value12 = jsonParser.getValueAsString(null);
                    collection12.add(value12);
                }
                instance.shareTargets = collection12;
                return;
            }
            instance.shareTargets = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ShareDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ShareDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.hasComment != null) {
            jsonGenerator.writeBooleanField("has_comment", object.hasComment.booleanValue());
        }
        List<VMRecipient> lslocalmessage_recipients = object.messageRecipients;
        if (lslocalmessage_recipients != null) {
            jsonGenerator.writeFieldName("message_recipients");
            jsonGenerator.writeStartArray();
            for (VMRecipient element1 : lslocalmessage_recipients) {
                if (element1 != null) {
                    VMRecipient$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (object.postId != null) {
            jsonGenerator.writeStringField("post_id", object.postId);
        }
        List<String> lslocalshare_targets = object.shareTargets;
        if (lslocalshare_targets != null) {
            jsonGenerator.writeFieldName("share_targets");
            jsonGenerator.writeStartArray();
            for (String element12 : lslocalshare_targets) {
                if (element12 != null) {
                    jsonGenerator.writeString(element12);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
