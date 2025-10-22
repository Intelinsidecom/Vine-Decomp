using System;
using System.Collections.Generic;
using System.Globalization;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Gen.Services;
using Microsoft.Phone.Controls;
using Vine.Datas;
using Vine.Pages.FullPicture;
using Vine.Pages.Likes;
using Vine.Services;
using Vine.Services.Models;

namespace Vine;

public static class NavigationServiceExt
{
	private static PhoneApplicationFrame nav;

	private static NumberFormatInfo nfi;

	static NavigationServiceExt()
	{
		//IL_000a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0014: Expected O, but got Unknown
		nav = (PhoneApplicationFrame)Application.Current.RootVisual;
		nfi = new NumberFormatInfo
		{
			NumberDecimalSeparator = "."
		};
	}

	public static void Navigate(Uri uri, bool removebackentry = false)
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Expected O, but got Unknown
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		if (nav == null)
		{
			nav = (PhoneApplicationFrame)Application.Current.RootVisual;
		}
		if (nav != null)
		{
			if (removebackentry)
			{
				((Frame)nav).Navigated += new NavigatedEventHandler(RemoveBackEntry_Navigated);
			}
			((Frame)nav).Navigate(uri);
		}
	}

	private static void RemoveBackEntry_Navigated(object sender, NavigationEventArgs e)
	{
		//IL_000c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0016: Expected O, but got Unknown
		((Frame)nav).Navigated -= new NavigatedEventHandler(RemoveBackEntry_Navigated);
		while (nav.RemoveBackEntry() != null)
		{
		}
	}

	public static void ToTimeline(string type = null, bool removebackentry = false, string nextpage = null, string extra = null)
	{
		Navigate(UriTimeline(type, removebackentry: false, nextpage, extra), removebackentry);
	}

	public static void ToSkydrive(string uri)
	{
		Navigate(new Uri("/Pages/Skydrive/SkydrivePage.xaml?uri=" + HttpUtility.UrlEncode(uri), UriKind.Relative));
	}

	public static void ToUniquePost(string id, bool autoscroll = true)
	{
	}

	public static void ToCollage(bool forceddirect)
	{
		Navigate(new Uri("/Pages/Collage/CollagePage.xaml?forceddirect=" + (forceddirect ? "true" : "false"), UriKind.Relative));
	}

	public static void ToSearch(bool selectFriends = false)
	{
		Navigate(new Uri("/Pages/Search/SearchPage.xaml?" + (selectFriends ? "selectFriends=true" : ""), UriKind.Relative));
	}

	public static void ToSettings()
	{
		Navigate(new Uri("/Pages/Settings/SettingsPage.xaml", UriKind.Relative));
	}

	internal static void ToLogin(bool removebackentry, string nextUri = null, bool directshowresetpassword = false, string forcedEmail = null)
	{
		Navigate(UriLogin(removebackentry: false, nextUri, directshowresetpassword, forcedEmail), removebackentry);
	}

	internal static void ToExplore()
	{
		Navigate(new Uri("/Pages/Explore/ExplorePage.xaml", UriKind.Relative));
	}

	internal static void ToPostVideo(string jobid = null, bool forceddirect = false, string videoToPost = null, string imageToPost = null)
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Expected O, but got Unknown
		if (nav == null)
		{
			nav = (PhoneApplicationFrame)Application.Current.RootVisual;
		}
		((Frame)nav).Navigate(new Uri("/Pages/PostVideo/PostVideoPage.xaml?s=s" + ((jobid != null) ? ("&id=" + jobid) : "") + ((videoToPost != null) ? ("&videotoupload=" + WebUtility.UrlEncode(videoToPost)) : "") + ((imageToPost != null) ? ("&imagetoupload=" + WebUtility.UrlEncode(imageToPost)) : "") + "&forceddirect=" + (forceddirect ? "true" : "false"), UriKind.Relative));
	}

	internal static void ToPostImage(string message = null, double? latitude = null, double? longitude = null, bool regram = false, bool forcedirect = false)
	{
		Navigate(GetUriForPictureUpload(message, latitude, longitude, regram, forcedirect));
	}

	internal static void ToCamera(List<IPerson> contactsForDirect = null)
	{
		Navigate(new Uri("/Pages/Camera/CameraPage.xaml?forceddirect=" + ((contactsForDirect != null) ? "true" : "false"), UriKind.Relative));
	}

	internal static void ToPreviewVideo(bool forceddirect, string imagepath, string videopath = null)
	{
		Navigate(new Uri("/Pages/PreviewVideo/PreviewVideoPage.xaml?forceddirect=" + (forceddirect ? "true" : "false") + ((videopath != null) ? ("&path=" + WebUtility.UrlEncode(videopath)) : "") + ((imagepath != null) ? ("&imagepath=" + WebUtility.UrlEncode(imagepath)) : ""), UriKind.Relative));
	}

	internal static void ToTutorial(bool removebackentry = false)
	{
		Navigate(new Uri("/Pages/Tutorial/TutorialPage.xaml", UriKind.Relative), removebackentry);
	}

	internal static void ToTutorialCamera(bool removebackentry = false)
	{
		Navigate(new Uri("/Pages/Camera/CameraPage.xaml?tutorial=true&", UriKind.Relative), removebackentry);
	}

	internal static void ToLikes(IPostRecord record, IListPersons likes)
	{
		LikesPage.TmpLikes = record.GetLikes();
		Navigate(new Uri("/Pages/Likes/LikesPage.xaml?type=likes&id=" + HttpUtility.UrlEncode(record.PostId), UriKind.Relative));
	}

	internal static void ToLikeOrNot()
	{
		Navigate(new Uri("/Pages/LikeOrNot/LikeOrNotPage.xaml", UriKind.Relative));
	}

	internal static void Followers(string id, int? number = null)
	{
		Navigate(new Uri("/Pages/Likes/LikesPage.xaml?type=followers&id=" + HttpUtility.UrlEncode(id) + (number.HasValue ? ("&number=" + number.Value) : ""), UriKind.Relative));
	}

	internal static void ToSelectDirectFriends()
	{
		Navigate(new Uri("/Pages/SelectDirectFriends/SelectDirectFriendsPage.xaml", UriKind.Relative));
	}

	internal static void ToManageAccount(bool removebackentry = false)
	{
		Navigate(new Uri("/Pages/ManageAccount/ManageAccountPage.xaml", UriKind.Relative), removebackentry);
	}

	internal static Uri GetUriPhotoApplyFilter(string message = null, bool removebackentry = false, double? latitude = null, double? longitude = null, bool fromcollage = false, bool forceddirect = false)
	{
		return new Uri("/Pages/PhotoApplyFilter/PhotoApplyFilterPage.xaml?s=s" + (removebackentry ? "&removebackentry=true" : "") + ((message != null) ? ("&message=" + HttpUtility.UrlEncode(message)) : "") + (fromcollage ? "&fromcollage=true" : "") + "&forceddirect=" + (forceddirect ? "true" : "false") + ((latitude.HasValue && longitude.HasValue) ? ("&lat=" + latitude.Value.ToString(nfi) + "&lng=" + longitude.Value.ToString(nfi)) : ""), UriKind.Relative);
	}

	internal static void ToFollowing(string id, int? number = null)
	{
		Navigate(new Uri("/Pages/Likes/LikesPage.xaml?type=following&id=" + HttpUtility.UrlEncode(id) + (number.HasValue ? ("&number=" + number.Value) : ""), UriKind.Relative));
	}

	internal static Uri UriDirect(string extra = "")
	{
		return new Uri("/Pages/Direct/DirectPage.xaml?" + extra, UriKind.Relative);
	}

	internal static void ToDirect()
	{
		Navigate(UriDirect());
	}

	internal static void ToComments(IPostRecord post, bool postComment = false, bool autoscroll = true)
	{
		if (post != null)
		{
			FullPicturePage.SelectedPost = post;
			string text = "post";
			Navigate(new Uri("/Pages/FullPicture/FullPicturePage.xaml?autoscroll=" + autoscroll + "&id=" + HttpUtility.UrlEncode(post.PostId) + (postComment ? "&postComment=true" : "") + "&type=" + text, UriKind.Relative));
		}
	}

	internal static void ToProfileFromUsername(string username)
	{
		Navigate(UriProfileFromName(username));
	}

	internal static void ToProfile(string id, bool removebackentry = false)
	{
		Navigate(UriProfile(id), removebackentry);
	}

	internal static void ToOtherApp()
	{
		Navigate(new Uri("/Pages/OtherApps/OtherAppPage.xaml", UriKind.Relative));
	}

	internal static void ToTwitter(string postid, string tweet, string image)
	{
		Navigate(new Uri("/Pages/TwitterAuth/TwitterAuthPage.xaml?type=post&postid=" + HttpUtility.UrlEncode(postid) + "&tweet=" + HttpUtility.UrlEncode(tweet) + "&img=" + HttpUtility.UrlEncode(image), UriKind.Relative));
	}

	internal static void ToTumblr()
	{
		Navigate(new Uri("/Pages/Tumblr/TumblrPage.xaml", UriKind.Relative));
	}

	internal static void ToTagFeed(string tag)
	{
		ToTimeline(UriServiceProvider.GetUriForTag(tag));
	}

	internal static void ToVK()
	{
		Navigate(new Uri("/Pages/VK/VKPage.xaml", UriKind.Relative));
	}

	internal static void ToFoursquare()
	{
		Navigate(new Uri("/Pages/FoursquareAuth/FoursquareAuthPage.xaml", UriKind.Relative));
	}

	internal static void ToFlickr()
	{
		Navigate(new Uri("/Pages/FlickrAuth/FlickrAuthPage.xaml", UriKind.Relative));
	}

	internal static void ToShareFacebook(string postid)
	{
		Navigate(new Uri("/Pages/Facebook/FacebookAuthPage.xaml?type=post&postid=" + HttpUtility.UrlEncode(postid), UriKind.Relative));
	}

	internal static void ToSyncTwitter()
	{
		Navigate(new Uri("/Pages/TwitterAuth/TwitterAuthPage.xaml?type=syncaccount", UriKind.Relative));
	}

	internal static Uri UriNotification(string extra = "")
	{
		return new Uri("/Pages/Notifications/NotificationsPage.xaml?" + extra, UriKind.Relative);
	}

	internal static void ToNotification(string extra = "")
	{
		Navigate(UriNotification(extra));
	}

	internal static void ToSuggestionContact()
	{
		Navigate(new Uri("/Pages/Likes/LikesPage.xaml?type=searchcontacts", UriKind.Relative));
	}

	internal static void ToSuggestionTwitter()
	{
		Navigate(new Uri("/Pages/Likes/LikesPage.xaml?type=searchtwitter", UriKind.Relative));
	}

	internal static void ToRotatePicture()
	{
		Navigate(new Uri("/Pages/RotatePicture/RotatePicturePage.xaml", UriKind.Relative));
	}

	internal static void ToPost(string id)
	{
		Navigate(UriPost(id));
	}

	internal static void ToPost(IPostRecord post, string type)
	{
		MainPage.ForcedPosts = new List<IPostRecord> { post };
		Navigate(UriTimeline(type));
	}

	internal static void ToTranscoding(bool frompost = false)
	{
		Navigate(new Uri("/Pages/Transcoding/TranscodingPage.xaml?" + (frompost ? "fromPost=true" : ""), UriKind.Relative));
	}

	internal static void ToPlaceSelection(string id, double? latitude = null, double? longitude = null)
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Expected O, but got Unknown
		if (nav == null)
		{
			nav = (PhoneApplicationFrame)Application.Current.RootVisual;
		}
		((Frame)nav).Navigate(new Uri("/Pages/PlaceSelection/PlaceSelectionPage.xaml?id=" + id + ((latitude.HasValue && longitude.HasValue) ? ("&lat=" + latitude.Value.ToString(nfi) + "&lng=" + longitude.Value.ToString(nfi)) : ""), UriKind.Relative));
	}

	internal static Uri UriTimeline(string type = null, bool removebackentry = false, string nextpage = null, string extra = null)
	{
		string text = "/Pages/Main/MainPage.xaml";
		return new Uri(text + "?&s=s" + ((type == null) ? "" : ("&type=" + type)) + (removebackentry ? "&removebackentry=true" : "") + ((nextpage != null) ? ("&nextpage=" + HttpUtility.UrlEncode(nextpage)) : "") + extra, UriKind.Relative);
	}

	internal static Uri UriPost(string id, bool removebackentry = false)
	{
		return UriTimeline(UriServiceProvider.GetUriForOnePicture(id), removebackentry);
	}

	internal static Uri UriProfile(string id, bool removebackentry = false)
	{
		return new Uri("/Pages/Profile/ProfilePage.xaml?id=" + HttpUtility.UrlEncode(id) + (removebackentry ? "&removebackentry=true" : ""), UriKind.Relative);
	}

	internal static Uri UriProfileFromName(string username, bool removebackentry = false)
	{
		return UriProfile(username, removebackentry);
	}

	internal static Uri UriLogin(bool removebackentry = false, string newuri = null, bool directshowresetpassword = false, string forcedEmail = null, string forcedTwitter = null)
	{
		return new Uri("/Pages/CreateAccount/CreateAccountPage.xaml?s=s" + (removebackentry ? "&removebackentry=true" : "") + ((newuri != null) ? ("&nexturi=" + HttpUtility.UrlEncode(newuri)) : "") + (directshowresetpassword ? "&directshowresetpassword=true" : "") + ((!string.IsNullOrEmpty(forcedEmail)) ? ("&forcedemail=" + WebUtility.UrlEncode(forcedEmail)) : "") + ((!string.IsNullOrEmpty(forcedTwitter)) ? ("&forcedtwitter=" + WebUtility.UrlEncode(forcedTwitter)) : ""), UriKind.Relative);
	}

	internal static void ToAbout()
	{
		Navigate(new Uri("/Pages/About/AboutPage.xaml", UriKind.Relative));
	}

	internal static void ToSyncFacebook()
	{
		Navigate(new Uri("/Pages/Facebook/FacebookAuthPage.xaml?type=sync", UriKind.Relative));
	}

	internal static void ToFindContactFacebook(string subtype = null)
	{
		Navigate(new Uri("/Pages/Facebook/FacebookAuthPage.xaml?type=contacts" + ((subtype != null) ? ("&subtype=" + subtype) : ""), UriKind.Relative));
	}

	internal static void ToChooseChannel(string encodingid)
	{
		Navigate(new Uri("/Pages/ChooseChannel/ChooseChannelPage.xaml?id=" + encodingid, UriKind.Relative));
	}

	internal static void ToCheckpoint(string uri)
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Expected O, but got Unknown
		if (nav == null)
		{
			nav = (PhoneApplicationFrame)Application.Current.RootVisual;
		}
		((Frame)nav).Navigate(new Uri("/Pages/Checkpoint/CheckpointPage.xaml?uri=" + HttpUtility.UrlEncode(uri), UriKind.Relative));
	}

	internal static void ToFollowing()
	{
		ToNotification("mode=following");
	}

	internal static void ToListContactsFacebook(bool removebackentry = false, string subtype = null)
	{
		Navigate(new Uri("/Pages/Likes/LikesPage.xaml?type=facebookcontacts" + ((subtype != null) ? ("&subtype=" + subtype) : ""), UriKind.Relative), removebackentry);
	}

	internal static void ToFriendsSuggestion(string type)
	{
		Navigate(new Uri("/Pages/Suggestions/SuggestionsPage.xaml?" + ((type != null) ? ("type=" + type) : ""), UriKind.Relative));
	}

	internal static void ToSearchFriendsFacebook()
	{
		Navigate(new Uri("/Pages/SearchFriendsFacebook/SearchFriendsFacebookPage.xaml", UriKind.Relative));
	}

	internal static Uri GetUriForPictureUpload(string message = null, double? latitude = null, double? longitude = null, bool regram = false, bool forceddirect = false)
	{
		return new Uri("/Pages/PostImage/PostImagePage.xaml?" + (regram ? "regram=true" : "regram=false") + ((message != null) ? ("&message=" + HttpUtility.UrlEncode(message)) : "") + "&forceddirect=" + (forceddirect ? "true" : "false") + ((latitude.HasValue && longitude.HasValue) ? ("&lat=" + latitude.Value.ToString(nfi) + "&lng=" + longitude.Value.ToString(nfi)) : ""), UriKind.Relative);
	}

	internal static void ToNFC(IPostRecord post)
	{
		Navigate(new Uri("/Pages/NFC/NFCPage.xaml?uri=" + HttpUtility.UrlEncode("http://www2.huynapps.com/wp/6sec/nfc.php?id=" + HttpUtility.UrlEncode(post.PostId) + "&ui=" + HttpUtility.UrlEncode(post.UserId) + "&uri=" + HttpUtility.UrlEncode(post.ShareUrl)), UriKind.Relative));
	}

	internal static void ToCrop(string param)
	{
		Navigate(new Uri("/Pages/CropImage/CropPage.xaml?" + param, UriKind.Relative));
	}

	internal static void ToCropVideo(string param)
	{
		Navigate(new Uri("/Pages/CropVideo/CropVideoPage.xaml?" + param, UriKind.Relative));
	}

	internal static void ToDirectPendingRequest()
	{
		Navigate(new Uri("/Pages/DirectPendingRequest/DirectPendingRequestPage.xaml", UriKind.Relative));
	}

	internal static void ToChat(Conversation chat)
	{
		DatasProvider.Instance.CurrentUser.CurrentConversation = chat;
		Navigate(new Uri("/Pages/Chat/ChatPage.xaml", UriKind.Relative));
	}

	internal static void ToFollowRequests()
	{
		Navigate(new Uri("/Pages/FollowRequests/FollowRequestsPage.xaml", UriKind.Relative));
	}
}
