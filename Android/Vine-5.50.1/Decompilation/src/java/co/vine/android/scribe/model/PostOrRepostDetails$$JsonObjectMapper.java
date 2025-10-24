package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class PostOrRepostDetails$$JsonObjectMapper extends JsonMapper<PostOrRepostDetails> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public PostOrRepostDetails parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static PostOrRepostDetails _parse(JsonParser jsonParser) throws IOException {
        PostOrRepostDetails instance = new PostOrRepostDetails();
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

    public static void parseField(PostOrRepostDetails postOrRepostDetails, String str, JsonParser jsonParser) throws IOException {
        if ("byline".equals(str)) {
            postOrRepostDetails.byline = Byline$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("has_similar_posts".equals(str)) {
            postOrRepostDetails.hasSimilarPosts = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("liked".equals(str)) {
            postOrRepostDetails.liked = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("longform_id".equals(str)) {
            postOrRepostDetails.longformId = jsonParser.getValueAsString(null);
            return;
        }
        if ("post_author_id".equals(str)) {
            postOrRepostDetails.postAuthorId = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
            return;
        }
        if ("post_id".equals(str)) {
            postOrRepostDetails.postId = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
            return;
        }
        if ("repost_author_id".equals(str)) {
            postOrRepostDetails.repostAuthorId = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
        } else if ("repost_id".equals(str)) {
            postOrRepostDetails.repostId = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
        } else if ("reposted".equals(str)) {
            postOrRepostDetails.reposted = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(PostOrRepostDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(PostOrRepostDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.byline != null) {
            jsonGenerator.writeFieldName("byline");
            Byline$$JsonObjectMapper._serialize(object.byline, jsonGenerator, true);
        }
        if (object.hasSimilarPosts != null) {
            jsonGenerator.writeBooleanField("has_similar_posts", object.hasSimilarPosts.booleanValue());
        }
        if (object.liked != null) {
            jsonGenerator.writeBooleanField("liked", object.liked.booleanValue());
        }
        if (object.longformId != null) {
            jsonGenerator.writeStringField("longform_id", object.longformId);
        }
        if (object.postAuthorId != null) {
            jsonGenerator.writeNumberField("post_author_id", object.postAuthorId.longValue());
        }
        if (object.postId != null) {
            jsonGenerator.writeNumberField("post_id", object.postId.longValue());
        }
        if (object.repostAuthorId != null) {
            jsonGenerator.writeNumberField("repost_author_id", object.repostAuthorId.longValue());
        }
        if (object.repostId != null) {
            jsonGenerator.writeNumberField("repost_id", object.repostId.longValue());
        }
        if (object.reposted != null) {
            jsonGenerator.writeBooleanField("reposted", object.reposted.booleanValue());
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
