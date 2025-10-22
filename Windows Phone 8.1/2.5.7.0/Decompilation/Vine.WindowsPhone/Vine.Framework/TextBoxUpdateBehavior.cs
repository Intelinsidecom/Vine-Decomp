using System;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Data;

namespace Vine.Framework;

public class TextBoxUpdateBehavior : Behavior<TextBox>
{
	protected override void Subscribe(object sender, RoutedEventArgs routedEventArgs)
	{
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_0032: Expected O, but got Unknown
		TextBox val = base.Object;
		WindowsRuntimeMarshal.AddEventHandler((Func<TextChangedEventHandler, EventRegistrationToken>)val.add_TextChanged, (Action<EventRegistrationToken>)val.remove_TextChanged, new TextChangedEventHandler(ObjectOnTextChanged));
	}

	private void ObjectOnTextChanged(object sender, TextChangedEventArgs textChangedEventArgs)
	{
		BindingExpression bindingExpression = ((FrameworkElement)base.Object).GetBindingExpression(TextBox.TextProperty);
		if (bindingExpression != null)
		{
			bindingExpression.UpdateSource();
		}
	}

	protected override void UnSubscribe(object sender, RoutedEventArgs routedEventArgs)
	{
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_0023: Expected O, but got Unknown
		WindowsRuntimeMarshal.RemoveEventHandler<TextChangedEventHandler>((Action<EventRegistrationToken>)base.Object.remove_TextChanged, new TextChangedEventHandler(ObjectOnTextChanged));
	}
}
