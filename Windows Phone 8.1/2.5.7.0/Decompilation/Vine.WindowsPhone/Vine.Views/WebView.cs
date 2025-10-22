using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Windows.Foundation;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class WebView : BasePage, IComponentConnector
{
	private string _webUrl;

	private bool _isBusy;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private WebView Browser;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public string WebUrl
	{
		get
		{
			return _webUrl;
		}
		set
		{
			_webUrl = value;
			OnPropertyChanged("WebUrl");
		}
	}

	public bool IsBusy
	{
		get
		{
			return _isBusy;
		}
		set
		{
			_isBusy = value;
			OnPropertyChanged("IsBusy");
		}
	}

	public WebView()
	{
		InitializeComponent();
	}

	protected override void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		WebUrl = (string)e.NavigationParameter;
	}

	private void WebView_OnNavigationStarting(WebView sender, WebViewNavigationStartingEventArgs args)
	{
		IsBusy = true;
	}

	private void WebView_OnNavigationCompleted(WebView sender, WebViewNavigationCompletedEventArgs args)
	{
		IsBusy = false;
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/WebView.xaml"), (ComponentResourceLocation)0);
			Browser = (WebView)((FrameworkElement)this).FindName("Browser");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		//IL_003d: Expected O, but got Unknown
		if (connectionId == 1)
		{
			WebView val = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<WebView, WebViewNavigationStartingEventArgs>, EventRegistrationToken>)val.add_NavigationStarting, (Action<EventRegistrationToken>)val.remove_NavigationStarting, (TypedEventHandler<WebView, WebViewNavigationStartingEventArgs>)WebView_OnNavigationStarting);
			val = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<WebView, WebViewNavigationCompletedEventArgs>, EventRegistrationToken>)val.add_NavigationCompleted, (Action<EventRegistrationToken>)val.remove_NavigationCompleted, (TypedEventHandler<WebView, WebViewNavigationCompletedEventArgs>)WebView_OnNavigationCompleted);
		}
		_contentLoaded = true;
	}
}
