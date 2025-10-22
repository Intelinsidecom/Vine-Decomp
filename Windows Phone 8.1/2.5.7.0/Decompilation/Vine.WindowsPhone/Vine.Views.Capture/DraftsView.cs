using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Windows.Foundation;
using Windows.Phone.UI.Input;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Markup;

namespace Vine.Views.Capture;

public sealed class DraftsView : BasePage, IComponentConnector
{
	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid LayoutRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private FlipView FlipView;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public ObservableCollection<RecordingVineModel> Items { get; set; }

	public string DraftId { get; set; }

	public DraftsView()
	{
		InitializeComponent();
		Items = new ObservableCollection<RecordingVineModel>();
	}

	protected unsafe override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		App.ScribeService.Log(new ViewImpressionEvent(Section.Capture, "drafts"));
		if (!Items.Any())
		{
			List<RecordingVineModel> newItemsSource = await ApplicationSettings.Current.GetRecordingDrafts();
			Items.Repopulate(newItemsSource);
		}
		DraftId = (string)e.NavigationParameter;
		WindowsRuntimeMarshal.RemoveEventHandler<EventHandler<BackPressedEventArgs>>(new Action<EventRegistrationToken>(null, (nint)(delegate*<EventRegistrationToken, void>)(&HardwareButtons.remove_BackPressed)), HardwareButtons_BackPressed);
		WindowsRuntimeMarshal.AddEventHandler(new Func<EventHandler<BackPressedEventArgs>, EventRegistrationToken>(null, (nint)(delegate*<EventHandler<BackPressedEventArgs>, EventRegistrationToken>)(&HardwareButtons.add_BackPressed)), new Action<EventRegistrationToken>(null, (nint)(delegate*<EventRegistrationToken, void>)(&HardwareButtons.remove_BackPressed)), HardwareButtons_BackPressed);
	}

	protected unsafe override void SaveState(object sender, SaveStateEventArgs e)
	{
		base.SaveState(sender, e);
		WindowsRuntimeMarshal.RemoveEventHandler<EventHandler<BackPressedEventArgs>>(new Action<EventRegistrationToken>(null, (nint)(delegate*<EventRegistrationToken, void>)(&HardwareButtons.remove_BackPressed)), HardwareButtons_BackPressed);
	}

	private void HardwareButtons_BackPressed(object sender, BackPressedEventArgs e)
	{
		e.put_Handled(!string.IsNullOrEmpty(DraftId) && Items.All((RecordingVineModel x) => x.DraftId != DraftId));
		if (e.Handled)
		{
			((Page)this).Frame.GoBackTo(CaptureViewHelper.GetCaptureView());
		}
	}

	private void Selector_OnSelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		_ = ((Selector)FlipView).SelectedIndex;
		_ = 0;
	}

	private async void UIElement_OnTapped(object sender, TappedRoutedEventArgs e)
	{
		RecordingVineModel obj = (RecordingVineModel)((FrameworkElement)sender).DataContext;
		obj = Serialization.DeepCopy<RecordingVineModel>(obj);
		await ApplicationSettings.Current.SetRecordingActiveVine(obj);
		((Page)this).Frame.GoBackTo(CaptureViewHelper.GetCaptureView());
	}

	private async void Trash_Click(object sender, RoutedEventArgs e)
	{
		RecordingVineModel vine = (RecordingVineModel)((Selector)FlipView).SelectedItem;
		int currentIndex = ((Selector)FlipView).SelectedIndex;
		if (vine != null)
		{
			Items.Remove(vine);
			await vine.DeleteAsync();
			if (vine.DraftId == DraftId)
			{
				await ApplicationSettings.Current.SetRecordingActiveVine(null);
			}
			if (!Items.Any())
			{
				App.RootFrame.GoBackTo(CaptureViewHelper.GetCaptureView());
			}
			else if (currentIndex > 0)
			{
				((Selector)FlipView).put_SelectedIndex(currentIndex - 1);
			}
			else
			{
				((Selector)FlipView).put_SelectedIndex(0);
			}
		}
	}

	private void FlipView_OnDataContextChanged(FrameworkElement sender, DataContextChangedEventArgs args)
	{
		if (string.IsNullOrEmpty(DraftId))
		{
			((Selector)FlipView).put_SelectedItem((object)Items.LastOrDefault());
			return;
		}
		RecordingVineModel recordingVineModel = Items.FirstOrDefault((RecordingVineModel x) => x.DraftId == DraftId);
		((Selector)FlipView).put_SelectedItem((object)recordingVineModel);
	}

	private void Cancel_Click(object sender, RoutedEventArgs e)
	{
		if (!string.IsNullOrEmpty(DraftId) && Items.All((RecordingVineModel x) => x.DraftId != DraftId))
		{
			((Page)this).Frame.GoBackTo(CaptureViewHelper.GetCaptureView());
		}
		else
		{
			((Page)this).Frame.GoBack();
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/Capture/DraftsView.xaml"), (ComponentResourceLocation)0);
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			FlipView = (FlipView)((FrameworkElement)this).FindName("FlipView");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_001e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0024: Expected O, but got Unknown
		//IL_0050: Unknown result type (might be due to invalid IL or missing references)
		//IL_0056: Expected O, but got Unknown
		//IL_0077: Unknown result type (might be due to invalid IL or missing references)
		//IL_0081: Expected O, but got Unknown
		//IL_0087: Unknown result type (might be due to invalid IL or missing references)
		//IL_008d: Expected O, but got Unknown
		//IL_00ae: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b8: Expected O, but got Unknown
		//IL_00bb: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c1: Expected O, but got Unknown
		//IL_00e2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ec: Expected O, but got Unknown
		//IL_00ef: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f5: Expected O, but got Unknown
		//IL_0116: Unknown result type (might be due to invalid IL or missing references)
		//IL_0120: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			FrameworkElement val3 = (FrameworkElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TypedEventHandler<FrameworkElement, DataContextChangedEventArgs>, EventRegistrationToken>)val3.add_DataContextChanged, (Action<EventRegistrationToken>)val3.remove_DataContextChanged, (TypedEventHandler<FrameworkElement, DataContextChangedEventArgs>)FlipView_OnDataContextChanged);
			Selector val4 = (Selector)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<SelectionChangedEventHandler, EventRegistrationToken>)val4.add_SelectionChanged, (Action<EventRegistrationToken>)val4.remove_SelectionChanged, new SelectionChangedEventHandler(Selector_OnSelectionChanged));
			break;
		}
		case 2:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Trash_Click));
			break;
		}
		case 3:
		{
			UIElement val2 = (UIElement)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)val2.add_Tapped, (Action<EventRegistrationToken>)val2.remove_Tapped, new TappedEventHandler(UIElement_OnTapped));
			break;
		}
		case 4:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Cancel_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
