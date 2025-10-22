using System;
using System.Runtime.InteropServices.WindowsRuntime;
using Microsoft.Xaml.Interactivity;
using Windows.UI.Xaml;

namespace Vine.Framework;

public abstract class Behavior<T> : DependencyObject, IBehavior where T : FrameworkElement
{
	public DependencyObject AssociatedObject { get; set; }

	public T Object
	{
		get
		{
			DependencyObject associatedObject = AssociatedObject;
			return (T)(object)((associatedObject is T) ? associatedObject : null);
		}
	}

	public void Attach(DependencyObject associatedObject)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		//IL_000f: Expected O, but got Unknown
		//IL_0031: Unknown result type (might be due to invalid IL or missing references)
		//IL_003b: Expected O, but got Unknown
		//IL_003c: Expected O, but got Unknown
		//IL_005e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0068: Expected O, but got Unknown
		AssociatedObject = associatedObject;
		FrameworkElement val = (FrameworkElement)associatedObject;
		FrameworkElement val2 = val;
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Loaded, (Action<EventRegistrationToken>)val2.remove_Loaded, new RoutedEventHandler(Subscribe));
		val2 = val;
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Unloaded, (Action<EventRegistrationToken>)val2.remove_Unloaded, new RoutedEventHandler(UnSubscribe));
	}

	protected abstract void UnSubscribe(object sender, RoutedEventArgs e);

	protected abstract void Subscribe(object sender, RoutedEventArgs e);

	public unsafe void Detach()
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_000c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0018: Expected O, but got Unknown
		//IL_0020: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Expected O, but got Unknown
		//IL_002a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0036: Expected O, but got Unknown
		//IL_003e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0048: Expected O, but got Unknown
		FrameworkElement val = (FrameworkElement)AssociatedObject;
		WindowsRuntimeMarshal.RemoveEventHandler<RoutedEventHandler>(new Action<EventRegistrationToken>((object)val, (nint)__ldvirtftn(FrameworkElement.remove_Loaded)), new RoutedEventHandler(Subscribe));
		WindowsRuntimeMarshal.RemoveEventHandler<RoutedEventHandler>(new Action<EventRegistrationToken>((object)val, (nint)__ldvirtftn(FrameworkElement.remove_Unloaded)), new RoutedEventHandler(UnSubscribe));
	}
}
