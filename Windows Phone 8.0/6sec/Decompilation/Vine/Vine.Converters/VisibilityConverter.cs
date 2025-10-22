using System;
using System.Globalization;
using System.Windows;
using System.Windows.Data;

namespace Vine.Converters;

public class VisibilityConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		if ((string)parameter == "inverse")
		{
			if (!(bool)value)
			{
				return (object)(Visibility)0;
			}
			return (object)(Visibility)1;
		}
		if ((bool)value)
		{
			return (object)(Visibility)0;
		}
		return (object)(Visibility)1;
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
