using System;
using System.Globalization;
using System.Windows.Data;
using Vine.Resources;

namespace Vine.Pages.Search;

public class NbrPostConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		long num = (long)value;
		return num + " " + ((num > 1) ? AppResources.ManyPosts : AppResources.OnePost);
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return null;
	}
}
