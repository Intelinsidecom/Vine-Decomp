using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Gen.Services;
using Microsoft.Phone.Controls;
using Vine.Controls;
using Vine.Datas;
using Vine.Pages.Direct.ViewModels;
using Vine.Services.Models;
using Vine.ViewModels;

namespace Vine.Pages.Direct;

public class DirectPage : PhoneApplicationPage
{
	private string _subtype;

	internal Grid LayoutRoot;

	internal Rectangle RectBackground;

	internal Grid ContentPanel;

	internal Rectangle ShadowRectangle;

	internal Grid ModernAppBarContainer;

	internal ModernAppBar ModernVineAppBar;

	internal Grid LoadingPanel;

	private bool _contentLoaded;

	public DirectPage()
	{
		//IL_0051: Unknown result type (might be due to invalid IL or missing references)
		//IL_005b: Expected O, but got Unknown
		DirectViewModel directStatic = ViewModelLocator.DirectStatic;
		directStatic.ClearCounter();
		((FrameworkElement)this).DataContext = directStatic;
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((UIElement)ShadowRectangle).Visibility = (Visibility)0;
			((Shape)RectBackground).Fill = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		((Page)this).OnNavigatedTo(e);
		((DirectViewModel)((FrameworkElement)this).DataContext).StartPollUpdate();
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		((Page)this).OnNavigatingFrom(e);
		((DirectViewModel)((FrameworkElement)this).DataContext).StopPollUpdate();
	}

	private void Refresh_Click(object sender, EventArgs e)
	{
		((DirectViewModel)((FrameworkElement)this).DataContext).Reload();
	}

	private void Direct_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_002e: Unknown result type (might be due to invalid IL or missing references)
		if (e.AddedItems.Count != 0 && e.AddedItems[0] != null)
		{
			Conversation chat = (Conversation)e.AddedItems[0];
			((LongListSelector)sender).SelectedItem = null;
			NavigationServiceExt.ToChat(chat);
		}
	}

	private void Delete_Click(object sender, RoutedEventArgs e)
	{
	}

	private void NewConversation_Click(object sender, EventArgs e)
	{
		ViewModelLocator.SelectDirectFriendsStatic.OtherItems.Clear();
		NavigationServiceExt.ToCamera(new List<IPerson>());
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
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Direct/DirectPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			RectBackground = (Rectangle)((FrameworkElement)this).FindName("RectBackground");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			ShadowRectangle = (Rectangle)((FrameworkElement)this).FindName("ShadowRectangle");
			ModernAppBarContainer = (Grid)((FrameworkElement)this).FindName("ModernAppBarContainer");
			ModernVineAppBar = (ModernAppBar)((FrameworkElement)this).FindName("ModernVineAppBar");
			LoadingPanel = (Grid)((FrameworkElement)this).FindName("LoadingPanel");
		}
	}
}
