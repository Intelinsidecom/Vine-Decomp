using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Vine.Special;
using Vine.Web;
using Windows.ApplicationModel.Chat;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;

namespace Vine.Views;

public sealed class ShareMessageView : BasePage, IComponentConnector
{
	private bool _tutorialHintVisibility;

	private UploadJob _job;

	private string _postId;

	private Brush _headerBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ShareMessageControl ShareMessageControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button ButtonShareMessage;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public string TitleText { get; set; }

	public bool TutorialHintVisibility
	{
		get
		{
			return _tutorialHintVisibility;
		}
		set
		{
			_tutorialHintVisibility = value;
			OnPropertyChanged("TutorialHintVisibility");
		}
	}

	public Brush HeaderBrush
	{
		get
		{
			return _headerBrush;
		}
		set
		{
			SetProperty(ref _headerBrush, value, "HeaderBrush");
		}
	}

	private ShareViewParameters PageParams => (ShareViewParameters)base.NavigationObject;

	public ShareMessageView()
	{
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Expected O, but got Unknown
		InitializeComponent();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		_job = PageParams.Job;
		_postId = PageParams.PostId;
		ShareMessageControl.IsSingleSelect = PageParams.IsSingleSelect;
		TitleText = (PageParams.IsSingleSelect ? ResourceHelper.GetString("new_message_preview") : ResourceHelper.GetString("Share"));
		if (!ApplicationSettings.Current.HasSeenShareMessagePage)
		{
			TutorialHintVisibility = true;
			ApplicationSettings.Current.HasSeenShareMessagePage = true;
		}
		else
		{
			TutorialHintVisibility = false;
		}
		await ShareMessageControl.Activate();
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
	}

	private void OnShareMessageClick(object sender, RoutedEventArgs e)
	{
		Complete();
	}

	private async void Complete()
	{
		List<string> userIds = new List<string>();
		List<VineUserModel> list = new List<VineUserModel>();
		List<string> emails = new List<string>();
		List<string> phones = new List<string>();
		foreach (VineContactViewModel selectedItem in ShareMessageControl.SelectedItems)
		{
			if (selectedItem.User.UserType == VineUserType.User)
			{
				userIds.Add(selectedItem.User.UserId);
				list.Add(selectedItem.User);
				continue;
			}
			if (!string.IsNullOrEmpty(selectedItem.User.Email))
			{
				emails.Add(selectedItem.User.Email);
			}
			if (!string.IsNullOrEmpty(selectedItem.User.PhoneNumber))
			{
				phones.Add(selectedItem.User.PhoneNumber);
			}
		}
		if (ShareMessageControl.IsSingleSelect)
		{
			string identifier = "";
			VineUserModel vineUserModel = null;
			VineUserType type;
			if (list.Any())
			{
				type = VineUserType.User;
				vineUserModel = list[0];
			}
			else if (phones.Any())
			{
				type = VineUserType.Phone;
				identifier = phones[0];
			}
			else
			{
				type = VineUserType.Email;
				identifier = emails[0];
			}
			if (vineUserModel == null)
			{
				App.RootFrame.NavigateWithObject(typeof(VineMessagesThreadView), new ConversationViewModel(type, identifier, new EmptyConversationModel()));
			}
			else
			{
				App.RootFrame.NavigateWithObject(typeof(VineMessagesThreadView), new ConversationViewModel(vineUserModel, new EmptyConversationModel()));
			}
		}
		else if (!string.IsNullOrEmpty(_postId))
		{
			App.RootFrame.GoBack();
			ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>> result = await App.Api.PostConversation(userIds, phones, emails, null, null, null, _postId, null, "US");
			if (!result.HasError && phones.Any() && result.Model.Data.Messages.Any() && !string.IsNullOrEmpty(result.Model.Data.Messages[0].ShareUrl))
			{
				ChatMessage val = new ChatMessage();
				val.put_Body(string.Format(ResourceHelper.GetString("sms_text") + Environment.NewLine + Environment.NewLine + ResourceHelper.GetString("sms_footer"), new object[1] { result.Model.Data.Messages[0].ShareUrl }));
				ChatMessage val2 = val;
				foreach (string item in phones)
				{
					val2.Recipients.Add(item);
				}
				await ReservedEx.SendSmsMessage(val2);
			}
			result.PopUpErrorIfExists();
		}
		else if (_job != null)
		{
			_job.UserIds = userIds;
			_job.Emails = emails;
			_job.PhoneNumbers = phones;
			UploadJobsViewModel.Current.Add(_job);
			ApplicationSettings.Current.SetRecordingActiveVine(null);
			App.HomePivotIndex = 0;
			App.HomeScrollToTop = true;
			App.RootFrame.Navigate(typeof(HomeView));
		}
	}

	private void ShareMessageControl_SelectionChanged(object sender)
	{
		if (ShareMessageControl.SelectedItems.Count == 0)
		{
			((UIElement)ButtonShareMessage).put_Visibility((Visibility)1);
		}
		else if (ShareMessageControl.IsSingleSelect)
		{
			Complete();
		}
		else
		{
			((UIElement)ButtonShareMessage).put_Visibility((Visibility)0);
			string format = ((ShareMessageControl.SelectedItems.Count > 1) ? ResourceHelper.GetString("ShareConfirmMessagePlural") : ResourceHelper.GetString("ShareConfirmMessageSingular"));
			((ContentControl)ButtonShareMessage).put_Content((object)string.Format(format, new object[1] { ShareMessageControl.SelectedItems.Count }));
		}
		if (TutorialHintVisibility)
		{
			TutorialHintVisibility = false;
			ApplicationSettings.Current.HasSeenShareMessagePage = true;
		}
	}

	private void ButtonShareMessage_OnDragOver(object sender, DragEventArgs e)
	{
		if (TutorialHintVisibility)
		{
			TutorialHintVisibility = false;
			ApplicationSettings.Current.HasSeenShareMessagePage = true;
		}
	}

	private void ShareMessageControl_OnPointerEntered(object sender, PointerRoutedEventArgs e)
	{
		if (TutorialHintVisibility)
		{
			TutorialHintVisibility = false;
			ApplicationSettings.Current.HasSeenShareMessagePage = true;
		}
	}

	private void Tutorial_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		if (TutorialHintVisibility)
		{
			TutorialHintVisibility = false;
			ApplicationSettings.Current.HasSeenShareMessagePage = true;
		}
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/ShareMessageView.xaml"), (ComponentResourceLocation)0);
			ShareMessageControl = (ShareMessageControl)((FrameworkElement)this).FindName("ShareMessageControl");
			ButtonShareMessage = (Button)((FrameworkElement)this).FindName("ButtonShareMessage");
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
		//IL_0068: Unknown result type (might be due to invalid IL or missing references)
		//IL_006e: Expected O, but got Unknown
		//IL_008f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0099: Expected O, but got Unknown
		//IL_009a: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a0: Expected O, but got Unknown
		//IL_00c1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cb: Expected O, but got Unknown
		//IL_00ce: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d4: Expected O, but got Unknown
		//IL_00f5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ff: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<PointerEventHandler, EventRegistrationToken>)val.add_PointerEntered, (Action<EventRegistrationToken>)val.remove_PointerEntered, new PointerEventHandler(ShareMessageControl_OnPointerEntered));
			((ShareMessageControl)target).SelectionChanged += ShareMessageControl_SelectionChanged;
			break;
		}
		case 2:
		{
			ButtonBase val2 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(OnShareMessageClick));
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<DragEventHandler, EventRegistrationToken>)val.add_DragOver, (Action<EventRegistrationToken>)val.remove_DragOver, new DragEventHandler(ButtonShareMessage_OnDragOver));
			break;
		}
		case 3:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(Tutorial_OnTapped));
			break;
		}
		}
		_contentLoaded = true;
	}
}
