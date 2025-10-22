using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using GalaSoft.MvvmLight.Messaging;
using Microsoft.Advertising;
using Microsoft.Advertising.Mobile.UI;
using Microsoft.Phone.Tasks;
using SOMAWP8;
using Vine;
using Vine.Datas;
using Vine.Services;
using Vine.Services.Models;

namespace Huyn.Ads;

public class AdRotator : UserControl
{
	private const string PubCenterApplicationId = "dbc50e45-5886-48cf-9297-846d1b4c1503";

	private const string GoogleAdUnitID = "ca-app-pub-0446979799878347/9253204582";

	private const string AdDuplexAppId = "57235";

	private const int SmaatoId = 130005002;

	private string[] PubCenterIds = new string[1] { "137736" };

	private const string NokiaId = "RudyHuyn_9Gag_WP";

	public string[] AdsOrder = new string[3] { "pubcenter", "smaato", "admob" };

	private Image ImageOwnAd;

	public static bool PubCenterOnly;

	public static int NextPubCenterIndex;

	public int NextTypeAdIndex;

	public static int LastWorkingPubCenter;

	public static Datas _settings;

	private static Random rand;

	private SomaAdViewer somaAd;

	internal Grid AdPanel;

	internal Button button;

	internal Path ClosePath;

	private bool _contentLoaded;

	public CustomAd CurrentCustomAd { get; set; }

	public static void ReInit()
	{
		NextPubCenterIndex = (LastWorkingPubCenter = 0);
	}

	static AdRotator()
	{
		NextPubCenterIndex = 0;
		LastWorkingPubCenter = 0;
		rand = new Random();
		_settings = DatasProvider.Instance;
	}

	public AdRotator()
	{
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0044: Unknown result type (might be due to invalid IL or missing references)
		//IL_004e: Expected O, but got Unknown
		//IL_004f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0054: Unknown result type (might be due to invalid IL or missing references)
		//IL_005e: Expected O, but got Unknown
		//IL_006c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0076: Expected O, but got Unknown
		//IL_007e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0088: Expected O, but got Unknown
		((Control)this).Background = (Brush)new SolidColorBrush(Colors.Black);
		((Control)this).Foreground = (Brush)new SolidColorBrush(Colors.White);
		InitializeComponent();
		((FrameworkElement)this).Unloaded += new RoutedEventHandler(AdRotator_Unloaded);
		((FrameworkElement)this).Loaded += new RoutedEventHandler(AdRotator_Loaded);
	}

	private void AdRotator_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		//IL_0016: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		//IL_0039: Expected O, but got Unknown
		//IL_0039: Unknown result type (might be due to invalid IL or missing references)
		//IL_003f: Expected O, but got Unknown
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Clear();
		ImageOwnAd = new Image
		{
			Height = 80.0,
			Width = 480.0
		};
		BitmapImage val = new BitmapImage();
		List<CustomAd> customAds = DatasProvider.Instance.CustomAds;
		if (customAds != null && customAds.Count > 0)
		{
			try
			{
				CurrentCustomAd = customAds[rand.Next(customAds.Count)];
			}
			catch
			{
			}
		}
		else
		{
			switch (rand.Next(6))
			{
			case 0:
				CurrentCustomAd = new CustomAd
				{
					Image = "/MyAds/fuseads.png",
					Link = "zune://8355da61-1ac5-49cd-a753-7f6afed2bb62"
				};
				break;
			case 1:
				CurrentCustomAd = new CustomAd
				{
					Image = "/MyAds/squaroads.png",
					Link = "zune://bb1a3440-c151-4b75-853c-f4bcf5ab152a"
				};
				break;
			case 2:
				CurrentCustomAd = new CustomAd
				{
					Image = "/MyAds/9gagads.png",
					Link = "zune://91000c5c-9943-43b8-aa65-7609d91057ef"
				};
				break;
			case 3:
				CurrentCustomAd = new CustomAd
				{
					Image = "/MyAds/6tagads.png",
					Link = "zune://7d795cdf-fb1b-4bdf-8f5e-76eb19f7079e"
				};
				break;
			case 4:
				CurrentCustomAd = new CustomAd
				{
					Image = "/MyAds/6napads.png",
					Link = "zune://82fa6341-28dc-4203-bd08-9749b167bc4b"
				};
				break;
			default:
				CurrentCustomAd = new CustomAd
				{
					Image = "/MyAds/tvshowads.png",
					Link = "zune://f593e6f6-cd49-e011-854c-00237de2db9e"
				};
				break;
			}
		}
		if (CurrentCustomAd != null)
		{
			val.UriSource = new Uri(CurrentCustomAd.Image, UriKind.RelativeOrAbsolute);
		}
		ImageOwnAd.Source = (ImageSource)(object)val;
		((UIElement)ImageOwnAd).Tap += BuyMyApp_Tap;
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Add((UIElement)(object)ImageOwnAd);
		LastWorkingPubCenter = (NextPubCenterIndex = 0);
		DisplayNextAd(add: false);
	}

	private void AddPubCenter()
	{
		if (PubCenterIds == null || PubCenterIds.Length == 0)
		{
			DisplayNextAd();
			return;
		}
		((UIElement)ImageOwnAd).Opacity = 0.0;
		AdControl adControl = CreatePubcenterAd();
		RemoveAds();
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)AdPanel).Children).Add((UIElement)(object)adControl);
	}

	private AdControl CreatePubcenterAd()
	{
		if (NextPubCenterIndex >= PubCenterIds.Length)
		{
			NextPubCenterIndex = 0;
		}
		AdControl adControl = new AdControl();
		((FrameworkElement)adControl).Width = 480.0;
		((FrameworkElement)adControl).Height = 80.0;
		adControl.IsAutoCollapseEnabled = true;
		adControl.ApplicationId = "dbc50e45-5886-48cf-9297-846d1b4c1503";
		adControl.IsAutoRefreshEnabled = true;
		((Control)adControl).Background = ((Control)this).Background;
		((Control)adControl).Foreground = ((Control)this).Foreground;
		adControl.AdUnitId = PubCenterIds[NextPubCenterIndex];
		adControl.ErrorOccurred += AdControl_ErrorOccurred;
		NextPubCenterIndex = (NextPubCenterIndex + 1) % PubCenterIds.Length;
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
				((UIElement)ImageOwnAd).Opacity = 1.0;
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
			((UIElement)ImageOwnAd).Opacity = 1.0;
			return;
		}
		try
		{
			switch (AdsOrder[NextTypeAdIndex])
			{
			case "pubcenter":
				AddPubCenter();
				break;
			case "smaato":
			case "smaatohigh":
				AddSmaato();
				break;
			default:
				DisplayNextAd();
				break;
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
		((UIElement)ImageOwnAd).Opacity = 1.0;
		SomaAdViewer somaAdViewer = new SomaAdViewer();
		((FrameworkElement)somaAdViewer).Width = 480.0;
		((FrameworkElement)somaAdViewer).Height = 80.0;
		somaAdViewer.AdInterval = 20000;
		somaAdViewer.Adspace = 130005002;
		somaAdViewer.Pub = 1100001940;
		somaAdViewer.AdSpaceHeight = 50;
		somaAdViewer.AdSpaceWidth = 320;
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
		((UIElement)ImageOwnAd).Opacity = 0.0;
	}

	private void BuyMyApp_Tap(object sender, GestureEventArgs e)
	{
		//IL_0037: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0053: Unknown result type (might be due to invalid IL or missing references)
		//IL_0095: Unknown result type (might be due to invalid IL or missing references)
		//IL_009a: Unknown result type (might be due to invalid IL or missing references)
		if (CurrentCustomAd != null && !string.IsNullOrEmpty(CurrentCustomAd.Link))
		{
			if (CurrentCustomAd.Link.StartsWith("zune://"))
			{
				new MarketplaceDetailTask
				{
					ContentIdentifier = CurrentCustomAd.Link.Substring(7),
					ContentType = (MarketplaceContentType)1
				}.Show();
			}
			else if (CurrentCustomAd.Link.StartsWith("page://"))
			{
				NavigationServiceExt.Navigate(new Uri(CurrentCustomAd.Link.Substring(6), UriKind.Relative));
			}
			else
			{
				new WebBrowserTask
				{
					Uri = new Uri(CurrentCustomAd.Link, UriKind.Absolute)
				}.Show();
			}
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
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Ads/AdRotator.xaml", UriKind.Relative));
			AdPanel = (Grid)((FrameworkElement)this).FindName("AdPanel");
			button = (Button)((FrameworkElement)this).FindName("button");
			ClosePath = (Path)((FrameworkElement)this).FindName("ClosePath");
		}
	}
}
