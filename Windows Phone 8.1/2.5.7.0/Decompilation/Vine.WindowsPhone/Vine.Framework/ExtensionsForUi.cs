using System;
using System.Collections.Generic;
using System.IO;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Documents;

namespace Vine.Framework;

public static class ExtensionsForUi
{
	public static readonly DependencyProperty RunListProperty = DependencyProperty.RegisterAttached("RunList", typeof(List<Run>), typeof(ExtensionsForUi), new PropertyMetadata((object)null, new PropertyChangedCallback(RunListPropertyChangedCallback)));

	public static readonly DependencyProperty TagProperty = DependencyProperty.RegisterAttached("ExtraTag", typeof(DependencyObject), typeof(ExtensionsForUi), new PropertyMetadata((object)null));

	public static readonly DependencyProperty MediaStreamProperty = DependencyProperty.RegisterAttached("MediaStream", typeof(Stream), typeof(ExtensionsForUi), new PropertyMetadata((object)null, new PropertyChangedCallback(PropertyChangedCallback)));

	private static void RunListPropertyChangedCallback(DependencyObject dependencyObject, DependencyPropertyChangedEventArgs e)
	{
		TextBlock val = (TextBlock)(object)((dependencyObject is TextBlock) ? dependencyObject : null);
		List<Run> list = e.NewValue as List<Run>;
		if (val == null || list == null)
		{
			return;
		}
		((ICollection<Inline>)val.Inlines).Clear();
		foreach (Run item in list)
		{
			((ICollection<Inline>)val.Inlines).Add((Inline)(object)item);
		}
	}

	public static void SetRunList(DependencyObject element, List<Run> value)
	{
		element.SetValue(RunListProperty, (object)value);
	}

	public static List<Run> GetRunList(DependencyObject element)
	{
		return (List<Run>)element.GetValue(RunListProperty);
	}

	public static void SetExtraTag(DependencyObject element, DependencyObject value)
	{
		element.SetValue(TagProperty, (object)value);
	}

	public static DependencyObject GetExtraTag(DependencyObject element)
	{
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		return (DependencyObject)element.GetValue(TagProperty);
	}

	private static void PropertyChangedCallback(DependencyObject dependencyObject, DependencyPropertyChangedEventArgs e)
	{
		MediaElement val = (MediaElement)(object)((dependencyObject is MediaElement) ? dependencyObject : null);
		if (val != null)
		{
			if (!(e.NewValue is Stream stream))
			{
				val.put_Source((Uri)null);
			}
			else
			{
				val.SetSource(stream.AsRandomAccessStream(), "video/mp4");
			}
		}
	}

	public static void SetMediaStream(DependencyObject element, Stream value)
	{
		element.SetValue(MediaStreamProperty, (object)value);
	}

	public static Stream GetMediaStream(DependencyObject element)
	{
		return (Stream)element.GetValue(MediaStreamProperty);
	}

	public static Task LayoutUpdatedAsync(this FrameworkElement element)
	{
		TaskCompletionSource<object> tcs = new TaskCompletionSource<object>();
		EventHandler<object> handler = null;
		handler = delegate
		{
			WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)element.remove_LayoutUpdated, handler);
			tcs.SetResult(null);
		};
		FrameworkElement val = element;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)val.add_LayoutUpdated, (Action<EventRegistrationToken>)val.remove_LayoutUpdated, handler);
		return tcs.Task;
	}

	public static Task WhenPlayingAsync(this MediaElement element)
	{
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_0031: Expected O, but got Unknown
		TaskCompletionSource<object> tcs = new TaskCompletionSource<object>();
		RoutedEventHandler handler = null;
		handler = (RoutedEventHandler)delegate
		{
			//IL_0006: Unknown result type (might be due to invalid IL or missing references)
			//IL_000c: Invalid comparison between Unknown and I4
			if ((int)element.CurrentState == 3)
			{
				WindowsRuntimeMarshal.RemoveEventHandler<RoutedEventHandler>((Action<EventRegistrationToken>)element.remove_CurrentStateChanged, handler);
				tcs.SetResult(null);
			}
		};
		MediaElement val = element;
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_CurrentStateChanged, (Action<EventRegistrationToken>)val.remove_CurrentStateChanged, handler);
		return tcs.Task;
	}
}
