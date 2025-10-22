using System;
using System.Globalization;
using System.Windows.Data;
using Vine.Resources;

namespace Vine.Converters;

public class DateStringToTimePassedConverter : IValueConverter
{
	private static CultureInfo culture = new CultureInfo("en-US");

	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return ConvertDate((DateTime)value);
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}

	internal static string ConvertDate(string value)
	{
		if (DateTime.TryParse(value, culture, DateTimeStyles.AssumeUniversal, out var result))
		{
			return ConvertDate(result);
		}
		return "";
	}

	internal static string ConvertDate(DateTime dateTime)
	{
		try
		{
			TimeSpan timeSpan = DateTime.Now - dateTime.ToLocalTime();
			if (timeSpan.TotalDays >= 1.0)
			{
				int num = (int)timeSpan.TotalDays;
				if (num < 0)
				{
					num = 0;
				}
				if (num == 0 || num == 1)
				{
					return string.Format(AppResources.TimestampDayAgo, 1);
				}
				return string.Format(AppResources.TimestampDaysAgo, (int)timeSpan.TotalDays);
			}
			if (timeSpan.TotalHours >= 1.0)
			{
				int num2 = (int)timeSpan.TotalHours;
				if (num2 < 0)
				{
					num2 = 0;
				}
				if (num2 == 0 || num2 == 1)
				{
					return string.Format(AppResources.TimestampHourAgo, num2);
				}
				return string.Format(AppResources.TimestampHoursAgo, num2);
			}
			int num3 = (int)timeSpan.TotalMinutes;
			if (num3 < 0)
			{
				num3 = 0;
			}
			return num3 switch
			{
				0 => AppResources.JustAMomentAgo, 
				1 => string.Format(AppResources.TimestampMinuteAgo, 1), 
				_ => string.Format(AppResources.TimestampMinutesAgo, num3), 
			};
		}
		catch
		{
		}
		return "";
	}
}
