using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class ConversationList : BaseUserControl, IIncrementalSource<ConversationViewModel>, IPullToRefresh, IComponentConnector
{
	private bool _isInbox;

	private bool _isBusy;

	private bool _isEmpty;

	private bool _hasError;

	private string _errorText = ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR");

	private bool _showRetry = true;

	private bool _hasNew;

	private ConversationCacheModel _cache;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private BaseUserControl Root;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private PullToRefreshListControl PullToRefreshView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public IncrementalLoadingCollection<ConversationViewModel> Items { get; set; }

	public double ItemWidth => Window.Current.CoreWindow.Bounds.Width / 2.0 - 3.0;

	public bool IsInbox
	{
		get
		{
			return _isInbox;
		}
		set
		{
			SetProperty(ref _isInbox, value, "IsInbox");
			NotifyOfPropertyChange(() => EmptyHeader);
			NotifyOfPropertyChange(() => EmptyMessage);
			NotifyOfPropertyChange(() => EmptyIcon);
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

	public string ErrorText
	{
		get
		{
			return _errorText;
		}
		set
		{
			SetProperty(ref _errorText, value, "ErrorText");
		}
	}

	public bool ShowRetry
	{
		get
		{
			return _showRetry;
		}
		set
		{
			SetProperty(ref _showRetry, value, "ShowRetry");
		}
	}

	public bool HasNew
	{
		get
		{
			return _hasNew;
		}
		set
		{
			SetProperty(ref _hasNew, value, "HasNew");
		}
	}

	public bool IsFinishedLoading { get; set; }

	public string EmptyIcon
	{
		get
		{
			if (IsInbox)
			{
				return "/Assets/welcomeVmsInbox.png";
			}
			return "/Assets/welcomeVmsOther.png";
		}
	}

	public string EmptyHeader
	{
		get
		{
			if (IsInbox)
			{
				return ResourceHelper.GetString("VMInboxSectionFriendsEmptyTitle");
			}
			return ResourceHelper.GetString("VMInboxSectionOtherEmptyTitle");
		}
	}

	public string EmptyMessage
	{
		get
		{
			if (IsInbox)
			{
				return ResourceHelper.GetString("VMInboxSectionFriendsEmptyMessage");
			}
			return ResourceHelper.GetString("VMInboxSectionOtherEmptyMessage");
		}
	}

	public ConversationList()
	{
		InitializeComponent();
		PullToRefreshView.AddReferences(ListView, this, 0.0);
		Items = new IncrementalLoadingCollection<ConversationViewModel>(this, PullToRefreshView);
	}

	public async Task OnActivate()
	{
		if (!IsFinishedLoading)
		{
			IsFinishedLoading = !(await Refresh()).ApiResult.HasError;
		}
	}

	public async Task PullToRefresh()
	{
		await Refresh();
	}

	private void Refresh_Click(object sender, RoutedEventArgs e)
	{
		Refresh();
	}

	public async Task<PagedItemsResult<ConversationViewModel>> Refresh()
	{
		ConversationList conversationList = this;
		bool isEmpty = (HasError = false);
		conversationList.IsEmpty = isEmpty;
		PagedItemsResult<ConversationViewModel> pagedItemsResult = await Items.Refresh();
		if (pagedItemsResult.ApiResult.HasError)
		{
			if (Items.Any())
			{
				pagedItemsResult.ApiResult.PopUpErrorIfExists();
			}
			else
			{
				HasError = true;
				if (pagedItemsResult.ApiResult.HasConnectivityError)
				{
					ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
					ShowRetry = false;
				}
				else
				{
					ErrorText = ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR");
					ShowRetry = true;
				}
				pagedItemsResult.ApiResult.LogError();
			}
		}
		else
		{
			Items.ResetItems(pagedItemsResult.ViewModels);
		}
		IsEmpty = !Items.Any() && !HasError;
		return pagedItemsResult;
	}

	public void OnDeactivate()
	{
	}

	public void ScrollToTop()
	{
		PullToRefreshView.ScrollToTopAnimated();
	}

	public async Task<PagedItemsResult<ConversationViewModel>> GetPagedItems(int page, int count, string anchor)
	{
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = true;
		});
		ApiResult<BaseVineResponseModel<ConversationMetaModel>> result = default(ApiResult<BaseVineResponseModel<ConversationMetaModel>>);
		_ = result;
		result = await App.Api.GetVineMessageConversations(page, count, IsInbox);
		List<ConversationViewModel> vms = null;
		if (!result.HasError)
		{
			vms = (from x in (from x in result.Model.Data.Records.Select(delegate(ConversationModel x)
					{
						string otherUserId = x.Users.FirstOrDefault((string id) => id != ApplicationSettings.Current.UserId);
						if (otherUserId == null)
						{
							return (ConversationViewModel)null;
						}
						VineUserModel vineUserModel = result.Model.Data.Users.FirstOrDefault((VineUserModel u) => u.UserId == otherUserId);
						if (vineUserModel == null)
						{
							return (ConversationViewModel)null;
						}
						ConversationViewModel conversationViewModel2 = null;
						if (vineUserModel.ExternalUser)
						{
							if (!string.IsNullOrEmpty(vineUserModel.Email))
							{
								conversationViewModel2 = new ConversationViewModel(VineUserType.Email, vineUserModel.Email, x);
							}
							else if (!string.IsNullOrEmpty(vineUserModel.PhoneNumber))
							{
								conversationViewModel2 = new ConversationViewModel(VineUserType.Phone, vineUserModel.PhoneNumber, x);
							}
						}
						if (conversationViewModel2 == null)
						{
							conversationViewModel2 = new ConversationViewModel(vineUserModel, x);
						}
						return conversationViewModel2;
					})
					where x != null
					select x).ToList()
				orderby x.Record.FirstMsg descending
				select x).ToList();
			if (_cache == null || page == 1)
			{
				ConversationList conversationList = this;
				_ = conversationList._cache;
				conversationList._cache = await FolderHelper.GetConversationInboxCache(IsInbox);
			}
			foreach (ConversationViewModel vm in vms)
			{
				_cache.Conversations.RemoveAll((ConversationViewModel x) => x.OtherUser.UserId == vm.OtherUser.UserId);
			}
			if (page >= 2)
			{
				foreach (ConversationViewModel item in Items)
				{
					_cache.Conversations.RemoveAll((ConversationViewModel x) => x.OtherUser.UserId == item.OtherUser.UserId);
				}
			}
			for (int num = 0; num < vms.Count; num++)
			{
				if (!_cache.Conversations.Any())
				{
					break;
				}
				ConversationViewModel conversationViewModel = _cache.Conversations[0];
				if (vms[num].Record.Created < conversationViewModel.Record.Created)
				{
					vms.Insert(num, conversationViewModel);
					_cache.Conversations.RemoveAt(0);
				}
				conversationViewModel.Record.UnreadMessageCount = vms[num].Record.UnreadMessageCount;
			}
			if (vms.Count == 0)
			{
				foreach (ConversationViewModel conversation in _cache.Conversations)
				{
					vms.Add(conversation);
				}
			}
		}
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = false;
		});
		return new PagedItemsResult<ConversationViewModel>
		{
			ApiResult = result,
			ViewModels = vms
		};
	}

	private void OnItemTapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		ConversationViewModel conversationViewModel = (ConversationViewModel)((FrameworkElement)sender).DataContext;
		conversationViewModel.Record.UnreadMessageCount = 0L;
		App.RootFrame.NavigateWithObject(typeof(VineMessagesThreadView), conversationViewModel);
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/ConversationList.xaml"), (ComponentResourceLocation)0);
			Root = (BaseUserControl)((FrameworkElement)this).FindName("Root");
			PullToRefreshView = (PullToRefreshListControl)((FrameworkElement)this).FindName("PullToRefreshView");
			ListView = (ListView)((FrameworkElement)this).FindName("ListView");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Expected O, but got Unknown
		//IL_0066: Unknown result type (might be due to invalid IL or missing references)
		//IL_0070: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			ButtonBase val2 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(Refresh_Click));
			break;
		}
		case 2:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(OnItemTapped));
			break;
		}
		}
		_contentLoaded = true;
	}
}
