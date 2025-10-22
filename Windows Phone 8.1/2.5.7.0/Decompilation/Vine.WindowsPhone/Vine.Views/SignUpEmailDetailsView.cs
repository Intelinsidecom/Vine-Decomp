using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
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

public sealed class SignUpEmailDetailsView : BasePage, IComponentConnector
{
	private string _email;

	public string _phoneNumber;

	private string _password;

	private bool _isBusy;

	private string _errorText;

	private bool _isError;

	private bool _isAcceptInProgress;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid ErrorState;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox PhoneNumberBox;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private PasswordBox PasswordBox;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox EmailBox;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AvatarControl UserAvatar;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private CommandBar cmdBarAccept;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton AcceptButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public VineUserModel User => (VineUserModel)base.NavigationObject;

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

	public string Password
	{
		get
		{
			return _password;
		}
		set
		{
			SetProperty(ref _password, value, "Password");
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

	public bool IsFinishedLoading { get; set; }

	private void HandleLoginError(bool isConnectivityError)
	{
		IsError = true;
		ErrorText = ResourceHelper.GetString("error_auth_check_internet_connection");
	}

	public SignUpEmailDetailsView()
	{
		//IL_0034: Unknown result type (might be due to invalid IL or missing references)
		//IL_003e: Expected O, but got Unknown
		InitializeComponent();
		IsError = false;
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)((FrameworkElement)this).add_Loaded, (Action<EventRegistrationToken>)((FrameworkElement)this).remove_Loaded, new RoutedEventHandler(SignUpEmailDetailsView_Loaded));
		AcceptButton.put_Label(ResourceHelper.GetString("SignIn"));
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

	private void SignUpEmailDetailsView_Loaded(object sender, RoutedEventArgs e)
	{
		((Control)EmailBox).Focus((FocusState)3);
	}

	protected override void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.LoggedOut, "signup_email_1"));
		if (!IsFinishedLoading)
		{
			if (e.PageState != null)
			{
				VineUserModel vineUserModel = e.LoadValueOrDefault<VineUserModel>("User");
				User.Username = vineUserModel.Username;
				PhoneNumber = vineUserModel.PhoneNumber;
				Email = vineUserModel.Email;
			}
			((FrameworkElement)UserAvatar).put_DataContext((object)User);
			((Control)EmailBox).Focus((FocusState)2);
			IsFinishedLoading = true;
		}
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		User.Email = Email;
		User.PhoneNumber = PhoneNumber;
		IsError = false;
		e.PageState["User"] = User;
	}

	private void EmailBox_KeyDown(object sender, KeyRoutedEventArgs e)
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
			((Control)PhoneNumberBox).Focus((FocusState)2);
		}
	}

	private void PhoneNumberBox_KeyDown(object sender, KeyRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0008: Invalid comparison between Unknown and I4
		if ((int)e.Key == 13)
		{
			AcceptButton_Click(sender, null);
		}
	}

	private async void AcceptButton_Click(object sender, RoutedEventArgs e)
	{
		if (!_isAcceptInProgress)
		{
			_isAcceptInProgress = true;
			string text = await Accept();
			if (!string.IsNullOrEmpty(text))
			{
				await new MessageDialog(text, ResourceHelper.GetString("vine"))
				{
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok")) }
				}.ShowAsync();
			}
			_isAcceptInProgress = false;
		}
	}

	private async Task<string> Accept()
	{
		string errorMsg = string.Empty;
		if (IsBusy)
		{
			return errorMsg;
		}
		IsBusy = true;
		if (PasswordBox.Password.Length < 6)
		{
			errorMsg = ResourceHelper.GetString("InvalidPassword");
			((Control)PasswordBox).Focus((FocusState)2);
		}
		if (string.IsNullOrWhiteSpace(EmailBox.Text) || !Regex.IsMatch(EmailBox.Text, "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"))
		{
			errorMsg = ResourceHelper.GetString("InvalidEmailAddress");
			((Control)EmailBox).Focus((FocusState)2);
		}
		if (string.IsNullOrEmpty(errorMsg))
		{
			ApiResult<BaseVineResponseModel<VineAuthToken>> apiResult = await Proceed();
			if (apiResult.Model == null)
			{
				errorMsg = ResourceHelper.GetString("UnknownAuthenticationError");
			}
			else if (!string.IsNullOrEmpty(apiResult.Model.Error))
			{
				errorMsg = apiResult.Model.Error;
			}
			else
			{
				ApplicationSettings.Current.VineSession = apiResult.Model.Data;
				if (App.TempNewAvatar != null && (await UserAvatar.CropAndUpload(App.TempNewAvatar)).HasError)
				{
					errorMsg = ResourceHelper.GetString("profile_image_save_error");
				}
				ApiResult<BaseVineResponseModel<VineUserModel>> apiResult2 = await App.Api.GetUserMe();
				if (apiResult2.HasError)
				{
					errorMsg = ResourceHelper.GetString("UnknownAuthenticationError");
				}
				else
				{
					ApplicationSettings.Current.User = apiResult2.Model.Data;
					App.ClearBackStack = true;
					App.RootFrame.Navigate(typeof(FriendFinderView), (object)true);
				}
			}
		}
		IsBusy = false;
		return errorMsg;
	}

	private async Task<ApiResult<BaseVineResponseModel<VineAuthToken>>> Proceed()
	{
		_ = string.Empty;
		ApiResult<BaseVineResponseModel<VineAuthToken>> apiResult = await App.Api.CreateAccount(User.Username, Email, Password, PhoneNumber);
		if (apiResult.HasError && apiResult.Model != null)
		{
			if (string.IsNullOrEmpty(apiResult.Model.Error))
			{
				apiResult.Model.Error = apiResult.GetErrorMessage(ResourceHelper.GetString("UnknownAuthenticationError"));
			}
			else
			{
				string error = apiResult.Model.Error;
				if (error.Contains("email"))
				{
					((Control)EmailBox).Focus((FocusState)2);
				}
				else if (error.Contains("password"))
				{
					((Control)PasswordBox).Focus((FocusState)2);
				}
				else if (error.Contains("phone"))
				{
					((Control)PhoneNumberBox).Focus((FocusState)2);
				}
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
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/SignUpEmailDetailsView.xaml"), (ComponentResourceLocation)0);
			ErrorState = (Grid)((FrameworkElement)this).FindName("ErrorState");
			PhoneNumberBox = (TextBox)((FrameworkElement)this).FindName("PhoneNumberBox");
			PasswordBox = (PasswordBox)((FrameworkElement)this).FindName("PasswordBox");
			EmailBox = (TextBox)((FrameworkElement)this).FindName("EmailBox");
			UserAvatar = (AvatarControl)((FrameworkElement)this).FindName("UserAvatar");
			cmdBarAccept = (CommandBar)((FrameworkElement)this).FindName("cmdBarAccept");
			AcceptButton = (AppBarButton)((FrameworkElement)this).FindName("AcceptButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_003e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0044: Expected O, but got Unknown
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		//IL_006f: Expected O, but got Unknown
		//IL_0075: Unknown result type (might be due to invalid IL or missing references)
		//IL_007b: Expected O, but got Unknown
		//IL_009c: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a6: Expected O, but got Unknown
		//IL_00a9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00af: Expected O, but got Unknown
		//IL_00d0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00da: Expected O, but got Unknown
		//IL_00dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e3: Expected O, but got Unknown
		//IL_0104: Unknown result type (might be due to invalid IL or missing references)
		//IL_010e: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
			((TOSControl)target).ErrorOccurredEvent += HandleLoginError;
			break;
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<KeyEventHandler, EventRegistrationToken>)val2.add_KeyDown, (Action<EventRegistrationToken>)val2.remove_KeyDown, new KeyEventHandler(PhoneNumberBox_KeyDown));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<KeyEventHandler, EventRegistrationToken>)val2.add_KeyDown, (Action<EventRegistrationToken>)val2.remove_KeyDown, new KeyEventHandler(PasswordBox_KeyDown));
			break;
		}
		case 4:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<KeyEventHandler, EventRegistrationToken>)val2.add_KeyDown, (Action<EventRegistrationToken>)val2.remove_KeyDown, new KeyEventHandler(EmailBox_KeyDown));
			break;
		}
		case 5:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(AcceptButton_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
