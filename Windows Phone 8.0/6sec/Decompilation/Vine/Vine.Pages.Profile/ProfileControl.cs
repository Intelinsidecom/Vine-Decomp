using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Shapes;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Messaging;
using Gen.Services;
using Microsoft.Phone.Tasks;
using Microsoft.Xna.Framework.Input.Touch;
using Vine.Datas;
using Vine.Models;
using Vine.Pages.Main.ViewModels;
using Vine.Pages.Profile.ViewModels;
using Vine.Resources;
using Vine.Services;
using Vine.Services.Models;
using Vine.Utils;
using Windows.System;

namespace Vine.Pages.Profile;

public class ProfileControl : UserControl
{
	private static Regex regUri;

	public static readonly DependencyProperty DescriptionProperty;

	private bool _ismyprofile;

	private static ICommand UriCommand;

	internal ScrollViewer Scroll;

	internal Grid LayoutRoot;

	internal ToggleButton FollowButton;

	internal ProgressBar FollowLoading;

	internal TextBlock WaitingAuthoriation;

	internal Image Avatar;

	internal TextBlock FollowYouTextBlock;

	internal RichTextBox DescriptionTextBlock;

	internal TextBlock FollowerCount;

	internal TextBlock FollowingCount;

	internal TextBlock PostCount;

	internal TextBlock PhotoOfYouCount;

	internal TextBlock PhotoOfYouCountLabel;

	internal Grid AnswerFollowingPanel;

	internal Path ClosePath;

	internal Grid ContentPanel1;

	internal ItemsControl ListPosts;

	internal Rectangle ShadowRectangle;

	private bool _contentLoaded;

	public ProfileViewModel ViewModel { get; set; }

	public string Description
	{
		get
		{
			return (string)((DependencyObject)this).GetValue(DescriptionProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(DescriptionProperty, (object)value);
		}
	}

	static ProfileControl()
	{
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		//IL_0041: Expected O, but got Unknown
		regUri = new Regex("(?<=^|\\W)https?://\\S+\\w", RegexOptions.Compiled | RegexOptions.Singleline);
		DescriptionProperty = DependencyProperty.Register("Description", typeof(string), typeof(ProfileControl), new PropertyMetadata((object)null, new PropertyChangedCallback(DescriptionCallback)));
		UriCommand = new RelayCommand<string>(delegate(string s)
		{
			Launcher.LaunchUriAsync(new Uri(s));
		});
	}

	public ProfileControl()
	{
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0035: Expected O, but got Unknown
		//IL_0098: Unknown result type (might be due to invalid IL or missing references)
		//IL_009e: Expected O, but got Unknown
		//IL_007d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0087: Expected O, but got Unknown
		ProfileViewModel dataContext = (ViewModel = new ProfileViewModel());
		((FrameworkElement)this).DataContext = dataContext;
		TouchPanel.EnabledGestures = GestureType.FreeDrag | GestureType.DragComplete;
		InitializeComponent();
		Binding val = new Binding("Profile.LikeCount");
		((FrameworkElement)PhotoOfYouCount).SetBinding(TextBlock.TextProperty, val);
		PhotoOfYouCountLabel.Text = AppResources.MyLikes;
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Control)Scroll).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
			((UIElement)ShadowRectangle).Visibility = (Visibility)0;
		}
		Binding val2 = new Binding("Profile.Description");
		((FrameworkElement)this).SetBinding(DescriptionProperty, val2);
	}

	public void Init(string id, Action callback = null)
	{
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (instance.CurrentUser != null && instance.CurrentUser.User != null && id == instance.CurrentUser.User.Id)
		{
			((UIElement)FollowButton).Visibility = (Visibility)1;
			_ismyprofile = true;
		}
		else
		{
			((UIElement)FollowButton).Visibility = (Visibility)0;
		}
		((ProfileViewModel)((FrameworkElement)this).DataContext).LoadProfile(id, delegate
		{
			if (callback != null)
			{
				callback();
			}
		});
	}

	private static void DescriptionCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		//IL_0021: Unknown result type (might be due to invalid IL or missing references)
		//IL_0027: Expected O, but got Unknown
		//IL_010d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0112: Unknown result type (might be due to invalid IL or missing references)
		//IL_0119: Unknown result type (might be due to invalid IL or missing references)
		//IL_0126: Unknown result type (might be due to invalid IL or missing references)
		//IL_0133: Unknown result type (might be due to invalid IL or missing references)
		//IL_013a: Unknown result type (might be due to invalid IL or missing references)
		//IL_013b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0145: Unknown result type (might be due to invalid IL or missing references)
		//IL_015a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0164: Expected O, but got Unknown
		//IL_0164: Unknown result type (might be due to invalid IL or missing references)
		//IL_0179: Unknown result type (might be due to invalid IL or missing references)
		//IL_0183: Expected O, but got Unknown
		//IL_0185: Expected O, but got Unknown
		//IL_00eb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f0: Unknown result type (might be due to invalid IL or missing references)
		//IL_010d: Expected O, but got Unknown
		//IL_01e5: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ea: Unknown result type (might be due to invalid IL or missing references)
		//IL_01fd: Expected O, but got Unknown
		RichTextBox descriptionTextBlock = ((ProfileControl)(object)d).DescriptionTextBlock;
		if (((DependencyPropertyChangedEventArgs)(ref e)).NewValue == null)
		{
			((PresentationFrameworkCollection<Block>)(object)descriptionTextBlock.Blocks).Clear();
			return;
		}
		Paragraph val = new Paragraph();
		List<Tuple<ICommand, int, string, string>> list = new List<Tuple<ICommand, int, string, string>>();
		string text = (string)((DependencyPropertyChangedEventArgs)(ref e)).NewValue;
		int num = 0;
		foreach (Match item in regUri.Matches(text))
		{
			list.Add(new Tuple<ICommand, int, string, string>(UriCommand, item.Index, item.Value, item.Value));
		}
		foreach (Tuple<ICommand, int, string, string> item2 in list.OrderBy((Tuple<ICommand, int, string, string> l) => l.Item2))
		{
			if (item2.Item2 > num)
			{
				((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)new Run
				{
					Text = text.Substring(num, item2.Item2 - num)
				});
			}
			Hyperlink val2 = new Hyperlink
			{
				MouseOverTextDecorations = null,
				Command = item2.Item1,
				CommandParameter = item2.Item4,
				TextDecorations = null,
				FontWeight = FontWeights.SemiBold,
				Foreground = (Brush)Application.Current.Resources[(object)"DarkGreyBrush"],
				MouseOverForeground = (Brush)Application.Current.Resources[(object)"DarkGreyBrush"]
			};
			((Span)val2).Inlines.Add(item2.Item3);
			((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)(object)val2);
			num = item2.Item2 + item2.Item3.Length;
		}
		if (num < text.Length)
		{
			((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)new Run
			{
				Text = text.Substring(num)
			});
		}
		((PresentationFrameworkCollection<Block>)(object)descriptionTextBlock.Blocks).Clear();
		((PresentationFrameworkCollection<Block>)(object)descriptionTextBlock.Blocks).Add((Block)(object)val);
	}

	private void Post_Click(object sender, RoutedEventArgs e)
	{
		IProfile profile = ((ProfileViewModel)((FrameworkElement)this).DataContext).Profile;
		if (profile != null)
		{
			NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForUser(profile.Id));
		}
	}

	private async void Follow_Click(object sender, RoutedEventArgs e)
	{
		ToggleButton button = (ToggleButton)sender;
		IProfile profile = ((ProfileViewModel)((FrameworkElement)this).DataContext).Profile;
		if (profile == null)
		{
			return;
		}
		bool val = button.IsChecked.Value;
		button.IsChecked = !val;
		if (val)
		{
			((Control)FollowLoading).Foreground = (Brush)new SolidColorBrush(Colors.White);
		}
		else
		{
			((Control)FollowLoading).Foreground = (Brush)Application.Current.Resources[(object)"PrincBrush"];
		}
		((Control)button).IsEnabled = false;
		((UIElement)FollowLoading).Visibility = (Visibility)0;
		FollowLoading.IsIndeterminate = true;
		string id = profile.Id;
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		try
		{
			bool num = await currentUser.Service.FollowUserAsync(id, val);
			Messenger.Default.Send(new FollowMessage(id, val));
			if (num)
			{
				button.IsChecked = val;
				if (val)
				{
					((UIElement)WaitingAuthoriation).Visibility = (Visibility)(!profile.Private);
				}
				else
				{
					((UIElement)WaitingAuthoriation).Visibility = (Visibility)1;
				}
			}
		}
		catch (ServiceServerErrorException ex)
		{
			if (ex.ReasonError == ServiceServerErrorType.CHECKPOINT)
			{
				ServiceUtils.ManageCheckPoint(ex.Checkpoint);
			}
			else
			{
				ToastHelper.Show(AppResources.ToastCantFollowThisPerson, afternav: false, (Orientation)0);
			}
		}
		finally
		{
			((Control)button).IsEnabled = true;
			((UIElement)FollowLoading).Visibility = (Visibility)1;
		}
	}

	private void Followers_Click(object sender, RoutedEventArgs e)
	{
		IProfile profile = ((ProfileViewModel)((FrameworkElement)this).DataContext).Profile;
		if (profile != null)
		{
			ProfileViewModel profileViewModel = (ProfileViewModel)((FrameworkElement)this).DataContext;
			int? number = null;
			try
			{
				number = profileViewModel.Profile.FollowerCount;
			}
			catch
			{
			}
			NavigationServiceExt.Followers(profile.Id, number);
		}
	}

	private void Following_Click(object sender, RoutedEventArgs e)
	{
		IProfile profile = ((ProfileViewModel)((FrameworkElement)this).DataContext).Profile;
		if (profile != null)
		{
			ProfileViewModel profileViewModel = (ProfileViewModel)((FrameworkElement)this).DataContext;
			int? number = null;
			try
			{
				number = profileViewModel.Profile.FollowingCount;
			}
			catch
			{
			}
			NavigationServiceExt.ToFollowing(profile.Id, number);
		}
	}

	private void VineUser_Click(object sender, RoutedEventArgs e)
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0016: Unknown result type (might be due to invalid IL or missing references)
		IProfile profile = ((ProfileViewModel)((FrameworkElement)this).DataContext).Profile;
		((ShareTaskBase)new ShareStatusTask
		{
			Status = string.Format("Follow {0} on Vine: {1}", profile.Name, "vine://user/" + profile.Id)
		}).Show();
	}

	private void AutoTextBlock_SizeChanged(object sender, SizeChangedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Expected O, but got Unknown
		//IL_0013: Unknown result type (might be due to invalid IL or missing references)
		TextBlock val = (TextBlock)sender;
		double fontSize;
		if (((FrameworkElement)val).ActualWidth > ((FrameworkElement)((FrameworkElement)val).Parent).ActualWidth - 6.0)
		{
			fontSize = val.FontSize - 1.0;
			val.FontSize = fontSize;
		}
		TextBlock followingCount = FollowingCount;
		TextBlock postCount = PostCount;
		double num = (PhotoOfYouCount.FontSize = val.FontSize);
		fontSize = (postCount.FontSize = num);
		followingCount.FontSize = fontSize;
	}

	protected void Post_SelectedTag(object sender, string tag)
	{
		NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForTag(tag));
	}

	private void AnswerFollowYes_Click(object sender, RoutedEventArgs e)
	{
		Answer(accept: true);
	}

	private void AnswerFollowNo_Click(object sender, RoutedEventArgs e)
	{
		Answer(accept: false);
	}

	private async Task Answer(bool accept)
	{
		ProfileViewModel vm = (ProfileViewModel)((FrameworkElement)this).DataContext;
		vm.IsLoading = true;
		vm.RaisePropertyChanged("IsLoading");
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		try
		{
			await currentUser.Service.AnswerFollowingRequestAsync(vm.Profile.Id, accept);
			vm.IsLoading = false;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				((UIElement)AnswerFollowingPanel).Visibility = (Visibility)1;
				vm.RaisePropertyChanged("IsLoading");
			});
		}
		catch (ServiceServerErrorException ex)
		{
			ServiceServerErrorException ex2 = ex;
			ServiceServerErrorException ex3 = ex2;
			vm.IsLoading = false;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				if (ex3.ReasonError == ServiceServerErrorType.CHECKPOINT)
				{
					ServiceUtils.ManageCheckPoint(ex3.Checkpoint);
				}
				else if (ex3.HttpErrorMessage != null)
				{
					ToastHelper.Show(ex3.HttpErrorMessage ?? "an error has occurred", afternav: false, (Orientation)0);
				}
			});
		}
	}

	protected void Thumb_Tap(object sender, RoutedEventArgs e)
	{
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		ProfileViewModel profileViewModel = (ProfileViewModel)((FrameworkElement)this).DataContext;
		IPostRecord postRecord = (IPostRecord)((FrameworkElement)sender).DataContext;
		MainPage.ForcedPosts = profileViewModel.AllPosts.Where((IPostRecord p) => !(p is MorePost)).ToList();
		if (postRecord is MorePost)
		{
			NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForUser(profileViewModel.UserId), removebackentry: false, profileViewModel.NextPage, "&displaygrid=last");
		}
		else
		{
			NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForUser(profileViewModel.UserId), removebackentry: false, profileViewModel.NextPage, "&focuson=" + postRecord.PostId);
		}
	}

	public void OpenMap()
	{
	}

	private void Website_Tap(object sender, GestureEventArgs e)
	{
	}

	private void Tagged_Click(object sender, RoutedEventArgs e)
	{
		IProfile profile = ((ProfileViewModel)((FrameworkElement)this).DataContext).Profile;
		if (profile != null)
		{
			NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForLikes(profile.Id));
		}
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Profile/ProfileControl.xaml", UriKind.Relative));
			Scroll = (ScrollViewer)((FrameworkElement)this).FindName("Scroll");
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			FollowButton = (ToggleButton)((FrameworkElement)this).FindName("FollowButton");
			FollowLoading = (ProgressBar)((FrameworkElement)this).FindName("FollowLoading");
			WaitingAuthoriation = (TextBlock)((FrameworkElement)this).FindName("WaitingAuthoriation");
			Avatar = (Image)((FrameworkElement)this).FindName("Avatar");
			FollowYouTextBlock = (TextBlock)((FrameworkElement)this).FindName("FollowYouTextBlock");
			DescriptionTextBlock = (RichTextBox)((FrameworkElement)this).FindName("DescriptionTextBlock");
			FollowerCount = (TextBlock)((FrameworkElement)this).FindName("FollowerCount");
			FollowingCount = (TextBlock)((FrameworkElement)this).FindName("FollowingCount");
			PostCount = (TextBlock)((FrameworkElement)this).FindName("PostCount");
			PhotoOfYouCount = (TextBlock)((FrameworkElement)this).FindName("PhotoOfYouCount");
			PhotoOfYouCountLabel = (TextBlock)((FrameworkElement)this).FindName("PhotoOfYouCountLabel");
			AnswerFollowingPanel = (Grid)((FrameworkElement)this).FindName("AnswerFollowingPanel");
			ClosePath = (Path)((FrameworkElement)this).FindName("ClosePath");
			ContentPanel1 = (Grid)((FrameworkElement)this).FindName("ContentPanel1");
			ListPosts = (ItemsControl)((FrameworkElement)this).FindName("ListPosts");
			ShadowRectangle = (Rectangle)((FrameworkElement)this).FindName("ShadowRectangle");
		}
	}
}
