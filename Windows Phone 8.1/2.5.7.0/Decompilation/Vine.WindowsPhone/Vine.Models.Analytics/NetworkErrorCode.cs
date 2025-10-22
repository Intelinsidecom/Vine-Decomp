using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Vine.Models.Analytics;

[JsonConverter(typeof(StringEnumConverter))]
public enum NetworkErrorCode
{
	CANCELLED = 1,
	NOT_CONNECTED_TO_INTERNET,
	DNS_ERROR,
	CONNECTION_ERROR,
	TIMED_OUT
}
