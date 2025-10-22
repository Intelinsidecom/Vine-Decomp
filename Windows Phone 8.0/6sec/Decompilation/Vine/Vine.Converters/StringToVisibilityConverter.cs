using System;
using System.Globalization;
using System.Windows;
using System.Windows.Data;

namespace Vine.Converters;

public class StringToVisibilityConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		if ((string)parameter == "inverse")
		{
			return (object)(Visibility)(!string.IsNullOrEmpty((string)value));
		}
		return (object)(Visibility)(string.IsNullOrEmpty((string)value) ? 1 : 0);
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
