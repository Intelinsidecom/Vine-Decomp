using System;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using GrayscaleTransform_Windows;
using Windows.Media.Capture;

namespace Vine.Views.Capture;

public static class VineCaptureExt
{
	public static Task PauseAsync(this VineCapture vine)
	{
		TaskCompletionSource<object> tcs = new TaskCompletionSource<object>();
		EventHandler<FrameInfo> handler = null;
		handler = delegate
		{
			WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)vine.remove_LastCapturedFrame, handler);
			tcs.SetResult(null);
		};
		VineCapture val = vine;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<FrameInfo>, EventRegistrationToken>)val.add_LastCapturedFrame, (Action<EventRegistrationToken>)val.remove_LastCapturedFrame, handler);
		vine.Pause = true;
		return tcs.Task;
	}

	public static int ToInt(this VideoRotation rotation)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0002: Unknown result type (might be due to invalid IL or missing references)
		//IL_0014: Expected I4, but got Unknown
		return (rotation - 1) switch
		{
			0 => 90, 
			1 => 180, 
			2 => 270, 
			_ => 0, 
		};
	}
}
