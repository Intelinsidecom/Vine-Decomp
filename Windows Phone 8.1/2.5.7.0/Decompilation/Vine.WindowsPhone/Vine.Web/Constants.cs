using System.Text;

namespace Vine.Web;

public static class Constants
{
	public const string AppInsightsKey = "cf23c5ab-0c73-469b-911d-c6dd1578a53a";

	public const string HockeyAppKey = "cf23c5ab0c73469b911dc6dd1578a53a";

	public const sbyte API_MAGIC = 21;

	private static readonly sbyte[] RAW_API_KEY = new sbyte[22]
	{
		-57, -90, -62, -90, -28, -62, -50, -81, -58, -79,
		-77, -89, -64, -50, -76, -64, -57, -80, -35, -92,
		-48, -82
	};

	private static readonly sbyte[] RAW_API_SECRET = new sbyte[40]
	{
		-83, -45, -28, -28, -96, -58, -49, -49, -95, -92,
		-79, -82, -76, -56, -66, -65, -30, -59, -64, -44,
		-27, -87, -35, -84, -59, -87, -36, -81, -57, -45,
		-27, -53, -88, -83, -99, -64, -90, -63, -45, -35
	};

	public static readonly sbyte[] FB_RAW_ACCESS_KEY = new sbyte[16]
	{
		-28, -28, -35, -32, -31, -34, -32, -36, -29, -28,
		-32, -36, -29, -28, -34, -27
	};

	public static readonly string FB_API_KEY = Transform(21, FB_RAW_ACCESS_KEY);

	public static readonly string VINE_API_KEY = Transform(21, RAW_API_KEY);

	public static readonly string VINE_API_SECRET = Transform(21, RAW_API_SECRET);

	public static string DefaultAvatarUrl = "https://v.cdn.vine.co/avatars/default.png";

	public static string Transform(sbyte magic, sbyte[] m)
	{
		StringBuilder stringBuilder = new StringBuilder(m.Length);
		foreach (sbyte b in m)
		{
			stringBuilder.Append((char)(magic - b));
		}
		return stringBuilder.ToString();
	}
}
