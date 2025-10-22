using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Events;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Web;
using Windows.Storage;
using Windows.Storage.Streams;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Imaging;

namespace Vine.Views.Capture;

public sealed class ShareCaptureView : BasePage, IComponentConnector
{
	private bool _isMention;

	private bool _tutorialHintVisibility;

	private bool _isBusy;

	private string _textInput = string.Empty;

	private bool _isTwitterOn = ApplicationSettings.Current.IsTwitterOn;

	private bool _isFacebookOn = ApplicationSettings.Current.IsFacebookOn;

	private Brush _headerBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];

	private bool _isVineOn = true;

	private RecordingVineModel _state;

	private bool _isCommenting;

	private Visibility _tagBarVisibility = (Visibility)1;

	private readonly List<Entity> _tappedAutoCompleteList = new List<Entity>();

	private bool _isAutoCompleteListOpen;

	private bool _ignoreLostFocus;

	private bool _ignoreTextChange;

	private bool _ignoreSelectionChanged;

	public List<CancellationTokenSource> _pendingActions = new List<CancellationTokenSource>();

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button HiddenButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ToggleButton ShareTypeToggle;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid PublicPanel;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button ButtonSharePost;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button ButtonShareMessage;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ToggleSwitch ToggleSwitchVine;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ToggleSwitch ToggleSwitchTwitter;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ToggleSwitch ToggleSwitchFacebook;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Image ThumbImage;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBlock TextBoxPlaceholder;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox TextInputBox;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public string MessageHeader => ResourceHelper.GetString("Message");

	public string OkLabel => ResourceHelper.GetString("OkLabel");

	public string AddTagTitle
	{
		get
		{
			if (!IsMention)
			{
				return ResourceHelper.GetString("tag_title");
			}
			return ResourceHelper.GetString("mention_title");
		}
	}

	public bool IsMention
	{
		get
		{
			return _isMention;
		}
		set
		{
			_isMention = value;
			NotifyOfPropertyChange(() => AddTagTitle);
		}
	}

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

	public string TextInput
	{
		get
		{
			return _textInput;
		}
		set
		{
			//IL_0088: Unknown result type (might be due to invalid IL or missing references)
			//IL_00a9: Unknown result type (might be due to invalid IL or missing references)
			//IL_00af: Invalid comparison between Unknown and I4
			if (!(_textInput == value))
			{
				bool isDeleting = value != null && _textInput != null && value.Length < _textInput.Length;
				if (!TextInputChanged(isDeleting))
				{
					SetProperty(ref _textInput, value, "TextInput");
				}
				NotifyOfPropertyChange(() => CharsLeft);
				if ((int)((UIElement)TextBoxPlaceholder).Visibility == 0 && !string.IsNullOrEmpty(value))
				{
					((UIElement)TextBoxPlaceholder).put_Visibility((Visibility)1);
				}
				if ((int)((UIElement)TextBoxPlaceholder).Visibility == 1 && string.IsNullOrEmpty(value))
				{
					((UIElement)TextBoxPlaceholder).put_Visibility((Visibility)0);
				}
			}
		}
	}

	public string CharsLeft
	{
		get
		{
			if (TextInput != null)
			{
				return (120 - TextInput.Length).ToStringInvariantCulture();
			}
			return "120";
		}
	}

	public bool IsTwitterOn
	{
		get
		{
			return _isTwitterOn;
		}
		set
		{
			SetProperty(ref _isTwitterOn, value, "IsTwitterOn");
		}
	}

	public bool IsFacebookOn
	{
		get
		{
			return _isFacebookOn;
		}
		set
		{
			SetProperty(ref _isFacebookOn, value, "IsFacebookOn");
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

	private Brush ProfileBrush => (Brush)(object)ApplicationSettings.Current.User.ProfileBgBrush;

	public bool IsVineOn
	{
		get
		{
			return _isVineOn;
		}
		set
		{
			SetProperty(ref _isVineOn, value, "IsVineOn");
			if (!IsVineOn)
			{
				IsFacebookOn = false;
				IsTwitterOn = false;
			}
		}
	}

	public static bool HasChannelSelectionFromAddChannel { get; set; }

	public static ChannelModel ChannelSelectionFromAddChannel { get; set; }

	public ChannelModel Channel { get; set; }

	public string ChannelStatus
	{
		get
		{
			if (Channel != null)
			{
				return Channel.ExploreName;
			}
			return ResourceHelper.GetString("AddAChannel");
		}
	}

	public bool IsCommenting
	{
		get
		{
			return _isCommenting;
		}
		set
		{
			SetProperty(ref _isCommenting, value, "IsCommenting");
		}
	}

	public Visibility TagBarVisibility
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return _tagBarVisibility;
		}
		set
		{
			//IL_0007: Unknown result type (might be due to invalid IL or missing references)
			SetProperty(ref _tagBarVisibility, value, "TagBarVisibility");
		}
	}

	public ObservableCollection<Entity> AutoCompleteList { get; set; }

	public bool IsAutoCompleteListOpen
	{
		get
		{
			return _isAutoCompleteListOpen;
		}
		set
		{
			SetProperty(ref _isAutoCompleteListOpen, value, "IsAutoCompleteListOpen");
		}
	}

	public ShareCaptureView()
	{
		//IL_0040: Unknown result type (might be due to invalid IL or missing references)
		//IL_004a: Expected O, but got Unknown
		//IL_0053: Unknown result type (might be due to invalid IL or missing references)
		InitializeComponent();
		AutoCompleteList = new ObservableCollection<Entity>();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.Capture, "share"));
		if (NavigationParam is string && (string)NavigationParam == "IsMessageTabDefault")
		{
			ShareTypeToggle.put_IsChecked((bool?)true);
			ShareTypeClicked(this, null);
		}
		if (e.PageState != null)
		{
			IsTwitterOn = ApplicationSettings.Current.IsTwitterEnabled && e.LoadValueOrDefault<bool>("IsTwitterOn");
			IsFacebookOn = ApplicationSettings.Current.IsFacebookEnabled && e.LoadValueOrDefault<bool>("IsFacebookOn");
			TextInput = e.LoadValueOrDefault<string>("ShareText");
			if (HasChannelSelectionFromAddChannel)
			{
				Channel = ChannelSelectionFromAddChannel;
			}
			else
			{
				Channel = e.LoadValueOrDefault<ChannelModel>("Channel");
			}
		}
		HasChannelSelectionFromAddChannel = false;
		ShareCaptureView shareCaptureView = this;
		_ = shareCaptureView._state;
		shareCaptureView._state = await ApplicationSettings.Current.GetRecordingActiveVine();
		await _state.SaveThumbAsync();
		IRandomAccessStreamWithContentType source = await (await StorageFile.GetFileFromPathAsync(_state.LastRenderedThumbFilePath)).OpenReadAsync();
		BitmapImage val = new BitmapImage();
		((BitmapSource)val).SetSource((IRandomAccessStream)(object)source);
		ThumbImage.put_Source((ImageSource)(object)val);
		IsVineOn = true;
		if (!ApplicationSettings.Current.HasSeenShareMessagePage && ShareTypeToggle.IsChecked == true)
		{
			TutorialHintVisibility = true;
			ApplicationSettings.Current.HasSeenShareMessagePage = true;
		}
		else
		{
			TutorialHintVisibility = false;
		}
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		base.SaveState(sender, e);
		if ((int)e.NavigationEventArgs.NavigationMode != 0 || (object)e.NavigationEventArgs.SourcePageType != typeof(HomeView))
		{
			e.PageState["IsTwitterOn"] = IsTwitterOn;
			e.PageState["IsFacebookOn"] = IsFacebookOn;
			e.PageState["ShareText"] = TextInput;
			e.PageState["Channel"] = Channel;
		}
	}

	private void ChannelSelect_Click(object sender, RoutedEventArgs e)
	{
		((Page)this).Frame.Navigate(typeof(ChannelSelectView));
	}

	private void ToggleSwitchVine_OnToggled(object sender, RoutedEventArgs e)
	{
		if (!ToggleSwitchVine.IsOn)
		{
			ToggleSwitchFacebook.put_IsOn(false);
			ToggleSwitchTwitter.put_IsOn(false);
		}
	}

	private async void ToggleSwitchTwitter_OnToggled(object sender, RoutedEventArgs e)
	{
		if (IsTwitterOn && !ApplicationSettings.Current.IsTwitterEnabled)
		{
			await Task.Delay(500);
			App.RootFrame.NavigateWithObject(typeof(LoginTwitterView), new LoginParameters
			{
				UpdatingTwitterCredentials = true
			});
		}
	}

	private async void ToggleSwitchFacebook_OnToggled(object sender, RoutedEventArgs e)
	{
		if (IsFacebookOn && !ApplicationSettings.Current.IsFacebookEnabled)
		{
			await Task.Delay(500);
			((Page)this).Frame.Navigate(typeof(FacebookView));
		}
	}

	private void OnSharePostClick(object sender, RoutedEventArgs e)
	{
		Post(isMessage: false);
		ApplicationSettings.Current.IsFacebookOn = IsFacebookOn;
		ApplicationSettings.Current.IsTwitterOn = IsTwitterOn;
	}

	private void OnShareMessageClick(object sender, RoutedEventArgs e)
	{
		Post(isMessage: true);
	}

	private async Task Post(bool isMessage)
	{
		UploadJob uploadJob = await RecordingVineModel.NewUploadJob(_state, TextInput, (Channel == null) ? "0" : Channel.ChannelId, isMessage);
		string text = TextInput;
		List<Entity> entities = new List<Entity>();
		if (_tappedAutoCompleteList.Any())
		{
			text = text.Replace("\u200b", string.Empty);
			_tappedAutoCompleteList.Apply(delegate(Entity x)
			{
				x.Text = x.Text.Replace("\u200b", string.Empty);
			});
			entities = EntityHelper.BuildFromList(text, _tappedAutoCompleteList);
			_tappedAutoCompleteList.Apply(delegate(Entity x)
			{
				x.Text = "\u200b" + x.Text + "\u200b";
			});
		}
		if (!isMessage)
		{
			uploadJob.PostToTwitter = IsTwitterOn;
			uploadJob.PostToFb = IsFacebookOn;
			uploadJob.Text = text;
			uploadJob.Entities = entities;
			UploadJobsViewModel.Current.Add(uploadJob);
			ApplicationSettings.Current.SetRecordingActiveVine(null);
			EventAggregator.Current.Publish(new IgnoreNavigationParameter());
			App.HomePivotIndex = 0;
			App.HomeScrollToTop = true;
			App.RootFrame.Navigate(typeof(HomeView));
		}
		else
		{
			uploadJob.Message = text;
			App.RootFrame.NavigateWithObject(typeof(ShareMessageView), new ShareViewParameters
			{
				Job = uploadJob
			});
		}
	}

	private async void ShareTypeClicked(object sender, RoutedEventArgs e)
	{
		if (ShareTypeToggle.IsChecked != true)
		{
			((UIElement)PublicPanel).put_Visibility((Visibility)0);
			((UIElement)ButtonShareMessage).put_Visibility((Visibility)1);
			((UIElement)ButtonSharePost).put_Visibility((Visibility)0);
			HeaderBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];
			TutorialHintVisibility = false;
			return;
		}
		((UIElement)PublicPanel).put_Visibility((Visibility)1);
		((UIElement)ButtonShareMessage).put_Visibility((Visibility)0);
		((UIElement)ButtonSharePost).put_Visibility((Visibility)1);
		HeaderBrush = ProfileBrush;
		if (!ApplicationSettings.Current.HasSeenShareMessagePage)
		{
			TutorialHintVisibility = true;
			ApplicationSettings.Current.HasSeenShareMessagePage = true;
		}
	}

	private void TextBox_OnGotFocus(object sender, RoutedEventArgs e)
	{
		//IL_0013: Unknown result type (might be due to invalid IL or missing references)
		//IL_0018: Unknown result type (might be due to invalid IL or missing references)
		//IL_002c: Expected O, but got Unknown
		IsCommenting = true;
		TagBarVisibility = (Visibility)0;
		Frame rootFrame = App.RootFrame;
		TranslateTransform val = new TranslateTransform();
		val.put_Y(-100.0);
		((UIElement)rootFrame).put_RenderTransform((Transform)val);
	}

	private async void TextBox_OnLostFocus(object sender, RoutedEventArgs e)
	{
		if (IsCommenting)
		{
			((Control)TextInputBox).Focus((FocusState)3);
			await Task.Delay(100);
			if (_ignoreLostFocus)
			{
				_ignoreLostFocus = false;
				return;
			}
			IsCommenting = false;
			TagBarVisibility = (Visibility)1;
			IsAutoCompleteListOpen = false;
			((Control)HiddenButton).Focus((FocusState)3);
			Frame rootFrame = App.RootFrame;
			TranslateTransform val = new TranslateTransform();
			val.put_Y(0.0);
			((UIElement)rootFrame).put_RenderTransform((Transform)val);
		}
	}

	private void Mention_Click(object sender, RoutedEventArgs e)
	{
		_ignoreLostFocus = true;
		TextInput += "@";
		TextInputBox.put_SelectionStart(TextInput.Length);
	}

	private void Hashtag_Click(object sender, RoutedEventArgs e)
	{
		_ignoreLostFocus = true;
		TextInput += "#";
		TextInputBox.put_SelectionStart(TextInput.Length);
	}

	private bool TextInputChanged(bool isDeleting)
	{
		if (_ignoreTextChange)
		{
			return false;
		}
		if (isDeleting)
		{
			List<Entity> collection = EntityHelper.BuildFromList(TextInput, _tappedAutoCompleteList);
			_tappedAutoCompleteList.Clear();
			_tappedAutoCompleteList.AddRange(collection);
		}
		if (TextInputBox.Text.Contains("@"))
		{
			UpdateTaggingAutoComplete(isMention: true);
		}
		else if (TextInputBox.Text.Contains("#"))
		{
			UpdateTaggingAutoComplete(isMention: false);
		}
		else
		{
			IsAutoCompleteListOpen = false;
		}
		return false;
	}

	private async void Tag_Click(object sender, TappedRoutedEventArgs e)
	{
		Entity tag = (Entity)((FrameworkElement)sender).DataContext;
		_ignoreLostFocus = true;
		_ignoreTextChange = true;
		AutoCompleteList.Remove(tag);
		char value = (TextInput.Contains('@') ? '@' : '#');
		int num = TextInputBox.Text.Substring(0, TextInputBox.SelectionStart).LastIndexOf(value);
		string beginText = TextInputBox.Text.Substring(0, num);
		string text = TextInputBox.Text.Substring(TextInputBox.SelectionStart);
		string textInput = beginText + tag.Text + " " + text;
		TextInput = textInput;
		tag.Range = new int[2];
		tag.Range[0] = num;
		tag.Range[1] = num + tag.Text.Length;
		_tappedAutoCompleteList.Add(tag);
		IsAutoCompleteListOpen = false;
		await ((FrameworkElement)(object)TextInputBox).LayoutUpdatedAsync();
		await Task.Delay(100);
		_ignoreSelectionChanged = true;
		TextInputBox.put_SelectionStart(beginText.Length + tag.Text.Length + 1);
		TextInputBox.put_SelectionLength(0);
		_ignoreSelectionChanged = false;
		_ignoreTextChange = false;
	}

	private void IgnoreFocus(object sender, RoutedEventArgs e)
	{
		_ignoreLostFocus = true;
	}

	private void TextBox_OnSelectionChanged(object sender, RoutedEventArgs e)
	{
		if (_ignoreSelectionChanged)
		{
			return;
		}
		_ignoreSelectionChanged = true;
		List<Entity> list = EntityHelper.BuildFromList(TextInputBox.Text, _tappedAutoCompleteList);
		_tappedAutoCompleteList.Clear();
		_tappedAutoCompleteList.AddRange(list);
		int start = TextInputBox.SelectionStart;
		Entity entity = list.FirstOrDefault((Entity x) => x.Range[0] <= start && start <= x.Range[1]);
		if (entity != null)
		{
			if (entity.Range[1] == TextInput.Length - 1 && TextInputBox.Text.Last() != ' ')
			{
				_ignoreTextChange = true;
				TextBox textInputBox = TextInputBox;
				textInputBox.put_Text(textInputBox.Text + " ");
				_ignoreTextChange = false;
			}
			TextInputBox.put_SelectionStart(entity.Range[0]);
			TextInputBox.put_SelectionLength(entity.Range[1] - entity.Range[0]);
		}
		_ignoreSelectionChanged = false;
	}

	private async Task UpdateTaggingAutoComplete(bool isMention)
	{
		if (_ignoreTextChange)
		{
			return;
		}
		char value = (isMention ? '@' : '#');
		int startIndex = TextInputBox.Text.Substring(0, TextInputBox.SelectionStart).LastIndexOf(value);
		string searchText = TextInputBox.Text.Substring(startIndex + 1, TextInputBox.SelectionStart - 1 - startIndex);
		if (string.IsNullOrWhiteSpace(searchText) || _tappedAutoCompleteList.Any((Entity r) => r.Range[0] == startIndex))
		{
			IsAutoCompleteListOpen = false;
			return;
		}
		CancelAllPendingActions();
		CancellationTokenSource cancelToken = new CancellationTokenSource();
		_pendingActions.Add(cancelToken);
		IsBusy = true;
		new List<Entity>();
		List<Entity> newItemsSource;
		if (isMention)
		{
			ApiResult<BaseVineResponseModel<VineUsersMetaModel>> apiResult = await App.Api.UserSearch(searchText, cancelToken.Token, null, null, "mention");
			if (cancelToken.IsCancellationRequested || apiResult.HasError || apiResult.Model.Data == null)
			{
				return;
			}
			if (!apiResult.Model.Data.Records.Any())
			{
				IsBusy = false;
				return;
			}
			newItemsSource = apiResult.Model.Data.Records.Select((VineUserModel x) => new Entity
			{
				Text = x.Username,
				User = x,
				Id = x.UserId,
				Type = EntityType.mention.ToString()
			}).ToList();
		}
		else
		{
			ApiResult<BaseVineResponseModel<VineTagMetaModel>> apiResult2 = await App.Api.TagSearch(searchText, cancelToken.Token);
			if (cancelToken.IsCancellationRequested || apiResult2.HasError || apiResult2.Model.Data == null)
			{
				return;
			}
			if (!apiResult2.Model.Data.Records.Any())
			{
				IsBusy = false;
				return;
			}
			newItemsSource = apiResult2.Model.Data.Records.Select((VineTagModel x) => new Entity
			{
				Text = x.FormattedTag,
				Title = x.Tag,
				Id = x.TagId,
				Type = EntityType.tag.ToString()
			}).ToList();
		}
		AutoCompleteList.Repopulate(newItemsSource);
		IsAutoCompleteListOpen = AutoCompleteList.Any();
		if (!cancelToken.IsCancellationRequested)
		{
			IsBusy = false;
		}
	}

	private void CancelAllPendingActions()
	{
		foreach (CancellationTokenSource pendingAction in _pendingActions)
		{
			pendingAction.Cancel();
		}
		_pendingActions.Clear();
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/Capture/ShareCaptureView.xaml"), (ComponentResourceLocation)0);
			HiddenButton = (Button)((FrameworkElement)this).FindName("HiddenButton");
			ShareTypeToggle = (ToggleButton)((FrameworkElement)this).FindName("ShareTypeToggle");
			PublicPanel = (Grid)((FrameworkElement)this).FindName("PublicPanel");
			ButtonSharePost = (Button)((FrameworkElement)this).FindName("ButtonSharePost");
			ButtonShareMessage = (Button)((FrameworkElement)this).FindName("ButtonShareMessage");
			ToggleSwitchVine = (ToggleSwitch)((FrameworkElement)this).FindName("ToggleSwitchVine");
			ToggleSwitchTwitter = (ToggleSwitch)((FrameworkElement)this).FindName("ToggleSwitchTwitter");
			ToggleSwitchFacebook = (ToggleSwitch)((FrameworkElement)this).FindName("ToggleSwitchFacebook");
			ThumbImage = (Image)((FrameworkElement)this).FindName("ThumbImage");
			TextBoxPlaceholder = (TextBlock)((FrameworkElement)this).FindName("TextBoxPlaceholder");
			TextInputBox = (TextBox)((FrameworkElement)this).FindName("TextInputBox");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0042: Unknown result type (might be due to invalid IL or missing references)
		//IL_0048: Expected O, but got Unknown
		//IL_0069: Unknown result type (might be due to invalid IL or missing references)
		//IL_0073: Expected O, but got Unknown
		//IL_0079: Unknown result type (might be due to invalid IL or missing references)
		//IL_007f: Expected O, but got Unknown
		//IL_00a0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00aa: Expected O, but got Unknown
		//IL_00b0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b6: Expected O, but got Unknown
		//IL_00d7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e1: Expected O, but got Unknown
		//IL_00e7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ed: Expected O, but got Unknown
		//IL_010e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0118: Expected O, but got Unknown
		//IL_011e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0124: Expected O, but got Unknown
		//IL_0145: Unknown result type (might be due to invalid IL or missing references)
		//IL_014f: Expected O, but got Unknown
		//IL_0155: Unknown result type (might be due to invalid IL or missing references)
		//IL_015b: Expected O, but got Unknown
		//IL_017c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0186: Expected O, but got Unknown
		//IL_018c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0192: Expected O, but got Unknown
		//IL_01b3: Unknown result type (might be due to invalid IL or missing references)
		//IL_01bd: Expected O, but got Unknown
		//IL_01c3: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c9: Expected O, but got Unknown
		//IL_01ea: Unknown result type (might be due to invalid IL or missing references)
		//IL_01f4: Expected O, but got Unknown
		//IL_01fa: Unknown result type (might be due to invalid IL or missing references)
		//IL_0200: Expected O, but got Unknown
		//IL_0221: Unknown result type (might be due to invalid IL or missing references)
		//IL_022b: Expected O, but got Unknown
		//IL_0231: Unknown result type (might be due to invalid IL or missing references)
		//IL_0237: Expected O, but got Unknown
		//IL_0258: Unknown result type (might be due to invalid IL or missing references)
		//IL_0262: Expected O, but got Unknown
		//IL_0268: Unknown result type (might be due to invalid IL or missing references)
		//IL_026e: Expected O, but got Unknown
		//IL_028f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0299: Expected O, but got Unknown
		//IL_029f: Unknown result type (might be due to invalid IL or missing references)
		//IL_02a5: Expected O, but got Unknown
		//IL_02c6: Unknown result type (might be due to invalid IL or missing references)
		//IL_02d0: Expected O, but got Unknown
		//IL_02d6: Unknown result type (might be due to invalid IL or missing references)
		//IL_02dc: Expected O, but got Unknown
		//IL_02fd: Unknown result type (might be due to invalid IL or missing references)
		//IL_0307: Expected O, but got Unknown
		//IL_0308: Unknown result type (might be due to invalid IL or missing references)
		//IL_030e: Expected O, but got Unknown
		//IL_032f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0339: Expected O, but got Unknown
		//IL_033a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0340: Expected O, but got Unknown
		//IL_0361: Unknown result type (might be due to invalid IL or missing references)
		//IL_036b: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(Tag_Click));
			break;
		}
		case 2:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(Tag_Click));
			break;
		}
		case 3:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(ShareTypeClicked));
			break;
		}
		case 4:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(OnSharePostClick));
			break;
		}
		case 5:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(OnShareMessageClick));
			break;
		}
		case 6:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_GotFocus, (Action<EventRegistrationToken>)val.remove_GotFocus, new RoutedEventHandler(IgnoreFocus));
			break;
		}
		case 7:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(ChannelSelect_Click));
			break;
		}
		case 8:
		{
			ToggleSwitch val4 = (ToggleSwitch)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val4.add_Toggled, (Action<EventRegistrationToken>)val4.remove_Toggled, new RoutedEventHandler(ToggleSwitchVine_OnToggled));
			break;
		}
		case 9:
		{
			ToggleSwitch val4 = (ToggleSwitch)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val4.add_Toggled, (Action<EventRegistrationToken>)val4.remove_Toggled, new RoutedEventHandler(ToggleSwitchTwitter_OnToggled));
			break;
		}
		case 10:
		{
			ToggleSwitch val4 = (ToggleSwitch)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val4.add_Toggled, (Action<EventRegistrationToken>)val4.remove_Toggled, new RoutedEventHandler(ToggleSwitchFacebook_OnToggled));
			break;
		}
		case 11:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(Mention_Click));
			break;
		}
		case 12:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(Hashtag_Click));
			break;
		}
		case 13:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_GotFocus, (Action<EventRegistrationToken>)val.remove_GotFocus, new RoutedEventHandler(TextBox_OnGotFocus));
			val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_LostFocus, (Action<EventRegistrationToken>)val.remove_LostFocus, new RoutedEventHandler(TextBox_OnLostFocus));
			TextBox val2 = (TextBox)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_SelectionChanged, (Action<EventRegistrationToken>)val2.remove_SelectionChanged, new RoutedEventHandler(TextBox_OnSelectionChanged));
			break;
		}
		}
		_contentLoaded = true;
	}
}
