using System;
using System.Globalization;
using System.Windows.Data;

namespace Vine.Converters;

public class EnumToBoolConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		if (value == null || parameter == null || !(value is Enum))
		{
			return false;
		}
		string text = value.ToString();
		string text2 = parameter.ToString();
		bool flag = false;
		string[] array = text2.Split('|');
		for (int i = 0; i < array.Length; i++)
		{
			string text3 = array[i].Trim();
			if (text3.StartsWith("!"))
			{
				text3 = text3.Substring(1);
				flag = text != text3;
			}
			else
			{
				flag = text == text3;
			}
			if (flag)
			{
				break;
			}
		}
		return flag;
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
