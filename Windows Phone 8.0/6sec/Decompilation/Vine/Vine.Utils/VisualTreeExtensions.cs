using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Media;

namespace Vine.Utils;

public static class VisualTreeExtensions
{
	public static IEnumerable<DependencyObject> GetVisualAncestors(this DependencyObject element)
	{
		if (element == null)
		{
			throw new ArgumentNullException("element");
		}
		return GetVisualAncestorsAndSelfIterator(element).Skip(1);
	}

	public static IEnumerable<DependencyObject> GetVisualAncestorsAndSelf(this DependencyObject element)
	{
		if (element == null)
		{
			throw new ArgumentNullException("element");
		}
		return GetVisualAncestorsAndSelfIterator(element);
	}

	private static IEnumerable<DependencyObject> GetVisualAncestorsAndSelfIterator(DependencyObject element)
	{
		for (DependencyObject dependencyObject = element; dependencyObject != null; dependencyObject = VisualTreeHelper.GetParent(dependencyObject))
		{
			yield return dependencyObject;
		}
	}

	public static IEnumerable<T> GetVisualChildren<T>(this DependencyObject target) where T : DependencyObject
	{
		return (from child in target.GetVisualChildren()
			where child is T
			select child).Cast<T>();
	}

	public static IEnumerable<T> GetVisualChildren<T>(this DependencyObject target, bool strict) where T : DependencyObject
	{
		return (from child in target.GetVisualChildren(strict)
			where child is T
			select child).Cast<T>();
	}

	public static IEnumerable<DependencyObject> GetVisualChildren(this DependencyObject target, bool strict)
	{
		int childrenCount = VisualTreeHelper.GetChildrenCount(target);
		if (childrenCount == 0)
		{
			if (!strict && target is ContentControl)
			{
				object content = ((ContentControl)target).Content;
				DependencyObject val = (DependencyObject)((content is DependencyObject) ? content : null);
				if (val != null)
				{
					yield return val;
				}
			}
		}
		else
		{
			for (int i = 0; i < childrenCount; i++)
			{
				yield return VisualTreeHelper.GetChild(target, i);
			}
		}
	}

	public static IEnumerable<DependencyObject> GetVisualChildren(this DependencyObject element)
	{
		if (element == null)
		{
			throw new ArgumentNullException("element");
		}
		return element.GetVisualChildrenAndSelfIterator().Skip(1);
	}

	public static IEnumerable<DependencyObject> GetVisualChildrenAndSelf(this DependencyObject element)
	{
		if (element == null)
		{
			throw new ArgumentNullException("element");
		}
		return element.GetVisualChildrenAndSelfIterator();
	}

	private static IEnumerable<DependencyObject> GetVisualChildrenAndSelfIterator(this DependencyObject element)
	{
		yield return element;
		int childrenCount = VisualTreeHelper.GetChildrenCount(element);
		for (int i = 0; i < childrenCount; i++)
		{
			yield return VisualTreeHelper.GetChild(element, i);
		}
	}

	private static IEnumerable<DependencyObject> GetVisualDecendants(DependencyObject target, bool strict, Queue<DependencyObject> queue)
	{
		foreach (DependencyObject visualChild in target.GetVisualChildren(strict))
		{
			queue.Enqueue(visualChild);
		}
		if (queue.Count == 0)
		{
			yield break;
		}
		DependencyObject dependencyObject = queue.Dequeue();
		yield return dependencyObject;
		foreach (DependencyObject visualDecendant in GetVisualDecendants(dependencyObject, strict, queue))
		{
			yield return visualDecendant;
		}
	}

	private static IEnumerable<DependencyObject> GetVisualDecendants(DependencyObject target, bool strict, Stack<DependencyObject> stack)
	{
		foreach (DependencyObject visualChild in target.GetVisualChildren(strict))
		{
			stack.Push(visualChild);
		}
		if (stack.Count == 0)
		{
			yield break;
		}
		DependencyObject dependencyObject = stack.Pop();
		yield return dependencyObject;
		foreach (DependencyObject visualDecendant in GetVisualDecendants(dependencyObject, strict, stack))
		{
			yield return visualDecendant;
		}
	}

	public static IEnumerable<DependencyObject> GetVisualDescendants(this DependencyObject element)
	{
		if (element == null)
		{
			throw new ArgumentNullException("element");
		}
		return GetVisualDescendantsAndSelfIterator(element).Skip(1);
	}

	public static IEnumerable<DependencyObject> GetVisualDescendantsAndSelf(this DependencyObject element)
	{
		if (element == null)
		{
			throw new ArgumentNullException("element");
		}
		return GetVisualDescendantsAndSelfIterator(element);
	}

	private static IEnumerable<DependencyObject> GetVisualDescendantsAndSelfIterator(DependencyObject element)
	{
		Queue<DependencyObject> queue = new Queue<DependencyObject>();
		queue.Enqueue(element);
		while (queue.Count > 0)
		{
			DependencyObject dependencyObject = queue.Dequeue();
			yield return dependencyObject;
			foreach (DependencyObject visualChild in dependencyObject.GetVisualChildren())
			{
				queue.Enqueue(visualChild);
			}
		}
	}

	public static IEnumerable<DependencyObject> GetVisualSiblings(this DependencyObject element)
	{
		return from p in element.GetVisualSiblingsAndSelf()
			where p != element
			select p;
	}

	public static IEnumerable<DependencyObject> GetVisualSiblingsAndSelf(this DependencyObject element)
	{
		if (element == null)
		{
			throw new ArgumentNullException("element");
		}
		DependencyObject parent = VisualTreeHelper.GetParent(element);
		if (parent != null)
		{
			return parent.GetVisualChildren();
		}
		return Enumerable.Empty<DependencyObject>();
	}

	public static Rect? GetBoundsRelativeTo(this FrameworkElement element, UIElement otherElement)
	{
		//IL_002b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0031: Unknown result type (might be due to invalid IL or missing references)
		//IL_0048: Unknown result type (might be due to invalid IL or missing references)
		//IL_0056: Unknown result type (might be due to invalid IL or missing references)
		//IL_0057: Unknown result type (might be due to invalid IL or missing references)
		//IL_0058: Unknown result type (might be due to invalid IL or missing references)
		if (element == null)
		{
			throw new ArgumentNullException("element");
		}
		if (otherElement == null)
		{
			throw new ArgumentNullException("otherElement");
		}
		try
		{
			GeneralTransform val = ((UIElement)element).TransformToVisual(otherElement);
			Point val2 = default(Point);
			Point val3 = default(Point);
			if (val != null && val.TryTransform(default(Point), ref val2) && val.TryTransform(new Point(element.ActualWidth, element.ActualHeight), ref val3))
			{
				return new Rect(val2, val3);
			}
		}
		catch (ArgumentException)
		{
		}
		return null;
	}

	public static T FindVisualChild<T>(DependencyObject obj) where T : DependencyObject
	{
		for (int i = 0; i < VisualTreeHelper.GetChildrenCount(obj); i++)
		{
			DependencyObject child = VisualTreeHelper.GetChild(obj, i);
			if (child != null && child is T)
			{
				return (T)(object)child;
			}
			T val = FindVisualChild<T>(child);
			if (val != null)
			{
				return val;
			}
		}
		return default(T);
	}

	public static FrameworkElement FindVisualChild(this FrameworkElement root, string name)
	{
		//IL_0026: Unknown result type (might be due to invalid IL or missing references)
		object obj = root.FindName(name);
		FrameworkElement val = (FrameworkElement)((obj is FrameworkElement) ? obj : null);
		if (val != null)
		{
			return val;
		}
		foreach (FrameworkElement visualChild in ((DependencyObject)(object)root).GetVisualChildren())
		{
			object obj2 = visualChild.FindName(name);
			val = (FrameworkElement)((obj2 is FrameworkElement) ? obj2 : null);
			if (val != null)
			{
				return val;
			}
		}
		return null;
	}

	public static IEnumerable<DependencyObject> GetVisuals(this DependencyObject root)
	{
		int childrenCount = VisualTreeHelper.GetChildrenCount(root);
		for (int i = 0; i < childrenCount; i++)
		{
			DependencyObject child = VisualTreeHelper.GetChild(root, i);
			yield return child;
			foreach (DependencyObject visual in child.GetVisuals())
			{
				yield return visual;
			}
		}
	}

	public static FrameworkElement GetVisualChild(this FrameworkElement node, int index)
	{
		DependencyObject child = VisualTreeHelper.GetChild((DependencyObject)(object)node, index);
		return (FrameworkElement)(object)((child is FrameworkElement) ? child : null);
	}

	public static FrameworkElement GetVisualParent(this FrameworkElement node)
	{
		DependencyObject parent = VisualTreeHelper.GetParent((DependencyObject)(object)node);
		return (FrameworkElement)(object)((parent is FrameworkElement) ? parent : null);
	}

	public static VisualStateGroup GetVisualStateGroup(this FrameworkElement root, string groupName, bool searchAncestors)
	{
		foreach (object visualStateGroup in VisualStateManager.GetVisualStateGroups(root))
		{
			VisualStateGroup val = (VisualStateGroup)((visualStateGroup is VisualStateGroup) ? visualStateGroup : null);
			if (val != null && val.Name == groupName)
			{
				return val;
			}
		}
		if (searchAncestors)
		{
			FrameworkElement visualParent = root.GetVisualParent();
			if (visualParent != null)
			{
				return visualParent.GetVisualStateGroup(groupName, searchAncestors: true);
			}
		}
		return null;
	}

	[Conditional("DEBUG")]
	public static void GetVisualChildTreeDebugText(this FrameworkElement root, StringBuilder result)
	{
		List<string> list = new List<string>();
		root.GetChildTree("", "  ", list);
		foreach (string item in list)
		{
			result.AppendLine(item);
		}
	}

	private static void GetChildTree(this FrameworkElement root, string prefix, string addPrefix, List<string> results)
	{
		//IL_0063: Unknown result type (might be due to invalid IL or missing references)
		//IL_0076: Expected O, but got Unknown
		string text = ((!string.IsNullOrEmpty(root.Name)) ? ("[" + root.Name + "]") : "[Anonymous]");
		text = text + " : " + ((object)root).GetType().Name;
		results.Add(prefix + text);
		foreach (FrameworkElement visualChild in ((DependencyObject)(object)root).GetVisualChildren())
		{
			GetChildTree(visualChild, prefix + addPrefix, addPrefix, results);
		}
	}

	[Conditional("DEBUG")]
	public static void GetAncestorVisualTreeDebugText(this FrameworkElement node, StringBuilder result)
	{
		List<string> list = new List<string>();
		node.GetAncestorVisualTree(list);
		string text = "";
		foreach (string item in list)
		{
			result.AppendLine(text + item);
			text += "  ";
		}
	}

	private static void GetAncestorVisualTree(this FrameworkElement node, List<string> children)
	{
		string item = (string.IsNullOrEmpty(node.Name) ? "[Anon]" : node.Name) + ": " + ((object)node).GetType().Name;
		children.Insert(0, item);
		FrameworkElement visualParent = node.GetVisualParent();
		if (visualParent != null)
		{
			visualParent.GetAncestorVisualTree(children);
		}
	}

	public static string GetTransformPropertyPath<RequestedType>(this FrameworkElement element, string subProperty) where RequestedType : Transform
	{
		Transform renderTransform = ((UIElement)element).RenderTransform;
		if (renderTransform is RequestedType)
		{
			return $"(RenderTransform).({typeof(RequestedType).Name}.{subProperty})";
		}
		if (renderTransform is TransformGroup)
		{
			TransformGroup val = (TransformGroup)(object)((renderTransform is TransformGroup) ? renderTransform : null);
			for (int i = 0; i < ((PresentationFrameworkCollection<Transform>)(object)val.Children).Count; i++)
			{
				if (((PresentationFrameworkCollection<Transform>)(object)val.Children)[i] is RequestedType)
				{
					return string.Format("(RenderTransform).(TransformGroup.Children)[" + i + "].({0}.{1})", typeof(RequestedType).Name, subProperty);
				}
			}
		}
		return "";
	}

	public static PlaneProjection GetPlaneProjection(this UIElement element, bool create)
	{
		//IL_0020: Unknown result type (might be due to invalid IL or missing references)
		//IL_0026: Expected O, but got Unknown
		Projection projection = element.Projection;
		PlaneProjection result = null;
		if (projection is PlaneProjection)
		{
			return (PlaneProjection)(object)((projection is PlaneProjection) ? projection : null);
		}
		if (projection == null && create)
		{
			result = (PlaneProjection)(object)(element.Projection = (Projection)new PlaneProjection());
		}
		return result;
	}

	public static void InvokeOnLayoutUpdated(this FrameworkElement element, Action action)
	{
		if (element == null)
		{
			throw new ArgumentNullException("element");
		}
		if (action == null)
		{
			throw new ArgumentNullException("action");
		}
		EventHandler handler = null;
		handler = delegate
		{
			element.LayoutUpdated -= handler;
			action();
		};
		element.LayoutUpdated += handler;
	}

	internal static IEnumerable<FrameworkElement> GetLogicalChildren(this FrameworkElement parent)
	{
		Popup val = (Popup)(object)((parent is Popup) ? parent : null);
		if (val != null)
		{
			UIElement child = val.Child;
			FrameworkElement val2 = (FrameworkElement)(object)((child is FrameworkElement) ? child : null);
			if (val2 != null)
			{
				yield return val2;
			}
		}
		ItemsControl itemsControl = (ItemsControl)(object)((parent is ItemsControl) ? parent : null);
		if (itemsControl != null)
		{
			foreach (FrameworkElement item in (from index in Enumerable.Range(0, ((PresentationFrameworkCollection<object>)(object)itemsControl.Items).Count)
				select itemsControl.ItemContainerGenerator.ContainerFromIndex(index)).OfType<FrameworkElement>())
			{
				yield return item;
			}
		}
		_ = parent.Name;
		Queue<FrameworkElement> queue = new Queue<FrameworkElement>(((DependencyObject)(object)parent).GetVisualChildren().OfType<FrameworkElement>());
		while (queue.Count > 0)
		{
			FrameworkElement frameworkElement2 = queue.Dequeue();
			if ((object)frameworkElement2.Parent == parent || frameworkElement2 is UserControl)
			{
				yield return frameworkElement2;
				continue;
			}
			foreach (FrameworkElement item2 in ((DependencyObject)(object)frameworkElement2).GetVisualChildren().OfType<FrameworkElement>())
			{
				queue.Enqueue(item2);
			}
		}
	}

	internal static IEnumerable<FrameworkElement> GetLogicalDescendents(this FrameworkElement parent)
	{
		return null;
	}
}
