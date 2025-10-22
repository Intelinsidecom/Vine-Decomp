using System;
using System.Collections;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Media;
using Gen.Services;
using Microsoft.Phone.Controls;
using Vine.Datas;
using Vine.Pages.Notifications.ViewModels;

namespace Vine.Pages.Notifications;

public class NotificationsPage : PhoneApplicationPage
{
	internal Grid LayoutRoot;

	internal Grid ContentPanel;

	private bool _contentLoaded;

	public NotificationsPage()
	{
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0049: Expected O, but got Unknown
		NotificationsViewModel dataContext = new NotificationsViewModel();
		((FrameworkElement)this).DataContext = dataContext;
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)ContentPanel).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
	}

	private void Likes_ItemRealized(object sender, ItemRealizationEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		IList itemsSource = ((LongListSelector)sender).ItemsSource;
		if (itemsSource.Count >= 2)
		{
			if (e.Container.Content == itemsSource[itemsSource.Count - 2])
			{
				((NotificationsViewModel)((FrameworkElement)this).DataContext).LoadMore();
			}
		}
	}

	private void ViewProfile_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		NavigationServiceExt.ToProfile(((IPerson)((FrameworkElement)sender).DataContext).Id);
	}

	private async void Following_Click(object sender, RoutedEventArgs e)
	{
		ToggleButton toggle = (ToggleButton)sender;
		((Control)toggle).IsEnabled = false;
		bool value = toggle.IsChecked.Value;
		toggle.IsChecked = !toggle.IsChecked;
		bool flag = await DatasProvider.Instance.CurrentUser.Service.FollowUserAsync(((IPerson)((FrameworkElement)toggle).DataContext).Id, value);
		try
		{
			if (flag)
			{
				toggle.IsChecked = value;
			}
		}
		catch
		{
		}
		finally
		{
			((Control)toggle).IsEnabled = true;
		}
	}

	private void GoHome_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToTimeline(null, removebackentry: true);
	}

	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Notifications/NotificationsPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
		}
	}
}
