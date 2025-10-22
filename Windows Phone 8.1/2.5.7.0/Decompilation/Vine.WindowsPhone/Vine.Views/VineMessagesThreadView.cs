using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Events;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Special;
using Vine.Views.Capture;
using Vine.Web;
using Windows.ApplicationModel.Chat;
using Windows.Foundation;
using Windows.UI;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

namespace Vine.Views;

public sealed class VineMessagesThreadView : BasePage, IIncrementalSource<VineMessageViewModel>, IComponentConnector
{
	private readonly DispatcherTimer _dispatcherTimer;

	private ConversationViewModel _viewModel;

	private string _newComment;

	private bool _hasError;

	private bool _isEmpty;

	private bool _isBusy;

	private bool _tutorialHintVisibility;

	private bool _isKeyboardVisible;

	private MessageCacheModel _cache;

	private bool _deleteCache;

	private bool _isViewModelLoaded;

	private bool _ignoreNextLostFocus;

	private double _lastVertOffset;

	private int _lastLayoutUpdated;

	private bool _isLayoutUpdating;

	private ScrollViewer _scrollViewer;

	private MediaElement _playingMediaElement;

	private VineMessageViewModel _lastPlayingVine;

	private VineUserModel CurrentUser;

	private Popup verifyPhonePopup = new Popup();

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private BasePage Root;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Border OpacityMask;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid KeyboardPlaceholder;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button btnSend;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox CommentTextBox;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private CommandBar AppBar;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton AppBarUser;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton AppBarIgnore;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton DeleteButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public IncrementalLoadingCollection<VineMessageViewModel> Items { get; set; }

	public ConversationViewModel ViewModel
	{
		get
		{
			if (_viewModel == null)
			{
				_viewModel = base.NavigationObject as ConversationViewModel;
			}
			return _viewModel;
		}
		set
		{
			_viewModel = value;
		}
	}

	public string ConversationId
	{
		get
		{
			if (ViewModel != null)
			{
				return ViewModel.Record.ConversationId;
			}
			return ((string)base.NavigationParam).Replace("\"", "");
		}
	}

	public string IgnoreLabel => string.Format(ResourceHelper.GetString("ignore_user"), new object[1] { ViewModel.OtherUser.Username });

	public string UserProfileLabel => string.Format(ResourceHelper.GetString("go_to_profile_conversation"), new object[1] { ViewModel.OtherUser.Username });

	public Brush CurrentUserBrush
	{
		get
		{
			//IL_000d: Unknown result type (might be due to invalid IL or missing references)
			//IL_0013: Expected O, but got Unknown
			if (ViewModel == null)
			{
				return (Brush)new SolidColorBrush(Colors.White);
			}
			return (Brush)(object)ViewModel.CurrentUserBrush;
		}
	}

	public Brush CurrentUserLightBrush
	{
		get
		{
			//IL_000d: Unknown result type (might be due to invalid IL or missing references)
			//IL_0013: Expected O, but got Unknown
			if (ViewModel == null)
			{
				return (Brush)new SolidColorBrush(Colors.White);
			}
			return ViewModel.CurrentUserLightBrush;
		}
	}

	public Brush OtherUserBrush
	{
		get
		{
			//IL_000d: Unknown result type (might be due to invalid IL or missing references)
			//IL_0013: Expected O, but got Unknown
			if (ViewModel == null)
			{
				return (Brush)new SolidColorBrush(Colors.White);
			}
			return ViewModel.OtherUserBrush;
		}
	}

	public Brush OtherUserLightBrush
	{
		get
		{
			//IL_000d: Unknown result type (might be due to invalid IL or missing references)
			//IL_0013: Expected O, but got Unknown
			if (ViewModel == null)
			{
				return (Brush)new SolidColorBrush(Colors.White);
			}
			return ViewModel.OtherUserLightBrush;
		}
	}

	public string OtherUsername
	{
		get
		{
			if (ViewModel == null || ViewModel.OtherUser == null)
			{
				return string.Empty;
			}
			return ViewModel.OtherUser.Username;
		}
	}

	public string NewComment
	{
		get
		{
			return _newComment;
		}
		set
		{
			SetProperty(ref _newComment, value, "NewComment");
			NotifyOfPropertyChange(() => SendEnabled);
		}
	}

	public bool HasError
	{
		get
		{
			return _hasError;
		}
		set
		{
			SetProperty(ref _hasError, value, "HasError");
		}
	}

	public bool IsEmpty
	{
		get
		{
			return _isEmpty;
		}
		set
		{
			SetProperty(ref _isEmpty, value, "IsEmpty");
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
			if (value)
			{
				IsEmpty = false;
				HasError = false;
			}
			SetProperty(ref _isBusy, value, "IsBusy");
		}
	}

	public bool TutorialHintVisibility
	{
		get
		{
			return _tutorialHintVisibility;
		}
		set
		{
			SetProperty(ref _tutorialHintVisibility, value, "TutorialHintVisibility");
		}
	}

	public bool IsKeyboardVisible
	{
		get
		{
			return _isKeyboardVisible;
		}
		set
		{
			SetProperty(ref _isKeyboardVisible, value, "IsKeyboardVisible");
		}
	}

	public bool IsFinishedLoading { get; set; }

	public bool SendEnabled => !string.IsNullOrEmpty(NewComment);

	public static UploadJob UploadJob { get; set; }

	public bool IsViewModelLoaded
	{
		get
		{
			return _isViewModelLoaded;
		}
		set
		{
			_isViewModelLoaded = value;
			OnPropertyChanged("IsViewModelLoaded");
			NotifyOfPropertyChange(() => CurrentUserBrush);
			NotifyOfPropertyChange(() => CurrentUserLightBrush);
			NotifyOfPropertyChange(() => OtherUserBrush);
			NotifyOfPropertyChange(() => OtherUserLightBrush);
			NotifyOfPropertyChange(() => AppBarIgnore);
			NotifyOfPropertyChange(() => AppBarUser);
			NotifyOfPropertyChange(() => OtherUsername);
		}
	}

	public bool IsVineUser
	{
		get
		{
			if (ViewModel != null && ViewModel.OtherUser != null)
			{
				return ViewModel.OtherUser.User != null;
			}
			return false;
		}
	}

	private PanelScrollingDirection ScrollingDirection { get; set; }

	public bool IsVolumeMuted => ApplicationSettings.Current.IsVolumeMuted;

	public IconElement MuteIcon
	{
		get
		{
			//IL_001c: Unknown result type (might be due to invalid IL or missing references)
			//IL_0022: Expected O, but got Unknown
			//IL_0011: Unknown result type (might be due to invalid IL or missing references)
			//IL_0017: Expected O, but got Unknown
			if (ApplicationSettings.Current.IsVolumeMuted)
			{
				return (IconElement)new SymbolIcon((Symbol)57693);
			}
			return (IconElement)new SymbolIcon((Symbol)57752);
		}
	}

	public string MuteLabel
	{
		get
		{
			if (!ApplicationSettings.Current.IsVolumeMuted)
			{
				return ResourceHelper.GetString("Mute");
			}
			return ResourceHelper.GetString("Unmute");
		}
	}

	public VineMessagesThreadView()
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		//IL_0018: Unknown result type (might be due to invalid IL or missing references)
		//IL_0022: Expected O, but got Unknown
		InitializeComponent();
		_dispatcherTimer = new DispatcherTimer();
		_dispatcherTimer.put_Interval(new TimeSpan(0, 0, 5));
		Items = new IncrementalLoadingCollection<VineMessageViewModel>(this);
		_scrollViewer = ((FrameworkElement)(object)ListView).GetFirstLogicalChildByType<ScrollViewer>(applyTemplates: true);
		ScrollViewer scrollViewer = _scrollViewer;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<ScrollViewerViewChangedEventArgs>, EventRegistrationToken>)scrollViewer.add_ViewChanged, (Action<EventRegistrationToken>)scrollViewer.remove_ViewChanged, (EventHandler<ScrollViewerViewChangedEventArgs>)ScrollViewerOnViewChanged);
		DeleteButton.put_Label(ResourceHelper.GetString("VMConversationMoreOptionsDeleteThread"));
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.VM, "vm_conversation"));
		TutorialHintVisibility = !ApplicationSettings.Current.HasSeenVMReplyHint;
		if (Items.Any())
		{
			for (int i = 0; i < 5; i++)
			{
				if (await ListBoxOnLayoutUpdated(this))
				{
					break;
				}
				await Task.Delay(100);
			}
		}
		WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)((FrameworkElement)ListView).remove_LayoutUpdated, (EventHandler<object>)ListBoxOnLayoutUpdated);
		ListView listView = ListView;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)((FrameworkElement)listView).add_LayoutUpdated, (Action<EventRegistrationToken>)((FrameworkElement)listView).remove_LayoutUpdated, (EventHandler<object>)ListBoxOnLayoutUpdated);
		if (IsFinishedLoading)
		{
			return;
		}
		IsViewModelLoaded = ViewModel != null;
		((UIElement)ListView).put_Opacity(0.0);
		if (ConversationId != null)
		{
			PagedItemsResult<VineMessageViewModel> pagedItemsResult = await Items.Refresh();
			if (!pagedItemsResult.ApiResult.HasError)
			{
				Items.ResetItems(pagedItemsResult.ViewModels);
			}
		}
		if (ViewModel != null && ApplicationSettings.Current.IsPhoneVerified && Items.Any() && Items[0].Model.MessageId == null && Items[0].RequiresVerification && Items[0].Model.Created > DateTime.UtcNow - TimeSpan.FromMinutes(10.0))
		{
			Items[0].RequiresVerification = false;
			Items[0].ErrorMessage = null;
			SendMessage(Items[0]);
		}
		if (UploadJob != null)
		{
			Items.Insert(0, new VineMessageViewModel(UploadJob));
			UploadJob uploadJob = UploadJob;
			UploadJob = null;
			UploadVideoJob(uploadJob);
		}
		ScrollToBottom();
		((UIElement)ListView).put_Opacity(1.0);
		IsFinishedLoading = true;
		WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)_dispatcherTimer.remove_Tick, (EventHandler<object>)_dispatcherTimer_Tick);
		DispatcherTimer dispatcherTimer = _dispatcherTimer;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)dispatcherTimer.add_Tick, (Action<EventRegistrationToken>)dispatcherTimer.remove_Tick, (EventHandler<object>)_dispatcherTimer_Tick);
		_dispatcherTimer.Start();
	}

	private void _dispatcherTimer_Tick(object sender, object e)
	{
		CheckForNewMessages();
	}

	private async void CheckForNewMessages()
	{
		if (ConversationId == null)
		{
			return;
		}
		_dispatcherTimer.Stop();
		PagedItemsResult<VineMessageViewModel> pagedItemsResult = await GetPagedItems(1, 20, bgPolling: true);
		if (pagedItemsResult.ApiResult != null && !pagedItemsResult.ApiResult.HasError)
		{
			foreach (VineMessageViewModel vm in pagedItemsResult.ViewModels.Reverse())
			{
				bool num = Items.Any((VineMessageViewModel i) => i.Model.MessageId == vm.Model.MessageId);
				bool flag = _cache.DeletedIds.Contains(vm.Model.MessageId);
				if (!num && !flag)
				{
					Items.Insert(0, vm);
					ScrollToBottom();
				}
			}
		}
		_dispatcherTimer.Start();
	}

	protected override async void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		_dispatcherTimer.Stop();
		if (_deleteCache)
		{
			ConversationCacheModel conversationCacheModel = await FolderHelper.GetConversationInboxCache(primary: true);
			conversationCacheModel.Conversations.RemoveAll((ConversationViewModel x) => x.OtherUser.UserId == ViewModel.OtherUser.UserId);
			await FolderHelper.SaveConversationInboxCache(primary: true, conversationCacheModel);
			await (await FolderHelper.GetConversationThreadCacheFile(ViewModel.OtherUser.UserId)).DeleteAsync();
		}
		else
		{
			if (ViewModel == null)
			{
				return;
			}
			List<VineMessageViewModel> list = Items.Where((VineMessageViewModel x) => x.Model.MessageId == null).ToList();
			FolderHelper.SaveConversationThreadCache(new MessageCacheModel
			{
				Messages = list,
				DeletedIds = ((_cache == null) ? new List<string>() : _cache.DeletedIds)
			}, ViewModel.OtherUser.UserId);
			if (list.Any() && ConversationId == null)
			{
				ConversationCacheModel conversationCacheModel2 = await FolderHelper.GetConversationInboxCache(primary: true);
				if (!conversationCacheModel2.Conversations.Any((ConversationViewModel x) => x.OtherUser.UserId == ViewModel.OtherUser.UserId))
				{
					EventAggregator.Current.Publish(new ConversationAdded
					{
						ConversationViewModel = ViewModel
					});
					ConversationViewModel viewModel = ViewModel;
					conversationCacheModel2.Conversations.Insert(0, viewModel);
					viewModel.Record.Created = DateTime.UtcNow;
					await FolderHelper.SaveConversationInboxCache(primary: true, conversationCacheModel2);
				}
			}
		}
	}

	public Task<PagedItemsResult<VineMessageViewModel>> GetPagedItems(int page, int count, string anchor)
	{
		return GetPagedItems(page, count, bgPolling: false);
	}

	private async Task<PagedItemsResult<VineMessageViewModel>> GetPagedItems(int page, int count, bool bgPolling)
	{
		if (!bgPolling)
		{
			DispatcherEx.BeginInvoke(delegate
			{
				IsBusy = true;
			});
		}
		ApiResult<BaseVineResponseModel<VineMessageMetaModel>> result = default(ApiResult<BaseVineResponseModel<VineMessageMetaModel>>);
		_ = result;
		result = await App.Api.GetVineMessagesByConversations(ConversationId, page, count, prefetch: false);
		List<VineMessageViewModel> list = new List<VineMessageViewModel>();
		if (!result.HasError)
		{
			if (ViewModel == null)
			{
				VineUserModel otherUser = result.Model.Data.Users.First((VineUserModel x) => x.UserId != ApplicationSettings.Current.UserId);
				ViewModel = new ConversationViewModel(otherUser, result.Model.Data);
				DispatcherEx.BeginInvoke(delegate
				{
					IsViewModelLoaded = true;
				});
			}
			if (page == 1 && !bgPolling)
			{
				VineMessagesThreadView vineMessagesThreadView = this;
				_ = vineMessagesThreadView._cache;
				vineMessagesThreadView._cache = await FolderHelper.GetConversationThreadCache(ViewModel.OtherUser.UserId);
			}
			list = result.Model.Data.Msgs.Select(delegate(VineMessageModel r)
			{
				VineUserModel user = result.Model.Data.Users.First((VineUserModel u) => u.UserId.Equals(r.UserId));
				return new VineMessageViewModel(r, user);
			}).ToList();
			foreach (VineMessageViewModel item in list.Where((VineMessageViewModel x) => _cache.DeletedIds.Contains(x.Model.MessageId)).ToList())
			{
				list.Remove(item);
			}
			for (int num = 0; num < list.Count; num++)
			{
				if (!_cache.Messages.Any())
				{
					break;
				}
				VineMessageViewModel vineMessageViewModel = _cache.Messages[0];
				if (list[num].Model.Created < vineMessageViewModel.Model.Created)
				{
					list.Insert(num, vineMessageViewModel);
					_cache.Messages.RemoveAt(0);
				}
			}
			if (!result.Model.Data.Msgs.Any())
			{
				DispatcherEx.BeginInvoke(delegate
				{
					((Control)ListView).put_Padding(new Thickness(0.0, 0.0, 0.0, 0.0));
				});
				foreach (VineMessageViewModel message in _cache.Messages)
				{
					list.Add(message);
				}
			}
			if (list.Any())
			{
				list[0].ShowCreatedDisplay = true;
			}
			for (int num2 = 1; num2 < list.Count; num2++)
			{
				list[num2].ShowCreatedDisplay = (list[num2].Model.Created - list[num2 - 1].Model.Created).Abs() > TimeSpan.FromHours(1.0);
			}
		}
		if (!bgPolling)
		{
			DispatcherEx.BeginInvoke(delegate
			{
				IsBusy = false;
			});
		}
		return new PagedItemsResult<VineMessageViewModel>
		{
			ApiResult = result,
			ViewModels = list
		};
	}

	private async void BtnSend_Click(object sender, RoutedEventArgs e)
	{
		if (!string.IsNullOrWhiteSpace(CommentTextBox.Text))
		{
			_ignoreNextLostFocus = true;
			((Control)CommentTextBox).Focus((FocusState)3);
			VineMessageViewModel item = new VineMessageViewModel(new VineMessageModel
			{
				Created = DateTime.UtcNow,
				Message = CommentTextBox.Text
			}, ViewModel.CurrentUser);
			Items.Insert(0, item);
			ScrollToBottom();
			NewComment = string.Empty;
			await SendMessage(item);
		}
	}

	private async Task SendMessage(VineMessageViewModel item)
	{
		if (_dispatcherTimer != null)
		{
			_dispatcherTimer.Stop();
		}
		new ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>>();
		List<string> userIds = new List<string>();
		List<string> emails = new List<string>();
		List<string> phones = new List<string>();
		if (ViewModel.OtherUser.UserType == VineUserType.User)
		{
			userIds.Add(ViewModel.OtherUser.UserId);
		}
		else if (ViewModel.OtherUser.UserType == VineUserType.Phone)
		{
			phones.Add(ViewModel.OtherUser.UserId);
		}
		else if (ViewModel.OtherUser.UserType == VineUserType.Email)
		{
			emails.Add(ViewModel.OtherUser.UserId);
		}
		ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>> result = ((ConversationId == null) ? (await App.Api.PostConversation(userIds, phones, emails, item.Model.Message, null, null, null, null, "US")) : (await App.Api.ReplyToConversation(ConversationId, item.Model.Message, null, null)));
		if (result.HasError)
		{
			if (result.ErrorParsed is MessageException ex)
			{
				item.ErrorMessage = ex.ResponseMessage;
				item.RequiresVerification = ex.Code == 616;
			}
			else
			{
				item.ErrorMessage = ResourceHelper.GetString("message_failed_tap_to_retry");
			}
			item.HasError = true;
		}
		else
		{
			item.Model.MessageId = result.Model.Data.Messages.FirstOrDefault().MessageId;
			item.RequiresVerification = false;
			item.ErrorMessage = null;
			item.HasError = false;
			if (phones.Any() && result.Model.Data.Messages.Any() && !string.IsNullOrEmpty(result.Model.Data.Messages[0].ShareUrl))
			{
				if (await ReservedEx.CanSendSmsMessage())
				{
					ChatMessage val = new ChatMessage();
					val.put_Body(string.Format(ResourceHelper.GetString("sms_text") + ResourceHelper.GetString("sms_footer"), new object[1] { result.Model.Data.Messages[0].ShareUrl }));
					ChatMessage val2 = val;
					foreach (string item2 in phones)
					{
						val2.Recipients.Add(item2);
					}
					await ReservedEx.SendSmsMessage(val2);
				}
				else
				{
					ToastHelper.Show(ResourceHelper.GetString("vine"), ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR"), "msgBoxOnTap");
				}
			}
		}
		item.Model.Created = DateTime.UtcNow;
		if (_dispatcherTimer != null)
		{
			_dispatcherTimer.Start();
		}
	}

	private void BtnText_Click(object sender, RoutedEventArgs e)
	{
		((Control)CommentTextBox).Focus((FocusState)3);
	}

	private void BtnVideo_Click(object sender, RoutedEventArgs e)
	{
		if (TutorialHintVisibility)
		{
			ApplicationSettings.Current.HasSeenVMReplyHint = true;
			TutorialHintVisibility = false;
		}
		App.RootFrame.NavigateWithObject(CaptureViewHelper.GetCaptureView(), new ReplyVmParameters
		{
			OtherUser = ViewModel.OtherUser,
			ConversationId = ConversationId
		});
	}

	private async void TextBox_OnGotFocus(object sender, RoutedEventArgs e)
	{
		IsKeyboardVisible = true;
		((UIElement)KeyboardPlaceholder).put_Visibility((Visibility)0);
		await ((FrameworkElement)(object)KeyboardPlaceholder).LayoutUpdatedAsync();
		ScrollToBottom();
	}

	private void TextBox_OnLostFocus(object sender, RoutedEventArgs e)
	{
		if (_ignoreNextLostFocus)
		{
			_ignoreNextLostFocus = false;
			return;
		}
		((UIElement)KeyboardPlaceholder).put_Visibility((Visibility)1);
		IsKeyboardVisible = false;
	}

	private void ScrollToBottom()
	{
	}

	private void AppBar_OnOpened(object sender, object e)
	{
		((Control)AppBar).put_Background(CurrentUserBrush);
	}

	private void AppBar_OnClosed(object sender, object e)
	{
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Expected O, but got Unknown
		((Control)AppBar).put_Background((Brush)new SolidColorBrush(Colors.Transparent));
		((UIElement)AppBar).put_Visibility((Visibility)1);
	}

	private void AppBarMore_PointerEntered(object sender, PointerRoutedEventArgs e)
	{
		((UIElement)AppBar).put_Visibility((Visibility)0);
		((AppBar)AppBar).put_IsOpen(true);
	}

	private void btnProfile_Click(object sender, RoutedEventArgs e)
	{
		App.RootFrame.Navigate(typeof(ProfileView), (object)ViewModel.OtherUser.UserId);
	}

	private async void btnIgnore_Click(object sender, RoutedEventArgs e)
	{
		MessageDialog val = new MessageDialog(ResourceHelper.GetString("VMConversationIgnoreUserMessage"), ResourceHelper.GetString("vine"))
		{
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("ignore_yes"), (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
		};
		val.put_CancelCommandIndex(1u);
		if ((int)(await val.ShowAsync()).Id == 0)
		{
			App.Api.IgnoreConversation(ApplicationSettings.Current.UserId, ViewModel.Record.ConversationId);
			EventAggregator.Current.Publish(new ConversationDeleted
			{
				ConversationId = ConversationId
			});
			App.RootFrame.GoBack();
		}
	}

	private async void btnDelete_Click(object sender, RoutedEventArgs e)
	{
		MessageDialog val = new MessageDialog(ResourceHelper.GetString("delete_conversation_prompt"), ResourceHelper.GetString("vine"))
		{
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("delete"), (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
		};
		val.put_CancelCommandIndex(1u);
		if ((int)(await val.ShowAsync()).Id == 0)
		{
			if (ConversationId == null)
			{
				_deleteCache = true;
				EventAggregator.Current.Publish(new ConversationDeleted
				{
					OtherUserId = ViewModel.OtherUser.UserId
				});
			}
			else
			{
				App.Api.DeleteConversation(ApplicationSettings.Current.UserId, ConversationId, ViewModel.Record.LastMessage, ignore: false);
				EventAggregator.Current.Publish(new ConversationDeleted
				{
					ConversationId = ConversationId
				});
			}
			App.RootFrame.GoBack();
		}
	}

	private void ScrollViewerOnViewChanged(object sender, ScrollViewerViewChangedEventArgs e)
	{
		//IL_005b: Unknown result type (might be due to invalid IL or missing references)
		//IL_006b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0092: Unknown result type (might be due to invalid IL or missing references)
		//IL_0097: Unknown result type (might be due to invalid IL or missing references)
		//IL_007f: Unknown result type (might be due to invalid IL or missing references)
		//IL_007b: Unknown result type (might be due to invalid IL or missing references)
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a8: Invalid comparison between Unknown and I4
		double num = _scrollViewer.VerticalOffset - _lastVertOffset;
		if (!(num > 5.0) && !(num < -50.0) && !(_scrollViewer.VerticalOffset < 200.0))
		{
			return;
		}
		PanelScrollingDirection val = ((_scrollViewer.VerticalOffset < 200.0) ? ((PanelScrollingDirection)2) : ((num < 0.0) ? ((PanelScrollingDirection)2) : ((!(num > 0.0)) ? ((PanelScrollingDirection)0) : ((PanelScrollingDirection)1))));
		_lastVertOffset = _scrollViewer.VerticalOffset;
		if (ScrollingDirection != val)
		{
			ScrollingDirection = val;
			if ((int)ScrollingDirection == 2 && IsKeyboardVisible)
			{
				double height = ((FrameworkElement)KeyboardPlaceholder).Height;
				((FrameworkElement)KeyboardPlaceholder).put_Height(0.0);
				IsKeyboardVisible = false;
				_scrollViewer.ChangeView((double?)null, (double?)(_lastVertOffset + height), (float?)null);
			}
		}
	}

	private void ListBoxOnLayoutUpdated(object sender, object e)
	{
		ListBoxOnLayoutUpdated(sender);
	}

	private async Task<bool> ListBoxOnLayoutUpdated(object sender)
	{
		Panel itemsPanelRoot = ((ItemsControl)ListView).ItemsPanelRoot;
		if (itemsPanelRoot == null || itemsPanelRoot.Children == null || !((IEnumerable<UIElement>)itemsPanelRoot.Children).Any() || ((FrameworkElement)this).ActualHeight == 0.0)
		{
			return false;
		}
		int tickCount = Environment.TickCount;
		if (tickCount - _lastLayoutUpdated < 100 || _isLayoutUpdating)
		{
			return false;
		}
		_lastLayoutUpdated = tickCount;
		_isLayoutUpdating = true;
		ListViewItem val = null;
		double num = 0.0;
		for (int i = 0; i < ((ICollection<UIElement>)itemsPanelRoot.Children).Count; i++)
		{
			UIElement obj = ((IList<UIElement>)itemsPanelRoot.Children)[i];
			ListViewItem val2 = (ListViewItem)(object)((obj is ListViewItem) ? obj : null);
			if (val2 != null)
			{
				GeneralTransform val3 = ((UIElement)val2).TransformToVisual((UIElement)(object)this);
				Rect rect = new Rect(val3.TransformPoint(new Point(0.0, WindowHeight / 2.0)), new Size(((FrameworkElement)val2).ActualWidth, ((FrameworkElement)val2).ActualHeight));
				rect.Intersect(new Rect(0.0, 130.0, ((FrameworkElement)this).ActualWidth, ((FrameworkElement)this).ActualHeight));
				if (num < rect.Height)
				{
					num = rect.Height;
					val = val2;
				}
			}
		}
		if (val != null)
		{
			VineMessageViewModel vine = (VineMessageViewModel)((ContentControl)val).Content;
			if (_lastPlayingVine != vine && vine != null)
			{
				if (_lastPlayingVine != null)
				{
					_lastPlayingVine.IsFinishedBuffering = false;
					await Task.Delay(100);
					_lastPlayingVine.IsPlaying = false;
				}
				vine.IsFinishedBuffering = false;
				vine.IsPlaying = true;
				_lastPlayingVine = vine;
			}
		}
		_isLayoutUpdating = false;
		return true;
	}

	private async void MediaElement_OnCurrentStateChanged(object sender, RoutedEventArgs e)
	{
		MediaElement val = (MediaElement)sender;
		VineMessageViewModel vine = (VineMessageViewModel)((FrameworkElement)val).DataContext;
		if (vine != null)
		{
			vine.IsLoadingVideo = (int)val.CurrentState == 1 || (int)val.CurrentState == 2;
			if (vine.IsLoadingVideo)
			{
				_playingMediaElement = val;
			}
			if ((int)val.CurrentState == 3 && !vine.IsFinishedBuffering)
			{
				await Task.Delay(200);
				vine.IsFinishedBuffering = true;
			}
		}
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

	private void MediaElement_OnMediaFailed(object sender, ExceptionRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		_ = (MediaElement)sender;
	}

	private async void Retry_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		if (((FrameworkElement)sender).DataContext is VineMessageViewModel { HasError: not false } retryItem)
		{
			if (retryItem.RequiresVerification)
			{
				MessageDialog val = new MessageDialog(ResourceHelper.GetString("VerificationPromptAlertMessageRequired"), ResourceHelper.GetString("vine"))
				{
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("settings_activate_account_confirm"), (UICommandInvokedHandler)null, (object)0) },
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
				};
				val.put_CancelCommandIndex(1u);
				if ((int)(await val.ShowAsync()).Id == 0)
				{
					VerifyPhoneEditControl verifyPhoneEditControl = new VerifyPhoneEditControl(ApplicationSettings.Current.User);
					((FrameworkElement)verifyPhoneEditControl).put_Width(((FrameworkElement)App.RootFrame).ActualWidth);
					((FrameworkElement)verifyPhoneEditControl).put_HorizontalAlignment((HorizontalAlignment)3);
					((FrameworkElement)verifyPhoneEditControl).put_Height(((FrameworkElement)App.RootFrame).ActualHeight);
					VerifyPhoneEditControl verifyPhoneEditControl2 = verifyPhoneEditControl;
					((UIElement)OpacityMask).put_Visibility((Visibility)0);
					verifyPhonePopup.put_Child((UIElement)(object)verifyPhoneEditControl2);
					verifyPhoneEditControl2.VerifyPhoneClicked += verifyPhoneControl_EnterCodeClicked;
					verifyPhonePopup.put_IsOpen(true);
				}
			}
			else
			{
				MessageDialog val2 = new MessageDialog(string.Format(ResourceHelper.GetString("VNMessageRetryTitle"), new object[1] { ResourceHelper.GetString("VNMessageRetryAction") }), ResourceHelper.GetString("vine"))
				{
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("VNMessageRetryAction"), (UICommandInvokedHandler)null, (object)0) },
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
				};
				val2.put_CancelCommandIndex(1u);
				if ((int)(await val2.ShowAsync()).Id == 0)
				{
					retryItem.ErrorMessage = null;
					retryItem.HasError = false;
					await SendMessage(retryItem);
				}
			}
		}
		else
		{
			e.put_Handled(true);
		}
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		if ((int)e.NavigationMode == 1 && verifyPhonePopup.IsOpen)
		{
			e.put_Cancel(true);
		}
		else
		{
			((Page)this).OnNavigatingFrom(e);
		}
		verifyPhonePopup.put_IsOpen(false);
		verifyPhonePopup.put_Child((UIElement)null);
		((UIElement)OpacityMask).put_Visibility((Visibility)1);
	}

	private async void verifyPhoneControl_EnterCodeClicked(object sender, EventArgs e)
	{
		ApiResult obj = await App.Api.SendVerifyPhoneRequest();
		obj.PopUpErrorIfExists();
		if (!obj.HasError)
		{
			App.RootFrame.NavigateWithObject(typeof(VerifyPhoneCodeEnterView), ApplicationSettings.Current.User);
		}
	}

	private void verifyPhoneControl_CancelClicked(object sender, EventArgs e)
	{
		verifyPhonePopup.put_IsOpen(false);
		verifyPhonePopup.put_Child((UIElement)null);
		((UIElement)OpacityMask).put_Visibility((Visibility)1);
	}

	private async void verifyPhoneControl_SendSMSClicked(object sender, EventArgs e)
	{
		verifyPhonePopup.put_IsOpen(false);
		verifyPhonePopup.put_Child((UIElement)null);
		((UIElement)OpacityMask).put_Visibility((Visibility)1);
		ApiResult obj = await App.Api.SendVerifyPhoneRequest();
		obj.PopUpErrorIfExists();
		if (!obj.HasError)
		{
			App.RootFrame.NavigateWithObject(typeof(VerifyPhoneCodeEnterView), CurrentUser);
		}
	}

	private void MuteVolume_Click(object sender, RoutedEventArgs e)
	{
		ApplicationSettings.Current.IsVolumeMuted = !ApplicationSettings.Current.IsVolumeMuted;
		NotifyOfPropertyChange(() => IsVolumeMuted);
	}

	private void Post_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineMessageViewModel vineMessageViewModel = (VineMessageViewModel)((FrameworkElement)sender).DataContext;
		App.RootFrame.NavigateWithObject(typeof(SingleVineView), new SingleVineViewParams
		{
			PostId = vineMessageViewModel.Model.Post.PostId,
			Section = Section.VM
		});
	}

	private void OnboardingHint_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		if (TutorialHintVisibility)
		{
			ApplicationSettings.Current.HasSeenVMReplyHint = true;
			TutorialHintVisibility = false;
		}
	}

	private async void RetryJob_Click(object sender, RoutedEventArgs e)
	{
		UploadJob job = (UploadJob)((FrameworkElement)sender).DataContext;
		if (job.RequiresValidation)
		{
			MessageDialog val = new MessageDialog(ResourceHelper.GetString("VerificationPromptAlertMessageRequired"), ResourceHelper.GetString("vine"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("settings_activate_account_confirm"), (UICommandInvokedHandler)null, (object)0) },
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Cancel"), (UICommandInvokedHandler)null, (object)1) }
			};
			val.put_CancelCommandIndex(1u);
			if ((int)(await val.ShowAsync()).Id == 0)
			{
				VerifyPhoneEditControl verifyPhoneEditControl = new VerifyPhoneEditControl(ApplicationSettings.Current.User);
				((FrameworkElement)verifyPhoneEditControl).put_Width(((FrameworkElement)App.RootFrame).ActualWidth);
				((FrameworkElement)verifyPhoneEditControl).put_HorizontalAlignment((HorizontalAlignment)3);
				((FrameworkElement)verifyPhoneEditControl).put_Height(((FrameworkElement)App.RootFrame).ActualHeight);
				VerifyPhoneEditControl verifyPhoneEditControl2 = verifyPhoneEditControl;
				((UIElement)OpacityMask).put_Visibility((Visibility)0);
				verifyPhonePopup.put_Child((UIElement)(object)verifyPhoneEditControl2);
				UploadJob = job;
				verifyPhoneEditControl2.VerifyPhoneClicked += verifyPhoneControl_EnterCodeClicked;
				verifyPhonePopup.put_IsOpen(true);
			}
		}
		else
		{
			UploadVideoJob(job);
		}
	}

	private async Task UploadVideoJob(UploadJob job)
	{
		ApiResult uploadResult = null;
		await DispatcherEx.InvokeBackground(async delegate
		{
			_ = uploadResult;
			uploadResult = await job.Execute();
		});
		if (!uploadResult.HasError)
		{
			BaseVineResponseModel<VineMessageVideoResponseModel> baseVineResponseModel = Serialization.Deserialize<BaseVineResponseModel<VineMessageVideoResponseModel>>(uploadResult.ResponseContent);
			VineMessageViewModel vineMessageViewModel = Items.FirstOrDefault((VineMessageViewModel x) => x.Model != null && x.Model.UploadJob != null && x.Model.UploadJob.Id == job.Id);
			if (vineMessageViewModel != null)
			{
				Items.Remove(vineMessageViewModel);
			}
			VineMessageViewModel item = new VineMessageViewModel(baseVineResponseModel.Data.Messages[0], ApplicationSettings.Current.User);
			Items.Insert(0, item);
			ScrollToBottom();
		}
		else if (uploadResult.ErrorParsed is MessageException ex)
		{
			job.RequiresValidation = ex.Code == 616;
			job.ErrorMessage = ex.ResponseMessage;
		}
	}

	private async void DeleteUploadJob_OnClick(object sender, RoutedEventArgs e)
	{
		UploadJob job = (UploadJob)((FrameworkElement)sender).DataContext;
		VineMessageViewModel vineMessageViewModel = Items.FirstOrDefault((VineMessageViewModel x) => x.Model != null && x.Model.UploadJob != null && x.Model.UploadJob.Id == job.Id);
		if (vineMessageViewModel != null)
		{
			Items.Remove(vineMessageViewModel);
		}
		await job.DeleteAsync();
	}

	private void DeleteMessage_OnClick(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		VineMessageViewModel vineMessageViewModel = (VineMessageViewModel)((FrameworkElement)sender).DataContext;
		if (vineMessageViewModel != null && Items.Contains(vineMessageViewModel))
		{
			Items.Remove(vineMessageViewModel);
			if (vineMessageViewModel.Model.MessageId != null)
			{
				_cache.DeletedIds.Add(vineMessageViewModel.Model.MessageId);
			}
		}
	}

	private async void FlyoutMenu_OnHolding(object sender, HoldingRoutedEventArgs e)
	{
		if ((int)e.HoldingState == 0)
		{
			FrameworkElement senderElement = (FrameworkElement)sender;
			await Task.Delay(250);
			FlyoutBase.GetAttachedFlyout(senderElement).ShowAt(senderElement);
		}
	}

	private void ProfileHeader_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		if (ViewModel != null && ViewModel.OtherUser != null && ViewModel.OtherUser.User != null)
		{
			App.RootFrame.Navigate(typeof(ProfileView), (object)ViewModel.OtherUser.UserId);
		}
	}

	private void CommentTextBox_TextChanged(object sender, TextChangedEventArgs e)
	{
		((Control)btnSend).put_IsEnabled(!string.IsNullOrWhiteSpace(CommentTextBox.Text));
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
		//IL_00dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e7: Expected O, but got Unknown
		//IL_00f3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fd: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/VineMessagesThreadView.xaml"), (ComponentResourceLocation)0);
			Root = (BasePage)((FrameworkElement)this).FindName("Root");
			OpacityMask = (Border)((FrameworkElement)this).FindName("OpacityMask");
			KeyboardPlaceholder = (Grid)((FrameworkElement)this).FindName("KeyboardPlaceholder");
			ListView = (ListView)((FrameworkElement)this).FindName("ListView");
			btnSend = (Button)((FrameworkElement)this).FindName("btnSend");
			CommentTextBox = (TextBox)((FrameworkElement)this).FindName("CommentTextBox");
			AppBar = (CommandBar)((FrameworkElement)this).FindName("AppBar");
			AppBarUser = (AppBarButton)((FrameworkElement)this).FindName("AppBarUser");
			AppBarIgnore = (AppBarButton)((FrameworkElement)this).FindName("AppBarIgnore");
			DeleteButton = (AppBarButton)((FrameworkElement)this).FindName("DeleteButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_009e: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a4: Expected O, but got Unknown
		//IL_00c5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cf: Expected O, but got Unknown
		//IL_00d5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00db: Expected O, but got Unknown
		//IL_00fc: Unknown result type (might be due to invalid IL or missing references)
		//IL_0106: Expected O, but got Unknown
		//IL_010c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0112: Expected O, but got Unknown
		//IL_0133: Unknown result type (might be due to invalid IL or missing references)
		//IL_013d: Expected O, but got Unknown
		//IL_0143: Unknown result type (might be due to invalid IL or missing references)
		//IL_0149: Expected O, but got Unknown
		//IL_016a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0174: Expected O, but got Unknown
		//IL_017a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0180: Expected O, but got Unknown
		//IL_01a1: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ab: Expected O, but got Unknown
		//IL_01b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b7: Expected O, but got Unknown
		//IL_01d8: Unknown result type (might be due to invalid IL or missing references)
		//IL_01e2: Expected O, but got Unknown
		//IL_01e8: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ee: Expected O, but got Unknown
		//IL_020f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0219: Expected O, but got Unknown
		//IL_021f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0225: Expected O, but got Unknown
		//IL_0246: Unknown result type (might be due to invalid IL or missing references)
		//IL_0250: Expected O, but got Unknown
		//IL_0256: Unknown result type (might be due to invalid IL or missing references)
		//IL_025c: Expected O, but got Unknown
		//IL_027d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0287: Expected O, but got Unknown
		//IL_0288: Unknown result type (might be due to invalid IL or missing references)
		//IL_028e: Expected O, but got Unknown
		//IL_02af: Unknown result type (might be due to invalid IL or missing references)
		//IL_02b9: Expected O, but got Unknown
		//IL_02ba: Unknown result type (might be due to invalid IL or missing references)
		//IL_02c0: Expected O, but got Unknown
		//IL_02e1: Unknown result type (might be due to invalid IL or missing references)
		//IL_02eb: Expected O, but got Unknown
		//IL_02f1: Unknown result type (might be due to invalid IL or missing references)
		//IL_02f7: Expected O, but got Unknown
		//IL_0318: Unknown result type (might be due to invalid IL or missing references)
		//IL_0322: Expected O, but got Unknown
		//IL_0328: Unknown result type (might be due to invalid IL or missing references)
		//IL_032e: Expected O, but got Unknown
		//IL_034f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0359: Expected O, but got Unknown
		//IL_035f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0365: Expected O, but got Unknown
		//IL_0386: Unknown result type (might be due to invalid IL or missing references)
		//IL_0390: Expected O, but got Unknown
		//IL_0396: Unknown result type (might be due to invalid IL or missing references)
		//IL_039c: Expected O, but got Unknown
		//IL_03bd: Unknown result type (might be due to invalid IL or missing references)
		//IL_03c7: Expected O, but got Unknown
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
		//IL_0468: Unknown result type (might be due to invalid IL or missing references)
		//IL_046e: Expected O, but got Unknown
		//IL_048f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0499: Expected O, but got Unknown
		//IL_049f: Unknown result type (might be due to invalid IL or missing references)
		//IL_04a5: Expected O, but got Unknown
		//IL_04c6: Unknown result type (might be due to invalid IL or missing references)
		//IL_04d0: Expected O, but got Unknown
		//IL_04d6: Unknown result type (might be due to invalid IL or missing references)
		//IL_04dc: Expected O, but got Unknown
		//IL_04fd: Unknown result type (might be due to invalid IL or missing references)
		//IL_0507: Expected O, but got Unknown
		//IL_050d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0513: Expected O, but got Unknown
		//IL_0534: Unknown result type (might be due to invalid IL or missing references)
		//IL_053e: Expected O, but got Unknown
		//IL_053f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0545: Expected O, but got Unknown
		//IL_0566: Unknown result type (might be due to invalid IL or missing references)
		//IL_0570: Expected O, but got Unknown
		//IL_0571: Unknown result type (might be due to invalid IL or missing references)
		//IL_0577: Expected O, but got Unknown
		//IL_0598: Unknown result type (might be due to invalid IL or missing references)
		//IL_05a2: Expected O, but got Unknown
		//IL_05a8: Unknown result type (might be due to invalid IL or missing references)
		//IL_05ae: Expected O, but got Unknown
		//IL_05cf: Unknown result type (might be due to invalid IL or missing references)
		//IL_05d9: Expected O, but got Unknown
		//IL_05df: Unknown result type (might be due to invalid IL or missing references)
		//IL_05e5: Expected O, but got Unknown
		//IL_0606: Unknown result type (might be due to invalid IL or missing references)
		//IL_0610: Expected O, but got Unknown
		//IL_0616: Unknown result type (might be due to invalid IL or missing references)
		//IL_061c: Expected O, but got Unknown
		//IL_063d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0647: Expected O, but got Unknown
		//IL_064d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0653: Expected O, but got Unknown
		//IL_0674: Unknown result type (might be due to invalid IL or missing references)
		//IL_067e: Expected O, but got Unknown
		//IL_0684: Unknown result type (might be due to invalid IL or missing references)
		//IL_068a: Expected O, but got Unknown
		//IL_06ab: Unknown result type (might be due to invalid IL or missing references)
		//IL_06b5: Expected O, but got Unknown
		//IL_06b6: Unknown result type (might be due to invalid IL or missing references)
		//IL_06bc: Expected O, but got Unknown
		//IL_06dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_06e7: Expected O, but got Unknown
		//IL_06e8: Unknown result type (might be due to invalid IL or missing references)
		//IL_06ee: Expected O, but got Unknown
		//IL_070f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0719: Expected O, but got Unknown
		//IL_071f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0725: Expected O, but got Unknown
		//IL_0746: Unknown result type (might be due to invalid IL or missing references)
		//IL_0750: Expected O, but got Unknown
		//IL_0756: Unknown result type (might be due to invalid IL or missing references)
		//IL_075c: Expected O, but got Unknown
		//IL_077d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0787: Expected O, but got Unknown
		//IL_078d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0793: Expected O, but got Unknown
		//IL_07b4: Unknown result type (might be due to invalid IL or missing references)
		//IL_07be: Expected O, but got Unknown
		//IL_07c4: Unknown result type (might be due to invalid IL or missing references)
		//IL_07cb: Expected O, but got Unknown
		//IL_07ee: Unknown result type (might be due to invalid IL or missing references)
		//IL_07f8: Expected O, but got Unknown
		//IL_07f9: Unknown result type (might be due to invalid IL or missing references)
		//IL_07ff: Expected O, but got Unknown
		//IL_0820: Unknown result type (might be due to invalid IL or missing references)
		//IL_082a: Expected O, but got Unknown
		//IL_082b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0831: Expected O, but got Unknown
		//IL_0852: Unknown result type (might be due to invalid IL or missing references)
		//IL_085c: Expected O, but got Unknown
		//IL_0862: Unknown result type (might be due to invalid IL or missing references)
		//IL_0868: Expected O, but got Unknown
		//IL_0889: Unknown result type (might be due to invalid IL or missing references)
		//IL_0893: Expected O, but got Unknown
		//IL_0899: Unknown result type (might be due to invalid IL or missing references)
		//IL_089f: Expected O, but got Unknown
		//IL_08c0: Unknown result type (might be due to invalid IL or missing references)
		//IL_08ca: Expected O, but got Unknown
		//IL_08d0: Unknown result type (might be due to invalid IL or missing references)
		//IL_08d6: Expected O, but got Unknown
		//IL_08f7: Unknown result type (might be due to invalid IL or missing references)
		//IL_0901: Expected O, but got Unknown
		//IL_0907: Unknown result type (might be due to invalid IL or missing references)
		//IL_090d: Expected O, but got Unknown
		//IL_092e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0938: Expected O, but got Unknown
		//IL_093e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0945: Expected O, but got Unknown
		//IL_0973: Unknown result type (might be due to invalid IL or missing references)
		//IL_097a: Expected O, but got Unknown
		//IL_09ad: Unknown result type (might be due to invalid IL or missing references)
		//IL_09b3: Expected O, but got Unknown
		//IL_09d4: Unknown result type (might be due to invalid IL or missing references)
		//IL_09de: Expected O, but got Unknown
		//IL_09e4: Unknown result type (might be due to invalid IL or missing references)
		//IL_09ea: Expected O, but got Unknown
		//IL_0a0b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0a15: Expected O, but got Unknown
		//IL_0a18: Unknown result type (might be due to invalid IL or missing references)
		//IL_0a1e: Expected O, but got Unknown
		//IL_0a3f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0a49: Expected O, but got Unknown
		//IL_0a4c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0a52: Expected O, but got Unknown
		//IL_0a73: Unknown result type (might be due to invalid IL or missing references)
		//IL_0a7d: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<HoldingEventHandler, EventRegistrationToken>)val3.add_Holding, (Action<EventRegistrationToken>)val3.remove_Holding, new HoldingEventHandler(FlyoutMenu_OnHolding));
			break;
		}
		case 2:
		{
			MenuFlyoutItem val6 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val6.add_Click, (Action<EventRegistrationToken>)val6.remove_Click, new RoutedEventHandler(DeleteUploadJob_OnClick));
			break;
		}
		case 3:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(RetryJob_Click));
			break;
		}
		case 4:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<HoldingEventHandler, EventRegistrationToken>)val3.add_Holding, (Action<EventRegistrationToken>)val3.remove_Holding, new HoldingEventHandler(FlyoutMenu_OnHolding));
			break;
		}
		case 5:
		{
			MenuFlyoutItem val6 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val6.add_Click, (Action<EventRegistrationToken>)val6.remove_Click, new RoutedEventHandler(DeleteMessage_OnClick));
			break;
		}
		case 6:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Retry_OnTapped));
			break;
		}
		case 7:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<HoldingEventHandler, EventRegistrationToken>)val3.add_Holding, (Action<EventRegistrationToken>)val3.remove_Holding, new HoldingEventHandler(FlyoutMenu_OnHolding));
			break;
		}
		case 8:
		{
			MenuFlyoutItem val6 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val6.add_Click, (Action<EventRegistrationToken>)val6.remove_Click, new RoutedEventHandler(DeleteMessage_OnClick));
			break;
		}
		case 9:
		{
			MediaElement val5 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val5.add_CurrentStateChanged, (Action<EventRegistrationToken>)val5.remove_CurrentStateChanged, new RoutedEventHandler(MediaElement_OnCurrentStateChanged));
			val5 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<ExceptionRoutedEventHandler, EventRegistrationToken>)val5.add_MediaFailed, (Action<EventRegistrationToken>)val5.remove_MediaFailed, new ExceptionRoutedEventHandler(MediaElement_OnMediaFailed));
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(MediaElement_OnTapped));
			break;
		}
		case 10:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<HoldingEventHandler, EventRegistrationToken>)val3.add_Holding, (Action<EventRegistrationToken>)val3.remove_Holding, new HoldingEventHandler(FlyoutMenu_OnHolding));
			break;
		}
		case 11:
		{
			MenuFlyoutItem val6 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val6.add_Click, (Action<EventRegistrationToken>)val6.remove_Click, new RoutedEventHandler(DeleteMessage_OnClick));
			break;
		}
		case 12:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Post_Click));
			break;
		}
		case 13:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(Retry_OnTapped));
			break;
		}
		case 14:
		{
			MediaElement val5 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val5.add_CurrentStateChanged, (Action<EventRegistrationToken>)val5.remove_CurrentStateChanged, new RoutedEventHandler(MediaElement_OnCurrentStateChanged));
			val5 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<ExceptionRoutedEventHandler, EventRegistrationToken>)val5.add_MediaFailed, (Action<EventRegistrationToken>)val5.remove_MediaFailed, new ExceptionRoutedEventHandler(MediaElement_OnMediaFailed));
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(MediaElement_OnTapped));
			break;
		}
		case 15:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<HoldingEventHandler, EventRegistrationToken>)val3.add_Holding, (Action<EventRegistrationToken>)val3.remove_Holding, new HoldingEventHandler(FlyoutMenu_OnHolding));
			break;
		}
		case 16:
		{
			MenuFlyoutItem val6 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val6.add_Click, (Action<EventRegistrationToken>)val6.remove_Click, new RoutedEventHandler(DeleteMessage_OnClick));
			break;
		}
		case 17:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Post_Click));
			break;
		}
		case 18:
		{
			MediaElement val5 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val5.add_CurrentStateChanged, (Action<EventRegistrationToken>)val5.remove_CurrentStateChanged, new RoutedEventHandler(MediaElement_OnCurrentStateChanged));
			val5 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<ExceptionRoutedEventHandler, EventRegistrationToken>)val5.add_MediaFailed, (Action<EventRegistrationToken>)val5.remove_MediaFailed, new ExceptionRoutedEventHandler(MediaElement_OnMediaFailed));
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(MediaElement_OnTapped));
			break;
		}
		case 19:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<HoldingEventHandler, EventRegistrationToken>)val3.add_Holding, (Action<EventRegistrationToken>)val3.remove_Holding, new HoldingEventHandler(FlyoutMenu_OnHolding));
			break;
		}
		case 20:
		{
			MenuFlyoutItem val6 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val6.add_Click, (Action<EventRegistrationToken>)val6.remove_Click, new RoutedEventHandler(DeleteMessage_OnClick));
			break;
		}
		case 21:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<HoldingEventHandler, EventRegistrationToken>)val3.add_Holding, (Action<EventRegistrationToken>)val3.remove_Holding, new HoldingEventHandler(FlyoutMenu_OnHolding));
			break;
		}
		case 22:
		{
			MenuFlyoutItem val6 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val6.add_Click, (Action<EventRegistrationToken>)val6.remove_Click, new RoutedEventHandler(DeleteMessage_OnClick));
			break;
		}
		case 23:
		{
			MediaElement val5 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val5.add_CurrentStateChanged, (Action<EventRegistrationToken>)val5.remove_CurrentStateChanged, new RoutedEventHandler(MediaElement_OnCurrentStateChanged));
			val5 = (MediaElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<ExceptionRoutedEventHandler, EventRegistrationToken>)val5.add_MediaFailed, (Action<EventRegistrationToken>)val5.remove_MediaFailed, new ExceptionRoutedEventHandler(MediaElement_OnMediaFailed));
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(MediaElement_OnTapped));
			break;
		}
		case 24:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(OnboardingHint_OnTapped));
			break;
		}
		case 25:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(BtnVideo_Click));
			break;
		}
		case 26:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(BtnSend_Click));
			break;
		}
		case 27:
		{
			TextBox val4 = (TextBox)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TextChangedEventHandler, EventRegistrationToken>)val4.add_TextChanged, (Action<EventRegistrationToken>)val4.remove_TextChanged, new TextChangedEventHandler(CommentTextBox_TextChanged));
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_GotFocus, (Action<EventRegistrationToken>)val3.remove_GotFocus, new RoutedEventHandler(TextBox_OnGotFocus));
			val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_LostFocus, (Action<EventRegistrationToken>)val3.remove_LostFocus, new RoutedEventHandler(TextBox_OnLostFocus));
			break;
		}
		case 28:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(BtnText_Click));
			break;
		}
		case 29:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(BtnVideo_Click));
			break;
		}
		case 30:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val3.add_PointerEntered, (Action<EventRegistrationToken>)val3.remove_PointerEntered, new PointerEventHandler(AppBarMore_PointerEntered));
			break;
		}
		case 31:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(ProfileHeader_OnTapped));
			break;
		}
		case 32:
		{
			AppBar val2 = (AppBar)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)val2.add_Opened, (Action<EventRegistrationToken>)val2.remove_Opened, (EventHandler<object>)AppBar_OnOpened);
			val2 = (AppBar)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)val2.add_Closed, (Action<EventRegistrationToken>)val2.remove_Closed, (EventHandler<object>)AppBar_OnClosed);
			break;
		}
		case 33:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(MuteVolume_Click));
			break;
		}
		case 34:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(btnProfile_Click));
			break;
		}
		case 35:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(btnIgnore_Click));
			break;
		}
		case 36:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(btnDelete_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
