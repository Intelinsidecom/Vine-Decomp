using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Gen.Services;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Vine.Pages.SelectDirectFriends.ViewModels;
using Vine.ViewModels;

namespace Vine.Pages.SelectDirectFriends;

public class SelectDirectFriendsPage : PhoneApplicationPage
{
	private string _mediaId;

	internal Grid MyCommentPanel;

	internal Grid Header;

	internal Rectangle RectBackground;

	internal Grid FriendsLongListSelectorPanel;

	internal LongListMultiSelector FriendsLongListSelector;

	internal ScrollViewer FriendsListScroll;

	internal TextBlock FriendsList;

	internal Button DirectSendButton;

	internal LongListMultiSelector SearchFriendsLongListSelector;

	internal Rectangle ShadowRectangle;

	private bool _contentLoaded;

	public SelectDirectFriendsPage()
	{
		//IL_0039: Unknown result type (might be due to invalid IL or missing references)
		//IL_0043: Expected O, but got Unknown
		//IL_0057: Unknown result type (might be due to invalid IL or missing references)
		//IL_0061: Expected O, but got Unknown
		SelectDirectFriendsViewModel selectDirectFriendsStatic = ViewModelLocator.SelectDirectFriendsStatic;
		selectDirectFriendsStatic.InitFilterOther();
		((FrameworkElement)this).DataContext = selectDirectFriendsStatic;
		InitializeComponent();
		((Shape)RectBackground).Fill = (Brush)Application.Current.Resources[(object)"NoStripeBrush"];
		((UIElement)ShadowRectangle).Visibility = (Visibility)0;
		((FrameworkElement)this).Loaded += new RoutedEventHandler(SelectDirectFriends_Loaded);
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		((Page)this).OnNavigatingFrom(e);
		((SelectDirectFriendsViewModel)((FrameworkElement)this).DataContext).BackupSelectedPerson(FriendsLongListSelector.SelectedItems.Cast<IPerson>().ToList());
	}

	public void SelectDirectFriends_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_0012: Expected O, but got Unknown
		((FrameworkElement)this).Loaded -= new RoutedEventHandler(SelectDirectFriends_Loaded);
		SelectDirectFriendsViewModel selectDirectFriendsViewModel = (SelectDirectFriendsViewModel)((FrameworkElement)this).DataContext;
		if (selectDirectFriendsViewModel.SelectedPerson == null)
		{
			return;
		}
		foreach (IPerson item in selectDirectFriendsViewModel.SelectedPerson)
		{
			FriendsLongListSelector.SelectedItems.Add(item);
		}
	}

	protected override void OnBackKeyPress(CancelEventArgs e)
	{
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		((PhoneApplicationPage)this).OnBackKeyPress(e);
		if ((int)((UIElement)SearchFriendsLongListSelector).Visibility == 0)
		{
			ManageSearchResult(saveresults: false);
			e.Cancel = true;
		}
	}

	private void ViewProfile_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		NavigationServiceExt.ToProfile(((IPerson)((FrameworkElement)sender).DataContext).Id);
	}

	private void FriendsList_OnSizeChanged(object sender, SizeChangedEventArgs e)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_000c: Unknown result type (might be due to invalid IL or missing references)
		ScrollViewer friendsListScroll = FriendsListScroll;
		Size newSize = e.NewSize;
		friendsListScroll.ScrollToHorizontalOffset(((Size)(ref newSize)).Width);
	}

	private void LongListMultiSelector_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		SelectDirectFriendsViewModel selectDirectFriendsViewModel = (SelectDirectFriendsViewModel)((FrameworkElement)this).DataContext;
		if (e.AddedItems.Count > 0 && FriendsLongListSelector.SelectedItems.Count + selectDirectFriendsViewModel.OtherItems.Count > 15)
		{
			foreach (object addedItem in e.AddedItems)
			{
				FriendsLongListSelector.SelectedItems.Remove(addedItem);
			}
		}
		ManageUpdateText();
	}

	private void SearchLongListMultiSelector_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_0091: Unknown result type (might be due to invalid IL or missing references)
		_ = (SelectDirectFriendsViewModel)((FrameworkElement)this).DataContext;
		if (e.AddedItems.Count > 0 && FriendsLongListSelector.SelectedItems.Count + SearchFriendsLongListSelector.SelectedItems.Count > 15)
		{
			foreach (object addedItem in e.AddedItems)
			{
				SearchFriendsLongListSelector.SelectedItems.Remove(addedItem);
			}
		}
		((ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[0]).IsEnabled = SearchFriendsLongListSelector.SelectedItems.Count > 0;
	}

	private void ManageUpdateText()
	{
		SelectDirectFriendsViewModel selectDirectFriendsViewModel = (SelectDirectFriendsViewModel)((FrameworkElement)this).DataContext;
		IList selectedItems = FriendsLongListSelector.SelectedItems;
		if ((selectDirectFriendsViewModel.OtherItems == null || selectDirectFriendsViewModel.OtherItems.Count == 0) && selectedItems.Count == 0)
		{
			FriendsList.Text = "";
			((Control)DirectSendButton).IsEnabled = false;
			return;
		}
		IEnumerable<IPerson> source = ((selectDirectFriendsViewModel.OtherItems != null && selectDirectFriendsViewModel.OtherItems.Count != 0) ? selectDirectFriendsViewModel.OtherItems.Concat(selectedItems.Cast<IPerson>()) : selectedItems.Cast<IPerson>());
		FriendsList.Text = source.Select((IPerson f) => f.Name).Aggregate((string a, string b) => a + ", " + b);
		((Control)DirectSendButton).IsEnabled = true;
	}

	private void DirectNext_Click(object sender, RoutedEventArgs e)
	{
		((Page)this).NavigationService.GoBack();
	}

	private void TextBox_OnTextChanged(object sender, TextChangedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Expected O, but got Unknown
		TextBox val = (TextBox)sender;
		if (!string.IsNullOrEmpty(val.Text))
		{
			((UIElement)FriendsLongListSelectorPanel).Visibility = (Visibility)1;
			((UIElement)SearchFriendsLongListSelector).Visibility = (Visibility)0;
			((SelectDirectFriendsViewModel)((FrameworkElement)this).DataContext).SearchUsers(val.Text);
			((PhoneApplicationPage)this).ApplicationBar.IsVisible = true;
		}
		else
		{
			((UIElement)FriendsLongListSelectorPanel).Visibility = (Visibility)0;
			((UIElement)SearchFriendsLongListSelector).Visibility = (Visibility)1;
			((PhoneApplicationPage)this).ApplicationBar.IsVisible = false;
		}
	}

	private void ManageSearchResult(bool saveresults)
	{
		((UIElement)FriendsLongListSelectorPanel).Visibility = (Visibility)0;
		((UIElement)SearchFriendsLongListSelector).Visibility = (Visibility)1;
		((PhoneApplicationPage)this).ApplicationBar.IsVisible = false;
		if (saveresults)
		{
			foreach (IPerson item in ((SelectDirectFriendsViewModel)((FrameworkElement)this).DataContext).AddOtherUsers(SearchFriendsLongListSelector.SelectedItems.Cast<IPerson>().ToList()))
			{
				if (!FriendsLongListSelector.SelectedItems.Contains(item))
				{
					FriendsLongListSelector.SelectedItems.Add(item);
				}
			}
			ManageUpdateText();
		}
		SearchFriendsLongListSelector.SelectedItems.Clear();
	}

	private void SearchResult_UnChecked(object sender, RoutedEventArgs e)
	{
		//IL_000c: Unknown result type (might be due to invalid IL or missing references)
		((SelectDirectFriendsViewModel)((FrameworkElement)this).DataContext).RemoveOtherUser((IPerson)((FrameworkElement)sender).DataContext);
		ManageUpdateText();
	}

	private void TextBox_GotFocus(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Expected O, but got Unknown
		TextBox val = (TextBox)sender;
		if (!string.IsNullOrEmpty(val.Text))
		{
			((SelectDirectFriendsViewModel)((FrameworkElement)this).DataContext).BackupSelectedPerson(FriendsLongListSelector.SelectedItems.Cast<IPerson>().ToList());
			((UIElement)FriendsLongListSelectorPanel).Visibility = (Visibility)1;
			((UIElement)SearchFriendsLongListSelector).Visibility = (Visibility)0;
			((SelectDirectFriendsViewModel)((FrameworkElement)this).DataContext).SearchUsers(val.Text);
			((PhoneApplicationPage)this).ApplicationBar.IsVisible = true;
		}
		else
		{
			((UIElement)FriendsLongListSelectorPanel).Visibility = (Visibility)0;
			((UIElement)SearchFriendsLongListSelector).Visibility = (Visibility)1;
			((PhoneApplicationPage)this).ApplicationBar.IsVisible = false;
		}
	}

	private void TextBox_LostFocus(object sender, RoutedEventArgs e)
	{
	}

	private void SelectSearchedPersons_Click(object sender, EventArgs e)
	{
		ManageSearchResult(saveresults: true);
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
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		//IL_00f3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fd: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/SelectDirectFriends/SelectDirectFriendsPage.xaml", UriKind.Relative));
			MyCommentPanel = (Grid)((FrameworkElement)this).FindName("MyCommentPanel");
			Header = (Grid)((FrameworkElement)this).FindName("Header");
			RectBackground = (Rectangle)((FrameworkElement)this).FindName("RectBackground");
			FriendsLongListSelectorPanel = (Grid)((FrameworkElement)this).FindName("FriendsLongListSelectorPanel");
			FriendsLongListSelector = (LongListMultiSelector)((FrameworkElement)this).FindName("FriendsLongListSelector");
			FriendsListScroll = (ScrollViewer)((FrameworkElement)this).FindName("FriendsListScroll");
			FriendsList = (TextBlock)((FrameworkElement)this).FindName("FriendsList");
			DirectSendButton = (Button)((FrameworkElement)this).FindName("DirectSendButton");
			SearchFriendsLongListSelector = (LongListMultiSelector)((FrameworkElement)this).FindName("SearchFriendsLongListSelector");
			ShadowRectangle = (Rectangle)((FrameworkElement)this).FindName("ShadowRectangle");
		}
	}
}
