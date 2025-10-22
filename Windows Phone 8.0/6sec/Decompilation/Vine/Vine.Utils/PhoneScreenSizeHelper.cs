using System;
using System.Windows;
using Microsoft.Phone.Info;

namespace Vine.Utils;

public class PhoneScreenSizeHelper
{
	private static double? _size;

	private static double? _minSize;

	public static bool IsPhablet()
	{
		return GetSize() >= 5.5;
	}

	public static double GetMinSize()
	{
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Unknown result type (might be due to invalid IL or missing references)
		if (!_minSize.HasValue)
		{
			object obj = default(object);
			if (DeviceExtendedProperties.TryGetValue("PhysicalScreenResolution", ref obj))
			{
				Size val = (Size)obj;
				_minSize = Math.Min(((Size)(ref val)).Width, ((Size)(ref val)).Height);
				return _minSize.Value;
			}
			_minSize = 0.0;
		}
		return _size.Value;
	}

	public static double GetSize()
	{
		//IL_001e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0023: Unknown result type (might be due to invalid IL or missing references)
		if (!_size.HasValue)
		{
			object obj = default(object);
			if (DeviceExtendedProperties.TryGetValue("PhysicalScreenResolution", ref obj))
			{
				Size val = (Size)obj;
				object obj2 = default(object);
				if (DeviceExtendedProperties.TryGetValue("RawDpiY", ref obj2))
				{
					double num = (double)obj2;
					if (num > 0.0)
					{
						double num2 = Math.Sqrt(Math.Pow(((Size)(ref val)).Width / num, 2.0) + Math.Pow(((Size)(ref val)).Height / num, 2.0));
						_size = num2;
						return num2;
					}
				}
			}
			_size = 0.0;
		}
		return _size.Value;
	}
}
