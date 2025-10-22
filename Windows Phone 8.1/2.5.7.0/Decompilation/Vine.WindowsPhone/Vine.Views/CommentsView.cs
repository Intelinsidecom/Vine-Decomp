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
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Web;
using Windows.Foundation;
using Windows.UI.ViewManagement;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class CommentsView : BasePage, IIncrementalSource<CommentModel>, IComponentConnector
{
	private readonly ScrollViewer _scrollViewer;

	private double _keyboardHeight;

	private bool _isBusy;

	private bool _isBusyPosting;

	private bool _hasError;

	private string _errorText = ResourceHelper.GetString("failed_to_load_comments");

	private bool _showRetry = true;

	private bool _isEmpty;

	private string _textInput = string.Empty;

	private bool _isTagging;

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
	private ListView ListView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBlock TextBoxPlaceholder;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TextBox TextInputBox;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public double KeyboardHeight
	{
		get
		{
			return _keyboardHeight;
		}
		set
		{
			SetProperty(ref _keyboardHeight, value, "KeyboardHeight");
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

	public bool IsBusyPosting
	{
		get
		{
			return _isBusyPosting;
		}
		set
		{
			SetProperty(ref _isBusyPosting, value, "IsBusyPosting");
		}
	}

	public bool HasError
	{
		get
		{
			return _hasError;
		}
		set
		{
			SetProperty(ref _hasError, value, "HasError");
		}
	}

	public string ErrorText
	{
		get
		{
			return _errorText;
		}
		set
		{
			SetProperty(ref _errorText, value, "ErrorText");
		}
	}

	public bool ShowRetry
	{
		get
		{
			return _showRetry;
		}
		set
		{
			SetProperty(ref _showRetry, value, "ShowRetry");
		}
	}

	public bool IsEmpty
	{
		get
		{
			return _isEmpty;
		}
		set
		{
			SetProperty(ref _isEmpty, value, "IsEmpty");
		}
	}

	public bool SendEnabled => !string.IsNullOrEmpty(TextInput);

	public bool IsFocusedByDefault { get; set; }

	public string TextInput
	{
		get
		{
			return _textInput;
		}
		set
		{
			//IL_00bd: Unknown result type (might be due to invalid IL or missing references)
			//IL_00de: Unknown result type (might be due to invalid IL or missing references)
			//IL_00e4: Invalid comparison between Unknown and I4
			if (!(_textInput == value))
			{
				bool isDeleting = value != null && _textInput != null && value.Length < _textInput.Length;
				if (!TextInputChanged(isDeleting))
				{
					SetProperty(ref _textInput, value, "TextInput");
				}
				NotifyOfPropertyChange(() => CharsLeft);
				NotifyOfPropertyChange(() => SendEnabled);
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

	public double ScrollOffset => _scrollViewer.VerticalOffset;

	public bool IsFinishedLoading { get; set; }

	public IncrementalLoadingCollection<CommentModel> Items { get; set; }

	public string PostId { get; set; }

	public Section Section { get; set; }

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

	public CommentsView()
	{
		//IL_0024: Unknown result type (might be due to invalid IL or missing references)
		//IL_0095: Unknown result type (might be due to invalid IL or missing references)
		//IL_009f: Expected O, but got Unknown
		InitializeComponent();
		Items = new IncrementalLoadingCollection<CommentModel>(this);
		AutoCompleteList = new ObservableCollection<Entity>();
		_scrollViewer = ((FrameworkElement)(object)ListView).GetFirstLogicalChildByType<ScrollViewer>(applyTemplates: true);
		WindowsRuntimeMarshal.AddEventHandler((Func<KeyEventHandler, EventRegistrationToken>)((UIElement)this).add_KeyUp, (Action<EventRegistrationToken>)((UIElement)this).remove_KeyUp, new KeyEventHandler(OnKeyUp));
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.None, "comments"));
		if (IsFinishedLoading)
		{
			return;
		}
		CommentNavigationObject commentNavigationObject = (CommentNavigationObject)NavigationObject;
		IsFocusedByDefault = commentNavigationObject.IsFocused;
		PostId = commentNavigationObject.PostId;
		Section = commentNavigationObject.Section;
		if (e.PageState != null)
		{
			((UIElement)ListView).put_Opacity(0.0);
			IsFocusedByDefault = e.LoadValueOrDefault<bool>("IsReadOnly");
			Items.CurrentPage = (int)e.LoadValueOrDefault<long>("CurrentPage");
			Items.ResetItems(e.LoadValueOrDefault<List<CommentModel>>("Items"));
			double offset = e.LoadValueOrDefault<double>("ScrollOffset");
			await _scrollViewer.ScrollToVerticalOffsetSpin(offset);
			((UIElement)ListView).put_Opacity(1.0);
			IsFinishedLoading = true;
			return;
		}
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)((FrameworkElement)this).add_Loaded, (Action<EventRegistrationToken>)((FrameworkElement)this).remove_Loaded, new RoutedEventHandler(FocusKeyboard));
		InputPane forCurrentView = InputPane.GetForCurrentView();
		WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<InputPane, InputPaneVisibilityEventArgs>, EventRegistrationToken>)forCurrentView.add_Showing, (Action<EventRegistrationToken>)forCurrentView.remove_Showing, (TypedEventHandler<InputPane, InputPaneVisibilityEventArgs>)InputPaneOnShowing);
		HasError = false;
		IsEmpty = false;
		PagedItemsResult<CommentModel> pagedItemsResult = await Items.Refresh();
		HasError = pagedItemsResult.ApiResult.HasError;
		IsEmpty = !HasError && !pagedItemsResult.ViewModels.Any();
		if (HasError && pagedItemsResult.ApiResult.HasConnectivityError)
		{
			ErrorText = ResourceHelper.GetString("NoResponseFromServerError");
			ShowRetry = false;
		}
		else if (HasError)
		{
			ErrorText = ResourceHelper.GetString("failed_to_load_comments");
			ShowRetry = true;
		}
		if (!HasError && pagedItemsResult.ViewModels.Any())
		{
			Items.ResetItems(pagedItemsResult.ViewModels);
		}
		IsFinishedLoading = !HasError;
	}

	private void InputPaneOnShowing(InputPane s, InputPaneVisibilityEventArgs args)
	{
		InputPane forCurrentView = InputPane.GetForCurrentView();
		KeyboardHeight = forCurrentView.OccludedRect.Height;
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		WindowsRuntimeMarshal.RemoveEventHandler<TypedEventHandler<InputPane, InputPaneVisibilityEventArgs>>((Action<EventRegistrationToken>)InputPane.GetForCurrentView().remove_Showing, (TypedEventHandler<InputPane, InputPaneVisibilityEventArgs>)InputPaneOnShowing);
		Items.Apply(delegate(CommentModel x)
		{
			x.ClearRichBody();
		});
		e.PageState["CurrentPage"] = (long)Items.CurrentPage;
		e.PageState["Items"] = Items.ToList();
		e.PageState["ScrollOffset"] = ScrollOffset;
		e.PageState["IsReadOnly"] = IsFocusedByDefault;
	}

	public async Task<PagedItemsResult<CommentModel>> GetPagedItems(int page, int count, string anchor)
	{
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = true;
		});
		ApiResult<BaseVineResponseModel<CommentMetaModel>> apiResult = await App.Api.GetPostComments(PostId, page, count);
		DispatcherEx.BeginInvoke(delegate
		{
			IsBusy = false;
		});
		if (apiResult.HasError || !apiResult.Model.Data.Records.Any())
		{
			DispatcherEx.BeginInvoke(delegate
			{
				((Control)ListView).put_Padding(new Thickness(0.0, 0.0, 0.0, 24.0));
			});
		}
		if (!apiResult.HasError)
		{
			foreach (CommentModel record in apiResult.Model.Data.Records)
			{
				record.AddUserToStart();
			}
			return new PagedItemsResult<CommentModel>
			{
				ApiResult = apiResult,
				ViewModels = apiResult.Model.Data.Records.OrderByDescending((CommentModel x) => x.Created).ToList()
			};
		}
		return new PagedItemsResult<CommentModel>
		{
			ApiResult = apiResult
		};
	}

	private async Task PostNewComment()
	{
		((Control)TextInputBox).put_IsTabStop(false);
		((UIElement)TextInputBox).put_Opacity(0.7);
		IsBusyPosting = true;
		string msg = TextInput;
		List<Entity> entities = new List<Entity>();
		if (_tappedAutoCompleteList.Any())
		{
			msg = msg.Replace("\u200b", string.Empty);
			_tappedAutoCompleteList.Apply(delegate(Entity x)
			{
				x.Text = x.Text.Replace("\u200b", string.Empty);
			});
			entities = EntityHelper.BuildFromList(msg, _tappedAutoCompleteList);
			_tappedAutoCompleteList.Apply(delegate(Entity x)
			{
				x.Text = "\u200b" + x.Text + "\u200b";
			});
		}
		ApiResult<BaseVineResponseModel<CommentModel>> apiResult = await App.Api.PostComment(PostId, msg, entities);
		if (!apiResult.HasError)
		{
			App.ScribeService.Log(new CommentEvent(apiResult.Model.Data.CommentId, ApplicationSettings.Current.UserId, Section, "comments"));
			entities.Apply(delegate(Entity x)
			{
				x.Title = x.Text.Replace("#", "");
			});
			CommentModel commentModel = new CommentModel
			{
				CommentId = apiResult.Model.Data.CommentId,
				Comment = msg,
				Created = DateTime.UtcNow,
				PostId = PostId,
				Entities = entities,
				User = ApplicationSettings.Current.User
			};
			commentModel.AddUserToStart();
			Items.Insert(0, commentModel);
			_ignoreTextChange = true;
			TextInput = string.Empty;
			_ignoreTextChange = false;
			_tappedAutoCompleteList.Clear();
			IsEmpty = false;
			HasError = false;
		}
		apiResult.PopUpErrorIfExists();
		IsBusyPosting = false;
		((Control)TextInputBox).put_IsTabStop(true);
		((UIElement)TextInputBox).put_Opacity(1.0);
	}

	private void FocusKeyboard(object sender, RoutedEventArgs e)
	{
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		//IL_001e: Expected O, but got Unknown
		WindowsRuntimeMarshal.RemoveEventHandler<RoutedEventHandler>((Action<EventRegistrationToken>)((FrameworkElement)this).remove_Loaded, new RoutedEventHandler(FocusKeyboard));
		if (!IsFocusedByDefault)
		{
			((Control)TextInputBox).Focus((FocusState)2);
		}
	}

	private void OnKeyUp(object sender, KeyRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0008: Invalid comparison between Unknown and I4
		if ((int)e.Key == 13)
		{
			((Control)HiddenButton).Focus((FocusState)3);
			PostNewComment();
		}
	}

	private void TextBox_OnGotFocus(object sender, RoutedEventArgs e)
	{
		IsCommenting = true;
		TagBarVisibility = (Visibility)0;
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

	private void User_Tapped(object sender, TappedRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		CommentModel commentModel = (CommentModel)((FrameworkElement)sender).DataContext;
		((Control)TextInputBox).put_IsEnabled(false);
		App.RootFrame.Navigate(typeof(ProfileView), (object)commentModel.User.UserId);
	}

	private void Delete_OnClick(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		CommentModel comment = (CommentModel)((FrameworkElement)sender).DataContext;
		DeleteComment(comment);
	}

	private async Task DeleteComment(CommentModel comment)
	{
		if (Items.Contains(comment))
		{
			int i = Items.IndexOf(comment);
			Items.RemoveAt(i);
			ApiResult<BaseVineResponseModel> apiResult = await App.Api.DeleteComment(comment.PostId, comment.CommentId);
			if (apiResult.HasError)
			{
				apiResult.PopUpErrorIfExists();
				Items.Insert(i, comment);
			}
		}
	}

	private void Refresh_Click(object sender, RoutedEventArgs e)
	{
		LoadState();
	}

	private async void Grid_Holding(object sender, HoldingRoutedEventArgs e)
	{
		if ((int)e.HoldingState == 0)
		{
			FrameworkElement senderElement = (FrameworkElement)sender;
			if (((CommentModel)senderElement.DataContext).IsUserComment)
			{
				((Control)HiddenButton).Focus((FocusState)3);
				await Task.Delay(250);
				FlyoutBase.GetAttachedFlyout(senderElement).ShowAt(senderElement);
			}
		}
	}

	private void BtnSend_Click(object sender, RoutedEventArgs e)
	{
		((Control)HiddenButton).Focus((FocusState)3);
		PostNewComment();
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/CommentsView.xaml"), (ComponentResourceLocation)0);
			HiddenButton = (Button)((FrameworkElement)this).FindName("HiddenButton");
			ListView = (ListView)((FrameworkElement)this).FindName("ListView");
			TextBoxPlaceholder = (TextBlock)((FrameworkElement)this).FindName("TextBoxPlaceholder");
			TextInputBox = (TextBox)((FrameworkElement)this).FindName("TextInputBox");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_003a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0040: Expected O, but got Unknown
		//IL_0061: Unknown result type (might be due to invalid IL or missing references)
		//IL_006b: Expected O, but got Unknown
		//IL_0071: Unknown result type (might be due to invalid IL or missing references)
		//IL_0077: Expected O, but got Unknown
		//IL_0098: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a2: Expected O, but got Unknown
		//IL_00a8: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ae: Expected O, but got Unknown
		//IL_00cf: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d9: Expected O, but got Unknown
		//IL_00df: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e5: Expected O, but got Unknown
		//IL_0106: Unknown result type (might be due to invalid IL or missing references)
		//IL_0110: Expected O, but got Unknown
		//IL_0116: Unknown result type (might be due to invalid IL or missing references)
		//IL_011c: Expected O, but got Unknown
		//IL_013d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0147: Expected O, but got Unknown
		//IL_014d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0153: Expected O, but got Unknown
		//IL_0174: Unknown result type (might be due to invalid IL or missing references)
		//IL_017e: Expected O, but got Unknown
		//IL_0184: Unknown result type (might be due to invalid IL or missing references)
		//IL_018a: Expected O, but got Unknown
		//IL_01ab: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b5: Expected O, but got Unknown
		//IL_01b6: Unknown result type (might be due to invalid IL or missing references)
		//IL_01bc: Expected O, but got Unknown
		//IL_01dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_01e7: Expected O, but got Unknown
		//IL_01e8: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ee: Expected O, but got Unknown
		//IL_020f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0219: Expected O, but got Unknown
		//IL_021f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0225: Expected O, but got Unknown
		//IL_0246: Unknown result type (might be due to invalid IL or missing references)
		//IL_0250: Expected O, but got Unknown
		//IL_0256: Unknown result type (might be due to invalid IL or missing references)
		//IL_025c: Expected O, but got Unknown
		//IL_027d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0287: Expected O, but got Unknown
		//IL_028a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0290: Expected O, but got Unknown
		//IL_02b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_02bb: Expected O, but got Unknown
		//IL_02be: Unknown result type (might be due to invalid IL or missing references)
		//IL_02c4: Expected O, but got Unknown
		//IL_02e5: Unknown result type (might be due to invalid IL or missing references)
		//IL_02ef: Expected O, but got Unknown
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
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_GotFocus, (Action<EventRegistrationToken>)val.remove_GotFocus, new RoutedEventHandler(IgnoreFocus));
			break;
		}
		case 4:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(BtnSend_Click));
			break;
		}
		case 5:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(Mention_Click));
			break;
		}
		case 6:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(Hashtag_Click));
			break;
		}
		case 7:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_GotFocus, (Action<EventRegistrationToken>)val.remove_GotFocus, new RoutedEventHandler(TextBox_OnGotFocus));
			val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_LostFocus, (Action<EventRegistrationToken>)val.remove_LostFocus, new RoutedEventHandler(TextBox_OnLostFocus));
			TextBox val4 = (TextBox)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val4.add_SelectionChanged, (Action<EventRegistrationToken>)val4.remove_SelectionChanged, new RoutedEventHandler(TextBox_OnSelectionChanged));
			break;
		}
		case 8:
		{
			ButtonBase val3 = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val3.add_Click, (Action<EventRegistrationToken>)val3.remove_Click, new RoutedEventHandler(Refresh_Click));
			break;
		}
		case 9:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<HoldingEventHandler, EventRegistrationToken>)val.add_Holding, (Action<EventRegistrationToken>)val.remove_Holding, new HoldingEventHandler(Grid_Holding));
			break;
		}
		case 10:
		{
			MenuFlyoutItem val2 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(Delete_OnClick));
			break;
		}
		case 11:
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(User_Tapped));
			break;
		}
		}
		_contentLoaded = true;
	}
}
