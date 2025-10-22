using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class SingleVineView : BasePage, IComponentConnector
{
	private SingleVineViewParams _params;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VineListControl List;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MusicInformationControl MusicControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton SettingsButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public SingleVineViewParams Params
	{
		get
		{
			if (_params == null)
			{
				_params = base.NavigationObject as SingleVineViewParams;
				if (_params == null)
				{
					string text = base.NavigationParam as string;
					if (!string.IsNullOrWhiteSpace(text) && text.StartsWith("{"))
					{
						_params = Serialization.Deserialize<SingleVineViewParams>(text);
					}
				}
			}
			return _params;
		}
	}

	public Section Section { get; set; }

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

	public SingleVineView()
	{
		InitializeComponent();
		SettingsButton.put_Label(ResourceHelper.GetString("dialog_options_settings"));
		List.MusicControl = MusicControl;
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		List.PostId = Params.PostId;
		Section = Params.Section;
		if (e.PageState != null)
		{
			List.PageStateItems = e.LoadValueOrDefault<List<VineModel>>("List.Items");
			List.PageStateScrollOffset = e.LoadValueOrDefault<double>("List.ScrollOffset");
		}
		await List.OnActivate();
	}

	protected override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		e.PageState["List.Items"] = List.Items.Select((VineViewModel x) => x.Model).ToList();
		e.PageState["List.ScrollOffset"] = List.ScrollOffset;
	}

	private void MuteVolume_Click(object sender, RoutedEventArgs e)
	{
		ApplicationSettings.Current.IsVolumeMuted = !ApplicationSettings.Current.IsVolumeMuted;
		NotifyOfPropertyChange(() => MuteIcon);
		NotifyOfPropertyChange(() => MuteLabel);
		List.NotifyMuteChange();
	}

	private void Settings_Click(object sender, RoutedEventArgs e)
	{
		((Page)this).Frame.Navigate(typeof(SettingsView));
	}

	private void List_OnLoaded(object sender, RoutedEventArgs e)
	{
		List.Section = Section;
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/SingleVineView.xaml"), (ComponentResourceLocation)0);
			List = (VineListControl)((FrameworkElement)this).FindName("List");
			MusicControl = (MusicInformationControl)((FrameworkElement)this).FindName("MusicControl");
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
			FrameworkElement val2 = (FrameworkElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Loaded, (Action<EventRegistrationToken>)val2.remove_Loaded, new RoutedEventHandler(List_OnLoaded));
			break;
		}
		case 2:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(MuteVolume_Click));
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
