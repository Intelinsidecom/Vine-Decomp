using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;
using Vine.Controls;
using Vine.Datas;
using Vine.Pages.ManageAccount.ViewModel;
using Vine.Pages.ManageAccount.ViewModels;
using Vine.ViewModels;

namespace Vine.Pages.ManageAccount;

public class ManageAccountPage : PhoneApplicationPage
{
	private bool _isInit;

	internal Grid LayoutRoot;

	internal Rectangle ShadowRectangle;

	internal Grid ModernAppBarContainer;

	internal ModernAppBar ModernInstaMainAppBar;

	private bool _contentLoaded;

	public Vine.Datas.Datas Data { get; set; }

	public ManageAccountPage()
	{
		//IL_0048: Unknown result type (might be due to invalid IL or missing references)
		//IL_0052: Expected O, but got Unknown
		Data = DatasProvider.Instance;
		((FrameworkElement)this).DataContext = new ManageAccountViewModel();
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)LayoutRoot).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
			((UIElement)ShadowRectangle).Visibility = (Visibility)0;
		}
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

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		((Page)this).OnNavigatedTo(e);
		if ((!_isInit || (int)e.NavigationMode == 0) && ((Page)this).NavigationContext.QueryString.ContainsKey("removebackentry"))
		{
			while (((Page)this).NavigationService.RemoveBackEntry() != null)
			{
			}
		}
	}

	protected void SetPrimary_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		ManageUser primaryUser = (ManageUser)((FrameworkElement)sender).DataContext;
		((ManageAccountViewModel)((FrameworkElement)this).DataContext).SetPrimaryUser(primaryUser);
	}

	protected void RemoveAccount_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		ManageUser manageUser = (ManageUser)((FrameworkElement)sender).DataContext;
		if (manageUser.User == Data.CurrentUser)
		{
			while (((Page)this).NavigationService.RemoveBackEntry() != null)
			{
			}
		}
		((ManageAccountViewModel)((FrameworkElement)this).DataContext).RemoveUser(manageUser);
	}

	protected void Add_Click(object sender, RoutedEventArgs e)
	{
		ViewModelLocator.Clear();
		NavigationServiceExt.ToLogin(removebackentry: false);
	}

	private void User_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_00a0: Unknown result type (might be due to invalid IL or missing references)
		if (e.AddedItems.Count != 0 && e.AddedItems[0] != null)
		{
			DataUser user = ((ManageUser)e.AddedItems[0]).User;
			if (user != Data.CurrentUser)
			{
				Data.ReinitUser();
				ViewModelLocator.Clear();
				Data.CurrentUserId = user.User.Id;
				NavigationServiceExt.ToTimeline(null, removebackentry: true);
				BackgroundDownloadManager.Init();
			}
			else if (((Page)this).NavigationService.CanGoBack)
			{
				((Page)this).NavigationService.GoBack();
			}
			else
			{
				NavigationServiceExt.ToTimeline(null, removebackentry: true);
				BackgroundDownloadManager.Init();
			}
			((Selector)(ListBox)sender).SelectedItem = null;
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/ManageAccount/ManageAccountPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			ShadowRectangle = (Rectangle)((FrameworkElement)this).FindName("ShadowRectangle");
			ModernAppBarContainer = (Grid)((FrameworkElement)this).FindName("ModernAppBarContainer");
			ModernInstaMainAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernInstaMainAppBar");
		}
	}
}
