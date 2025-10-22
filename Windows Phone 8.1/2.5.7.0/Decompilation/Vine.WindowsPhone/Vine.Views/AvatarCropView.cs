using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Windows.ApplicationModel.Activation;
using Windows.Storage;
using Windows.Storage.AccessCache;
using Windows.Storage.Pickers;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Imaging;

namespace Vine.Views;

public sealed class AvatarCropView : BasePage, IComponentConnector
{
	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid LayoutRoot;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VisualState DefaultLayout;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private VisualState Below768Layout;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid Output;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Border brdCapture;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ScrollViewer scrollViewer;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Image imgCrop;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private AppBarButton CropButton;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	private bool IsFinishedLoading { get; set; }

	public AvatarCropView()
	{
		InitializeComponent();
		Image val = imgCrop;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)((FrameworkElement)val).add_LayoutUpdated, (Action<EventRegistrationToken>)((FrameworkElement)val).remove_LayoutUpdated, (EventHandler<object>)imgCrop_LayoutUpdated);
		CropButton.put_Label(ResourceHelper.GetString("button_crop"));
	}

	private void imgCrop_LayoutUpdated(object sender, object e)
	{
		if (((FrameworkElement)imgCrop).ActualWidth > 0.0 && ((FrameworkElement)imgCrop).ActualHeight > 0.0)
		{
			if (((FrameworkElement)imgCrop).ActualHeight > ((FrameworkElement)imgCrop).ActualWidth)
			{
				scrollViewer.ChangeView((double?)(scrollViewer.ScrollableWidth / 2.0), (double?)(scrollViewer.ScrollableHeight / 2.0), (float?)(float)(380.0 / ((FrameworkElement)imgCrop).ActualHeight), true);
			}
			else
			{
				scrollViewer.ChangeView((double?)(scrollViewer.ScrollableWidth / 2.0), (double?)(scrollViewer.ScrollableHeight / 2.0), (float?)(float)(380.0 / ((FrameworkElement)imgCrop).ActualWidth), true);
			}
			WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)((FrameworkElement)imgCrop).remove_LayoutUpdated, (EventHandler<object>)imgCrop_LayoutUpdated);
		}
	}

	protected override void LoadState(object sender, LoadStateEventArgs e)
	{
		if (!IsFinishedLoading)
		{
			SelectPhotoFromLibrary();
			IsFinishedLoading = true;
		}
	}

	private void SelectPhotoFromLibrary()
	{
		//IL_0020: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		//IL_002c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Unknown result type (might be due to invalid IL or missing references)
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		App.ContinuationEventArgsChanged = (EventHandler<IContinuationActivatedEventArgs>)Delegate.Combine(App.ContinuationEventArgsChanged, new EventHandler<IContinuationActivatedEventArgs>(ContinuationEventArgsChanged));
		FileOpenPicker val = new FileOpenPicker();
		val.put_ViewMode((PickerViewMode)1);
		val.put_SuggestedStartLocation((PickerLocationId)6);
		val.FileTypeFilter.Add(".jpg");
		val.FileTypeFilter.Add(".png");
		val.PickSingleFileAndContinue();
	}

	private async void ContinuationEventArgsChanged(object sender, IContinuationActivatedEventArgs args)
	{
		App.ContinuationEventArgsChanged = (EventHandler<IContinuationActivatedEventArgs>)Delegate.Remove(App.ContinuationEventArgsChanged, new EventHandler<IContinuationActivatedEventArgs>(ContinuationEventArgsChanged));
		FileOpenPickerContinuationEventArgs e = (FileOpenPickerContinuationEventArgs)(object)((args is FileOpenPickerContinuationEventArgs) ? args : null);
		if (e == null || !e.Files.Any())
		{
			NavigationHelper.GoBack();
			return;
		}
		string text = StorageApplicationPermissions.MostRecentlyUsedList.Add((IStorageItem)(object)e.Files[0]);
		StorageFile obj = await StorageApplicationPermissions.MostRecentlyUsedList.GetFileAsync(text);
		BitmapImage image = new BitmapImage();
		BitmapImage val = image;
		await ((BitmapSource)val).SetSourceAsync(await obj.OpenAsync((FileAccessMode)0));
		imgCrop.put_Source((ImageSource)(object)image);
		((UIElement)LayoutRoot).put_Visibility((Visibility)0);
	}

	private async void Crop_Click(object sender, RoutedEventArgs e)
	{
		((Control)CropButton).put_IsEnabled(false);
		string fileName = "avatar.jpg";
		RenderTargetBitmap renderTargetBitmap = new RenderTargetBitmap();
		await renderTargetBitmap.RenderAsync((UIElement)(object)scrollViewer);
		StorageFile file = await ApplicationData.Current.LocalFolder.CreateFileAsync(fileName, (CreationCollisionOption)1);
		await renderTargetBitmap.ToFileAsync(file);
		App.TempNewAvatar = file;
		((Control)CropButton).put_IsEnabled(true);
		NavigationHelper.GoBack();
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
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/AvatarCropView.xaml"), (ComponentResourceLocation)0);
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			DefaultLayout = (VisualState)((FrameworkElement)this).FindName("DefaultLayout");
			Below768Layout = (VisualState)((FrameworkElement)this).FindName("Below768Layout");
			Output = (Grid)((FrameworkElement)this).FindName("Output");
			brdCapture = (Border)((FrameworkElement)this).FindName("brdCapture");
			scrollViewer = (ScrollViewer)((FrameworkElement)this).FindName("scrollViewer");
			imgCrop = (Image)((FrameworkElement)this).FindName("imgCrop");
			CropButton = (AppBarButton)((FrameworkElement)this).FindName("CropButton");
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
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(Crop_Click));
		}
		_contentLoaded = true;
	}
}
