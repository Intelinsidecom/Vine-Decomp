using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading;
using System.Threading.Tasks;
using Vine.Events;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Tiles;
using Vine.Web;
using Windows.UI;
using Windows.UI.StartScreen;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Imaging;

namespace Vine.Views;

public sealed class SearchControl : BaseUserControl, IComponentConnector
{
	private bool _isBusy;

	private bool _isEmpty;

	private bool _hasError;

	private bool _showError;

	private bool _showRetry;

	private string _errorText;

	private bool _searchListVisible;

	private string _searchText;

	private bool _isActive;

	private string _baseEmptyMessage;

	private string _tileText;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private SearchSmallTile SearchSmallTile;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	public VineListControl VineList;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button HiddenButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox SearchTextBox;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public UserControlWrapper ControlWrapper { get; set; }

	public ObservableCollection<SearchResultModel> SearchResults { get; set; }

	public CancellationTokenSource Pending { get; set; }

	public ObservableCollection<VineRecentSearch> RecentSearches { get; set; }

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

	public bool ShowError
	{
		get
		{
			if (HasError)
			{
				return SearchListVisible;
			}
			return false;
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

	public bool SearchListVisible
	{
		get
		{
			return _searchListVisible;
		}
		set
		{
			SetProperty(ref _searchListVisible, value, "SearchListVisible");
			NotifyOfPropertyChange(() => PlaceHolderLabel);
			if (!value)
			{
				IsEmpty = false;
			}
		}
	}

	public string SearchText
	{
		get
		{
			return _searchText;
		}
		set
		{
			SetProperty(ref _searchText, value, "SearchText");
		}
	}

	public string LastSearchText { get; set; }

	public string PlaceHolderLabel => ResourceHelper.GetString("SearchHint");

	public string EmptyText
	{
		get
		{
			if (_baseEmptyMessage == null)
			{
				_baseEmptyMessage = ResourceHelper.GetString("empty_unified_search_message");
			}
			return string.Format(_baseEmptyMessage, new object[1] { SearchText });
		}
	}

	public string TileText
	{
		get
		{
			return _tileText;
		}
		set
		{
			SetProperty(ref _tileText, value, "TileText");
		}
	}

	public bool HasTile
	{
		get
		{
			if (!SecondaryTile.Exists(TileId) && !SecondaryTile.Exists("0" + TileId))
			{
				return SecondaryTile.Exists("1" + TileId);
			}
			return true;
		}
	}

	private string TileId => ("=" + SearchText).Replace(" ", string.Empty);

	public event EventHandler<FocusState> SearchFocus;

	public SearchControl()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		InitializeComponent();
		WindowsRuntimeMarshal.AddEventHandler((Func<KeyEventHandler, EventRegistrationToken>)((UIElement)this).add_KeyUp, (Action<EventRegistrationToken>)((UIElement)this).remove_KeyUp, new KeyEventHandler(OnKeyUp));
		SearchResults = new ObservableCollection<SearchResultModel>();
		RecentSearches = new ObservableCollection<VineRecentSearch>();
		ControlWrapper = new UserControlWrapper
		{
			Model = this
		};
	}

	private async void OnKeyUp(object sender, KeyRoutedEventArgs e)
	{
		string searchMode = "typing";
		if ((int)e.Key == 13)
		{
			((Control)HiddenButton).Focus((FocusState)3);
			searchMode = "search";
		}
		await Refresh(searchMode);
	}

	public async Task OnActivate(SearchView.SearchViewParams param)
	{
		if (!IsBusy)
		{
			if (!RecentSearches.Any())
			{
				List<VineRecentSearch> newItemsSource = await ApplicationSettings.Current.GetRecentSearches();
				RecentSearches.Repopulate(newItemsSource);
			}
			if (param != null)
			{
				SearchListVisible = true;
				VineList.ClearPlayingVine();
				await VineList.OnActivate();
				SearchText = param.SearchTerm;
				await Refresh("search");
				VineList.SetHeader();
				_isActive = true;
			}
			else if (SearchResults.Any())
			{
				SearchListVisible = true;
				EventAggregator.Current.Publish(new SearchPinChanged());
				await ((FrameworkElement)(object)VineList).LayoutUpdatedAsync();
				VineList.SetHeader();
				await VineList.OnActivate();
				await VineList.RefreshPlayingVine();
			}
			else
			{
				await VineList.OnActivate();
				await Refresh();
				await ((FrameworkElement)(object)VineList).LayoutUpdatedAsync();
				VineList.SetHeader();
			}
		}
	}

	public void OnDeactivate()
	{
		_isActive = false;
		VineList.OnDeactivate();
		ApplicationSettings.Current.SetRecentSearches(RecentSearches.ToList());
	}

	private async Task Refresh(string searchMode = "typing")
	{
		if (searchMode == "search" && !string.IsNullOrWhiteSpace(SearchText) && !RecentSearches.Any((VineRecentSearch s) => s.Query.Equals(SearchText, StringComparison.CurrentCultureIgnoreCase)))
		{
			if (RecentSearches.Count >= 3)
			{
				RecentSearches.RemoveAt(RecentSearches.Count - 1);
			}
			RecentSearches.Insert(0, new VineRecentSearch
			{
				Query = SearchText
			});
		}
		List<SearchResultModel> searchResults = new List<SearchResultModel>();
		List<VineViewModel> searchResultVines = new List<VineViewModel>();
		if ((string.IsNullOrWhiteSpace(SearchText) || SearchText.Length < 2) && RecentSearches.Any())
		{
			searchResults.Add(new SearchResultModel
			{
				HeaderText = ResourceHelper.GetString("tab_title_recent")
			});
			searchResults.AddRange(RecentSearches.Select((VineRecentSearch r) => new SearchResultModel
			{
				Recent = r
			}));
		}
		if (string.IsNullOrWhiteSpace(LastSearchText) || SearchText != LastSearchText || HasError || searchMode == "search")
		{
			IsBusy = true;
			if (Pending != null)
			{
				Pending.Cancel(throwOnFirstException: true);
				Pending = null;
			}
			CancellationTokenSource cancellationTokenSource = (Pending = new CancellationTokenSource());
			ApiResult<BaseVineResponseModel<VineUnifiedSearchMetaModel>> apiResult = await App.Api.UnifiedSearch(SearchText, cancellationTokenSource.Token, null, null, searchMode);
			if (apiResult.HasError && searchMode == "typing")
			{
				IsBusy = false;
				return;
			}
			if (!apiResult.HasError)
			{
				HasError = false;
				LastSearchText = SearchText;
				if (searchMode == "search" && (apiResult.Model.Data.Results == null || !apiResult.Model.Data.Results.Any()))
				{
					SearchResults.Clear();
					VineList.Items.Clear();
					IsEmpty = true;
					IsBusy = false;
					return;
				}
				IsEmpty = false;
				if (apiResult.Model.Data.Suggestions != null && apiResult.Model.Data.Suggestions.Any())
				{
					searchResults.Add(new SearchResultModel
					{
						HeaderText = ResourceHelper.GetString("ExploreSearchSuggestions")
					});
					searchResults.AddRange(apiResult.Model.Data.Suggestions.Select((VineSearchSuggestions s) => new SearchResultModel
					{
						Suggestion = s
					}));
				}
				if (apiResult.Model.Data.Results != null && apiResult.Model.Data.Results.Any())
				{
					VineUnifiedSearchResultMetaModel vineUnifiedSearchResultMetaModel = apiResult.Model.Data.Results.FirstOrDefault((VineUnifiedSearchResultMetaModel r) => r.Users != null);
					VineUnifiedSearchResultMetaModel vineUnifiedSearchResultMetaModel2 = apiResult.Model.Data.Results.FirstOrDefault((VineUnifiedSearchResultMetaModel r) => r.Posts != null);
					VineUnifiedSearchResultMetaModel vineUnifiedSearchResultMetaModel3 = apiResult.Model.Data.Results.FirstOrDefault((VineUnifiedSearchResultMetaModel r) => r.Tags != null);
					if (vineUnifiedSearchResultMetaModel != null && vineUnifiedSearchResultMetaModel.DisplayCount.GetValueOrDefault() > 0)
					{
						searchResults.Add(new SearchResultModel
						{
							HeaderText = ResourceHelper.GetString("ExploreSearchPeople"),
							HeaderViewAllVisible = true,
							SearchType = SearchType.People
						});
						searchResults.AddRange(from u in vineUnifiedSearchResultMetaModel.Users.Records.Take(vineUnifiedSearchResultMetaModel.DisplayCount.GetValueOrDefault())
							select new SearchResultModel
							{
								User = u
							});
					}
					if (vineUnifiedSearchResultMetaModel3 != null && vineUnifiedSearchResultMetaModel3.DisplayCount.GetValueOrDefault() > 0)
					{
						searchResults.Add(new SearchResultModel
						{
							HeaderText = ResourceHelper.GetString("ExploreSearchTags"),
							HeaderViewAllVisible = true,
							SearchType = SearchType.Tags
						});
						searchResults.AddRange(from t in vineUnifiedSearchResultMetaModel3.Tags.Records.Take(vineUnifiedSearchResultMetaModel3.DisplayCount.GetValueOrDefault())
							select new SearchResultModel
							{
								Tag = t
							});
					}
					if (vineUnifiedSearchResultMetaModel2 != null && vineUnifiedSearchResultMetaModel2.Posts.Records != null && vineUnifiedSearchResultMetaModel2.Posts.Records.Any())
					{
						Brush secondaryBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];
						searchResults.Add(new SearchResultModel
						{
							HeaderText = ResourceHelper.GetString("ExploreSearchPosts"),
							HeaderViewAllVisible = true,
							SearchType = SearchType.Posts
						});
						searchResultVines = vineUnifiedSearchResultMetaModel2.Posts.Records.Select((VineModel v) => new VineViewModel(v, secondaryBrush, Section.Explore, "search")).ToList();
					}
				}
				HasError = false;
			}
			else
			{
				HasError = true;
				if (apiResult.HasConnectivityError)
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
			SearchResults.Repopulate(searchResults);
			VineList.Items.Repopulate(searchResultVines);
			await VineList.RefreshPlayingVine();
			EventAggregator.Current.Publish(new SearchPinChanged());
		}
		IsBusy = false;
	}

	private void User_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		SearchResultModel searchResultModel = (SearchResultModel)((FrameworkElement)sender).DataContext;
		if (searchResultModel.IsUser)
		{
			VineUserModel user = searchResultModel.User;
			App.RootFrame.Navigate(typeof(ProfileView), (object)user.UserId);
		}
	}

	private void TagItem_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		SearchResultModel searchResultModel = (SearchResultModel)((FrameworkElement)sender).DataContext;
		if (searchResultModel.IsTag)
		{
			VineTagModel tag = searchResultModel.Tag;
			App.RootFrame.Navigate(typeof(TagVineListView), (object)tag.Tag);
		}
	}

	private async void Refresh_Click(object sender, RoutedEventArgs e)
	{
		await Refresh();
	}

	private async void SearchTextBox_GotFocus(object sender, RoutedEventArgs e)
	{
		if (this.SearchFocus != null)
		{
			object originalSource = e.OriginalSource;
			TextBox val = (TextBox)((originalSource is TextBox) ? originalSource : null);
			this.SearchFocus(this, ((Control)val).FocusState);
		}
		if (!SearchListVisible)
		{
			SearchListVisible = true;
			await Refresh();
		}
	}

	public void Reset()
	{
		SearchListVisible = false;
		LastSearchText = null;
		SearchText = string.Empty;
		VineList.ClearPlayingVine();
		VineList.Items.Clear();
		HasError = false;
		SearchResults.Clear();
		ApplicationSettings.Current.SetRecentSearches(RecentSearches.ToList());
	}

	public async Task PinUnPinCommandButton_OnClick(object sender, RoutedEventArgs e)
	{
		if (HasTile)
		{
			string tileId = TileId;
			if (SecondaryTile.Exists(tileId))
			{
				await TileHelper.DeleteSecondaryTile(tileId);
				return;
			}
			tileId = "0" + TileId;
			if (SecondaryTile.Exists(tileId))
			{
				await TileHelper.DeleteSecondaryTile(tileId);
				return;
			}
			tileId = "1" + TileId;
			if (SecondaryTile.Exists(tileId))
			{
				await TileHelper.DeleteSecondaryTile(tileId);
			}
		}
		else
		{
			TileText = SearchText;
			RenderTargetBitmap bitmap = new RenderTargetBitmap();
			await bitmap.RenderAsync((UIElement)(object)SearchSmallTile);
			await TileHelper.CreateSearchTile(SearchText, TileText, 0, TileId, bitmap);
		}
	}

	private async void SearchTextBox_OnLostFocus(object sender, RoutedEventArgs e)
	{
		await Task.Delay(1);
	}

	private async void Suggestion_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		SearchResultModel searchResultModel = (SearchResultModel)((FrameworkElement)sender).DataContext;
		if (searchResultModel.IsSearchSuggestion)
		{
			SearchText = searchResultModel.Suggestion.Query;
			await Refresh("search");
		}
	}

	private async void Recent_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		SearchResultModel searchResultModel = (SearchResultModel)((FrameworkElement)sender).DataContext;
		if (searchResultModel.IsRecent)
		{
			SearchText = searchResultModel.Recent.Query;
			await Refresh("search");
		}
	}

	private void RemoveSuggestion_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		SearchResultModel searchResultModel = (SearchResultModel)((FrameworkElement)sender).DataContext;
		if (!searchResultModel.IsRecent)
		{
			return;
		}
		e.put_Handled(true);
		RecentSearches.Remove(searchResultModel.Recent);
		SearchResults.Remove(searchResultModel);
		if (!SearchResults.Any((SearchResultModel sr) => sr.IsRecent))
		{
			SearchResultModel searchResultModel2 = SearchResults.FirstOrDefault((SearchResultModel sr) => sr.IsHeader && sr.HeaderText.Equals(ResourceHelper.GetString("tab_title_recent")));
			if (searchResultModel2 != null)
			{
				SearchResults.Remove(searchResultModel2);
			}
		}
	}

	private void Header_Tapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		SearchResultModel searchResultModel = (SearchResultModel)((FrameworkElement)sender).DataContext;
		if (searchResultModel.IsHeader && searchResultModel.HeaderViewAllVisible)
		{
			if (searchResultModel.SearchType == SearchType.Posts)
			{
				Color color = (Color)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenColor"];
				string color2 = color.R.ToString("X2") + color.G.ToString("X2") + color.B.ToString("X2");
				VineListViewParams param = new VineListViewParams
				{
					Title = SearchText,
					Type = ListType.Search,
					ChannelShowRecent = true,
					Color = color2
				};
				App.RootFrame.NavigateWithObject(typeof(ChannelVineListView), param);
			}
			else if (searchResultModel.SearchType == SearchType.People)
			{
				VineUserListViewParams param2 = new VineUserListViewParams
				{
					Type = UserListType.Search,
					SearchText = SearchText
				};
				App.RootFrame.NavigateWithObject(typeof(VineUserListView), param2);
			}
			else
			{
				App.RootFrame.NavigateWithObject(typeof(SearchTagsAllView), SearchText);
			}
		}
	}

	private async void ClearButton_Tapped(object sender, TappedRoutedEventArgs e)
	{
		SearchText = string.Empty;
		LastSearchText = SearchText;
		((Control)SearchTextBox).Focus((FocusState)2);
		await Refresh();
	}

	private async void Refresh_OnClick(object sender, RoutedEventArgs e)
	{
		await Refresh("search");
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/SearchControl.xaml"), (ComponentResourceLocation)0);
			SearchSmallTile = (SearchSmallTile)((FrameworkElement)this).FindName("SearchSmallTile");
			VineList = (VineListControl)((FrameworkElement)this).FindName("VineList");
			HiddenButton = (Button)((FrameworkElement)this).FindName("HiddenButton");
			SearchTextBox = (TextBox)((FrameworkElement)this).FindName("SearchTextBox");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_0038: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_0069: Unknown result type (might be due to invalid IL or missing references)
		//IL_006f: Expected O, but got Unknown
		//IL_0090: Unknown result type (might be due to invalid IL or missing references)
		//IL_009a: Expected O, but got Unknown
		//IL_00a0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a6: Expected O, but got Unknown
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		//IL_00d7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00dd: Expected O, but got Unknown
		//IL_00fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_0108: Expected O, but got Unknown
		//IL_010e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0114: Expected O, but got Unknown
		//IL_0135: Unknown result type (might be due to invalid IL or missing references)
		//IL_013f: Expected O, but got Unknown
		//IL_0145: Unknown result type (might be due to invalid IL or missing references)
		//IL_014b: Expected O, but got Unknown
		//IL_016c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0176: Expected O, but got Unknown
		//IL_017c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0182: Expected O, but got Unknown
		//IL_01a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ad: Expected O, but got Unknown
		//IL_01ae: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b4: Expected O, but got Unknown
		//IL_01d5: Unknown result type (might be due to invalid IL or missing references)
		//IL_01df: Expected O, but got Unknown
		//IL_01e2: Unknown result type (might be due to invalid IL or missing references)
		//IL_01e8: Expected O, but got Unknown
		//IL_0209: Unknown result type (might be due to invalid IL or missing references)
		//IL_0213: Expected O, but got Unknown
		//IL_0216: Unknown result type (might be due to invalid IL or missing references)
		//IL_021c: Expected O, but got Unknown
		//IL_023d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0247: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Header_Tapped));
			break;
		}
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Suggestion_OnTapped));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(User_OnTapped));
			break;
		}
		case 4:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(TagItem_OnTapped));
			break;
		}
		case 5:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Recent_OnTapped));
			break;
		}
		case 6:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(RemoveSuggestion_OnTapped));
			break;
		}
		case 7:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_LostFocus, (Action<EventRegistrationToken>)val2.remove_LostFocus, new RoutedEventHandler(SearchTextBox_OnLostFocus));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_GotFocus, (Action<EventRegistrationToken>)val2.remove_GotFocus, new RoutedEventHandler(SearchTextBox_GotFocus));
			break;
		}
		case 8:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(ClearButton_Tapped));
			break;
		}
		case 9:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Refresh_OnClick));
			break;
		}
		}
		_contentLoaded = true;
	}
}
