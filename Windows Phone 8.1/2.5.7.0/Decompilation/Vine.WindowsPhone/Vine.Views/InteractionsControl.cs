using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Vine.Background;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Views.Capture;
using Vine.Web;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class InteractionsControl : NotifyPage, IIncrementalSource<InteractionModel>, IPullToRefresh, IComponentConnector
{
	private bool _isBusy;

	private bool _isEmpty;

	private bool _hasError;

	private string _errorText = ResourceHelper.GetString("CouldntLoadActivities");

	private bool _showRetry = true;

	private long _newCount;

	private bool _stopLoading;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private NotifyPage RootPage;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private PullToRefreshListControl PullToRefreshView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public IncrementalLoadingCollection<InteractionModel> Items { get; set; }

	public bool IsBusy
	{
		get
		{
			return _isBusy;
		}
		set
		{
			if (value)
			{
				IsEmpty = false;
				HasError = false;
			}
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

	public long NewCount
	{
		get
		{
			return _newCount;
		}
		set
		{
			SetProperty(ref _newCount, value, "NewCount");
		}
	}

	public bool IsFinishedLoading { get; set; }

	public bool IsActive { get; set; }

	public InteractionsControl()
	{
		InitializeComponent();
		PullToRefreshView.AddReferences(ListView, this, 65.0);
		Items = new IncrementalLoadingCollection<InteractionModel>(this, PullToRefreshView);
	}

	public async Task OnActivate()
	{
		IsActive = true;
		if (!IsFinishedLoading && !IsBusy)
		{
			IsFinishedLoading = !(await Refresh()).ApiResult.HasError;
		}
	}

	public async Task PullToRefresh()
	{
		await Refresh();
	}

	public async Task<PagedItemsResult<InteractionModel>> Refresh()
	{
		InteractionsControl interactionsControl = this;
		bool isEmpty = (HasError = false);
		interactionsControl.IsEmpty = isEmpty;
		PagedItemsResult<InteractionModel> pagedItemsResult = await Items.Refresh();
		if (pagedItemsResult.ApiResult.HasError)
		{
			if (Items.Any())
			{
				pagedItemsResult.ApiResult.PopUpErrorIfExists();
			}
			else
			{
				HasError = true;
				if (pagedItemsResult.ApiResult.HasConnectivityError)
				{
					ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
					ShowRetry = false;
				}
				else
				{
					ErrorText = ResourceHelper.GetString("CouldntLoadActivities");
					ShowRetry = true;
				}
				pagedItemsResult.ApiResult.LogError();
			}
		}
		else
		{
			Items.ResetItems(pagedItemsResult.ViewModels);
			NewCount = 0L;
			CheckActivityCount();
		}
		IsEmpty = !Items.Any() && !HasError;
		return pagedItemsResult;
	}

	public void OnDeactivate()
	{
		if (IsActive)
		{
			NewCount = 0L;
		}
		IsActive = false;
	}

	private async Task CheckActivityCount()
	{
		ApiResult<BaseVineResponseModel<VineMessageActivityCounts>> apiResult = await App.Api.GetActivityCounts(ApplicationSettings.Current.UserId);
		if (!apiResult.HasError && apiResult.Model != null && apiResult.Model.Data != null)
		{
			NewCount = apiResult.Model.Data.Notifications;
			BgLiveTiles.UpdateMainTileCount(apiResult.Model.Data.Messages + apiResult.Model.Data.Notifications);
			if (apiResult.Model.Data.Notifications == 0)
			{
				ToastHelper.Delete("notifications");
			}
			if (apiResult.Model.Data.Messages == 0)
			{
				ToastHelper.Delete("messages");
			}
			if (NewCount > 0)
			{
				IsFinishedLoading = false;
			}
		}
	}

	public void SetNewCount(long count)
	{
		NewCount = count;
		if (NewCount > 0)
		{
			IsFinishedLoading = false;
		}
	}

	public async Task<PagedItemsResult<InteractionModel>> GetPagedItems(int page, int count, string anchor)
	{
		PagedItemsResult<InteractionModel> result = new PagedItemsResult<InteractionModel>();
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = true;
		});
		ApiResult<BaseVineResponseModel<InteractionMetaModel>> interactionResults = await App.Api.GetNotifications(page, count);
		if (!interactionResults.HasError && interactionResults.Model.Data != null && interactionResults.Model.Data.Records != null && interactionResults.Model.Data.Records.RemoveAll((InteractionModel x) => x.InteractionType == InteractionType.Unknown) > 0)
		{
			Debugger.Break();
		}
		if (page == 1)
		{
			ApiResult<BaseVineResponseModel<InteractionMetaModel>> apiResult = await App.Api.GetFollowingApprovalsPending();
			if (!interactionResults.HasError && !apiResult.HasError)
			{
				result.ApiResult = interactionResults;
				result.ViewModels = AggItems(interactionResults, apiResult);
				_stopLoading = !interactionResults.Model.Data.NextPage.HasValue;
			}
			else if (interactionResults.HasError)
			{
				result.ApiResult = interactionResults;
			}
			else if (apiResult.HasError)
			{
				result.ApiResult = apiResult;
			}
		}
		else if (!_stopLoading)
		{
			result.ApiResult = interactionResults;
			if (!interactionResults.HasError && interactionResults.Model.Data != null)
			{
				result.ViewModels = interactionResults.Model.Data.Records;
				_stopLoading = !interactionResults.Model.Data.NextPage.HasValue;
			}
		}
		else
		{
			result.ApiResult = new EmptyApiResult();
		}
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = false;
		});
		return result;
	}

	private List<InteractionModel> AggItems(ApiResult<BaseVineResponseModel<InteractionMetaModel>> interactionResults, ApiResult<BaseVineResponseModel<InteractionMetaModel>> followingResults)
	{
		List<InteractionModel> list = ((followingResults.Model.Data == null) ? null : followingResults.Model.Data.Records.ToList());
		List<InteractionModel> list2 = ((interactionResults.Model.Data == null) ? null : interactionResults.Model.Data.Records.ToList());
		if (list != null && list.Any())
		{
			list.Insert(0, new InteractionModel
			{
				Type = InteractionType.Header.ToString(),
				HeaderText = ResourceHelper.GetString("FollowRequestsHeader")
			});
			if (list2 != null && list2.Any())
			{
				list2.Insert(0, new InteractionModel
				{
					Type = InteractionType.Header.ToString(),
					HeaderText = ResourceHelper.GetString("tab_title_activity")
				});
			}
		}
		List<InteractionModel> list3 = list ?? new List<InteractionModel>();
		if (list2 != null)
		{
			list3.AddRange(list2);
		}
		return list3;
	}

	private void Item_Tapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		InteractionModel item = (InteractionModel)((FrameworkElement)sender).DataContext;
		if (!NavigateToPost(item))
		{
			NavigateToProfile(item);
		}
	}

	private void Profile_Tapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		InteractionModel item = (InteractionModel)((FrameworkElement)sender).DataContext;
		NavigateToProfile(item);
	}

	private void Post_Tapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		InteractionModel item = (InteractionModel)((FrameworkElement)sender).DataContext;
		NavigateToPost(item);
	}

	private bool NavigateToProfile(InteractionModel item)
	{
		if (item.Entities != null && item.Entities.FirstOrDefault((Entity x) => x.EntityType == EntityType.user) != null)
		{
			Entity entity = item.Entities.FirstOrDefault((Entity x) => x.EntityType == EntityType.user);
			App.RootFrame.Navigate(typeof(ProfileView), (object)entity.Id);
			return true;
		}
		if (item.User != null)
		{
			App.RootFrame.Navigate(typeof(ProfileView), (object)item.User.UserId);
			return true;
		}
		return false;
	}

	private bool NavigateToPost(InteractionModel item)
	{
		if (item.Entities != null && item.Entities.FirstOrDefault((Entity x) => x.EntityType == EntityType.post) != null)
		{
			Entity entity = item.Entities.FirstOrDefault((Entity x) => x.EntityType == EntityType.post);
			App.RootFrame.NavigateWithObject(typeof(SingleVineView), new SingleVineViewParams
			{
				PostId = entity.Id,
				Section = Section.Activity
			});
			return true;
		}
		if (item.Entities != null && item.Entities.FirstOrDefault((Entity x) => x.EntityType == EntityType.commentList) != null)
		{
			string postId = item.Entities.FirstOrDefault((Entity x) => x.EntityType == EntityType.commentList).Link.Split(new char[1] { '/' }, StringSplitOptions.RemoveEmptyEntries)[2];
			App.RootFrame.NavigateWithObject(typeof(CommentsView), new CommentNavigationObject
			{
				IsFocused = true,
				PostId = postId,
				Section = Section.Activity
			});
			return true;
		}
		if (item.Post != null)
		{
			App.RootFrame.NavigateWithObject(typeof(SingleVineView), new SingleVineViewParams
			{
				PostId = item.Post.PostId,
				Section = Section.Activity
			});
			return true;
		}
		return false;
	}

	private void MilestoneGrid_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		InteractionModel interactionModel = (InteractionModel)((FrameworkElement)sender).DataContext;
		if (interactionModel.InteractionType == InteractionType.CampaignChannel)
		{
			string pattern = "\\d+";
			MatchCollection matchCollection = Regex.Matches(interactionModel.Milestone.MilestoneUrl, pattern);
			if (matchCollection.Count == 0)
			{
				string msg = ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR");
				ToastHelper.Show(ResourceHelper.GetString("vine"), msg, "msgBoxOnTap");
				return;
			}
			VineListViewParams param = new VineListViewParams
			{
				ChannelId = matchCollection[0].Value,
				Type = ListType.ChannelFeatured,
				Title = "In the Loop"
			};
			App.RootFrame.NavigateWithObject(typeof(TagVineListView), param);
			return;
		}
		if (interactionModel.InteractionType == InteractionType.First)
		{
			App.RootFrame.Navigate(CaptureViewHelper.GetCaptureView());
			return;
		}
		if (interactionModel.InteractionType == InteractionType.FirstExplore)
		{
			App.RootFrame.NavigateWithObject(typeof(HomeView), new ExploreControl.ExploreViewParams());
			return;
		}
		if (interactionModel.InteractionType == InteractionType.FirstFriendFinder)
		{
			App.RootFrame.Navigate(typeof(FriendFinderView));
			return;
		}
		if (interactionModel.InteractionType == InteractionType.FirstPopNow)
		{
			VineListViewParams param2 = new VineListViewParams
			{
				Title = ResourceHelper.GetString("PopularNow"),
				Type = ListType.PopularNow
			};
			App.RootFrame.NavigateWithObject(typeof(TagVineListView), param2);
			return;
		}
		if (interactionModel.Post != null)
		{
			App.RootFrame.NavigateWithObject(typeof(SingleVineView), new SingleVineViewParams
			{
				PostId = interactionModel.Post.PostId,
				Section = Section.Activity
			});
			return;
		}
		if (interactionModel.User != null)
		{
			App.RootFrame.Navigate(typeof(ProfileView), (object)interactionModel.User.UserId);
			return;
		}
		string text = interactionModel.Milestone.MilestoneUrl.Split(new char[1] { '/' }, StringSplitOptions.RemoveEmptyEntries)[1];
		if (text == "user-id")
		{
			App.RootFrame.Navigate(typeof(ProfileView), (object)interactionModel.Milestone.MilestoneUrl.Split(new char[1] { '/' }, StringSplitOptions.RemoveEmptyEntries)[2]);
		}
		else if (text == "post")
		{
			App.RootFrame.NavigateWithObject(typeof(SingleVineView), new SingleVineViewParams
			{
				PostId = interactionModel.Milestone.MilestoneUrl.Split(new char[1] { '/' }, StringSplitOptions.RemoveEmptyEntries)[2],
				Section = Section.Activity
			});
		}
	}

	public void ScrollToTop()
	{
		PullToRefreshView.ScrollToTopAnimated();
	}

	private void Refresh_Click(object sender, RoutedEventArgs e)
	{
		IsFinishedLoading = false;
		OnActivate();
	}

	public void Clear()
	{
		IsFinishedLoading = false;
		Items.ClearAndStop();
	}

	private void GroupedCount_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		Entity entity = ((InteractionModel)((FrameworkElement)sender).DataContext).Entities.FirstOrDefault();
		if (entity == null)
		{
			return;
		}
		string[] array = entity.Link.Split(new char[1] { '/' }, StringSplitOptions.RemoveEmptyEntries);
		if (array.Length == 4)
		{
			string text = array[2];
			string text2 = array[3];
			if (text2 == "comments")
			{
				App.RootFrame.NavigateWithObject(typeof(CommentsView), new CommentNavigationObject
				{
					IsFocused = true,
					PostId = text,
					Section = Section.Activity
				});
			}
			else if (text2 == "userList")
			{
				App.RootFrame.NavigateWithObject(typeof(VineUserListView), new VineUserListViewParams
				{
					Type = UserListType.Notification,
					ActivityId = text
				});
			}
		}
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/InteractionsControl.xaml"), (ComponentResourceLocation)0);
			RootPage = (NotifyPage)((FrameworkElement)this).FindName("RootPage");
			PullToRefreshView = (PullToRefreshListControl)((FrameworkElement)this).FindName("PullToRefreshView");
			ListView = (ListView)((FrameworkElement)this).FindName("ListView");
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
		//IL_01b0: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b6: Expected O, but got Unknown
		//IL_01d7: Unknown result type (might be due to invalid IL or missing references)
		//IL_01e1: Expected O, but got Unknown
		//IL_01e4: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ea: Expected O, but got Unknown
		//IL_020b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0215: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Profile_Tapped));
			break;
		}
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Item_Tapped));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Post_Tapped));
			break;
		}
		case 4:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Profile_Tapped));
			break;
		}
		case 5:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Item_Tapped));
			break;
		}
		case 6:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Post_Tapped));
			break;
		}
		case 7:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(MilestoneGrid_OnTapped));
			break;
		}
		case 8:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(GroupedCount_OnTapped));
			break;
		}
		case 9:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Refresh_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
