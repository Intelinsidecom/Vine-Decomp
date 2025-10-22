using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Xna.Framework.Input.Touch;
using Vine.Controls;
using Vine.Datas;
using Vine.Resources;
using Vine.Services.Models;

namespace Vine;

public class TutorialPage : PhoneApplicationPage
{
	private bool firsttime = true;

	internal Storyboard StoryboardSwipe;

	internal Storyboard StoryboardStart;

	internal Storyboard StoryboardShowButton;

	internal Grid TutoPanel;

	internal Grid LayoutRoot;

	internal PostControl Post;

	internal Grid Hand;

	internal Grid LegendPanel;

	internal TextBlock textBlock0;

	internal TextBlock textBlock;

	internal TextBlock textBlock1;

	internal TextBlock textBlock2;

	internal TextBlock textBlock3;

	internal Button button;

	internal Border border;

	internal Border border1;

	private bool _contentLoaded;

	public TutorialPage()
	{
		//IL_008d: Unknown result type (might be due to invalid IL or missing references)
		//IL_013b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0145: Expected O, but got Unknown
		//IL_0039: Unknown result type (might be due to invalid IL or missing references)
		//IL_0043: Expected O, but got Unknown
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)TutoPanel).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
		textBlock0.Text = "revine";
		textBlock3.Text = AppResources.SaveVideo;
		((FrameworkElement)LegendPanel).Margin = new Thickness(0.0, 60.0, 0.0, 0.0);
		Post.Post = new SamplePostRecord
		{
			UserName = "Rudy H.",
			Description = "tutorial sample",
			NbrComments = 42,
			NbrLikes = 23,
			AvatarUrl = "http://www.feelmygeek.com/6sec/tutoavatar.jpg",
			Thumb = "http://www.feelmygeek.com/6sec/tutorialThumb.jpg",
			Date = DateTime.UtcNow.AddMinutes(-4.0)
		};
		TouchPanel.EnabledGestures = GestureType.None;
		((Timeline)StoryboardSwipe).Completed += StoryboardSwipe_Completed;
		((Timeline)StoryboardStart).Completed += StoryboardStart_Completed;
		((FrameworkElement)this).Loaded += new RoutedEventHandler(Tuto_Loaded);
	}

	private void StoryboardStart_Completed(object sender, EventArgs e)
	{
		LaunchAnim(0.0);
	}

	private void StoryboardSwipe_Completed(object sender, EventArgs e)
	{
		LaunchAnim();
		if (firsttime)
		{
			firsttime = false;
			StoryboardShowButton.Begin();
		}
	}

	private void LaunchAnim(double sec = 2.0)
	{
		TimerHelper.ToTime(TimeSpan.FromSeconds(sec), delegate
		{
			Post.ResetFrontToBack();
			StoryboardSwipe.Stop();
			TimerHelper.ToTime(TimeSpan.FromSeconds(1.0), delegate
			{
				StoryboardSwipe.Begin();
			});
			TimerHelper.ToTime(TimeSpan.FromSeconds(2.1), delegate
			{
				Post.FrontToBack(fromDotsMenu: false);
			});
		});
	}

	protected void Tuto_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_002c: Unknown result type (might be due to invalid IL or missing references)
		((CompositeTransform)((UIElement)border).RenderTransform).TranslateX = 0.0 - ((FrameworkElement)border).ActualWidth;
		((CompositeTransform)((UIElement)border1).RenderTransform).TranslateX = 0.0 - ((FrameworkElement)border1).ActualWidth;
		StoryboardStart.Begin();
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		if (((Page)this).NavigationContext.QueryString.ContainsKey("removebackentry"))
		{
			((Page)this).NavigationService.RemoveBackEntry();
		}
	}

	private void Swipe_Completed(object sender, EventArgs e)
	{
	}

	private void Next_Click(object sender, RoutedEventArgs e)
	{
		Vine.Datas.Datas instance = DatasProvider.Instance;
		instance.DisplayedTutorial = true;
		instance.Save();
		NavigationServiceExt.ToTimeline(null, removebackentry: true);
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
		//IL_0161: Unknown result type (might be due to invalid IL or missing references)
		//IL_016b: Expected O, but got Unknown
		//IL_0177: Unknown result type (might be due to invalid IL or missing references)
		//IL_0181: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Tutorial/TutorialPage.xaml", UriKind.Relative));
			StoryboardSwipe = (Storyboard)((FrameworkElement)this).FindName("StoryboardSwipe");
			StoryboardStart = (Storyboard)((FrameworkElement)this).FindName("StoryboardStart");
			StoryboardShowButton = (Storyboard)((FrameworkElement)this).FindName("StoryboardShowButton");
			TutoPanel = (Grid)((FrameworkElement)this).FindName("TutoPanel");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			Post = (PostControl)((FrameworkElement)this).FindName("Post");
			Hand = (Grid)((FrameworkElement)this).FindName("Hand");
			LegendPanel = (Grid)((FrameworkElement)this).FindName("LegendPanel");
			textBlock0 = (TextBlock)((FrameworkElement)this).FindName("textBlock0");
			textBlock = (TextBlock)((FrameworkElement)this).FindName("textBlock");
			textBlock1 = (TextBlock)((FrameworkElement)this).FindName("textBlock1");
			textBlock2 = (TextBlock)((FrameworkElement)this).FindName("textBlock2");
			textBlock3 = (TextBlock)((FrameworkElement)this).FindName("textBlock3");
			button = (Button)((FrameworkElement)this).FindName("button");
			border = (Border)((FrameworkElement)this).FindName("border");
			border1 = (Border)((FrameworkElement)this).FindName("border1");
		}
	}
}
