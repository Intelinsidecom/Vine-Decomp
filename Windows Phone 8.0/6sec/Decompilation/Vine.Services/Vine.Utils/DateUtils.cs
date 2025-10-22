using System;

namespace Vine.Utils;

public static class DateUtils
{
	public static DateTime UnixTimeToDateTime(long unixTime)
	{
		return new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc).AddSeconds(unixTime).ToLocalTime();
	}

	public static DateTime SQliteToDateTime(long unixTime)
	{
		return new DateTime(2001, 1, 1, 0, 0, 0, DateTimeKind.Utc).AddSeconds(unixTime).ToLocalTime();
	}

	public static long ToUnixTime(this DateTime datetime)
	{
		return (long)(datetime.ToUniversalTime() - new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc)).TotalSeconds;
	}

	public static long ToUnixTimeMilli(this DateTime datetime)
	{
		return (long)(datetime.ToUniversalTime() - new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc)).TotalMilliseconds;
	}
}
