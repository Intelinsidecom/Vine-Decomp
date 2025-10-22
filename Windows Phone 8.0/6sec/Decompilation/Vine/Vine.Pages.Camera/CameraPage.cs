using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;
using Huyn.Ads;
using Lumia.Imaging;
using Lumia.Imaging.Transforms;
using Lumia.InteropServices.WindowsRuntime;
using Microsoft.Devices;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Reactive;
using Microsoft.Phone.Shell;
using Microsoft.Xna.Framework.GamerServices;
using VideoEffects;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;
using Vine.Utils;
using Windows.Foundation;
using Windows.Media.Editing;
using Windows.Media.MediaProperties;
using Windows.Media.Transcoding;
using Windows.Phone.Media.Capture;
using Windows.Storage;
using Windows.Storage.FileProperties;
using Windows.Storage.Streams;

namespace Vine.Pages.Camera;

public class CameraPage : PhoneApplicationPage
{
	internal class VideoPartInfo
	{
		public double Duration { get; internal set; }

		public string Filename { get; internal set; }
	}

	private AudioVideoCaptureDevice _CaptureDevice;

	private IRandomAccessStream _videostream;

	private StorageFolder _dataFolder;

	public const string FilePartFolder = "mpegpart";

	public const string FinalFileName = "finalRender.mp4";

	public const string FinalFileNameImage = "finalRender.jpg";

	private FilePickerHelper _filePickerHelper;

	private bool _isLandscape;

	private bool UseFrontCamera;

	private bool _isInitCapteur;

	private StorageFolder _applicationFolder;

	private bool test;

	private double _totalDuration;

	private DateTime startDate;

	private bool inProgress;

	private DispatcherTimer _timer = new DispatcherTimer
	{
		Interval = TimeSpan.FromMilliseconds(50.0)
	};

	private bool _paused;

	private bool _AudioVideoCaptureDeviceReady;

	private bool NoEnter;

	private VideoBrush VideoRecorderBrush;

	private StorageFile _outputFile;

	private bool _istutorial;

	private bool _flashMode;

	private bool _focusActivated;

	private bool _gridActivated;

	private ApplicationBarMenuItem _menuLastPart;

	private bool _forceddirect;

	private List<VideoPartInfo> _currentJobVideoPart = new List<VideoPartInfo>();

	private bool _alreadyaskedtoresume;

	private int[] _bufferPreview;

	private Storyboard _currentStoryboardProgress;

	internal PhoneApplicationPage phoneApplicationPage;

	internal Storyboard StoryboardStart;

	internal Storyboard StoryboardStartHidePanel;

	internal Storyboard StoryboardPressAction;

	internal Storyboard StoryboardFocusAnim;

	internal Grid MainLayout;

	internal VisualStateGroup VisualStateGroup;

	internal VisualState Portrait;

	internal VisualState Landscape;

	internal Grid LayoutRoot;

	internal Grid PreviewPanel;

	internal Rectangle viewfinderRectangle;

	internal Path FocusZone;

	internal Path GridViewport;

	internal Rectangle ProgressRect;

	internal Grid DonePanel;

	internal Button CloseButton;

	internal Path ClosePath;

	internal Button NextButton;

	internal Path ClosePath1;

	internal Border HintPanel;

	internal TextBlock HintInfoTitle;

	internal TextBlock HintInfo;

	internal Grid Hand;

	internal Ellipse ellipse;

	internal Ellipse ellipse1;

	internal Grid TutoWelcomePanel;

	internal Border border;

	internal Border border1;

	internal Grid EncodingPanel;

	internal ProgressBar EncodingProgress;

	internal Grid EncodingPanelAd;

	private bool _contentLoaded;

	public CameraPage()
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Expected O, but got Unknown
		//IL_00aa: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cc: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d6: Expected O, but got Unknown
		//IL_0108: Unknown result type (might be due to invalid IL or missing references)
		//IL_0131: Unknown result type (might be due to invalid IL or missing references)
		//IL_013b: Expected O, but got Unknown
		//IL_0077: Unknown result type (might be due to invalid IL or missing references)
		//IL_0081: Expected O, but got Unknown
		_filePickerHelper = new FilePickerHelper();
		_applicationFolder = ApplicationData.Current.LocalFolder;
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)MainLayout).Background = (Brush)Application.Current.Resources[(object)"DarkStripeBrush"];
		}
		ManageTorchAppBar();
		ManageGridAppBar();
		ManageFocusAppBar();
		ManageAppBar();
		((ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[0]).IsEnabled = Camera.IsCameraTypeSupported((CameraType)1);
		_menuLastPart = (ApplicationBarMenuItem)((PhoneApplicationPage)this).ApplicationBar.MenuItems[0];
		_menuLastPart.Text = AppResources.RemoveLastPart;
		CameraButtons.ShutterKeyHalfPressed += CameraButtons_ShutterKeyHalfPressed;
		((ApplicationBarMenuItem)((PhoneApplicationPage)this).ApplicationBar.MenuItems[1]).Text = AppResources.SelectAFile;
		((PhoneApplicationPage)this).OrientationChanged += CameraPage_OrientationChanged;
		((FrameworkElement)this).Loaded += new RoutedEventHandler(CameraPage_Loaded);
	}

	private void CameraPage_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_0012: Expected O, but got Unknown
		((FrameworkElement)this).Loaded -= new RoutedEventHandler(CameraPage_Loaded);
		if (!_istutorial)
		{
			return;
		}
		StoryboardStart.Begin();
		((Timeline)StoryboardStart).Completed += delegate
		{
			//IL_000d: Unknown result type (might be due to invalid IL or missing references)
			//IL_0017: Expected O, but got Unknown
			((UIElement)TutoWelcomePanel).MouseLeftButtonDown += (MouseButtonEventHandler)delegate
			{
				((UIElement)TutoWelcomePanel).IsHitTestVisible = false;
				StoryboardStartHidePanel.Begin();
			};
		};
	}

	private void CameraPage_OrientationChanged(object sender, OrientationChangedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0009: Invalid comparison between Unknown and I4
		//IL_0040: Unknown result type (might be due to invalid IL or missing references)
		//IL_0047: Invalid comparison between Unknown and I4
		//IL_0023: Unknown result type (might be due to invalid IL or missing references)
		//IL_006b: Unknown result type (might be due to invalid IL or missing references)
		//IL_004a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0051: Invalid comparison between Unknown and I4
		if ((e.Orientation & 1) == 1)
		{
			VisualStateManager.GoToState((Control)(object)this, "Portrait", false);
			((TranslateTransform)((UIElement)ProgressRect).RenderTransform).Y = 0.0;
			_isLandscape = false;
		}
		else if ((int)e.Orientation == 34 || (int)e.Orientation == 18)
		{
			VisualStateManager.GoToState((Control)(object)this, "Landscape", false);
			((TranslateTransform)((UIElement)ProgressRect).RenderTransform).X = 0.0;
			_isLandscape = true;
		}
		SetProgress(TimeSpan.FromSeconds(_totalDuration));
		ManageVideoBrush((CameraSensorLocation)(UseFrontCamera ? 1 : 0));
	}

	private async void CameraButtons_ShutterKeyHalfPressed(object sender, EventArgs e)
	{
		_ = 1;
		try
		{
			if (_CaptureDevice != null && (int)_CaptureDevice.SensorLocation == 0)
			{
				((UIElement)FocusZone).RenderTransform = (Transform)new TranslateTransform
				{
					X = 240.0,
					Y = 240.0
				};
				StoryboardFocusAnim.Begin();
				if ((((PhoneApplicationPage)this).Orientation & 1) == 1)
				{
					_CaptureDevice.put_FocusRegion((Rect?)new Rect(215.0, 295.0, 50.0, 50.0));
				}
				else
				{
					_CaptureDevice.put_FocusRegion((Rect?)new Rect(295.0, 215.0, 50.0, 50.0));
				}
				await _CaptureDevice.FocusAsync();
			}
		}
		catch
		{
		}
	}

	private void ManageAppBar()
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Expected O, but got Unknown
		ApplicationBarIconButton val = (ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[0];
		if (!UseFrontCamera)
		{
			val.IconUri = new Uri("/Assets/AppBar/BackCamera.png", UriKind.Relative);
			val.Text = AppResources.BackCamera;
		}
		else
		{
			val.IconUri = new Uri("/Assets/AppBar/FrontCamera.png", UriKind.Relative);
			val.Text = AppResources.FrontCamera;
		}
	}

	protected override async void OnNavigatedTo(NavigationEventArgs e)
	{
		_003C_003En__FabricatedMethod0(e);
		if ((int)e.NavigationMode == 1 && await _filePickerHelper.ManageOnNavigationAsync(_forceddirect))
		{
			return;
		}
		Init();
		if ((int)e.NavigationMode == 0)
		{
			string value = null;
			if (((Page)this).NavigationContext.QueryString.TryGetValue("forceddirect", out value))
			{
				_forceddirect = value == "true";
			}
			if (((Page)this).NavigationContext.QueryString.ContainsKey("tutorial"))
			{
				_menuLastPart.IsEnabled = false;
				_istutorial = true;
				((UIElement)TutoWelcomePanel).Visibility = (Visibility)0;
				((PhoneApplicationPage)this).SupportedOrientations = (SupportedPageOrientation)1;
				_timer.Tick += _timerTuto_Tick;
			}
			else
			{
				_timer.Tick += _timer_Tick;
			}
			if (!((Page)this).NavigationService.CanGoBack)
			{
				ApplicationBarMenuItem val = new ApplicationBarMenuItem();
				val.Text = AppResources.Home;
				val.Click += GoHome_Click;
				((PhoneApplicationPage)this).ApplicationBar.MenuItems.Insert(0, val);
			}
		}
	}

	private void GoHome_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToTimeline(null, removebackentry: true);
	}

	protected override async void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		_003C_003En__FabricatedMethod1(e);
		await CloseCapture();
	}

	private async Task CloseCapture()
	{
		if (_CaptureDevice != null)
		{
			((Shape)viewfinderRectangle).Fill = null;
			_CaptureDevice.Dispose();
			_CaptureDevice = null;
		}
	}

	private async Task Init()
	{
		if (!_isInitCapteur)
		{
			_isInitCapteur = true;
			try
			{
				CameraPage cameraPage = this;
				_ = cameraPage._dataFolder;
				cameraPage._dataFolder = await ApplicationData.Current.LocalFolder.CreateFolderAsync("mpegpart", (CreationCollisionOption)3);
			}
			catch
			{
				_dataFolder = null;
			}
			foreach (StorageFile file in (await _dataFolder.GetFilesAsync()).OrderBy((StorageFile f) => f.Name))
			{
				VideoProperties val = await file.Properties.GetVideoPropertiesAsync();
				_currentJobVideoPart.Add(new VideoPartInfo
				{
					Filename = file.Name.Split(new char[1] { '.' }, 2)[0],
					Duration = val.Duration.TotalSeconds
				});
			}
		}
		CreateCamera((CameraSensorLocation)(UseFrontCamera ? 1 : 0));
	}

	private async Task CreateCamera(CameraSensorLocation location)
	{
		//IL_000a: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		_AudioVideoCaptureDeviceReady = false;
		if (_CaptureDevice != null)
		{
			_CaptureDevice.Dispose();
		}
		if (!_alreadyaskedtoresume)
		{
			_alreadyaskedtoresume = true;
			if (_currentJobVideoPart.Count <= 0)
			{
				await Reinit();
			}
			else if (_istutorial)
			{
				Reinit();
			}
			else
			{
				Guide.BeginShowMessageBox(AppResources.ContinueCapture, AppResources.ContinueCaptureDescription, new string[2]
				{
					AppResources.Yes,
					AppResources.NoRemoveIt
				}, 0, MessageBoxIcon.Alert, delegate(IAsyncResult res)
				{
					int? choice = Guide.EndShowMessageBox(res);
					((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
					{
						//IL_000a: Unknown result type (might be due to invalid IL or missing references)
						//IL_000b: Unknown result type (might be due to invalid IL or missing references)
						//IL_0014: Unknown result type (might be due to invalid IL or missing references)
						SupportedPageOrientation val = (SupportedPageOrientation)DatasProvider.Instance.CaptureOrientation;
						if ((int)val != 0)
						{
							((PhoneApplicationPage)this).SupportedOrientations = val;
						}
						Reinit(!choice.HasValue || choice.Value == 1);
					});
				}, null);
			}
		}
		RemoveButtonDetector();
		CameraPage cameraPage = this;
		_ = cameraPage._CaptureDevice;
		cameraPage._CaptureDevice = await AudioVideoCaptureDevice.OpenAsync(location, AudioVideoCaptureDevice.GetAvailableCaptureResolutions(location).First());
		IReadOnlyList<object> supportedPropertyValues = AudioVideoCaptureDevice.GetSupportedPropertyValues(_CaptureDevice.SensorLocation, KnownCameraAudioVideoProperties.VideoTorchMode);
		bool isEnabled = false;
		foreach (uint item in supportedPropertyValues)
		{
			if (item == 2)
			{
				isEnabled = true;
				break;
			}
		}
		((ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[1]).IsEnabled = isEnabled;
		((ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[2]).IsEnabled = (int)location == 0;
		if ((int)location == 0)
		{
			try
			{
				_CaptureDevice.SetProperty(KnownCameraAudioVideoProperties.VideoTorchMode, (object)(VideoTorchMode)(_flashMode ? 2 : 0));
			}
			catch
			{
			}
		}
		else
		{
			_flashMode = false;
			_focusActivated = false;
			ManageTorchAppBar();
			ManageFocusAppBar();
		}
		Size captureResolutionAsync = (from d in AudioVideoCaptureDevice.GetAvailableCaptureResolutions(location)
			orderby d.Width
			select d).First();
		await _CaptureDevice.SetCaptureResolutionAsync(captureResolutionAsync);
		VideoRecorderBrush = new VideoBrush
		{
			AlignmentX = (AlignmentX)1,
			AlignmentY = (AlignmentY)1,
			Stretch = (Stretch)3
		};
		((Shape)viewfinderRectangle).Fill = (Brush)(object)VideoRecorderBrush;
		ManageVideoBrush(location);
		CameraVideoBrushExtensions.SetSource(VideoRecorderBrush, (object)_CaptureDevice);
		_AudioVideoCaptureDeviceReady = true;
		AddButtonDetector();
	}

	private void ManageVideoBrush(CameraSensorLocation location)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		//IL_0024: Expected O, but got Unknown
		//IL_0031: Unknown result type (might be due to invalid IL or missing references)
		//IL_0036: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		//IL_003a: Invalid comparison between Unknown and I4
		//IL_005c: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Unknown result type (might be due to invalid IL or missing references)
		//IL_003f: Invalid comparison between Unknown and I4
		CompositeTransform val = new CompositeTransform
		{
			CenterX = 0.5,
			CenterY = 0.5
		};
		try
		{
			uint num = _CaptureDevice.SensorRotationInDegrees;
			PageOrientation orientation = ((PhoneApplicationPage)this).Orientation;
			if ((int)orientation != 18)
			{
				if ((int)orientation == 34)
				{
					num += 90;
				}
			}
			else
			{
				num -= 90;
			}
			val.Rotation = num;
		}
		catch
		{
		}
		val.ScaleX = (((int)location == 0) ? 1 : (-1));
		if (VideoRecorderBrush != null)
		{
			((Brush)VideoRecorderBrush).RelativeTransform = (Transform)(object)val;
		}
	}

	private async Task OpenFilePart()
	{
		CameraPage cameraPage = this;
		_ = cameraPage._outputFile;
		cameraPage._outputFile = await _dataFolder.CreateFileAsync(DateTime.Now.ToString("yyyyMMddhhmmssfff") + ".mp4", (CreationCollisionOption)1);
		cameraPage = this;
		_ = cameraPage._videostream;
		cameraPage._videostream = await _outputFile.OpenAsync((FileAccessMode)1);
	}

	private void ApplicationBarSwitchIconButton_Click(object sender, EventArgs e)
	{
		//IL_000c: Unknown result type (might be due to invalid IL or missing references)
		//IL_001d: Unknown result type (might be due to invalid IL or missing references)
		CameraSensorLocation location = (CameraSensorLocation)(!UseFrontCamera);
		UseFrontCamera = !UseFrontCamera;
		CreateCamera(location);
		ManageAppBar();
	}

	private async void Close_Click(object sender, RoutedEventArgs e)
	{
		RemoveButtonDetector();
		if (_currentJobVideoPart.Count == 0 || (int)MessageBox.Show(AppResources.ToastDeleteVideoMessage, AppResources.ToastDeleteVideoTitle, (MessageBoxButton)1) == 1)
		{
			await Reinit();
		}
		AddButtonDetector();
	}

	private async Task Reinit(bool clearfile = true)
	{
		try
		{
			await CloseStream();
		}
		catch
		{
		}
		HideDone();
		if (_istutorial)
		{
			Border hintPanel = HintPanel;
			Grid hand = Hand;
			Visibility visibility = (Visibility)0;
			((UIElement)hand).Visibility = (Visibility)0;
			((UIElement)hintPanel).Visibility = visibility;
			((UIElement)HintInfoTitle).Visibility = (Visibility)1;
			HintInfo.Text = AppResources.TutoCameraPressToCapture;
		}
		_totalDuration = (clearfile ? 0.0 : _currentJobVideoPart.Select((VideoPartInfo s) => s.Duration).Sum());
		SetProgress(TimeSpan.FromSeconds(_totalDuration));
		if (_istutorial)
		{
			((PhoneApplicationPage)this).SupportedOrientations = (SupportedPageOrientation)1;
			((Control)NextButton).IsEnabled = !clearfile && _totalDuration >= 6.0;
		}
		else
		{
			_menuLastPart.IsEnabled = _currentJobVideoPart.Count > 0;
			((PhoneApplicationPage)this).SupportedOrientations = (SupportedPageOrientation)3;
			((Control)NextButton).IsEnabled = !clearfile && _totalDuration >= 2.0;
		}
		if (clearfile)
		{
			_currentJobVideoPart.Clear();
			await AppUtils.ClearVideoFiles();
		}
	}

	private void SetProgress(TimeSpan offset)
	{
		//IL_004d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0013: Unknown result type (might be due to invalid IL or missing references)
		if (_isLandscape)
		{
			((TranslateTransform)((UIElement)ProgressRect).RenderTransform).Y = 480.0 + _totalDuration / 6.0 * -480.0;
		}
		else
		{
			((TranslateTransform)((UIElement)ProgressRect).RenderTransform).X = -480.0 + _totalDuration / 6.0 * 480.0;
		}
	}

	private void HideDone()
	{
		((UIElement)DonePanel).Visibility = (Visibility)1;
	}

	private async void PreviewPanel_MouseEnter(object sender, ManipulationStartedEventArgs e)
	{
		if (_focusActivated)
		{
			try
			{
				if (_CaptureDevice != null)
				{
					Point manipulationOrigin = e.ManipulationOrigin;
					PageOrientation orientation = ((PhoneApplicationPage)this).Orientation;
					if ((int)orientation != 18)
					{
						if ((int)orientation == 34)
						{
							_CaptureDevice.put_FocusRegion((Rect?)new Rect(new Point(560.0 - Math.Min(480.0, Math.Max(0.0, ((Point)(ref manipulationOrigin)).X - 15.0)), Math.Min(480.0, Math.Max(0.0, ((Point)(ref manipulationOrigin)).Y - 15.0))), new Size(30.0, 30.0)));
						}
						else
						{
							_CaptureDevice.put_FocusRegion((Rect?)new Rect(new Point(Math.Min(480.0, Math.Max(0.0, ((Point)(ref manipulationOrigin)).Y - 15.0)), 560.0 - Math.Min(480.0, Math.Max(0.0, ((Point)(ref manipulationOrigin)).X - 15.0))), new Size(30.0, 30.0)));
						}
					}
					else
					{
						_CaptureDevice.put_FocusRegion((Rect?)new Rect(new Point(80.0 + Math.Min(480.0, Math.Max(0.0, ((Point)(ref manipulationOrigin)).X - 15.0)), Math.Min(480.0, Math.Max(0.0, ((Point)(ref manipulationOrigin)).Y - 15.0))), new Size(30.0, 30.0)));
					}
					((UIElement)FocusZone).RenderTransform = (Transform)new TranslateTransform
					{
						X = ((Point)(ref manipulationOrigin)).X,
						Y = ((Point)(ref manipulationOrigin)).Y
					};
					_CaptureDevice.FocusAsync();
					StoryboardFocusAnim.Begin();
					if ((int)_CaptureDevice.SensorLocation == 0 && _flashMode)
					{
						_CaptureDevice.SetProperty(KnownCameraAudioVideoProperties.VideoTorchMode, (object)(VideoTorchMode)0);
						_CaptureDevice.SetProperty(KnownCameraAudioVideoProperties.VideoTorchMode, (object)(VideoTorchMode)2);
					}
				}
				return;
			}
			catch
			{
				return;
			}
		}
		await PressAction();
	}

	private async Task PressAction()
	{
		if (NoEnter)
		{
			return;
		}
		if (_istutorial)
		{
			StoryboardPressAction.Begin();
			((UIElement)HintInfoTitle).Visibility = (Visibility)1;
			HintInfo.Text = AppResources.TutoCameraContinueToPress;
		}
		NoEnter = true;
		RemoveButtonDetector();
		IObservable<IEvent<ManipulationCompletedEventArgs>> observable = Observable.FromEvent<ManipulationCompletedEventArgs>((object)PreviewPanel, "ManipulationCompleted");
		IObservable<IEvent<EventArgs>> observable2 = Observable.FromEvent<EventHandler, EventArgs>((Func<EventHandler<EventArgs>, EventHandler>)((EventHandler<EventArgs> x) => x.Invoke), (Action<EventHandler>)delegate(EventHandler h)
		{
			CameraButtons.ShutterKeyReleased += h;
		}, (Action<EventHandler>)delegate(EventHandler h)
		{
			CameraButtons.ShutterKeyReleased -= h;
		});
		Subject<int> observableTimer = new Subject<int>();
		IDisposable subscri = null;
		subscri = ObservableExtensions.Subscribe<IEvent<EventArgs>>(Observable.Zip<IEvent<EventArgs>, int, IEvent<EventArgs>>(Observable.Merge<IEvent<EventArgs>>((IObservable<IEvent<EventArgs>>)observable, observable2), Observable.AsObservable<int>((IObservable<int>)observableTimer), (Func<IEvent<EventArgs>, int, IEvent<EventArgs>>)((IEvent<EventArgs> a, int b) => a)), (Action<IEvent<EventArgs>>)delegate
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				subscri.Dispose();
				ReleaseAction();
			});
		});
		_paused = false;
		if (_paused || _CaptureDevice == null || !_AudioVideoCaptureDeviceReady)
		{
			subscri.Dispose();
			AddButtonDetector();
			return;
		}
		await OpenFilePart();
		if (_paused)
		{
			subscri.Dispose();
			AddButtonDetector();
			return;
		}
		if ((((PhoneApplicationPage)this).Orientation & 1) == 1)
		{
			((PhoneApplicationPage)this).SupportedOrientations = (SupportedPageOrientation)1;
		}
		else
		{
			((PhoneApplicationPage)this).SupportedOrientations = (SupportedPageOrientation)2;
		}
		DatasProvider.Instance.CaptureOrientation = (int)((PhoneApplicationPage)this).SupportedOrientations;
		try
		{
			long num = _CaptureDevice.SensorRotationInDegrees;
			PageOrientation orientation = ((PhoneApplicationPage)this).Orientation;
			if ((int)orientation != 18)
			{
				if ((int)orientation == 34)
				{
					num += 90;
				}
			}
			else
			{
				num -= 90;
			}
			num = (((int)_CaptureDevice.SensorLocation == 0) ? num : (-num));
			_CaptureDevice.SetProperty(KnownCameraGeneralProperties.EncodeWithOrientation, (object)num);
		}
		catch
		{
		}
		try
		{
			await _CaptureDevice.StartRecordingToStreamAsync(_videostream);
			if (_paused)
			{
				await CloseStream();
			}
		}
		catch (Exception)
		{
			subscri.Dispose();
			AddButtonDetector();
			return;
		}
		if (_paused)
		{
			subscri.Dispose();
			AddButtonDetector();
			return;
		}
		startDate = DateTime.Now;
		inProgress = true;
		_timer.Start();
		CreateAndStartStoryboard();
		if (_currentJobVideoPart.Count == 0)
		{
			AudioVideoCaptureDevice captureDevice = _CaptureDevice;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<ICameraCaptureDevice, object>, EventRegistrationToken>)captureDevice.add_PreviewFrameAvailable, (Action<EventRegistrationToken>)captureDevice.remove_PreviewFrameAvailable, (TypedEventHandler<ICameraCaptureDevice, object>)_CaptureDevice_PreviewFrameAvailable);
		}
		TimerHelper.ToTime(TimeSpan.FromMilliseconds(200.0), delegate
		{
			observableTimer.OnNext(0);
		});
	}

	private void CreateAndStartStoryboard()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0021: Unknown result type (might be due to invalid IL or missing references)
		//IL_0035: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		//IL_0042: Expected O, but got Unknown
		//IL_006d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0061: Unknown result type (might be due to invalid IL or missing references)
		//IL_0077: Expected O, but got Unknown
		Storyboard val = new Storyboard();
		TimeSpan timeSpan = TimeSpan.FromSeconds(6.0 - _totalDuration);
		DoubleAnimation val2 = new DoubleAnimation
		{
			To = 0.0,
			Duration = Duration.op_Implicit(timeSpan)
		};
		Storyboard.SetTarget((Timeline)(object)val2, (DependencyObject)(object)((UIElement)ProgressRect).RenderTransform);
		Storyboard.SetTargetProperty((Timeline)(object)val2, _isLandscape ? new PropertyPath((object)TranslateTransform.YProperty) : new PropertyPath((object)TranslateTransform.XProperty));
		((PresentationFrameworkCollection<Timeline>)(object)val.Children).Add((Timeline)(object)val2);
		val.Begin();
		_currentStoryboardProgress = val;
	}

	private void RemoveButtonDetector()
	{
		((UIElement)PreviewPanel).ManipulationStarted -= PreviewPanel_MouseEnter;
		CameraButtons.ShutterKeyPressed -= CameraButtons_ShutterKeyPressed;
	}

	private async void ReleaseAction()
	{
		await PauseVideo();
		if (_istutorial)
		{
			StoryboardPressAction.Stop();
		}
		AddButtonDetector();
		NoEnter = false;
	}

	private void AddButtonDetector()
	{
		((UIElement)PreviewPanel).ManipulationStarted += PreviewPanel_MouseEnter;
		CameraButtons.ShutterKeyPressed += CameraButtons_ShutterKeyPressed;
	}

	private void CameraButtons_ShutterKeyPressed(object sender, EventArgs e)
	{
		if (!_focusActivated)
		{
			PressAction();
		}
	}

	private async Task CloseStream()
	{
		if (_videostream != null)
		{
			await _CaptureDevice.StopRecordingAsync();
			await ((IOutputStream)_videostream).FlushAsync();
			_videostream.AsStream().Close();
			((IDisposable)_videostream).Dispose();
			_videostream = null;
		}
	}

	private async Task PauseVideo()
	{
		try
		{
			if (!_timer.IsEnabled)
			{
				return;
			}
			if (_currentStoryboardProgress != null)
			{
				_currentStoryboardProgress.Pause();
			}
			_timer.Stop();
			DateTime date = DateTime.Now;
			await CloseStream();
			BasicProperties info = await _outputFile.GetBasicPropertiesAsync();
			if (_istutorial)
			{
				double totalSeconds = (date - startDate).TotalSeconds;
				if (totalSeconds >= 1.5)
				{
					AddVideoPart(totalSeconds);
					((UIElement)HintInfoTitle).Visibility = (Visibility)0;
					HintInfoTitle.Text = AppResources.TutoCameraNice;
					HintInfo.Text = AppResources.TutoCameraPressToCapture;
				}
				else
				{
					await _outputFile.DeleteAsync();
					((UIElement)HintInfoTitle).Visibility = (Visibility)0;
					HintInfoTitle.Text = AppResources.TutoCameraTooSmall;
					HintInfo.Text = AppResources.TutoCameraPressToCapture;
				}
			}
			else if (info.Size <= 5000)
			{
				await _outputFile.DeleteAsync();
			}
			else
			{
				AddVideoPart((date - startDate).TotalSeconds);
			}
			if (_bufferPreview != null)
			{
				CreatePreview((int)_CaptureDevice.SensorLocation == 0, (int)_CaptureDevice.PreviewResolution.Width, (int)_CaptureDevice.PreviewResolution.Height, _bufferPreview);
				_bufferPreview = null;
			}
			inProgress = false;
			SetProgress(TimeSpan.FromSeconds(_totalDuration));
		}
		catch
		{
		}
	}

	private void AddVideoPart(double diff)
	{
		_totalDuration += diff;
		_menuLastPart.IsEnabled = true;
		_currentJobVideoPart.Add(new VideoPartInfo
		{
			Duration = diff,
			Filename = _outputFile.Name
		});
	}

	private async void _timer_Tick(object sender, EventArgs e)
	{
		if (!inProgress)
		{
			return;
		}
		double num = _totalDuration + (DateTime.Now - startDate).TotalSeconds;
		if (num >= 2.0)
		{
			((Control)NextButton).IsEnabled = true;
			if (num >= 6.0)
			{
				await PauseVideo();
				ShowDone();
			}
		}
	}

	private void ShowDone()
	{
		//IL_0022: Unknown result type (might be due to invalid IL or missing references)
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		((UIElement)DonePanel).Visibility = (Visibility)0;
		if (_istutorial)
		{
			Border hintPanel = HintPanel;
			Grid hand = Hand;
			Visibility visibility = (Visibility)1;
			((UIElement)hand).Visibility = (Visibility)1;
			((UIElement)hintPanel).Visibility = visibility;
		}
	}

	private async void _timerTuto_Tick(object sender, EventArgs e)
	{
		if (!inProgress)
		{
			return;
		}
		double totalSeconds = (DateTime.Now - startDate).TotalSeconds;
		double num = _totalDuration + totalSeconds;
		if (totalSeconds >= 1.5)
		{
			if (_totalDuration == 0.0)
			{
				PauseVideo();
			}
			else
			{
				((UIElement)HintInfoTitle).Visibility = (Visibility)1;
				HintInfo.Text = AppResources.TutoCameraReleaseToStop;
			}
		}
		if (num >= 6.0)
		{
			((Control)NextButton).IsEnabled = true;
			await PauseVideo();
			ShowDone();
		}
	}

	private void StopCamera()
	{
		if (_CaptureDevice != null)
		{
			_CaptureDevice.Dispose();
			_CaptureDevice = null;
		}
	}

	private async void Next_Click(object sender, RoutedEventArgs e)
	{
		ShowProgress();
		try
		{
			IReadOnlyList<StorageFile> thefiles = await _dataFolder.GetFilesAsync();
			for (int i = 0; i < thefiles.Count; i++)
			{
				if ((await thefiles[i].GetBasicPropertiesAsync()).Size < 500)
				{
					await thefiles[i].DeleteAsync((StorageDeleteOption)0);
				}
			}
			if (_istutorial)
			{
				Vine.Datas.Datas instance = DatasProvider.Instance;
				instance.DisplayedTutorialCamera = true;
				instance.Save();
				_istutorial = false;
				((PhoneApplicationPage)this).SupportedOrientations = (SupportedPageOrientation)3;
				_timer.Tick -= _timerTuto_Tick;
				_timer.Tick += _timer_Tick;
				((UIElement)TutoWelcomePanel).Visibility = (Visibility)1;
			}
			StopCamera();
			string fileResult = await CropVideoAndJoin(_dataFolder);
			if (fileResult == null)
			{
				CreateCamera((CameraSensorLocation)(UseFrontCamera ? 1 : 0));
				return;
			}
			StorageFile val = await ApplicationData.Current.TemporaryFolder.GetFileAsync("finalRender.jpg");
			NavigationServiceExt.ToPreviewVideo(_forceddirect, val.Path, fileResult);
		}
		catch
		{
		}
		finally
		{
			HideProgress();
		}
	}

	private async Task<string> CropVideoAndJoin(StorageFolder folder)
	{
		try
		{
			StorageFile val = await ApplicationData.Current.TemporaryFolder.GetFileAsync("finalRender.mp4");
			if (val != null)
			{
				await val.DeleteAsync();
			}
		}
		catch
		{
		}
		List<StorageFile> list = (await folder.GetFilesAsync()).ToList();
		if (list == null || !list.Any())
		{
			return null;
		}
		try
		{
			MediaEncodingProfile encodingProfile = MediaEncodingHelper.GetEncoding();
			double deltaByFile = 1.0 / (double)list.Count;
			double currentProgress = 0.0;
			MediaComposition composition = new MediaComposition();
			StorageFile firstcroppedVideo = null;
			foreach (StorageFile video in list)
			{
				if (video.Name.EndsWith(".cropped.mp4"))
				{
					IList<MediaClip> clips = composition.Clips;
					clips.Add(await MediaClip.CreateFromFileAsync((IStorageFile)(object)video));
					if (firstcroppedVideo == null)
					{
						firstcroppedVideo = video;
					}
					continue;
				}
				string destName = Path.GetFileNameWithoutExtension(video.Name) + ".cropped.mp4";
				VideoProperties val2 = await video.Properties.GetVideoPropertiesAsync();
				uint num = ((val2.Height <= val2.Width) ? val2.Height : val2.Width);
				MediaTranscoder transcoder = new MediaTranscoder();
				if (num == 480)
				{
					CropEffectDefinition val3 = new CropEffectDefinition
					{
						InputWidth = val2.Width,
						InputHeight = val2.Height,
						OutputWidth = 480u,
						OutputHeight = 480u
					};
					transcoder.AddVideoEffect(val3.ActivatableClassId, true, val3.Properties);
				}
				else
				{
					Rect cropArea;
					if (val2.Height > val2.Width)
					{
						cropArea = new Rect(0.0, (val2.Height - val2.Width) / 2, val2.Width, val2.Width);
					}
					else
					{
						cropArea = new Rect((val2.Width - val2.Height) / 2, 0.0, val2.Height, val2.Height);
					}
					LumiaEffectDefinition val4 = new LumiaEffectDefinition((FilterChainFactory)(() => (IEnumerable<IFilter>)(object)new IFilter[1] { (IFilter)new CropFilter(cropArea) }))
					{
						InputWidth = val2.Width,
						InputHeight = val2.Height,
						OutputWidth = 480u,
						OutputHeight = 480u
					};
					transcoder.AddVideoEffect(val4.ActivatableClassId, true, val4.Properties);
				}
				StorageFile resultFile = await folder.CreateFileAsync(destName, (CreationCollisionOption)1);
				IRandomAccessStreamWithContentType sourceStream = await video.OpenReadAsync();
				try
				{
					IRandomAccessStream outputStream = await resultFile.OpenAsync((FileAccessMode)1);
					try
					{
						IAsyncActionWithProgress<double> obj2 = (await transcoder.PrepareStreamTranscodeAsync((IRandomAccessStream)(object)sourceStream, outputStream, encodingProfile)).TranscodeAsync();
						obj2.put_Progress((AsyncActionProgressHandler<double>)delegate(IAsyncActionWithProgress<double> e, double progress)
						{
							ShowProgress(currentProgress + progress * deltaByFile);
						});
						await obj2;
					}
					finally
					{
						((IDisposable)outputStream)?.Dispose();
					}
				}
				finally
				{
					((IDisposable)sourceStream)?.Dispose();
				}
				await video.DeleteAsync();
				currentProgress += deltaByFile * 100.0;
				if (firstcroppedVideo == null)
				{
					firstcroppedVideo = resultFile;
				}
				MediaClip item = await MediaClip.CreateFromFileAsync((IStorageFile)(object)resultFile);
				composition.Clips.Add(item);
			}
			if (composition.Clips.Count > 1)
			{
				StorageFile file = await ApplicationData.Current.TemporaryFolder.CreateFileAsync("finalRender.mp4", (CreationCollisionOption)1);
				await composition.RenderToFileAsync((IStorageFile)(object)file);
				return file.Path;
			}
			if (firstcroppedVideo != null)
			{
				return firstcroppedVideo.Path;
			}
		}
		catch (Exception)
		{
		}
		return null;
	}

	private void Torch_Click(object sender, EventArgs e)
	{
		try
		{
			_flashMode = !_flashMode;
			ManageTorchAppBar();
			_CaptureDevice.SetProperty(KnownCameraAudioVideoProperties.VideoTorchMode, (object)(VideoTorchMode)(_flashMode ? 2 : 0));
		}
		catch
		{
		}
	}

	private void ManageTorchAppBar()
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Expected O, but got Unknown
		ApplicationBarIconButton val = (ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[1];
		if (_flashMode)
		{
			val.Text = AppResources.TurnOffTorch;
			val.IconUri = new Uri("/Assets/AppBar/appbar.camera.flash.png", UriKind.Relative);
		}
		else
		{
			val.Text = AppResources.TurnOnTorch;
			val.IconUri = new Uri("/Assets/AppBar/appbar.camera.flash.off.png", UriKind.Relative);
		}
	}

	private void ManageFocusAppBar()
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Expected O, but got Unknown
		ApplicationBarIconButton val = (ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[2];
		if (_focusActivated)
		{
			val.Text = AppResources.ChangeFocus;
			val.IconUri = new Uri("/Assets/AppBar/focusOn.png", UriKind.Relative);
		}
		else
		{
			val.Text = AppResources.SetFocus;
			val.IconUri = new Uri("/Assets/AppBar/focus.png", UriKind.Relative);
		}
	}

	private void ManageGridAppBar()
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Expected O, but got Unknown
		ApplicationBarIconButton val = (ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[3];
		if (_gridActivated)
		{
			val.Text = AppResources.HideGrid;
			val.IconUri = new Uri("/Assets/AppBar/gridOn.png", UriKind.Relative);
			((UIElement)GridViewport).Visibility = (Visibility)0;
		}
		else
		{
			val.Text = AppResources.ShowGrid;
			val.IconUri = new Uri("/Assets/AppBar/grid.png", UriKind.Relative);
			((UIElement)GridViewport).Visibility = (Visibility)1;
		}
	}

	private async void RemoveLastPart_Click(object sender, EventArgs e)
	{
		if (_currentJobVideoPart.Count == 0)
		{
			return;
		}
		VideoPartInfo item = _currentJobVideoPart.Last();
		try
		{
			StorageFile val = await _dataFolder.GetFileAsync(item.Filename);
			if (val != null)
			{
				await val.DeleteAsync();
			}
		}
		catch
		{
		}
		_currentJobVideoPart.Remove(item);
		((ApplicationBarMenuItem)sender).IsEnabled = _currentJobVideoPart.Count > 0;
		_totalDuration -= item.Duration;
		SetProgress(TimeSpan.FromSeconds(_totalDuration));
		HideDone();
		((Control)NextButton).IsEnabled = _totalDuration >= 2.0;
	}

	private void AppBarFocus_Click(object sender, EventArgs e)
	{
		_focusActivated = !_focusActivated;
		ManageFocusAppBar();
	}

	private void AppBarGrid_Click(object sender, EventArgs e)
	{
		_gridActivated = !_gridActivated;
		ManageGridAppBar();
	}

	private void AppBarSelectFile_Click(object sender, EventArgs e)
	{
		_filePickerHelper.Launch();
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

	private void _CaptureDevice_PreviewFrameAvailable(ICameraCaptureDevice sender, object args)
	{
		WindowsRuntimeMarshal.RemoveEventHandler<TypedEventHandler<ICameraCaptureDevice, object>>((Action<EventRegistrationToken>)_CaptureDevice.remove_PreviewFrameAvailable, (TypedEventHandler<ICameraCaptureDevice, object>)_CaptureDevice_PreviewFrameAvailable);
		int[] array = new int[(int)(_CaptureDevice.PreviewResolution.Height * _CaptureDevice.PreviewResolution.Width)];
		_CaptureDevice.GetPreviewBufferArgb(array);
		_bufferPreview = array;
	}

	private async void CreatePreview(bool isback, int width, int height, int[] preview)
	{
		WriteableBitmap val = new WriteableBitmap(width, height);
		for (int i = 0; i < preview.Length; i++)
		{
			val.Pixels[i] = preview[i];
		}
		BitmapImageSource source = new BitmapImageSource(val.AsBitmap());
		try
		{
			ImageProviderInfo val2 = await source.GetInfoAsync();
			double num;
			double num2;
			double num3;
			if (val2.ImageSize.Height > val2.ImageSize.Width)
			{
				num = val2.ImageSize.Width;
				num2 = 0.0;
				num3 = (val2.ImageSize.Height - val2.ImageSize.Width) / 2.0;
			}
			else
			{
				num = val2.ImageSize.Height;
				num3 = 0.0;
				num2 = (val2.ImageSize.Width - val2.ImageSize.Height) / 2.0;
			}
			FilterEffect filters = new FilterEffect((IImageProvider)(object)source);
			try
			{
				IFilter[] array = new IFilter[2];
				double x = num2;
				double y = num3;
				double num4 = num;
				array[0] = (IFilter)new CropFilter(new Rect(x, y, num4, num4));
				array[1] = (IFilter)new RotationFilter(isback ? 90.0 : 270.0);
				filters.Filters = (IEnumerable<IFilter>)(object)array;
				JpegRenderer render = new JpegRenderer((IImageProvider)(object)filters);
				try
				{
					render.Size = new Size(480.0, 480.0);
					Stream stream = (await render.RenderAsync()).AsStream();
					using Stream destination = await ((IStorageFile)(object)(await ApplicationData.Current.TemporaryFolder.CreateFileAsync("finalRender.jpg", (CreationCollisionOption)1))).OpenStreamForWriteAsync();
					stream.CopyTo(destination);
					stream.Flush();
				}
				finally
				{
					((IDisposable)render)?.Dispose();
				}
			}
			finally
			{
				((IDisposable)filters)?.Dispose();
			}
		}
		finally
		{
			((IDisposable)source)?.Dispose();
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Camera/CameraPage.xaml", UriKind.Relative));
			phoneApplicationPage = (PhoneApplicationPage)((FrameworkElement)this).FindName("phoneApplicationPage");
			StoryboardStart = (Storyboard)((FrameworkElement)this).FindName("StoryboardStart");
			StoryboardStartHidePanel = (Storyboard)((FrameworkElement)this).FindName("StoryboardStartHidePanel");
			StoryboardPressAction = (Storyboard)((FrameworkElement)this).FindName("StoryboardPressAction");
			StoryboardFocusAnim = (Storyboard)((FrameworkElement)this).FindName("StoryboardFocusAnim");
			MainLayout = (Grid)((FrameworkElement)this).FindName("MainLayout");
			VisualStateGroup = (VisualStateGroup)((FrameworkElement)this).FindName("VisualStateGroup");
			Portrait = (VisualState)((FrameworkElement)this).FindName("Portrait");
			Landscape = (VisualState)((FrameworkElement)this).FindName("Landscape");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			PreviewPanel = (Grid)((FrameworkElement)this).FindName("PreviewPanel");
			viewfinderRectangle = (Rectangle)((FrameworkElement)this).FindName("viewfinderRectangle");
			FocusZone = (Path)((FrameworkElement)this).FindName("FocusZone");
			GridViewport = (Path)((FrameworkElement)this).FindName("GridViewport");
			ProgressRect = (Rectangle)((FrameworkElement)this).FindName("ProgressRect");
			DonePanel = (Grid)((FrameworkElement)this).FindName("DonePanel");
			CloseButton = (Button)((FrameworkElement)this).FindName("CloseButton");
			ClosePath = (Path)((FrameworkElement)this).FindName("ClosePath");
			NextButton = (Button)((FrameworkElement)this).FindName("NextButton");
			ClosePath1 = (Path)((FrameworkElement)this).FindName("ClosePath1");
			HintPanel = (Border)((FrameworkElement)this).FindName("HintPanel");
			HintInfoTitle = (TextBlock)((FrameworkElement)this).FindName("HintInfoTitle");
			HintInfo = (TextBlock)((FrameworkElement)this).FindName("HintInfo");
			Hand = (Grid)((FrameworkElement)this).FindName("Hand");
			ellipse = (Ellipse)((FrameworkElement)this).FindName("ellipse");
			ellipse1 = (Ellipse)((FrameworkElement)this).FindName("ellipse1");
			TutoWelcomePanel = (Grid)((FrameworkElement)this).FindName("TutoWelcomePanel");
			border = (Border)((FrameworkElement)this).FindName("border");
			border1 = (Border)((FrameworkElement)this).FindName("border1");
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

	[CompilerGenerated]
	private void _003C_003En__FabricatedMethod1(NavigatingCancelEventArgs e)
	{
		((Page)this).OnNavigatingFrom(e);
	}
}
