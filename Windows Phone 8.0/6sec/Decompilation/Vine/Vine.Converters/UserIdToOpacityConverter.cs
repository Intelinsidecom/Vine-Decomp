using System;
using System.Globalization;
using System.Windows.Data;
using Vine.Datas;

namespace Vine.Converters;

public class UserIdToOpacityConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		if ((string)value == DatasProvider.Instance.CurrentUser.User.Id)
		{
			return 1;
		}
		return 0.5;
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
