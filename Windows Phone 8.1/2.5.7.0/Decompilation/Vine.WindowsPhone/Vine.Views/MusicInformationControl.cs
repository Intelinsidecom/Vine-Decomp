using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using Vine.Framework;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;

namespace Vine.Views;

public sealed class MusicInformationControl : BaseUserControl, IComponentConnector
{
	private string _musicTrack;

	private string _musicArtist;

	private bool _visible;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid MusicInfo;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VisualStateGroup VisualStateGroup;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VisualState Expanded;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VisualState Mini;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private TranslateTransform Translation;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public string MusicTrack
	{
		get
		{
			return _musicTrack;
		}
		set
		{
			SetProperty(ref _musicTrack, value, "MusicTrack");
		}
	}

	public string MusicArtist
	{
		get
		{
			return _musicArtist;
		}
		set
		{
			SetProperty(ref _musicArtist, value, "MusicArtist");
		}
	}

	public MusicInformationControl()
	{
		InitializeComponent();
		VisualStateManager.GoToState((Control)(object)this, "Mini", false);
		EventAggregator.Current.Subscribe(this);
	}

	public void IconTapped()
	{
		if (!_visible)
		{
			_visible = true;
			VisualStateManager.GoToState((Control)(object)this, "Expanded", true);
		}
	}

	public void Hide()
	{
		if (_visible)
		{
			VisualStateManager.GoToState((Control)(object)this, "Mini", true);
			_visible = false;
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/MusicInformationControl.xaml"), (ComponentResourceLocation)0);
			MusicInfo = (Grid)((FrameworkElement)this).FindName("MusicInfo");
			VisualStateGroup = (VisualStateGroup)((FrameworkElement)this).FindName("VisualStateGroup");
			Expanded = (VisualState)((FrameworkElement)this).FindName("Expanded");
			Mini = (VisualState)((FrameworkElement)this).FindName("Mini");
			Translation = (TranslateTransform)((FrameworkElement)this).FindName("Translation");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		_contentLoaded = true;
	}
}
