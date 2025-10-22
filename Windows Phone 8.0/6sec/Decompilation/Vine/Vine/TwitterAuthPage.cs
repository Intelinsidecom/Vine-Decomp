using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Tasks;
using Newtonsoft.Json;
using RestSharp.Authenticators;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;

namespace Vine;

public class TwitterAuthPage : PhoneApplicationPage
{
	private string _type;

	private bool _isInit;

	internal Storyboard StoryboardLaunch;

	internal Storyboard StoryboardShowLogin;

	internal Storyboard StoryboardHideLogin;

	internal Storyboard StoryboardShowCreate;

	internal Storyboard StoryboardHideCreate;

	internal Storyboard StoryboardHideTwitter;

	internal Storyboard StoryboardShowTwitter;

	internal Grid LayoutRoot;

	internal Grid TwitterPanel;

	internal TextBox EmailTwitter;

	internal PasswordBox PasswordTwitter;

	internal Button CreateButton1;

	internal Grid TweetPanel;

	internal TextBox TweetTxt;

	internal Image TweetImg;

	internal Grid LoadingPanel;

	internal ProgressBar ProgressLoading;

	private bool _contentLoaded;

	public TwitterAuthPage()
	{
		//IL_0044: Unknown result type (might be due to invalid IL or missing references)
		//IL_004e: Expected O, but got Unknown
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)TwitterPanel).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
		((FrameworkElement)this).Loaded += new RoutedEventHandler(TwitterAuthPage_Loaded);
	}

	private void HideLoading()
	{
		((UIElement)LoadingPanel).Visibility = (Visibility)1;
		ProgressLoading.IsIndeterminate = false;
	}

	private void ShowLoading()
	{
		((UIElement)LoadingPanel).Visibility = (Visibility)0;
		ProgressLoading.IsIndeterminate = true;
	}

	private void TwitterAuthPage_Loaded(object sender, RoutedEventArgs e)
	{
		if (DatasProvider.Instance.CurrentUser.TwitterAccess == null)
		{
			((Control)EmailTwitter).Focus();
		}
		else
		{
			((Control)TweetTxt).Focus();
		}
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0099: Expected O, but got Unknown
		((Page)this).OnNavigatedTo(e);
		if (_isInit && (int)e.NavigationMode != 0)
		{
			return;
		}
		_isInit = true;
		_type = ((Page)this).NavigationContext.QueryString["type"];
		if (_type == "post")
		{
			TweetTxt.Text = ((Page)this).NavigationContext.QueryString["tweet"];
			TweetImg.Source = (ImageSource)new BitmapImage(new Uri(((Page)this).NavigationContext.QueryString["img"], UriKind.Absolute));
			if (DatasProvider.Instance.CurrentUser.TwitterAccess == null)
			{
				((UIElement)TwitterPanel).Visibility = (Visibility)0;
				((UIElement)TweetPanel).Visibility = (Visibility)1;
			}
			else
			{
				ShowTweetPanel();
			}
		}
		else if (_type == "syncaccount")
		{
			((UIElement)TwitterPanel).Visibility = (Visibility)0;
			((UIElement)TweetPanel).Visibility = (Visibility)1;
		}
	}

	private void ShowTweetPanel()
	{
		((UIElement)TwitterPanel).Visibility = (Visibility)1;
		((UIElement)TweetPanel).Visibility = (Visibility)0;
		((Control)TweetTxt).Focus();
	}

	private void CallbackAccount(bool res, bool isCreation)
	{
		if (!res)
		{
			ToastHelper.Show(AppResources.ToastBadUserPassword, afternav: false, (Orientation)0);
		}
		else if (_type == "post")
		{
			ShowTweetPanel();
		}
		else if (_type == "syncaccount")
		{
			((Page)this).NavigationService.GoBack();
		}
		HideLoading();
	}

	private void FailCallback(ServiceServerErrorException ex)
	{
		ToastHelper.Show(ex.HttpErrorMessage ?? AppResources.ToastBadUserPassword, afternav: false, (Orientation)0);
		HideLoading();
	}

	private void EmailTxt_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 3)
		{
			((Control)PasswordTwitter).Focus();
		}
	}

	private void Password_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 3)
		{
			((Control)this).Focus();
		}
	}

	private async void TwitterSignIn_Click(object sender, RoutedEventArgs e)
	{
		if (string.IsNullOrEmpty(EmailTwitter.Text) || string.IsNullOrEmpty(PasswordTwitter.Password))
		{
			return;
		}
		OAuth1Authenticator oAuth1Authenticator = OAuth1Authenticator.ForXAuth(TwitterSettings.ConsumerKey, TwitterSettings.ConsumerKeySecret, EmailTwitter.Text, PasswordTwitter.Password);
		HttpClient httpClient = new HttpClient
		{
			BaseAddress = new Uri("https://api.twitter.com")
		};
		httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
		httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("text/json"));
		httpClient.DefaultRequestHeaders.UserAgent.TryParseAdd(ServiceInfo.UserAgent);
		ShowLoading();
		Uri requestUri = new Uri("/oauth/access_token", UriKind.Relative);
		List<KeyValuePair<string, string>> requestParameters = new List<KeyValuePair<string, string>>
		{
			new KeyValuePair<string, string>("send_error_codes", "true")
		};
		HttpRequestMessage httpRequestMessage = new HttpRequestMessage(HttpMethod.Post, requestUri);
		oAuth1Authenticator.Authenticate(httpClient, httpRequestMessage, requestParameters);
		HttpResponseMessage response = await httpClient.SendAsync(httpRequestMessage);
		if (!response.IsSuccessStatusCode)
		{
			try
			{
				TwitterErrorRootObject result = null;
				Stream stream = await response.Content.ReadAsStreamAsync();
				if (stream != null && stream.Length > 0)
				{
					result = new JsonSerializer().Deserialize<TwitterErrorRootObject>(new JsonTextReader(new StreamReader(stream)));
				}
				if (result != null && result.errors != null && result.errors.Count > 0 && result.errors[0].code == 231)
				{
					if ((int)MessageBox.Show("You use a 2-step authentification, you need to generate a temporary password to sign in", "Twitter", (MessageBoxButton)1) == 1)
					{
						new WebBrowserTask
						{
							Uri = new Uri("https://twitter.com/settings/applications")
						}.Show();
					}
					return;
				}
				try
				{
					ToastHelper.Show((result != null || (result.errors != null && result.errors.Count > 0 && !string.IsNullOrEmpty(result.errors[0].message))) ? AppResources.ToastBadUserPassword : result.errors[0].message, afternav: false, (Orientation)0);
					return;
				}
				catch
				{
					return;
				}
			}
			catch
			{
				return;
			}
			finally
			{
				HideLoading();
			}
		}
		IDictionary<string, string> dictionary = HttpParseQuery.ParseQueryString(await response.Content.ReadAsStringAsync());
		dictionary.TryGetValue("oauth_token", out var value);
		dictionary.TryGetValue("oauth_token_secret", out var value2);
		dictionary.TryGetValue("user_id", out var value3);
		dictionary.TryGetValue("screen_name", out var value4);
		TwitterAccess twitteruser = new TwitterAccess
		{
			AccessToken = value,
			AccessTokenSecret = value2,
			UserId = value3,
			ScreenName = value4
		};
		Vine.Datas.Datas data = DatasProvider.Instance;
		try
		{
			CallbackAccount(await data.CurrentUser.Service.AddTwitterToProfileAsync(twitteruser), isCreation: true);
			data.CurrentUser.TwitterAccess = twitteruser;
			data.Save();
		}
		catch (ServiceServerErrorException ex)
		{
			FailCallback(ex);
		}
		finally
		{
			HideLoading();
		}
	}

	private async void Tweet_Click(object sender, RoutedEventArgs e)
	{
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		ShowLoading();
		try
		{
			string postId = ((Page)this).NavigationContext.QueryString["postid"];
			if (!(await currentUser.Service.SharePostTwitterAsync(postId, TweetTxt.Text)))
			{
				ToastHelper.Show(AppResources.ToastCantPostMessage, afternav: false, (Orientation)0);
				return;
			}
			ToastHelper.Show(AppResources.ToastTweetPosted, afternav: true, (Orientation)0);
			((Page)this).NavigationService.GoBack();
		}
		catch (ServiceServerErrorException)
		{
			ToastHelper.Show(AppResources.ToastCantPostMessage, afternav: false, (Orientation)0);
		}
		finally
		{
			HideLoading();
		}
	}

	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Expected O, but got Unknown
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Expected O, but got Unknown
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		//IL_00dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e7: Expected O, but got Unknown
		//IL_00f3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fd: Expected O, but got Unknown
		//IL_0109: Unknown result type (might be due to invalid IL or missing references)
		//IL_0113: Expected O, but got Unknown
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0129: Expected O, but got Unknown
		//IL_0135: Unknown result type (might be due to invalid IL or missing references)
		//IL_013f: Expected O, but got Unknown
		//IL_014b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0155: Expected O, but got Unknown
		//IL_0161: Unknown result type (might be due to invalid IL or missing references)
		//IL_016b: Expected O, but got Unknown
		//IL_0177: Unknown result type (might be due to invalid IL or missing references)
		//IL_0181: Expected O, but got Unknown
		//IL_018d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0197: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/TwitterAuth/TwitterAuthPage.xaml", UriKind.Relative));
			StoryboardLaunch = (Storyboard)((FrameworkElement)this).FindName("StoryboardLaunch");
			StoryboardShowLogin = (Storyboard)((FrameworkElement)this).FindName("StoryboardShowLogin");
			StoryboardHideLogin = (Storyboard)((FrameworkElement)this).FindName("StoryboardHideLogin");
			StoryboardShowCreate = (Storyboard)((FrameworkElement)this).FindName("StoryboardShowCreate");
			StoryboardHideCreate = (Storyboard)((FrameworkElement)this).FindName("StoryboardHideCreate");
			StoryboardHideTwitter = (Storyboard)((FrameworkElement)this).FindName("StoryboardHideTwitter");
			StoryboardShowTwitter = (Storyboard)((FrameworkElement)this).FindName("StoryboardShowTwitter");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			TwitterPanel = (Grid)((FrameworkElement)this).FindName("TwitterPanel");
			EmailTwitter = (TextBox)((FrameworkElement)this).FindName("EmailTwitter");
			PasswordTwitter = (PasswordBox)((FrameworkElement)this).FindName("PasswordTwitter");
			CreateButton1 = (Button)((FrameworkElement)this).FindName("CreateButton1");
			TweetPanel = (Grid)((FrameworkElement)this).FindName("TweetPanel");
			TweetTxt = (TextBox)((FrameworkElement)this).FindName("TweetTxt");
			TweetImg = (Image)((FrameworkElement)this).FindName("TweetImg");
			LoadingPanel = (Grid)((FrameworkElement)this).FindName("LoadingPanel");
			ProgressLoading = (ProgressBar)((FrameworkElement)this).FindName("ProgressLoading");
		}
	}
}
