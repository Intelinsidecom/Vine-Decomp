using System;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Media.Imaging;

namespace Vine.Models;

public class MosaicImageViewModel : NotifyObject
{
	private bool _isLoaded;

	private bool _isFailed;

	public VineModel Model { get; set; }

	public BitmapImage BitmapImage { get; private set; }

	public bool IsLoaded
	{
		get
		{
			return _isLoaded;
		}
		set
		{
			SetProperty(ref _isLoaded, value, "IsLoaded");
		}
	}

	public bool IsFailed
	{
		get
		{
			return _isFailed;
		}
		set
		{
			SetProperty(ref _isFailed, value, "IsFailed");
		}
	}

	public MosaicImageViewModel(VineModel model)
	{
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0018: Expected O, but got Unknown
		//IL_007c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0086: Expected O, but got Unknown
		//IL_00ae: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b8: Expected O, but got Unknown
		Model = model;
		BitmapImage = new BitmapImage();
		BitmapImage.put_UriSource((Uri)null);
		if (Model != null && Model.ThumbnailUrl != null && Uri.TryCreate(Model.ThumbnailUrl, UriKind.Absolute, out var result))
		{
			BitmapImage bitmapImage = BitmapImage;
			WindowsRuntimeMarshal.AddEventHandler((Func<ExceptionRoutedEventHandler, EventRegistrationToken>)bitmapImage.add_ImageFailed, (Action<EventRegistrationToken>)bitmapImage.remove_ImageFailed, new ExceptionRoutedEventHandler(OnImageFailed));
			bitmapImage = BitmapImage;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)bitmapImage.add_ImageOpened, (Action<EventRegistrationToken>)bitmapImage.remove_ImageOpened, new RoutedEventHandler(OnImageOpened));
			BitmapImage.put_UriSource(result);
		}
	}

	private void OnImageOpened(object sender, RoutedEventArgs routedEventArgs)
	{
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_0023: Expected O, but got Unknown
		//IL_003c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0046: Expected O, but got Unknown
		WindowsRuntimeMarshal.RemoveEventHandler<ExceptionRoutedEventHandler>((Action<EventRegistrationToken>)BitmapImage.remove_ImageFailed, new ExceptionRoutedEventHandler(OnImageFailed));
		WindowsRuntimeMarshal.RemoveEventHandler<RoutedEventHandler>((Action<EventRegistrationToken>)BitmapImage.remove_ImageOpened, new RoutedEventHandler(OnImageOpened));
		IsLoaded = true;
	}

	private void OnImageFailed(object sender, ExceptionRoutedEventArgs exceptionRoutedEventArgs)
	{
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_0023: Expected O, but got Unknown
		//IL_003c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0046: Expected O, but got Unknown
		WindowsRuntimeMarshal.RemoveEventHandler<ExceptionRoutedEventHandler>((Action<EventRegistrationToken>)BitmapImage.remove_ImageFailed, new ExceptionRoutedEventHandler(OnImageFailed));
		WindowsRuntimeMarshal.RemoveEventHandler<RoutedEventHandler>((Action<EventRegistrationToken>)BitmapImage.remove_ImageOpened, new RoutedEventHandler(OnImageOpened));
		IsFailed = true;
	}
}
