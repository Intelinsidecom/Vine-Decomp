using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class VinePressedButton : Button, IComponentConnector
{
	private FrameworkElement _releasedUI;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ContentControl ContentControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public FrameworkElement ReleasedUI
	{
		get
		{
			return _releasedUI;
		}
		set
		{
			_releasedUI = value;
			ContentControl.put_Content((object)value);
		}
	}

	public FrameworkElement PressedUI { get; set; }

	public VinePressedButton()
	{
		InitializeComponent();
	}

	protected override void OnHolding(HoldingRoutedEventArgs e)
	{
		e.put_Handled(true);
		ContentControl.put_Content((object)ReleasedUI);
	}

	protected override void OnPointerExited(PointerRoutedEventArgs e)
	{
		e.put_Handled(true);
		ContentControl.put_Content((object)ReleasedUI);
	}

	protected override void OnPointerCaptureLost(PointerRoutedEventArgs e)
	{
		e.put_Handled(true);
		ContentControl.put_Content((object)ReleasedUI);
	}

	protected override void OnPointerPressed(PointerRoutedEventArgs e)
	{
		e.put_Handled(true);
		ContentControl.put_Content((object)PressedUI);
	}

	protected override void OnPointerReleased(PointerRoutedEventArgs e)
	{
		((Control)this).OnPointerReleased(e);
		ContentControl.put_Content((object)ReleasedUI);
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/VinePressedButton.xaml"), (ComponentResourceLocation)0);
			ContentControl = (ContentControl)((FrameworkElement)this).FindName("ContentControl");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		_contentLoaded = true;
	}
}
