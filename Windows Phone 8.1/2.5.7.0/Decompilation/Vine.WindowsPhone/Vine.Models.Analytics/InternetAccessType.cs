using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Vine.Models.Analytics;

[JsonConverter(typeof(StringEnumConverter))]
public enum InternetAccessType
{
	UNREACHABLE,
	MOBILE,
	WIFI,
	HARDLINE
}
