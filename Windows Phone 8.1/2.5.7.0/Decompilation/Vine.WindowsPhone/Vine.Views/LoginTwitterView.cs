using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Globalization;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Web;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class LoginTwitterView : BasePage, IComponentConnector
{
	private string _errorText;

	private bool _isError;

	private string _username;

	private string _password;

	private bool _isBusy;

	private bool UpdatingTwitterCredentials;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid ErrorState;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private PasswordBox PasswordBox;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button HiddenButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox UsernameBox;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton SignInButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

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

	public bool IsError
	{
		get
		{
			return _isError;
		}
		set
		{
			SetProperty(ref _isError, value, "IsError");
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
			_username = value;
			OnPropertyChanged("Username");
		}
	}

	public string Password
	{
		get
		{
			return _password;
		}
		set
		{
			_password = value;
			OnPropertyChanged("Password");
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
			_isBusy = value;
			OnPropertyChanged("IsBusy");
			NotifyOfPropertyChange(() => IsNotBusy);
		}
	}

	public bool IsNotBusy => !IsBusy;

	public LoginTwitterView()
	{
		//IL_0034: Unknown result type (might be due to invalid IL or missing references)
		//IL_003e: Expected O, but got Unknown
		InitializeComponent();
		IsError = false;
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)((FrameworkElement)this).add_Loaded, (Action<EventRegistrationToken>)((FrameworkElement)this).remove_Loaded, new RoutedEventHandler(OnLoaded));
		SignInButton.put_Label(ResourceHelper.GetString("SignIn"));
		base.NavigationHelper.GoBackCommand = new RelayCommand(HandleBackPressed);
	}

	private void HandleBackPressed()
	{
		if (IsError)
		{
			IsError = false;
		}
		else
		{
			base.NavigationHelper.GoBack();
		}
	}

	private void HandleLoginError(bool isConnectivityError)
	{
		IsError = true;
		ErrorText = ResourceHelper.GetString("error_auth_check_internet_connection");
	}

	private void OnLoaded(object sender, RoutedEventArgs routedEventArgs)
	{
		if (Username == null)
		{
			((Control)UsernameBox).Focus((FocusState)2);
		}
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.LoggedOut, "signup_twitter"));
		if (NavigationObject is LoginParameters loginParameters)
		{
			UpdatingTwitterCredentials = loginParameters.UpdatingTwitterCredentials;
		}
		if (e.PageState != null)
		{
			Username = e.LoadValueOrDefault("username", string.Empty);
		}
	}

	private async void Login_Click(object sender, RoutedEventArgs e)
	{
		if (IsBusy || string.IsNullOrEmpty(Username) || string.IsNullOrEmpty(Password))
		{
			return;
		}
		((Control)HiddenButton).Focus((FocusState)3);
		IsBusy = true;
		string errorMsg = null;
		ApiResult<AccessToken> apiResult = await App.Api.LoginTwitter(Username, Password);
		bool isNux = false;
		if (apiResult.HasError)
		{
			if (!string.IsNullOrEmpty(apiResult.XDate) && DateTime.TryParseExact(apiResult.XDate, "ddd, dd MMM yyyy HH:mm:ss UTC", CultureInfo.CurrentCulture, DateTimeStyles.AdjustToUniversal, out var result) && Math.Abs((DateTime.UtcNow - result).TotalMinutes) >= 30.0)
			{
				errorMsg = ResourceHelper.GetString("InvalidDateErrorMsg");
			}
			if (string.IsNullOrEmpty(errorMsg))
			{
				errorMsg = (string.IsNullOrEmpty(apiResult.ResponseContent) ? apiResult.GetErrorMessage(ResourceHelper.GetString("error_xauth")) : apiResult.GetErrorMessage(ResourceHelper.GetString("error_auth_username_pass")));
			}
		}
		else
		{
			AccessToken twitterToken = apiResult.Model;
			if (!UpdatingTwitterCredentials)
			{
				ApiResult<VineAuthTokenResult> vineAuth = await App.Api.LoginVineWithTwitterToken(twitterToken);
				bool success = false;
				if (!vineAuth.HasError)
				{
					success = true;
					ApplicationSettings.Current.VineSession = vineAuth.Model.Data;
				}
				else
				{
					BaseVineResponseModel baseResponse = Serialization.Deserialize<BaseVineResponseModel>(vineAuth.ResponseContent);
					if (vineAuth.Model != null && vineAuth.Model.Code == "101")
					{
						MessageDialog val = new MessageDialog(ResourceHelper.GetString("CreateFromTwitterPrompt"), ResourceHelper.GetString("NewAccount"))
						{
							Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok"), (UICommandInvokedHandler)null, (object)0) },
							Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
						};
						val.put_CancelCommandIndex(1u);
						if ((int)(await val.ShowAsync()).Id == 0)
						{
							ApiResult<BaseVineResponseModel<VineAuthToken>> apiResult2 = await App.Api.CreateAccountWithTwitterCredentials(Username, Username, twitterToken);
							if (apiResult2.HasError)
							{
								errorMsg = ((apiResult2.ResponseContent != null) ? apiResult2.GetErrorMessage(ResourceHelper.GetString("error_xauth")) : apiResult2.GetErrorMessage(ResourceHelper.GetString("error_auth_check_internet_connection")));
							}
							else
							{
								success = true;
								ApplicationSettings.Current.VineSession = apiResult2.Model.Data;
								isNux = true;
							}
						}
					}
					else if (baseResponse != null && baseResponse.Code == "187")
					{
						MessageDialog val2 = new MessageDialog(ResourceHelper.GetString("settings_activate_account_dialog"), ResourceHelper.GetString("NewAccount"))
						{
							Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("settings_activate_account_confirm"), (UICommandInvokedHandler)null, (object)0) },
							Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
						};
						val2.put_CancelCommandIndex(1u);
						if ((int)(await val2.ShowAsync()).Id == 0)
						{
							ApiResult<VineAuthTokenResult> apiResult3 = await App.Api.ActivateAccountFromTwitter(twitterToken);
							if (apiResult3.HasError)
							{
								errorMsg = ((apiResult3.ResponseContent != null) ? apiResult3.GetErrorMessage(ResourceHelper.GetString("SignUpErrorMessage")) : apiResult3.GetErrorMessage(ResourceHelper.GetString("error_auth_check_internet_connection")));
							}
							else
							{
								success = true;
								ApplicationSettings.Current.VineSession = apiResult3.Model.Data;
							}
						}
					}
					else
					{
						errorMsg = vineAuth.GetErrorMessage(ResourceHelper.GetString("error_xauth"));
					}
				}
				if (success)
				{
					ApiResult<BaseVineResponseModel<VineUserModel>> apiResult4 = await App.Api.GetUserMe();
					if (!apiResult4.HasError)
					{
						ApplicationSettings.Current.User = apiResult4.Model.Data;
					}
					if (isNux)
					{
						App.ClearBackStack = true;
						App.RootFrame.Navigate(typeof(FriendFinderView), (object)true);
					}
					else
					{
						App.RootFrame.Navigate(typeof(HomeView));
					}
				}
			}
			else
			{
				ApiResult<BaseVineResponseModel<VineAuthToken>> apiResult5 = await App.Api.UpdateTwitterCredentials(ApplicationSettings.Current.VineSession.UserId, twitterToken.OAuth_Token, twitterToken.OAuth_Token_Secret);
				if (apiResult5.HasError)
				{
					errorMsg = ((apiResult5.ResponseContent != null) ? apiResult5.GetErrorMessage(ResourceHelper.GetString("error_xauth")) : apiResult5.GetErrorMessage(ResourceHelper.GetString("error_auth_check_internet_connection")));
				}
				else
				{
					ApplicationSettings.Current.IsTwitterEnabled = true;
					((Page)this).Frame.GoBack();
				}
			}
		}
		if (!string.IsNullOrEmpty(errorMsg))
		{
			await new MessageDialog(errorMsg, ResourceHelper.GetString("vine"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok")) }
			}.ShowAsync();
		}
		IsBusy = false;
	}

	private void UsernameBox_KeyDown(object sender, KeyRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0008: Invalid comparison between Unknown and I4
		if ((int)e.Key == 13)
		{
			((Control)PasswordBox).Focus((FocusState)2);
		}
	}

	private void PasswordBox_KeyDown(object sender, KeyRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0008: Invalid comparison between Unknown and I4
		if ((int)e.Key == 13)
		{
			Login_Click(this, null);
		}
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		e.PageState["username"] = Username;
	}

	private void ErrorState_Click(object sender, RoutedEventArgs e)
	{
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/LoginTwitterView.xaml"), (ComponentResourceLocation)0);
			ErrorState = (Grid)((FrameworkElement)this).FindName("ErrorState");
			PasswordBox = (PasswordBox)((FrameworkElement)this).FindName("PasswordBox");
			HiddenButton = (Button)((FrameworkElement)this).FindName("HiddenButton");
			UsernameBox = (TextBox)((FrameworkElement)this).FindName("UsernameBox");
			SignInButton = (AppBarButton)((FrameworkElement)this).FindName("SignInButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_003a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0040: Expected O, but got Unknown
		//IL_0061: Unknown result type (might be due to invalid IL or missing references)
		//IL_006b: Expected O, but got Unknown
		//IL_006e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0074: Expected O, but got Unknown
		//IL_0095: Unknown result type (might be due to invalid IL or missing references)
		//IL_009f: Expected O, but got Unknown
		//IL_00a2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a8: Expected O, but got Unknown
		//IL_00c9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d3: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
			((TOSControl)target).ErrorOccurredEvent += HandleLoginError;
			break;
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<KeyEventHandler, EventRegistrationToken>)val2.add_KeyDown, (Action<EventRegistrationToken>)val2.remove_KeyDown, new KeyEventHandler(PasswordBox_KeyDown));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<KeyEventHandler, EventRegistrationToken>)val2.add_KeyDown, (Action<EventRegistrationToken>)val2.remove_KeyDown, new KeyEventHandler(UsernameBox_KeyDown));
			break;
		}
		case 4:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Login_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
