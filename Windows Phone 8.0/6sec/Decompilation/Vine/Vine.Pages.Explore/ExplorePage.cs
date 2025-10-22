using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using Microsoft.Phone.Controls;
using Vine.Datas;
using Vine.Pages.Explore.ViewModels;
using Vine.Services;
using Vine.Services.Models;
using Vine.ViewModels;

namespace Vine.Pages.Explore;

public class ExplorePage : PhoneApplicationPage
{
	internal Grid LayoutRoot;

	internal Grid ContentPanel;

	internal Pivot ResultPivot;

	internal ItemsControl HighlightList;

	private bool _contentLoaded;

	public ExplorePage()
	{
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0049: Expected O, but got Unknown
		ExploreViewModel exploreStatic = ViewModelLocator.ExploreStatic;
		((FrameworkElement)this).DataContext = exploreStatic;
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)ContentPanel).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
	}

	private void OnTheRise_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToTimeline("users/trending");
	}

	private void Popular_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriPopular());
	}

	private void Channels_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Unknown result type (might be due to invalid IL or missing references)
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		Channel channel = (Channel)((FrameworkElement)sender).DataContext;
		NavigationServiceExt.ToTimeline("channels/" + channel.Id + "/popular", removebackentry: false, null, "&bg=" + VineGenUtils.ColorToString(channel.BackgroundColor) + "&fg=" + VineGenUtils.ColorToString(channel.ForegroundColor));
	}

	private void Trendings_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForTag((string)((FrameworkElement)sender).DataContext));
	}

	private void Favorite_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForTag((string)((FrameworkElement)sender).DataContext));
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Explore/ExplorePage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			ResultPivot = (Pivot)((FrameworkElement)this).FindName("ResultPivot");
			HighlightList = (ItemsControl)((FrameworkElement)this).FindName("HighlightList");
		}
	}
}
