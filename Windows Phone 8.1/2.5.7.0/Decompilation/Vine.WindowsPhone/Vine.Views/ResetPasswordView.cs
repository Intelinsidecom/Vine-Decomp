using System;
using System.CodeDom.Compiler;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Text.RegularExpressions;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class ResetPasswordView : BasePage, IComponentConnector
{
	private bool _isBusy;

	private string _email;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public string ExampleEmail => ResourceHelper.GetString("ExampleEmail");

	public string ResetPasswordText => ResourceHelper.GetString("Ok");

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

	public string Email
	{
		get
		{
			return _email;
		}
		set
		{
			_email = value;
			OnPropertyChanged("Email");
		}
	}

	public ObservableCollection<VineUserModel> Items { get; set; }

	public ResetPasswordView()
	{
		InitializeComponent();
		Items = new ObservableCollection<VineUserModel>();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.LoggedOut, "reset_password"));
		string text = e.NavigationParameter as string;
		if (!string.IsNullOrEmpty(text))
		{
			Email = text;
		}
	}

	private async void ResetPassword_Click(object sender, RoutedEventArgs e)
	{
		Regex regex = new Regex("^([a-zA-Z0-9_\\-\\.\\+]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		if (string.IsNullOrWhiteSpace(Email) || !regex.IsMatch(Email))
		{
			await new MessageDialog(ResourceHelper.GetString("InvalidEmailAddress"), ResourceHelper.GetString("vine"))
			{
				Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok")) }
			}.ShowAsync();
			return;
		}
		IsBusy = true;
		MessageDialog val = new MessageDialog(ResourceHelper.GetString("DoYouWantToResetPass"), ResourceHelper.GetString("vine"))
		{
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Yes"), (UICommandInvokedHandler)null, (object)0) },
			Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("No"), (UICommandInvokedHandler)null, (object)1) }
		};
		val.put_CancelCommandIndex(1u);
		if ((int)(await val.ShowAsync()).Id == 0)
		{
			if ((await App.Api.ResetPassword(Email)).HasError)
			{
				await new MessageDialog(ResourceHelper.GetString("ResetPasswordFailed"), ResourceHelper.GetString("vine"))
				{
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok")) }
				}.ShowAsync();
			}
			else
			{
				await new MessageDialog(ResourceHelper.GetString("ResetPasswordSuccess"), ResourceHelper.GetString("vine"))
				{
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok")) }
				}.ShowAsync();
				if (((Page)this).Frame.CanGoBack)
				{
					((Page)this).Frame.GoBack();
				}
			}
		}
		IsBusy = false;
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/ResetPasswordView.xaml"), (ComponentResourceLocation)0);
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
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ResetPassword_Click));
		}
		_contentLoaded = true;
	}
}
