using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media.Animation;
using Microsoft.Phone.Tasks;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;

namespace Vine;

public class RateAppControl : UserControl
{
	public delegate void CloseEvent();

	internal Storyboard StoryboardStart;

	internal Grid grid;

	internal StackPanel LayoutRoot;

	internal TextBlock Title;

	private bool _contentLoaded;

	public event CloseEvent Closed;

	public RateAppControl()
	{
		//IL_0033: Unknown result type (might be due to invalid IL or missing references)
		//IL_003d: Expected O, but got Unknown
		InitializeComponent();
		try
		{
			Title.Text = string.Format(AppResources.RatingTitle, AppVersion.AppName);
		}
		catch
		{
		}
		((FrameworkElement)this).Loaded += new RoutedEventHandler(RateAppControl_Loaded);
	}

	private void RateAppControl_Loaded(object sender, RoutedEventArgs e)
	{
		StoryboardStart.Begin();
	}

	private void Yes_Click(object sender, RoutedEventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		new MarketplaceReviewTask().Show();
		DatasProvider.Instance.HasEvaluate = true;
		if (this.Closed != null)
		{
			this.Closed();
		}
	}

	private void Later_Click(object sender, RoutedEventArgs e)
	{
		if (this.Closed != null)
		{
			this.Closed();
		}
	}

	private void No_Click(object sender, RoutedEventArgs e)
	{
		DatasProvider.Instance.HasEvaluate = true;
		if (this.Closed != null)
		{
			this.Closed();
		}
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
			Application.LoadComponent((object)this, new Uri("/Vine;component/Controls/RateAppControl.xaml", UriKind.Relative));
			StoryboardStart = (Storyboard)((FrameworkElement)this).FindName("StoryboardStart");
			grid = (Grid)((FrameworkElement)this).FindName("grid");
			LayoutRoot = (StackPanel)((FrameworkElement)this).FindName("LayoutRoot");
			Title = (TextBlock)((FrameworkElement)this).FindName("Title");
		}
	}
}
