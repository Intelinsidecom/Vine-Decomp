using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Windows.Foundation;
using Windows.Media.Capture;
using Windows.Media.Devices;
using Windows.Media.MediaProperties;

namespace Vine.Camera;

public static class MediaCaptureHelper
{
	public static async Task StartPreviewAsync(this MediaCapture mediaCapture, double desiredPreviewArea)
	{
		List<string> supportedVideoFormats = new List<string> { "nv12", "rgb32" };
		VideoEncodingProperties val = (from p in mediaCapture.VideoDeviceController.GetAvailableMediaStreamProperties((MediaStreamType)0).OfType<VideoEncodingProperties>()
			where p != null && !string.IsNullOrEmpty(p.Subtype) && supportedVideoFormats.Contains(p.Subtype.ToLower())
			orderby Math.Abs((double)(p.Height * p.Width) - desiredPreviewArea)
			select p).ToList().FirstOrDefault();
		await mediaCapture.VideoDeviceController.SetMediaStreamPropertiesAsync((MediaStreamType)0, (IMediaEncodingProperties)(object)val);
		await mediaCapture.StartPreviewAsync();
	}

	public static bool IsTorchSupported(this MediaCapture mediaCapture)
	{
		return mediaCapture.VideoDeviceController.TorchControl.Supported;
	}

	public static void TorchState(this MediaCapture mediaCapture, bool turnOn)
	{
		if (!mediaCapture.IsTorchSupported())
		{
			throw new InvalidOperationException("torch not supported by current device.");
		}
		mediaCapture.VideoDeviceController.TorchControl.put_Enabled(turnOn);
		if (turnOn && mediaCapture.VideoDeviceController.TorchControl.PowerSupported)
		{
			mediaCapture.VideoDeviceController.TorchControl.put_PowerPercent(100f);
		}
	}

	public static bool IsFocusSupported(this MediaCapture mediaCapture)
	{
		if (mediaCapture.VideoDeviceController.FocusControl.Supported)
		{
			RegionsOfInterestControl val = null;
			try
			{
				val = mediaCapture.VideoDeviceController.RegionsOfInterestControl;
			}
			catch
			{
			}
			if (val != null)
			{
				if (val.AutoFocusSupported)
				{
					return val.MaxRegions != 0;
				}
				return false;
			}
		}
		return false;
	}

	public static async Task<IAsyncAction> BeginFocusAtPoint(this MediaCapture mediaCapture, Point focusPoint)
	{
		if (!mediaCapture.IsFocusSupported())
		{
			throw new InvalidOperationException("Focus at point is not supported by the current device.");
		}
		if (focusPoint.X < 0.0 || focusPoint.X > 1.0)
		{
			throw new ArgumentOutOfRangeException("focusPoint", focusPoint, "Horizontal location in the viewfinder should be between 0.0 and 1.0.");
		}
		if (focusPoint.Y < 0.0 || focusPoint.Y > 1.0)
		{
			throw new ArgumentOutOfRangeException("focusPoint", focusPoint, "Vertical location in the viewfinder should be between 0.0 and 1.0.");
		}
		double num = focusPoint.X;
		double num2 = focusPoint.Y;
		double num3 = 0.01;
		if (num >= 1.0 - num3)
		{
			num = 1.0 - 2.0 * num3;
		}
		if (num2 >= 0.99)
		{
			num2 = 1.0 - 2.0 * num3;
		}
		mediaCapture.ConfigureAutoFocus(continuous: false);
		RegionsOfInterestControl regionsOfInterestControl = mediaCapture.VideoDeviceController.RegionsOfInterestControl;
		RegionOfInterest[] array = new RegionOfInterest[1];
		RegionOfInterest val = new RegionOfInterest();
		val.put_Type((RegionOfInterestType)0);
		val.put_Bounds(new Rect(num, num2, num3, num3));
		val.put_BoundsNormalized(true);
		val.put_AutoFocusEnabled(true);
		val.put_Weight(1u);
		array[0] = val;
		await regionsOfInterestControl.SetRegionsAsync((IEnumerable<RegionOfInterest>)(object)array);
		return mediaCapture.VideoDeviceController.FocusControl.FocusAsync();
	}

	public static void ConfigureAutoFocus(this MediaCapture mediaCapture, bool continuous)
	{
		//IL_0018: Unknown result type (might be due to invalid IL or missing references)
		//IL_001d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0024: Unknown result type (might be due to invalid IL or missing references)
		//IL_0003: Unknown result type (might be due to invalid IL or missing references)
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		//IL_002c: Expected O, but got Unknown
		FocusSettings val;
		if (!continuous)
		{
			val = new FocusSettings();
			val.put_Mode((FocusMode)1);
			val.put_AutoFocusRange((AutoFocusRange)0);
		}
		else
		{
			val = new FocusSettings();
			val.put_Mode((FocusMode)2);
			val.put_AutoFocusRange((AutoFocusRange)2);
		}
		FocusSettings val2 = val;
		mediaCapture.VideoDeviceController.FocusControl.Configure(val2);
	}
}
