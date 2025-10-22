using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class SettingsContentView : BasePage, IComponentConnector
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
	private TextBlock DeactivateButton;

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

	public VineToggleButtonState AllowAddressBookButtonState
	{
		get
		{
			if (!ApplicationSettings.Current.IsAddressBookEnabled)
			{
				return VineToggleButtonState.Off;
			}
			return VineToggleButtonState.On;
		}
	}

	public SettingsContentView()
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
			((UIElement)DeactivateButton).put_Visibility((Visibility)1);
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

	private async void AllowContacts_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		ApplicationSettings.Current.IsAddressBookEnabled = !ApplicationSettings.Current.IsAddressBookEnabled;
		NotifyOfPropertyChange(() => AllowAddressBookButtonState);
	}

	private async void Protected_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		MessageDialog val = new MessageDialog("");
		val.put_Content(User.Private ? ResourceHelper.GetString("ContentControlsUnprotectAlertMessage") : ResourceHelper.GetString("ContentControlsProtectAlertMessage"));
		val.put_Title(User.Private ? ResourceHelper.GetString("ContentControlsUnprotectAlertTitle") : ResourceHelper.GetString("ContentControlsProtectAlertTitle"));
		val.Commands.Add((IUICommand)new UICommand(User.Private ? ResourceHelper.GetString("ContentControlsUnprotectAlertConfirm") : ResourceHelper.GetString("ContentControlsProtectAlertConfirm"), (UICommandInvokedHandler)null, (object)0));
		val.Commands.Add((IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1));
		val.put_CancelCommandIndex(1u);
		if ((int)(await val.ShowAsync()).Id == 0)
		{
			User.Private = !User.Private;
			ApiResult<BaseVineResponseModel> obj = await App.Api.PutUser(User.UserId, null, null, null, null, null, null, User.Private);
			obj.PopUpErrorIfExists();
			if (obj.HasError)
			{
				User.Private = !User.Private;
			}
		}
	}

	private async void Sensitive_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		MessageDialog val = new MessageDialog("");
		val.put_Content(User.ExplicitContent ? ResourceHelper.GetString("content_controls_unexplicit_my_posts_message") : ResourceHelper.GetString("content_controls_explicit_my_posts_message"));
		val.put_Title(User.ExplicitContent ? ResourceHelper.GetString("content_controls_unexplicit_my_posts") : ResourceHelper.GetString("content_controls_explicit_my_posts"));
		val.Commands.Add((IUICommand)new UICommand(User.ExplicitContent ? ResourceHelper.GetString("content_controls_unmake_explicit") : ResourceHelper.GetString("content_controls_make_explicit"), (UICommandInvokedHandler)null, (object)0));
		val.Commands.Add((IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1));
		val.put_CancelCommandIndex(1u);
		if ((int)(await val.ShowAsync()).Id == 0)
		{
			User.ExplicitContent = !User.ExplicitContent;
			ApiResult<BaseVineResponseModel> apiResult = ((!User.ExplicitContent) ? (await App.Api.ClearExplicitContent(User.UserId)) : (await App.Api.SetExplicitContent(User.UserId)));
			apiResult.PopUpErrorIfExists();
			if (apiResult.HasError)
			{
				User.ExplicitContent = !User.ExplicitContent;
			}
		}
	}

	private async void Deactivate_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		MessageDialog val = new MessageDialog(ResourceHelper.GetString("settings_deactivate_account_dialog"), ResourceHelper.GetString("settings_deactivate_account_title"))
		{
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("settings_deactivate_account_confirm"), (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
		};
		val.put_CancelCommandIndex(1u);
		if ((int)(await val.ShowAsync()).Id == 0)
		{
			ApiResult obj = await App.Api.DeactivateAccount();
			obj.PopUpErrorIfExists();
			if (!obj.HasError)
			{
				App.SignOut();
			}
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
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/SettingsContentView.xaml"), (ComponentResourceLocation)0);
			ErrorState = (Grid)((FrameworkElement)this).FindName("ErrorState");
			MainPanel = (StackPanel)((FrameworkElement)this).FindName("MainPanel");
			DeactivateButton = (TextBlock)((FrameworkElement)this).FindName("DeactivateButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0026: Unknown result type (might be due to invalid IL or missing references)
		//IL_002c: Expected O, but got Unknown
		//IL_004d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0057: Expected O, but got Unknown
		//IL_005d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_0084: Unknown result type (might be due to invalid IL or missing references)
		//IL_008e: Expected O, but got Unknown
		//IL_0094: Unknown result type (might be due to invalid IL or missing references)
		//IL_009a: Expected O, but got Unknown
		//IL_00bb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c5: Expected O, but got Unknown
		//IL_00cb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		//IL_00f2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fc: Expected O, but got Unknown
		//IL_00ff: Unknown result type (might be due to invalid IL or missing references)
		//IL_0105: Expected O, but got Unknown
		//IL_0126: Unknown result type (might be due to invalid IL or missing references)
		//IL_0130: Expected O, but got Unknown
		//IL_0133: Unknown result type (might be due to invalid IL or missing references)
		//IL_0139: Expected O, but got Unknown
		//IL_015a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0164: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Deactivate_OnTapped));
			break;
		}
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(AllowContacts_OnTapped));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Sensitive_OnTapped));
			break;
		}
		case 4:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Protected_OnTapped));
			break;
		}
		case 5:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ErrorState_Click));
			break;
		}
		case 6:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Signout_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
