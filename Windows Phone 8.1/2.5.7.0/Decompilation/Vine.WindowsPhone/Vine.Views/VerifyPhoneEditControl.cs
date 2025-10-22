using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class VerifyPhoneEditControl : UserControl, IComponentConnector
{
	public delegate void PopupButtonClickedEventHandler(object sender, EventArgs e);

	public static readonly DependencyProperty IsNextEnabledProperty = DependencyProperty.Register("IsNextEnabled", typeof(bool), typeof(VerifyPhoneEditControl), new PropertyMetadata((object)false));

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBlock HeaderText;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button NextButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox TextBoxPhone;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public VineUserModel User { get; set; }

	public bool IsNextEnabled
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(IsNextEnabledProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(IsNextEnabledProperty, (object)value);
		}
	}

	public event PopupButtonClickedEventHandler VerifyPhoneClicked;

	public VerifyPhoneEditControl(VineUserModel user)
	{
		InitializeComponent();
		User = user;
		TextBoxPhone.put_Text(string.IsNullOrWhiteSpace(user.PhoneNumber) ? string.Empty : user.PhoneNumber);
		IsNextEnabled = !string.IsNullOrEmpty(TextBoxPhone.Text) && TextBoxPhone.Text.Length > 9;
		((ContentControl)NextButton).put_Content((object)ResourceHelper.GetString("next"));
	}

	private async void Button_Next_OnClick(object sender, RoutedEventArgs e)
	{
		if (TextBoxPhone.Text != User.PhoneNumber)
		{
			ApiResult<BaseVineResponseModel> obj = await App.Api.PutUser(User.UserId, null, null, null, null, TextBoxPhone.Text);
			obj.PopUpErrorIfExists();
			if (!obj.HasError)
			{
				ApplicationSettings.Current.User.PhoneNumber = TextBoxPhone.Text;
				if (this.VerifyPhoneClicked != null)
				{
					this.VerifyPhoneClicked(this, new EventArgs());
				}
			}
		}
		else if (this.VerifyPhoneClicked != null)
		{
			this.VerifyPhoneClicked(this, new EventArgs());
		}
	}

	private void TextBoxPhone_OnTextChanged(object sender, TextChangedEventArgs e)
	{
		IsNextEnabled = !string.IsNullOrEmpty(TextBoxPhone.Text) && TextBoxPhone.Text.Length > 9;
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/VerifyPhoneEditControl.xaml"), (ComponentResourceLocation)0);
			HeaderText = (TextBlock)((FrameworkElement)this).FindName("HeaderText");
			NextButton = (Button)((FrameworkElement)this).FindName("NextButton");
			TextBoxPhone = (TextBox)((FrameworkElement)this).FindName("TextBoxPhone");
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
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(Button_Next_OnClick));
			break;
		}
		case 2:
		{
			TextBox val = (TextBox)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TextChangedEventHandler, EventRegistrationToken>)val.add_TextChanged, (Action<EventRegistrationToken>)val.remove_TextChanged, new TextChangedEventHandler(TextBoxPhone_OnTextChanged));
			break;
		}
		}
		_contentLoaded = true;
	}
}
