using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Web;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class SignUpEmailView : BasePage, IComponentConnector
{
	private VineUserModel _user = new VineUserModel
	{
		ProfileBackground = "ffffff"
	};

	private bool _isAcceptInProgress;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AvatarControl UserAvatar;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBlock EnterName;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox NameTextBlock;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private CommandBar cmdBarAccept;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton AcceptButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public VineUserModel User
	{
		get
		{
			return _user;
		}
		set
		{
			SetProperty(ref _user, value, "User");
		}
	}

	public bool IsFinishedLoading { get; set; }

	public SignUpEmailView()
	{
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		InitializeComponent();
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)((FrameworkElement)this).add_Loaded, (Action<EventRegistrationToken>)((FrameworkElement)this).remove_Loaded, new RoutedEventHandler(SignUpEmailView_Loaded));
		AcceptButton.put_Label(ResourceHelper.GetString("UniversalOkay"));
	}

	private void SignUpEmailView_Loaded(object sender, RoutedEventArgs e)
	{
		((Control)NameTextBlock).Focus((FocusState)3);
	}

	protected override void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.LoggedOut, "signup_email_1"));
		if (IsFinishedLoading)
		{
			return;
		}
		if (e.PageState != null)
		{
			User = e.LoadValueOrDefault<VineUserModel>("User");
			if (User.Username != null)
			{
				NameTextBlock.put_Text(User.Username);
				CheckValidNameLength();
			}
		}
		if (App.TempNewAvatar != null)
		{
			User.AvatarUrl = App.TempNewAvatar.Path;
		}
		((FrameworkElement)UserAvatar).put_DataContext((object)User);
		IsFinishedLoading = true;
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		e.PageState["User"] = User;
	}

	private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
	{
		CheckValidNameLength();
	}

	private void CheckValidNameLength()
	{
		if (NameTextBlock.Text.Trim().Length >= 3)
		{
			((Control)AcceptButton).put_IsEnabled(true);
		}
		else
		{
			((Control)AcceptButton).put_IsEnabled(false);
		}
	}

	private async void TextBox_KeyDown(object sender, KeyRoutedEventArgs e)
	{
		if ((int)e.Key == 13 && NameTextBlock.Text.Length >= 3)
		{
			await Accept();
		}
	}

	private async void AcceptButton_Click(object sender, RoutedEventArgs e)
	{
		await Accept();
	}

	private async Task Accept()
	{
		if (!_isAcceptInProgress)
		{
			_isAcceptInProgress = true;
			User.Username = NameTextBlock.Text;
			if (User.AvatarUrl == Vine.Web.Constants.DefaultAvatarUrl)
			{
				MessageDialog val = new MessageDialog(ResourceHelper.GetString("profile_photo_prompt"))
				{
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("skip"), new UICommandInvokedHandler(CommandInvokedHandler), (object)0) },
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("set_photo"), new UICommandInvokedHandler(CommandInvokedHandler), (object)1) }
				};
				val.put_DefaultCommandIndex(1u);
				val.put_CancelCommandIndex(0u);
				await val.ShowAsync();
			}
			else
			{
				Proceed();
			}
			_isAcceptInProgress = false;
		}
	}

	private void CommandInvokedHandler(IUICommand command)
	{
		if ((int)command.Id == 1)
		{
			AvatarControl.ChooseNewAvatar();
		}
		else
		{
			Proceed();
		}
	}

	private void Proceed()
	{
		((Page)this).Frame.NavigateWithObject(typeof(SignUpEmailDetailsView), User);
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/SignUpEmailView.xaml"), (ComponentResourceLocation)0);
			UserAvatar = (AvatarControl)((FrameworkElement)this).FindName("UserAvatar");
			EnterName = (TextBlock)((FrameworkElement)this).FindName("EnterName");
			NameTextBlock = (TextBox)((FrameworkElement)this).FindName("NameTextBlock");
			cmdBarAccept = (CommandBar)((FrameworkElement)this).FindName("cmdBarAccept");
			AcceptButton = (AppBarButton)((FrameworkElement)this).FindName("AcceptButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0014: Expected O, but got Unknown
		//IL_0035: Unknown result type (might be due to invalid IL or missing references)
		//IL_003f: Expected O, but got Unknown
		//IL_0040: Unknown result type (might be due to invalid IL or missing references)
		//IL_0046: Expected O, but got Unknown
		//IL_0067: Unknown result type (might be due to invalid IL or missing references)
		//IL_0071: Expected O, but got Unknown
		//IL_0074: Unknown result type (might be due to invalid IL or missing references)
		//IL_007a: Expected O, but got Unknown
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			TextBox val2 = (TextBox)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TextChangedEventHandler, EventRegistrationToken>)val2.add_TextChanged, (Action<EventRegistrationToken>)val2.remove_TextChanged, new TextChangedEventHandler(TextBox_TextChanged));
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<KeyEventHandler, EventRegistrationToken>)val3.add_KeyDown, (Action<EventRegistrationToken>)val3.remove_KeyDown, new KeyEventHandler(TextBox_KeyDown));
			break;
		}
		case 2:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(AcceptButton_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
