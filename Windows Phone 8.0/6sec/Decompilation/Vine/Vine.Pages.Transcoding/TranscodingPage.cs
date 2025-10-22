using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;
using Vine.Controls;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;
using Vine.ViewModels;
using Windows.Phone.System.Power;

namespace Vine.Pages.Transcoding;

public class TranscodingPage : PhoneApplicationPage
{
	private bool fromPost;

	private bool _isInit;

	internal Grid LayoutRoot;

	internal TextBlock PageTitle;

	internal Grid ContentPanel;

	internal Rectangle ShadowRectangle;

	internal Grid BatterySaverPanel;

	internal Grid ModernAppBarContainer;

	internal ModernAppBar ModernAppBar;

	private bool _contentLoaded;

	public TranscodingPage()
	{
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		//IL_006f: Expected O, but got Unknown
		//IL_0042: Unknown result type (might be due to invalid IL or missing references)
		//IL_004c: Expected O, but got Unknown
		((FrameworkElement)this).DataContext = DatasProvider.Instance.Encodings;
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)ContentPanel).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
			((UIElement)ShadowRectangle).Visibility = (Visibility)0;
		}
		BackgroundDownloadManager.RelaunchAllJobs();
		((FrameworkElement)this).Loaded += new RoutedEventHandler(TranscodingPage_Loaded);
	}

	private void TranscodingPage_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Invalid comparison between Unknown and I4
		if ((int)PowerManager.PowerSavingMode == 1)
		{
			((UIElement)BatterySaverPanel).Visibility = (Visibility)0;
		}
		else
		{
			((UIElement)BatterySaverPanel).Visibility = (Visibility)1;
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
		else if (fromPost)
		{
			e.Cancel = true;
			NavigationServiceExt.ToTimeline(null, removebackentry: true);
		}
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		((Page)this).OnNavigatedTo(e);
		if (_isInit && (int)e.NavigationMode != 0)
		{
			return;
		}
		_isInit = true;
		fromPost = ((Page)this).NavigationContext.QueryString.ContainsKey("fromPost");
		if (fromPost)
		{
			while (((Page)this).NavigationService.RemoveBackEntry() != null)
			{
			}
		}
	}

	private void Trans_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_00c3: Unknown result type (might be due to invalid IL or missing references)
		if (e.AddedItems.Count == 0 || e.AddedItems[0] == null)
		{
			return;
		}
		EncodingJob job = (EncodingJob)e.AddedItems[0];
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (job.UserId != instance.CurrentUser.User.Id)
		{
			DataUser dataUser = instance.Users.FirstOrDefault((DataUser use) => use.User.Id == job.UserId);
			if (dataUser != null)
			{
				RetryWhenOtherUser(dataUser, job);
			}
		}
		if (job.State == EncodingStep.ERROR && job.PreviousState != EncodingStep.NONE)
		{
			BackgroundDownloadManager.RelaunchJob(job);
		}
		NavigationServiceExt.ToPostVideo(job.Id);
		((LongListSelector)sender).SelectedItem = null;
	}

	private void RetryWhenOtherUser(DataUser user, EncodingJob job)
	{
		//IL_0021: Unknown result type (might be due to invalid IL or missing references)
		//IL_0027: Invalid comparison between Unknown and I4
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if ((int)MessageBox.Show(string.Format(AppResources.EncodeLogOtherUser, user.User.Name), AppVersion.AppName, (MessageBoxButton)1) == 1)
		{
			instance.ReinitUser();
			ViewModelLocator.Clear();
			instance.CurrentUserId = user.User.Id;
			while (((Page)this).NavigationService.RemoveBackEntry() != null)
			{
			}
		}
		else
		{
			job.ChangeUserId(instance.CurrentUserId);
			BackgroundDownloadManager.Retry(job);
		}
	}

	private void MenuCancel_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		if (((FrameworkElement)sender).DataContext is EncodingJob job)
		{
			BackgroundDownloadManager.RemoveJob(job);
		}
	}

	private void MenuCancelAll_Click(object sender, RoutedEventArgs e)
	{
		Vine.Datas.Datas instance = DatasProvider.Instance;
		BackgroundDownloadManager.RemoveAllJobs();
		instance.Save();
	}

	private void MenuRetry_Click(object sender, RoutedEventArgs e)
	{
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		Vine.Datas.Datas instance = DatasProvider.Instance;
		EncodingJob job = ((FrameworkElement)sender).DataContext as EncodingJob;
		if (job == null)
		{
			return;
		}
		if (job.UserId != instance.CurrentUser.User.Id)
		{
			DataUser dataUser = instance.Users.FirstOrDefault((DataUser use) => use.User.Id == job.UserId);
			if (dataUser != null)
			{
				RetryWhenOtherUser(dataUser, job);
				return;
			}
		}
		BackgroundDownloadManager.Retry(job);
	}

	private void BatterySaverPanel_Tap(object sender, GestureEventArgs e)
	{
		((UIElement)BatterySaverPanel).Visibility = (Visibility)1;
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
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Transcoding/TranscodingPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			PageTitle = (TextBlock)((FrameworkElement)this).FindName("PageTitle");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			ShadowRectangle = (Rectangle)((FrameworkElement)this).FindName("ShadowRectangle");
			BatterySaverPanel = (Grid)((FrameworkElement)this).FindName("BatterySaverPanel");
			ModernAppBarContainer = (Grid)((FrameworkElement)this).FindName("ModernAppBarContainer");
			ModernAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernAppBar");
		}
	}
}
