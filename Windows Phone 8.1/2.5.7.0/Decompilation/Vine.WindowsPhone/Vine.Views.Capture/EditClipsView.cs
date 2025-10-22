using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Windows.Graphics.Imaging;
using Windows.Media.Core;
using Windows.Media.Editing;
using Windows.Storage;
using Windows.Storage.Streams;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Imaging;

namespace Vine.Views.Capture;

public sealed class EditClipsView : BasePage, IComponentConnector
{
	private RecordingVineModel _state;

	private Dictionary<string, MediaClip> _videoToClips = new Dictionary<string, MediaClip>();

	public const int NumberOfThumbnails = 9;

	private bool _isBusy;

	private const double ThumbWidth = 9.5;

	private EditClipsViewModel _selected;

	private int _ignoreValueChanged;

	private bool _sliderThumbDown;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private BasePage PageRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MediaElement MediaElement;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Image ThumbImage;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Slider RightSlider;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Slider LeftSlider;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private CommandBar AppBar;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton OkButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public ObservableCollection<EditClipsViewModel> Items { get; set; }

	public ObservableCollection<BitmapImage> TrimThumbnails { get; set; }

	public double TrimThumbnailWidth => base.WindowWidth / 9.0;

	public bool IsFinishedLoading { get; set; }

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

	public double TrimHighlightRectX => ((RangeBase)LeftSlider).Value / 100.0 * (base.WindowWidth - 9.5 - 40.0) + 9.5;

	public double TrimHighlightRectWidth => Math.Max(0.0, (100.0 - ((RangeBase)RightSlider).Value - ((RangeBase)LeftSlider).Value) / 100.0 * (base.WindowWidth - 9.5 - 40.0) + 40.0 - 9.5);

	public EditClipsViewModel Selected
	{
		get
		{
			return _selected;
		}
		set
		{
			if (_selected != value)
			{
				if (_selected != null)
				{
					_selected.IsActive = false;
				}
				SetProperty(ref _selected, value, "Selected");
				if (value != null)
				{
					value.IsActive = true;
					LoadTrimThumbnailsAsync();
				}
				NotifyOfPropertyChange(() => HasSelection);
				NotifyOfPropertyChange(() => LeftSliderValue);
				NotifyOfPropertyChange(() => RightSliderValue);
				NotifyOfPropertyChange(() => TrimHighlightRectX);
				NotifyOfPropertyChange(() => TrimHighlightRectWidth);
				if (value == null)
				{
					PlayFullVideo();
				}
			}
		}
	}

	public bool HasSelection
	{
		get
		{
			//IL_000e: Unknown result type (might be due to invalid IL or missing references)
			//IL_0014: Invalid comparison between Unknown and I4
			if (Selected != null)
			{
				return (int)((ListViewBase)ListView).ReorderMode == 0;
			}
			return false;
		}
	}

	public bool HasMoreThanOneClip => Items.Count > 1;

	public double RightSliderValue
	{
		get
		{
			if (Selected == null)
			{
				return 0.0;
			}
			EditClipsViewModel selected = Selected;
			return 100.0 - (double)selected.ClipModel.EditEndTime / (double)(selected.ClipModel.FileEndTime - selected.ClipModel.FileStartTime) * 100.0;
		}
		set
		{
			if (Selected != null)
			{
				EditClipsViewModel selected = Selected;
				selected.ClipModel.EditEndTime = (long)((1.0 - value / 100.0) * (double)(selected.ClipModel.FileEndTime - selected.ClipModel.FileStartTime));
				OnPropertyChanged("RightSliderValue");
				SliderValueChanged(selected, selected.ClipModel.FileStartTime + selected.ClipModel.EditEndTime, isLeftSlider: false);
				NotifyOfPropertyChange(() => TrimHighlightRectWidth);
			}
		}
	}

	public double LeftSliderValue
	{
		get
		{
			if (Selected == null)
			{
				return 0.0;
			}
			EditClipsViewModel selected = Selected;
			return (double)selected.ClipModel.EditStartTime / (double)selected.ClipModel.EditEndTime * 100.0;
		}
		set
		{
			if (Selected != null)
			{
				EditClipsViewModel selected = Selected;
				selected.ClipModel.EditStartTime = (long)(value / 100.0 * (double)selected.ClipModel.EditEndTime);
				OnPropertyChanged("LeftSliderValue");
				SliderValueChanged(selected, selected.ClipModel.FileStartTime + selected.ClipModel.EditStartTime, isLeftSlider: true);
				NotifyOfPropertyChange(() => TrimHighlightRectX);
				NotifyOfPropertyChange(() => TrimHighlightRectWidth);
			}
		}
	}

	public EditClipsView()
	{
		InitializeComponent();
		Items = new ObservableCollection<EditClipsViewModel>();
		TrimThumbnails = new ObservableCollection<BitmapImage>();
		base.NavigationHelper.GoBackCommand = new RelayCommand(GoBackCmd);
		OkButton.put_Label(ResourceHelper.GetString("UniversalOkay"));
	}

	private async Task SliderValueChanged(EditClipsViewModel vm, long thumbnailTicks, bool isLeftSlider)
	{
		if (!_sliderThumbDown || _ignoreValueChanged > 0)
		{
			if (_ignoreValueChanged > 0)
			{
				_ignoreValueChanged++;
			}
			return;
		}
		_ignoreValueChanged = 1;
		ImageStream thumb = null;
		try
		{
			thumbnailTicks = Math.Min(thumbnailTicks, vm.Composition.Duration.Ticks - 100);
			thumb = await vm.Composition.GetThumbnailAsync(TimeSpan.FromTicks(thumbnailTicks), 480, 480, (VideoFramePrecision)0);
		}
		catch
		{
		}
		if (thumb != null)
		{
			BitmapImage img = new BitmapImage();
			await ((BitmapSource)img).SetSourceAsync((IRandomAccessStream)(object)thumb);
			ThumbImage.put_Source((ImageSource)(object)img);
			await ((FrameworkElement)(object)ThumbImage).LayoutUpdatedAsync();
			if ((int)MediaElement.CurrentState != 4)
			{
				MediaElement.Pause();
			}
		}
		if (_sliderThumbDown && _ignoreValueChanged > 1)
		{
			_ignoreValueChanged = 0;
			thumbnailTicks = ((!isLeftSlider) ? (vm.ClipModel.FileStartTime + vm.ClipModel.EditEndTime) : (vm.ClipModel.FileStartTime + vm.ClipModel.EditStartTime));
			await SliderValueChanged(vm, thumbnailTicks, isLeftSlider);
		}
		_ignoreValueChanged = 0;
	}

	private async void SliderOnThumbUp(object sender, PointerRoutedEventArgs e)
	{
		_sliderThumbDown = false;
		((RangeBase)RightSlider).put_Value(Math.Min(((RangeBase)RightSlider).Value, ((RangeBase)LeftSlider).Maximum - ((RangeBase)LeftSlider).Value - 1.0));
		((RangeBase)LeftSlider).put_Value(Math.Min(((RangeBase)LeftSlider).Value, ((RangeBase)RightSlider).Maximum - ((RangeBase)RightSlider).Value - 1.0));
		while (_ignoreValueChanged > 0)
		{
			await Task.Delay(100);
		}
		await ResumePlayingSelection();
	}

	private async void SliderGrid_ManipulationCompleted(object sender, ManipulationCompletedRoutedEventArgs e)
	{
		await Task.Delay(TimeSpan.FromMilliseconds(200.0));
		_sliderThumbDown = false;
		await ResumePlayingSelection();
	}

	private async Task ResumePlayingSelection()
	{
		PlaySelection();
		Selected.Thumb = ThumbImage.Source;
		await MediaElement.WhenPlayingAsync();
		ThumbImage.put_Source((ImageSource)null);
	}

	private void SliderOnThumbDown(object sender, PointerRoutedEventArgs e)
	{
		_sliderThumbDown = true;
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		if (IsFinishedLoading)
		{
			return;
		}
		IsBusy = true;
		_state = Serialization.DeepCopy<RecordingVineModel>(await ApplicationSettings.Current.GetRecordingActiveVine());
		foreach (RecordingClipModel clipModel in _state.Clips)
		{
			MediaClip mediaClip;
			if (!_videoToClips.ContainsKey(clipModel.VideoFilePath))
			{
				mediaClip = await MediaClip.CreateFromFileAsync((IStorageFile)(object)(await StorageFile.GetFileFromPathAsync(clipModel.VideoFilePath)));
				_videoToClips[clipModel.VideoFilePath] = mediaClip;
			}
			else
			{
				mediaClip = _videoToClips[clipModel.VideoFilePath].Clone();
			}
			MediaComposition composition = new MediaComposition();
			composition.Clips.Add(mediaClip);
			long value = Math.Min(composition.Duration.Ticks - 100, clipModel.FileStartTime + clipModel.EditStartTime);
			ImageStream sourceAsync = await composition.GetThumbnailAsync(TimeSpan.FromTicks(value), 200, 200, (VideoFramePrecision)0);
			BitmapImage img = new BitmapImage();
			await ((BitmapSource)img).SetSourceAsync((IRandomAccessStream)(object)sourceAsync);
			EditClipsViewModel item = new EditClipsViewModel
			{
				Thumb = (ImageSource)(object)img,
				MediaClip = mediaClip,
				Composition = composition,
				ClipModel = clipModel
			};
			Items.Add(item);
		}
		PlayFullVideo();
		IsBusy = false;
		IsFinishedLoading = true;
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
	}

	private async void GoBackCmd()
	{
		IUICommand val = await new MessageDialog("Save?", ResourceHelper.GetString("vine"))
		{
			Commands = { (IUICommand)new UICommand("save", (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand("discard", (UICommandInvokedHandler)null, (object)1) }
		}.ShowAsync();
		if (val != null)
		{
			if ((int)val.Id == 0)
			{
				await SaveChanges();
			}
			if (_state.Duration < 20000000)
			{
				App.RootFrame.GoBackTo(CaptureViewHelper.GetCaptureView());
			}
			else
			{
				App.RootFrame.GoBack();
			}
		}
	}

	private void PlayFullVideo()
	{
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_0023: Expected O, but got Unknown
		//IL_005d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_0097: Unknown result type (might be due to invalid IL or missing references)
		//IL_009c: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a8: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b9: Expected O, but got Unknown
		//IL_0126: Unknown result type (might be due to invalid IL or missing references)
		//IL_0130: Expected O, but got Unknown
		WindowsRuntimeMarshal.RemoveEventHandler<TimelineMarkerRoutedEventHandler>((Action<EventRegistrationToken>)MediaElement.remove_MarkerReached, new TimelineMarkerRoutedEventHandler(MediaElementOnMarkerReached));
		((ICollection<TimelineMarker>)MediaElement.Markers).Clear();
		Items.Apply(delegate(EditClipsViewModel x)
		{
			x.IsPlaying = false;
		});
		MediaComposition val = new MediaComposition();
		long num = 0L;
		for (int num2 = 0; num2 < Items.Count; num2++)
		{
			EditClipsViewModel editClipsViewModel = Items[num2];
			MediaClip item = editClipsViewModel.ToMediaClip();
			val.Clips.Add(item);
			TimelineMarkerCollection markers = MediaElement.Markers;
			TimelineMarker val2 = new TimelineMarker();
			val2.put_Time(TimeSpan.FromTicks(num));
			val2.put_Type(num2.ToStringInvariantCulture());
			((ICollection<TimelineMarker>)markers).Add(val2);
			num += editClipsViewModel.ClipModel.EditEndTime - editClipsViewModel.ClipModel.EditStartTime;
		}
		try
		{
			MediaStreamSource mediaStreamSource = val.GenerateMediaStreamSource();
			MediaElement.SetMediaStreamSource((IMediaSource)(object)mediaStreamSource);
			MediaElement mediaElement = MediaElement;
			WindowsRuntimeMarshal.AddEventHandler((Func<TimelineMarkerRoutedEventHandler, EventRegistrationToken>)mediaElement.add_MarkerReached, (Action<EventRegistrationToken>)mediaElement.remove_MarkerReached, new TimelineMarkerRoutedEventHandler(MediaElementOnMarkerReached));
		}
		catch (Exception)
		{
		}
	}

	private void MediaElementOnMarkerReached(object sender, TimelineMarkerRoutedEventArgs e)
	{
		//IL_001a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Invalid comparison between Unknown and I4
		if (int.TryParse(e.Marker.Type, out var result) && (int)((ListViewBase)ListView).ReorderMode != 1)
		{
			if (result > 0)
			{
				Items[result - 1].IsPlaying = false;
			}
			if (result == 0 && Items.Count > 1)
			{
				Items.Last().IsPlaying = false;
			}
			Items[result].IsPlaying = true;
			((ListViewBase)ListView).ScrollIntoView((object)Items[result], (ScrollIntoViewAlignment)1);
		}
	}

	private void PlaySelection()
	{
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_0023: Expected O, but got Unknown
		//IL_0045: Unknown result type (might be due to invalid IL or missing references)
		//IL_004b: Expected O, but got Unknown
		WindowsRuntimeMarshal.RemoveEventHandler<TimelineMarkerRoutedEventHandler>((Action<EventRegistrationToken>)MediaElement.remove_MarkerReached, new TimelineMarkerRoutedEventHandler(MediaElementOnMarkerReached));
		((ICollection<TimelineMarker>)MediaElement.Markers).Clear();
		EditClipsViewModel selected = Selected;
		if (selected == null)
		{
			return;
		}
		MediaClip item = selected.ToMediaClip();
		MediaComposition val = new MediaComposition();
		val.Clips.Add(item);
		Items.Apply(delegate(EditClipsViewModel x)
		{
			x.IsPlaying = false;
		});
		selected.IsPlaying = true;
		try
		{
			MediaStreamSource mediaStreamSource = val.GenerateMediaStreamSource();
			MediaElement.SetMediaStreamSource((IMediaSource)(object)mediaStreamSource);
		}
		catch (Exception)
		{
		}
	}

	private void ListViewBase_OnItemClick(object sender, ItemClickEventArgs e)
	{
		EditClipsViewModel editClipsViewModel = (EditClipsViewModel)e.ClickedItem;
		if (editClipsViewModel == Selected)
		{
			Selected = null;
			return;
		}
		Selected = editClipsViewModel;
		PlaySelection();
	}

	private void ClipOnHolding(object sender, HoldingRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		EditClipsViewModel selected = (EditClipsViewModel)((FrameworkElement)sender).DataContext;
		Selected = selected;
		((ListViewBase)ListView).put_ReorderMode((ListViewReorderMode)1);
		((UIElement)AppBar).put_Visibility((Visibility)0);
		NotifyOfPropertyChange(() => HasSelection);
		Items.Apply(delegate(EditClipsViewModel x)
		{
			x.IsPlaying = true;
		});
	}

	private void Ok_Click(object sender, RoutedEventArgs e)
	{
		((ListViewBase)ListView).put_ReorderMode((ListViewReorderMode)0);
		((UIElement)AppBar).put_Visibility((Visibility)1);
		NotifyOfPropertyChange(() => HasSelection);
		Items.Apply(delegate(EditClipsViewModel x)
		{
			x.IsPlaying = false;
		});
		if (Selected != null)
		{
			Selected.IsPlaying = true;
		}
	}

	private void Delete_Click(object sender, RoutedEventArgs e)
	{
		MediaElement.Pause();
		EditClipsViewModel selected = Selected;
		Selected = null;
		Items.Remove(selected);
		NotifyOfPropertyChange(() => HasMoreThanOneClip);
		PlayFullVideo();
	}

	private void Duplicate_Click(object sender, RoutedEventArgs e)
	{
		//IL_00b9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bf: Expected O, but got Unknown
		//IL_0079: Unknown result type (might be due to invalid IL or missing references)
		if (Items.Sum((EditClipsViewModel x) => x.ClipModel.EditEndTime - x.ClipModel.EditStartTime) + Selected.ClipModel.EditEndTime - Selected.ClipModel.EditStartTime > TimeSpan.FromSeconds(6.0).Ticks)
		{
			new MessageDialog(ResourceHelper.GetString("error_duplicate_too_long"), ResourceHelper.GetString("vine")).ShowAsync();
			return;
		}
		int num = Items.IndexOf(Selected);
		RecordingClipModel clipModel = Serialization.DeepCopy<RecordingClipModel>(Selected.ClipModel);
		MediaClip val = Selected.MediaClip.Clone();
		MediaComposition val2 = new MediaComposition();
		val2.Clips.Add(val);
		EditClipsViewModel item = new EditClipsViewModel
		{
			Thumb = Selected.Thumb,
			MediaClip = val,
			Composition = val2,
			ClipModel = clipModel
		};
		Items.Insert(num + 1, item);
	}

	private async void Save_Click(object sender, RoutedEventArgs e)
	{
		await SaveChanges();
		if (_state.Duration < 20000000)
		{
			App.RootFrame.GoBackTo(CaptureViewHelper.GetCaptureView());
		}
		else
		{
			App.RootFrame.GoBack();
		}
	}

	private async Task SaveChanges()
	{
		IsBusy = true;
		_state.Clips = Items.Select((EditClipsViewModel x) => x.ClipModel).ToList();
		PreviewCaptureView.InvalidatePageState = true;
		await ApplicationSettings.Current.SetRecordingActiveVine(_state);
		IsBusy = false;
	}

	private void MediaElement_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000c: Invalid comparison between Unknown and I4
		if ((int)MediaElement.CurrentState == 4)
		{
			if (Selected != null)
			{
				PlaySelection();
			}
			else
			{
				PlayFullVideo();
			}
		}
		else
		{
			MediaElement.Pause();
		}
	}

	private void ListView_OnPointerEntered(object sender, PointerRoutedEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000c: Invalid comparison between Unknown and I4
		if ((int)MediaElement.CurrentState != 4)
		{
			MediaElement.Pause();
		}
	}

	private void SliderGrid_ManipulationDelta(object sender, ManipulationDeltaRoutedEventArgs e)
	{
		//IL_0081: Unknown result type (might be due to invalid IL or missing references)
		//IL_0086: Unknown result type (might be due to invalid IL or missing references)
		//IL_0116: Unknown result type (might be due to invalid IL or missing references)
		//IL_011b: Unknown result type (might be due to invalid IL or missing references)
		//IL_014a: Unknown result type (might be due to invalid IL or missing references)
		//IL_014f: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d6: Unknown result type (might be due to invalid IL or missing references)
		if (((RangeBase)LeftSlider).Value / 100.0 * base.WindowWidth < e.Position.X && (100.0 - ((RangeBase)RightSlider).Value) / 100.0 * base.WindowWidth > e.Position.X && (((RangeBase)RightSlider).Value > 0.0 || (e.Delta.Translation.X < 0.0 && ((RangeBase)RightSlider).Value == 0.0)) && (((RangeBase)LeftSlider).Value > 0.0 || (e.Delta.Translation.X > 0.0 && ((RangeBase)LeftSlider).Value == 0.0)))
		{
			((RangeBase)LeftSlider).put_Value(((RangeBase)LeftSlider).Value + e.Delta.Translation.X / 4.0);
			((RangeBase)RightSlider).put_Value(((RangeBase)RightSlider).Value - e.Delta.Translation.X / 4.0);
		}
	}

	private void ListView_OnDragItemsStarting(object sender, DragItemsStartingEventArgs e)
	{
		if (e.Items.FirstOrDefault() is EditClipsViewModel selected)
		{
			Selected = selected;
			NotifyOfPropertyChange(() => HasSelection);
		}
	}

	private async Task LoadTrimThumbnailsAsync()
	{
		if (!HasSelection || Selected == null)
		{
			return;
		}
		TrimThumbnails.Clear();
		MediaClip val = Selected.ToMediaClip();
		long thumbInterval = val.TrimmedDuration.Ticks / 8;
		List<TimeSpan> list = (from i in Enumerable.Range(0, 9)
			select TimeSpan.FromTicks(thumbInterval * i)).ToList();
		foreach (ImageStream item in await new MediaComposition
		{
			Clips = { val }
		}.GetThumbnailsAsync((IEnumerable<TimeSpan>)list, 128, 128, (VideoFramePrecision)0))
		{
			BitmapImage val2 = new BitmapImage();
			val2.put_DecodePixelHeight(64);
			val2.put_DecodePixelWidth(64);
			BitmapImage image = val2;
			await ((BitmapSource)image).SetSourceAsync((IRandomAccessStream)(object)item);
			TrimThumbnails.Add(image);
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/Capture/EditClipsView.xaml"), (ComponentResourceLocation)0);
			PageRoot = (BasePage)((FrameworkElement)this).FindName("PageRoot");
			MediaElement = (MediaElement)((FrameworkElement)this).FindName("MediaElement");
			ThumbImage = (Image)((FrameworkElement)this).FindName("ThumbImage");
			ListView = (ListView)((FrameworkElement)this).FindName("ListView");
			RightSlider = (Slider)((FrameworkElement)this).FindName("RightSlider");
			LeftSlider = (Slider)((FrameworkElement)this).FindName("LeftSlider");
			AppBar = (CommandBar)((FrameworkElement)this).FindName("AppBar");
			OkButton = (AppBarButton)((FrameworkElement)this).FindName("OkButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_002e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0034: Expected O, but got Unknown
		//IL_0055: Unknown result type (might be due to invalid IL or missing references)
		//IL_005f: Expected O, but got Unknown
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		//IL_006b: Expected O, but got Unknown
		//IL_008c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0096: Expected O, but got Unknown
		//IL_009c: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a2: Expected O, but got Unknown
		//IL_00c3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cd: Expected O, but got Unknown
		//IL_00ce: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d4: Expected O, but got Unknown
		//IL_00f5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ff: Expected O, but got Unknown
		//IL_0100: Unknown result type (might be due to invalid IL or missing references)
		//IL_0106: Expected O, but got Unknown
		//IL_0127: Unknown result type (might be due to invalid IL or missing references)
		//IL_0131: Expected O, but got Unknown
		//IL_0132: Unknown result type (might be due to invalid IL or missing references)
		//IL_0138: Expected O, but got Unknown
		//IL_0159: Unknown result type (might be due to invalid IL or missing references)
		//IL_0163: Expected O, but got Unknown
		//IL_0169: Unknown result type (might be due to invalid IL or missing references)
		//IL_016f: Expected O, but got Unknown
		//IL_0190: Unknown result type (might be due to invalid IL or missing references)
		//IL_019a: Expected O, but got Unknown
		//IL_019b: Unknown result type (might be due to invalid IL or missing references)
		//IL_01a1: Expected O, but got Unknown
		//IL_01c2: Unknown result type (might be due to invalid IL or missing references)
		//IL_01cc: Expected O, but got Unknown
		//IL_01cd: Unknown result type (might be due to invalid IL or missing references)
		//IL_01d3: Expected O, but got Unknown
		//IL_01f4: Unknown result type (might be due to invalid IL or missing references)
		//IL_01fe: Expected O, but got Unknown
		//IL_0204: Unknown result type (might be due to invalid IL or missing references)
		//IL_020a: Expected O, but got Unknown
		//IL_022b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0235: Expected O, but got Unknown
		//IL_023b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0241: Expected O, but got Unknown
		//IL_0262: Unknown result type (might be due to invalid IL or missing references)
		//IL_026c: Expected O, but got Unknown
		//IL_026f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0275: Expected O, but got Unknown
		//IL_0296: Unknown result type (might be due to invalid IL or missing references)
		//IL_02a0: Expected O, but got Unknown
		//IL_02a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_02a9: Expected O, but got Unknown
		//IL_02ca: Unknown result type (might be due to invalid IL or missing references)
		//IL_02d4: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Save_Click));
			break;
		}
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(MediaElement_OnTapped));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<ManipulationDeltaEventHandler, EventRegistrationToken>)val2.add_ManipulationDelta, (Action<EventRegistrationToken>)val2.remove_ManipulationDelta, new ManipulationDeltaEventHandler(SliderGrid_ManipulationDelta));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<ManipulationCompletedEventHandler, EventRegistrationToken>)val2.add_ManipulationCompleted, (Action<EventRegistrationToken>)val2.remove_ManipulationCompleted, new ManipulationCompletedEventHandler(SliderGrid_ManipulationCompleted));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerCaptureLost, (Action<EventRegistrationToken>)val2.remove_PointerCaptureLost, new PointerEventHandler(SliderOnThumbUp));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerEntered, (Action<EventRegistrationToken>)val2.remove_PointerEntered, new PointerEventHandler(SliderOnThumbDown));
			break;
		}
		case 4:
		{
			ListViewBase val3 = (ListViewBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<ItemClickEventHandler, EventRegistrationToken>)val3.add_ItemClick, (Action<EventRegistrationToken>)val3.remove_ItemClick, new ItemClickEventHandler(ListViewBase_OnItemClick));
			val3 = (ListViewBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<DragItemsStartingEventHandler, EventRegistrationToken>)val3.add_DragItemsStarting, (Action<EventRegistrationToken>)val3.remove_DragItemsStarting, new DragItemsStartingEventHandler(ListView_OnDragItemsStarting));
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val2.add_PointerEntered, (Action<EventRegistrationToken>)val2.remove_PointerEntered, new PointerEventHandler(ListView_OnPointerEntered));
			break;
		}
		case 5:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Delete_Click));
			break;
		}
		case 6:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Duplicate_Click));
			break;
		}
		case 7:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<HoldingEventHandler, EventRegistrationToken>)val2.add_Holding, (Action<EventRegistrationToken>)val2.remove_Holding, new HoldingEventHandler(ClipOnHolding));
			break;
		}
		case 8:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Ok_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
