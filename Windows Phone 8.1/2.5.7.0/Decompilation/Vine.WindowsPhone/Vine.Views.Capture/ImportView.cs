using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using GrayscaleTransform_Windows;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Windows.ApplicationModel.Activation;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.Graphics.Imaging;
using Windows.Media.Core;
using Windows.Media.Editing;
using Windows.Media.MediaProperties;
using Windows.Media.Transcoding;
using Windows.Storage;
using Windows.Storage.AccessCache;
using Windows.Storage.Pickers;
using Windows.Storage.Streams;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Imaging;
using Windows.UI.Xaml.Navigation;

namespace Vine.Views.Capture;

public sealed class ImportView : BasePage, IProgress<double>, IComponentConnector
{
	private RecordingVineModel _state;

	private const long SixSecTicks = 60000000L;

	private bool _hasOpenedPicker;

	private bool _trimSliderThumbDown;

	private string _selectedVideoFileAccessToken;

	private MediaComposition _composition;

	private long _importDuration;

	private MediaClip _importClip;

	private bool _isAutoPlay = true;

	private double _trimSliderValue = 100.0;

	private double _scrubValue;

	private Visibility _scrubSliderVisibility = (Visibility)1;

	private ImageSource _scrubImg;

	private bool _isBusy;

	private double _progressValue;

	private Task _renderTask;

	private bool _isPaging;

	private ScrollViewer _scrollViewer;

	private double _lastOffset;

	private bool _isRenderingPreviewVideo;

	private int _listViewLayoutBusy;

	private int _loadingScrubThumb;

	private bool _erroringOut;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid LayoutRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ScrollViewer CropScrollViewer;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Slider TrimSlider;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Image ScrubImageControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MediaElement MediaElement;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public RandomAccessLoadingCollection<ImportViewModel> Items { get; set; }

	public bool IsAutoPlay
	{
		get
		{
			return _isAutoPlay;
		}
		set
		{
			SetProperty(ref _isAutoPlay, value, "IsAutoPlay");
		}
	}

	public double TrimSliderValue
	{
		get
		{
			return _trimSliderValue;
		}
		set
		{
			SetProperty(ref _trimSliderValue, value, "TrimSliderValue");
			if (_trimSliderThumbDown)
			{
				LoadScrubThumbAsync(atStart: false);
			}
		}
	}

	public double ScrubValue
	{
		get
		{
			return _scrubValue;
		}
		set
		{
			double scrubValue = _scrubValue;
			if (Math.Abs(value - scrubValue) > 5.0)
			{
				SetProperty(ref _scrubValue, value, "ScrubValue");
				double num = value / 100.0;
				double num2 = _scrollViewer.ScrollableWidth * num;
				_scrollViewer.ScrollToHorizontalOffset(num2);
			}
		}
	}

	public Visibility ScrubSliderVisibility
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return _scrubSliderVisibility;
		}
		set
		{
			//IL_0007: Unknown result type (might be due to invalid IL or missing references)
			SetProperty(ref _scrubSliderVisibility, value, "ScrubSliderVisibility");
		}
	}

	public ImageSource ScrubImg
	{
		get
		{
			return _scrubImg;
		}
		set
		{
			SetProperty(ref _scrubImg, value, "ScrubImg");
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
			NotifyOfPropertyChange(() => IsProgressZero);
		}
	}

	public bool IsProgressZero
	{
		get
		{
			if (ProgressValue == 0.0)
			{
				return IsBusy;
			}
			return false;
		}
	}

	public double ProgressValue
	{
		get
		{
			return _progressValue;
		}
		set
		{
			double progressValue = ProgressValue;
			SetProperty(ref _progressValue, value, "ProgressValue");
			if (progressValue == 0.0 && value > 0.0)
			{
				NotifyOfPropertyChange(() => IsProgressZero);
			}
		}
	}

	public ImportView()
	{
		//IL_0018: Unknown result type (might be due to invalid IL or missing references)
		InitializeComponent();
		_scrollViewer = ((FrameworkElement)(object)ListView).GetFirstLogicalChildByType<ScrollViewer>(applyTemplates: true);
		Items = new RandomAccessLoadingCollection<ImportViewModel>(new ImportViewModel(), 0);
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.Capture, "import"));
		try
		{
			await LoadStateAsync(e);
		}
		catch
		{
			UnknownError();
		}
	}

	private async Task LoadStateAsync(LoadStateEventArgs e)
	{
		IsBusy = true;
		if (_selectedVideoFileAccessToken == null)
		{
			_selectedVideoFileAccessToken = (string)e.NavigationParameter;
		}
		if (!_hasOpenedPicker && _selectedVideoFileAccessToken == null)
		{
			((UIElement)LayoutRoot).put_Opacity(0.0);
			await ((FrameworkElement)(object)this).LayoutUpdatedAsync();
			FileOpenPicker val = new FileOpenPicker();
			val.put_ViewMode((PickerViewMode)1);
			val.put_SuggestedStartLocation((PickerLocationId)6);
			val.FileTypeFilter.Clear();
			val.FileTypeFilter.Add(".mp4");
			App.ContinuationEventArgsChanged = (EventHandler<IContinuationActivatedEventArgs>)Delegate.Combine(App.ContinuationEventArgsChanged, new EventHandler<IContinuationActivatedEventArgs>(ContinuationEventArgsChanged));
			val.PickSingleFileAndContinue();
			_hasOpenedPicker = true;
		}
		else if (_selectedVideoFileAccessToken != null)
		{
			((UIElement)LayoutRoot).put_Opacity(1.0);
			await TaskManager.CleanUpTasks.WaitForEmptyQueue();
			ImportView importView = this;
			_ = importView._state;
			importView._state = await ApplicationSettings.Current.GetRecordingActiveVine();
			long activeVineDuration = ((_state != null) ? _state.Duration : 0);
			double num = (double)activeVineDuration / 60000000.0;
			double left = WindowWidth * num;
			((FrameworkElement)TrimSlider).put_Margin(new Thickness(left, 0.0, -46.0, 0.0));
			((Control)ListView).put_Padding(new Thickness(left, 0.0, 0.0, 0.0));
			_composition = new MediaComposition();
			try
			{
				StorageFile obj = await StorageApplicationPermissions.MostRecentlyUsedList.GetFileAsync(_selectedVideoFileAccessToken);
				importView = this;
				_ = importView._importClip;
				importView._importClip = await MediaClip.CreateFromFileAsync((IStorageFile)(object)obj);
			}
			catch
			{
				UnknownError();
				return;
			}
			_composition.Clips.Add(_importClip);
			_importDuration = _composition.Duration.Ticks - 100000;
			if (activeVineDuration + _importDuration < 60000000)
			{
				double num2 = ((FrameworkElement)TrimSlider).Margin.Left / WindowWidth * 60000000.0;
				double num3 = (double)_importDuration / (60000000.0 - num2);
				double num4 = 40.0 / WindowWidth;
				int num5 = (int)(num3 / num4);
				TrimSliderValue = (double)num5 * num4 * 100.0;
			}
			uint width = _importClip.GetVideoEncodingProperties().Width;
			uint height = _importClip.GetVideoEncodingProperties().Height;
			if (width > height)
			{
				Image scrubImageControl = ScrubImageControl;
				double num6;
				((FrameworkElement)MediaElement).put_Width(num6 = WindowWidth * ((double)width * 1.0 / (double)height));
				((FrameworkElement)scrubImageControl).put_Width(num6);
				Image scrubImageControl2 = ScrubImageControl;
				((FrameworkElement)MediaElement).put_Height(num6 = WindowWidth);
				((FrameworkElement)scrubImageControl2).put_Height(num6);
			}
			else
			{
				Image scrubImageControl3 = ScrubImageControl;
				double num6;
				((FrameworkElement)MediaElement).put_Width(num6 = WindowWidth);
				((FrameworkElement)scrubImageControl3).put_Width(num6);
				Image scrubImageControl4 = ScrubImageControl;
				((FrameworkElement)MediaElement).put_Height(num6 = WindowWidth * ((double)height * 1.0 / (double)width));
				((FrameworkElement)scrubImageControl4).put_Height(num6);
			}
			Items.ItemsLoadStateChanged -= ItemsOnItemsLoadStateChanged;
			Items.ItemsLoadStateChanged += ItemsOnItemsLoadStateChanged;
			Items.Count = (int)(TimeSpan.FromTicks(_importDuration).TotalSeconds * 1.0);
			WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)((FrameworkElement)ListView).remove_LayoutUpdated, (EventHandler<object>)ListBoxOnLayoutUpdated);
			ListView listView = ListView;
			WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)((FrameworkElement)listView).add_LayoutUpdated, (Action<EventRegistrationToken>)((FrameworkElement)listView).remove_LayoutUpdated, (EventHandler<object>)ListBoxOnLayoutUpdated);
			await Task.Delay(100);
			ScrubSliderVisibility = (Visibility)(_scrollViewer.ScrollableWidth == 0.0);
			IsBusy = false;
		}
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		//IL_001a: Invalid comparison between Unknown and I4
		e.put_Cancel(_renderTask != null && (int)((IAsyncInfo)_renderTask.AsAsyncAction()).Status == 0);
		((Page)this).OnNavigatingFrom(e);
	}

	private async void ItemsOnItemsLoadStateChanged(object sender, int indexAdded)
	{
		if (!_isPaging)
		{
			_isPaging = true;
			await OnItemsLoadStateChanged();
			_isPaging = false;
		}
	}

	private async Task OnItemsLoadStateChanged()
	{
		Dictionary<int, ImportViewModel> dict = new Dictionary<int, ImportViewModel>();
		await DispatcherEx.InvokeBackground(async delegate
		{
			List<int> indexToLoad = Items.TakeTopTenLoading();
			List<TimeSpan> list = new List<TimeSpan>();
			for (int i = 0; i < indexToLoad.Count; i++)
			{
				list.Add(TimeSpan.FromTicks((long)((double)_importDuration * ((double)indexToLoad[i] / (double)Items.Count))));
			}
			IReadOnlyList<ImageStream> thumbs;
			try
			{
				thumbs = await _composition.GetThumbnailsAsync((IEnumerable<TimeSpan>)list, 128, 128, (VideoFramePrecision)1);
			}
			catch
			{
				DispatcherEx.BeginInvoke(UnknownError);
				return;
			}
			for (int j = 0; j < indexToLoad.Count; j++)
			{
				ImportViewModel value = new ImportViewModel
				{
					Stream = (IRandomAccessStream)(object)thumbs[j]
				};
				dict[indexToLoad[j]] = value;
			}
			List<ImportViewModel> list2 = Items.TakeTopTenUnloading();
			for (int k = 0; k < list2.Count; k++)
			{
				ImportViewModel importViewModel2 = list2[k];
				if (importViewModel2.Stream != null)
				{
					((IDisposable)importViewModel2.Stream).Dispose();
					importViewModel2.Stream = null;
				}
			}
			GC.Collect();
			GC.WaitForPendingFinalizers();
			Items.PopLoadingItems(indexToLoad, list2);
		});
		foreach (int key in dict.Keys)
		{
			ImportViewModel importViewModel = dict[key];
			BitmapImage val = new BitmapImage();
			val.put_DecodePixelHeight(128);
			val.put_DecodePixelWidth(128);
			((BitmapSource)val).SetSource(importViewModel.Stream);
			importViewModel.Thumb = (ImageSource)(object)val;
			Items[key] = importViewModel;
		}
		if ((Items.ItemsLoading.Any() || Items.ItemsUnLoading.Any()) && !_erroringOut)
		{
			await OnItemsLoadStateChanged();
		}
	}

	private async void ListBoxOnLayoutUpdated(object sender, object e)
	{
		_scrollViewer.HorizontalOffset.ToStringInvariantCulture();
		if (_listViewLayoutBusy > 0)
		{
			_listViewLayoutBusy++;
			return;
		}
		_listViewLayoutBusy = 1;
		await ListBoxOnLayoutUpdatedAsync();
		_listViewLayoutBusy = 0;
	}

	private async Task ListBoxOnLayoutUpdatedAsync()
	{
		if (_lastOffset != _scrollViewer.HorizontalOffset)
		{
			_lastOffset = _scrollViewer.HorizontalOffset;
			await LoadScrubThumbAsync(atStart: true);
			if (_lastOffset == _scrollViewer.HorizontalOffset && IsAutoPlay && !_trimSliderThumbDown)
			{
				_listViewLayoutBusy++;
			}
		}
		else if (IsAutoPlay && !_trimSliderThumbDown && _loadingScrubThumb == 0 && !_isRenderingPreviewVideo)
		{
			RenderVideoPreview();
		}
		if (_listViewLayoutBusy >= 2)
		{
			_listViewLayoutBusy = 1;
			await ListBoxOnLayoutUpdatedAsync();
		}
	}

	private void RenderVideoPreview()
	{
		_isRenderingPreviewVideo = true;
		try
		{
			MediaStreamSource mediaStreamSource = VideoPreviewComposition().GenerateMediaStreamSource();
			MediaElement.SetMediaStreamSource((IMediaSource)(object)mediaStreamSource);
			((UIElement)MediaElement).put_Opacity(1.0);
		}
		catch
		{
			UnknownError();
		}
	}

	private MediaComposition VideoPreviewComposition()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_010f: Unknown result type (might be due to invalid IL or missing references)
		//IL_011c: Expected O, but got Unknown
		MediaComposition val = new MediaComposition();
		MediaClip val2 = _importClip.Clone();
		double num = ((!(_scrollViewer.ScrollableWidth - ((Control)ListView).Padding.Right <= 0.0)) ? (_scrollViewer.HorizontalOffset / (_scrollViewer.ScrollableWidth - ((Control)ListView).Padding.Right)) : 0.0);
		double num2 = ((FrameworkElement)TrimSlider).Margin.Left / base.WindowWidth * 60000000.0;
		long val3 = (long)(num * ((double)(_importDuration - 60000000) + num2));
		long num3 = (long)(num * ((double)(_importDuration - 60000000) + num2) + TrimSliderValue / 100.0 * (60000000.0 - num2));
		val2.put_TrimTimeFromStart(TimeSpan.FromTicks(Math.Max(val3, 0L)));
		val2.put_TrimTimeFromEnd(TimeSpan.FromTicks(Math.Max(_importDuration - num3, 0L)));
		val.Clips.Add(val2);
		return val;
	}

	private async Task LoadScrubThumbAsync(bool atStart)
	{
		if (_loadingScrubThumb > 0)
		{
			_loadingScrubThumb++;
			return;
		}
		_loadingScrubThumb = 1;
		double num = _scrollViewer.HorizontalOffset / (_scrollViewer.ScrollableWidth - ((Control)ListView).Padding.Right);
		double num2 = ((FrameworkElement)TrimSlider).Margin.Left / WindowWidth * 60000000.0;
		long value;
		if (atStart)
		{
			value = (long)(num * ((double)(_importDuration - 60000000) + num2));
			_scrubValue = num * 100.0;
			NotifyOfPropertyChange(() => ScrubValue);
		}
		else
		{
			value = (long)(num * ((double)(_importDuration - 60000000) + num2) + TrimSliderValue / 100.0 * (60000000.0 - num2));
		}
		ImageStream thumbStream;
		try
		{
			thumbStream = await _composition.GetThumbnailAsync(TimeSpan.FromTicks(value), 0, 0, (VideoFramePrecision)0);
		}
		catch
		{
			_loadingScrubThumb = 0;
			return;
		}
		BitmapImage val = new BitmapImage();
		((BitmapSource)val).SetSource((IRandomAccessStream)(object)thumbStream);
		_isRenderingPreviewVideo = false;
		MediaElement.put_Source((Uri)null);
		((UIElement)MediaElement).put_Opacity(0.0);
		ScrubImg = (ImageSource)(object)val;
		if (_loadingScrubThumb > 1)
		{
			_loadingScrubThumb = 0;
			await LoadScrubThumbAsync(atStart);
		}
		else
		{
			_loadingScrubThumb = 0;
		}
	}

	private void ContinuationEventArgsChanged(object sender, IContinuationActivatedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Invalid comparison between Unknown and I4
		//IL_0036: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		if ((int)((IActivatedEventArgs)e).Kind == 1002)
		{
			if (App.ContinuationEventArgsChanged != null)
			{
				App.ContinuationEventArgsChanged = (EventHandler<IContinuationActivatedEventArgs>)Delegate.Remove(App.ContinuationEventArgsChanged, new EventHandler<IContinuationActivatedEventArgs>(ContinuationEventArgsChanged));
			}
			FileOpenPickerContinuationEventArgs e2 = (FileOpenPickerContinuationEventArgs)e;
			if (e2.Files.Any())
			{
				_selectedVideoFileAccessToken = StorageApplicationPermissions.MostRecentlyUsedList.Add((IStorageItem)(object)e2.Files[0]);
				LoadState();
			}
			else
			{
				App.RootFrame.GoBack();
			}
		}
	}

	private void SliderOnThumbDown(object sender, PointerRoutedEventArgs e)
	{
		_trimSliderThumbDown = true;
	}

	private async void SliderOnThumbUp(object sender, PointerRoutedEventArgs e)
	{
		_trimSliderThumbDown = false;
		Thickness padding = ((Control)ListView).Padding;
		double num = TrimSliderValue / 100.0;
		padding.Right = WindowWidth - (padding.Left + (WindowWidth - padding.Left) * num);
		((Control)ListView).put_Padding(padding);
		if (IsAutoPlay)
		{
			RenderVideoPreview();
		}
	}

	private void Viewport_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		IsAutoPlay = !IsAutoPlay;
		if (!IsAutoPlay)
		{
			MediaElement.Pause();
		}
		else
		{
			MediaElement.Play();
		}
	}

	private async void Save_Click(object sender, RoutedEventArgs e)
	{
		IsBusy = true;
		MediaElement.put_Source((Uri)null);
		MediaElement.Stop();
		try
		{
			StorageFile inputFile = await StorageApplicationPermissions.MostRecentlyUsedList.GetFileAsync(_selectedVideoFileAccessToken);
			StorageFile outputFile = await _state.NewVideoRenderFile();
			MediaClip val = VideoPreviewComposition().Clips[0];
			int width;
			int num = (width = (int)val.GetVideoEncodingProperties().Width);
			int height;
			int num2 = (height = (int)val.GetVideoEncodingProperties().Height);
			int num3;
			int num4;
			int num5;
			int num6;
			if (height > width)
			{
				num3 = 0;
				num4 = num;
				num5 = (int)(CropScrollViewer.VerticalOffset / ((FrameworkElement)MediaElement).ActualHeight * (double)height);
				num6 = num + num5;
				num2 = num;
			}
			else
			{
				num5 = 0;
				num6 = num2;
				num3 = (int)(CropScrollViewer.HorizontalOffset / ((FrameworkElement)MediaElement).ActualWidth * (double)width);
				num4 = num2 + num3;
				num = num2;
			}
			VineCapture vineCapture = new VineCapture((SurfaceImageSource)null, (SurfaceImageSource)null, false, false, false, false, !OSVersionHelper.IsWindows10, width, height, num, num2, num3, num5, num4, num6);
			EventHandler<object> readyForInitHandler = null;
			readyForInitHandler = delegate
			{
				WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)vineCapture.remove_ReadyForInit, readyForInitHandler);
				DispatcherEx.BeginInvoke(delegate
				{
					vineCapture.Initialize();
				});
			};
			VineCapture val2 = vineCapture;
			WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)val2.add_ReadyForInit, (Action<EventRegistrationToken>)val2.remove_ReadyForInit, readyForInitHandler);
			PropertySet val3 = new PropertySet();
			((IDictionary<string, object>)val3).Add("CaptureInterface", (object)vineCapture);
			MediaTranscoder val4 = new MediaTranscoder();
			val4.put_TrimStartTime(val.TrimTimeFromStart);
			val4.put_TrimStopTime(val.TrimTimeFromStart + val.TrimmedDuration);
			MediaTranscoder val5 = val4;
			val5.AddVideoEffect("GrayscaleTransform.GrayscaleEffect", true, (IPropertySet)(object)val3);
			MediaEncodingProfile val6 = MediaEncodingProfile.CreateMp4((VideoEncodingQuality)6);
			if (val6 == null || val6.Video == null)
			{
				UnknownError();
				return;
			}
			val6.Video.put_Height(480u);
			val6.Video.put_Width(480u);
			PrepareTranscodeResult val7 = await val5.PrepareFileTranscodeAsync((IStorageFile)(object)inputFile, (IStorageFile)(object)outputFile, val6);
			if (val7.CanTranscode)
			{
				_renderTask = val7.TranscodeAsync().AsTask(this);
				await _renderTask;
				MediaComposition composition = new MediaComposition();
				MediaClip item = await MediaClip.CreateFromFileAsync((IStorageFile)(object)outputFile);
				composition.Clips.Add(item);
				long duration = composition.Duration.Ticks - TimeSpan.FromMilliseconds(100.0).Ticks;
				StorageFile ghostFile = await _state.NewGhostFile();
				ImageStream ghostStream = await composition.GetThumbnailAsync(TimeSpan.FromTicks(duration), 480, 480, (VideoFramePrecision)0);
				IRandomAccessStream fileRandStream = await ghostFile.OpenAsync((FileAccessMode)1);
				using (Stream fileStream = ((IOutputStream)(object)fileRandStream).AsStreamForWrite())
				{
					await ((IInputStream)(object)ghostStream).AsStreamForRead().CopyToAsync(fileStream);
				}
				((IDisposable)fileRandStream).Dispose();
				_state.Clips.Add(new RecordingClipModel
				{
					EditStartTime = 0L,
					EditEndTime = duration,
					FileStartTime = 0L,
					FileEndTime = duration,
					GhostFilePath = ghostFile.Path,
					VideoFileDuration = duration,
					VideoFilePath = outputFile.Path
				});
				App.RootFrame.GoBack();
			}
			else
			{
				UnknownError();
			}
		}
		catch
		{
			UnknownError();
		}
	}

	public void Report(double value)
	{
		DispatcherEx.BeginInvoke(delegate
		{
			ProgressValue = value;
		});
	}

	private async void UnknownError()
	{
		if (!_erroringOut)
		{
			_erroringOut = true;
			App.RootFrame.GoBack();
			await new MessageDialog(ResourceHelper.GetString("error_import_video_unknown_error"), ResourceHelper.GetString("vine")).ShowAsync();
		}
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/Capture/ImportView.xaml"), (ComponentResourceLocation)0);
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			CropScrollViewer = (ScrollViewer)((FrameworkElement)this).FindName("CropScrollViewer");
			ListView = (ListView)((FrameworkElement)this).FindName("ListView");
			TrimSlider = (Slider)((FrameworkElement)this).FindName("TrimSlider");
			ScrubImageControl = (Image)((FrameworkElement)this).FindName("ScrubImageControl");
			MediaElement = (MediaElement)((FrameworkElement)this).FindName("MediaElement");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_001a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Expected O, but got Unknown
		//IL_0041: Unknown result type (might be due to invalid IL or missing references)
		//IL_004b: Expected O, but got Unknown
		//IL_0051: Unknown result type (might be due to invalid IL or missing references)
		//IL_0057: Expected O, but got Unknown
		//IL_0078: Unknown result type (might be due to invalid IL or missing references)
		//IL_0082: Expected O, but got Unknown
		//IL_0083: Unknown result type (might be due to invalid IL or missing references)
		//IL_0089: Expected O, but got Unknown
		//IL_00aa: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b4: Expected O, but got Unknown
		//IL_00b7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bd: Expected O, but got Unknown
		//IL_00de: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e8: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			ButtonBase val2 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(Save_Click));
			break;
		}
		case 2:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val.add_PointerCaptureLost, (Action<EventRegistrationToken>)val.remove_PointerCaptureLost, new PointerEventHandler(SliderOnThumbUp));
			val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val.add_PointerEntered, (Action<EventRegistrationToken>)val.remove_PointerEntered, new PointerEventHandler(SliderOnThumbDown));
			break;
		}
		case 3:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(Viewport_OnTapped));
			break;
		}
		}
		_contentLoaded = true;
	}
}
