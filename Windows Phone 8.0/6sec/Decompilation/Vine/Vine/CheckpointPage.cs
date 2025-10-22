using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using Microsoft.Phone.Controls;
using Vine.Datas;
using Vine.Services;

namespace Vine;

public class CheckpointPage : PhoneApplicationPage
{
	private HttpClient _httpClient;

	private List<KeyValuePair<string, string>> _htmlInput;

	private int counter;

	public static Regex regHtml = new Regex("<input[^>]+type\\s*=\\s*\"hidden\"[^>]+name\\s*=\\s*\"(?<name>[^\"]+)\"[^>]+value\\s*=\\s*\"(?<value>[^\"]*)\"", RegexOptions.Compiled | RegexOptions.Singleline | RegexOptions.IgnorePatternWhitespace | RegexOptions.CultureInvariant);

	internal Grid LayoutRoot;

	internal StackPanel TitlePanel;

	internal TextBlock ApplicationTitle;

	internal StackPanel ContentPanel;

	internal Image CaptchaImage;

	internal TextBox CaptchaTextblock;

	internal Grid LoadingPanel;

	private bool _contentLoaded;

	public CheckpointPage()
	{
		//IL_011b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0125: Expected O, but got Unknown
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		InitializeComponent();
		if (!DatasProvider.Instance.AddStripComputed)
		{
			((Panel)LayoutRoot).Background = (Brush)Application.Current.Resources[(object)"NoStripeBrush"];
		}
		_htmlInput = new List<KeyValuePair<string, string>>();
		CookieContainer cookieContainer = new CookieContainer();
		HttpClientHandler handler = new HttpClientHandler
		{
			CookieContainer = cookieContainer,
			AllowAutoRedirect = false
		};
		_httpClient = new HttpClient(handler);
		_httpClient.DefaultRequestHeaders.UserAgent.ParseAdd(ServiceInfo.UserAgent);
		_httpClient.DefaultRequestHeaders.Add("Origin", "https://vine.co");
		_httpClient.DefaultRequestHeaders.Accept.Clear();
		_httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("text/html"));
		_httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xhtml+xml"));
		_httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));
		((FrameworkElement)this).Loaded += new RoutedEventHandler(Page_Loaded);
	}

	private void Page_Loaded(object sender, RoutedEventArgs e)
	{
		LoadCheckpoint();
	}

	private async Task LoadCheckpoint()
	{
		try
		{
			CaptchaTextblock.Text = "";
			((UIElement)LoadingPanel).Visibility = (Visibility)0;
			string requestUri = ((Page)this).NavigationContext.QueryString["uri"];
			string input = await (await _httpClient.GetAsync(requestUri)).Content.ReadAsStringAsync();
			_htmlInput.Clear();
			foreach (Match item in regHtml.Matches(input))
			{
				string value = item.Groups["name"].Value;
				string value2 = item.Groups["value"].Value;
				_htmlInput.Add(new KeyValuePair<string, string>(value, value2));
			}
			HttpResponseMessage obj2 = await _httpClient.GetAsync(new Uri("https://vine.co/captcha/image.jpg?counter=" + ++counter));
			BitmapImage bitmap = new BitmapImage();
			BitmapImage val = bitmap;
			((BitmapSource)val).SetSource(await obj2.Content.ReadAsStreamAsync());
			CaptchaImage.Source = (ImageSource)(object)bitmap;
		}
		catch
		{
		}
		finally
		{
			((UIElement)LoadingPanel).Visibility = (Visibility)1;
		}
	}

	private async Task SubmitCheckpoint(string text)
	{
		_ = 1;
		try
		{
			((UIElement)LoadingPanel).Visibility = (Visibility)0;
			string text2 = ((Page)this).NavigationContext.QueryString["uri"];
			FormUrlEncodedContent content = new FormUrlEncodedContent(new List<KeyValuePair<string, string>>
			{
				new KeyValuePair<string, string>("txt", text)
			}.Concat(_htmlInput));
			HttpRequestMessage httpRequestMessage = new HttpRequestMessage(HttpMethod.Post, text2)
			{
				Content = content
			};
			httpRequestMessage.Headers.Referrer = new Uri(text2);
			if ((await _httpClient.SendAsync(httpRequestMessage)).StatusCode == HttpStatusCode.Found)
			{
				ValidationInner();
				return;
			}
			CaptchaImage.Source = null;
			LoadCheckpoint();
		}
		catch (Exception)
		{
		}
		finally
		{
			((UIElement)LoadingPanel).Visibility = (Visibility)1;
		}
	}

	private void ValidationInner()
	{
		ToastHelper.Show("Account checked!, you need to log you again.", afternav: true, (Orientation)0);
		Vine.Datas.Datas instance = DatasProvider.Instance;
		DataUser currentUser = instance.CurrentUser;
		instance.RemoveUser();
		instance.Save();
		NavigationServiceExt.ToLogin(removebackentry: true, null, directshowresetpassword: false, currentUser.Email);
	}

	public void ForceValidation()
	{
		ValidationInner();
	}

	private void ChangePicture_Click(object sender, RoutedEventArgs e)
	{
		CaptchaImage.Source = null;
		LoadCheckpoint();
	}

	private void Validate_Click(object sender, RoutedEventArgs e)
	{
		if (CaptchaTextblock.Text != null && CaptchaTextblock.Text.Length >= 4)
		{
			SubmitCheckpoint(CaptchaTextblock.Text);
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Checkpoint/CheckpointPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			TitlePanel = (StackPanel)((FrameworkElement)this).FindName("TitlePanel");
			ApplicationTitle = (TextBlock)((FrameworkElement)this).FindName("ApplicationTitle");
			ContentPanel = (StackPanel)((FrameworkElement)this).FindName("ContentPanel");
			CaptchaImage = (Image)((FrameworkElement)this).FindName("CaptchaImage");
			CaptchaTextblock = (TextBox)((FrameworkElement)this).FindName("CaptchaTextblock");
			LoadingPanel = (Grid)((FrameworkElement)this).FindName("LoadingPanel");
		}
	}
}
