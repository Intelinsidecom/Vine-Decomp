using System;

namespace Vine.Framework;

public class TimeStampHelper
{
	private static readonly DateTime UnixEpoch = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);

	public static double GetSecondsSinceUnixEpoch()
	{
		return (DateTime.UtcNow - UnixEpoch).TotalSeconds;
	}
}
