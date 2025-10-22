using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using Vine.Controls.Toast;
using Vine.Services;

namespace Vine;

public class ToastHelper
{
	public static void Show(string text, bool afternav = false, Orientation orient = (Orientation)0)
	{
		//IL_0030: Unknown result type (might be due to invalid IL or missing references)
		//IL_003a: Expected O, but got Unknown
		//IL_005f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0069: Expected O, but got Unknown
		//IL_006a: Unknown result type (might be due to invalid IL or missing references)
		if (!string.IsNullOrEmpty(text) && !(text == "login_required"))
		{
			ToastPrompt toastPrompt = new ToastPrompt();
			((Control)toastPrompt).Background = (Brush)Application.Current.Resources[(object)"PrincBrush"];
			toastPrompt.Title = AppVersion.AppName;
			toastPrompt.Message = text;
			toastPrompt.TextOrientation = (Orientation)0;
			toastPrompt.ImageSource = (ImageSource)new BitmapImage(new Uri("/Assets/ApplicationIcon.png", UriKind.Relative));
			toastPrompt.TextOrientation = orient;
			toastPrompt.Show(afternav);
		}
	}
}
