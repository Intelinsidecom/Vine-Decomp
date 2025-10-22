using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading;
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

namespace Vine.Views;

public sealed class VineUserListView : BasePage, IIncrementalSource<VineUserModel>, IPullToRefresh, IComponentConnector
{
	private bool _hasError;

	private string _pageTitle;

	private bool _isBusy;

	private string _emptyText;

	private bool _isEmpty;

	private int? _pageCursor;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private BasePage Root;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private PullToRefreshListControl PullToRefreshView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

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

	public string PageTitle
	{
		get
		{
			return _pageTitle;
		}
		set
		{
			SetProperty(ref _pageTitle, value, "PageTitle");
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

	public string EmptyText
	{
		get
		{
			return _emptyText;
		}
		set
		{
			_emptyText = value;
		}
	}

	public IncrementalLoadingCollection<VineUserModel> Items { get; set; }

	public VineUserListViewParams Params => (VineUserListViewParams)base.NavigationObject;

	public bool IsFinishedLoading { get; set; }

	public bool IsEmpty
	{
		get
		{
			return _isEmpty;
		}
		set
		{
			SetProperty(ref _isEmpty, value, "IsEmpty");
		}
	}

	private string SearchText { get; set; }

	public bool ShowLoopCount { get; set; }

	public VineUserListView()
	{
		InitializeComponent();
		PullToRefreshView.AddReferences(ListView, this, 0.0);
		Items = new IncrementalLoadingCollection<VineUserModel>(this, PullToRefreshView);
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		if (!IsFinishedLoading)
		{
			switch (Params.Type)
			{
			case UserListType.Likes:
				App.ScribeService.Log(new ViewImpressionEvent(Section.None, "likes"));
				EmptyText = string.Format(ResourceHelper.GetString("FeedCellTextLikes"), new object[1] { 0 });
				PageTitle = ResourceHelper.GetString("LikesTitle");
				break;
			case UserListType.Revines:
				App.ScribeService.Log(new ViewImpressionEvent(Section.None, "revines"));
				EmptyText = string.Format(ResourceHelper.GetString("FeedCellTextReposts"), new object[1] { 0 });
				PageTitle = ResourceHelper.GetString("RevinesTitle");
				break;
			case UserListType.Followers:
				App.ScribeService.Log(new ViewImpressionEvent(Section.None, "followers"));
				EmptyText = string.Format(ResourceHelper.GetString("profile_followers_other"), new object[1] { 0 });
				PageTitle = ResourceHelper.GetString("FollowersTitle");
				break;
			case UserListType.Following:
				App.ScribeService.Log(new ViewImpressionEvent(Section.None, "following"));
				EmptyText = string.Format(ResourceHelper.GetString("profile_following_other"), new object[1] { 0 });
				PageTitle = ResourceHelper.GetString("FollowingTitle");
				break;
			case UserListType.Search:
				ShowLoopCount = true;
				PageTitle = Params.SearchText;
				SearchText = Params.SearchText;
				break;
			}
			if (e.PageState != null)
			{
				((UIElement)ListView).put_Opacity(0.0);
				SearchText = e.LoadValueOrDefault<string>("SearchText");
				_pageCursor = e.LoadValueOrDefault<int?>("_pageCursor");
				Items.CurrentPage = (int)e.LoadValueOrDefault<long>("Items.CurrentPage");
				Items.ResetItems(e.LoadValueOrDefault<List<VineUserModel>>("Items") ?? new List<VineUserModel>());
				double offset = e.LoadValueOrDefault<double>("_scrollViewer.VerticalOffset");
				await ((FrameworkElement)(object)PullToRefreshView.ScrollViewer).LayoutUpdatedAsync();
				await Task.Delay(250);
				await PullToRefreshView.UpdateListPadding();
				await PullToRefreshView.ScrollViewer.ScrollToVerticalOffsetSpin(offset);
				((UIElement)ListView).put_Opacity(1.0);
				IsEmpty = !Items.Any();
				ClearPageState();
				IsFinishedLoading = true;
			}
			else
			{
				IsFinishedLoading = !(await Refresh()).ApiResult.HasError;
			}
		}
	}

	private async Task<PagedItemsResult<VineUserModel>> Refresh()
	{
		_pageCursor = 1;
		VineUserListView vineUserListView = this;
		bool isEmpty = (HasError = false);
		vineUserListView.IsEmpty = isEmpty;
		PagedItemsResult<VineUserModel> pagedItemsResult = await Items.Refresh();
		if (pagedItemsResult.ApiResult.HasError)
		{
			if (Items.Any())
			{
				pagedItemsResult.ApiResult.PopUpErrorIfExists();
			}
			else
			{
				HasError = true;
				pagedItemsResult.ApiResult.LogError();
			}
		}
		else
		{
			Items.ResetItems(pagedItemsResult.ViewModels);
		}
		IsEmpty = !Items.Any() && !HasError;
		return pagedItemsResult;
	}

	private void Refresh_Click(object sender, RoutedEventArgs e)
	{
		Refresh();
	}

	public async Task PullToRefresh()
	{
		await Refresh();
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		e.PageState["_pageCursor"] = _pageCursor;
		e.PageState["Items.CurrentPage"] = (long)Items.CurrentPage;
		e.PageState["Items"] = Items.ToList();
		e.PageState["_scrollViewer.VerticalOffset"] = PullToRefreshView.ScrollViewer.VerticalOffset;
		e.PageState["SearchText"] = SearchText;
	}

	public async Task<PagedItemsResult<VineUserModel>> GetPagedItems(int page, int count, string anchor)
	{
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = true;
		});
		ApiResult<BaseVineResponseModel<VineUsersMetaModel>> result;
		switch (Params.Type)
		{
		case UserListType.Likes:
			result = await App.Api.GetPostLikes(Params.PostId, page, count);
			break;
		case UserListType.Revines:
			result = await App.Api.GetPostRevines(Params.PostId, page, count);
			break;
		case UserListType.Followers:
			result = await App.Api.GetFollowers(Params.UserId, page, count);
			break;
		case UserListType.Following:
			result = await App.Api.GetFollowing(Params.UserId, page, count);
			break;
		case UserListType.Notification:
			if (!_pageCursor.HasValue)
			{
				DispatcherEx.BeginInvoke(delegate
				{
					IsBusy = false;
				});
				return new PagedItemsResult<VineUserModel>
				{
					ApiResult = new EmptyApiResult(),
					ViewModels = new List<VineUserModel>()
				};
			}
			result = await App.Api.GetNotificationUsers(Params.ActivityId, page, count);
			if (!result.HasError)
			{
				_pageCursor = result.Model.Data.NextPage;
			}
			break;
		case UserListType.Search:
		{
			CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
			result = await App.Api.UserSearch(SearchText, cancellationTokenSource.Token, page, count);
			break;
		}
		default:
			throw new NotImplementedException();
		}
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = false;
		});
		return new PagedItemsResult<VineUserModel>
		{
			ApiResult = result,
			ViewModels = (result.HasError ? null : result.Model.Data.Records)
		};
	}

	private void User_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineUserModel vineUserModel = (VineUserModel)((FrameworkElement)sender).DataContext;
		((Page)this).Frame.Navigate(typeof(ProfileView), (object)vineUserModel.UserId);
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/VineUserListView.xaml"), (ComponentResourceLocation)0);
			Root = (BasePage)((FrameworkElement)this).FindName("Root");
			PullToRefreshView = (PullToRefreshListControl)((FrameworkElement)this).FindName("PullToRefreshView");
			ListView = (ListView)((FrameworkElement)this).FindName("ListView");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Expected O, but got Unknown
		//IL_0066: Unknown result type (might be due to invalid IL or missing references)
		//IL_0070: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			ButtonBase val2 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(Refresh_Click));
			break;
		}
		case 2:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(User_OnTapped));
			break;
		}
		}
		_contentLoaded = true;
	}
}
