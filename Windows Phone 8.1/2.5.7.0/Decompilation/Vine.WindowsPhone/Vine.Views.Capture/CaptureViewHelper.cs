using System;
using Vine.Framework;

namespace Vine.Views.Capture;

public static class CaptureViewHelper
{
	public static Type GetCaptureView()
	{
		if (!OSVersionHelper.IsWindows10)
		{
			return typeof(CaptureView8);
		}
		return typeof(CaptureView10);
	}
}
