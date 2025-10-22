using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Vine.Tiles;
using Windows.UI.StartScreen;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media.Imaging;

namespace Vine.Views;

public sealed class TagVineListView : BasePage, IComponentConnector
{
	private string _pageTitle;

	private bool _isBusy;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private SpecificTagSmallTile SpecificSmallTile;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private GenericSmallTile GenericTileSmall;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MusicInformationControl MusicControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VineListControl List;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton PinUnPinCommandButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton SettingsButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public IconElement MuteIcon
	{
		get
		{
			//IL_001c: Unknown result type (might be due to invalid IL or missing references)
			//IL_0022: Expected O, but got Unknown
			//IL_0011: Unknown result type (might be due to invalid IL or missing references)
			//IL_0017: Expected O, but got Unknown
			if (ApplicationSettings.Current.IsVolumeMuted)
			{
				return (IconElement)new SymbolIcon((Symbol)57693);
			}
			return (IconElement)new SymbolIcon((Symbol)57752);
		}
	}

	public string MuteLabel
	{
		get
		{
			if (!ApplicationSettings.Current.IsVolumeMuted)
			{
				return ResourceHelper.GetString("Mute");
			}
			return ResourceHelper.GetString("Unmute");
		}
	}

	public string PageTitle
	{
		get
		{
			return _pageTitle;
		}
		set
		{
			SetProperty(ref _pageTitle, value, "PageTitle");
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

	public string SearchTerm { get; set; }

	public bool CanPin { get; set; }

	private bool TileExists => SecondaryTile.Exists(TileId);

	private string TileId
	{
		get
		{
			if (List.ListParams != null)
			{
				return List.ListParams.Type switch
				{
					ListType.PopularNow => SecondaryTileType.PopularNow.ToString(), 
					ListType.TrendingPeople => SecondaryTileType.OnTheRise.ToString(), 
					_ => string.Concat(SecondaryTileType.TagResult, "=", PageTitle), 
				};
			}
			return string.Concat(SecondaryTileType.TagResult, "=", PageTitle);
		}
	}

	public TagVineListView()
	{
		InitializeComponent();
		SettingsButton.put_Label(ResourceHelper.GetString("dialog_options_settings"));
		List.MusicControl = MusicControl;
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		((UIElement)PinUnPinCommandButton).put_Visibility((Visibility)1);
		VineListViewParams vineListViewParams = NavigationObject as VineListViewParams;
		string text = e.NavigationParameter as string;
		if (vineListViewParams != null)
		{
			PageTitle = vineListViewParams.Title;
			SearchTerm = vineListViewParams.Title;
			List.ListParams = vineListViewParams;
			CanPin = vineListViewParams.Type == ListType.ChannelFeatured || vineListViewParams.Type == ListType.ChannelRecent || vineListViewParams.Type == ListType.PopularNow || vineListViewParams.Type == ListType.Tag || vineListViewParams.Type == ListType.TrendingPeople;
		}
		else if (!string.IsNullOrEmpty(text))
		{
			PageTitle = "#" + text;
			SearchTerm = text;
			List.SearchTag = text;
			CanPin = true;
		}
		if (e.PageState != null)
		{
			List.Items.CurrentPage = (int)e.LoadValueOrDefault<long>("List.Items.CurrentPage");
			List.Items.Anchor = e.LoadValueOrDefault<string>("List.Items.Anchor");
			List.PageStateItems = e.LoadValueOrDefault<List<VineModel>>("List.Items");
			List.PageStateScrollOffset = e.LoadValueOrDefault<double>("List.ScrollOffset");
			CanPin = e.LoadValueOrDefault<bool>("CanPin");
		}
		UpdateAppBar();
		await List.OnActivate();
		if (CanPin)
		{
			((UIElement)PinUnPinCommandButton).put_Visibility((Visibility)0);
		}
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		List.OnDeactivate();
		e.PageState["List.Items.CurrentPage"] = (long)List.Items.CurrentPage;
		e.PageState["List.Items.Anchor"] = List.Items.Anchor;
		e.PageState["List.Items"] = List.Items.Select((VineViewModel x) => x.Model).ToList();
		e.PageState["List.ScrollOffset"] = List.ScrollOffset;
		e.PageState["CanPin"] = CanPin;
	}

	private void MuteVolume_Click(object sender, RoutedEventArgs e)
	{
		ApplicationSettings.Current.IsVolumeMuted = !ApplicationSettings.Current.IsVolumeMuted;
		NotifyOfPropertyChange(() => MuteIcon);
		NotifyOfPropertyChange(() => MuteLabel);
		List.NotifyMuteChange();
	}

	private void UpdateAppBar()
	{
		//IL_0054: Unknown result type (might be due to invalid IL or missing references)
		//IL_005e: Expected O, but got Unknown
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_0032: Expected O, but got Unknown
		if (!TileExists)
		{
			PinUnPinCommandButton.put_Label(ResourceHelper.GetString("Pin"));
			PinUnPinCommandButton.put_Icon((IconElement)new SymbolIcon((Symbol)57665));
		}
		else
		{
			PinUnPinCommandButton.put_Label(ResourceHelper.GetString("Unpin"));
			PinUnPinCommandButton.put_Icon((IconElement)new SymbolIcon((Symbol)57750));
		}
		((UIElement)PinUnPinCommandButton).UpdateLayout();
	}

	private async void PinUnPinCommandButton_OnClick(object sender, RoutedEventArgs e)
	{
		if (TileExists)
		{
			await TileHelper.DeleteSecondaryTile(TileId);
		}
		else if (List.ListParams != null)
		{
			switch (List.ListParams.Type)
			{
			case ListType.PopularNow:
			{
				RenderTargetBitmap bitmap = new RenderTargetBitmap();
				await bitmap.RenderAsync((UIElement)(object)GenericTileSmall);
				await TileHelper.CreatePopularNowTile(List.ListParams, bitmap, TileId);
				break;
			}
			case ListType.TrendingPeople:
			{
				RenderTargetBitmap bitmap = new RenderTargetBitmap();
				await bitmap.RenderAsync((UIElement)(object)GenericTileSmall);
				await TileHelper.CreateOnTheRiseTile(List.ListParams, bitmap, TileId);
				break;
			}
			default:
			{
				RenderTargetBitmap bitmap = new RenderTargetBitmap();
				await bitmap.RenderAsync((UIElement)(object)SpecificSmallTile);
				await TileHelper.CreateSpecificTagTile(List.SearchTag, TileId, bitmap);
				break;
			}
			}
		}
		else
		{
			RenderTargetBitmap bitmap = new RenderTargetBitmap();
			await bitmap.RenderAsync((UIElement)(object)SpecificSmallTile);
			await TileHelper.CreateSpecificTagTile(List.SearchTag, TileId, bitmap);
		}
		UpdateAppBar();
	}

	private void Settings_Click(object sender, RoutedEventArgs e)
	{
		((Page)this).Frame.Navigate(typeof(SettingsView));
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Expected O, but got Unknown
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/TagVineListView.xaml"), (ComponentResourceLocation)0);
			SpecificSmallTile = (SpecificTagSmallTile)((FrameworkElement)this).FindName("SpecificSmallTile");
			GenericTileSmall = (GenericSmallTile)((FrameworkElement)this).FindName("GenericTileSmall");
			MusicControl = (MusicInformationControl)((FrameworkElement)this).FindName("MusicControl");
			List = (VineListControl)((FrameworkElement)this).FindName("List");
			PinUnPinCommandButton = (AppBarButton)((FrameworkElement)this).FindName("PinUnPinCommandButton");
			SettingsButton = (AppBarButton)((FrameworkElement)this).FindName("SettingsButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_001a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Expected O, but got Unknown
		//IL_0041: Unknown result type (might be due to invalid IL or missing references)
		//IL_004b: Expected O, but got Unknown
		//IL_004e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0054: Expected O, but got Unknown
		//IL_0075: Unknown result type (might be due to invalid IL or missing references)
		//IL_007f: Expected O, but got Unknown
		//IL_0082: Unknown result type (might be due to invalid IL or missing references)
		//IL_0088: Expected O, but got Unknown
		//IL_00a9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b3: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(MuteVolume_Click));
			break;
		}
		case 2:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(PinUnPinCommandButton_OnClick));
			break;
		}
		case 3:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Settings_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
