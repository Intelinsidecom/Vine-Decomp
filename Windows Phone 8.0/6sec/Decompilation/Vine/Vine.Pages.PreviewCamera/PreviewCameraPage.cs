using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Vine.Controls;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;
using Windows.Storage;

namespace Vine.Pages.PreviewCamera;

public class PreviewCameraPage : PhoneApplicationPage
{
	private StorageFolder _applicationFolder;

	private List<string> _playlist = new List<string>();

	private int _index = -1;

	private bool _navigatedAfterBuy;

	private bool _forceddirect;

	private bool _isInit;

	private string _selectedPath;

	internal Grid LayoutRoot;

	internal Grid PreviewPanel;

	internal MediaElement MediaPlayer;

	internal TextBlock PreviewSubtitleTextBlock;

	internal Grid ModernAppBarContainer;

	internal ModernAppBar ModernInstaMainAppBar;

	private bool _contentLoaded;

	public PreviewCameraPage()
	{
		//IL_0042: Unknown result type (might be due to invalid IL or missing references)
		//IL_004c: Expected O, but got Unknown
		InitializeComponent();
		((UIElement)PreviewSubtitleTextBlock).Visibility = (Visibility)1;
		_applicationFolder = ApplicationData.Current.LocalFolder;
		((FrameworkElement)this).Loaded += new RoutedEventHandler(PreviewCameraPage_Loaded);
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

	private async void PreviewCameraPage_Loaded(object sender, RoutedEventArgs e)
	{
		((FrameworkElement)this).Loaded -= new RoutedEventHandler(PreviewCameraPage_Loaded);
		TimerHelper.ToTime(TimeSpan.FromMilliseconds(300.0), delegate
		{
			PlayVideo();
		});
	}

	private async Task PlayVideo()
	{
		while (_playlist.Count != 0)
		{
			_index = (_index + 1) % _playlist.Count;
			try
			{
				MediaPlayer.Source = new Uri(_playlist[_index]);
				MediaPlayer.Play();
				break;
			}
			catch
			{
				Thread.Sleep(10);
			}
		}
	}

	private async Task Init(string path)
	{
		_selectedPath = path;
		if (path == null)
		{
			try
			{
				StorageFolder folder = await ApplicationData.Current.LocalFolder.GetFolderAsync("mpegpart");
				try
				{
					StorageFile val = await folder.GetFileAsync("finalRender.mp4");
					if (val != null)
					{
						_playlist.Add(val.Path);
					}
					return;
				}
				catch
				{
				}
				foreach (StorageFile item in (await folder.GetFilesAsync()).OrderBy((StorageFile f) => f.Name))
				{
					_playlist.Add(item.Path);
				}
				return;
			}
			catch
			{
				return;
			}
		}
		_playlist.Add(path);
	}

	private void MediaPlayer_MediaEnded(object sender, RoutedEventArgs e)
	{
		PlayVideo();
	}

	protected override async void OnNavigatedTo(NavigationEventArgs e)
	{
		_003C_003En__FabricatedMethod0(e);
		if (_playlist == null || _playlist.Count == 0)
		{
			string value = null;
			((Page)this).NavigationContext.QueryString.TryGetValue("path", out value);
			await Init(value);
		}
		if ((int)e.NavigationMode == 1 && _navigatedAfterBuy)
		{
			_navigatedAfterBuy = false;
			if (AppVersion.IsCanUpload())
			{
				GoPostVideo();
			}
		}
		if (!_isInit || (int)e.NavigationMode == 0)
		{
			_isInit = true;
			string value2 = null;
			if (((Page)this).NavigationContext.QueryString.TryGetValue("forceddirect", out value2))
			{
				_forceddirect = value2 == "true";
			}
		}
	}

	private async void AppBarNext_Click(object sender, RoutedEventArgs e)
	{
		if (DatasProvider.Instance.HAU && !AppVersion.IsCanUpload())
		{
			if ((int)MessageBox.Show(AppResources.UploadLimitation, AppResources.InAppPurchase, (MessageBoxButton)1) == 1)
			{
				_navigatedAfterBuy = true;
				await AppVersion.BuyUpload();
			}
		}
		else
		{
			GoPostVideo();
		}
	}

	private void GoPostVideo()
	{
		string imagePath = null;
		if (((Page)this).NavigationContext.QueryString.TryGetValue("imagepath", out imagePath))
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				NavigationServiceExt.ToPostVideo(null, _forceddirect, _selectedPath, imagePath);
			});
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/PreviewVideo/PreviewVideoPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			PreviewPanel = (Grid)((FrameworkElement)this).FindName("PreviewPanel");
			MediaPlayer = (MediaElement)((FrameworkElement)this).FindName("MediaPlayer");
			PreviewSubtitleTextBlock = (TextBlock)((FrameworkElement)this).FindName("PreviewSubtitleTextBlock");
			ModernAppBarContainer = (Grid)((FrameworkElement)this).FindName("ModernAppBarContainer");
			ModernInstaMainAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernInstaMainAppBar");
		}
	}

	[CompilerGenerated]
	private void _003C_003En__FabricatedMethod0(NavigationEventArgs e)
	{
		((Page)this).OnNavigatedTo(e);
	}
}
