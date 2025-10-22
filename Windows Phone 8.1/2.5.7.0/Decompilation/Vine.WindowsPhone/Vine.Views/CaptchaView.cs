using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Vine.Special;
using Vine.Web;
using Windows.Foundation;
using Windows.System;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;
using Windows.Web.Http;
using Windows.Web.Http.Headers;

namespace Vine.Views;

public sealed class CaptchaView : BasePage, IComponentConnector
{
	private bool _isLoading;

	private bool _hasError;

	private CaptchaParameter captchaParam;

	private bool _redirectFailedRetryOnce;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private WebView Browser;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	private CaptchaParameter Parameter => (CaptchaParameter)base.NavigationObject;

	public bool IsFinishedLoading { get; set; }

	public bool IsLoading
	{
		get
		{
			return _isLoading;
		}
		set
		{
			if (value)
			{
				HasError = false;
			}
			_isLoading = value;
			((UIElement)Browser).put_Opacity(_isLoading ? 0.5 : 1.0);
			((UIElement)Browser).put_IsHitTestVisible(!_isLoading);
			NotifyOfPropertyChange(() => IsLoading);
		}
	}

	public bool HasError
	{
		get
		{
			return _hasError;
		}
		set
		{
			SetProperty(ref _hasError, value, "HasError");
			OnPropertyChanged("HasError");
		}
	}

	private string Url { get; set; }

	public CaptchaView()
	{
		InitializeComponent();
	}

	protected override void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		Url = Parameter.Url;
		OnActivate();
	}

	public void OnActivate()
	{
		//IL_003c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0042: Expected O, but got Unknown
		//IL_0052: Unknown result type (might be due to invalid IL or missing references)
		//IL_005c: Expected O, but got Unknown
		//IL_0076: Unknown result type (might be due to invalid IL or missing references)
		//IL_0080: Expected O, but got Unknown
		if (!IsFinishedLoading)
		{
			IsLoading = true;
			HasError = false;
			string text = "winphone/" + ApplicationSettings.Current.ClientVersion;
			HttpRequestMessage val = new HttpRequestMessage(HttpMethod.Get, new Uri(Url));
			((ICollection<HttpLanguageRangeWithQualityHeaderValue>)val.Headers.AcceptLanguage).Add(new HttpLanguageRangeWithQualityHeaderValue(WebDataProvider.AcceptLangValue()));
			((ICollection<HttpProductInfoHeaderValue>)val.Headers.UserAgent).Add(new HttpProductInfoHeaderValue("winphone", ApplicationSettings.Current.ClientVersion));
			((IDictionary<string, string>)val.Headers).Add("X-Vine-Client", text);
			ReservedEx.UserAgent(text);
			Browser.NavigateWithHttpRequestMessage(val);
		}
	}

	private void Refresh_Click(object sender, RoutedEventArgs e)
	{
		IsFinishedLoading = false;
		OnActivate();
	}

	private void Browser_OnDOMContentLoaded(WebView sender, WebViewDOMContentLoadedEventArgs args)
	{
		IsLoading = false;
		IsFinishedLoading = true;
	}

	private void Browser_OnNavigationFailed(object sender, WebViewNavigationFailedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0008: Invalid comparison between Unknown and I4
		if ((int)e.WebErrorStatus == 17 && !_redirectFailedRetryOnce)
		{
			_redirectFailedRetryOnce = true;
			OnActivate();
		}
		else
		{
			IsLoading = false;
			HasError = true;
		}
	}

	private void Browser_OnNavigationCompleted(WebView sender, WebViewNavigationCompletedEventArgs args)
	{
		args.Uri.ToString();
	}

	private async void ButtonBase_OnClick(object sender, RoutedEventArgs e)
	{
		await Launcher.LaunchUriAsync(new Uri("vine://_captcha_complete"));
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/CaptchaView.xaml"), (ComponentResourceLocation)0);
			Browser = (WebView)((FrameworkElement)this).FindName("Browser");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_001a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Expected O, but got Unknown
		//IL_004c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0052: Expected O, but got Unknown
		//IL_0073: Unknown result type (might be due to invalid IL or missing references)
		//IL_007d: Expected O, but got Unknown
		//IL_007e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0084: Expected O, but got Unknown
		//IL_00b2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b8: Expected O, but got Unknown
		//IL_00d9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e3: Expected O, but got Unknown
		//IL_00e6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ec: Expected O, but got Unknown
		//IL_010d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0117: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			WebView val2 = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<WebView, WebViewNavigationCompletedEventArgs>, EventRegistrationToken>)val2.add_NavigationCompleted, (Action<EventRegistrationToken>)val2.remove_NavigationCompleted, (TypedEventHandler<WebView, WebViewNavigationCompletedEventArgs>)Browser_OnNavigationCompleted);
			val2 = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<WebViewNavigationFailedEventHandler, EventRegistrationToken>)val2.add_NavigationFailed, (Action<EventRegistrationToken>)val2.remove_NavigationFailed, new WebViewNavigationFailedEventHandler(Browser_OnNavigationFailed));
			val2 = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<WebView, WebViewDOMContentLoadedEventArgs>, EventRegistrationToken>)val2.add_DOMContentLoaded, (Action<EventRegistrationToken>)val2.remove_DOMContentLoaded, (TypedEventHandler<WebView, WebViewDOMContentLoadedEventArgs>)Browser_OnDOMContentLoaded);
			break;
		}
		case 2:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ButtonBase_OnClick));
			break;
		}
		case 3:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Refresh_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
