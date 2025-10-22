using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;

namespace Vine.Views;

public sealed class VerifyPhoneCodeEnterView : BasePage, IComponentConnector
{
	private bool _isBusy;

	private string _headerText;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBlock TextBlockHeader;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox TextBoxCode;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button ButtonDone;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBlock TextBlockRetry;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public bool IsBusy
	{
		get
		{
			return _isBusy;
		}
		set
		{
			SetProperty(ref _isBusy, value, "IsBusy");
		}
	}

	public string RetryText => ResourceHelper.GetString("confirm_phone_tap_to_resend");

	public VineUserModel User => ApplicationSettings.Current.User;

	public string HeaderText
	{
		get
		{
			return _headerText;
		}
		set
		{
			_headerText = value;
			OnPropertyChanged("HeaderText");
		}
	}

	public VerifyPhoneCodeEnterView()
	{
		InitializeComponent();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		HeaderText = string.Format(ResourceHelper.GetString("confirm_phone_message"), new object[1] { User.PhoneNumber });
	}

	private async void Retry_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		IsBusy = true;
		Task<ApiResult> apiTask = App.Api.SendVerifyPhoneRequest();
		Task task = Task.Delay(1000);
		await Task.WhenAll(apiTask, task);
		apiTask.Result.PopUpErrorIfExists();
		IsBusy = false;
	}

	private async void ButtonDone_OnClick(object sender, RoutedEventArgs e)
	{
		IsBusy = true;
		ApiResult apiResult = await App.Api.VerifyPhone(TextBoxCode.Text);
		if (!apiResult.HasError)
		{
			ApplicationSettings.Current.IsPhoneVerified = true;
			((Page)this).Frame.GoBack();
		}
		else
		{
			HeaderText = ((apiResult.ResponseContent != null) ? ParsePhoneVerifyResponse(apiResult.ResponseContent) : ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR"));
			TextBlockHeader.put_Foreground((Brush)new SolidColorBrush(Colors.Red));
		}
		IsBusy = false;
	}

	private string ParsePhoneVerifyResponse(string response)
	{
		VerifyPhoneResponse verifyPhoneResponse = Serialization.Deserialize<VerifyPhoneResponse>(response);
		if (verifyPhoneResponse != null)
		{
			return verifyPhoneResponse.error;
		}
		return ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR");
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/VerifyPhoneCodeEnterView.xaml"), (ComponentResourceLocation)0);
			TextBlockHeader = (TextBlock)((FrameworkElement)this).FindName("TextBlockHeader");
			TextBoxCode = (TextBox)((FrameworkElement)this).FindName("TextBoxCode");
			ButtonDone = (Button)((FrameworkElement)this).FindName("ButtonDone");
			TextBlockRetry = (TextBlock)((FrameworkElement)this).FindName("TextBlockRetry");
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
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(ButtonDone_OnClick));
			break;
		}
		case 2:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(Retry_OnTapped));
			break;
		}
		}
		_contentLoaded = true;
	}
}
