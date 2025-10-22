using System;
using System.Globalization;
using System.Windows.Data;
using Vine.Resources;

namespace Vine.Converters;

internal class ShortDateStringToTimePassedConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return ConvertDate((DateTime)value);
	}

	internal static string ConvertDate(DateTime dateTime)
	{
		TimeSpan timeSpan = DateTime.Now - dateTime;
		if (timeSpan.TotalDays >= 1.0)
		{
			return (int)timeSpan.TotalDays + AppResources.TimestampDayAbbreviation;
		}
		if (timeSpan.TotalHours >= 1.0)
		{
			return (int)timeSpan.TotalHours + AppResources.TimestampHourAbbreviation;
		}
		return Math.Max(0, (int)timeSpan.TotalMinutes) + AppResources.TimestampMinuteAbbreviation;
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
