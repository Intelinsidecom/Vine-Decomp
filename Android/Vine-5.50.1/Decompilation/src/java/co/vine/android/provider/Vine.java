package co.vine.android.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.StringAnchorManager;
import java.util.HashMap;

/* loaded from: classes.dex */
public class Vine {
    public static String AUTHORITY = CrossConstants.getAuthority(".provider.VineProvider");
    public static final String CONTENT_AUTHORITY = "content://" + AUTHORITY + "/";
    private static final HashMap<String, StringAnchorManager> sUserSectionAnchorManager = new HashMap<>();

    public static final class Channels implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "channels");
    }

    public static final class ConversationMessageUsersView implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "message_users_view");
        public static final Uri CONTENT_URI_CONVERSATION = Uri.parse(Vine.CONTENT_AUTHORITY + "message_users_view/conversation");
    }

    public static final class ConversationRecipients implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "conversation_recipients");
        public static final Uri CONTENT_URI_CONVERSATION = Uri.parse(Vine.CONTENT_AUTHORITY + "conversation_recipients/conversation");
    }

    public static final class ConversationRecipientsUsersView implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "conversation_recipients_users_view");
        public static final Uri CONTENT_URI_CONVERSATION = Uri.parse(Vine.CONTENT_AUTHORITY + "conversation_recipients_users_view/conversation");
    }

    public static final class Conversations implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "conversations");
    }

    public static final class Editions implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "editions");
    }

    public static final class InboxView implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "inbox_view");
    }

    public static final class Likes implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "likes");
    }

    public static final class Messages implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "messages");
        public static final Uri CONTENT_URI_SINGLE_MESSAGE = Uri.parse(Vine.CONTENT_AUTHORITY + "messages/message");
    }

    public static final class PostCommentsLikesView implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view");
        public static final Uri CONTENT_URI_ALL_TIMELINE = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view/all_timeline");
        public static final Uri CONTENT_URI_ALL_TIMELINE_USER = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view/all_timeline_user");
        public static final Uri CONTENT_URI_ALL_TIMELINE_USER_LIKES = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view/all_timeline_user_likes");
        public static final Uri CONTENT_URI_ALL_TIMELINE_ON_THE_RISE = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view/all_timeline_on_the_rise");
        public static final Uri CONTENT_URI_ALL_TIMELINE_POPULAR_NOW = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view/all_timeline_popular");
        public static final Uri CONTENT_URI_ALL_TIMELINE_TAG = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view/all_timeline_tag");
        public static final Uri CONTENT_URI_ALL_TIMELINE_SINGLE = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view/post");
        public static final Uri CONTENT_URI_ALL_TIMELINE_CHANNEL_POPULAR = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view/timeline_channel_popular");
        public static final Uri CONTENT_URI_ALL_TIMELINE_CHANNEL_RECENT = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view/timeline_channel_recent");
        public static final Uri CONTENT_URI_ARBITRARY_TIMELINE = Uri.parse(Vine.CONTENT_AUTHORITY + "post_comments_likes_view/timelines");
    }

    public static final class PostGroupsView implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "post_groups_view");
        public static final Uri CONTENT_URI_TIMELINE = Uri.parse(Vine.CONTENT_AUTHORITY + "post_groups_view/timeline");
        public static final Uri CONTENT_URI_USER_PROFILE = Uri.parse(Vine.CONTENT_AUTHORITY + "post_groups_view/user_profile");
        public static final Uri CONTENT_URI_FIND_USER_LIKES = Uri.parse(Vine.CONTENT_AUTHORITY + "post_groups_view/user_likes");
    }

    public static final class Settings implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "settings");
    }

    public static final class TagSearchResults implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "tag_search_results");
    }

    public static final class TagsRecentlyUsed implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "tag_recently_used");
        public static final Uri CONTENT_URI_PUT_TAG = Uri.parse(Vine.CONTENT_AUTHORITY + "tag_recently_used/put_tag");
        public static final Uri CONTENT_URI_UPDATE_RECENT_TAG = Uri.parse(Vine.CONTENT_AUTHORITY + "tag_recently_used/update_tag");
    }

    public static final class UserGroupsView implements BaseColumns, UserSections {
        public static final Uri CONTENT_URI = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view");
        public static final Uri CONTENT_URI_FOLLOWERS = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view/followers");
        public static final Uri CONTENT_URI_FOLLOWING = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view/following");
        public static final Uri CONTENT_URI_ALL_FOLLOW = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view/all_follow");
        public static final Uri CONTENT_URI_FRIENDS = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view/friends");
        public static final Uri CONTENT_URI_FRIENDS_FILTER = Uri.withAppendedPath(CONTENT_URI_FRIENDS, "filter");
        public static final Uri CONTENT_URI_FIND_FRIENDS_TWITTER = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view/find_friends_twitter");
        public static final Uri CONTENT_URI_FIND_FRIENDS_FACEBOOK = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view/find_friends_facebook");
        public static final Uri CONTENT_URI_FIND_FRIENDS_ADDRESS = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view/find_friends_address");
        public static final Uri CONTENT_URI_LIKERS = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view/likers");
        public static final Uri CONTENT_URI_REVINERS = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view/reviners");
        public static final Uri CONTENT_URI_USER_SEARCH_RESULTS = Uri.parse(Vine.CONTENT_AUTHORITY + "user_groups_view/user_search_results");
    }

    public interface UserSections extends BaseColumns {
    }

    public static StringAnchorManager getUserSectionsAnchorManager(Context context, int sectionType) {
        String key = "user_sections_" + sectionType;
        StringAnchorManager manager = sUserSectionAnchorManager.get(key);
        if (manager == null) {
            StringAnchorManager manager2 = new StringAnchorManager(context.getApplicationContext(), key);
            sUserSectionAnchorManager.put(key, manager2);
            return manager2;
        }
        return manager;
    }
}
