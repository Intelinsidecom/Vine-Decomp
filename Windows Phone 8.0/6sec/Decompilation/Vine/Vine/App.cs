using System;
using System.Diagnostics;
using System.IO;
using System.IO.IsolatedStorage;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Markup;
using System.Windows.Media;
using System.Windows.Navigation;
using FlurryWP8SDK;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Scheduler;
using Microsoft.Phone.Shell;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;
using Vine.Utils;
using Windows.ApplicationModel.Activation;
using Windows.ApplicationModel.DataTransfer.ShareTarget;
using Windows.Storage;

namespace Vine;

public class App : Application
{
	public Stream StreamToSave;

	public ShareOperation ShareOperation;

	private bool phoneApplicationInitialized;

	private bool _contentLoaded;

	public static PhoneApplicationFrame RootFrame { get; private set; }

	public App()
	{
		//IL_002e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0041: Unknown result type (might be due to invalid IL or missing references)
		//IL_0046: Unknown result type (might be due to invalid IL or missing references)
		//IL_0071: Expected O, but got Unknown
		((Application)this).UnhandledException += Application_UnhandledException;
		InitializeComponent();
		if ((int)(Visibility)((Application)this).Resources[(object)"PhoneLightThemeVisibility"] == 0)
		{
			DarkTheme();
		}
		bool flag = PhoneScreenSizeHelper.IsPhablet();
		ResourceDictionary val = new ResourceDictionary
		{
			Source = new Uri("/Vine;component/Style/Screen" + (flag ? "Phablet" : "Normal") + "Style.xaml", UriKind.Relative)
		};
		Application.Current.Resources.MergedDictionaries.Add(val);
		SwitchTheme(DatasProvider.Instance.ModernColor);
		InitializePhoneApplication();
		InitializeLanguage();
		if (Debugger.IsAttached)
		{
			PhoneApplicationService.Current.UserIdleDetectionMode = (IdleDetectionMode)1;
		}
	}

	private void DarkTheme()
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		//IL_003a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0053: Unknown result type (might be due to invalid IL or missing references)
		//IL_0058: Unknown result type (might be due to invalid IL or missing references)
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_005f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0060: Unknown result type (might be due to invalid IL or missing references)
		//IL_0061: Unknown result type (might be due to invalid IL or missing references)
		//IL_0067: Unknown result type (might be due to invalid IL or missing references)
		//IL_007d: Unknown result type (might be due to invalid IL or missing references)
		//IL_008a: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a4: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e4: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_0114: Unknown result type (might be due to invalid IL or missing references)
		//IL_012e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0144: Unknown result type (might be due to invalid IL or missing references)
		//IL_015e: Unknown result type (might be due to invalid IL or missing references)
		//IL_016b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0185: Unknown result type (might be due to invalid IL or missing references)
		//IL_019e: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b8: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c5: Unknown result type (might be due to invalid IL or missing references)
		//IL_01df: Unknown result type (might be due to invalid IL or missing references)
		//IL_01f8: Unknown result type (might be due to invalid IL or missing references)
		//IL_0212: Unknown result type (might be due to invalid IL or missing references)
		//IL_021c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0236: Unknown result type (might be due to invalid IL or missing references)
		//IL_024f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0269: Unknown result type (might be due to invalid IL or missing references)
		//IL_0282: Unknown result type (might be due to invalid IL or missing references)
		//IL_029c: Unknown result type (might be due to invalid IL or missing references)
		//IL_02b5: Unknown result type (might be due to invalid IL or missing references)
		//IL_02cf: Unknown result type (might be due to invalid IL or missing references)
		//IL_02e8: Unknown result type (might be due to invalid IL or missing references)
		//IL_0302: Unknown result type (might be due to invalid IL or missing references)
		//IL_031b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0335: Unknown result type (might be due to invalid IL or missing references)
		//IL_034b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0365: Unknown result type (might be due to invalid IL or missing references)
		//IL_0372: Unknown result type (might be due to invalid IL or missing references)
		//IL_038c: Unknown result type (might be due to invalid IL or missing references)
		//IL_039c: Unknown result type (might be due to invalid IL or missing references)
		//IL_03b6: Unknown result type (might be due to invalid IL or missing references)
		//IL_03cc: Unknown result type (might be due to invalid IL or missing references)
		//IL_03e6: Unknown result type (might be due to invalid IL or missing references)
		//IL_03ff: Unknown result type (might be due to invalid IL or missing references)
		//IL_0419: Unknown result type (might be due to invalid IL or missing references)
		//IL_0432: Unknown result type (might be due to invalid IL or missing references)
		//IL_044c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0465: Unknown result type (might be due to invalid IL or missing references)
		SolidColorBrush val = (SolidColorBrush)((Application)this).Resources[(object)"PhoneRadioCheckBoxCheckBrush"];
		SolidColorBrush val2 = (SolidColorBrush)((Application)this).Resources[(object)"PhoneRadioCheckBoxBorderBrush"];
		Color val3 = (((SolidColorBrush)((Application)this).Resources[(object)"PhoneForegroundBrush"]).Color = Color.FromArgb(byte.MaxValue, byte.MaxValue, byte.MaxValue, byte.MaxValue));
		Color color = (val2.Color = val3);
		val.Color = color;
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneBackgroundBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)0, (byte)0, (byte)0);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneContrastForegroundBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)0, (byte)0, (byte)0);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneContrastBackgroundBrush"]).Color = Color.FromArgb(byte.MaxValue, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneDisabledBrush"]).Color = Color.FromArgb((byte)102, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneProgressBarBackgroundBrush"]).Color = Color.FromArgb((byte)25, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneTextCaretBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)0, (byte)0, (byte)0);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneTextBoxBrush"]).Color = Color.FromArgb((byte)191, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneTextBoxForegroundBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)0, (byte)0, (byte)0);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneTextBoxEditBackgroundBrush"]).Color = Color.FromArgb(byte.MaxValue, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneTextBoxReadOnlyBrush"]).Color = Color.FromArgb((byte)119, (byte)0, (byte)0, (byte)0);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneSubtleBrush"]).Color = Color.FromArgb((byte)153, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneTextBoxSelectionForegroundBrush"]).Color = Color.FromArgb(byte.MaxValue, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneButtonBasePressedForegroundBrush"]).Color = Color.FromArgb(byte.MaxValue, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneTextHighContrastBrush"]).Color = Color.FromArgb(byte.MaxValue, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneTextMidContrastBrush"]).Color = Color.FromArgb((byte)153, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneTextLowContrastBrush"]).Color = Color.FromArgb((byte)115, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneSemitransparentBrush"]).Color = Color.FromArgb((byte)170, (byte)0, (byte)0, (byte)0);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneChromeBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)31, (byte)31, (byte)31);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneInactiveBrush"]).Color = Color.FromArgb((byte)51, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneInverseInactiveBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)204, (byte)204, (byte)204);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneInverseBackgroundBrush"]).Color = Color.FromArgb(byte.MaxValue, byte.MaxValue, byte.MaxValue, byte.MaxValue);
		((SolidColorBrush)((Application)this).Resources[(object)"PhoneBorderBrush"]).Color = Color.FromArgb((byte)191, byte.MaxValue, byte.MaxValue, byte.MaxValue);
	}

	private void Application_Launching(object sender, LaunchingEventArgs e)
	{
		SetFlurry();
		ProdMessenger.GetMessage();
		BackgroundDownloadManager.Init();
		ManageAgents();
		ShareLaunchingEventArgs e2 = (ShareLaunchingEventArgs)(object)((e is ShareLaunchingEventArgs) ? e : null);
		if (e2 != null)
		{
			ShareOperation = e2.ShareTargetActivatedEventArgs.ShareOperation;
		}
	}

	private void ManageAgents()
	{
		ScheduledAction periodAgent = PeriodicAgent.GetPeriodAgent();
		if (periodAgent != null && (!periodAgent.IsScheduled || (periodAgent.ExpirationTime - DateTime.Now).TotalDays < 8.0))
		{
			PeriodicAgent.StartPeriodicAgent();
		}
	}

	private static void SetFlurry()
	{
		Api.StartSession("3D43GW52GJJHDDHHGN3R");
		Api.SetVersion(AppVersion.Version);
	}

	private void Application_Activated(object sender, ActivatedEventArgs e)
	{
		SetFlurry();
		ManageAgents();
	}

	private void Application_Deactivated(object sender, DeactivatedEventArgs e)
	{
		ActionWhenClosing();
	}

	private async Task ActionWhenClosing()
	{
		AsyncHelper.RunSync(() => DatasProvider.Instance.Save());
		IsolatedStorageSettings.ApplicationSettings.Save();
		AsyncHelper.RunSync(() => VineServiceWithAuth.RemoveTempFile());
		LowProfileImageDownloader.Save();
	}

	private void Application_Closing(object sender, ClosingEventArgs e)
	{
		ActionWhenClosing();
	}

	private void RootFrame_NavigationFailed(object sender, NavigationFailedEventArgs e)
	{
		if (Debugger.IsAttached)
		{
			Debugger.Break();
		}
	}

	private void Application_UnhandledException(object sender, ApplicationUnhandledExceptionEventArgs e)
	{
		if (e.ExceptionObject is UriFormatException)
		{
			if (((ContentControl)RootFrame).Content is CheckpointPage)
			{
				((CheckpointPage)((ContentControl)RootFrame).Content).ForceValidation();
				e.Handled = true;
				return;
			}
		}
		else if (e.ExceptionObject.Source != null && e.ExceptionObject.Source.StartsWith("Microsoft.ApplicationInsights"))
		{
			e.Handled = true;
			return;
		}
		((Application)this).UnhandledException -= Application_UnhandledException;
		if (Debugger.IsAttached)
		{
			Debugger.Break();
		}
	}

	private void InitializePhoneApplication()
	{
		//IL_001f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0029: Expected O, but got Unknown
		//IL_0044: Unknown result type (might be due to invalid IL or missing references)
		//IL_004e: Expected O, but got Unknown
		//IL_005a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0064: Expected O, but got Unknown
		if (!phoneApplicationInitialized)
		{
			RootFrame = (PhoneApplicationFrame)(object)new TransitionFrame();
			((Frame)RootFrame).Navigated += new NavigatedEventHandler(CompleteInitializePhoneApplication);
			((Frame)RootFrame).UriMapper = (UriMapperBase)(object)new VineUriMapper();
			((Frame)RootFrame).NavigationFailed += new NavigationFailedEventHandler(RootFrame_NavigationFailed);
			((Frame)RootFrame).Navigated += new NavigatedEventHandler(CheckForResetNavigation);
			phoneApplicationInitialized = true;
			PhoneApplicationService.Current.ContractActivated += Application_ContractActivated;
		}
	}

	private async void Application_ContractActivated(object sender, IActivatedEventArgs e)
	{
		FileSavePickerContinuationEventArgs filePickerContinuationArgs = (FileSavePickerContinuationEventArgs)(object)((e is FileSavePickerContinuationEventArgs) ? e : null);
		if (filePickerContinuationArgs == null || filePickerContinuationArgs.File == null)
		{
			return;
		}
		CachedFileManager.DeferUpdates((IStorageFile)(object)filePickerContinuationArgs.File);
		if (StreamToSave != null)
		{
			using Stream fileStream = await ((IStorageFile)(object)filePickerContinuationArgs.File).OpenStreamForWriteAsync();
			StreamToSave.Position = 0L;
			await StreamToSave.CopyToAsync(fileStream);
		}
		if ((int)(await CachedFileManager.CompleteUpdatesAsync((IStorageFile)(object)filePickerContinuationArgs.File)) == 1)
		{
			ToastHelper.Show(AppResources.VideoSaved, afternav: false, (Orientation)0);
		}
	}

	private void CompleteInitializePhoneApplication(object sender, NavigationEventArgs e)
	{
		//IL_0024: Unknown result type (might be due to invalid IL or missing references)
		//IL_002e: Expected O, but got Unknown
		if ((object)((Application)this).RootVisual != RootFrame)
		{
			((Application)this).RootVisual = (UIElement)(object)RootFrame;
		}
		((Frame)RootFrame).Navigated -= new NavigatedEventHandler(CompleteInitializePhoneApplication);
	}

	private void CheckForResetNavigation(object sender, NavigationEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Invalid comparison between Unknown and I4
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Expected O, but got Unknown
		if ((int)e.NavigationMode == 4)
		{
			((Frame)RootFrame).Navigated += new NavigatedEventHandler(ClearBackStackAfterReset);
		}
	}

	private void ClearBackStackAfterReset(object sender, NavigationEventArgs e)
	{
		//IL_000c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0016: Expected O, but got Unknown
		//IL_0017: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Invalid comparison between Unknown and I4
		((Frame)RootFrame).Navigated -= new NavigatedEventHandler(ClearBackStackAfterReset);
		if ((int)e.NavigationMode == 0 || (int)e.NavigationMode == 3)
		{
			while (RootFrame.RemoveBackEntry() != null)
			{
			}
		}
	}

	private void InitializeLanguage()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_0038: Unknown result type (might be due to invalid IL or missing references)
		try
		{
			try
			{
				((FrameworkElement)RootFrame).Language = XmlLanguage.GetLanguage(AppResources.ResourceLanguage);
			}
			catch
			{
			}
			FlowDirection flowDirection = (FlowDirection)Enum.Parse(typeof(FlowDirection), AppResources.ResourceFlowDirection);
			((FrameworkElement)RootFrame).FlowDirection = flowDirection;
		}
		catch (Exception)
		{
			if (Debugger.IsAttached)
			{
				Debugger.Break();
			}
			throw;
		}
	}

	public void SwitchTheme(int theme)
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_0016: Expected O, but got Unknown
		//IL_0026: Unknown result type (might be due to invalid IL or missing references)
		//IL_002c: Expected O, but got Unknown
		//IL_004f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0068: Unknown result type (might be due to invalid IL or missing references)
		//IL_0082: Unknown result type (might be due to invalid IL or missing references)
		//IL_0087: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00df: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f8: Unknown result type (might be due to invalid IL or missing references)
		//IL_0112: Unknown result type (might be due to invalid IL or missing references)
		//IL_0122: Unknown result type (might be due to invalid IL or missing references)
		//IL_013c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0155: Unknown result type (might be due to invalid IL or missing references)
		//IL_016f: Unknown result type (might be due to invalid IL or missing references)
		//IL_017f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0199: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b2: Unknown result type (might be due to invalid IL or missing references)
		//IL_01d1: Unknown result type (might be due to invalid IL or missing references)
		//IL_01e4: Unknown result type (might be due to invalid IL or missing references)
		//IL_01fe: Unknown result type (might be due to invalid IL or missing references)
		//IL_0203: Unknown result type (might be due to invalid IL or missing references)
		//IL_020a: Unknown result type (might be due to invalid IL or missing references)
		//IL_020f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0224: Unknown result type (might be due to invalid IL or missing references)
		//IL_022e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0242: Expected O, but got Unknown
		//IL_0248: Unknown result type (might be due to invalid IL or missing references)
		//IL_024d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0262: Unknown result type (might be due to invalid IL or missing references)
		//IL_026c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0280: Expected O, but got Unknown
		//IL_0291: Unknown result type (might be due to invalid IL or missing references)
		//IL_02aa: Unknown result type (might be due to invalid IL or missing references)
		//IL_02c4: Unknown result type (might be due to invalid IL or missing references)
		//IL_02c9: Unknown result type (might be due to invalid IL or missing references)
		//IL_02e3: Unknown result type (might be due to invalid IL or missing references)
		//IL_02e8: Unknown result type (might be due to invalid IL or missing references)
		//IL_0302: Unknown result type (might be due to invalid IL or missing references)
		//IL_0307: Unknown result type (might be due to invalid IL or missing references)
		//IL_0321: Unknown result type (might be due to invalid IL or missing references)
		//IL_033a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0354: Unknown result type (might be due to invalid IL or missing references)
		//IL_036d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0387: Unknown result type (might be due to invalid IL or missing references)
		//IL_0397: Unknown result type (might be due to invalid IL or missing references)
		//IL_03b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_03c1: Unknown result type (might be due to invalid IL or missing references)
		//IL_03db: Unknown result type (might be due to invalid IL or missing references)
		//IL_03e0: Unknown result type (might be due to invalid IL or missing references)
		//IL_03f6: Unknown result type (might be due to invalid IL or missing references)
		//IL_0415: Unknown result type (might be due to invalid IL or missing references)
		//IL_042f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0434: Unknown result type (might be due to invalid IL or missing references)
		//IL_043b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0440: Unknown result type (might be due to invalid IL or missing references)
		//IL_0455: Unknown result type (might be due to invalid IL or missing references)
		//IL_045f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0473: Expected O, but got Unknown
		//IL_0479: Unknown result type (might be due to invalid IL or missing references)
		//IL_047e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0493: Unknown result type (might be due to invalid IL or missing references)
		//IL_049d: Unknown result type (might be due to invalid IL or missing references)
		//IL_04b1: Expected O, but got Unknown
		//IL_04be: Unknown result type (might be due to invalid IL or missing references)
		//IL_04dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_04f7: Unknown result type (might be due to invalid IL or missing references)
		//IL_0507: Unknown result type (might be due to invalid IL or missing references)
		//IL_0521: Unknown result type (might be due to invalid IL or missing references)
		//IL_0526: Unknown result type (might be due to invalid IL or missing references)
		//IL_0540: Unknown result type (might be due to invalid IL or missing references)
		//IL_0545: Unknown result type (might be due to invalid IL or missing references)
		//IL_055f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0564: Unknown result type (might be due to invalid IL or missing references)
		//IL_057e: Unknown result type (might be due to invalid IL or missing references)
		//IL_058e: Unknown result type (might be due to invalid IL or missing references)
		//IL_05a8: Unknown result type (might be due to invalid IL or missing references)
		//IL_05b8: Unknown result type (might be due to invalid IL or missing references)
		//IL_05d2: Unknown result type (might be due to invalid IL or missing references)
		//IL_05e2: Unknown result type (might be due to invalid IL or missing references)
		//IL_05fc: Unknown result type (might be due to invalid IL or missing references)
		//IL_060c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0626: Unknown result type (might be due to invalid IL or missing references)
		//IL_062b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0645: Unknown result type (might be due to invalid IL or missing references)
		//IL_064a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0651: Unknown result type (might be due to invalid IL or missing references)
		//IL_0656: Unknown result type (might be due to invalid IL or missing references)
		//IL_0662: Unknown result type (might be due to invalid IL or missing references)
		//IL_066c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0680: Expected O, but got Unknown
		//IL_0686: Unknown result type (might be due to invalid IL or missing references)
		//IL_068b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0697: Unknown result type (might be due to invalid IL or missing references)
		//IL_06a1: Unknown result type (might be due to invalid IL or missing references)
		//IL_06b5: Expected O, but got Unknown
		SolidColorBrush val = (SolidColorBrush)((Application)this).Resources[(object)"ModernHeaderBrush"];
		SolidColorBrush val2 = (SolidColorBrush)((Application)this).Resources[(object)"ModernForegroundBrush"];
		switch (theme)
		{
		case 0:
		{
			((SolidColorBrush)((Application)this).Resources[(object)"NoStripeBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)249, (byte)249, (byte)250);
			((SolidColorBrush)((Application)this).Resources[(object)"PostBackgroundBrush"]).Color = Colors.White;
			((SolidColorBrush)((Application)this).Resources[(object)"PostForegroundBrush"]).Color = Colors.Black;
			((SolidColorBrush)((Application)this).Resources[(object)"ThemeTextBrush"]).Color = Colors.Black;
			((SolidColorBrush)((Application)this).Resources[(object)"GreyBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)210, (byte)210, (byte)210);
			((SolidColorBrush)((Application)this).Resources[(object)"DarkerBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)107, (byte)107, (byte)107);
			((SolidColorBrush)((Application)this).Resources[(object)"ShadowBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)217, (byte)217, (byte)217);
			((SolidColorBrush)((Application)this).Resources[(object)"BackCommandBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)107, (byte)107, (byte)107);
			((SolidColorBrush)((Application)this).Resources[(object)"HeaderBottomSeparatorBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)224, (byte)224, (byte)224);
			val.Color = Color.FromArgb(byte.MaxValue, byte.MaxValue, byte.MaxValue, byte.MaxValue);
			val2.Color = Color.FromArgb(byte.MaxValue, (byte)0, (byte)0, (byte)0);
			LinearGradientBrush val5 = (LinearGradientBrush)((Application)this).Resources[(object)"StripeBrush"];
			((PresentationFrameworkCollection<GradientStop>)(object)((GradientBrush)val5).GradientStops)[0] = new GradientStop
			{
				Color = Color.FromArgb(byte.MaxValue, (byte)243, (byte)243, (byte)243),
				Offset = 0.499999
			};
			((PresentationFrameworkCollection<GradientStop>)(object)((GradientBrush)val5).GradientStops)[1] = new GradientStop
			{
				Color = Color.FromArgb(byte.MaxValue, (byte)235, (byte)235, (byte)235),
				Offset = 0.499999
			};
			break;
		}
		case 1:
		{
			((SolidColorBrush)((Application)this).Resources[(object)"NoStripeBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)242, (byte)242, (byte)242);
			((SolidColorBrush)((Application)this).Resources[(object)"PostBackgroundBrush"]).Color = Colors.White;
			((SolidColorBrush)((Application)this).Resources[(object)"PostForegroundBrush"]).Color = Colors.Black;
			((SolidColorBrush)((Application)this).Resources[(object)"ThemeTextBrush"]).Color = Colors.Black;
			((SolidColorBrush)((Application)this).Resources[(object)"GreyBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)210, (byte)210, (byte)210);
			((SolidColorBrush)((Application)this).Resources[(object)"ShadowBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)217, (byte)217, (byte)217);
			((SolidColorBrush)((Application)this).Resources[(object)"DarkerBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)107, (byte)107, (byte)107);
			((SolidColorBrush)((Application)this).Resources[(object)"BackCommandBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)107, (byte)107, (byte)107);
			((SolidColorBrush)((Application)this).Resources[(object)"HeaderBottomSeparatorBrush"]).Color = Colors.Transparent;
			val.Color = Color.FromArgb(byte.MaxValue, (byte)33, (byte)33, (byte)33);
			val2.Color = Color.FromArgb(byte.MaxValue, byte.MaxValue, byte.MaxValue, byte.MaxValue);
			LinearGradientBrush val4 = (LinearGradientBrush)((Application)this).Resources[(object)"StripeBrush"];
			((PresentationFrameworkCollection<GradientStop>)(object)((GradientBrush)val4).GradientStops)[0] = new GradientStop
			{
				Color = Color.FromArgb(byte.MaxValue, (byte)243, (byte)243, (byte)243),
				Offset = 0.499999
			};
			((PresentationFrameworkCollection<GradientStop>)(object)((GradientBrush)val4).GradientStops)[1] = new GradientStop
			{
				Color = Color.FromArgb(byte.MaxValue, (byte)235, (byte)235, (byte)235),
				Offset = 0.499999
			};
			break;
		}
		case 2:
		{
			val.Color = Color.FromArgb(byte.MaxValue, (byte)33, (byte)33, (byte)33);
			val2.Color = Color.FromArgb(byte.MaxValue, byte.MaxValue, byte.MaxValue, byte.MaxValue);
			((SolidColorBrush)((Application)this).Resources[(object)"NoStripeBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)20, (byte)20, (byte)20);
			((SolidColorBrush)((Application)this).Resources[(object)"PostBackgroundBrush"]).Color = Colors.Black;
			((SolidColorBrush)((Application)this).Resources[(object)"PostForegroundBrush"]).Color = Colors.White;
			((SolidColorBrush)((Application)this).Resources[(object)"ThemeTextBrush"]).Color = Colors.White;
			((SolidColorBrush)((Application)this).Resources[(object)"GreyBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)90, (byte)90, (byte)90);
			((SolidColorBrush)((Application)this).Resources[(object)"ShadowBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)26, (byte)26, (byte)26);
			((SolidColorBrush)((Application)this).Resources[(object)"DarkerBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)64, (byte)64, (byte)64);
			((SolidColorBrush)((Application)this).Resources[(object)"BackCommandBrush"]).Color = Color.FromArgb(byte.MaxValue, (byte)90, (byte)90, (byte)90);
			((SolidColorBrush)((Application)this).Resources[(object)"HeaderBottomSeparatorBrush"]).Color = Colors.Transparent;
			LinearGradientBrush val3 = (LinearGradientBrush)((Application)this).Resources[(object)"StripeBrush"];
			((PresentationFrameworkCollection<GradientStop>)(object)((GradientBrush)val3).GradientStops)[0] = new GradientStop
			{
				Color = Color.FromArgb(byte.MaxValue, (byte)20, (byte)20, (byte)20),
				Offset = 0.499999
			};
			((PresentationFrameworkCollection<GradientStop>)(object)((GradientBrush)val3).GradientStops)[1] = new GradientStop
			{
				Color = Color.FromArgb(byte.MaxValue, (byte)12, (byte)12, (byte)12),
				Offset = 0.499999
			};
			break;
		}
		}
	}

	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/App.xaml", UriKind.Relative));
		}
	}
}
