using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace Vine.Controls;

public class PullRefreshControl : UserControl
{
	internal Storyboard StoryboardExternal;

	internal Storyboard StoryboardInternal;

	internal Grid LayoutRoot;

	internal Grid InternalCircleExt;

	internal Path InternalCircle;

	internal Grid ExternalCircleExt;

	internal Path ExternalCircle;

	private bool _contentLoaded;

	public PullRefreshControl()
	{
		InitializeComponent();
		Reinit();
	}

	public void Turn()
	{
		StoryboardInternal.Begin();
		StoryboardExternal.Begin();
	}

	public void Reinit()
	{
		//IL_0022: Unknown result type (might be due to invalid IL or missing references)
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_0044: Expected O, but got Unknown
		//IL_004a: Unknown result type (might be due to invalid IL or missing references)
		//IL_004f: Unknown result type (might be due to invalid IL or missing references)
		//IL_006c: Expected O, but got Unknown
		StoryboardExternal.Stop();
		StoryboardInternal.Stop();
		Random random = new Random();
		((UIElement)InternalCircleExt).RenderTransform = (Transform)new RotateTransform
		{
			Angle = -180 + random.Next(360)
		};
		((UIElement)ExternalCircleExt).RenderTransform = (Transform)new RotateTransform
		{
			Angle = -180 + random.Next(360)
		};
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Controls/PullRefreshControl.xaml", UriKind.Relative));
			StoryboardExternal = (Storyboard)((FrameworkElement)this).FindName("StoryboardExternal");
			StoryboardInternal = (Storyboard)((FrameworkElement)this).FindName("StoryboardInternal");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			InternalCircleExt = (Grid)((FrameworkElement)this).FindName("InternalCircleExt");
			InternalCircle = (Path)((FrameworkElement)this).FindName("InternalCircle");
			ExternalCircleExt = (Grid)((FrameworkElement)this).FindName("ExternalCircleExt");
			ExternalCircle = (Path)((FrameworkElement)this).FindName("ExternalCircle");
		}
	}
}
