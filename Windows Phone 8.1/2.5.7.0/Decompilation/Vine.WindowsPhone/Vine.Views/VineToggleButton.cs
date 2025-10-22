using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class VineToggleButton : Button, IComponentConnector
{
	public static readonly DependencyProperty StateProperty = DependencyProperty.Register("State", typeof(VineToggleButtonState), typeof(VineToggleButton), new PropertyMetadata((object)VineToggleButtonState.Off, new PropertyChangedCallback(StateChanged)));

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ContentControl ContentControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public VineToggleButtonState State
	{
		get
		{
			return (VineToggleButtonState)((DependencyObject)this).GetValue(StateProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(StateProperty, (object)value);
		}
	}

	public FrameworkElement ActiveVisual
	{
		set
		{
			ContentControl.put_Content((object)value);
		}
	}

	public FrameworkElement OnVisual { get; set; }

	public FrameworkElement OffVisual { get; set; }

	public FrameworkElement DisabledVisual { get; set; }

	public FrameworkElement NotFollowingVisual { get; set; }

	public FrameworkElement FollowingVisual { get; set; }

	public FrameworkElement FollowRequestedVisual { get; set; }

	public VineToggleButton()
	{
		InitializeComponent();
	}

	private static void StateChanged(DependencyObject dependencyObject, DependencyPropertyChangedEventArgs dependencyPropertyChangedEventArgs)
	{
		((VineToggleButton)(object)dependencyObject).UpdateVisual();
	}

	private void UpdateVisual()
	{
		switch (State)
		{
		case VineToggleButtonState.Disabled:
			ActiveVisual = DisabledVisual;
			break;
		case VineToggleButtonState.Off:
			ActiveVisual = OffVisual;
			break;
		case VineToggleButtonState.On:
			ActiveVisual = OnVisual;
			break;
		case VineToggleButtonState.Following:
			ActiveVisual = FollowingVisual;
			break;
		case VineToggleButtonState.NotFollowing:
			ActiveVisual = NotFollowingVisual;
			break;
		case VineToggleButtonState.FollowRequested:
			ActiveVisual = FollowRequestedVisual;
			break;
		}
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/VineToggleButton.xaml"), (ComponentResourceLocation)0);
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
