using System;
using System.Runtime.CompilerServices;
using System.Threading;
using System.Windows;
using System.Windows.Controls;

namespace Vine.Controls;

public class AlterTextBox : TextBox
{
	[CompilerGenerated]
	private RoutedEventHandler m_Modified;

	private string _currentVal;

	public event RoutedEventHandler Modified
	{
		[CompilerGenerated]
		add
		{
			//IL_0010: Unknown result type (might be due to invalid IL or missing references)
			//IL_0016: Expected O, but got Unknown
			RoutedEventHandler val = this.m_Modified;
			RoutedEventHandler val2;
			do
			{
				val2 = val;
				RoutedEventHandler value2 = (RoutedEventHandler)Delegate.Combine((Delegate)(object)val2, (Delegate)(object)value);
				val = Interlocked.CompareExchange(ref this.m_Modified, value2, val2);
			}
			while (val != val2);
		}
		[CompilerGenerated]
		remove
		{
			//IL_0010: Unknown result type (might be due to invalid IL or missing references)
			//IL_0016: Expected O, but got Unknown
			RoutedEventHandler val = this.m_Modified;
			RoutedEventHandler val2;
			do
			{
				val2 = val;
				RoutedEventHandler value2 = (RoutedEventHandler)Delegate.Remove((Delegate)(object)val2, (Delegate)(object)value);
				val = Interlocked.CompareExchange(ref this.m_Modified, value2, val2);
			}
			while (val != val2);
		}
	}

	public AlterTextBox()
	{
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0018: Expected O, but got Unknown
		//IL_0020: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Expected O, but got Unknown
		((UIElement)this).GotFocus += new RoutedEventHandler(AlterTextBox_GotFocus);
		((UIElement)this).LostFocus += new RoutedEventHandler(AlterTextBox_LostFocus);
	}

	private void AlterTextBox_LostFocus(object sender, RoutedEventArgs e)
	{
		if (!(_currentVal == ((TextBox)this).Text))
		{
			this.Modified.Invoke((object)this, e);
		}
	}

	private void AlterTextBox_GotFocus(object sender, RoutedEventArgs e)
	{
		_currentVal = ((TextBox)this).Text;
	}
}
