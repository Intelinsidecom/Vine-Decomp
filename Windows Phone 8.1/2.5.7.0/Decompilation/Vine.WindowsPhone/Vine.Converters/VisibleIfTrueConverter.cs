using System;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Data;

namespace Vine.Converters;

public class VisibleIfTrueConverter : IValueConverter
{
	public bool InvertValue { get; set; }

	public object Convert(object value, Type targetType, object parameter, string language)
	{
		if (value == null)
		{
			return (object)(Visibility)1;
		}
		bool? flag = (bool?)value;
		if (InvertValue)
		{
			flag = !flag;
		}
		return (object)(Visibility)(flag != true);
	}

	public object ConvertBack(object value, Type targetType, object parameter, string language)
	{
		throw new NotImplementedException();
	}
}
