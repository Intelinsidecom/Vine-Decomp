using System;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Data;

namespace Vine.Converters;

public class NoneToVisibilityConverter : IValueConverter
{
	public bool InvertValue { get; set; }

	public object Convert(object value, Type targetType, object parameter, string language)
	{
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0022: Invalid comparison between Unknown and I4
		if (value is bool && !(bool)value)
		{
			value = null;
		}
		if (value is Visibility && (int)(Visibility)value == 1)
		{
			value = null;
		}
		if (value is string && (string)value == string.Empty)
		{
			value = null;
		}
		if (value != null && (value.ToString() == "0.0" || value.ToString() == "0"))
		{
			value = null;
		}
		if (InvertValue)
		{
			return (object)(Visibility)(value != null);
		}
		return (object)(Visibility)(value == null);
	}

	public object ConvertBack(object value, Type targetType, object parameter, string language)
	{
		throw new NotImplementedException();
	}
}
