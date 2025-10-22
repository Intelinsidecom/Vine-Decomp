using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;
using Huyn.Ads;
using Lumia.Imaging;
using Lumia.Imaging.Transforms;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using VideoEffects;
using Vine.Controls;
using Vine.Resources;
using Vine.Services;
using Vine.Utils;
using Windows.Foundation;
using Windows.Media.MediaProperties;
using Windows.Media.Transcoding;
using Windows.Storage;
using Windows.Storage.FileProperties;
using Windows.Storage.Streams;

namespace Vine.Pages.CropVideo;

public class CropVideoPage : PhoneApplicationPage
{
	private class CropVideoResult
	{
		public StorageFile Video;

		public IBuffer FirstFrame;
	}

	private DispatcherTimer _timer;

	public static StorageFile VideoFile;

	private bool _forceDirect;

	private bool _isInit;

	private bool _firstPlay;

	private bool _isAutoFit;

	internal Grid LayoutRoot;

	internal Rectangle AutoFitBorder;

	internal Grid ContentPanel;

	internal Grid MediaPanel;

	internal CropControl CropControl;

	internal MediaElement Media;

	internal StackPanel BackgroundChooserPanel;

	internal Grid TimespanPanel;

	internal RangeSlider CropTimeSlider;

	internal Grid EncodingPanel;

	internal ProgressBar EncodingProgress;

	internal Grid EncodingPanelAd;

	private bool _contentLoaded;

	public CropVideoPage()
	{
		//IL_001d: Unknown result type (might be due to invalid IL or missing references)
		//IL_003d: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0052: Unknown result type (might be due to invalid IL or missing references)
		//IL_006b: Expected O, but got Unknown
		InitializeComponent();
		((ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[0]).Text = AppResources.AutoFit;
		((ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[2]).Text = AppResources.Validate;
		_timer = new DispatcherTimer
		{
			Interval = TimeSpan.FromMilliseconds(20.0)
		};
		_timer.Tick += _timer_Tick;
	}

	private void _timer_Tick(object sender, EventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000c: Invalid comparison between Unknown and I4
		if ((int)Media.CurrentState == 3)
		{
			double totalMilliseconds = Media.Position.TotalMilliseconds;
			if (totalMilliseconds - 10.0 >= CropTimeSlider.UpperValue)
			{
				Pause();
				CropTimeSlider.SetProgress(CropTimeSlider.UpperValue);
			}
			else
			{
				CropTimeSlider.SetProgress(totalMilliseconds);
			}
		}
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		((Page)this).OnNavigatingFrom(e);
		_timer.Stop();
		Pause();
	}

	protected override async void OnNavigatedTo(NavigationEventArgs e)
	{
		_003C_003En__FabricatedMethod0(e);
		_timer.Start();
		if (!_isInit || (int)e.NavigationMode == 0)
		{
			string value = null;
			if (((Page)this).NavigationContext.QueryString.TryGetValue("forceDirect", out value))
			{
				_forceDirect = value.ToLower() == "true";
			}
			if (VideoFile != null)
			{
				VideoProperties val = await VideoFile.Properties.GetVideoPropertiesAsync();
				if (val.Duration.TotalSeconds < 3.0)
				{
					MessageBox.Show(AppResources.VideoTooShort);
					Application.Current.Terminate();
					return;
				}
				if (val.Height != val.Width)
				{
					((ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[0]).IsEnabled = true;
				}
				ManageVideoOrientation(val.Orientation);
			}
		}
		if (VideoFile != null && Media.Source == null)
		{
			VideoProperties val2 = await VideoFile.Properties.GetVideoPropertiesAsync();
			_firstPlay = true;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				Media.Source = new Uri(VideoFile.Path);
			});
			ChangeAppButtonState(play: false);
			if (!_isInit)
			{
				CropTimeSlider.Maximum = val2.Duration.TotalMilliseconds;
				CropTimeSlider.UpperValue = Math.Min(val2.Duration.TotalMilliseconds, 15000.0);
				CropTimeSlider.LowerValueChanged += TimerSlider_ValueChanged;
				CropTimeSlider.UpperValueChanged += TimerSlider_ValueChanged;
				_isInit = true;
			}
		}
	}

	private void TimerSlider_ValueChanged(object sender, double value)
	{
		if (Media.CanSeek)
		{
			Pause();
			Media.Position = TimeSpan.FromMilliseconds(value);
		}
	}

	public void ChangeAppButtonState(bool play)
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Expected O, but got Unknown
		ApplicationBarIconButton val = (ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[1];
		if (play)
		{
			val.Text = AppResources.Play;
			val.IconUri = new Uri("/Assets/AppBar/transport.play.png", UriKind.Relative);
		}
		else
		{
			val.Text = AppResources.Pause;
			val.IconUri = new Uri("/Assets/AppBar/transport.pause.png", UriKind.Relative);
		}
	}

	private void Pause()
	{
		Media.Pause();
	}

	private void PlayPause_Click(object sender, EventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000c: Invalid comparison between Unknown and I4
		if ((int)Media.CurrentState == 3)
		{
			Pause();
			return;
		}
		Media.Position = TimeSpan.FromMilliseconds(CropTimeSlider.LowerValue);
		Media.Play();
		ChangeAppButtonState(play: false);
	}

	private void Media_CurrentStateChanged(object sender, RoutedEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000c: Invalid comparison between Unknown and I4
		//IL_007f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0085: Invalid comparison between Unknown and I4
		//IL_008d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0093: Invalid comparison between Unknown and I4
		if ((int)Media.CurrentState == 3)
		{
			((UIElement)CropControl).Opacity = 1.0;
			if (_firstPlay)
			{
				_firstPlay = false;
				if (CropTimeSlider.LowerValue != 0.0)
				{
					Media.Pause();
					Media.Position = TimeSpan.FromMilliseconds(CropTimeSlider.LowerValue);
					Media.Play();
				}
			}
		}
		else if ((int)Media.CurrentState == 5 || (int)Media.CurrentState == 4)
		{
			ChangeAppButtonState(play: true);
		}
	}

	private async void Validate_Click(object sender, EventArgs e)
	{
		Media.Source = null;
		ShowProgress();
		((ApplicationBarIconButton)sender).IsEnabled = false;
		try
		{
			CropVideoResult result;
			if (_isAutoFit)
			{
				result = await AutoFitVideo(((SolidColorBrush)((Shape)AutoFitBorder).Fill).Color, TimeSpan.FromMilliseconds(CropTimeSlider.LowerValue), TimeSpan.FromMilliseconds(CropTimeSlider.UpperValue));
			}
			else
			{
				VideoProperties val = await VideoFile.Properties.GetVideoPropertiesAsync();
				double num;
				double num2;
				if ((int)val.Orientation == 90 || (int)val.Orientation == 270)
				{
					num = val.Height;
					num2 = val.Width;
				}
				else
				{
					num = val.Width;
					num2 = val.Height;
				}
				Rect selection = CropControl.GetSelection();
				VideoOrientation orientation = val.Orientation;
				if ((int)orientation != 90)
				{
					if ((int)orientation != 180)
					{
						if ((int)orientation == 270)
						{
							((Rect)(ref selection))._002Ector(((Rect)(ref selection)).Top, 1.0 - ((Rect)(ref selection)).Right, ((Rect)(ref selection)).Height, ((Rect)(ref selection)).Width);
						}
					}
					else
					{
						((Rect)(ref selection))._002Ector(1.0 - ((Rect)(ref selection)).Right, 1.0 - ((Rect)(ref selection)).Bottom, ((Rect)(ref selection)).Width, ((Rect)(ref selection)).Height);
					}
				}
				else
				{
					((Rect)(ref selection))._002Ector(1.0 - ((Rect)(ref selection)).Bottom, ((Rect)(ref selection)).Left, ((Rect)(ref selection)).Height, ((Rect)(ref selection)).Width);
				}
				double num3 = ((Rect)(ref selection)).X * num;
				double num4 = ((Rect)(ref selection)).Y * num2;
				double num5 = Math.Min(((Rect)(ref selection)).Width * num, ((Rect)(ref selection)).Height * num2);
				if (num3 + num5 > num)
				{
					num3 = num - num5;
				}
				if (num4 + num5 > num2)
				{
					num4 = num2 - num5;
				}
				result = await CropVideo(num3, num4, (uint)num5, TimeSpan.FromMilliseconds(CropTimeSlider.LowerValue), TimeSpan.FromMilliseconds(CropTimeSlider.UpperValue));
			}
			if (result != null && result.Video != null)
			{
				IBuffer firstFrame = result.FirstFrame;
				StorageFile thumbFile = await ApplicationData.Current.TemporaryFolder.CreateFileAsync("currentimagevideo.jpg", (CreationCollisionOption)1);
				using (Stream stream = await ((IStorageFile)(object)thumbFile).OpenStreamForWriteAsync())
				{
					await firstFrame.AsStream().CopyToAsync(stream);
				}
				NavigationServiceExt.ToPreviewVideo(_forceDirect, thumbFile.Path, result.Video.Path);
				return;
			}
		}
		catch (Exception ex)
		{
			_ = ex.Message;
			_ = ex.Source;
		}
		finally
		{
			HideProgress();
			((ApplicationBarIconButton)sender).IsEnabled = true;
		}
		Media.Source = new Uri(VideoFile.Path);
	}

	private async Task<CropVideoResult> AutoFitVideo(Color color, TimeSpan start, TimeSpan end)
	{
		//IL_000a: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		VideoProperties val = await VideoFile.Properties.GetVideoPropertiesAsync();
		uint num;
		uint num2;
		if ((int)val.Orientation == 90 || (int)val.Orientation == 270)
		{
			num = val.Height;
			num2 = val.Width;
		}
		else
		{
			num = val.Width;
			num2 = val.Height;
		}
		Rect rec;
		if (num2 < num)
		{
			Point location = new Point
			{
				X = 0.0,
				Y = ((int)num2 - num) / 2
			};
			rec = new Rect(location, new Size(num, num));
		}
		else
		{
			Point location2 = new Point
			{
				X = ((int)num - num2) / 2,
				Y = 0.0
			};
			rec = new Rect(location2, new Size(num2, num2));
		}
		LumiaEffectDefinition definition = new LumiaEffectDefinition((FilterChainFactory)(() => (IEnumerable<IFilter>)(object)new IFilter[1] { (IFilter)new ReframingFilter(rec, 0.0) }), (((Color)(ref color)).R << 16) + (((Color)(ref color)).G << 8) + ((Color)(ref color)).B)
		{
			InputWidth = num,
			InputHeight = num2,
			OutputWidth = 480u,
			OutputHeight = 480u
		};
		return await TranscodeVideo(definition, start, end);
	}

	private async Task<CropVideoResult> CropVideo(double x, double y, uint size, TimeSpan start, TimeSpan end)
	{
		VideoProperties val = await VideoFile.Properties.GetVideoPropertiesAsync();
		uint num;
		uint num2;
		if ((int)val.Orientation == 90 || (int)val.Orientation == 270)
		{
			num = val.Height;
			num2 = val.Width;
		}
		else
		{
			num = val.Width;
			num2 = val.Height;
		}
		size = Math.Min(num2, Math.Min(num, size));
		if ((double)num < x + (double)size)
		{
			x = num - size;
		}
		if ((double)num2 < y + (double)size)
		{
			y = num2 - size;
		}
		Rect cropArea = new Rect(x, y, size, size);
		LumiaEffectDefinition definition = new LumiaEffectDefinition((FilterChainFactory)(() => (IEnumerable<IFilter>)(object)new IFilter[1] { (IFilter)new CropFilter(cropArea) }))
		{
			InputWidth = num,
			InputHeight = num2,
			OutputWidth = 480u,
			OutputHeight = 480u
		};
		return await TranscodeVideo(definition, start, end);
	}

	private async Task<CropVideoResult> TranscodeVideo(LumiaEffectDefinition definition, TimeSpan start, TimeSpan end)
	{
		MediaTranscoder transcoder = new MediaTranscoder();
		transcoder.AddVideoEffect(definition.ActivatableClassId, true, definition.Properties);
		transcoder.put_TrimStartTime(start);
		transcoder.put_TrimStopTime(end);
		StorageFile resultFile = await ApplicationData.Current.TemporaryFolder.CreateFileAsync("finalRender.mp4", (CreationCollisionOption)1);
		MediaEncodingProfile encoding = MediaEncodingHelper.GetEncoding();
		IAsyncActionWithProgress<double> obj = (await transcoder.PrepareFileTranscodeAsync((IStorageFile)(object)VideoFile, (IStorageFile)(object)resultFile, encoding)).TranscodeAsync();
		obj.put_Progress((AsyncActionProgressHandler<double>)delegate(IAsyncActionWithProgress<double> e, double progress)
		{
			ShowProgress(progress);
		});
		await obj;
		return new CropVideoResult
		{
			FirstFrame = definition.GetFirstFrame(),
			Video = resultFile
		};
	}

	public void ShowProgress()
	{
		AddAds();
		((UIElement)EncodingPanel).Visibility = (Visibility)0;
		EncodingProgress.IsIndeterminate = true;
		((PhoneApplicationPage)this).ApplicationBar.IsVisible = false;
	}

	public void ShowProgress(double progress)
	{
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			EncodingProgress.IsIndeterminate = false;
			((RangeBase)EncodingProgress).Value = progress;
		});
	}

	public void HideProgress()
	{
		((UIElement)EncodingPanel).Visibility = (Visibility)1;
		EncodingProgress.IsIndeterminate = false;
		((PhoneApplicationPage)this).ApplicationBar.IsVisible = true;
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)EncodingPanelAd).Children).Clear();
	}

	private async void ContentPanel_SizeChanged(object sender, SizeChangedEventArgs e)
	{
		Size newSize = e.NewSize;
		if (((Size)(ref newSize)).Width != 0.0)
		{
			ManageVideoOrientation((await VideoFile.Properties.GetVideoPropertiesAsync()).Orientation);
		}
	}

	private void ManageVideoOrientation(VideoOrientation orientation)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0003: Invalid comparison between Unknown and I4
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Invalid comparison between Unknown and I4
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_0118: Unknown result type (might be due to invalid IL or missing references)
		//IL_0128: Unknown result type (might be due to invalid IL or missing references)
		//IL_012d: Unknown result type (might be due to invalid IL or missing references)
		//IL_013c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0150: Expected O, but got Unknown
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0022: Invalid comparison between Unknown and I4
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_000e: Invalid comparison between Unknown and I4
		//IL_0195: Unknown result type (might be due to invalid IL or missing references)
		//IL_01a5: Unknown result type (might be due to invalid IL or missing references)
		//IL_01aa: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b9: Unknown result type (might be due to invalid IL or missing references)
		//IL_01cf: Expected O, but got Unknown
		//IL_006c: Unknown result type (might be due to invalid IL or missing references)
		//IL_007c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0081: Unknown result type (might be due to invalid IL or missing references)
		//IL_0090: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a6: Expected O, but got Unknown
		if ((int)orientation <= 90)
		{
			if ((int)orientation != 0)
			{
				if ((int)orientation == 90)
				{
					((FrameworkElement)MediaPanel).Height = ((FrameworkElement)ContentPanel).ActualWidth;
					((FrameworkElement)MediaPanel).Width = ((FrameworkElement)ContentPanel).ActualHeight;
					((UIElement)MediaPanel).RenderTransformOrigin = new Point(0.0, 0.0);
					((UIElement)MediaPanel).RenderTransform = (Transform)new CompositeTransform
					{
						Rotation = 90.0,
						TranslateX = ((FrameworkElement)ContentPanel).ActualWidth
					};
				}
			}
			else
			{
				((FrameworkElement)MediaPanel).Height = ((FrameworkElement)ContentPanel).ActualHeight;
				((FrameworkElement)MediaPanel).Width = ((FrameworkElement)ContentPanel).ActualWidth;
			}
		}
		else if ((int)orientation != 180)
		{
			if ((int)orientation == 270)
			{
				((FrameworkElement)MediaPanel).Height = ((FrameworkElement)ContentPanel).ActualWidth;
				((FrameworkElement)MediaPanel).Width = ((FrameworkElement)ContentPanel).ActualHeight;
				((UIElement)MediaPanel).RenderTransformOrigin = new Point(0.0, 0.0);
				((UIElement)MediaPanel).RenderTransform = (Transform)new CompositeTransform
				{
					Rotation = -90.0,
					TranslateY = ((FrameworkElement)ContentPanel).ActualHeight
				};
			}
		}
		else
		{
			((FrameworkElement)MediaPanel).Height = ((FrameworkElement)ContentPanel).ActualHeight;
			((FrameworkElement)MediaPanel).Width = ((FrameworkElement)ContentPanel).ActualWidth;
			((UIElement)MediaPanel).RenderTransformOrigin = new Point(0.5, 0.5);
			((UIElement)MediaPanel).RenderTransform = (Transform)new ScaleTransform
			{
				ScaleX = -1.0,
				ScaleY = -1.0
			};
		}
	}

	private async void AutoFit_Click(object sender, EventArgs e)
	{
		if (VideoFile != null)
		{
			_isAutoFit = !_isAutoFit;
			if (_isAutoFit)
			{
				CropControl cropControl = CropControl;
				double width = (((FrameworkElement)CropControl).Height = 440.0);
				((FrameworkElement)cropControl).Width = width;
				Rectangle autoFitBorder = AutoFitBorder;
				StackPanel backgroundChooserPanel = BackgroundChooserPanel;
				Visibility visibility = (Visibility)0;
				((UIElement)backgroundChooserPanel).Visibility = (Visibility)0;
				((UIElement)autoFitBorder).Visibility = visibility;
			}
			else
			{
				CropControl cropControl2 = CropControl;
				double width = (((FrameworkElement)CropControl).Height = double.NaN);
				((FrameworkElement)cropControl2).Width = width;
				Rectangle autoFitBorder2 = AutoFitBorder;
				StackPanel backgroundChooserPanel2 = BackgroundChooserPanel;
				Visibility visibility = (Visibility)1;
				((UIElement)backgroundChooserPanel2).Visibility = (Visibility)1;
				((UIElement)autoFitBorder2).Visibility = visibility;
			}
			ManageCropAppBar();
			TimerHelper.ToTime(TimeSpan.FromMilliseconds(100.0), async delegate
			{
				CropControl.SetAutoFit(_isAutoFit);
				ManageVideoOrientation((await VideoFile.Properties.GetVideoPropertiesAsync()).Orientation);
			});
		}
	}

	private void FitWhite_Click(object sender, RoutedEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Expected O, but got Unknown
		((Shape)AutoFitBorder).Fill = (Brush)new SolidColorBrush(Colors.White);
	}

	private void FitBlack_Click(object sender, RoutedEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Expected O, but got Unknown
		((Shape)AutoFitBorder).Fill = (Brush)new SolidColorBrush(Colors.Black);
	}

	private void ManageCropAppBar()
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Expected O, but got Unknown
		ApplicationBarIconButton val = (ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[0];
		if (_isAutoFit)
		{
			val.IconUri = new Uri("/Assets/AppBar/cropoutIn.png", UriKind.Relative);
		}
		else
		{
			val.IconUri = new Uri("/Assets/AppBar/cropout.png", UriKind.Relative);
		}
	}

	private void AddAds()
	{
		if (AppVersion.IsHaveAds())
		{
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)EncodingPanelAd).Children).Clear();
			UIElementCollection children = ((Panel)EncodingPanelAd).Children;
			AdRotator adRotator = new AdRotator();
			((FrameworkElement)adRotator).Width = 480.0;
			((FrameworkElement)adRotator).Height = 80.0;
			((PresentationFrameworkCollection<UIElement>)(object)children).Add((UIElement)(object)adRotator);
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
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		//IL_00f3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fd: Expected O, but got Unknown
		//IL_0109: Unknown result type (might be due to invalid IL or missing references)
		//IL_0113: Expected O, but got Unknown
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0129: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/CropVideo/CropVideoPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			AutoFitBorder = (Rectangle)((FrameworkElement)this).FindName("AutoFitBorder");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			MediaPanel = (Grid)((FrameworkElement)this).FindName("MediaPanel");
			CropControl = (CropControl)((FrameworkElement)this).FindName("CropControl");
			Media = (MediaElement)((FrameworkElement)this).FindName("Media");
			BackgroundChooserPanel = (StackPanel)((FrameworkElement)this).FindName("BackgroundChooserPanel");
			TimespanPanel = (Grid)((FrameworkElement)this).FindName("TimespanPanel");
			CropTimeSlider = (RangeSlider)((FrameworkElement)this).FindName("CropTimeSlider");
			EncodingPanel = (Grid)((FrameworkElement)this).FindName("EncodingPanel");
			EncodingProgress = (ProgressBar)((FrameworkElement)this).FindName("EncodingProgress");
			EncodingPanelAd = (Grid)((FrameworkElement)this).FindName("EncodingPanelAd");
		}
	}

	[CompilerGenerated]
	private void _003C_003En__FabricatedMethod0(NavigationEventArgs e)
	{
		((Page)this).OnNavigatedTo(e);
	}
}
