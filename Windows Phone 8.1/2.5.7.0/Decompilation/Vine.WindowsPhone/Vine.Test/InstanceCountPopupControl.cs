using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;

namespace Vine.Test;

public class InstanceCountPopupControl : UserControl, IComponentConnector
{
	private Dictionary<string, MemoryObject> _instanceCounts;

	private Popup _popup;

	private Popup _itemsPopup;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid LayoutRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBlock TextBlock;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public InstanceCountPopupControl()
	{
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0049: Expected O, but got Unknown
		InitializeComponent();
		_instanceCounts = new Dictionary<string, MemoryObject>();
		TextBlock textBlock = TextBlock;
		WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)((UIElement)textBlock).add_Tapped, (Action<EventRegistrationToken>)((UIElement)textBlock).remove_Tapped, (TappedEventHandler)delegate
		{
			CreateDropDown();
		});
	}

	private void CreateDropDown()
	{
		//IL_0009: Unknown result type (might be due to invalid IL or missing references)
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		//IL_001d: Unknown result type (might be due to invalid IL or missing references)
		//IL_002c: Unknown result type (might be due to invalid IL or missing references)
		//IL_005c: Expected O, but got Unknown
		//IL_005c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0062: Expected O, but got Unknown
		//IL_007c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0086: Expected O, but got Unknown
		//IL_00a4: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ab: Expected O, but got Unknown
		//IL_00ab: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bc: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00db: Expected O, but got Unknown
		//IL_00dd: Expected O, but got Unknown
		//IL_0115: Unknown result type (might be due to invalid IL or missing references)
		//IL_011f: Expected O, but got Unknown
		//IL_0155: Unknown result type (might be due to invalid IL or missing references)
		//IL_015f: Expected O, but got Unknown
		if (_itemsPopup != null)
		{
			return;
		}
		Grid val = new Grid();
		((FrameworkElement)val).put_Width(400.0);
		((FrameworkElement)val).put_Height(800.0);
		((FrameworkElement)val).put_Margin(new Thickness(0.0, 50.0, 0.0, 0.0));
		Grid val2 = val;
		ListBox val3 = new ListBox();
		((Control)val3).put_Background((Brush)new SolidColorBrush(Color.FromArgb(200, byte.MaxValue, byte.MaxValue, byte.MaxValue)));
		foreach (MemoryObject value in _instanceCounts.Values)
		{
			ListBoxItem val4 = new ListBoxItem();
			TextBlock val5 = new TextBlock();
			val5.put_Text(value.DisplayText);
			val5.put_FontSize(23.0);
			val5.put_Foreground((Brush)new SolidColorBrush(Colors.Black));
			TextBlock val6 = val5;
			((ContentControl)val4).put_Content((object)val6);
			((FrameworkElement)val4).put_Tag((object)value);
			ListBoxItem val7 = val4;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)((UIElement)val7).add_Tapped, (Action<EventRegistrationToken>)((UIElement)val7).remove_Tapped, new TappedEventHandler(ListItemOnTapped));
			((ICollection<object>)((ItemsControl)val3).Items).Add((object)val4);
		}
		((ICollection<UIElement>)((Panel)val2).Children).Add((UIElement)(object)val3);
		_itemsPopup = new Popup();
		_itemsPopup.put_Child((UIElement)(object)val2);
		_itemsPopup.put_IsOpen(true);
	}

	private void ListItemOnTapped(object sender, TappedRoutedEventArgs tappedRoutedEventArgs)
	{
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		_itemsPopup.put_IsOpen(false);
		MemoryObject memoryObject = (MemoryObject)((FrameworkElement)(ListBoxItem)sender).Tag;
		TextBlock.put_Text(memoryObject.DisplayText);
		_itemsPopup = null;
	}

	public void Open()
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		_popup = new Popup();
		_popup.put_Child((UIElement)(object)this);
		_popup.put_IsOpen(true);
	}

	[Conditional("DEBUG")]
	public void Increment(Type type)
	{
		if (_instanceCounts.ContainsKey(TrimType(type)))
		{
			_instanceCounts[TrimType(type)].InstanceCount++;
		}
		else
		{
			_instanceCounts.Add(TrimType(type), new MemoryObject
			{
				InstanceCount = 1,
				Type = TrimType(type)
			});
		}
		TextBlock.put_Text(_instanceCounts[TrimType(type)].DisplayText);
	}

	[Conditional("DEBUG")]
	public void Decrement(Type type)
	{
		DispatcherEx.InvokeBackground(() => DispatcherEx.BeginInvoke(delegate
		{
			_instanceCounts[TrimType(type)].InstanceCount--;
			TextBlock.put_Text(_instanceCounts[TrimType(type)].DisplayText);
		}));
	}

	private string TrimType(Type type)
	{
		return type.FullName.Replace(type.Namespace, "").Trim('.');
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Test/InstanceCountPopupControl.xaml"), (ComponentResourceLocation)0);
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			TextBlock = (TextBlock)((FrameworkElement)this).FindName("TextBlock");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		_contentLoaded = true;
	}
}
