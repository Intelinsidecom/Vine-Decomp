using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading;
using System.Threading.Tasks;
using Vine.Background;
using Vine.Common;
using Vine.Events;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Views.Capture;
using Vine.Web;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

namespace Vine.Views;

public sealed class HomeView : BasePage, IEventHandler<SigningOut>, IEventHandler<ProfileChanged>, IEventHandler<MuteSettingChanged>, IEventHandler<ConversationDeleted>, IEventHandler<ConversationAdded>, IEventHandler<SearchPinChanged>, IEventHandler<IgnoreNavigationParameter>, IComponentConnector
{
	public const int HomeVinePivotIndex = 0;

	public const int DiscoverPivotIndex = 1;

	public const int NotificationPivotIndex = 2;

	public const int MePivotIndex = 3;

	public const int VMsPivotIndex = 4;

	private bool _pinSearchVisible;

	private Brush _homeIconFill;

	private Brush _notificationIconFill;

	private Brush _discoverIconFill;

	private Brush _meIconFill;

	private Brush _vmsIconFillBrush;

	private CancellationTokenSource debounce;

	private bool _tutorialWelcomeVisibility;

	private bool _isRedOn;

	private bool _isOrangeOn;

	private bool _isYellowOn;

	private bool _isGreenOn;

	private bool _isTealOn;

	private bool _isBlueLightOn;

	private bool _isBlueDarkOn;

	private bool _isPurpleOn;

	private bool _isPinkOn;

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
	private Grid LayoutRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VisualStateGroup VisualStateGroup;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VisualState Expanded;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VisualState Mini;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Pivot PivotRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	public Grid grid;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MusicInformationControl MusicControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid TutorialColorGrid1;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid TutorialColorGrid2;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button TutorialButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Border GrayHardPlaceholder;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VineMessagesInbox VMs;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ProfileControl Profile;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private InteractionsControl Interactions;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ExploreControl Explore;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VineListControl HomeVineList;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private CommandBar AppBar;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton NewMessageButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton SettingsButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton PinUnPinCommandButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public bool PinSearchVisible
	{
		get
		{
			return _pinSearchVisible;
		}
		set
		{
			SetProperty(ref _pinSearchVisible, value, "PinSearchVisible");
		}
	}

	public Brush HomeIconFill
	{
		get
		{
			return _homeIconFill;
		}
		set
		{
			SetProperty(ref _homeIconFill, value, "HomeIconFill");
		}
	}

	public Brush NotificationIconFill
	{
		get
		{
			return _notificationIconFill;
		}
		set
		{
			SetProperty(ref _notificationIconFill, value, "NotificationIconFill");
		}
	}

	public Brush DiscoverIconFill
	{
		get
		{
			return _discoverIconFill;
		}
		set
		{
			SetProperty(ref _discoverIconFill, value, "DiscoverIconFill");
		}
	}

	public Brush MeIconFill
	{
		get
		{
			return _meIconFill;
		}
		set
		{
			SetProperty(ref _meIconFill, value, "MeIconFill");
		}
	}

	public Brush VMsIconFill
	{
		get
		{
			return _vmsIconFillBrush;
		}
		set
		{
			SetProperty(ref _vmsIconFillBrush, value, "VMsIconFill");
		}
	}

	public string VideoAppBarIcon
	{
		get
		{
			if (PivotRoot.SelectedIndex == 4)
			{
				return "M13.0274,23.0003 C12.5155,22.9933 12.001,23.1815 11.605,23.566 C10.813,24.337 10.796,25.603 11.566,26.394 C13.828,28.72 16.823,30 20,30 C23.177,30 26.172,28.72 28.434,26.394 C29.204,25.603 29.186,24.337 28.395,23.566 C27.602,22.797 26.336,22.813 25.566,23.606 C24.064,25.149 22.087,26 20,26 C17.913,26 15.936,25.149 14.434,23.606 C14.0485,23.2095 13.5392,23.0073 13.0274,23.0003 z M27,14 C25.344,14 24,15.344 24,17 C24,18.656 25.344,20 27,20 C28.656,20 30,18.656 30,17 C30,15.344 28.656,14 27,14 z M13,14 C11.344,14 10,15.344 10,17 C10,18.656 11.344,20 13,20 C14.656,20 16,18.656 16,17 C16,15.344 14.656,14 13,14 z M64,7E-06 C64.516,7E-06 65.035,0.100007 65.531,0.305007 C67.025,0.924007 68,2.38201 68,4.00001 L68,36 C68,37.618 67.025,39.076 65.531,39.695 C65.035,39.9 64.516,40 64,40 C62.961,40 61.937,39.594 61.172,38.828 L45.172,22.828 C43.609,21.267 43.609,18.733 45.172,17.172 L61.172,1.17201 C61.937,0.406007 62.961,7E-06 64,7E-06 z M8,0 L32,0 C36.4,0 40,3.6 40,8 L40,32 C40,36.4 36.4,40 32,40 L8,40 C3.6,40 0,36.4 0,32 L0,8 C0,3.6 3.6,0 8,0 z";
			}
			return "M60,40c-1.023,0-2.048-0.392-2.829-1.173L41.903,22.828 c-1.562-1.563-1.562-4.095,0-5.657l15.268-16C57.952,0.391,58.976,0,60,0c2.209,0,4,1.791,4,4v32C64,38.209,62.209,40,60,40z M30,40H8c-4.4,0-8-3.601-8-8.001V8c0-4.401,3.6-8,8-8h22c4.4,0,8,3.599,8,8v23.999C38,36.399,34.4,40,30,40z M19,14c-3.312,0-6,2.687-6,6C13,23.313,15.688,26,19,26s6-2.687,6-6.001C25,16.686,22.312,14,19,14z";
		}
	}

	public Brush AppBarBrush
	{
		get
		{
			//IL_0032: Unknown result type (might be due to invalid IL or missing references)
			//IL_0038: Expected O, but got Unknown
			if (PivotRoot.SelectedIndex == 4)
			{
				return (Brush)(object)ApplicationSettings.Current.User.ProfileBgBrush;
			}
			return (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];
		}
	}

	public UserControlWrapper ControlWrapper { get; set; }

	public UploadJobsViewModel UploadJobs => UploadJobsViewModel.Current;

	private DateTime LastUpdatedInteractions { get; set; }

	public VineUserModel User => ApplicationSettings.Current.User;

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

	public bool TutorialWelcomeVisibility
	{
		get
		{
			return _tutorialWelcomeVisibility;
		}
		set
		{
			_tutorialWelcomeVisibility = value;
			OnPropertyChanged("TutorialWelcomeVisibility");
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

	private bool _ignoreNavigationParam { get; set; }

	public HomeView()
	{
		InitializeComponent();
		_homeIconFill = GrayHardPlaceholder.Background;
		_notificationIconFill = GrayHardPlaceholder.Background;
		_discoverIconFill = GrayHardPlaceholder.Background;
		_meIconFill = GrayHardPlaceholder.Background;
		Profile.List.ScrollingDirectionChanged += OnScrollingDirectionChanged;
		base.AlwaysClearBackStack = true;
		ControlWrapper = new UserControlWrapper
		{
			Model = this
		};
		Profile.List.MusicControl = MusicControl;
		HomeVineList.MusicControl = MusicControl;
		Explore.Search.VineList.MusicControl = MusicControl;
		EventAggregator.Current.Subscribe(this);
		NewMessageButton.put_Label(ResourceHelper.GetString("dm_new_message"));
		SettingsButton.put_Label(ResourceHelper.GetString("dialog_options_settings"));
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		((UIElement)AppBar).put_Visibility((Visibility)1);
		int num = -1;
		bool scrollToTop = App.HomeScrollToTop;
		App.HomeScrollToTop = false;
		ExploreControl.ExploreViewParams exploreParams = NavigationObject as ExploreControl.ExploreViewParams;
		SearchView.SearchViewParams searchParams = NavigationObject as SearchView.SearchViewParams;
		if ((searchParams != null || exploreParams != null) && !_ignoreNavigationParam)
		{
			Explore.Search.Reset();
			ClearNavigationParam();
			num = 1;
		}
		else if (App.HomePivotIndex > -1)
		{
			num = App.HomePivotIndex;
			App.HomePivotIndex = -1;
		}
		else if (e.PageState != null)
		{
			num = (int)e.LoadValueOrDefault<long>("PivotRoot.SelectedIndex");
			if (!HomeVineList.IsFinishedLoading)
			{
				HomeVineList.Items.CurrentPage = (int)e.LoadValueOrDefault<long>("HomeVineList.Items.CurrentPage");
				HomeVineList.PageStateItems = e.LoadValueOrDefault<List<VineModel>>("HomeVineList.Items");
				HomeVineList.PageStateScrollOffset = e.LoadValueOrDefault<double>("HomeVineList.ScrollOffset");
			}
			if (!Profile.List.IsFinishedLoading)
			{
				Profile.List.Items.CurrentPage = (int)e.LoadValueOrDefault<long>("Profile.List.Items.CurrentPage");
				Profile.List.PageStateItems = e.LoadValueOrDefault<List<VineModel>>("Profile.List.Items");
				Profile.List.PageStateScrollOffset = e.LoadValueOrDefault<double>("Profile.List.ScrollOffset");
			}
			Explore.SearchActive = e.LoadValueOrDefault<bool>("Explore.SearchActive");
			if (Explore.SearchActive)
			{
				long? num2 = e.LoadValueOrDefault<long?>("Explore.Search.PivotRoot.SelectedIndex", null);
				if (num2.HasValue)
				{
					string text = e.LoadValueOrDefault("Explore.Search.LastPplRequestedSearch", string.Empty);
					string text2 = e.LoadValueOrDefault("Explore.Search.LastTagsRequestedSearch", string.Empty);
					Explore.Search.SearchText = ((num2 == 0) ? text : text2);
					Explore.Search.LastSearchText = Explore.Search.SearchText;
				}
				else
				{
					Explore.Search.SearchText = e.LoadValueOrDefault("Explore.Search.SearchText", string.Empty);
					Explore.Search.SearchResults.Repopulate(e.LoadValueOrDefault<List<SearchResultModel>>("Explore.Search.Items") ?? new List<SearchResultModel>());
					Explore.Search.LastSearchText = e.LoadValueOrDefault<string>("Explore.Search.LastRequestedSearch");
					Explore.Search.VineList.PageStateItems = e.LoadValueOrDefault<List<VineModel>>("Explore.Search.Vines");
					Explore.Search.VineList.PageStateScrollOffset = e.LoadValueOrDefault<double>("Explore.Search.VerticalOffset");
				}
			}
		}
		if (string.IsNullOrEmpty(ApplicationSettings.Current.OSVersion))
		{
			OSVersionHelper.GetOSVersion((Panel)(object)LayoutRoot, delegate(string osVersion)
			{
				ApplicationSettings.Current.OSVersion = osVersion;
			});
		}
		if (PivotRoot.SelectedIndex != num && num > -1)
		{
			WindowsRuntimeMarshal.RemoveEventHandler<SelectionChangedEventHandler>((Action<EventRegistrationToken>)PivotRoot.remove_SelectionChanged, new SelectionChangedEventHandler(PivotRoot_OnSelectionChanged));
			PivotRoot.put_SelectedIndex(num);
			Pivot pivotRoot = PivotRoot;
			WindowsRuntimeMarshal.AddEventHandler((Func<SelectionChangedEventHandler, EventRegistrationToken>)pivotRoot.add_SelectionChanged, (Action<EventRegistrationToken>)pivotRoot.remove_SelectionChanged, new SelectionChangedEventHandler(PivotRoot_OnSelectionChanged));
		}
		await OnActivate(PivotRoot.SelectedIndex);
		if (scrollToTop && PivotRoot.SelectedIndex == 0)
		{
			HomeVineList.ScrollToTop();
		}
		if (PivotRoot.SelectedIndex == 1 && !_ignoreNavigationParam)
		{
			if (searchParams != null)
			{
				Explore.SearchActive = true;
				await Explore.Search.OnActivate(searchParams);
			}
			else if (exploreParams != null)
			{
				Explore.DeactivateSearch();
			}
		}
		if (new TimeSpan(DateTime.UtcNow.Ticks - ApplicationSettings.Current.LastUpdatedTile.Ticks).TotalMinutes > 10.0)
		{
			BgLiveTiles.UpdateTiles();
			ApplicationSettings.Current.LastUpdatedTile = DateTime.UtcNow;
		}
		if (new TimeSpan(DateTime.UtcNow.Ticks - LastUpdatedInteractions.Ticks).TotalMinutes > 1.0)
		{
			ApiResult<BaseVineResponseModel<VineMessageActivityCounts>> apiResult = await App.Api.GetActivityCounts(ApplicationSettings.Current.UserId);
			if (!apiResult.HasError && apiResult.Model != null)
			{
				Interactions.SetNewCount(apiResult.Model.Data.Notifications);
				VMs.SetNewCount(apiResult.Model.Data.Messages);
				if (apiResult.Model.Data.Notifications == 0)
				{
					ToastHelper.Delete("notifications");
				}
				if (apiResult.Model.Data.Messages == 0)
				{
					ToastHelper.Delete("messages");
				}
				LastUpdatedInteractions = DateTime.UtcNow;
			}
		}
		if (_ignoreNavigationParam)
		{
			_ignoreNavigationParam = false;
		}
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		DeactiveAllExcept(-1);
		e.PageState["HomeVineList.Items.CurrentPage"] = (long)HomeVineList.Items.CurrentPage;
		e.PageState["Profile.List.Items.CurrentPage"] = (long)Profile.List.Items.CurrentPage;
		e.PageState["HomeVineList.Items"] = HomeVineList.Items.Select((VineViewModel x) => x.Model).ToList();
		e.PageState["Profile.List.Items"] = Profile.List.Items.Select((VineViewModel x) => x.Model).ToList();
		e.PageState["HomeVineList.ScrollOffset"] = HomeVineList.ScrollOffset;
		e.PageState["Profile.List.ScrollOffset"] = Profile.List.ScrollOffset;
		e.PageState["PivotRoot.SelectedIndex"] = (long)PivotRoot.SelectedIndex;
		e.PageState["Explore.SearchActive"] = Explore.SearchActive;
		if (Explore.SearchActive)
		{
			e.PageState["Explore.Search.SearchText"] = Explore.Search.SearchText;
			e.PageState["Explore.Search.Items"] = Explore.Search.SearchResults.ToList();
			e.PageState["Explore.Search.LastRequestedSearch"] = Explore.Search.LastSearchText;
			e.PageState["Explore.Search.VerticalOffset"] = Explore.Search.VineList.ScrollOffset;
			e.PageState["Explore.Search.Vines"] = Explore.Search.VineList.Items.Select((VineViewModel x) => x.Model).ToList();
		}
	}

	private async void PivotRoot_OnSelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		if (e.RemovedItems.Count > 0)
		{
			DeactiveAllExcept(PivotRoot.SelectedIndex);
			await OnActivate(PivotRoot.SelectedIndex);
		}
	}

	private async Task OnActivate(int index)
	{
		HomeVineList.ScrollingDirection = (PanelScrollingDirection)2;
		Profile.List.ScrollingDirection = (PanelScrollingDirection)2;
		OnScrollingDirectionChanged(this, (PanelScrollingDirection)2);
		HomeIconFill = (Brush)((PivotRoot.SelectedIndex == 0) ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"] : GrayHardPlaceholder.Background);
		DiscoverIconFill = (Brush)((PivotRoot.SelectedIndex == 1) ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"] : GrayHardPlaceholder.Background);
		NotificationIconFill = (Brush)((PivotRoot.SelectedIndex == 2) ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"] : GrayHardPlaceholder.Background);
		MeIconFill = (Brush)((PivotRoot.SelectedIndex == 3) ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"] : GrayHardPlaceholder.Background);
		VMsIconFill = (Brush)((PivotRoot.SelectedIndex == 4) ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"] : GrayHardPlaceholder.Background);
		NotifyOfPropertyChange(() => AppBarBrush);
		NotifyOfPropertyChange(() => VideoAppBarIcon);
		switch (index)
		{
		case 0:
			App.ScribeService.Log(new ViewImpressionEvent(Section.Home, "timeline"));
			await HomeVineList.OnActivate();
			break;
		case 1:
			await Explore.OnActivate();
			break;
		case 2:
			App.ScribeService.Log(new ViewImpressionEvent(Section.Activity, "timeline"));
			await Interactions.OnActivate();
			break;
		case 3:
			App.ScribeService.Log(new ViewImpressionEvent(Section.MyProfile, "profile"));
			await Profile.OnActivate();
			break;
		case 4:
			if (!ApplicationSettings.Current.HasSeenVMWelcomeTutorial)
			{
				SetCurrentColor(User.ProfileBackground);
				TutorialWelcomeVisibility = true;
			}
			App.ScribeService.Log(new ViewImpressionEvent(Section.VM, "vm"));
			await VMs.OnActivate();
			break;
		}
		DeactiveAllExcept(PivotRoot.SelectedIndex);
	}

	private void DeactiveAllExcept(int index)
	{
		if (index != 0)
		{
			HomeVineList.OnDeactivate();
		}
		if (index != 1)
		{
			Explore.OnDeactivate();
		}
		if (index != 2)
		{
			Interactions.OnDeactivate();
		}
		if (index != 3)
		{
			Profile.OnDeactivate();
		}
		if (index != 4)
		{
			VMs.OnDeactivate();
		}
	}

	private void Home_Tap(object sender, TappedRoutedEventArgs e)
	{
		if (PivotRoot.SelectedIndex == 0)
		{
			HomeVineList.ScrollToTop();
		}
		else
		{
			PivotRoot.put_SelectedIndex(0);
		}
	}

	private void Discover_Tap(object sender, TappedRoutedEventArgs e)
	{
		PivotRoot.put_SelectedIndex(1);
	}

	private void Notifications_Tap(object sender, TappedRoutedEventArgs e)
	{
		if (PivotRoot.SelectedIndex == 2)
		{
			Interactions.ScrollToTop();
		}
		else
		{
			PivotRoot.put_SelectedIndex(2);
		}
	}

	private void VM_Tap(object sender, TappedRoutedEventArgs e)
	{
		if (PivotRoot.SelectedIndex == 4)
		{
			VMs.ScrollToTop();
		}
		else
		{
			PivotRoot.put_SelectedIndex(4);
		}
	}

	private void Me_Tap(object sender, TappedRoutedEventArgs e)
	{
		if (PivotRoot.SelectedIndex == 3)
		{
			Profile.ScrollToTop();
		}
		else
		{
			PivotRoot.put_SelectedIndex(3);
		}
	}

	private void Camera_Click(object sender, RoutedEventArgs e)
	{
		if (PivotRoot.SelectedIndex == 4)
		{
			ApplicationSettings.Current.HasSeenVMCreateHint = true;
		}
		else if (PivotRoot.SelectedIndex == 3)
		{
			ApplicationSettings.Current.HasSeenEmptyPostsHint = true;
		}
		App.RootFrame.Navigate(CaptureViewHelper.GetCaptureView(), (object)((PivotRoot.SelectedIndex == 4) ? "IsMessageTabDefault" : null));
	}

	private void FriendFinder_Click(object sender, RoutedEventArgs e)
	{
		App.RootFrame.Navigate(typeof(FriendFinderView));
	}

	private async void Settings_Click(object sender, RoutedEventArgs e)
	{
		await Task.Delay(100);
		((Page)this).Frame.Navigate(typeof(SettingsView));
	}

	private async void NewMessage_Click(object sender, RoutedEventArgs e)
	{
		await Task.Delay(100);
		App.RootFrame.NavigateWithObject(typeof(ShareMessageView), new ShareViewParameters
		{
			IsSingleSelect = true
		});
	}

	public void Handle(SigningOut e)
	{
		HomeVineList.Clear();
		Profile.Clear();
		Interactions.Clear();
		VMs.Clear();
	}

	private async void OnScrollingDirectionChanged(object sender, PanelScrollingDirection direction)
	{
		//IL_000a: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		if (debounce != null)
		{
			debounce.Cancel();
		}
		if ((int)direction != 1)
		{
			if ((int)direction == 2)
			{
				((UIElement)grid).put_Opacity(1.0);
				VisualStateManager.GoToState((Control)(object)this, "Expanded", true);
			}
			return;
		}
		debounce = new CancellationTokenSource();
		await Task.Run(async delegate
		{
			await Task.Delay(150);
			if (debounce == null || debounce.IsCancellationRequested)
			{
				debounce = null;
			}
			else
			{
				await DispatcherEx.BeginInvoke(delegate
				{
					VisualStateManager.GoToState((Control)(object)this, "Mini", true);
				});
				debounce = null;
			}
		});
	}

	private void Explore_OnSwipEvent(object sender, SwipeEvent e)
	{
		if (e == SwipeEvent.SwipeLeft)
		{
			PivotRoot.put_SelectedIndex(PivotRoot.SelectedIndex + 1);
		}
		else
		{
			PivotRoot.put_SelectedIndex(PivotRoot.SelectedIndex - 1);
		}
	}

	private void MuteVolume_Click(object sender, RoutedEventArgs e)
	{
		ApplicationSettings.Current.IsVolumeMuted = !ApplicationSettings.Current.IsVolumeMuted;
	}

	private async void PinUnPinCommandButton_OnClick(object sender, RoutedEventArgs e)
	{
		await Explore.Search.PinUnPinCommandButton_OnClick(this, e);
		UpdateSearchPinAppBar();
	}

	private void UpdateSearchPinAppBar()
	{
		PinUnPinCommandButton.put_Label(Explore.Search.HasTile ? ResourceHelper.GetString("Unpin") : ResourceHelper.GetString("Pin"));
	}

	public void Handle(SearchPinChanged e)
	{
		if (Explore.SearchActive && PivotRoot.SelectedIndex == 1)
		{
			PinSearchVisible = true;
			UpdateSearchPinAppBar();
		}
		else
		{
			PinSearchVisible = false;
		}
	}

	public void Handle(MuteSettingChanged e)
	{
		UpdateMuteAppbar();
	}

	public void Handle(ProfileChanged e)
	{
		Profile.User = e.Profile;
	}

	public void Handle(ConversationDeleted e)
	{
		VMs.DeleteConversation(e);
	}

	public void Handle(ConversationAdded e)
	{
		VMs.AddConversation(e);
	}

	private void UpdateMuteAppbar()
	{
		NotifyOfPropertyChange(() => MuteIcon);
		NotifyOfPropertyChange(() => MuteLabel);
		HomeVineList.NotifyMuteChange();
		Profile.List.NotifyMuteChange();
	}

	private void AppBar_OnOpened(object sender, object e)
	{
		((Control)AppBar).put_Background(AppBarBrush);
	}

	private void AppBar_OnClosed(object sender, object e)
	{
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Expected O, but got Unknown
		((Control)AppBar).put_Background((Brush)new SolidColorBrush(Colors.Transparent));
		((UIElement)AppBar).put_Visibility((Visibility)1);
	}

	private void AppBarMore_PointerEntered(object sender, PointerRoutedEventArgs e)
	{
		((UIElement)AppBar).put_Visibility((Visibility)0);
		((AppBar)AppBar).put_IsOpen(true);
	}

	private void UploadJob_Tapped(object sender, TappedRoutedEventArgs e)
	{
		App.RootFrame.Navigate(typeof(UploadJobsView));
	}

	public void TutorialButton_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		ApplicationSettings.Current.HasSeenVMWelcomeTutorial = true;
		TutorialWelcomeVisibility = false;
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

	public void Handle(IgnoreNavigationParameter e)
	{
		_ignoreNavigationParam = true;
	}

	protected override void OnNavigatedFrom(NavigationEventArgs e)
	{
		string text = e.Parameter as string;
		if (!string.IsNullOrWhiteSpace(text) && text.StartsWith("{"))
		{
			Type type = Serialization.DeserializeType(text).GetType();
			if ((object)type == typeof(SearchView.SearchViewParams) || (object)type == typeof(ExploreControl.ExploreViewParams))
			{
				ClearNavigationParam();
			}
		}
		base.OnNavigatedFrom(e);
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
		//IL_00dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e7: Expected O, but got Unknown
		//IL_00f3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fd: Expected O, but got Unknown
		//IL_0109: Unknown result type (might be due to invalid IL or missing references)
		//IL_0113: Expected O, but got Unknown
		//IL_018d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0197: Expected O, but got Unknown
		//IL_01a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ad: Expected O, but got Unknown
		//IL_01b9: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c3: Expected O, but got Unknown
		//IL_01cf: Unknown result type (might be due to invalid IL or missing references)
		//IL_01d9: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/HomeView.xaml"), (ComponentResourceLocation)0);
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			VisualStateGroup = (VisualStateGroup)((FrameworkElement)this).FindName("VisualStateGroup");
			Expanded = (VisualState)((FrameworkElement)this).FindName("Expanded");
			Mini = (VisualState)((FrameworkElement)this).FindName("Mini");
			PivotRoot = (Pivot)((FrameworkElement)this).FindName("PivotRoot");
			grid = (Grid)((FrameworkElement)this).FindName("grid");
			MusicControl = (MusicInformationControl)((FrameworkElement)this).FindName("MusicControl");
			TutorialColorGrid1 = (Grid)((FrameworkElement)this).FindName("TutorialColorGrid1");
			TutorialColorGrid2 = (Grid)((FrameworkElement)this).FindName("TutorialColorGrid2");
			TutorialButton = (Button)((FrameworkElement)this).FindName("TutorialButton");
			GrayHardPlaceholder = (Border)((FrameworkElement)this).FindName("GrayHardPlaceholder");
			VMs = (VineMessagesInbox)((FrameworkElement)this).FindName("VMs");
			Profile = (ProfileControl)((FrameworkElement)this).FindName("Profile");
			Interactions = (InteractionsControl)((FrameworkElement)this).FindName("Interactions");
			Explore = (ExploreControl)((FrameworkElement)this).FindName("Explore");
			HomeVineList = (VineListControl)((FrameworkElement)this).FindName("HomeVineList");
			AppBar = (CommandBar)((FrameworkElement)this).FindName("AppBar");
			NewMessageButton = (AppBarButton)((FrameworkElement)this).FindName("NewMessageButton");
			SettingsButton = (AppBarButton)((FrameworkElement)this).FindName("SettingsButton");
			PinUnPinCommandButton = (AppBarButton)((FrameworkElement)this).FindName("PinUnPinCommandButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_007a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0080: Expected O, but got Unknown
		//IL_00a1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ab: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b7: Expected O, but got Unknown
		//IL_00d8: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e2: Expected O, but got Unknown
		//IL_00e8: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ee: Expected O, but got Unknown
		//IL_010f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0119: Expected O, but got Unknown
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0125: Expected O, but got Unknown
		//IL_0146: Unknown result type (might be due to invalid IL or missing references)
		//IL_0150: Expected O, but got Unknown
		//IL_0156: Unknown result type (might be due to invalid IL or missing references)
		//IL_015c: Expected O, but got Unknown
		//IL_017d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0187: Expected O, but got Unknown
		//IL_018d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0193: Expected O, but got Unknown
		//IL_01b4: Unknown result type (might be due to invalid IL or missing references)
		//IL_01be: Expected O, but got Unknown
		//IL_01c4: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ca: Expected O, but got Unknown
		//IL_01eb: Unknown result type (might be due to invalid IL or missing references)
		//IL_01f5: Expected O, but got Unknown
		//IL_01fb: Unknown result type (might be due to invalid IL or missing references)
		//IL_0201: Expected O, but got Unknown
		//IL_0222: Unknown result type (might be due to invalid IL or missing references)
		//IL_022c: Expected O, but got Unknown
		//IL_0232: Unknown result type (might be due to invalid IL or missing references)
		//IL_0238: Expected O, but got Unknown
		//IL_0259: Unknown result type (might be due to invalid IL or missing references)
		//IL_0263: Expected O, but got Unknown
		//IL_0269: Unknown result type (might be due to invalid IL or missing references)
		//IL_026f: Expected O, but got Unknown
		//IL_0290: Unknown result type (might be due to invalid IL or missing references)
		//IL_029a: Expected O, but got Unknown
		//IL_02a0: Unknown result type (might be due to invalid IL or missing references)
		//IL_02a6: Expected O, but got Unknown
		//IL_02c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_02d1: Expected O, but got Unknown
		//IL_02d7: Unknown result type (might be due to invalid IL or missing references)
		//IL_02dd: Expected O, but got Unknown
		//IL_02fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_0308: Expected O, but got Unknown
		//IL_030e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0314: Expected O, but got Unknown
		//IL_0335: Unknown result type (might be due to invalid IL or missing references)
		//IL_033f: Expected O, but got Unknown
		//IL_0345: Unknown result type (might be due to invalid IL or missing references)
		//IL_034b: Expected O, but got Unknown
		//IL_036c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0376: Expected O, but got Unknown
		//IL_037c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0382: Expected O, but got Unknown
		//IL_03a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_03ad: Expected O, but got Unknown
		//IL_03b3: Unknown result type (might be due to invalid IL or missing references)
		//IL_03b9: Expected O, but got Unknown
		//IL_03da: Unknown result type (might be due to invalid IL or missing references)
		//IL_03e4: Expected O, but got Unknown
		//IL_03ea: Unknown result type (might be due to invalid IL or missing references)
		//IL_03f0: Expected O, but got Unknown
		//IL_0411: Unknown result type (might be due to invalid IL or missing references)
		//IL_041b: Expected O, but got Unknown
		//IL_0421: Unknown result type (might be due to invalid IL or missing references)
		//IL_0427: Expected O, but got Unknown
		//IL_0448: Unknown result type (might be due to invalid IL or missing references)
		//IL_0452: Expected O, but got Unknown
		//IL_0458: Unknown result type (might be due to invalid IL or missing references)
		//IL_045e: Expected O, but got Unknown
		//IL_047f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0489: Expected O, but got Unknown
		//IL_048f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0495: Expected O, but got Unknown
		//IL_04b6: Unknown result type (might be due to invalid IL or missing references)
		//IL_04c0: Expected O, but got Unknown
		//IL_04fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_0504: Expected O, but got Unknown
		//IL_0530: Unknown result type (might be due to invalid IL or missing references)
		//IL_0536: Expected O, but got Unknown
		//IL_0567: Unknown result type (might be due to invalid IL or missing references)
		//IL_056d: Expected O, but got Unknown
		//IL_058e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0598: Expected O, but got Unknown
		//IL_059e: Unknown result type (might be due to invalid IL or missing references)
		//IL_05a4: Expected O, but got Unknown
		//IL_05c5: Unknown result type (might be due to invalid IL or missing references)
		//IL_05cf: Expected O, but got Unknown
		//IL_05d2: Unknown result type (might be due to invalid IL or missing references)
		//IL_05d8: Expected O, but got Unknown
		//IL_05f9: Unknown result type (might be due to invalid IL or missing references)
		//IL_0603: Expected O, but got Unknown
		//IL_0606: Unknown result type (might be due to invalid IL or missing references)
		//IL_060c: Expected O, but got Unknown
		//IL_062d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0637: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(UploadJob_Tapped));
			break;
		}
		case 2:
		{
			Pivot val4 = (Pivot)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<SelectionChangedEventHandler, EventRegistrationToken>)val4.add_SelectionChanged, (Action<EventRegistrationToken>)val4.remove_SelectionChanged, new SelectionChangedEventHandler(PivotRoot_OnSelectionChanged));
			break;
		}
		case 3:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(TutorialButton_OnTapped));
			break;
		}
		case 4:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(BlueLight_Tapped));
			break;
		}
		case 5:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(BlueDark_Tapped));
			break;
		}
		case 6:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Purple_Tapped));
			break;
		}
		case 7:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Pink_Tapped));
			break;
		}
		case 8:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Red_Tapped));
			break;
		}
		case 9:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Orange_Tapped));
			break;
		}
		case 10:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Yellow_Tapped));
			break;
		}
		case 11:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Green_Tapped));
			break;
		}
		case 12:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Teal_Tapped));
			break;
		}
		case 13:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(FriendFinder_Click));
			break;
		}
		case 14:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Camera_Click));
			break;
		}
		case 15:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val3.add_PointerEntered, (Action<EventRegistrationToken>)val3.remove_PointerEntered, new PointerEventHandler(AppBarMore_PointerEntered));
			break;
		}
		case 16:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Home_Tap));
			break;
		}
		case 17:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Discover_Tap));
			break;
		}
		case 18:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Notifications_Tap));
			break;
		}
		case 19:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Me_Tap));
			break;
		}
		case 20:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(VM_Tap));
			break;
		}
		case 21:
			((ExploreControl)target).SwipEvent += Explore_OnSwipEvent;
			break;
		case 22:
			((VineListControl)target).ScrollingDirectionChanged += OnScrollingDirectionChanged;
			break;
		case 23:
		{
			AppBar val2 = (AppBar)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)val2.add_Opened, (Action<EventRegistrationToken>)val2.remove_Opened, (EventHandler<object>)AppBar_OnOpened);
			val2 = (AppBar)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)val2.add_Closed, (Action<EventRegistrationToken>)val2.remove_Closed, (EventHandler<object>)AppBar_OnClosed);
			break;
		}
		case 24:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(MuteVolume_Click));
			break;
		}
		case 25:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(NewMessage_Click));
			break;
		}
		case 26:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Settings_Click));
			break;
		}
		case 27:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(PinUnPinCommandButton_OnClick));
			break;
		}
		}
		_contentLoaded = true;
	}
}
