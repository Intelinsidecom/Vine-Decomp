using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class SettingsPrivacyView : BasePage, IComponentConnector
{
	private VineUserModel _user;

	private bool _isError;

	private string _errorText;

	private bool _showRetry;

	private bool _isLoaded;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid ErrorState;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private StackPanel MainPanel;

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

	public bool IsLoaded
	{
		get
		{
			return _isLoaded;
		}
		set
		{
			SetProperty(ref _isLoaded, value, "IsLoaded");
		}
	}

	public SettingsPrivacyView()
	{
		InitializeComponent();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		IsLoaded = false;
		ApiResult<BaseVineResponseModel<VineUserModel>> apiResult = await App.Api.GetUserMe();
		if (!apiResult.HasError)
		{
			User = apiResult.Model.Data;
			ApplicationSettings.Current.User = User;
		}
		else
		{
			IsError = true;
			((UIElement)MainPanel).put_Visibility((Visibility)1);
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
		IsLoaded = true;
	}

	private void Signout_Click(object sender, RoutedEventArgs e)
	{
		App.SignOut();
	}

	private void ErrorState_Click(object sender, RoutedEventArgs e)
	{
		LoadState();
	}

	private async void Phone_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		bool origValue = User.HiddenPhoneNumber;
		User.HiddenPhoneNumber = !origValue;
		ApiResult<BaseVineResponseModel> obj = await App.Api.PutUserPreferences(User.UserId, null, User.HiddenPhoneNumber);
		obj.PopUpErrorIfExists();
		if (obj.HasError)
		{
			User.HiddenPhoneNumber = origValue;
		}
	}

	private async void Email_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		bool origValue = User.HiddenEmail;
		User.HiddenEmail = !origValue;
		ApiResult<BaseVineResponseModel> obj = await App.Api.PutUserPreferences(User.UserId, null, null, User.HiddenEmail);
		obj.PopUpErrorIfExists();
		if (obj.HasError)
		{
			User.HiddenEmail = origValue;
		}
	}

	private async void Twitter_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		bool origValue = User.HiddenTwitter;
		User.HiddenTwitter = !origValue;
		ApiResult<BaseVineResponseModel> obj = await App.Api.PutUserPreferences(User.UserId, User.HiddenTwitter);
		obj.PopUpErrorIfExists();
		if (obj.HasError)
		{
			User.HiddenTwitter = origValue;
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/SettingsPrivacyView.xaml"), (ComponentResourceLocation)0);
			ErrorState = (Grid)((FrameworkElement)this).FindName("ErrorState");
			MainPanel = (StackPanel)((FrameworkElement)this).FindName("MainPanel");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0022: Unknown result type (might be due to invalid IL or missing references)
		//IL_0028: Expected O, but got Unknown
		//IL_0049: Unknown result type (might be due to invalid IL or missing references)
		//IL_0053: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_005f: Expected O, but got Unknown
		//IL_0080: Unknown result type (might be due to invalid IL or missing references)
		//IL_008a: Expected O, but got Unknown
		//IL_0090: Unknown result type (might be due to invalid IL or missing references)
		//IL_0096: Expected O, but got Unknown
		//IL_00b7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c1: Expected O, but got Unknown
		//IL_00c4: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ca: Expected O, but got Unknown
		//IL_00eb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f5: Expected O, but got Unknown
		//IL_00f8: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fe: Expected O, but got Unknown
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0129: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Twitter_OnTapped));
			break;
		}
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Email_OnTapped));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Phone_OnTapped));
			break;
		}
		case 4:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ErrorState_Click));
			break;
		}
		case 5:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Signout_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
