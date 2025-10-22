using System;
using System.CodeDom.Compiler;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views.Capture;

public sealed class ChannelSelectView : BasePage, IComponentConnector
{
	private bool _isBusy;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public ObservableCollection<ChannelModel> Items { get; set; }

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

	public ChannelSelectView()
	{
		InitializeComponent();
		Items = new ObservableCollection<ChannelModel>();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		IsBusy = true;
		ApiResult<BaseVineResponseModel<ChannelMetaData>> apiResult = await App.Api.GetChannelList();
		if (!apiResult.HasError)
		{
			Items.Repopulate(apiResult.Model.Data.Records);
			Items.Add(new ChannelModel
			{
				ExploreName = "None"
			});
		}
		IsBusy = false;
	}

	private void Item_Click(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		ChannelModel channelModel = (ChannelModel)((FrameworkElement)sender).DataContext;
		if (channelModel.ChannelId == null)
		{
			ShareCaptureView.ChannelSelectionFromAddChannel = null;
		}
		else
		{
			ShareCaptureView.ChannelSelectionFromAddChannel = channelModel;
		}
		ShareCaptureView.HasChannelSelectionFromAddChannel = true;
		((Page)this).Frame.GoBack();
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/Capture/ChannelSelectView.xaml"), (ComponentResourceLocation)0);
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		//IL_002c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0036: Expected O, but got Unknown
		if (connectionId == 1)
		{
			UIElement val = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val.add_Tapped, (Action<EventRegistrationToken>)val.remove_Tapped, new TappedEventHandler(Item_Click));
		}
		_contentLoaded = true;
	}
}
