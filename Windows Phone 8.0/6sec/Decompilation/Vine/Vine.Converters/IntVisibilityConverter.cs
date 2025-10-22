using System;
using System.Globalization;
using System.Windows;
using System.Windows.Data;

namespace Vine.Converters;

public class IntVisibilityConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		if ((string)parameter == "inverse")
		{
			return (object)(Visibility)(((int)value != 0) ? 1 : 0);
		}
		return (object)(Visibility)((int)value == 0);
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
