using System.Diagnostics;
using System.Threading.Tasks;
using Windows.UI.Xaml.Controls;

namespace Vine.Framework;

public static class ScrollViewerExtensions
{
	public static async Task ScrollToVerticalOffsetSpin(this ScrollViewer scroll, double offset, bool animation = false)
	{
		int i = 0;
		while (scroll.VerticalOffset != offset && i < 15)
		{
			scroll.ChangeView((double?)null, (double?)offset, (float?)null, !animation);
			await Task.Delay(100);
			if (scroll.VerticalOffset == offset)
			{
				break;
			}
			i++;
		}
		if (i == 15)
		{
			Debugger.Break();
		}
	}
}
