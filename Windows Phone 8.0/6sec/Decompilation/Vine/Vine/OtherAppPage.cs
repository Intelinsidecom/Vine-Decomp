using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Tasks;
using Vine.ViewModels;

namespace Vine;

public class OtherAppPage : PhoneApplicationPage
{
	internal Grid ContentPanel;

	internal Grid IsLoading;

	private bool _contentLoaded;

	public OtherAppPage()
	{
		((FrameworkElement)this).DataContext = ViewModelLocator.OtherAppStatic;
		InitializeComponent();
	}

	private void ListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		//IL_0042: Unknown result type (might be due to invalid IL or missing references)
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		if (e.AddedItems.Count != 0)
		{
			try
			{
				new MarketplaceDetailTask
				{
					ContentType = (MarketplaceContentType)1,
					ContentIdentifier = ((OtherApp)e.AddedItems[0]).Id
				}.Show();
			}
			catch
			{
			}
			((Selector)(ListBox)sender).SelectedItem = null;
		}
	}

	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/OtherApps/OtherAppPage.xaml", UriKind.Relative));
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			IsLoading = (Grid)((FrameworkElement)this).FindName("IsLoading");
		}
	}
}
