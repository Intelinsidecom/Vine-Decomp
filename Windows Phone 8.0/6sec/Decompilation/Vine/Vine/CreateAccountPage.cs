using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microsoft.Phone.Tasks;
using Newtonsoft.Json;
using RestSharp.Authenticators;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;
using Vine.Services.Response.Auth;
using Vine.Services.Utils;
using Vine.TwitterAuth;
using Vine.Utils;

namespace Vine;

public class CreateAccountPage : PhoneApplicationPage
{
	private TwitterProfileInfo _twitterinfo;

	private string _profile_image_url;

	private TwitterAccess _twitterAccess;

	internal Storyboard StoryboardLaunch;

	internal Storyboard StoryboardShowLogin;

	internal Storyboard StoryboardHideLogin;

	internal Storyboard StoryboardShowCreate;

	internal Storyboard StoryboardHideCreate;

	internal Storyboard StoryboardHideTwitter;

	internal Storyboard StoryboardShowTwitter;

	internal Grid LayoutRoot;

	internal Grid ContentPanel;

	internal MediaElement Media;

	internal Button button1_Copy;

	internal Button button1;

	internal Button button;

	internal Grid LoginPanel;

	internal TextBox Login;

	internal PasswordBox LoginPassword;

	internal CheckBox LoginAcceptPolicy;

	internal Grid CreatePanel;

	internal TextBox FullnameTxt;

	internal StackPanel EmailZone;

	internal TextBox EmailTxt;

	internal StackPanel PasswordZone;

	internal TextBox PasswordTxt;

	internal TextBox PhoneTxt;

	internal CheckBox CreateAcceptPolicy;

	internal Button CreateButton;

	internal Grid TwitterPanel;

	internal TextBox EmailTwitter;

	internal PasswordBox PasswordTwitter;

	internal CheckBox AcceptPolicyTwitter;

	internal Button CreateButton1;

	internal Grid LostPasswordPanel;

	internal TextBox EmailLostPasswordTxt;

	internal Grid LoadingPanel;

	internal ProgressBar ProgressLoading;

	private bool _contentLoaded;

	public CreateAccountPage()
	{
		//IL_006a: Unknown result type (might be due to invalid IL or missing references)
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0076: Unknown result type (might be due to invalid IL or missing references)
		//IL_0082: Unknown result type (might be due to invalid IL or missing references)
		//IL_008c: Expected O, but got Unknown
		//IL_0091: Expected O, but got Unknown
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_001e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		//IL_0031: Unknown result type (might be due to invalid IL or missing references)
		//IL_003b: Expected O, but got Unknown
		//IL_0040: Expected O, but got Unknown
		//IL_00e5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ef: Expected O, but got Unknown
		//IL_00c3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c8: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ca: Expected O, but got Unknown
		//IL_00cf: Expected O, but got Unknown
		InitializeComponent();
		if (DeviceHelper.CanDisplayVideoOnCreateAccount())
		{
			((Panel)LayoutRoot).Background = (Brush)new ImageBrush
			{
				Stretch = (Stretch)1,
				ImageSource = (ImageSource)new BitmapImage(new Uri("/Assets/videofirstimage.png", UriKind.Relative))
			};
			if (DatasProvider.Instance.AutoPlayComputed)
			{
				Media.Source = new Uri("/Assets/background.wmv", UriKind.Relative);
			}
		}
		else
		{
			((Panel)LayoutRoot).Background = (Brush)new ImageBrush
			{
				Stretch = (Stretch)1,
				ImageSource = (ImageSource)new BitmapImage(new Uri("/Assets/lowfirstimage.png", UriKind.Relative))
			};
		}
		if (DatasProvider.Instance.AddStripComputed)
		{
			Grid twitterPanel = TwitterPanel;
			Grid createPanel = CreatePanel;
			Grid loginPanel = LoginPanel;
			Brush val = (Brush)Application.Current.Resources[(object)"StripeBrush"];
			Brush val2 = val;
			((Panel)loginPanel).Background = val;
			Brush background = (((Panel)createPanel).Background = val2);
			((Panel)twitterPanel).Background = background;
		}
		((FrameworkElement)this).Loaded += new RoutedEventHandler(CreateAccountPage_Loaded);
		((Timeline)StoryboardShowCreate).Completed += delegate
		{
			//IL_0014: Unknown result type (might be due to invalid IL or missing references)
			SystemTray.ForegroundColor = (Color)Application.Current.Resources[(object)"DarkGreyColor"];
			((Control)FullnameTxt).Focus();
		};
		((Timeline)StoryboardShowLogin).Completed += delegate
		{
			//IL_0014: Unknown result type (might be due to invalid IL or missing references)
			SystemTray.ForegroundColor = (Color)Application.Current.Resources[(object)"DarkGreyColor"];
			((Control)Login).Focus();
		};
		((Timeline)StoryboardShowTwitter).Completed += delegate
		{
			//IL_0014: Unknown result type (might be due to invalid IL or missing references)
			SystemTray.ForegroundColor = (Color)Application.Current.Resources[(object)"DarkGreyColor"];
			((Control)EmailTwitter).Focus();
		};
	}

	private void CreateAccountPage_Loaded(object sender, RoutedEventArgs e)
	{
		StoryboardLaunch.Begin();
	}

	protected override void OnBackKeyPress(CancelEventArgs e)
	{
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Unknown result type (might be due to invalid IL or missing references)
		//IL_0070: Unknown result type (might be due to invalid IL or missing references)
		//IL_0053: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ad: Unknown result type (might be due to invalid IL or missing references)
		//IL_0090: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cd: Unknown result type (might be due to invalid IL or missing references)
		((PhoneApplicationPage)this).OnBackKeyPress(e);
		if ((int)((UIElement)LostPasswordPanel).Visibility == 0)
		{
			e.Cancel = true;
			((UIElement)LostPasswordPanel).Visibility = (Visibility)1;
		}
		else if (((CompositeTransform)((UIElement)LoginPanel).RenderTransform).TranslateY == 0.0)
		{
			StoryboardHideLogin.Begin();
			SystemTray.ForegroundColor = Colors.White;
			e.Cancel = true;
		}
		else if (((CompositeTransform)((UIElement)CreatePanel).RenderTransform).TranslateY == 0.0)
		{
			StoryboardHideCreate.Begin();
			SystemTray.ForegroundColor = Colors.White;
			e.Cancel = true;
		}
		else if (((CompositeTransform)((UIElement)TwitterPanel).RenderTransform).TranslateY == 0.0)
		{
			StoryboardHideTwitter.Begin();
			SystemTray.ForegroundColor = Colors.White;
			e.Cancel = true;
		}
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		((Page)this).OnNavigatingFrom(e);
		Media.Stop();
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		((Page)this).OnNavigatedTo(e);
		if ((int)e.NavigationMode == 0)
		{
			if (((Page)this).NavigationContext.QueryString.ContainsKey("removebackentry"))
			{
				while (((Page)this).NavigationService.RemoveBackEntry() != null)
				{
				}
			}
			if (((Page)this).NavigationContext.QueryString.ContainsKey("forcedemail"))
			{
				_twitterAccess = null;
				Login.Text = ((Page)this).NavigationContext.QueryString["forcedemail"];
				StoryboardShowLogin.Begin();
			}
			else if (((Page)this).NavigationContext.QueryString.ContainsKey("forcedtwitter"))
			{
				_twitterAccess = null;
				EmailTwitter.Text = ((Page)this).NavigationContext.QueryString["forcedtwitter"];
				StoryboardShowTwitter.Begin();
			}
			Vine.Datas.Datas instance = DatasProvider.Instance;
			if (instance.CurrentUserInValidation != null)
			{
				instance.CurrentUserInValidation = null;
				instance.Save();
			}
		}
		else
		{
			Media.Position = TimeSpan.FromMilliseconds(1.0);
			Media.Play();
		}
	}

	private void CallbackAccount(AuthData res, bool isCreation)
	{
		if (res == null)
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				//IL_0024: Unknown result type (might be due to invalid IL or missing references)
				//IL_0018: Unknown result type (might be due to invalid IL or missing references)
				HideLoading();
				if (isCreation)
				{
					MessageBox.Show(AppResources.ToastCantCreateAccount);
				}
				else
				{
					MessageBox.Show(AppResources.ToastCantConnect);
				}
			});
			return;
		}
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			try
			{
				ToastHelper.Show(string.Format(AppResources.ToastWelcome, res.Name), afternav: true, (Orientation)1);
			}
			catch
			{
			}
			if (((Page)this).NavigationContext.QueryString.ContainsKey("nexturi"))
			{
				NavigationServiceExt.Navigate(new Uri(((Page)this).NavigationContext.QueryString["nexturi"], UriKind.Relative), removebackentry: true);
			}
			else
			{
				NavigationServiceExt.ToTimeline(null, removebackentry: true);
			}
		});
	}

	private void FailCallback(ServiceServerErrorException ex)
	{
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			ToastHelper.Show(ex.HttpErrorMessage ?? AppResources.ToastFailToConnect, afternav: false, (Orientation)0);
			HideLoading();
		});
	}

	private async void Create_Click(object sender, RoutedEventArgs e)
	{
		if ((int)((UIElement)EmailZone).Visibility == 0 && EmailTxt.Text.Count((char c) => c == '@') != 1)
		{
			MessageBox.Show(AppResources.ToastEmailNotValid);
			return;
		}
		if (string.IsNullOrEmpty(FullnameTxt.Text))
		{
			MessageBox.Show(AppResources.ToastFullnameMandatory);
			return;
		}
		if ((int)((UIElement)EmailZone).Visibility == 0 && string.IsNullOrEmpty(EmailTxt.Text))
		{
			MessageBox.Show(AppResources.ToastEmailMandatory);
			return;
		}
		if ((int)((UIElement)PasswordZone).Visibility == 0 && string.IsNullOrEmpty(PasswordTxt.Text))
		{
			MessageBox.Show(AppResources.ToastPasswordMissing);
			return;
		}
		ShowLoading();
		Vine.Datas.Datas data = DatasProvider.Instance;
		if (_twitterAccess != null)
		{
			try
			{
				AuthData authData = await VineService.Instance.CreateAccountWithTwitterAsync(_twitterAccess, FullnameTxt.Text, PhoneTxt.Text, (_twitterinfo != null) ? _twitterinfo.location : null, (_twitterinfo != null) ? _twitterinfo.description : null);
				data.CreateNewUser(EmailTxt.Text, PasswordTxt.Text, _twitterAccess, authData);
				data.Save();
				if (!string.IsNullOrEmpty(_profile_image_url))
				{
					UploadImageProfile(_profile_image_url.Replace("_normal.jpg", "_bigger.jpg"));
				}
				CallbackAccount(authData, isCreation: true);
				return;
			}
			catch (ServiceServerErrorException ex)
			{
				FailCallback(ex);
				return;
			}
		}
		string password = PasswordTxt.Text;
		string email = EmailTxt.Text;
		try
		{
			AuthData authData2 = await VineService.Instance.CreateAccountAsync(FullnameTxt.Text, email, password, PhoneTxt.Text);
			data.CreateNewUser(email, password, null, authData2);
			data.Save();
			CallbackAccount(authData2, isCreation: true);
		}
		catch (ServiceServerErrorException ex2)
		{
			FailCallback(ex2);
		}
	}

	private void ShowLogin_Click(object sender, RoutedEventArgs e)
	{
		_twitterAccess = null;
		StoryboardShowLogin.Begin();
	}

	private void ShowCreate_Click(object sender, RoutedEventArgs e)
	{
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		StackPanel passwordZone = PasswordZone;
		StackPanel emailZone = EmailZone;
		Visibility visibility = (Visibility)0;
		((UIElement)emailZone).Visibility = (Visibility)0;
		((UIElement)passwordZone).Visibility = visibility;
		_twitterAccess = null;
		StoryboardShowCreate.Begin();
	}

	private void FullName_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 3)
		{
			((Control)EmailTxt).Focus();
		}
	}

	private void EmailTxt_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 3)
		{
			((Control)PasswordTxt).Focus();
		}
	}

	private void Phone_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 3)
		{
			((Control)CreateButton).Focus();
		}
	}

	private void Login_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 3)
		{
			((Control)LoginPassword).Focus();
		}
	}

	private void LoginPassword_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 3)
		{
			LoginMe(Login.Text, LoginPassword.Password);
		}
	}

	private async Task LoginMe(string login, string password)
	{
		ShowLoading();
		try
		{
			AuthData authData = await VineService.Instance.LoginAsync(login, password);
			Vine.Datas.Datas instance = DatasProvider.Instance;
			instance.CreateNewUser(login, password, null, authData);
			instance.Save();
			CallbackAccount(authData, isCreation: false);
		}
		catch (ServiceServerErrorException ex)
		{
			FailCallback(ex);
		}
	}

	private void Login_Click(object sender, RoutedEventArgs e)
	{
		_twitterAccess = null;
		LoginMe(Login.Text, LoginPassword.Password);
	}

	private void Password_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 3)
		{
			((Control)PhoneTxt).Focus();
		}
	}

	private void TwitterAuth_Click(object sender, RoutedEventArgs e)
	{
		_twitterAccess = null;
		StoryboardShowTwitter.Begin();
	}

	private async void TwitterSignIn_Click(object sender, RoutedEventArgs e)
	{
		if (string.IsNullOrEmpty(EmailTwitter.Text) || string.IsNullOrEmpty(PasswordTwitter.Password))
		{
			return;
		}
		ShowLoading();
		HttpClient httpClient = new HttpClient
		{
			BaseAddress = new Uri("https://api.twitter.com")
		};
		httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
		httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("text/json"));
		httpClient.DefaultRequestHeaders.UserAgent.TryParseAdd(ServiceInfo.UserAgent);
		OAuth1Authenticator oAuth1Authenticator = OAuth1Authenticator.ForXAuth(TwitterSettings.ConsumerKey, TwitterSettings.ConsumerKeySecret, EmailTwitter.Text, PasswordTwitter.Password);
		Uri requestUri = new Uri("/oauth/access_token", UriKind.Relative);
		List<KeyValuePair<string, string>> requestParameters = new List<KeyValuePair<string, string>>
		{
			new KeyValuePair<string, string>("send_error_codes", "true")
		};
		HttpRequestMessage httpRequestMessage = new HttpRequestMessage(HttpMethod.Post, requestUri);
		oAuth1Authenticator.Authenticate(httpClient, httpRequestMessage, requestParameters);
		HttpResponseMessage response = await httpClient.SendAsync(httpRequestMessage);
		string text = await response.Content.ReadAsStringAsync();
		if (!response.IsSuccessStatusCode)
		{
			try
			{
				TwitterErrorRootObject twitterErrorRootObject = null;
				if (!string.IsNullOrEmpty(text))
				{
					twitterErrorRootObject = new JsonSerializer().Deserialize<TwitterErrorRootObject>(new JsonTextReader(new StringReader(text)));
				}
				if (twitterErrorRootObject != null && twitterErrorRootObject.errors != null && twitterErrorRootObject.errors.Count > 0 && twitterErrorRootObject.errors[0].code == 231)
				{
					if ((int)MessageBox.Show("You use a 2-step authentification, you need to generate a temporary password to sign in", "Twitter", (MessageBoxButton)1) == 1)
					{
						new WebBrowserTask
						{
							Uri = new Uri("https://twitter.com/settings/applications")
						}.Show();
					}
				}
				else
				{
					ToastHelper.Show((twitterErrorRootObject != null && twitterErrorRootObject.errors != null && twitterErrorRootObject.errors.Count > 0 && !string.IsNullOrEmpty(twitterErrorRootObject.errors[0].message)) ? AppResources.ToastBadUserPassword : twitterErrorRootObject.errors[0].message, afternav: false, (Orientation)0);
				}
				return;
			}
			catch
			{
				ToastHelper.Show(AppResources.ToastBadUserPassword, afternav: false, (Orientation)0);
				return;
			}
			finally
			{
				HideLoading();
			}
		}
		IDictionary<string, string> dictionary = HttpParseQuery.ParseQueryString(text);
		dictionary.TryGetValue("oauth_token", out var value);
		dictionary.TryGetValue("oauth_token_secret", out var value2);
		dictionary.TryGetValue("user_id", out var user_id);
		dictionary.TryGetValue("screen_name", out var value3);
		_twitterAccess = new TwitterAccess
		{
			AccessToken = value,
			AccessTokenSecret = value2,
			UserId = user_id,
			ScreenName = value3
		};
		Vine.Datas.Datas data = DatasProvider.Instance;
		try
		{
			AuthData authData = await VineService.Instance.LoginTwitterAsync(_twitterAccess);
			data.CreateNewUser(null, null, _twitterAccess, authData);
			CallbackAccount(authData, isCreation: true);
		}
		catch (ServiceServerErrorException ex)
		{
			if (ex.ErrorType == "101")
			{
				if ((int)MessageBox.Show(AppResources.ToastSyncTwitterAccountMessage, AppResources.ToastSyncTwitterAccountTitle, (MessageBoxButton)1) != 1)
				{
					return;
				}
				StackPanel passwordZone = PasswordZone;
				StackPanel emailZone = EmailZone;
				Visibility visibility = (Visibility)1;
				((UIElement)emailZone).Visibility = (Visibility)1;
				((UIElement)passwordZone).Visibility = visibility;
				((UIElement)CreateAcceptPolicy).Visibility = (Visibility)1;
				((ToggleButton)CreateAcceptPolicy).IsChecked = true;
				TwitterProfileInfo twitterProfileInfo = await new TwitterHelper(_twitterAccess).GetInfoUserAsync(user_id);
				_profile_image_url = null;
				if (twitterProfileInfo != null)
				{
					FullnameTxt.Text = twitterProfileInfo.name;
					_twitterinfo = twitterProfileInfo;
					if (!twitterProfileInfo.default_profile_image && !string.IsNullOrEmpty(twitterProfileInfo.profile_image_url))
					{
						_profile_image_url = twitterProfileInfo.profile_image_url.Replace("_normal.jpg", "_bigger.jpg");
					}
				}
				StoryboardHideTwitter.Begin();
				StoryboardShowCreate.Begin();
				return;
			}
			FailCallback(ex);
		}
		finally
		{
			HideLoading();
		}
	}

	private async Task UploadImageProfile(string uri)
	{
		HttpWebRequest request = WebRequest.CreateHttp(uri);
		request.BeginGetResponse(async delegate(IAsyncResult iar)
		{
			_ = 1;
			try
			{
				DataUser user = DatasProvider.Instance.CurrentUser;
				Stream responseStream = request.EndGetResponse(iar).GetResponseStream();
				MemoryStream memoryStream = new MemoryStream();
				responseStream.CopyTo(memoryStream);
				try
				{
					string text = await user.Service.UploadAvatarAsync(memoryStream);
					if (text != null)
					{
						user.Service.ModifyProfileAsync("avatarUrl", text, null);
					}
				}
				catch
				{
				}
			}
			catch
			{
			}
		}, null);
	}

	private void EmailTwitter_KeyUp(object sender, KeyEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.Key == 3)
		{
			((Control)PasswordTwitter).Focus();
		}
	}

	private void Media_MediaOpened_1(object sender, RoutedEventArgs e)
	{
		TimerHelper.ToTime(TimeSpan.FromMilliseconds(200.0), delegate
		{
			((UIElement)Media).Opacity = 0.99;
		});
	}

	private void PasswordLost_Click(object sender, RoutedEventArgs e)
	{
		((UIElement)LostPasswordPanel).Visibility = (Visibility)0;
		((Control)EmailLostPasswordTxt).Focus();
	}

	private void ResetPassword_Click(object sender, RoutedEventArgs e)
	{
		string text = EmailLostPasswordTxt.Text;
		if (string.IsNullOrWhiteSpace(text))
		{
			return;
		}
		ShowLoading();
		ServiceUtils.ResetPassword(text, delegate
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				HideLoading();
			});
		});
	}

	private void OpenPrivacyPolicy_Tap(object sender, GestureEventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		new WebBrowserTask
		{
			Uri = new Uri("http://www2.huynapps.com/wp/6sec/privacy.php")
		}.Show();
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
		//IL_01a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ad: Expected O, but got Unknown
		//IL_01b9: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c3: Expected O, but got Unknown
		//IL_01cf: Unknown result type (might be due to invalid IL or missing references)
		//IL_01d9: Expected O, but got Unknown
		//IL_01e5: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ef: Expected O, but got Unknown
		//IL_01fb: Unknown result type (might be due to invalid IL or missing references)
		//IL_0205: Expected O, but got Unknown
		//IL_0211: Unknown result type (might be due to invalid IL or missing references)
		//IL_021b: Expected O, but got Unknown
		//IL_0227: Unknown result type (might be due to invalid IL or missing references)
		//IL_0231: Expected O, but got Unknown
		//IL_023d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0247: Expected O, but got Unknown
		//IL_0253: Unknown result type (might be due to invalid IL or missing references)
		//IL_025d: Expected O, but got Unknown
		//IL_0269: Unknown result type (might be due to invalid IL or missing references)
		//IL_0273: Expected O, but got Unknown
		//IL_027f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0289: Expected O, but got Unknown
		//IL_0295: Unknown result type (might be due to invalid IL or missing references)
		//IL_029f: Expected O, but got Unknown
		//IL_02ab: Unknown result type (might be due to invalid IL or missing references)
		//IL_02b5: Expected O, but got Unknown
		//IL_02c1: Unknown result type (might be due to invalid IL or missing references)
		//IL_02cb: Expected O, but got Unknown
		//IL_02d7: Unknown result type (might be due to invalid IL or missing references)
		//IL_02e1: Expected O, but got Unknown
		//IL_02ed: Unknown result type (might be due to invalid IL or missing references)
		//IL_02f7: Expected O, but got Unknown
		//IL_0303: Unknown result type (might be due to invalid IL or missing references)
		//IL_030d: Expected O, but got Unknown
		//IL_0319: Unknown result type (might be due to invalid IL or missing references)
		//IL_0323: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/CreateAccount/CreateAccountPage.xaml", UriKind.Relative));
			StoryboardLaunch = (Storyboard)((FrameworkElement)this).FindName("StoryboardLaunch");
			StoryboardShowLogin = (Storyboard)((FrameworkElement)this).FindName("StoryboardShowLogin");
			StoryboardHideLogin = (Storyboard)((FrameworkElement)this).FindName("StoryboardHideLogin");
			StoryboardShowCreate = (Storyboard)((FrameworkElement)this).FindName("StoryboardShowCreate");
			StoryboardHideCreate = (Storyboard)((FrameworkElement)this).FindName("StoryboardHideCreate");
			StoryboardHideTwitter = (Storyboard)((FrameworkElement)this).FindName("StoryboardHideTwitter");
			StoryboardShowTwitter = (Storyboard)((FrameworkElement)this).FindName("StoryboardShowTwitter");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			Media = (MediaElement)((FrameworkElement)this).FindName("Media");
			button1_Copy = (Button)((FrameworkElement)this).FindName("button1_Copy");
			button1 = (Button)((FrameworkElement)this).FindName("button1");
			button = (Button)((FrameworkElement)this).FindName("button");
			LoginPanel = (Grid)((FrameworkElement)this).FindName("LoginPanel");
			Login = (TextBox)((FrameworkElement)this).FindName("Login");
			LoginPassword = (PasswordBox)((FrameworkElement)this).FindName("LoginPassword");
			LoginAcceptPolicy = (CheckBox)((FrameworkElement)this).FindName("LoginAcceptPolicy");
			CreatePanel = (Grid)((FrameworkElement)this).FindName("CreatePanel");
			FullnameTxt = (TextBox)((FrameworkElement)this).FindName("FullnameTxt");
			EmailZone = (StackPanel)((FrameworkElement)this).FindName("EmailZone");
			EmailTxt = (TextBox)((FrameworkElement)this).FindName("EmailTxt");
			PasswordZone = (StackPanel)((FrameworkElement)this).FindName("PasswordZone");
			PasswordTxt = (TextBox)((FrameworkElement)this).FindName("PasswordTxt");
			PhoneTxt = (TextBox)((FrameworkElement)this).FindName("PhoneTxt");
			CreateAcceptPolicy = (CheckBox)((FrameworkElement)this).FindName("CreateAcceptPolicy");
			CreateButton = (Button)((FrameworkElement)this).FindName("CreateButton");
			TwitterPanel = (Grid)((FrameworkElement)this).FindName("TwitterPanel");
			EmailTwitter = (TextBox)((FrameworkElement)this).FindName("EmailTwitter");
			PasswordTwitter = (PasswordBox)((FrameworkElement)this).FindName("PasswordTwitter");
			AcceptPolicyTwitter = (CheckBox)((FrameworkElement)this).FindName("AcceptPolicyTwitter");
			CreateButton1 = (Button)((FrameworkElement)this).FindName("CreateButton1");
			LostPasswordPanel = (Grid)((FrameworkElement)this).FindName("LostPasswordPanel");
			EmailLostPasswordTxt = (TextBox)((FrameworkElement)this).FindName("EmailLostPasswordTxt");
			LoadingPanel = (Grid)((FrameworkElement)this).FindName("LoadingPanel");
			ProgressLoading = (ProgressBar)((FrameworkElement)this).FindName("ProgressLoading");
		}
	}
}
