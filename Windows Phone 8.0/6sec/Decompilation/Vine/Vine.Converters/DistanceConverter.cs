using System;
using System.Globalization;
using System.Windows.Data;
using Vine.Resources;

namespace Vine.Converters;

public class DistanceConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		double num = (double)value;
		if (RegionInfo.CurrentRegion.IsMetric)
		{
			if (num < 1000.0)
			{
				return num - num % 100.0 + AppResources.Meter;
			}
			return (num / 1000.0).ToString("0.#") + AppResources.Kilometer;
		}
		return (num / 1000.0 * 0.621371192).ToString("0.##") + " mi";
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
