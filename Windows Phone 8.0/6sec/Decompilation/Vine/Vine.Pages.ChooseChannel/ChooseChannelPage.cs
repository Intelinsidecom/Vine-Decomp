using System;
using System.Diagnostics;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using Microsoft.Phone.Controls;
using Vine.Datas;
using Vine.Pages.ChooseChannel.ViewModels;
using Vine.Services;
using Vine.Services.Models;

namespace Vine.Pages.ChooseChannel;

public class ChooseChannelPage : PhoneApplicationPage
{
	internal Grid LayoutRoot;

	internal Grid ContentPanel;

	internal ItemsControl HighlightList;

	private bool _contentLoaded;

	public ChooseChannelPage()
	{
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0049: Expected O, but got Unknown
		ChooseChannelViewModel dataContext = new ChooseChannelViewModel();
		((FrameworkElement)this).DataContext = dataContext;
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)ContentPanel).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
	}

	private void Channels_Click(object sender, RoutedEventArgs e)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		Channel channel = (Channel)((FrameworkElement)sender).DataContext;
		string encoid = ((Page)this).NavigationContext.QueryString["id"];
		EncodingJob encodingJob = DatasProvider.Instance.Encodings.FirstOrDefault((EncodingJob enc) => enc.Id == encoid);
		if (encodingJob != null)
		{
			encodingJob.SetChannel(channel.Id, channel.Name);
			DatasProvider.Instance.Save();
		}
		((Page)this).NavigationService.GoBack();
	}

	private void NoChannel_Click(object sender, RoutedEventArgs e)
	{
		string encoid = ((Page)this).NavigationContext.QueryString["id"];
		EncodingJob encodingJob = DatasProvider.Instance.Encodings.FirstOrDefault((EncodingJob enc) => enc.Id == encoid);
		if (encodingJob != null)
		{
			encodingJob.SetChannel(0L, null);
			DatasProvider.Instance.Save();
		}
		((Page)this).NavigationService.GoBack();
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/ChooseChannel/ChooseChannelPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			HighlightList = (ItemsControl)((FrameworkElement)this).FindName("HighlightList");
		}
	}
}
