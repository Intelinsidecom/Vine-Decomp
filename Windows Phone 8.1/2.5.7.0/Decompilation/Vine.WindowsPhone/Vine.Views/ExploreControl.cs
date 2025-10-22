using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Events;
using Vine.Framework;
using Vine.Models;
using Vine.Special;
using Vine.Web;
using Windows.Foundation;
using Windows.Phone.UI.Input;
using Windows.Storage;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;
using Windows.Web.Http;
using Windows.Web.Http.Headers;

namespace Vine.Views;

public sealed class ExploreControl : BaseUserControl, IComponentConnector
{
	public class ExploreViewParams
	{
	}

	private bool _isLoading;

	private bool _hasError;

	private bool _searchActive;

	private string _errorText = ResourceHelper.GetString("ExploreErrorLoading");

	private bool _showRetry = true;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	public SearchControl Search;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private WebView Browser;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

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
			NotifyOfPropertyChange(() => BrowserVisible);
		}
	}

	public bool BrowserVisible
	{
		get
		{
			if (!HasError)
			{
				return !SearchActive;
			}
			return false;
		}
	}

	public bool SearchActive
	{
		get
		{
			return _searchActive;
		}
		set
		{
			SetProperty(ref _searchActive, value, "SearchActive");
			NotifyOfPropertyChange(() => BrowserVisible);
		}
	}

	public string ErrorText
	{
		get
		{
			return _errorText;
		}
		set
		{
			SetProperty(ref _errorText, value, "ErrorText");
		}
	}

	public bool ShowRetry
	{
		get
		{
			return _showRetry;
		}
		set
		{
			SetProperty(ref _showRetry, value, "ShowRetry");
		}
	}

	public event EventHandler<SwipeEvent> SwipEvent;

	public ExploreControl()
	{
		InitializeComponent();
	}

	public unsafe async Task OnActivate()
	{
		WindowsRuntimeMarshal.RemoveEventHandler<EventHandler<BackPressedEventArgs>>(new Action<EventRegistrationToken>(null, (nint)(delegate*<EventRegistrationToken, void>)(&HardwareButtons.remove_BackPressed)), HardwareButtons_BackPressed);
		WindowsRuntimeMarshal.AddEventHandler(new Func<EventHandler<BackPressedEventArgs>, EventRegistrationToken>(null, (nint)(delegate*<EventHandler<BackPressedEventArgs>, EventRegistrationToken>)(&HardwareButtons.add_BackPressed)), new Action<EventRegistrationToken>(null, (nint)(delegate*<EventRegistrationToken, void>)(&HardwareButtons.remove_BackPressed)), HardwareButtons_BackPressed);
		if (IsFinishedLoading)
		{
			if (SearchActive)
			{
				await ((FrameworkElement)(object)Search).LayoutUpdatedAsync();
				await Search.OnActivate(null);
				EventAggregator.Current.Publish(new SearchPinChanged());
			}
		}
		else
		{
			IsLoading = true;
			string text = "winphone/" + ApplicationSettings.Current.ClientVersion;
			HttpRequestMessage val = new HttpRequestMessage(HttpMethod.Get, new Uri("https://uvr.a1429.lol/expl"));
			((ICollection<HttpLanguageRangeWithQualityHeaderValue>)val.Headers.AcceptLanguage).Add(new HttpLanguageRangeWithQualityHeaderValue(WebDataProvider.AcceptLangValue()));
			((ICollection<HttpProductInfoHeaderValue>)val.Headers.UserAgent).Add(new HttpProductInfoHeaderValue("winphone", ApplicationSettings.Current.ClientVersion));
			((IDictionary<string, string>)val.Headers).Add("X-Vine-Client", text);
			ReservedEx.UserAgent(text);
			Browser.NavigateWithHttpRequestMessage(val);
			await Search.OnActivate(null);
		}
	}

	public unsafe void OnDeactivate()
	{
		Search.OnDeactivate();
		WindowsRuntimeMarshal.RemoveEventHandler<EventHandler<BackPressedEventArgs>>(new Action<EventRegistrationToken>(null, (nint)(delegate*<EventRegistrationToken, void>)(&HardwareButtons.remove_BackPressed)), HardwareButtons_BackPressed);
		EventAggregator.Current.Publish(new SearchPinChanged());
	}

	private async void Browser_OnDOMContentLoaded(WebView sender, WebViewDOMContentLoadedEventArgs args)
	{
		string text = await FileIO.ReadTextAsync((IStorageFile)(object)(await StorageFile.GetFileFromApplicationUriAsync(new Uri("ms-appx:///js/ExploreInjection.js"))));
		await Browser.InvokeScriptAsync("eval", (IEnumerable<string>)new string[1] { text });
		IsLoading = false;
		IsFinishedLoading = true;
	}

	private void Browser_OnScriptNotify(object sender, NotifyEventArgs e)
	{
		if (this.SwipEvent != null)
		{
			this.SwipEvent(this, (!(e.Value == "swipeleft")) ? SwipeEvent.SwipeRight : SwipeEvent.SwipeLeft);
		}
	}

	private void Browser_OnNavigationStarting(WebView sender, WebViewNavigationStartingEventArgs args)
	{
		if (!(args.Uri != null) || !args.Uri.OriginalString.StartsWith("https://windowsphone.vine.co/"))
		{
			return;
		}
		args.put_Cancel(true);
		string text = args.Uri.OriginalString.Replace("https://windowsphone.vine.co", "");
		if (text.StartsWith("/popular-now"))
		{
			App.RootFrame.NavigateWithObject(typeof(TagVineListView), new VineListViewParams
			{
				Title = ResourceHelper.GetString("PopularNow"),
				Type = ListType.PopularNow
			});
		}
		else if (text.StartsWith("/trending-people"))
		{
			App.RootFrame.NavigateWithObject(typeof(TagVineListView), new VineListViewParams
			{
				Title = ResourceHelper.GetString("OnTheRise"),
				Type = ListType.TrendingPeople
			});
		}
		else if (text.StartsWith("/channels/"))
		{
			string text2 = text.Substring(text.IndexOf("/channels/") + 10);
			string channelId = text2.Substring(0, text2.IndexOf("?"));
			text2 = text2.Substring(text2.IndexOf("?") + 1);
			Dictionary<string, string> dictionary = (from p in text2.Split('&')
				select p.Split('=')).ToDictionary((string[] nvp) => nvp[0].ToLower(), (string[] nvp) => nvp[1]);
			string title = WebUtility.UrlDecode(dictionary["name"]);
			string color = dictionary["navrgb"];
			string icon = null;
			bool channelShowRecent = true;
			if (dictionary.ContainsKey("showrecent") && !string.IsNullOrWhiteSpace(dictionary["showrecent"]))
			{
				channelShowRecent = Convert.ToBoolean(Convert.ToInt16(dictionary["showrecent"]));
			}
			if (dictionary.ContainsKey("icon") && !string.IsNullOrEmpty(dictionary["icon"]))
			{
				icon = "https://vine.co" + WebUtility.UrlDecode(dictionary["icon"]);
			}
			App.RootFrame.NavigateWithObject(typeof(ChannelVineListView), new VineListViewParams
			{
				Title = title,
				Type = ListType.ChannelFeatured,
				Color = color,
				ChannelId = channelId,
				Icon = icon,
				ChannelShowRecent = channelShowRecent
			});
		}
		else if (text.StartsWith("/tag/"))
		{
			string text3 = text.Replace("/tag/", "");
			if (text3.Contains("?"))
			{
				text3 = text3.Substring(0, text.IndexOf('?'));
			}
			App.RootFrame.Navigate(typeof(TagVineListView), (object)text3);
		}
	}

	private void Browser_OnNavigationFailed(object sender, WebViewNavigationFailedEventArgs e)
	{
		IsLoading = false;
		HasError = true;
		ErrorText = ResourceHelper.GetString("ExploreErrorLoading");
		ShowRetry = true;
	}

	private void Refresh_Click(object sender, RoutedEventArgs e)
	{
		IsFinishedLoading = false;
		OnActivate();
	}

	private async void Search_GotFocus(object sender, FocusState e)
	{
		SearchActive = true;
		await Search.OnActivate(null);
		EventAggregator.Current.Publish(new SearchPinChanged());
	}

	private void HardwareButtons_BackPressed(object sender, BackPressedEventArgs e)
	{
		if (SearchActive && IsActivePage())
		{
			e.put_Handled(true);
			DeactivateSearch();
		}
	}

	public void DeactivateSearch()
	{
		SearchActive = false;
		Search.Reset();
		EventAggregator.Current.Publish(new SearchPinChanged());
	}

	private bool IsActivePage()
	{
		if (((ContentControl)App.RootFrame).Content != null && (object)((object)(BasePage)((ContentControl)App.RootFrame).Content).GetType() == typeof(HomeView))
		{
			return true;
		}
		return false;
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/ExploreControl.xaml"), (ComponentResourceLocation)0);
			Search = (SearchControl)((FrameworkElement)this).FindName("Search");
			Browser = (WebView)((FrameworkElement)this).FindName("Browser");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0036: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		//IL_0068: Unknown result type (might be due to invalid IL or missing references)
		//IL_006e: Expected O, but got Unknown
		//IL_008f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0099: Expected O, but got Unknown
		//IL_009a: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a0: Expected O, but got Unknown
		//IL_00c1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cb: Expected O, but got Unknown
		//IL_00cc: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d2: Expected O, but got Unknown
		//IL_0100: Unknown result type (might be due to invalid IL or missing references)
		//IL_0106: Expected O, but got Unknown
		//IL_0127: Unknown result type (might be due to invalid IL or missing references)
		//IL_0131: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
			((SearchControl)target).SearchFocus += Search_GotFocus;
			break;
		case 2:
		{
			WebView val2 = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<WebView, WebViewNavigationStartingEventArgs>, EventRegistrationToken>)val2.add_NavigationStarting, (Action<EventRegistrationToken>)val2.remove_NavigationStarting, (TypedEventHandler<WebView, WebViewNavigationStartingEventArgs>)Browser_OnNavigationStarting);
			val2 = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<WebViewNavigationFailedEventHandler, EventRegistrationToken>)val2.add_NavigationFailed, (Action<EventRegistrationToken>)val2.remove_NavigationFailed, new WebViewNavigationFailedEventHandler(Browser_OnNavigationFailed));
			val2 = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<NotifyEventHandler, EventRegistrationToken>)val2.add_ScriptNotify, (Action<EventRegistrationToken>)val2.remove_ScriptNotify, new NotifyEventHandler(Browser_OnScriptNotify));
			val2 = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<WebView, WebViewDOMContentLoadedEventArgs>, EventRegistrationToken>)val2.add_DOMContentLoaded, (Action<EventRegistrationToken>)val2.remove_DOMContentLoaded, (TypedEventHandler<WebView, WebViewDOMContentLoadedEventArgs>)Browser_OnDOMContentLoaded);
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
