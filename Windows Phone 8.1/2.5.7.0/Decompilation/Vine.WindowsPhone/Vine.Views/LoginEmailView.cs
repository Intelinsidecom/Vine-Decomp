using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Globalization;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Web;
using Windows.Foundation;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Documents;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class LoginEmailView : BasePage, IComponentConnector
{
	private string _username;

	private string _password;

	private bool _isBusy;

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

	public LoginEmailView()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		InitializeComponent();
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)((FrameworkElement)this).add_Loaded, (Action<EventRegistrationToken>)((FrameworkElement)this).remove_Loaded, new RoutedEventHandler(OnLoaded));
		SignInButton.put_Label(ResourceHelper.GetString("SignIn"));
	}

	private void OnLoaded(object sender, RoutedEventArgs routedEventArgs)
	{
		App.ScribeService.Log(new ViewImpressionEvent(Section.LoggedOut, "signin"));
		if (Username == null)
		{
			((Control)UsernameBox).Focus((FocusState)2);
		}
	}

	private async void Login_Click(object sender, RoutedEventArgs e)
	{
		string text = await AttemptLogin();
		if (!string.IsNullOrEmpty(text))
		{
			await new MessageDialog(text, ResourceHelper.GetString("vine"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok")) }
			}.ShowAsync();
		}
	}

	private void ForgotPassword_Click(Hyperlink sender, HyperlinkClickEventArgs args)
	{
		App.RootFrame.Navigate(typeof(ResetPasswordView), (object)Username);
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

	private async void PasswordBox_KeyDown(object sender, KeyRoutedEventArgs e)
	{
		if ((int)e.Key == 13)
		{
			await AttemptLogin();
		}
	}

	private async Task<string> AttemptLogin()
	{
		string errorMsg = string.Empty;
		if (IsBusy)
		{
			return errorMsg;
		}
		if (string.IsNullOrEmpty(Username) || string.IsNullOrEmpty(Password))
		{
			return errorMsg;
		}
		((Control)HiddenButton).Focus((FocusState)3);
		IsBusy = true;
		if (!string.IsNullOrEmpty(Password))
		{
			if (Password.Length < 6)
			{
				errorMsg = ResourceHelper.GetString("InvalidPassword");
			}
		}
		else
		{
			errorMsg = ResourceHelper.GetString("InvalidPassword");
		}
		if (string.IsNullOrWhiteSpace(UsernameBox.Text) || !Regex.IsMatch(UsernameBox.Text, "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"))
		{
			errorMsg = ResourceHelper.GetString("InvalidEmailAddress");
		}
		if (string.IsNullOrEmpty(errorMsg))
		{
			ApiResult<VineAuthTokenResult> result = await Proceed();
			if (!result.HasError)
			{
				ApplicationSettings.Current.VineSession = result.Model.Data;
				ApiResult<BaseVineResponseModel<VineUserModel>> apiResult = await App.Api.GetUserMe();
				if (!apiResult.HasError)
				{
					ApplicationSettings.Current.User = apiResult.Model.Data;
				}
				App.RootFrame.Navigate(typeof(HomeView));
			}
			else if (!string.IsNullOrEmpty(result.ResponseContent))
			{
				BaseVineResponseModel baseVineResponseModel = Serialization.Deserialize<BaseVineResponseModel>(result.ResponseContent);
				if (baseVineResponseModel != null && baseVineResponseModel.Code == "187")
				{
					MessageDialog val = new MessageDialog(ResourceHelper.GetString("settings_activate_account_dialog"), ResourceHelper.GetString("NewAccount"))
					{
						Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("settings_activate_account_confirm"), (UICommandInvokedHandler)null, (object)0) },
						Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
					};
					val.put_CancelCommandIndex(1u);
					if ((int)(await val.ShowAsync()).Id == 0)
					{
						ApiResult<VineAuthTokenResult> apiResult2 = await App.Api.ActivateAccountFromVine(Username, Password);
						if (!apiResult2.HasError)
						{
							ApplicationSettings.Current.VineSession = apiResult2.Model.Data;
							App.RootFrame.Navigate(typeof(HomeView));
							IsBusy = false;
							return "";
						}
						errorMsg = apiResult2.GetErrorMessage(ResourceHelper.GetString("SignUpErrorMessage"));
					}
				}
				if (!string.IsNullOrEmpty(result.XDate) && DateTime.TryParseExact(result.XDate, "ddd, dd MMM yyyy HH:mm:ss UTC", CultureInfo.CurrentCulture, DateTimeStyles.AdjustToUniversal, out var result2) && Math.Abs((DateTime.UtcNow - result2).TotalMinutes) >= 30.0)
				{
					errorMsg = ResourceHelper.GetString("InvalidDateErrorMsg");
				}
			}
			if (result.HasError)
			{
				if (result.Model != null && !string.IsNullOrEmpty(result.Model.Error))
				{
					errorMsg = result.Model.Error;
				}
				else if (string.IsNullOrEmpty(errorMsg))
				{
					errorMsg = ResourceHelper.GetString("error_unknown");
				}
			}
		}
		IsBusy = false;
		return errorMsg;
	}

	private async Task<ApiResult<VineAuthTokenResult>> Proceed()
	{
		ApiResult<VineAuthTokenResult> apiResult = await App.Api.LoginEmail(Username, Password);
		if (apiResult.HasError)
		{
			if (!string.IsNullOrEmpty(apiResult.XDate) && DateTime.TryParse(apiResult.XDate, out var result) && Math.Abs((DateTime.UtcNow - result.ToUniversalTime()).TotalMinutes) >= 30.0)
			{
				ResourceHelper.GetString("InvalidDateErrorMsg");
			}
			if (apiResult.Model == null)
			{
				ResourceHelper.GetString("UnknownAuthenticationError");
			}
			else if (string.IsNullOrEmpty(apiResult.Model.Error))
			{
				apiResult.GetErrorMessage(ResourceHelper.GetString("UnknownAuthenticationError"));
			}
			else
			{
				_ = apiResult.Model.Error;
			}
		}
		return apiResult;
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/LoginEmailView.xaml"), (ComponentResourceLocation)0);
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
		//IL_001e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0024: Expected O, but got Unknown
		//IL_0055: Unknown result type (might be due to invalid IL or missing references)
		//IL_005b: Expected O, but got Unknown
		//IL_007c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0086: Expected O, but got Unknown
		//IL_0089: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Expected O, but got Unknown
		//IL_00b0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ba: Expected O, but got Unknown
		//IL_00bd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c3: Expected O, but got Unknown
		//IL_00e4: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ee: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			Hyperlink val3 = (Hyperlink)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<Hyperlink, HyperlinkClickEventArgs>, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, (TypedEventHandler<Hyperlink, HyperlinkClickEventArgs>)ForgotPassword_Click);
			break;
		}
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
