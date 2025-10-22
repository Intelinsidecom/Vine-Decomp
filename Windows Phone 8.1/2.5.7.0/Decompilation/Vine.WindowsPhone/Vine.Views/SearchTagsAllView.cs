using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class SearchTagsAllView : BasePage, IIncrementalSource<VineTagModel>, IPullToRefresh, IComponentConnector
{
	private bool _isEmpty;

	private bool _showRetry;

	private bool _hasError;

	private string _pageTitle;

	private bool _isBusy;

	private string _emptyText;

	private string _errorText = ResourceHelper.GetString("ExploreSearchTagsError");

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private PullToRefreshListControl PullToRefreshView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public IncrementalLoadingCollection<VineTagModel> Items { get; set; }

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
			SetProperty(ref _emptyText, value, "EmptyText");
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

	private string SearchText { get; set; }

	public SearchTagsAllView()
	{
		InitializeComponent();
		PullToRefreshView.AddReferences(ListView, this, 0.0);
		Items = new IncrementalLoadingCollection<VineTagModel>(this, PullToRefreshView);
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		string searchText = NavigationParam as string;
		if (e.PageState != null)
		{
			((UIElement)ListView).put_Opacity(0.0);
			SearchTagsAllView searchTagsAllView = this;
			string pageTitle = (SearchText = e.LoadValueOrDefault<string>("SearchText"));
			searchTagsAllView.PageTitle = pageTitle;
			Items.CurrentPage = (int)e.LoadValueOrDefault<long>("Items.CurrentPage");
			Items.ResetItems(e.LoadValueOrDefault<List<VineTagModel>>("Items") ?? new List<VineTagModel>());
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
		else if (!string.IsNullOrWhiteSpace(searchText))
		{
			SearchTagsAllView searchTagsAllView2 = this;
			string pageTitle = (SearchText = (searchText.StartsWith("\"") ? Serialization.Deserialize<string>(searchText) : searchText));
			searchTagsAllView2.PageTitle = pageTitle;
			await Refresh();
		}
		else
		{
			IsEmpty = true;
		}
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		e.PageState["Items.CurrentPage"] = (long)Items.CurrentPage;
		e.PageState["Items"] = Items.ToList();
		e.PageState["_scrollViewer.VerticalOffset"] = PullToRefreshView.ScrollViewer.VerticalOffset;
		e.PageState["SearchText"] = SearchText;
	}

	private async Task<PagedItemsResult<VineTagModel>> Refresh()
	{
		IsBusy = true;
		SearchTagsAllView searchTagsAllView = this;
		bool isEmpty = (HasError = false);
		searchTagsAllView.IsEmpty = isEmpty;
		PagedItemsResult<VineTagModel> pagedItemsResult = await Items.Refresh();
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
				if (pagedItemsResult.ApiResult.HasConnectivityError)
				{
					ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
					ShowRetry = false;
				}
				else
				{
					ErrorText = ResourceHelper.GetString("ExploreSearchTagsError");
					ShowRetry = true;
				}
			}
		}
		else
		{
			Items.ResetItems(pagedItemsResult.ViewModels);
		}
		IsEmpty = !Items.Any() && !HasError;
		return pagedItemsResult;
	}

	public async Task PullToRefresh()
	{
		await Refresh();
	}

	public async Task<PagedItemsResult<VineTagModel>> GetPagedItems(int page, int count, string anchor)
	{
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = true;
		});
		CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
		ApiResult<BaseVineResponseModel<VineTagMetaModel>> apiResult = await App.Api.TagSearch(SearchText, cancellationTokenSource.Token, page, count);
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = false;
		});
		return new PagedItemsResult<VineTagModel>
		{
			ApiResult = apiResult,
			ViewModels = (apiResult.HasError ? null : apiResult.Model.Data.Records)
		};
	}

	private void TagItem_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineTagModel vineTagModel = (VineTagModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.Navigate(typeof(TagVineListView), (object)vineTagModel.Tag);
	}

	private async void Refresh_Click(object sender, RoutedEventArgs e)
	{
		await Refresh();
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/SearchTagsAllView.xaml"), (ComponentResourceLocation)0);
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
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(TagItem_OnTapped));
			break;
		}
		}
		_contentLoaded = true;
	}
}
