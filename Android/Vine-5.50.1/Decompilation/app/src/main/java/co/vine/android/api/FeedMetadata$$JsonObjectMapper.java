package co.vine.android.api;

import co.vine.android.api.FeedMetadata;
import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class FeedMetadata$$JsonObjectMapper extends JsonMapper<FeedMetadata> {
    protected static final FeedMetadata.FeedTypeConverter FEED_TYPE_CONVERTER = new FeedMetadata.FeedTypeConverter();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public FeedMetadata parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static FeedMetadata _parse(JsonParser jsonParser) throws IOException {
        FeedMetadata instance = new FeedMetadata();
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

    public static void parseField(FeedMetadata instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("channel".equals(fieldName)) {
            instance.channel = VineChannel$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("feedType".equals(fieldName)) {
            instance.feedType = FEED_TYPE_CONVERTER.parse(jsonParser);
        } else if ("profileUserId".equals(fieldName)) {
            instance.profileUserId = jsonParser.getValueAsLong();
        } else if ("userProfile".equals(fieldName)) {
            instance.userProfile = (VineUser) LoganSquare.typeConverterFor(VineUser.class).parse(jsonParser);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(FeedMetadata object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(FeedMetadata object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.channel != null) {
            jsonGenerator.writeFieldName("channel");
            VineChannel$$JsonObjectMapper._serialize(object.channel, jsonGenerator, true);
        }
        FEED_TYPE_CONVERTER.serialize(object.feedType, "feedType", true, jsonGenerator);
        jsonGenerator.writeNumberField("profileUserId", object.profileUserId);
        if (object.userProfile != null) {
            LoganSquare.typeConverterFor(VineUser.class).serialize(object.userProfile, "userProfile", true, jsonGenerator);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
