using System;
using System.Windows;
using Microsoft.Phone.Info;
using Microsoft.Phone.Shell;

namespace Vine.Utils;

public class PhoneSizeUtils
{
	public static double ApplicationBarHeightStatic;

	public static double ApplicationBarMiniHeightStatic;

	public static double ApplicationBarOpacityHeightStatic;

	public static double ApplicationBarOpacityMiniHeightStatic;

	public static double KeyboardPortraitdWithSuggestionsHeightStatic;

	public static double KeyboardPortraitHeightStatic;

	public static double KeyboardLandscapeWithSuggestionsHeightStatic;

	public static double KeyboardLandscapeHeightStatic;

	public double ApplicationBarHeight => ApplicationBarHeightStatic;

	public double ApplicationBarMiniHeight => ApplicationBarMiniHeightStatic;

	public double ApplicationBarOpacityHeight => ApplicationBarOpacityHeightStatic;

	public double ApplicationBarOpacityMiniHeight => ApplicationBarOpacityMiniHeightStatic;

	public double KeyboardPortraitdWithSuggestionsHeight => KeyboardPortraitdWithSuggestionsHeightStatic;

	public double KeyboardPortraitHeight => KeyboardPortraitHeightStatic;

	public double KeyboarLandscapeWithSuggestionsHeight => KeyboardLandscapeWithSuggestionsHeightStatic;

	public double KeyboardLandscapeHeight => KeyboardLandscapeHeightStatic;

	static PhoneSizeUtils()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Expected O, but got Unknown
		//IL_0074: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Unknown result type (might be due to invalid IL or missing references)
		ApplicationBar val = new ApplicationBar
		{
			Opacity = 0.0
		};
		ApplicationBarHeightStatic = val.DefaultSize;
		ApplicationBarMiniHeightStatic = val.MiniSize;
		KeyboardPortraitHeightStatic = 339.0;
		KeyboardLandscapeHeightStatic = 403.0;
		try
		{
			object obj = default(object);
			if (new Version(8, 1) <= Environment.OSVersion.Version && DeviceExtendedProperties.TryGetValue("PhysicalScreenResolution", ref obj))
			{
				Size val2 = (Size)obj;
				object obj2 = default(object);
				if (DeviceExtendedProperties.TryGetValue("RawDpiY", ref obj2))
				{
					double num = (double)obj2;
					if (num > 0.0 && Math.Sqrt(Math.Pow(((Size)(ref val2)).Width / num, 2.0) + Math.Pow(((Size)(ref val2)).Height / num, 2.0)) > 5.9)
					{
						ApplicationBarOpacityHeightStatic = 56.0;
						ApplicationBarOpacityMiniHeightStatic = 23.0;
						KeyboardPortraitHeightStatic = 280.0;
						KeyboardLandscapeHeightStatic = 214.0;
						return;
					}
				}
			}
			ApplicationBarOpacityHeightStatic = val.DefaultSize;
			ApplicationBarOpacityMiniHeightStatic = val.MiniSize;
		}
		finally
		{
			KeyboardPortraitdWithSuggestionsHeightStatic = KeyboardPortraitHeightStatic + ApplicationBarOpacityHeightStatic - 3.0;
			KeyboardLandscapeWithSuggestionsHeightStatic = KeyboardLandscapeHeightStatic + ApplicationBarOpacityHeightStatic - 3.0;
		}
	}
}
