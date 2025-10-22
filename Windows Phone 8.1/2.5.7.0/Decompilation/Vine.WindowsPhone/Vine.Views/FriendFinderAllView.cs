using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.ApplicationModel.Contacts;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class FriendFinderAllView : BasePage, IPullToRefresh, IComponentConnector
{
	private bool _isBusy;

	private bool _isNux;

	private bool _hasError;

	private string _errorText = ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR");

	private bool _showRetry = true;

	private FriendFinderListSource _listSource;

	private bool _isFinishedLoading;

	private bool _pullToRefreshInitialized;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid LayoutRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private PullToRefreshListControl PullToRefreshView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ListOfFriends;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private CommandBar Commands;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton AcceptButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

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

	public bool IsNux
	{
		get
		{
			return _isNux;
		}
		set
		{
			SetProperty(ref _isNux, value, "IsNux");
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
			NotifyOfPropertyChange(() => IsScrollViewVisible);
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

	public ObservableCollection<FriendFinderModel> Items { get; set; }

	public FriendFinderListSource ListSource
	{
		get
		{
			return _listSource;
		}
		set
		{
			SetProperty(ref _listSource, value, "ListSource");
			NotifyOfPropertyChange(() => HeaderText);
			NotifyOfPropertyChange(() => IsSuggestedVisible);
		}
	}

	public string HeaderText => ListSource switch
	{
		FriendFinderListSource.Contacts => ResourceHelper.GetString("find_friends_address_subtitle"), 
		FriendFinderListSource.Twitter => ResourceHelper.GetString("find_friends_twitter_subtitle"), 
		FriendFinderListSource.Suggested => ResourceHelper.GetString("ExploreSearchSuggestions"), 
		_ => string.Empty, 
	};

	public bool IsSuggestedVisible => ListSource == FriendFinderListSource.Suggested;

	public bool IsScrollViewVisible
	{
		get
		{
			if (IsFinishedLoading)
			{
				return !HasError;
			}
			return false;
		}
	}

	public bool IsFinishedLoading
	{
		get
		{
			return _isFinishedLoading;
		}
		set
		{
			SetProperty(ref _isFinishedLoading, value, "IsFinishedLoading");
			NotifyOfPropertyChange(() => IsScrollViewVisible);
		}
	}

	public FriendFinderAllView()
	{
		InitializeComponent();
		Items = new ObservableCollection<FriendFinderModel>();
		PullToRefreshView.AddReferences(ListOfFriends, this, 0.0);
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		if (IsFinishedLoading || IsBusy)
		{
			return;
		}
		AcceptButton.put_Label(ResourceHelper.GetString("UniversalOkay"));
		if (e.PageState != null)
		{
			IsBusy = true;
			Items.Repopulate(e.LoadValueOrDefault<List<FriendFinderModel>>("VineUsers"));
			ListSource = (FriendFinderListSource)e.LoadValueOrDefault<long>("ListSource");
			IsNux = e.LoadValueOrDefault<bool>("IsNux");
			long offSet = e.LoadValueOrDefault<long>("ScrollOffset");
			((UIElement)PullToRefreshView.ListView).put_Opacity(0.0);
			await ((FrameworkElement)(object)PullToRefreshView.ListView).LayoutUpdatedAsync();
			await Task.Delay(250);
			await PullToRefreshView.UpdateListPadding();
			await PullToRefreshView.ScrollViewer.ScrollToVerticalOffsetSpin(offSet);
			_pullToRefreshInitialized = true;
			((UIElement)PullToRefreshView.ListView).put_Opacity(1.0);
			IsBusy = false;
		}
		else
		{
			FriendFinderAllNavigationParameter friendFinderAllNavigationParameter = Serialization.Deserialize<FriendFinderAllNavigationParameter>(e.NavigationParameter.ToString());
			IsNux = friendFinderAllNavigationParameter.IsNux;
			ListSource = friendFinderAllNavigationParameter.Source;
			if (friendFinderAllNavigationParameter.VineUsers == null || !friendFinderAllNavigationParameter.VineUsers.Any())
			{
				await Refresh();
			}
			else
			{
				IsBusy = true;
				((UIElement)ListOfFriends).put_Opacity(0.0);
				List<FriendFinderModel> list = new List<FriendFinderModel>();
				list.Add(new FriendFinderModel
				{
					HeaderText = HeaderText,
					Source = ListSource,
					SeeAllVisible = false
				});
				list.AddRange(friendFinderAllNavigationParameter.VineUsers.Select((VineUserModel user) => new FriendFinderModel
				{
					VineUserModel = user,
					Source = ListSource
				}));
				Items.Repopulate(list);
				await ((FrameworkElement)(object)PullToRefreshView.ListView).LayoutUpdatedAsync();
				await Task.Delay(250);
				await PullToRefreshView.UpdateListPadding();
				await PullToRefreshView.ScrollToTop();
				_pullToRefreshInitialized = true;
				((UIElement)ListOfFriends).put_Opacity(1.0);
				IsBusy = false;
			}
		}
		IsFinishedLoading = true;
	}

	protected override async void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		e.PageState["VineUsers"] = Items.ToList();
		e.PageState["ListSource"] = (long)ListSource;
		e.PageState["ScrollOffset"] = (long)PullToRefreshView.ScrollViewer.VerticalOffset;
		e.PageState["IsNux"] = IsNux;
	}

	private async Task Refresh()
	{
		IsBusy = true;
		HasError = false;
		Task<ApiResult<BaseVineResponseModel<List<VineUserModel>>>> apiCall;
		switch (ListSource)
		{
		case FriendFinderListSource.Contacts:
		{
			List<VineContactModel> contacts = VineContactModel.ConvertContactsForVine(await (await ContactManager.RequestStoreAsync()).FindContactsAsync());
			apiCall = App.Api.GetUserContacts(ApplicationSettings.Current.VineSession.UserId, contacts);
			break;
		}
		case FriendFinderListSource.Twitter:
			apiCall = App.Api.GetTwitterFriends(ApplicationSettings.Current.VineSession.UserId);
			break;
		default:
			apiCall = App.Api.GetSuggestedToFollow(ApplicationSettings.Current.UserId);
			break;
		}
		await apiCall;
		ApiResult<BaseVineResponseModel<List<VineUserModel>>> result = apiCall.Result;
		if (!result.HasError)
		{
			List<FriendFinderModel> list = new List<FriendFinderModel>();
			list.Add(new FriendFinderModel
			{
				HeaderText = HeaderText,
				Source = ListSource,
				SeeAllVisible = false
			});
			list.AddRange(result.Model.Data.Select((VineUserModel user) => new FriendFinderModel
			{
				VineUserModel = user,
				Source = ListSource
			}));
			Items.Repopulate(list);
		}
		HasError = result.HasError;
		if (result.HasConnectivityError)
		{
			ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
			ShowRetry = false;
		}
		else
		{
			ErrorText = ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR");
			ShowRetry = true;
		}
		IsBusy = false;
		if (!HasError && !_pullToRefreshInitialized)
		{
			await PullToRefreshView.UpdateListPadding();
			await PullToRefreshView.ScrollToTop();
			_pullToRefreshInitialized = true;
		}
	}

	private void User_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineUserModel vineUserModel = (VineUserModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.Navigate(typeof(ProfileView), (object)vineUserModel.UserId);
	}

	private async void FollowAll_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		IEnumerable<VineUserModel> notFollowing = from i in Items
			where i.IsUser && !i.VineUserModel.Following
			select i.VineUserModel;
		string[] array = notFollowing.Select((VineUserModel s) => s.UserId).ToArray();
		if (!array.Any())
		{
			return;
		}
		IsBusy = true;
		ApiResult<string> bulkFollow = await App.Api.BulkFollowUsers(array);
		if (!bulkFollow.HasError)
		{
			foreach (VineUserModel item in notFollowing)
			{
				item.Following = true;
			}
			await Task.Delay(500);
		}
		else
		{
			bulkFollow.PopUpErrorIfExists();
		}
		IsBusy = false;
	}

	private async void Refresh_OnClick(object sender, RoutedEventArgs e)
	{
		await Refresh();
	}

	private void AcceptButton_Click(object sender, RoutedEventArgs e)
	{
		App.RootFrame.Navigate(typeof(HomeView));
	}

	public async Task PullToRefresh()
	{
		await Refresh();
	}

	private void SeeAll_OnTapped(object sender, TappedRoutedEventArgs e)
	{
	}

	private void FollowButton_OnTapped(object sender, TappedRoutedEventArgs e)
	{
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Expected O, but got Unknown
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/FriendFinderAllView.xaml"), (ComponentResourceLocation)0);
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			PullToRefreshView = (PullToRefreshListControl)((FrameworkElement)this).FindName("PullToRefreshView");
			ListOfFriends = (ListView)((FrameworkElement)this).FindName("ListOfFriends");
			Commands = (CommandBar)((FrameworkElement)this).FindName("Commands");
			AcceptButton = (AppBarButton)((FrameworkElement)this).FindName("AcceptButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_002a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0030: Expected O, but got Unknown
		//IL_0051: Unknown result type (might be due to invalid IL or missing references)
		//IL_005b: Expected O, but got Unknown
		//IL_0061: Unknown result type (might be due to invalid IL or missing references)
		//IL_0067: Expected O, but got Unknown
		//IL_0088: Unknown result type (might be due to invalid IL or missing references)
		//IL_0092: Expected O, but got Unknown
		//IL_0098: Unknown result type (might be due to invalid IL or missing references)
		//IL_009e: Expected O, but got Unknown
		//IL_00bf: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c9: Expected O, but got Unknown
		//IL_00cf: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d5: Expected O, but got Unknown
		//IL_00f6: Unknown result type (might be due to invalid IL or missing references)
		//IL_0100: Expected O, but got Unknown
		//IL_0106: Unknown result type (might be due to invalid IL or missing references)
		//IL_010c: Expected O, but got Unknown
		//IL_012d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0137: Expected O, but got Unknown
		//IL_013a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0140: Expected O, but got Unknown
		//IL_0161: Unknown result type (might be due to invalid IL or missing references)
		//IL_016b: Expected O, but got Unknown
		//IL_016e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0174: Expected O, but got Unknown
		//IL_0195: Unknown result type (might be due to invalid IL or missing references)
		//IL_019f: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(SeeAll_OnTapped));
			break;
		}
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(User_OnTapped));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(FollowButton_OnTapped));
			break;
		}
		case 4:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Refresh_OnClick));
			break;
		}
		case 5:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(FollowAll_OnTapped));
			break;
		}
		case 6:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(FollowAll_OnTapped));
			break;
		}
		case 7:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(AcceptButton_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
