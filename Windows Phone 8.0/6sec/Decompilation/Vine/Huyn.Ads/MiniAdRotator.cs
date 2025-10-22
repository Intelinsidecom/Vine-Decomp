using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using GalaSoft.MvvmLight.Messaging;
using Microsoft.Advertising;
using Microsoft.Advertising.Mobile.UI;
using SOMAWP8;
using Vine.Datas;
using Vine.Services;

namespace Huyn.Ads;

public class MiniAdRotator : UserControl
{
	private const string PubCenterApplicationId = "68ebdd48-3bd6-4d46-a087-2982a1a49282";

	private const int SmaatoId = 130020281;

	private string[] AdsOrder = new string[2] { "pubcenter", "smaato" };

	public string[] MiniPubCenterIds = new string[1] { "222959" };

	public static int NextPubCenterIndex;

	public int NextTypeAdIndex;

	public static int LastWorkingPubCenter;

	public static Datas _settings;

	private SomaAdViewer somaAd;

	internal Grid AdPanel;

	private bool _contentLoaded;

	public static void ReInit()
	{
		NextPubCenterIndex = (LastWorkingPubCenter = 0);
	}

	static MiniAdRotator()
	{
		_settings = DatasProvider.Instance;
	}

	public MiniAdRotator()
	{
		//IL_0044: Unknown result type (might be due to invalid IL or missing references)
		//IL_004e: Expected O, but got Unknown
		//IL_0056: Unknown result type (might be due to invalid IL or missing references)
		//IL_0060: Expected O, but got Unknown
		InitializeComponent();
		((FrameworkElement)this).Unloaded += new RoutedEventHandler(AdRotator_Unloaded);
		((FrameworkElement)this).Loaded += new RoutedEventHandler(AdRotator_Loaded);
	}

	private void AdRotator_Loaded(object sender, RoutedEventArgs e)
	{
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
		LastWorkingPubCenter = (NextPubCenterIndex = 0);
		DisplayNextAd(add: false);
	}

	private void AddPubCenter()
	{
		if (MiniPubCenterIds == null || MiniPubCenterIds.Length == 0)
		{
			DisplayNextAd();
			return;
		}
		AdControl adControl = CreatePubcenterAd();
		RemoveAds();
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Add((UIElement)(object)adControl);
	}

	private AdControl CreatePubcenterAd()
	{
		if (NextPubCenterIndex >= MiniPubCenterIds.Length)
		{
			NextPubCenterIndex = 0;
		}
		AdControl adControl = new AdControl();
		((FrameworkElement)adControl).Width = 300.0;
		((FrameworkElement)adControl).Height = 50.0;
		adControl.IsAutoCollapseEnabled = false;
		adControl.ApplicationId = "68ebdd48-3bd6-4d46-a087-2982a1a49282";
		adControl.IsAutoRefreshEnabled = true;
		adControl.IsBackgroundTransparent = true;
		adControl.AdUnitId = MiniPubCenterIds[NextPubCenterIndex];
		_ = DatasProvider.Instance.CurrentUser;
		adControl.ErrorOccurred += AdControl_ErrorOccurred;
		NextPubCenterIndex = (NextPubCenterIndex + 1) % MiniPubCenterIds.Length;
		return adControl;
	}

	private void AdControl_ErrorOccurred(object sender, AdErrorEventArgs e)
	{
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			if (LastWorkingPubCenter != NextPubCenterIndex)
			{
				AddPubCenter();
			}
			else
			{
				DisplayNextAd();
			}
		});
	}

	private void DisplayNextAd(bool add = true)
	{
		if (add)
		{
			NextTypeAdIndex++;
		}
		if (AdsOrder.Length <= NextTypeAdIndex)
		{
			return;
		}
		try
		{
			string text = AdsOrder[NextTypeAdIndex];
			if (text == "pubcenter" || !(text == "smaato"))
			{
				AddPubCenter();
			}
			else
			{
				AddSmaato();
			}
		}
		catch
		{
		}
	}

	private void RemoveAds()
	{
		(from elem in ((IEnumerable<UIElement>)((Panel)AdPanel).Children).Skip(1)
			select (elem)).ToList().ForEach(delegate(UIElement ad)
		{
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Remove(ad);
		});
	}

	private void AddSmaato()
	{
		RemoveAds();
		SomaAdViewer somaAdViewer = new SomaAdViewer();
		((FrameworkElement)somaAdViewer).Width = 300.0;
		((FrameworkElement)somaAdViewer).Height = 50.0;
		somaAdViewer.AdInterval = 20000;
		somaAdViewer.Adspace = 130020281;
		somaAdViewer.Pub = 1100001940;
		somaAdViewer.AdSpaceHeight = 50;
		somaAdViewer.AdSpaceWidth = 300;
		somaAdViewer.Kws = "instagram video vine twitter facebook picture";
		somaAd = somaAdViewer;
		somaAd.AdError += somaAd_AdError;
		somaAd.NewAdAvailable += ads_NewAdAvailable;
		somaAd.StartAds();
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Add((UIElement)(object)somaAd);
	}

	private void somaAd_AdError(object sender, string ErrorCode, string ErrorDescription)
	{
		DisplayNextAd();
		((UIElement)somaAd).IsHitTestVisible = false;
	}

	private void ads_NewAdAvailable(object sender, EventArgs e)
	{
		if (somaAd != null)
		{
			((UIElement)somaAd).IsHitTestVisible = true;
		}
	}

	private void AdRotator_Unloaded(object sender, RoutedEventArgs e)
	{
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
	}

	private async void RemoveAds_Click(object sender, RoutedEventArgs e)
	{
		if (await AppVersion.BuyAds())
		{
			Messenger.Default.Send(new NotificationMessage(this, "ADREMOVED"));
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
			Application.LoadComponent((object)this, new Uri("/Vine;component/Ads/MiniAdRotator.xaml", UriKind.Relative));
			AdPanel = (Grid)((FrameworkElement)this).FindName("AdPanel");
		}
	}
}
