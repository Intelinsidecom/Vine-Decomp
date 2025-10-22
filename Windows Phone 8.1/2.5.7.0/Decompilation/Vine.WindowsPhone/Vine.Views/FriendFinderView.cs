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
using Windows.ApplicationModel.Chat;
using Windows.ApplicationModel.Contacts;
using Windows.ApplicationModel.Email;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class FriendFinderView : BasePage, IPullToRefresh, IComponentConnector
{
	private bool _isBusy;

	private bool _isNux;

	private bool _placeholderVisibility;

	private bool _twitterEnabled;

	private bool _contactsEnabled;

	private bool _hasError;

	private string _errorText = ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR");

	private bool _showRetry = true;

	private const int VineUsersToShowSmall = 5;

	private const int VinueUsersToShowLarge = 10;

	private bool _isFinishedLoading;

	private List<FriendFinderModel> _hiddenTwitterFriends;

	private List<FriendFinderModel> _hiddenSuggestedFriends;

	private List<FriendFinderModel> _hiddenContactFriends;

	private List<VineUserModel> _allSuggestedFriends;

	private bool _pullToRefreshInitialized;

	private bool _pausePullToRefresh;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid LayoutRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private PullToRefreshListControl PullToRefreshView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView SuggestedList;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button ConnectToTwitterButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button ConnectToContactsButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private CommandBar Commands;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton AcceptButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton InviteEmailCommandButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton InviteTextCommandButton;

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

	public bool PlaceholderVisibility
	{
		get
		{
			return _placeholderVisibility;
		}
		set
		{
			_placeholderVisibility = value;
			OnPropertyChanged("PlaceholderVisibility");
		}
	}

	public bool TwitterEnabled
	{
		get
		{
			return _twitterEnabled;
		}
		set
		{
			SetProperty(ref _twitterEnabled, value, "TwitterEnabled");
			NotifyOfPropertyChange(() => ConnectAccountsVisble);
		}
	}

	public bool ContactsEnabled
	{
		get
		{
			return _contactsEnabled;
		}
		set
		{
			SetProperty(ref _contactsEnabled, value, "ContactsEnabled");
			NotifyOfPropertyChange(() => ConnectAccountsVisble);
		}
	}

	public bool ConnectAccountsVisble
	{
		get
		{
			if (ContactsEnabled)
			{
				return TwitterEnabled;
			}
			return false;
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

	private int VineUsersToShow { get; set; }

	public bool IsFinishedLoading
	{
		get
		{
			return _isFinishedLoading;
		}
		set
		{
			SetProperty(ref _isFinishedLoading, value, "IsFinishedLoading");
		}
	}

	public ObservableCollection<FriendFinderModel> Items { get; set; }

	public FriendFinderView()
	{
		InitializeComponent();
		_hiddenTwitterFriends = new List<FriendFinderModel>();
		_hiddenContactFriends = new List<FriendFinderModel>();
		_hiddenSuggestedFriends = new List<FriendFinderModel>();
		Items = new ObservableCollection<FriendFinderModel>();
		PullToRefreshView.AddReferences(SuggestedList, this, 0.0);
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.None, "find_friends"));
		if (!IsFinishedLoading && !IsBusy)
		{
			TwitterEnabled = ApplicationSettings.Current.IsTwitterEnabled;
			ContactsEnabled = ApplicationSettings.Current.IsAddressBookEnabled;
			bool? flag = e.NavigationParameter as bool?;
			if (flag.HasValue)
			{
				IsNux = flag == true;
			}
			else if (e.PageState != null)
			{
				IsNux = e.LoadValueOrDefault<bool>("IsNux");
			}
			else
			{
				IsNux = false;
			}
			UpdateAppBar();
			if (e.PageState != null && e.LoadValueOrDefault<bool>("TwitterEnabled") == TwitterEnabled && (int)e.NavigationEvent.NavigationMode != 1)
			{
				((UIElement)PullToRefreshView).put_Opacity(0.0);
				Items.Repopulate(e.LoadValueOrDefault<List<FriendFinderModel>>("Items"));
				_hiddenContactFriends = e.LoadValueOrDefault<List<FriendFinderModel>>("HiddenContactList") ?? new List<FriendFinderModel>();
				_hiddenTwitterFriends = e.LoadValueOrDefault<List<FriendFinderModel>>("HiddenTwitterList") ?? new List<FriendFinderModel>();
				_hiddenSuggestedFriends = e.LoadValueOrDefault<List<FriendFinderModel>>("HiddenSuggestedList") ?? new List<FriendFinderModel>();
				_allSuggestedFriends = e.LoadValueOrDefault<List<VineUserModel>>("AllSuggestedList") ?? new List<VineUserModel>();
				VineUsersToShow = (int)e.LoadValueOrDefault<long>("VineUsersToShow");
				long offSet = e.LoadValueOrDefault<long>("ScrollOffset");
				IsFinishedLoading = true;
				await ((FrameworkElement)(object)PullToRefreshView.ListView).LayoutUpdatedAsync();
				await PullToRefreshView.UpdateListPadding();
				await PullToRefreshView.ScrollViewer.ScrollToVerticalOffsetSpin(offSet);
				_pullToRefreshInitialized = true;
				((UIElement)PullToRefreshView).put_Opacity(1.0);
			}
			else
			{
				await Refresh();
				IsFinishedLoading = true;
			}
		}
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		e.PageState["Items"] = Items.ToList();
		e.PageState["HiddenContactList"] = _hiddenContactFriends;
		e.PageState["HiddenTwitterList"] = _hiddenTwitterFriends;
		e.PageState["HiddenSuggestedList"] = _hiddenSuggestedFriends;
		e.PageState["AllSuggestedList"] = _allSuggestedFriends;
		e.PageState["ScrollOffset"] = (long)PullToRefreshView.ScrollViewer.VerticalOffset;
		e.PageState["TwitterEnabled"] = TwitterEnabled;
		e.PageState["VineUsersToShow"] = VineUsersToShow;
		e.PageState["IsNux"] = IsNux;
	}

	private async Task Refresh()
	{
		IsBusy = true;
		HasError = false;
		List<Task> taskList = new List<Task>();
		Task<ApiResult<BaseVineResponseModel<List<VineUserModel>>>> twitterTask = null;
		Task<ApiResult<BaseVineResponseModel<List<VineUserModel>>>> findContactsTask = null;
		List<FriendFinderModel> listItems = new List<FriendFinderModel>();
		if (TwitterEnabled)
		{
			twitterTask = App.Api.GetTwitterFriends(ApplicationSettings.Current.VineSession.UserId);
			taskList.Add(twitterTask);
		}
		if (ContactsEnabled)
		{
			List<VineContactModel> contacts = VineContactModel.ConvertContactsForVine(await (await ContactManager.RequestStoreAsync()).FindContactsAsync());
			findContactsTask = App.Api.GetUserContacts(ApplicationSettings.Current.VineSession.UserId, contacts);
			taskList.Add(findContactsTask);
		}
		Task<ApiResult<BaseVineResponseModel<List<VineUserModel>>>> suggestionTask = App.Api.GetSuggestedToFollow(ApplicationSettings.Current.VineSession.UserId);
		taskList.Add(suggestionTask);
		await Task.WhenAll(taskList);
		bool flag = twitterTask == null || twitterTask.Result.HasError || !twitterTask.Result.Model.Data.Any() || findContactsTask == null || findContactsTask.Result.HasError || !findContactsTask.Result.Model.Data.Any() || suggestionTask.Result.HasError || !suggestionTask.Result.Model.Data.Any();
		VineUsersToShow = (flag ? 10 : 5);
		bool connectivityError = false;
		if (findContactsTask != null)
		{
			ApiResult<BaseVineResponseModel<List<VineUserModel>>> result = findContactsTask.Result;
			if (!result.HasError)
			{
				List<VineUserModel> data = result.Model.Data;
				if (data.Any())
				{
					listItems.Add(new FriendFinderModel
					{
						HeaderText = ResourceHelper.GetString("find_friends_address_subtitle"),
						Source = FriendFinderListSource.Contacts,
						SeeAllVisible = (data.Count > VineUsersToShow)
					});
					IEnumerable<FriendFinderModel> collection = from vu in data.Take(VineUsersToShow)
						select new FriendFinderModel
						{
							Source = FriendFinderListSource.Contacts,
							VineUserModel = vu
						};
					listItems.AddRange(collection);
					_hiddenContactFriends = (from vu in data.Skip(VineUsersToShow)
						where !vu.Following
						select new FriendFinderModel
						{
							Source = FriendFinderListSource.Twitter,
							VineUserModel = vu
						}).ToList();
				}
				else
				{
					_hiddenContactFriends = new List<FriendFinderModel>();
				}
			}
			else if (findContactsTask.Result.HasConnectivityError)
			{
				connectivityError = true;
			}
		}
		if (twitterTask != null)
		{
			ApiResult<BaseVineResponseModel<List<VineUserModel>>> result2 = twitterTask.Result;
			if (!result2.HasError)
			{
				List<VineUserModel> data2 = result2.Model.Data;
				listItems.Add(new FriendFinderModel
				{
					HeaderText = ResourceHelper.GetString("find_friends_twitter_subtitle"),
					Source = FriendFinderListSource.Twitter,
					SeeAllVisible = (data2.Count > VineUsersToShow)
				});
				IEnumerable<FriendFinderModel> collection2 = from vu in data2.Take(VineUsersToShow)
					select new FriendFinderModel
					{
						Source = FriendFinderListSource.Twitter,
						VineUserModel = vu
					};
				listItems.AddRange(collection2);
				_hiddenTwitterFriends = (from vu in data2.Skip(VineUsersToShow)
					where !vu.Following
					select new FriendFinderModel
					{
						Source = FriendFinderListSource.Twitter,
						VineUserModel = vu
					}).ToList();
			}
			else if (result2.HasConnectivityError)
			{
				connectivityError = true;
			}
		}
		ApiResult<BaseVineResponseModel<List<VineUserModel>>> result3 = suggestionTask.Result;
		if (!result3.HasError && result3.Model.Data.Any())
		{
			_allSuggestedFriends = result3.Model.Data;
			_hiddenSuggestedFriends = (from vu in result3.Model.Data.Skip(VineUsersToShow)
				select new FriendFinderModel
				{
					Source = FriendFinderListSource.Suggested,
					VineUserModel = vu
				}).ToList();
			listItems.Add(new FriendFinderModel
			{
				HeaderText = ResourceHelper.GetString("ExploreSearchSuggestions"),
				Source = FriendFinderListSource.Suggested,
				SeeAllVisible = (_allSuggestedFriends.Count > VineUsersToShow)
			});
			IEnumerable<FriendFinderModel> collection3 = from vu in _allSuggestedFriends.Take(VineUsersToShow)
				select new FriendFinderModel
				{
					Source = FriendFinderListSource.Suggested,
					VineUserModel = vu
				};
			listItems.AddRange(collection3);
		}
		else if (result3.HasConnectivityError)
		{
			connectivityError = true;
		}
		Items.Repopulate(listItems);
		CheckIfErrorState(connectivityError);
		if (!HasError && !_pullToRefreshInitialized)
		{
			await ((FrameworkElement)(object)PullToRefreshView.ListView).LayoutUpdatedAsync();
			await PullToRefreshView.UpdateListPadding();
			await PullToRefreshView.ScrollToTop();
			_pullToRefreshInitialized = true;
		}
		IsBusy = false;
	}

	private void CheckIfErrorState(bool connectivityError)
	{
		HasError = !Items.Any();
		if (connectivityError)
		{
			ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
			ShowRetry = false;
		}
		else
		{
			ErrorText = ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR");
			ShowRetry = true;
		}
	}

	private async Task ReloadSuggestions()
	{
		ApiResult<BaseVineResponseModel<List<VineUserModel>>> apiResult = await App.Api.GetSuggestedToFollow(ApplicationSettings.Current.VineSession.UserId);
		if (!apiResult.HasError)
		{
			_hiddenSuggestedFriends = apiResult.Model.Data.Select((VineUserModel u) => new FriendFinderModel
			{
				Source = FriendFinderListSource.Suggested,
				VineUserModel = u
			}).ToList();
			ResetList(_hiddenSuggestedFriends, FriendFinderListSource.Suggested);
			_allSuggestedFriends = apiResult.Model.Data;
		}
		CheckIfErrorState(apiResult.HasConnectivityError);
	}

	private void User_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0009: Unknown result type (might be due to invalid IL or missing references)
		if (!IsNux)
		{
			VineUserModel vineUserModel = (VineUserModel)((FrameworkElement)sender).DataContext;
			App.RootFrame.Navigate(typeof(ProfileView), (object)vineUserModel.UserId);
		}
	}

	private void SeeAll_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		FriendFinderModel friendFinderModel = ((FrameworkElement)sender).DataContext as FriendFinderModel;
		FriendFinderAllNavigationParameter friendFinderAllNavigationParameter = new FriendFinderAllNavigationParameter
		{
			Source = friendFinderModel.Source,
			IsNux = IsNux
		};
		if (friendFinderModel.Source == FriendFinderListSource.Suggested)
		{
			friendFinderAllNavigationParameter.VineUsers = _allSuggestedFriends.OrderBy((VineUserModel v) => v.Following).ToList();
		}
		App.RootFrame.NavigateWithObject(typeof(FriendFinderAllView), friendFinderAllNavigationParameter);
	}

	private async void FollowButton_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		await TryResetAllLists();
	}

	private async void ConnectToContactsButton_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		MessageDialog val = new MessageDialog("");
		val.put_Content(ResourceHelper.GetString("settings_address_book_desc"));
		val.put_Title(ResourceHelper.GetString("settings_address_book"));
		val.Commands.Add((IUICommand)new UICommand(ResourceHelper.GetString("settings_activate_account_confirm"), (UICommandInvokedHandler)null, (object)0));
		val.Commands.Add((IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1));
		val.put_CancelCommandIndex(1u);
		if ((int)(await val.ShowAsync()).Id != 1)
		{
			ApplicationSettings.Current.IsAddressBookEnabled = true;
			ContactsEnabled = true;
			((UIElement)PullToRefreshView.ListView).put_Opacity(0.0);
			_pausePullToRefresh = true;
			await Refresh();
			await ((FrameworkElement)(object)PullToRefreshView.ListView).LayoutUpdatedAsync();
			await PullToRefreshView.UpdateListPadding();
			await PullToRefreshView.ScrollToTop();
			((UIElement)PullToRefreshView.ListView).put_Opacity(1.0);
			_pausePullToRefresh = false;
		}
	}

	private void ConnectToTwitterButton_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		App.RootFrame.NavigateWithObject(typeof(LoginTwitterView), new LoginParameters
		{
			UpdatingTwitterCredentials = true
		});
	}

	private async void FollowAll_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		VineUserModel[] notFollowing = (from i in Items
			where i.IsUser && !i.VineUserModel.Following
			select i.VineUserModel).ToArray();
		if (!notFollowing.Any())
		{
			return;
		}
		string[] userIdsToFollow = notFollowing.Select((VineUserModel s) => s.UserId).ToArray();
		IsBusy = true;
		ApiResult<string> bulkFollow = await App.Api.BulkFollowUsers(userIdsToFollow);
		if (!bulkFollow.HasError)
		{
			VineUserModel[] array = notFollowing;
			for (int num = 0; num < array.Length; num++)
			{
				array[num].Following = true;
			}
			await Task.Delay(500);
			await TryResetAllLists();
		}
		else
		{
			bulkFollow.PopUpErrorIfExists();
		}
		IsBusy = false;
	}

	private async Task TryResetAllLists()
	{
		if (!Items.Any((FriendFinderModel i) => i.Source == FriendFinderListSource.Contacts && i.IsUser && !i.VineUserModel.Following))
		{
			ResetList(_hiddenContactFriends, FriendFinderListSource.Contacts);
		}
		if (!Items.Any((FriendFinderModel i) => i.Source == FriendFinderListSource.Twitter && i.IsUser && !i.VineUserModel.Following))
		{
			ResetList(_hiddenTwitterFriends, FriendFinderListSource.Twitter);
		}
		if (!Items.Any((FriendFinderModel i) => i.Source == FriendFinderListSource.Suggested && i.IsUser && !i.VineUserModel.Following))
		{
			if (_hiddenSuggestedFriends.Any())
			{
				ResetList(_hiddenSuggestedFriends, FriendFinderListSource.Suggested);
			}
			else
			{
				await ReloadSuggestions();
			}
		}
	}

	private void ResetList(List<FriendFinderModel> hiddenFriends, FriendFinderListSource source)
	{
		if (!hiddenFriends.Any())
		{
			return;
		}
		FriendFinderModel friendFinderModel = Items.FirstOrDefault((FriendFinderModel i) => i.IsUser && i.Source == source);
		FriendFinderModel item = Items.Last((FriendFinderModel i) => i.IsUser && i.Source == source);
		if (friendFinderModel != null)
		{
			int num = Items.IndexOf(friendFinderModel);
			int num2 = Items.IndexOf(item);
			int num3 = 0;
			while (hiddenFriends.Count > 0 && num3 < VineUsersToShow && num <= num2)
			{
				Items[num] = hiddenFriends[0];
				hiddenFriends.RemoveAt(0);
				num++;
				num3++;
			}
		}
	}

	private void UpdateAppBar()
	{
		InviteEmailCommandButton.put_Label(ResourceHelper.GetString("InviteViaEmail"));
		InviteTextCommandButton.put_Label(ResourceHelper.GetString("InviteViaText"));
		AcceptButton.put_Label(ResourceHelper.GetString("UniversalOkay"));
		((AppBar)Commands).put_IsOpen(IsNux);
		((AppBar)Commands).put_IsSticky(IsNux);
		((AppBar)Commands).put_ClosedDisplayMode((AppBarClosedDisplayMode)(!IsNux));
	}

	private async void Refresh_OnClick(object sender, RoutedEventArgs e)
	{
		await Refresh();
	}

	private async void InviteEmail_OnClick(object sender, RoutedEventArgs e)
	{
		EmailMessage val = new EmailMessage();
		val.put_Body(GetInviteMessage());
		val.put_Subject(ResourceHelper.GetString("FollowOnVineEmailSubject"));
		await EmailManager.ShowComposeNewEmailAsync(val);
	}

	private string GetInviteMessage()
	{
		return string.Format(ResourceHelper.GetString("find_friends_invite_email"), new object[1] { ApplicationSettings.Current.VineSession.UserId });
	}

	private async void InviteText_OnClick(object sender, RoutedEventArgs e)
	{
		ChatMessage val = new ChatMessage();
		val.put_Body(GetInviteMessage());
		await ChatMessageManager.ShowComposeSmsMessageAsync(val);
	}

	private void AcceptButton_Click(object sender, RoutedEventArgs e)
	{
		App.RootFrame.Navigate(typeof(HomeView));
	}

	public async Task PullToRefresh()
	{
		if (!_pausePullToRefresh && !IsBusy)
		{
			await Refresh();
		}
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
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		//IL_00dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e7: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/FriendFinderView.xaml"), (ComponentResourceLocation)0);
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			PullToRefreshView = (PullToRefreshListControl)((FrameworkElement)this).FindName("PullToRefreshView");
			SuggestedList = (ListView)((FrameworkElement)this).FindName("SuggestedList");
			ConnectToTwitterButton = (Button)((FrameworkElement)this).FindName("ConnectToTwitterButton");
			ConnectToContactsButton = (Button)((FrameworkElement)this).FindName("ConnectToContactsButton");
			Commands = (CommandBar)((FrameworkElement)this).FindName("Commands");
			AcceptButton = (AppBarButton)((FrameworkElement)this).FindName("AcceptButton");
			InviteEmailCommandButton = (AppBarButton)((FrameworkElement)this).FindName("InviteEmailCommandButton");
			InviteTextCommandButton = (AppBarButton)((FrameworkElement)this).FindName("InviteTextCommandButton");
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
		//IL_0071: Unknown result type (might be due to invalid IL or missing references)
		//IL_0077: Expected O, but got Unknown
		//IL_0098: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a2: Expected O, but got Unknown
		//IL_00a8: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ae: Expected O, but got Unknown
		//IL_00cf: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d9: Expected O, but got Unknown
		//IL_00df: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e5: Expected O, but got Unknown
		//IL_0106: Unknown result type (might be due to invalid IL or missing references)
		//IL_0110: Expected O, but got Unknown
		//IL_0116: Unknown result type (might be due to invalid IL or missing references)
		//IL_011c: Expected O, but got Unknown
		//IL_013d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0147: Expected O, but got Unknown
		//IL_014d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0153: Expected O, but got Unknown
		//IL_0174: Unknown result type (might be due to invalid IL or missing references)
		//IL_017e: Expected O, but got Unknown
		//IL_0184: Unknown result type (might be due to invalid IL or missing references)
		//IL_018a: Expected O, but got Unknown
		//IL_01ab: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b5: Expected O, but got Unknown
		//IL_01bb: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c1: Expected O, but got Unknown
		//IL_01e2: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ec: Expected O, but got Unknown
		//IL_01f2: Unknown result type (might be due to invalid IL or missing references)
		//IL_01f8: Expected O, but got Unknown
		//IL_0219: Unknown result type (might be due to invalid IL or missing references)
		//IL_0223: Expected O, but got Unknown
		//IL_0226: Unknown result type (might be due to invalid IL or missing references)
		//IL_022c: Expected O, but got Unknown
		//IL_024d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0257: Expected O, but got Unknown
		//IL_025a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0260: Expected O, but got Unknown
		//IL_0281: Unknown result type (might be due to invalid IL or missing references)
		//IL_028b: Expected O, but got Unknown
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
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(ConnectToTwitterButton_OnTapped));
			break;
		}
		case 6:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(ConnectToContactsButton_OnTapped));
			break;
		}
		case 7:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(FollowAll_OnTapped));
			break;
		}
		case 8:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(FollowAll_OnTapped));
			break;
		}
		case 9:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(AcceptButton_Click));
			break;
		}
		case 10:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(InviteEmail_OnClick));
			break;
		}
		case 11:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(InviteText_OnClick));
			break;
		}
		}
		_contentLoaded = true;
	}
}
