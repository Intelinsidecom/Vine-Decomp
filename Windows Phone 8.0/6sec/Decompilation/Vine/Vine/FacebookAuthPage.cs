using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;

namespace Vine;

public class FacebookAuthPage : PhoneApplicationPage
{
	private readonly FBHelper FBHelp;

	internal Grid LayoutRoot;

	internal ProgressBar ProgressBar;

	internal StackPanel TitlePanel;

	internal TextBlock ApplicationTitle;

	internal Grid ContentPanel;

	internal WebBrowser BrowserControl;

	private bool _contentLoaded;

	public FacebookAuthPage()
	{
		//IL_0060: Unknown result type (might be due to invalid IL or missing references)
		//IL_006a: Expected O, but got Unknown
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)LayoutRoot).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
		FBHelp = new FBHelper();
		FBHelp.SetBrowser(BrowserControl);
		((FrameworkElement)this).Loaded += new RoutedEventHandler(Page_Loaded);
	}

	private async void Page_Loaded(object sender, RoutedEventArgs e)
	{
		string type = ((Page)this).NavigationContext.QueryString["type"];
		string postid = null;
		if (((Page)this).NavigationContext.QueryString.ContainsKey("postid"))
		{
			postid = ((Page)this).NavigationContext.QueryString["postid"];
		}
		FBHelp.ConnectMe(shorttoken: true, async delegate(string token, string facebookUserId)
		{
			if (token == null || facebookUserId == null)
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					ToastHelper.Show(AppResources.ToastCantPostFacebook, afternav: true, (Orientation)0);
					((Page)this).NavigationService.GoBack();
				});
				return;
			}
			try
			{
				DatasProvider.Instance.CurrentUser.Service.AddFacebookToProfileAsync(facebookUserId, token);
				Vine.Datas.Datas instance = DatasProvider.Instance;
				instance.CurrentUser.FacebookAccess = new FacebookAccess
				{
					AccessToken = token,
					UserId = facebookUserId
				};
				instance.Save();
				if (type == "sync")
				{
					((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
					{
						((Page)this).NavigationService.GoBack();
					});
				}
				else if (type == "contacts")
				{
					if (((Page)this).NavigationContext.QueryString.ContainsKey("subtype"))
					{
						((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
						{
							NavigationServiceExt.ToListContactsFacebook(removebackentry: true, ((Page)this).NavigationContext.QueryString["subtype"]);
						});
					}
					else
					{
						((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
						{
							NavigationServiceExt.ToListContactsFacebook(removebackentry: true);
						});
					}
				}
				else
				{
					DataUser currentUser = DatasProvider.Instance.CurrentUser;
					if (currentUser != null)
					{
						try
						{
							await currentUser.Service.SharePostFacebookAsync(postid);
							ToastHelper.Show(AppResources.ToastMessagePostedOnFacebook, afternav: true, (Orientation)0);
							return;
						}
						catch (ServiceServerErrorException ex)
						{
							ToastHelper.Show(string.IsNullOrEmpty(ex.HttpErrorMessage) ? AppResources.ToastCantPostFacebook : ex.HttpErrorMessage, afternav: true, (Orientation)0);
							return;
						}
						finally
						{
							((Page)this).NavigationService.GoBack();
						}
					}
				}
			}
			catch (ServiceServerErrorException ex2)
			{
				ServiceServerErrorException ex3 = ex2;
				ServiceServerErrorException ex4 = ex3;
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					ToastHelper.Show(ex4.HttpErrorMessage ?? AppResources.ToastCantSyncFacebook, afternav: true, (Orientation)0);
					((Page)this).NavigationService.GoBack();
				});
			}
		});
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		((UIElement)ProgressBar).Visibility = (Visibility)0;
		ProgressBar.IsIndeterminate = true;
	}

	private void Browser_Navigated(object sender, NavigationEventArgs e)
	{
		ProgressBar.IsIndeterminate = false;
		((UIElement)ProgressBar).Visibility = (Visibility)1;
	}

	private void Browser_Navigating(object sender, NavigatingEventArgs e)
	{
		ProgressBar.IsIndeterminate = true;
		((UIElement)ProgressBar).Visibility = (Visibility)0;
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Facebook/FacebookAuthPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			ProgressBar = (ProgressBar)((FrameworkElement)this).FindName("ProgressBar");
			TitlePanel = (StackPanel)((FrameworkElement)this).FindName("TitlePanel");
			ApplicationTitle = (TextBlock)((FrameworkElement)this).FindName("ApplicationTitle");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			BrowserControl = (WebBrowser)((FrameworkElement)this).FindName("BrowserControl");
		}
	}
}
