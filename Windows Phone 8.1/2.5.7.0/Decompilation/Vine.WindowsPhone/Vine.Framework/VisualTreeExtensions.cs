using System.Collections.Generic;
using System.Linq;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media;

namespace Vine.Framework;

public static class VisualTreeExtensions
{
	public static T GetFirstLogicalChildByType<T>(this FrameworkElement parent, bool applyTemplates) where T : FrameworkElement
	{
		Queue<FrameworkElement> queue = new Queue<FrameworkElement>();
		queue.Enqueue(parent);
		while (queue.Count > 0)
		{
			FrameworkElement val = queue.Dequeue();
			Control val2 = (Control)(object)((val is Control) ? val : null);
			if (applyTemplates && val2 != null)
			{
				val2.ApplyTemplate();
			}
			if (val is T && val != parent)
			{
				return (T)(object)val;
			}
			foreach (FrameworkElement item in ((DependencyObject)(object)val).GetVisualChildren().OfType<FrameworkElement>())
			{
				queue.Enqueue(item);
			}
		}
		return default(T);
	}

	public static IEnumerable<T> GetLogicalChildrenByType<T>(this FrameworkElement parent, bool applyTemplates) where T : FrameworkElement
	{
		if (applyTemplates && parent is Control)
		{
			((Control)parent).ApplyTemplate();
		}
		Queue<FrameworkElement> queue = new Queue<FrameworkElement>(((DependencyObject)(object)parent).GetVisualChildren().OfType<FrameworkElement>());
		while (queue.Count > 0)
		{
			FrameworkElement element = queue.Dequeue();
			if (applyTemplates && element is Control)
			{
				((Control)element).ApplyTemplate();
			}
			if (element is T)
			{
				yield return (T)(object)element;
			}
			foreach (FrameworkElement item in ((DependencyObject)(object)element).GetVisualChildren().OfType<FrameworkElement>())
			{
				queue.Enqueue(item);
			}
		}
	}

	public static IEnumerable<DependencyObject> GetVisualChildren(this DependencyObject parent)
	{
		int childCount = VisualTreeHelper.GetChildrenCount(parent);
		for (int counter = 0; counter < childCount; counter++)
		{
			yield return VisualTreeHelper.GetChild(parent, counter);
		}
	}

	public static IEnumerable<FrameworkElement> GetLogicalChildrenBreadthFirst(this FrameworkElement parent)
	{
		Queue<FrameworkElement> queue = new Queue<FrameworkElement>(((DependencyObject)(object)parent).GetVisualChildren().OfType<FrameworkElement>());
		while (queue.Count > 0)
		{
			FrameworkElement element = queue.Dequeue();
			yield return element;
			foreach (FrameworkElement item in ((DependencyObject)(object)element).GetVisualChildren().OfType<FrameworkElement>())
			{
				queue.Enqueue(item);
			}
		}
	}
}
