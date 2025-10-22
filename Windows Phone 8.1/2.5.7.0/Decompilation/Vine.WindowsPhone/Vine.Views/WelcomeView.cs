using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models.Analytics;
using Windows.Foundation;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Documents;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class WelcomeView : BasePage, IComponentConnector
{
	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MediaElement MediaElement;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public WelcomeView()
	{
		//IL_003b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Expected O, but got Unknown
		InitializeComponent();
		base.AlwaysClearBackStack = true;
		MediaElement mediaElement = MediaElement;
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)mediaElement.add_MediaEnded, (Action<EventRegistrationToken>)mediaElement.remove_MediaEnded, new RoutedEventHandler(OnMediaEnded));
	}

	protected override void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.LoggedOut, "front"));
	}

	private void OnMediaEnded(object sender, RoutedEventArgs e)
	{
		MediaElement.Play();
	}

	private void SigninWithTwitter_Click(object sender, RoutedEventArgs e)
	{
		((Page)this).Frame.Navigate(typeof(LoginTwitterView));
	}

	private void SiginWithEmail_Click(Hyperlink sender, HyperlinkClickEventArgs args)
	{
		((Page)this).Frame.Navigate(typeof(LoginEmailView));
	}

	private void SignupWithEmail_Click(object sender, RoutedEventArgs e)
	{
		((Page)this).Frame.Navigate(typeof(SignUpEmailView));
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/WelcomeView.xaml"), (ComponentResourceLocation)0);
			MediaElement = (MediaElement)((FrameworkElement)this).FindName("MediaElement");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_001a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Expected O, but got Unknown
		//IL_0041: Unknown result type (might be due to invalid IL or missing references)
		//IL_004b: Expected O, but got Unknown
		//IL_004e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0054: Expected O, but got Unknown
		//IL_0075: Unknown result type (might be due to invalid IL or missing references)
		//IL_007f: Expected O, but got Unknown
		//IL_0082: Unknown result type (might be due to invalid IL or missing references)
		//IL_0088: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			ButtonBase val2 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(SigninWithTwitter_Click));
			break;
		}
		case 2:
		{
			ButtonBase val2 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(SignupWithEmail_Click));
			break;
		}
		case 3:
		{
			Hyperlink val = (Hyperlink)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<Hyperlink, HyperlinkClickEventArgs>, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, (TypedEventHandler<Hyperlink, HyperlinkClickEventArgs>)SiginWithEmail_Click);
			break;
		}
		}
		_contentLoaded = true;
	}
}
