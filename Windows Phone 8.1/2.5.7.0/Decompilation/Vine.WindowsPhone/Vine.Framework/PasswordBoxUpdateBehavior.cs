using System;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Data;

namespace Vine.Framework;

public class PasswordBoxUpdateBehavior : Behavior<PasswordBox>
{
	protected override void Subscribe(object sender, RoutedEventArgs routedEventArgs)
	{
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_0032: Expected O, but got Unknown
		PasswordBox val = base.Object;
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_PasswordChanged, (Action<EventRegistrationToken>)val.remove_PasswordChanged, new RoutedEventHandler(AssociatedObjectOnTextChanged));
	}

	protected override void UnSubscribe(object sender, RoutedEventArgs routedEventArgs)
	{
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_0023: Expected O, but got Unknown
		WindowsRuntimeMarshal.RemoveEventHandler<RoutedEventHandler>((Action<EventRegistrationToken>)base.Object.remove_PasswordChanged, new RoutedEventHandler(AssociatedObjectOnTextChanged));
	}

	private void AssociatedObjectOnTextChanged(object sender, RoutedEventArgs args)
	{
		BindingExpression bindingExpression = ((FrameworkElement)base.Object).GetBindingExpression(PasswordBox.PasswordProperty);
		if (bindingExpression != null)
		{
			bindingExpression.UpdateSource();
		}
	}
}
