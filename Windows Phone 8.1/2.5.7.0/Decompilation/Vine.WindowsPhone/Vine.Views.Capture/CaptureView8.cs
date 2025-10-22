using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading;
using System.Threading.Tasks;
using CameraSampleCS.Models.Camera;
using GrayscaleTransform_Windows;
using VideoEdit;
using Vine.Camera;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Tiles;
using Windows.Devices.Enumeration;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.Media.Capture;
using Windows.Media.Core;
using Windows.Media.Devices;
using Windows.Media.Editing;
using Windows.Media.MediaProperties;
using Windows.Storage;
using Windows.Storage.Streams;
using Windows.UI.Popups;
using Windows.UI.StartScreen;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Animation;
using Windows.UI.Xaml.Media.Imaging;
using Windows.UI.Xaml.Navigation;
using Windows.UI.Xaml.Shapes;

namespace Vine.Views.Capture;

public sealed class CaptureView8 : BasePage, IComponentConnector
{
	public enum TutorialState
	{
		Welcome,
		FirstCapture,
		FirstCaptureRunning,
		FirstCaptureDone,
		SecondCapture,
		SecondCaptureRunning,
		SecondCaptureDone,
		ThirdCapture,
		ThirdCaptureRunning,
		ThirdCaptureDone,
		Encoding,
		FinalMessage,
		NotRunning
	}

	public enum ButtonsTutorialEnum
	{
		CameraRoll = 0,
		Undo = 1,
		Delete = 2,
		Tools = 3,
		Done = 10
	}

	private TutorialState _currentTutorialState;

	private bool _hasTutorialBeenSeen;

	private bool _buttonTutorialUndoVisibility;

	private RecordingVineModel _state;

	private Visibility _nextButtonVisibility = (Visibility)1;

	private bool _isBusy;

	private bool _isTorchSupported;

	private bool _isFrontCameraSupported;

	private bool _isFocusSupported;

	private bool _isFocusLocked;

	private bool _isGhostModeHighlighted;

	private bool _isGridHighlighted;

	private bool _isExpanded;

	private bool _isGridVisible = true;

	private bool _isUndoHighlighted;

	private bool _isCameraHighlighted;

	private bool _isTorchHighlighted;

	private bool _isFocusModeHighlighted;

	private bool _pendingChanges;

	private int _recordingDraftCount;

	private bool _isDraftsEnabled;

	private bool _isUndoBusy;

	private bool _isUndoEnabled;

	private ImageSource _ghostImageSource;

	private VineCapture _recordVineCapture;

	private VineCapture _previewVineCapture;

	private SurfaceImageSource _pausedImageSource;

	private StorageFile _recordStorageFile;

	private MediaEncodingProfile _profile;

	private VideoRotation _mediaRotation;

	private readonly SimpleStopWatch _stopwatch;

	private List<long> _frameCounts;

	private List<string> _ghosts;

	private long _frameMFtime;

	private static readonly object _locker = new object();

	private double _progressValue;

	private static readonly SemaphoreSlim _criticalSection = new SemaphoreSlim(1, 1);

	private bool _isMessageTabDefault;

	private bool _cancelNavBack = true;

	private bool _preparingToNavigate;

	private Task _lastCapturedFrameTask;

	private uint _pointerId;

	private long _pageStateTrimmedDurationCache = -1L;

	private bool _nextClicked;

	private bool _isfocusing;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Storyboard Expand;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Storyboard Collapse;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Storyboard ProgressStoryboard;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private DoubleAnimation ProgressAnimation;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Image VideoPausedControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MediaElement MediaElement;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Image UndoGhostImage;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Rectangle rectangle;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Border border;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private CaptureElement CaptureElement;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Ellipse FocusCircle;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Rectangle Progress;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Border UndoTimeRectangle;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBlock ToUsername;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton PinUnPinCommandButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public TutorialState CurrentTutorialState
	{
		get
		{
			return _currentTutorialState;
		}
		set
		{
			_currentTutorialState = value;
			NotifyOfPropertyChange(() => TutorialWelcomeVisibility);
			NotifyOfPropertyChange(() => TutorialHintVisibility);
			NotifyOfPropertyChange(() => TutorialMessage);
		}
	}

	public bool TutorialHintVisibility
	{
		get
		{
			if (!_hasTutorialBeenSeen && CurrentTutorialState != TutorialState.Welcome && CurrentTutorialState != TutorialState.NotRunning)
			{
				return CurrentTutorialState != TutorialState.Encoding;
			}
			return false;
		}
	}

	public bool TutorialWelcomeVisibility
	{
		get
		{
			if (!_hasTutorialBeenSeen)
			{
				return CurrentTutorialState == TutorialState.Welcome;
			}
			return false;
		}
	}

	public string TutorialMessage
	{
		get
		{
			switch (CurrentTutorialState)
			{
			case TutorialState.FirstCapture:
				return ResourceHelper.GetString("TutorialFirstMessage");
			case TutorialState.SecondCapture:
				return ResourceHelper.GetString("TutorialSecondMessage");
			case TutorialState.ThirdCapture:
				return ResourceHelper.GetString("TutorialThirdMessage");
			case TutorialState.FirstCaptureRunning:
			case TutorialState.SecondCaptureRunning:
			case TutorialState.ThirdCaptureRunning:
				return ResourceHelper.GetString("TutorialRunning");
			case TutorialState.FirstCaptureDone:
				return ResourceHelper.GetString("TutorialFirstDone");
			case TutorialState.SecondCaptureDone:
				return ResourceHelper.GetString("TutorialSecondDone");
			case TutorialState.ThirdCaptureDone:
				return ResourceHelper.GetString("TutorialThirdDone");
			case TutorialState.Encoding:
				return null;
			case TutorialState.FinalMessage:
				return ResourceHelper.GetString("TutorialFinalMessage");
			default:
				return null;
			}
		}
	}

	public bool ButtonTutorialCameraToolsVisibility
	{
		get
		{
			if (_hasTutorialBeenSeen)
			{
				return ButtonTutorialState == ButtonsTutorialEnum.Tools;
			}
			return false;
		}
	}

	public bool ButtonTutorialUndoVisibility
	{
		get
		{
			if (_hasTutorialBeenSeen && ButtonTutorialState == ButtonsTutorialEnum.Undo)
			{
				return _buttonTutorialUndoVisibility;
			}
			return false;
		}
		set
		{
			SetProperty(ref _buttonTutorialUndoVisibility, value, "ButtonTutorialUndoVisibility");
		}
	}

	public bool ButtonTutorialGrabVideoVisibility
	{
		get
		{
			if (_hasTutorialBeenSeen)
			{
				return ButtonTutorialState == ButtonsTutorialEnum.CameraRoll;
			}
			return false;
		}
	}

	public ButtonsTutorialEnum ButtonTutorialState { get; set; }

	public Visibility NextButtonVisibility
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return _nextButtonVisibility;
		}
		set
		{
			//IL_0007: Unknown result type (might be due to invalid IL or missing references)
			SetProperty(ref _nextButtonVisibility, value, "NextButtonVisibility");
		}
	}

	public bool IsBusy
	{
		get
		{
			return _isBusy;
		}
		set
		{
			SetProperty(ref _isBusy, value, "IsBusy");
		}
	}

	public bool IsTorchSupported
	{
		get
		{
			return _isTorchSupported;
		}
		set
		{
			SetProperty(ref _isTorchSupported, value, "IsTorchSupported");
		}
	}

	public bool IsFrontCameraSupported
	{
		get
		{
			return _isFrontCameraSupported;
		}
		set
		{
			SetProperty(ref _isFrontCameraSupported, value, "IsFrontCameraSupported");
		}
	}

	public bool IsFocusSupported
	{
		get
		{
			return _isFocusSupported;
		}
		set
		{
			SetProperty(ref _isFocusSupported, value, "IsFocusSupported");
		}
	}

	public bool IsFocusLocked
	{
		get
		{
			return _isFocusLocked;
		}
		set
		{
			SetProperty(ref _isFocusLocked, value, "IsFocusLocked");
		}
	}

	public bool IsGhostModeHighlighted
	{
		get
		{
			return _isGhostModeHighlighted;
		}
		set
		{
			SetProperty(ref _isGhostModeHighlighted, value, "IsGhostModeHighlighted");
			NotifyOfPropertyChange(() => GhostButtonBrush);
		}
	}

	public bool IsGridHighlighted
	{
		get
		{
			return _isGridHighlighted;
		}
		set
		{
			SetProperty(ref _isGridHighlighted, value, "IsGridHighlighted");
			NotifyOfPropertyChange(() => GridButtonBrush);
		}
	}

	public bool IsExpanded
	{
		get
		{
			return _isExpanded;
		}
		set
		{
			SetProperty(ref _isExpanded, value, "IsExpanded");
			NotifyOfPropertyChange(() => WrenchBrush);
		}
	}

	public bool IsGridVisible
	{
		get
		{
			return _isGridVisible;
		}
		set
		{
			SetProperty(ref _isGridVisible, value, "IsGridVisible");
			NotifyOfPropertyChange(() => GridButtonBrush);
		}
	}

	public bool IsUndoHighlighted
	{
		get
		{
			return _isUndoHighlighted;
		}
		set
		{
			SetProperty(ref _isUndoHighlighted, value, "IsUndoHighlighted");
			if (!value)
			{
				((UIElement)UndoTimeRectangle).put_Visibility((Visibility)1);
			}
		}
	}

	public bool IsCameraHighlighted
	{
		get
		{
			if (_isCameraHighlighted)
			{
				return IsFrontCameraSupported;
			}
			return false;
		}
		set
		{
			SetProperty(ref _isCameraHighlighted, value, "IsCameraHighlighted");
			NotifyOfPropertyChange(() => CameraButtonBrush);
		}
	}

	public bool IsTorchHighlighted
	{
		get
		{
			return _isTorchHighlighted;
		}
		set
		{
			SetProperty(ref _isTorchHighlighted, value, "IsTorchHighlighted");
			NotifyOfPropertyChange(() => TorchButtonBrush);
		}
	}

	public bool IsFocusModeHighlighted
	{
		get
		{
			return _isFocusModeHighlighted;
		}
		set
		{
			SetProperty(ref _isFocusModeHighlighted, value, "IsFocusModeHighlighted");
			NotifyOfPropertyChange(() => FocusButtonBrush);
		}
	}

	public bool PendingChanges
	{
		get
		{
			if (!_pendingChanges)
			{
				if (_state != null && _state.Clips.Any())
				{
					return _state.HasPendingChangesOnCapture;
				}
				return false;
			}
			return true;
		}
		set
		{
			SetProperty(ref _pendingChanges, value, "PendingChanges");
		}
	}

	public int RecordingDraftCount
	{
		get
		{
			return _recordingDraftCount;
		}
		set
		{
			SetProperty(ref _recordingDraftCount, value, "RecordingDraftCount");
			NotifyOfPropertyChange(() => DraftNumber);
		}
	}

	public string DraftNumber => RecordingDraftCount.ToStringInvariantCulture();

	public bool IsDraftsEnabled
	{
		get
		{
			return _isDraftsEnabled;
		}
		set
		{
			SetProperty(ref _isDraftsEnabled, value, "IsDraftsEnabled");
		}
	}

	public bool IsUndoEnabled
	{
		get
		{
			return _isUndoEnabled;
		}
		set
		{
			SetProperty(ref _isUndoEnabled, value, "IsUndoEnabled");
		}
	}

	public ImageSource GhostImageSource
	{
		get
		{
			return _ghostImageSource;
		}
		set
		{
			SetProperty(ref _ghostImageSource, value, "GhostImageSource");
		}
	}

	public Brush FocusButtonBrush => (Brush)(IsFocusModeHighlighted ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"] : Application.Current.Resources.DarkResource("VineGraySoftBrush"));

	public Brush GhostButtonBrush => (Brush)(IsGhostModeHighlighted ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"] : Application.Current.Resources.DarkResource("VineGraySoftBrush"));

	public Brush GridButtonBrush => (Brush)(IsGridHighlighted ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"] : Application.Current.Resources.DarkResource("VineGraySoftBrush"));

	public Brush CameraButtonBrush => (Brush)(IsCameraHighlighted ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"] : Application.Current.Resources.DarkResource("VineGraySoftBrush"));

	public Brush WrenchBrush => (Brush)(IsExpanded ? Application.Current.Resources.DarkResource("AppBarItemForegroundThemeBrush") : Application.Current.Resources.DarkResource("VineGraySoftBrush"));

	public Brush TorchButtonBrush => (Brush)(IsTorchHighlighted ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"] : Application.Current.Resources.DarkResource("VineGraySoftBrush"));

	public MediaCapture MediaCapture { get; set; }

	private bool IsRecording { get; set; }

	public ReplyVmParameters VMParameters => base.NavigationObject as ReplyVmParameters;

	private bool TileExists => SecondaryTile.Exists(SecondaryTileType.CapturePage.ToString());

	private void Tap_TutorialOK(object sender, TappedRoutedEventArgs e)
	{
		CurrentTutorialState++;
	}

	private void Tap_SkipTutorial(object sender, TappedRoutedEventArgs e)
	{
		ApplicationSettings.Current.HasSeenCaptureTutorial = (_hasTutorialBeenSeen = true);
		CurrentTutorialState = TutorialState.NotRunning;
		NotifyOfPropertyChange(() => ButtonTutorialGrabVideoVisibility);
	}

	private void TutorialGrabVideo_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		ButtonTutorialState = ButtonsTutorialEnum.Undo;
		ApplicationSettings.Current.ButtonsTutorialState = (int)ButtonTutorialState;
		UpdateTutorialButtons();
	}

	private void TutorialUndo_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		if (ButtonTutorialState == ButtonsTutorialEnum.Undo)
		{
			ButtonTutorialState = ButtonsTutorialEnum.Tools;
			ApplicationSettings.Current.ButtonsTutorialState = (int)ButtonTutorialState;
			UpdateTutorialButtons();
		}
	}

	private void TutorialCameraTools_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		if (ButtonTutorialState == ButtonsTutorialEnum.Tools)
		{
			ButtonTutorialState = ButtonsTutorialEnum.Done;
			ApplicationSettings.Current.ButtonsTutorialState = (int)ButtonTutorialState;
			UpdateTutorialButtons();
		}
	}

	public void UpdateTutorialButtons()
	{
		NotifyOfPropertyChange(() => ButtonTutorialCameraToolsVisibility);
		NotifyOfPropertyChange(() => ButtonTutorialUndoVisibility);
		NotifyOfPropertyChange(() => ButtonTutorialGrabVideoVisibility);
	}

	public CaptureView8()
	{
		//IL_0002: Unknown result type (might be due to invalid IL or missing references)
		InitializeComponent();
		_stopwatch = new SimpleStopWatch();
		_state = new RecordingVineModel();
		_ghosts = new List<string>();
		_frameCounts = new List<long>();
		ProgressAnimation.put_From((double?)0.0);
		ProgressAnimation.put_To((double?)base.WindowWidth);
		((Timeline)ProgressAnimation).put_Duration((Duration)TimeSpan.FromTicks(60000000L));
		ProgressStoryboard.Begin();
		ProgressStoryboard.Pause();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.Capture, "camera"));
		if (!IsBusy)
		{
			IsBusy = true;
			if (NavigationParam is string && (string)NavigationParam == "IsMessageTabDefault")
			{
				_isMessageTabDefault = true;
				IsCameraHighlighted = _recordVineCapture == null || IsCameraHighlighted;
			}
			if (VMParameters != null)
			{
				SolidColorBrush profileBgBrush = ApplicationSettings.Current.User.ProfileBgBrush;
				((Shape)Progress).put_Fill((Brush)(object)profileBgBrush);
				ToUsername.put_Text(VMParameters.OtherUser.Username);
				IsCameraHighlighted = _recordVineCapture == null || IsCameraHighlighted;
			}
			UpdateAppBar();
			_hasTutorialBeenSeen = ApplicationSettings.Current.HasSeenCaptureTutorial;
			ButtonTutorialState = (ButtonsTutorialEnum)ApplicationSettings.Current.ButtonsTutorialState;
			if (e.PageState != null)
			{
				CurrentTutorialState = (TutorialState)e.LoadValueOrDefault<long>("CurrentTutorialState");
			}
			else
			{
				CurrentTutorialState = (_hasTutorialBeenSeen ? TutorialState.NotRunning : TutorialState.Welcome);
			}
			await TaskManager.CleanUpTasks.RunAtEndOfQueue(LoadStateAsync);
		}
	}

	private async Task LoadStateAsync()
	{
		RecordingVineModel recordingVineModel = await ApplicationSettings.Current.GetRecordingActiveVine();
		if (recordingVineModel != null)
		{
			_state = recordingVineModel;
		}
		NextButtonVisibility = (Visibility)((_state.Duration <= 20000000) ? 1 : 0);
		IsUndoEnabled = _state.Clips.Any();
		RecordingDraftCount = (await ApplicationSettings.Current.GetRecordingDrafts()).Count;
		IsDraftsEnabled = PendingChanges || RecordingDraftCount > 0;
		_ghosts = new List<string>();
		_frameCounts = new List<long>();
		_pageStateTrimmedDurationCache = -1L;
		_frameMFtime = 0L;
		UpdateProgress();
		GetActiveProgress(out var elapsed, out var percent);
		_progressValue = percent * 100.0;
		UpdateProgressBar(elapsed);
		await RecordFromStop();
		Task.Run(delegate
		{
			_pageStateTrimmedDurationCache = -1L;
			while (IsRecording)
			{
				UpdateProgress();
				new ManualResetEvent(initialState: false).WaitOne(50);
			}
		});
		IsBusy = false;
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		//IL_002a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0030: Invalid comparison between Unknown and I4
		base.SaveState(sender, e);
		e.PageState["CurrentTutorialState"] = (long)CurrentTutorialState;
		if ((int)e.NavigationEventArgs.NavigationMode == 2 && (object)e.NavigationEventArgs.SourcePageType == typeof(CaptureView8))
		{
			TaskManager.CleanUpTasks.RunAtEndOfQueue(() => PrepareNavigating(backward: false));
		}
	}

	protected override async void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		e.put_Cancel((int)e.NavigationMode == 1 && _cancelNavBack && PendingChanges);
		if (!e.Cancel)
		{
			TaskManager.CleanUpTasks.RunAtEndOfQueue(() => PrepareNavigating((int)e.NavigationMode == 1));
		}
		_003C_003En__1(e);
		if (!e.Cancel)
		{
			return;
		}
		IUICommand val = await new MessageDialog(ResourceHelper.GetString("SaveForLaterPrompt"), ResourceHelper.GetString("vine"))
		{
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("SaveForLater"), (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Discard"), (UICommandInvokedHandler)null, (object)1) }
		}.ShowAsync();
		if (val == null)
		{
			return;
		}
		_cancelNavBack = false;
		((Page)this).Frame.GoBack();
		if ((int)val.Id == 0)
		{
			TaskManager.CleanUpTasks.RunAtEndOfQueue(() => PrepareNavigating(backward: true, SaveToDraftsAsync));
		}
		else
		{
			TaskManager.CleanUpTasks.RunAtEndOfQueue(() => PrepareNavigating(backward: true, DiscardAsync));
		}
	}

	private async Task PrepareNavigating(bool backward, Func<Task> action = null)
	{
		if (!_preparingToNavigate)
		{
			_preparingToNavigate = true;
			await CleanupCaptureResources();
			if (action != null)
			{
				await action();
			}
			await ApplicationSettings.Current.SetRecordingActiveVine(backward ? null : _state);
			_preparingToNavigate = false;
		}
	}

	public async Task CleanupCaptureResources()
	{
		IsBusy = true;
		await EnterCriticalSection();
		try
		{
			while (!_recordVineCapture.Pause)
			{
				await Task.Delay(100);
			}
			bool wasRecording = IsRecording;
			if (wasRecording)
			{
				IsRecording = false;
				await MediaCapture.StopPreviewAsync();
				await MediaCapture.StopRecordAsync();
				MediaCapture.Dispose();
			}
			if (_lastCapturedFrameTask != null)
			{
				await _lastCapturedFrameTask;
				_lastCapturedFrameTask = null;
			}
			WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)_recordVineCapture.remove_LastCapturedFrame, (EventHandler<FrameInfo>)VineCaptureOnLastCapturedFrame);
			if (!_frameCounts.Any() && wasRecording)
			{
				await _recordStorageFile.DeleteAsync();
				_recordStorageFile = null;
			}
			else if (wasRecording)
			{
				MediaRatio frameRate = (await MediaClip.CreateFromFileAsync((IStorageFile)(object)_recordStorageFile)).GetVideoEncodingProperties().FrameRate;
				long frameRate2 = (long)(10000000.0 / ((double)frameRate.Numerator / (double)frameRate.Denominator));
				MediaFile mediaFile = new MediaFile(true)
				{
					UseHardware = true
				};
				await mediaFile.SetVideo(_recordStorageFile);
				await mediaFile.OpenMF();
				long num = await mediaFile.GetRenderFrame(0L, true);
				mediaFile.CloseMF();
				long[] array = new long[_frameCounts.Count];
				long[] array2 = new long[_frameCounts.Count];
				for (int i = 0; i < array.Length; i++)
				{
					array2[i] = num + _frameCounts[i] * frameRate2;
					if (i == 0)
					{
						array[i] = 0L;
					}
					else
					{
						array[i] = array2[i - 1] + frameRate2 * 2;
					}
				}
				long videoFileDuration = array2.Last() + 3 * frameRate2;
				for (int j = 0; j < array.Length; j++)
				{
					_state.Clips.Add(new RecordingClipModel
					{
						FileStartTime = array[j],
						FileEndTime = array2[j],
						EditStartTime = 0L,
						EditEndTime = array2[j] - array[j],
						GhostFilePath = _ghosts[j],
						VideoFileDuration = videoFileDuration,
						VideoFilePath = _recordStorageFile.Path,
						FrameRate = frameRate2
					});
				}
			}
		}
		catch (Exception)
		{
		}
		ExitCriticalSection();
		IsBusy = false;
	}

	private void Viewport_PointerPressed(object sender, PointerRoutedEventArgs e)
	{
		RecordFromPaused(e.GetCurrentPoint((UIElement)null).PointerId);
	}

	private void Viewport_PointerReleased(object sender, PointerRoutedEventArgs e)
	{
		PauseFromRecording(e.GetCurrentPoint((UIElement)null).PointerId);
		if (!_hasTutorialBeenSeen && (CurrentTutorialState == TutorialState.FirstCaptureDone || CurrentTutorialState == TutorialState.SecondCaptureDone || CurrentTutorialState == TutorialState.ThirdCaptureDone))
		{
			CurrentTutorialState++;
			if (CurrentTutorialState == TutorialState.Encoding)
			{
				Next_Click(this, null);
			}
		}
	}

	private async Task RecordFromStop()
	{
		await EnterCriticalSection();
		try
		{
			await InitializeMediaCapture();
			CaptureView8 captureView = this;
			_ = captureView._recordStorageFile;
			captureView._recordStorageFile = await _state.NewVideoCaptureFile();
			await MediaCapture.StartPreviewAsync();
			await MediaCapture.StartRecordToStorageFileAsync(_profile, (IStorageFile)(object)_recordStorageFile);
			IsRecording = true;
			IsTorchSupported = MediaCapture.IsTorchSupported();
			if (!IsTorchSupported)
			{
				IsTorchHighlighted = false;
			}
			IsFocusSupported = MediaCapture.IsFocusSupported();
			if (!IsFocusSupported)
			{
				IsFocusModeHighlighted = false;
			}
		}
		catch (Exception)
		{
			UnknownError();
		}
		ExitCriticalSection();
	}

	private async Task UnknownError()
	{
		await new MessageDialog(ResourceHelper.GetString("camera_failed_to_open"), ResourceHelper.GetString("vine")).ShowAsync();
		_cancelNavBack = false;
		App.RootFrame.GoBack();
	}

	private void RecordFromPaused(uint pointerId)
	{
		if (!IsRecording || IsFocusModeHighlighted || _progressValue >= 100.0)
		{
			return;
		}
		lock (_locker)
		{
			if (_pointerId != 0)
			{
				return;
			}
			_pointerId = pointerId;
			_recordVineCapture.Pause = false;
			_stopwatch.Start();
			GetActiveProgress(out var elapsed, out var _);
			UpdateProgressBar(elapsed);
			ProgressStoryboard.Resume();
		}
		PendingChanges = true;
		IsDraftsEnabled = true;
		IsUndoEnabled = true;
		if (ButtonTutorialState == ButtonsTutorialEnum.Undo)
		{
			ButtonTutorialUndoVisibility = true;
		}
	}

	private async Task PauseFromRecording(uint pointerId)
	{
		if (_stopwatch.ElapsedTicks < TimeSpan.FromMilliseconds(100.0).Ticks)
		{
			await Task.Delay(TimeSpan.FromMilliseconds(100.0) - TimeSpan.FromTicks(_stopwatch.ElapsedTicks));
		}
		if (IsFocusModeHighlighted)
		{
			return;
		}
		lock (_locker)
		{
			if (_pointerId == pointerId && !_recordVineCapture.Pause)
			{
				ProgressStoryboard.Pause();
				_recordVineCapture.Pause = true;
			}
		}
	}

	private async Task InitializeMediaCapture()
	{
		_pausedImageSource = new SurfaceImageSource(480, 480);
		VideoPausedControl.put_Source((ImageSource)(object)_pausedImageSource);
		int num = 640;
		int num2 = 480;
		int num3 = 480;
		int num4 = 480;
		int num5 = (num - num3) / 2;
		int num6 = (num2 - num4) / 2;
		int num7 = num5;
		int num8 = num6;
		int num9 = num7 + num3;
		int num10 = num8 + num4;
		_recordVineCapture = new VineCapture((SurfaceImageSource)null, _pausedImageSource, true, false, true, true, true, num, num2, num3, num4, num7, num8, num9, num10);
		VineCapture recordVineCapture = _recordVineCapture;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<FrameInfo>, EventRegistrationToken>)recordVineCapture.add_LastCapturedFrame, (Action<EventRegistrationToken>)recordVineCapture.remove_LastCapturedFrame, (EventHandler<FrameInfo>)VineCaptureOnLastCapturedFrame);
		recordVineCapture = _recordVineCapture;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)recordVineCapture.add_ReadyForInit, (Action<EventRegistrationToken>)recordVineCapture.remove_ReadyForInit, (EventHandler<object>)VineCaptureOnReadyForInitRecord);
		_previewVineCapture = new VineCapture((SurfaceImageSource)null, (SurfaceImageSource)null, false, false, false, false, true, num, num2, num3, num4, num7, num8, num9, num10);
		recordVineCapture = _previewVineCapture;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)recordVineCapture.add_ReadyForInit, (Action<EventRegistrationToken>)recordVineCapture.remove_ReadyForInit, (EventHandler<object>)VineCaptureOnReadyForInitPreview);
		DeviceInformation primaryCameraInfo = await DeviceHelper.GetCameraDeviceInfoAsync(CameraType.Primary);
		DeviceInformation val = await DeviceHelper.GetCameraDeviceInfoAsync(CameraType.FrontFacing);
		if (val != null)
		{
			IsFrontCameraSupported = true;
		}
		DeviceInformation val2 = ((IsCameraHighlighted && val != null) ? val : primaryCameraInfo);
		MediaCaptureInitializationSettings val3 = new MediaCaptureInitializationSettings();
		val3.put_StreamingCaptureMode((StreamingCaptureMode)0);
		val3.put_PhotoCaptureSource((PhotoCaptureSource)0);
		val3.put_AudioDeviceId(string.Empty);
		val3.put_VideoDeviceId(val2.Id);
		MediaCaptureInitializationSettings val4 = val3;
		MediaCapture = new MediaCapture();
		await MediaCapture.InitializeAsync(val4);
		MediaCapture.VideoDeviceController.put_PrimaryUse((CaptureUse)2);
		_mediaRotation = (VideoRotation)((!IsCameraHighlighted) ? 1 : 3);
		MediaCapture.SetPreviewRotation(_mediaRotation);
		MediaCapture.SetRecordRotation(_mediaRotation);
		PropertySet val5 = new PropertySet();
		((IDictionary<string, object>)val5).Add("CaptureInterface", (object)_recordVineCapture);
		PropertySet property = val5;
		await MediaCapture.AddEffectAsync((MediaStreamType)1, "GrayscaleTransform.GrayscaleEffect", (IPropertySet)(object)property);
		await MediaCapture.AddEffectAsync((MediaStreamType)2, "AudioTransform.AudioEffect", (IPropertySet)(object)property);
		PropertySet val6 = new PropertySet();
		((IDictionary<string, object>)val6).Add("CaptureInterface", (object)_previewVineCapture);
		PropertySet val7 = val6;
		await MediaCapture.AddEffectAsync((MediaStreamType)0, "GrayscaleTransform.GrayscaleEffect", (IPropertySet)(object)val7);
		CaptureElement.put_Source(MediaCapture);
		_profile = MediaEncodingProfile.CreateMp4((VideoEncodingQuality)6);
		if (_profile == null || _profile.Video == null)
		{
			await UnknownError();
			return;
		}
		_profile.Video.put_Height(480u);
		_profile.Video.put_Width(480u);
	}

	private async Task EnterCriticalSection()
	{
		await _criticalSection.WaitAsync();
	}

	private void ExitCriticalSection()
	{
		_criticalSection.Release();
	}

	private void VineCaptureOnReadyForInitRecord(object sender, object o)
	{
		WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)_recordVineCapture.remove_ReadyForInit, (EventHandler<object>)VineCaptureOnReadyForInitRecord);
		DispatcherEx.BeginInvoke(delegate
		{
			_recordVineCapture.Initialize();
		});
	}

	private void VineCaptureOnReadyForInitPreview(object sender, object o)
	{
		WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)_previewVineCapture.remove_ReadyForInit, (EventHandler<object>)VineCaptureOnReadyForInitPreview);
		DispatcherEx.BeginInvoke(delegate
		{
			_previewVineCapture.Initialize();
		});
	}

	private void VineCaptureOnLastCapturedFrame(object sender, FrameInfo frame)
	{
		_frameCounts.Add(frame.FrameCount);
		_frameMFtime = frame.TimeStamp;
		_pointerId = 0u;
		_lastCapturedFrameTask = DispatcherEx.Invoke(async delegate
		{
			_recordVineCapture.RenderPausedTexture();
			GetActiveProgress(out var elapsed, out var percent);
			_progressValue = percent * 100.0;
			UpdateProgressBar(elapsed);
			try
			{
				RenderTargetBitmap bitmap = new RenderTargetBitmap();
				await bitmap.RenderAsync((UIElement)(object)VideoPausedControl);
				IBuffer pixelBuffer = await bitmap.GetPixelsAsync();
				WriteableBitmap wb = (await WriteableBitmapExtensions.FromPixelBuffer(new WriteableBitmap(1, 1), pixelBuffer, bitmap.PixelWidth, bitmap.PixelHeight)).Rotate(_mediaRotation.ToInt());
				if (IsGhostModeHighlighted)
				{
					GhostImageSource = (ImageSource)(object)wb;
				}
				StorageFile file = await _state.NewGhostFile();
				await wb.ToFileAsync(file);
				_ghosts.Add(file.Path);
			}
			catch (Exception)
			{
			}
		});
	}

	private void GetActiveProgress(out long elapsed, out double percent)
	{
		elapsed = _frameMFtime;
		if (_recordVineCapture != null && !_recordVineCapture.Pause)
		{
			elapsed += _stopwatch.ElapsedTicks;
		}
		if (_pageStateTrimmedDurationCache == -1)
		{
			_pageStateTrimmedDurationCache = _state.Duration;
		}
		elapsed += _pageStateTrimmedDurationCache;
		percent = (double)elapsed / 60000000.0;
	}

	private void UpdateProgress()
	{
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0022: Invalid comparison between Unknown and I4
		if (_recordVineCapture == null)
		{
			return;
		}
		GetActiveProgress(out var elapsed, out var percent);
		if (_hasTutorialBeenSeen)
		{
			if ((int)NextButtonVisibility == 1 && elapsed > 20000000)
			{
				DispatcherEx.BeginInvoke(delegate
				{
					NextButtonVisibility = (Visibility)0;
				});
			}
			else
			{
				if (elapsed < 60000000 || _nextClicked || _recordVineCapture.Pause)
				{
					return;
				}
				_nextClicked = true;
				DispatcherEx.BeginInvoke(async delegate
				{
					if (!_recordVineCapture.Pause)
					{
						await _recordVineCapture.PauseAsync();
						await _lastCapturedFrameTask;
					}
					if (_ghosts.Count != _frameCounts.Count)
					{
						await Task.Delay(100);
					}
					Next_Click(this, null);
				});
			}
		}
		else
		{
			if (_recordVineCapture.Pause)
			{
				return;
			}
			if (percent > 0.0 && CurrentTutorialState == TutorialState.FirstCapture)
			{
				DispatcherEx.BeginInvoke(delegate
				{
					CurrentTutorialState++;
				});
			}
			else if (CurrentTutorialState == TutorialState.FirstCaptureRunning && percent > 0.33)
			{
				DispatcherEx.BeginInvoke(async delegate
				{
					CurrentTutorialState++;
					await PauseFromRecording(_pointerId);
				});
			}
			else if (CurrentTutorialState == TutorialState.SecondCapture)
			{
				DispatcherEx.BeginInvoke(delegate
				{
					CurrentTutorialState++;
				});
			}
			else if (CurrentTutorialState == TutorialState.SecondCaptureRunning && percent > 0.66)
			{
				DispatcherEx.BeginInvoke(async delegate
				{
					CurrentTutorialState++;
					await PauseFromRecording(_pointerId);
				});
			}
			else if (CurrentTutorialState == TutorialState.ThirdCapture)
			{
				DispatcherEx.BeginInvoke(delegate
				{
					CurrentTutorialState++;
				});
			}
			else if (CurrentTutorialState == TutorialState.ThirdCaptureRunning && percent >= 1.0)
			{
				DispatcherEx.BeginInvoke(async delegate
				{
					CurrentTutorialState++;
					await PauseFromRecording(_pointerId);
				});
			}
		}
	}

	private void UpdateProgressBar(long elapsed)
	{
		ProgressStoryboard.SeekAlignedToLastTick(TimeSpan.FromTicks(elapsed));
	}

	private async void Viewport_Tapped(object sender, TappedRoutedEventArgs e)
	{
		if (!_isfocusing && IsFocusModeHighlighted)
		{
			_isfocusing = true;
			FrameworkElement val = (FrameworkElement)sender;
			Point position = e.GetPosition((UIElement)(object)val);
			TranslateTransform val2 = (TranslateTransform)((UIElement)FocusCircle).RenderTransform;
			val2.put_X(position.X);
			val2.put_Y(position.Y);
			Task circleDisplayTask = DispatcherEx.Invoke(async delegate
			{
				((UIElement)FocusCircle).put_Visibility((Visibility)0);
				await Task.Delay(2000);
				((UIElement)FocusCircle).put_Visibility((Visibility)1);
			});
			Point focusPoint = new Point(position.X / val.ActualWidth, position.Y / val.ActualHeight);
			if (IsCameraHighlighted)
			{
				focusPoint.X = 1.0 - focusPoint.X;
			}
			try
			{
				await MediaCapture.BeginFocusAtPoint(focusPoint);
			}
			catch (Exception)
			{
			}
			await circleDisplayTask;
			_isfocusing = false;
		}
	}

	private async Task DiscardAsync()
	{
		if (_state.DraftId != null)
		{
			await _state.DiscardDraftChanges();
		}
		else
		{
			await _state.DeleteAsync();
		}
	}

	private async Task SaveToDraftsAsync()
	{
		await _state.SaveToDrafts();
		_ghosts = new List<string>();
	}

	private void UpdateAppBar()
	{
		//IL_0054: Unknown result type (might be due to invalid IL or missing references)
		//IL_005e: Expected O, but got Unknown
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_0032: Expected O, but got Unknown
		if (!TileExists)
		{
			PinUnPinCommandButton.put_Label(ResourceHelper.GetString("Pin"));
			PinUnPinCommandButton.put_Icon((IconElement)new SymbolIcon((Symbol)57665));
		}
		else
		{
			PinUnPinCommandButton.put_Label(ResourceHelper.GetString("Unpin"));
			PinUnPinCommandButton.put_Icon((IconElement)new SymbolIcon((Symbol)57750));
		}
		((UIElement)PinUnPinCommandButton).UpdateLayout();
	}

	private async void PinUnPinCommandButton_OnClick(object sender, RoutedEventArgs e)
	{
		if (TileExists)
		{
			await TileHelper.DeleteSecondaryTile(SecondaryTileType.CapturePage.ToString());
		}
		else
		{
			await TileHelper.CreateCapturePageTile(SecondaryTileType.CapturePage.ToString());
		}
		UpdateAppBar();
	}

	private void Cancel_Click(object sender, RoutedEventArgs e)
	{
		if (((Page)this).Frame.CanGoBack)
		{
			((Page)this).Frame.GoBack();
		}
		else
		{
			App.RootFrame.Navigate(typeof(HomeView));
		}
	}

	private void Next_Click(object sender, RoutedEventArgs e)
	{
		App.RootFrame.NavigateWithObject(typeof(PreviewCaptureView), new PreviewCaptureParams
		{
			VMParameters = VMParameters,
			IsMessageTabDefault = _isMessageTabDefault,
			FullCaptureVideoFile = ((_recordStorageFile == null) ? null : _recordStorageFile.Path)
		});
	}

	private async void btnImport_Click(object sender, RoutedEventArgs e)
	{
		GetActiveProgress(out var _, out var percent);
		if (percent >= 0.98)
		{
			await new MessageDialog(ResourceHelper.GetString("ImportAudioDeleteVideoAlert"), ResourceHelper.GetString("vine")).ShowAsync();
			return;
		}
		App.RootFrame.Navigate(typeof(ImportView));
		ButtonTutorialState = ButtonsTutorialEnum.Undo;
		ApplicationSettings.Current.ButtonsTutorialState = (int)ButtonTutorialState;
		UpdateTutorialButtons();
	}

	private async void btnUndo_Click(object sender, RoutedEventArgs e)
	{
		if (_isUndoBusy)
		{
			return;
		}
		_isUndoBusy = true;
		if (ButtonTutorialState == ButtonsTutorialEnum.Undo)
		{
			ButtonTutorialState = ButtonsTutorialEnum.Tools;
			ApplicationSettings.Current.ButtonsTutorialState = (int)ButtonTutorialState;
			UpdateTutorialButtons();
		}
		IsUndoHighlighted = !IsUndoHighlighted;
		if (IsUndoHighlighted)
		{
			IsBusy = true;
			UndoGhostImage.put_Source((ImageSource)null);
			if (_frameCounts.Any())
			{
				await CleanupCaptureResources();
			}
			if (_state.Clips.Any())
			{
				RecordingClipModel clipModel = _state.Clips.Last();
				try
				{
					MediaClip val = await MediaClip.CreateFromFileAsync((IStorageFile)(object)(await StorageFile.GetFileFromPathAsync(clipModel.VideoFilePath)));
					clipModel.ToMediaClip(val);
					MediaStreamSource mediaStreamSource = new MediaComposition
					{
						Clips = { val }
					}.GenerateMediaStreamSource();
					MediaElement.SetMediaStreamSource((IMediaSource)(object)mediaStreamSource);
					MediaElement.Play();
				}
				catch
				{
					DispatcherEx.BeginInvoke(async delegate
					{
						if (_state.Clips.LastOrDefault() != null)
						{
							BitmapImage val2 = await (await StorageFile.GetFileFromPathAsync(_state.Clips.Last().GhostFilePath)).ToBitmap();
							UndoGhostImage.put_Source((ImageSource)(object)val2);
						}
					});
				}
				long num = _state.Duration - clipModel.EditEndTime - clipModel.EditStartTime - 2 * clipModel.FrameRate;
				long duration = _state.Duration;
				double num2 = (double)num / 60000000.0;
				double num3 = (double)duration / 60000000.0;
				((FrameworkElement)UndoTimeRectangle).put_Margin(new Thickness(num2 * WindowWidth, 0.0, (1.0 - num3) * WindowWidth, 0.0));
				((UIElement)UndoTimeRectangle).put_Visibility((Visibility)0);
			}
			IsBusy = false;
		}
		else if (_state.Clips.Any())
		{
			MediaElement.Stop();
			_state.Clips.RemoveAt(_state.Clips.Count - 1);
			IsUndoEnabled = _state.Clips.Any();
			_pageStateTrimmedDurationCache = -1L;
			GetActiveProgress(out var elapsed, out var percent);
			_progressValue = percent * 100.0;
			UpdateProgressBar(elapsed);
			NextButtonVisibility = (Visibility)((_state.Duration <= 20000000) ? 1 : 0);
			if (!IsRecording)
			{
				await LoadStateAsync();
			}
		}
		_isUndoBusy = false;
	}

	private async void UndoCancel_PointerPressed(object sender, PointerRoutedEventArgs e)
	{
		_isUndoBusy = true;
		MediaElement.Stop();
		if (!IsRecording)
		{
			await LoadStateAsync();
		}
		_isUndoBusy = false;
	}

	private void UndoCancel_PointerReleased(object sender, PointerRoutedEventArgs e)
	{
		IsUndoHighlighted = false;
	}

	private void btnWrench_Click(object sender, RoutedEventArgs e)
	{
		if (ButtonTutorialState == ButtonsTutorialEnum.Tools)
		{
			ButtonTutorialState = ButtonsTutorialEnum.Done;
			ApplicationSettings.Current.ButtonsTutorialState = (int)ButtonTutorialState;
			UpdateTutorialButtons();
		}
		if (IsExpanded)
		{
			Collapse.Begin();
		}
		else
		{
			Expand.Begin();
		}
		IsExpanded = !IsExpanded;
	}

	private async void SwitchCamera_Click(object sender, RoutedEventArgs e)
	{
		IsCameraHighlighted = !IsCameraHighlighted;
		await CleanupCaptureResources();
		_ghosts = new List<string>();
		_pageStateTrimmedDurationCache = -1L;
		LoadState();
	}

	private async void Drafts_Click(object sender, RoutedEventArgs e)
	{
		if (!PendingChanges)
		{
			await TaskManager.CleanUpTasks.RunAtEndOfQueue(() => PrepareNavigating(backward: false));
		}
		else
		{
			await TaskManager.CleanUpTasks.RunAtEndOfQueue(() => PrepareNavigating(backward: false, SaveToDraftsAsync));
		}
		App.RootFrame.Navigate(typeof(DraftsView), (object)_state.DraftId);
	}

	private void Grid_Click(object sender, RoutedEventArgs e)
	{
		IsGridHighlighted = !IsGridHighlighted;
	}

	private void FocusMode_Clicked(object sender, RoutedEventArgs e)
	{
		IsFocusModeHighlighted = !IsFocusModeHighlighted;
	}

	private async void Ghost_Click(object sender, RoutedEventArgs e)
	{
		IsGhostModeHighlighted = !IsGhostModeHighlighted;
		if (IsGhostModeHighlighted)
		{
			if (_ghosts.Any())
			{
				await UpdateGhostImgSrc(_ghosts.Last());
			}
			else if (_state.Clips.Any())
			{
				await UpdateGhostImgSrc(_state.Clips.Last().GhostFilePath);
			}
		}
	}

	private async Task UpdateGhostImgSrc(string filePath)
	{
		GhostImageSource = (ImageSource)(object)(await (await StorageFile.GetFileFromPathAsync(filePath)).ToBitmap());
	}

	private void Torch_Click(object sender, RoutedEventArgs e)
	{
		IsTorchHighlighted = !IsTorchHighlighted;
		MediaCapture.TorchState(IsTorchHighlighted);
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/Capture/CaptureView8.xaml"), (ComponentResourceLocation)0);
			Expand = (Storyboard)((FrameworkElement)this).FindName("Expand");
			Collapse = (Storyboard)((FrameworkElement)this).FindName("Collapse");
			ProgressStoryboard = (Storyboard)((FrameworkElement)this).FindName("ProgressStoryboard");
			ProgressAnimation = (DoubleAnimation)((FrameworkElement)this).FindName("ProgressAnimation");
			VideoPausedControl = (Image)((FrameworkElement)this).FindName("VideoPausedControl");
			MediaElement = (MediaElement)((FrameworkElement)this).FindName("MediaElement");
			UndoGhostImage = (Image)((FrameworkElement)this).FindName("UndoGhostImage");
			rectangle = (Rectangle)((FrameworkElement)this).FindName("rectangle");
			border = (Border)((FrameworkElement)this).FindName("border");
			CaptureElement = (CaptureElement)((FrameworkElement)this).FindName("CaptureElement");
			FocusCircle = (Ellipse)((FrameworkElement)this).FindName("FocusCircle");
			Progress = (Rectangle)((FrameworkElement)this).FindName("Progress");
			UndoTimeRectangle = (Border)((FrameworkElement)this).FindName("UndoTimeRectangle");
			ToUsername = (TextBlock)((FrameworkElement)this).FindName("ToUsername");
			PinUnPinCommandButton = (AppBarButton)((FrameworkElement)this).FindName("PinUnPinCommandButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0062: Unknown result type (might be due to invalid IL or missing references)
		//IL_0068: Expected O, but got Unknown
		//IL_0089: Unknown result type (might be due to invalid IL or missing references)
		//IL_0093: Expected O, but got Unknown
		//IL_0094: Unknown result type (might be due to invalid IL or missing references)
		//IL_009a: Expected O, but got Unknown
		//IL_00bb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c5: Expected O, but got Unknown
		//IL_00c6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cc: Expected O, but got Unknown
		//IL_00ed: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f7: Expected O, but got Unknown
		//IL_00f8: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fe: Expected O, but got Unknown
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0129: Expected O, but got Unknown
		//IL_012a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0130: Expected O, but got Unknown
		//IL_0151: Unknown result type (might be due to invalid IL or missing references)
		//IL_015b: Expected O, but got Unknown
		//IL_015c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0162: Expected O, but got Unknown
		//IL_0183: Unknown result type (might be due to invalid IL or missing references)
		//IL_018d: Expected O, but got Unknown
		//IL_0193: Unknown result type (might be due to invalid IL or missing references)
		//IL_0199: Expected O, but got Unknown
		//IL_01ba: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c4: Expected O, but got Unknown
		//IL_01c5: Unknown result type (might be due to invalid IL or missing references)
		//IL_01cb: Expected O, but got Unknown
		//IL_01ec: Unknown result type (might be due to invalid IL or missing references)
		//IL_01f6: Expected O, but got Unknown
		//IL_01f7: Unknown result type (might be due to invalid IL or missing references)
		//IL_01fd: Expected O, but got Unknown
		//IL_021e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0228: Expected O, but got Unknown
		//IL_0229: Unknown result type (might be due to invalid IL or missing references)
		//IL_022f: Expected O, but got Unknown
		//IL_0250: Unknown result type (might be due to invalid IL or missing references)
		//IL_025a: Expected O, but got Unknown
		//IL_025b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0261: Expected O, but got Unknown
		//IL_0282: Unknown result type (might be due to invalid IL or missing references)
		//IL_028c: Expected O, but got Unknown
		//IL_0292: Unknown result type (might be due to invalid IL or missing references)
		//IL_0298: Expected O, but got Unknown
		//IL_02b9: Unknown result type (might be due to invalid IL or missing references)
		//IL_02c3: Expected O, but got Unknown
		//IL_02c4: Unknown result type (might be due to invalid IL or missing references)
		//IL_02ca: Expected O, but got Unknown
		//IL_02eb: Unknown result type (might be due to invalid IL or missing references)
		//IL_02f5: Expected O, but got Unknown
		//IL_02f6: Unknown result type (might be due to invalid IL or missing references)
		//IL_02fc: Expected O, but got Unknown
		//IL_031d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0327: Expected O, but got Unknown
		//IL_0328: Unknown result type (might be due to invalid IL or missing references)
		//IL_032e: Expected O, but got Unknown
		//IL_034f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0359: Expected O, but got Unknown
		//IL_035a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0360: Expected O, but got Unknown
		//IL_0381: Unknown result type (might be due to invalid IL or missing references)
		//IL_038b: Expected O, but got Unknown
		//IL_0391: Unknown result type (might be due to invalid IL or missing references)
		//IL_0397: Expected O, but got Unknown
		//IL_03b8: Unknown result type (might be due to invalid IL or missing references)
		//IL_03c2: Expected O, but got Unknown
		//IL_03c8: Unknown result type (might be due to invalid IL or missing references)
		//IL_03ce: Expected O, but got Unknown
		//IL_03ef: Unknown result type (might be due to invalid IL or missing references)
		//IL_03f9: Expected O, but got Unknown
		//IL_03ff: Unknown result type (might be due to invalid IL or missing references)
		//IL_0405: Expected O, but got Unknown
		//IL_0426: Unknown result type (might be due to invalid IL or missing references)
		//IL_0430: Expected O, but got Unknown
		//IL_0436: Unknown result type (might be due to invalid IL or missing references)
		//IL_043c: Expected O, but got Unknown
		//IL_045d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0467: Expected O, but got Unknown
		//IL_046d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0473: Expected O, but got Unknown
		//IL_0494: Unknown result type (might be due to invalid IL or missing references)
		//IL_049e: Expected O, but got Unknown
		//IL_04a4: Unknown result type (might be due to invalid IL or missing references)
		//IL_04aa: Expected O, but got Unknown
		//IL_04cb: Unknown result type (might be due to invalid IL or missing references)
		//IL_04d5: Expected O, but got Unknown
		//IL_04db: Unknown result type (might be due to invalid IL or missing references)
		//IL_04e1: Expected O, but got Unknown
		//IL_0502: Unknown result type (might be due to invalid IL or missing references)
		//IL_050c: Expected O, but got Unknown
		//IL_0512: Unknown result type (might be due to invalid IL or missing references)
		//IL_0518: Expected O, but got Unknown
		//IL_0539: Unknown result type (might be due to invalid IL or missing references)
		//IL_0543: Expected O, but got Unknown
		//IL_0549: Unknown result type (might be due to invalid IL or missing references)
		//IL_054f: Expected O, but got Unknown
		//IL_0570: Unknown result type (might be due to invalid IL or missing references)
		//IL_057a: Expected O, but got Unknown
		//IL_0580: Unknown result type (might be due to invalid IL or missing references)
		//IL_0586: Expected O, but got Unknown
		//IL_05a7: Unknown result type (might be due to invalid IL or missing references)
		//IL_05b1: Expected O, but got Unknown
		//IL_05b7: Unknown result type (might be due to invalid IL or missing references)
		//IL_05bd: Expected O, but got Unknown
		//IL_05de: Unknown result type (might be due to invalid IL or missing references)
		//IL_05e8: Expected O, but got Unknown
		//IL_05ee: Unknown result type (might be due to invalid IL or missing references)
		//IL_05f4: Expected O, but got Unknown
		//IL_0615: Unknown result type (might be due to invalid IL or missing references)
		//IL_061f: Expected O, but got Unknown
		//IL_0625: Unknown result type (might be due to invalid IL or missing references)
		//IL_062b: Expected O, but got Unknown
		//IL_064c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0656: Expected O, but got Unknown
		//IL_065c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0662: Expected O, but got Unknown
		//IL_0683: Unknown result type (might be due to invalid IL or missing references)
		//IL_068d: Expected O, but got Unknown
		//IL_0693: Unknown result type (might be due to invalid IL or missing references)
		//IL_0699: Expected O, but got Unknown
		//IL_06ba: Unknown result type (might be due to invalid IL or missing references)
		//IL_06c4: Expected O, but got Unknown
		//IL_06ca: Unknown result type (might be due to invalid IL or missing references)
		//IL_06d0: Expected O, but got Unknown
		//IL_06f1: Unknown result type (might be due to invalid IL or missing references)
		//IL_06fb: Expected O, but got Unknown
		//IL_06fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_0704: Expected O, but got Unknown
		//IL_0725: Unknown result type (might be due to invalid IL or missing references)
		//IL_072f: Expected O, but got Unknown
		//IL_0732: Unknown result type (might be due to invalid IL or missing references)
		//IL_0738: Expected O, but got Unknown
		//IL_0759: Unknown result type (might be due to invalid IL or missing references)
		//IL_0763: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Viewport_Tapped));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerPressed, (Action<EventRegistrationToken>)val2.remove_PointerPressed, new PointerEventHandler(Viewport_PointerPressed));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerReleased, (Action<EventRegistrationToken>)val2.remove_PointerReleased, new PointerEventHandler(Viewport_PointerReleased));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerCanceled, (Action<EventRegistrationToken>)val2.remove_PointerCanceled, new PointerEventHandler(Viewport_PointerReleased));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerCaptureLost, (Action<EventRegistrationToken>)val2.remove_PointerCaptureLost, new PointerEventHandler(Viewport_PointerReleased));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerExited, (Action<EventRegistrationToken>)val2.remove_PointerExited, new PointerEventHandler(Viewport_PointerReleased));
			break;
		}
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerPressed, (Action<EventRegistrationToken>)val2.remove_PointerPressed, new PointerEventHandler(UndoCancel_PointerPressed));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerReleased, (Action<EventRegistrationToken>)val2.remove_PointerReleased, new PointerEventHandler(UndoCancel_PointerReleased));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerCanceled, (Action<EventRegistrationToken>)val2.remove_PointerCanceled, new PointerEventHandler(UndoCancel_PointerReleased));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerCaptureLost, (Action<EventRegistrationToken>)val2.remove_PointerCaptureLost, new PointerEventHandler(UndoCancel_PointerReleased));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerExited, (Action<EventRegistrationToken>)val2.remove_PointerExited, new PointerEventHandler(UndoCancel_PointerReleased));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerPressed, (Action<EventRegistrationToken>)val2.remove_PointerPressed, new PointerEventHandler(UndoCancel_PointerPressed));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerReleased, (Action<EventRegistrationToken>)val2.remove_PointerReleased, new PointerEventHandler(UndoCancel_PointerReleased));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerCanceled, (Action<EventRegistrationToken>)val2.remove_PointerCanceled, new PointerEventHandler(UndoCancel_PointerReleased));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerCaptureLost, (Action<EventRegistrationToken>)val2.remove_PointerCaptureLost, new PointerEventHandler(UndoCancel_PointerReleased));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerExited, (Action<EventRegistrationToken>)val2.remove_PointerExited, new PointerEventHandler(UndoCancel_PointerReleased));
			break;
		}
		case 4:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(btnUndo_Click));
			break;
		}
		case 5:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Tap_SkipTutorial));
			break;
		}
		case 6:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Tap_TutorialOK));
			break;
		}
		case 7:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(btnImport_Click));
			break;
		}
		case 8:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(btnUndo_Click));
			break;
		}
		case 9:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(btnWrench_Click));
			break;
		}
		case 10:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(SwitchCamera_Click));
			break;
		}
		case 11:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Drafts_Click));
			break;
		}
		case 12:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(TutorialCameraTools_OnTapped));
			break;
		}
		case 13:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(TutorialUndo_OnTapped));
			break;
		}
		case 14:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(TutorialGrabVideo_OnTapped));
			break;
		}
		case 15:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Grid_Click));
			break;
		}
		case 16:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(FocusMode_Clicked));
			break;
		}
		case 17:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Ghost_Click));
			break;
		}
		case 18:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Torch_Click));
			break;
		}
		case 19:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Cancel_Click));
			break;
		}
		case 20:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Next_Click));
			break;
		}
		case 21:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(PinUnPinCommandButton_OnClick));
			break;
		}
		}
		_contentLoaded = true;
	}

	[CompilerGenerated]
	[DebuggerHidden]
	private void _003C_003En__1(NavigatingCancelEventArgs e)
	{
		((Page)this).OnNavigatingFrom(e);
	}
}
