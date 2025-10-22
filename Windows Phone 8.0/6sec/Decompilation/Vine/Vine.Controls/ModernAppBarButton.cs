using System.Windows;
using System.Windows.Controls;

namespace Vine.Controls;

public class ModernAppBarButton : Button
{
	public static readonly DependencyProperty TextProperty = DependencyProperty.Register("Text", typeof(string), typeof(ModernAppBarButton), new PropertyMetadata((object)null));

	public string Text
	{
		get
		{
			return (string)((DependencyObject)this).GetValue(TextProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(TextProperty, (object)value.ToLower());
		}
	}

	public ModernAppBarButton()
	{
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Expected O, but got Unknown
		((FrameworkElement)this).Style = (Style)Application.Current.Resources[(object)"ModernAppBarButtonStyle"];
	}
}
