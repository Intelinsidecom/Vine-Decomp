package co.vine.android.api;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineEndlessLikesPostBody$$JsonObjectMapper extends JsonMapper<VineEndlessLikesPostBody> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineEndlessLikesPostBody parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineEndlessLikesPostBody _parse(JsonParser jsonParser) throws IOException {
        VineEndlessLikesPostBody instance = new VineEndlessLikesPostBody();
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

    public static void parseField(VineEndlessLikesPostBody instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("likes".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VineEndlessLikesPostRecord> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VineEndlessLikesPostRecord value1 = VineEndlessLikesPostRecord$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.likes = collection1;
                return;
            }
            instance.likes = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineEndlessLikesPostBody object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineEndlessLikesPostBody object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<VineEndlessLikesPostRecord> lslocallikes = object.likes;
        if (lslocallikes != null) {
            jsonGenerator.writeFieldName("likes");
            jsonGenerator.writeStartArray();
            for (VineEndlessLikesPostRecord element1 : lslocallikes) {
                if (element1 != null) {
                    VineEndlessLikesPostRecord$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
