package co.vine.android.api;

import android.text.TextUtils;
import co.vine.android.api.ComplaintMenuOption;
import co.vine.android.api.FoursquareVenue;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.VineSolicitor;
import co.vine.android.api.VineUrlAction;
import co.vine.android.api.response.ComplaintMenuOptionResponse;
import co.vine.android.api.response.FoursquareResponse;
import co.vine.android.api.response.GeneralError;
import co.vine.android.api.response.GeneralResponse;
import co.vine.android.api.response.LikeResponse;
import co.vine.android.api.response.PagedActivityResponse;
import co.vine.android.api.response.ParsingTypeConverter;
import co.vine.android.api.response.ServerStatus;
import co.vine.android.api.response.SignUpResponse;
import co.vine.android.api.response.VineActivityCounts;
import co.vine.android.api.response.VineActivityCountsResponse;
import co.vine.android.api.response.VineAudioMetadataResponse;
import co.vine.android.api.response.VineAudioMetadataResponse$$JsonObjectMapper;
import co.vine.android.api.response.VineChannelsResponse;
import co.vine.android.api.response.VineClientFlags;
import co.vine.android.api.response.VineClientFlagsResponse;
import co.vine.android.api.response.VineCommentResponse;
import co.vine.android.api.response.VineCommentsResponse;
import co.vine.android.api.response.VineEditions;
import co.vine.android.api.response.VineEndlessLikesResponse;
import co.vine.android.api.response.VineEntityResponse;
import co.vine.android.api.response.VineEntityResponse$$JsonObjectMapper;
import co.vine.android.api.response.VineHomeFeedSettingsResponse;
import co.vine.android.api.response.VineLikesResponse;
import co.vine.android.api.response.VineLoginResponse;
import co.vine.android.api.response.VineLoopSubmissionResponse;
import co.vine.android.api.response.VineLoopSubmissionResponseWrapper;
import co.vine.android.api.response.VineNotificationSettingsResponse;
import co.vine.android.api.response.VinePagedTagsResponse;
import co.vine.android.api.response.VinePagedUsersResponse;
import co.vine.android.api.response.VinePostResponse;
import co.vine.android.api.response.VinePostResponseWrapper;
import co.vine.android.api.response.VineRepostResponse;
import co.vine.android.api.response.VineSourceResponse;
import co.vine.android.api.response.VineSourceResponse$$JsonObjectMapper;
import co.vine.android.api.response.VineTagResponse;
import co.vine.android.api.response.VineTagResponse$$JsonObjectMapper;
import co.vine.android.api.response.VineUserResponse$$JsonObjectMapper;
import co.vine.android.api.response.VineUserResponseToUser;
import co.vine.android.api.response.VineUserResponseWrapper;
import co.vine.android.api.response.VineUsersResponseWrapper;
import co.vine.android.model.VineTag;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.DateTimeUtil;
import co.vine.android.util.Util;
import com.bluelinelabs.logansquare.LoganSquare;
import com.edisonwang.android.slog.SLog;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class VineParsers {
    private static final RecordParser<VineComment> COMMENT_PARSER;
    private static final RecordParser<VineConversation> CONVERSATIONS_PARSER;
    private static final RecordParser<VineEverydayNotification> GROUPED_ACTIVITY_PARSER;
    public static final JsonFactory JSON_FACTORY;
    private static final RecordParser<VineLike> LIKES_PARSER;
    private static final RecordParser<VinePrivateMessage> MESSAGES_PARSER;
    private static final RecordParser<VinePost> POSTS_PARSER;
    private static final RecordParser<TimelineItem> TIMELINE_PARSER;
    private static final RecordParser<VineUser> USER_PARSER;

    public enum ApiResponse {
        SUCCESS,
        FAILURE
    }

    public interface PagedDataParser<T> {
    }

    public static abstract class RecordListParser<T> implements PagedDataParser<T> {
        public abstract ArrayList<T> parse(JsonParser jsonParser) throws IOException;
    }

    public static abstract class RecordParser<T> implements PagedDataParser<T> {
        public abstract T parse(JsonParser jsonParser) throws IOException;
    }

    static {
        LoganSquare.registerTypeConverter(VineEntity.class, new VineEntityConverter());
        LoganSquare.registerTypeConverter(VineTag.class, new VineTagConverter());
        LoganSquare.registerTypeConverter(VineUser.class, new VineUserResponseToUser());
        LoganSquare.registerTypeConverter(TimelineItem.class, new TimelineItemConverter());
        JSON_FACTORY = new JsonFactory();
        USER_PARSER = new RecordParser<VineUser>() { // from class: co.vine.android.api.VineParsers.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // co.vine.android.api.VineParsers.RecordParser
            public VineUser parse(JsonParser parser) throws IOException {
                return VineParsers.parseUser(parser);
            }
        };
        LIKES_PARSER = new RecordParser<VineLike>() { // from class: co.vine.android.api.VineParsers.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // co.vine.android.api.VineParsers.RecordParser
            public VineLike parse(JsonParser parser) throws IOException {
                return VineParsers.parseLike(parser);
            }
        };
        COMMENT_PARSER = new RecordParser<VineComment>() { // from class: co.vine.android.api.VineParsers.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // co.vine.android.api.VineParsers.RecordParser
            public VineComment parse(JsonParser parser) throws IOException {
                return VineParsers.parseComment(parser);
            }
        };
        TIMELINE_PARSER = new RecordParser<TimelineItem>() { // from class: co.vine.android.api.VineParsers.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // co.vine.android.api.VineParsers.RecordParser
            public TimelineItem parse(JsonParser parser) throws IOException {
                return VineParsers.parseTimelineItem(parser);
            }
        };
        POSTS_PARSER = new RecordParser<VinePost>() { // from class: co.vine.android.api.VineParsers.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // co.vine.android.api.VineParsers.RecordParser
            public VinePost parse(JsonParser parser) throws IOException {
                return VineParsers.parsePost(parser);
            }
        };
        GROUPED_ACTIVITY_PARSER = new RecordParser<VineEverydayNotification>() { // from class: co.vine.android.api.VineParsers.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // co.vine.android.api.VineParsers.RecordParser
            public VineEverydayNotification parse(JsonParser parser) throws IOException {
                return VineParsers.parseEverydayNotification(parser);
            }
        };
        MESSAGES_PARSER = new RecordParser<VinePrivateMessage>() { // from class: co.vine.android.api.VineParsers.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // co.vine.android.api.VineParsers.RecordParser
            public VinePrivateMessage parse(JsonParser parser) throws IOException {
                return VineParsers.parsePrivateMessage(parser);
            }
        };
        CONVERSATIONS_PARSER = new RecordParser<VineConversation>() { // from class: co.vine.android.api.VineParsers.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // co.vine.android.api.VineParsers.RecordParser
            public VineConversation parse(JsonParser parser) throws IOException {
                return VineParsers.parseConversation(parser);
            }
        };
    }

    private static final class TimelineItemConverter extends ParsingTypeConverter<TimelineItem> {
        private TimelineItemConverter() {
        }

        @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
        public TimelineItem parse(JsonParser jsonParser) throws IOException {
            return VineParsers.parseTimelineItem(jsonParser);
        }
    }

    private static final class VineEntityConverter extends ParsingTypeConverter<VineEntity> {
        private VineEntityConverter() {
        }

        @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
        public VineEntity parse(JsonParser jsonParser) throws IOException {
            VineEntityResponse response = VineEntityResponse$$JsonObjectMapper._parse(jsonParser);
            if (response == null) {
                return null;
            }
            int start = -1;
            int end = -1;
            if (response.range != null && response.range.size() >= 2) {
                start = response.range.get(0).intValue();
                end = response.range.get(1).intValue();
            }
            return new VineEntity(response.type, response.title, response.link, start, end, response.id);
        }
    }

    private static final class VineTagConverter extends ParsingTypeConverter<VineTag> {
        private VineTagConverter() {
        }

        @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
        public VineTag parse(JsonParser jsonParser) throws IOException {
            VineTagResponse response = VineTagResponse$$JsonObjectMapper._parse(jsonParser);
            if (response == null) {
                return null;
            }
            return VineTag.create(response.tagName, response.tagId, response.postCount);
        }
    }

    public static VineParserReader.VineParsersInterface createParserCreator() {
        return new VineParserReader.VineParsersInterface() { // from class: co.vine.android.api.VineParsers.9
            @Override // co.vine.android.api.VineParserReader.VineParsersInterface
            public Object parseVineResponse(InputStream inputStream, int type) throws IOException {
                return VineParsers.parseVineResponse(inputStream, type);
            }

            @Override // co.vine.android.api.VineParserReader.VineParsersInterface
            public Object parseError(InputStream inputStream) throws IOException {
                return VineParsers.parseError(inputStream);
            }
        };
    }

    public static Object parseVineResponse(InputStream inputStream, int type) throws IOException {
        if (type == 16) {
            return null;
        }
        if (type == 15) {
            return parseServerStatus(inputStream);
        }
        if (type == 1) {
            return parseGeneral(inputStream);
        }
        if (type == 4) {
            return parseLogin(inputStream);
        }
        if (type == 31) {
            return parseFoursquareResponse(inputStream);
        }
        if (type == 5) {
            return parseSignUp(inputStream);
        }
        if (type == 29) {
            return parseClientFlags(inputStream);
        }
        if (type == 10) {
            return parseLikeId(inputStream);
        }
        if (type == 12) {
            return parseVinePostResponse(inputStream);
        }
        if (type == 19) {
            return parseRepostResponse(inputStream);
        }
        if (type == 26) {
            return parseActivityCounts(inputStream);
        }
        if (type == 27) {
            return parseLoopsSubmission(inputStream);
        }
        if (type == 35) {
            return parseComplaintMenu(inputStream);
        }
        if (type == 20) {
            return parseEditions(inputStream);
        }
        if (type == 2) {
            return parseUser(inputStream);
        }
        if (type == 11) {
            return parsePagedLikes(inputStream);
        }
        if (type == 6) {
            return parsePagedComments(inputStream);
        }
        if (type == 7) {
            return parseComment(inputStream);
        }
        if (type == 30) {
            return parsePagedNotificationSettings(inputStream);
        }
        if (type == 37) {
            return parsePagedHomeFeedSettings(inputStream);
        }
        if (type == 13) {
            return parseUsersArray(inputStream);
        }
        if (type == 18) {
            return parsePagedChannels(inputStream);
        }
        if (type == 3) {
            return parsePagedUsers(inputStream);
        }
        if (type == 9) {
            return parsePagedActivity(inputStream);
        }
        if (type == 14) {
            return parsePagedTags(inputStream);
        }
        if (type == 38) {
            return parseEndlessLikes(inputStream);
        }
        JsonParser p = createParser(inputStream);
        for (JsonToken t = p.nextToken(); t != null && t != JsonToken.END_OBJECT; t = p.nextToken()) {
            switch (t) {
                case START_OBJECT:
                    if ("data".equals(p.getCurrentName())) {
                        switch (type) {
                            case 8:
                                return parsePagedData(p, POSTS_PARSER);
                            case 22:
                                return parsePagedData(p, MESSAGES_PARSER);
                            case 23:
                                return parsePrivateMessage(p);
                            case 24:
                                return parsePrivateMessageResponse(p);
                            case 25:
                                return parsePagedData(p, CONVERSATIONS_PARSER);
                            case 28:
                                return parsePagedData(p, GROUPED_ACTIVITY_PARSER);
                            case 33:
                                return parseSearchResults(p);
                            case 36:
                                return parsePagedData(p, TIMELINE_PARSER);
                            default:
                                p.skipChildren();
                                break;
                        }
                    } else {
                        continue;
                    }
            }
        }
        throw new IllegalArgumentException("Unhandled parse type " + type);
    }

    private static VineCommentsResponse.Data parsePagedComments(InputStream inputStream) throws IOException {
        VineCommentsResponse response = (VineCommentsResponse) LoganSquare.parse(inputStream, VineCommentsResponse.class);
        VineCommentsResponse.Data comments = response.data;
        if (comments != null && comments.items != null) {
            for (int i = 0; i < comments.items.size(); i++) {
                VineComment comment = comments.items.get(i);
                postProcessComment(comment);
            }
        }
        return comments;
    }

    private static VineComment postProcessComment(VineComment comment) {
        if (!CrossConstants.SHOW_TWITTER_SCREENNAME) {
            comment.twitterScreenname = null;
        }
        if (comment.twitterVerified) {
        }
        comment.twitterVerified = false;
        ArrayList<VineEntity> entities = comment.entities;
        if (entities != null) {
            Util.validateAndFixEntities(entities);
        }
        return comment;
    }

    private static VineLikesResponse.Data parsePagedLikes(InputStream inputStream) throws IOException {
        VineLikesResponse response = (VineLikesResponse) LoganSquare.parse(inputStream, VineLikesResponse.class);
        VineLikesResponse.Data pagedData = response.data;
        if (pagedData.items != null) {
            Iterator<VineLike> it = pagedData.items.iterator();
            while (it.hasNext()) {
                VineLike like = it.next();
                if (like.user != null) {
                    like.user.following = like.following == null ? -1 : like.following.intValue();
                    like.user.blocked = like.blocked == null ? 0 : like.blocked.intValue();
                }
            }
        }
        return pagedData;
    }

    private static VineLoopSubmissionResponse parseLoopsSubmission(InputStream inputStream) throws IOException {
        return ((VineLoopSubmissionResponseWrapper) LoganSquare.parse(inputStream, VineLoopSubmissionResponseWrapper.class)).data;
    }

    private static VineEditions parseEditions(InputStream inputStream) throws IOException {
        return (VineEditions) LoganSquare.parse(inputStream, VineEditions.class);
    }

    private static VineRepost parseRepostResponse(InputStream inputStream) throws IOException {
        long j = 0;
        VineRepostResponse response = (VineRepostResponse) LoganSquare.parse(inputStream, VineRepostResponse.class);
        VineRepost vr = new VineRepost();
        vr.repostId = (response == null || response.data == null) ? 0L : response.data.repostId;
        if (response != null && response.data != null) {
            j = response.data.postId;
        }
        vr.postId = j;
        return vr;
    }

    private static VineActivityCounts parseActivityCounts(InputStream inputStream) throws IOException {
        return ((VineActivityCountsResponse) LoganSquare.parse(inputStream, VineActivityCountsResponse.class)).data;
    }

    public static ArrayList<FoursquareVenue> parseFoursquareResponse(InputStream inputStream) throws IOException {
        FoursquareResponse response = (FoursquareResponse) LoganSquare.parse(inputStream, FoursquareResponse.class);
        ArrayList<FoursquareVenue> venues = new ArrayList<>();
        if (response != null && response.response != null && response.response.venues != null) {
            Iterator<FoursquareResponse.Venue> it = response.response.venues.iterator();
            while (it.hasNext()) {
                FoursquareResponse.Venue venue = it.next();
                FoursquareVenue.Category category = null;
                if (venue.categories != null && venue.categories.size() > 0) {
                    FoursquareResponse.Category rawCategory = venue.categories.get(0);
                    String leafName = null;
                    String parentName = null;
                    if (rawCategory.icon != null && rawCategory.icon.prefix != null) {
                        String[] tokens = rawCategory.icon.prefix.split("/");
                        if (tokens.length > 1) {
                            leafName = tokens[tokens.length - 1].toLowerCase();
                            parentName = tokens[tokens.length - 2].toLowerCase();
                        }
                    }
                    category = FoursquareVenue.Category.create(rawCategory.shortName, leafName, parentName);
                }
                venues.add(FoursquareVenue.create(venue.id, venue.name, venue.location != null ? venue.location.address : null, category));
            }
        }
        return venues;
    }

    private static <T> VinePagedData<T> parsePagedData(JsonParser jsonParser, PagedDataParser<T> pagedDataParser) throws IOException {
        VinePagedData<T> vinePagedData = new VinePagedData<>();
        vinePagedData.items = new ArrayList<>();
        int i = 1;
        RecordParser<T> recordParser = null;
        RecordListParser<T> recordListParser = null;
        if (pagedDataParser instanceof RecordListParser) {
            recordListParser = (RecordListParser) pagedDataParser;
        } else {
            recordParser = (RecordParser) pagedDataParser;
        }
        JsonToken jsonTokenNextToken = jsonParser.nextToken();
        while (jsonTokenNextToken != null && jsonTokenNextToken != JsonToken.END_OBJECT) {
            switch (jsonTokenNextToken) {
                case START_OBJECT:
                    if ("channel".equals(jsonParser.getCurrentName())) {
                        vinePagedData.channel = parseChannel(jsonParser);
                        break;
                    } else {
                        jsonParser.skipChildren();
                        break;
                    }
                case VALUE_STRING:
                    String currentName = jsonParser.getCurrentName();
                    if ("anchor".equals(currentName) || "anchorStr".equals(currentName)) {
                        vinePagedData.anchor = jsonParser.getText();
                        break;
                    } else if ("inbox".equals(jsonParser.getCurrentName())) {
                        if (!"other".equals(jsonParser.getValueAsString())) {
                            break;
                        } else {
                            i = 2;
                            break;
                        }
                    } else if (!"title".equals(currentName)) {
                        break;
                    } else {
                        vinePagedData.title = jsonParser.getText();
                        break;
                    }
                    break;
                case VALUE_NUMBER_INT:
                    String currentName2 = jsonParser.getCurrentName();
                    if ("count".equals(currentName2)) {
                        vinePagedData.count = jsonParser.getIntValue();
                        break;
                    } else if ("nextPage".equals(currentName2)) {
                        vinePagedData.nextPage = jsonParser.getIntValue();
                        break;
                    } else if ("previousPage".equals(currentName2)) {
                        vinePagedData.previousPage = jsonParser.getIntValue();
                        break;
                    } else if ("size".equals(currentName2)) {
                        vinePagedData.size = jsonParser.getIntValue();
                        break;
                    } else if ("anchor".equals(currentName2)) {
                        vinePagedData.anchor = Long.toString(jsonParser.getLongValue());
                        break;
                    } else if ("unreadMessageCount".equals(currentName2)) {
                        vinePagedData.unreadMessageCount = jsonParser.getLongValue();
                        break;
                    } else if ("lastMessageRead".equals(currentName2)) {
                        vinePagedData.lastMessageRead = jsonParser.getLongValue();
                        break;
                    } else if (!"lastMessage".equals(currentName2)) {
                        break;
                    } else {
                        vinePagedData.lastMessage = jsonParser.getLongValue();
                        break;
                    }
                case START_ARRAY:
                    if ("records".equals(jsonParser.getCurrentName())) {
                        if (recordListParser != null) {
                            vinePagedData.items.addAll(recordListParser.parse(jsonParser));
                        } else {
                            if (recordParser == null) {
                                throw new RuntimeException("Invalid parser.");
                            }
                            JsonToken jsonTokenNextToken2 = jsonParser.nextToken();
                            while (jsonTokenNextToken2 != null && jsonTokenNextToken2 != JsonToken.END_ARRAY) {
                                T t = recordParser.parse(jsonParser);
                                if (t != null) {
                                    vinePagedData.items.add(t);
                                }
                                jsonTokenNextToken2 = jsonParser.nextToken();
                            }
                        }
                    }
                    if (!"users".equals(jsonParser.getCurrentName())) {
                        break;
                    } else {
                        JsonToken jsonTokenNextToken3 = jsonParser.nextToken();
                        if (vinePagedData.participants == null) {
                            vinePagedData.participants = new ArrayList<>();
                        }
                        while (jsonTokenNextToken3 != null && jsonTokenNextToken3 != JsonToken.END_ARRAY) {
                            VineUser vineUser = USER_PARSER.parse(jsonParser);
                            if (vineUser != null) {
                                vinePagedData.participants.add(vineUser);
                            }
                            jsonTokenNextToken3 = jsonParser.nextToken();
                        }
                        break;
                    }
            }
            jsonTokenNextToken = jsonParser.nextToken();
        }
        vinePagedData.networkType = i;
        return vinePagedData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static VineLike parseLike(JsonParser p) throws IOException {
        VineLike vineLike = VineLike$$JsonObjectMapper._parse(p);
        if (vineLike == null) {
            return null;
        }
        if (vineLike.user != null) {
            vineLike.user.following = vineLike.following == null ? -1 : vineLike.following.intValue();
            vineLike.user.blocked = vineLike.blocked == null ? 0 : vineLike.blocked.intValue();
            return vineLike;
        }
        return vineLike;
    }

    private static VineByline parseByline(JsonParser p) throws IOException {
        String iconUrl = null;
        String body = null;
        VineBylineAction bylineAction = null;
        ArrayList<VineEntity> entities = new ArrayList<>();
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    if ("bylineAction".equals(p.getCurrentName())) {
                        bylineAction = parseBylineAction(p);
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
                case VALUE_STRING:
                    String cn = p.getCurrentName();
                    if ("iconUrl".equals(cn)) {
                        iconUrl = p.getText();
                        break;
                    } else if (!"body".equals(cn)) {
                        break;
                    } else {
                        body = p.getText();
                        break;
                    }
                case START_ARRAY:
                    if (!"entities".equals(p.getCurrentName())) {
                        break;
                    } else {
                        JsonToken t2 = p.nextToken();
                        while (t2 != null && t2 != JsonToken.END_ARRAY) {
                            VineEntity entity = parseEntity(p);
                            if (entity != null) {
                                entities.add(entity);
                            }
                            t2 = p.nextToken();
                        }
                        Util.validateAndFixEntities(entities);
                        break;
                    }
            }
            t = p.nextToken();
        }
        return new VineByline(body, iconUrl, entities, bylineAction);
    }

    private static VineBylineAction parseBylineAction(JsonParser p) throws IOException {
        VineBylineAction bylineAction = VineBylineAction$$JsonObjectMapper._parse(p);
        return bylineAction;
    }

    private static VineRepost parseRepost(JsonParser p) throws IOException {
        String username = null;
        String avatarUrl = null;
        String location = null;
        String description = null;
        long userId = 0;
        long postId = 0;
        long repostId = 0;
        int verified = 0;
        int priv = 0;
        int unflaggable = 0;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    if (PropertyConfiguration.USER.equals(p.getCurrentName())) {
                        JsonToken t2 = p.nextToken();
                        while (t2 != null && t2 != JsonToken.END_OBJECT) {
                            switch (t2) {
                                case START_OBJECT:
                                case START_ARRAY:
                                    p.skipChildren();
                                    break;
                                case VALUE_STRING:
                                    String cn = p.getCurrentName();
                                    if ("description".equals(cn)) {
                                        description = p.getText();
                                        break;
                                    } else if ("avatarUrl".equals(cn)) {
                                        avatarUrl = p.getText();
                                        break;
                                    } else if ("location".equals(cn)) {
                                        location = p.getText();
                                        break;
                                    } else if (!"username".equals(cn)) {
                                        break;
                                    } else {
                                        username = p.getText();
                                        break;
                                    }
                                case VALUE_NUMBER_INT:
                                    String cn2 = p.getCurrentName();
                                    if ("verified".equals(cn2)) {
                                        verified = p.getIntValue();
                                        break;
                                    } else if ("private".equals(cn2)) {
                                        priv = p.getIntValue();
                                        break;
                                    } else if ("unflaggable".equals(cn2)) {
                                        unflaggable = p.getIntValue();
                                        break;
                                    } else if (!"userId".equals(cn2)) {
                                        break;
                                    } else {
                                        userId = p.getLongValue();
                                        break;
                                    }
                            }
                            t2 = p.nextToken();
                        }
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
                    break;
                case VALUE_NUMBER_INT:
                    String cn3 = p.getCurrentName();
                    if ("repostId".equals(cn3)) {
                        repostId = p.getLongValue();
                        break;
                    } else if (!"postId".equals(cn3)) {
                        break;
                    } else {
                        postId = p.getLongValue();
                        break;
                    }
                case START_ARRAY:
                    p.skipChildren();
                    break;
            }
            t = p.nextToken();
        }
        return new VineRepost(username, avatarUrl, location, description, userId, postId, repostId, verified, priv, unflaggable);
    }

    static TimelineItem parseTimelineItem(JsonParser p) throws IOException {
        VinePost vinePost = new VinePost();
        String address = null;
        String categoryIconUrl = null;
        String categoryId = null;
        String categoryShortName = null;
        String city = null;
        String countryCode = null;
        String venueName = null;
        String state = null;
        String videoWebmUrl = null;
        JsonToken t = p.nextToken();
        boolean explicitContent = false;
        boolean verified = false;
        boolean liked = false;
        boolean revined = false;
        boolean promoted = false;
        boolean postVerified = false;
        boolean priv = false;
        long loops = 0;
        double velocity = 0.0d;
        boolean onFire = false;
        VineByline byline = null;
        boolean following = false;
        String timelineItemType = "post";
        String mosaicType = "";
        String link = "";
        String reference = "";
        String avatarUrl = "";
        VineMosaic mosaic = null;
        String title = "";
        boolean hidden = false;
        String descriptionEditableTill = "";
        boolean pinnable = false;
        boolean remixDisabled = false;
        VineUrlAction.VineUrlActionBuilder urlActionBuilder = new VineUrlAction.VineUrlActionBuilder();
        VineSolicitor.VineSolicitorBuilder solicitorBuilder = new VineSolicitor.VineSolicitorBuilder();
        VineFeed.VineFeedBuilder feedBuilder = new VineFeed.VineFeedBuilder();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    String cn = p.getCurrentName();
                    if ("comments".equals(cn)) {
                        vinePost.comments = parsePagedData(p, COMMENT_PARSER);
                        break;
                    } else if ("likes".equals(cn)) {
                        vinePost.likes = parsePagedData(p, LIKES_PARSER);
                        break;
                    } else if ("loops".equals(cn)) {
                        JsonToken t2 = p.nextToken();
                        while (t2 != null && t2 != JsonToken.END_OBJECT) {
                            switch (t2) {
                                case START_OBJECT:
                                case START_ARRAY:
                                    p.skipChildren();
                                    break;
                                case VALUE_NUMBER_INT:
                                    if ("count".equals(p.getCurrentName())) {
                                        loops = p.getLongValue();
                                        break;
                                    } else if (!"onFire".equals(p.getCurrentName())) {
                                        break;
                                    } else if (p.getIntValue() != 1) {
                                        onFire = false;
                                        break;
                                    } else {
                                        onFire = true;
                                        break;
                                    }
                                case VALUE_NUMBER_FLOAT:
                                    if ("count".equals(p.getCurrentName())) {
                                        loops = (long) p.getDoubleValue();
                                        break;
                                    } else if (!"velocity".equals(p.getCurrentName())) {
                                        break;
                                    } else {
                                        velocity = p.getDoubleValue();
                                        break;
                                    }
                                case VALUE_TRUE:
                                case VALUE_FALSE:
                                    if (!"onFire".equals(p.getCurrentName())) {
                                        break;
                                    } else {
                                        onFire = p.getBooleanValue();
                                        break;
                                    }
                            }
                            t2 = p.nextToken();
                        }
                        break;
                    } else if (PropertyConfiguration.USER.equals(cn)) {
                        VineUser user = parseUser(p);
                        vinePost.user = user;
                        vinePost.userBackgroundColor = vinePost.user.profileBackground;
                        feedBuilder.user = user;
                        break;
                    } else if ("repost".equals(cn)) {
                        vinePost.repost = parseRepost(p);
                        break;
                    } else if ("reposts".equals(cn)) {
                        JsonToken t3 = p.nextToken();
                        while (t3 != null && t3 != JsonToken.END_OBJECT) {
                            switch (t3) {
                                case START_OBJECT:
                                case START_ARRAY:
                                    p.skipChildren();
                                    break;
                                case VALUE_NUMBER_INT:
                                    if (!"count".equals(p.getCurrentName())) {
                                        break;
                                    } else {
                                        vinePost.revinersCount = p.getIntValue();
                                        break;
                                    }
                            }
                            t3 = p.nextToken();
                        }
                        break;
                    } else if ("byline".equals(cn)) {
                        byline = parseByline(p);
                        break;
                    } else if ("sources".equals(cn)) {
                        vinePost.sources = parseSources(p);
                        break;
                    } else if ("longform".equals(cn)) {
                        vinePost.longform = parseLongform(p);
                        break;
                    } else if ("coverPost".equals(cn)) {
                        TimelineItem item = parseTimelineItem(p);
                        if (!(item instanceof VinePost)) {
                            break;
                        } else {
                            feedBuilder.coverPost = (VinePost) item;
                            break;
                        }
                    } else if ("feedMetadata".equals(cn)) {
                        feedBuilder.feedMetadata = parseFeedMetadata(p);
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
                    break;
                case VALUE_STRING:
                    String cn2 = p.getCurrentName();
                    if ("foursquareVenueId".equals(cn2)) {
                        vinePost.foursquareVenueId = p.getText();
                        break;
                    } else if ("description".equals(cn2)) {
                        vinePost.description = p.getText();
                        break;
                    } else if ("created".equals(cn2)) {
                        vinePost.created = DateTimeUtil.getTimeInMsFromString(p.getText(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                        break;
                    } else if ("location".equals(cn2)) {
                        vinePost.location = p.getText();
                        break;
                    } else if ("avatarUrl".equals(cn2)) {
                        avatarUrl = p.getText();
                        vinePost.avatarUrl = avatarUrl;
                        break;
                    } else if ("videoLowURL".equals(cn2)) {
                        vinePost.videoLowUrl = p.getText();
                        break;
                    } else if ("videoPreview".equals(cn2)) {
                        vinePost.videoPreview = p.getText();
                        break;
                    } else if ("videoUrl".equals(cn2)) {
                        vinePost.videoUrl = p.getText();
                        break;
                    } else if ("videoWebmUrl".equals(cn2)) {
                        videoWebmUrl = p.getText();
                        break;
                    } else if ("username".equals(cn2)) {
                        vinePost.username = p.getText();
                        break;
                    } else if ("shareUrl".equals(cn2)) {
                        vinePost.shareUrl = p.getText();
                        break;
                    } else if ("thumbnailUrl".equals(cn2)) {
                        vinePost.thumbnailUrl = p.getText();
                        break;
                    } else if ("thumbnailLowUrl".equals(cn2)) {
                        vinePost.thumbnailLowUrl = p.getText();
                        break;
                    } else if ("thumbnailMedUrl".equals(cn2)) {
                        vinePost.thumbnailMedUrl = p.getText();
                        break;
                    } else if ("venueAddress".equals(cn2)) {
                        address = p.getText();
                        break;
                    } else if ("venueCategoryIconUrl".equals(cn2)) {
                        categoryIconUrl = p.getText();
                        break;
                    } else if ("venueCategoryId".equals(cn2)) {
                        categoryId = p.getText();
                        break;
                    } else if ("venueCategoryShortName".equals(cn2)) {
                        categoryShortName = p.getText();
                        break;
                    } else if ("venueCity".equals(cn2)) {
                        city = p.getText();
                        break;
                    } else if ("venueCountryCode".equals(cn2)) {
                        countryCode = p.getText();
                        break;
                    } else if ("venueName".equals(cn2)) {
                        venueName = p.getText();
                        break;
                    } else if ("state".equals(cn2)) {
                        state = p.getText();
                        break;
                    } else if ("profileBackground".equals(cn2)) {
                        vinePost.userBackgroundColor = (int) Long.parseLong(p.getText().substring(2), 16);
                        break;
                    } else if ("type".equalsIgnoreCase(cn2)) {
                        timelineItemType = p.getText();
                        break;
                    } else if ("mosaicType".equals(cn2)) {
                        mosaicType = p.getText();
                        break;
                    } else if ("link".equals(cn2)) {
                        link = p.getText();
                        break;
                    } else if ("title".equals(cn2)) {
                        title = p.getText();
                        break;
                    } else if ("backgroundColor".equals(cn2)) {
                        feedBuilder.backgroundColor(p.getText());
                        break;
                    } else if ("secondaryColor".equals(cn2)) {
                        feedBuilder.secondaryColor(p.getText());
                        break;
                    } else if ("backgroundImage".equals(cn2)) {
                        urlActionBuilder.backgroundImageUrl(p.getText());
                        break;
                    } else if ("backgroundVideo".equals(cn2)) {
                        urlActionBuilder.backgroundVideoUrl(p.getText());
                        break;
                    } else if ("actionTitle".equals(cn2)) {
                        urlActionBuilder.actionTitle(p.getText());
                        break;
                    } else if ("actionIcon".equals(cn2)) {
                        urlActionBuilder.actionIconUrl(p.getText());
                        break;
                    } else if ("actionLink".equals(cn2)) {
                        urlActionBuilder.actionLink(p.getText());
                        break;
                    } else if ("reference".equals(cn2)) {
                        reference = p.getText();
                        urlActionBuilder.reference(reference);
                        solicitorBuilder.reference(reference);
                        break;
                    } else if ("buttonText".equals(cn2)) {
                        solicitorBuilder.buttonText(p.getText());
                        break;
                    } else if ("dismissText".equals(cn2)) {
                        solicitorBuilder.dismissText(p.getText());
                        break;
                    } else if ("completeTitle".equals(cn2)) {
                        solicitorBuilder.completeTitle(p.getText());
                        break;
                    } else if ("completeDescription".equals(cn2)) {
                        solicitorBuilder.completeDescription(p.getText());
                        break;
                    } else if ("completeExplanation".equals(cn2)) {
                        solicitorBuilder.completeExplanation(p.getText());
                        break;
                    } else if ("completeButton".equals(cn2)) {
                        solicitorBuilder.completeButton(p.getText());
                        break;
                    } else if ("customLikeIconUrl".equals(cn2)) {
                        vinePost.customLikeIconUrl = p.getText();
                        break;
                    } else if (!"descriptionEditableTill".equals(cn2)) {
                        break;
                    } else {
                        descriptionEditableTill = p.getText();
                        break;
                    }
                case VALUE_NUMBER_INT:
                    String cn3 = p.getCurrentName();
                    if ("explicitContent".equals(cn3)) {
                        if (p.getIntValue() != 1) {
                            explicitContent = false;
                            break;
                        } else {
                            explicitContent = true;
                            break;
                        }
                    } else if ("postFlags".equals(cn3)) {
                        vinePost.postFlags = p.getIntValue();
                        break;
                    } else if ("postVerified".equals(cn3)) {
                        if (p.getIntValue() != 1) {
                            postVerified = false;
                            break;
                        } else {
                            postVerified = true;
                            break;
                        }
                    } else if ("promoted".equals(cn3)) {
                        if (p.getIntValue() != 1) {
                            promoted = false;
                            break;
                        } else {
                            promoted = true;
                            break;
                        }
                    } else if ("verified".equals(cn3)) {
                        if (p.getIntValue() != 1) {
                            verified = false;
                            break;
                        } else {
                            verified = true;
                            break;
                        }
                    } else if ("postId".equals(cn3)) {
                        vinePost.postId = p.getLongValue();
                        break;
                    } else if ("timelineItemId".equals(cn3)) {
                        vinePost.timelineItemId = p.getLongValue();
                        break;
                    } else if ("feedId".equals(cn3)) {
                        feedBuilder.feedId(p.getLongValue());
                        break;
                    } else if ("userId".equals(cn3)) {
                        long userId = p.getLongValue();
                        vinePost.userId = userId;
                        feedBuilder.userId(userId);
                        break;
                    } else if ("myRepostId".equals(cn3)) {
                        vinePost.myRepostId = p.getLongValue();
                        if (vinePost.myRepostId <= 0) {
                            break;
                        } else {
                            revined = true;
                            break;
                        }
                    } else if (!"private".equals(cn3)) {
                        if (!"liked".equals(cn3)) {
                            if ("following".equals(cn3)) {
                                if (p.getIntValue() != 1) {
                                    following = false;
                                    break;
                                } else {
                                    following = true;
                                    break;
                                }
                            } else if ("closeable".equals(cn3)) {
                                boolean closeable = p.getIntValue() == 1;
                                urlActionBuilder.closeable(closeable);
                                solicitorBuilder.closeable(closeable);
                                break;
                            } else if ("hidden".equals(cn3)) {
                                if (p.getIntValue() != 1) {
                                    hidden = false;
                                    break;
                                } else {
                                    hidden = true;
                                    break;
                                }
                            } else if ("pinnable".equals(cn3)) {
                                if (p.getIntValue() != 1) {
                                    pinnable = false;
                                    break;
                                } else {
                                    pinnable = true;
                                    break;
                                }
                            } else if (!"remixDisabled".equals(cn3)) {
                                break;
                            } else if (p.getIntValue() != 1) {
                                remixDisabled = false;
                                break;
                            } else {
                                remixDisabled = true;
                                break;
                            }
                        } else if (p.getLongValue() != 1) {
                            liked = false;
                            break;
                        } else {
                            liked = true;
                            break;
                        }
                    } else if (p.getLongValue() != 1) {
                        priv = false;
                        break;
                    } else {
                        priv = true;
                        break;
                    }
                case START_ARRAY:
                    String cn4 = p.getCurrentName();
                    if ("tags".equals(cn4)) {
                        vinePost.tags = parseTagsArray(p);
                        break;
                    } else if ("entities".equals(cn4)) {
                        JsonToken t4 = p.nextToken();
                        ArrayList<VineEntity> entities = null;
                        while (t4 != null && t4 != JsonToken.END_ARRAY) {
                            VineEntity entity = parseEntity(p);
                            if (entities == null) {
                                entities = new ArrayList<>();
                            }
                            if (entity != null) {
                                entities.add(entity);
                            }
                            t4 = p.nextToken();
                        }
                        if (entities == null) {
                            break;
                        } else {
                            Util.validateAndFixEntities(entities);
                            vinePost.entities = entities;
                            break;
                        }
                    } else if ("videoUrls".equals(cn4)) {
                        vinePost.setUpUrls(parseVideoUrls(p));
                        break;
                    } else if ("audio_tracks".equals(cn4)) {
                        vinePost.audioMetadata = parseAudioMetadata(p);
                        break;
                    } else if ("records".equals(cn4)) {
                        mosaic = parseVineMosaicRecords(p);
                        break;
                    } else if ("thumbnails".equals(cn4)) {
                        vinePost.setUpThumbnails(parseThumbsArray(p));
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
                case VALUE_NUMBER_FLOAT:
                    if (!"duration".equals(p.getCurrentName())) {
                        break;
                    } else {
                        vinePost.duration = p.getDoubleValue();
                        break;
                    }
                case VALUE_TRUE:
                case VALUE_FALSE:
                    String cn5 = p.getCurrentName();
                    if ("liked".equals(cn5)) {
                        liked = p.getBooleanValue();
                        break;
                    } else if (!"revined".equals(cn5)) {
                        break;
                    } else {
                        revined = p.getBooleanValue();
                        break;
                    }
            }
            t = p.nextToken();
        }
        if ("urlAction".equals(timelineItemType)) {
            urlActionBuilder.type(timelineItemType).title(title).description(vinePost.description);
            return new VineUrlAction(urlActionBuilder);
        }
        if ("solicitation".equals(timelineItemType)) {
            solicitorBuilder.type(timelineItemType).title(title).description(vinePost.description);
            return new VineSolicitor(solicitorBuilder);
        }
        if ("feed".equals(timelineItemType)) {
            feedBuilder.type(timelineItemType).title(title).description(vinePost.description);
            if (feedBuilder.coverPost == null || feedBuilder.feedMetadata == null) {
                SLog.d("feed item has empty coverpost or feedmetadata id: {}", Long.valueOf(feedBuilder.feedId));
                return null;
            }
            return new VineFeed(feedBuilder);
        }
        if (vinePost.postId == 0 && vinePost.avatarUrl != null && vinePost.username != null) {
            VineUserRecord userRecord = new VineUserRecord();
            userRecord.userId = vinePost.userId;
            userRecord.avatarUrl = vinePost.avatarUrl;
            userRecord.username = vinePost.username;
            return userRecord;
        }
        if (mosaic != null) {
            mosaic.title = title;
            mosaic.description = vinePost.description;
            mosaic.link = link;
            mosaic.reference = reference;
            mosaic.avatarUrl = avatarUrl;
            mosaic.type = timelineItemType;
            mosaic.mosaicType = mosaicType;
            mosaic.pinnable = Boolean.valueOf(pinnable);
            return mosaic;
        }
        if (TextUtils.isEmpty(vinePost.videoUrl) && videoWebmUrl != null) {
            vinePost.videoUrl = videoWebmUrl;
        }
        if (vinePost.foursquareVenueId != null) {
            vinePost.venueData = new VineVenue(categoryIconUrl, categoryShortName, categoryId, city, countryCode, venueName, state, address);
        }
        vinePost.setFlagExplicit(explicitContent);
        vinePost.setFlagVerified(verified);
        vinePost.setFlagRevined(revined);
        vinePost.setFlagLiked(liked);
        vinePost.setFlagPromoted(promoted);
        vinePost.setFlagPostVerified(postVerified);
        vinePost.setFlagPrivate(priv);
        vinePost.likesCount = vinePost.likes != null ? vinePost.likes.count : 0;
        vinePost.commentsCount = vinePost.comments != null ? vinePost.comments.count : 0;
        vinePost.loops = loops;
        vinePost.lastRefresh = System.currentTimeMillis();
        vinePost.velocity = velocity;
        vinePost.onFire = onFire;
        vinePost.byline = byline;
        vinePost.following = following;
        vinePost.originalFollowingState = following;
        vinePost.hidden = hidden;
        vinePost.remixDisabled = remixDisabled;
        vinePost.descriptionEditableTill = DateTimeUtil.getTimeInMsFromString(descriptionEditableTill, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        return vinePost;
    }

    private static VineMosaic parseVineMosaicRecords(JsonParser p) throws IOException {
        JsonToken t = p.nextToken();
        VineMosaic mosaic = new VineMosaic();
        mosaic.timelineItemType = TimelineItemType.POST_MOSAIC;
        ArrayList<TimelineItem> items = new ArrayList<>();
        while (t != null && t != JsonToken.END_ARRAY) {
            TimelineItem item = parseTimelineItem(p);
            if (item != null) {
                items.add(item);
                if (item instanceof VineUserRecord) {
                    mosaic.timelineItemType = TimelineItemType.USER_MOSAIC;
                }
            }
            t = p.nextToken();
        }
        mosaic.mosaicItems = items;
        return mosaic;
    }

    static VinePost parsePost(JsonParser p) throws IOException {
        TimelineItem item = parseTimelineItem(p);
        return item.getType() == TimelineItemType.POST ? (VinePost) item : new VinePost();
    }

    private static List<VineVideoUrlTier> parseVideoUrls(JsonParser p) throws IOException {
        ArrayList<VineVideoUrlTier> urls = new ArrayList<>();
        JsonToken t = p.getCurrentToken();
        while (t != null && t != JsonToken.END_ARRAY) {
            t = p.nextToken();
            if (t == JsonToken.START_OBJECT) {
                String url = null;
                float rate = 0.0f;
                String format = null;
                t = p.nextToken();
                while (t != null && t != JsonToken.END_OBJECT) {
                    String currentName = p.getCurrentName();
                    if (t == JsonToken.VALUE_STRING) {
                        if ("videoUrl".equals(currentName)) {
                            url = p.getText();
                        } else if ("format".equals(currentName)) {
                            format = p.getText();
                        }
                    } else if (t == JsonToken.VALUE_NUMBER_FLOAT) {
                        if ("rate".equals(currentName)) {
                            rate = p.getFloatValue();
                        }
                    } else if (t == JsonToken.VALUE_NUMBER_INT && "rate".equals(currentName)) {
                        rate = p.getIntValue();
                    }
                    t = p.nextToken();
                }
                if (!"dash".equals(format)) {
                    VineVideoUrlTier vinePostUrl = new VineVideoUrlTier(url, rate, format);
                    urls.add(vinePostUrl);
                }
            }
        }
        return urls;
    }

    public static ArrayList<VineTag> parseTagsArray(JsonParser p) throws IOException {
        ArrayList<VineTag> tags = new ArrayList<>();
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_ARRAY) {
            if (t == JsonToken.START_OBJECT) {
                tags.add(parseTag(p));
            }
            t = p.nextToken();
        }
        return tags;
    }

    public static ArrayList<ThumbnailData> parseThumbsArray(JsonParser p) throws IOException {
        ArrayList<ThumbnailData> thumbnails = new ArrayList<>();
        JsonToken t = p.getCurrentToken();
        while (t != null && t != JsonToken.END_ARRAY) {
            if (t == JsonToken.START_OBJECT) {
                String url = null;
                int width = 0;
                int height = 0;
                t = p.nextToken();
                while (t != null && t != JsonToken.END_OBJECT) {
                    String currentName = p.getCurrentName();
                    if (t == JsonToken.VALUE_STRING) {
                        if ("url".equals(currentName)) {
                            url = p.getText();
                        }
                    } else if (t == JsonToken.VALUE_NUMBER_INT) {
                        if ("width".equals(currentName)) {
                            width = p.getIntValue();
                        } else if ("height".equals(currentName)) {
                            height = p.getIntValue();
                        }
                    }
                    t = p.nextToken();
                }
                thumbnails.add(new ThumbnailData(url, width, height));
            }
        }
        return thumbnails;
    }

    private static ArrayList<VineAudioMetadata> parseAudioMetadata(JsonParser p) throws IOException {
        VineAudioMetadata audioMetadata;
        ArrayList<VineAudioMetadata> metadata = new ArrayList<>();
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_ARRAY) {
            if (t == JsonToken.START_OBJECT && (audioMetadata = parseAudioMetadatum(p)) != null) {
                metadata.add(audioMetadata);
            }
            t = p.nextToken();
        }
        if (metadata.size() == 0) {
            return null;
        }
        return metadata;
    }

    private static VineAudioMetadata parseAudioMetadatum(JsonParser p) throws IOException {
        VineAudioMetadataResponse response = VineAudioMetadataResponse$$JsonObjectMapper._parse(p);
        if (response == null || response.track == null || response.track.artistName == null || response.track.trackName == null) {
            return null;
        }
        long trackId = response.trackId == 0 ? response.track.trackId : response.trackId;
        return VineAudioMetadata.create(trackId, response.track.artistName, response.track.trackName, response.track.albumArtUrl, response.origin, response.track.hasAudioTrackTimeline);
    }

    private static VineLongform parseLongform(JsonParser p) throws IOException {
        return VineLongform$$JsonObjectMapper._parse(p);
    }

    private static ArrayList<VineSource> parseSources(JsonParser p) throws IOException {
        VineSourceResponse response = VineSourceResponse$$JsonObjectMapper._parse(p);
        if (response == null || response.records == null) {
            return null;
        }
        ArrayList<VineSource> sources = new ArrayList<>();
        for (int i = 0; i < response.records.size(); i++) {
            VineSourceResponse.Record record = response.records.get(i);
            if (record.contentType != 0 && record.postId != 0 && record.username != null && record.thumbnailUrl != null) {
                sources.add(VineSource.create(record.contentType, record.postId, record.username, record.description, record.thumbnailUrl, "", 0.0f, 0.0f, 0.0f));
            }
        }
        if (sources.isEmpty()) {
            return null;
        }
        return sources;
    }

    private static VineChannelsResponse.Data parsePagedChannels(InputStream inputStream) throws IOException {
        return ((VineChannelsResponse) LoganSquare.parse(inputStream, VineChannelsResponse.class)).data;
    }

    private static synchronized VineChannel parseChannel(JsonParser p) throws IOException {
        return VineChannel$$JsonObjectMapper._parse(p);
    }

    public static VinePagedTagsResponse.Data parsePagedTags(InputStream inputStream) throws IOException {
        return ((VinePagedTagsResponse) LoganSquare.parse(inputStream, VinePagedTagsResponse.class)).data;
    }

    public static VineEndlessLikesResponse parseEndlessLikes(InputStream inputStream) throws IOException {
        return (VineEndlessLikesResponse) LoganSquare.parse(inputStream, VineEndlessLikesResponse.class);
    }

    public static VineTag parseTag(JsonParser p) throws IOException {
        return new VineTagConverter().parse(p);
    }

    private static VineComment parseComment(InputStream inputStream) throws IOException {
        return postProcessComment(((VineCommentResponse) LoganSquare.parse(inputStream, VineCommentResponse.class)).data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static VineComment parseComment(JsonParser p) throws IOException {
        return postProcessComment(VineComment$$JsonObjectMapper._parse(p));
    }

    private static ApiResponse parseGeneral(InputStream inputStream) throws IOException {
        GeneralResponse response = (GeneralResponse) LoganSquare.parse(inputStream, GeneralResponse.class);
        if (response.success == null) {
            return null;
        }
        return response.success.booleanValue() ? ApiResponse.SUCCESS : ApiResponse.FAILURE;
    }

    private static Long parseLikeId(InputStream inputStream) throws IOException {
        LikeResponse response = (LikeResponse) LoganSquare.parse(inputStream, LikeResponse.class);
        return Long.valueOf((response == null || response.data == null) ? 0L : response.data.likeId);
    }

    private static VinePagedUsersResponse.Data parsePagedUsers(InputStream inputStream) throws IOException {
        return ((VinePagedUsersResponse) LoganSquare.parse(inputStream, VinePagedUsersResponse.class)).data;
    }

    private static ArrayList<VineUser> parseUsersArray(InputStream inputStream) throws IOException {
        return ((VineUsersResponseWrapper) LoganSquare.parse(inputStream, VineUsersResponseWrapper.class)).data;
    }

    private static ArrayList<VineUser> parseUsersArray(JsonParser p) throws IOException {
        JsonToken t = p.nextToken();
        ArrayList<VineUser> users = new ArrayList<>();
        while (t != null && t != JsonToken.END_ARRAY) {
            if (t == JsonToken.START_OBJECT) {
                users.add(parseUser(p));
            }
            t = p.nextToken();
        }
        return users;
    }

    private static VineUser parseUser(InputStream inputStream) throws IOException {
        return VineUser.fromVineUserResponse(((VineUserResponseWrapper) LoganSquare.parse(inputStream, VineUserResponseWrapper.class)).data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static VineUser parseUser(JsonParser p) throws IOException {
        return VineUser.fromVineUserResponse(VineUserResponse$$JsonObjectMapper._parse(p));
    }

    private static VineLogin parseSignUp(InputStream inputStream) throws IOException {
        SignUpResponse response = (SignUpResponse) LoganSquare.parse(inputStream, SignUpResponse.class);
        return new VineLogin((response == null || response.data == null) ? null : response.data.key, null, (response == null || response.data == null) ? 0L : response.data.userId, (response == null || response.data == null) ? null : response.data.edition);
    }

    public static FeedMetadata parseFeedMetadata(JsonParser p) throws IOException {
        return FeedMetadata$$JsonObjectMapper._parse(p);
    }

    public static TwitterUser parseTwitterUser(InputStream inputStream) throws IOException {
        TwitterUser twitterUser = (TwitterUser) LoganSquare.parse(inputStream, TwitterUser.class);
        if (twitterUser.profileUrl != null) {
            twitterUser.profileUrl = twitterUser.profileUrl.replace("_normal", "_bigger");
        }
        return twitterUser;
    }

    public static VineLogin parseLogin(InputStream inputStream) throws IOException {
        VineLoginResponse login = (VineLoginResponse) LoganSquare.parse(inputStream, VineLoginResponse.class);
        if (!Boolean.TRUE.equals(login.success)) {
            return null;
        }
        return new VineLogin(login.data.key, login.data.username, login.data.userId.longValue(), login.data.edition);
    }

    public static VineError parseError(InputStream inputStream) throws IOException {
        GeneralError error = (GeneralError) LoganSquare.parse(inputStream, GeneralError.class);
        return VineError.create(error.code.intValue(), error.error, error.data);
    }

    public static VineError parseMessageError(JsonParser p) throws IOException {
        int error = 0;
        String reason = null;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    p.skipChildren();
                    break;
                case VALUE_STRING:
                    if (!"message".equals(p.getCurrentName())) {
                        break;
                    } else {
                        reason = p.getText();
                        break;
                    }
                case VALUE_NUMBER_INT:
                    if (!"code".equals(p.getCurrentName())) {
                        break;
                    } else {
                        error = p.getIntValue();
                        break;
                    }
                case START_ARRAY:
                    p.skipChildren();
                    break;
            }
            t = p.nextToken();
        }
        return VineError.create(error, reason);
    }

    public static ArrayList<VineRTCConversation> parseRTCEvent(JsonParser p) throws IOException, NumberFormatException {
        ArrayList<VineRTCConversation> event = null;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    if (!"data".equals(p.getCurrentName())) {
                        break;
                    } else {
                        JsonToken t2 = p.nextToken();
                        while (t2 != null && t2 != JsonToken.END_OBJECT) {
                            switch (t2) {
                                case START_OBJECT:
                                    if (!"conversations".equals(p.getCurrentName())) {
                                        break;
                                    } else {
                                        event = parseRTCConversations(p);
                                        break;
                                    }
                            }
                            t2 = p.nextToken();
                        }
                        break;
                    }
                    break;
                case START_ARRAY:
                    p.skipChildren();
                    break;
            }
            t = p.nextToken();
        }
        return event;
    }

    public static ArrayList<VineRTCConversation> parseRTCConversations(JsonParser p) throws IOException, NumberFormatException {
        ArrayList<VineRTCConversation> conversations = new ArrayList<>();
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    long conversationId = Long.parseLong(p.getCurrentName());
                    ArrayList<VineRTCParticipant> participants = parseRTCParticipants(p);
                    conversations.add(new VineRTCConversation(conversationId, participants));
                    break;
                case START_ARRAY:
                    p.skipChildren();
                    break;
            }
            t = p.nextToken();
        }
        return conversations;
    }

    public static ArrayList<VineRTCParticipant> parseRTCParticipants(JsonParser p) throws IOException, NumberFormatException {
        ArrayList<VineRTCParticipant> participants = new ArrayList<>();
        boolean connected = false;
        boolean typing = false;
        long lastMessageId = -1;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    long userId = Long.parseLong(p.getCurrentName());
                    JsonToken t2 = p.nextToken();
                    while (t2 != null && t2 != JsonToken.END_OBJECT) {
                        switch (t2) {
                            case VALUE_NUMBER_INT:
                                if (!"last_message_id".equals(p.getCurrentName())) {
                                    break;
                                } else {
                                    lastMessageId = p.getLongValue();
                                    break;
                                }
                            case VALUE_TRUE:
                            case VALUE_FALSE:
                                String cn = p.getCurrentName();
                                if ("connected".equals(cn)) {
                                    connected = p.getBooleanValue();
                                    break;
                                } else if (!"typing".equals(cn)) {
                                    break;
                                } else {
                                    typing = p.getBooleanValue();
                                    break;
                                }
                        }
                        t2 = p.nextToken();
                    }
                    participants.add(new VineRTCParticipant(userId, connected, typing, lastMessageId));
                    break;
                case START_ARRAY:
                    p.skipChildren();
                    break;
            }
            t = p.nextToken();
        }
        return participants;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static VineConversation parseConversation(JsonParser p) throws IOException {
        JsonToken t = p.nextToken();
        long conversationId = 0;
        long createdBy = 0;
        long lastMessage = 0;
        long unreadMessageCount = 0;
        ArrayList<VinePrivateMessage> messages = null;
        ArrayList<Long> users = null;
        int networkType = 1;
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    p.skipChildren();
                    break;
                case VALUE_STRING:
                    if (!"inbox".equals(p.getCurrentName()) || !"other".equals(p.getValueAsString())) {
                        break;
                    } else {
                        networkType = 2;
                        break;
                    }
                case VALUE_NUMBER_INT:
                    String cn = p.getCurrentName();
                    if ("conversationId".equals(cn)) {
                        conversationId = p.getLongValue();
                        break;
                    } else if ("lastMessage".equals(cn)) {
                        lastMessage = p.getLongValue();
                        break;
                    } else if ("lastMessageRead".equals(cn)) {
                        p.getLongValue();
                        break;
                    } else if ("unreadMessageCount".equals(cn)) {
                        unreadMessageCount = p.getLongValue();
                        break;
                    } else if (!"createdBy".equals(cn)) {
                        break;
                    } else {
                        createdBy = p.getLongValue();
                        break;
                    }
                case START_ARRAY:
                    String name = p.getCurrentName();
                    if ("messages".equals(name)) {
                        JsonToken t2 = p.nextToken();
                        while (t2 != null && t2 != JsonToken.END_ARRAY) {
                            VinePrivateMessage message = parsePrivateMessage(p);
                            if (messages == null) {
                                messages = new ArrayList<>();
                            }
                            if (messages != null) {
                                messages.add(message);
                            }
                            t2 = p.nextToken();
                        }
                        break;
                    } else if ("users".equals(name)) {
                        JsonToken t3 = p.nextToken();
                        while (t3 != null && t3 != JsonToken.END_ARRAY) {
                            switch (t3) {
                                case VALUE_NUMBER_INT:
                                    long userId = p.getLongValue();
                                    if (users == null) {
                                        users = new ArrayList<>();
                                    }
                                    if (users == null) {
                                        break;
                                    } else {
                                        users.add(Long.valueOf(userId));
                                        break;
                                    }
                            }
                            t3 = p.nextToken();
                        }
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
                case VALUE_TRUE:
                case VALUE_FALSE:
                    if (!"deleted".equals(p.getCurrentName())) {
                        break;
                    } else {
                        p.getBooleanValue();
                        break;
                    }
            }
            t = p.nextToken();
        }
        return new VineConversation(conversationId, lastMessage, createdBy, messages, users, networkType, unreadMessageCount);
    }

    private static VineNotificationSettingsResponse.Data parsePagedNotificationSettings(InputStream inputStream) throws IOException {
        return ((VineNotificationSettingsResponse) LoganSquare.parse(inputStream, VineNotificationSettingsResponse.class)).data;
    }

    private static VineHomeFeedSettingsResponse.Data parsePagedHomeFeedSettings(InputStream inputStream) throws IOException {
        return ((VineHomeFeedSettingsResponse) LoganSquare.parse(inputStream, VineHomeFeedSettingsResponse.class)).data;
    }

    public static VinePrivateMessage parsePrivateMessage(JsonParser p) throws IOException {
        JsonToken t = p.nextToken();
        long conversationId = 0;
        long messageId = 0;
        long userId = 0;
        long created = 0;
        String message = null;
        String videoUrl = null;
        String thumbnailUrl = null;
        VinePost post = null;
        int networkType = 1;
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    if ("post".equals(p.getCurrentName())) {
                        post = parsePost(p);
                        videoUrl = post.videoUrl;
                        thumbnailUrl = post.thumbnailUrl;
                        String str = post.thumbnailLowUrl;
                        String str2 = post.thumbnailMedUrl;
                        String str3 = post.videoPreview;
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
                case VALUE_STRING:
                    String name = p.getCurrentName();
                    if ("message".equals(name)) {
                        message = p.getText();
                        break;
                    } else if ("videoUrl".equals(name)) {
                        videoUrl = p.getText();
                        break;
                    } else if ("thumbnailUrl".equals(name)) {
                        thumbnailUrl = p.getText();
                        break;
                    } else if ("created".equals(name)) {
                        created = DateTimeUtil.getTimeInMsFromString(p.getText(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                        break;
                    } else if (!"inbox".equals(p.getCurrentName()) || !"other".equals(p.getValueAsString())) {
                        break;
                    } else {
                        networkType = 2;
                        break;
                    }
                    break;
                case VALUE_NUMBER_INT:
                    String name2 = p.getCurrentName();
                    if ("conversationId".equals(name2)) {
                        conversationId = p.getLongValue();
                        break;
                    } else if ("messageId".equals(name2)) {
                        messageId = p.getLongValue();
                        break;
                    } else if ("userId".equals(name2)) {
                        userId = p.getLongValue();
                        break;
                    } else {
                        break;
                    }
                case START_ARRAY:
                    String name3 = p.getCurrentName();
                    if ("videoUrls".equals(name3)) {
                        List<VineVideoUrlTier> urls = parseVideoUrls(p);
                        videoUrl = getHighestH264Url(urls);
                        if (TextUtils.isEmpty(videoUrl)) {
                            videoUrl = getHighestWebmUrl(urls);
                            break;
                        } else {
                            break;
                        }
                    } else if ("thumbnails".equals(name3)) {
                        for (ThumbnailData data : parseThumbsArray(p)) {
                            if (data.width == 120) {
                                String str4 = data.url;
                            } else if (data.width == 240) {
                                String str5 = data.url;
                            } else if (data.width == 480) {
                                thumbnailUrl = data.url;
                            }
                        }
                        break;
                    } else {
                        break;
                    }
            }
            t = p.nextToken();
        }
        return new VinePrivateMessage(-1L, conversationId, messageId, userId, created, message, videoUrl, thumbnailUrl, networkType, false, post, 0, (String) null);
    }

    private static String getHighestH264Url(List<VineVideoUrlTier> urls) {
        if (urls != null && urls.size() > 0) {
            VineVideoUrlTier topTier = null;
            for (int i = 0; i < urls.size(); i++) {
                if ("h264".equals(urls.get(i).format) && (topTier == null || urls.get(i).rate > topTier.rate)) {
                    VineVideoUrlTier topTier2 = urls.get(i);
                    topTier = topTier2;
                }
            }
            if (topTier != null) {
                SLog.d("ryango highest h264 {}", topTier.url);
                return topTier.url;
            }
        }
        return null;
    }

    private static String getHighestWebmUrl(List<VineVideoUrlTier> urls) {
        if (urls != null && urls.size() > 0) {
            VineVideoUrlTier topTier = null;
            for (int i = 0; i < urls.size(); i++) {
                if ("webm".equals(urls.get(i).format) && (topTier == null || urls.get(i).rate > topTier.rate)) {
                    VineVideoUrlTier topTier2 = urls.get(i);
                    topTier = topTier2;
                }
            }
            if (topTier != null) {
                SLog.d("ryango highest webm {}", topTier.url);
                return topTier.url;
            }
        }
        return null;
    }

    private static PagedActivityResponse.Data parsePagedActivity(InputStream inputStream) throws IOException {
        PagedActivityResponse response = (PagedActivityResponse) LoganSquare.parse(inputStream, PagedActivityResponse.class);
        PagedActivityResponse.Data pagedData = response.data;
        if (pagedData.items != null) {
            for (int i = 0; i < pagedData.items.size(); i++) {
                postProcessSingleNotification(pagedData.items.get(i));
            }
        }
        return pagedData;
    }

    public static VineSingleNotification parsePushNotification(String str) throws IOException {
        List<VineSingleNotification> notifications = LoganSquare.parseList(str, VineSingleNotification.class);
        if (notifications.size() < 1) {
            return null;
        }
        return postProcessSingleNotification(notifications.get(0));
    }

    private static VineSingleNotification postProcessSingleNotification(VineSingleNotification notification) {
        if (notification == null) {
            return null;
        }
        if (notification.getComment() != null) {
            notification.username = Util.getUsernameFromActivity(notification.getComment());
        }
        if (notification.entities != null) {
            Util.validateAndFixEntities(notification.entities);
            return notification;
        }
        return notification;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static VineEverydayNotification parseEverydayNotification(JsonParser p) throws IOException, NumberFormatException {
        JsonToken t = p.nextToken();
        String body = null;
        long notificationId = 0;
        long createdAt = Long.MAX_VALUE;
        String notificationType = null;
        ArrayList<VineEntity> entities = null;
        VineUser user = null;
        VinePost post = null;
        VineMilestone milestone = null;
        boolean isNew = false;
        String anchor = null;
        String backAnchor = null;
        String link = null;
        String shortBody = null;
        String commentText = null;
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    if (PropertyConfiguration.USER.equals(p.getCurrentName())) {
                        user = parseUser(p);
                        break;
                    } else if ("post".equals(p.getCurrentName())) {
                        post = parsePost(p);
                        break;
                    } else if ("milestone".equals(p.getCurrentName())) {
                        milestone = parseMilestone(p);
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
                case VALUE_STRING:
                    String name = p.getCurrentName();
                    if ("body".equals(name)) {
                        body = p.getText();
                        break;
                    } else if ("type".equals(name)) {
                        notificationType = p.getText();
                        break;
                    } else if ("lastActivityTime".equals(name)) {
                        createdAt = DateTimeUtil.getTimeInMsFromString(p.getText(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                        break;
                    } else if ("anchor".equals(name)) {
                        anchor = p.getText();
                        break;
                    } else if ("backAnchor".equals(name)) {
                        backAnchor = p.getText();
                        break;
                    } else if ("link".equals(name)) {
                        link = p.getText();
                        break;
                    } else if ("shortBody".equals(name)) {
                        shortBody = p.getText();
                        break;
                    } else if (!"commentText".equals(name)) {
                        break;
                    } else {
                        commentText = p.getText();
                        break;
                    }
                case VALUE_NUMBER_INT:
                    String name2 = p.getCurrentName();
                    if ("activityId".equals(name2)) {
                        notificationId = p.getLongValue();
                        break;
                    } else if ("isNew".equals(name2)) {
                        if (p.getIntValue() != 1) {
                            isNew = false;
                            break;
                        } else {
                            isNew = true;
                            break;
                        }
                    } else if (!"anchor".equals(name2)) {
                        break;
                    } else {
                        anchor = Long.toString(p.getLongValue());
                        break;
                    }
                case START_ARRAY:
                    if ("entities".equals(p.getCurrentName())) {
                        JsonToken t2 = p.nextToken();
                        while (t2 != null && t2 != JsonToken.END_ARRAY) {
                            VineEntity entity = parseEntity(p);
                            if (entities == null) {
                                entities = new ArrayList<>();
                            }
                            if (entity != null) {
                                entities.add(entity);
                            }
                            t2 = p.nextToken();
                        }
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
            }
            t = p.nextToken();
        }
        VineEverydayNotification result = new VineEverydayNotification(body, notificationId, notificationType, post, user, entities, milestone, createdAt, isNew, anchor, backAnchor, link, shortBody, commentText);
        if (entities != null) {
            Util.validateAndFixEntities(entities);
        }
        return result;
    }

    public static VineMilestone parseMilestone(JsonParser p) throws IOException, NumberFormatException {
        String backgroundImageUrl = null;
        String backgroundVideoUrl = null;
        String iconUrl = null;
        String title = null;
        String description = null;
        String milestoneUrl = null;
        int overlayColor = 0;
        int blurRadius = 0;
        float overlayAlpha = 0.0f;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case VALUE_STRING:
                    if ("backgroundImageUrl".equals(p.getCurrentName())) {
                        backgroundImageUrl = p.getText();
                        break;
                    } else if ("backgroundVideoUrl".equals(p.getCurrentName())) {
                        backgroundVideoUrl = p.getText();
                        break;
                    } else if ("iconUrl".equals(p.getCurrentName())) {
                        iconUrl = p.getText();
                        break;
                    } else if ("title".equals(p.getCurrentName())) {
                        title = p.getText();
                        break;
                    } else if ("description".equals(p.getCurrentName())) {
                        description = p.getText();
                        break;
                    } else if ("milestoneUrl".equals(p.getCurrentName())) {
                        milestoneUrl = p.getText();
                        break;
                    } else if ("overlayAlpha".equals(p.getCurrentName())) {
                        overlayAlpha = Float.parseFloat(p.getText());
                        break;
                    } else if ("overlayColor".equals(p.getCurrentName())) {
                        overlayColor = Integer.decode(p.getText()).intValue();
                        break;
                    } else if (!"gaussianBlurRadius".equals(p.getCurrentName())) {
                        break;
                    } else {
                        blurRadius = Integer.parseInt(p.getText());
                        break;
                    }
                case VALUE_NUMBER_INT:
                    if (!"gaussianBlurRadius".equals(p.getCurrentName())) {
                        break;
                    } else {
                        blurRadius = p.getIntValue();
                        break;
                    }
            }
            t = p.nextToken();
        }
        return new VineMilestone(backgroundImageUrl, backgroundVideoUrl, iconUrl, title, description, milestoneUrl, overlayColor, overlayAlpha, blurRadius);
    }

    public static VineEntity parseEntity(JsonParser p) throws IOException {
        String type = null;
        String link = null;
        String title = null;
        long id = 0;
        int start = -1;
        int end = -1;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case VALUE_STRING:
                    String cn = p.getCurrentName();
                    if ("type".equals(cn)) {
                        type = p.getText();
                        break;
                    } else if ("link".equals(cn)) {
                        link = p.getText();
                        break;
                    } else if (!"title".equals(cn)) {
                        break;
                    } else {
                        title = p.getText();
                        break;
                    }
                case VALUE_NUMBER_INT:
                    if (!"id".equals(p.getCurrentName())) {
                        break;
                    } else {
                        id = p.getLongValue();
                        break;
                    }
                case START_ARRAY:
                    if ("range".equals(p.getCurrentName())) {
                        JsonToken t2 = p.nextToken();
                        while (t2 != null && t2 != JsonToken.END_ARRAY) {
                            switch (t2) {
                                case VALUE_NUMBER_INT:
                                    if (start == -1) {
                                        start = p.getIntValue();
                                        break;
                                    } else {
                                        end = p.getIntValue();
                                        break;
                                    }
                            }
                            t2 = p.nextToken();
                        }
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
            }
            t = p.nextToken();
        }
        return new VineEntity(type, title, link, start, end, id);
    }

    private static ServerStatus parseServerStatus(InputStream inputStream) throws IOException {
        return (ServerStatus) LoganSquare.parse(inputStream, ServerStatus.class);
    }

    private static VineClientFlags parseClientFlags(InputStream inputStream) throws IOException {
        VineClientFlags flags = ((VineClientFlagsResponse) LoganSquare.parse(inputStream, VineClientFlagsResponse.class)).data;
        if (flags.doubleTapToLikeOnPause == null) {
            flags.doubleTapToLikeOnPause = true;
        }
        if (flags.audioLatencyUs == null) {
            flags.audioLatencyUs = 160000;
        }
        if (flags.prefetchEnabled == null) {
            flags.prefetchEnabled = false;
        }
        if (flags.useVinePlayer == null) {
            flags.useVinePlayer = false;
        }
        if (flags.ttlSeconds == null) {
            flags.ttlSeconds = 0L;
        }
        return flags;
    }

    private static VinePostResponse parseVinePostResponse(InputStream inputStream) throws IOException {
        return ((VinePostResponseWrapper) LoganSquare.parse(inputStream, VinePostResponseWrapper.class)).data;
    }

    private static VinePrivateMessagePostResponseWithUsers parsePrivateMessageResponse(JsonParser p) throws IOException {
        ArrayList<VinePrivateMessageResponse> responses = new ArrayList<>();
        ArrayList<VineRecipient> recipients = new ArrayList<>();
        ArrayList<VineUser> users = null;
        VinePost post = null;
        int networkType = 1;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case START_OBJECT:
                    p.skipChildren();
                    break;
                case VALUE_STRING:
                    if (!"inbox".equals(p.getCurrentName()) || !"other".equals(p.getValueAsString())) {
                        break;
                    } else {
                        networkType = 2;
                        break;
                    }
                    break;
                case START_ARRAY:
                    if ("messages".equals(p.getCurrentName())) {
                        while (t != null && t != JsonToken.END_ARRAY) {
                            switch (t) {
                                case START_OBJECT:
                                    VineError error = null;
                                    String videoUrl = null;
                                    String thumbnailUrl = null;
                                    String shareUrl = null;
                                    String email = null;
                                    String phone = null;
                                    String recipientIdString = null;
                                    String videoWebmUrl = null;
                                    long conversationId = 0;
                                    long messageId = 0;
                                    long userId = -1;
                                    while (t != null && t != JsonToken.END_OBJECT) {
                                        String name = p.getCurrentName();
                                        switch (t) {
                                            case START_OBJECT:
                                                String objName = p.getCurrentName();
                                                if ("to".equals(objName)) {
                                                    while (t != null && t != JsonToken.END_OBJECT) {
                                                        String toName = p.getCurrentName();
                                                        switch (t) {
                                                            case VALUE_STRING:
                                                                if (toName.equals("email")) {
                                                                    email = p.getText();
                                                                    break;
                                                                } else if (toName.equals("phoneNumber")) {
                                                                    phone = p.getText();
                                                                    break;
                                                                } else if (!toName.equals("recipientId")) {
                                                                    break;
                                                                } else {
                                                                    recipientIdString = p.getText();
                                                                    break;
                                                                }
                                                            case VALUE_NUMBER_INT:
                                                                if (!toName.equals("userId")) {
                                                                    break;
                                                                } else {
                                                                    userId = p.getLongValue();
                                                                    break;
                                                                }
                                                        }
                                                        t = p.nextToken();
                                                    }
                                                    break;
                                                } else if ("error".equals(objName)) {
                                                    error = parseMessageError(p);
                                                    break;
                                                } else if (!"post".equals(objName)) {
                                                    break;
                                                } else {
                                                    post = parsePost(p);
                                                    break;
                                                }
                                            case VALUE_STRING:
                                                if ("videoUrl".equals(name)) {
                                                    videoUrl = p.getText();
                                                    break;
                                                } else if ("thumbnailUrl".equals(name)) {
                                                    thumbnailUrl = p.getText();
                                                    break;
                                                } else if ("shareUrl".equals(name)) {
                                                    shareUrl = p.getText();
                                                    break;
                                                } else if (!"videoWebmUrl".equals(name)) {
                                                    break;
                                                } else {
                                                    videoWebmUrl = p.getText();
                                                    break;
                                                }
                                            case VALUE_NUMBER_INT:
                                                if ("conversationId".equals(name)) {
                                                    conversationId = p.getLongValue();
                                                    break;
                                                } else if (!"messageId".equals(name)) {
                                                    break;
                                                } else {
                                                    messageId = p.getLongValue();
                                                    break;
                                                }
                                            case START_ARRAY:
                                                if (!"videoUrls".equals(p.getCurrentName())) {
                                                    break;
                                                } else {
                                                    List<VineVideoUrlTier> urls = parseVideoUrls(p);
                                                    videoUrl = getHighestH264Url(urls);
                                                    videoWebmUrl = getHighestWebmUrl(urls);
                                                    if (!TextUtils.isEmpty(videoUrl)) {
                                                        break;
                                                    } else {
                                                        videoUrl = videoWebmUrl;
                                                        break;
                                                    }
                                                }
                                        }
                                        t = p.nextToken();
                                    }
                                    long recipientId = -1;
                                    if (!TextUtils.isEmpty(recipientIdString)) {
                                        recipientId = Long.valueOf(recipientIdString).longValue();
                                    }
                                    VineRecipient recipient = null;
                                    if (email != null) {
                                        recipient = VineRecipient.fromEmail(null, userId, email, recipientId);
                                    } else if (phone != null) {
                                        recipient = VineRecipient.fromPhone(null, userId, phone, recipientId);
                                    } else if (userId > 0) {
                                        recipient = VineRecipient.fromUser(null, userId, 0, recipientId);
                                    } else {
                                        CrashUtil.log("No email, phone, or userId for recipient for message {}", Long.valueOf(messageId));
                                    }
                                    if (TextUtils.isEmpty(videoUrl)) {
                                        videoUrl = videoWebmUrl;
                                    }
                                    recipients.add(recipient);
                                    responses.add(new VinePrivateMessageResponse(recipient, conversationId, messageId, videoUrl, thumbnailUrl, shareUrl, error, networkType));
                                    break;
                            }
                            t = p.nextToken();
                        }
                        break;
                    } else if ("users".equals(p.getCurrentName())) {
                        users = parseUsersArray(p);
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
                    break;
            }
            t = p.nextToken();
        }
        return new VinePrivateMessagePostResponseWithUsers(responses, recipients, users, post);
    }

    public static ArrayList<ComplaintMenuOption> parseComplaintMenu(InputStream inputStream) throws IOException {
        ComplaintMenuOptionResponse response = (ComplaintMenuOptionResponse) LoganSquare.parse(inputStream, ComplaintMenuOptionResponse.class);
        ArrayList<ComplaintMenuOption> result = new ArrayList<>();
        if (response != null && response.data != null && response.data.size() != 0) {
            for (Map.Entry<String, ComplaintMenuOptionResponse.ComplaintMenuData> entry : response.data.entrySet()) {
                ComplaintMenuOption option = new ComplaintMenuOption();
                option.complaintType = entry.getKey();
                option.prompt = entry.getValue().prompt;
                ArrayList<ComplaintMenuOptionResponse.ComplaintMenuData.Choice> choices = entry.getValue().choices;
                option.complaintChoices = new ComplaintMenuOption.ComplaintChoice[choices.size()];
                for (int i = 0; i < choices.size(); i++) {
                    option.complaintChoices[i] = new ComplaintMenuOption.ComplaintChoice();
                    option.complaintChoices[i].title = choices.get(i).title;
                    option.complaintChoices[i].value = choices.get(i).value;
                    option.complaintChoices[i].confirmation = choices.get(i).confirmation;
                }
                result.add(option);
            }
        }
        return result;
    }

    public static JsonParser createParser(InputStream inputStream) throws IOException {
        JsonParser jp = JSON_FACTORY.createJsonParser(inputStream);
        jp.nextToken();
        return jp;
    }

    public static JsonParser createParser(String inputString) throws IOException {
        JsonParser jp = JSON_FACTORY.createJsonParser(inputString);
        jp.nextToken();
        return jp;
    }

    public static SearchResult parseSearchResults(JsonParser p) throws IOException {
        ListSection<VineSearchSuggestion> suggestions = null;
        ListSection<VineChannel> users = null;
        ListSection<VineChannel> listSection = null;
        ListSection<VineChannel> listSection2 = null;
        ListSection<VineChannel> channels = null;
        boolean hasResults = false;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            String name = p.getCurrentName();
            if ("suggestions".equals(name)) {
                ArrayList<VineSearchSuggestion> items = new ArrayList<>();
                JsonToken t2 = p.nextToken();
                if (t2 == JsonToken.START_ARRAY) {
                    while (t2 != null && t2 != JsonToken.END_ARRAY) {
                        if (t2 == JsonToken.VALUE_STRING && "query".equals(p.getCurrentName()) && !TextUtils.isEmpty(p.getText())) {
                            items.add(VineSearchSuggestion.create(p.getText()));
                        }
                        t2 = p.nextToken();
                    }
                }
                suggestions = ListSection.create(0, null, null, "suggestions", items);
            } else if ("results".equals(name)) {
                while (t != null && t != JsonToken.END_ARRAY) {
                    ListSection<VineChannel> section = parseListSection(p);
                    if (section != null && section.getType() != null) {
                        switch (section.getType()) {
                            case "users":
                                users = section;
                                break;
                            case "tags":
                                listSection = section;
                                break;
                            case "posts":
                                listSection2 = section;
                                break;
                            case "channels":
                                channels = section;
                                break;
                        }
                        if (section.getItems().size() > 0) {
                            hasResults = true;
                        }
                    }
                    t = p.nextToken();
                }
            }
            t = p.nextToken();
        }
        return SearchResult.create(suggestions, users, listSection, listSection2, channels, hasResults);
    }

    public static ListSection parseListSection(JsonParser p) throws IOException {
        int displayCount = 0;
        String anchorStr = null;
        String backAnchor = null;
        String type = null;
        ArrayList items = new ArrayList();
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            String name = p.getCurrentName();
            switch (t) {
                case START_OBJECT:
                    type = name;
                    if (type == null) {
                        break;
                    } else {
                        while (t != null && t != JsonToken.END_OBJECT) {
                            String name2 = p.getCurrentName();
                            switch (t) {
                                case VALUE_STRING:
                                    if ("anchorStr".equals(name2) || "anchor".equals(name2)) {
                                        anchorStr = p.getText();
                                        break;
                                    } else if (!"backAnchor".equals(name2)) {
                                        break;
                                    } else {
                                        backAnchor = p.getText();
                                        break;
                                    }
                                case START_ARRAY:
                                    if (!"records".equals(name2)) {
                                        break;
                                    } else if ("users".equals(type)) {
                                        items = parseUsersArray(p);
                                        break;
                                    } else if ("tags".equals(type)) {
                                        items = parseTagsArray(p);
                                        break;
                                    } else if ("posts".equals(type)) {
                                        JsonToken t2 = p.nextToken();
                                        while (t2 != null && t2 != JsonToken.END_ARRAY) {
                                            VinePost post = parsePost(p);
                                            items.add(post);
                                            t2 = p.nextToken();
                                        }
                                        break;
                                    } else if (!"channels".equals(type)) {
                                        break;
                                    } else {
                                        JsonToken t3 = p.nextToken();
                                        while (t3 != null && t3 != JsonToken.END_ARRAY) {
                                            VineChannel channel = parseChannel(p);
                                            items.add(channel);
                                            t3 = p.nextToken();
                                        }
                                        break;
                                    }
                            }
                            t = p.nextToken();
                        }
                        break;
                    }
                    break;
                case VALUE_NUMBER_INT:
                    if (!"displayCount".equals(name)) {
                        break;
                    } else {
                        displayCount = p.getIntValue();
                        break;
                    }
            }
            t = p.nextToken();
        }
        if (items.size() > 0) {
            return ListSection.create(displayCount, anchorStr, backAnchor, type, items);
        }
        return null;
    }
}
