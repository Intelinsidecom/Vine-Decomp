package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public final class Item$$JsonObjectMapper extends JsonMapper<Item> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public Item parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static Item _parse(JsonParser jsonParser) throws IOException {
        Item instance = new Item();
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

    public static void parseField(Item instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("activity".equals(fieldName)) {
            instance.activity = ActivityDetails$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("comment".equals(fieldName)) {
            instance.comment = CommentDetails$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("item_type".equals(fieldName)) {
            instance.itemType = jsonParser.getValueAsString(null);
            return;
        }
        if ("position".equals(fieldName)) {
            instance.position = ItemPosition$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("post_mosaic".equals(fieldName)) {
            instance.postMosaic = MosaicDetails$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("post_or_repost".equals(fieldName)) {
            instance.postOrRepost = PostOrRepostDetails$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("reference".equals(fieldName)) {
            instance.reference = jsonParser.getValueAsString(null);
            return;
        }
        if ("suggestion".equals(fieldName)) {
            instance.suggestion = SuggestionDetails$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("tag".equals(fieldName)) {
            instance.tag = TagDetails$$JsonObjectMapper._parse(jsonParser);
        } else if (PropertyConfiguration.USER.equals(fieldName)) {
            instance.user = UserDetails$$JsonObjectMapper._parse(jsonParser);
        } else if ("user_mosaic".equals(fieldName)) {
            instance.userMosaic = MosaicDetails$$JsonObjectMapper._parse(jsonParser);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(Item object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(Item object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.activity != null) {
            jsonGenerator.writeFieldName("activity");
            ActivityDetails$$JsonObjectMapper._serialize(object.activity, jsonGenerator, true);
        }
        if (object.comment != null) {
            jsonGenerator.writeFieldName("comment");
            CommentDetails$$JsonObjectMapper._serialize(object.comment, jsonGenerator, true);
        }
        if (object.itemType != null) {
            jsonGenerator.writeStringField("item_type", object.itemType);
        }
        if (object.position != null) {
            jsonGenerator.writeFieldName("position");
            ItemPosition$$JsonObjectMapper._serialize(object.position, jsonGenerator, true);
        }
        if (object.postMosaic != null) {
            jsonGenerator.writeFieldName("post_mosaic");
            MosaicDetails$$JsonObjectMapper._serialize(object.postMosaic, jsonGenerator, true);
        }
        if (object.postOrRepost != null) {
            jsonGenerator.writeFieldName("post_or_repost");
            PostOrRepostDetails$$JsonObjectMapper._serialize(object.postOrRepost, jsonGenerator, true);
        }
        if (object.reference != null) {
            jsonGenerator.writeStringField("reference", object.reference);
        }
        if (object.suggestion != null) {
            jsonGenerator.writeFieldName("suggestion");
            SuggestionDetails$$JsonObjectMapper._serialize(object.suggestion, jsonGenerator, true);
        }
        if (object.tag != null) {
            jsonGenerator.writeFieldName("tag");
            TagDetails$$JsonObjectMapper._serialize(object.tag, jsonGenerator, true);
        }
        if (object.user != null) {
            jsonGenerator.writeFieldName(PropertyConfiguration.USER);
            UserDetails$$JsonObjectMapper._serialize(object.user, jsonGenerator, true);
        }
        if (object.userMosaic != null) {
            jsonGenerator.writeFieldName("user_mosaic");
            MosaicDetails$$JsonObjectMapper._serialize(object.userMosaic, jsonGenerator, true);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
