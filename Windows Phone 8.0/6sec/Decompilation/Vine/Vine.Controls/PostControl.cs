using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Net;
using System.Runtime.CompilerServices;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using System.Windows.Threading;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Messaging;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microsoft.Xna.Framework.Input.Touch;
using Vine.Converters;
using Vine.Datas;
using Vine.Pages.Chat.Models;
using Vine.Resources;
using Vine.Services;
using Vine.Services.Models;
using Vine.Services.Response;
using Vine.Utils;
using Windows.System;

namespace Vine.Controls;

public class PostControl : UserControl
{
	private static string DirectoryPath;

	private PostControlBack _postControlBack;

	public static readonly DependencyProperty PostForegroundProperty;

	private DispatcherTimer _loopDispatcherTimer;

	public static readonly DependencyProperty UseGestureProperty;

	public static readonly DependencyProperty FullScreenProperty;

	public static readonly DependencyProperty IsFullCommentProperty;

	private bool _manipulationInProgress;

	private double deltaChange;

	private double _deltaManipulation;

	[CompilerGenerated]
	private RoutedEventHandler m_Comment;

	public static readonly DependencyProperty PostProperty;

	private ICommand HashTagCommand;

	private static Regex regHashTag;

	private HttpWebRequest videorequest;

	private static object lockreq;

	private MediaElement _media;

	private Timer _timer;

	private bool _previousstatePlay;

	private ProgressRing _progressRing;

	private bool _usertagDisplayed;

	private DispatcherTimer _dispatcherTimerPicture;

	private ContextMenu _contextmenu;

	internal bool _isDirect;

	private bool _forceplaydisplay;

	internal UserControl RootControl;

	internal Storyboard StoryboardFrontToBack;

	internal Storyboard StoryboardBackToFront;

	internal Storyboard StoryboardFrontLike;

	internal Storyboard StoryboardFrontDisLike;

	internal Storyboard StoryboardUserTagShow;

	internal Storyboard StoryboardUserTagHide;

	internal Grid LayoutRoot;

	internal Grid FrontPanel;

	internal Image Image;

	internal Button RevinePanel;

	internal TextBlock RevineText;

	internal Image Avatar;

	internal TextBlock Date;

	internal TextBlock NameTxt;

	internal RichTextBox DescriptionTxt;

	internal Button PlacePanel;

	internal TextBlock PlaceText;

	internal Grid PostBottomInfo;

	internal TextBlock LittleLikeIcon;

	internal TextBlock LikeLabelTxt;

	internal TextBlock CommentLabelTxt;

	internal StackPanel LoopPanel;

	internal TextBlock LoopTxt;

	internal Button ShowMenuButton;

	internal Rectangle ShadowF1;

	internal Rectangle ShadowF2;

	internal Canvas VideoContainer;

	internal Canvas UserTaggedContainer;

	internal Path ButtonPlay;

	internal Path ButtonPause;

	internal Path FrontLikedPath;

	internal Path FrontDisLikedPath;

	internal Path HasUserTagged;

	internal Grid BackPanel;

	internal Rectangle FullScreenSeparator;

	private bool _contentLoaded;

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

	public bool UseGesture
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(UseGestureProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(UseGestureProperty, (object)value);
		}
	}

	public bool FullScreen
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(FullScreenProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(FullScreenProperty, (object)value);
		}
	}

	public bool IsFullComment
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(IsFullCommentProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(IsFullCommentProperty, (object)value);
		}
	}

	public IPostRecord Post
	{
		get
		{
			return (IPostRecord)((DependencyObject)this).GetValue(PostProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(PostProperty, (object)value);
		}
	}

	public ProgressBar _progress { get; set; }

	public RelayCommand<string> PeopleTagCommand { get; set; }

	public RelayCommand<string> UriCommand { get; set; }

	public Vine.Datas.Datas Data { get; set; }

	public event SelectHashTagHandler SelectedHashTag;

	public event SelectMyselfTagHandler SelectedMyselfTagPeople;

	public event ShareHandler Share;

	public event SaveMediaHandler SaveMedia;

	public event RoutedEventHandler Comment
	{
		[CompilerGenerated]
		add
		{
			//IL_0010: Unknown result type (might be due to invalid IL or missing references)
			//IL_0016: Expected O, but got Unknown
			RoutedEventHandler val = this.m_Comment;
			RoutedEventHandler val2;
			do
			{
				val2 = val;
				RoutedEventHandler value2 = (RoutedEventHandler)Delegate.Combine((Delegate)(object)val2, (Delegate)(object)value);
				val = Interlocked.CompareExchange(ref this.m_Comment, value2, val2);
			}
			while (val != val2);
		}
		[CompilerGenerated]
		remove
		{
			//IL_0010: Unknown result type (might be due to invalid IL or missing references)
			//IL_0016: Expected O, but got Unknown
			RoutedEventHandler val = this.m_Comment;
			RoutedEventHandler val2;
			do
			{
				val2 = val;
				RoutedEventHandler value2 = (RoutedEventHandler)Delegate.Remove((Delegate)(object)val2, (Delegate)(object)value);
				val = Interlocked.CompareExchange(ref this.m_Comment, value2, val2);
			}
			while (val != val2);
		}
	}

	static PostControl()
	{
		//IL_0024: Unknown result type (might be due to invalid IL or missing references)
		//IL_002e: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_005e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0068: Expected O, but got Unknown
		//IL_0093: Unknown result type (might be due to invalid IL or missing references)
		//IL_009d: Expected O, but got Unknown
		//IL_0098: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a2: Expected O, but got Unknown
		//IL_00cd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d7: Expected O, but got Unknown
		//IL_00d2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00dc: Expected O, but got Unknown
		//IL_0102: Unknown result type (might be due to invalid IL or missing references)
		//IL_010c: Expected O, but got Unknown
		//IL_0107: Unknown result type (might be due to invalid IL or missing references)
		//IL_0111: Expected O, but got Unknown
		DirectoryPath = "tmpVideo";
		PostForegroundProperty = DependencyProperty.Register("PostForeground", typeof(Brush), typeof(PostControl), new PropertyMetadata((object)null));
		UseGestureProperty = DependencyProperty.Register("UseGesture", typeof(bool), typeof(PostControl), new PropertyMetadata((object)false, new PropertyChangedCallback(UseGestureCallback)));
		FullScreenProperty = DependencyProperty.Register("FullScreen", typeof(bool), typeof(PostControl), new PropertyMetadata((object)false, new PropertyChangedCallback(FullScreenCallback)));
		IsFullCommentProperty = DependencyProperty.Register("IsFullComment", typeof(bool), typeof(PostControl), new PropertyMetadata((object)false, new PropertyChangedCallback(IsFullCommentCallback)));
		PostProperty = DependencyProperty.Register("Post", typeof(IPostRecord), typeof(PostControl), new PropertyMetadata((object)null, new PropertyChangedCallback(PostCallback)));
		regHashTag = new Regex("\\#\\w+", RegexOptions.Compiled | RegexOptions.Singleline);
		lockreq = new object();
		using IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication();
		if (!isolatedStorageFile.DirectoryExists(DirectoryPath))
		{
			isolatedStorageFile.CreateDirectory(DirectoryPath);
		}
	}

	public PostControl()
	{
		//IL_0036: Unknown result type (might be due to invalid IL or missing references)
		//IL_0055: Unknown result type (might be due to invalid IL or missing references)
		//IL_005f: Expected O, but got Unknown
		//IL_00a7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ac: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c5: Expected O, but got Unknown
		//IL_015d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0162: Unknown result type (might be due to invalid IL or missing references)
		//IL_016c: Expected O, but got Unknown
		//IL_0191: Unknown result type (might be due to invalid IL or missing references)
		//IL_019b: Expected O, but got Unknown
		//IL_01f4: Unknown result type (might be due to invalid IL or missing references)
		//IL_01fa: Unknown result type (might be due to invalid IL or missing references)
		//IL_022a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0243: Unknown result type (might be due to invalid IL or missing references)
		//IL_024d: Expected O, but got Unknown
		//IL_0255: Unknown result type (might be due to invalid IL or missing references)
		//IL_025f: Expected O, but got Unknown
		Data = DatasProvider.Instance;
		((FrameworkElement)this).Margin = new Thickness(0.0, 24.0, 0.0, 0.0);
		PostForeground = (Brush)Application.Current.Resources[(object)"PrincBrush"];
		InitializeComponent();
		((Timeline)StoryboardBackToFront).Completed += StoryboardBackToFront_Completed;
		HashTagCommand = new RelayCommand<string>(delegate(string s)
		{
			this.SelectedHashTag(this, s);
		});
		if (_dispatcherTimerPicture != null)
		{
			_dispatcherTimerPicture.Stop();
		}
		_dispatcherTimerPicture = new DispatcherTimer
		{
			Interval = TimeSpan.FromMilliseconds(200.0)
		};
		_dispatcherTimerPicture.Tick += delegate
		{
			_dispatcherTimerPicture.Stop();
			PictureTapAction();
		};
		PeopleTagCommand = new RelayCommand<string>(delegate(string userid)
		{
			NavigationServiceExt.ToProfileFromUsername(userid);
		});
		UriCommand = new RelayCommand<string>(delegate(string uri)
		{
			Launcher.LaunchUriAsync(new Uri(uri));
		});
		((Timeline)StoryboardFrontLike).Completed += delegate
		{
			((UIElement)FrontLikedPath).Visibility = (Visibility)1;
		};
		((Timeline)StoryboardFrontDisLike).Completed += delegate
		{
			((UIElement)FrontDisLikedPath).Visibility = (Visibility)1;
		};
		ContextMenu contextMenu = new ContextMenu();
		((Control)contextMenu).Background = (Brush)new SolidColorBrush(Colors.White);
		contextMenu.IsZoomEnabled = false;
		_contextmenu = contextMenu;
		MenuItem menuItem = new MenuItem
		{
			Header = AppResources.Share
		};
		menuItem.Click += new RoutedEventHandler(MenuShare_Click);
		((PresentationFrameworkCollection<object>)(object)((ItemsControl)_contextmenu).Items).Add((object)menuItem);
		ContextMenuService.SetContextMenu((DependencyObject)(object)FrontPanel, _contextmenu);
		if (Data.UseFullScreenPost)
		{
			FullScreen = true;
		}
		else
		{
			((UIElement)FullScreenSeparator).Visibility = (Visibility)1;
			UseGestureCallback(use: true);
			Rectangle shadowF = ShadowF1;
			Rectangle shadowF2 = ShadowF2;
			Visibility visibility = (Visibility)0;
			((UIElement)shadowF2).Visibility = (Visibility)0;
			((UIElement)shadowF).Visibility = visibility;
			((FrameworkElement)BackPanel).Margin = new Thickness(-5.0, 0.0, 0.0, 0.0);
			UseGesture = true;
		}
		((FrameworkElement)this).Unloaded += new RoutedEventHandler(PostControl_Unloaded);
		((FrameworkElement)this).Loaded += new RoutedEventHandler(PostControl_Loaded);
	}

	private void StoryboardBackToFront_Completed(object sender, EventArgs e)
	{
		_postControlBack = null;
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)BackPanel).Children).Clear();
		PlayVideo();
	}

	private void PostControl_Loaded(object sender, RoutedEventArgs e)
	{
		if (_loopDispatcherTimer != null)
		{
			_loopDispatcherTimer.Start();
		}
	}

	private void PostControl_Unloaded(object sender, RoutedEventArgs e)
	{
		if (_loopDispatcherTimer != null)
		{
			_loopDispatcherTimer.Stop();
		}
	}

	private void _loopDispatcherTimer_Tick(object sender, EventArgs e)
	{
		try
		{
			if (Post is VinePostRecord { loops: not null } vinePostRecord)
			{
				LoopTxt.Text = ((int)(vinePostRecord.loops.Count + (DateTime.Now - vinePostRecord.loops.CountStarted).TotalSeconds * vinePostRecord.loops.Velocity)).ToString();
			}
		}
		catch
		{
		}
	}

	public void CreateBackPanel(bool fromDotsMenu)
	{
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		PostControlBack postControlBack = new PostControlBack(this, fromDotsMenu);
		if (Post != null)
		{
			postControlBack.SetRecord(Post);
		}
		Visibility visibility = (Visibility)(UseGesture ? 1 : 0);
		((UIElement)postControlBack.BackShowMenuButton).Visibility = visibility;
		_postControlBack = postControlBack;
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)BackPanel).Children).Clear();
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)BackPanel).Children).Add((UIElement)(object)postControlBack);
	}

	private void HideShadow()
	{
		//IL_003a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0040: Unknown result type (might be due to invalid IL or missing references)
		//IL_0055: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Unknown result type (might be due to invalid IL or missing references)
		//IL_0026: Unknown result type (might be due to invalid IL or missing references)
		Visibility visibility;
		if (_postControlBack != null)
		{
			Rectangle shadowB = _postControlBack.ShadowB1;
			Rectangle shadowB2 = _postControlBack.ShadowB2;
			visibility = (Visibility)1;
			((UIElement)shadowB2).Visibility = (Visibility)1;
			((UIElement)shadowB).Visibility = visibility;
		}
		Rectangle shadowF = ShadowF1;
		Rectangle shadowF2 = ShadowF2;
		visibility = (Visibility)1;
		((UIElement)shadowF2).Visibility = (Visibility)1;
		((UIElement)shadowF).Visibility = visibility;
		((FrameworkElement)BackPanel).Margin = new Thickness(0.0);
	}

	private static void UseGestureCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((PostControl)(object)d).UseGestureCallback((bool)((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
	}

	private void UseGestureCallback(bool use)
	{
		if (use)
		{
			if (_postControlBack != null)
			{
				((UIElement)_postControlBack.BackShowMenuButton).Visibility = (Visibility)1;
			}
			((UIElement)ShowMenuButton).Visibility = (Visibility)1;
			((UIElement)this).ManipulationCompleted += PostControl_ManipulationCompleted;
			((UIElement)this).ManipulationDelta += PostControl_ManipulationDelta;
			((UIElement)this).ManipulationStarted += PostControl_ManipulationStarted;
		}
		else
		{
			if (_postControlBack != null)
			{
				((UIElement)_postControlBack.BackShowMenuButton).Visibility = (Visibility)0;
			}
			((UIElement)ShowMenuButton).Visibility = (Visibility)0;
			((UIElement)this).ManipulationCompleted -= PostControl_ManipulationCompleted;
			((UIElement)this).ManipulationDelta -= PostControl_ManipulationDelta;
			((UIElement)this).ManipulationStarted -= PostControl_ManipulationStarted;
		}
	}

	private static void FullScreenCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((PostControl)(object)d).FullScreenCallback((bool)((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
	}

	private void FullScreenCallback(bool isfullscreen)
	{
		//IL_019d: Unknown result type (might be due to invalid IL or missing references)
		//IL_01f3: Unknown result type (might be due to invalid IL or missing references)
		//IL_0218: Unknown result type (might be due to invalid IL or missing references)
		//IL_002b: Unknown result type (might be due to invalid IL or missing references)
		//IL_005f: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00da: Unknown result type (might be due to invalid IL or missing references)
		//IL_027e: Unknown result type (might be due to invalid IL or missing references)
		//IL_02b2: Unknown result type (might be due to invalid IL or missing references)
		//IL_02e6: Unknown result type (might be due to invalid IL or missing references)
		//IL_024a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0132: Unknown result type (might be due to invalid IL or missing references)
		//IL_0138: Unknown result type (might be due to invalid IL or missing references)
		//IL_0168: Unknown result type (might be due to invalid IL or missing references)
		//IL_010c: Unknown result type (might be due to invalid IL or missing references)
		if (isfullscreen)
		{
			((FrameworkElement)this).Margin = new Thickness(0.0, 0.0, 0.0, 0.0);
			((FrameworkElement)LayoutRoot).Margin = new Thickness(0.0, 0.0, 0.0, 0.0);
			Image image = Image;
			double width = (((FrameworkElement)Image).Height = 480.0);
			((FrameworkElement)image).Width = width;
			((FrameworkElement)Image).Margin = new Thickness(0.0, 0.0, 0.0, 0.0);
			((PresentationFrameworkCollection<ColumnDefinition>)(object)FrontPanel.ColumnDefinitions)[0].Width = new GridLength(1.0, (GridUnitType)2);
			if (_postControlBack != null)
			{
				((PresentationFrameworkCollection<ColumnDefinition>)(object)_postControlBack.BackPanel.ColumnDefinitions)[1].Width = new GridLength(1.0, (GridUnitType)2);
			}
			Canvas userTaggedContainer = UserTaggedContainer;
			Canvas videoContainer = VideoContainer;
			Thickness margin = default(Thickness);
			((Thickness)(ref margin))._002Ector(0.0);
			((FrameworkElement)videoContainer).Margin = margin;
			((FrameworkElement)userTaggedContainer).Margin = margin;
			((FrameworkElement)HasUserTagged).Margin = new Thickness(12.0, 438.0, 0.0, 0.0);
		}
		else
		{
			((FrameworkElement)LayoutRoot).Margin = new Thickness(14.0, 0.0, 0.0, 0.0);
			Image image2 = Image;
			double width = (((FrameworkElement)Image).Height = 432.0);
			((FrameworkElement)image2).Width = width;
			((FrameworkElement)Image).Margin = new Thickness(0.0, 10.0, 0.0, 0.0);
			((PresentationFrameworkCollection<ColumnDefinition>)(object)FrontPanel.ColumnDefinitions)[0].Width = new GridLength(452.0, (GridUnitType)1);
			if (_postControlBack != null)
			{
				((PresentationFrameworkCollection<ColumnDefinition>)(object)_postControlBack.BackPanel.ColumnDefinitions)[1].Width = new GridLength(452.0, (GridUnitType)1);
			}
			((FrameworkElement)UserTaggedContainer).Margin = new Thickness(13.0, 12.0, 0.0, 0.0);
			((FrameworkElement)VideoContainer).Margin = new Thickness(10.0, 10.0, 0.0, 0.0);
			((FrameworkElement)HasUserTagged).Margin = new Thickness(22.0, 399.0, 0.0, 0.0);
		}
	}

	private static void IsFullCommentCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((PostControl)(object)d).IsFullCommentCallback((bool)((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
	}

	private void IsFullCommentCallback(bool isfullscreen)
	{
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0047: Unknown result type (might be due to invalid IL or missing references)
		//IL_004c: Unknown result type (might be due to invalid IL or missing references)
		//IL_005b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0060: Unknown result type (might be due to invalid IL or missing references)
		//IL_006e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0073: Unknown result type (might be due to invalid IL or missing references)
		//IL_008b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0090: Unknown result type (might be due to invalid IL or missing references)
		//IL_0098: Unknown result type (might be due to invalid IL or missing references)
		FullScreen = true;
		HideShadow();
		((FrameworkElement)LayoutRoot).Margin = new Thickness(0.0);
		((UIElement)FullScreenSeparator).Visibility = (Visibility)1;
		Image avatar = Avatar;
		Thickness margin = ((FrameworkElement)Avatar).Margin;
		double num = 14.0 + ((Thickness)(ref margin)).Left;
		margin = ((FrameworkElement)Avatar).Margin;
		double top = ((Thickness)(ref margin)).Top;
		margin = ((FrameworkElement)Avatar).Margin;
		double num2 = ((Thickness)(ref margin)).Right + 6.0;
		margin = ((FrameworkElement)Avatar).Margin;
		((FrameworkElement)avatar).Margin = new Thickness(num, top, num2, ((Thickness)(ref margin)).Bottom);
	}

	private void PostControl_ManipulationDelta(object sender, ManipulationDeltaEventArgs e)
	{
		if (!_manipulationInProgress)
		{
			return;
		}
		double num = 0.0;
		while (TouchPanel.IsGestureAvailable)
		{
			try
			{
				GestureSample gestureSample = TouchPanel.ReadGesture();
				GestureType gestureType = gestureSample.GestureType;
				if (gestureType == GestureType.HorizontalDrag)
				{
					num += (double)gestureSample.Delta.X;
				}
			}
			catch
			{
				return;
			}
		}
		if (num != 0.0)
		{
			deltaChange += num;
			double num2 = 0.0 - (_deltaManipulation + deltaChange / 2.0) % 360.0;
			double num3 = Math.Abs(num2);
			if (num3 >= 90.0 && num3 < 270.0)
			{
				AnimChangePanelBack(num2);
			}
			else
			{
				AnimChangePanelFront(num2);
			}
		}
	}

	private void AnimChangePanelFront(double angle)
	{
		//IL_0023: Unknown result type (might be due to invalid IL or missing references)
		((UIElement)FrontPanel).Visibility = (Visibility)0;
		((UIElement)BackPanel).Visibility = (Visibility)1;
		((PlaneProjection)((UIElement)FrontPanel).Projection).RotationY = angle;
	}

	private void AnimChangePanelBack(double angle)
	{
		//IL_004f: Unknown result type (might be due to invalid IL or missing references)
		((UIElement)BackPanel).Visibility = (Visibility)0;
		if (((FrameworkElement)FrontPanel).ActualHeight > 0.0)
		{
			((FrameworkElement)BackPanel).Height = ((FrameworkElement)FrontPanel).ActualHeight;
		}
		((UIElement)FrontPanel).Visibility = (Visibility)1;
		((PlaneProjection)((UIElement)BackPanel).Projection).RotationY = angle - 180.0;
	}

	private void PostControl_ManipulationStarted(object sender, ManipulationStartedEventArgs e)
	{
		//IL_0041: Unknown result type (might be due to invalid IL or missing references)
		//IL_0056: Unknown result type (might be due to invalid IL or missing references)
		TouchPanel.EnabledGestures = GestureType.HorizontalDrag;
		deltaChange = 0.0;
		_manipulationInProgress = true;
		if (_manipulationInProgress)
		{
			if (_postControlBack == null)
			{
				CreateBackPanel(fromDotsMenu: false);
			}
			PauseVideo();
			_deltaManipulation = (((int)((UIElement)BackPanel).Visibility == 0) ? ((((PlaneProjection)((UIElement)BackPanel).Projection).RotationY > 0.0) ? 180 : (-180)) : 0);
		}
	}

	private void PostControl_ManipulationCompleted(object sender, ManipulationCompletedEventArgs e)
	{
		//IL_0020: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		TouchPanel.EnabledGestures = GestureType.None;
		_manipulationInProgress = false;
		((UIElement)FrontPanel).IsHitTestVisible = true;
		Point linearVelocity = e.FinalVelocities.LinearVelocity;
		AnimRotation(((Point)(ref linearVelocity)).X);
	}

	public void FrontToBack(bool fromDotsMenu)
	{
		if (_postControlBack == null)
		{
			CreateBackPanel(fromDotsMenu);
		}
		PauseVideo();
		StoryboardFrontToBack.Begin();
	}

	public void ResetFrontToBack()
	{
		StoryboardFrontToBack.Stop();
	}

	private void AnimRotation(double velocity)
	{
		//IL_0026: Unknown result type (might be due to invalid IL or missing references)
		//IL_002c: Invalid comparison between Unknown and I4
		double num = Math.Abs(deltaChange / 2.0 % 360.0);
		if ((int)((UIElement)BackPanel).Visibility == 1)
		{
			if ((num <= 90.0 && Math.Abs(velocity) > 2800.0) || (num > 70.0 && num <= 90.0))
			{
				if (_postControlBack == null)
				{
					CreateBackPanel(fromDotsMenu: true);
				}
				PauseVideo();
				AnimChangePanelBack(((!(deltaChange > 0.0)) ? 1 : (-1)) * 90);
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					AnimRotationInterBack();
				});
			}
			else
			{
				AnimRotationInterFront(num);
			}
		}
		else if ((num <= 90.0 && Math.Abs(velocity) > 2800.0) || (num > 70.0 && num <= 90.0))
		{
			AnimChangePanelFront(((deltaChange > 0.0) ? 1 : (-1)) * 90);
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				AnimRotationInterFront(90.0);
			});
		}
		else
		{
			AnimRotationInterBack();
		}
	}

	private void AnimRotationInterBack()
	{
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0061: Unknown result type (might be due to invalid IL or missing references)
		//IL_007c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0087: Expected O, but got Unknown
		//IL_0094: Unknown result type (might be due to invalid IL or missing references)
		//IL_009e: Expected O, but got Unknown
		//IL_009e: Unknown result type (might be due to invalid IL or missing references)
		//IL_00aa: Unknown result type (might be due to invalid IL or missing references)
		PlaneProjection val = (PlaneProjection)((UIElement)BackPanel).Projection;
		if (Math.Abs(val.RotationY) % 360.0 != 0.0)
		{
			Storyboard val2 = new Storyboard();
			DoubleAnimation val3 = new DoubleAnimation
			{
				To = ((val.RotationY < -180.0) ? (-360) : 0),
				Duration = Duration.op_Implicit(TimeSpan.FromMilliseconds(Math.Abs(val.RotationY) * 1.1))
			};
			Storyboard.SetTarget((Timeline)(object)val3, (DependencyObject)(object)val);
			Storyboard.SetTargetProperty((Timeline)(object)val3, new PropertyPath((object)PlaneProjection.RotationYProperty));
			((PresentationFrameworkCollection<Timeline>)(object)val2.Children).Add((Timeline)(object)val3);
			val2.Begin();
			((Timeline)val2).Completed += delegate
			{
				//IL_000f: Unknown result type (might be due to invalid IL or missing references)
				//IL_0015: Invalid comparison between Unknown and I4
				_previousstatePlay = _media != null && (int)_media.CurrentState == 3;
				PauseVideo();
				RemoveProgressRing();
			};
		}
	}

	private void AnimRotationInterFront(double manipulation)
	{
		//IL_001f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Expected O, but got Unknown
		//IL_0029: Unknown result type (might be due to invalid IL or missing references)
		//IL_002e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Unknown result type (might be due to invalid IL or missing references)
		//IL_0070: Unknown result type (might be due to invalid IL or missing references)
		//IL_008b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0096: Expected O, but got Unknown
		//IL_00a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ad: Expected O, but got Unknown
		//IL_00ad: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b9: Unknown result type (might be due to invalid IL or missing references)
		PlaneProjection val = (PlaneProjection)((UIElement)FrontPanel).Projection;
		if (val == null)
		{
			return;
		}
		Storyboard val2 = new Storyboard();
		DoubleAnimation val3 = new DoubleAnimation
		{
			To = ((val.RotationY > 180.0) ? 360 : ((val.RotationY < -180.0) ? (-360) : 0)),
			Duration = Duration.op_Implicit(TimeSpan.FromMilliseconds(Math.Abs(val.RotationY) * 1.1))
		};
		Storyboard.SetTarget((Timeline)(object)val3, (DependencyObject)(object)val);
		Storyboard.SetTargetProperty((Timeline)(object)val3, new PropertyPath((object)PlaneProjection.RotationYProperty));
		((PresentationFrameworkCollection<Timeline>)(object)val2.Children).Add((Timeline)(object)val3);
		((Timeline)val2).Completed += delegate
		{
			if (_previousstatePlay && manipulation > 40.0)
			{
				PlayVideo();
			}
			_postControlBack = null;
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)BackPanel).Children).Clear();
		};
		val2.Begin();
	}

	private static void PostCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((PostControl)(object)d).PostCallback((IPostRecord)((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
	}

	private void PostCallback(IPostRecord record)
	{
		//IL_00bb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d9: Unknown result type (might be due to invalid IL or missing references)
		//IL_0104: Unknown result type (might be due to invalid IL or missing references)
		//IL_0157: Unknown result type (might be due to invalid IL or missing references)
		//IL_015d: Expected O, but got Unknown
		//IL_0134: Unknown result type (might be due to invalid IL or missing references)
		//IL_0139: Unknown result type (might be due to invalid IL or missing references)
		//IL_0146: Expected O, but got Unknown
		//IL_04e9: Unknown result type (might be due to invalid IL or missing references)
		//IL_0546: Unknown result type (might be due to invalid IL or missing references)
		//IL_054b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0553: Expected O, but got Unknown
		//IL_0519: Unknown result type (might be due to invalid IL or missing references)
		//IL_051e: Unknown result type (might be due to invalid IL or missing references)
		//IL_052b: Expected O, but got Unknown
		//IL_02b9: Unknown result type (might be due to invalid IL or missing references)
		//IL_02be: Unknown result type (might be due to invalid IL or missing references)
		//IL_02d7: Expected O, but got Unknown
		//IL_0375: Unknown result type (might be due to invalid IL or missing references)
		//IL_037a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0381: Unknown result type (might be due to invalid IL or missing references)
		//IL_038e: Unknown result type (might be due to invalid IL or missing references)
		//IL_039b: Unknown result type (might be due to invalid IL or missing references)
		//IL_03a2: Unknown result type (might be due to invalid IL or missing references)
		//IL_03a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_03ad: Unknown result type (might be due to invalid IL or missing references)
		//IL_03c2: Unknown result type (might be due to invalid IL or missing references)
		//IL_03cc: Expected O, but got Unknown
		//IL_03cc: Unknown result type (might be due to invalid IL or missing references)
		//IL_03e1: Unknown result type (might be due to invalid IL or missing references)
		//IL_03eb: Expected O, but got Unknown
		//IL_03ed: Expected O, but got Unknown
		//IL_0355: Unknown result type (might be due to invalid IL or missing references)
		//IL_035a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0375: Expected O, but got Unknown
		//IL_044b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0450: Unknown result type (might be due to invalid IL or missing references)
		//IL_0462: Expected O, but got Unknown
		_usertagDisplayed = false;
		RemoveProgressRing();
		if (_media != null)
		{
			Stop();
		}
		if (record is ChatPostRecord)
		{
			((UIElement)PostBottomInfo).Visibility = (Visibility)1;
			((UIElement)this).ManipulationCompleted -= PostControl_ManipulationCompleted;
			((UIElement)this).ManipulationDelta -= PostControl_ManipulationDelta;
			((UIElement)this).ManipulationStarted -= PostControl_ManipulationStarted;
		}
		_postControlBack = null;
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)BackPanel).Children).Clear();
		((UIElement)BackPanel).Visibility = (Visibility)1;
		((UIElement)FrontPanel).Visibility = (Visibility)0;
		((UIElement)ButtonPause).Visibility = (Visibility)1;
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)UserTaggedContainer).Children).Clear();
		((PlaneProjection)((UIElement)FrontPanel).Projection).RotationY = 0.0;
		((PlaneProjection)((UIElement)BackPanel).Projection).RotationY = 0.0;
		if (Avatar.Source != null)
		{
			((BitmapImage)Avatar.Source).UriSource = null;
			Avatar.Source = null;
		}
		if (record.AvatarUrl != null)
		{
			Avatar.Source = (ImageSource)new BitmapImage(new Uri(record.AvatarUrl))
			{
				CreateOptions = (BitmapCreateOptions)18
			};
		}
		NameTxt.Text = record.UserName;
		Paragraph val = new Paragraph();
		int num = 0;
		string description = record.Description;
		if (description != null)
		{
			List<Tuple<ICommand, int, string, string>> list = new List<Tuple<ICommand, int, string, string>>();
			foreach (Match item in regHashTag.Matches(description))
			{
				list.Add(new Tuple<ICommand, int, string, string>(HashTagCommand, item.Index, item.Value, item.Value.Substring(1)));
			}
			if (record is VinePostRecord)
			{
				VinePostRecord vinePostRecord = (VinePostRecord)record;
				if (vinePostRecord.entities != null)
				{
					foreach (Entity entity in vinePostRecord.entities)
					{
						if (entity.type == "mention" && entity.title != null)
						{
							list.Add(new Tuple<ICommand, int, string, string>(PeopleTagCommand, entity.range[0], entity.title, entity.id));
						}
					}
				}
				((UIElement)LoopPanel).Visibility = (Visibility)0;
				LoopTxt.Text = vinePostRecord.loops.Count.ToString();
				if (_loopDispatcherTimer != null)
				{
					_loopDispatcherTimer.Stop();
				}
				_loopDispatcherTimer = new DispatcherTimer
				{
					Interval = TimeSpan.FromSeconds(4.0)
				};
				_loopDispatcherTimer.Tick += _loopDispatcherTimer_Tick;
				_loopDispatcherTimer.Start();
			}
			else
			{
				((UIElement)LoopPanel).Visibility = (Visibility)1;
				_loopDispatcherTimer = null;
			}
			foreach (Tuple<ICommand, int, string, string> item2 in list.OrderBy((Tuple<ICommand, int, string, string> l) => l.Item2))
			{
				if (item2.Item2 > num)
				{
					((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)new Run
					{
						Text = description.Substring(num, item2.Item2 - num)
					});
				}
				Hyperlink val2 = new Hyperlink
				{
					MouseOverTextDecorations = null,
					Command = item2.Item1,
					CommandParameter = item2.Item4,
					TextDecorations = null,
					FontWeight = FontWeights.SemiBold,
					Foreground = (Brush)Application.Current.Resources[(object)"DarkGreyBrush"],
					MouseOverForeground = (Brush)Application.Current.Resources[(object)"DarkGreyBrush"]
				};
				((Span)val2).Inlines.Add(item2.Item3);
				((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)(object)val2);
				num = item2.Item2 + item2.Item3.Length;
			}
			if (num < description.Length)
			{
				((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)new Run
				{
					Text = description.Substring(num)
				});
			}
		}
		((PresentationFrameworkCollection<Block>)(object)DescriptionTxt.Blocks).Clear();
		((PresentationFrameworkCollection<Block>)(object)DescriptionTxt.Blocks).Add((Block)(object)val);
		if (record.Thumb.Contains("vineapp"))
		{
			Uri uri = new Uri(record.Thumb);
			LowProfileImageDownloader.DownloadImage(uri, delegate(Uri uriresponse, string localfile)
			{
				//IL_0031: Unknown result type (might be due to invalid IL or missing references)
				//IL_0037: Expected O, but got Unknown
				if (uri.OriginalString == Post.Thumb)
				{
					using (IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication())
					{
						using IsolatedStorageFileStream source = isolatedStorageFile.OpenFile(localfile, FileMode.Open);
						BitmapImage val4 = new BitmapImage();
						((BitmapSource)val4).SetSource((Stream)source);
						Image.Source = (ImageSource)(object)val4;
					}
				}
			});
		}
		else
		{
			if (Image.Source != null)
			{
				((BitmapImage)Image.Source).UriSource = null;
				Image.Source = null;
			}
			if (record.Thumb != null)
			{
				Image.Source = (ImageSource)new BitmapImage(new Uri(record.Thumb))
				{
					CreateOptions = (BitmapCreateOptions)18
				};
			}
		}
		Date.Text = ShortTimerConverter.ToShortTimer(record.Date);
		Binding val3 = new Binding("NbrLikes")
		{
			Source = record
		};
		((FrameworkElement)LikeLabelTxt).SetBinding(TextBlock.TextProperty, val3);
		CommentLabelTxt.Text = record.NbrComments.ToString();
		ManageLikeButton();
		if (_postControlBack != null)
		{
			_postControlBack.SetRecord(record);
		}
		if (_postControlBack != null)
		{
			if (Post.IsMyPost)
			{
				((UIElement)_postControlBack.Option2Bis).Visibility = (Visibility)1;
			}
			else
			{
				((UIElement)_postControlBack.Option2Bis).Visibility = (Visibility)0;
			}
		}
		((UIElement)PlacePanel).Visibility = (Visibility)(string.IsNullOrEmpty(record.PlaceName) ? 1 : 0);
		PlaceText.Text = record.PlaceName;
		if (!string.IsNullOrEmpty(record.RepostByName))
		{
			try
			{
				RevineText.Text = string.Format(AppResources.RevinedBy, record.RepostByName);
			}
			catch
			{
			}
			((UIElement)RevinePanel).Visibility = (Visibility)0;
		}
		else
		{
			((UIElement)RevinePanel).Visibility = (Visibility)1;
		}
		if (Post.IsVideo && (!Data.AutoPlayComputed || _forceplaydisplay))
		{
			((UIElement)ButtonPlay).Visibility = (Visibility)0;
		}
		else
		{
			((UIElement)ButtonPlay).Visibility = (Visibility)1;
		}
	}

	internal async Task RegramAction()
	{
		if (!(Post is VinePostRecord))
		{
			return;
		}
		if (_postControlBack != null)
		{
			((UIElement)_postControlBack.Option2Bis).Opacity = 0.2;
		}
		if (_postControlBack != null)
		{
			_postControlBack.RevineProgress.IsIndeterminate = true;
		}
		VinePostRecord vinerecord = (VinePostRecord)Post;
		try
		{
			string text = (vinerecord.MyRepostId = await DatasProvider.Instance.CurrentUser.Service.ReVineAsync(Post.PostId, vinerecord.MyRepostId));
			if (text != "0")
			{
				ToastHelper.Show(AppResources.ToastMessageRevined, afternav: false, (Orientation)0);
				((UIElement)_postControlBack.Option2Bis).Opacity = 0.33;
			}
			else
			{
				((UIElement)_postControlBack.Option2Bis).Opacity = 1.0;
				Messenger.Default.Send(new NotificationMessage<string>(Post.PostId, "RemovePost"));
			}
		}
		catch
		{
			((UIElement)_postControlBack.Option2Bis).Opacity = 1.0;
		}
		finally
		{
			if (_postControlBack != null)
			{
				_postControlBack.RevineProgress.IsIndeterminate = false;
			}
		}
	}

	public void ForcePlayDisplay()
	{
		_forceplaydisplay = true;
		((UIElement)ButtonPlay).Visibility = (Visibility)0;
	}

	internal void CreatePlayer()
	{
		bool lockTaken = false;
		PostControl obj = default(PostControl);
		try
		{
			obj = this;
			Monitor.Enter(obj, ref lockTaken);
			if (Post.IsVideo && ((PresentationFrameworkCollection<UIElement>)(object)((Panel)VideoContainer).Children).Count <= 0)
			{
				LoadPlayer();
			}
		}
		finally
		{
			if (lockTaken)
			{
				Monitor.Exit(obj);
			}
		}
	}

	private async void LoadPlayer(bool forcedplay = false)
	{
		bool autoplay = !((IEnumerable<UIElement>)((Panel)BackPanel).Children).Any() && (forcedplay || DatasProvider.Instance.AutoPlayComputed);
		MediaElement video = new MediaElement
		{
			AutoPlay = autoplay,
			Opacity = 0.0
		};
		double height = (((FrameworkElement)video).Width = ((FrameworkElement)Image).Width);
		((FrameworkElement)video).Height = height;
		if (autoplay)
		{
			Path buttonPause = ButtonPause;
			Path buttonPlay = ButtonPlay;
			Visibility visibility = (Visibility)1;
			((UIElement)buttonPlay).Visibility = (Visibility)1;
			((UIElement)buttonPause).Visibility = visibility;
		}
		string videosource = Post.VideoLink;
		_media = video;
		((UIElement)video).Tap += video_Tap;
		((UIElement)video).DoubleTap += video_DoubleTap;
		video.CurrentStateChanged += new RoutedEventHandler(video_CurrentStateChanged);
		video.MediaEnded += new RoutedEventHandler(video_MediaEnded);
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)VideoContainer).Children).Add((UIElement)(object)video);
		if (videosource.Contains("vineapp.com"))
		{
			try
			{
				Uri uri = await DatasProvider.Instance.CurrentUser.Service.DownloadAndGetUriAsync(Post);
				using IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication();
				using IsolatedStorageFileStream source = isolatedStorageFile.OpenFile(uri.OriginalString, FileMode.Open);
				video.SetSource((Stream)source);
			}
			catch
			{
			}
		}
		else
		{
			video.Source = new Uri(videosource);
		}
		if (autoplay)
		{
			AddProgressRing();
		}
		_progress = new ProgressBar
		{
			Padding = new Thickness(0.0),
			Width = ((FrameworkElement)video).Width,
			Height = 10.0,
			Maximum = 1.0,
			Background = null,
			Foreground = PostForeground
		};
		Canvas.SetTop((UIElement)(object)_progress, ((FrameworkElement)video).Width - 5.0);
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)VideoContainer).Children).Add((UIElement)(object)_progress);
	}

	private void video_DoubleTap(object sender, GestureEventArgs e)
	{
		if (!DatasProvider.Instance.DisableDoubleTap)
		{
			video_Tap(sender, e);
			Front_DoubleTap(sender, e);
		}
	}

	private void AddProgressRing()
	{
		if (Data.UseProgressBar)
		{
			if (_progress != null)
			{
				_progress.IsIndeterminate = true;
			}
		}
		else if (_progressRing == null)
		{
			ProgressRing progressRing = new ProgressRing();
			((FrameworkElement)progressRing).Height = 160.0;
			((FrameworkElement)progressRing).Width = 160.0;
			((Control)progressRing).IsEnabled = true;
			progressRing.IsActive = true;
			((Control)progressRing).Foreground = PostForeground;
			_progressRing = progressRing;
			Canvas.SetLeft((UIElement)(object)_progressRing, (((FrameworkElement)Image).Width - ((FrameworkElement)_progressRing).Width) / 2.0);
			Canvas.SetTop((UIElement)(object)_progressRing, (((FrameworkElement)Image).Height - ((FrameworkElement)_progressRing).Height) / 2.0);
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)VideoContainer).Children).Add((UIElement)(object)_progressRing);
		}
	}

	private void RemoveProgressRing()
	{
		if (_progress != null)
		{
			_progress.IsIndeterminate = false;
		}
		if (_progressRing != null)
		{
			try
			{
				((PresentationFrameworkCollection<UIElement>)(object)((Panel)VideoContainer).Children).Remove((UIElement)(object)_progressRing);
			}
			catch
			{
			}
			_progressRing = null;
		}
	}

	private void video_MediaEnded(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		((MediaElement)sender).Position = TimeSpan.Zero;
		((MediaElement)sender).Play();
	}

	private void video_Tap(object sender, GestureEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Expected O, but got Unknown
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_000e: Invalid comparison between Unknown and I4
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Invalid comparison between Unknown and I4
		//IL_0021: Unknown result type (might be due to invalid IL or missing references)
		//IL_0029: Unknown result type (might be due to invalid IL or missing references)
		//IL_002f: Invalid comparison between Unknown and I4
		MediaElement val = (MediaElement)sender;
		if ((int)val.CurrentState == 4 || (int)val.CurrentState == 1)
		{
			PlayVideo();
		}
		else if ((int)val.CurrentState == 0 || (int)val.CurrentState == 5)
		{
			val.Position = TimeSpan.Zero;
			PlayVideo();
		}
		else
		{
			PauseVideo();
		}
	}

	private void PlayVideo()
	{
		if (_media == null || _timer != null)
		{
			LoadPlayer(forcedplay: true);
			return;
		}
		LaunchTimer();
		((UIElement)ButtonPause).Visibility = (Visibility)1;
		((UIElement)ButtonPlay).Visibility = (Visibility)1;
		_media.Play();
	}

	private void PauseVideo()
	{
		//IL_001f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Invalid comparison between Unknown and I4
		StopTimer();
		if (_media != null)
		{
			_media.Pause();
			if ((int)((UIElement)ButtonPlay).Visibility == 1)
			{
				((UIElement)ButtonPause).Visibility = (Visibility)0;
			}
		}
	}

	private void video_CurrentStateChanged(object sender, RoutedEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_0012: Expected O, but got Unknown
		//IL_0018: Unknown result type (might be due to invalid IL or missing references)
		//IL_001d: Unknown result type (might be due to invalid IL or missing references)
		//IL_001e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Unknown result type (might be due to invalid IL or missing references)
		//IL_003e: Expected I4, but got Unknown
		//IL_0054: Unknown result type (might be due to invalid IL or missing references)
		//IL_0060: Unknown result type (might be due to invalid IL or missing references)
		//IL_006a: Expected O, but got Unknown
		MediaElement media = (MediaElement)sender;
		MediaElementState currentState = media.CurrentState;
		switch (currentState - 2)
		{
		case 0:
		case 5:
			AddProgressRing();
			break;
		case 4:
			RemoveProgressRing();
			break;
		case 1:
			RemoveProgressRing();
			((MediaElement)sender).CurrentStateChanged -= new RoutedEventHandler(video_CurrentStateChanged);
			LaunchTimer();
			TimerHelper.ToTime(TimeSpan.FromMilliseconds(200.0), delegate
			{
				((UIElement)media).InvalidateArrange();
				((UIElement)media).Opacity = 0.999;
			});
			break;
		case 2:
		case 3:
			break;
		}
	}

	public void LaunchTimer()
	{
		try
		{
			if (_timer != null)
			{
				_timer.Dispose();
			}
			_timer = new Timer(delegate
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					//IL_001a: Unknown result type (might be due to invalid IL or missing references)
					//IL_001f: Unknown result type (might be due to invalid IL or missing references)
					//IL_00ca: Unknown result type (might be due to invalid IL or missing references)
					//IL_00cf: Unknown result type (might be due to invalid IL or missing references)
					if (_timer != null && _media != null)
					{
						Duration naturalDuration = _media.NaturalDuration;
						double num = ((Duration)(ref naturalDuration)).TimeSpan.TotalMilliseconds - _media.Position.TotalMilliseconds;
						if (num <= 70.0)
						{
							_media.Position = TimeSpan.Zero;
							_media.Play();
							((RangeBase)_progress).Value = 0.0;
						}
						else
						{
							if (num <= 300.0)
							{
								_timer.Change(TimeSpan.Zero, TimeSpan.FromMilliseconds(50.0));
							}
							ProgressBar progress = _progress;
							double totalMilliseconds = _media.Position.TotalMilliseconds;
							naturalDuration = _media.NaturalDuration;
							((RangeBase)progress).Value = totalMilliseconds / ((Duration)(ref naturalDuration)).TimeSpan.TotalMilliseconds;
						}
					}
				});
			}, null, TimeSpan.Zero, TimeSpan.FromMilliseconds(200.0));
		}
		catch
		{
		}
	}

	public void StopTimer()
	{
		if (_timer != null)
		{
			try
			{
				_timer.Dispose();
				_timer = null;
			}
			catch
			{
			}
		}
	}

	internal void Stop()
	{
		StopTimer();
		if (Post.IsVideo || _media != null)
		{
			_media = null;
			_progress = null;
			_progressRing = null;
			while (((PresentationFrameworkCollection<UIElement>)(object)((Panel)VideoContainer).Children).Count > 0)
			{
				((PresentationFrameworkCollection<UIElement>)(object)((Panel)VideoContainer).Children).RemoveAt(0);
			}
			if (!DatasProvider.Instance.AutoPlayComputed)
			{
				((UIElement)ButtonPlay).Visibility = (Visibility)0;
				((UIElement)ButtonPause).Visibility = (Visibility)1;
			}
		}
	}

	internal void LikeError(ServiceServerErrorException ex)
	{
		if (_postControlBack != null)
		{
			((UIElement)_postControlBack.BackLike).Opacity = 1.0;
			_postControlBack.LikedProgress.IsIndeterminate = false;
		}
		if (ex.ReasonError == ServiceServerErrorType.CHECKPOINT)
		{
			ServiceUtils.ManageCheckPoint(ex.Checkpoint);
		}
		else if (ex.HttpErrorMessage != null)
		{
			ToastHelper.Show(ex.HttpErrorMessage, afternav: false, (Orientation)0);
		}
	}

	internal void LikeAnswer(bool res)
	{
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			if (_postControlBack != null)
			{
				((UIElement)_postControlBack.BackLike).Opacity = 1.0;
			}
			Post.Liked = res;
			if (res)
			{
				Post.ChangeNbrLike(Post.NbrLikes + 1);
			}
			else
			{
				Post.ChangeNbrLike(Post.NbrLikes - 1);
			}
			ManageLikeButton();
			if (_postControlBack != null)
			{
				_postControlBack.LikedProgress.IsIndeterminate = false;
			}
		});
	}

	internal async Task SavePicture()
	{
		if (this.SaveMedia != null)
		{
			this.SaveMedia(this, Post);
		}
	}

	private void ManageLikeButton()
	{
		//IL_006d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0077: Expected O, but got Unknown
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_0031: Expected O, but got Unknown
		if (Post.Liked)
		{
			LittleLikeIcon.Foreground = (Brush)(SolidColorBrush)Application.Current.Resources[(object)"LikeBrush"];
			LittleLikeIcon.Text = "\ue00b";
		}
		else
		{
			LittleLikeIcon.Text = "\ue006";
			LittleLikeIcon.Foreground = (Brush)(SolidColorBrush)Application.Current.Resources[(object)"DarkGreyBrush"];
		}
		if (_postControlBack != null)
		{
			_postControlBack.SetLike(Post.Liked);
		}
	}

	private void MenuShare_Click(object sender, RoutedEventArgs e)
	{
		if (this.Share != null)
		{
			this.Share(this, Post);
		}
	}

	private void MenuCopyLink_Click(object sender, RoutedEventArgs e)
	{
		Clipboard.SetText(Post.ShareUrl);
		ToastHelper.Show(AppResources.LinkCopied, afternav: false, (Orientation)0);
	}

	private void Like_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToLikes(Post, Post.GetLikes());
	}

	private void Comment_Click(object sender, RoutedEventArgs e)
	{
		CommentAction();
	}

	internal void CommentAction()
	{
		if (IsFullComment)
		{
			if (this.Comment != null)
			{
				this.Comment.Invoke((object)this, (RoutedEventArgs)null);
			}
		}
		else
		{
			NavigationServiceExt.ToComments(Post);
		}
	}

	private void Profile_Tap(object sender, GestureEventArgs e)
	{
		NavigationServiceExt.ToProfile(Post.UserId);
	}

	private async void Remove_Click(object sender, RoutedEventArgs e)
	{
		if (Post.RepostById == DatasProvider.Instance.CurrentUser.Id)
		{
			RegramAction();
		}
		else
		{
			RemoveAction();
		}
	}

	internal async Task RemoveAction()
	{
		if ((int)MessageBox.Show(AppResources.ToastRemovePostMessage, AppResources.ToastRemovePostTitle, (MessageBoxButton)1) != 1)
		{
			return;
		}
		DataUser currentUser = Data.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		SystemTray.ProgressIndicator = new ProgressIndicator
		{
			Text = "remove post",
			IsIndeterminate = true,
			IsVisible = true
		};
		try
		{
			await currentUser.Service.RemovePostAsync(Post.PostId);
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				SystemTray.ProgressIndicator = null;
				Messenger.Default.Send(new NotificationMessage<string>(Post.PostId, "RemovePost"));
			});
		}
		catch (ServiceServerErrorException ex)
		{
			SystemTray.ProgressIndicator = null;
			ToastHelper.Show(ex.HttpErrorMessage, afternav: false, (Orientation)0);
		}
	}

	private void Place_Click(object sender, RoutedEventArgs e)
	{
		if (Post is VinePostRecord)
		{
			VinePostRecord vinePostRecord = (VinePostRecord)Post;
			NavigationServiceExt.ToTimeline("venues/" + vinePostRecord.PlaceId, removebackentry: false, null, "&venuename=" + HttpUtility.UrlEncode(Post.PlaceName) + "&venueaddress=" + HttpUtility.UrlEncode(vinePostRecord.venueAddress) + "&venuecity=" + HttpUtility.UrlEncode(vinePostRecord.venueCity));
		}
	}

	private void Repost_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToProfile(Post.RepostById);
	}

	private void Picture_Tap(object sender, GestureEventArgs e)
	{
		_dispatcherTimerPicture.Start();
		if (Post.IsVideo && (!DatasProvider.Instance.AutoPlayComputed || _forceplaydisplay))
		{
			((UIElement)ButtonPlay).Visibility = (Visibility)1;
			if (((PresentationFrameworkCollection<UIElement>)(object)((Panel)VideoContainer).Children).Count == 0)
			{
				Messenger.Default.Send(new NotificationMessage<string>(this, Post.PostId, "RemoveOtherPlayers"));
				LoadPlayer(forcedplay: true);
			}
		}
	}

	private void PictureTapAction()
	{
	}

	private void HideTags()
	{
	}

	private void pin_Tap(object sender, GestureEventArgs e)
	{
	}

	private void Front_DoubleTap(object sender, GestureEventArgs e)
	{
		if (DatasProvider.Instance.DisableDoubleTap)
		{
			return;
		}
		_dispatcherTimerPicture.Stop();
		if (Post.Liked)
		{
			((UIElement)FrontDisLikedPath).Visibility = (Visibility)0;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				StoryboardFrontDisLike.Begin();
			});
		}
		else
		{
			((UIElement)FrontLikedPath).Visibility = (Visibility)0;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				StoryboardFrontLike.Begin();
			});
		}
		LikeAction();
	}

	internal async Task LikeAction()
	{
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		try
		{
			bool like = !Post.Liked;
			await currentUser.Service.LikePostAsync(Post.PostId, like);
			LikeAnswer(like);
		}
		catch (ServiceServerErrorException ex)
		{
			LikeError(ex);
		}
	}

	private void TurnPost_Click(object sender, RoutedEventArgs e)
	{
		FrontToBack(fromDotsMenu: true);
	}

	internal void SetIsDirect()
	{
		_isDirect = true;
		((UIElement)PostBottomInfo).Visibility = (Visibility)1;
		ContextMenuService.SetContextMenu((DependencyObject)(object)FrontPanel, null);
	}

	internal void SharePost()
	{
		if (this.Share != null)
		{
			this.Share(this, Post);
		}
	}

	public void BackTurnAction()
	{
		StoryboardBackToFront.Begin();
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
		//IL_02ed: Unknown result type (might be due to invalid IL or missing references)
		//IL_02f7: Expected O, but got Unknown
		//IL_0303: Unknown result type (might be due to invalid IL or missing references)
		//IL_030d: Expected O, but got Unknown
		//IL_0319: Unknown result type (might be due to invalid IL or missing references)
		//IL_0323: Expected O, but got Unknown
		//IL_032f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0339: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Controls/PostControl.xaml", UriKind.Relative));
			RootControl = (UserControl)((FrameworkElement)this).FindName("RootControl");
			StoryboardFrontToBack = (Storyboard)((FrameworkElement)this).FindName("StoryboardFrontToBack");
			StoryboardBackToFront = (Storyboard)((FrameworkElement)this).FindName("StoryboardBackToFront");
			StoryboardFrontLike = (Storyboard)((FrameworkElement)this).FindName("StoryboardFrontLike");
			StoryboardFrontDisLike = (Storyboard)((FrameworkElement)this).FindName("StoryboardFrontDisLike");
			StoryboardUserTagShow = (Storyboard)((FrameworkElement)this).FindName("StoryboardUserTagShow");
			StoryboardUserTagHide = (Storyboard)((FrameworkElement)this).FindName("StoryboardUserTagHide");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			FrontPanel = (Grid)((FrameworkElement)this).FindName("FrontPanel");
			Image = (Image)((FrameworkElement)this).FindName("Image");
			RevinePanel = (Button)((FrameworkElement)this).FindName("RevinePanel");
			RevineText = (TextBlock)((FrameworkElement)this).FindName("RevineText");
			Avatar = (Image)((FrameworkElement)this).FindName("Avatar");
			Date = (TextBlock)((FrameworkElement)this).FindName("Date");
			NameTxt = (TextBlock)((FrameworkElement)this).FindName("NameTxt");
			DescriptionTxt = (RichTextBox)((FrameworkElement)this).FindName("DescriptionTxt");
			PlacePanel = (Button)((FrameworkElement)this).FindName("PlacePanel");
			PlaceText = (TextBlock)((FrameworkElement)this).FindName("PlaceText");
			PostBottomInfo = (Grid)((FrameworkElement)this).FindName("PostBottomInfo");
			LittleLikeIcon = (TextBlock)((FrameworkElement)this).FindName("LittleLikeIcon");
			LikeLabelTxt = (TextBlock)((FrameworkElement)this).FindName("LikeLabelTxt");
			CommentLabelTxt = (TextBlock)((FrameworkElement)this).FindName("CommentLabelTxt");
			LoopPanel = (StackPanel)((FrameworkElement)this).FindName("LoopPanel");
			LoopTxt = (TextBlock)((FrameworkElement)this).FindName("LoopTxt");
			ShowMenuButton = (Button)((FrameworkElement)this).FindName("ShowMenuButton");
			ShadowF1 = (Rectangle)((FrameworkElement)this).FindName("ShadowF1");
			ShadowF2 = (Rectangle)((FrameworkElement)this).FindName("ShadowF2");
			VideoContainer = (Canvas)((FrameworkElement)this).FindName("VideoContainer");
			UserTaggedContainer = (Canvas)((FrameworkElement)this).FindName("UserTaggedContainer");
			ButtonPlay = (Path)((FrameworkElement)this).FindName("ButtonPlay");
			ButtonPause = (Path)((FrameworkElement)this).FindName("ButtonPause");
			FrontLikedPath = (Path)((FrameworkElement)this).FindName("FrontLikedPath");
			FrontDisLikedPath = (Path)((FrameworkElement)this).FindName("FrontDisLikedPath");
			HasUserTagged = (Path)((FrameworkElement)this).FindName("HasUserTagged");
			BackPanel = (Grid)((FrameworkElement)this).FindName("BackPanel");
			FullScreenSeparator = (Rectangle)((FrameworkElement)this).FindName("FullScreenSeparator");
		}
	}
}
