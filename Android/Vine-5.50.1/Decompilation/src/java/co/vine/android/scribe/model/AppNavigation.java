package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.mobileapptracker.MATEvent;

@JsonObject
/* loaded from: classes.dex */
public class AppNavigation {

    @JsonField(name = {"capture_source_section"})
    public String captureSourceSection;

    @JsonField(name = {"filtering"})
    public String filtering;

    @JsonField(name = {"new_search_view"})
    public boolean isNewSearchView;

    @JsonField(name = {"search_query"})
    public String searchQuery;

    @JsonField(name = {"section"})
    public String section;

    @JsonField(name = {"subview"})
    public String subview;

    @JsonField(name = {"timeline_api_url"})
    public String timelineApiUrl;

    @JsonField(name = {"ui_element"})
    public String ui_element;

    @JsonField(name = {"view"})
    public String view;

    public enum Sections {
        HOME("home"),
        EXPLORE("explore"),
        ACTIVITY("activity"),
        MY_PROFILE("my_profile"),
        CAPTURE("capture"),
        LOGGED_OUT("logged_out"),
        EMPTY("");

        private String name;

        Sections(String name) {
            this.name = name;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }

    public enum Views {
        CAMERA("camera"),
        DRAFTS("drafts"),
        IMPORT("import"),
        EDIT("edit"),
        CREATE("create"),
        PROFILE("profile"),
        FOLLOWERS("followers"),
        FOLLOWING("following"),
        ACTIVITY_USER_LIST("activity_user_list"),
        TIMELINE("timeline"),
        ACTIVITY_LIST("activity_list"),
        EXPLORE_WEBVIEW("explore_webview"),
        EXPLORE_GRID("explore_grid"),
        COMMENTS("comments"),
        REVINES("revines"),
        LIKES("likes"),
        FIND_FRIENDS("find_friends"),
        VM("vm"),
        VM_CONVERSATION("vm_conversation"),
        VM_SEARCH("vm_search"),
        SETTINGS("settings"),
        SEARCH(MATEvent.SEARCH),
        SHARE(MATEvent.SHARE),
        TERMS("terms"),
        PRIVACY_POLICY("privacy_policy"),
        TWITTER_TERMS("twitter_terms"),
        TWITTER_PRIVACY_POLICY("twitter_privacy_policy"),
        TWITTER_XAUTH("twitter_xauth"),
        WELCOME_FEED("welcome_feed"),
        PLAYLIST("playlist"),
        FRONT("front"),
        SIGN_UP_EMAIL_1("signup_email_1"),
        SIGN_UP_EMAIL_2("signup_email_2"),
        SIGNUP_TWITTER("signup_twitter"),
        SIGNUP_TWITTER_XAUTH("signup_twitter_xauth"),
        SIGNIN("signin"),
        RESET_PASSWORD("reset_password"),
        CHANNEL_FOLLOW("channel_follow"),
        EMPTY("");

        private String name;

        Views(String name) {
            this.name = name;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }

    public enum Subviews {
        VIDEO("video"),
        AUDIO("audio"),
        TRIM_VIDEO("trim_video"),
        TRIM_AUDIO("trim_audio"),
        ADDRESS_BOOK("address_book"),
        TWITTER("twitter"),
        FACEBOOK("facebook"),
        SUGGESTIONS("suggestions"),
        PROMPT_CONNECT("prompt_connect"),
        INVITE_TEXT("invite_text"),
        INVITE_EMAIL("invite_email"),
        TAGS("tags"),
        POSTS("posts"),
        UNIVERSAL("universal"),
        FRIENDS("friends"),
        EMPTY("");

        private String name;

        Subviews(String name) {
            this.name = name;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }

    public enum UIElements {
        WELCOME_FEED_DONE("done"),
        WELCOME_FEED_LAST_CELL("last_cell"),
        PLAYLIST_SWIPE_RIGHT("swipe_right"),
        PLAYLIST_SWIPE_LEFT("swipe_left"),
        PLAYLIST_HOLD_TO_LOOP("hold_to_loop"),
        PLAYLIST_DISMISS_X("dismiss_x"),
        PLAYLIST_DISMISS_SWIPE("dismiss_swipe"),
        BYLINE("byline"),
        FOLLOW_ON_TWITTER_CARD("follow_on_twitter_card"),
        LAUNCH_TWITTER_FROM_FOLLOW_CARD("launch_twitter_from_follow_card"),
        TWITTER_HANDLE_TAP("twitter_handle_tap"),
        OPEN_TWITTER_TAP("open_twitter_tap"),
        SHOW_TWITTER_CARD("show_twitter_card"),
        TWITTER_LINK("twitter_link"),
        REC_CAROUSEL_RIGHT_SWIPE("follow_card_carousel_swipe_right"),
        REC_CAROUSEL_LEFT_SWIPE("follow_card_carousel_swipe_left");

        private String name;

        UIElements(String name) {
            this.name = name;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }
}
