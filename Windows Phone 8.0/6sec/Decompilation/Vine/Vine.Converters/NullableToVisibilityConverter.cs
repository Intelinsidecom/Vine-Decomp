using System;
using System.Globalization;
using System.Windows;
using System.Windows.Data;

namespace Vine.Converters;

public class NullableToVisibilityConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		if ((string)parameter == "inverse")
		{
			return (object)(Visibility)(value != null);
		}
		return (object)(Visibility)(value == null);
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return null;
	}
}
