using System;
using System.Globalization;
using System.Windows.Data;
using Vine.Resources;

namespace Vine.Converters;

public class EncodingInProgressConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		int num = (int)value;
		if (num < 2)
		{
			return AppResources.NbrEncodingInProgress;
		}
		try
		{
			return string.Format(AppResources.NbrEncodingsInProgress, num);
		}
		catch
		{
			return num + " " + AppResources.NbrEncodingInProgress;
		}
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
