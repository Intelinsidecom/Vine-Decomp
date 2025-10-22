using System;
using System.Globalization;
using System.Text.RegularExpressions;
using System.Windows.Data;

namespace Vine.Converters;

public class TrimConverter : IValueConverter
{
	private static Regex _reg;

	static TrimConverter()
	{
		_reg = new Regex("\\s\\s+", RegexOptions.Compiled | RegexOptions.Singleline);
	}

	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		if (value == null)
		{
			return "";
		}
		return _reg.Replace(((string)value).Trim(), " ");
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
