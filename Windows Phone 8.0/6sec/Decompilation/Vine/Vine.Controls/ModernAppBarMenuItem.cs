using System.Windows;
using System.Windows.Controls;

namespace Vine.Controls;

public class ModernAppBarMenuItem : Button
{
	public ModernAppBarMenuItem()
	{
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Expected O, but got Unknown
		((FrameworkElement)this).Style = (Style)Application.Current.Resources[(object)"ModernAppBarMenuItemStyle"];
	}
}
