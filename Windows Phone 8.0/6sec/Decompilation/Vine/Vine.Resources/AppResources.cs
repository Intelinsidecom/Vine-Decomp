using System.CodeDom.Compiler;
using System.ComponentModel;
using System.Diagnostics;
using System.Globalization;
using System.Resources;
using System.Runtime.CompilerServices;

namespace Vine.Resources;

[GeneratedCode("System.Resources.Tools.StronglyTypedResourceBuilder", "4.0.0.0")]
[DebuggerNonUserCode]
[CompilerGenerated]
public class AppResources
{
	private static ResourceManager resourceMan;

	private static CultureInfo resourceCulture;

	[EditorBrowsable(EditorBrowsableState.Advanced)]
	public static ResourceManager ResourceManager
	{
		get
		{
			if (resourceMan == null)
			{
				resourceMan = new ResourceManager("Vine.Resources.AppResources", typeof(AppResources).Assembly);
			}
			return resourceMan;
		}
	}

	[EditorBrowsable(EditorBrowsableState.Advanced)]
	public static CultureInfo Culture
	{
		get
		{
			return resourceCulture;
		}
		set
		{
			resourceCulture = value;
		}
	}

	public static string About => ResourceManager.GetString("About", resourceCulture);

	public static string AboutTwitter => ResourceManager.GetString("AboutTwitter", resourceCulture);

	public static string AcceptFollowRequest => ResourceManager.GetString("AcceptFollowRequest", resourceCulture);

	public static string AcceptPolicyPrivacy => ResourceManager.GetString("AcceptPolicyPrivacy", resourceCulture);

	public static string ActivateToastNotification => ResourceManager.GetString("ActivateToastNotification", resourceCulture);

	public static string ActiveUpload => ResourceManager.GetString("ActiveUpload", resourceCulture);

	public static string Activity => ResourceManager.GetString("Activity", resourceCulture);

	public static string ActivityFollowing => ResourceManager.GetString("ActivityFollowing", resourceCulture);

	public static string ActivityNews => ResourceManager.GetString("ActivityNews", resourceCulture);

	public static string Add => ResourceManager.GetString("Add", resourceCulture);

	public static string AddALocation => ResourceManager.GetString("AddALocation", resourceCulture);

	public static string AddFavorite => ResourceManager.GetString("AddFavorite", resourceCulture);

	public static string AddFriends => ResourceManager.GetString("AddFriends", resourceCulture);

	public static string AddSomeFriendsToSeePosts => ResourceManager.GetString("AddSomeFriendsToSeePosts", resourceCulture);

	public static string AddToPhotoMap => ResourceManager.GetString("AddToPhotoMap", resourceCulture);

	public static string AlreadyHaveAccount => ResourceManager.GetString("AlreadyHaveAccount", resourceCulture);

	public static string AppBaseline => ResourceManager.GetString("AppBaseline", resourceCulture);

	public static string AppBy => ResourceManager.GetString("AppBy", resourceCulture);

	public static string AppDesign => ResourceManager.GetString("AppDesign", resourceCulture);

	public static string ApplyFilter => ResourceManager.GetString("ApplyFilter", resourceCulture);

	public static string AppVersion => ResourceManager.GetString("AppVersion", resourceCulture);

	public static string AskGeoPosition => ResourceManager.GetString("AskGeoPosition", resourceCulture);

	public static string AutoFit => ResourceManager.GetString("AutoFit", resourceCulture);

	public static string AutoPlayVideos => ResourceManager.GetString("AutoPlayVideos", resourceCulture);

	public static string BackCamera => ResourceManager.GetString("BackCamera", resourceCulture);

	public static string BackgroundAgent => ResourceManager.GetString("BackgroundAgent", resourceCulture);

	public static string BackgroundAgentActivate => ResourceManager.GetString("BackgroundAgentActivate", resourceCulture);

	public static string BackgroundAgentMessage => ResourceManager.GetString("BackgroundAgentMessage", resourceCulture);

	public static string BackgroundAgentProblem => ResourceManager.GetString("BackgroundAgentProblem", resourceCulture);

	public static string BestFriends => ResourceManager.GetString("BestFriends", resourceCulture);

	public static string Birthday => ResourceManager.GetString("Birthday", resourceCulture);

	public static string BlockThisPerson => ResourceManager.GetString("BlockThisPerson", resourceCulture);

	public static string BlurLevel => ResourceManager.GetString("BlurLevel", resourceCulture);

	public static string Brush => ResourceManager.GetString("Brush", resourceCulture);

	public static string Cancel => ResourceManager.GetString("Cancel", resourceCulture);

	public static string CancelAll => ResourceManager.GetString("CancelAll", resourceCulture);

	public static string CancelUploadingMessage => ResourceManager.GetString("CancelUploadingMessage", resourceCulture);

	public static string CancelUploadingTitle => ResourceManager.GetString("CancelUploadingTitle", resourceCulture);

	public static string CantExportDataToSkydrive => ResourceManager.GetString("CantExportDataToSkydrive", resourceCulture);

	public static string Caption => ResourceManager.GetString("Caption", resourceCulture);

	public static string Capture => ResourceManager.GetString("Capture", resourceCulture);

	public static string ChangeColor => ResourceManager.GetString("ChangeColor", resourceCulture);

	public static string ChangeFocus => ResourceManager.GetString("ChangeFocus", resourceCulture);

	public static string Changelog => ResourceManager.GetString("Changelog", resourceCulture);

	public static string Channels => ResourceManager.GetString("Channels", resourceCulture);

	public static string CheckpointEnterCredential => ResourceManager.GetString("CheckpointEnterCredential", resourceCulture);

	public static string CheckpointMessage => ResourceManager.GetString("CheckpointMessage", resourceCulture);

	public static string ChooseChannel => ResourceManager.GetString("ChooseChannel", resourceCulture);

	public static string ChooseLocation => ResourceManager.GetString("ChooseLocation", resourceCulture);

	public static string ChoosePicture => ResourceManager.GetString("ChoosePicture", resourceCulture);

	public static string ChoosePosition => ResourceManager.GetString("ChoosePosition", resourceCulture);

	public static string CollageEmptyImage => ResourceManager.GetString("CollageEmptyImage", resourceCulture);

	public static string CollageInnerBorder => ResourceManager.GetString("CollageInnerBorder", resourceCulture);

	public static string CollageOuterBorder => ResourceManager.GetString("CollageOuterBorder", resourceCulture);

	public static string Colors => ResourceManager.GetString("Colors", resourceCulture);

	public static string Comment => ResourceManager.GetString("Comment", resourceCulture);

	public static string Comments => ResourceManager.GetString("Comments", resourceCulture);

	public static string Connect => ResourceManager.GetString("Connect", resourceCulture);

	public static string Connecting => ResourceManager.GetString("Connecting", resourceCulture);

	public static string ConnectToTwitter => ResourceManager.GetString("ConnectToTwitter", resourceCulture);

	public static string ContinueCapture => ResourceManager.GetString("ContinueCapture", resourceCulture);

	public static string ContinueCaptureDescription => ResourceManager.GetString("ContinueCaptureDescription", resourceCulture);

	public static string Contributors => ResourceManager.GetString("Contributors", resourceCulture);

	public static string CopyLink => ResourceManager.GetString("CopyLink", resourceCulture);

	public static string CornerRadius => ResourceManager.GetString("CornerRadius", resourceCulture);

	public static string Create => ResourceManager.GetString("Create", resourceCulture);

	public static string CreateAccount => ResourceManager.GetString("CreateAccount", resourceCulture);

	public static string CreateACustomLocation => ResourceManager.GetString("CreateACustomLocation", resourceCulture);

	public static string Delete => ResourceManager.GetString("Delete", resourceCulture);

	public static string DeleteMyAccount => ResourceManager.GetString("DeleteMyAccount", resourceCulture);

	public static string Description => ResourceManager.GetString("Description", resourceCulture);

	public static string Direct => ResourceManager.GetString("Direct", resourceCulture);

	public static string DisableDoubleTapToLike => ResourceManager.GetString("DisableDoubleTapToLike", resourceCulture);

	public static string DisableToastDuringNight => ResourceManager.GetString("DisableToastDuringNight", resourceCulture);

	public static string Disconnect => ResourceManager.GetString("Disconnect", resourceCulture);

	public static string DisconnectFacebook => ResourceManager.GetString("DisconnectFacebook", resourceCulture);

	public static string DisconnectTwitter => ResourceManager.GetString("DisconnectTwitter", resourceCulture);

	public static string DisplayPopular => ResourceManager.GetString("DisplayPopular", resourceCulture);

	public static string DisplayRecent => ResourceManager.GetString("DisplayRecent", resourceCulture);

	public static string Downloading => ResourceManager.GetString("Downloading", resourceCulture);

	public static string Duration => ResourceManager.GetString("Duration", resourceCulture);

	public static string EditFilter => ResourceManager.GetString("EditFilter", resourceCulture);

	public static string EditMyProfile => ResourceManager.GetString("EditMyProfile", resourceCulture);

	public static string EditorsPicks => ResourceManager.GetString("EditorsPicks", resourceCulture);

	public static string EditPost => ResourceManager.GetString("EditPost", resourceCulture);

	public static string Email => ResourceManager.GetString("Email", resourceCulture);

	public static string EmptyInternetCache => ResourceManager.GetString("EmptyInternetCache", resourceCulture);

	public static string EncodeLogOtherUser => ResourceManager.GetString("EncodeLogOtherUser", resourceCulture);

	public static string EncodingInProgress => ResourceManager.GetString("EncodingInProgress", resourceCulture);

	public static string EnterYourSearch => ResourceManager.GetString("EnterYourSearch", resourceCulture);

	public static string Eraser => ResourceManager.GetString("Eraser", resourceCulture);

	public static string Explore => ResourceManager.GetString("Explore", resourceCulture);

	public static string FacebookChangePage => ResourceManager.GetString("FacebookChangePage", resourceCulture);

	public static string FacebookFriends => ResourceManager.GetString("FacebookFriends", resourceCulture);

	public static string FacebookFriendsFindButton => ResourceManager.GetString("FacebookFriendsFindButton", resourceCulture);

	public static string FacebookFriendsSearchDescription => ResourceManager.GetString("FacebookFriendsSearchDescription", resourceCulture);

	public static string FacebookSelectWhereToPost => ResourceManager.GetString("FacebookSelectWhereToPost", resourceCulture);

	public static string FacebookShareLikeDesc => ResourceManager.GetString("FacebookShareLikeDesc", resourceCulture);

	public static string FacebookShareToPage => ResourceManager.GetString("FacebookShareToPage", resourceCulture);

	public static string Favorite => ResourceManager.GetString("Favorite", resourceCulture);

	public static string Favorites => ResourceManager.GetString("Favorites", resourceCulture);

	public static string Filter => ResourceManager.GetString("Filter", resourceCulture);

	public static string FilterBlur => ResourceManager.GetString("FilterBlur", resourceCulture);

	public static string FilterBlurManual => ResourceManager.GetString("FilterBlurManual", resourceCulture);

	public static string FilterBlurManualFocus => ResourceManager.GetString("FilterBlurManualFocus", resourceCulture);

	public static string FilterBlurNormal => ResourceManager.GetString("FilterBlurNormal", resourceCulture);

	public static string FilterBlurTiltEffect => ResourceManager.GetString("FilterBlurTiltEffect", resourceCulture);

	public static string FilterBorder => ResourceManager.GetString("FilterBorder", resourceCulture);

	public static string FilterContrast => ResourceManager.GetString("FilterContrast", resourceCulture);

	public static string FilterRotate => ResourceManager.GetString("FilterRotate", resourceCulture);

	public static string FindFriends => ResourceManager.GetString("FindFriends", resourceCulture);

	public static string FindFriendsFacebook => ResourceManager.GetString("FindFriendsFacebook", resourceCulture);

	public static string FindFriendsInstagram => ResourceManager.GetString("FindFriendsInstagram", resourceCulture);

	public static string FindFriendsMessage => ResourceManager.GetString("FindFriendsMessage", resourceCulture);

	public static string FindFriendsPhone => ResourceManager.GetString("FindFriendsPhone", resourceCulture);

	public static string FindFriendsTwitter => ResourceManager.GetString("FindFriendsTwitter", resourceCulture);

	public static string FindFriendsVine => ResourceManager.GetString("FindFriendsVine", resourceCulture);

	public static string Flash => ResourceManager.GetString("Flash", resourceCulture);

	public static string FollowButtonChecked => ResourceManager.GetString("FollowButtonChecked", resourceCulture);

	public static string FollowButtonUnchecked => ResourceManager.GetString("FollowButtonUnchecked", resourceCulture);

	public static string FollowEditorPicks => ResourceManager.GetString("FollowEditorPicks", resourceCulture);

	public static string FollowerRequests => ResourceManager.GetString("FollowerRequests", resourceCulture);

	public static string Followers => ResourceManager.GetString("Followers", resourceCulture);

	public static string Following => ResourceManager.GetString("Following", resourceCulture);

	public static string FollowYou => ResourceManager.GetString("FollowYou", resourceCulture);

	public static string ForgetSomething => ResourceManager.GetString("ForgetSomething", resourceCulture);

	public static string FriendRequest => ResourceManager.GetString("FriendRequest", resourceCulture);

	public static string Friends => ResourceManager.GetString("Friends", resourceCulture);

	public static string FriendSuggestions => ResourceManager.GetString("FriendSuggestions", resourceCulture);

	public static string FromContacts => ResourceManager.GetString("FromContacts", resourceCulture);

	public static string FrontCamera => ResourceManager.GetString("FrontCamera", resourceCulture);

	public static string FullName => ResourceManager.GetString("FullName", resourceCulture);

	public static string FullScreenPosts => ResourceManager.GetString("FullScreenPosts", resourceCulture);

	public static string Gender => ResourceManager.GetString("Gender", resourceCulture);

	public static string GenderFemale => ResourceManager.GetString("GenderFemale", resourceCulture);

	public static string GenderMale => ResourceManager.GetString("GenderMale", resourceCulture);

	public static string GenderNotSpecified => ResourceManager.GetString("GenderNotSpecified", resourceCulture);

	public static string General => ResourceManager.GetString("General", resourceCulture);

	public static string Geolocation => ResourceManager.GetString("Geolocation", resourceCulture);

	public static string GoToTheTop => ResourceManager.GetString("GoToTheTop", resourceCulture);

	public static string HideGrid => ResourceManager.GetString("HideGrid", resourceCulture);

	public static string HideSponsoredPosts => ResourceManager.GetString("HideSponsoredPosts", resourceCulture);

	public static string Highlight => ResourceManager.GetString("Highlight", resourceCulture);

	public static string Home => ResourceManager.GetString("Home", resourceCulture);

	public static string IdentifyPeople => ResourceManager.GetString("IdentifyPeople", resourceCulture);

	public static string ILike => ResourceManager.GetString("ILike", resourceCulture);

	public static string ImageSaved => ResourceManager.GetString("ImageSaved", resourceCulture);

	public static string InAppPurchase => ResourceManager.GetString("InAppPurchase", resourceCulture);

	public static string Information => ResourceManager.GetString("Information", resourceCulture);

	public static string Initialization => ResourceManager.GetString("Initialization", resourceCulture);

	public static string InstagramAppBaseline => ResourceManager.GetString("InstagramAppBaseline", resourceCulture);

	public static string Invert => ResourceManager.GetString("Invert", resourceCulture);

	public static string JustAMomentAgo => ResourceManager.GetString("JustAMomentAgo", resourceCulture);

	public static string Kilometer => ResourceManager.GetString("Kilometer", resourceCulture);

	public static string LetsGo => ResourceManager.GetString("LetsGo", resourceCulture);

	public static string LikeOrNot => ResourceManager.GetString("LikeOrNot", resourceCulture);

	public static string Likes => ResourceManager.GetString("Likes", resourceCulture);

	public static string LinkCopied => ResourceManager.GetString("LinkCopied", resourceCulture);

	public static string LiveTile => ResourceManager.GetString("LiveTile", resourceCulture);

	public static string Loading => ResourceManager.GetString("Loading", resourceCulture);

	public static string LoadingComments => ResourceManager.GetString("LoadingComments", resourceCulture);

	public static string LoadingInProgress => ResourceManager.GetString("LoadingInProgress", resourceCulture);

	public static string LoadingNotifications => ResourceManager.GetString("LoadingNotifications", resourceCulture);

	public static string LoadingPlaces => ResourceManager.GetString("LoadingPlaces", resourceCulture);

	public static string LoadingPosts => ResourceManager.GetString("LoadingPosts", resourceCulture);

	public static string LoadingProfile => ResourceManager.GetString("LoadingProfile", resourceCulture);

	public static string LoadingRecords => ResourceManager.GetString("LoadingRecords", resourceCulture);

	public static string LoadingResults => ResourceManager.GetString("LoadingResults", resourceCulture);

	public static string LoadMoreComments => ResourceManager.GetString("LoadMoreComments", resourceCulture);

	public static string Location => ResourceManager.GetString("Location", resourceCulture);

	public static string Login => ResourceManager.GetString("Login", resourceCulture);

	public static string Logout => ResourceManager.GetString("Logout", resourceCulture);

	public static string LostPassword => ResourceManager.GetString("LostPassword", resourceCulture);

	public static string ManyPosts => ResourceManager.GetString("ManyPosts", resourceCulture);

	public static string Map => ResourceManager.GetString("Map", resourceCulture);

	public static string ME => ResourceManager.GetString("ME", resourceCulture);

	public static string Meter => ResourceManager.GetString("Meter", resourceCulture);

	public static string MorePosts => ResourceManager.GetString("MorePosts", resourceCulture);

	public static string MyLikes => ResourceManager.GetString("MyLikes", resourceCulture);

	public static string MyProfile => ResourceManager.GetString("MyProfile", resourceCulture);

	public static string MyStory => ResourceManager.GetString("MyStory", resourceCulture);

	public static string MyTimeline => ResourceManager.GetString("MyTimeline", resourceCulture);

	public static string NbrEncodingInProgress => ResourceManager.GetString("NbrEncodingInProgress", resourceCulture);

	public static string NbrEncodingsInProgress => ResourceManager.GetString("NbrEncodingsInProgress", resourceCulture);

	public static string Nearby => ResourceManager.GetString("Nearby", resourceCulture);

	public static string NeverShowAgain => ResourceManager.GetString("NeverShowAgain", resourceCulture);

	public static string Next => ResourceManager.GetString("Next", resourceCulture);

	public static string NightFrom => ResourceManager.GetString("NightFrom", resourceCulture);

	public static string NightTo => ResourceManager.GetString("NightTo", resourceCulture);

	public static string NoChannel => ResourceManager.GetString("NoChannel", resourceCulture);

	public static string NoInstagramPost => ResourceManager.GetString("NoInstagramPost", resourceCulture);

	public static string NoMessages => ResourceManager.GetString("NoMessages", resourceCulture);

	public static string NoRemoveIt => ResourceManager.GetString("NoRemoveIt", resourceCulture);

	public static string OneComment => ResourceManager.GetString("OneComment", resourceCulture);

	public static string OneLike => ResourceManager.GetString("OneLike", resourceCulture);

	public static string OnePendingRequest => ResourceManager.GetString("OnePendingRequest", resourceCulture);

	public static string OnePost => ResourceManager.GetString("OnePost", resourceCulture);

	public static string OnTheRise => ResourceManager.GetString("OnTheRise", resourceCulture);

	public static string Opacity => ResourceManager.GetString("Opacity", resourceCulture);

	public static string Optional => ResourceManager.GetString("Optional", resourceCulture);

	public static string OtherAppBy => ResourceManager.GetString("OtherAppBy", resourceCulture);

	public static string Others => ResourceManager.GetString("Others", resourceCulture);

	public static string Password => ResourceManager.GetString("Password", resourceCulture);

	public static string Pause => ResourceManager.GetString("Pause", resourceCulture);

	public static string PendingRequests => ResourceManager.GetString("PendingRequests", resourceCulture);

	public static string PersonalPlaces => ResourceManager.GetString("PersonalPlaces", resourceCulture);

	public static string PhoneContacts => ResourceManager.GetString("PhoneContacts", resourceCulture);

	public static string PhoneNumber => ResourceManager.GetString("PhoneNumber", resourceCulture);

	public static string PhoneNumberOptional => ResourceManager.GetString("PhoneNumberOptional", resourceCulture);

	public static string PinToStart => ResourceManager.GetString("PinToStart", resourceCulture);

	public static string Play => ResourceManager.GetString("Play", resourceCulture);

	public static string PolicyPrivacy => ResourceManager.GetString("PolicyPrivacy", resourceCulture);

	public static string Popular => ResourceManager.GetString("Popular", resourceCulture);

	public static string PopularNow => ResourceManager.GetString("PopularNow", resourceCulture);

	public static string PostAreProtected => ResourceManager.GetString("PostAreProtected", resourceCulture);

	public static string PostAreProtectedDescription => ResourceManager.GetString("PostAreProtectedDescription", resourceCulture);

	public static string Posts => ResourceManager.GetString("Posts", resourceCulture);

	public static string Preview => ResourceManager.GetString("Preview", resourceCulture);

	public static string PreviewButton => ResourceManager.GetString("PreviewButton", resourceCulture);

	public static string PreviewSubtitle => ResourceManager.GetString("PreviewSubtitle", resourceCulture);

	public static string PrimaryAccount => ResourceManager.GetString("PrimaryAccount", resourceCulture);

	public static string PrivateMessage => ResourceManager.GetString("PrivateMessage", resourceCulture);

	public static string Profile => ResourceManager.GetString("Profile", resourceCulture);

	public static string Protection => ResourceManager.GetString("Protection", resourceCulture);

	public static string PullToRefresh => ResourceManager.GetString("PullToRefresh", resourceCulture);

	public static string RatingLater => ResourceManager.GetString("RatingLater", resourceCulture);

	public static string RatingMessage => ResourceManager.GetString("RatingMessage", resourceCulture);

	public static string RatingNo => ResourceManager.GetString("RatingNo", resourceCulture);

	public static string RatingTitle => ResourceManager.GetString("RatingTitle", resourceCulture);

	public static string RatingYes => ResourceManager.GetString("RatingYes", resourceCulture);

	public static string ReceiverTo => ResourceManager.GetString("ReceiverTo", resourceCulture);

	public static string Recents => ResourceManager.GetString("Recents", resourceCulture);

	public static string RecentUpdates => ResourceManager.GetString("RecentUpdates", resourceCulture);

	public static string Refresh => ResourceManager.GetString("Refresh", resourceCulture);

	public static string Remove => ResourceManager.GetString("Remove", resourceCulture);

	public static string RemoveAds => ResourceManager.GetString("RemoveAds", resourceCulture);

	public static string RemoveFavorite => ResourceManager.GetString("RemoveFavorite", resourceCulture);

	public static string RemoveLastPart => ResourceManager.GetString("RemoveLastPart", resourceCulture);

	public static string RemoveMyPost => ResourceManager.GetString("RemoveMyPost", resourceCulture);

	public static string RemoveStripes => ResourceManager.GetString("RemoveStripes", resourceCulture);

	public static string RemoveTag => ResourceManager.GetString("RemoveTag", resourceCulture);

	public static string Reply => ResourceManager.GetString("Reply", resourceCulture);

	public static string ReplyAll => ResourceManager.GetString("ReplyAll", resourceCulture);

	public static string ReplyComment => ResourceManager.GetString("ReplyComment", resourceCulture);

	public static string ReplyToSomeone => ResourceManager.GetString("ReplyToSomeone", resourceCulture);

	public static string ReportBugsSuggestions => ResourceManager.GetString("ReportBugsSuggestions", resourceCulture);

	public static string Requests => ResourceManager.GetString("Requests", resourceCulture);

	public static string ResetYourPassword => ResourceManager.GetString("ResetYourPassword", resourceCulture);

	public static string ResourceFlowDirection => ResourceManager.GetString("ResourceFlowDirection", resourceCulture);

	public static string ResourceLanguage => ResourceManager.GetString("ResourceLanguage", resourceCulture);

	public static string Retry => ResourceManager.GetString("Retry", resourceCulture);

	public static string RevinedBy => ResourceManager.GetString("RevinedBy", resourceCulture);

	public static string SaveImageWhenPublish => ResourceManager.GetString("SaveImageWhenPublish", resourceCulture);

	public static string SavePicture => ResourceManager.GetString("SavePicture", resourceCulture);

	public static string SaveVideo => ResourceManager.GetString("SaveVideo", resourceCulture);

	public static string SaveVideoToSkydrive => ResourceManager.GetString("SaveVideoToSkydrive", resourceCulture);

	public static string Score => ResourceManager.GetString("Score", resourceCulture);

	public static string Search => ResourceManager.GetString("Search", resourceCulture);

	public static string SearchAnUser => ResourceManager.GetString("SearchAnUser", resourceCulture);

	public static string SearchWith => ResourceManager.GetString("SearchWith", resourceCulture);

	public static string SelectAFile => ResourceManager.GetString("SelectAFile", resourceCulture);

	public static string SelectAnAccount => ResourceManager.GetString("SelectAnAccount", resourceCulture);

	public static string SelectAPicture => ResourceManager.GetString("SelectAPicture", resourceCulture);

	public static string SelectAsPrimaryAccount => ResourceManager.GetString("SelectAsPrimaryAccount", resourceCulture);

	public static string SelectFriends => ResourceManager.GetString("SelectFriends", resourceCulture);

	public static string SelectSaveDestination => ResourceManager.GetString("SelectSaveDestination", resourceCulture);

	public static string SendComment => ResourceManager.GetString("SendComment", resourceCulture);

	public static string SendDirect => ResourceManager.GetString("SendDirect", resourceCulture);

	public static string Sending => ResourceManager.GetString("Sending", resourceCulture);

	public static string SendingComment => ResourceManager.GetString("SendingComment", resourceCulture);

	public static string SendTo => ResourceManager.GetString("SendTo", resourceCulture);

	public static string SendTweet => ResourceManager.GetString("SendTweet", resourceCulture);

	public static string SendVine => ResourceManager.GetString("SendVine", resourceCulture);

	public static string SensitivePosts => ResourceManager.GetString("SensitivePosts", resourceCulture);

	public static string SensitivePostsDescription => ResourceManager.GetString("SensitivePostsDescription", resourceCulture);

	public static string SetFocus => ResourceManager.GetString("SetFocus", resourceCulture);

	public static string Settings => ResourceManager.GetString("Settings", resourceCulture);

	public static string Share => ResourceManager.GetString("Share", resourceCulture);

	public static string ShareFacebook => ResourceManager.GetString("ShareFacebook", resourceCulture);

	public static string ShareTwitter => ResourceManager.GetString("ShareTwitter", resourceCulture);

	public static string ShowGrid => ResourceManager.GetString("ShowGrid", resourceCulture);

	public static string ShowLastComments => ResourceManager.GetString("ShowLastComments", resourceCulture);

	public static string SignIn => ResourceManager.GetString("SignIn", resourceCulture);

	public static string SignInNow => ResourceManager.GetString("SignInNow", resourceCulture);

	public static string SignInTwitter => ResourceManager.GetString("SignInTwitter", resourceCulture);

	public static string SignUpEmail => ResourceManager.GetString("SignUpEmail", resourceCulture);

	public static string Skip => ResourceManager.GetString("Skip", resourceCulture);

	public static string SnapAddedToYourStory => ResourceManager.GetString("SnapAddedToYourStory", resourceCulture);

	public static string Social => ResourceManager.GetString("Social", resourceCulture);

	public static string SocialShare => ResourceManager.GetString("SocialShare", resourceCulture);

	public static string SpecialThanksTo => ResourceManager.GetString("SpecialThanksTo", resourceCulture);

	public static string SplitHorizontally => ResourceManager.GetString("SplitHorizontally", resourceCulture);

	public static string SplitVertically => ResourceManager.GetString("SplitVertically", resourceCulture);

	public static string Stories => ResourceManager.GetString("Stories", resourceCulture);

	public static string StoryAddTo => ResourceManager.GetString("StoryAddTo", resourceCulture);

	public static string StoryAddToDescription => ResourceManager.GetString("StoryAddToDescription", resourceCulture);

	public static string Suggestions => ResourceManager.GetString("Suggestions", resourceCulture);

	public static string SwitchAccount => ResourceManager.GetString("SwitchAccount", resourceCulture);

	public static string Synchronize => ResourceManager.GetString("Synchronize", resourceCulture);

	public static string SynchronizeWithFacebook => ResourceManager.GetString("SynchronizeWithFacebook", resourceCulture);

	public static string SynchronizeWithTwitter => ResourceManager.GetString("SynchronizeWithTwitter", resourceCulture);

	public static string Tagged => ResourceManager.GetString("Tagged", resourceCulture);

	public static string TagMePhotoOfYou => ResourceManager.GetString("TagMePhotoOfYou", resourceCulture);

	public static string TagMeRemoved => ResourceManager.GetString("TagMeRemoved", resourceCulture);

	public static string TagMeRemoveMeFromPhoto => ResourceManager.GetString("TagMeRemoveMeFromPhoto", resourceCulture);

	public static string TagMeShowOnMyProfile => ResourceManager.GetString("TagMeShowOnMyProfile", resourceCulture);

	public static string Tags => ResourceManager.GetString("Tags", resourceCulture);

	public static string TakeVideo => ResourceManager.GetString("TakeVideo", resourceCulture);

	public static string ThemeBlackAndWhite => ResourceManager.GetString("ThemeBlackAndWhite", resourceCulture);

	public static string ThemeDark => ResourceManager.GetString("ThemeDark", resourceCulture);

	public static string ThemeLight => ResourceManager.GetString("ThemeLight", resourceCulture);

	public static string TimestampDayAbbreviation => ResourceManager.GetString("TimestampDayAbbreviation", resourceCulture);

	public static string TimestampDayAgo => ResourceManager.GetString("TimestampDayAgo", resourceCulture);

	public static string TimestampDaysAgo => ResourceManager.GetString("TimestampDaysAgo", resourceCulture);

	public static string TimestampHourAbbreviation => ResourceManager.GetString("TimestampHourAbbreviation", resourceCulture);

	public static string TimestampHourAgo => ResourceManager.GetString("TimestampHourAgo", resourceCulture);

	public static string TimestampHoursAgo => ResourceManager.GetString("TimestampHoursAgo", resourceCulture);

	public static string TimestampMinuteAbbreviation => ResourceManager.GetString("TimestampMinuteAbbreviation", resourceCulture);

	public static string TimestampMinuteAgo => ResourceManager.GetString("TimestampMinuteAgo", resourceCulture);

	public static string TimestampMinutesAgo => ResourceManager.GetString("TimestampMinutesAgo", resourceCulture);

	public static string TitleAbout => ResourceManager.GetString("TitleAbout", resourceCulture);

	public static string TitleAccountManager => ResourceManager.GetString("TitleAccountManager", resourceCulture);

	public static string TitleActivity => ResourceManager.GetString("TitleActivity", resourceCulture);

	public static string TitleComments => ResourceManager.GetString("TitleComments", resourceCulture);

	public static string TitleConnecting => ResourceManager.GetString("TitleConnecting", resourceCulture);

	public static string TitleEditPost => ResourceManager.GetString("TitleEditPost", resourceCulture);

	public static string TitleEncodingQueue => ResourceManager.GetString("TitleEncodingQueue", resourceCulture);

	public static string TitleExplore => ResourceManager.GetString("TitleExplore", resourceCulture);

	public static string TitleLikes => ResourceManager.GetString("TitleLikes", resourceCulture);

	public static string TitlePlaceSelection => ResourceManager.GetString("TitlePlaceSelection", resourceCulture);

	public static string TitlePostVine => ResourceManager.GetString("TitlePostVine", resourceCulture);

	public static string TitleSearch => ResourceManager.GetString("TitleSearch", resourceCulture);

	public static string TitleSettings => ResourceManager.GetString("TitleSettings", resourceCulture);

	public static string ToastAvatarUpdated => ResourceManager.GetString("ToastAvatarUpdated", resourceCulture);

	public static string ToastBadUserPassword => ResourceManager.GetString("ToastBadUserPassword", resourceCulture);

	public static string ToastCantConnect => ResourceManager.GetString("ToastCantConnect", resourceCulture);

	public static string ToastCantCreateAccount => ResourceManager.GetString("ToastCantCreateAccount", resourceCulture);

	public static string ToastCantDisconnect => ResourceManager.GetString("ToastCantDisconnect", resourceCulture);

	public static string ToastCantDownload => ResourceManager.GetString("ToastCantDownload", resourceCulture);

	public static string ToastCantFindYourPosition => ResourceManager.GetString("ToastCantFindYourPosition", resourceCulture);

	public static string ToastCantFollowThisPerson => ResourceManager.GetString("ToastCantFollowThisPerson", resourceCulture);

	public static string ToastCantPostComment => ResourceManager.GetString("ToastCantPostComment", resourceCulture);

	public static string ToastCantPostFacebook => ResourceManager.GetString("ToastCantPostFacebook", resourceCulture);

	public static string ToastCantPostMessage => ResourceManager.GetString("ToastCantPostMessage", resourceCulture);

	public static string ToastCantPostVideo => ResourceManager.GetString("ToastCantPostVideo", resourceCulture);

	public static string ToastCantResetPassword => ResourceManager.GetString("ToastCantResetPassword", resourceCulture);

	public static string ToastCantSyncFacebook => ResourceManager.GetString("ToastCantSyncFacebook", resourceCulture);

	public static string ToastCantUploadAvatar => ResourceManager.GetString("ToastCantUploadAvatar", resourceCulture);

	public static string ToastCloseBeforeEndEncodingMessage => ResourceManager.GetString("ToastCloseBeforeEndEncodingMessage", resourceCulture);

	public static string ToastCloseBeforeEndEncodingTitle => ResourceManager.GetString("ToastCloseBeforeEndEncodingTitle", resourceCulture);

	public static string ToastCommentPosted => ResourceManager.GetString("ToastCommentPosted", resourceCulture);

	public static string ToastDataUpdated => ResourceManager.GetString("ToastDataUpdated", resourceCulture);

	public static string ToastDeleteVideoMessage => ResourceManager.GetString("ToastDeleteVideoMessage", resourceCulture);

	public static string ToastDeleteVideoTitle => ResourceManager.GetString("ToastDeleteVideoTitle", resourceCulture);

	public static string ToastEmailMandatory => ResourceManager.GetString("ToastEmailMandatory", resourceCulture);

	public static string ToastEmailNotValid => ResourceManager.GetString("ToastEmailNotValid", resourceCulture);

	public static string ToastFailToConnect => ResourceManager.GetString("ToastFailToConnect", resourceCulture);

	public static string ToastFullnameMandatory => ResourceManager.GetString("ToastFullnameMandatory", resourceCulture);

	public static string ToastMessagePostedOnFacebook => ResourceManager.GetString("ToastMessagePostedOnFacebook", resourceCulture);

	public static string ToastMessageRevined => ResourceManager.GetString("ToastMessageRevined", resourceCulture);

	public static string ToastMuteAudioMessage => ResourceManager.GetString("ToastMuteAudioMessage", resourceCulture);

	public static string ToastMuteAudioTitle => ResourceManager.GetString("ToastMuteAudioTitle", resourceCulture);

	public static string ToastPasswordMissing => ResourceManager.GetString("ToastPasswordMissing", resourceCulture);

	public static string ToastPleaseSetEmailFirst => ResourceManager.GetString("ToastPleaseSetEmailFirst", resourceCulture);

	public static string ToastPostEdited => ResourceManager.GetString("ToastPostEdited", resourceCulture);

	public static string ToastRemovePostMessage => ResourceManager.GetString("ToastRemovePostMessage", resourceCulture);

	public static string ToastRemovePostTitle => ResourceManager.GetString("ToastRemovePostTitle", resourceCulture);

	public static string ToastResetPasswordInstructions => ResourceManager.GetString("ToastResetPasswordInstructions", resourceCulture);

	public static string ToastResetPasswordMessage => ResourceManager.GetString("ToastResetPasswordMessage", resourceCulture);

	public static string ToastResetPasswordTitle => ResourceManager.GetString("ToastResetPasswordTitle", resourceCulture);

	public static string ToastSyncTwitterAccountMessage => ResourceManager.GetString("ToastSyncTwitterAccountMessage", resourceCulture);

	public static string ToastSyncTwitterAccountTitle => ResourceManager.GetString("ToastSyncTwitterAccountTitle", resourceCulture);

	public static string ToastTweetPosted => ResourceManager.GetString("ToastTweetPosted", resourceCulture);

	public static string ToastUploadDone => ResourceManager.GetString("ToastUploadDone", resourceCulture);

	public static string ToastVideoLiked => ResourceManager.GetString("ToastVideoLiked", resourceCulture);

	public static string ToastVideoUnliked => ResourceManager.GetString("ToastVideoUnliked", resourceCulture);

	public static string ToastWelcome => ResourceManager.GetString("ToastWelcome", resourceCulture);

	public static string ToastYourAreDisconnected => ResourceManager.GetString("ToastYourAreDisconnected", resourceCulture);

	public static string Trending => ResourceManager.GetString("Trending", resourceCulture);

	public static string TurnOffTorch => ResourceManager.GetString("TurnOffTorch", resourceCulture);

	public static string TurnOnTorch => ResourceManager.GetString("TurnOnTorch", resourceCulture);

	public static string TutoCameraContinueToPress => ResourceManager.GetString("TutoCameraContinueToPress", resourceCulture);

	public static string TutoCameraNice => ResourceManager.GetString("TutoCameraNice", resourceCulture);

	public static string TutoCameraPressToCapture => ResourceManager.GetString("TutoCameraPressToCapture", resourceCulture);

	public static string TutoCameraReleaseToStop => ResourceManager.GetString("TutoCameraReleaseToStop", resourceCulture);

	public static string TutoCameraTooSmall => ResourceManager.GetString("TutoCameraTooSmall", resourceCulture);

	public static string TutoMessage1 => ResourceManager.GetString("TutoMessage1", resourceCulture);

	public static string TutoMessage2 => ResourceManager.GetString("TutoMessage2", resourceCulture);

	public static string Tutorial => ResourceManager.GetString("Tutorial", resourceCulture);

	public static string TwitterContacts => ResourceManager.GetString("TwitterContacts", resourceCulture);

	public static string UnBlockThisPerson => ResourceManager.GetString("UnBlockThisPerson", resourceCulture);

	public static string UploadLimitation => ResourceManager.GetString("UploadLimitation", resourceCulture);

	public static string UploadLimitBatterySaver => ResourceManager.GetString("UploadLimitBatterySaver", resourceCulture);

	public static string UploadToSkydrive => ResourceManager.GetString("UploadToSkydrive", resourceCulture);

	public static string UseCustomToastSound => ResourceManager.GetString("UseCustomToastSound", resourceCulture);

	public static string UseFilePosition => ResourceManager.GetString("UseFilePosition", resourceCulture);

	public static string UseMyAccentColourForLiveTile => ResourceManager.GetString("UseMyAccentColourForLiveTile", resourceCulture);

	public static string UseMyPosition => ResourceManager.GetString("UseMyPosition", resourceCulture);

	public static string UsePositionFromImageFile => ResourceManager.GetString("UsePositionFromImageFile", resourceCulture);

	public static string UseProgressBar => ResourceManager.GetString("UseProgressBar", resourceCulture);

	public static string UserIsNowBlocked => ResourceManager.GetString("UserIsNowBlocked", resourceCulture);

	public static string UserIsNowUnblocked => ResourceManager.GetString("UserIsNowUnblocked", resourceCulture);

	public static string Username => ResourceManager.GetString("Username", resourceCulture);

	public static string UsernameOrEmail => ResourceManager.GetString("UsernameOrEmail", resourceCulture);

	public static string Users => ResourceManager.GetString("Users", resourceCulture);

	public static string Validate => ResourceManager.GetString("Validate", resourceCulture);

	public static string VideoSaved => ResourceManager.GetString("VideoSaved", resourceCulture);

	public static string VideoSavedLocally => ResourceManager.GetString("VideoSavedLocally", resourceCulture);

	public static string VideoTooShort => ResourceManager.GetString("VideoTooShort", resourceCulture);

	public static string View => ResourceManager.GetString("View", resourceCulture);

	public static string VineUploadBegin => ResourceManager.GetString("VineUploadBegin", resourceCulture);

	public static string VineUploadCompleted => ResourceManager.GetString("VineUploadCompleted", resourceCulture);

	public static string VineUploadError => ResourceManager.GetString("VineUploadError", resourceCulture);

	public static string VineUploadGetImage => ResourceManager.GetString("VineUploadGetImage", resourceCulture);

	public static string VineUploadGetVideo => ResourceManager.GetString("VineUploadGetVideo", resourceCulture);

	public static string VineUploadReadyToPublish => ResourceManager.GetString("VineUploadReadyToPublish", resourceCulture);

	public static string VineUploadUploadTo6SecServer => ResourceManager.GetString("VineUploadUploadTo6SecServer", resourceCulture);

	public static string VineUploadUploadVineImage => ResourceManager.GetString("VineUploadUploadVineImage", resourceCulture);

	public static string VineUploadUploadVineVideo => ResourceManager.GetString("VineUploadUploadVineVideo", resourceCulture);

	public static string VineUploadWaitingEncodingResult => ResourceManager.GetString("VineUploadWaitingEncodingResult", resourceCulture);

	public static string WaitingAuthorization => ResourceManager.GetString("WaitingAuthorization", resourceCulture);

	public static string Website => ResourceManager.GetString("Website", resourceCulture);

	public static string WhoAddedMe => ResourceManager.GetString("WhoAddedMe", resourceCulture);

	public static string WhoIsIt => ResourceManager.GetString("WhoIsIt", resourceCulture);

	public static string Yes => ResourceManager.GetString("Yes", resourceCulture);

	public static string YouHaveBeenLoggedOut => ResourceManager.GetString("YouHaveBeenLoggedOut", resourceCulture);

	public static string YourComment => ResourceManager.GetString("YourComment", resourceCulture);

	public static string YourContent => ResourceManager.GetString("YourContent", resourceCulture);

	public static string YourLikes => ResourceManager.GetString("YourLikes", resourceCulture);

	internal AppResources()
	{
	}
}
