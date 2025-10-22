using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Windows.Networking.Proximity;

namespace Wikipedia;

public class NFCPage : PhoneApplicationPage
{
	private ProximityDevice device;

	private long _id;

	internal Grid LayoutRoot;

	private bool _contentLoaded;

	public NFCPage()
	{
		InitializeComponent();
		device = ProximityDevice.GetDefault();
	}

	protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
	{
		((Page)this).OnNavigatingFrom(e);
		if (device == null)
		{
			return;
		}
		try
		{
			device.StopPublishingMessage(_id);
		}
		catch
		{
		}
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		((Page)this).OnNavigatedTo(e);
		string uriString = ((Page)this).NavigationContext.QueryString["uri"];
		if (device != null)
		{
			_id = device.PublishUriMessage(new Uri(uriString, UriKind.Absolute));
		}
	}

	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/NFC/NFCPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
		}
	}
}
