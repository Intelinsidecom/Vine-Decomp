using System;
using System.Globalization;
using System.Windows.Data;
using Vine.Resources;

namespace Vine.Converters;

public class ShortTimerConverter : IValueConverter
{
	public static string ToShortTimer(DateTime dateTime)
	{
		TimeSpan timeSpan = DateTime.Now - dateTime.ToLocalTime();
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

	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return ToShortTimer((DateTime)value);
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return null;
	}
}
