using System;
using System.Globalization;
using System.Windows.Data;
using Vine.Resources;

namespace Vine.Converters;

public class PendingUsersConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		int num = (int)value;
		return num switch
		{
			0 => "", 
			1 => AppResources.OnePendingRequest, 
			_ => string.Format(AppResources.PendingRequests, num), 
		};
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return null;
	}
}
