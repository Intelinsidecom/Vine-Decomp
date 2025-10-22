using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Gen.Services;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Reactive;
using Vine.Controls;
using Vine.Datas;
using Vine.Models;
using Vine.Pages.PostVideo.ViewModels;
using Vine.Pages.SelectDirectFriends.ViewModels;
using Vine.Resources;
using Vine.Services;
using Vine.Utils;
using Vine.ViewModels;
using Windows.Storage;

namespace Vine.Pages.PostVideo;

public class PostVideoPage : PhoneApplicationPage
{
	public static readonly DependencyProperty StepProperty = DependencyProperty.Register("Step", typeof(EncodingStep), typeof(PostVideoPage), new PropertyMetadata((object)EncodingStep.NONE, new PropertyChangedCallback(EncodingStepCallback)));

	private List<string> _playlist = new List<string>();

	private IsolatedStorageFile isf;

	private EncodingJob _currentJob;

	private bool _gotoselectdirectfriends;

	private Transform _exframerender;

	private string _lastsearch;

	private Regex regHasTag = new Regex("(?:(?<total>(?<type>#|((?<=^|[^\\w])@))(?<val>\\w*))|(?<isspace>^|\\s))$", RegexOptions.Compiled);

	private PostVideoViewModel vm;

	private bool _forceddirect;

	internal Grid MyCommentPanel;

	internal Grid Header;

	internal Rectangle RectBackground;

	internal Grid PostVideoNormalPanel;

	internal ScrollViewer PrincScroll;

	internal StackPanel StackScroll;

	internal TextBox MyComment;

	internal Image Preview;

	internal TextBlock NbrCaractText;

	internal CheckBox TwitterToggle;

	internal CheckBox FacebookToggle;

	internal Grid PostVideoDirectPanel;

	internal ScrollViewer PrincScroll2;

	internal StackPanel StackScroll2;

	internal TextBox MyComment2;

	internal Image Preview2;

	internal Button SelectDirectFriends;

	internal ItemsControl FriendsLongListSelector;

	internal Rectangle ShadowRectangle;

	internal StackPanel ProgressInfoPanel;

	internal Button RetryButton;

	internal TextBlock InfoText;

	internal Grid SuggestionsPanel;

	internal Grid SuggestionsLine;

	internal ListBox SuggestionsList;

	internal Grid SuggestionsCommands;

	internal Grid ModernAppBarContainer;

	internal ModernAppBar ModernAppBar;

	internal ProgressBar ProgressInfo;

	internal Grid LoadingPanel;

	private bool _contentLoaded;

	public EncodingStep Step
	{
		get
		{
			return (EncodingStep)((DependencyObject)this).GetValue(StepProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(StepProperty, (object)value);
		}
	}

	public PostVideoPage()
	{
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		//IL_0095: Unknown result type (might be due to invalid IL or missing references)
		//IL_009f: Expected O, but got Unknown
		vm = new PostVideoViewModel();
		((FrameworkElement)this).DataContext = vm;
		InitializeComponent();
		((FrameworkElement)SuggestionsPanel).Margin = new Thickness(0.0, 0.0, 0.0, PhoneSizeUtils.KeyboardPortraitdWithSuggestionsHeightStatic);
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Shape)RectBackground).Fill = (Brush)Application.Current.Resources[(object)"StripeBrush"];
			((UIElement)ShadowRectangle).Visibility = (Visibility)0;
		}
		Init();
		ObservableExtensions.Subscribe<IEvent<KeyEventArgs>>(Observable.Throttle<IEvent<KeyEventArgs>>(Observable.FromEvent<KeyEventArgs>((object)MyComment, "KeyUp"), TimeSpan.FromMilliseconds(100.0)), (Action<IEvent<KeyEventArgs>>)delegate
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				ManageKeyboard(MyComment);
			});
		});
		ObservableExtensions.Subscribe<IEvent<KeyEventArgs>>(Observable.Throttle<IEvent<KeyEventArgs>>(Observable.FromEvent<KeyEventArgs>((object)MyComment2, "KeyUp"), TimeSpan.FromMilliseconds(100.0)), (Action<IEvent<KeyEventArgs>>)delegate
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				ManageKeyboard(MyComment2);
			});
		});
	}

	private void Comment_TextChanged(object sender, TextChangedEventArgs e)
	{
		//IL_0017: Unknown result type (might be due to invalid IL or missing references)
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		ScrollViewer princScroll = PrincScroll;
		Rect rectFromCharacterIndex = MyComment.GetRectFromCharacterIndex(MyComment.SelectionStart);
		princScroll.ScrollToVerticalOffset(((Rect)(ref rectFromCharacterIndex)).Top - 80.0);
		MyComment2.Text = MyComment.Text;
	}

	private void ViewProfile_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		NavigationServiceExt.ToProfile(((IPerson)((FrameworkElement)sender).DataContext).Id);
	}

	private void TwitterChecked(object sender, RoutedEventArgs e)
	{
		if (((ToggleButton)TwitterToggle).IsChecked.Value && DatasProvider.Instance.CurrentUser.TwitterAccess == null)
		{
			NavigationServiceExt.ToSyncTwitter();
		}
	}

	private void FacebookChecked(object sender, RoutedEventArgs e)
	{
		if (((ToggleButton)FacebookToggle).IsChecked.Value && DatasProvider.Instance.CurrentUser.FacebookAccess == null)
		{
			NavigationServiceExt.ToSyncFacebook();
		}
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_000e: Invalid comparison between Unknown and I4
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_002e: Invalid comparison between Unknown and I4
		((Page)this).OnNavigatingFrom(e);
		if ((int)e.NavigationMode == 1 && !_currentJob.Saved)
		{
			if ((int)MessageBox.Show(AppResources.CancelUploadingMessage, AppResources.CancelUploadingTitle, (MessageBoxButton)1) == 1)
			{
				BackgroundDownloadManager.RemoveJob(_currentJob);
			}
			else
			{
				((CancelEventArgs)(object)e).Cancel = true;
			}
		}
		UIElement rootVisual = Application.Current.RootVisual;
		((rootVisual is PhoneApplicationFrame) ? rootVisual : null).RenderTransform = _exframerender;
	}

	protected override async void OnNavigatedTo(NavigationEventArgs e)
	{
		_003C_003En__FabricatedMethod0(e);
		UIElement rootVisual = Application.Current.RootVisual;
		PhoneApplicationFrame val = (PhoneApplicationFrame)(object)((rootVisual is PhoneApplicationFrame) ? rootVisual : null);
		_exframerender = ((UIElement)val).RenderTransform;
		((UIElement)val).RenderTransform = null;
		NavigationMode navigationMode = e.NavigationMode;
		if ((int)navigationMode != 0)
		{
			if ((int)navigationMode == 1)
			{
				if (_gotoselectdirectfriends)
				{
					_gotoselectdirectfriends = false;
					SelectDirectFriendsViewModel selectDirectFriendsStatic = ViewModelLocator.SelectDirectFriendsStatic;
					vm.SetDirectPersons(selectDirectFriendsStatic.OtherItems.Concat(selectDirectFriendsStatic.SelectedPerson).ToList());
				}
				if (((ToggleButton)FacebookToggle).IsChecked.Value)
				{
					((ToggleButton)FacebookToggle).IsChecked = DatasProvider.Instance.CurrentUser.FacebookAccess != null;
				}
				if (((ToggleButton)TwitterToggle).IsChecked.Value)
				{
					((ToggleButton)TwitterToggle).IsChecked = DatasProvider.Instance.CurrentUser.TwitterAccess != null;
				}
			}
			return;
		}
		if (((Page)this).NavigationContext.QueryString.ContainsKey("id"))
		{
			string id = ((Page)this).NavigationContext.QueryString["id"];
			_currentJob = DatasProvider.Instance.Encodings.FirstOrDefault((EncodingJob f) => f.Id == id);
		}
		else
		{
			PostVideoPage postVideoPage = this;
			_ = postVideoPage._currentJob;
			postVideoPage._currentJob = await CreateJob();
		}
		string value = null;
		if (((Page)this).NavigationContext.QueryString.TryGetValue("forceddirect", out value))
		{
			_forceddirect = value == "true";
			if (_forceddirect)
			{
				((UIElement)PostVideoNormalPanel).Visibility = (Visibility)1;
				((UIElement)PostVideoDirectPanel).Visibility = (Visibility)0;
			}
		}
		((FrameworkElement)this).SetBinding(StepProperty, new Binding("State")
		{
			Source = _currentJob
		});
		vm.SetCurrentJob(_currentJob);
		if (_currentJob.VineComment != null)
		{
			MyComment.Text = _currentJob.VineComment;
		}
		((ToggleButton)TwitterToggle).IsChecked = _currentJob.VineShareTwitter;
		((ToggleButton)FacebookToggle).IsChecked = _currentJob.VineShareFacebook;
		((FrameworkElement)ProgressInfoPanel).DataContext = _currentJob;
		if (_currentJob.IsDirect)
		{
			((UIElement)SelectDirectFriends).Visibility = (Visibility)1;
			FriendsLongListSelector.ItemsSource = _currentJob.SelectedUsers;
		}
		else if (_forceddirect)
		{
			_currentJob.IsDirect = true;
			SelectDirectFriendsViewModel selectDirectFriendsStatic2 = ViewModelLocator.SelectDirectFriendsStatic;
			if (selectDirectFriendsStatic2.SelectedPerson != null)
			{
				vm.SetDirectPersons(selectDirectFriendsStatic2.OtherItems.Concat(selectDirectFriendsStatic2.SelectedPerson).ToList());
			}
		}
	}

	private async Task<double> GetCurrentDuration()
	{
		List<StorageFile> list = (await (await ApplicationData.Current.LocalFolder.CreateFolderAsync("mpegpart", (CreationCollisionOption)3)).GetFilesAsync()).ToList();
		if (list == null || !list.Any())
		{
			return 0.0;
		}
		double total = 0.0;
		try
		{
			foreach (StorageFile item in list)
			{
				total += (await item.Properties.GetVideoPropertiesAsync()).Duration.TotalSeconds;
			}
		}
		catch
		{
		}
		return total;
	}

	private static void EncodingStepCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((PostVideoPage)(object)d).EncodingStepCallback((EncodingStep)((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
	}

	private void EncodingStepCallback(EncodingStep encodingStep)
	{
		if (encodingStep == EncodingStep.ERROR)
		{
			((UIElement)RetryButton).Visibility = (Visibility)0;
		}
		ManageAppBar(encodingStep);
	}

	private void Init()
	{
		isf = IsolatedStorageFile.GetUserStoreForApplication();
		foreach (string item in from r in isf.GetFileNames("mpegpart/*")
			orderby r
			select r)
		{
			_playlist.Add("mpegpart/" + item);
		}
	}

	private void SelectDirectFriends_Click(object sender, RoutedEventArgs e)
	{
		_gotoselectdirectfriends = true;
		NavigationServiceExt.ToSelectDirectFriends();
	}

	public async Task<EncodingJob> CreateJob()
	{
		EncodingJob job = await DatasProvider.Instance.CreateEncodingJob(0.0);
		job.NoNeedEncoding = true;
		job.State = EncodingStep.BEGIN;
		job.UpdateState();
		((UIElement)RetryButton).Visibility = (Visibility)1;
		ProgressInfo.IsIndeterminate = false;
		if (((Page)this).NavigationContext.QueryString.ContainsKey("videotoupload"))
		{
			StorageFile file = await StorageFile.GetFileFromPathAsync(((Page)this).NavigationContext.QueryString["videotoupload"]);
			StorageFile imagefile = await StorageFile.GetFileFromPathAsync(((Page)this).NavigationContext.QueryString["imagetoupload"]);
			try
			{
				await SaveVideoAsync(file);
				await BackgroundDownloadManager.PrepareEncoding(job, (IEnumerable<StorageFile>)(object)new StorageFile[1] { file }, imagefile);
				BackgroundDownloadManager.BeginProcess(job);
			}
			catch
			{
			}
			return job;
		}
		InfoText.Text = "error";
		((UIElement)ProgressInfo).Visibility = (Visibility)1;
		return null;
	}

	private async Task SaveVideoAsync(StorageFile sourceFile)
	{
		try
		{
			StorageFolder val = await KnownFolders.PicturesLibrary.CreateFolderAsync("6sec", (CreationCollisionOption)3);
			string text = "6sec " + DateTime.Now.ToString("yyyy-MM-dd mm-ss") + ".avi";
			await sourceFile.CopyAsync((IStorageFolder)(object)val, text, (NameCollisionOption)1);
		}
		catch (Exception)
		{
		}
	}

	private async void Post_Click(object sender, RoutedEventArgs e)
	{
		((Control)this).Focus();
		if (DatasProvider.Instance.HAU && !AppVersion.IsCanUpload() && ((int)MessageBox.Show(AppResources.UploadLimitation, AppResources.InAppPurchase, (MessageBoxButton)1) != 1 || !(await AppVersion.BuyUpload())))
		{
			return;
		}
		DataUser user = DatasProvider.Instance.CurrentUser;
		Button button = (Button)sender;
		((Control)button).IsEnabled = false;
		SaveJobData();
		if (_currentJob.State != EncodingStep.READYTOPUBLISH)
		{
			if ((int)MessageBox.Show(AppResources.ToastCloseBeforeEndEncodingMessage, AppResources.ToastCloseBeforeEndEncodingTitle, (MessageBoxButton)1) == 1)
			{
				_currentJob.Saved = true;
				if (!((Page)this).NavigationContext.QueryString.ContainsKey("id"))
				{
					await AppUtils.ClearVideoFiles();
				}
				NavigationServiceExt.ToTranscoding(frompost: true);
			}
			else
			{
				((Control)button).IsEnabled = true;
			}
			return;
		}
		await AppUtils.ClearVideoFiles();
		((UIElement)LoadingPanel).Visibility = (Visibility)0;
		try
		{
			UploadVideoCallback(await user.Service.UploadAsync(_currentJob));
		}
		catch (ServiceServerErrorException ex)
		{
			UploadVideoError(ex);
		}
	}

	private void UploadVideoError(ServiceServerErrorException ex)
	{
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			((UIElement)LoadingPanel).Visibility = (Visibility)1;
			((Control)ModernAppBar.Buttons[0]).IsEnabled = true;
			ToastHelper.Show(ex.HttpErrorMessage ?? AppResources.ToastCantPostVideo, afternav: false, (Orientation)0);
		});
	}

	private void UploadVideoCallback(bool resupload)
	{
		Vine.Datas.Datas data = DatasProvider.Instance;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			((UIElement)LoadingPanel).Visibility = (Visibility)1;
			while (((Page)this).NavigationService.RemoveBackEntry() != null)
			{
			}
			BackgroundDownloadManager.RemoveJob(_currentJob);
			data.HAU = true;
			data.Save();
			if (!((Page)this).NavigationContext.QueryString.ContainsKey("id"))
			{
				AppUtils.ClearVideoFiles();
			}
			ToastHelper.Show(AppResources.ToastUploadDone, afternav: true, (Orientation)0);
			NavigationServiceExt.ToTimeline(null, removebackentry: true);
		});
	}

	private void SaveJobData()
	{
		_currentJob.VineComment = MyComment.Text;
		_currentJob.VineShareTwitter = ((ToggleButton)TwitterToggle).IsChecked.Value;
		_currentJob.VineShareFacebook = ((ToggleButton)FacebookToggle).IsChecked.Value;
		if (_currentJob.IsDirect && vm.DirectPersons != null)
		{
			_currentJob.SelectedUsers = vm.DirectPersons.Select((IPerson c) => new EncodingJobReceiver
			{
				Id = c.Id,
				Name = c.Name,
				SubName = c.SubName,
				Picture = c.Picture
			}).ToList();
		}
	}

	private async void Retry_Click(object sender, RoutedEventArgs e)
	{
		((UIElement)RetryButton).Visibility = (Visibility)1;
		_currentJob.State = EncodingStep.BEGIN;
		_currentJob.UpdateState();
		ProgressInfo.IsIndeterminate = false;
		try
		{
			BackgroundDownloadManager.BeginProcess(_currentJob);
		}
		catch
		{
		}
	}

	private void Comment_Changed(object sender, TextChangedEventArgs e)
	{
		//IL_0017: Unknown result type (might be due to invalid IL or missing references)
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		ScrollViewer princScroll = PrincScroll;
		Rect rectFromCharacterIndex = MyComment.GetRectFromCharacterIndex(MyComment.SelectionStart);
		princScroll.ScrollToVerticalOffset(((Rect)(ref rectFromCharacterIndex)).Top - 80.0);
	}

	private async void ManageKeyboard(TextBox textbox)
	{
		try
		{
			DataUser user = DatasProvider.Instance.CurrentUser;
			string currentsearch = null;
			Match m = regHasTag.Match((textbox.Text != null) ? textbox.Text.Substring(0, textbox.SelectionStart) : "");
			bool flag = Clipboard.ContainsText();
			InputScopeNameValue val = (InputScopeNameValue)49;
			if (m.Groups["type"].Success)
			{
				currentsearch = (_lastsearch = m.Groups["total"].Value);
				if (!flag)
				{
					val = (InputScopeNameValue)0;
				}
			}
			if (flag)
			{
				((FrameworkElement)SuggestionsLine).Margin = new Thickness(0.0, 0.0, 0.0, PhoneSizeUtils.ApplicationBarOpacityHeightStatic);
			}
			else
			{
				((FrameworkElement)SuggestionsLine).Margin = new Thickness(0.0, 0.0, 0.0, 0.0);
			}
			if (((InputScopeName)textbox.InputScope.Names[0]).NameValue != val)
			{
				InputScope val2 = new InputScope();
				val2.Names.Add((object)new InputScopeName
				{
					NameValue = val
				});
				textbox.InputScope = val2;
				((UIElement)textbox).GotFocus -= new RoutedEventHandler(Comment_GotFocus);
				((UIElement)textbox).LostFocus -= new RoutedEventHandler(Comment_LostFocus);
				((Control)this).Focus();
				((Control)textbox).Focus();
				((UIElement)textbox).GotFocus += new RoutedEventHandler(Comment_GotFocus);
				((UIElement)textbox).LostFocus += new RoutedEventHandler(Comment_LostFocus);
				((ItemsControl)SuggestionsList).ItemsSource = null;
			}
			if (m.Success)
			{
				if (m.Groups["isspace"].Success)
				{
					((UIElement)SuggestionsCommands).Visibility = (Visibility)0;
					((ItemsControl)SuggestionsList).ItemsSource = null;
				}
				else
				{
					((UIElement)SuggestionsCommands).Visibility = (Visibility)1;
					((ItemsControl)SuggestionsList).ItemsSource = null;
				}
				string value = m.Groups["type"].Value;
				if (value == "#")
				{
					try
					{
						IListTags res = default(IListTags);
						_ = res;
						res = await user.Service.SearchTagAsync(m.Groups["val"].Value, "1");
						if (!(currentsearch == _lastsearch))
						{
							return;
						}
						((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
						{
							//IL_001f: Unknown result type (might be due to invalid IL or missing references)
							//IL_0029: Expected O, but got Unknown
							((ItemsControl)SuggestionsList).ItemTemplate = (DataTemplate)Application.Current.Resources[(object)"DataTemplateTagResult"];
							((ItemsControl)SuggestionsList).ItemsSource = res.Tags.Select((ITag t) => t.Tag).ToList();
						});
						return;
					}
					catch
					{
						return;
					}
				}
				if (!(value == "@"))
				{
					return;
				}
				try
				{
					IListPersons res2 = default(IListPersons);
					_ = res2;
					res2 = await user.Service.SearchUserMentionAsync(m.Groups["val"].Value);
					if (currentsearch == _lastsearch)
					{
						((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
						{
							//IL_001f: Unknown result type (might be due to invalid IL or missing references)
							//IL_0029: Expected O, but got Unknown
							((ItemsControl)SuggestionsList).ItemTemplate = (DataTemplate)Application.Current.Resources[(object)"DataTemplateUserResult"];
							((ItemsControl)SuggestionsList).ItemsSource = res2.Persons;
						});
					}
					return;
				}
				catch
				{
					return;
				}
			}
			((UIElement)SuggestionsCommands).Visibility = (Visibility)1;
			((ItemsControl)SuggestionsList).ItemsSource = null;
		}
		catch
		{
		}
	}

	private void Comment_LostFocus(object sender, RoutedEventArgs e)
	{
		//IL_0036: Unknown result type (might be due to invalid IL or missing references)
		((UIElement)Header).Visibility = (Visibility)0;
		((FrameworkElement)StackScroll).Margin = new Thickness(12.0, 24.0, 12.0, 0.0);
		((UIElement)SuggestionsPanel).Visibility = (Visibility)1;
		((UIElement)SuggestionsLine).Visibility = (Visibility)1;
	}

	private void Comment_GotFocus(object sender, RoutedEventArgs e)
	{
		//IL_0018: Unknown result type (might be due to invalid IL or missing references)
		//IL_001d: Unknown result type (might be due to invalid IL or missing references)
		//IL_002b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0030: Unknown result type (might be due to invalid IL or missing references)
		//IL_003e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_0054: Unknown result type (might be due to invalid IL or missing references)
		//IL_0078: Unknown result type (might be due to invalid IL or missing references)
		//IL_0082: Expected O, but got Unknown
		((UIElement)Header).Visibility = (Visibility)1;
		StackPanel stackScroll = StackScroll;
		Thickness margin = ((FrameworkElement)StackScroll).Margin;
		double left = ((Thickness)(ref margin)).Left;
		margin = ((FrameworkElement)StackScroll).Margin;
		double top = ((Thickness)(ref margin)).Top;
		margin = ((FrameworkElement)StackScroll).Margin;
		((FrameworkElement)stackScroll).Margin = new Thickness(left, top, ((Thickness)(ref margin)).Right, 600.0);
		((UIElement)SuggestionsPanel).Visibility = (Visibility)0;
		((ItemsControl)SuggestionsList).ItemsSource = null;
		ManageKeyboard((TextBox)sender);
		((UIElement)SuggestionsLine).Visibility = (Visibility)0;
	}

	private void SuggestionsList_SelectionsChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_0022: Unknown result type (might be due to invalid IL or missing references)
		//IL_0028: Expected O, but got Unknown
		//IL_002e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Unknown result type (might be due to invalid IL or missing references)
		//IL_0040: Expected O, but got Unknown
		//IL_0048: Unknown result type (might be due to invalid IL or missing references)
		//IL_023a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0213: Unknown result type (might be due to invalid IL or missing references)
		//IL_0218: Unknown result type (might be due to invalid IL or missing references)
		//IL_0226: Expected O, but got Unknown
		if (e.AddedItems.Count == 0 || e.AddedItems[0] == null)
		{
			return;
		}
		InputScope val = new InputScope();
		val.Names.Add((object)new InputScopeName
		{
			NameValue = (InputScopeNameValue)49
		});
		TextBox textbox = (((int)((UIElement)PostVideoNormalPanel).Visibility == 0) ? MyComment : MyComment2);
		textbox.InputScope = val;
		object obj = e.AddedItems[0];
		string text = null;
		int num = -1;
		string text2 = textbox.Text;
		if (obj is string)
		{
			text = "#" + (string)obj;
			num = text2.Substring(0, textbox.SelectionStart).LastIndexOf("#");
		}
		else if (obj is IPerson)
		{
			IPerson user = (IPerson)obj;
			text = user.Name;
			num = text2.Substring(0, textbox.SelectionStart).LastIndexOf("@");
			if (_currentJob.CommentPeople.All((IPerson d) => d.Id != user.Id))
			{
				_currentJob.CommentPeople.Add(new SimpleUser
				{
					Name = user.Name,
					Id = user.Id
				});
			}
			DatasProvider.Instance.Save();
		}
		if (num >= 0)
		{
			int num2 = num;
			int selectionStart = textbox.SelectionStart;
			string text3 = text2.Substring(selectionStart);
			string text4 = ((text3.Length <= 0 || text3[0] != ' ') ? " " : "");
			textbox.SelectionStart = num2;
			textbox.SelectionLength = selectionStart - num2;
			textbox.SelectedText = text + text4;
			textbox.SelectionStart = num + text.Length + text4.Length + 1;
			Binding val2 = new Binding("Text.Length")
			{
				Source = textbox
			};
			((FrameworkElement)NbrCaractText).SetBinding(TextBlock.TextProperty, val2);
		}
		((Selector)(ListBox)sender).SelectedItem = null;
		((Control)textbox).Focus();
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			((Control)textbox).Focus();
		});
	}

	private void SelectionLocation_Click(object sender, RoutedEventArgs e)
	{
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Invalid comparison between Unknown and I4
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (!instance.GeoPositionAsked)
		{
			if ((int)MessageBox.Show(AppResources.AskGeoPosition, AppResources.ChooseLocation, (MessageBoxButton)1) != 1)
			{
				return;
			}
			instance.GeoPositionAsked = true;
			instance.Save();
		}
		NavigationServiceExt.ToPlaceSelection(_currentJob.Id);
	}

	private void SuggestionsAddDiese_Click(object sender, RoutedEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		TextBox obj = (((int)((UIElement)PostVideoNormalPanel).Visibility == 0) ? MyComment : MyComment2);
		((Control)obj).Focus();
		obj.SelectedText = "#";
	}

	private void SuggestionsAddAt_Click(object sender, RoutedEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		TextBox obj = (((int)((UIElement)PostVideoNormalPanel).Visibility == 0) ? MyComment : MyComment2);
		((Control)obj).Focus();
		obj.SelectedText = "@";
	}

	private void SelectionChannel_Click(object sender, RoutedEventArgs e)
	{
		NavigationServiceExt.ToChooseChannel(_currentJob.Id);
	}

	private void Comment2_TextChanged(object sender, TextChangedEventArgs e)
	{
		//IL_0017: Unknown result type (might be due to invalid IL or missing references)
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		ScrollViewer princScroll = PrincScroll2;
		Rect rectFromCharacterIndex = MyComment2.GetRectFromCharacterIndex(MyComment2.SelectionStart);
		princScroll.ScrollToVerticalOffset(((Rect)(ref rectFromCharacterIndex)).Top - 80.0);
		MyComment.Text = MyComment2.Text;
	}

	private void MyPivot_OnSelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		ManageAppBar(_currentJob.State);
	}

	private void ManageAppBar(EncodingStep encodingStep)
	{
		//IL_0031: Unknown result type (might be due to invalid IL or missing references)
		ModernAppBarButton modernAppBarButton = ModernAppBar.Buttons[0];
		bool flag = (((Control)modernAppBarButton).IsEnabled = encodingStep != EncodingStep.INITIALIZATION && encodingStep != EncodingStep.BEGIN);
		bool flag3 = flag;
		if ((int)((UIElement)PostVideoNormalPanel).Visibility == 0)
		{
			((Control)modernAppBarButton).IsEnabled = flag3;
			return;
		}
		PostVideoViewModel postVideoViewModel = (PostVideoViewModel)((FrameworkElement)this).DataContext;
		((Control)modernAppBarButton).IsEnabled = flag3 && postVideoViewModel.DirectPersons != null && postVideoViewModel.DirectPersons.Count > 0;
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
		//IL_0161: Unknown result type (might be due to invalid IL or missing references)
		//IL_016b: Expected O, but got Unknown
		//IL_0177: Unknown result type (might be due to invalid IL or missing references)
		//IL_0181: Expected O, but got Unknown
		//IL_018d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0197: Expected O, but got Unknown
		//IL_01a3: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ad: Expected O, but got Unknown
		//IL_01b9: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c3: Expected O, but got Unknown
		//IL_01cf: Unknown result type (might be due to invalid IL or missing references)
		//IL_01d9: Expected O, but got Unknown
		//IL_01e5: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ef: Expected O, but got Unknown
		//IL_01fb: Unknown result type (might be due to invalid IL or missing references)
		//IL_0205: Expected O, but got Unknown
		//IL_0211: Unknown result type (might be due to invalid IL or missing references)
		//IL_021b: Expected O, but got Unknown
		//IL_0227: Unknown result type (might be due to invalid IL or missing references)
		//IL_0231: Expected O, but got Unknown
		//IL_023d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0247: Expected O, but got Unknown
		//IL_0253: Unknown result type (might be due to invalid IL or missing references)
		//IL_025d: Expected O, but got Unknown
		//IL_0269: Unknown result type (might be due to invalid IL or missing references)
		//IL_0273: Expected O, but got Unknown
		//IL_0295: Unknown result type (might be due to invalid IL or missing references)
		//IL_029f: Expected O, but got Unknown
		//IL_02ab: Unknown result type (might be due to invalid IL or missing references)
		//IL_02b5: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/PostVideo/PostVideoPage.xaml", UriKind.Relative));
			MyCommentPanel = (Grid)((FrameworkElement)this).FindName("MyCommentPanel");
			Header = (Grid)((FrameworkElement)this).FindName("Header");
			RectBackground = (Rectangle)((FrameworkElement)this).FindName("RectBackground");
			PostVideoNormalPanel = (Grid)((FrameworkElement)this).FindName("PostVideoNormalPanel");
			PrincScroll = (ScrollViewer)((FrameworkElement)this).FindName("PrincScroll");
			StackScroll = (StackPanel)((FrameworkElement)this).FindName("StackScroll");
			MyComment = (TextBox)((FrameworkElement)this).FindName("MyComment");
			Preview = (Image)((FrameworkElement)this).FindName("Preview");
			NbrCaractText = (TextBlock)((FrameworkElement)this).FindName("NbrCaractText");
			TwitterToggle = (CheckBox)((FrameworkElement)this).FindName("TwitterToggle");
			FacebookToggle = (CheckBox)((FrameworkElement)this).FindName("FacebookToggle");
			PostVideoDirectPanel = (Grid)((FrameworkElement)this).FindName("PostVideoDirectPanel");
			PrincScroll2 = (ScrollViewer)((FrameworkElement)this).FindName("PrincScroll2");
			StackScroll2 = (StackPanel)((FrameworkElement)this).FindName("StackScroll2");
			MyComment2 = (TextBox)((FrameworkElement)this).FindName("MyComment2");
			Preview2 = (Image)((FrameworkElement)this).FindName("Preview2");
			SelectDirectFriends = (Button)((FrameworkElement)this).FindName("SelectDirectFriends");
			FriendsLongListSelector = (ItemsControl)((FrameworkElement)this).FindName("FriendsLongListSelector");
			ShadowRectangle = (Rectangle)((FrameworkElement)this).FindName("ShadowRectangle");
			ProgressInfoPanel = (StackPanel)((FrameworkElement)this).FindName("ProgressInfoPanel");
			RetryButton = (Button)((FrameworkElement)this).FindName("RetryButton");
			InfoText = (TextBlock)((FrameworkElement)this).FindName("InfoText");
			SuggestionsPanel = (Grid)((FrameworkElement)this).FindName("SuggestionsPanel");
			SuggestionsLine = (Grid)((FrameworkElement)this).FindName("SuggestionsLine");
			SuggestionsList = (ListBox)((FrameworkElement)this).FindName("SuggestionsList");
			SuggestionsCommands = (Grid)((FrameworkElement)this).FindName("SuggestionsCommands");
			ModernAppBarContainer = (Grid)((FrameworkElement)this).FindName("ModernAppBarContainer");
			ModernAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernAppBar");
			ProgressInfo = (ProgressBar)((FrameworkElement)this).FindName("ProgressInfo");
			LoadingPanel = (Grid)((FrameworkElement)this).FindName("LoadingPanel");
		}
	}

	[CompilerGenerated]
	private void _003C_003En__FabricatedMethod0(NavigationEventArgs e)
	{
		((Page)this).OnNavigatedTo(e);
	}
}
