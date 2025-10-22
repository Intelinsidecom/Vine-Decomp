using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class UploadJobsView : BasePage, IComponentConnector
{
	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ListView ListControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public UploadJobsViewModel ViewModel => UploadJobsViewModel.Current;

	public UploadJobsView()
	{
		InitializeComponent();
		((FrameworkElement)this).put_DataContext((object)ViewModel);
	}

	private void UIElement_OnHolding(object sender, HoldingRoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Expected O, but got Unknown
		FrameworkElement val = (FrameworkElement)sender;
		FlyoutBase.GetAttachedFlyout(val).ShowAt(val);
	}

	private void Delete_OnClick(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		UploadJob job = (UploadJob)((FrameworkElement)sender).DataContext;
		ViewModel.Delete(job);
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/UploadJobsView.xaml"), (ComponentResourceLocation)0);
			ListControl = (ListView)((FrameworkElement)this).FindName("ListControl");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Expected O, but got Unknown
		//IL_0066: Unknown result type (might be due to invalid IL or missing references)
		//IL_0070: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<HoldingEventHandler, EventRegistrationToken>)val2.add_Holding, (Action<EventRegistrationToken>)val2.remove_Holding, new HoldingEventHandler(UIElement_OnHolding));
			break;
		}
		case 2:
		{
			MenuFlyoutItem val = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Delete_OnClick));
			break;
		}
		}
		_contentLoaded = true;
	}
}
