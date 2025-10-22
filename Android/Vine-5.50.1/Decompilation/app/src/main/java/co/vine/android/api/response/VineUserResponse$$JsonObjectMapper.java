package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineUserResponse$$JsonObjectMapper extends JsonMapper<VineUserResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineUserResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineUserResponse _parse(JsonParser jsonParser) throws IOException {
        VineUserResponse instance = new VineUserResponse();
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

    public static void parseField(VineUserResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("acceptsOutOfNetworkConversations".equals(fieldName)) {
            instance.acceptsOutOfNetworkConversations = jsonParser.getValueAsBoolean();
            return;
        }
        if ("authoredPostCount".equals(fieldName)) {
            instance.authoredPostCount = jsonParser.getValueAsInt();
            return;
        }
        if ("avatarUrl".equals(fieldName)) {
            instance.avatarUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("blocked".equals(fieldName)) {
            instance.blocked = jsonParser.getValueAsInt();
            return;
        }
        if ("blocking".equals(fieldName)) {
            instance.blocking = jsonParser.getValueAsInt();
            return;
        }
        if ("byline".equals(fieldName)) {
            instance.byline = jsonParser.getValueAsString(null);
            return;
        }
        if ("description".equals(fieldName)) {
            instance.description = jsonParser.getValueAsString(null);
            return;
        }
        if ("disableAddressBook".equals(fieldName)) {
            instance.disableAddressBook = jsonParser.getValueAsBoolean();
            return;
        }
        if ("edition".equals(fieldName)) {
            instance.edition = jsonParser.getValueAsString(null);
            return;
        }
        if ("email".equals(fieldName)) {
            instance.email = jsonParser.getValueAsString(null);
            return;
        }
        if ("explicitContent".equals(fieldName)) {
            instance.explicit = jsonParser.getValueAsInt();
            return;
        }
        if ("externalUser".equals(fieldName)) {
            instance.externalUser = jsonParser.getValueAsBoolean();
            return;
        }
        if ("followApprovalPending".equals(fieldName)) {
            instance.followApprovalPending = jsonParser.getValueAsBoolean();
            return;
        }
        if ("followRequested".equals(fieldName)) {
            instance.followRequested = jsonParser.getValueAsBoolean();
            return;
        }
        if ("followerCount".equals(fieldName)) {
            instance.followerCount = jsonParser.getValueAsInt();
            return;
        }
        if ("following".equals(fieldName)) {
            instance.following = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
            return;
        }
        if ("followingCount".equals(fieldName)) {
            instance.followingCount = jsonParser.getValueAsInt();
            return;
        }
        if ("followingOnTwitter".equals(fieldName)) {
            instance.followingOnTwitter = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
            return;
        }
        if ("hiddenEmail".equals(fieldName)) {
            instance.hiddenEmail = jsonParser.getValueAsBoolean();
            return;
        }
        if ("hiddenPhoneNumber".equals(fieldName)) {
            instance.hiddenPhoneNumber = jsonParser.getValueAsBoolean();
            return;
        }
        if ("hiddenTwitter".equals(fieldName)) {
            instance.hiddenTwitter = jsonParser.getValueAsBoolean();
            return;
        }
        if ("includePromoted".equals(fieldName)) {
            instance.includePromoted = jsonParser.getValueAsInt();
            return;
        }
        if ("likeCount".equals(fieldName)) {
            instance.likeCount = jsonParser.getValueAsInt();
            return;
        }
        if ("location".equals(fieldName)) {
            instance.location = jsonParser.getValueAsString(null);
            return;
        }
        if ("loopCount".equals(fieldName)) {
            instance.loopCount = jsonParser.getValueAsLong();
            return;
        }
        if ("notifyPosts".equals(fieldName)) {
            instance.notifyPosts = jsonParser.getValueAsBoolean();
            return;
        }
        if ("followId".equals(fieldName)) {
            instance.orderId = jsonParser.getValueAsLong();
            return;
        }
        if ("phoneNumber".equals(fieldName)) {
            instance.phoneNumber = jsonParser.getValueAsString(null);
            return;
        }
        if ("postCount".equals(fieldName)) {
            instance.postCount = jsonParser.getValueAsInt();
            return;
        }
        if ("private".equals(fieldName)) {
            instance.privateAccount = jsonParser.getValueAsInt();
            return;
        }
        if ("profileBackground".equals(fieldName)) {
            instance.profileBackgroundString = jsonParser.getValueAsString(null);
            return;
        }
        if ("repostsEnabled".equals(fieldName)) {
            instance.repostsEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
            return;
        }
        if ("secondaryColor".equals(fieldName)) {
            instance.secondaryColorString = jsonParser.getValueAsString(null);
            return;
        }
        if ("sectionId".equals(fieldName)) {
            instance.sectionId = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
            return;
        }
        if ("section".equals(fieldName)) {
            instance.sectionTitle = jsonParser.getValueAsString(null);
            return;
        }
        if ("twitterConnected".equals(fieldName)) {
            instance.twitterConnected = jsonParser.getValueAsInt();
            return;
        }
        if ("twitterScreenname".equals(fieldName)) {
            instance.twitterScreenname = jsonParser.getValueAsString(null);
            return;
        }
        if ("twitterVerified".equals(fieldName)) {
            instance.twitterVerified = jsonParser.getValueAsBoolean();
            return;
        }
        if ("userId".equals(fieldName)) {
            instance.userId = jsonParser.getValueAsLong();
            return;
        }
        if ("username".equals(fieldName)) {
            instance.username = jsonParser.getValueAsString(null);
            return;
        }
        if ("verified".equals(fieldName)) {
            instance.verified = jsonParser.getValueAsInt();
        } else if ("verifiedEmail".equals(fieldName)) {
            instance.verifiedEmail = jsonParser.getValueAsBoolean();
        } else if ("verifiedPhoneNumber".equals(fieldName)) {
            instance.verifiedPhoneNumber = jsonParser.getValueAsBoolean();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineUserResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineUserResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeBooleanField("acceptsOutOfNetworkConversations", object.acceptsOutOfNetworkConversations);
        jsonGenerator.writeNumberField("authoredPostCount", object.authoredPostCount);
        if (object.avatarUrl != null) {
            jsonGenerator.writeStringField("avatarUrl", object.avatarUrl);
        }
        jsonGenerator.writeNumberField("blocked", object.blocked);
        jsonGenerator.writeNumberField("blocking", object.blocking);
        if (object.byline != null) {
            jsonGenerator.writeStringField("byline", object.byline);
        }
        if (object.description != null) {
            jsonGenerator.writeStringField("description", object.description);
        }
        jsonGenerator.writeBooleanField("disableAddressBook", object.disableAddressBook);
        if (object.edition != null) {
            jsonGenerator.writeStringField("edition", object.edition);
        }
        if (object.email != null) {
            jsonGenerator.writeStringField("email", object.email);
        }
        jsonGenerator.writeNumberField("explicitContent", object.explicit);
        jsonGenerator.writeBooleanField("externalUser", object.externalUser);
        jsonGenerator.writeBooleanField("followApprovalPending", object.followApprovalPending);
        jsonGenerator.writeBooleanField("followRequested", object.followRequested);
        jsonGenerator.writeNumberField("followerCount", object.followerCount);
        if (object.following != null) {
            jsonGenerator.writeNumberField("following", object.following.intValue());
        }
        jsonGenerator.writeNumberField("followingCount", object.followingCount);
        if (object.followingOnTwitter != null) {
            jsonGenerator.writeNumberField("followingOnTwitter", object.followingOnTwitter.intValue());
        }
        jsonGenerator.writeBooleanField("hiddenEmail", object.hiddenEmail);
        jsonGenerator.writeBooleanField("hiddenPhoneNumber", object.hiddenPhoneNumber);
        jsonGenerator.writeBooleanField("hiddenTwitter", object.hiddenTwitter);
        jsonGenerator.writeNumberField("includePromoted", object.includePromoted);
        jsonGenerator.writeNumberField("likeCount", object.likeCount);
        if (object.location != null) {
            jsonGenerator.writeStringField("location", object.location);
        }
        jsonGenerator.writeNumberField("loopCount", object.loopCount);
        jsonGenerator.writeBooleanField("notifyPosts", object.notifyPosts);
        jsonGenerator.writeNumberField("followId", object.orderId);
        if (object.phoneNumber != null) {
            jsonGenerator.writeStringField("phoneNumber", object.phoneNumber);
        }
        jsonGenerator.writeNumberField("postCount", object.postCount);
        jsonGenerator.writeNumberField("private", object.privateAccount);
        if (object.profileBackgroundString != null) {
            jsonGenerator.writeStringField("profileBackground", object.profileBackgroundString);
        }
        if (object.repostsEnabled != null) {
            jsonGenerator.writeNumberField("repostsEnabled", object.repostsEnabled.intValue());
        }
        if (object.secondaryColorString != null) {
            jsonGenerator.writeStringField("secondaryColor", object.secondaryColorString);
        }
        if (object.sectionId != null) {
            jsonGenerator.writeNumberField("sectionId", object.sectionId.intValue());
        }
        if (object.sectionTitle != null) {
            jsonGenerator.writeStringField("section", object.sectionTitle);
        }
        jsonGenerator.writeNumberField("twitterConnected", object.twitterConnected);
        if (object.twitterScreenname != null) {
            jsonGenerator.writeStringField("twitterScreenname", object.twitterScreenname);
        }
        jsonGenerator.writeBooleanField("twitterVerified", object.twitterVerified);
        jsonGenerator.writeNumberField("userId", object.userId);
        if (object.username != null) {
            jsonGenerator.writeStringField("username", object.username);
        }
        jsonGenerator.writeNumberField("verified", object.verified);
        jsonGenerator.writeBooleanField("verifiedEmail", object.verifiedEmail);
        jsonGenerator.writeBooleanField("verifiedPhoneNumber", object.verifiedPhoneNumber);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
