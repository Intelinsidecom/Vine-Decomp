using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Navigation;
using GalaSoft.MvvmLight.Messaging;
using Gen.Services;
using Huyn.Ads;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Vine.Controls;
using Vine.Datas;
using Vine.Pages.Profile.ViewModels;
using Vine.Resources;
using Vine.Services;
using Vine.Services.Models;
using Vine.Utils;

namespace Vine.Pages.Profile;

public class ProfilePage : PhoneApplicationPage
{
	private PostControl _lastpostcontrol;

	private bool _isInit;

	private bool _ismyprofile;

	private ModernAppBarButton _appBarDirectButton;

	private ModernAppBarButton _appBarEditButton;

	internal Grid AdPanel;

	internal ProfileControl ProfileControl;

	internal Grid ModernAppBarContainer;

	internal ModernAppBar ModernMainAppBar;

	private bool _contentLoaded;

	public ProfilePage()
	{
		InitializeComponent();
		_appBarDirectButton = ModernMainAppBar.Buttons[0];
		((FrameworkElement)ModernMainAppBar.Buttons[2]).DataContext = ProfileControl;
		ManageMenuBlocked();
		Messenger.Default.Register(this, delegate(NotificationMessage mes)
		{
			if (mes.Notification == "ADREMOVED")
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
				});
			}
		});
	}

	protected override void OnBackKeyPress(CancelEventArgs e)
	{
		((PhoneApplicationPage)this).OnBackKeyPress(e);
		ModernAppBar modernAppBar = (ModernAppBar)(object)((IEnumerable<UIElement>)((Panel)ModernAppBarContainer).Children).FirstOrDefault((UIElement a) => (int)a.Visibility == 0);
		if (modernAppBar != null && modernAppBar.IsMenuOpened)
		{
			modernAppBar.IsMenuOpened = false;
			e.Cancel = true;
		}
	}

	private void AddAds()
	{
		if (AppVersion.IsHaveAds())
		{
			SystemTray.IsVisible = false;
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
			UIElementCollection children = ((Panel)AdPanel).Children;
			AdRotator adRotator = new AdRotator();
			((FrameworkElement)adRotator).Width = 480.0;
			((FrameworkElement)adRotator).Height = 80.0;
			((PresentationFrameworkCollection<UIElement>)(object)children).Add((UIElement)(object)adRotator);
		}
		else
		{
			SystemTray.IsVisible = true;
		}
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_0076: Unknown result type (might be due to invalid IL or missing references)
		//IL_0080: Expected O, but got Unknown
		((Page)this).OnNavigatedTo(e);
		if (_isInit && (int)e.NavigationMode != 0)
		{
			return;
		}
		_isInit = true;
		if (((Page)this).NavigationContext.QueryString.ContainsKey("removebackentry") || !((Page)this).NavigationService.CanGoBack)
		{
			while (((Page)this).NavigationService.RemoveBackEntry() != null)
			{
			}
			ModernAppBarButton obj = new ModernAppBarButton
			{
				Text = AppResources.Home
			};
			((ContentControl)obj).Content = "\ue10f";
			ModernAppBarButton modernAppBarButton = obj;
			((ButtonBase)modernAppBarButton).Click += new RoutedEventHandler(GoHome_Click);
			ModernMainAppBar.Buttons.Insert(0, modernAppBarButton);
		}
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (((Page)this).NavigationContext.QueryString.ContainsKey("id"))
		{
			string text = ((Page)this).NavigationContext.QueryString["id"];
			ProfileControl.Init(text, delegate
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					ManageMenuBlocked();
				});
			});
			if (instance.CurrentUser != null && instance.CurrentUser.User != null && text == instance.CurrentUser.User.Id)
			{
				((ContentControl)ModernMainAppBar.MenuItems[0]).Content = AppResources.EditMyProfile;
				((UIElement)ModernMainAppBar.Buttons[0]).Visibility = (Visibility)1;
			}
			else
			{
				((UIElement)ModernMainAppBar.Buttons[0]).Visibility = (Visibility)0;
			}
		}
		AddAds();
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		((Page)this).OnNavigatingFrom(e);
		ClearAds();
	}

	private void ClearAds()
	{
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
	}

	private void GoHome_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToTimeline(null, removebackentry: true);
	}

	private void ManageMenuBlocked()
	{
		if (!_ismyprofile)
		{
			IProfile profile = ProfileControl.ViewModel.Profile;
			((ContentControl)ModernMainAppBar.MenuItems[0]).Content = ((profile != null && profile.BlockedByMe) ? AppResources.UnBlockThisPerson : AppResources.BlockThisPerson);
			((Control)ModernMainAppBar.MenuItems[0]).IsEnabled = profile != null && profile.Id != DatasProvider.Instance.CurrentUser.Id;
		}
	}

	private async void MenuBlockPerson_Click(object sender, RoutedEventArgs e)
	{
		if (_ismyprofile)
		{
			NavigationServiceExt.ToSettings();
			return;
		}
		ProfileViewModel vm = ProfileControl.ViewModel;
		vm.IsLoading = true;
		vm.RaisePropertyChanged("IsLoading");
		bool val = !vm.Profile.BlockedByMe;
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		try
		{
			await currentUser.Service.BlockUserAsync(vm.Profile.Id, val);
			vm.Profile.BlockedByMe = val;
			vm.IsLoading = false;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				ManageMenuBlocked();
				ToastHelper.Show(val ? AppResources.UserIsNowBlocked : AppResources.UserIsNowUnblocked, afternav: false, (Orientation)0);
				vm.RaisePropertyChanged("IsLoading");
			});
		}
		catch (ServiceServerErrorException ex)
		{
			ServiceServerErrorException ex2 = ex;
			ServiceServerErrorException ex3 = ex2;
			vm.IsLoading = false;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				vm.RaisePropertyChanged("IsLoading");
				ToastHelper.Show(ex3.HttpErrorMessage ?? "an error has occurred", afternav: false, (Orientation)0);
			});
		}
	}

	private void PinStart_Click(object sender, RoutedEventArgs e)
	{
		try
		{
			ProfileViewModel viewModel = ProfileControl.ViewModel;
			IProfile profile = viewModel.Profile;
			IPostRecord post = viewModel.Posts.FirstOrDefault();
			AppUtils.CreateProfileTile(profile, post);
		}
		catch
		{
		}
	}

	private void AppBarSendMessage_Click(object sender, RoutedEventArgs e)
	{
		try
		{
			NavigationServiceExt.ToCamera(new List<IPerson> { ProfileControl.ViewModel.Profile });
		}
		catch
		{
		}
	}

	private void MyProfileMap_Click(object sender, RoutedEventArgs e)
	{
		ProfileControl.OpenMap();
	}

	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Profile/ProfilePage.xaml", UriKind.Relative));
			AdPanel = (Grid)((FrameworkElement)this).FindName("AdPanel");
			ProfileControl = (ProfileControl)((FrameworkElement)this).FindName("ProfileControl");
			ModernAppBarContainer = (Grid)((FrameworkElement)this).FindName("ModernAppBarContainer");
			ModernMainAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernMainAppBar");
		}
	}
}
