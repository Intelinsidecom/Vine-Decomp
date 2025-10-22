using System;
using System.ComponentModel;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using GalaSoft.MvvmLight.Messaging;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microsoft.Phone.Tasks;
using Vine.Datas;
using Vine.Pages.Settings.ViewModels;
using Vine.Resources;
using Vine.Services;
using Vine.Utils;
using Vine.ViewModels;

namespace Vine.Pages.Settings;

public class SettingsPage : PhoneApplicationPage
{
	private bool _stripeselected;

	private bool _usefullpost;

	private bool _isInit;

	internal Grid BrowserPanel;

	internal Grid LayoutRoot;

	internal Grid ContentPanel1;

	internal Image Picture;

	internal Grid ContentPanel2;

	internal Button SyncTwitterButton;

	internal Button DisconnectTwitterButton;

	internal Button SyncFaceButton;

	internal Button DisconnectFacebookButton;

	internal Button ResetPasswordButton;

	internal ListPicker ListPickerModernColor;

	internal CheckBox UseFullScreenCheckBox;

	internal CheckBox AgentCheckBox;

	internal Button AdButton;

	internal Button TwitterButton;

	internal Grid YourContentPanel;

	internal CheckBox ProtectedCheck;

	internal CheckBox SensitiveCheck;

	internal Grid LoadingPanel;

	internal ProgressBar ProgressLoading;

	private bool _contentLoaded;

	public Vine.Datas.Datas Data { get; set; }

	public SettingsPage()
	{
		//IL_004c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0051: Unknown result type (might be due to invalid IL or missing references)
		//IL_0053: Expected O, but got Unknown
		//IL_0058: Expected O, but got Unknown
		//IL_00a4: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ae: Expected O, but got Unknown
		//IL_00b6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c0: Expected O, but got Unknown
		SettingsViewModel settingsViewModel = (SettingsViewModel)(((FrameworkElement)this).DataContext = ViewModelLocator.SettingsStatic);
		settingsViewModel.LoadProfile();
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			Grid yourContentPanel = YourContentPanel;
			Grid layoutRoot = LayoutRoot;
			Brush val = (Brush)Application.Current.Resources[(object)"StripeBrush"];
			Brush background = val;
			((Panel)layoutRoot).Background = val;
			((Panel)yourContentPanel).Background = background;
		}
		Data = DatasProvider.Instance;
		if (Data.CurrentUser.TwitterAccess == null)
		{
			((Control)TwitterButton).IsEnabled = false;
		}
		((ToggleButton)AgentCheckBox).IsChecked = PeriodicAgent.HasPeriodicAgent();
		((FrameworkElement)this).Loaded += new RoutedEventHandler(SettingsPage_Loaded);
		((FrameworkElement)this).Loaded += new RoutedEventHandler(SettingsPageAd_Loaded);
	}

	private void SettingsPageAd_Loaded(object sender, RoutedEventArgs e)
	{
		if (AppVersion.IsHaveAds())
		{
			((UIElement)AdButton).Visibility = (Visibility)0;
		}
		else
		{
			((UIElement)AdButton).Visibility = (Visibility)1;
		}
	}

	private void SettingsPage_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_0012: Expected O, but got Unknown
		//IL_0012: Unknown result type (might be due to invalid IL or missing references)
		//IL_0018: Expected O, but got Unknown
		//IL_0024: Unknown result type (might be due to invalid IL or missing references)
		//IL_002e: Expected O, but got Unknown
		((FrameworkElement)this).Loaded -= new RoutedEventHandler(SettingsPage_Loaded);
		Binding val = new Binding();
		val.Path = new PropertyPath("Data.ModernColor", new object[0]);
		val.Mode = (BindingMode)3;
		Binding val2 = val;
		((FrameworkElement)ListPickerModernColor).SetBinding(ListPicker.SelectedIndexProperty, val2);
	}

	protected override void OnBackKeyPress(CancelEventArgs e)
	{
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		((PhoneApplicationPage)this).OnBackKeyPress(e);
		if ((int)((UIElement)YourContentPanel).Visibility == 0)
		{
			((UIElement)YourContentPanel).Visibility = (Visibility)1;
			e.Cancel = true;
		}
	}

	private void ModernColor_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		SetBackground();
		((App)(object)Application.Current).SwitchTheme(((ListPicker)sender).SelectedIndex);
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		((Page)this).OnNavigatedTo(e);
		if (!_isInit || (int)e.NavigationMode == 0)
		{
			_isInit = true;
			Vine.Datas.Datas instance = DatasProvider.Instance;
			_stripeselected = instance.RemoveStripes;
			_usefullpost = instance.UseFullScreenPost;
		}
		ManageTwitterButton();
		ManageFacebookButton();
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_000e: Invalid comparison between Unknown and I4
		((Page)this).OnNavigatingFrom(e);
		if ((int)e.NavigationMode == 1)
		{
			Vine.Datas.Datas instance = DatasProvider.Instance;
			if (_stripeselected != instance.RemoveStripes || _usefullpost != instance.UseFullScreenPost)
			{
				((CancelEventArgs)(object)e).Cancel = true;
				NavigationServiceExt.ToTimeline(null, removebackentry: true);
			}
		}
	}

	private void ManageTwitterButton()
	{
		if (DatasProvider.Instance.CurrentUser.TwitterAccess != null)
		{
			((UIElement)SyncTwitterButton).Visibility = (Visibility)1;
			((UIElement)DisconnectTwitterButton).Visibility = (Visibility)0;
		}
		else
		{
			((UIElement)SyncTwitterButton).Visibility = (Visibility)0;
			((UIElement)DisconnectTwitterButton).Visibility = (Visibility)1;
		}
	}

	private void ChangeTileColor_Click(object sender, RoutedEventArgs e)
	{
		//IL_000a: Unknown result type (might be due to invalid IL or missing references)
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0040: Unknown result type (might be due to invalid IL or missing references)
		//IL_0039: Unknown result type (might be due to invalid IL or missing references)
		//IL_004f: Expected O, but got Unknown
		ShellTile.ActiveTiles.First().Update((ShellTileData)new IconicTileData
		{
			BackgroundColor = (Color)(((ToggleButton)(CheckBox)sender).IsChecked.Value ? Colors.Transparent : ((Color)Application.Current.Resources[(object)"PrincColor"]))
		});
	}

	private void ManageFacebookButton()
	{
		if (DatasProvider.Instance.CurrentUser.FacebookAccess != null)
		{
			((UIElement)SyncFaceButton).Visibility = (Visibility)1;
			((UIElement)DisconnectFacebookButton).Visibility = (Visibility)0;
		}
		else
		{
			((UIElement)SyncFaceButton).Visibility = (Visibility)0;
			((UIElement)DisconnectFacebookButton).Visibility = (Visibility)1;
		}
	}

	private void Name_Modified(object sender, RoutedEventArgs e)
	{
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		string username = ((TextBox)sender).Text;
		UpdateData("username", username, AppResources.Username, delegate(bool res)
		{
			if (res)
			{
				Data.CurrentUser.User.Name = username;
				((SettingsViewModel)((FrameworkElement)this).DataContext).Profile.ChangeName(username);
			}
		});
	}

	private void Logout_Click(object sender, RoutedEventArgs e)
	{
		Vine.Datas.Datas instance = DatasProvider.Instance;
		instance.CurrentUser.Service.LogoutAsync();
		instance.RemoveUser();
		instance.Save();
		ToastHelper.Show(AppResources.ToastYourAreDisconnected, afternav: true, (Orientation)1);
		ViewModelLocator.Clear();
		if (instance.Users.Count > 0)
		{
			NavigationServiceExt.ToManageAccount(removebackentry: true);
		}
		else
		{
			NavigationServiceExt.ToLogin(removebackentry: true);
		}
	}

	private void Description_Modified(object sender, RoutedEventArgs e)
	{
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		string description = ((TextBox)sender).Text;
		UpdateData("description", description, AppResources.Description, delegate(bool res)
		{
			if (res)
			{
				((SettingsViewModel)((FrameworkElement)this).DataContext).Profile.ChangeDescription(description);
			}
		});
	}

	private void Location_Modified(object sender, RoutedEventArgs e)
	{
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		string location = ((TextBox)sender).Text;
		UpdateData("location", location, AppResources.Location, delegate(bool res)
		{
			if (res)
			{
				((SettingsViewModel)((FrameworkElement)this).DataContext).Profile.ChangeLocation(location);
			}
		});
	}

	private async Task UpdateData(string name, string value, string literalName, Action<bool> HasChanged, string extra = null)
	{
		Vine.Datas.Datas instance = DatasProvider.Instance;
		try
		{
			bool flag = await instance.CurrentUser.Service.ModifyProfileAsync(name, value, extra);
			if (flag)
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					try
					{
						ToastHelper.Show(string.Format(AppResources.ToastDataUpdated, literalName), afternav: false, (Orientation)1);
					}
					catch
					{
					}
				});
			}
			HasChanged(flag);
		}
		catch (ServiceServerErrorException ex)
		{
			ToastHelper.Show(ex.HttpErrorMessage, afternav: false, (Orientation)0);
			HasChanged(obj: false);
		}
	}

	private async void IncludePromoted_Click(object sender, RoutedEventArgs e)
	{
		CheckBox toggle = (CheckBox)sender;
		bool value = ((ToggleButton)toggle).IsChecked.Value;
		((Control)toggle).IsEnabled = false;
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			bool num = await currentUser.Service.ModifyPreferencesAsync("includePromoted", value ? "1" : "0");
			if (!num)
			{
				((ToggleButton)toggle).IsChecked = !((ToggleButton)toggle).IsChecked;
			}
			else
			{
				((SettingsViewModel)((FrameworkElement)this).DataContext).Profile.IncludePromoted = value;
			}
			if (num)
			{
				Messenger.Default.Send(new NotificationMessage("ChangeEditorFollow"));
			}
		}
		catch (ServiceServerErrorException ex)
		{
			ToastHelper.Show(ex.HttpErrorMessage, afternav: false, (Orientation)0);
		}
		finally
		{
			((Control)toggle).IsEnabled = true;
		}
	}

	private async void ChangeAvatar_Click(object sender, RoutedEventArgs e)
	{
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		PhotoChooserTask val = new PhotoChooserTask
		{
			PixelHeight = 200,
			PixelWidth = 200,
			ShowCamera = true
		};
		((ChooserBase<PhotoResult>)val).Completed += async delegate(object senderr, PhotoResult er)
		{
			if ((int)((TaskEventArgs)er).TaskResult == 1)
			{
				try
				{
					string uri = await currentUser.Service.UploadAvatarAsync(er.ChosenPhoto);
					if (uri != null)
					{
						if (await currentUser.Service.ModifyProfileAsync("avatarUrl", null, uri))
						{
							BitmapImage val2 = new BitmapImage();
							((BitmapSource)val2).SetSource(er.ChosenPhoto);
							Picture.Source = (ImageSource)(object)val2;
							ToastHelper.Show(AppResources.ToastAvatarUpdated, afternav: false, (Orientation)0);
							((SettingsViewModel)((FrameworkElement)this).DataContext).Profile.ChangePicture(uri);
							Vine.Datas.Datas instance = DatasProvider.Instance;
							instance.CurrentUser.User.ChangePicture(uri);
							instance.Save();
						}
					}
					else
					{
						ToastHelper.Show(AppResources.ToastAvatarUpdated, afternav: false, (Orientation)0);
					}
				}
				catch (ServiceServerErrorException ex)
				{
					ToastHelper.Show(ex.HttpErrorMessage ?? AppResources.ToastFailToConnect, afternav: false, (Orientation)0);
				}
			}
		};
		((ChooserBase<PhotoResult>)val).Show();
	}

	private void VineSearch_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToSearch(selectFriends: true);
	}

	private void SearchContactPhone_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToSuggestionContact();
	}

	private void SearchContactTwitter_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToSuggestionTwitter();
	}

	private void SyncTwitter_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToSyncTwitter();
	}

	private void ResetPassword_Click(object sender, RoutedEventArgs e)
	{
		SettingsViewModel obj = (SettingsViewModel)((FrameworkElement)this).DataContext;
		ShowLoading();
		ServiceUtils.ResetPassword(obj.Profile.Email, delegate
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				HideLoading();
			});
		});
	}

	private void Email_Modified(object sender, RoutedEventArgs e)
	{
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		string txt = ((TextBox)sender).Text;
		UpdateData("email", txt, AppResources.Email, delegate(bool res)
		{
			if (res)
			{
				((SettingsViewModel)((FrameworkElement)this).DataContext).Profile.Email = txt;
			}
		});
	}

	private void Phone_Modified(object sender, RoutedEventArgs e)
	{
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		string txt = ((TextBox)sender).Text;
		UpdateData("phoneNumber", txt, AppResources.PhoneNumber, delegate(bool res)
		{
			if (res)
			{
				((SettingsViewModel)((FrameworkElement)this).DataContext).Profile.PhoneNumber = txt;
			}
		}, "&locale=" + RegionInfo.CurrentRegion.TwoLetterISORegionName);
	}

	private async void DisconnectTwitter_Click(object sender, RoutedEventArgs e)
	{
		ShowLoading();
		Vine.Datas.Datas data = DatasProvider.Instance;
		try
		{
			if (await data.CurrentUser.Service.DisconnectTwitterAsync())
			{
				data.CurrentUser.TwitterAccess = null;
				data.Save();
			}
			ManageTwitterButton();
		}
		catch (ServiceServerErrorException ex)
		{
			ToastHelper.Show(ex.HttpErrorMessage ?? AppResources.ToastCantDisconnect, afternav: false, (Orientation)0);
		}
		finally
		{
			HideLoading();
		}
	}

	private void Agent_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_001d: Unknown result type (might be due to invalid IL or missing references)
		if (((ToggleButton)(CheckBox)sender).IsChecked.Value)
		{
			if (PeriodicAgent.StartPeriodicAgent() == null)
			{
				((ToggleButton)(CheckBox)sender).IsChecked = false;
			}
		}
		else
		{
			PeriodicAgent.RemovePeriodicAgent();
		}
	}

	private void YourContent_Click(object sender, RoutedEventArgs e)
	{
		_ = DatasProvider.Instance;
		((UIElement)YourContentPanel).Visibility = (Visibility)0;
	}

	private async void Protected_Click(object sender, RoutedEventArgs e)
	{
		CheckBox toggle = (CheckBox)sender;
		bool value = ((ToggleButton)toggle).IsChecked.Value;
		((Control)toggle).IsEnabled = false;
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			bool num = await currentUser.Service.ModifyProfileAsync("private", value ? "1" : "0", null);
			((Control)toggle).IsEnabled = true;
			if (!num)
			{
				((ToggleButton)toggle).IsChecked = !((ToggleButton)toggle).IsChecked;
			}
			else
			{
				((SettingsViewModel)((FrameworkElement)this).DataContext).Profile.Private = value;
			}
		}
		catch (ServiceServerErrorException ex)
		{
			ToastHelper.Show(ex.HttpErrorMessage, afternav: false, (Orientation)0);
			((Control)toggle).IsEnabled = true;
		}
	}

	private async void Sensitive_Click(object sender, RoutedEventArgs e)
	{
		CheckBox toggle = (CheckBox)sender;
		bool value = ((ToggleButton)toggle).IsChecked.Value;
		((Control)toggle).IsEnabled = false;
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			if (!(await currentUser.Service.ModifyExplicitAsync(value)))
			{
				((ToggleButton)toggle).IsChecked = !((ToggleButton)toggle).IsChecked;
			}
			else
			{
				((SettingsViewModel)((FrameworkElement)this).DataContext).Profile.ExplicitContent = value;
			}
		}
		catch (ServiceServerErrorException ex)
		{
			ToastHelper.Show(ex.HttpErrorMessage, afternav: false, (Orientation)0);
		}
		finally
		{
			((Control)toggle).IsEnabled = true;
		}
	}

	private void SyncFacebook_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToSyncFacebook();
	}

	private void RemoveStripe_Click(object sender, RoutedEventArgs e)
	{
		SetBackground();
	}

	private void SetBackground()
	{
		//IL_0054: Unknown result type (might be due to invalid IL or missing references)
		//IL_005e: Expected O, but got Unknown
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0039: Expected O, but got Unknown
		if (LayoutRoot != null)
		{
			if (!DatasProvider.Instance.AddStripComputed)
			{
				((Panel)LayoutRoot).Background = (Brush)Application.Current.Resources[(object)"NoStripeBrush"];
			}
			else
			{
				((Panel)LayoutRoot).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
			}
		}
	}

	private async void DisconnectFacebook_Click(object sender, RoutedEventArgs e)
	{
		ShowLoading();
		Vine.Datas.Datas data = DatasProvider.Instance;
		WebBrowser bro = new WebBrowser();
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)BrowserPanel).Children).Add((UIElement)(object)bro);
		try
		{
			if (await data.CurrentUser.Service.DisconnectFacebookAsync())
			{
				data.CurrentUser.FacebookAccess = null;
				await WebBrowserExtensions.ClearCookiesAsync(bro);
				data.Save();
			}
			ManageFacebookButton();
		}
		catch (ServiceServerErrorException ex)
		{
			ToastHelper.Show(ex.HttpErrorMessage ?? AppResources.ToastCantDisconnect, afternav: false, (Orientation)0);
		}
		finally
		{
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)BrowserPanel).Children).Clear();
			HideLoading();
		}
	}

	private void EmptyInternetCache_Click(object sender, RoutedEventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		//IL_0011: Expected O, but got Unknown
		WebBrowser val = new WebBrowser();
		WebBrowserExtensions.ClearInternetCacheAsync(val);
		WebBrowserExtensions.ClearCookiesAsync(val);
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

	private void RemoveAds_Click(object sender, RoutedEventArgs e)
	{
		AppVersion.BuyAds();
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Settings/SettingsPage.xaml", UriKind.Relative));
			BrowserPanel = (Grid)((FrameworkElement)this).FindName("BrowserPanel");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			ContentPanel1 = (Grid)((FrameworkElement)this).FindName("ContentPanel1");
			Picture = (Image)((FrameworkElement)this).FindName("Picture");
			ContentPanel2 = (Grid)((FrameworkElement)this).FindName("ContentPanel2");
			SyncTwitterButton = (Button)((FrameworkElement)this).FindName("SyncTwitterButton");
			DisconnectTwitterButton = (Button)((FrameworkElement)this).FindName("DisconnectTwitterButton");
			SyncFaceButton = (Button)((FrameworkElement)this).FindName("SyncFaceButton");
			DisconnectFacebookButton = (Button)((FrameworkElement)this).FindName("DisconnectFacebookButton");
			ResetPasswordButton = (Button)((FrameworkElement)this).FindName("ResetPasswordButton");
			ListPickerModernColor = (ListPicker)((FrameworkElement)this).FindName("ListPickerModernColor");
			UseFullScreenCheckBox = (CheckBox)((FrameworkElement)this).FindName("UseFullScreenCheckBox");
			AgentCheckBox = (CheckBox)((FrameworkElement)this).FindName("AgentCheckBox");
			AdButton = (Button)((FrameworkElement)this).FindName("AdButton");
			TwitterButton = (Button)((FrameworkElement)this).FindName("TwitterButton");
			YourContentPanel = (Grid)((FrameworkElement)this).FindName("YourContentPanel");
			ProtectedCheck = (CheckBox)((FrameworkElement)this).FindName("ProtectedCheck");
			SensitiveCheck = (CheckBox)((FrameworkElement)this).FindName("SensitiveCheck");
			LoadingPanel = (Grid)((FrameworkElement)this).FindName("LoadingPanel");
			ProgressLoading = (ProgressBar)((FrameworkElement)this).FindName("ProgressLoading");
		}
	}
}
