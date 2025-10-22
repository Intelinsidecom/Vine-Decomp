using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;
using GalaSoft.MvvmLight.Messaging;
using Gen.Services;
using Huyn.Ads;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Reactive;
using Microsoft.Phone.Shell;
using Microsoft.Phone.Tasks;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Media;
using Vine.Annotations;
using Vine.Controls;
using Vine.Datas;
using Vine.Pages.Direct.ViewModels;
using Vine.Pages.Main.ViewModels;
using Vine.Resources;
using Vine.Services;
using Vine.Services.Models;
using Vine.Services.Response;
using Vine.Utils;
using Vine.ViewModels;
using Windows.Networking.Proximity;

namespace Vine;

public class MainPage : PhoneApplicationPage, INotifyPropertyChanged
{
	private PostControl _lastpostcontrol;

	private FilePickerHelper _filePickerHelper;

	private MainViewModel ViewModel;

	public static readonly DependencyProperty PostForegroundProperty;

	private bool navigating;

	private bool _isInit;

	private bool _evalAlreadyManagedDuringLifetime;

	private List<PostControl> _visiblePosts = new List<PostControl>();

	private DispatcherTimer _timerRegular;

	private ApplicationBar _FavAppBar;

	private IPostRecord _currentShareRecord;

	private bool isrecentmode;

	private bool _followChannel;

	private bool zoomactive;

	private bool _needtodisplayrating;

	private IPostRecord _currenttagmepost;

	private bool _startedwithgrid;

	private static bool _hasNFC;

	private bool _animateheader;

	private bool _hasHeader;

	private bool _forceDontHideHeader;

	private bool _isnearby;

	private static int MAXITEMSBEFOREHIDE;

	private bool _isPhablet;

	private int _lastpostcontrolIndex;

	internal PhoneApplicationPage Page;

	internal Storyboard StoryboardChangeNotificationCounter;

	internal Storyboard StoryboardShowPost;

	internal Storyboard StoryboardShowGrid;

	internal Grid MainPanel;

	internal VisualStateGroup HeaderState;

	internal VisualState Normal;

	internal VisualState HeaderHidden;

	internal VisualStateGroup DeviceType;

	internal VisualState PhabletMode;

	internal Rectangle RectBackground;

	internal Grid RootPanel;

	internal LongListSelector ListPost;

	internal StackPanel PullrefreshPanel;

	internal PullRefreshControl Pullrefreshcontrol;

	internal Button EncodingButton;

	internal Grid LayoutRoot;

	internal Grid NoItemInFeedPanel;

	internal LongListSelector ListPostGrid;

	internal Grid ListPostGridFooter;

	internal Grid LoadingPanel;

	internal Grid MapHeader;

	internal Image VenueImage;

	internal TextBlock VenueName;

	internal TextBlock VenueAddress;

	internal Grid Header;

	internal Border border;

	internal Button DirectButton;

	internal Path path;

	internal Button NotifButton;

	internal Button MeButton;

	internal Image MeButtonImage;

	internal Grid AdPanel;

	internal Grid ModernAppBarContainer;

	internal ModernAppBar ModernVineAppBar;

	internal ModernAppBar ModernVineFavAppBar;

	internal ModernAppBar ModernInstaAppBar;

	internal ModernAppBar ModernNearbyAppBar;

	internal ModernAppBar ModernOtherAppBar;

	internal ModernAppBar ModernInstaFavAppBar;

	internal ModernAppBar ModernVineChannelAppBar;

	internal Grid SharePanel;

	internal StackPanel stackPanel;

	internal Button ShareTwitter;

	internal Button ShareFacebook;

	internal Button ShareEmbedButton;

	internal Button ShareNFC;

	internal Button ShareCopyLinkButton;

	internal Grid NearbySettingsPanel;

	internal Slider NearbySlide;

	internal Grid TagInfoPanel;

	internal ToggleSwitch TagMeAcceptToggle;

	internal Grid SaveMediaLoadingPanel;

	internal ProgressBar SaveMediaLoadingProgress;

	private bool _contentLoaded;

	public static IEnumerable<IPostRecord> ForcedPosts { get; set; }

	public Brush PostForeground
	{
		get
		{
			//IL_000b: Unknown result type (might be due to invalid IL or missing references)
			//IL_0011: Expected O, but got Unknown
			return (Brush)((DependencyObject)this).GetValue(PostForegroundProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(PostForegroundProperty, (object)value);
		}
	}

	public event PropertyChangedEventHandler PropertyChanged;

	static MainPage()
	{
		//IL_001a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0024: Expected O, but got Unknown
		PostForegroundProperty = DependencyProperty.Register("PostForeground", typeof(Brush), typeof(MainPage), new PropertyMetadata((object)null));
		_hasNFC = ProximityDevice.GetDefault() != null;
		if (Application.Current.Host.Content.ScaleFactor == 150)
		{
			MAXITEMSBEFOREHIDE = 7;
		}
		else
		{
			MAXITEMSBEFOREHIDE = 5;
		}
	}

	public MainPage()
	{
		//IL_003a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0044: Expected O, but got Unknown
		//IL_0070: Unknown result type (might be due to invalid IL or missing references)
		//IL_0075: Unknown result type (might be due to invalid IL or missing references)
		//IL_0088: Unknown result type (might be due to invalid IL or missing references)
		//IL_0092: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b5: Expected O, but got Unknown
		//IL_00f8: Unknown result type (might be due to invalid IL or missing references)
		//IL_0102: Expected O, but got Unknown
		//IL_011d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0127: Expected O, but got Unknown
		((FrameworkElement)this).DataContext = (ViewModel = new MainViewModel());
		PostForeground = (Brush)Application.Current.Resources[(object)"PrincBrush"];
		InitializeComponent();
		_isPhablet = PhoneScreenSizeHelper.IsPhablet();
		if (_isPhablet)
		{
			VisualStateManager.GoToState((Control)(object)this, "PhabletMode", false);
			((UIElement)MeButtonImage).Clip = (Geometry)new EllipseGeometry
			{
				Center = new Point(20.0, 20.0),
				RadiusX = 20.0,
				RadiusY = 20.0
			};
		}
		_filePickerHelper = new FilePickerHelper();
		DirectViewModel directStatic = ViewModelLocator.DirectStatic;
		((FrameworkElement)DirectButton).DataContext = directStatic;
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Shape)RectBackground).Fill = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
		if (!_hasNFC)
		{
			((UIElement)ShareNFC).Visibility = (Visibility)1;
		}
		((FrameworkElement)this).Loaded += new RoutedEventHandler(MainPage_Loaded);
		if (DatasProvider.Instance.CurrentUser != null && DatasProvider.Instance.CurrentUser.User == null)
		{
			((UIElement)this).Opacity = 0.0;
		}
		Messenger.Default.Register(this, delegate(NotificationMessage<string> message)
		{
			if (message.Notification == "ChangeEditorFollow")
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					((MainViewModel)((FrameworkElement)this).DataContext).Reload();
				});
			}
			else if (message.Notification == "RemoveOtherPlayers")
			{
				if (_lastpostcontrol != null && _lastpostcontrol.Post.PostId != message.Content)
				{
					_lastpostcontrol.Stop();
				}
				_lastpostcontrol = null;
				_lastpostcontrolIndex = -1;
				DisplayHeader();
			}
		});
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
		((Timeline)StoryboardShowPost).Completed += StoryboardShowPost_Completed;
		((Timeline)StoryboardShowGrid).Completed += StoryboardShowGrid_Completed;
		ObservableExtensions.Subscribe<IEvent<EventArgs>>(Observable.Throttle<IEvent<EventArgs>>(Observable.FromEvent<EventHandler, EventArgs>((Func<EventHandler<EventArgs>, EventHandler>)((EventHandler<EventArgs> handlerAction) => delegate(object _, EventArgs args)
		{
			handlerAction(ListPost, args);
		}), (Action<EventHandler>)delegate(EventHandler handler)
		{
			ListPost.ManipulationStateChanged += handler;
		}, (Action<EventHandler>)delegate(EventHandler handler)
		{
			ListPost.ManipulationStateChanged -= handler;
		}), TimeSpan.FromMilliseconds(400.0)), (Action<IEvent<EventArgs>>)delegate
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				//IL_0006: Unknown result type (might be due to invalid IL or missing references)
				//IL_000c: Invalid comparison between Unknown and I4
				if ((int)ListPost.ManipulationState == 1 && Pullrefreshcontrol != null && IsListPostTop())
				{
					Pullrefreshcontrol.Turn();
					if (IsListPostTop())
					{
						Reload(scrollToTop: false);
					}
				}
			});
		});
	}

	public ApplicationBar GetApplicationBarFavorite()
	{
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0026: Expected O, but got Unknown
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		//IL_0057: Unknown result type (might be due to invalid IL or missing references)
		//IL_0077: Unknown result type (might be due to invalid IL or missing references)
		//IL_009e: Unknown result type (might be due to invalid IL or missing references)
		if (_FavAppBar == null)
		{
			_FavAppBar = (ApplicationBar)((FrameworkElement)this).Resources[(object)"FavAppBar"];
			((ApplicationBarIconButton)_FavAppBar.Buttons[0]).Text = AppResources.Home;
			((ApplicationBarIconButton)_FavAppBar.Buttons[2]).Text = AppResources.PinToStart;
			((ApplicationBarMenuItem)_FavAppBar.MenuItems[0]).Text = AppResources.Refresh;
			if (AppVersion.IsHaveAds())
			{
				((ApplicationBarMenuItem)_FavAppBar.MenuItems[1]).Text = AppResources.RemoveAds;
			}
			else
			{
				_FavAppBar.MenuItems.RemoveAt(1);
			}
		}
		return _FavAppBar;
	}

	private void StoryboardShowPost_Completed(object sender, EventArgs e)
	{
		//IL_0017: Unknown result type (might be due to invalid IL or missing references)
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		((UIElement)ListPostGrid).Visibility = (Visibility)1;
		CompositeTransform val = (CompositeTransform)((UIElement)ListPostGrid).RenderTransform;
		double scaleX = (val.ScaleY = 1.0);
		val.ScaleX = scaleX;
	}

	private void StoryboardShowGrid_Completed(object sender, EventArgs e)
	{
		//IL_0017: Unknown result type (might be due to invalid IL or missing references)
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		((UIElement)ListPost).Visibility = (Visibility)1;
		CompositeTransform val = (CompositeTransform)((UIElement)ListPostGrid).RenderTransform;
		double scaleX = (val.ScaleY = 1.0);
		val.ScaleX = scaleX;
	}

	private async Task UpdateNotif()
	{
		Vine.Datas.Datas data = DatasProvider.Instance;
		if (data.CurrentUser == null)
		{
			return;
		}
		try
		{
			PendingNotificationsInfo notifsInfos = default(PendingNotificationsInfo);
			_ = notifsInfos;
			notifsInfos = await data.CurrentUser.Service.GetPendingNotificationsAsync();
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				//IL_0103: Unknown result type (might be due to invalid IL or missing references)
				//IL_0108: Unknown result type (might be due to invalid IL or missing references)
				//IL_0136: Unknown result type (might be due to invalid IL or missing references)
				//IL_012f: Unknown result type (might be due to invalid IL or missing references)
				//IL_0140: Unknown result type (might be due to invalid IL or missing references)
				//IL_017b: Expected O, but got Unknown
				if (((ContentControl)NotifButton).Content.ToString() != notifsInfos.NbrNotifications.ToString() && notifsInfos.NbrNotifications != 0)
				{
					((ContentControl)NotifButton).Content = notifsInfos.NbrNotifications;
					StoryboardChangeNotificationCounter.Begin();
				}
				else
				{
					((ContentControl)NotifButton).Content = notifsInfos.NbrNotifications;
				}
				if (notifsInfos.NbrMessages.HasValue)
				{
					ViewModelLocator.DirectStatic.SetNotifCounter(notifsInfos.NbrMessages.Value);
				}
				if (data.CurrentUser == data.PrimaryUser)
				{
					ShellTile.ActiveTiles.First().Update((ShellTileData)new IconicTileData
					{
						BackgroundColor = (Color)(data.UseMyAccentColourForLiveTile ? Colors.Transparent : ((Color)Application.Current.Resources[(object)"PrincColor"])),
						Count = notifsInfos.NbrNotifications + (notifsInfos.NbrMessages ?? 0)
					});
				}
			});
		}
		catch
		{
		}
	}

	private void StartTimer()
	{
		//IL_0016: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Expected O, but got Unknown
		if (!DatasProvider.Instance.AutoPlayComputed)
		{
			return;
		}
		if (_timerRegular == null)
		{
			_timerRegular = new DispatcherTimer();
			_timerRegular.Interval = TimeSpan.FromMilliseconds(800.0);
			_timerRegular.Tick += delegate
			{
				PlayCurrentVideo();
			};
		}
		_timerRegular.Start();
	}

	private void StopTimer()
	{
		if (_timerRegular != null)
		{
			_timerRegular.Stop();
		}
	}

	protected void Post_SelectedTag(object sender, string tag)
	{
		if (tag != ((MainViewModel)((FrameworkElement)this).DataContext).FeedType)
		{
			NavigationServiceExt.ToTagFeed(tag);
		}
	}

	private void MainPage_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_0012: Expected O, but got Unknown
		((FrameworkElement)this).Loaded -= new RoutedEventHandler(MainPage_Loaded);
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (instance.CurrentUser == null || instance.CurrentUser.User == null)
		{
			NavigationServiceExt.ToLogin(removebackentry: true);
		}
		else if (_needtodisplayrating)
		{
			_needtodisplayrating = false;
			RateAppControl rate = new RateAppControl();
			rate.Closed += delegate
			{
				((PresentationFrameworkCollection<UIElement>)(object)((Panel)MainPanel).Children).Remove((UIElement)(object)rate);
			};
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)MainPanel).Children).Add((UIElement)(object)rate);
		}
	}

	protected override void OnBackKeyPress(CancelEventArgs e)
	{
		//IL_0058: Unknown result type (might be due to invalid IL or missing references)
		//IL_007b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0081: Invalid comparison between Unknown and I4
		//IL_00bd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00de: Unknown result type (might be due to invalid IL or missing references)
		((PhoneApplicationPage)this).OnBackKeyPress(e);
		ModernAppBar modernAppBar = (ModernAppBar)(object)((IEnumerable<UIElement>)((Panel)ModernAppBarContainer).Children).FirstOrDefault((UIElement a) => (int)a.Visibility == 0);
		if (modernAppBar != null && modernAppBar.IsMenuOpened)
		{
			modernAppBar.IsMenuOpened = false;
			e.Cancel = true;
		}
		else if ((int)((UIElement)SharePanel).Visibility == 0)
		{
			HideSharePanel();
			e.Cancel = true;
		}
		else if (_startedwithgrid && (int)((UIElement)ListPostGrid).Visibility == 1)
		{
			DisplayGrid();
			e.Cancel = true;
		}
		else if (!_startedwithgrid && (int)((UIElement)ListPostGrid).Visibility == 0)
		{
			DisplayList();
			e.Cancel = true;
		}
		else if ((int)((UIElement)NearbySettingsPanel).Visibility == 0)
		{
			((UIElement)NearbySettingsPanel).Visibility = (Visibility)1;
			e.Cancel = true;
		}
		else if ((int)((UIElement)TagInfoPanel).Visibility == 0)
		{
			((UIElement)TagInfoPanel).Visibility = (Visibility)1;
			e.Cancel = true;
		}
	}

	private void HideSharePanel()
	{
		((UIElement)ListPost).IsHitTestVisible = true;
		((UIElement)SharePanel).Visibility = (Visibility)1;
	}

	private void DisplayHeader()
	{
		if (_hasHeader)
		{
			_lastpostcontrol = null;
			_lastpostcontrolIndex = -1;
			VisualStateManager.GoToState((Control)(object)this, "Normal", true);
		}
	}

	private void SetAppBar(ModernAppBar appbar)
	{
		((PhoneApplicationPage)this).ApplicationBar = null;
		foreach (ModernAppBar item in (PresentationFrameworkCollection<UIElement>)(object)((Panel)ModernAppBarContainer).Children)
		{
			((UIElement)item).Visibility = (Visibility)((item != appbar) ? 1 : 0);
		}
	}

	protected override async void OnNavigatedTo(NavigationEventArgs e)
	{
		_003C_003En__FabricatedMethod0(e);
		if ((int)e.NavigationMode == 1 && await _filePickerHelper.ManageOnNavigationAsync())
		{
			return;
		}
		navigating = false;
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (instance.CurrentUser == null || instance.CurrentUser.User == null)
		{
			return;
		}
		MainViewModel vm = (MainViewModel)((FrameworkElement)this).DataContext;
		if (!_isInit || (int)e.NavigationMode == 0 || (int)e.NavigationMode == 3)
		{
			_isInit = true;
			bool flag = false;
			if (((Page)this).NavigationContext.QueryString.ContainsKey("removebackentry"))
			{
				while (((Page)this).NavigationService.RemoveBackEntry() != null)
				{
				}
			}
			bool clearafter = false;
			if (!flag)
			{
				if (((Page)this).NavigationContext.QueryString.ContainsKey("type"))
				{
					string text = ((Page)this).NavigationContext.QueryString["type"];
					vm.FeedType = text;
					SetType(text);
					((UIElement)NoItemInFeedPanel).Visibility = (Visibility)1;
					if (text.StartsWith("tags/"))
					{
						SetAppBar(ModernVineFavAppBar);
						ManageTagAppbar();
					}
					else if (text.StartsWith("channels/"))
					{
						SetAppBar(ModernVineChannelAppBar);
						ManageChannelAppbar();
						ManageFollowChannelAppbar();
						MainPage mainPage = this;
						ModernAppBar modernVineChannelAppBar = ModernVineChannelAppBar;
						SolidColorBrush val = new SolidColorBrush(VineGenUtils.HexColor(((Page)this).NavigationContext.QueryString["bg"]));
						Brush postForeground = (Brush)val;
						((Control)modernVineChannelAppBar).Background = (Brush)val;
						mainPage.PostForeground = postForeground;
					}
					else if (text.StartsWith("feed/location"))
					{
						if (((Page)this).NavigationContext.QueryString.ContainsKey("venuename"))
						{
							VenueName.Text = ((Page)this).NavigationContext.QueryString["venuename"];
							VenueAddress.Text = (((Page)this).NavigationContext.QueryString.ContainsKey("venueaddress") ? ((Page)this).NavigationContext.QueryString["venueaddress"] : "");
						}
						else
						{
							TextBlock venueName = VenueName;
							string text2 = (VenueAddress.Text = null);
							venueName.Text = text2;
						}
						((UIElement)MapHeader).Visibility = (Visibility)0;
						_hasHeader = true;
						((FrameworkElement)RootPanel).Margin = new Thickness(0.0);
						SetAppBar(ModernOtherAppBar);
					}
					else
					{
						SetAppBar(ModernOtherAppBar);
					}
					((UIElement)NoItemInFeedPanel).Visibility = (Visibility)1;
				}
				else
				{
					((UIElement)Header).Visibility = (Visibility)0;
					_hasHeader = true;
					((UIElement)LoadingPanel).Visibility = (Visibility)1;
					SetAppBar(ModernVineAppBar);
					((UIElement)NoItemInFeedPanel).Visibility = (Visibility)1;
					_animateheader = true;
					clearafter = vm.LoadCachedData();
					if ((int)e.NavigationMode == 0 && !((Page)this).NavigationService.CanGoBack)
					{
						ManageEval();
					}
					UpdateNotif();
					UpdateUserPicture();
				}
			}
			if (ForcedPosts != null)
			{
				((UIElement)ListPost).Opacity = 0.0;
				((FrameworkElement)this).Loaded += new RoutedEventHandler(Forced_Loaded);
			}
			else
			{
				vm.LoadData(clearafter, ((Page)this).NavigationContext.QueryString.ContainsKey("nextpage") ? ((Page)this).NavigationContext.QueryString["nextpage"] : null, delegate(IListPosts listposts)
				{
					_followChannel = listposts.Followed;
					ManageFollowChannelAppbar();
				});
			}
			if (((Page)this).NavigationContext.QueryString.ContainsKey("displaygrid"))
			{
				_startedwithgrid = true;
				DisplayGrid(fromscratch: true, withAnimation: false);
				if (((Page)this).NavigationContext.QueryString["displaygrid"] == "last")
				{
					TimerHelper.ToTime(TimeSpan.FromMilliseconds(20.0), delegate
					{
						try
						{
							ListPostGrid.ScrollTo((object)ListPostGridFooter);
							vm.LoadData();
						}
						catch
						{
						}
					});
				}
			}
			if (_hasHeader)
			{
				if (_isPhablet)
				{
					((FrameworkElement)PullrefreshPanel).Margin = new Thickness(0.0, -41.0, 0.0, 0.0);
				}
				else
				{
					((FrameworkElement)PullrefreshPanel).Margin = new Thickness(0.0, -24.0, 0.0, 0.0);
				}
				DisplayHeader();
			}
			else
			{
				((UIElement)EncodingButton).Visibility = (Visibility)1;
				((FrameworkElement)PullrefreshPanel).Margin = new Thickness(0.0, -98.0, 0.0, 0.0);
			}
		}
		else
		{
			if (_hasHeader)
			{
				DisplayHeader();
			}
			if (vm.Posts == null || vm.Posts.Count == 0 || (int)e.NavigationMode == 3)
			{
				Reload(scrollToTop: true);
			}
		}
		if (!((Page)this).NavigationService.CanGoBack && instance.AutoPlayComputed)
		{
			try
			{
				FrameworkDispatcher.Update();
				if (!MediaPlayer.GameHasControl && (int)MessageBox.Show(AppResources.ToastMuteAudioMessage, AppResources.ToastMuteAudioTitle, (MessageBoxButton)1) == 1)
				{
					instance.AutoPlayComputed = false;
				}
				FrameworkDispatcher.Update();
			}
			catch
			{
			}
		}
		AddAds();
		StartTimer();
	}

	private void Forced_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_0012: Expected O, but got Unknown
		((FrameworkElement)this).Loaded -= new RoutedEventHandler(Forced_Loaded);
		MainViewModel mainViewModel = (MainViewModel)((FrameworkElement)this).DataContext;
		mainViewModel.SetForcedPosts(ForcedPosts);
		if (((Page)this).NavigationContext.QueryString.ContainsKey("nextpage"))
		{
			mainViewModel.SetNextPage(((Page)this).NavigationContext.QueryString["nextpage"]);
		}
		ForcedPosts = null;
		if (((Page)this).NavigationContext.QueryString.ContainsKey("focuson"))
		{
			string focusid = ((Page)this).NavigationContext.QueryString["focuson"];
			IPostRecord post = mainViewModel.Posts.FirstOrDefault((IPostRecord f) => f.PostId == focusid);
			if (post != null)
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					try
					{
						ListPost.ScrollTo((object)post);
					}
					catch
					{
					}
					((UIElement)ListPost).Opacity = 1.0;
				});
			}
			else
			{
				((UIElement)ListPost).Opacity = 1.0;
			}
		}
		else
		{
			((UIElement)ListPost).Opacity = 1.0;
		}
	}

	private void ManageEval()
	{
		if (_evalAlreadyManagedDuringLifetime)
		{
			return;
		}
		_evalAlreadyManagedDuringLifetime = true;
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (instance.DisableReview)
		{
			return;
		}
		if (instance.NbrLaunch < 100)
		{
			int nbrLaunch = instance.NbrLaunch + 1;
			instance.NbrLaunch = nbrLaunch;
		}
		if (!instance.HasEvaluate)
		{
			int nbrLaunch2 = instance.NbrLaunch;
			if (nbrLaunch2 % 10 == 5)
			{
				_needtodisplayrating = true;
			}
			else if (nbrLaunch2 > 70)
			{
				instance.HasEvaluate = true;
			}
		}
	}

	private async Task UpdateUserPicture()
	{
		Vine.Datas.Datas data = DatasProvider.Instance;
		DataUser currentUser = data.CurrentUser;
		if (currentUser.User.Picture != null)
		{
			return;
		}
		try
		{
			IProfile profile = await currentUser.Service.GetProfilInfoAsync(data.CurrentUser.User.Id);
			if (data.CurrentUser.User.Picture != profile.Picture)
			{
				data.CurrentUser.User.ChangePicture(profile.Picture);
				data.Save();
			}
		}
		catch
		{
		}
	}

	private void AddAds()
	{
		if (AppVersion.IsHaveAds())
		{
			if (((Page)this).NavigationContext.QueryString.ContainsKey("type"))
			{
				((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
				UIElementCollection children = ((Panel)AdPanel).Children;
				AdRotator adRotator = new AdRotator();
				((FrameworkElement)adRotator).Width = 480.0;
				((FrameworkElement)adRotator).Height = 80.0;
				((PresentationFrameworkCollection<UIElement>)(object)children).Add((UIElement)(object)adRotator);
			}
			return;
		}
		if (ModernNearbyAppBar.MenuItems.Count == 1 && (string)((ContentControl)ModernNearbyAppBar.MenuItems[0]).Content == AppResources.RemoveAds)
		{
			ModernNearbyAppBar.MenuItems.RemoveAt(0);
		}
		if (ModernOtherAppBar.MenuItems.Count == 1 && (string)((ContentControl)ModernOtherAppBar.MenuItems[0]).Content == AppResources.RemoveAds)
		{
			ModernOtherAppBar.MenuItems.RemoveAt(0);
		}
		if (ModernInstaFavAppBar.MenuItems.Count == 1 && (string)((ContentControl)ModernInstaFavAppBar.MenuItems[0]).Content == AppResources.RemoveAds)
		{
			ModernInstaFavAppBar.MenuItems.RemoveAt(0);
		}
	}

	private void SetType(string type)
	{
		type.StartsWith("tags/");
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		//IL_0021: Unknown result type (might be due to invalid IL or missing references)
		//IL_0027: Invalid comparison between Unknown and I4
		((Page)this).OnNavigatingFrom(e);
		ClearAds();
		navigating = true;
		StopTimer();
		StopVideos();
		if ((int)e.NavigationMode == 1)
		{
			Messenger.Default.Unregister(this);
		}
	}

	private void ClearAds()
	{
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
	}

	private void StopVideos()
	{
		if (_lastpostcontrol != null)
		{
			_lastpostcontrol.Stop();
		}
	}

	private void LongListSelector_ItemRealized(object sender, ItemRealizationEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		//IL_0054: Unknown result type (might be due to invalid IL or missing references)
		if ((int)e.ItemKind != 2)
		{
			return;
		}
		PostControl postControl = (PostControl)(object)VisualTreeHelper.GetChild((DependencyObject)(object)e.Container, 0);
		_visiblePosts.Add(postControl);
		if (_visiblePosts.Count == 1)
		{
			_lastpostcontrol = postControl;
			_lastpostcontrolIndex = 0;
			postControl.CreatePlayer();
		}
		else
		{
			PlayCurrentVideo();
		}
		IList itemsSource = ((LongListSelector)sender).ItemsSource;
		if (itemsSource.Count >= 2)
		{
			if (e.Container.Content == itemsSource[itemsSource.Count - 2])
			{
				((MainViewModel)((FrameworkElement)this).DataContext).LoadData();
				AddAds();
			}
		}
	}

	private void LongListSelector_ItemUnrealized(object sender, ItemRealizationEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.ItemKind == 2)
		{
			PostControl item = (PostControl)(object)VisualTreeHelper.GetChild((DependencyObject)(object)e.Container, 0);
			_visiblePosts.Remove(item);
		}
	}

	private void PlayCurrentVideo(bool dontHideHeader = false)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_005e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Unknown result type (might be due to invalid IL or missing references)
		//IL_0068: Unknown result type (might be due to invalid IL or missing references)
		//IL_0103: Unknown result type (might be due to invalid IL or missing references)
		//IL_0109: Invalid comparison between Unknown and I4
		try
		{
			if ((int)((UIElement)ListPostGrid).Visibility == 0)
			{
				return;
			}
			double num = 2147483647.0;
			PostControl postControl = null;
			foreach (PostControl visiblePost in _visiblePosts)
			{
				if (((FrameworkElement)visiblePost).ActualHeight == 0.0)
				{
					continue;
				}
				Point val = ((UIElement)visiblePost).TransformToVisual((UIElement)(object)this).Transform(new Point(1.0, 0.0));
				if (!(((Point)(ref val)).Y < -200.0))
				{
					double y = ((Point)(ref val)).Y;
					if (y < num)
					{
						_ = 0.0;
						postControl = visiblePost;
						num = y;
					}
				}
			}
			if (postControl == null || postControl == _lastpostcontrol)
			{
				return;
			}
			StopVideos();
			int num2 = ViewModel.Posts.IndexOf(postControl.Post);
			if (!dontHideHeader)
			{
				if (!ViewModel.IsLoading && !_forceDontHideHeader && (int)((UIElement)ListPostGrid).Visibility == 1 && _animateheader && num2 >= MAXITEMSBEFOREHIDE && _lastpostcontrol != null && _lastpostcontrolIndex >= 0 && _lastpostcontrolIndex < num2)
				{
					VisualStateManager.GoToState((Control)(object)this, "HeaderHidden", true);
				}
				else
				{
					VisualStateManager.GoToState((Control)(object)this, "Normal", true);
				}
			}
			_lastpostcontrol = postControl;
			_lastpostcontrolIndex = num2;
			if (DatasProvider.Instance.AutoPlayComputed)
			{
				postControl.CreatePlayer();
			}
		}
		catch
		{
		}
	}

	private void ApplicationBar_StateChanged(object sender, ApplicationBarStateChangedEventArgs e)
	{
		SystemTray.IsVisible = e.IsMenuVisible;
	}

	private void Camera_Click(object sender, EventArgs e)
	{
		if (DatasProvider.Instance.DisplayedTutorialCamera)
		{
			NavigationServiceExt.ToCamera();
		}
		else
		{
			NavigationServiceExt.ToTutorialCamera();
		}
	}

	private void Search_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToSearch();
	}

	private void MyProfile_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToProfile(DatasProvider.Instance.CurrentUser.User.Id);
	}

	private void Uservoice_Click(object sender, EventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		new WebBrowserTask
		{
			Uri = new Uri("https://6sec.uservoice.com/", UriKind.Absolute)
		}.Show();
	}

	private void Settings_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToSettings();
	}

	private void Explore_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToExplore();
	}

	private void GoHome_Click(object sender, RoutedEventArgs e)
	{
		while (((Page)this).NavigationService.RemoveBackEntry() != null)
		{
		}
		NavigationServiceExt.ToTimeline(null, removebackentry: true);
	}

	private void Notif_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToNotification();
	}

	private void Job_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToTranscoding();
	}

	private void Share_Selected(object sender, IPostRecord record)
	{
		//IL_0031: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		//IL_004b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0051: Unknown result type (might be due to invalid IL or missing references)
		((UIElement)ListPost).IsHitTestVisible = false;
		_currentShareRecord = record;
		Button shareEmbedButton = ShareEmbedButton;
		Button shareCopyLinkButton = ShareCopyLinkButton;
		int num = (string.IsNullOrEmpty(record.ShareUrl) ? 1 : 0);
		Visibility visibility = (Visibility)num;
		((UIElement)shareCopyLinkButton).Visibility = (Visibility)num;
		((UIElement)shareEmbedButton).Visibility = visibility;
		Button shareFacebook = ShareFacebook;
		Button shareTwitter = ShareTwitter;
		visibility = (Visibility)1;
		((UIElement)shareTwitter).Visibility = (Visibility)1;
		((UIElement)shareFacebook).Visibility = visibility;
		((UIElement)SharePanel).Visibility = (Visibility)0;
	}

	private void ShareTwitter_Click(object sender, RoutedEventArgs e)
	{
		HideSharePanel();
		NavigationServiceExt.ToTwitter(_currentShareRecord.PostId, _currentShareRecord.Description, _currentShareRecord.Thumb);
	}

	private async void ShareFacebook_Click(object sender, RoutedEventArgs e)
	{
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser != null && currentUser.FacebookAccess != null)
		{
			try
			{
				await currentUser.Service.SharePostFacebookAsync(_currentShareRecord.PostId);
				ToastHelper.Show(AppResources.ToastMessagePostedOnFacebook, afternav: false, (Orientation)0);
				return;
			}
			catch
			{
				NavigationServiceExt.ToShareFacebook(_currentShareRecord.PostId);
				return;
			}
			finally
			{
				HideSharePanel();
			}
		}
		NavigationServiceExt.ToShareFacebook(_currentShareRecord.PostId);
	}

	private void ShareEmbed_Click(object sender, RoutedEventArgs e)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		ShareLinkTask val = new ShareLinkTask();
		val.LinkUri = new Uri(_currentShareRecord.ShareUrl, UriKind.Absolute);
		try
		{
			val.Message = _currentShareRecord.Description;
		}
		catch
		{
		}
		((ShareTaskBase)val).Show();
	}

	private void ShareCopyLink_Click(object sender, RoutedEventArgs e)
	{
		Clipboard.SetText(_currentShareRecord.ShareUrl);
		ToastHelper.Show(AppResources.LinkCopied, afternav: false, (Orientation)0);
	}

	private void ShareNFC_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToNFC(_currentShareRecord);
	}

	private void PinStart_Click(object sender, EventArgs e)
	{
		//IL_0029: Unknown result type (might be due to invalid IL or missing references)
		//IL_002e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0052: Expected O, but got Unknown
		//IL_00e2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_011a: Expected O, but got Unknown
		try
		{
			MainViewModel mainViewModel = (MainViewModel)((FrameworkElement)this).DataContext;
			string text = mainViewModel.FeedType.TrimEnd('/');
			FlipTileData val = new FlipTileData();
			((ShellTileData)val).Title = "#" + text.Substring(text.LastIndexOf("/") + 1);
			FlipTileData val2 = val;
			IPostRecord postRecord = mainViewModel.Posts.FirstOrDefault();
			((StandardTileData)val2).BackgroundImage = new Uri("/Assets/Tiles/DarkTile.png", UriKind.Relative);
			val2.WideBackgroundImage = new Uri("/Assets/Tiles/WideDarkTile.png", UriKind.Relative);
			Uri uri = new Uri("/Timeline/" + mainViewModel.FeedType, UriKind.Relative);
			ShellTile.Create(uri, (ShellTileData)(object)val2, true);
			if (postRecord != null)
			{
				string distantTile = TileGenerator.GetDistantTile(mainViewModel.FeedType, postRecord.Thumb);
				ShellTile val3 = ShellTile.ActiveTiles.FirstOrDefault((ShellTile t) => t.NavigationUri == uri);
				if (val3 != null)
				{
					val3.Update((ShellTileData)new FlipTileData
					{
						BackgroundImage = new Uri(distantTile + "&size=apollo"),
						WideBackgroundImage = new Uri(distantTile + "&size=apolloW")
					});
				}
			}
		}
		catch (Exception)
		{
		}
	}

	private void About_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToAbout();
	}

	private void SwitchChannelMode_Click(object sender, RoutedEventArgs e)
	{
		isrecentmode = !isrecentmode;
		ManageChannelAppbar();
		((MainViewModel)((FrameworkElement)this).DataContext).LoadData(clearafter: true);
	}

	private void SwitchTagMode_Click(object sender, RoutedEventArgs e)
	{
		isrecentmode = !isrecentmode;
		ManageTagAppbar();
		((MainViewModel)((FrameworkElement)this).DataContext).LoadData(clearafter: true);
	}

	private void ManageChannelAppbar()
	{
		MainViewModel mainViewModel = (MainViewModel)((FrameworkElement)this).DataContext;
		ModernAppBarButton modernAppBarButton = ModernVineChannelAppBar.Buttons[2];
		if (isrecentmode)
		{
			int length = mainViewModel.FeedType.LastIndexOf('/');
			mainViewModel.Reinit(mainViewModel.FeedType.Substring(0, length) + "/recent");
			modernAppBarButton.Text = AppResources.DisplayPopular;
			((ContentControl)modernAppBarButton).Content = "\ue95e";
		}
		else
		{
			int length2 = mainViewModel.FeedType.LastIndexOf('/');
			mainViewModel.Reinit(mainViewModel.FeedType.Substring(0, length2) + "/popular");
			modernAppBarButton.Text = AppResources.DisplayRecent;
			((ContentControl)modernAppBarButton).Content = "\ue916";
		}
	}

	private void ManageTagAppbar()
	{
		MainViewModel mainViewModel = (MainViewModel)((FrameworkElement)this).DataContext;
		ModernAppBarButton modernAppBarButton = ModernVineChannelAppBar.Buttons[2];
		if (isrecentmode)
		{
			mainViewModel.Reinit(mainViewModel.FeedType, "recent");
			modernAppBarButton.Text = AppResources.DisplayPopular;
			((ContentControl)modernAppBarButton).Content = "\ue95e";
		}
		else
		{
			mainViewModel.Reinit(mainViewModel.FeedType, "top");
			modernAppBarButton.Text = AppResources.DisplayRecent;
			((ContentControl)modernAppBarButton).Content = "\ue916";
		}
	}

	private void ManageFollowChannelAppbar()
	{
		_ = (MainViewModel)((FrameworkElement)this).DataContext;
		ModernAppBarButton modernAppBarButton = ModernVineChannelAppBar.Buttons[1];
		if (_followChannel)
		{
			modernAppBarButton.Text = AppResources.FollowButtonUnchecked;
			((ContentControl)modernAppBarButton).Content = "\ue108";
		}
		else
		{
			modernAppBarButton.Text = AppResources.FollowButtonChecked;
			((ContentControl)modernAppBarButton).Content = "\ue109";
		}
	}

	private async void RemoveAds_Click(object sender, EventArgs e)
	{
		await AppVersion.BuyAds();
	}

	private void Retry_Click(object sender, RoutedEventArgs e)
	{
		((MainViewModel)((FrameworkElement)this).DataContext).LoadData(clearafter: true, null, delegate
		{
		});
	}

	private void DisplayGrid(bool fromscratch = false, bool withAnimation = true)
	{
		if (fromscratch)
		{
			MainViewModel mainViewModel = (MainViewModel)((FrameworkElement)this).DataContext;
			mainViewModel.CreateGroup();
			ListPostGrid.ItemsSource = mainViewModel.PostsGrouped;
		}
		((UIElement)ListPostGrid).Visibility = (Visibility)0;
		if (withAnimation)
		{
			((UIElement)ListPostGrid).Opacity = 0.0;
			StoryboardShowGrid.Begin();
		}
		else
		{
			((UIElement)ListPostGrid).Opacity = 1.0;
			((UIElement)ListPostGrid).Visibility = (Visibility)0;
			((UIElement)ListPost).Visibility = (Visibility)1;
		}
		StopVideos();
		DisplayHeader();
		foreach (ModernAppBar item in (PresentationFrameworkCollection<UIElement>)(object)((Panel)ModernAppBarContainer).Children)
		{
			try
			{
				((ContentControl)item.LeftButtons[0]).Content = "\ue292";
			}
			catch
			{
			}
		}
	}

	private void DisplayList(bool withAnimation = true)
	{
		//IL_002b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0030: Unknown result type (might be due to invalid IL or missing references)
		((UIElement)ListPost).Opacity = 0.0;
		((UIElement)ListPost).Visibility = (Visibility)0;
		CompositeTransform val = (CompositeTransform)((UIElement)ListPost).RenderTransform;
		double scaleX = (val.ScaleY = 1.0);
		val.ScaleX = scaleX;
		if (withAnimation)
		{
			StoryboardShowPost.Begin();
		}
		else
		{
			((UIElement)ListPostGrid).Visibility = (Visibility)1;
		}
		foreach (ModernAppBar item in (PresentationFrameworkCollection<UIElement>)(object)((Panel)ModernAppBarContainer).Children)
		{
			try
			{
				((ContentControl)item.LeftButtons[0]).Content = "\ue80a";
			}
			catch
			{
			}
		}
	}

	protected void AppBarSwitchView_Click(object sender, RoutedEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		if ((int)((UIElement)ListPost).Visibility == 0)
		{
			DisplayGrid(fromscratch: true);
		}
		else
		{
			DisplayList();
		}
	}

	private void PhotoGrid_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_00b0: Unknown result type (might be due to invalid IL or missing references)
		if (e.AddedItems.Count == 0 || e.AddedItems[0] == null)
		{
			return;
		}
		IPostRecord item = (IPostRecord)e.AddedItems[0];
		if (item is MorePost)
		{
			((MainViewModel)((FrameworkElement)this).DataContext).LoadData();
		}
		else
		{
			DisplayList(withAnimation: false);
			try
			{
				ListPost.ScrollTo((object)item);
				StoryboardShowPost.Begin();
			}
			catch
			{
				TimerHelper.ToTime(TimeSpan.FromMilliseconds(20.0), delegate
				{
					try
					{
						ListPost.ScrollTo((object)item);
					}
					catch
					{
					}
					StoryboardShowPost.Begin();
				});
			}
			StartTimer();
		}
		((LongListSelector)sender).SelectedItem = null;
	}

	private void InstaFollowing_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToFollowing();
	}

	private void Logo_Tap(object sender, GestureEventArgs e)
	{
		Reload(scrollToTop: true);
	}

	private void Reload(bool scrollToTop)
	{
		try
		{
			_forceDontHideHeader = true;
			DisplayHeader();
			MainViewModel mainViewModel = (MainViewModel)((FrameworkElement)this).DataContext;
			mainViewModel.Reload(delegate
			{
				_forceDontHideHeader = true;
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					DisplayHeader();
				});
				TimerHelper.ToTime(TimeSpan.FromSeconds(1.0), delegate
				{
					_forceDontHideHeader = false;
				});
			});
			if (scrollToTop)
			{
				ListPost.ScrollTo((object)mainViewModel.Posts.FirstOrDefault());
			}
		}
		catch
		{
		}
		TimerHelper.ToTime(TimeSpan.FromSeconds(1.0), delegate
		{
			_forceDontHideHeader = false;
		});
	}

	private void YourLikes_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForLikes(DatasProvider.Instance.CurrentUser.User.Id));
	}

	private async void TagMeRemove_Click(object sender, RoutedEventArgs e)
	{
	}

	private void CloseTagInfoPanel()
	{
		((UIElement)TagInfoPanel).Visibility = (Visibility)1;
	}

	private async void TagMeChangeVisibility_Click(object sender, RoutedEventArgs e)
	{
	}

	private void Post_SelectedMyselfTagPeople(object sender, IPostRecord post)
	{
	}

	private void Refresh_Click(object sender, RoutedEventArgs e)
	{
		_ = (MainViewModel)((FrameworkElement)this).DataContext;
		try
		{
			ListPost.ScrollTo(ListPost.ListHeader);
			ListPostGrid.ScrollTo(ListPostGrid.ListHeader);
		}
		catch
		{
		}
		Reload(scrollToTop: true);
	}

	private void SwitchAccount_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToManageAccount();
	}

	private void LongListSelector_ManipulationStateChanged(object sender, EventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Expected O, but got Unknown
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Expected I4, but got Unknown
		LongListSelector val = (LongListSelector)sender;
		ManipulationState manipulationState = val.ManipulationState;
		switch ((int)manipulationState)
		{
		case 2:
			if (Pullrefreshcontrol != null)
			{
				Pullrefreshcontrol.Reinit();
			}
			break;
		case 0:
			PlayCurrentVideo();
			StartTimer();
			break;
		case 1:
			StopTimer();
			break;
		}
	}

	private bool IsListPostTop()
	{
		if (_visiblePosts == null)
		{
			return true;
		}
		return !_visiblePosts.Any(delegate(PostControl f)
		{
			//IL_001e: Unknown result type (might be due to invalid IL or missing references)
			//IL_0023: Unknown result type (might be due to invalid IL or missing references)
			//IL_0028: Unknown result type (might be due to invalid IL or missing references)
			try
			{
				Point val = ((UIElement)f).TransformToVisual((UIElement)(object)ListPost).Transform(new Point(0.0, 0.0));
				return ((Point)(ref val)).Y < 36.0;
			}
			catch
			{
				return true;
			}
		});
	}

	private void NearbyShowCustomSettings_Click(object sender, EventArgs e)
	{
		((UIElement)NearbySettingsPanel).Visibility = (Visibility)0;
	}

	private int GetNearbyDistance()
	{
		int num = (int)((RangeBase)NearbySlide).Value;
		return num - num % 100;
	}

	private void NearbySettingsOK_Click(object sender, RoutedEventArgs e)
	{
	}

	private void NearbyMap_Click(object sender, EventArgs e)
	{
	}

	private void Nearby_Click(object sender, EventArgs e)
	{
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Invalid comparison between Unknown and I4
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (!instance.GeoPositionAsked)
		{
			if ((int)MessageBox.Show(AppResources.AskGeoPosition, AppResources.Nearby, (MessageBoxButton)1) != 1)
			{
				return;
			}
			instance.GeoPositionAsked = true;
			instance.Save();
		}
		NavigationServiceExt.ToTimeline("nearby");
	}

	private void Pin_Click(object sender, EventArgs e)
	{
		try
		{
			AppUtils.CreateHomeTile(((MainViewModel)((FrameworkElement)this).DataContext).Posts.FirstOrDefault());
		}
		catch
		{
		}
	}

	private void Direct_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToDirect();
	}

	private void LikeOrNot_Click(object sender, EventArgs e)
	{
		NavigationServiceExt.ToLikeOrNot();
	}

	private void ShowSaveMediaPanel()
	{
		((UIElement)LayoutRoot).IsHitTestVisible = false;
		((UIElement)SaveMediaLoadingPanel).Visibility = (Visibility)0;
		SaveMediaLoadingProgress.IsIndeterminate = true;
	}

	private void HideSaveMediaPanel()
	{
		((UIElement)LayoutRoot).IsHitTestVisible = true;
		((UIElement)SaveMediaLoadingPanel).Visibility = (Visibility)1;
		SaveMediaLoadingProgress.IsIndeterminate = false;
	}

	private void PostControl_OnSaveMedia(object sender, IPostRecord record)
	{
		SaveVideo(record);
	}

	private async void SaveVideo(IPostRecord post)
	{
		ShowSaveMediaPanel();
		try
		{
			await MediaSaver.Save(post);
		}
		catch
		{
		}
		finally
		{
			HideSaveMediaPanel();
		}
	}

	private void ChannelAdd_Click(object sender, RoutedEventArgs e)
	{
		((MainViewModel)((FrameworkElement)this).DataContext).FollowChannel(!_followChannel);
		_followChannel = !_followChannel;
		ManageFollowChannelAppbar();
	}

	[NotifyPropertyChangedInvocator]
	protected virtual void OnPropertyChanged([CallerMemberName] string propertyName = null)
	{
		this.PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
	}

	private void Camera_Hold(object sender, GestureEventArgs e)
	{
		e.Handled = true;
		_filePickerHelper.Launch();
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
		//IL_02ed: Unknown result type (might be due to invalid IL or missing references)
		//IL_02f7: Expected O, but got Unknown
		//IL_0303: Unknown result type (might be due to invalid IL or missing references)
		//IL_030d: Expected O, but got Unknown
		//IL_03b3: Unknown result type (might be due to invalid IL or missing references)
		//IL_03bd: Expected O, but got Unknown
		//IL_03c9: Unknown result type (might be due to invalid IL or missing references)
		//IL_03d3: Expected O, but got Unknown
		//IL_03df: Unknown result type (might be due to invalid IL or missing references)
		//IL_03e9: Expected O, but got Unknown
		//IL_03f5: Unknown result type (might be due to invalid IL or missing references)
		//IL_03ff: Expected O, but got Unknown
		//IL_040b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0415: Expected O, but got Unknown
		//IL_0421: Unknown result type (might be due to invalid IL or missing references)
		//IL_042b: Expected O, but got Unknown
		//IL_0437: Unknown result type (might be due to invalid IL or missing references)
		//IL_0441: Expected O, but got Unknown
		//IL_044d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0457: Expected O, but got Unknown
		//IL_0463: Unknown result type (might be due to invalid IL or missing references)
		//IL_046d: Expected O, but got Unknown
		//IL_0479: Unknown result type (might be due to invalid IL or missing references)
		//IL_0483: Expected O, but got Unknown
		//IL_04a5: Unknown result type (might be due to invalid IL or missing references)
		//IL_04af: Expected O, but got Unknown
		//IL_04bb: Unknown result type (might be due to invalid IL or missing references)
		//IL_04c5: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Main/MainPage.xaml", UriKind.Relative));
			Page = (PhoneApplicationPage)((FrameworkElement)this).FindName("Page");
			StoryboardChangeNotificationCounter = (Storyboard)((FrameworkElement)this).FindName("StoryboardChangeNotificationCounter");
			StoryboardShowPost = (Storyboard)((FrameworkElement)this).FindName("StoryboardShowPost");
			StoryboardShowGrid = (Storyboard)((FrameworkElement)this).FindName("StoryboardShowGrid");
			MainPanel = (Grid)((FrameworkElement)this).FindName("MainPanel");
			HeaderState = (VisualStateGroup)((FrameworkElement)this).FindName("HeaderState");
			Normal = (VisualState)((FrameworkElement)this).FindName("Normal");
			HeaderHidden = (VisualState)((FrameworkElement)this).FindName("HeaderHidden");
			DeviceType = (VisualStateGroup)((FrameworkElement)this).FindName("DeviceType");
			PhabletMode = (VisualState)((FrameworkElement)this).FindName("PhabletMode");
			RectBackground = (Rectangle)((FrameworkElement)this).FindName("RectBackground");
			RootPanel = (Grid)((FrameworkElement)this).FindName("RootPanel");
			ListPost = (LongListSelector)((FrameworkElement)this).FindName("ListPost");
			PullrefreshPanel = (StackPanel)((FrameworkElement)this).FindName("PullrefreshPanel");
			Pullrefreshcontrol = (PullRefreshControl)((FrameworkElement)this).FindName("Pullrefreshcontrol");
			EncodingButton = (Button)((FrameworkElement)this).FindName("EncodingButton");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			NoItemInFeedPanel = (Grid)((FrameworkElement)this).FindName("NoItemInFeedPanel");
			ListPostGrid = (LongListSelector)((FrameworkElement)this).FindName("ListPostGrid");
			ListPostGridFooter = (Grid)((FrameworkElement)this).FindName("ListPostGridFooter");
			LoadingPanel = (Grid)((FrameworkElement)this).FindName("LoadingPanel");
			MapHeader = (Grid)((FrameworkElement)this).FindName("MapHeader");
			VenueImage = (Image)((FrameworkElement)this).FindName("VenueImage");
			VenueName = (TextBlock)((FrameworkElement)this).FindName("VenueName");
			VenueAddress = (TextBlock)((FrameworkElement)this).FindName("VenueAddress");
			Header = (Grid)((FrameworkElement)this).FindName("Header");
			border = (Border)((FrameworkElement)this).FindName("border");
			DirectButton = (Button)((FrameworkElement)this).FindName("DirectButton");
			path = (Path)((FrameworkElement)this).FindName("path");
			NotifButton = (Button)((FrameworkElement)this).FindName("NotifButton");
			MeButton = (Button)((FrameworkElement)this).FindName("MeButton");
			MeButtonImage = (Image)((FrameworkElement)this).FindName("MeButtonImage");
			AdPanel = (Grid)((FrameworkElement)this).FindName("AdPanel");
			ModernAppBarContainer = (Grid)((FrameworkElement)this).FindName("ModernAppBarContainer");
			ModernVineAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernVineAppBar");
			ModernVineFavAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernVineFavAppBar");
			ModernInstaAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernInstaAppBar");
			ModernNearbyAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernNearbyAppBar");
			ModernOtherAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernOtherAppBar");
			ModernInstaFavAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernInstaFavAppBar");
			ModernVineChannelAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernVineChannelAppBar");
			SharePanel = (Grid)((FrameworkElement)this).FindName("SharePanel");
			stackPanel = (StackPanel)((FrameworkElement)this).FindName("stackPanel");
			ShareTwitter = (Button)((FrameworkElement)this).FindName("ShareTwitter");
			ShareFacebook = (Button)((FrameworkElement)this).FindName("ShareFacebook");
			ShareEmbedButton = (Button)((FrameworkElement)this).FindName("ShareEmbedButton");
			ShareNFC = (Button)((FrameworkElement)this).FindName("ShareNFC");
			ShareCopyLinkButton = (Button)((FrameworkElement)this).FindName("ShareCopyLinkButton");
			NearbySettingsPanel = (Grid)((FrameworkElement)this).FindName("NearbySettingsPanel");
			NearbySlide = (Slider)((FrameworkElement)this).FindName("NearbySlide");
			TagInfoPanel = (Grid)((FrameworkElement)this).FindName("TagInfoPanel");
			TagMeAcceptToggle = (ToggleSwitch)((FrameworkElement)this).FindName("TagMeAcceptToggle");
			SaveMediaLoadingPanel = (Grid)((FrameworkElement)this).FindName("SaveMediaLoadingPanel");
			SaveMediaLoadingProgress = (ProgressBar)((FrameworkElement)this).FindName("SaveMediaLoadingProgress");
		}
	}

	[CompilerGenerated]
	private void _003C_003En__FabricatedMethod0(NavigationEventArgs e)
	{
		((Page)this).OnNavigatedTo(e);
	}
}
