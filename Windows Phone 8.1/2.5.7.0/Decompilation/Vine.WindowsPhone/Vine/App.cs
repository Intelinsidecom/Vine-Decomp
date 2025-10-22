using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Text;
using System.Threading.Tasks;
using HockeyApp;
using Vine.Common;
using Vine.Events;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Scribe;
using Vine.Views;
using Vine.Views.Capture;
using Vine.Vine_WindowsPhone_XamlTypeInfo;
using Vine.Web;
using Windows.ApplicationModel;
using Windows.ApplicationModel.Activation;
using Windows.ApplicationModel.Background;
using Windows.Graphics.Display;
using Windows.Media.SpeechRecognition;
using Windows.Security.Cryptography.Core;
using Windows.Security.ExchangeActiveSyncProvisioning;
using Windows.System;
using Windows.Storage;
using Windows.UI;
using Windows.UI.Notifications;
using Windows.UI.Popups;
using Windows.UI.ViewManagement;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Animation;
using Windows.UI.Xaml.Navigation;

namespace Vine;

public sealed class App : Application, IComponentConnector, IXamlMetadataProvider
{
	public static EventHandler<IContinuationActivatedEventArgs> ContinuationEventArgsChanged;

	private static int _homePivotIndex = -1;

	private static WebDataProvider _api = null;

	private static ScribeTimer _scribeTimer;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	private XamlTypeInfoProvider _provider;

	public static Frame RootFrame { get; set; }

	public static LaunchParameters LaunchParams { get; set; }

	public static DateTime LastUpdateTile { get; set; }

	public static StorageFile TempNewAvatar { get; set; }

	public static int HomePivotIndex
	{
		get
		{
			return _homePivotIndex;
		}
		set
		{
			_homePivotIndex = value;
		}
	}

	public static bool HomeScrollToTop { get; set; }

	public static bool ClearBackStack { get; set; }

	public static Color? DefaultStatusBarColor { get; set; }

	public static WebDataProvider Api
	{
		get
		{
			if (_api == null)
			{
				_api = new WebDataProvider();
			}
			return _api;
		}
	}

	public static ScribeTimer ScribeService => _scribeTimer ?? (_scribeTimer = new ScribeTimer());

	public static double WindowWidth => Window.Current.CoreWindow.Bounds.Width;

	public static double WindowHeight => Window.Current.CoreWindow.Bounds.Height;

	private static bool IsSigningOut { get; set; }

	public App()
	{
		//IL_0067: Unknown result type (might be due to invalid IL or missing references)
		//IL_0071: Expected O, but got Unknown
		TelemetryInitializer.InitializeAsync();
		HockeyClient.Current.Configure("cf23c5ab0c73469b911dc6dd1578a53a").SetExceptionDescriptionLoader(delegate(Exception e)
		{
			string stackTrace = e.StackTrace;
			Type type = null;
			try
			{
				type = ((RootFrame != null && ((ContentControl)RootFrame).Content != null) ? ((ContentControl)RootFrame).Content.GetType() : null);
			}
			catch
			{
			}
			string text = (((object)type != null) ? ("\n\nPage: " + type) : "");
			string message = e.Message;
			string text2 = "\nWindows " + (OSVersionHelper.IsWindows10 ? "10" : ApplicationSettings.Current.OSVersion);
			string text3 = string.Concat(((object)e).GetType(), "\n", message, "\n", stackTrace, text, text2);
			string hash = HashHelper.GetHash(HashAlgorithmNames.Sha1, text3);
			string text4 = text3 + "\nProblemHash:" + hash;
			ApplicationSettings.Current.UnhandledException = text4;
			return text4;
		});
		InitializeComponent();
		WindowsRuntimeMarshal.AddEventHandler((Func<SuspendingEventHandler, EventRegistrationToken>)((Application)this).add_Suspending, (Action<EventRegistrationToken>)((Application)this).remove_Suspending, new SuspendingEventHandler(OnSuspending));
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)((Application)this).add_Resuming, (Action<EventRegistrationToken>)((Application)this).remove_Resuming, (EventHandler<object>)OnResuming);
		((Application)this).put_RequestedTheme((ApplicationTheme)0);
		MobileNetworkInfoHelper.InitializeAsync();
	}

	protected override async void OnActivated(IActivatedEventArgs args)
	{
		_003C_003En__0(args);
		IContinuationActivatedEventArgs e = (IContinuationActivatedEventArgs)(object)((args is IContinuationActivatedEventArgs) ? args : null);
		if (ContinuationEventArgsChanged != null)
		{
			if ((int)args.Kind == 1002)
			{
				LaunchParams = null;
			}
			ContinuationEventArgsChanged(this, e);
		}
		if ((int)args.Kind == 4)
		{
			ProtocolActivatedEventArgs protocolArgs = (ProtocolActivatedEventArgs)args;
			string uri = protocolArgs.Uri.AbsoluteUri;
			if (protocolArgs.Uri.OriginalString.StartsWith("vine://post/"))
			{
				string postId = uri.Replace("vine://post/", "");
				LaunchParams = new LaunchParameters
				{
					Type = typeof(SingleVineView).ToString(),
					Data = Serialization.SerializeType(new SingleVineViewParams
					{
						PostId = postId,
						Section = Section.None
					})
				};
			}
			else if (protocolArgs.Uri.OriginalString.StartsWith("vine://_captcha_complete"))
			{
				await new MessageDialog(ResourceHelper.GetString("logging_out"), ResourceHelper.GetString("share_login_to_post"))
				{
					Commands = { (IUICommand)new UICommand(ResourceHelper.GetString("Ok")) }
				}.ShowAsync();
				SignOut();
			}
			else if (protocolArgs.Uri.OriginalString.StartsWith("vine://user/"))
			{
				string data = uri.Replace("vine://user/", "");
				LaunchParams = new LaunchParameters
				{
					Type = typeof(ProfileView).ToString(),
					Data = data
				};
			}
		}
		else if ((int)args.Kind == 16)
		{
			VoiceCommandActivatedEventArgs e2 = (VoiceCommandActivatedEventArgs)args;
			string text = e2.Result.RulePath.First();
			string text2 = SemanticInterpretation("tagSearch", e2.Result);
			LaunchParams = null;
			if (!(text == "VineRecord"))
			{
				if (text == "VineSearchTag")
				{
					LaunchParams = new LaunchParameters();
					LaunchParams.Type = typeof(HomeView).ToString();
					if (text2.StartsWith("#") || text2.Replace(" ", string.Empty).ToLower().StartsWith("hashtag"))
					{
						text2 = text2.Replace(" ", string.Empty);
						text2 = text2.Replace("#", string.Empty);
						text2 = text2.Replace("hashtag", string.Empty);
						SearchView.SearchViewParams obj = new SearchView.SearchViewParams
						{
							Pivot = 1,
							SearchTerm = text2.Replace(" ", string.Empty)
						};
						LaunchParams.Data = Serialization.SerializeType(obj);
					}
					else
					{
						SearchView.SearchViewParams obj2 = new SearchView.SearchViewParams
						{
							Pivot = 0,
							SearchTerm = text2
						};
						LaunchParams.Data = Serialization.SerializeType(obj2);
					}
				}
			}
			else
			{
				LaunchParams = new LaunchParameters();
				LaunchParams.Type = CaptureViewHelper.GetCaptureView().ToString();
			}
		}
		await SetRootFrame(args.PreviousExecutionState);
	}

	protected override async void OnLaunched(LaunchActivatedEventArgs e)
	{
		if (!string.IsNullOrEmpty(e.Arguments))
		{
			try
			{
				LaunchParams = Serialization.Deserialize<LaunchParameters>(e.Arguments);
			}
			catch (Exception)
			{
				Debugger.Break();
			}
		}
		else
		{
			LaunchParams = null;
		}
		await SetRootFrame(e.PreviousExecutionState);
		if (!string.IsNullOrEmpty(ApplicationSettings.Current.UnhandledException))
		{
			ApplicationSettings.Current.UnhandledException = null;
		}
		if (BackgroundTaskRegistration.AllTasks.Count == 0)
		{
			await Extensions.RegisterBackgroundTimedTask("Vine.Background.VineBackgroundTask");
		}
		try
		{
			await VoiceCommandManager.InstallCommandSetsFromStorageFileAsync(await StorageFile.GetFileFromApplicationUriAsync(new Uri("ms-appx:///VoiceCommands.xml", UriKind.Absolute)));
		}
		catch (Exception)
		{
			Debugger.Break();
		}
		await HockeyClient.Current.SendCrashesAsync(sendWithoutAsking: true);
		try
		{
			MessageDialog val = new MessageDialog("Vine on Windows is no longer supported or available in the Windows App Store from January 17, 2017. More here vine.co/faq.")
			{
				Commands = { (IUICommand)new UICommand("close", (UICommandInvokedHandler)null, (object)0) },
				Commands = { (IUICommand)new UICommand("more info", (UICommandInvokedHandler)null, (object)1) }
			};
			val.put_CancelCommandIndex(0u);
			IUICommand val2 = await val.ShowAsync();
			if (val2 != null && (int)val2.Id == 1)
			{
				await Launcher.LaunchUriAsync(new Uri("http://vine.co/faq"));
			}
		}
		catch
		{
		}
	}

	private async Task SetRootFrame(ApplicationExecutionState previousExecutionState)
	{
		//IL_000a: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		UIElement content = Window.Current.Content;
		RootFrame = (Frame)(object)((content is Frame) ? content : null);
		bool navigationPerformed = false;
		if (RootFrame == null)
		{
			Frame val = new Frame();
			((Control)val).put_Background((Brush)new SolidColorBrush(Colors.White));
			RootFrame = val;
			SuspensionManager.RegisterFrame(RootFrame, "AppFrame");
			RootFrame.put_CacheSize(2);
			if ((int)previousExecutionState == 3)
			{
				SetPhoneAnimations();
				if (LaunchParams == null)
				{
					try
					{
						await SuspensionManager.RestoreAsync();
						navigationPerformed = true;
					}
					catch (SuspensionManagerException)
					{
					}
				}
			}
			Window.Current.put_Content((UIElement)(object)RootFrame);
		}
		if (((ContentControl)RootFrame).Content == null)
		{
			SetPhoneAnimations();
		}
		bool flag = ((ContentControl)RootFrame).Content != null && LaunchParams == null;
		if (!navigationPerformed && !flag)
		{
			if (LaunchParams != null && LaunchParams.Type == "msgBoxOnTap")
			{
				LaunchParams.Navigate();
			}
			else if (ApplicationSettings.Current.IsNotLoggedIn)
			{
				try
				{
					StorageFile val2 = await StorageFile.GetFileFromPathAsync(ApplicationData.Current.LocalFolder.Path + "\\__ApplicationSettings");
					RootFrame.Navigate(typeof(UpgradeView), (object)val2);
				}
				catch (Exception)
				{
					RootFrame.Navigate(typeof(WelcomeView));
				}
			}
			else if (LaunchParams == null)
			{
				RootFrame.Navigate(typeof(HomeView));
			}
			else
			{
				LaunchParams.Navigate();
			}
		}
		Window.Current.Activate();
		DisplayInformation.put_AutoRotationPreferences((DisplayOrientations)2);
	}

	private string GetEmailBody()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		EasClientDeviceInformation val = new EasClientDeviceInformation();
		string systemFirmwareVersion = val.SystemFirmwareVersion;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.AppendLine(ApplicationSettings.Current.UnhandledException);
		stringBuilder.AppendLine("App version: " + ApplicationSettings.Current.ClientVersion);
		stringBuilder.AppendLine("OS version: 8.1");
		stringBuilder.AppendLine("Firmware version: " + systemFirmwareVersion);
		stringBuilder.AppendLine("Phone model: " + val.SystemSku);
		stringBuilder.AppendLine(HashHelper.GetHash(HashAlgorithmNames.Sha1, stringBuilder.ToString()));
		return stringBuilder.ToString();
	}

	private void SetPhoneAnimations()
	{
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		((ContentControl)RootFrame).put_ContentTransitions((TransitionCollection)null);
		Frame rootFrame = RootFrame;
		WindowsRuntimeMarshal.AddEventHandler((Func<NavigatedEventHandler, EventRegistrationToken>)rootFrame.add_Navigated, (Action<EventRegistrationToken>)rootFrame.remove_Navigated, new NavigatedEventHandler(RootFrame_FirstNavigated));
		ApplicationView.GetForCurrentView().SetDesiredBoundsMode((ApplicationViewBoundsMode)0);
		StatusBar forCurrentView = StatusBar.GetForCurrentView();
		forCurrentView.HideAsync();
		DefaultStatusBarColor = forCurrentView.ForegroundColor;
	}

	private unsafe void RootFrame_FirstNavigated(object sender, NavigationEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0019: Expected O, but got Unknown
		//IL_0020: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Expected O, but got Unknown
		Frame val = (Frame)sender;
		((ContentControl)val).put_ContentTransitions((TransitionCollection)null);
		WindowsRuntimeMarshal.RemoveEventHandler<NavigatedEventHandler>(new Action<EventRegistrationToken>((object)val, (nint)__ldvirtftn(Frame.remove_Navigated)), new NavigatedEventHandler(RootFrame_FirstNavigated));
	}

	private async void OnSuspending(object sender, SuspendingEventArgs e)
	{
		SuspendingDeferral deferral = e.SuspendingOperation.GetDeferral();
		ScribeService.Log(new SessionEndedEvent());
		await SuspensionManager.SaveAsync();
		await MobileNetworkInfoHelper.ShutdownAsync();
		deferral.Complete();
	}

	private void OnResuming(object sender, object o)
	{
		EventAggregator.Current.Publish(new AppResuming());
		ScribeService.Log(new SessionStartedEvent());
	}

	public static void Captcha(string url)
	{
		RootFrame.NavigateWithObject(typeof(CaptchaView), new CaptchaParameter
		{
			Url = url
		});
	}

	public static async void SignOut()
	{
		if (!IsSigningOut)
		{
			IsSigningOut = true;
			RootFrame.Navigate(typeof(WelcomeView));
			EventAggregator.Current.Publish(new SigningOut());
			ApplicationSettings.Current.VineSession = null;
			HomePivotIndex = 0;
			TileUpdateManager.CreateTileUpdaterForApplication().Clear();
			await Task.Delay(5000);
			IsSigningOut = false;
		}
	}

	private string SemanticInterpretation(string key, SpeechRecognitionResult speechRecognitionResult)
	{
		if (speechRecognitionResult.SemanticInterpretation.Properties.ContainsKey(key))
		{
			return speechRecognitionResult.SemanticInterpretation.Properties[key][0];
		}
		return "";
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		_contentLoaded = true;
	}

	public IXamlType GetXamlType(Type type)
	{
		if (_provider == null)
		{
			_provider = new XamlTypeInfoProvider();
		}
		return _provider.GetXamlTypeByType(type);
	}

	public IXamlType GetXamlType(string fullName)
	{
		if (_provider == null)
		{
			_provider = new XamlTypeInfoProvider();
		}
		return _provider.GetXamlTypeByName(fullName);
	}

	public XmlnsDefinition[] GetXmlnsDefinitions()
	{
		return (XmlnsDefinition[])(object)new XmlnsDefinition[0];
	}

	[CompilerGenerated]
	[DebuggerHidden]
	private void _003C_003En__0(IActivatedEventArgs args)
	{
		((Application)this).OnActivated(args);
	}
}
