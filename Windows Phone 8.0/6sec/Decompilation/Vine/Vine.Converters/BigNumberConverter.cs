using System;
using System.Globalization;
using System.Windows.Data;

namespace Vine.Converters;

public class BigNumberConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		int num = (int)value;
		if (num > 1000000)
		{
			double num2 = (double)num / 1000000.0;
			if (num2 > 100.0)
			{
				return (num2 - 0.05).ToString("#.#") + "M";
			}
			return (num2 - 0.005).ToString("#.##") + "M";
		}
		if (num > 1000)
		{
			return ((double)num / 1000.0 - 0.05).ToString("#.#") + "K";
		}
		return num.ToString();
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return null;
	}
}
