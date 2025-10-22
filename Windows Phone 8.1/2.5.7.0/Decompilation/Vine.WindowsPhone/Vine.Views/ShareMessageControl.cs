using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading;
using System.Threading.Tasks;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.ApplicationModel.Contacts;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class ShareMessageControl : BaseUserControl, IIncrementalSource<VineContactViewModel>, IComponentConnector
{
	public delegate void SelectionChangedHandler(object sender);

	private readonly List<VineContactViewModel> _friendsCache;

	private bool _isFiltering;

	private string _searchText;

	private bool _isSingleSelect;

	private bool _pplHasError;

	private bool _pplIsEmpty;

	public IReadOnlyList<Contact> PhoneContacts;

	private bool _isAddressBook;

	private bool _searchTextHasChanged;

	private bool _isFriendsEmpty;

	private bool _isContactsEmpty;

	private bool _isBusy;

	private bool _hasError;

	private string _errorText = ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR");

	private bool _showRetry = true;

	private string _friendsPagingAnchor;

	private static object _locker = new object();

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Style ListViewItemStyle1;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid LayoutRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button HiddenButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ToggleButton ContactsTypeToggle;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView FriendsListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ContactsListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid menuHost;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox SearchTextBox;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public List<VineContactViewModel> SelectedItems { get; set; }

	public string SearchText
	{
		get
		{
			return _searchText;
		}
		set
		{
			if (!(_searchText == value))
			{
				SetProperty(ref _searchText, value, "SearchText");
				SearchTextChanged();
			}
		}
	}

	public bool IsPplFinishedLoading { get; set; }

	public bool IsSingleSelect
	{
		get
		{
			return _isSingleSelect;
		}
		set
		{
			_isSingleSelect = value;
			OnPropertyChanged("IsSingleSelect");
		}
	}

	public bool PplHasError
	{
		get
		{
			return _pplHasError;
		}
		set
		{
			SetProperty(ref _pplHasError, value, "PplHasError");
		}
	}

	public bool PplIsEmpty
	{
		get
		{
			return _pplIsEmpty;
		}
		set
		{
			SetProperty(ref _pplIsEmpty, value, "PplIsEmpty");
		}
	}

	public CancellationTokenSource Pending { get; set; }

	public IncrementalLoadingCollection<VineContactViewModel> Friends { get; set; }

	public ObservableCollection<VineContactViewModel> Contacts { get; set; }

	public bool IsFriendsEmpty
	{
		get
		{
			return _isFriendsEmpty;
		}
		set
		{
			SetProperty(ref _isFriendsEmpty, value, "IsFriendsEmpty");
			NotifyOfPropertyChange(() => FriendsEmptyVisibility);
		}
	}

	public bool IsContactsEmpty
	{
		get
		{
			return _isContactsEmpty;
		}
		set
		{
			SetProperty(ref _isContactsEmpty, value, "IsContactsEmpty");
			NotifyOfPropertyChange(() => ContactsEmptyVisibility);
		}
	}

	public bool FriendsEmptyVisibility
	{
		get
		{
			if (IsFriendsEmpty)
			{
				return !_isAddressBook;
			}
			return false;
		}
	}

	public bool ContactsEmptyVisibility
	{
		get
		{
			if (IsContactsEmpty)
			{
				return _isAddressBook;
			}
			return false;
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

	public bool HasError
	{
		get
		{
			return _hasError;
		}
		set
		{
			SetProperty(ref _hasError, value, "HasError");
			NotifyOfPropertyChange(() => ErrorVisibility);
			NotifyOfPropertyChange(() => FriendsEmptyVisibility);
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

	public bool ErrorVisibility
	{
		get
		{
			if (HasError)
			{
				return !_isAddressBook;
			}
			return false;
		}
	}

	public bool IsFinishedLoading { get; set; }

	public event SelectionChangedHandler SelectionChanged;

	public ShareMessageControl()
	{
		//IL_0044: Unknown result type (might be due to invalid IL or missing references)
		//IL_004e: Expected O, but got Unknown
		InitializeComponent();
		WindowsRuntimeMarshal.AddEventHandler((Func<KeyEventHandler, EventRegistrationToken>)((UIElement)this).add_KeyUp, (Action<EventRegistrationToken>)((UIElement)this).remove_KeyUp, new KeyEventHandler(OnKeyUp));
		SelectedItems = new List<VineContactViewModel>();
		Friends = new IncrementalLoadingCollection<VineContactViewModel>(this);
		Contacts = new ObservableCollection<VineContactViewModel>();
		_friendsCache = new List<VineContactViewModel>();
		_isAddressBook = false;
		_isFiltering = false;
	}

	private void OnKeyUp(object sender, KeyRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0008: Invalid comparison between Unknown and I4
		if ((int)e.Key == 13)
		{
			((Control)HiddenButton).Focus((FocusState)3);
		}
	}

	private async Task SearchTextChanged()
	{
		_isFiltering = true;
		_searchTextHasChanged = true;
		if (_isAddressBook)
		{
			await RefreshAddressBook();
		}
		else if (string.IsNullOrEmpty(SearchText) || SearchText.Length != 1)
		{
			await Refresh();
		}
		_isFiltering = false;
	}

	public async Task Activate()
	{
		if (!IsFinishedLoading)
		{
			await Refresh();
			IsFinishedLoading = true;
		}
	}

	private async Task RefreshAddressBook()
	{
		IsBusy = true;
		CancellationTokenSource token = new CancellationTokenSource();
		lock (_locker)
		{
			if (Pending != null)
			{
				Pending.Cancel(throwOnFirstException: true);
				Pending = null;
			}
			Pending = token;
		}
		Contacts.Clear();
		await Task.Delay(100);
		if (token.IsCancellationRequested)
		{
			return;
		}
		List<VineContactViewModel> list = new List<VineContactViewModel>();
		string searchText = SearchText;
		await DispatcherEx.InvokeBackground(async delegate
		{
			_ = list;
			list = await PopulatePhoneContacts(searchText);
		});
		if (token.IsCancellationRequested)
		{
			return;
		}
		IsContactsEmpty = !list.Any();
		if (!IsContactsEmpty)
		{
			Contacts.Add(new VineContactViewModel
			{
				HeaderText = ResourceHelper.GetString("contacts")
			});
		}
		foreach (VineContactViewModel item in list)
		{
			Contacts.Add(item);
		}
		IsBusy = false;
	}

	private async Task Refresh()
	{
		IsFriendsEmpty = false;
		Contacts.Clear();
		Friends.ClearAndStop();
		HasError = false;
		PagedItemsResult<VineContactViewModel> pagedItemsResult = await Friends.Refresh();
		if (!pagedItemsResult.ApiResult.HasError)
		{
			Friends.ResetItems(pagedItemsResult.ViewModels);
		}
		else if (pagedItemsResult.ApiResult.HasConnectivityError)
		{
			ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
			ShowRetry = false;
		}
		else if (!pagedItemsResult.IsCancelled)
		{
			ErrorText = ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR");
			ShowRetry = true;
		}
		IsFriendsEmpty = !HasError && !Friends.Any();
		NotifyOfPropertyChange(() => FriendsEmptyVisibility);
	}

	public async Task<PagedItemsResult<VineContactViewModel>> GetPagedItems(int page, int count, string anchor)
	{
		string searchText = (string.IsNullOrEmpty(SearchText) ? string.Empty : SearchText.Trim());
		bool isSearching = searchText.Length > 1;
		CancellationTokenSource token;
		lock (_locker)
		{
			if (Pending != null)
			{
				Pending.Cancel(throwOnFirstException: true);
				Pending = null;
			}
			token = (Pending = new CancellationTokenSource());
		}
		if (!isSearching && page == 1 && _friendsCache.Any())
		{
			return new PagedItemsResult<VineContactViewModel>
			{
				ApiResult = new EmptyApiResult(),
				ViewModels = _friendsCache
			};
		}
		if (!isSearching && page > 1 && _friendsPagingAnchor == null)
		{
			return new PagedItemsResult<VineContactViewModel>
			{
				ApiResult = new EmptyApiResult(),
				ViewModels = new List<VineContactViewModel>()
			};
		}
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = true;
		});
		ApiResult<BaseVineResponseModel<VineUsersMetaModel>> apiResult = ((!isSearching) ? (await App.Api.GetFriends(ApplicationSettings.Current.UserId, count, _friendsPagingAnchor)) : (await App.Api.UserSearch(searchText, token.Token, page, count, "message")));
		List<VineContactViewModel> list = new List<VineContactViewModel>();
		if (!apiResult.HasError)
		{
			if (!isSearching)
			{
				_friendsPagingAnchor = apiResult.Model.Data.Anchor;
			}
			else if (page == 1)
			{
				foreach (VineContactViewModel friend in _friendsCache.Where((VineContactViewModel r) => r.User != null && (r.User.Username.ToLower().StartsWith(searchText.ToLower()) || r.User.Username.ToLower().Contains(" " + searchText.ToLower()))).ToList())
				{
					VineContactViewModel vineContactViewModel = SelectedItems.FirstOrDefault((VineContactViewModel r) => r.User.UserId == friend.User.UserId);
					if (vineContactViewModel == null)
					{
						vineContactViewModel = new VineContactViewModel
						{
							User = friend.User,
							Section = friend.Section,
							UserVisibility = (Visibility)(IsSingleSelect ? 1 : 0)
						};
					}
					list.Add(vineContactViewModel);
				}
			}
			PopulateFriendsList(apiResult, list, page, !isSearching);
			if (!isSearching)
			{
				_friendsCache.AddRange(list);
			}
		}
		else if (!token.IsCancellationRequested && page == 1)
		{
			DispatcherEx.BeginInvoke(delegate
			{
				HasError = true;
			});
		}
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = false;
		});
		return new PagedItemsResult<VineContactViewModel>
		{
			ApiResult = apiResult,
			ViewModels = list,
			IsCancelled = token.IsCancellationRequested
		};
	}

	private async Task<List<VineContactViewModel>> PopulatePhoneContacts(string searchText)
	{
		if (PhoneContacts == null)
		{
			ContactStore obj = await ContactManager.RequestStoreAsync();
			ShareMessageControl shareMessageControl = this;
			_ = shareMessageControl.PhoneContacts;
			shareMessageControl.PhoneContacts = await obj.FindContactsAsync();
		}
		List<Contact> list = PhoneContacts.ToList();
		if (!string.IsNullOrEmpty(searchText))
		{
			list = list.Where((Contact r) => r.DisplayName.StartsWith(searchText, StringComparison.CurrentCultureIgnoreCase) || r.Emails.Any((ContactEmail e) => e.Address.StartsWith(searchText, StringComparison.CurrentCultureIgnoreCase)) || r.Emails.Any((ContactEmail e) => e.Address.ToLower().Contains("@" + searchText)) || r.Emails.Any((ContactEmail e) => e.Address.ToLower().Contains("." + searchText)) || r.Phones.Any((ContactPhone p) => p.Number.Replace("(", "").Replace(")", "").Replace("-", "")
				.StartsWith(searchText.ToLower().Replace("(", "").Replace(")", "")
					.Replace("-", ""))) || r.FirstName.StartsWith(searchText, StringComparison.CurrentCultureIgnoreCase) || r.LastName.StartsWith(searchText, StringComparison.CurrentCultureIgnoreCase)).ToList();
		}
		List<VineContactViewModel> list2 = new List<VineContactViewModel>();
		foreach (Contact item in list)
		{
			if (!item.Emails.Any() && !item.Phones.Any())
			{
				continue;
			}
			string displayName = item.DisplayName;
			if (string.IsNullOrEmpty(displayName))
			{
				displayName = (item.Phones.Any() ? item.Phones[0].Number : item.Emails[0].Address);
			}
			VineContactViewModel vineContactViewModel = SelectedItems.FirstOrDefault((VineContactViewModel r) => r.User.Description == displayName && r.User.UserType == VineUserType.Contact);
			if (vineContactViewModel == null)
			{
				vineContactViewModel = new VineContactViewModel
				{
					EmailVisibility = (Visibility)(item.Emails.Count <= 0),
					PhoneVisibility = (Visibility)(item.Phones.Count <= 0),
					Phones = item.Phones.Select((ContactPhone p) => new Tuple<string, string>(((object)p.Kind/*cast due to .constrained prefix*/).ToString(), p.Number)).ToList(),
					Emails = item.Emails.Select((ContactEmail r) => r.Address).ToList(),
					User = new VineUserModel
					{
						UserType = VineUserType.Contact,
						Description = displayName
					}
				};
			}
			list2.Add(vineContactViewModel);
		}
		return list2;
	}

	private async void ContactsTypeClicked(object sender, RoutedEventArgs e)
	{
		_isAddressBook = ContactsTypeToggle.IsChecked == true;
		if (_isAddressBook && !ApplicationSettings.Current.IsAddressBookEnabled)
		{
			MessageDialog val = new MessageDialog("");
			val.put_Content(ResourceHelper.GetString("settings_address_book_desc"));
			val.put_Title(ResourceHelper.GetString("settings_address_book"));
			val.Commands.Add((IUICommand)new UICommand(ResourceHelper.GetString("settings_activate_account_confirm"), (UICommandInvokedHandler)null, (object)0));
			val.Commands.Add((IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1));
			val.put_CancelCommandIndex(1u);
			if ((int)(await val.ShowAsync()).Id == 1)
			{
				ContactsTypeToggle.put_IsChecked(!ContactsTypeToggle.IsChecked);
				return;
			}
			ApplicationSettings.Current.IsAddressBookEnabled = true;
		}
		((UIElement)ContactsListView).put_Visibility((Visibility)(!_isAddressBook));
		((UIElement)FriendsListView).put_Visibility((Visibility)(_isAddressBook ? 1 : 0));
		NotifyOfPropertyChange(() => FriendsEmptyVisibility);
		NotifyOfPropertyChange(() => ContactsEmptyVisibility);
		_isFiltering = true;
		if (_isAddressBook && (!Contacts.Any() || _searchTextHasChanged))
		{
			await RefreshAddressBook();
		}
		else if (!_isAddressBook && (!Friends.Any() || _searchTextHasChanged))
		{
			await Refresh();
		}
		_searchTextHasChanged = false;
		_isFiltering = false;
		NotifyOfPropertyChange(() => ErrorVisibility);
	}

	private void PopulateFriendsList(ApiResult<BaseVineResponseModel<VineUsersMetaModel>> friendsResults, List<VineContactViewModel> vineContactViewModels, int page, bool showHeader)
	{
		foreach (VineUserModel record in friendsResults.Model.Data.Records)
		{
			if (showHeader)
			{
				VineContactViewModel vineContactViewModel = (vineContactViewModels.Any() ? vineContactViewModels[vineContactViewModels.Count - 1] : null);
				if ((page == 1 && vineContactViewModel == null) || (vineContactViewModel != null && vineContactViewModel.User != null && vineContactViewModel.Section != record.Section))
				{
					VineContactViewModel vineContactViewModel2 = new VineContactViewModel();
					vineContactViewModel2.HeaderText = record.Section;
					vineContactViewModels.Add(vineContactViewModel2);
				}
			}
			VineContactViewModel vineContactViewModel3 = SelectedItems.FirstOrDefault((VineContactViewModel r) => r.User.UserId == record.UserId);
			if (vineContactViewModel3 == null)
			{
				vineContactViewModel3 = new VineContactViewModel
				{
					User = record,
					Section = record.Section,
					IsSelected = SelectedItems.Any((VineContactViewModel r) => r.User.UserId == record.UserId),
					UserVisibility = (Visibility)(IsSingleSelect ? 1 : 0)
				};
			}
			vineContactViewModels.Add(vineContactViewModel3);
		}
	}

	private void ToggleSelection(VineContactViewModel vm)
	{
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_002e: Expected O, but got Unknown
		//IL_0060: Unknown result type (might be due to invalid IL or missing references)
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		//IL_006c: Unknown result type (might be due to invalid IL or missing references)
		//IL_008d: Expected O, but got Unknown
		//IL_00b3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bd: Expected O, but got Unknown
		//IL_010e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0115: Expected O, but got Unknown
		//IL_0190: Unknown result type (might be due to invalid IL or missing references)
		//IL_019a: Expected O, but got Unknown
		if (vm.Emails.Count + vm.Phones.Count > 1)
		{
			MenuFlyout val = (MenuFlyout)FlyoutBase.GetAttachedFlyout((FrameworkElement)(object)menuHost);
			val.Items.Clear();
			if (vm.Emails.Count > 0)
			{
				foreach (string email in vm.Emails)
				{
					MenuFlyoutItem val2 = new MenuFlyoutItem();
					val2.put_Text(email);
					((FrameworkElement)val2).put_Tag((object)new MenuTag
					{
						Target = vm,
						Value = email,
						UserType = VineUserType.Email
					});
					MenuFlyoutItem val3 = val2;
					MenuFlyoutItem val4 = val3;
					WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val4.add_Click, (Action<EventRegistrationToken>)val4.remove_Click, new RoutedEventHandler(OnMenuClick));
					val.Items.Add((MenuFlyoutItemBase)(object)val3);
				}
			}
			if (vm.Phones.Count > 0)
			{
				foreach (Tuple<string, string> phone in vm.Phones)
				{
					MenuFlyoutItem val4 = new MenuFlyoutItem();
					val4.put_Text(string.Format("{0} - {1}", new object[2] { phone.Item1, phone.Item2 }));
					((FrameworkElement)val4).put_Tag((object)new MenuTag
					{
						Target = vm,
						Value = phone.Item2,
						UserType = VineUserType.Phone
					});
					MenuFlyoutItem val3 = val4;
					val4 = val3;
					WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val4.add_Click, (Action<EventRegistrationToken>)val4.remove_Click, new RoutedEventHandler(OnMenuClick));
					val.Items.Add((MenuFlyoutItemBase)(object)val3);
				}
			}
			((FlyoutBase)val).ShowAt((FrameworkElement)(object)menuHost);
			return;
		}
		if (vm.IsSelected)
		{
			vm.IsSelected = false;
			SelectedItems.Remove(vm);
		}
		else
		{
			if (vm.Emails.Count > 0)
			{
				vm.User.Email = vm.Emails[0];
			}
			else
			{
				vm.User.PhoneNumber = vm.Phones[0].Item2;
			}
			vm.IsSelected = true;
			SelectedItems.Add(vm);
		}
		if (this.SelectionChanged != null)
		{
			this.SelectionChanged(FriendsListView);
		}
	}

	private void OnMenuClick(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		MenuTag menuTag = (MenuTag)((FrameworkElement)sender).Tag;
		VineContactViewModel target = menuTag.Target;
		if (menuTag.UserType == VineUserType.Phone)
		{
			if (menuTag.Value == target.User.PhoneNumber)
			{
				target.User.PhoneNumber = null;
				target.PhoneSelectionVisibility = (Visibility)1;
			}
			else
			{
				target.User.PhoneNumber = menuTag.Value;
				target.PhoneSelectionVisibility = (Visibility)0;
			}
		}
		else if (menuTag.Value == target.User.Email)
		{
			target.User.Email = null;
			target.EmailSelectionVisibility = (Visibility)1;
		}
		else
		{
			target.User.Email = menuTag.Value;
			target.EmailSelectionVisibility = (Visibility)0;
		}
		if (string.IsNullOrEmpty(target.User.Email) && string.IsNullOrEmpty(target.User.PhoneNumber))
		{
			SelectedItems.Remove(target);
			target.IsSelected = false;
		}
		else if (!SelectedItems.Contains(target))
		{
			SelectedItems.Add(target);
			target.IsSelected = true;
		}
		if (this.SelectionChanged != null)
		{
			this.SelectionChanged(FriendsListView);
		}
	}

	private void ContactListView_OnSelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		if (_isFiltering)
		{
			return;
		}
		List<object> list = e.AddedItems.ToList();
		list.AddRange(e.RemovedItems.ToList());
		foreach (VineContactViewModel item in list)
		{
			if (item.User == null)
			{
				continue;
			}
			if (item.User.UserType == VineUserType.User)
			{
				item.IsSelected = !item.IsSelected;
				if (item.IsSelected)
				{
					SelectedItems.Add(item);
				}
				else
				{
					SelectedItems.Remove(item);
				}
				if (this.SelectionChanged != null)
				{
					this.SelectionChanged(sender);
				}
			}
			else
			{
				ToggleSelection(item);
			}
		}
	}

	private async void Refresh_OnClick(object sender, RoutedEventArgs e)
	{
		await Refresh();
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
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/ShareMessageControl.xaml"), (ComponentResourceLocation)0);
			ListViewItemStyle1 = (Style)((FrameworkElement)this).FindName("ListViewItemStyle1");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			HiddenButton = (Button)((FrameworkElement)this).FindName("HiddenButton");
			ContactsTypeToggle = (ToggleButton)((FrameworkElement)this).FindName("ContactsTypeToggle");
			FriendsListView = (ListView)((FrameworkElement)this).FindName("FriendsListView");
			ContactsListView = (ListView)((FrameworkElement)this).FindName("ContactsListView");
			menuHost = (Grid)((FrameworkElement)this).FindName("menuHost");
			SearchTextBox = (TextBox)((FrameworkElement)this).FindName("SearchTextBox");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_001e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0024: Expected O, but got Unknown
		//IL_0045: Unknown result type (might be due to invalid IL or missing references)
		//IL_004f: Expected O, but got Unknown
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
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ContactsTypeClicked));
			break;
		}
		case 2:
		{
			Selector val2 = (Selector)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<SelectionChangedEventHandler, EventRegistrationToken>)val2.add_SelectionChanged, (Action<EventRegistrationToken>)val2.remove_SelectionChanged, new SelectionChangedEventHandler(ContactListView_OnSelectionChanged));
			break;
		}
		case 3:
		{
			Selector val2 = (Selector)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<SelectionChangedEventHandler, EventRegistrationToken>)val2.add_SelectionChanged, (Action<EventRegistrationToken>)val2.remove_SelectionChanged, new SelectionChangedEventHandler(ContactListView_OnSelectionChanged));
			break;
		}
		case 4:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Refresh_OnClick));
			break;
		}
		}
		_contentLoaded = true;
	}
}
