using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Events;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Views.Settings;
using Vine.Web;
using Windows.ApplicationModel.Chat;
using Windows.ApplicationModel.Email;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media.Animation;
using Windows.UI.Xaml.Navigation;
using Windows.Web.Http;
using Windows.Web.Http.Filters;

namespace Vine.Views;

public sealed class SettingsView : BasePage, IComponentConnector
{
	private VineUserModel _user;

	private bool _isRedOn;

	private bool _isOrangeOn;

	private bool _isYellowOn;

	private bool _isGreenOn;

	private bool _isTealOn;

	private bool _isBlueLightOn;

	private bool _isBlueDarkOn;

	private bool _isPurpleOn;

	private bool _isPinkOn;

	private bool _isBusy;

	private bool _isError;

	private string _errorText = ResourceHelper.GetString("SettingsEmptyCell");

	private bool _showRetry = true;

	private Popup verifyEmailPopup = new Popup();

	private Popup verifyPhonePopup = new Popup();

	private string _username;

	private string _description;

	private string _location;

	private string _email;

	private string _phone;

	public const string RedColor = "0xff5967";

	public const string OrangeColor = "0xff794d";

	public const string YellowColor = "0xffaf40";

	public const string GreenColor = "0x68bf60";

	public const string TealColor = "0x33ccbf";

	public const string BlueLightColor = "0x6db0f2";

	public const string BlueDarkColor = "0x5082e5";

	public const string PurpleColor = "0x7870cc";

	public const string PinkColor = "0xf279ac";

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Storyboard EntranceStoryboard;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid ErrorState;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ScrollViewer ScrollViewer;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Border OpacityMask;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid TitleBarLoaded;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox UserName;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AvatarControl UserAvatar;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid TitleBarError;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public VineUserModel User
	{
		get
		{
			return _user;
		}
		set
		{
			SetProperty(ref _user, value, "User");
			NotifyOfPropertyChange(() => TwitterConnectedText);
			NotifyOfPropertyChange(() => FacebookConnectedText);
			NotifyOfPropertyChange(() => EmailVerifiedImage);
			NotifyOfPropertyChange(() => PhoneVerifiedImage);
			NotifyOfPropertyChange(() => EmailVerifiedFill);
			NotifyOfPropertyChange(() => PhoneVerifiedFill);
		}
	}

	public bool IsRedOn
	{
		get
		{
			return _isRedOn;
		}
		set
		{
			SetProperty(ref _isRedOn, value, "IsRedOn");
		}
	}

	public bool IsOrangeOn
	{
		get
		{
			return _isOrangeOn;
		}
		set
		{
			SetProperty(ref _isOrangeOn, value, "IsOrangeOn");
		}
	}

	public bool IsYellowOn
	{
		get
		{
			return _isYellowOn;
		}
		set
		{
			SetProperty(ref _isYellowOn, value, "IsYellowOn");
		}
	}

	public bool IsGreenOn
	{
		get
		{
			return _isGreenOn;
		}
		set
		{
			SetProperty(ref _isGreenOn, value, "IsGreenOn");
		}
	}

	public bool IsTealOn
	{
		get
		{
			return _isTealOn;
		}
		set
		{
			SetProperty(ref _isTealOn, value, "IsTealOn");
		}
	}

	public bool IsBlueLightOn
	{
		get
		{
			return _isBlueLightOn;
		}
		set
		{
			SetProperty(ref _isBlueLightOn, value, "IsBlueLightOn");
		}
	}

	public bool IsBlueDarkOn
	{
		get
		{
			return _isBlueDarkOn;
		}
		set
		{
			SetProperty(ref _isBlueDarkOn, value, "IsBlueDarkOn");
		}
	}

	public bool IsPurpleOn
	{
		get
		{
			return _isPurpleOn;
		}
		set
		{
			SetProperty(ref _isPurpleOn, value, "IsPurpleOn");
		}
	}

	public bool IsPinkOn
	{
		get
		{
			return _isPinkOn;
		}
		set
		{
			SetProperty(ref _isPinkOn, value, "IsPinkOn");
		}
	}

	public bool IsBusy
	{
		get
		{
			return _isBusy;
		}
		set
		{
			SetProperty(ref _isBusy, value, "IsBusy");
			NotifyOfPropertyChange(() => IsErrorOrBusy);
		}
	}

	public bool IsError
	{
		get
		{
			return _isError;
		}
		set
		{
			SetProperty(ref _isError, value, "IsError");
			NotifyOfPropertyChange(() => IsErrorOrBusy);
		}
	}

	public bool IsErrorOrBusy
	{
		get
		{
			if (!IsBusy)
			{
				return IsError;
			}
			return true;
		}
	}

	public string ErrorText
	{
		get
		{
			return _errorText;
		}
		set
		{
			SetProperty(ref _errorText, value, "ErrorText");
		}
	}

	public bool ShowRetry
	{
		get
		{
			return _showRetry;
		}
		set
		{
			SetProperty(ref _showRetry, value, "ShowRetry");
		}
	}

	public string EmailVerifiedImage
	{
		get
		{
			if (User == null || !User.VerifiedEmail)
			{
				return "M 30.5653,20.904L 28,32L 25.3333,32L 22.768,20.904C 22.7027,20.6147 22.6667,20.3133 22.6667,20C 22.6667,17.792 24.4573,16 26.6667,16C 28.876,16 30.6667,17.792 30.6667,20C 30.6667,20.3133 30.632,20.6147 30.5653,20.904 Z M 26.6667,42.6667C 24.4573,42.6667 22.6667,40.8787 22.6667,38.6667C 22.6667,36.4586 24.4573,34.6667 26.6667,34.6667C 28.876,34.6667 30.6667,36.4586 30.6667,38.6667C 30.6667,40.8787 28.876,42.6667 26.6667,42.6667 Z M 52.9947,44.0347L 28.9947,1.36804C 28.5387,0.552002 27.6667,0 26.6667,0C 25.6667,0 24.7947,0.552002 24.3387,1.36804L 0.338654,44.0347C 0.122681,44.42 0,44.8627 0,45.3333C 0,46.808 1.19467,48 2.66666,48L 50.6667,48C 52.14,48 53.3333,46.808 53.3333,45.3333C 53.3333,44.8627 53.2107,44.42 52.9947,44.0347 Z";
			}
			return "M1.2,9.599c-0.663,0-1.2,0.538-1.2,1.201c0,0.238,0.069,0.458,0.19,0.646l7.42,11.497 C8.041,23.58,8.771,24,9.6,24c0.888,0,1.662-0.482,2.079-1.199l12.16-21C23.94,1.624,24,1.418,24,1.201C24,0.538,23.461,0,22.8,0 h-2.771c-0.443,0-0.832,0.242-1.039,0.6L9.487,17.004l-4.437-6.873c-0.216-0.32-0.581-0.53-0.996-0.53L1.2,9.599z";
		}
	}

	public string PhoneVerifiedImage
	{
		get
		{
			if (User == null || !User.VerifiedPhoneNumber)
			{
				return "M 30.5653,20.904L 28,32L 25.3333,32L 22.768,20.904C 22.7027,20.6147 22.6667,20.3133 22.6667,20C 22.6667,17.792 24.4573,16 26.6667,16C 28.876,16 30.6667,17.792 30.6667,20C 30.6667,20.3133 30.632,20.6147 30.5653,20.904 Z M 26.6667,42.6667C 24.4573,42.6667 22.6667,40.8787 22.6667,38.6667C 22.6667,36.4586 24.4573,34.6667 26.6667,34.6667C 28.876,34.6667 30.6667,36.4586 30.6667,38.6667C 30.6667,40.8787 28.876,42.6667 26.6667,42.6667 Z M 52.9947,44.0347L 28.9947,1.36804C 28.5387,0.552002 27.6667,0 26.6667,0C 25.6667,0 24.7947,0.552002 24.3387,1.36804L 0.338654,44.0347C 0.122681,44.42 0,44.8627 0,45.3333C 0,46.808 1.19467,48 2.66666,48L 50.6667,48C 52.14,48 53.3333,46.808 53.3333,45.3333C 53.3333,44.8627 53.2107,44.42 52.9947,44.0347 Z";
			}
			return "M1.2,9.599c-0.663,0-1.2,0.538-1.2,1.201c0,0.238,0.069,0.458,0.19,0.646l7.42,11.497 C8.041,23.58,8.771,24,9.6,24c0.888,0,1.662-0.482,2.079-1.199l12.16-21C23.94,1.624,24,1.418,24,1.201C24,0.538,23.461,0,22.8,0 h-2.771c-0.443,0-0.832,0.242-1.039,0.6L9.487,17.004l-4.437-6.873c-0.216-0.32-0.581-0.53-0.996-0.53L1.2,9.599z";
		}
	}

	public string EmailVerifiedFill
	{
		get
		{
			if (User == null || !User.VerifiedEmail)
			{
				return "#E94D57";
			}
			return "#36B98D";
		}
	}

	public string PhoneVerifiedFill
	{
		get
		{
			if (User == null || !User.VerifiedPhoneNumber)
			{
				return "#E94D57";
			}
			return "#36B98D";
		}
	}

	public string TwitterConnectedText
	{
		get
		{
			if (User == null)
			{
				return "";
			}
			if (User.TwitterConnected)
			{
				if (!string.IsNullOrEmpty(User.TwitterScreenname))
				{
					return "@" + User.TwitterScreenname;
				}
				return ResourceHelper.GetString("Connected").ToLower();
			}
			return ResourceHelper.GetString("NotConnected");
		}
	}

	public string FacebookConnectedText
	{
		get
		{
			if (User == null)
			{
				return "";
			}
			if (!User.FacebookConnected)
			{
				return ResourceHelper.GetString("NotConnected");
			}
			return ResourceHelper.GetString("Connected");
		}
	}

	public string Version => "V" + ApplicationSettings.Current.ClientVersion;

	public bool IsFinishedLoading { get; set; }

	public SettingsView()
	{
		//IL_0018: Unknown result type (might be due to invalid IL or missing references)
		//IL_0022: Expected O, but got Unknown
		//IL_0023: Unknown result type (might be due to invalid IL or missing references)
		//IL_002d: Expected O, but got Unknown
		InitializeComponent();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.MyProfile, "settings"));
		if (IsFinishedLoading)
		{
			return;
		}
		IsError = false;
		((UIElement)ScrollViewer).put_Visibility((Visibility)0);
		VineUserModel vineUserModel = e.LoadValueOrDefault<VineUserModel>("User");
		if (e.PageState != null && vineUserModel != null)
		{
			User = vineUserModel;
			User.FacebookConnected = ApplicationSettings.Current.IsFacebookEnabled;
			User.TwitterConnected = ApplicationSettings.Current.IsTwitterEnabled;
			User.VerifiedEmail = ApplicationSettings.Current.IsEmailVerified;
			User.VerifiedPhoneNumber = ApplicationSettings.Current.IsPhoneVerified;
			User.ProfileBackground = ApplicationSettings.Current.User.ProfileBackground;
			double offset = e.LoadValueOrDefault<double>("ScrollOffset");
			await ScrollViewer.ScrollToVerticalOffsetSpin(offset);
		}
		else
		{
			((UIElement)ScrollViewer).put_Opacity(0.0);
			((UIElement)ScrollViewer).put_Visibility((Visibility)1);
			IsBusy = true;
			ApiResult<BaseVineResponseModel<VineUserModel>> apiResult = await App.Api.GetUserMe();
			if (!apiResult.HasError)
			{
				User = apiResult.Model.Data;
				ApplicationSettings.Current.User = User;
				((UIElement)ScrollViewer).put_Visibility((Visibility)0);
				EntranceStoryboard.Begin();
			}
			else
			{
				IsError = true;
				if (apiResult.HasConnectivityError)
				{
					ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
					ShowRetry = false;
				}
				else
				{
					ErrorText = ResourceHelper.GetString("SettingsEmptyCell");
					ShowRetry = true;
				}
			}
			IsBusy = false;
		}
		if (User != null)
		{
			((FrameworkElement)UserAvatar).put_DataContext((object)User);
			if (App.TempNewAvatar != null)
			{
				(await UserAvatar.CropAndUpload(App.TempNewAvatar)).PopUpErrorIfExists();
			}
			SetCurrentColor(User.ProfileBackground);
			NotifyOfPropertyChange(() => EmailVerifiedImage);
			NotifyOfPropertyChange(() => PhoneVerifiedImage);
			NotifyOfPropertyChange(() => EmailVerifiedFill);
			NotifyOfPropertyChange(() => PhoneVerifiedFill);
		}
		if (!IsError)
		{
			IsFinishedLoading = true;
		}
	}

	private void ErrorState_Click(object sender, RoutedEventArgs e)
	{
		if (NetworkHelper.CheckForConnectivity())
		{
			IsFinishedLoading = false;
			LoadState();
		}
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		e.PageState["ScrollOffset"] = ScrollViewer.VerticalOffset;
		e.PageState["User"] = User;
	}

	private void TurnOffAllColors()
	{
		IsRedOn = false;
		IsOrangeOn = false;
		IsYellowOn = false;
		IsGreenOn = false;
		IsTealOn = false;
		IsBlueLightOn = false;
		IsBlueDarkOn = false;
		IsPurpleOn = false;
		IsPinkOn = false;
	}

	private void Username_GotFocus(object sender, RoutedEventArgs e)
	{
		_username = User.Username;
	}

	private async void Username_LostFocus(object sender, RoutedEventArgs e)
	{
		if (_username != User.Username)
		{
			ApiResult<BaseVineResponseModel> obj = await App.Api.PutUser(User.UserId, User.Username);
			obj.PopUpErrorIfExists();
			if (!obj.HasError)
			{
				EventAggregator.Current.Publish(new ProfileChanged
				{
					Profile = User
				});
			}
		}
	}

	private void Description_GotFocus(object sender, RoutedEventArgs e)
	{
		_description = User.Description;
	}

	private async void Description_LostFocus(object sender, RoutedEventArgs e)
	{
		if (_description != User.Description)
		{
			ApiResult<BaseVineResponseModel> obj = await App.Api.PutUser(User.UserId, null, User.Description);
			obj.PopUpErrorIfExists();
			if (!obj.HasError)
			{
				EventAggregator.Current.Publish(new ProfileChanged
				{
					Profile = User
				});
			}
		}
	}

	private void Location_GotFocus(object sender, RoutedEventArgs e)
	{
		_location = User.Location;
	}

	private async void Location_LostFocus(object sender, RoutedEventArgs e)
	{
		if (_location != User.Location)
		{
			ApiResult<BaseVineResponseModel> obj = await App.Api.PutUser(User.UserId, null, null, User.Location);
			obj.PopUpErrorIfExists();
			if (!obj.HasError)
			{
				EventAggregator.Current.Publish(new ProfileChanged
				{
					Profile = User
				});
			}
		}
	}

	private void Email_GotFocus(object sender, RoutedEventArgs e)
	{
		_email = User.Email;
	}

	private async void Email_LostFocus(object sender, RoutedEventArgs e)
	{
		if (!(_email != User.Email))
		{
			return;
		}
		ApiResult<BaseVineResponseModel> obj = await App.Api.PutUser(User.UserId, null, null, null, User.Email);
		obj.PopUpErrorIfExists();
		if (!obj.HasError)
		{
			ApplicationSettings.Current.IsEmailVerified = false;
			User.VerifiedEmail = false;
			NotifyOfPropertyChange(() => EmailVerifiedImage);
			NotifyOfPropertyChange(() => EmailVerifiedFill);
			EventAggregator.Current.Publish(new ProfileChanged
			{
				Profile = User
			});
		}
	}

	private void Phone_GotFocus(object sender, RoutedEventArgs e)
	{
		_phone = User.PhoneNumber;
	}

	private async void Phone_LostFocus(object sender, RoutedEventArgs e)
	{
		if (!(_phone != User.PhoneNumber))
		{
			return;
		}
		ApiResult<BaseVineResponseModel> obj = await App.Api.PutUser(User.UserId, null, null, null, null, User.PhoneNumber);
		obj.PopUpErrorIfExists();
		if (!obj.HasError)
		{
			ApplicationSettings.Current.IsPhoneVerified = false;
			User.VerifiedPhoneNumber = false;
			NotifyOfPropertyChange(() => PhoneVerifiedImage);
			NotifyOfPropertyChange(() => PhoneVerifiedFill);
			EventAggregator.Current.Publish(new ProfileChanged
			{
				Profile = User
			});
		}
	}

	private async Task ChangeColor(string hex, bool state, Action<bool> toggleState)
	{
		TurnOffAllColors();
		state = !state;
		toggleState(state);
		string oldBg = User.ProfileBackground;
		if (state)
		{
			User.ProfileBackground = hex;
		}
		else
		{
			User.ProfileBackground = string.Empty;
		}
		ApiResult<BaseVineResponseModel> apiResult = await App.Api.PutUser(User.UserId, null, null, null, null, null, null, null, User.ProfileBackground);
		if (apiResult.HasError)
		{
			apiResult.PopUpErrorIfExists();
			User.ProfileBackground = oldBg;
			toggleState(!state);
			SetCurrentColor(User.ProfileBackground);
		}
		else
		{
			ApplicationSettings.Current.User = User;
			EventAggregator.Current.Publish(new ProfileChanged
			{
				Profile = User
			});
		}
	}

	private void SetCurrentColor(string hex)
	{
		switch (hex)
		{
		case "0xff5967":
			IsRedOn = true;
			break;
		case "0xff794d":
			IsOrangeOn = true;
			break;
		case "0xffaf40":
			IsYellowOn = true;
			break;
		case "0x68bf60":
			IsGreenOn = true;
			break;
		case "0x33ccbf":
			IsTealOn = true;
			break;
		case "0x6db0f2":
			IsBlueLightOn = true;
			break;
		case "0x5082e5":
			IsBlueDarkOn = true;
			break;
		case "0x7870cc":
			IsPurpleOn = true;
			break;
		case "0xf279ac":
			IsPinkOn = true;
			break;
		}
	}

	private async void Red_Tapped(object sender, TappedRoutedEventArgs e)
	{
		await ChangeColor("0xff5967", IsRedOn, delegate(bool state)
		{
			IsRedOn = state;
		});
	}

	private async void Orange_Tapped(object sender, TappedRoutedEventArgs e)
	{
		await ChangeColor("0xff794d", IsOrangeOn, delegate(bool state)
		{
			IsOrangeOn = state;
		});
	}

	private async void Yellow_Tapped(object sender, TappedRoutedEventArgs e)
	{
		await ChangeColor("0xffaf40", IsYellowOn, delegate(bool state)
		{
			IsYellowOn = state;
		});
	}

	private async void Green_Tapped(object sender, TappedRoutedEventArgs e)
	{
		await ChangeColor("0x68bf60", IsGreenOn, delegate(bool state)
		{
			IsGreenOn = state;
		});
	}

	private async void Teal_Tapped(object sender, TappedRoutedEventArgs e)
	{
		await ChangeColor("0x33ccbf", IsTealOn, delegate(bool state)
		{
			IsTealOn = state;
		});
	}

	private async void BlueLight_Tapped(object sender, TappedRoutedEventArgs e)
	{
		await ChangeColor("0x6db0f2", IsBlueLightOn, delegate(bool state)
		{
			IsBlueLightOn = state;
		});
	}

	private async void BlueDark_Tapped(object sender, TappedRoutedEventArgs e)
	{
		await ChangeColor("0x5082e5", IsBlueDarkOn, delegate(bool state)
		{
			IsBlueDarkOn = state;
		});
	}

	private async void Purple_Tapped(object sender, TappedRoutedEventArgs e)
	{
		await ChangeColor("0x7870cc", IsPurpleOn, delegate(bool state)
		{
			IsPurpleOn = state;
		});
	}

	private async void Pink_Tapped(object sender, TappedRoutedEventArgs e)
	{
		await ChangeColor("0xf279ac", IsPinkOn, delegate(bool state)
		{
			IsPinkOn = state;
		});
	}

	private void Signout_Click(object sender, RoutedEventArgs e)
	{
		App.SignOut();
	}

	private void YourContent_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		((Page)this).Frame.Navigate(typeof(SettingsContentView));
	}

	private void Privacy_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		((Page)this).Frame.Navigate(typeof(SettingsPrivacyView));
	}

	private async void ResetPassword_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		if (string.IsNullOrEmpty(User.Email))
		{
			await new MessageDialog(ResourceHelper.GetString("SettingsResetPasswordNoEmailErrorMessage"), ResourceHelper.GetString("SettingsResetPasswordNoEmailErrorTitle"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok")) }
			}.ShowAsync();
			return;
		}
		MessageDialog val = new MessageDialog(ResourceHelper.GetString("ResetPasswordPrompt"), ResourceHelper.GetString("ResetPassword"))
		{
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Send"), (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
		};
		val.put_CancelCommandIndex(1u);
		if ((int)(await val.ShowAsync()).Id == 0 && (await App.Api.ResetPassword(User.Email)).HasError)
		{
			await new MessageDialog(ResourceHelper.GetString("NoResponseFromServerError"), ResourceHelper.GetString("vine"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok")) }
			}.ShowAsync();
		}
	}

	private async void ButtonFacebook_OnClick(object sender, RoutedEventArgs e)
	{
		if (!User.FacebookConnected)
		{
			((Page)this).Frame.Navigate(typeof(FacebookView));
			return;
		}
		MessageDialog val = new MessageDialog(ResourceHelper.GetString("DisconnectFromFacebook"), ResourceHelper.GetString("ConfirmCaption"))
		{
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Disconnect"), (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
		};
		val.put_CancelCommandIndex(1u);
		if ((int)(await val.ShowAsync()).Id != 0)
		{
			return;
		}
		User.FacebookConnected = false;
		NotifyOfPropertyChange(() => FacebookConnectedText);
		ApiResult result = await App.Api.UpdateFacebookCredentials(string.Empty, "\\s", ApplicationSettings.Current.VineSession.UserId);
		if (!result.HasError && ApplicationSettings.Current.FbSession != null)
		{
			await App.Api.FbPermissionsDelete(ApplicationSettings.Current.FbSession.Id, ApplicationSettings.Current.FbSession.Token);
		}
		if (!result.HasError && ApplicationSettings.Current.FbSession != null)
		{
			HttpCookieManager cookieManager = new HttpBaseProtocolFilter().CookieManager;
			foreach (HttpCookie item in (IEnumerable<HttpCookie>)cookieManager.GetCookies(FacebookConstants.AuthUri))
			{
				cookieManager.DeleteCookie(item);
			}
			ApplicationSettings.Current.FbSession = null;
			ApplicationSettings.Current.IsFacebookEnabled = false;
		}
		if (result.HasError)
		{
			User.FacebookConnected = true;
			NotifyOfPropertyChange(() => FacebookConnectedText);
		}
	}

	private async void ButtonTwitter_OnClick(object sender, RoutedEventArgs e)
	{
		if (!User.TwitterConnected)
		{
			App.RootFrame.NavigateWithObject(typeof(LoginTwitterView), new LoginParameters
			{
				UpdatingTwitterCredentials = true
			});
		}
		else if (!string.IsNullOrEmpty(User.Email))
		{
			MessageDialog val = new MessageDialog(ResourceHelper.GetString("DisconnectFromTwitter"), ResourceHelper.GetString("ConfirmCaption"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Disconnect"), (UICommandInvokedHandler)null, (object)0) },
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
			};
			val.put_CancelCommandIndex(1u);
			if ((int)(await val.ShowAsync()).Id != 0)
			{
				return;
			}
			User.TwitterConnected = false;
			NotifyOfPropertyChange(() => TwitterConnectedText);
			if ((await App.Api.UpdateTwitterCredentials("\\s", "\\s", "\\s")).HasError)
			{
				User.TwitterConnected = true;
				NotifyOfPropertyChange(() => TwitterConnectedText);
			}
			else
			{
				ApplicationSettings.Current.IsTwitterEnabled = false;
			}
		}
		else
		{
			await new MessageDialog(ResourceHelper.GetString("RequiresTwitterAccount"), ResourceHelper.GetString("vine"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok"), (UICommandInvokedHandler)null, (object)0) }
			}.ShowAsync();
		}
	}

	private async void BottonText_OnClick(object sender, RoutedEventArgs e)
	{
		ChatMessage val = new ChatMessage();
		val.put_Body(GetInviteMessage());
		await ChatMessageManager.ShowComposeSmsMessageAsync(val);
	}

	private async void ButtonEmail_OnClick(object sender, RoutedEventArgs e)
	{
		EmailMessage val = new EmailMessage();
		val.put_Body(GetInviteMessage());
		val.put_Subject(ResourceHelper.GetString("FollowOnVineEmailSubject"));
		await EmailManager.ShowComposeNewEmailAsync(val);
	}

	private void ButtonFindPeople_OnClick(object sender, RoutedEventArgs e)
	{
		App.RootFrame.Navigate(typeof(FriendFinderView));
	}

	private string GetInviteMessage()
	{
		return string.Format(ResourceHelper.GetString("find_friends_invite_email"), new object[1] { User.UserId });
	}

	private void TOS_Click(object sender, RoutedEventArgs e)
	{
		if (NetworkHelper.CheckForConnectivity())
		{
			App.RootFrame.Navigate(typeof(WebView), (object)"https://vine.co/terms");
		}
		else
		{
			DisplayConnectivityError();
		}
	}

	private void DisplayConnectivityError()
	{
		((UIElement)ScrollViewer).put_Visibility((Visibility)1);
		ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
		ShowRetry = false;
		IsError = true;
	}

	private void Privacy_Click(object sender, RoutedEventArgs e)
	{
		if (NetworkHelper.CheckForConnectivity())
		{
			App.RootFrame.Navigate(typeof(WebView), (object)"https://vine.co/privacy");
		}
		else
		{
			DisplayConnectivityError();
		}
	}

	private void Attribute_Click(object sender, RoutedEventArgs e)
	{
		((Page)this).Frame.Navigate(typeof(AttributionView));
	}

	private void Help_Click(object sender, RoutedEventArgs e)
	{
		if (NetworkHelper.CheckForConnectivity())
		{
			App.RootFrame.Navigate(typeof(WebView), (object)"https://vine.co/mobile-faq");
		}
		else
		{
			DisplayConnectivityError();
		}
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.NavigationMode == 1 && (verifyEmailPopup.IsOpen || verifyPhonePopup.IsOpen))
		{
			e.put_Cancel(true);
		}
		else
		{
			((Page)this).OnNavigatingFrom(e);
		}
		ClosePopups();
	}

	private void ClosePopups()
	{
		verifyEmailPopup.put_IsOpen(false);
		verifyEmailPopup.put_Child((UIElement)null);
		verifyPhonePopup.put_IsOpen(false);
		verifyPhonePopup.put_Child((UIElement)null);
		((UIElement)OpacityMask).put_Visibility((Visibility)1);
	}

	private void EmailVerify_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		if (User != null && !string.IsNullOrEmpty(User.Email) && !User.VerifiedEmail)
		{
			VerifyEmailPopupControl verifyEmailPopupControl = new VerifyEmailPopupControl(User.Email);
			((FrameworkElement)verifyEmailPopupControl).put_Width(((FrameworkElement)App.RootFrame).ActualWidth);
			((FrameworkElement)verifyEmailPopupControl).put_HorizontalAlignment((HorizontalAlignment)3);
			VerifyEmailPopupControl verifyEmailPopupControl2 = verifyEmailPopupControl;
			((UIElement)OpacityMask).put_Visibility((Visibility)0);
			verifyEmailPopup.put_Child((UIElement)(object)verifyEmailPopupControl2);
			verifyEmailPopupControl2.CancelClicked += VerifyEmailCancelClicked;
			verifyEmailPopupControl2.SendEmailClicked += VerifyEmailSendClicked;
			verifyEmailPopupControl2.EnterCodeClicked += verifyEmailControl_EnterCodeClicked;
			verifyEmailPopup.put_IsOpen(true);
		}
	}

	private void VerifyEmailCancelClicked(object sender, EventArgs e)
	{
		ClosePopups();
	}

	private void verifyEmailControl_EnterCodeClicked(object sender, EventArgs e)
	{
		App.RootFrame.NavigateWithObject(typeof(VerifyEmailCodeEnterView), User);
	}

	private async void VerifyEmailSendClicked(object sender, EventArgs e)
	{
		ClosePopups();
		ApiResult obj = await App.Api.SendVerifyEmailRequest();
		obj.PopUpErrorIfExists();
		if (!obj.HasError)
		{
			App.RootFrame.NavigateWithObject(typeof(VerifyEmailCodeEnterView), User);
		}
	}

	private async void PhoneVerify_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		if (User != null && !string.IsNullOrEmpty(User.PhoneNumber) && !User.VerifiedPhoneNumber)
		{
			VerifyPhonePopupControl verifyPhonePopupControl = new VerifyPhonePopupControl(User.PhoneNumber);
			((FrameworkElement)verifyPhonePopupControl).put_Width(((FrameworkElement)App.RootFrame).ActualWidth);
			((FrameworkElement)verifyPhonePopupControl).put_HorizontalAlignment((HorizontalAlignment)3);
			VerifyPhonePopupControl verifyPhonePopupControl2 = verifyPhonePopupControl;
			((UIElement)OpacityMask).put_Visibility((Visibility)0);
			verifyPhonePopup.put_Child((UIElement)(object)verifyPhonePopupControl2);
			verifyPhonePopupControl2.CancelClicked += verifyPhoneControl_CancelClicked;
			verifyPhonePopupControl2.SendSMSClicked += verifyPhoneControl_SendSMSClicked;
			verifyPhonePopupControl2.EnterCodeClicked += verifyPhoneControl_EnterCodeClicked;
			verifyPhonePopup.put_IsOpen(true);
		}
	}

	private void verifyPhoneControl_EnterCodeClicked(object sender, EventArgs e)
	{
		App.RootFrame.NavigateWithObject(typeof(VerifyPhoneCodeEnterView), User);
	}

	private async void verifyPhoneControl_SendSMSClicked(object sender, EventArgs e)
	{
		ClosePopups();
		ApiResult obj = await App.Api.SendVerifyPhoneRequest();
		obj.PopUpErrorIfExists();
		if (!obj.HasError)
		{
			App.RootFrame.NavigateWithObject(typeof(VerifyPhoneCodeEnterView), User);
		}
	}

	private void verifyPhoneControl_CancelClicked(object sender, EventArgs e)
	{
		ClosePopups();
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Expected O, but got Unknown
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Expected O, but got Unknown
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/SettingsView.xaml"), (ComponentResourceLocation)0);
			EntranceStoryboard = (Storyboard)((FrameworkElement)this).FindName("EntranceStoryboard");
			ErrorState = (Grid)((FrameworkElement)this).FindName("ErrorState");
			ScrollViewer = (ScrollViewer)((FrameworkElement)this).FindName("ScrollViewer");
			OpacityMask = (Border)((FrameworkElement)this).FindName("OpacityMask");
			TitleBarLoaded = (Grid)((FrameworkElement)this).FindName("TitleBarLoaded");
			UserName = (TextBox)((FrameworkElement)this).FindName("UserName");
			UserAvatar = (AvatarControl)((FrameworkElement)this).FindName("UserAvatar");
			TitleBarError = (Grid)((FrameworkElement)this).FindName("TitleBarError");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_008a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0090: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		//IL_00bc: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c2: Expected O, but got Unknown
		//IL_00e3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ed: Expected O, but got Unknown
		//IL_00f3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f9: Expected O, but got Unknown
		//IL_011a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0124: Expected O, but got Unknown
		//IL_0125: Unknown result type (might be due to invalid IL or missing references)
		//IL_012b: Expected O, but got Unknown
		//IL_014c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0156: Expected O, but got Unknown
		//IL_015c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0162: Expected O, but got Unknown
		//IL_0183: Unknown result type (might be due to invalid IL or missing references)
		//IL_018d: Expected O, but got Unknown
		//IL_018e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0194: Expected O, but got Unknown
		//IL_01b5: Unknown result type (might be due to invalid IL or missing references)
		//IL_01bf: Expected O, but got Unknown
		//IL_01c5: Unknown result type (might be due to invalid IL or missing references)
		//IL_01cb: Expected O, but got Unknown
		//IL_01ec: Unknown result type (might be due to invalid IL or missing references)
		//IL_01f6: Expected O, but got Unknown
		//IL_01fc: Unknown result type (might be due to invalid IL or missing references)
		//IL_0202: Expected O, but got Unknown
		//IL_0223: Unknown result type (might be due to invalid IL or missing references)
		//IL_022d: Expected O, but got Unknown
		//IL_0233: Unknown result type (might be due to invalid IL or missing references)
		//IL_0239: Expected O, but got Unknown
		//IL_025a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0264: Expected O, but got Unknown
		//IL_026a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0270: Expected O, but got Unknown
		//IL_0291: Unknown result type (might be due to invalid IL or missing references)
		//IL_029b: Expected O, but got Unknown
		//IL_02a1: Unknown result type (might be due to invalid IL or missing references)
		//IL_02a7: Expected O, but got Unknown
		//IL_02c8: Unknown result type (might be due to invalid IL or missing references)
		//IL_02d2: Expected O, but got Unknown
		//IL_02d8: Unknown result type (might be due to invalid IL or missing references)
		//IL_02de: Expected O, but got Unknown
		//IL_02ff: Unknown result type (might be due to invalid IL or missing references)
		//IL_0309: Expected O, but got Unknown
		//IL_030f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0315: Expected O, but got Unknown
		//IL_0336: Unknown result type (might be due to invalid IL or missing references)
		//IL_0340: Expected O, but got Unknown
		//IL_0346: Unknown result type (might be due to invalid IL or missing references)
		//IL_034c: Expected O, but got Unknown
		//IL_036d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0377: Expected O, but got Unknown
		//IL_037d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0383: Expected O, but got Unknown
		//IL_03a4: Unknown result type (might be due to invalid IL or missing references)
		//IL_03ae: Expected O, but got Unknown
		//IL_03b4: Unknown result type (might be due to invalid IL or missing references)
		//IL_03ba: Expected O, but got Unknown
		//IL_03db: Unknown result type (might be due to invalid IL or missing references)
		//IL_03e5: Expected O, but got Unknown
		//IL_03eb: Unknown result type (might be due to invalid IL or missing references)
		//IL_03f1: Expected O, but got Unknown
		//IL_0412: Unknown result type (might be due to invalid IL or missing references)
		//IL_041c: Expected O, but got Unknown
		//IL_0422: Unknown result type (might be due to invalid IL or missing references)
		//IL_0428: Expected O, but got Unknown
		//IL_0449: Unknown result type (might be due to invalid IL or missing references)
		//IL_0453: Expected O, but got Unknown
		//IL_0459: Unknown result type (might be due to invalid IL or missing references)
		//IL_045f: Expected O, but got Unknown
		//IL_0480: Unknown result type (might be due to invalid IL or missing references)
		//IL_048a: Expected O, but got Unknown
		//IL_0490: Unknown result type (might be due to invalid IL or missing references)
		//IL_0496: Expected O, but got Unknown
		//IL_04b7: Unknown result type (might be due to invalid IL or missing references)
		//IL_04c1: Expected O, but got Unknown
		//IL_04c2: Unknown result type (might be due to invalid IL or missing references)
		//IL_04c8: Expected O, but got Unknown
		//IL_04e9: Unknown result type (might be due to invalid IL or missing references)
		//IL_04f3: Expected O, but got Unknown
		//IL_04f9: Unknown result type (might be due to invalid IL or missing references)
		//IL_04ff: Expected O, but got Unknown
		//IL_0520: Unknown result type (might be due to invalid IL or missing references)
		//IL_052a: Expected O, but got Unknown
		//IL_0530: Unknown result type (might be due to invalid IL or missing references)
		//IL_0536: Expected O, but got Unknown
		//IL_0557: Unknown result type (might be due to invalid IL or missing references)
		//IL_0561: Expected O, but got Unknown
		//IL_0562: Unknown result type (might be due to invalid IL or missing references)
		//IL_0568: Expected O, but got Unknown
		//IL_0589: Unknown result type (might be due to invalid IL or missing references)
		//IL_0593: Expected O, but got Unknown
		//IL_0599: Unknown result type (might be due to invalid IL or missing references)
		//IL_059f: Expected O, but got Unknown
		//IL_05c0: Unknown result type (might be due to invalid IL or missing references)
		//IL_05ca: Expected O, but got Unknown
		//IL_05d0: Unknown result type (might be due to invalid IL or missing references)
		//IL_05d6: Expected O, but got Unknown
		//IL_05f7: Unknown result type (might be due to invalid IL or missing references)
		//IL_0601: Expected O, but got Unknown
		//IL_0607: Unknown result type (might be due to invalid IL or missing references)
		//IL_060d: Expected O, but got Unknown
		//IL_062e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0638: Expected O, but got Unknown
		//IL_063e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0644: Expected O, but got Unknown
		//IL_0665: Unknown result type (might be due to invalid IL or missing references)
		//IL_066f: Expected O, but got Unknown
		//IL_0675: Unknown result type (might be due to invalid IL or missing references)
		//IL_067b: Expected O, but got Unknown
		//IL_069c: Unknown result type (might be due to invalid IL or missing references)
		//IL_06a6: Expected O, but got Unknown
		//IL_06ac: Unknown result type (might be due to invalid IL or missing references)
		//IL_06b2: Expected O, but got Unknown
		//IL_06d3: Unknown result type (might be due to invalid IL or missing references)
		//IL_06dd: Expected O, but got Unknown
		//IL_06e3: Unknown result type (might be due to invalid IL or missing references)
		//IL_06e9: Expected O, but got Unknown
		//IL_070a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0714: Expected O, but got Unknown
		//IL_071a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0720: Expected O, but got Unknown
		//IL_0741: Unknown result type (might be due to invalid IL or missing references)
		//IL_074b: Expected O, but got Unknown
		//IL_0751: Unknown result type (might be due to invalid IL or missing references)
		//IL_0757: Expected O, but got Unknown
		//IL_0778: Unknown result type (might be due to invalid IL or missing references)
		//IL_0782: Expected O, but got Unknown
		//IL_0788: Unknown result type (might be due to invalid IL or missing references)
		//IL_078e: Expected O, but got Unknown
		//IL_07af: Unknown result type (might be due to invalid IL or missing references)
		//IL_07b9: Expected O, but got Unknown
		//IL_07bc: Unknown result type (might be due to invalid IL or missing references)
		//IL_07c2: Expected O, but got Unknown
		//IL_07e3: Unknown result type (might be due to invalid IL or missing references)
		//IL_07ed: Expected O, but got Unknown
		//IL_07f0: Unknown result type (might be due to invalid IL or missing references)
		//IL_07f6: Expected O, but got Unknown
		//IL_0817: Unknown result type (might be due to invalid IL or missing references)
		//IL_0821: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_GotFocus, (Action<EventRegistrationToken>)val2.remove_GotFocus, new RoutedEventHandler(Username_GotFocus));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_LostFocus, (Action<EventRegistrationToken>)val2.remove_LostFocus, new RoutedEventHandler(Username_LostFocus));
			break;
		}
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_GotFocus, (Action<EventRegistrationToken>)val2.remove_GotFocus, new RoutedEventHandler(Description_GotFocus));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_LostFocus, (Action<EventRegistrationToken>)val2.remove_LostFocus, new RoutedEventHandler(Description_LostFocus));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_GotFocus, (Action<EventRegistrationToken>)val2.remove_GotFocus, new RoutedEventHandler(Location_GotFocus));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_LostFocus, (Action<EventRegistrationToken>)val2.remove_LostFocus, new RoutedEventHandler(Location_LostFocus));
			break;
		}
		case 4:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(ResetPassword_OnTapped));
			break;
		}
		case 5:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(YourContent_OnTapped));
			break;
		}
		case 6:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Privacy_OnTapped));
			break;
		}
		case 7:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(ButtonFindPeople_OnClick));
			break;
		}
		case 8:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(BottonText_OnClick));
			break;
		}
		case 9:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ButtonEmail_OnClick));
			break;
		}
		case 10:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ButtonTwitter_OnClick));
			break;
		}
		case 11:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ButtonFacebook_OnClick));
			break;
		}
		case 12:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(TOS_Click));
			break;
		}
		case 13:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Privacy_Click));
			break;
		}
		case 14:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Attribute_Click));
			break;
		}
		case 15:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Help_Click));
			break;
		}
		case 16:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Signout_Click));
			break;
		}
		case 17:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_GotFocus, (Action<EventRegistrationToken>)val2.remove_GotFocus, new RoutedEventHandler(Phone_GotFocus));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_LostFocus, (Action<EventRegistrationToken>)val2.remove_LostFocus, new RoutedEventHandler(Phone_LostFocus));
			break;
		}
		case 18:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(PhoneVerify_OnTapped));
			break;
		}
		case 19:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_GotFocus, (Action<EventRegistrationToken>)val2.remove_GotFocus, new RoutedEventHandler(Email_GotFocus));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_LostFocus, (Action<EventRegistrationToken>)val2.remove_LostFocus, new RoutedEventHandler(Email_LostFocus));
			break;
		}
		case 20:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(EmailVerify_OnTapped));
			break;
		}
		case 21:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(BlueLight_Tapped));
			break;
		}
		case 22:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(BlueDark_Tapped));
			break;
		}
		case 23:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Purple_Tapped));
			break;
		}
		case 24:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Pink_Tapped));
			break;
		}
		case 25:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Red_Tapped));
			break;
		}
		case 26:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Orange_Tapped));
			break;
		}
		case 27:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Yellow_Tapped));
			break;
		}
		case 28:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Green_Tapped));
			break;
		}
		case 29:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Teal_Tapped));
			break;
		}
		case 30:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ErrorState_Click));
			break;
		}
		case 31:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Signout_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
