using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Web;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;

namespace Vine.Views;

public sealed class ProfileControl : NotifyPage, IComponentConnector
{
	private readonly ScrollViewer _scrollViewer;

	private const int PlaceholderOffset = 750;

	private bool _isScrolledBelowPlaceholder;

	private double _profileHeaderHeight;

	private bool _followApprovalBusy;

	private Thickness _footerMargin = new Thickness(0.0, 0.0, 0.0, 400.0);

	private Brush _likeBrush;

	private VineUserModel _user;

	private bool _isBusy;

	private bool _isEmpty;

	private bool _isSwitchingTab;

	private bool _hasError;

	private string _errorText = ResourceHelper.GetString("ProfileErrorLoading");

	private bool _showRetry;

	private bool _showExplore;

	private bool _showSuggestions;

	private Brush _postBrush;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	public VineListControl List;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public Section Section { get; set; }

	public bool IsScrolledBelowPlaceholder
	{
		get
		{
			return _isScrolledBelowPlaceholder;
		}
		set
		{
			SetProperty(ref _isScrolledBelowPlaceholder, value, "IsScrolledBelowPlaceholder");
			NotifyOfPropertyChange(() => TutorialHintVisibility);
		}
	}

	public double ProfileHeaderHeight
	{
		get
		{
			return _profileHeaderHeight;
		}
		set
		{
			SetProperty(ref _profileHeaderHeight, value, "ProfileHeaderHeight");
		}
	}

	public bool FollowApprovalBusy
	{
		get
		{
			return _followApprovalBusy;
		}
		set
		{
			_followApprovalBusy = value;
			OnPropertyChanged("FollowApprovalBusy");
			NotifyOfPropertyChange(() => FollowApprovalNotBusy);
		}
	}

	public bool FollowApprovalNotBusy => !FollowApprovalBusy;

	public Thickness PullToRefreshMargin
	{
		set
		{
			List.PullToRefreshMargin = value;
		}
	}

	public Thickness ListViewPadding
	{
		set
		{
			List.ListViewPadding = value;
		}
	}

	public Thickness FooterMargin
	{
		get
		{
			return _footerMargin;
		}
		set
		{
			SetProperty(ref _footerMargin, value, "FooterMargin");
		}
	}

	public Brush LikeBrush
	{
		get
		{
			return _likeBrush;
		}
		set
		{
			SetProperty(ref _likeBrush, value, "LikeBrush");
		}
	}

	public UserControlWrapper ControlWrapper { get; set; }

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

	public string UserId { get; set; }

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

	public bool IsEmpty
	{
		get
		{
			return _isEmpty;
		}
		set
		{
			SetProperty(ref _isEmpty, value, "IsEmpty");
			NotifyOfPropertyChange(() => EmptyText);
			NotifyOfPropertyChange(() => TutorialHintVisibility);
		}
	}

	public string EmptyText
	{
		get
		{
			if (!IsEmpty)
			{
				return null;
			}
			string result = string.Empty;
			if (User != null)
			{
				result = ((List.CurrentTab == VineListControl.Tab.Posts) ? ((!User.IsCurrentUser) ? string.Format(ResourceHelper.GetString("ProfileNoPostsUsername"), new object[1] { User.Username }) : ResourceHelper.GetString("ProfileNoPostsYou")) : ((!User.IsCurrentUser) ? string.Format(ResourceHelper.GetString("ProfileNoLikesUsername"), new object[1] { User.Username }) : (ResourceHelper.GetString("ProfileNoLikesYou") + ".\n" + ResourceHelper.GetString("CheckOutSomeNewVines"))));
			}
			return result;
		}
	}

	public bool IsSwitchingTab
	{
		get
		{
			return _isSwitchingTab;
		}
		set
		{
			if (value)
			{
				IsEmpty = false;
			}
			SetProperty(ref _isSwitchingTab, value, "IsSwitchingTab");
		}
	}

	public bool HasError
	{
		get
		{
			return _hasError;
		}
		set
		{
			SetProperty(ref _hasError, value, "HasError");
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

	public bool IsFinishedLoading { get; set; }

	public bool TutorialHintVisibility
	{
		get
		{
			if (List.CurrentTab == VineListControl.Tab.Posts && IsEmpty && IsScrolledBelowPlaceholder && !ApplicationSettings.Current.HasSeenEmptyPostsHint)
			{
				return User.IsCurrentUser;
			}
			return false;
		}
	}

	public bool ShowExplore
	{
		get
		{
			return _showExplore;
		}
		set
		{
			SetProperty(ref _showExplore, value, "ShowExplore");
		}
	}

	public bool ShowSuggestions
	{
		get
		{
			return _showSuggestions;
		}
		set
		{
			SetProperty(ref _showSuggestions, value, "ShowSuggestions");
		}
	}

	public List<VineUserModel> _suggestedToFollow { get; set; }

	public ObservableCollection<VineUserModel> VisibleSuggestedToFollow { get; set; }

	public bool IsSuggestedLoaded { get; set; }

	public bool IsActive { get; set; }

	public Brush PostBrush
	{
		get
		{
			return _postBrush;
		}
		set
		{
			SetProperty(ref _postBrush, value, "PostBrush");
		}
	}

	public ProfileControl()
	{
		InitializeComponent();
		List.EmptyText = null;
		_scrollViewer = ((FrameworkElement)(object)List).GetFirstLogicalChildByType<ScrollViewer>(applyTemplates: true);
		ScrollViewer scrollViewer = _scrollViewer;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<ScrollViewerViewChangedEventArgs>, EventRegistrationToken>)scrollViewer.add_ViewChanged, (Action<EventRegistrationToken>)scrollViewer.remove_ViewChanged, (EventHandler<ScrollViewerViewChangedEventArgs>)ScrollViewerOnViewChanged);
		ControlWrapper = new UserControlWrapper
		{
			Model = this
		};
		VisibleSuggestedToFollow = new ObservableCollection<VineUserModel>();
		_suggestedToFollow = new List<VineUserModel>();
	}

	public async Task OnActivate()
	{
		IsActive = true;
		UpdateTabColors();
		if (IsFinishedLoading)
		{
			await List.OnActivate();
			return;
		}
		Task<ApiResult<BaseVineResponseModel<VineUsersMetaModel>>> suggestedTask = null;
		if (string.IsNullOrEmpty(UserId))
		{
			UserId = ApplicationSettings.Current.UserId;
		}
		else if (!IsSuggestedLoaded)
		{
			suggestedTask = App.Api.GetSimilarSuggestions(UserId);
		}
		await Refresh();
		IsFinishedLoading = !IsEmpty && !HasError;
		if (suggestedTask == null)
		{
			return;
		}
		ApiResult<BaseVineResponseModel<VineUsersMetaModel>> apiResult = await suggestedTask;
		if (!apiResult.HasError && apiResult.Model.Data != null && apiResult.Model.Data.Records != null)
		{
			IEnumerable<VineUserModel> source = apiResult.Model.Data.Records.Where((VineUserModel user) => !user.Following);
			_suggestedToFollow = source.Skip(3).ToList();
			VisibleSuggestedToFollow.Repopulate(source.Take(3));
		}
	}

	public async Task PullToRefresh()
	{
		await Refresh();
	}

	private async Task Refresh()
	{
		HasError = false;
		IsBusy = true;
		ApiResult<BaseVineResponseModel<VineUserModel>> apiResult = await App.Api.GetUser(UserId);
		if (apiResult.HasError)
		{
			if (User == null)
			{
				HasError = true;
				if (apiResult.HasConnectivityError)
				{
					ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
					ShowRetry = false;
				}
				else
				{
					ErrorText = ResourceHelper.GetString("ProfileErrorLoading");
					ShowRetry = true;
				}
			}
			else
			{
				apiResult.PopUpErrorIfExists();
			}
		}
		else
		{
			User = apiResult.Model.Data;
			UpdateProfileHeaderHeight();
			if (User.IsCurrentUser)
			{
				ApplicationSettings.Current.User = User;
			}
			List.UserId = User.UserId;
			UpdateTabColors();
			IsEmpty = false;
			if (User.AreVinesViewable)
			{
				if (List.Header == null)
				{
					List.Header = ControlWrapper;
				}
				if (List.Footer == null)
				{
					List.Footer = ControlWrapper;
				}
				if (!List.IsFinishedLoading)
				{
					await List.OnActivate();
				}
				else
				{
					await List.Refresh();
				}
				IsEmpty = !List.Items.Any() && !List.HasError;
				UpdateFooterMargin();
			}
			else
			{
				List.SetHeader();
				List.Clear();
			}
		}
		IsBusy = false;
	}

	private void UpdateFooterMargin()
	{
		if (List.Items.Count >= 1)
		{
			FooterMargin = new Thickness(0.0);
		}
		else if (List.Items.Count == 0)
		{
			FooterMargin = new Thickness(0.0, 0.0, 0.0, 350.0);
		}
	}

	private void UpdateProfileHeaderHeight()
	{
		ProfileHeaderHeight = ((User != null && User.TwitterDisplayScreenname != null) ? 283 : 262);
	}

	public void OnDeactivate()
	{
		IsActive = false;
		IsScrolledBelowPlaceholder = false;
		List.OnDeactivate();
	}

	public void ScrollToTop()
	{
		List.ScrollToTop();
	}

	public void Clear()
	{
		IsFinishedLoading = false;
		User = null;
		UserId = null;
		List.Clear();
	}

	private async void Posts_Click(object sender, RoutedEventArgs e)
	{
		IsSwitchingTab = true;
		List.CurrentTab = VineListControl.Tab.Posts;
		ShowExplore = false;
		UpdateTabColors();
		PagedItemsResult<VineViewModel> result = await List.Items.Refresh();
		if (result.ApiResult.HasError)
		{
			result.ApiResult.PopUpErrorIfExists();
			List.CurrentTab = VineListControl.Tab.Likes;
			UpdateTabColors();
		}
		else
		{
			List.Items.ResetItems(result.ViewModels, scrollToTop: false);
			UpdateFooterMargin();
			await List.RefreshPlayingVine();
		}
		IsEmpty = !List.Items.Any() && !HasError;
		List.IsFinishedLoading = !result.ApiResult.HasError;
		IsSwitchingTab = false;
	}

	private async void Likes_Click(object sender, RoutedEventArgs e)
	{
		IsSwitchingTab = true;
		List.CurrentTab = VineListControl.Tab.Likes;
		UpdateTabColors();
		PagedItemsResult<VineViewModel> result = await List.Items.Refresh();
		if (result.ApiResult.HasError)
		{
			result.ApiResult.PopUpErrorIfExists();
			List.CurrentTab = VineListControl.Tab.Posts;
			UpdateTabColors();
		}
		else
		{
			List.Items.ResetItems(result.ViewModels, scrollToTop: false);
			UpdateFooterMargin();
			await List.RefreshPlayingVine();
		}
		IsEmpty = !List.Items.Any() && !HasError;
		ShowExplore = IsEmpty && User.IsCurrentUser;
		List.IsFinishedLoading = !result.ApiResult.HasError;
		IsSwitchingTab = false;
	}

	private void UpdateTabColors()
	{
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		//IL_007b: Unknown result type (might be due to invalid IL or missing references)
		//IL_011d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0127: Expected O, but got Unknown
		//IL_00c3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cd: Expected O, but got Unknown
		//IL_014f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0159: Expected O, but got Unknown
		//IL_00f5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ff: Expected O, but got Unknown
		List.SecondaryBrush = (Brush)((User != null) ? ((object)User.ProfileBgBrush) : ((object)(Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"]));
		foreach (VineViewModel item in List.Items)
		{
			item.SecondaryBrush = (Brush)((User == null) ? ((object)(Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"]) : ((object)User.ProfileBgBrush));
		}
		if (List.CurrentTab == VineListControl.Tab.Posts)
		{
			if (User == null)
			{
				PostBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];
			}
			else
			{
				PostBrush = (Brush)(object)User.ProfileBgBrush;
			}
			LikeBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGrayMediumBrush"];
		}
		else
		{
			if (User == null)
			{
				LikeBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];
			}
			else
			{
				LikeBrush = (Brush)(object)User.ProfileBgBrush;
			}
			PostBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGrayMediumBrush"];
		}
	}

	private async void ButtonAcceptRequest_OnClick(object sender, RoutedEventArgs e)
	{
		FollowApprovalBusy = true;
		ApiResult<BaseVineResponseModel> apiResult = await App.Api.AcceptFollowerRequest(ApplicationSettings.Current.UserId, UserId);
		if (!apiResult.HasError && apiResult.Model.Success)
		{
			User.FollowApprovalPending = false;
		}
		FollowApprovalBusy = false;
	}

	private async void ButtonDeclineRequest_OnClick(object sender, RoutedEventArgs e)
	{
		FollowApprovalBusy = true;
		ApiResult<BaseVineResponseModel> apiResult = await App.Api.DeclineFollowerRequest(ApplicationSettings.Current.UserId, UserId);
		if (!apiResult.HasError && apiResult.Model.Success)
		{
			User.FollowApprovalPending = false;
		}
		FollowApprovalBusy = false;
	}

	private void TextBlockFollowing_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		App.RootFrame.NavigateWithObject(typeof(VineUserListView), new VineUserListViewParams
		{
			Type = UserListType.Following,
			UserId = User.UserId
		});
	}

	private void TextBlockFollowers_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		App.RootFrame.NavigateWithObject(typeof(VineUserListView), new VineUserListViewParams
		{
			Type = UserListType.Followers,
			UserId = User.UserId
		});
	}

	private void Profile_Tapped(object sender, TappedRoutedEventArgs e)
	{
		if (User.IsCurrentUser)
		{
			App.RootFrame.Navigate(typeof(SettingsView));
		}
	}

	public void DeletedPost()
	{
		User.PostCount -= 1;
		IsEmpty = !List.Items.Any();
	}

	private void MessageButton_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		App.RootFrame.NavigateWithObject(typeof(VineMessagesThreadView), new ConversationViewModel(User, new EmptyConversationModel()));
	}

	private void List_OnLoaded(object sender, RoutedEventArgs e)
	{
		List.Section = Section;
	}

	private void TutorialHint_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		ApplicationSettings.Current.HasSeenEmptyPostsHint = true;
		NotifyOfPropertyChange(() => TutorialHintVisibility);
	}

	private void ExploreButton_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		App.RootFrame.NavigateWithObject(typeof(HomeView), new ExploreControl.ExploreViewParams());
	}

	private void ScrollViewerOnViewChanged(object sender, ScrollViewerViewChangedEventArgs e)
	{
		IsScrolledBelowPlaceholder = _scrollViewer.VerticalOffset + _scrollViewer.ViewportHeight > 750.0;
	}

	private async void FollowButton_Tapped(object sender, TappedRoutedEventArgs e)
	{
		ShowSuggestions = !User.Following && VisibleSuggestedToFollow.Any();
		User.FollowToggle();
		if (!User.Following && !User.FollowRequested)
		{
			await Refresh();
		}
		e.put_Handled(true);
	}

	private void User_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineUserModel vineUserModel = (VineUserModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.Navigate(typeof(ProfileView), (object)vineUserModel.UserId);
	}

	private async void ListFollowButton_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		await Task.Delay(100);
		VineUserModel item = (VineUserModel)((FrameworkElement)sender).DataContext;
		VisibleSuggestedToFollow.Remove(item);
		if (_suggestedToFollow.Any())
		{
			await Task.Delay(100);
			VineUserModel item2 = _suggestedToFollow[0];
			_suggestedToFollow.RemoveAt(0);
			VisibleSuggestedToFollow.Add(item2);
		}
		else if (!VisibleSuggestedToFollow.Any())
		{
			ShowSuggestions = false;
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/ProfileControl.xaml"), (ComponentResourceLocation)0);
			List = (VineListControl)((FrameworkElement)this).FindName("List");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0046: Unknown result type (might be due to invalid IL or missing references)
		//IL_004c: Expected O, but got Unknown
		//IL_006d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0077: Expected O, but got Unknown
		//IL_007d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0083: Expected O, but got Unknown
		//IL_00a4: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ae: Expected O, but got Unknown
		//IL_00b4: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ba: Expected O, but got Unknown
		//IL_00db: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e5: Expected O, but got Unknown
		//IL_00eb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f1: Expected O, but got Unknown
		//IL_0112: Unknown result type (might be due to invalid IL or missing references)
		//IL_011c: Expected O, but got Unknown
		//IL_0122: Unknown result type (might be due to invalid IL or missing references)
		//IL_0128: Expected O, but got Unknown
		//IL_0149: Unknown result type (might be due to invalid IL or missing references)
		//IL_0153: Expected O, but got Unknown
		//IL_0159: Unknown result type (might be due to invalid IL or missing references)
		//IL_015f: Expected O, but got Unknown
		//IL_0180: Unknown result type (might be due to invalid IL or missing references)
		//IL_018a: Expected O, but got Unknown
		//IL_0190: Unknown result type (might be due to invalid IL or missing references)
		//IL_0196: Expected O, but got Unknown
		//IL_01b7: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c1: Expected O, but got Unknown
		//IL_01c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_01cd: Expected O, but got Unknown
		//IL_01ee: Unknown result type (might be due to invalid IL or missing references)
		//IL_01f8: Expected O, but got Unknown
		//IL_01fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_0204: Expected O, but got Unknown
		//IL_0225: Unknown result type (might be due to invalid IL or missing references)
		//IL_022f: Expected O, but got Unknown
		//IL_0235: Unknown result type (might be due to invalid IL or missing references)
		//IL_023b: Expected O, but got Unknown
		//IL_025c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0266: Expected O, but got Unknown
		//IL_026c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0272: Expected O, but got Unknown
		//IL_0293: Unknown result type (might be due to invalid IL or missing references)
		//IL_029d: Expected O, but got Unknown
		//IL_02a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_02a9: Expected O, but got Unknown
		//IL_02ca: Unknown result type (might be due to invalid IL or missing references)
		//IL_02d4: Expected O, but got Unknown
		//IL_02d7: Unknown result type (might be due to invalid IL or missing references)
		//IL_02dd: Expected O, but got Unknown
		//IL_02fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_0308: Expected O, but got Unknown
		//IL_030b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0311: Expected O, but got Unknown
		//IL_0332: Unknown result type (might be due to invalid IL or missing references)
		//IL_033c: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(ExploreButton_OnTapped));
			break;
		}
		case 2:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(Posts_Click));
			break;
		}
		case 3:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(Likes_Click));
			break;
		}
		case 4:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(FollowButton_Tapped));
			break;
		}
		case 5:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(MessageButton_OnTapped));
			break;
		}
		case 6:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(Profile_Tapped));
			break;
		}
		case 7:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(TextBlockFollowers_OnTapped));
			break;
		}
		case 8:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(TextBlockFollowing_OnTapped));
			break;
		}
		case 9:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(ButtonAcceptRequest_OnClick));
			break;
		}
		case 10:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(ButtonDeclineRequest_OnClick));
			break;
		}
		case 11:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(User_OnTapped));
			break;
		}
		case 12:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(ListFollowButton_OnTapped));
			break;
		}
		case 13:
		{
			FrameworkElement val2 = (FrameworkElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Loaded, (Action<EventRegistrationToken>)val2.remove_Loaded, new RoutedEventHandler(List_OnLoaded));
			break;
		}
		case 14:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(TutorialHint_OnTapped));
			break;
		}
		}
		_contentLoaded = true;
	}
}
