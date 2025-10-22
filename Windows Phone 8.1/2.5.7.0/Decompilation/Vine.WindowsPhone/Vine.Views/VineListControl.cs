using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Web;
using Windows.ApplicationModel.DataTransfer;
using Windows.ApplicationModel.Email;
using Windows.Foundation;
using Windows.Media.Core;
using Windows.Media.Editing;
using Windows.Storage;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;

namespace Vine.Views;

public sealed class VineListControl : NotifyPage, IIncrementalSource<VineViewModel>, IPullToRefresh, IComponentConnector
{
	public enum Tab
	{
		Posts,
		Likes
	}

	public Brush _secondaryBrush;

	private DateTime LastLoopCountSent;

	private int MinLoopUploadSeconds = 30;

	private readonly ScrollViewer _scrollViewer;

	private Thickness _mosaicThumbnailMargin;

	public static readonly DependencyProperty HeaderProperty = DependencyProperty.Register("Header", typeof(object), typeof(VineListControl), new PropertyMetadata((object)null));

	public static readonly DependencyProperty MusicControlProperty = DependencyProperty.Register("MusicControl", typeof(object), typeof(VineListControl), new PropertyMetadata((object)null));

	public static readonly DependencyProperty HeaderSuccessfulTemplateProperty = DependencyProperty.Register("HeaderSuccessfulTemplate", typeof(DataTemplate), typeof(VineListControl), new PropertyMetadata((object)null));

	public static readonly DependencyProperty FooterProperty = DependencyProperty.Register("Footer", typeof(object), typeof(VineListControl), new PropertyMetadata((object)null));

	public static readonly DependencyProperty FooterTemplateProperty = DependencyProperty.Register("FooterTemplate", typeof(DataTemplate), typeof(VineListControl), new PropertyMetadata((object)null));

	public static readonly DependencyProperty EmptyTextProperty = DependencyProperty.Register("EmptyText", typeof(string), typeof(VineListControl), new PropertyMetadata((object)null));

	public static readonly DependencyProperty IsEmptyProperty = DependencyProperty.Register("IsEmpty", typeof(bool), typeof(VineListControl), new PropertyMetadata((object)false, new PropertyChangedCallback(IsEmptyChanged)));

	public static readonly DependencyProperty HasErrorProperty = DependencyProperty.Register("HasError", typeof(bool), typeof(VineListControl), new PropertyMetadata((object)false, new PropertyChangedCallback(HasErrorChanged)));

	public static readonly DependencyProperty ErrorTextProperty = DependencyProperty.Register("ErrorText", typeof(string), typeof(VineListControl), new PropertyMetadata((object)ResourceHelper.GetString("failed_to_load_posts")));

	public static readonly DependencyProperty ShowRetryProperty = DependencyProperty.Register("ShowRetry", typeof(bool), typeof(VineListControl), new PropertyMetadata((object)true));

	public static readonly DependencyProperty DisablePullToRefreshProperty = DependencyProperty.Register("DisablePullToRefresh", typeof(bool), typeof(VineListControl), new PropertyMetadata((object)false));

	private string _user;

	private bool _isBusy;

	private bool _Active;

	private double _lastVertOffset;

	private bool _updatingPlayingVine;

	private MediaElement _playingMediaElement;

	private VineViewModel _lastPlayingVine;

	private int? _lastTapToLikeAngle;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private NotifyPage Root;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid Grid;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	public PullToRefreshListControl PullToRefreshView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public Section Section { get; set; }

	public Brush SecondaryBrush
	{
		get
		{
			return _secondaryBrush;
		}
		set
		{
			SetProperty(ref _secondaryBrush, value, "SecondaryBrush");
		}
	}

	public Thickness MosaicThumbnailMargin
	{
		get
		{
			return _mosaicThumbnailMargin;
		}
		set
		{
			SetProperty(ref _mosaicThumbnailMargin, value, "MosaicThumbnailMargin");
		}
	}

	public Thickness PullToRefreshMargin
	{
		set
		{
			((FrameworkElement)PullToRefreshView).put_Margin(value);
		}
	}

	public Thickness ListViewPadding
	{
		set
		{
			PullToRefreshView.DefaultPadding = value;
		}
	}

	public object Header
	{
		get
		{
			return ((DependencyObject)this).GetValue(HeaderProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(HeaderProperty, value);
		}
	}

	public MusicInformationControl MusicControl { get; set; }

	public DataTemplate HeaderSuccessfulTemplate
	{
		get
		{
			//IL_000b: Unknown result type (might be due to invalid IL or missing references)
			//IL_0011: Expected O, but got Unknown
			return (DataTemplate)((DependencyObject)this).GetValue(HeaderSuccessfulTemplateProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(HeaderSuccessfulTemplateProperty, (object)value);
		}
	}

	public object Footer
	{
		get
		{
			return ((DependencyObject)this).GetValue(FooterProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(FooterProperty, value);
		}
	}

	public DataTemplate FooterTemplate
	{
		get
		{
			//IL_000b: Unknown result type (might be due to invalid IL or missing references)
			//IL_0011: Expected O, but got Unknown
			return (DataTemplate)((DependencyObject)this).GetValue(FooterTemplateProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(FooterTemplateProperty, (object)value);
		}
	}

	public string EmptyText
	{
		get
		{
			return (string)((DependencyObject)this).GetValue(EmptyTextProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(EmptyTextProperty, (object)value);
		}
	}

	public bool IsEmpty
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(IsEmptyProperty);
		}
		set
		{
			if (!(string.IsNullOrEmpty(EmptyText) && value))
			{
				((DependencyObject)this).SetValue(IsEmptyProperty, (object)value);
			}
		}
	}

	public bool HasError
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(HasErrorProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(HasErrorProperty, (object)value);
		}
	}

	public string ErrorText
	{
		get
		{
			return (string)((DependencyObject)this).GetValue(ErrorTextProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(ErrorTextProperty, (object)value);
		}
	}

	public bool ShowRetry
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(ShowRetryProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(ShowRetryProperty, (object)value);
		}
	}

	public bool DisablePullToRefresh
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(DisablePullToRefreshProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(DisablePullToRefreshProperty, (object)value);
		}
	}

	public ProfileControl ProfileView
	{
		get
		{
			if (Header != null && Header is UserControlWrapper && ((UserControlWrapper)Header).Model is ProfileControl)
			{
				return (ProfileControl)((UserControlWrapper)Header).Model;
			}
			return null;
		}
	}

	public IncrementalLoadingCollection<VineViewModel> Items { get; set; }

	public bool IsVolumeMuted => ApplicationSettings.Current.IsVolumeMuted;

	public Tab CurrentTab { get; set; }

	public string UserId
	{
		get
		{
			return _user;
		}
		set
		{
			_user = value;
		}
	}

	public string PostId { get; set; }

	public string SearchTag { get; set; }

	public VineListViewParams ListParams { get; set; }

	public List<VineModel> PageStateItems { get; set; }

	public double PageStateScrollOffset { get; set; }

	public bool IsBusy
	{
		get
		{
			return _isBusy;
		}
		set
		{
			if (value)
			{
				HasError = false;
				IsEmpty = false;
			}
			SetProperty(ref _isBusy, value, "IsBusy");
		}
	}

	public bool Active
	{
		get
		{
			return _Active;
		}
		set
		{
			SetProperty(ref _Active, value, "Active");
		}
	}

	public bool IsFinishedLoading { get; set; }

	public double ScrollOffset => _scrollViewer.VerticalOffset;

	public PanelScrollingDirection ScrollingDirection { get; set; }

	public event EventHandler<PanelScrollingDirection> ScrollingDirectionChanged;

	public VineListControl()
	{
		//IL_0029: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Expected O, but got Unknown
		InitializeComponent();
		SecondaryBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];
		PullToRefreshView.AddReferences(ListView, this, 55.0);
		_scrollViewer = PullToRefreshView.ScrollViewer;
		Items = new IncrementalLoadingCollection<VineViewModel>(this, PullToRefreshView);
		MosaicThumbnailMargin = new Thickness(0.0, 0.0, (base.WindowWidth - 28.0 - 120.0) / 4.0 - 120.0, 0.0);
	}

	private static void IsEmptyChanged(DependencyObject dependencyObject, DependencyPropertyChangedEventArgs dependencyPropertyChangedEventArgs)
	{
		((VineListControl)(object)dependencyObject).NotifyHasNoErrorAndIsNotEmpty();
	}

	private static void HasErrorChanged(DependencyObject dependencyObject, DependencyPropertyChangedEventArgs e)
	{
		((VineListControl)(object)dependencyObject).NotifyHasNoErrorAndIsNotEmpty();
	}

	private void NotifyHasNoErrorAndIsNotEmpty()
	{
	}

	private string GetView()
	{
		if (!string.IsNullOrEmpty(PostId))
		{
			return "timeline";
		}
		if (!string.IsNullOrEmpty(UserId))
		{
			switch (CurrentTab)
			{
			case Tab.Posts:
				return "profile";
			case Tab.Likes:
				return "likes";
			}
		}
		else if (!string.IsNullOrEmpty(SearchTag))
		{
			return "search";
		}
		return "timeline";
	}

	public async Task OnActivate()
	{
		Active = true;
		_updatingPlayingVine = false;
		if (DisablePullToRefresh && !IsFinishedLoading)
		{
			Items.ClearAndStop(disableLoading: true);
		}
		await RefreshPlayingVine();
		WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)_scrollViewer.remove_ViewChanged, (EventHandler<ScrollViewerViewChangedEventArgs>)ScrollViewerOnViewChanged);
		ScrollViewer scrollViewer = _scrollViewer;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<ScrollViewerViewChangedEventArgs>, EventRegistrationToken>)scrollViewer.add_ViewChanged, (Action<EventRegistrationToken>)scrollViewer.remove_ViewChanged, (EventHandler<ScrollViewerViewChangedEventArgs>)ScrollViewerOnViewChanged);
		if (IsBusy || IsFinishedLoading)
		{
			return;
		}
		if (Footer == null)
		{
			Footer = new UserControlWrapper
			{
				Model = this
			};
		}
		if (FooterTemplate == null)
		{
			FooterTemplate = (DataTemplate)((IDictionary<object, object>)((FrameworkElement)this).Resources)[(object)"DefaultFooterTemplate"];
		}
		if (PageStateItems != null && PageStateItems.Any())
		{
			List<VineViewModel> vms = PageStateItems.Select((VineModel x) => new VineViewModel(x, SecondaryBrush, Section, GetView())).ToList();
			((UIElement)ListView).put_Opacity(0.0);
			SetHeader();
			await SendLoopCountUpdate();
			Items.ResetItems(vms, scrollToTop: false);
			await _scrollViewer.ScrollToVerticalOffsetSpin(PageStateScrollOffset);
			((UIElement)ListView).put_Opacity(1.0);
			PageStateItems = null;
			IsFinishedLoading = true;
		}
		else
		{
			PagedItemsResult<VineViewModel> pagedItemsResult = await Refresh();
			if (pagedItemsResult != null)
			{
				IsFinishedLoading = !pagedItemsResult.ApiResult.HasError;
				return;
			}
			IsFinishedLoading = true;
			SetHeader();
		}
	}

	public async Task RefreshPlayingVine()
	{
		if (!Items.Any())
		{
			return;
		}
		for (int i = 0; i < 5; i++)
		{
			if (await ScrollViewerOnViewChanged(this))
			{
				break;
			}
			await Task.Delay(200);
		}
		if (_playingMediaElement != null && ((int)_playingMediaElement.CurrentState == 4 || (int)_playingMediaElement.CurrentState == 5))
		{
			await ((FrameworkElement)(object)ListView).LayoutUpdatedAsync();
			await ((FrameworkElement)(object)_playingMediaElement).LayoutUpdatedAsync();
			_playingMediaElement.Play();
		}
	}

	public async Task<PagedItemsResult<VineViewModel>> Refresh()
	{
		HasError = false;
		PagedItemsResult<VineViewModel> pagedItemsResult = await Items.Refresh();
		if (pagedItemsResult == null)
		{
			return pagedItemsResult;
		}
		if (pagedItemsResult.ApiResult.HasError)
		{
			if (!(Header is UserControlWrapper userControlWrapper) || !(userControlWrapper.Model is ProfileControl))
			{
				if (Items.Any())
				{
					pagedItemsResult.ApiResult.PopUpErrorIfExists();
				}
				else
				{
					HasError = true;
					if (!pagedItemsResult.ApiResult.HasConnectivityError)
					{
						ErrorText = ResourceHelper.GetString("failed_to_load_posts");
						ShowRetry = true;
					}
					else
					{
						ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
						ShowRetry = false;
					}
					pagedItemsResult.ApiResult.LogError();
				}
			}
		}
		else
		{
			SetHeader();
			Items.ResetItems(pagedItemsResult.ViewModels);
		}
		IsEmpty = !Items.Any() && !HasError;
		LastLoopCountSent = DateTime.UtcNow;
		return pagedItemsResult;
	}

	private void Refresh_Click(object sender, RoutedEventArgs e)
	{
		PullToRefresh();
	}

	public async Task PullToRefresh()
	{
		if (ProfileView != null)
		{
			await ProfileView.PullToRefresh();
		}
		else
		{
			await Refresh();
		}
	}

	public async Task<PagedItemsResult<VineViewModel>> GetPagedItems(int page, int count, string anchor)
	{
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = true;
		});
		ApiResult<BaseVineResponseModel<VineTimelineMetaData>> apiResult = await GetTimeline(page, count, anchor);
		List<VineViewModel> viewModels = null;
		string anchor2 = null;
		if (!apiResult.HasError)
		{
			List<VineModel> records = apiResult.Model.Data.Records;
			anchor2 = apiResult.Model.Data.Anchor;
			string timelineUrl = null;
			if (apiResult.HttpResponse != null && apiResult.HttpResponse.RequestMessage != null && apiResult.HttpResponse.RequestMessage.RequestUri != null)
			{
				timelineUrl = apiResult.HttpResponse.RequestMessage.RequestUri.ToString();
			}
			viewModels = (from i in records
				where i.RecordType == RecordType.Post || (i.RecordType == RecordType.PostMosaic && i.ParsedMosaicType != MosaicType.Unknown)
				select new VineViewModel(i, SecondaryBrush, Section, GetView())
				{
					TimelineApiUrl = timelineUrl
				}).ToList();
		}
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = false;
		});
		return new PagedItemsResult<VineViewModel>
		{
			ApiResult = apiResult,
			ViewModels = viewModels,
			Anchor = anchor2
		};
	}

	public void SetHeader()
	{
		if (((ListViewBase)ListView).Header == null && Header != null)
		{
			((ListViewBase)ListView).put_Header(Header);
		}
		if (((ListViewBase)ListView).HeaderTemplate == null && HeaderSuccessfulTemplate != null)
		{
			((ListViewBase)ListView).put_HeaderTemplate(HeaderSuccessfulTemplate);
		}
	}

	private async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetTimeline(int page, int count, string anchor = null)
	{
		ApiResult<BaseVineResponseModel<VineTimelineMetaData>> result = null;
		if (!string.IsNullOrEmpty(PostId))
		{
			result = ((page != 1) ? new ApiResult<BaseVineResponseModel<VineTimelineMetaData>>
			{
				Model = new BaseVineResponseModel<VineTimelineMetaData>
				{
					Data = new VineTimelineMetaData
					{
						Records = new List<VineModel>()
					}
				}
			} : (await App.Api.GetPost(PostId)));
		}
		else if (!string.IsNullOrEmpty(UserId))
		{
			switch (CurrentTab)
			{
			case Tab.Posts:
				result = await App.Api.GetTimelineUser(UserId, page, count);
				break;
			case Tab.Likes:
				result = await App.Api.GetUserLikes(UserId, page, count);
				break;
			default:
				Debugger.Break();
				break;
			}
		}
		else if (!string.IsNullOrEmpty(SearchTag))
		{
			result = await App.Api.GetTimelineByTag(SearchTag, page, count);
		}
		else if (ListParams != null)
		{
			switch (ListParams.Type)
			{
			case ListType.PopularNow:
				result = await App.Api.GetPopularNow(page, count);
				break;
			case ListType.TrendingPeople:
				result = await App.Api.GetOnTheRise(page, count);
				break;
			case ListType.ChannelRecent:
				result = await App.Api.GetChannelRecent(ListParams.ChannelId, page, count);
				break;
			case ListType.ChannelFeatured:
				result = await App.Api.GetChannelFeatured(ListParams.ChannelId, page, count);
				break;
			case ListType.SimilarVines:
				result = await App.Api.GetSimilarPosts(ListParams.PostId, page, count);
				break;
			case ListType.SearchFeatured:
			case ListType.SearchRecent:
				result = await App.Api.GetSearchTimeline(ListParams.Title, ListParams.Type, page, count);
				break;
			case ListType.TimelinePath:
				result = await App.Api.GetTimelineByPath(ListParams.TimelinePath, page, count, anchor);
				break;
			}
		}
		else
		{
			result = await App.Api.GetTimeline(page, count);
		}
		return result;
	}

	public async void OnDeactivate()
	{
		Active = false;
		WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)_scrollViewer.remove_ViewChanged, (EventHandler<ScrollViewerViewChangedEventArgs>)ScrollViewerOnViewChanged);
		ClearPlayingVine();
		if (MusicControl != null)
		{
			MusicControl.Hide();
		}
		DispatcherEx.InvokeBackground(async delegate
		{
			SendLoopCountUpdate();
		});
	}

	public void Clear()
	{
		ClearPlayingVine();
		IsFinishedLoading = false;
		Items.ClearAndStop();
	}

	public void ClearPlayingVine()
	{
		if (_playingMediaElement != null)
		{
			MediaElement playingMediaElement = _playingMediaElement;
			playingMediaElement.Stop();
			playingMediaElement.put_Source((Uri)null);
			VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)playingMediaElement).DataContext;
			if (vineViewModel != null)
			{
				vineViewModel.IsPlaying = false;
			}
		}
		_playingMediaElement = null;
		_lastPlayingVine = null;
	}

	public void ScrollToTop()
	{
		PullToRefreshView.ScrollToTopAnimated();
	}

	private void ScrollViewerOnViewChanged(object sender, ScrollViewerViewChangedEventArgs e)
	{
		ScrollViewerOnViewChanged(sender);
		if (this.ScrollingDirectionChanged != null)
		{
			UpdateScrollingDirectingChanged();
		}
	}

	private void UpdateScrollingDirectingChanged()
	{
		//IL_0058: Unknown result type (might be due to invalid IL or missing references)
		//IL_0068: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0094: Unknown result type (might be due to invalid IL or missing references)
		//IL_007c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0078: Unknown result type (might be due to invalid IL or missing references)
		//IL_0098: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ae: Unknown result type (might be due to invalid IL or missing references)
		double num = _scrollViewer.VerticalOffset - _lastVertOffset;
		if (!(num > 50.0) && !(num < -50.0) && !(_scrollViewer.VerticalOffset < 200.0))
		{
			return;
		}
		PanelScrollingDirection val = ((_scrollViewer.VerticalOffset < 200.0) ? ((PanelScrollingDirection)2) : ((num < 0.0) ? ((PanelScrollingDirection)2) : ((!(num > 0.0)) ? ((PanelScrollingDirection)0) : ((PanelScrollingDirection)1))));
		_lastVertOffset = _scrollViewer.VerticalOffset;
		if (ScrollingDirection != val)
		{
			ScrollingDirection = val;
			if (this.ScrollingDirectionChanged != null)
			{
				this.ScrollingDirectionChanged(this, ScrollingDirection);
			}
		}
	}

	private async Task<bool> ScrollViewerOnViewChanged(object sender)
	{
		if (_updatingPlayingVine)
		{
			return false;
		}
		_updatingPlayingVine = true;
		bool setPlayingVine = false;
		try
		{
			ItemsStackPanel val = (ItemsStackPanel)((ItemsControl)ListView).ItemsPanelRoot;
			if (val == null || ((Panel)val).Children == null || !((IEnumerable<UIElement>)((Panel)val).Children).Any() || ((FrameworkElement)this).ActualHeight == 0.0)
			{
				return false;
			}
			ListViewItem val2 = null;
			double num = double.MaxValue;
			int firstVisibleIndex = val.FirstVisibleIndex;
			int lastVisibleIndex = val.LastVisibleIndex;
			double num2 = 0.0;
			if (Header != null)
			{
				Grid val3 = ((FrameworkElement)(object)ListView).GetLogicalChildrenByType<Grid>(applyTemplates: true).FirstOrDefault((Grid g) => ((FrameworkElement)g).Name == "ListHeaderContainer");
				num2 = ((val3 != null) ? ((FrameworkElement)val3).ActualHeight : 0.0);
			}
			double num3 = _scrollViewer.VerticalOffset - PullToRefreshView.DefaultPadding.Top - num2;
			double num4 = _scrollViewer.ViewportHeight / 2.0;
			for (int num5 = firstVisibleIndex; num5 <= lastVisibleIndex; num5++)
			{
				ListViewItem val4 = (ListViewItem)((ItemsControl)ListView).ContainerFromIndex(num5);
				if (val4 == null)
				{
					continue;
				}
				VineViewModel vineViewModel = (VineViewModel)((ContentControl)val4).Content;
				if (vineViewModel == null || vineViewModel.Model.RecordType == RecordType.Post)
				{
					Rect layoutSlot = LayoutInformation.GetLayoutSlot((FrameworkElement)(object)val4);
					double num6 = layoutSlot.Y - num3 + layoutSlot.Height / 2.0;
					double num7 = Math.Abs(num4 - num6);
					if (num7 < num)
					{
						num = num7;
						val2 = val4;
					}
				}
			}
			if (val2 != null)
			{
				VineViewModel vine = (VineViewModel)((ContentControl)val2).Content;
				if (_lastPlayingVine != vine && vine != null && vine.Model.RecordType == RecordType.Post)
				{
					if (_lastPlayingVine != null)
					{
						_lastPlayingVine.IsFinishedBuffering = false;
						_lastPlayingVine.IsPlaying = false;
						if (_playingMediaElement != null)
						{
							_playingMediaElement.Stop();
							_playingMediaElement.put_Source((Uri)null);
						}
					}
					vine.IsPlaying = true;
					vine.IsFinishedBuffering = false;
					MediaElement mediaElement = ((FrameworkElement)(object)val2).GetFirstLogicalChildByType<MediaElement>(applyTemplates: true);
					if (mediaElement != null)
					{
						bool num8 = await vine.IsCached();
						vine.LoopsPerClip = 1;
						if (!num8)
						{
							mediaElement.put_Source(vine.PlayingVideoUrl);
						}
						else
						{
							MediaStreamSource val5 = await GenerateSeamlessVine(vine);
							if (val5 != null)
							{
								mediaElement.put_Source((Uri)null);
								mediaElement.SetMediaStreamSource((IMediaSource)(object)val5);
								vine.IsSeamlessLooping = true;
								mediaElement.Play();
							}
							else
							{
								mediaElement.put_Source(vine.PlayingVideoUrl);
							}
						}
						_playingMediaElement = mediaElement;
						setPlayingVine = true;
					}
					_lastPlayingVine = vine;
				}
			}
		}
		catch (Exception)
		{
		}
		finally
		{
			_updatingPlayingVine = false;
		}
		return setPlayingVine;
	}

	private async Task<MediaStreamSource> GenerateSeamlessVine(VineViewModel vine)
	{
		_ = 1;
		try
		{
			MediaClip val = await MediaClip.CreateFromFileAsync((IStorageFile)(object)(await StorageFile.GetFileFromPathAsync(vine.PlayingVideoUrl.LocalPath)));
			val.put_TrimTimeFromStart(TimeSpan.FromSeconds(0.05));
			val.put_TrimTimeFromEnd(TimeSpan.FromSeconds(0.05));
			MediaComposition val2 = new MediaComposition();
			int val3 = (int)(162.5 / val.TrimmedDuration.TotalSeconds);
			val3 = (vine.LoopsPerClip = Math.Max(25, Math.Min(val3, 50)));
			val2.Clips.Add(val);
			for (int i = 0; i < val3 - 1; i++)
			{
				val2.Clips.Add(val.Clone());
			}
			return val2.GenerateMediaStreamSource();
		}
		catch (Exception)
		{
			return null;
		}
	}

	private async void MediaElement_OnCurrentStateChanged(object sender, RoutedEventArgs e)
	{
		MediaElement mediaElement = (MediaElement)sender;
		VineViewModel vine = (VineViewModel)((FrameworkElement)mediaElement).DataContext;
		if (vine == null)
		{
			return;
		}
		vine.IsLoadingVideo = (int)mediaElement.CurrentState == 1 || (int)mediaElement.CurrentState == 2;
		if ((int)mediaElement.CurrentState != 3 || vine.IsFinishedBuffering)
		{
			return;
		}
		vine.IsFinishedBuffering = true;
		await Task.Delay(200);
		((ICollection<TimelineMarker>)mediaElement.Markers).Clear();
		if (vine.IsSeamlessLooping)
		{
			SetSeamlessVineMarkers(mediaElement);
		}
		else
		{
			TimelineMarkerCollection markers = mediaElement.Markers;
			TimelineMarker val = new TimelineMarker();
			val.put_Type("loopStart");
			val.put_Time(new TimeSpan(0, 0, 0, 0, 500));
			((ICollection<TimelineMarker>)markers).Add(val);
			TimelineMarkerCollection markers2 = mediaElement.Markers;
			TimelineMarker val2 = new TimelineMarker();
			val2.put_Type("3sec");
			val2.put_Time(new TimeSpan(0, 0, 0, 0, 3000));
			((ICollection<TimelineMarker>)markers2).Add(val2);
			TimelineMarkerCollection markers3 = mediaElement.Markers;
			TimelineMarker val3 = new TimelineMarker();
			val3.put_Type("nearEnd");
			val3.put_Time(mediaElement.NaturalDuration.TimeSpan.Subtract(TimeSpan.FromMilliseconds(100.0)));
			((ICollection<TimelineMarker>)markers3).Add(val3);
		}
		DispatcherEx.InvokeBackground(async delegate
		{
			int count = ((IList<object>)((ItemsControl)ListView).Items).IndexOf((object)vine);
			await Task.WhenAll(from v in (from v in ((IEnumerable<object>)((ItemsControl)ListView).Items).Skip(count).Take(3)
					select v as VineViewModel).ToArray()
				select v.DownloadVine());
		});
	}

	private static void SetSeamlessVineMarkers(MediaElement mediaElement)
	{
		//IL_005f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0064: Unknown result type (might be due to invalid IL or missing references)
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0087: Expected O, but got Unknown
		//IL_008d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0092: Unknown result type (might be due to invalid IL or missing references)
		//IL_009d: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b5: Expected O, but got Unknown
		//IL_00bb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e4: Expected O, but got Unknown
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)mediaElement).DataContext;
		if (vineViewModel != null && vineViewModel.LoopsPerClip != 0)
		{
			int num = (int)mediaElement.NaturalDuration.TimeSpan.TotalMilliseconds / vineViewModel.LoopsPerClip;
			int num2 = 500;
			int num3 = 3000;
			int num4 = num - 100;
			for (int i = 0; i < vineViewModel.LoopsPerClip; i++)
			{
				int num5 = num * i;
				TimelineMarkerCollection markers = mediaElement.Markers;
				TimelineMarker val = new TimelineMarker();
				val.put_Type("loopStart");
				val.put_Time(new TimeSpan(0, 0, 0, 0, num2 + num5));
				((ICollection<TimelineMarker>)markers).Add(val);
				TimelineMarkerCollection markers2 = mediaElement.Markers;
				TimelineMarker val2 = new TimelineMarker();
				val2.put_Type("3sec");
				val2.put_Time(new TimeSpan(0, 0, 0, 0, num3 + num5));
				((ICollection<TimelineMarker>)markers2).Add(val2);
				TimelineMarkerCollection markers3 = mediaElement.Markers;
				TimelineMarker val3 = new TimelineMarker();
				val3.put_Type("nearEnd");
				val3.put_Time(new TimeSpan(0, 0, 0, 0, num4 + num5));
				((ICollection<TimelineMarker>)markers3).Add(val3);
			}
		}
	}

	private async void MediaElement_OnMarkerReached(object sender, TimelineMarkerRoutedEventArgs e)
	{
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)(MediaElement)sender).DataContext;
		if (vineViewModel != null)
		{
			if (e.Marker.Type == "loopStart" && vineViewModel.PendingLoopCount == 0)
			{
				UpdateDisplayLoopCount(vineViewModel);
			}
			else if (e.Marker.Type == "3sec")
			{
				UpdateDisplayLoopCount(vineViewModel);
			}
			else if (e.Marker.Type == "nearEnd")
			{
				vineViewModel.PendingLoopCount++;
				vineViewModel.LoopsWatchedCount++;
				vineViewModel.LastLoopFinishTime = DateTime.UtcNow;
				App.ScribeService.Log(new PostPlayedEvent(vineViewModel, Section, GetView()));
				UpdateDisplayLoopCount(vineViewModel);
			}
		}
		if ((DateTime.UtcNow - LastLoopCountSent).TotalSeconds >= (double)MinLoopUploadSeconds)
		{
			await SendLoopCountUpdate();
		}
	}

	private void UpdateDisplayLoopCount(VineViewModel vine)
	{
		if (vine.LastLoopFinishTime == default(DateTime))
		{
			vine.LastLoopFinishTime = DateTime.UtcNow;
		}
		if (vine.FirstLoopStartTime == default(DateTime))
		{
			vine.FirstLoopStartTime = DateTime.UtcNow;
			if (vine.PendingLoopCount == 0)
			{
				vine.PendingLoopCount++;
				App.ScribeService.Log(new PostPlayedEvent(vine, Section, GetView()));
			}
		}
		vine.DisplayLoops = vine.Model.Loops.Count + (long)(vine.Model.Loops.Velocity * DateTime.UtcNow.Subtract(vine.FirstLoopStartTime).TotalSeconds) + vine.LoopsWatchedCount;
		vine.NotifyLoopChange();
	}

	private async Task SendLoopCountUpdate()
	{
		DateTime dateTime = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
		List<LoopCountsModel> list = new List<LoopCountsModel>();
		foreach (VineViewModel item in Items.Where((VineViewModel r) => r.PendingLoopCount > 0))
		{
			int num = (int)(item.LastLoopFinishTime - dateTime).TotalSeconds;
			list.Add(new LoopCountsModel
			{
				count = item.PendingLoopCount,
				PostId = item.Model.PostId,
				ts = num
			});
		}
		if (!list.Any())
		{
			return;
		}
		LastLoopCountSent = DateTime.UtcNow;
		ApiResult<BaseVineResponseModel<LoopResponseModel>> apiResult = await App.Api.UpdateLoops(list);
		if (apiResult.HasError)
		{
			return;
		}
		foreach (VineViewModel item2 in Items)
		{
			item2.PendingLoopCount = 0;
		}
		MinLoopUploadSeconds = apiResult.Model.Data.nextAfter;
	}

	private void MediaElement_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Expected O, but got Unknown
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_000e: Invalid comparison between Unknown and I4
		//IL_0018: Unknown result type (might be due to invalid IL or missing references)
		//IL_001e: Invalid comparison between Unknown and I4
		MediaElement val = (MediaElement)sender;
		if ((int)val.CurrentState == 3)
		{
			val.Pause();
		}
		else if ((int)val.CurrentState == 4)
		{
			val.Play();
		}
	}

	private async void MediaElement_OnDoubleTapped(object sender, DoubleTappedRoutedEventArgs e)
	{
		MediaElement mediaElement = (MediaElement)sender;
		if (mediaElement == null)
		{
			return;
		}
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)mediaElement).DataContext;
		if (vineViewModel != null)
		{
			await LikeVine(vineViewModel, true);
			if ((int)mediaElement.CurrentState == 3)
			{
				mediaElement.Pause();
			}
			else if ((int)mediaElement.CurrentState == 4)
			{
				mediaElement.Play();
			}
			DependencyObject parent = ((FrameworkElement)mediaElement).Parent;
			Grid val = (Grid)(object)((parent is Grid) ? parent : null);
			if (val != null)
			{
				Point position = e.GetPosition((UIElement)(object)val);
				TappedToLikeControl tappedToLikeControl = new TappedToLikeControl();
				_lastTapToLikeAngle = tappedToLikeControl.Init(position.X, position.Y, _lastTapToLikeAngle);
				((ICollection<UIElement>)((Panel)val).Children).Add((UIElement)(object)tappedToLikeControl);
			}
		}
	}

	private void MediaElement_OnMediaFailed(object sender, ExceptionRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Expected O, but got Unknown
		try
		{
			MediaElement val = (MediaElement)sender;
			VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)val).DataContext;
			if (vineViewModel != null)
			{
				vineViewModel.FallbackToRemoteUri();
				val.put_Source(vineViewModel.PlayingVideoUrl);
			}
		}
		catch (Exception)
		{
		}
	}

	private void Profile_Tapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.Navigate(typeof(ProfileView), (object)vineViewModel.Model.UserId);
	}

	private void ProfileRevine_Tapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.Navigate(typeof(ProfileView), (object)vineViewModel.Model.Repost.User.UserId);
	}

	private void ShowFlyout(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Expected O, but got Unknown
		FrameworkElement val = (FrameworkElement)sender;
		FlyoutBase.GetAttachedFlyout(val).ShowAt(val);
	}

	private async void MenuFlyoutItemEmailPost_OnClick(object sender, RoutedEventArgs e)
	{
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)sender).DataContext;
		EmailMessage val = new EmailMessage();
		val.put_Body(string.Format(ResourceHelper.GetString("share_post_text"), new object[2]
		{
			vineViewModel.Model.UserName,
			vineViewModel.Model.PermalinkUrl
		}));
		val.put_Subject(string.Format(ResourceHelper.GetString("share_post_subject"), new object[1] { vineViewModel.Model.UserName }));
		await EmailManager.ShowComposeNewEmailAsync(val);
	}

	private void MenuFlyoutItemSharePost_OnClick(object sender, RoutedEventArgs e)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_000d: Expected O, but got Unknown
		FrameworkElement val = (FrameworkElement)sender;
		VineViewModel vine = (VineViewModel)val.DataContext;
		DataTransferManager forCurrentView = DataTransferManager.GetForCurrentView();
		WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<DataTransferManager, DataRequestedEventArgs>, EventRegistrationToken>)forCurrentView.add_DataRequested, (Action<EventRegistrationToken>)forCurrentView.remove_DataRequested, (TypedEventHandler<DataTransferManager, DataRequestedEventArgs>)delegate(DataTransferManager senderDtm, DataRequestedEventArgs argsDtm)
		{
			DataRequest request = argsDtm.Request;
			request.Data.Properties.put_Title(" ");
			request.Data.SetText(string.Format(ResourceHelper.GetString("share_post_text"), new object[2]
			{
				vine.Model.UserName,
				vine.Model.PermalinkUrl
			}));
		});
		App.ScribeService.Log(new ViewImpressionEvent(Section.None, "share"));
		DataTransferManager.ShowShareUI();
	}

	private async void MenuFlyoutItemReportPost_OnClick(object sender, RoutedEventArgs e)
	{
		FrameworkElement val = (FrameworkElement)sender;
		VineViewModel vine = (VineViewModel)val.DataContext;
		MessageDialog val2 = new MessageDialog(ResourceHelper.GetString("DoYouWantToReportPost"), ResourceHelper.GetString("vine"))
		{
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("ReportThisPost"), (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
		};
		val2.put_CancelCommandIndex(1u);
		if ((int)(await val2.ShowAsync()).Id == 0 && !(await App.Api.ReportVine(vine.Model.PostId)).HasError)
		{
			ToastHelper.Show("", ResourceHelper.GetString("post_reported"));
		}
	}

	private async void DeletePost_OnClick(object sender, RoutedEventArgs e)
	{
		FrameworkElement val = (FrameworkElement)sender;
		VineViewModel vine = (VineViewModel)val.DataContext;
		MessageDialog val2 = new MessageDialog(ResourceHelper.GetString("delete_confirm"), ResourceHelper.GetString("vine"))
		{
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("delete"), (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
		};
		val2.put_CancelCommandIndex(1u);
		if ((int)(await val2.ShowAsync()).Id == 0 && !(await App.Api.DeletePost(vine.Model.PostId)).HasError)
		{
			ToastHelper.Show("", ResourceHelper.GetString("post_deleted"));
			if (Items.Count == 1)
			{
				((Control)ListView).put_Padding(new Thickness(((Control)ListView).Padding.Left, ((Control)ListView).Padding.Top, ((Control)ListView).Padding.Right, 800.0));
			}
			Items.Remove(vine);
			await PullToRefreshView.UpdateListPadding();
			if (ProfileView != null)
			{
				ProfileView.DeletedPost();
			}
		}
	}

	private async void LikeTapped(object sender, TappedRoutedEventArgs e)
	{
		FrameworkElement val = (FrameworkElement)((sender is FrameworkElement) ? sender : null);
		if (val != null && val.DataContext is VineViewModel vine)
		{
			await LikeVine(vine);
		}
	}

	private async Task LikeVine(VineViewModel vine, bool? like = null)
	{
		if (vine == null || vine.Model == null)
		{
			return;
		}
		App.ScribeService.Log(new LikeEvent(vine, Section, GetView()));
		bool oldState = vine.Model.Liked;
		if (oldState == like)
		{
			return;
		}
		bool newState = !vine.Model.Liked;
		VineToggleButtonState likeButtonState = (newState ? VineToggleButtonState.On : VineToggleButtonState.Off);
		vine.LikeButtonState = likeButtonState;
		vine.Model.Liked = newState;
		if (vine.Model.Likes != null)
		{
			if (newState)
			{
				vine.Model.Likes.Count++;
			}
			else
			{
				vine.Model.Likes.Count--;
			}
			vine.NotifyCountChange();
		}
		ApiResult<BaseVineResponseModel> apiResult = ((!oldState) ? (await App.Api.Like(vine.Model.PostId)) : (await App.Api.UnLike(vine.Model.PostId)));
		if (!apiResult.HasError || apiResult.HttpResponse == null || (int)apiResult.HttpResponse.StatusCode == 404)
		{
			return;
		}
		vine.Model.Liked = oldState;
		vine.LikeButtonState = (oldState ? VineToggleButtonState.On : VineToggleButtonState.Off);
		if (vine.Model.Likes != null)
		{
			if (newState)
			{
				vine.Model.Likes.Count--;
			}
			else
			{
				vine.Model.Likes.Count++;
			}
			vine.NotifyCountChange();
		}
	}

	private async void RevineTapped(object sender, TappedRoutedEventArgs e)
	{
		VineViewModel vine = (VineViewModel)((FrameworkElement)sender).DataContext;
		if (!vine.IsRevinedByMe)
		{
			App.ScribeService.Log(new RevineEvent(vine, Section, GetView()));
			vine.RevineButtonState = VineToggleButtonState.On;
			vine.Model.Reposts.Count++;
			vine.NotifyCountChange();
			ApiResult<BaseVineResponseModel<RevineModel>> apiResult = await App.Api.Revine(vine.Model.PostId);
			if (apiResult.HasError)
			{
				vine.Model.MyRepostId = "0";
				vine.Model.Reposts.Count--;
				vine.NotifyCountChange();
				vine.RevineButtonState = VineToggleButtonState.Off;
			}
			else
			{
				vine.Model.MyRepostId = apiResult.Model.Data.RepostId;
			}
		}
		else if (vine.Model.MyRepostId != null && vine.Model.MyRepostId != "0")
		{
			vine.RevineButtonState = VineToggleButtonState.Off;
			string oldMyRepostId = vine.Model.MyRepostId;
			vine.Model.MyRepostId = "0";
			vine.Model.Reposts.Count--;
			vine.NotifyCountChange();
			if ((await App.Api.UnVine(vine.Model.PostId, oldMyRepostId)).HasError)
			{
				vine.Model.MyRepostId = oldMyRepostId;
				vine.Model.Reposts.Count++;
				vine.NotifyCountChange();
				vine.RevineButtonState = VineToggleButtonState.On;
			}
		}
	}

	private void Likes_OnClicked(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.NavigateWithObject(typeof(VineUserListView), new VineUserListViewParams
		{
			Type = UserListType.Likes,
			PostId = vineViewModel.Model.PostId
		});
	}

	private void Comments_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.NavigateWithObject(typeof(CommentsView), new CommentNavigationObject
		{
			IsFocused = false,
			PostId = vineViewModel.Model.PostId,
			Section = Section
		});
	}

	private void CommentCount_OnClicked(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.NavigateWithObject(typeof(CommentsView), new CommentNavigationObject
		{
			IsFocused = true,
			PostId = vineViewModel.Model.PostId,
			Section = Section
		});
	}

	private void Revines_OnClicked(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.NavigateWithObject(typeof(VineUserListView), new VineUserListViewParams
		{
			Type = UserListType.Revines,
			PostId = vineViewModel.Model.PostId
		});
	}

	public void NotifyMuteChange()
	{
		NotifyOfPropertyChange(() => IsVolumeMuted);
	}

	private void MenuFlyoutItemVineMessageLink_OnClick(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.NavigateWithObject(typeof(ShareMessageView), new ShareViewParameters
		{
			PostId = vineViewModel.Model.PostId
		});
	}

	private void MusicInfo_Tapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		AudioTracks audioTracks = ((VineViewModel)((FrameworkElement)sender).DataContext).Model.AudioTracks.FirstOrDefault();
		if (audioTracks != null && audioTracks.Track != null && MusicControl != null)
		{
			MusicControl.MusicArtist = (string.IsNullOrWhiteSpace(audioTracks.Track.ArtistName) ? audioTracks.Track.AlbumArtist : audioTracks.Track.ArtistName);
			MusicControl.MusicTrack = audioTracks.Track.TrackName;
			ScrollViewer scrollViewer = _scrollViewer;
			WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<ScrollViewerViewChangedEventArgs>, EventRegistrationToken>)scrollViewer.add_ViewChanged, (Action<EventRegistrationToken>)scrollViewer.remove_ViewChanged, (EventHandler<ScrollViewerViewChangedEventArgs>)_scrollViewer_ViewChanged);
			MusicControl.IconTapped();
		}
	}

	private void _scrollViewer_ViewChanged(object sender, ScrollViewerViewChangedEventArgs e)
	{
		MusicControl.Hide();
		WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)_scrollViewer.remove_ViewChanged, (EventHandler<ScrollViewerViewChangedEventArgs>)_scrollViewer_ViewChanged);
	}

	private void SimilarVines_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineViewModel vineViewModel = (VineViewModel)((FrameworkElement)sender).DataContext;
		if (vineViewModel != null && vineViewModel.Model != null)
		{
			VineListViewParams param = new VineListViewParams
			{
				Title = "Similar Vines",
				Type = ListType.SimilarVines,
				PostId = vineViewModel.Model.PostId
			};
			App.RootFrame.NavigateWithObject(typeof(TagVineListView), param);
		}
	}

	private void MosaicPost_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineViewModel vm = (VineViewModel)((FrameworkElement)sender).DataContext;
		NavigateToLink(vm);
	}

	private void NavigateToLink(VineViewModel vm)
	{
		if (vm != null && vm.Model != null && vm.Model.LinkPath != null)
		{
			if (vm.Model.LinkPath.StartsWith("/user/"))
			{
				string text = vm.Model.LinkPath.Replace("/user/", "");
				App.RootFrame.Navigate(typeof(ProfileView), (object)text);
			}
			else if (vm.Model.LinkPath.StartsWith("/timelines/"))
			{
				VineListViewParams param = new VineListViewParams
				{
					Title = vm.Model.Title,
					Type = ListType.TimelinePath,
					TimelinePath = vm.Model.LinkPath
				};
				App.RootFrame.NavigateWithObject(typeof(TagVineListView), param);
			}
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/VineListControl.xaml"), (ComponentResourceLocation)0);
			Root = (NotifyPage)((FrameworkElement)this).FindName("Root");
			Grid = (Grid)((FrameworkElement)this).FindName("Grid");
			PullToRefreshView = (PullToRefreshListControl)((FrameworkElement)this).FindName("PullToRefreshView");
			ListView = (ListView)((FrameworkElement)this).FindName("ListView");
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
		//IL_0099: Unknown result type (might be due to invalid IL or missing references)
		//IL_009f: Expected O, but got Unknown
		//IL_00c0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ca: Expected O, but got Unknown
		//IL_00d0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d6: Expected O, but got Unknown
		//IL_00f7: Unknown result type (might be due to invalid IL or missing references)
		//IL_0101: Expected O, but got Unknown
		//IL_0107: Unknown result type (might be due to invalid IL or missing references)
		//IL_010d: Expected O, but got Unknown
		//IL_012e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0138: Expected O, but got Unknown
		//IL_013e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0144: Expected O, but got Unknown
		//IL_0165: Unknown result type (might be due to invalid IL or missing references)
		//IL_016f: Expected O, but got Unknown
		//IL_0175: Unknown result type (might be due to invalid IL or missing references)
		//IL_017b: Expected O, but got Unknown
		//IL_019c: Unknown result type (might be due to invalid IL or missing references)
		//IL_01a6: Expected O, but got Unknown
		//IL_01ac: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b2: Expected O, but got Unknown
		//IL_01d3: Unknown result type (might be due to invalid IL or missing references)
		//IL_01dd: Expected O, but got Unknown
		//IL_01e3: Unknown result type (might be due to invalid IL or missing references)
		//IL_01e9: Expected O, but got Unknown
		//IL_020a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0214: Expected O, but got Unknown
		//IL_021a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0220: Expected O, but got Unknown
		//IL_0241: Unknown result type (might be due to invalid IL or missing references)
		//IL_024b: Expected O, but got Unknown
		//IL_0251: Unknown result type (might be due to invalid IL or missing references)
		//IL_0257: Expected O, but got Unknown
		//IL_0278: Unknown result type (might be due to invalid IL or missing references)
		//IL_0282: Expected O, but got Unknown
		//IL_0288: Unknown result type (might be due to invalid IL or missing references)
		//IL_028e: Expected O, but got Unknown
		//IL_02af: Unknown result type (might be due to invalid IL or missing references)
		//IL_02b9: Expected O, but got Unknown
		//IL_02bf: Unknown result type (might be due to invalid IL or missing references)
		//IL_02c5: Expected O, but got Unknown
		//IL_02e6: Unknown result type (might be due to invalid IL or missing references)
		//IL_02f0: Expected O, but got Unknown
		//IL_02f6: Unknown result type (might be due to invalid IL or missing references)
		//IL_02fc: Expected O, but got Unknown
		//IL_031d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0327: Expected O, but got Unknown
		//IL_032d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0333: Expected O, but got Unknown
		//IL_0354: Unknown result type (might be due to invalid IL or missing references)
		//IL_035e: Expected O, but got Unknown
		//IL_0364: Unknown result type (might be due to invalid IL or missing references)
		//IL_036a: Expected O, but got Unknown
		//IL_038b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0395: Expected O, but got Unknown
		//IL_039b: Unknown result type (might be due to invalid IL or missing references)
		//IL_03a1: Expected O, but got Unknown
		//IL_03c2: Unknown result type (might be due to invalid IL or missing references)
		//IL_03cc: Expected O, but got Unknown
		//IL_03cd: Unknown result type (might be due to invalid IL or missing references)
		//IL_03d3: Expected O, but got Unknown
		//IL_03f4: Unknown result type (might be due to invalid IL or missing references)
		//IL_03fe: Expected O, but got Unknown
		//IL_03ff: Unknown result type (might be due to invalid IL or missing references)
		//IL_0405: Expected O, but got Unknown
		//IL_0426: Unknown result type (might be due to invalid IL or missing references)
		//IL_0430: Expected O, but got Unknown
		//IL_0431: Unknown result type (might be due to invalid IL or missing references)
		//IL_0437: Expected O, but got Unknown
		//IL_0458: Unknown result type (might be due to invalid IL or missing references)
		//IL_0462: Expected O, but got Unknown
		//IL_0463: Unknown result type (might be due to invalid IL or missing references)
		//IL_0469: Expected O, but got Unknown
		//IL_048a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0494: Expected O, but got Unknown
		//IL_049a: Unknown result type (might be due to invalid IL or missing references)
		//IL_04a0: Expected O, but got Unknown
		//IL_04c1: Unknown result type (might be due to invalid IL or missing references)
		//IL_04cb: Expected O, but got Unknown
		//IL_04d1: Unknown result type (might be due to invalid IL or missing references)
		//IL_04d7: Expected O, but got Unknown
		//IL_04f8: Unknown result type (might be due to invalid IL or missing references)
		//IL_0502: Expected O, but got Unknown
		//IL_0508: Unknown result type (might be due to invalid IL or missing references)
		//IL_050e: Expected O, but got Unknown
		//IL_052f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0539: Expected O, but got Unknown
		//IL_053c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0542: Expected O, but got Unknown
		//IL_0563: Unknown result type (might be due to invalid IL or missing references)
		//IL_056d: Expected O, but got Unknown
		//IL_0570: Unknown result type (might be due to invalid IL or missing references)
		//IL_0576: Expected O, but got Unknown
		//IL_0597: Unknown result type (might be due to invalid IL or missing references)
		//IL_05a1: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(ProfileRevine_Tapped));
			break;
		}
		case 2:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(SimilarVines_OnTapped));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(ShowFlyout));
			break;
		}
		case 4:
		{
			MenuFlyoutItem val4 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val4.add_Click, (Action<EventRegistrationToken>)val4.remove_Click, new RoutedEventHandler(MenuFlyoutItemVineMessageLink_OnClick));
			break;
		}
		case 5:
		{
			MenuFlyoutItem val4 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val4.add_Click, (Action<EventRegistrationToken>)val4.remove_Click, new RoutedEventHandler(MenuFlyoutItemSharePost_OnClick));
			break;
		}
		case 6:
		{
			MenuFlyoutItem val4 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val4.add_Click, (Action<EventRegistrationToken>)val4.remove_Click, new RoutedEventHandler(MenuFlyoutItemReportPost_OnClick));
			break;
		}
		case 7:
		{
			MenuFlyoutItem val4 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val4.add_Click, (Action<EventRegistrationToken>)val4.remove_Click, new RoutedEventHandler(DeletePost_OnClick));
			break;
		}
		case 8:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(RevineTapped));
			break;
		}
		case 9:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Revines_OnClicked));
			break;
		}
		case 10:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Comments_OnTapped));
			break;
		}
		case 11:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(CommentCount_OnClicked));
			break;
		}
		case 12:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(LikeTapped));
			break;
		}
		case 13:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Likes_OnClicked));
			break;
		}
		case 14:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(Profile_Tapped));
			break;
		}
		case 15:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(MusicInfo_Tapped));
			break;
		}
		case 16:
		{
			MediaElement val3 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TimelineMarkerRoutedEventHandler, EventRegistrationToken>)val3.add_MarkerReached, (Action<EventRegistrationToken>)val3.remove_MarkerReached, new TimelineMarkerRoutedEventHandler(MediaElement_OnMarkerReached));
			val3 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_CurrentStateChanged, (Action<EventRegistrationToken>)val3.remove_CurrentStateChanged, new RoutedEventHandler(MediaElement_OnCurrentStateChanged));
			val3 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<ExceptionRoutedEventHandler, EventRegistrationToken>)val3.add_MediaFailed, (Action<EventRegistrationToken>)val3.remove_MediaFailed, new ExceptionRoutedEventHandler(MediaElement_OnMediaFailed));
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(MediaElement_OnTapped));
			val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<DoubleTappedEventHandler, EventRegistrationToken>)val2.add_DoubleTapped, (Action<EventRegistrationToken>)val2.remove_DoubleTapped, new DoubleTappedEventHandler(MediaElement_OnDoubleTapped));
			break;
		}
		case 17:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(MosaicPost_OnTapped));
			break;
		}
		case 18:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(MosaicPost_OnTapped));
			break;
		}
		case 19:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(MosaicPost_OnTapped));
			break;
		}
		case 20:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Refresh_Click));
			break;
		}
		case 21:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Refresh_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
