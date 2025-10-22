namespace Vine.Models.Analytics;

public static class Constants
{
	public static class EventTypes
	{
		public const string Revine = "revine";

		public const string Like = "like";

		public const string Comment = "comment";

		public const string ApiRequest = "api_request";

		public const string VideoDownloaded = "video_downloaded";

		public const string PostPlayed = "post_played";

		public const string VideoUploaded = "video_uploaded";

		public const string PostCreated = "post_created";

		public const string SessionStarted = "session_started";

		public const string SessionEnded = "session_ended";

		public const string ViewImpression = "view_impression";

		public const string LaunchViewDataComplete = "launch_view_data_complete";

		public const string LaunchViewComplete = "launch_view_complete";

		public const string VideoPlayedAfterAppstart = "video_played_after_appstart";

		public const string SearchQueryChanged = "search_query_changed";

		public const string ItemSelected = "item_selected";

		public const string ItemUnselected = "item_unselected";

		public const string ItemShared = "item_shared";

		public const string MessageCreated = "message_created";

		public const string Follow = "follow";

		public const string Unfollow = "unfollow";

		public const string FollowAll = "follow_all";

		public const string UnfollowAll = "unfollow_all";

		public const string AlertDisplay = "alert_display";

		public const string AlertDismiss = "alert_dismiss";

		public const string ExternalApiRequest = "external_api_request";
	}

	public static class Views
	{
		public const string Camera = "camera";

		public const string Drafts = "drafts";

		public const string Import = "import";

		public const string Edit = "edit";

		public const string Details = "details";

		public const string Profile = "profile";

		public const string Followers = "followers";

		public const string Following = "following";

		public const string Timeline = "timeline";

		public const string Comments = "comments";

		public const string Revines = "revines";

		public const string Likes = "likes";

		public const string FindFriends = "find_friends";

		public const string VM = "vm";

		public const string VMConversation = "vm_conversation";

		public const string VMSearch = "vm_search";

		public const string Settings = "settings";

		public const string Search = "search";

		public const string Share = "share";

		public const string Terms = "terms";

		public const string PrivacyPolicy = "privacy_policy";

		public const string TwitterTerms = "twitter_terms";

		public const string TwitterPrivacyPolicy = "twitter_privacy_policy";

		public const string TwitterXAuth = "twitter_xauth";

		public const string Front = "front";

		public const string SignupEmail1 = "signup_email_1";

		public const string SignupEmail2 = "signup_email_2";

		public const string SignupTwitter = "signup_twitter";

		public const string SignupTwitterXAuth = "signup_twitter_xauth";

		public const string Signin = "signin";

		public const string ResetPassword = "reset_password";

		public const string ChannelFollow = "channel_follow";
	}

	public static class SubViews
	{
		public static class Import
		{
			public const string Video = "video";

			public const string Audio = "audio";

			public const string TrimVideo = "trim_video";

			public const string TrimAudio = "trim_audio";
		}

		public static class FindFriends
		{
			public const string AddressBook = "address_book";

			public const string Twitter = "twitter";

			public const string Suggestions = "suggestions";

			public const string PromptConnect = "prompt_connect";

			public const string InviteText = "invite_text";

			public const string InviteEmail = "invite_email";
		}

		public static class Search
		{
			public const string Users = "users";

			public const string Tags = "tags";

			public const string Posts = "posts";

			public const string Universal = "universal";
		}

		public static class Share
		{
			public const string Friends = "friends";
		}
	}

	public static class Alerts
	{
		public const string NoChannels = "no_channels";

		public const string NoFriends = "no_friends";

		public const string AllowAbAccess = "allow_ab_access";

		public const string AbAccessDenied = "ab_access_denied";

		public const string CreateAccountTwitter = "create_account_twitter";

		public const string NoAvatar = "no_avatar";

		public const string DeactivatedAccount = "deactivated_account";

		public const string DeactivatedTwitterAccount = "deactivated_twitter_account";

		public const string ResetPassword = "reset_password";

		public const string AccountExistsTwitter = "account_exists_twitter";

		public const string NoPassword = "no_password";

		public const string InvalidPassword = "invalid_password";

		public const string NoEmail = "no_email";

		public const string InvalidEmail = "invalid_email";

		public const string NoUsername = "no_username";

		public const string InvalidUsername = "invalid_username";
	}
}
