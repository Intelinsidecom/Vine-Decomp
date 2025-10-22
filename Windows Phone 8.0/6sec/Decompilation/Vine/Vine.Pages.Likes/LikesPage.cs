using System;
using System.Collections;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Documents;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Gen.Services;
using Microsoft.Phone.Controls;
using Vine.Datas;
using Vine.Pages.Likes.ViewModels;
using Vine.Resources;
using Vine.Services;
using Vine.Utils;

namespace Vine.Pages.Likes;

public class LikesPage : PhoneApplicationPage
{
	private string _subtype;

	private bool _isInit;

	internal Grid LayoutRoot;

	internal Rectangle RectBackground;

	internal Run PageTitle;

	internal Run NumberTitle;

	internal Grid ContentPanel;

	internal LongListSelector PersonsList;

	internal Rectangle ShadowRectangle;

	internal Grid NextPanel;

	private bool _contentLoaded;

	public static IListPersons TmpLikes { get; set; }

	public LikesPage()
	{
		//IL_004b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0055: Expected O, but got Unknown
		LikesViewModel dataContext = new LikesViewModel();
		((FrameworkElement)this).DataContext = dataContext;
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((UIElement)ShadowRectangle).Visibility = (Visibility)0;
			((Shape)RectBackground).Fill = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		((Page)this).OnNavigatedTo(e);
		if (_isInit && (int)e.NavigationMode != 0)
		{
			return;
		}
		_isInit = true;
		LikesViewModel likesViewModel = (LikesViewModel)((FrameworkElement)this).DataContext;
		if (TmpLikes != null)
		{
			likesViewModel.AddLikes(TmpLikes.Persons);
			TmpLikes = null;
		}
		if (((Page)this).NavigationContext.QueryString.ContainsKey("number"))
		{
			NumberTitle.Text = "(" + ((Page)this).NavigationContext.QueryString["number"] + ")";
		}
		switch (((Page)this).NavigationContext.QueryString["type"])
		{
		case "searchcontacts":
			PageTitle.Text = AppResources.PhoneContacts.ToUpper();
			likesViewModel.SearchContactFromPhone();
			break;
		case "searchtwitter":
			PageTitle.Text = AppResources.TwitterContacts.ToUpper();
			likesViewModel.SearchContactFromTwitter();
			break;
		case "followers":
		{
			PageTitle.Text = AppResources.Followers.ToUpper();
			string id3 = ((Page)this).NavigationContext.QueryString["id"];
			likesViewModel.LoadFollowers(id3);
			break;
		}
		case "following":
		{
			PageTitle.Text = AppResources.Following.ToUpper();
			string id2 = ((Page)this).NavigationContext.QueryString["id"];
			likesViewModel.LoadFollowing(id2);
			break;
		}
		case "facebookcontacts":
			PageTitle.Text = "FACEBOOK";
			likesViewModel.LoadFacebookContacts();
			((Page)this).NavigationContext.QueryString.TryGetValue("subtype", out _subtype);
			if (_subtype == "firstlaunch")
			{
				((UIElement)NextPanel).Visibility = (Visibility)0;
			}
			break;
		default:
		{
			string id = ((Page)this).NavigationContext.QueryString["id"];
			PageTitle.Text = AppResources.Likes.ToUpper();
			likesViewModel.LoadLikes(id);
			break;
		}
		}
		if (((Page)this).NavigationContext.QueryString.ContainsKey("removebackentry"))
		{
			((Page)this).NavigationService.RemoveBackEntry();
		}
	}

	private void Likes_ItemRealized(object sender, ItemRealizationEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		IList itemsSource = ((LongListSelector)sender).ItemsSource;
		if (itemsSource.Count >= 2)
		{
			if (e.Container.Content == itemsSource[itemsSource.Count - 2])
			{
				((LikesViewModel)((FrameworkElement)this).DataContext).LoadMore();
			}
		}
	}

	private void ViewProfile_Click(object sender, RoutedEventArgs e)
	{
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		if (!(_subtype == "firstlaunch"))
		{
			NavigationServiceExt.ToProfile(((IPerson)((FrameworkElement)sender).DataContext).Id);
		}
	}

	private async void Following_Click(object sender, RoutedEventArgs e)
	{
		ToggleButton toggle = (ToggleButton)sender;
		((Control)toggle).IsEnabled = false;
		bool value = toggle.IsChecked.Value;
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		try
		{
			if (!(await currentUser.Service.FollowUserAsync(((IPerson)((FrameworkElement)toggle).DataContext).Id, value)))
			{
				((IPerson)((FrameworkElement)toggle).DataContext).ChangeFollow(!value);
			}
			((Control)toggle).IsEnabled = true;
		}
		catch (ServiceServerErrorException ex)
		{
			((Control)toggle).IsEnabled = true;
			((IPerson)((FrameworkElement)toggle).DataContext).ChangeFollow(!value);
			if (ex.ReasonError == ServiceServerErrorType.CHECKPOINT)
			{
				ServiceUtils.ManageCheckPoint(ex.Checkpoint);
			}
			else
			{
				ToastHelper.Show(AppResources.ToastCantFollowThisPerson, afternav: false, (Orientation)0);
			}
		}
	}

	private void Next_Click(object sender, RoutedEventArgs e)
	{
		((Page)this).NavigationService.RemoveBackEntry();
		NavigationServiceExt.ToFriendsSuggestion("firstlaunch");
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Likes/LikesPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			RectBackground = (Rectangle)((FrameworkElement)this).FindName("RectBackground");
			PageTitle = (Run)((FrameworkElement)this).FindName("PageTitle");
			NumberTitle = (Run)((FrameworkElement)this).FindName("NumberTitle");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			PersonsList = (LongListSelector)((FrameworkElement)this).FindName("PersonsList");
			ShadowRectangle = (Rectangle)((FrameworkElement)this).FindName("ShadowRectangle");
			NextPanel = (Grid)((FrameworkElement)this).FindName("NextPanel");
		}
	}
}
