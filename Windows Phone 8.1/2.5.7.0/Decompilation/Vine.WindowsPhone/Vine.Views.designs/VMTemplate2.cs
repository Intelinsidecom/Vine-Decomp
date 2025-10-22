using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Navigation;
using Windows.UI.Xaml.Shapes;

namespace Vine.Views.designs;

public sealed class VMTemplate2 : Page, IComponentConnector
{
	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Ellipse AvatarTo;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Border VideoToFrom;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public VMTemplate2()
	{
		InitializeComponent();
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
	}

	private void MediaElement_OnCurrentStateChanged(object sender, RoutedEventArgs e)
	{
		throw new NotImplementedException();
	}

	private void MediaElement_OnMediaFailed(object sender, ExceptionRoutedEventArgs e)
	{
		throw new NotImplementedException();
	}

	private void MediaElement_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		throw new NotImplementedException();
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/designs/VMTemplate2.xaml"), (ComponentResourceLocation)0);
			AvatarTo = (Ellipse)((FrameworkElement)this).FindName("AvatarTo");
			VideoToFrom = (Border)((FrameworkElement)this).FindName("VideoToFrom");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_000e: Expected O, but got Unknown
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0039: Expected O, but got Unknown
		//IL_003a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0040: Expected O, but got Unknown
		//IL_0061: Unknown result type (might be due to invalid IL or missing references)
		//IL_006b: Expected O, but got Unknown
		//IL_006c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0072: Expected O, but got Unknown
		//IL_0093: Unknown result type (might be due to invalid IL or missing references)
		//IL_009d: Expected O, but got Unknown
		if (connectionId == 1)
		{
			MediaElement val = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_CurrentStateChanged, (Action<EventRegistrationToken>)val.remove_CurrentStateChanged, new RoutedEventHandler(MediaElement_OnCurrentStateChanged));
			val = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<ExceptionRoutedEventHandler, EventRegistrationToken>)val.add_MediaFailed, (Action<EventRegistrationToken>)val.remove_MediaFailed, new ExceptionRoutedEventHandler(MediaElement_OnMediaFailed));
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(MediaElement_OnTapped));
		}
		_contentLoaded = true;
	}
}
