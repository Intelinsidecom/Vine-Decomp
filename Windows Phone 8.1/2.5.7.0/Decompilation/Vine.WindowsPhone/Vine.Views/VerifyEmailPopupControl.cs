using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class VerifyEmailPopupControl : UserControl, IComponentConnector
{
	public delegate void PopupButtonClickedEventHandler(object sender, EventArgs e);

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBlock HeaderText;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button ButtonSendEmail;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button ButtonEnterCode;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button ButtonCancel;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public event PopupButtonClickedEventHandler CancelClicked;

	public event PopupButtonClickedEventHandler SendEmailClicked;

	public event PopupButtonClickedEventHandler EnterCodeClicked;

	public VerifyEmailPopupControl(string email)
	{
		InitializeComponent();
		HeaderText.put_Text(string.Format(ResourceHelper.GetString("SettingsVerifyEmailAlertMessage"), new object[2] { email, email }));
	}

	private void ButtonCancel_OnClick(object sender, RoutedEventArgs e)
	{
		if (this.CancelClicked != null)
		{
			this.CancelClicked(this, new EventArgs());
		}
	}

	private void ButtonEnterCode_OnClick(object sender, RoutedEventArgs e)
	{
		if (this.EnterCodeClicked != null)
		{
			this.EnterCodeClicked(this, new EventArgs());
		}
	}

	private void ButtonSendEmail_OnClick(object sender, RoutedEventArgs e)
	{
		if (this.SendEmailClicked != null)
		{
			this.SendEmailClicked(this, new EventArgs());
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/VerifyEmailPopupControl.xaml"), (ComponentResourceLocation)0);
			HeaderText = (TextBlock)((FrameworkElement)this).FindName("HeaderText");
			ButtonSendEmail = (Button)((FrameworkElement)this).FindName("ButtonSendEmail");
			ButtonEnterCode = (Button)((FrameworkElement)this).FindName("ButtonEnterCode");
			ButtonCancel = (Button)((FrameworkElement)this).FindName("ButtonCancel");
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
		//IL_004e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0054: Expected O, but got Unknown
		//IL_0075: Unknown result type (might be due to invalid IL or missing references)
		//IL_007f: Expected O, but got Unknown
		//IL_0082: Unknown result type (might be due to invalid IL or missing references)
		//IL_0088: Expected O, but got Unknown
		//IL_00a9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b3: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ButtonSendEmail_OnClick));
			break;
		}
		case 2:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ButtonEnterCode_OnClick));
			break;
		}
		case 3:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ButtonCancel_OnClick));
			break;
		}
		}
		_contentLoaded = true;
	}
}
