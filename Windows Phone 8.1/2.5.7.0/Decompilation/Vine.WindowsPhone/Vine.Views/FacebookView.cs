using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.Foundation;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class FacebookView : BasePage, IComponentConnector
{
	private bool _hasError;

	private bool _isLoading;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private WebView WebView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ProgressBar progressBar;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public bool HasError
	{
		get
		{
			return _hasError;
		}
		set
		{
			SetProperty(ref _hasError, value, "HasError");
		}
	}

	public bool IsLoading
	{
		get
		{
			return _isLoading;
		}
		set
		{
			_isLoading = value;
			((UIElement)WebView).put_Opacity(_isLoading ? 0.5 : 1.0);
			((UIElement)WebView).put_IsHitTestVisible(!_isLoading);
			NotifyOfPropertyChange(() => IsLoading);
		}
	}

	public FacebookView()
	{
		InitializeComponent();
	}

	private void NavigateToSigninPage()
	{
		IsLoading = true;
		HasError = false;
		WebView.Navigate(FacebookConstants.AuthUri);
	}

	protected override void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		NavigateToSigninPage();
	}

	private async void WebView_OnNavigationCompleted(WebView sender, WebViewNavigationCompletedEventArgs args)
	{
		string text = args.Uri.ToString();
		if (text.StartsWith("https://m.facebook.com/dialog/oauth/write"))
		{
			NavigateToSigninPage();
			return;
		}
		if (!text.StartsWith("https://www.facebook.com/connect/login_success.html"))
		{
			IsLoading = false;
			return;
		}
		Dictionary<string, string> queryParameters = GetQueryParameters(args.Uri);
		Dictionary<string, string> fragmentValues = ParseUriFragment(args.Uri.Fragment);
		if (queryParameters != null && queryParameters.ContainsKey("error"))
		{
			string text2 = queryParameters["error"];
			_ = queryParameters["error_reason"];
			if (!(queryParameters["error_description"] == "Permissions+error") && !(text2 == "access_denied"))
			{
				await new MessageDialog("Facebook OAuth Failure: " + text2, ResourceHelper.GetString("vine"))
				{
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok")) }
				}.ShowAsync();
			}
			((Page)this).Frame.GoBack();
		}
		else if (fragmentValues != null && fragmentValues.ContainsKey("access_token"))
		{
			string token = fragmentValues["access_token"];
			ApiResult<FbUserModel> meResult = await App.Api.FbMeGet(token);
			ApiResult apiResult = meResult;
			if (!apiResult.HasError)
			{
				ApiResult<FbPermissions> apiResult2 = await App.Api.FbPermissionsGet(meResult.Model.Id, token);
				apiResult = apiResult2;
				if (!apiResult.HasError && apiResult2.Model.HasPublishPermission)
				{
					apiResult = await App.Api.UpdateFacebookCredentials(meResult.Model.Id, token, ApplicationSettings.Current.VineSession.UserId);
					if (!apiResult.HasError)
					{
						meResult.Model.Token = token;
						ApplicationSettings.Current.FbSession = meResult.Model;
						ApplicationSettings.Current.IsFacebookEnabled = true;
					}
				}
			}
			apiResult.PopUpErrorIfExists();
			((Page)this).Frame.GoBack();
		}
		IsLoading = false;
	}

	public static Dictionary<string, string> GetQueryParameters(Uri uri)
	{
		if (uri.OriginalString.Contains("?"))
		{
			return (from pair in uri.OriginalString.Substring(uri.OriginalString.IndexOf('?') + 1).Split('&')
				select pair.Split('=')).ToDictionary((string[] tokens) => Uri.EscapeDataString(tokens[0]), (string[] tokens) => Uri.EscapeDataString(tokens[1]), StringComparer.OrdinalIgnoreCase);
		}
		return new Dictionary<string, string>();
	}

	public static Dictionary<string, string> ParseUriFragment(string fragment)
	{
		if (fragment.Contains("#"))
		{
			return (from pair in fragment.Substring(fragment.IndexOf('#') + 1).Split(new char[1] { '&' }, StringSplitOptions.RemoveEmptyEntries)
				select pair.Split('=')).ToDictionary((string[] tokens) => Uri.EscapeDataString(tokens[0]), (string[] tokens) => Uri.EscapeDataString(tokens[1]), StringComparer.OrdinalIgnoreCase);
		}
		return new Dictionary<string, string>();
	}

	private void WebView_OnNavigationStarting(WebView sender, WebViewNavigationStartingEventArgs args)
	{
		IsLoading = true;
		if (args.Uri.OriginalString.StartsWith("https://www.facebook.com/connect/login_success.html"))
		{
			((UIElement)WebView).put_Opacity(0.0);
		}
	}

	private void Browser_OnNavigationFailed(object sender, WebViewNavigationFailedEventArgs e)
	{
		IsLoading = false;
		HasError = true;
	}

	private void Refresh_Click(object sender, RoutedEventArgs e)
	{
		NavigateToSigninPage();
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/FacebookView.xaml"), (ComponentResourceLocation)0);
			WebView = (WebView)((FrameworkElement)this).FindName("WebView");
			progressBar = (ProgressBar)((FrameworkElement)this).FindName("progressBar");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_0049: Expected O, but got Unknown
		//IL_006a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0074: Expected O, but got Unknown
		//IL_0075: Unknown result type (might be due to invalid IL or missing references)
		//IL_007b: Expected O, but got Unknown
		//IL_00a9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00af: Expected O, but got Unknown
		//IL_00d0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00da: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			WebView val2 = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<WebView, WebViewNavigationStartingEventArgs>, EventRegistrationToken>)val2.add_NavigationStarting, (Action<EventRegistrationToken>)val2.remove_NavigationStarting, (TypedEventHandler<WebView, WebViewNavigationStartingEventArgs>)WebView_OnNavigationStarting);
			val2 = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<WebViewNavigationFailedEventHandler, EventRegistrationToken>)val2.add_NavigationFailed, (Action<EventRegistrationToken>)val2.remove_NavigationFailed, new WebViewNavigationFailedEventHandler(Browser_OnNavigationFailed));
			val2 = (WebView)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<WebView, WebViewNavigationCompletedEventArgs>, EventRegistrationToken>)val2.add_NavigationCompleted, (Action<EventRegistrationToken>)val2.remove_NavigationCompleted, (TypedEventHandler<WebView, WebViewNavigationCompletedEventArgs>)WebView_OnNavigationCompleted);
			break;
		}
		case 2:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Refresh_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
