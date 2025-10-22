using System.Collections.Generic;
using Windows.Data.Xml.Dom;

namespace Vine.Background.Framework;

internal static class Extensions
{
	public static extern string ToStringInvariantCulture(this long number);

	public static extern string ToStringInvariantCulture(this int number);

	public static extern string ToStringInvariantCulture(this double number);

	public static extern string ToDataString(this IEnumerable<KeyValuePair<string, string>> nameValueCollection);

	public static extern void LoadXml(this XmlDocument xmlDoc, string format, params object[] values);

	public static extern string ToXmlFormattedString(this string xml);
}
