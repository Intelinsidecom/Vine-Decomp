using System.Collections.Generic;
using System.Runtime.Serialization;
using Vine.Common;
using Vine.Framework;
using Vine.Views;
using Vine.Web;
using Windows.UI.Text;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Documents;
using Windows.UI.Xaml.Media;

namespace Vine.Models;

public class VineUserModel : NotifyObject
{
	private string _section;

	private VineUserModel _user;

	private string _profileBackground;

	private string _phoneNumber = string.Empty;

	private string _email;

	private string _username;

	private string _description;

	private string _avatarUrl = Constants.DefaultAvatarUrl;

	private string _location;

	private long _followingCount;

	private long _followerCount;

	private long _postCount;

	private long _likeCount;

	private long _loopCount;

	private bool _blocked;

	private bool _following;

	private bool _verified;

	private bool _followRequested;

	private bool _private;

	private bool _twitterConnected;

	private bool _verifiedPhoneNumber;

	private bool _verifiedEmail;

	private bool _facebookConnected;

	private bool _explicitContent;

	private bool _hiddenEmail;

	private bool _hiddenPhoneNumber;

	private bool _hiddenTwitter;

	private bool _followApprovalPending;

	private string _byLine;

	private RelayCommand _followCommand;

	public string Section
	{
		get
		{
			return _section;
		}
		set
		{
			SetProperty(ref _section, value, "Section");
		}
	}

	public VineUserModel User
	{
		get
		{
			return _user;
		}
		set
		{
			SetProperty(ref _user, value, "User");
		}
	}

	public bool ExternalUser { get; set; }

	public string ProfileBackground
	{
		get
		{
			return _profileBackground;
		}
		set
		{
			_profileBackground = value;
			NotifyOfPropertyChange(() => ProfileBgBrush);
			NotifyOfPropertyChange(() => ProfileBgBannerBrush);
		}
	}

	public Brush ProfileBgBannerBrush
	{
		get
		{
			//IL_0032: Unknown result type (might be due to invalid IL or missing references)
			//IL_0038: Expected O, but got Unknown
			//IL_0021: Unknown result type (might be due to invalid IL or missing references)
			//IL_0027: Expected O, but got Unknown
			if (!string.IsNullOrEmpty(ProfileBackground))
			{
				return (Brush)new SolidColorBrush(Extensions.ColorHex(ProfileBackground));
			}
			return (Brush)Application.Current.Resources.DarkResource("VineBackgroundBrush");
		}
	}

	public SolidColorBrush ProfileBgBrush
	{
		get
		{
			//IL_0033: Unknown result type (might be due to invalid IL or missing references)
			//IL_0039: Expected O, but got Unknown
			//IL_0044: Unknown result type (might be due to invalid IL or missing references)
			//IL_004a: Expected O, but got Unknown
			if (!string.IsNullOrEmpty(ProfileBackground) && !(ProfileBackground == "0x333333"))
			{
				return new SolidColorBrush(Extensions.ColorHex(ProfileBackground));
			}
			return (SolidColorBrush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];
		}
	}

	public Brush ProfileBgLightBrush
	{
		get
		{
			//IL_000b: Unknown result type (might be due to invalid IL or missing references)
			//IL_0010: Unknown result type (might be due to invalid IL or missing references)
			//IL_0020: Expected O, but got Unknown
			SolidColorBrush val = new SolidColorBrush(ProfileBgBrush.Color);
			((Brush)val).put_Opacity(0.2);
			return (Brush)val;
		}
	}

	public string PhoneNumber
	{
		get
		{
			return _phoneNumber;
		}
		set
		{
			SetProperty(ref _phoneNumber, value, "PhoneNumber");
		}
	}

	public string Email
	{
		get
		{
			return _email;
		}
		set
		{
			SetProperty(ref _email, value, "Email");
		}
	}

	public string Username
	{
		get
		{
			return _username;
		}
		set
		{
			SetProperty(ref _username, value, "Username");
		}
	}

	public string Description
	{
		get
		{
			return _description;
		}
		set
		{
			SetProperty(ref _description, value, "Description");
		}
	}

	public string AvatarUrl
	{
		get
		{
			return _avatarUrl;
		}
		set
		{
			SetProperty(ref _avatarUrl, value, "AvatarUrl");
		}
	}

	public string Location
	{
		get
		{
			return _location;
		}
		set
		{
			SetProperty(ref _location, value, "Location");
		}
	}

	public string UserId { get; set; }

	public long FollowingCount
	{
		get
		{
			return _followingCount;
		}
		set
		{
			SetProperty(ref _followingCount, value, "FollowingCount");
		}
	}

	public long FollowerCount
	{
		get
		{
			return _followerCount;
		}
		set
		{
			SetProperty(ref _followerCount, value, "FollowerCount");
		}
	}

	public long PostCount
	{
		get
		{
			return _postCount;
		}
		set
		{
			SetProperty(ref _postCount, value, "PostCount");
			NotifyOfPropertyChange(() => RichPostCount);
		}
	}

	public long LikeCount
	{
		get
		{
			return _likeCount;
		}
		set
		{
			SetProperty(ref _likeCount, value, "LikeCount");
		}
	}

	public long LoopCount
	{
		get
		{
			return _loopCount;
		}
		set
		{
			SetProperty(ref _loopCount, value, "LoopCount");
		}
	}

	public bool Blocked
	{
		get
		{
			return _blocked;
		}
		set
		{
			SetProperty(ref _blocked, value, "Blocked");
		}
	}

	public bool Following
	{
		get
		{
			if (_following)
			{
				return !FollowRequested;
			}
			return false;
		}
		set
		{
			SetProperty(ref _following, value, "Following");
			NotifyOfPropertyChange(() => FollowingEnabled);
			NotifyOfPropertyChange(() => FollowButtonState);
		}
	}

	public bool Verified
	{
		get
		{
			return _verified;
		}
		set
		{
			SetProperty(ref _verified, value, "Verified");
		}
	}

	public bool FollowRequested
	{
		get
		{
			return _followRequested;
		}
		set
		{
			SetProperty(ref _followRequested, value, "FollowRequested");
			NotifyOfPropertyChange(() => FollowingEnabled);
			NotifyOfPropertyChange(() => FollowButtonState);
		}
	}

	public bool Private
	{
		get
		{
			return _private;
		}
		set
		{
			SetProperty(ref _private, value, "Private");
			NotifyOfPropertyChange(() => ProtectedButtonState);
		}
	}

	public string TwitterScreenname { get; set; }

	public string TwitterDisplayScreenname
	{
		get
		{
			if (string.IsNullOrWhiteSpace(TwitterScreenname) || HiddenTwitter)
			{
				return null;
			}
			return "@" + TwitterScreenname;
		}
	}

	public bool TwitterConnected
	{
		get
		{
			return _twitterConnected;
		}
		set
		{
			SetProperty(ref _twitterConnected, value, "TwitterConnected");
		}
	}

	public bool VerifiedPhoneNumber
	{
		get
		{
			return _verifiedPhoneNumber;
		}
		set
		{
			SetProperty(ref _verifiedPhoneNumber, value, "VerifiedPhoneNumber");
		}
	}

	public bool VerifiedEmail
	{
		get
		{
			return _verifiedEmail;
		}
		set
		{
			SetProperty(ref _verifiedEmail, value, "VerifiedEmail");
		}
	}

	public bool FacebookConnected
	{
		get
		{
			return _facebookConnected;
		}
		set
		{
			SetProperty(ref _facebookConnected, value, "FacebookConnected");
		}
	}

	public bool ExplicitContent
	{
		get
		{
			return _explicitContent;
		}
		set
		{
			SetProperty(ref _explicitContent, value, "ExplicitContent");
			NotifyOfPropertyChange(() => ExplicitContentButtonState);
		}
	}

	public bool HiddenEmail
	{
		get
		{
			return _hiddenEmail;
		}
		set
		{
			SetProperty(ref _hiddenEmail, value, "HiddenEmail");
			NotifyOfPropertyChange(() => HiddenEmailButtonState);
		}
	}

	public VineToggleButtonState HiddenEmailButtonState
	{
		get
		{
			if (HiddenEmail)
			{
				return VineToggleButtonState.Off;
			}
			return VineToggleButtonState.On;
		}
	}

	public bool HiddenPhoneNumber
	{
		get
		{
			return _hiddenPhoneNumber;
		}
		set
		{
			SetProperty(ref _hiddenPhoneNumber, value, "HiddenPhoneNumber");
			NotifyOfPropertyChange(() => HiddenPhoneNumberButtonState);
		}
	}

	public VineToggleButtonState HiddenPhoneNumberButtonState
	{
		get
		{
			if (HiddenPhoneNumber)
			{
				return VineToggleButtonState.Off;
			}
			return VineToggleButtonState.On;
		}
	}

	public bool HiddenTwitter
	{
		get
		{
			return _hiddenTwitter;
		}
		set
		{
			SetProperty(ref _hiddenTwitter, value, "HiddenTwitter");
			NotifyOfPropertyChange(() => HiddenTwitterButtonState);
		}
	}

	public VineToggleButtonState HiddenTwitterButtonState
	{
		get
		{
			if (HiddenTwitter)
			{
				return VineToggleButtonState.Off;
			}
			return VineToggleButtonState.On;
		}
	}

	public bool FollowApprovalPending
	{
		get
		{
			return _followApprovalPending;
		}
		set
		{
			SetProperty(ref _followApprovalPending, value, "FollowApprovalPending");
			OnPropertyChanged("FollowApprovalPending");
		}
	}

	public string ByLine
	{
		get
		{
			return _byLine;
		}
		set
		{
			SetProperty(ref _byLine, value, "ByLine");
			OnPropertyChanged("ByLine");
		}
	}

	public bool FollowingEnabled => !FollowRequested;

	public VineToggleButtonState FollowButtonState
	{
		get
		{
			if (FollowRequested)
			{
				return VineToggleButtonState.FollowRequested;
			}
			return Following ? VineToggleButtonState.Following : VineToggleButtonState.NotFollowing;
		}
	}

	public VineToggleButtonState ExplicitContentButtonState
	{
		get
		{
			if (!ExplicitContent)
			{
				return VineToggleButtonState.Off;
			}
			return VineToggleButtonState.On;
		}
	}

	public VineToggleButtonState ProtectedButtonState
	{
		get
		{
			if (!Private)
			{
				return VineToggleButtonState.Off;
			}
			return VineToggleButtonState.On;
		}
	}

	public bool IsCurrentUser => ApplicationSettings.Current.UserId == UserId;

	public bool AreVinesViewable
	{
		get
		{
			if (!Following && Private)
			{
				return IsCurrentUser;
			}
			return true;
		}
	}

	public RelayCommand FollowCommand
	{
		get
		{
			if (_followCommand == null)
			{
				_followCommand = new RelayCommand(FollowToggle, CanFollow);
			}
			return _followCommand;
		}
	}

	public VineUserType UserType { get; set; }

	[IgnoreDataMember]
	public List<Run> RichFollowers => EntityHelper.GetFormattedStringRuns((FollowerCount == 1) ? ResourceHelper.GetString("profile_followers_one") : ResourceHelper.GetString("profile_followers_other"), FollowerCount.ToString(), FontWeights.Bold, null, typeof(long));

	[IgnoreDataMember]
	public List<Run> RichFollowing => EntityHelper.GetFormattedStringRuns((FollowingCount == 1) ? ResourceHelper.GetString("profile_following_one") : ResourceHelper.GetString("profile_following_other"), FollowingCount.ToString(), FontWeights.Bold, null, typeof(long));

	[IgnoreDataMember]
	public List<Run> RichLoops => EntityHelper.GetFormattedStringRuns((LoopCount == 1) ? ResourceHelper.GetString("profile_loops_one") : ResourceHelper.GetString("profile_loops_other"), LoopCount.ToCommaSeperated() + " ", FontWeights.Bold, null, typeof(string));

	[IgnoreDataMember]
	public string LoopCountShortened => string.Format((LoopCount == 1) ? ResourceHelper.GetString("profile_loops_one") : ResourceHelper.GetString("profile_loops_other"), new object[1] { LoopCount.ToVineCount() });

	[IgnoreDataMember]
	public List<Run> RichPostCount => EntityHelper.GetFormattedStringRuns((PostCount == 1) ? ResourceHelper.GetString("profile_posts_one") : ResourceHelper.GetString("profile_posts_other"), PostCount.ToString(), FontWeights.Bold, null, typeof(long));

	[IgnoreDataMember]
	public List<Run> RichLikesCount => EntityHelper.GetFormattedStringRuns((LikeCount == 1) ? ResourceHelper.GetString("profile_likes_one") : ResourceHelper.GetString("profile_likes_other"), LikeCount.ToString(), FontWeights.Bold, null, typeof(long));

	public async void FollowToggle()
	{
		if (Following)
		{
			Following = false;
			if ((await App.Api.UnfollowUser(UserId)).HasError)
			{
				Following = true;
			}
		}
		else if (Private)
		{
			bool oldValue = FollowRequested;
			FollowRequested = Private;
			if ((await App.Api.FollowUser(UserId)).HasError)
			{
				FollowRequested = oldValue;
			}
		}
		else if (!Following)
		{
			Following = true;
			if ((await App.Api.FollowUser(UserId)).HasError)
			{
				Following = false;
			}
		}
	}

	public virtual bool CanFollow()
	{
		return FollowingEnabled;
	}
}
