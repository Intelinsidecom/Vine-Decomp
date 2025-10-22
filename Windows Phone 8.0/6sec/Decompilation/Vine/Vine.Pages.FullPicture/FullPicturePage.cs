using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Navigation;
using System.Windows.Shapes;
using GalaSoft.MvvmLight.Messaging;
using Gen.Services;
using Huyn.Ads;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Reactive;
using Microsoft.Phone.Shell;
using Microsoft.Phone.Tasks;
using Vine.Controls;
using Vine.Datas;
using Vine.Models;
using Vine.Pages.FullPicture.ViewModels;
using Vine.Resources;
using Vine.Services;
using Vine.Services.Models;
using Vine.Utils;

namespace Vine.Pages.FullPicture;

public class FullPicturePage : PhoneApplicationPage
{
	private FullPictureViewModel ViewModel;

	public static readonly DependencyProperty CommentLoadedProperty = DependencyProperty.Register("CommentLoaded", typeof(bool), typeof(FullPicturePage), new PropertyMetadata((object)false, new PropertyChangedCallback(LoadedCallback)));

	private string _type;

	private string _lastsearch;

	private List<IPerson> _people = new List<IPerson>();

	private Transform _exframerender;

	private ApplicationBar _WriteAppBar;

	private Regex regHasTag = new Regex("(?:(?<total>(?<type>#|(?:(?<=^|[^\\w])@))(?<val>\\w*))|(?<isspace>^|\\s))$", RegexOptions.Compiled | RegexOptions.RightToLeft);

	private IPostRecord _currentShareRecord;

	private ApplicationBar _ChannelAppBar;

	private bool _autoscroll;

	internal Storyboard StoryboardShowCommentPanel;

	internal Rectangle RectBackground;

	internal Grid LayoutRoot;

	internal Grid AdPanel;

	internal Grid ContentPanel1;

	internal LongListSelector CommentsList;

	internal PostControl Post;

	internal Grid ToPanel;

	internal Grid ModernAppBarContainer;

	internal ModernAppBar ModernInstaAppBar;

	internal Grid SharePanel;

	internal StackPanel stackPanel;

	internal Button ShareTwitter;

	internal Button ShareFacebook;

	internal Button ShareEmbedButton;

	internal Button ShareNFC;

	internal Button ShareCopyLinkButton;

	internal Grid MyCommentPanel;

	internal ScrollViewer PrincScroll;

	internal TextBox MyComment;

	internal StackPanel CommentCounter;

	internal TextBlock NbrCaractText;

	internal Grid SuggestionsPanel;

	internal Grid SuggestionsPanelInner;

	internal Grid SuggestionsLine;

	internal ListBox SuggestionsList;

	internal Grid SuggestionsCommands;

	internal Grid SendingCommentPanel;

	internal ProgressBar SendingCommentProgress;

	internal Grid LoadingPanel;

	internal Grid SaveMediaLoadingPanel;

	internal ProgressBar SaveMediaLoadingProgress;

	private bool _contentLoaded;

	public bool CommentLoaded
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(CommentLoadedProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(CommentLoadedProperty, (object)value);
		}
	}

	public static IPostRecord SelectedPost { get; set; }

	public static bool IsDirect { get; set; }

	public FullPicturePage()
	{
		//IL_003b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Expected O, but got Unknown
		//IL_009d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0101: Unknown result type (might be due to invalid IL or missing references)
		//IL_010b: Expected O, but got Unknown
		//IL_011c: Unknown result type (might be due to invalid IL or missing references)
		//IL_00de: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e5: Expected O, but got Unknown
		//IL_00ea: Expected O, but got Unknown
		ViewModel = new FullPictureViewModel();
		ViewModel.CommentLoaded += new RoutedEventHandler(ViewModel_CommentLoaded);
		ViewModel.Init(SelectedPost);
		ViewModel.IsDirect = IsDirect;
		((FrameworkElement)this).DataContext = ViewModel;
		InitializeComponent();
		((FrameworkElement)SuggestionsPanelInner).Margin = new Thickness(0.0, 0.0, 0.0, PhoneSizeUtils.KeyboardPortraitdWithSuggestionsHeightStatic);
		Post.ForcePlayDisplay();
		if (DatasProvider.Instance.AddStripComputed)
		{
			Grid myCommentPanel = MyCommentPanel;
			Rectangle rectBackground = RectBackground;
			Brush val = (Brush)Application.Current.Resources[(object)"StripeBrush"];
			Brush background = val;
			((Shape)rectBackground).Fill = val;
			((Panel)myCommentPanel).Background = background;
		}
		_WriteAppBar = (ApplicationBar)((FrameworkElement)this).Resources[(object)"WriteApplicationBar"];
		((ApplicationBarIconButton)_WriteAppBar.Buttons[0]).Text = AppResources.SendComment;
		((Timeline)StoryboardShowCommentPanel).Completed += StoryboardShowCommentPanel_Completed;
		ObservableExtensions.Subscribe<IEvent<KeyEventArgs>>(Observable.Throttle<IEvent<KeyEventArgs>>(Observable.FromEvent<KeyEventArgs>((object)MyComment, "KeyUp"), TimeSpan.FromMilliseconds(140.0)), (Action<IEvent<KeyEventArgs>>)delegate
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				ManageKeyboard(MyComment);
			});
		});
		Messenger.Default.Register(this, delegate(NotificationMessage<IComment> res)
		{
			string notification = res.Notification;
			if (notification == "COMMENTREPLY")
			{
				MyComment.Text = res.Content.Username + " ";
				_people.Add(new SimpleUser
				{
					Id = res.Content.UserId,
					Name = res.Content.Username
				});
				MyComment.SelectionStart = MyComment.Text.Length;
				ShowForm();
			}
		});
		_ = DatasProvider.Instance;
	}

	private void ViewModel_CommentLoaded(object sender, RoutedEventArgs routedEventArgs)
	{
		if (_autoscroll)
		{
			TimerHelper.ToTime(TimeSpan.FromMilliseconds(100.0), delegate
			{
				ManageScrolling();
			});
		}
	}

	private void FullPicturePage_ScrollLoaded(object sender, RoutedEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_0012: Expected O, but got Unknown
		((FrameworkElement)this).Loaded -= new RoutedEventHandler(FullPicturePage_ScrollLoaded);
		ManageScrolling();
	}

	protected void Post_SelectedTag(object sender, string tag)
	{
		NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForTag(tag));
	}

	private static void LoadedCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((FullPicturePage)(object)d).ManageScrolling();
	}

	private void ManageScrolling()
	{
		if (!_autoscroll || ViewModel.Comments == null || ViewModel.Comments.Count <= 0)
		{
			return;
		}
		_autoscroll = false;
		TimerHelper.ToTime(TimeSpan.FromMilliseconds(50.0), delegate
		{
			try
			{
				CommentsList.ScrollTo((object)ViewModel.Comments.LastOrDefault());
			}
			catch
			{
			}
		});
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		ClearAds();
		((Page)this).OnNavigatingFrom(e);
		UIElement rootVisual = Application.Current.RootVisual;
		((rootVisual is PhoneApplicationFrame) ? rootVisual : null).RenderTransform = _exframerender;
	}

	private void WriteComment_Click(object sender, RoutedEventArgs e)
	{
		ShowForm();
	}

	protected override async void OnNavigatedTo(NavigationEventArgs e)
	{
		_003C_003En__FabricatedMethod0(e);
		UIElement rootVisual = Application.Current.RootVisual;
		PhoneApplicationFrame val = (PhoneApplicationFrame)(object)((rootVisual is PhoneApplicationFrame) ? rootVisual : null);
		_exframerender = ((UIElement)val).RenderTransform;
		((UIElement)val).RenderTransform = null;
		if ((int)e.NavigationMode == 0 || ViewModel.Post == null)
		{
			_type = null;
			if (SelectedPost == null && ((Page)this).NavigationContext.QueryString.TryGetValue("id", out var value))
			{
				await ViewModel.LoadPost(value);
			}
			if (ViewModel.Post == null)
			{
				TimerHelper.ToTime(TimeSpan.FromMilliseconds(100.0), delegate
				{
					try
					{
						if (((Page)this).NavigationService.CanGoBack)
						{
							((Page)this).NavigationService.GoBack();
						}
					}
					catch
					{
					}
				});
				return;
			}
			ModernInstaAppBar.Buttons[0].Text = (ViewModel.Post.IsVideo ? AppResources.SaveVideo : AppResources.SavePicture);
			if (((Page)this).NavigationContext.QueryString.TryGetValue("type", out _type))
			{
				if (_type == "direct")
				{
					Post.SetIsDirect();
					ViewModel.SetDirect();
				}
				else
				{
					ViewModel.IsDirect = false;
				}
			}
			else
			{
				ViewModel.IsDirect = false;
			}
			_autoscroll = ((Page)this).NavigationContext.QueryString.ContainsKey("autoscroll");
			if (((Page)this).NavigationContext.QueryString.ContainsKey("postComment"))
			{
				((FrameworkElement)this).Loaded += new RoutedEventHandler(FullPicturePageDisplayForm_Loaded);
			}
			else
			{
				((PhoneApplicationPage)this).ApplicationBar = null;
			}
		}
		AddAds();
	}

	private void AppBarReplybutton_Click(object sender, RoutedEventArgs e)
	{
	}

	private void ReplyTo(List<IPerson> persons)
	{
	}

	private void FullPicturePageDisplayForm_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_0012: Expected O, but got Unknown
		((FrameworkElement)this).Loaded -= new RoutedEventHandler(FullPicturePageDisplayForm_Loaded);
		ShowForm();
	}

	protected override void OnBackKeyPress(CancelEventArgs e)
	{
		//IL_0058: Unknown result type (might be due to invalid IL or missing references)
		//IL_0073: Unknown result type (might be due to invalid IL or missing references)
		//IL_009e: Unknown result type (might be due to invalid IL or missing references)
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
		else if ((int)((UIElement)MyCommentPanel).Visibility == 0)
		{
			((PhoneApplicationPage)this).ApplicationBar = null;
			SystemTray.IsVisible = false;
			((UIElement)MyCommentPanel).Visibility = (Visibility)1;
			((CompositeTransform)((UIElement)MyCommentPanel).RenderTransform).TranslateY = 870.0;
			e.Cancel = true;
		}
	}

	private void HideSharePanel()
	{
		((PhoneApplicationPage)this).ApplicationBar.IsVisible = true;
		((UIElement)SharePanel).Visibility = (Visibility)1;
	}

	private void StoryboardShowCommentPanel_Completed(object sender, EventArgs e)
	{
		((Control)MyComment).Focus();
	}

	private void ShowForm()
	{
		((UIElement)MyCommentPanel).Visibility = (Visibility)0;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			_WriteAppBar.IsVisible = true;
			((PhoneApplicationPage)this).ApplicationBar = (IApplicationBar)(object)_WriteAppBar;
			SystemTray.IsVisible = true;
			StoryboardShowCommentPanel.Begin();
		});
	}

	public void ShowSendingCommentLoading()
	{
		((UIElement)SendingCommentPanel).Visibility = (Visibility)0;
		SendingCommentProgress.IsIndeterminate = true;
	}

	public void HideSendingCommentLoading()
	{
		((UIElement)SendingCommentPanel).Visibility = (Visibility)1;
		SendingCommentProgress.IsIndeterminate = false;
	}

	private async void PostComment_Click(object sender, EventArgs e)
	{
		ShowSendingCommentLoading();
		_WriteAppBar.IsVisible = false;
		((Control)this).Focus();
		FullPictureViewModel fullPictureViewModel = (FullPictureViewModel)((FrameworkElement)this).DataContext;
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		try
		{
			ManagePostResponse(await currentUser.Service.PostCommentAsync(fullPictureViewModel.Post.PostId, MyComment.Text, _people));
		}
		catch (ServiceServerErrorException ex)
		{
			ManagePostError(ex);
		}
	}

	private void ManagePostError(ServiceServerErrorException ex)
	{
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			HideSendingCommentLoading();
			((PhoneApplicationPage)this).ApplicationBar.IsVisible = true;
			if (ex.ReasonError == ServiceServerErrorType.CHECKPOINT)
			{
				ServiceUtils.ManageCheckPoint(ex.Checkpoint);
			}
			else if (!string.IsNullOrEmpty(ex.HttpErrorMessage))
			{
				ToastHelper.Show(ex.HttpErrorMessage, afternav: false, (Orientation)0);
			}
		});
	}

	private void ManagePostResponse(IComment res)
	{
		try
		{
			res.IsRemovable = true;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				HideSendingCommentLoading();
				MyComment.Text = "";
				((PhoneApplicationPage)this).ApplicationBar.IsVisible = true;
				if (res != null)
				{
					((FullPictureViewModel)((FrameworkElement)this).DataContext).Reload();
					((UIElement)MyCommentPanel).Visibility = (Visibility)1;
					((PhoneApplicationPage)this).ApplicationBar = null;
					SystemTray.IsVisible = false;
				}
			});
		}
		catch
		{
		}
	}

	private async Task ManageKeyboard(TextBox textbox)
	{
		try
		{
			string currentsearch = null;
			Match m = regHasTag.Match((textbox.Text != null) ? textbox.Text.Substring(0, textbox.SelectionStart) : "");
			bool flag = Clipboard.ContainsText();
			InputScopeNameValue val = (InputScopeNameValue)49;
			bool flag2 = false;
			if (m.Groups["type"].Success)
			{
				currentsearch = (_lastsearch = m.Groups["total"].Value);
				if (!flag)
				{
					flag2 = m.Groups["type"].Value == "#" && (!m.Groups["val"].Success || m.Groups["val"].Value == "");
					val = (InputScopeNameValue)(flag2 ? 50 : 0);
				}
			}
			if ((int)val != 0)
			{
				((FrameworkElement)SuggestionsLine).Margin = new Thickness(0.0, 0.0, 0.0, PhoneSizeUtils.ApplicationBarOpacityHeightStatic);
			}
			else
			{
				((FrameworkElement)SuggestionsLine).Margin = new Thickness(0.0, 0.0, 0.0, 0.0);
			}
			if (((InputScopeName)MyComment.InputScope.Names[0]).NameValue != val)
			{
				InputScope val2 = new InputScope();
				val2.Names.Add((object)new InputScopeName
				{
					NameValue = val
				});
				textbox.InputScope = val2;
				((UIElement)textbox).GotFocus -= new RoutedEventHandler(Comment_GotFocus);
				((UIElement)textbox).LostFocus -= new RoutedEventHandler(Comment_LostFocus);
				((Control)this).Focus();
				((Control)textbox).Focus();
				((UIElement)textbox).GotFocus += new RoutedEventHandler(Comment_GotFocus);
				((UIElement)textbox).LostFocus += new RoutedEventHandler(Comment_LostFocus);
				((ItemsControl)SuggestionsList).ItemsSource = null;
			}
			if (flag2 && !DatasProvider.Instance.CurrentUser.Tags.Any())
			{
				Grid suggestionsCommands = SuggestionsCommands;
				Grid suggestionsLine = SuggestionsLine;
				Visibility visibility = (Visibility)1;
				((UIElement)suggestionsLine).Visibility = (Visibility)1;
				((UIElement)suggestionsCommands).Visibility = visibility;
				((ItemsControl)SuggestionsList).ItemsSource = null;
				return;
			}
			((UIElement)SuggestionsLine).Visibility = (Visibility)0;
			if (m.Success)
			{
				((UIElement)SuggestionsPanel).Visibility = (Visibility)0;
				if (m.Groups["isspace"].Success)
				{
					((UIElement)SuggestionsCommands).Visibility = (Visibility)0;
					((ItemsControl)SuggestionsList).ItemsSource = null;
				}
				else
				{
					((UIElement)SuggestionsCommands).Visibility = (Visibility)1;
					((ItemsControl)SuggestionsList).ItemsSource = null;
				}
				DataUser currentUser = DatasProvider.Instance.CurrentUser;
				if (currentUser == null)
				{
					return;
				}
				string value = m.Groups["type"].Value;
				if (!(value == "#"))
				{
					if (!(value == "@"))
					{
						return;
					}
					try
					{
						IListPersons res = default(IListPersons);
						_ = res;
						res = await currentUser.Service.SearchUserMentionAsync(m.Groups["val"].Value);
						if (currentsearch == _lastsearch)
						{
							((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
							{
								//IL_001f: Unknown result type (might be due to invalid IL or missing references)
								//IL_0029: Expected O, but got Unknown
								((ItemsControl)SuggestionsList).ItemTemplate = (DataTemplate)Application.Current.Resources[(object)"DataTemplateUserResult"];
								((ItemsControl)SuggestionsList).ItemsSource = res.Persons;
							});
						}
						return;
					}
					catch
					{
						return;
					}
				}
				string value2 = m.Groups["val"].Value;
				if (value2.Length > 0)
				{
					try
					{
						IListTags res2 = default(IListTags);
						_ = res2;
						res2 = await currentUser.Service.SearchTagAsync(value2, "1");
						if (currentsearch == _lastsearch)
						{
							((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
							{
								//IL_001f: Unknown result type (might be due to invalid IL or missing references)
								//IL_0029: Expected O, but got Unknown
								((ItemsControl)SuggestionsList).ItemTemplate = (DataTemplate)Application.Current.Resources[(object)"DataTemplateTagResult"];
								((ItemsControl)SuggestionsList).ItemsSource = res2.Tags.Select((ITag t) => t.Tag).ToList();
							});
						}
						return;
					}
					catch
					{
						return;
					}
				}
				((ItemsControl)SuggestionsList).ItemsSource = null;
			}
			else
			{
				((UIElement)SuggestionsPanel).Visibility = (Visibility)1;
				((ItemsControl)SuggestionsList).ItemsSource = null;
			}
		}
		catch
		{
		}
	}

	private void Comment_LostFocus(object sender, RoutedEventArgs e)
	{
		((UIElement)SuggestionsPanel).Visibility = (Visibility)1;
		((UIElement)SuggestionsLine).Visibility = (Visibility)1;
	}

	private void Comment_GotFocus(object sender, RoutedEventArgs e)
	{
		((ItemsControl)SuggestionsList).ItemsSource = null;
		((UIElement)SuggestionsPanel).Visibility = (Visibility)0;
		ManageKeyboard(MyComment);
	}

	private void SuggestionsList_SelectionsChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_0029: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Expected O, but got Unknown
		//IL_0040: Unknown result type (might be due to invalid IL or missing references)
		//IL_004a: Expected O, but got Unknown
		//IL_004a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0050: Expected O, but got Unknown
		//IL_0056: Unknown result type (might be due to invalid IL or missing references)
		//IL_005b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0068: Expected O, but got Unknown
		//IL_0231: Unknown result type (might be due to invalid IL or missing references)
		//IL_0249: Unknown result type (might be due to invalid IL or missing references)
		//IL_0253: Expected O, but got Unknown
		//IL_0260: Unknown result type (might be due to invalid IL or missing references)
		//IL_026a: Expected O, but got Unknown
		//IL_020a: Unknown result type (might be due to invalid IL or missing references)
		//IL_020f: Unknown result type (might be due to invalid IL or missing references)
		//IL_021d: Expected O, but got Unknown
		if (e.AddedItems.Count == 0 || e.AddedItems[0] == null)
		{
			return;
		}
		((UIElement)MyComment).GotFocus -= new RoutedEventHandler(Comment_GotFocus);
		((UIElement)MyComment).LostFocus -= new RoutedEventHandler(Comment_LostFocus);
		InputScope val = new InputScope();
		val.Names.Add((object)new InputScopeName
		{
			NameValue = (InputScopeNameValue)49
		});
		MyComment.InputScope = val;
		object obj = e.AddedItems[0];
		string text = null;
		int num = -1;
		string text2 = MyComment.Text;
		if (obj is string)
		{
			text = "#" + (string)obj;
			num = text2.Substring(0, MyComment.SelectionStart).LastIndexOf("#");
		}
		else if (obj is IPerson)
		{
			IPerson user = (IPerson)obj;
			text = user.Name;
			num = text2.Substring(0, MyComment.SelectionStart).LastIndexOf("@");
			if (!_people.Any((IPerson d) => d.Id == user.Id))
			{
				_people.Add(new SimpleUser
				{
					Name = user.Name,
					Id = user.Id
				});
			}
			DatasProvider.Instance.Save();
		}
		if (num >= 0)
		{
			int num2 = num;
			int selectionStart = MyComment.SelectionStart;
			string text3 = text2.Substring(selectionStart);
			string text4 = ((text3.Length <= 0 || text3[0] != ' ') ? " " : "");
			MyComment.SelectionStart = num2;
			MyComment.SelectionLength = selectionStart - num2;
			MyComment.SelectedText = text + text4;
			MyComment.SelectionStart = num + text.Length + text4.Length + 1;
			Binding val2 = new Binding("Text.Length")
			{
				Source = MyComment
			};
			((FrameworkElement)NbrCaractText).SetBinding(TextBlock.TextProperty, val2);
		}
		((Selector)(ListBox)sender).SelectedItem = null;
		((UIElement)MyComment).GotFocus += new RoutedEventHandler(Comment_GotFocus);
		((UIElement)MyComment).LostFocus += new RoutedEventHandler(Comment_LostFocus);
		((Control)MyComment).Focus();
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			((Control)MyComment).Focus();
		});
	}

	private void ClearAds()
	{
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
	}

	private void AddAds()
	{
		if (AppVersion.IsHaveAds())
		{
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
			UIElementCollection children = ((Panel)AdPanel).Children;
			AdRotator adRotator = new AdRotator();
			((FrameworkElement)adRotator).Width = 480.0;
			((FrameworkElement)adRotator).Height = 80.0;
			((PresentationFrameworkCollection<UIElement>)(object)children).Add((UIElement)(object)adRotator);
		}
	}

	private void SuggestionsAddDiese_Click(object sender, RoutedEventArgs e)
	{
		((Control)MyComment).Focus();
		MyComment.SelectedText = "#";
	}

	private void SuggestionsAddAt_Click(object sender, RoutedEventArgs e)
	{
		((Control)MyComment).Focus();
		MyComment.SelectedText = "@";
	}

	private void MyComment_TextChanged(object sender, TextChangedEventArgs e)
	{
		//IL_0017: Unknown result type (might be due to invalid IL or missing references)
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		ScrollViewer princScroll = PrincScroll;
		Rect rectFromCharacterIndex = MyComment.GetRectFromCharacterIndex(MyComment.SelectionStart);
		princScroll.ScrollToVerticalOffset(((Rect)(ref rectFromCharacterIndex)).Top + 30.0);
	}

	private void Refresh_Click(object sender, RoutedEventArgs e)
	{
		((FullPictureViewModel)((FrameworkElement)this).DataContext).Reload();
	}

	private void CreateComment_Tap(object sender, GestureEventArgs e)
	{
		ShowForm();
	}

	private void GoTop_Click(object sender, RoutedEventArgs e)
	{
		CommentsList.ScrollTo(CommentsList.ListHeader);
	}

	private void ApplicationBar_StateChanged_1(object sender, ApplicationBarStateChangedEventArgs e)
	{
		//IL_002e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0047: Unknown result type (might be due to invalid IL or missing references)
		//IL_0052: Unknown result type (might be due to invalid IL or missing references)
		//IL_0009: Unknown result type (might be due to invalid IL or missing references)
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		if (e.IsMenuVisible)
		{
			((ApplicationBar)sender).ForegroundColor = Colors.White;
			((ApplicationBar)sender).Opacity = 0.99;
		}
		else
		{
			((ApplicationBar)sender).ForegroundColor = (Color)Application.Current.Resources[(object)"DarkGreyColor"];
			((ApplicationBar)sender).Opacity = 0.0;
		}
	}

	private void ShowLasdComments_Click(object sender, RoutedEventArgs e)
	{
		try
		{
			FullPictureViewModel fullPictureViewModel = (FullPictureViewModel)((FrameworkElement)this).DataContext;
			CommentsList.ScrollTo((object)fullPictureViewModel.Comments.LastOrDefault());
		}
		catch
		{
		}
	}

	private void Share_Selected(object sender, IPostRecord record)
	{
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		//IL_002b: Unknown result type (might be due to invalid IL or missing references)
		_currentShareRecord = record;
		Button shareEmbedButton = ShareEmbedButton;
		Button shareCopyLinkButton = ShareCopyLinkButton;
		int num = (string.IsNullOrEmpty(record.ShareUrl) ? 1 : 0);
		Visibility visibility = (Visibility)num;
		((UIElement)shareCopyLinkButton).Visibility = (Visibility)num;
		((UIElement)shareEmbedButton).Visibility = visibility;
		((UIElement)SharePanel).Visibility = (Visibility)0;
		if (((PhoneApplicationPage)this).ApplicationBar != null)
		{
			((PhoneApplicationPage)this).ApplicationBar.IsVisible = false;
		}
	}

	private void ShareTwitter_Click(object sender, RoutedEventArgs e)
	{
		HideSharePanel();
		NavigationServiceExt.ToTwitter(_currentShareRecord.PostId, _currentShareRecord.Description, _currentShareRecord.Thumb);
	}

	private async void ShareFacebook_Click(object sender, RoutedEventArgs e)
	{
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser.FacebookAccess != null)
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

	private void CommentPost(object sender, RoutedEventArgs e)
	{
		ShowForm();
	}

	private void ViewToProfile_Tap(object sender, GestureEventArgs e)
	{
	}

	private async void LoadMoreComments_Click(object sender, RoutedEventArgs e)
	{
		_ = 1;
		try
		{
			IComment comment = await ((FullPictureViewModel)((FrameworkElement)this).DataContext).LoadMore();
			if (comment != null)
			{
				try
				{
					CommentsList.ScrollTo((object)comment);
					return;
				}
				catch
				{
					return;
				}
			}
		}
		catch (Exception)
		{
		}
	}

	private void HideSaveMediaPanel()
	{
		((UIElement)LayoutRoot).IsHitTestVisible = true;
		((UIElement)SaveMediaLoadingPanel).Visibility = (Visibility)1;
		SaveMediaLoadingProgress.IsIndeterminate = false;
		if (((PhoneApplicationPage)this).ApplicationBar != null)
		{
			((PhoneApplicationPage)this).ApplicationBar.IsVisible = true;
		}
	}

	private void ShowSaveMediaPanel()
	{
		((UIElement)LayoutRoot).IsHitTestVisible = false;
		((UIElement)SaveMediaLoadingPanel).Visibility = (Visibility)0;
		SaveMediaLoadingProgress.IsIndeterminate = true;
		if (((PhoneApplicationPage)this).ApplicationBar != null)
		{
			((PhoneApplicationPage)this).ApplicationBar.IsVisible = false;
		}
	}

	private async void SaveVideo(IPostRecord post)
	{
		ShowSaveMediaPanel();
		try
		{
			MediaSaver.Save(post);
		}
		catch
		{
		}
		finally
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				HideSaveMediaPanel();
			});
		}
	}

	private void Post_OnSaveMedia(object sender, IPostRecord record)
	{
		SaveMedia(record);
	}

	private void AppBarSave_Click(object sender, RoutedEventArgs e)
	{
		SaveMedia(Post.Post);
	}

	private void SaveMedia(IPostRecord record)
	{
		SaveVideo(record);
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
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		//IL_00dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e7: Expected O, but got Unknown
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
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/FullPicture/FullPicturePage.xaml", UriKind.Relative));
			StoryboardShowCommentPanel = (Storyboard)((FrameworkElement)this).FindName("StoryboardShowCommentPanel");
			RectBackground = (Rectangle)((FrameworkElement)this).FindName("RectBackground");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			AdPanel = (Grid)((FrameworkElement)this).FindName("AdPanel");
			ContentPanel1 = (Grid)((FrameworkElement)this).FindName("ContentPanel1");
			CommentsList = (LongListSelector)((FrameworkElement)this).FindName("CommentsList");
			Post = (PostControl)((FrameworkElement)this).FindName("Post");
			ToPanel = (Grid)((FrameworkElement)this).FindName("ToPanel");
			ModernAppBarContainer = (Grid)((FrameworkElement)this).FindName("ModernAppBarContainer");
			ModernInstaAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernInstaAppBar");
			SharePanel = (Grid)((FrameworkElement)this).FindName("SharePanel");
			stackPanel = (StackPanel)((FrameworkElement)this).FindName("stackPanel");
			ShareTwitter = (Button)((FrameworkElement)this).FindName("ShareTwitter");
			ShareFacebook = (Button)((FrameworkElement)this).FindName("ShareFacebook");
			ShareEmbedButton = (Button)((FrameworkElement)this).FindName("ShareEmbedButton");
			ShareNFC = (Button)((FrameworkElement)this).FindName("ShareNFC");
			ShareCopyLinkButton = (Button)((FrameworkElement)this).FindName("ShareCopyLinkButton");
			MyCommentPanel = (Grid)((FrameworkElement)this).FindName("MyCommentPanel");
			PrincScroll = (ScrollViewer)((FrameworkElement)this).FindName("PrincScroll");
			MyComment = (TextBox)((FrameworkElement)this).FindName("MyComment");
			CommentCounter = (StackPanel)((FrameworkElement)this).FindName("CommentCounter");
			NbrCaractText = (TextBlock)((FrameworkElement)this).FindName("NbrCaractText");
			SuggestionsPanel = (Grid)((FrameworkElement)this).FindName("SuggestionsPanel");
			SuggestionsPanelInner = (Grid)((FrameworkElement)this).FindName("SuggestionsPanelInner");
			SuggestionsLine = (Grid)((FrameworkElement)this).FindName("SuggestionsLine");
			SuggestionsList = (ListBox)((FrameworkElement)this).FindName("SuggestionsList");
			SuggestionsCommands = (Grid)((FrameworkElement)this).FindName("SuggestionsCommands");
			SendingCommentPanel = (Grid)((FrameworkElement)this).FindName("SendingCommentPanel");
			SendingCommentProgress = (ProgressBar)((FrameworkElement)this).FindName("SendingCommentProgress");
			LoadingPanel = (Grid)((FrameworkElement)this).FindName("LoadingPanel");
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
