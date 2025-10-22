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

public sealed class TOSControl : UserControl, IComponentConnector
{
	public delegate void ErrorStateEventHandler(bool isConnectivityError = false);

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public event ErrorStateEventHandler ErrorOccurredEvent;

	public TOSControl()
	{
		InitializeComponent();
	}

	private void TOS_Click(Hyperlink sender, HyperlinkClickEventArgs args)
	{
		App.ScribeService.Log(new ViewImpressionEvent(Section.None, "terms"));
		if (NetworkHelper.CheckForConnectivity())
		{
			App.RootFrame.Navigate(typeof(WebView), (object)"https://vine.co/terms");
		}
		else if (this.ErrorOccurredEvent != null)
		{
			this.ErrorOccurredEvent();
		}
	}

	private void Privacy_Click(Hyperlink sender, HyperlinkClickEventArgs args)
	{
		App.ScribeService.Log(new ViewImpressionEvent(Section.None, "privacy_policy"));
		if (NetworkHelper.CheckForConnectivity())
		{
			App.RootFrame.Navigate(typeof(WebView), (object)"https://vine.co/privacy");
		}
		else if (this.ErrorOccurredEvent != null)
		{
			this.ErrorOccurredEvent();
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/TOSControl.xaml"), (ComponentResourceLocation)0);
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			Hyperlink val = (Hyperlink)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<Hyperlink, HyperlinkClickEventArgs>, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, (TypedEventHandler<Hyperlink, HyperlinkClickEventArgs>)TOS_Click);
			break;
		}
		case 2:
		{
			Hyperlink val = (Hyperlink)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<Hyperlink, HyperlinkClickEventArgs>, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, (TypedEventHandler<Hyperlink, HyperlinkClickEventArgs>)Privacy_Click);
			break;
		}
		}
		_contentLoaded = true;
	}
}
