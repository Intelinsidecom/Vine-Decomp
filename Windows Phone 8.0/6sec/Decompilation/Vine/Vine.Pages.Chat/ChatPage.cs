using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Navigation;
using Gen.Services;
using Huyn.Ads;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Vine.Controls;
using Vine.Datas;
using Vine.Pages.Chat.Models;
using Vine.Pages.Chat.ViewModels;
using Vine.Resources;
using Vine.Services;
using Vine.Services.Models;
using Vine.ViewModels;
using Windows.Phone.Speech.Recognition;

namespace Vine.Pages.Chat;

public class ChatPage : PhoneApplicationPage
{
	internal Grid LayoutRoot;

	internal Grid AdPanel;

	internal StackPanel ProfileStackPanel;

	internal Grid ContentPanel;

	internal LongListSelector Messages;

	internal Grid Footer;

	internal Grid Header;

	internal TextBlock WelcomeMessage;

	internal TextBlock MotivateCommentBlock;

	internal TextBox CommentText;

	internal Grid ReportPanel;

	internal Grid WriteReportPanel;

	internal TextBox ReportCustomTextBox;

	internal Grid ModernAppBarContainer;

	internal ModernAppBar ModernVineAppBar;

	private bool _contentLoaded;

	public ChatPage()
	{
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_0034: Unknown result type (might be due to invalid IL or missing references)
		//IL_0039: Unknown result type (might be due to invalid IL or missing references)
		//IL_0043: Expected O, but got Unknown
		//IL_007b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0085: Expected O, but got Unknown
		//IL_0096: Unknown result type (might be due to invalid IL or missing references)
		//IL_0069: Unknown result type (might be due to invalid IL or missing references)
		//IL_0073: Expected O, but got Unknown
		((FrameworkElement)this).DataContext = new ChatViewModel();
		InitializeComponent();
		if ((int)(Visibility)((FrameworkElement)this).Resources[(object)"PhoneLightThemeVisibility"] == 0)
		{
			((Panel)AdPanel).Background = (Brush)new SolidColorBrush(Colors.Black);
		}
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)LayoutRoot).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
		((FrameworkElement)this).Loaded += new RoutedEventHandler(ChatPage_Loaded);
		((ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[0]).Text = AppResources.SendComment;
	}

	protected override void OnBackKeyPress(CancelEventArgs e)
	{
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		((PhoneApplicationPage)this).OnBackKeyPress(e);
		if ((int)((UIElement)ReportPanel).Visibility == 0)
		{
			HideReport();
			e.Cancel = true;
		}
		else if ((int)((UIElement)WriteReportPanel).Visibility == 0)
		{
			HideWriteReport();
			e.Cancel = true;
		}
	}

	private void HideReport()
	{
		((UIElement)ReportPanel).Visibility = (Visibility)1;
		((PhoneApplicationPage)this).ApplicationBar.IsVisible = true;
	}

	private void ChatPage_Loaded(object sender, RoutedEventArgs e)
	{
		try
		{
			Messages.ScrollTo((object)Footer);
		}
		catch
		{
		}
	}

	private void AddAds()
	{
		//IL_00b5: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		if (AppVersion.IsHaveAds())
		{
			SystemTray.IsVisible = false;
			((FrameworkElement)ProfileStackPanel).Margin = new Thickness(0.0, 12.0, 0.0, 0.0);
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
			UIElementCollection children = ((Panel)AdPanel).Children;
			AdRotator adRotator = new AdRotator();
			((FrameworkElement)adRotator).Width = 480.0;
			((FrameworkElement)adRotator).Height = 80.0;
			((PresentationFrameworkCollection<UIElement>)(object)children).Add((UIElement)(object)adRotator);
		}
		else
		{
			SystemTray.IsVisible = true;
			((FrameworkElement)ProfileStackPanel).Margin = new Thickness(0.0, 32.0, 0.0, 0.0);
		}
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Unknown result type (might be due to invalid IL or missing references)
		//IL_004f: Expected O, but got Unknown
		ChatViewModel chatViewModel = (ChatViewModel)((FrameworkElement)this).DataContext;
		((Page)this).OnNavigatedTo(e);
		if ((int)e.NavigationMode == 0)
		{
			DataUser currentUser = DatasProvider.Instance.CurrentUser;
			Conversation conversation = null;
			if (currentUser != null && currentUser.CurrentConversation != null)
			{
				conversation = currentUser.CurrentConversation;
			}
			if (conversation == null)
			{
				((FrameworkElement)this).Loaded += (RoutedEventHandler)delegate
				{
					((Page)this).NavigationService.GoBack();
				};
			}
			else
			{
				chatViewModel.SetConversation(conversation, delegate
				{
					Messages.ScrollTo((object)Footer);
				});
			}
		}
		else
		{
			chatViewModel.StartPollUpdate();
		}
		AddAds();
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		//IL_0021: Unknown result type (might be due to invalid IL or missing references)
		//IL_0027: Invalid comparison between Unknown and I4
		((Page)this).OnNavigatingFrom(e);
		ClearAds();
		ChatViewModel chatViewModel = (ChatViewModel)((FrameworkElement)this).DataContext;
		chatViewModel.StopPollUpdate();
		if ((int)e.NavigationMode == 1 && chatViewModel.Conversation != null)
		{
			chatViewModel.Conversation.ResetCounter();
		}
	}

	private void ClearAds()
	{
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
	}

	private void PostComment_Click(object sender, EventArgs e)
	{
		if (!(CommentText.Text == ""))
		{
			((Control)this).Focus();
			((ChatViewModel)((FrameworkElement)this).DataContext).PostMessage(CommentText.Text, delegate
			{
				Messages.ScrollTo((object)Footer);
			});
			CommentText.Text = "";
		}
	}

	private void CopyMessage_Click(object sender, RoutedEventArgs e)
	{
	}

	private void Profile_Tap(object sender, GestureEventArgs e)
	{
	}

	private void Report_Click(object sender, EventArgs e)
	{
		((Control)this).Focus();
		((UIElement)ReportPanel).Visibility = (Visibility)0;
	}

	private void Block_Click(object sender, EventArgs e)
	{
	}

	private void ReportClassic_Click(object sender, EventArgs e)
	{
	}

	private void ReportCustom_Click(object sender, RoutedEventArgs e)
	{
		((UIElement)ReportPanel).Visibility = (Visibility)1;
		((UIElement)WriteReportPanel).Visibility = (Visibility)0;
		((Control)ReportCustomTextBox).Focus();
	}

	private void ReportCustomCancel_Click(object sender, RoutedEventArgs e)
	{
		HideWriteReport();
	}

	public void ReportCustomSend_Click(object sender, RoutedEventArgs e)
	{
	}

	private void HideWriteReport()
	{
		((UIElement)WriteReportPanel).Visibility = (Visibility)1;
	}

	private async void PinToStart_Click(object sender, EventArgs e)
	{
	}

	private async void Speak_Click(object sender, RoutedEventArgs e)
	{
		_ = 1;
		try
		{
			SpeechRecognizerUI val = new SpeechRecognizerUI();
			val.Settings.put_ShowConfirmation(false);
			SpeechRecognitionUIResult val2 = await val.RecognizeWithUIAsync();
			if ((int)val2.ResultStatus == 0)
			{
				CommentText.Text = val2.RecognitionResult.Text;
			}
		}
		catch
		{
		}
	}

	private void PostVideo_Click(object sender, RoutedEventArgs e)
	{
		ObservableCollection<IPerson> otherItems = ViewModelLocator.SelectDirectFriendsStatic.OtherItems;
		ChatViewModel chatViewModel = (ChatViewModel)((FrameworkElement)this).DataContext;
		otherItems.Clear();
		otherItems.Add(chatViewModel.Conversation.User);
		NavigationServiceExt.ToCamera(new List<IPerson> { chatViewModel.Conversation.User });
	}

	private void Video_Tap(object sender, GestureEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		DirectMessage directMessage = (DirectMessage)((FrameworkElement)sender).DataContext;
		ChatViewModel chatViewModel = (ChatViewModel)((FrameworkElement)this).DataContext;
		NavigationServiceExt.ToPost(new ChatPostRecord
		{
			Thumb = directMessage.ThumbnailUrl,
			VideoLink = directMessage.VideoUrl,
			Date = directMessage.Created,
			Description = directMessage.Message,
			UserName = chatViewModel.Conversation.User.Name,
			UserId = chatViewModel.Conversation.User.Id,
			AvatarUrl = chatViewModel.Conversation.User.Picture
		}, "chatvideo");
	}

	private void CommentText_OnLostFocus(object sender, RoutedEventArgs e)
	{
		((PhoneApplicationPage)this).ApplicationBar.IsVisible = false;
		((UIElement)ModernVineAppBar).Visibility = (Visibility)0;
	}

	private void CommentText_OnGotFocus(object sender, RoutedEventArgs e)
	{
		((PhoneApplicationPage)this).ApplicationBar.IsVisible = true;
		((UIElement)ModernVineAppBar).Visibility = (Visibility)1;
		ManageSendButton();
	}

	private void CommentText_OnTextChanged(object sender, TextChangedEventArgs e)
	{
		ManageSendButton();
	}

	private void ManageSendButton()
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		((ApplicationBarIconButton)((PhoneApplicationPage)this).ApplicationBar.Buttons[0]).IsEnabled = CommentText.Text.Length > 0;
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Chat/ChatPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			AdPanel = (Grid)((FrameworkElement)this).FindName("AdPanel");
			ProfileStackPanel = (StackPanel)((FrameworkElement)this).FindName("ProfileStackPanel");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			Messages = (LongListSelector)((FrameworkElement)this).FindName("Messages");
			Footer = (Grid)((FrameworkElement)this).FindName("Footer");
			Header = (Grid)((FrameworkElement)this).FindName("Header");
			WelcomeMessage = (TextBlock)((FrameworkElement)this).FindName("WelcomeMessage");
			MotivateCommentBlock = (TextBlock)((FrameworkElement)this).FindName("MotivateCommentBlock");
			CommentText = (TextBox)((FrameworkElement)this).FindName("CommentText");
			ReportPanel = (Grid)((FrameworkElement)this).FindName("ReportPanel");
			WriteReportPanel = (Grid)((FrameworkElement)this).FindName("WriteReportPanel");
			ReportCustomTextBox = (TextBox)((FrameworkElement)this).FindName("ReportCustomTextBox");
			ModernAppBarContainer = (Grid)((FrameworkElement)this).FindName("ModernAppBarContainer");
			ModernVineAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernVineAppBar");
		}
	}
}
