using System;
using System.Threading;
using System.Windows;
using System.Windows.Media.Animation;

namespace Vine.Utils;

public static class StoryboardHelper
{
	public static void BeginWithPrelaunch(this Storyboard story, FrameworkElement item, int timeout = int.MinValue, Action timeoutcallback = null)
	{
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Expected O, but got Unknown
		//IL_001f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0024: Unknown result type (might be due to invalid IL or missing references)
		//IL_0030: Unknown result type (might be due to invalid IL or missing references)
		//IL_0036: Unknown result type (might be due to invalid IL or missing references)
		//IL_0041: Expected O, but got Unknown
		//IL_005a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0064: Expected O, but got Unknown
		//IL_0064: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a8: Unknown result type (might be due to invalid IL or missing references)
		Storyboard val = new Storyboard();
		ObjectAnimationUsingKeyFrames val2 = new ObjectAnimationUsingKeyFrames();
		DiscreteObjectKeyFrame val3 = new DiscreteObjectKeyFrame
		{
			Value = (object)(Visibility)0,
			KeyTime = KeyTime.FromTimeSpan(TimeSpan.Zero)
		};
		((PresentationFrameworkCollection<ObjectKeyFrame>)(object)val2.KeyFrames).Add((ObjectKeyFrame)(object)val3);
		Storyboard.SetTarget((Timeline)(object)val2, (DependencyObject)(object)item);
		Storyboard.SetTargetProperty((Timeline)(object)val2, new PropertyPath((object)UIElement.VisibilityProperty));
		((PresentationFrameworkCollection<Timeline>)(object)val.Children).Add((Timeline)(object)val2);
		bool started = false;
		Timer timer = null;
		if (timeout > 0 && timeoutcallback != null)
		{
			timer = new Timer(delegate
			{
				if (!started)
				{
					started = true;
					timeoutcallback();
				}
			}, null, timeout, int.MaxValue);
		}
		((Timeline)val).Completed += delegate
		{
			if (!started)
			{
				started = true;
				if (timer != null)
				{
					timer.Change(int.MaxValue, int.MaxValue);
					timer = null;
				}
				story.Begin();
			}
		};
		val.Begin();
	}
}
