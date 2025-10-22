using System;
using System.Collections;
using System.Diagnostics;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Gen.Services;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Reactive;
using Vine.Datas;
using Vine.Pages.Main.ViewModels;
using Vine.Pages.Search.ViewModels;
using Vine.Resources;
using Vine.Services;
using Vine.Services.Models;
using Vine.Utils;
using Vine.ViewModels;

namespace Vine.Pages.Search;

public class SearchPage : PhoneApplicationPage
{
	private bool _issearchtextboxFocuses;

	private bool _isInit;

	internal Grid LayoutRoot;

	internal VisualStateGroup VisualStateGroup;

	internal VisualState DirectSearch;

	internal VisualState Popular;

	internal StackPanel WhitePanel;

	internal Rectangle ShadowRectangle;

	internal TextBox SearchBox;

	internal Grid ContentPanel;

	internal Pivot ResultPivot;

	internal LongListSelector ListUser;

	internal PivotItem PivotTag;

	internal LongListSelector TagsList;

	internal Grid PopularPanel;

	internal ItemsControl ListPosts;

	private bool _contentLoaded;

	public SearchPage()
	{
		//IL_006b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0075: Expected O, but got Unknown
		//IL_00a8: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b2: Expected O, but got Unknown
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0049: Expected O, but got Unknown
		SearchViewModel searchStatic = ViewModelLocator.SearchStatic;
		((FrameworkElement)this).DataContext = searchStatic;
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)LayoutRoot).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
			((UIElement)ShadowRectangle).Visibility = (Visibility)0;
		}
		ListUser.ItemTemplate = (DataTemplate)((FrameworkElement)this).Resources[(object)"VineUserDataTemplate"];
		((UIElement)PopularPanel).Visibility = (Visibility)1;
		ResultPivot.SelectedItem = PivotTag;
		TagsList.ItemTemplate = (DataTemplate)((FrameworkElement)this).Resources[(object)"VineTag"];
		ObservableExtensions.Subscribe<IEvent<KeyEventArgs>>(Observable.Throttle<IEvent<KeyEventArgs>>(Observable.FromEvent<KeyEventArgs>((object)SearchBox, "KeyUp"), TimeSpan.FromMilliseconds(300.0)), (Action<IEvent<KeyEventArgs>>)delegate
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				Search(SearchBox.Text, clearOther: true);
			});
		});
	}

	private async void Search(string search, bool clearOther)
	{
		if (string.Compare(search, "rudyhfixit", StringComparison.InvariantCultureIgnoreCase) == 0)
		{
			await FixItHelper.Show();
			return;
		}
		SearchViewModel searchViewModel = (SearchViewModel)((FrameworkElement)this).DataContext;
		if (ResultPivot.SelectedIndex == 1)
		{
			searchViewModel.SearchTags(search, clearothers: true);
		}
		else
		{
			searchViewModel.SearchUsers(search, clearothers: true);
		}
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		//IL_0029: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Expected O, but got Unknown
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		((Page)this).OnNavigatedTo(e);
		if (!_isInit || (int)e.NavigationMode == 0)
		{
			_isInit = true;
			((FrameworkElement)this).Loaded += new RoutedEventHandler(SearchPage_Loaded);
			if (((Page)this).NavigationContext.QueryString.ContainsKey("selectFriends"))
			{
				VisualStateManager.GoToState((Control)(object)this, "DirectSearch", true);
				ResultPivot.SelectedIndex = 0;
			}
			else if (((Page)this).NavigationContext.QueryString.ContainsKey("searchtag"))
			{
				ResultPivot.SelectedIndex = 1;
				VisualStateManager.GoToState((Control)(object)this, "DirectSearch", true);
				string search = ((Page)this).NavigationContext.QueryString["searchtag"];
				Search(search, clearOther: true);
			}
			else if (((Page)this).NavigationContext.QueryString.ContainsKey("searchuser"))
			{
				ResultPivot.SelectedIndex = 0;
				VisualStateManager.GoToState((Control)(object)this, "DirectSearch", true);
				string search2 = ((Page)this).NavigationContext.QueryString["searchtag"];
				Search(search2, clearOther: true);
			}
		}
	}

	private void SearchPage_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_0012: Expected O, but got Unknown
		((FrameworkElement)this).Loaded -= new RoutedEventHandler(SearchPage_Loaded);
		((Control)SearchBox).Focus();
	}

	private void ListTags_ItemRealized(object sender, ItemRealizationEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		IList itemsSource = ((LongListSelector)sender).ItemsSource;
		if (itemsSource.Count >= 2)
		{
			if (e.Container.Content == itemsSource[itemsSource.Count - 2])
			{
				((SearchViewModel)((FrameworkElement)this).DataContext).LoadMoreTags(clear: false);
			}
		}
	}

	private void Tag_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_0040: Unknown result type (might be due to invalid IL or missing references)
		if (e.AddedItems.Count != 0 && e.AddedItems[0] != null)
		{
			NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForTag(((ITag)e.AddedItems[0]).Tag));
			((LongListSelector)sender).SelectedItem = null;
		}
	}

	private void ListUsers_ItemRealized(object sender, ItemRealizationEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		IList itemsSource = ((LongListSelector)sender).ItemsSource;
		if (itemsSource.Count >= 2)
		{
			SearchViewModel searchViewModel = (SearchViewModel)((FrameworkElement)this).DataContext;
			if (e.Container.Content == itemsSource[itemsSource.Count - 2])
			{
				searchViewModel.LoadMoreUsers(searchViewModel.Search, clear: false);
			}
		}
	}

	private void Users_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_0039: Unknown result type (might be due to invalid IL or missing references)
		if (e.AddedItems.Count != 0 && e.AddedItems[0] != null)
		{
			NavigationServiceExt.ToProfile(((IPerson)e.AddedItems[0]).Id);
			((LongListSelector)sender).SelectedItem = null;
		}
	}

	private async void Following_Click(object sender, RoutedEventArgs e)
	{
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		ToggleButton toggle = (ToggleButton)sender;
		((Control)toggle).IsEnabled = false;
		bool value = toggle.IsChecked.Value;
		IPerson user = (IPerson)((FrameworkElement)toggle).DataContext;
		try
		{
			if (!(await currentUser.Service.FollowUserAsync(user.Id, value)))
			{
				user.ChangeFollow(!value);
			}
			((Control)toggle).IsEnabled = true;
		}
		catch (ServiceServerErrorException ex)
		{
			((Control)toggle).IsEnabled = true;
			user.ChangeFollow(!value);
			if (ex.ReasonError == ServiceServerErrorType.CHECKPOINT)
			{
				ServiceUtils.ManageCheckPoint(ex.Checkpoint);
			}
			else
			{
				ToastHelper.Show(AppResources.ToastCantFollowThisPerson, afternav: false, (Orientation)0);
			}
		}
	}

	private void ViewProfile_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		NavigationServiceExt.ToProfile(((IPerson)((FrameworkElement)sender).DataContext).Id);
	}

	private void Pivot_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_0021: Unknown result type (might be due to invalid IL or missing references)
		if (_issearchtextboxFocuses)
		{
			((Control)SearchBox).Focus();
		}
		SearchViewModel searchViewModel = (SearchViewModel)((FrameworkElement)this).DataContext;
		if (((Pivot)sender).SelectedIndex == 1)
		{
			if (searchViewModel.ResultsTags.Count == 0)
			{
				searchViewModel.SearchTags(SearchBox.Text);
			}
		}
		else if (searchViewModel.ResultsUsers.Count == 0)
		{
			searchViewModel.SearchUsers(SearchBox.Text);
		}
	}

	protected void Thumb_Tap(object sender, RoutedEventArgs e)
	{
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		SearchViewModel searchViewModel = (SearchViewModel)((FrameworkElement)this).DataContext;
		IPostRecord postRecord = (IPostRecord)((FrameworkElement)sender).DataContext;
		MainPage.ForcedPosts = searchViewModel.PopularPosts.Take(searchViewModel.PopularPosts.Count - 1).ToList();
		if (!(postRecord is MorePost))
		{
			NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriPopular(), removebackentry: false, null, "&focuson=" + postRecord.PostId);
		}
		else
		{
			NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriPopular(), removebackentry: false, null, "&displaygrid=last&focuson=" + postRecord.PostId);
		}
	}

	private void SearchBox_GotFocus(object sender, RoutedEventArgs e)
	{
		_issearchtextboxFocuses = true;
	}

	private void SearchOnOtherApp_Click(object sender, EventArgs e)
	{
	}

	private void SearchBox_LostFocus(object sender, RoutedEventArgs e)
	{
		_issearchtextboxFocuses = false;
	}

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
		//IL_00dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e7: Expected O, but got Unknown
		//IL_00f3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fd: Expected O, but got Unknown
		//IL_0109: Unknown result type (might be due to invalid IL or missing references)
		//IL_0113: Expected O, but got Unknown
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0129: Expected O, but got Unknown
		//IL_0135: Unknown result type (might be due to invalid IL or missing references)
		//IL_013f: Expected O, but got Unknown
		//IL_014b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0155: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Search/SearchPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			VisualStateGroup = (VisualStateGroup)((FrameworkElement)this).FindName("VisualStateGroup");
			DirectSearch = (VisualState)((FrameworkElement)this).FindName("DirectSearch");
			Popular = (VisualState)((FrameworkElement)this).FindName("Popular");
			WhitePanel = (StackPanel)((FrameworkElement)this).FindName("WhitePanel");
			ShadowRectangle = (Rectangle)((FrameworkElement)this).FindName("ShadowRectangle");
			SearchBox = (TextBox)((FrameworkElement)this).FindName("SearchBox");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			ResultPivot = (Pivot)((FrameworkElement)this).FindName("ResultPivot");
			ListUser = (LongListSelector)((FrameworkElement)this).FindName("ListUser");
			PivotTag = (PivotItem)((FrameworkElement)this).FindName("PivotTag");
			TagsList = (LongListSelector)((FrameworkElement)this).FindName("TagsList");
			PopularPanel = (Grid)((FrameworkElement)this).FindName("PopularPanel");
			ListPosts = (ItemsControl)((FrameworkElement)this).FindName("ListPosts");
		}
	}
}
