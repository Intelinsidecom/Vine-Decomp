using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Background;
using Vine.Events;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class VineMessagesInbox : BaseUserControl, IComponentConnector
{
	private bool _tutorialHintVisibility;

	private long _newCount;

	private bool _isOtherInboxActive;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private BaseUserControl Root;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ToggleButton MessageTypeToggle;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ConversationList FriendsInboxList;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ConversationList OtherInboxList;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

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

	public long NewCount
	{
		get
		{
			return _newCount;
		}
		set
		{
			SetProperty(ref _newCount, value, "NewCount");
		}
	}

	public bool IsOtherInboxActive
	{
		get
		{
			return _isOtherInboxActive;
		}
		set
		{
			SetProperty(ref _isOtherInboxActive, value, "IsOtherInboxActive");
			OnActivate();
		}
	}

	public VineMessagesInbox()
	{
		InitializeComponent();
	}

	public async Task OnActivate()
	{
		TutorialHintVisibility = !ApplicationSettings.Current.HasSeenVMCreateHint;
		CheckMessageCount();
		if (IsOtherInboxActive)
		{
			await OtherInboxList.OnActivate();
		}
		else
		{
			await FriendsInboxList.OnActivate();
		}
	}

	public void OnDeactivate()
	{
		if (IsOtherInboxActive)
		{
			OtherInboxList.OnDeactivate();
		}
		else
		{
			FriendsInboxList.OnDeactivate();
		}
	}

	public void SetNewCount(int count)
	{
		NewCount = count;
		if (NewCount > 0)
		{
			OtherInboxList.IsFinishedLoading = false;
			FriendsInboxList.IsFinishedLoading = false;
		}
	}

	private async Task CheckMessageCount()
	{
		ApiResult<BaseVineResponseModel<VineMessageActivityCounts>> apiResult = await App.Api.GetActivityCounts(ApplicationSettings.Current.UserId);
		if (!apiResult.HasError && apiResult.Model != null && apiResult.Model.Data != null)
		{
			NewCount = apiResult.Model.Data.Messages;
			BgLiveTiles.UpdateMainTileCount(apiResult.Model.Data.Messages + apiResult.Model.Data.Notifications);
			if (apiResult.Model.Data.Notifications == 0)
			{
				ToastHelper.Delete("notifications");
			}
			if (apiResult.Model.Data.Messages == 0)
			{
				ToastHelper.Delete("messages");
			}
			if (NewCount > 0)
			{
				OtherInboxList.IsFinishedLoading = false;
				FriendsInboxList.IsFinishedLoading = false;
			}
		}
	}

	public void AddConversation(ConversationAdded e)
	{
		if (FriendsInboxList.IsFinishedLoading)
		{
			FriendsInboxList.Items.Insert(0, e.ConversationViewModel);
			FriendsInboxList.IsEmpty = false;
		}
	}

	public void DeleteConversation(ConversationDeleted e)
	{
		ConversationViewModel conversationViewModel = null;
		if (FriendsInboxList.IsFinishedLoading)
		{
			if (e.ConversationId != null)
			{
				conversationViewModel = FriendsInboxList.Items.FirstOrDefault((ConversationViewModel r) => r.Record.ConversationId == e.ConversationId);
			}
			else if (e.OtherUserId != null)
			{
				conversationViewModel = FriendsInboxList.Items.FirstOrDefault((ConversationViewModel r) => r.OtherUser.UserId == e.OtherUserId);
			}
			if (conversationViewModel != null)
			{
				FriendsInboxList.Items.Remove(conversationViewModel);
			}
		}
		if (!OtherInboxList.IsFinishedLoading)
		{
			return;
		}
		if (e.ConversationId != null)
		{
			conversationViewModel = OtherInboxList.Items.FirstOrDefault((ConversationViewModel r) => r.Record.ConversationId == e.ConversationId);
		}
		else if (e.OtherUserId != null)
		{
			conversationViewModel = OtherInboxList.Items.FirstOrDefault((ConversationViewModel r) => r.OtherUser.UserId == e.OtherUserId);
		}
		if (conversationViewModel != null)
		{
			OtherInboxList.Items.Remove(conversationViewModel);
		}
	}

	public void ScrollToTop()
	{
		if (IsOtherInboxActive)
		{
			OtherInboxList.ScrollToTop();
		}
		else
		{
			FriendsInboxList.ScrollToTop();
		}
	}

	private void TutorialHint_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		ApplicationSettings.Current.HasSeenVMCreateHint = true;
		TutorialHintVisibility = false;
	}

	public void Clear()
	{
		OtherInboxList.IsFinishedLoading = false;
		FriendsInboxList.IsFinishedLoading = false;
		OtherInboxList.Items.ClearAndStop();
		FriendsInboxList.Items.ClearAndStop();
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/VineMessagesInbox.xaml"), (ComponentResourceLocation)0);
			Root = (BaseUserControl)((FrameworkElement)this).FindName("Root");
			MessageTypeToggle = (ToggleButton)((FrameworkElement)this).FindName("MessageTypeToggle");
			FriendsInboxList = (ConversationList)((FrameworkElement)this).FindName("FriendsInboxList");
			OtherInboxList = (ConversationList)((FrameworkElement)this).FindName("OtherInboxList");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		//IL_002c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0036: Expected O, but got Unknown
		if (connectionId == 1)
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(TutorialHint_OnTapped));
		}
		_contentLoaded = true;
	}
}
