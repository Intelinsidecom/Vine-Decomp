using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Tiles;
using Vine.Web;
using Windows.ApplicationModel.Chat;
using Windows.UI.Popups;
using Windows.UI.StartScreen;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class ProfileView : BasePage, IComponentConnector
{
	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ProfileControl Profile;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MusicInformationControl MusicControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton PinUnPinCommandButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton ReportButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton ShareButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public IconElement MuteIcon
	{
		get
		{
			//IL_001c: Unknown result type (might be due to invalid IL or missing references)
			//IL_0022: Expected O, but got Unknown
			//IL_0011: Unknown result type (might be due to invalid IL or missing references)
			//IL_0017: Expected O, but got Unknown
			if (ApplicationSettings.Current.IsVolumeMuted)
			{
				return (IconElement)new SymbolIcon((Symbol)57693);
			}
			return (IconElement)new SymbolIcon((Symbol)57752);
		}
	}

	public string MuteLabel
	{
		get
		{
			if (!ApplicationSettings.Current.IsVolumeMuted)
			{
				return ResourceHelper.GetString("Mute");
			}
			return ResourceHelper.GetString("Unmute");
		}
	}

	public bool AppBarButtonVisibility
	{
		get
		{
			if (Profile.User != null)
			{
				return Profile.UserId != ApplicationSettings.Current.UserId;
			}
			return false;
		}
	}

	private bool TileExists => SecondaryTile.Exists(TileId);

	private string TileId => string.Concat(SecondaryTileType.PersonResult, "=", Profile.UserId);

	public string BlockedText
	{
		get
		{
			if (Profile.User == null)
			{
				return "";
			}
			if (!Profile.User.Blocked)
			{
				return ResourceHelper.GetString("block_person");
			}
			return ResourceHelper.GetString("unblock_person");
		}
	}

	public ProfileView()
	{
		InitializeComponent();
		ReportButton.put_Label(ResourceHelper.GetString("Report"));
		ShareButton.put_Label(ResourceHelper.GetString("ShareProfile"));
		Profile.List.MusicControl = MusicControl;
	}

	private void MuteVolume_Click(object sender, RoutedEventArgs e)
	{
		ApplicationSettings.Current.IsVolumeMuted = !ApplicationSettings.Current.IsVolumeMuted;
		NotifyOfPropertyChange(() => MuteIcon);
		NotifyOfPropertyChange(() => MuteLabel);
		Profile.List.NotifyMuteChange();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.None, "profile"));
		if (Profile.IsFinishedLoading)
		{
			await Profile.OnActivate();
			return;
		}
		Profile.UserId = e.NavigationParameter as string;
		if (e.PageState != null)
		{
			Profile.List.Items.CurrentPage = (int)e.LoadValueOrDefault<long>("Profile.List.Items.CurrentPage");
			Profile.List.PageStateItems = e.LoadValueOrDefault<List<VineModel>>("Profile.List.Items");
			Profile.List.PageStateScrollOffset = e.LoadValueOrDefault<double>("Profile.List.ScrollOffset");
			Profile.List.CurrentTab = (VineListControl.Tab)e.LoadValueOrDefault<long>("Profile.List.CurrentTab");
			Profile.User = e.LoadValueOrDefault<VineUserModel>("Profile.User");
			Profile.VisibleSuggestedToFollow.Repopulate(e.LoadValueOrDefault<List<VineUserModel>>("Profile.Suggested.Visible") ?? new List<VineUserModel>());
			Profile._suggestedToFollow = e.LoadValueOrDefault<List<VineUserModel>>("Profile.Suggested.Hidden") ?? new List<VineUserModel>();
			Profile.ShowSuggestions = e.LoadValueOrDefault<bool>("Profile.Suggested.Show");
			Profile.IsSuggestedLoaded = Profile.VisibleSuggestedToFollow.Any();
		}
		UpdateAppBar();
		await Profile.OnActivate();
		NotifyOfPropertyChange(() => AppBarButtonVisibility);
		NotifyOfPropertyChange(() => BlockedText);
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		Profile.OnDeactivate();
		e.PageState["Profile.List.Items.CurrentPage"] = (long)Profile.List.Items.CurrentPage;
		e.PageState["Profile.List.Items"] = Profile.List.Items.Select((VineViewModel x) => x.Model).ToList();
		e.PageState["Profile.List.ScrollOffset"] = Profile.List.ScrollOffset;
		e.PageState["Profile.List.CurrentTab"] = (long)Profile.List.CurrentTab;
		e.PageState["Profile.User"] = Profile.User;
		e.PageState["Profile.Suggested.Visible"] = Profile.VisibleSuggestedToFollow.ToList();
		e.PageState["Profile.Suggested.Hidden"] = Profile._suggestedToFollow;
		e.PageState["Profile.Suggested.Show"] = Profile.ShowSuggestions;
	}

	private void UpdateAppBar()
	{
		//IL_0054: Unknown result type (might be due to invalid IL or missing references)
		//IL_005e: Expected O, but got Unknown
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_0032: Expected O, but got Unknown
		if (!TileExists)
		{
			PinUnPinCommandButton.put_Label(ResourceHelper.GetString("Pin"));
			PinUnPinCommandButton.put_Icon((IconElement)new SymbolIcon((Symbol)57665));
		}
		else
		{
			PinUnPinCommandButton.put_Label(ResourceHelper.GetString("Unpin"));
			PinUnPinCommandButton.put_Icon((IconElement)new SymbolIcon((Symbol)57750));
		}
		((UIElement)PinUnPinCommandButton).UpdateLayout();
	}

	private async void PinUnPinCommandButton_OnClick(object sender, RoutedEventArgs e)
	{
		if (TileExists)
		{
			await TileHelper.DeleteSecondaryTile(TileId);
		}
		else
		{
			await TileHelper.CreateProfileTile(Profile.UserId, Profile.User.Username, TileId);
		}
		UpdateAppBar();
	}

	private async void BlockPersonCommandButton_OnClick(object sender, RoutedEventArgs e)
	{
		if (!Profile.User.Blocked)
		{
			MessageDialog val = new MessageDialog(ResourceHelper.GetString("BlockUserConfirm"), ResourceHelper.GetString("vine"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Yes"), (UICommandInvokedHandler)null, (object)0) },
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("No"), (UICommandInvokedHandler)null, (object)1) }
			};
			val.put_CancelCommandIndex(1u);
			if ((int)(await val.ShowAsync()).Id != 0)
			{
				return;
			}
			Profile.User.Blocked = !Profile.User.Blocked;
			NotifyOfPropertyChange(() => BlockedText);
			if ((await App.Api.BlockUser(ApplicationSettings.Current.UserId, Profile.UserId)).HasError)
			{
				Profile.User.Blocked = !Profile.User.Blocked;
				NotifyOfPropertyChange(() => BlockedText);
				await new MessageDialog(ResourceHelper.GetString("UserBlockFailed"), ResourceHelper.GetString("vine"))
				{
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok"), (UICommandInvokedHandler)null, (object)0) }
				}.ShowAsync();
			}
			else
			{
				await new MessageDialog(ResourceHelper.GetString("UserBlocked"), ResourceHelper.GetString("vine"))
				{
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok"), (UICommandInvokedHandler)null, (object)0) }
				}.ShowAsync();
			}
			return;
		}
		Profile.User.Blocked = !Profile.User.Blocked;
		NotifyOfPropertyChange(() => BlockedText);
		if ((await App.Api.UnblockUser(ApplicationSettings.Current.UserId, Profile.UserId)).HasError)
		{
			Profile.User.Blocked = !Profile.User.Blocked;
			NotifyOfPropertyChange(() => BlockedText);
			await new MessageDialog(ResourceHelper.GetString("UserBlockFailed"), ResourceHelper.GetString("vine"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok"), (UICommandInvokedHandler)null, (object)0) }
			}.ShowAsync();
		}
		else
		{
			await new MessageDialog(ResourceHelper.GetString("UserUnblocked"), ResourceHelper.GetString("vine"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok"), (UICommandInvokedHandler)null, (object)0) }
			}.ShowAsync();
		}
	}

	private async void ReportPersonCommandButton_OnClick(object sender, RoutedEventArgs e)
	{
		MessageDialog val = new MessageDialog(ResourceHelper.GetString("ReportUser"), ResourceHelper.GetString("vine"))
		{
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Yes"), (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("No"), (UICommandInvokedHandler)null, (object)1) }
		};
		val.put_CancelCommandIndex(1u);
		if ((int)(await val.ShowAsync()).Id == 0)
		{
			ApiResult<BaseVineResponseModel> apiResult = await App.Api.ReportUser(Profile.UserId);
			await new MessageDialog((!apiResult.HasError && apiResult.Model.Success) ? ResourceHelper.GetString("UserReported") : ResourceHelper.GetString("UserReportedFailed"), ResourceHelper.GetString("vine"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok"), (UICommandInvokedHandler)null, (object)0) }
			}.ShowAsync();
		}
	}

	private async void ShareButton_OnClick(object sender, RoutedEventArgs e)
	{
		string text = string.Format(ResourceHelper.GetString("share_profile_txt"), new object[2]
		{
			Profile.User.Username,
			Profile.User.UserId
		});
		ChatMessage val = new ChatMessage();
		val.put_Body(text);
		await ChatMessageManager.ShowComposeSmsMessageAsync(val);
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Expected O, but got Unknown
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/ProfileView.xaml"), (ComponentResourceLocation)0);
			Profile = (ProfileControl)((FrameworkElement)this).FindName("Profile");
			MusicControl = (MusicInformationControl)((FrameworkElement)this).FindName("MusicControl");
			PinUnPinCommandButton = (AppBarButton)((FrameworkElement)this).FindName("PinUnPinCommandButton");
			ReportButton = (AppBarButton)((FrameworkElement)this).FindName("ReportButton");
			ShareButton = (AppBarButton)((FrameworkElement)this).FindName("ShareButton");
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
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(MuteVolume_Click));
			break;
		}
		case 2:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(PinUnPinCommandButton_OnClick));
			break;
		}
		case 3:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(BlockPersonCommandButton_OnClick));
			break;
		}
		case 4:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ReportPersonCommandButton_OnClick));
			break;
		}
		case 5:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ShareButton_OnClick));
			break;
		}
		}
		_contentLoaded = true;
	}
}
