package co.vine.android.provider;

import android.database.Cursor;
import co.vine.android.api.VineRecipient;

/* loaded from: classes.dex */
public class VineDatabaseSQL {

    public static final class ChannelsQuery {
        public static final String[] PROJECTION = {"_id", "channel_id", "channel", "timestamp", "background_color", "font_color", "last_used_timestamp", "is_last", "icon_full_url", "retina_icon_full_url", "following", "retina_explore_icon_full_url"};
    }

    public static final class ConversationMessageUsersQuery {
        public static final String[] PROJECTION = {"_id", "conversation_row_id", "message_id", "user_row_id", "timestamp", "message", "video_url", "thumbnail_url", "is_last", "vanished", "max_loops", "vanished_date", "ephemeral", "local_start_time", "deleted", "post_id", "upload_path", "error_code", "error_reason", "conversation_id", "network_type", "unread_message_count", "is_hidden", "sender_user_name", "sender_avatar", "sender_profile_background", "author_user_id", "author_user_name", "author_avatar", "post_entities", "post_description", "post_share_url"};
    }

    public static final class ConversationRecipientsUsersViewQuery {
        public static final String[] PROJECTION = {"_id", "conversation_row_id", "user_row_id", "username", "phone_number", "email_address", "user_id", "profile_background"};
    }

    public static final class EditionsQuery {
        public static final String[] PROJECTION = {"_id", "edition_code", "edition_name"};
    }

    public static final class InboxQuery {
        public static final String[] PROJECTION = {"_id", "conversation_row_id", "timestamp", "network_type", "unread_message_count", "is_hidden", "last_message", "is_last", "username", "user_row_id", "user_id", "following_flag", "avatar_url", "profile_background", "recipients_count", "error_reason", "user_is_external", "twitter_screenname", "twitter_hidden", "user_verified"};
    }

    public static final class LikesQuery {
        public static final String[] PROJECTION = {"_id", "like_id", "post_id", "avatar_url", "user_id", "username", "timestamp", "location", "verified"};
    }

    public static final class MessagesQuery {
        public static final String[] PROJECTION = {"_id", "conversation_row_id", "message_id", "user_row_id", "timestamp", "message", "video_url", "thumbnail_url", "is_last", "vanished", "max_loops", "vanished_date", "ephemeral", "local_start_time", "deleted", "post_id", "upload_path", "error_code", "error_reason"};
    }

    public static final class NotificationsQuery {
        public static final String[] PROJECTION = {"_id", "notification_id", "notification_type", "message", "cleared", "conversation_row_id", "avatar_url"};
    }

    public static final class PageCursorsQuery {
        public static final String[] PROJECTION = {"_id", "type", "tag", "kind", "next_page", "previous_page", "anchor", "reversed"};
    }

    public static final class PostGroupsQuery {
        public static final String[] PROJECTION = {"post_id", "post_type", "tag", "sort_id"};
    }

    public static final class TableQuery {
        public static final String[] PROJECTION = {"_id"};
    }

    public static final class TagsQuery {
        public static final String[] PROJECTION = {"_id", "tag_id", "tag_name", "last_used_timestamp", "is_last"};
    }

    public static final class TagsRecentlyUsedQuery {
        public static final String[] PROJECTION = {"_id", "tag_id", "tag_name", "last_used_timestamp"};
    }

    public static final class UserGroupsQuery {
        public static final String[] PROJECTION = {"user_id"};
    }

    public static final class UsersQuery {
        public static final String[] PROJECTION = {"_id", "user_id", "avatar_url", "blocked", "blocking", "description", "location", "explicit", "follower_count", "following_count", "following_flag", "like_count", "post_count", "username", "verified", "follow_status", "order_id", "is_last", "accepts_oon_conversations", "profile_background", "section_index", "section_type", "section_title", "last_section_refresh", "type", "loop_count", "twitter_screenname", "twitter_hidden"};

        public static VineRecipient getVineRecipient(Cursor cursor) {
            VineRecipient vr = VineRecipient.fromUser(cursor.getString(13), cursor.getLong(1), cursor.getInt(19), -1L);
            vr.avatarUrl = cursor.getString(2);
            vr.lastFriendRefresh = cursor.getLong(23);
            vr.sectionTitle = cursor.getString(22);
            vr.friendIndex = cursor.getLong(20);
            vr.sectionIndex = (int) (vr.friendIndex >> 32);
            vr.twitterScreenname = cursor.getString(26);
            vr.twitterHidden = cursor.getInt(27) == 1;
            vr.verified = cursor.getInt(14) == 1;
            return vr;
        }
    }
}
