using System;
using System.Diagnostics;
using System.Globalization;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Tasks;
using Vine.Datas;
using Vine.Services;
using Windows.System;

namespace Vine.Pages.About;

public class AboutPage : PhoneApplicationPage
{
	internal Grid LayoutRoot;

	internal Run VersionNumber;

	internal Button AppChallengeButton;

	private bool _contentLoaded;

	public AboutPage()
	{
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)LayoutRoot).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
		if (CultureInfo.CurrentCulture.Name == "fr-FR")
		{
			((UIElement)AppChallengeButton).Visibility = (Visibility)0;
		}
		VersionNumber.Text = AppVersion.Version;
	}

	private void Changelog_Click(object sender, RoutedEventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		new WebBrowserTask
		{
			Uri = new Uri("http://www2.huynapps.com/wp/6sec/changelog.php")
		}.Show();
	}

	private void OtherApp_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToOtherApp();
	}

	private void UserVoice_Click(object sender, RoutedEventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		new WebBrowserTask
		{
			Uri = new Uri("https://6sec.uservoice.com/", UriKind.Absolute)
		}.Show();
	}

	private void Contributors_Click(object sender, RoutedEventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		new WebBrowserTask
		{
			Uri = new Uri("http://www2.huynapps.com/wp/6sec/contributors.php")
		}.Show();
	}

	private void Mail_Click(object sender, RoutedEventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		EmailComposeTask val = new EmailComposeTask();
		val.To = "6sec@6studio.net";
		val.Body = string.Concat("\n\n\n------------------------------------------\napp:" + AppVersion.AppName, AppVersion.Version, "\ndeviceid:", VineGenUtils.GetDeviceUniqueID(), "\n------------------------------------------");
		val.Show();
	}

	private void FAQ_Click(object sender, RoutedEventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		new WebBrowserTask
		{
			Uri = new Uri("http://www2.huynapps.com/wp/6sec/faq.php")
		}.Show();
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

	private void AppChallenge_Click(object sender, RoutedEventArgs e)
	{
		Launcher.LaunchUriAsync(new Uri("appchallenge:appGuid=b91c0dcb-3cad-402b-aa3d-ac9faacc2265", UriKind.Absolute));
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/About/AboutPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			VersionNumber = (Run)((FrameworkElement)this).FindName("VersionNumber");
			AppChallengeButton = (Button)((FrameworkElement)this).FindName("AppChallengeButton");
		}
	}
}
