using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Windows.Foundation;
using Windows.Media.Editing;
using Windows.Media.Transcoding;
using Windows.Storage;
using Windows.Storage.Streams;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

namespace Vine.Views.Capture;

public sealed class PreviewCaptureView : BasePage, IProgress<double>, IComponentConnector
{
	private bool _hasTutorialBeenSeen = true;

	private bool _isBusy;

	private double _progressValue;

	private Task _renderTask;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid LayoutRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MediaElement MediaElement;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBlock ToUsername;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public bool HasTutorialBeenSeen
	{
		get
		{
			return _hasTutorialBeenSeen;
		}
		set
		{
			SetProperty(ref _hasTutorialBeenSeen, value, "HasTutorialBeenSeen");
		}
	}

	public string TutorialMessage => ResourceHelper.GetString("TutorialFinalMessage");

	public PreviewCaptureParams Params => (PreviewCaptureParams)base.NavigationObject;

	public StorageFile RenderFile { get; set; }

	public bool IsBusy
	{
		get
		{
			return _isBusy;
		}
		set
		{
			SetProperty(ref _isBusy, value, "IsBusy");
			NotifyOfPropertyChange(() => IsProgressZero);
		}
	}

	public bool IsProgressZero
	{
		get
		{
			if (ProgressValue == 0.0)
			{
				return IsBusy;
			}
			return false;
		}
	}

	public double ProgressValue
	{
		get
		{
			return _progressValue;
		}
		set
		{
			double progressValue = ProgressValue;
			SetProperty(ref _progressValue, value, "ProgressValue");
			if (progressValue == 0.0 && value > 0.0)
			{
				NotifyOfPropertyChange(() => IsProgressZero);
			}
		}
	}

	public Brush ShareButtonBrush
	{
		get
		{
			//IL_002e: Unknown result type (might be due to invalid IL or missing references)
			//IL_0034: Expected O, but got Unknown
			if (Params.VMParameters == null && !Params.IsMessageTabDefault)
			{
				return (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];
			}
			return (Brush)(object)ApplicationSettings.Current.User.ProfileBgBrush;
		}
	}

	public bool IsFinishedLoading { get; set; }

	public static bool InvalidatePageState { get; set; }

	public PreviewCaptureView()
	{
		InitializeComponent();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.Capture, "details"));
		if (IsBusy || IsFinishedLoading)
		{
			return;
		}
		if (Params.VMParameters != null)
		{
			ToUsername.put_Text(Params.VMParameters.OtherUser.Username);
		}
		if (e.PageState != null && !InvalidatePageState)
		{
			IsFinishedLoading = e.LoadValueOrDefault<bool>("IsFinishedLoading");
		}
		InvalidatePageState = false;
		if (IsFinishedLoading)
		{
			RenderFile = await StorageFile.GetFileFromPathAsync((await ApplicationSettings.Current.GetRecordingActiveVine()).LastRenderedVideoFilePath);
			IRandomAccessStreamWithContentType val = await RenderFile.OpenReadAsync();
			MediaElement.SetSource((IRandomAccessStream)(object)val, string.Empty);
			return;
		}
		IsBusy = true;
		await TaskManager.CleanUpTasks.WaitForEmptyQueue();
		RecordingVineModel state = default(RecordingVineModel);
		_ = state;
		state = await ApplicationSettings.Current.GetRecordingActiveVine();
		if (state.IsClipsSequentialOneFile)
		{
			RenderFile = await StorageFile.GetFileFromPathAsync(state.Clips[0].VideoFilePath);
		}
		else
		{
			MediaComposition mediaComposition = await state.GenerateMediaComposition();
			RenderFile = await state.NewVideoRenderFile();
			_renderTask = mediaComposition.RenderToFileAsync((IStorageFile)(object)RenderFile).AsTask<TranscodeFailureReason, double>(this);
			await _renderTask;
		}
		state.LastRenderedVideoFilePath = RenderFile.Path;
		await TaskManager.CleanUpTasks.RunAtEndOfQueue(() => ApplicationSettings.Current.SetRecordingActiveVine(state));
		IRandomAccessStreamWithContentType val2 = await RenderFile.OpenReadAsync();
		MediaElement.SetSource((IRandomAccessStream)(object)val2, string.Empty);
		HasTutorialBeenSeen = ApplicationSettings.Current.HasSeenCaptureTutorial;
		ApplicationSettings.Current.HasSeenCaptureTutorial = true;
		IsBusy = false;
		IsFinishedLoading = true;
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		//IL_001a: Invalid comparison between Unknown and I4
		e.put_Cancel(_renderTask != null && (int)((IAsyncInfo)_renderTask.AsAsyncAction()).Status == 0);
	}

	public void Report(double value)
	{
		DispatcherEx.BeginInvoke(delegate
		{
			ProgressValue = value;
		});
	}

	private async void SaveForLater_OnClick(object sender, RoutedEventArgs e)
	{
		IsBusy = true;
		RecordingVineModel state = await ApplicationSettings.Current.GetRecordingActiveVine();
		await Task.Delay(500);
		await state.SaveToDrafts();
		App.RootFrame.Navigate(typeof(DraftsView), (object)state.DraftId);
		IsBusy = false;
	}

	private async void SaveInCameraRoll_OnClick(object sender, RoutedEventArgs e)
	{
		IsBusy = true;
		await (await ApplicationSettings.Current.GetRecordingActiveVine()).SaveToCameraRollAsync();
		IsBusy = false;
	}

	private async void Discard_OnClick(object sender, RoutedEventArgs e)
	{
		IsBusy = true;
		RecordingVineModel state = await ApplicationSettings.Current.GetRecordingActiveVine();
		await Task.Delay(500);
		await ApplicationSettings.Current.SetRecordingActiveVine(null);
		if (state.DraftId != null)
		{
			await state.DiscardDraftChanges();
		}
		if (Params.VMParameters == null)
		{
			App.RootFrame.GoBackTo(typeof(HomeView));
		}
		else
		{
			App.RootFrame.GoBackTo(typeof(VineMessagesThreadView));
		}
		IsBusy = false;
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		e.PageState["IsFinishedLoading"] = IsFinishedLoading;
	}

	private void Back_Click(object sender, RoutedEventArgs e)
	{
		App.RootFrame.GoBack();
	}

	private void Next_Click(object sender, RoutedEventArgs e)
	{
		if (Params.VMParameters == null)
		{
			App.RootFrame.Navigate(typeof(ShareCaptureView), (object)(Params.IsMessageTabDefault ? "IsMessageTabDefault" : null));
		}
		else
		{
			PostMessage();
		}
	}

	private async void PostMessage()
	{
		UploadJob uploadJob = await RecordingVineModel.NewUploadMsgJob(await ApplicationSettings.Current.GetRecordingActiveVine(), Params.VMParameters.ConversationId);
		if (Params.VMParameters.ConversationId == null)
		{
			VineUserModel otherUser = Params.VMParameters.OtherUser;
			if (otherUser.UserType == VineUserType.User)
			{
				uploadJob.UserIds = new List<string> { otherUser.UserId };
			}
			else if (otherUser.UserType == VineUserType.Phone)
			{
				uploadJob.PhoneNumbers = new List<string> { otherUser.UserId };
			}
			else if (otherUser.UserType == VineUserType.Email)
			{
				uploadJob.Emails = new List<string> { otherUser.UserId };
			}
		}
		VineMessagesThreadView.UploadJob = uploadJob;
		App.RootFrame.GoBackTo(typeof(VineMessagesThreadView));
		await ApplicationSettings.Current.SetRecordingActiveVine(null);
	}

	private void Edit_Click(object sender, RoutedEventArgs e)
	{
		App.RootFrame.Navigate(typeof(EditClipsView));
	}

	private void ShowFlyout(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Expected O, but got Unknown
		FrameworkElement val = (FrameworkElement)sender;
		FlyoutBase.GetAttachedFlyout(val).ShowAt(val);
	}

	private void TutorialHint_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		HasTutorialBeenSeen = true;
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/Capture/PreviewCaptureView.xaml"), (ComponentResourceLocation)0);
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			MediaElement = (MediaElement)((FrameworkElement)this).FindName("MediaElement");
			ToUsername = (TextBlock)((FrameworkElement)this).FindName("ToUsername");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_002e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0034: Expected O, but got Unknown
		//IL_0055: Unknown result type (might be due to invalid IL or missing references)
		//IL_005f: Expected O, but got Unknown
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		//IL_006b: Expected O, but got Unknown
		//IL_008c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0096: Expected O, but got Unknown
		//IL_009c: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a2: Expected O, but got Unknown
		//IL_00c3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cd: Expected O, but got Unknown
		//IL_00d3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d9: Expected O, but got Unknown
		//IL_00fa: Unknown result type (might be due to invalid IL or missing references)
		//IL_0104: Expected O, but got Unknown
		//IL_010a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0110: Expected O, but got Unknown
		//IL_0131: Unknown result type (might be due to invalid IL or missing references)
		//IL_013b: Expected O, but got Unknown
		//IL_0141: Unknown result type (might be due to invalid IL or missing references)
		//IL_0147: Expected O, but got Unknown
		//IL_0168: Unknown result type (might be due to invalid IL or missing references)
		//IL_0172: Expected O, but got Unknown
		//IL_0175: Unknown result type (might be due to invalid IL or missing references)
		//IL_017b: Expected O, but got Unknown
		//IL_019c: Unknown result type (might be due to invalid IL or missing references)
		//IL_01a6: Expected O, but got Unknown
		//IL_01a9: Unknown result type (might be due to invalid IL or missing references)
		//IL_01af: Expected O, but got Unknown
		//IL_01d0: Unknown result type (might be due to invalid IL or missing references)
		//IL_01da: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Edit_Click));
			break;
		}
		case 2:
		{
			UIElement val3 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val3.add_Tapped, (Action<EventRegistrationToken>)val3.remove_Tapped, new TappedEventHandler(TutorialHint_OnTapped));
			break;
		}
		case 3:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Next_Click));
			break;
		}
		case 4:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(ShowFlyout));
			break;
		}
		case 5:
		{
			MenuFlyoutItem val2 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(SaveForLater_OnClick));
			break;
		}
		case 6:
		{
			MenuFlyoutItem val2 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(SaveInCameraRoll_OnClick));
			break;
		}
		case 7:
		{
			MenuFlyoutItem val2 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(Discard_OnClick));
			break;
		}
		case 8:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Back_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
