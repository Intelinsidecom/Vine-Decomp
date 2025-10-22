using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Vine.Tiles;
using Windows.Storage.Streams;
using Windows.UI;
using Windows.UI.StartScreen;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Imaging;

namespace Vine.Views;

public sealed class ChannelVineListView : BasePage, IComponentConnector
{
	private const int ReventPivotIndex = 1;

	private Brush _white;

	private Brush _fadedWhite;

	private Brush _featuredPivotHeaderBrush;

	private Brush _recentPivotHeaderBrush;

	private string _pageTitle;

	private bool _isBusy;

	private Brush _featuredBrush;

	private Brush _recentBrush;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ChannelTileWide ChannelWideTile;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MusicInformationControl MusicControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Pivot PivotRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VineListControl RecentVineList;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VineListControl FeaturedVineList;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton PinUnPinCommandButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton SettingsButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public Brush FeaturedPivotHeaderBrush
	{
		get
		{
			return _featuredPivotHeaderBrush;
		}
		set
		{
			SetProperty(ref _featuredPivotHeaderBrush, value, "FeaturedPivotHeaderBrush");
		}
	}

	public Brush RecentPivotHeaderBrush
	{
		get
		{
			return _recentPivotHeaderBrush;
		}
		set
		{
			SetProperty(ref _recentPivotHeaderBrush, value, "RecentPivotHeaderBrush");
		}
	}

	public Brush ChannelBrush { get; set; }

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

	public Brush FeaturedBrush
	{
		get
		{
			return _featuredBrush;
		}
		set
		{
			SetProperty(ref _featuredBrush, value, "FeaturedBrush");
			OnPropertyChanged("FeaturedBrush");
		}
	}

	public Brush RecentBrush
	{
		get
		{
			return _recentBrush;
		}
		set
		{
			SetProperty(ref _recentBrush, value, "RecentBrush");
			OnPropertyChanged("RecentBrush");
		}
	}

	public VineListViewParams Model => (VineListViewParams)base.NavigationObject;

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

	private bool HasTile => SecondaryTile.Exists(TileId);

	private string TileId => string.Concat(SecondaryTileType.ChannelResult, "=", PageTitle).Replace(" ", string.Empty);

	public ChannelVineListView()
	{
		//IL_0021: Unknown result type (might be due to invalid IL or missing references)
		//IL_002b: Expected O, but got Unknown
		//IL_0040: Unknown result type (might be due to invalid IL or missing references)
		//IL_004a: Expected O, but got Unknown
		//IL_0050: Unknown result type (might be due to invalid IL or missing references)
		//IL_005a: Expected O, but got Unknown
		//IL_0071: Unknown result type (might be due to invalid IL or missing references)
		//IL_007b: Expected O, but got Unknown
		InitializeComponent();
		FeaturedBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];
		RecentBrush = (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGrayMediumBrush"];
		_white = (Brush)new SolidColorBrush(Colors.White);
		_fadedWhite = (Brush)new SolidColorBrush(Color.FromArgb(127, byte.MaxValue, byte.MaxValue, byte.MaxValue));
		_featuredPivotHeaderBrush = _fadedWhite;
		_recentPivotHeaderBrush = _fadedWhite;
		SettingsButton.put_Label(ResourceHelper.GetString("dialog_options_settings"));
		FeaturedVineList.MusicControl = MusicControl;
		RecentVineList.MusicControl = MusicControl;
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		PageTitle = Model.Title;
		ChannelBrush = GetHexColor(Model.Color);
		UpdateAppBar();
		Model.Type = ((Model.Type != ListType.Search) ? ListType.ChannelFeatured : ListType.SearchFeatured);
		FeaturedVineList.ListParams = Model;
		FeaturedVineList.SecondaryBrush = ChannelBrush;
		if (Model.ChannelShowRecent)
		{
			VineListViewParams vineListViewParams = Serialization.DeepCopy<VineListViewParams>(Model);
			vineListViewParams.Type = ((Model.Type != ListType.SearchFeatured) ? ListType.ChannelRecent : ListType.SearchRecent);
			RecentVineList.ListParams = vineListViewParams;
			RecentVineList.SecondaryBrush = ChannelBrush;
		}
		else if (!Model.ChannelShowRecent && ((ICollection<object>)((ItemsControl)PivotRoot).Items).Count > 1)
		{
			((IList<object>)((ItemsControl)PivotRoot).Items).RemoveAt(1);
		}
		if (e.PageState != null)
		{
			FeaturedVineList.Items.CurrentPage = (int)e.LoadValueOrDefault<long>("FeaturedVineList.Items.CurrentPage");
			FeaturedVineList.PageStateItems = e.LoadValueOrDefault<List<VineModel>>("FeaturedVineList.Items");
			FeaturedVineList.PageStateScrollOffset = e.LoadValueOrDefault<double>("FeaturedVineList.ScrollOffset");
			if (Model.ChannelShowRecent)
			{
				RecentVineList.Items.CurrentPage = (int)e.LoadValueOrDefault<long>("RecentVineList.Items.CurrentPage");
				RecentVineList.PageStateItems = e.LoadValueOrDefault<List<VineModel>>("RecentVineList.Items");
				RecentVineList.PageStateScrollOffset = e.LoadValueOrDefault<double>("RecentVineList.ScrollOffset");
			}
			int num = (int)e.LoadValueOrDefault<long>("PivotRoot.SelectedIndex");
			if (PivotRoot.SelectedIndex != num)
			{
				((UIElement)PivotRoot).put_Opacity(0.0);
				WindowsRuntimeMarshal.RemoveEventHandler<SelectionChangedEventHandler>((Action<EventRegistrationToken>)PivotRoot.remove_SelectionChanged, new SelectionChangedEventHandler(PivotRoot_OnSelectionChanged));
				PivotRoot.put_SelectedIndex(num);
				Pivot pivotRoot = PivotRoot;
				WindowsRuntimeMarshal.AddEventHandler((Func<SelectionChangedEventHandler, EventRegistrationToken>)pivotRoot.add_SelectionChanged, (Action<EventRegistrationToken>)pivotRoot.remove_SelectionChanged, new SelectionChangedEventHandler(PivotRoot_OnSelectionChanged));
				DispatcherEx.BeginInvoke(async delegate
				{
					await Task.Delay(400);
					((UIElement)PivotRoot).put_Opacity(1.0);
				});
			}
		}
		await Activate(PivotRoot.SelectedIndex);
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		DeactiveAllExcept(-1);
		e.PageState["FeaturedVineList.Items.CurrentPage"] = (long)FeaturedVineList.Items.CurrentPage;
		e.PageState["FeaturedVineList.Items"] = FeaturedVineList.Items.Select((VineViewModel x) => x.Model).ToList();
		e.PageState["FeaturedVineList.ScrollOffset"] = FeaturedVineList.ScrollOffset;
		e.PageState["RecentVineList.Items.CurrentPage"] = (long)RecentVineList.Items.CurrentPage;
		e.PageState["RecentVineList.Items"] = RecentVineList.Items.Select((VineViewModel x) => x.Model).ToList();
		e.PageState["RecentVineList.ScrollOffset"] = RecentVineList.ScrollOffset;
		e.PageState["PivotRoot.SelectedIndex"] = (long)PivotRoot.SelectedIndex;
	}

	private async void PivotRoot_OnSelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		if (e.RemovedItems.Count > 0)
		{
			DeactiveAllExcept(PivotRoot.SelectedIndex);
			await Activate(PivotRoot.SelectedIndex);
		}
	}

	private async Task Activate(int index)
	{
		if (index == 0)
		{
			FeaturedPivotHeaderBrush = _white;
			await FeaturedVineList.OnActivate();
		}
		if (index == 1)
		{
			RecentPivotHeaderBrush = _white;
			await RecentVineList.OnActivate();
		}
	}

	private void DeactiveAllExcept(int index)
	{
		if (index != 0)
		{
			FeaturedPivotHeaderBrush = _fadedWhite;
			FeaturedVineList.OnDeactivate();
		}
		if (index != 1 && Model.ChannelShowRecent)
		{
			RecentPivotHeaderBrush = _fadedWhite;
			RecentVineList.OnDeactivate();
		}
	}

	private Brush GetHexColor(string hex)
	{
		//IL_0084: Unknown result type (might be due to invalid IL or missing references)
		//IL_008a: Expected O, but got Unknown
		//IL_006a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0070: Expected O, but got Unknown
		if (hex != null && hex.Length == 6 && byte.TryParse(hex.Substring(0, 2), NumberStyles.AllowHexSpecifier, CultureInfo.InvariantCulture, out var result) && byte.TryParse(hex.Substring(2, 2), NumberStyles.AllowHexSpecifier, CultureInfo.InvariantCulture, out var result2) && byte.TryParse(hex.Substring(4, 2), NumberStyles.AllowHexSpecifier, CultureInfo.InvariantCulture, out var result3))
		{
			return (Brush)new SolidColorBrush(Color.FromArgb(byte.MaxValue, result, result2, result3));
		}
		return (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"];
	}

	private void MuteVolume_Click(object sender, RoutedEventArgs e)
	{
		ApplicationSettings.Current.IsVolumeMuted = !ApplicationSettings.Current.IsVolumeMuted;
		NotifyOfPropertyChange(() => MuteIcon);
		NotifyOfPropertyChange(() => MuteLabel);
		FeaturedVineList.NotifyMuteChange();
		if (Model.ChannelShowRecent)
		{
			RecentVineList.NotifyMuteChange();
		}
	}

	private async void PinUnPinCommandButton_OnClick(object sender, RoutedEventArgs e)
	{
		if (HasTile)
		{
			await TileHelper.DeleteSecondaryTile(TileId);
		}
		else
		{
			Image channelImg = ChannelWideTile.GetImage();
			((Panel)ChannelWideTile.GetChannelBackground()).put_Background((Brush)new SolidColorBrush(Extensions.ColorHex(Model.Color)));
			try
			{
				BitmapImage bmi = new BitmapImage();
				IRandomAccessStreamWithContentType fileStream = await RandomAccessStreamReference.CreateFromUri(new Uri(Model.Icon, UriKind.Absolute)).OpenReadAsync();
				try
				{
					await ((BitmapSource)bmi).SetSourceAsync((IRandomAccessStream)(object)fileStream);
				}
				finally
				{
					((IDisposable)fileStream)?.Dispose();
				}
				channelImg.put_Source((ImageSource)(object)bmi);
			}
			catch (Exception)
			{
			}
			RenderTargetBitmap bitmap = new RenderTargetBitmap();
			await bitmap.RenderAsync((UIElement)(object)ChannelWideTile);
			await TileHelper.CreateChannelTagTile(PageTitle, Model, bitmap, TileId);
		}
		UpdateAppBar();
	}

	private void UpdateAppBar()
	{
		PinUnPinCommandButton.put_Label(HasTile ? ResourceHelper.GetString("Unpin") : ResourceHelper.GetString("Pin"));
	}

	private void Settings_Click(object sender, RoutedEventArgs e)
	{
		((Page)this).Frame.Navigate(typeof(SettingsView));
	}

	private void Image_ImageFailed(object sender, ExceptionRoutedEventArgs e)
	{
		FrameworkElement val = (FrameworkElement)((sender is FrameworkElement) ? sender : null);
		if (val != null)
		{
			((UIElement)val).put_Visibility((Visibility)1);
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/ChannelVineListView.xaml"), (ComponentResourceLocation)0);
			ChannelWideTile = (ChannelTileWide)((FrameworkElement)this).FindName("ChannelWideTile");
			MusicControl = (MusicInformationControl)((FrameworkElement)this).FindName("MusicControl");
			PivotRoot = (Pivot)((FrameworkElement)this).FindName("PivotRoot");
			RecentVineList = (VineListControl)((FrameworkElement)this).FindName("RecentVineList");
			FeaturedVineList = (VineListControl)((FrameworkElement)this).FindName("FeaturedVineList");
			PinUnPinCommandButton = (AppBarButton)((FrameworkElement)this).FindName("PinUnPinCommandButton");
			SettingsButton = (AppBarButton)((FrameworkElement)this).FindName("SettingsButton");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0022: Unknown result type (might be due to invalid IL or missing references)
		//IL_0028: Expected O, but got Unknown
		//IL_0049: Unknown result type (might be due to invalid IL or missing references)
		//IL_0053: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_005f: Expected O, but got Unknown
		//IL_0080: Unknown result type (might be due to invalid IL or missing references)
		//IL_008a: Expected O, but got Unknown
		//IL_0090: Unknown result type (might be due to invalid IL or missing references)
		//IL_0096: Expected O, but got Unknown
		//IL_00b7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c1: Expected O, but got Unknown
		//IL_00c4: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ca: Expected O, but got Unknown
		//IL_00eb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f5: Expected O, but got Unknown
		//IL_00f8: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fe: Expected O, but got Unknown
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0129: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			Pivot val3 = (Pivot)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<SelectionChangedEventHandler, EventRegistrationToken>)val3.add_SelectionChanged, (Action<EventRegistrationToken>)val3.remove_SelectionChanged, new SelectionChangedEventHandler(PivotRoot_OnSelectionChanged));
			break;
		}
		case 2:
		{
			Image val2 = (Image)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<ExceptionRoutedEventHandler, EventRegistrationToken>)val2.add_ImageFailed, (Action<EventRegistrationToken>)val2.remove_ImageFailed, new ExceptionRoutedEventHandler(Image_ImageFailed));
			break;
		}
		case 3:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(MuteVolume_Click));
			break;
		}
		case 4:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(PinUnPinCommandButton_OnClick));
			break;
		}
		case 5:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Settings_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
